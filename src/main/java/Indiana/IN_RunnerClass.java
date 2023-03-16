package Indiana;

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

public class IN_RunnerClass 
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
public static String chargeCodesTable ="automation.chargeCodesConfiguration_IN";

	public boolean runAutomation(String portfolio, String leaseName, String leaseOwnername)  throws Exception
	{
		
		//IN_RunnerClass IN_RunnerClass = new IN_RunnerClass();
		IN_RunnerClass.openBrowser();
		//Login to Propertyware
		IN_PropertyWare downloadLeaseAgreement =new  IN_PropertyWare();
		downloadLeaseAgreement.login();
		
		boolean selectLeaseResult = downloadLeaseAgreement.selectLease(leaseName);
		if(selectLeaseResult==false)
			return false;
		//Empty all static variable values
		IN_RunnerClass.emptyAllValues();
		
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
			IN_PropertyWare.portfolioType = "MCH";
		else IN_PropertyWare.portfolioType = "Others";
		
		/*
		if(RunnerClass.portfolio.contains("MAN")||RunnerClass.portfolio.contains("HS")||RunnerClass.portfolio.contains("MCH"))
		{
		IN_PropertyWare.portfolioType = "MCH";
		}
		else
		IN_PropertyWare.portfolioType = "Others";
		*/
		// Decide the PDF Format
        pdfFormatType = IN_RunnerClass.decidePDFFormat();
        try
        {
        IN_RunnerClass.document.close();
        }
        catch(Exception e) {}
        if(pdfFormatType.equalsIgnoreCase("Format1"))
        {
        	System.out.println("PDF Type = Format 1");
        	IN_ExtractDataFromPDF getDataFromPDF = new IN_ExtractDataFromPDF();
    		boolean getDataFromPDFResult =  getDataFromPDF.arizona();
    		if(getDataFromPDFResult == false)
    			return false;
        }
        else if(pdfFormatType.equalsIgnoreCase("Format2"))
             {
        	    System.out.println("PDF Type = Format 2");
        	    IN_ExtractDataFromPDF_Format2 getDataFromPDF_format2 = new IN_ExtractDataFromPDF_Format2();
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
        	startDate= RunnerClass.convertDate(IN_PropertyWare.commensementDate).trim();
        }
        catch(Exception e)
        {
        	InsertDataIntoDatabase.notAutomatedFields(RunnerClass.leaseName, "Unable to get Start Date"+'\n');
        	//return false;
        }
        String endDate = RunnerClass.convertDate(IN_PropertyWare.expirationDate).trim();
		//Check if the Start Date, End Date and Move In Date matches in both PW and Lease Agreement
        if(!IN_PropertyWare.leaseStartDate_PW.trim().equalsIgnoreCase(startDate))
        {
        	System.out.println("Start Date doesn't Match");
 	    	InsertDataIntoDatabase.notAutomatedFields(RunnerClass.leaseName, "Start Date is not matched"+'\n');
        }
        if(!IN_PropertyWare.leaseEndDate_PW.trim().equalsIgnoreCase(endDate))
        {
        	System.out.println("End Date doesn't Match");
 	    	InsertDataIntoDatabase.notAutomatedFields(RunnerClass.leaseName, "End Date is not matched"+'\n');
        }
		
        InsertDataIntoPropertyWare.insertData();
        try
        {
        IN_RunnerClass.document.close();
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
		IN_RunnerClass.document = PDDocument.load(fis);
	    String text = new PDFTextStripper().getText(document);
	    IN_PropertyWare.pdfText  = text;
	    if(text.contains(AppConfig.PDFFormatConfirmationText)) 
	    {
	    	document.close();
			return "Format1";
	    	
	    }
	    else if(text.contains(AppConfig.PDFFormat2ConfirmationText))
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
		IN_PropertyWare.commensementDate ="";
		IN_PropertyWare.expirationDate ="";
		IN_PropertyWare.proratedRent ="";
		IN_PropertyWare.proratedRentDate ="";
		IN_PropertyWare.monthlyRent="";
		IN_PropertyWare.monthlyRentDate="";
		IN_PropertyWare.adminFee="";
		IN_PropertyWare.airFilterFee="";
		IN_PropertyWare.earlyTermination="";
		IN_PropertyWare.occupants="";
		IN_PropertyWare.lateChargeDay="";
		IN_PropertyWare.lateChargeFee="";
		IN_PropertyWare.proratedPetRent="";
		IN_PropertyWare.petRentWithTax="";
		IN_PropertyWare.proratedPetRentDate="";
		IN_PropertyWare.petSecurityDeposit="";
		IN_PropertyWare.RCDetails="";
		IN_PropertyWare.petRent="";
		IN_PropertyWare.petFee="";
		IN_PropertyWare.pet1Type="";
		IN_PropertyWare.pet2Type="";
		IN_PropertyWare.serviceAnimalType="";
		IN_PropertyWare.pet1Breed="";
		IN_PropertyWare.pet2Breed="";
		IN_PropertyWare.serviceAnimalBreed="";
		IN_PropertyWare.pet1Weight="";
		IN_PropertyWare.pet2Weight="";
		IN_PropertyWare.serviceAnimalWeight="";
		IN_PropertyWare.petOneTimeNonRefundableFee="";
		IN_PropertyWare.countOfTypeWordInText=0;
		IN_PropertyWare.lateFeeChargeDay="";
		IN_PropertyWare.lateFeeAmount="";
		IN_PropertyWare.lateFeeChargePerDay="";
		IN_PropertyWare.additionalLateCharges="";
		IN_PropertyWare.additionalLateChargesLimit="";
		IN_PropertyWare.CDEType="";
		IN_PropertyWare.monthlyTenantAdminFee_Amount=0.00;
		IN_PropertyWare.calculatedPetRent=0.00;
		IN_PropertyWare.df = new DecimalFormat("0.00");
		IN_PropertyWare.pdfText="";
		IN_PropertyWare.securityDeposit="";
		IN_PropertyWare.leaseStartDate_PW="";
		IN_PropertyWare.leaseEndDate_PW="";
		IN_PropertyWare.prepaymentCharge="";
		IN_PropertyWare.petType=null;
		IN_PropertyWare.petBreed=null;
		IN_PropertyWare.petWeight=null;
		IN_PropertyWare.robot=null;
		IN_PropertyWare.concessionAddendumFlag = false;
		IN_PropertyWare.petSecurityDepositFlag = false;
		IN_PropertyWare.petFlag = false;
		IN_PropertyWare.portfolioType="";
		IN_PropertyWare.incrementRentFlag = false;
		IN_PropertyWare.proratedRentDateIsInMoveInMonthFlag=false;
		IN_PropertyWare.increasedRent_previousRentStartDate ="";
		IN_PropertyWare.increasedRent_previousRentEndDate ="";
		IN_PropertyWare.increasedRent_amount ="";
		IN_PropertyWare.increasedRent_newStartDate ="";
		IN_PropertyWare.increasedRent_newEndDate ="";
		IN_PropertyWare.serviceAnimalFlag = false;
		IN_PropertyWare.lateFeeType ="";
		IN_PropertyWare.flatFeeAmount ="";
		IN_PropertyWare.lateFeePercentage="";
		IN_PropertyWare.HVACFilterFlag = false;
		IN_PropertyWare.HVACFilterFlag = false;
		IN_PropertyWare.residentBenefitsPackageAvailabilityCheck = false;
	}
	

}
