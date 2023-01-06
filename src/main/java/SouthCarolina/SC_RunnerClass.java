package SouthCarolina;

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

public class SC_RunnerClass 
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

	public boolean runAutomation(String portfolio, String leaseName, String leaseOwnername)  throws Exception
	{
		
		//SC_RunnerClass SC_RunnerClass = new SC_RunnerClass();
		SC_RunnerClass.openBrowser();
		//Login to Propertyware
		SC_PropertyWare downloadLeaseAgreement =new  SC_PropertyWare();
		downloadLeaseAgreement.login();
		
		boolean selectLeaseResult = downloadLeaseAgreement.selectLease(leaseName);
		if(selectLeaseResult==false)
			return false;
		//Empty all static variable values
		SC_RunnerClass.emptyAllValues();
		
		boolean downloadLeaseAgreementResult =  downloadLeaseAgreement.validateSelectedLease(leaseOwnername);//leaseOwnername
		if(downloadLeaseAgreementResult==false)
			return false;
		//Extract data from PDF
		
		if(RunnerClass.portfolio.contains("MAN")||RunnerClass.portfolio.contains("HS")||RunnerClass.portfolio.contains("MCH"))
		{
		SC_PropertyWare.portfolioType = "MCH";
		}
		else
		SC_PropertyWare.portfolioType = "Others";
		
		// Decide the PDF Format
        pdfFormatType = SC_RunnerClass.decidePDFFormat();
        try
        {
        SC_RunnerClass.document.close();
        }
        catch(Exception e) {}
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
        	startDate= RunnerClass.convertDate(SC_PropertyWare.commensementDate).trim();
        }
        catch(Exception e)
        {
        	InsertDataIntoDatabase.notAutomatedFields(RunnerClass.leaseName, "Unable to get Start Date"+'\n');
        	//return false;
        }
        String endDate = RunnerClass.convertDate(SC_PropertyWare.expirationDate).trim();
		//Check if the Start Date, End Date and Move In Date matches in both PW and Lease Agreement
        if(!SC_PropertyWare.leaseStartDate_PW.trim().equalsIgnoreCase(startDate))
        {
        	System.out.println("Start Date doesn't Match");
 	    	InsertDataIntoDatabase.notAutomatedFields(RunnerClass.leaseName, "Start Date is not matched"+'\n');
        }
        if(!SC_PropertyWare.leaseEndDate_PW.trim().equalsIgnoreCase(endDate))
        {
        	System.out.println("End Date doesn't Match");
 	    	InsertDataIntoDatabase.notAutomatedFields(RunnerClass.leaseName, "End Date is not matched"+'\n');
        }
		
        InsertDataIntoPropertyWare.insertData();
        try
        {
        SC_RunnerClass.document.close();
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
		SC_RunnerClass.document = PDDocument.load(fis);
	    String text = new PDFTextStripper().getText(document);
	    SC_PropertyWare.pdfText  = text;
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
		SC_PropertyWare.commensementDate ="";
		SC_PropertyWare.expirationDate ="";
		SC_PropertyWare.proratedRent ="";
		SC_PropertyWare.proratedRentDate ="";
		SC_PropertyWare.monthlyRent="";
		SC_PropertyWare.monthlyRentDate="";
		SC_PropertyWare.adminFee="";
		SC_PropertyWare.airFilterFee="";
		SC_PropertyWare.earlyTermination="";
		SC_PropertyWare.occupants="";
		SC_PropertyWare.lateChargeDay="";
		SC_PropertyWare.lateChargeFee="";
		SC_PropertyWare.proratedPetRent="";
		SC_PropertyWare.petRentWithTax="";
		SC_PropertyWare.proratedPetRentDate="";
		SC_PropertyWare.petSecurityDeposit="";
		SC_PropertyWare.RCDetails="";
		SC_PropertyWare.petRent="";
		SC_PropertyWare.petFee="";
		SC_PropertyWare.pet1Type="";
		SC_PropertyWare.pet2Type="";
		SC_PropertyWare.serviceAnimalType="";
		SC_PropertyWare.pet1Breed="";
		SC_PropertyWare.pet2Breed="";
		SC_PropertyWare.serviceAnimalBreed="";
		SC_PropertyWare.pet1Weight="";
		SC_PropertyWare.pet2Weight="";
		SC_PropertyWare.serviceAnimalWeight="";
		SC_PropertyWare.petOneTimeNonRefundableFee="";
		SC_PropertyWare.countOfTypeWordInText=0;
		SC_PropertyWare.lateFeeChargeDay="";
		SC_PropertyWare.lateFeeAmount="";
		SC_PropertyWare.lateFeeChargePerDay="";
		SC_PropertyWare.additionalLateCharges="";
		SC_PropertyWare.additionalLateChargesLimit="";
		SC_PropertyWare.CDEType="";
		SC_PropertyWare.monthlyTenantAdminFee_Amount=0.00;
		SC_PropertyWare.calculatedPetRent=0.00;
		SC_PropertyWare.df = new DecimalFormat("0.00");
		SC_PropertyWare.pdfText="";
		SC_PropertyWare.securityDeposit="";
		SC_PropertyWare.leaseStartDate_PW="";
		SC_PropertyWare.leaseEndDate_PW="";
		SC_PropertyWare.prepaymentCharge="";
		SC_PropertyWare.petType=null;
		SC_PropertyWare.petBreed=null;
		SC_PropertyWare.petWeight=null;
		SC_PropertyWare.robot=null;
		SC_PropertyWare.concessionAddendumFlag = false;
		SC_PropertyWare.petSecurityDepositFlag = false;
		SC_PropertyWare.petFlag = false;
		SC_PropertyWare.portfolioType="";
		SC_PropertyWare.incrementRentFlag = false;
		SC_PropertyWare.proratedRentDateIsInMoveInMonthFlag=false;
		SC_PropertyWare.increasedRent_previousRentStartDate ="";
		SC_PropertyWare.increasedRent_previousRentEndDate ="";
		SC_PropertyWare.increasedRent_amount ="";
		SC_PropertyWare.increasedRent_newStartDate ="";
		SC_PropertyWare.increasedRent_newEndDate ="";
		SC_PropertyWare.serviceAnimalFlag = false;
		SC_PropertyWare.lateFeeType ="";
		SC_PropertyWare.flatFeeAmount ="";
		SC_PropertyWare.lateFeePercentage="";
		SC_PropertyWare.HVACFilterFlag = false;
		SC_PropertyWare.residentBenefitsPackageAvailabilityCheck = false;
	}
	

}
