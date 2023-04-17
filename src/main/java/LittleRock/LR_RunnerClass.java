package LittleRock;

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

public class LR_RunnerClass 
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
public static String chargeCodesTable ="automation.chargeCodesConfiguration_LR";

	public boolean runAutomation(String portfolio, String leaseName, String leaseOwnername)  throws Exception
	{
		
		//LR_RunnerClass LR_RunnerClass = new LR_RunnerClass();
		LR_RunnerClass.openBrowser();
		//Login to Propertyware
		LR_PropertyWare downloadLeaseAgreement =new  LR_PropertyWare();
		downloadLeaseAgreement.login();
		
		boolean selectLeaseResult = downloadLeaseAgreement.selectLease(leaseName);
		if(selectLeaseResult==false)
			return false;
		//Empty all static variable values
		LR_RunnerClass.emptyAllValues();
		
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
			LR_PropertyWare.portfolioType = "MCH";
		else LR_PropertyWare.portfolioType = "Others";
		
		/*
		if(RunnerClass.portfolio.contains("MAN")||RunnerClass.portfolio.contains("HS")||RunnerClass.portfolio.contains("MCH"))
		{
		LR_PropertyWare.portfolioType = "MCH";
		}
		else
		LR_PropertyWare.portfolioType = "Others";
		*/
		// Decide the PDF Format
        pdfFormatType = LR_RunnerClass.decidePDFFormat();
        try
        {
        LR_RunnerClass.document.close();
        }
        catch(Exception e) {}
        if(pdfFormatType.equalsIgnoreCase("Format1"))
        {
        	System.out.println("PDF Type = Format 1");
        	LR_ExtractDataFromPDF getDataFromPDF = new LR_ExtractDataFromPDF();
    		boolean getDataFromPDFResult =  getDataFromPDF.arizona();
    		if(getDataFromPDFResult == false)
    			return false;
        }
        else if(pdfFormatType.equalsIgnoreCase("Format2"))
             {
        	    System.out.println("PDF Type = Format 2");
        	    LR_ExtractDataFromPDF_Format2 getDataFromPDF_format2 = new LR_ExtractDataFromPDF_Format2();
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
        	startDate= RunnerClass.convertDate(LR_PropertyWare.commensementDate).trim();
        }
        catch(Exception e)
        {
        	InsertDataIntoDatabase.notAutomatedFields(RunnerClass.leaseName, "Unable to get Start Date"+'\n');
        	//return false;
        }
        String endDate = RunnerClass.convertDate(LR_PropertyWare.expirationDate).trim();
		//Check if the Start Date, End Date and Move In Date matches in both PW and Lease Agreement
        if(!LR_PropertyWare.leaseStartDate_PW.trim().equalsIgnoreCase(startDate))
        {
        	System.out.println("Start Date doesn't Match");
 	    	InsertDataIntoDatabase.notAutomatedFields(RunnerClass.leaseName, "Start Date is not matched"+'\n');
        }
        if(!LR_PropertyWare.leaseEndDate_PW.trim().equalsIgnoreCase(endDate))
        {
        	System.out.println("End Date doesn't Match");
 	    	InsertDataIntoDatabase.notAutomatedFields(RunnerClass.leaseName, "End Date is not matched"+'\n');
        }
		
        LR_InsertDataIntoPropertyWare.insertData();
        try
        {
        LR_RunnerClass.document.close();
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
		LR_RunnerClass.document = PDDocument.load(fis);
	    String text = new PDFTextStripper().getText(document);
	    LR_PropertyWare.pdfText  = text;
	    if(text.contains(LR_AppConfig.PDFFormatConfirmationText)) 
	    {
	    	document.close();
			return "Format1";
	    	
	    }
	    else if(text.contains(LR_AppConfig.PDFFormat2ConfirmationText))
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
		LR_PropertyWare.commensementDate ="";
		LR_PropertyWare.expirationDate ="";
		LR_PropertyWare.proratedRent ="";
		LR_PropertyWare.proratedRentDate ="";
		LR_PropertyWare.monthlyRent="";
		LR_PropertyWare.monthlyRentDate="";
		LR_PropertyWare.adminFee="";
		LR_PropertyWare.airFilterFee="";
		LR_PropertyWare.earlyTermination="";
		LR_PropertyWare.occupants="";
		LR_PropertyWare.lateChargeDay="";
		LR_PropertyWare.lateChargeFee="";
		LR_PropertyWare.proratedPetRent="";
		LR_PropertyWare.petRentWithTax="";
		LR_PropertyWare.proratedPetRentDate="";
		LR_PropertyWare.petSecurityDeposit="";
		LR_PropertyWare.RCDetails="";
		LR_PropertyWare.petRent="";
		LR_PropertyWare.petFee="";
		LR_PropertyWare.pet1Type="";
		LR_PropertyWare.pet2Type="";
		LR_PropertyWare.serviceAnimalType="";
		LR_PropertyWare.pet1Breed="";
		LR_PropertyWare.pet2Breed="";
		LR_PropertyWare.serviceAnimalBreed="";
		LR_PropertyWare.pet1Weight="";
		LR_PropertyWare.pet2Weight="";
		LR_PropertyWare.serviceAnimalWeight="";
		LR_PropertyWare.petOneTimeNonRefundableFee="";
		LR_PropertyWare.countOfTypeWordInText=0;
		LR_PropertyWare.lateFeeChargeDay="";
		LR_PropertyWare.lateFeeAmount="";
		LR_PropertyWare.lateFeeChargePerDay="";
		LR_PropertyWare.additionalLateCharges="";
		LR_PropertyWare.additionalLateChargesLimit="";
		LR_PropertyWare.CDEType="";
		LR_PropertyWare.monthlyTenantAdminFee_Amount=0.00;
		LR_PropertyWare.calculatedPetRent=0.00;
		LR_PropertyWare.df = new DecimalFormat("0.00");
		LR_PropertyWare.pdfText="";
		LR_PropertyWare.securityDeposit="";
		LR_PropertyWare.leaseStartDate_PW="";
		LR_PropertyWare.leaseEndDate_PW="";
		LR_PropertyWare.prepaymentCharge="";
		LR_PropertyWare.petType=null;
		LR_PropertyWare.petBreed=null;
		LR_PropertyWare.petWeight=null;
		LR_PropertyWare.robot=null;
		LR_PropertyWare.concessionAddendumFlag = false;
		LR_PropertyWare.petSecurityDepositFlag = false;
		LR_PropertyWare.petFlag = false;
		LR_PropertyWare.portfolioType="";
		LR_PropertyWare.incrementRentFlag = false;
		LR_PropertyWare.proratedRentDateIsInMoveInMonthFlag=false;
		LR_PropertyWare.increasedRent_previousRentStartDate ="";
		LR_PropertyWare.increasedRent_previousRentEndDate ="";
		LR_PropertyWare.increasedRent_amount ="";
		LR_PropertyWare.increasedRent_newStartDate ="";
		LR_PropertyWare.increasedRent_newEndDate ="";
		LR_PropertyWare.serviceAnimalFlag = false;
		LR_PropertyWare.lateFeeType ="";
		LR_PropertyWare.flatFeeAmount ="";
		LR_PropertyWare.lateFeePercentage="";
		LR_PropertyWare.HVACFilterFlag = false;
		LR_PropertyWare.HVACFilterFlag = false;
		LR_PropertyWare.residentBenefitsPackageAvailabilityCheck = false;
	}
	



}
