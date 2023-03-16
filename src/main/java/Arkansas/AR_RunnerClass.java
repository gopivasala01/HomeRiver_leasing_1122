package Arkansas;

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

public class AR_RunnerClass 
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
public static String chargeCodesTable ="automation.chargeCodesConfiguration_AR";

	public boolean runAutomation(String portfolio, String leaseName, String leaseOwnername)  throws Exception
	{
		
		//AR_RunnerClass AR_RunnerClass = new AR_RunnerClass();
		AR_RunnerClass.openBrowser();
		//Login to Propertyware
		AR_PropertyWare downloadLeaseAgreement =new  AR_PropertyWare();
		downloadLeaseAgreement.login();
		
		boolean selectLeaseResult = downloadLeaseAgreement.selectLease(leaseName);
		if(selectLeaseResult==false)
			return false;
		//Empty all static variable values
		AR_RunnerClass.emptyAllValues();
		
		boolean downloadLeaseAgreementResult =  downloadLeaseAgreement.validateSelectedLease(leaseOwnername);//leaseOwnername
		if(downloadLeaseAgreementResult==false)
			return false;
		//Extract data from PDF
		//Decide Portfolio Type
		int portfolioFlag =0;
		for(int i=0;i<mainPackage.AppConfig.IAGClientList.length;i++)
		{
			if(RunnerClass.portfolio.toLowerCase().startsWith(mainPackage.AppConfig.IAGClientList[i].toLowerCase()))
			{
				portfolioFlag =1;
				break;
				//AL_PropertyWare.portfolioType = "MCH";
			}
		}
		
		if(portfolioFlag==1)
			AR_PropertyWare.portfolioType = "MCH";
		else AR_PropertyWare.portfolioType = "Others";
		
		/*
		if(RunnerClass.portfolio.contains("MAN")||RunnerClass.portfolio.contains("HS")||RunnerClass.portfolio.contains("MCH"))
		{
		AR_PropertyWare.portfolioType = "MCH";
		}
		else
		AR_PropertyWare.portfolioType = "Others";
		*/
		// Decide the PDF Format
        pdfFormatType = AR_RunnerClass.decidePDFFormat();
        try
        {
        AR_RunnerClass.document.close();
        }
        catch(Exception e) {}
        if(pdfFormatType.equalsIgnoreCase("Format1"))
        {
        	System.out.println("PDF Type = Format 1");
        	AR_ExtractDataFromPDF getDataFromPDF = new AR_ExtractDataFromPDF();
    		boolean getDataFromPDFResult =  getDataFromPDF.arizona();
    		if(getDataFromPDFResult == false)
    			return false;
        }
        else if(pdfFormatType.equalsIgnoreCase("Format2"))
             {
        	    System.out.println("PDF Type = Format 2");
        	    AR_ExtractDataFromPDF_Format2 getDataFromPDF_format2 = new AR_ExtractDataFromPDF_Format2();
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
        	startDate= RunnerClass.convertDate(AR_PropertyWare.commensementDate).trim();
        }
        catch(Exception e)
        {
        	InsertDataIntoDatabase.notAutomatedFields(RunnerClass.leaseName, "Unable to get Start Date"+'\n');
        	//return false;
        }
        String endDate = RunnerClass.convertDate(AR_PropertyWare.expirationDate).trim();
		//Check if the Start Date, End Date and Move In Date matches in both PW and Lease Agreement
        if(!AR_PropertyWare.leaseStartDate_PW.trim().equalsIgnoreCase(startDate))
        {
        	System.out.println("Start Date doesn't Match");
 	    	InsertDataIntoDatabase.notAutomatedFields(RunnerClass.leaseName, "Start Date is not matched"+'\n');
        }
        if(!AR_PropertyWare.leaseEndDate_PW.trim().equalsIgnoreCase(endDate))
        {
        	System.out.println("End Date doesn't Match");
 	    	InsertDataIntoDatabase.notAutomatedFields(RunnerClass.leaseName, "End Date is not matched"+'\n');
        }
		
        AR_InsertDataIntoPropertyWare.insertData();
        try
        {
        AR_RunnerClass.document.close();
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
        options.addArguments("--remote-allow-origins=*");
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
		AR_RunnerClass.document = PDDocument.load(fis);
	    String text = new PDFTextStripper().getText(document);
	    AR_PropertyWare.pdfText  = text;
	    if(text.contains(AR_AppConfig.PDFFormatConfirmationText)) 
	    {
	    	document.close();
			return "Format1";
	    	
	    }
	    else if(text.contains(AR_AppConfig.PDFFormat2ConfirmationText))
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
		AR_PropertyWare.commensementDate ="";
		AR_PropertyWare.expirationDate ="";
		AR_PropertyWare.proratedRent ="";
		AR_PropertyWare.proratedRentDate ="";
		AR_PropertyWare.monthlyRent="";
		AR_PropertyWare.monthlyRentDate="";
		AR_PropertyWare.adminFee="";
		AR_PropertyWare.airFilterFee="";
		AR_PropertyWare.earlyTermination="";
		AR_PropertyWare.occupants="";
		AR_PropertyWare.lateChargeDay="";
		AR_PropertyWare.lateChargeFee="";
		AR_PropertyWare.proratedPetRent="";
		AR_PropertyWare.petRentWithTax="";
		AR_PropertyWare.proratedPetRentDate="";
		AR_PropertyWare.petSecurityDeposit="";
		AR_PropertyWare.RCDetails="";
		AR_PropertyWare.petRent="";
		AR_PropertyWare.petFee="";
		AR_PropertyWare.pet1Type="";
		AR_PropertyWare.pet2Type="";
		AR_PropertyWare.serviceAnimalType="";
		AR_PropertyWare.pet1Breed="";
		AR_PropertyWare.pet2Breed="";
		AR_PropertyWare.serviceAnimalBreed="";
		AR_PropertyWare.pet1Weight="";
		AR_PropertyWare.pet2Weight="";
		AR_PropertyWare.serviceAnimalWeight="";
		AR_PropertyWare.petOneTimeNonRefundableFee="";
		AR_PropertyWare.countOfTypeWordInText=0;
		AR_PropertyWare.lateFeeChargeDay="";
		AR_PropertyWare.lateFeeAmount="";
		AR_PropertyWare.lateFeeChargePerDay="";
		AR_PropertyWare.additionalLateCharges="";
		AR_PropertyWare.additionalLateChargesLimit="";
		AR_PropertyWare.CDEType="";
		AR_PropertyWare.monthlyTenantAdminFee_Amount=0.00;
		AR_PropertyWare.calculatedPetRent=0.00;
		AR_PropertyWare.df = new DecimalFormat("0.00");
		AR_PropertyWare.pdfText="";
		AR_PropertyWare.securityDeposit="";
		AR_PropertyWare.leaseStartDate_PW="";
		AR_PropertyWare.leaseEndDate_PW="";
		AR_PropertyWare.prepaymentCharge="";
		AR_PropertyWare.petType=null;
		AR_PropertyWare.petBreed=null;
		AR_PropertyWare.petWeight=null;
		AR_PropertyWare.robot=null;
		AR_PropertyWare.concessionAddendumFlag = false;
		AR_PropertyWare.petSecurityDepositFlag = false;
		AR_PropertyWare.petFlag = false;
		AR_PropertyWare.portfolioType="";
		AR_PropertyWare.incrementRentFlag = false;
		AR_PropertyWare.proratedRentDateIsInMoveInMonthFlag=false;
		AR_PropertyWare.increasedRent_previousRentStartDate ="";
		AR_PropertyWare.increasedRent_previousRentEndDate ="";
		AR_PropertyWare.increasedRent_amount ="";
		AR_PropertyWare.increasedRent_newStartDate ="";
		AR_PropertyWare.increasedRent_newEndDate ="";
		AR_PropertyWare.serviceAnimalFlag = false;
		AR_PropertyWare.lateFeeType ="";
		AR_PropertyWare.flatFeeAmount ="";
		AR_PropertyWare.lateFeePercentage="";
		AR_PropertyWare.HVACFilterFlag = false;
		AR_PropertyWare.HVACFilterFlag = false;
		AR_PropertyWare.residentBenefitsPackageAvailabilityCheck = false;
	}
	


}
