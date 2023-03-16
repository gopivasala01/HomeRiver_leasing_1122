package NorthCarolina;

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

public class NC_RunnerClass 
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
		
		//NC_RunnerClass NC_RunnerClass = new NC_RunnerClass();
		NC_RunnerClass.openBrowser();
		//Login to Propertyware
		NC_PropertyWare downloadLeaseAgreement =new  NC_PropertyWare();
		downloadLeaseAgreement.login();
		
		boolean selectLeaseResult = downloadLeaseAgreement.selectLease(leaseName);
		if(selectLeaseResult==false)
			return false;
		//Empty all static variable values
		NC_RunnerClass.emptyAllValues();
		
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
			NC_PropertyWare.portfolioType = "MCH";
		else NC_PropertyWare.portfolioType = "Others";
		
		/*
		if(RunnerClass.portfolio.contains("MAN")||RunnerClass.portfolio.contains("HS")||RunnerClass.portfolio.contains("MCH"))
		{
		NC_PropertyWare.portfolioType = "MCH";
		}
		else
		NC_PropertyWare.portfolioType = "Others";
		*/
		// Decide the PDF Format
        pdfFormatType = NC_RunnerClass.decidePDFFormat();
        try
        {
        NC_RunnerClass.document.close();
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
        	startDate= RunnerClass.convertDate(NC_PropertyWare.commensementDate).trim();
        }
        catch(Exception e)
        {
        	InsertDataIntoDatabase.notAutomatedFields(RunnerClass.leaseName, "Unable to get Start Date"+'\n');
        	//return false;
        }
        String endDate = RunnerClass.convertDate(NC_PropertyWare.expirationDate).trim();
		//Check if the Start Date, End Date and Move In Date matches in both PW and Lease Agreement
        if(!NC_PropertyWare.leaseStartDate_PW.trim().equalsIgnoreCase(startDate))
        {
        	System.out.println("Start Date doesn't Match");
 	    	InsertDataIntoDatabase.notAutomatedFields(RunnerClass.leaseName, "Start Date is not matched"+'\n');
        }
        if(!NC_PropertyWare.leaseEndDate_PW.trim().equalsIgnoreCase(endDate))
        {
        	System.out.println("End Date doesn't Match");
 	    	InsertDataIntoDatabase.notAutomatedFields(RunnerClass.leaseName, "End Date is not matched"+'\n');
        }
		
        InsertDataIntoPropertyWare.insertData();
        try
        {
        NC_RunnerClass.document.close();
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
		NC_RunnerClass.document = PDDocument.load(fis);
	    String text = new PDFTextStripper().getText(document);
	    NC_PropertyWare.pdfText  = text;
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
		NC_PropertyWare.commensementDate ="";
		NC_PropertyWare.expirationDate ="";
		NC_PropertyWare.proratedRent ="";
		NC_PropertyWare.proratedRentDate ="";
		NC_PropertyWare.monthlyRent="";
		NC_PropertyWare.monthlyRentDate="";
		NC_PropertyWare.adminFee="";
		NC_PropertyWare.airFilterFee="";
		NC_PropertyWare.earlyTermination="";
		NC_PropertyWare.occupants="";
		NC_PropertyWare.lateChargeDay="";
		NC_PropertyWare.lateChargeFee="";
		NC_PropertyWare.proratedPetRent="";
		NC_PropertyWare.petRentWithTax="";
		NC_PropertyWare.proratedPetRentDate="";
		NC_PropertyWare.petSecurityDeposit="";
		NC_PropertyWare.RCDetails="";
		NC_PropertyWare.petRent="";
		NC_PropertyWare.petFee="";
		NC_PropertyWare.pet1Type="";
		NC_PropertyWare.pet2Type="";
		NC_PropertyWare.serviceAnimalType="";
		NC_PropertyWare.pet1Breed="";
		NC_PropertyWare.pet2Breed="";
		NC_PropertyWare.serviceAnimalBreed="";
		NC_PropertyWare.pet1Weight="";
		NC_PropertyWare.pet2Weight="";
		NC_PropertyWare.serviceAnimalWeight="";
		NC_PropertyWare.petOneTimeNonRefundableFee="";
		NC_PropertyWare.countOfTypeWordInText=0;
		NC_PropertyWare.lateFeeChargeDay="";
		NC_PropertyWare.lateFeeAmount="";
		NC_PropertyWare.lateFeeChargePerDay="";
		NC_PropertyWare.additionalLateCharges="";
		NC_PropertyWare.additionalLateChargesLimit="";
		NC_PropertyWare.CDEType="";
		NC_PropertyWare.monthlyTenantAdminFee_Amount=0.00;
		NC_PropertyWare.calculatedPetRent=0.00;
		NC_PropertyWare.df = new DecimalFormat("0.00");
		NC_PropertyWare.pdfText="";
		NC_PropertyWare.securityDeposit="";
		NC_PropertyWare.leaseStartDate_PW="";
		NC_PropertyWare.leaseEndDate_PW="";
		NC_PropertyWare.prepaymentCharge="";
		NC_PropertyWare.petType=null;
		NC_PropertyWare.petBreed=null;
		NC_PropertyWare.petWeight=null;
		NC_PropertyWare.robot=null;
		NC_PropertyWare.concessionAddendumFlag = false;
		NC_PropertyWare.petSecurityDepositFlag = false;
		NC_PropertyWare.petFlag = false;
		NC_PropertyWare.portfolioType="";
		NC_PropertyWare.incrementRentFlag = false;
		NC_PropertyWare.proratedRentDateIsInMoveInMonthFlag=false;
		NC_PropertyWare.increasedRent_previousRentStartDate ="";
		NC_PropertyWare.increasedRent_previousRentEndDate ="";
		NC_PropertyWare.increasedRent_amount ="";
		NC_PropertyWare.increasedRent_newStartDate ="";
		NC_PropertyWare.increasedRent_newEndDate ="";
		NC_PropertyWare.serviceAnimalFlag = false;
		NC_PropertyWare.lateFeeType ="";
		NC_PropertyWare.flatFeeAmount ="";
		NC_PropertyWare.lateFeePercentage="";
		NC_PropertyWare.HVACFilterFlag = false;
		NC_PropertyWare.residentBenefitsPackageAvailabilityCheck = false;
	}
	
	
}
