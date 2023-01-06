package Florida;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
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

public class FL_RunnerClass 
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

	public boolean runAutomation(String portfolio, String leaseName, String leaseOwnername)  throws Exception
	{
		
		//FL_RunnerClass FL_runnerClass = new FL_RunnerClass();
		FL_RunnerClass.openBrowser();
		//Login to Propertyware
		FL_PropertyWare downloadLeaseAgreement =new  FL_PropertyWare();
		downloadLeaseAgreement.login();
		
		boolean selectLeaseResult = downloadLeaseAgreement.selectLease(leaseName);
		if(selectLeaseResult==false)
			return false;
		//Empty all static variable values
		FL_RunnerClass.emptyAllValues();
		
		boolean downloadLeaseAgreementResult =  downloadLeaseAgreement.validateSelectedLease(leaseOwnername);//leaseOwnername
		if(downloadLeaseAgreementResult==false)
			return false;
		//Extract data from PDF
		
		//Decide Portfolio Type
		int portfolioFlag =0;
		for(int i=0;i<mainPackage.AppConfig.IAGClientList.length;i++)
		{
			if(RunnerClass.portfolio.contains(mainPackage.AppConfig.IAGClientList[i]))
			{
				portfolioFlag =1;
				break;
				//AL_PropertyWare.portfolioType = "MCH";
			}
		}
		
		if(portfolioFlag==1)
			FL_PropertyWare.portfolioType = "MCH";
		else FL_PropertyWare.portfolioType = "Others";
		
		/*
		if(RunnerClass.portfolio.contains("MAN")||RunnerClass.portfolio.contains("HS")||RunnerClass.portfolio.contains("MCH"))
		{
		FL_PropertyWare.portfolioType = "MCH";
		}
		else
		FL_PropertyWare.portfolioType = "Others";
		*/
		// Decide the PDF Format
        pdfFormatType = FL_RunnerClass.decidePDFFormat();
        if(pdfFormatType.equalsIgnoreCase("Format1"))
        {
        	System.out.println("PDF Type = Format 1");
        	ExtractDataFromPDF getDataFromPDF = new ExtractDataFromPDF();
    		boolean getDataFromPDFResult =  getDataFromPDF.arizona();
    		if(getDataFromPDFResult == false)
    			return false;
        }
        else if(pdfFormatType.equalsIgnoreCase("Format2"))
             {
        	    System.out.println("PDF Type = Format 2");
        	    ExtractDataFromPDF_Format2 getDataFromPDF_format2 = new ExtractDataFromPDF_Format2();
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
        	startDate= RunnerClass.convertDate(FL_PropertyWare.commensementDate).trim();
        }
        catch(Exception e)
        {
        	InsertDataIntoDatabase.notAutomatedFields(RunnerClass.leaseName, "Unable to get Start Date"+'\n');
        	//return false;
        }
        String endDate = RunnerClass.convertDate(FL_PropertyWare.expirationDate).trim();
		//Check if the Start Date, End Date and Move In Date matches in both PW and Lease Agreement
        if(!FL_PropertyWare.leaseStartDate_PW.trim().equalsIgnoreCase(startDate))
        {
        	System.out.println("Start Date doesn't Match");
 	    	InsertDataIntoDatabase.notAutomatedFields(RunnerClass.leaseName, "Start Date is not matched"+'\n');
        }
        if(!FL_PropertyWare.leaseEndDate_PW.trim().equalsIgnoreCase(endDate))
        {
        	System.out.println("End Date doesn't Match");
 	    	InsertDataIntoDatabase.notAutomatedFields(RunnerClass.leaseName, "End Date is not matched"+'\n');
        }
		
        InsertDataIntoPropertyWare.insertData();
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
		PDDocument document = PDDocument.load(fis);
	    String text = new PDFTextStripper().getText(document);
	    FL_PropertyWare.pdfText  = text;
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
		FL_PropertyWare.commensementDate ="";
		FL_PropertyWare.expirationDate ="";
		FL_PropertyWare.proratedRent ="";
		FL_PropertyWare.proratedRentDate ="";
		FL_PropertyWare.monthlyRent="";
		FL_PropertyWare.monthlyRentDate="";
		FL_PropertyWare.adminFee="";
		FL_PropertyWare.airFilterFee="";
		FL_PropertyWare.earlyTermination="";
		FL_PropertyWare.occupants="";
		FL_PropertyWare.lateChargeDay="";
		FL_PropertyWare.lateChargeFee="";
		FL_PropertyWare.proratedPetRent="";
		FL_PropertyWare.petRentWithTax="";
		FL_PropertyWare.proratedPetRentDate="";
		FL_PropertyWare.petSecurityDeposit="";
		FL_PropertyWare.RCDetails="";
		FL_PropertyWare.petRent="";
		FL_PropertyWare.petFee="";
		FL_PropertyWare.pet1Type="";
		FL_PropertyWare.pet2Type="";
		FL_PropertyWare.serviceAnimalType="";
		FL_PropertyWare.pet1Breed="";
		FL_PropertyWare.pet2Breed="";
		FL_PropertyWare.serviceAnimalBreed="";
		FL_PropertyWare.pet1Weight="";
		FL_PropertyWare.pet2Weight="";
		FL_PropertyWare.serviceAnimalWeight="";
		FL_PropertyWare.petOneTimeNonRefundableFee="";
		FL_PropertyWare.countOfTypeWordInText=0;
		FL_PropertyWare.lateFeeChargeDay="";
		FL_PropertyWare.lateFeeAmount="";
		FL_PropertyWare.lateFeeChargePerDay="";
		FL_PropertyWare.additionalLateCharges="";
		FL_PropertyWare.additionalLateChargesLimit="";
		FL_PropertyWare.CDEType="";
		FL_PropertyWare.monthlyTenantAdminFee_Amount=0.00;
		FL_PropertyWare.calculatedPetRent=0.00;
		FL_PropertyWare.df = new DecimalFormat("0.00");
		FL_PropertyWare.pdfText="";
		FL_PropertyWare.securityDeposit="";
		FL_PropertyWare.leaseStartDate_PW="";
		FL_PropertyWare.leaseEndDate_PW="";
		FL_PropertyWare.prepaymentCharge="";
		FL_PropertyWare.petType=null;
		FL_PropertyWare.petBreed=null;
		FL_PropertyWare.petWeight=null;
		FL_PropertyWare.robot=null;
		FL_PropertyWare.concessionAddendumFlag = false;
		FL_PropertyWare.petSecurityDepositFlag = false;
		FL_PropertyWare.petFlag = false;
		FL_PropertyWare.portfolioType="";
		FL_PropertyWare.incrementRentFlag = false;
		FL_PropertyWare.proratedRentDateIsInMoveInMonthFlag=false;
		FL_PropertyWare.increasedRent_previousRentStartDate ="";
		FL_PropertyWare.increasedRent_previousRentEndDate ="";
		FL_PropertyWare.increasedRent_amount ="";
		FL_PropertyWare.increasedRent_newStartDate ="";
		FL_PropertyWare.increasedRent_newEndDate ="";
		FL_PropertyWare.serviceAnimalFlag = false;
		FL_PropertyWare.HVACFilterFlag = false;
		FL_PropertyWare.HVACFilterFlag = false;
		FL_PropertyWare.residentBenefitsPackageAvailabilityCheck = false;
	}
	
	
}
