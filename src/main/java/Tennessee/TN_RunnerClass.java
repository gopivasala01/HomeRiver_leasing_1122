package Tennessee;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.text.DecimalFormat;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.WebDriverWait;

import io.github.bonigarcia.wdm.WebDriverManager;
import mainPackage.InsertDataIntoDatabase;
import mainPackage.RunnerClass;

public class TN_RunnerClass 
{

public static ChromeDriver FL_driver;
public static Actions FL_actions;
public static JavascriptExecutor FL_js;
public static File FL_file;
public static FileInputStream FL_fis;
public static StringBuilder FL_stringBuilder = new StringBuilder() ;
public static WebDriverWait FL_wait;
public static FileOutputStream FL_fos;
public static String pdfFormatType;
public static PDDocument document;
public static String chargeCodesTable ="automation.chargeCodesConfiguration_TN";

	public boolean runAutomation(String portfolio, String leaseName, String leaseOwnername)  throws Exception
	{
		
		//TN_RunnerClass TN_RunnerClass = new TN_RunnerClass();
		TN_RunnerClass.openBrowser();
		//Login to Propertyware
		TN_PropertyWare downloadLeaseAgreement =new  TN_PropertyWare();
		downloadLeaseAgreement.login();
		
		boolean selectLeaseResult = downloadLeaseAgreement.selectLease(leaseName);
		if(selectLeaseResult==false)
			return false;
		//Empty all static variable values
		TN_RunnerClass.emptyAllValues();
		
		boolean downloadLeaseAgreementResult =  downloadLeaseAgreement.validateSelectedLease(leaseOwnername);//leaseOwnername
		if(downloadLeaseAgreementResult==false)
			return false;
		//Extract data from PDF
		//Decide Portfolio Type
		int portfolioFlag =0;
		for(int i=0;i<mainPackage.AppConfig.IAGClientList.length;i++)
		{
			String portfolioStarting = mainPackage.AppConfig.IAGClientList[i].toLowerCase();
			if(RunnerClass.portfolio.toLowerCase().startsWith(portfolioStarting))
			{
				portfolioFlag =1;
				break;
				//AL_PropertyWare.portfolioType = "MCH";
			}
		}
		
		if(portfolioFlag==1)
			TN_PropertyWare.portfolioType = "MCH";
		else TN_PropertyWare.portfolioType = "Others";
		
		/*
		if(RunnerClass.portfolio.contains("MAN")||RunnerClass.portfolio.contains("HS")||RunnerClass.portfolio.contains("MCH"))
		{
		TN_PropertyWare.portfolioType = "MCH";
		}
		else
		TN_PropertyWare.portfolioType = "Others";
		*/
		// Decide the PDF Format
        pdfFormatType = TN_RunnerClass.decidePDFFormat();
        try
        {
        TN_RunnerClass.document.close();
        }
        catch(Exception e) {}
        
        RunnerClass.lateFeeRuleValues.clear();
        
        if(pdfFormatType.equalsIgnoreCase("Format1"))
        {
        	System.out.println("PDF Type = Format 1");
        	TN_ExtractDataFromPDF getDataFromPDF = new TN_ExtractDataFromPDF();
    		boolean getDataFromPDFResult =  getDataFromPDF.arizona();
    		if(getDataFromPDFResult == false)
    			return false;
        }
        else if(pdfFormatType.equalsIgnoreCase("Format2"))
             {
        	    System.out.println("PDF Type = Format 2");
        	    TN_ExtractDatFromPDF_Format2 getDataFromPDF_format2 = new TN_ExtractDatFromPDF_Format2();
	    		boolean getDataFromPDFResult =  getDataFromPDF_format2.arizona();
	    		if(getDataFromPDFResult == false)
    			return false;
             }
             else
             {
            	 System.out.println("PDF Type = Not Supported Format");
            	 return false;
             }
        String startDate="";
        try
        {
        	startDate= RunnerClass.convertDate(TN_PropertyWare.commensementDate).trim();
        }
        catch(Exception e)
        {
        	InsertDataIntoDatabase.notAutomatedFields(RunnerClass.leaseName, "Unable to get Start Date"+'\n');
        	//return false;
        }
        String endDate = RunnerClass.convertDate(TN_PropertyWare.expirationDate).trim();
		//Check if the Start Date, End Date and Move In Date matches in both PW and Lease Agreement
        if(!TN_PropertyWare.leaseStartDate_PW.trim().equalsIgnoreCase(startDate))
        {
        	System.out.println("Start Date doesn't Match");
 	    	InsertDataIntoDatabase.notAutomatedFields(RunnerClass.leaseName, "Start Date is not matched"+'\n');
        }
        if(!TN_PropertyWare.leaseEndDate_PW.trim().equalsIgnoreCase(endDate))
        {
        	System.out.println("End Date doesn't Match");
 	    	InsertDataIntoDatabase.notAutomatedFields(RunnerClass.leaseName, "End Date is not matched"+'\n');
        }
		
        TN_InsertDataIntoPropertyWare.insertData();
        try
        {
        TN_RunnerClass.document.close();
        }
        catch(Exception e) {}
		return true;
	}

	public  static void openBrowser() throws Exception
	{
		Map<String, Object> prefs = new HashMap<String, Object>();
        // Use File.separator as it will work on any OS
		RunnerClass.downloadFilePath = "C:\\Gopi\\Projects\\Property ware\\Lease Close Outs\\PDFS\\"+RunnerClass.leaseName.replaceAll("[^a-zA-Z0-9]+","");
	    // Use File.separator as it will work on any OS
		File file = new File(RunnerClass.downloadFilePath);
		//file.mkdir();
		if(file.exists())
		{
			FileUtils.cleanDirectory(file);
			FileUtils.deleteDirectory(file);
		}
		FileUtils.forceMkdir(file);
	    prefs.put("download.default_directory",
	    		RunnerClass.downloadFilePath);
        // Adding cpabilities to ChromeOptions
        ChromeOptions options = new ChromeOptions();
        options.setExperimentalOption("prefs", prefs);
        // Printing set download directory
         
        // Launching browser with desired capabilities
        WebDriverManager.chromedriver().setup();
        FL_driver= new ChromeDriver(options);
        FL_actions = new Actions(FL_driver);
        FL_js = (JavascriptExecutor)FL_driver;
        FL_driver.manage().timeouts().implicitlyWait(30,TimeUnit.SECONDS);
        FL_wait = new WebDriverWait(FL_driver, Duration.ofSeconds(50));
       // FL_driver.get(AppConfig.propertyWareURL);
        
	}
	
	public static String decidePDFFormat() throws Exception
	{
		try
		{
		File file = RunnerClass.getLastModified();
		FileInputStream fis = new FileInputStream(file);
		TN_RunnerClass.document = PDDocument.load(fis);
	    String text = new PDFTextStripper().getText(document);
	    TN_PropertyWare.pdfText  = text;
	    if(text.contains(TN_AppConfig.PDFFormatConfirmationText)) 
	    {
	    	document.close();
			return "Format1";
	    	
	    }
	    else if(text.contains(TN_AppConfig.PDFFormat2ConfirmationText))
	         {
	    	document.close();
            return "Format2";	
	         }
	         else 
	         {
	        	System.out.println("Wrong PDF Format");
	 	    	InsertDataIntoDatabase.notAutomatedFields(RunnerClass.leaseName, "Wrong Lease Agreement PDF Format"+'\n');
	 			RunnerClass.leaseCompletedStatus = 3;
	 			document.close();
	 			return "Others";
	          }
		}
		catch(Exception e)
		{
			System.out.println("Lease Agreement was not downloaded, Bad Network");
 	    	InsertDataIntoDatabase.notAutomatedFields(RunnerClass.leaseName, "Lease Agreement was not downloaded, Bad Network"+'\n');
 			RunnerClass.leaseCompletedStatus = 3;
 			return "Others";
		}
	}
	public static void emptyAllValues()
	{
		TN_PropertyWare.commensementDate ="";
		TN_PropertyWare.expirationDate ="";
		TN_PropertyWare.proratedRent ="";
		TN_PropertyWare.proratedRentDate ="";
		TN_PropertyWare.monthlyRent="";
		TN_PropertyWare.monthlyRentDate="";
		TN_PropertyWare.adminFee="";
		TN_PropertyWare.airFilterFee="";
		TN_PropertyWare.earlyTermination="";
		TN_PropertyWare.occupants="";
		TN_PropertyWare.lateChargeDay="";
		TN_PropertyWare.lateChargeFee="";
		TN_PropertyWare.proratedPetRent="";
		TN_PropertyWare.petRentWithTax="";
		TN_PropertyWare.proratedPetRentDate="";
		TN_PropertyWare.petSecurityDeposit="";
		TN_PropertyWare.RCDetails="";
		TN_PropertyWare.petRent="";
		TN_PropertyWare.petFee="";
		TN_PropertyWare.pet1Type="";
		TN_PropertyWare.pet2Type="";
		TN_PropertyWare.serviceAnimalType="";
		TN_PropertyWare.pet1Breed="";
		TN_PropertyWare.pet2Breed="";
		TN_PropertyWare.serviceAnimalBreed="";
		TN_PropertyWare.pet1Weight="";
		TN_PropertyWare.pet2Weight="";
		TN_PropertyWare.serviceAnimalWeight="";
		TN_PropertyWare.petOneTimeNonRefundableFee="";
		TN_PropertyWare.countOfTypeWordInText=0;
		TN_PropertyWare.lateFeeChargeDay="";
		TN_PropertyWare.lateFeeAmount="";
		TN_PropertyWare.lateFeeChargePerDay="";
		TN_PropertyWare.additionalLateCharges="";
		TN_PropertyWare.additionalLateChargesLimit="";
		TN_PropertyWare.CDEType="";
		TN_PropertyWare.monthlyTenantAdminFee_Amount=0.00;
		TN_PropertyWare.calculatedPetRent=0.00;
		TN_PropertyWare.df = new DecimalFormat("0.00");
		TN_PropertyWare.pdfText="";
		TN_PropertyWare.securityDeposit="";
		TN_PropertyWare.leaseStartDate_PW="";
		TN_PropertyWare.leaseEndDate_PW="";
		TN_PropertyWare.prepaymentCharge="";
		TN_PropertyWare.petType=null;
		TN_PropertyWare.petBreed=null;
		TN_PropertyWare.petWeight=null;
		TN_PropertyWare.robot=null;
		TN_PropertyWare.concessionAddendumFlag = false;
		TN_PropertyWare.petSecurityDepositFlag = false;
		TN_PropertyWare.petFlag = false;
		TN_PropertyWare.portfolioType="";
		TN_PropertyWare.incrementRentFlag = false;
		TN_PropertyWare.proratedRentDateIsInMoveInMonthFlag=false;
		TN_PropertyWare.increasedRent_previousRentStartDate ="";
		TN_PropertyWare.increasedRent_previousRentEndDate ="";
		TN_PropertyWare.increasedRent_amount ="";
		TN_PropertyWare.increasedRent_newStartDate ="";
		TN_PropertyWare.increasedRent_newEndDate ="";
		TN_PropertyWare.serviceAnimalFlag = false;
		TN_PropertyWare.lateFeeType ="";
		TN_PropertyWare.flatFeeAmount ="";
		TN_PropertyWare.lateFeePercentage="";
		TN_PropertyWare.HVACFilterFlag = false;
		TN_PropertyWare.HVACFilterFlag = false;
		TN_PropertyWare.residentBenefitsPackageAvailabilityCheck = false;
	}
	

}
