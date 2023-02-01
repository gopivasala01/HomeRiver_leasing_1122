package Georgia;

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

public class GA_RunnerClass 
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
public static String chargeCodesTable ="automation.chargeCodesConfiguration_GA";

	public boolean runAutomation(String portfolio, String leaseName, String leaseOwnername)  throws Exception
	{
		
		//GA_RunnerClass GA_RunnerClass = new GA_RunnerClass();
		GA_RunnerClass.openBrowser();
		//Login to Propertyware
		GA_PropertyWare downloadLeaseAgreement =new  GA_PropertyWare();
		downloadLeaseAgreement.login();
		
		boolean selectLeaseResult = downloadLeaseAgreement.selectLease(leaseName);
		if(selectLeaseResult==false)
			return false;
		//Empty all static variable values
		GA_RunnerClass.emptyAllValues();
		
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
			GA_PropertyWare.portfolioType = "MCH";
		else GA_PropertyWare.portfolioType = "Others";
		
		/*
		if(RunnerClass.portfolio.contains("MAN")||RunnerClass.portfolio.contains("HS")||RunnerClass.portfolio.contains("MCH"))
		{
		GA_PropertyWare.portfolioType = "MCH";
		}
		else
		GA_PropertyWare.portfolioType = "Others";
		*/
		// Decide the PDF Format
        pdfFormatType = GA_RunnerClass.decidePDFFormat();
        try
        {
        GA_RunnerClass.document.close();
        }
        catch(Exception e) {}
        if(pdfFormatType.equalsIgnoreCase("Format1"))
        {
        	System.out.println("PDF Type = Format 1");
        	GA_ExtractDataFromPDF getDataFromPDF = new GA_ExtractDataFromPDF();
    		boolean getDataFromPDFResult =  getDataFromPDF.arizona();
    		if(getDataFromPDFResult == false)
    			return false;
        }
        else if(pdfFormatType.equalsIgnoreCase("Format2"))
             {
        	    System.out.println("PDF Type = Format 2");
        	    GA_ExtractDataFromPDF_Format2 getDataFromPDF_format2 = new GA_ExtractDataFromPDF_Format2();
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
        	startDate= RunnerClass.convertDate(GA_PropertyWare.commensementDate).trim();
        }
        catch(Exception e)
        {
        	InsertDataIntoDatabase.notAutomatedFields(RunnerClass.leaseName, "Unable to get Start Date"+'\n');
        	//return false;
        }
        String endDate = RunnerClass.convertDate(GA_PropertyWare.expirationDate).trim();
		//Check if the Start Date, End Date and Move In Date matches in both PW and Lease Agreement
        if(!GA_PropertyWare.leaseStartDate_PW.trim().equalsIgnoreCase(startDate))
        {
        	System.out.println("Start Date doesn't Match");
 	    	InsertDataIntoDatabase.notAutomatedFields(RunnerClass.leaseName, "Start Date is not matched"+'\n');
        }
        if(!GA_PropertyWare.leaseEndDate_PW.trim().equalsIgnoreCase(endDate))
        {
        	System.out.println("End Date doesn't Match");
 	    	InsertDataIntoDatabase.notAutomatedFields(RunnerClass.leaseName, "End Date is not matched"+'\n');
        }
		
        InsertDataIntoPropertyWare.insertData();
        try
        {
        GA_RunnerClass.document.close();
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
		GA_RunnerClass.document = PDDocument.load(fis);
	    String text = new PDFTextStripper().getText(document);
	    GA_PropertyWare.pdfText  = text;
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
		GA_PropertyWare.commensementDate ="";
		GA_PropertyWare.expirationDate ="";
		GA_PropertyWare.proratedRent ="";
		GA_PropertyWare.proratedRentDate ="";
		GA_PropertyWare.monthlyRent="";
		GA_PropertyWare.monthlyRentDate="";
		GA_PropertyWare.adminFee="";
		GA_PropertyWare.airFilterFee="";
		GA_PropertyWare.earlyTermination="";
		GA_PropertyWare.occupants="";
		GA_PropertyWare.lateChargeDay="";
		GA_PropertyWare.lateChargeFee="";
		GA_PropertyWare.proratedPetRent="";
		GA_PropertyWare.petRentWithTax="";
		GA_PropertyWare.proratedPetRentDate="";
		GA_PropertyWare.petSecurityDeposit="";
		GA_PropertyWare.RCDetails="";
		GA_PropertyWare.petRent="";
		GA_PropertyWare.petFee="";
		GA_PropertyWare.pet1Type="";
		GA_PropertyWare.pet2Type="";
		GA_PropertyWare.serviceAnimalType="";
		GA_PropertyWare.pet1Breed="";
		GA_PropertyWare.pet2Breed="";
		GA_PropertyWare.serviceAnimalBreed="";
		GA_PropertyWare.pet1Weight="";
		GA_PropertyWare.pet2Weight="";
		GA_PropertyWare.serviceAnimalWeight="";
		GA_PropertyWare.petOneTimeNonRefundableFee="";
		GA_PropertyWare.countOfTypeWordInText=0;
		GA_PropertyWare.lateFeeChargeDay="";
		GA_PropertyWare.lateFeeAmount="";
		GA_PropertyWare.lateFeeChargePerDay="";
		GA_PropertyWare.additionalLateCharges="";
		GA_PropertyWare.additionalLateChargesLimit="";
		GA_PropertyWare.CDEType="";
		GA_PropertyWare.monthlyTenantAdminFee_Amount=0.00;
		GA_PropertyWare.calculatedPetRent=0.00;
		GA_PropertyWare.df = new DecimalFormat("0.00");
		GA_PropertyWare.pdfText="";
		GA_PropertyWare.securityDeposit="";
		GA_PropertyWare.leaseStartDate_PW="";
		GA_PropertyWare.leaseEndDate_PW="";
		GA_PropertyWare.prepaymentCharge="";
		GA_PropertyWare.petType=null;
		GA_PropertyWare.petBreed=null;
		GA_PropertyWare.petWeight=null;
		GA_PropertyWare.robot=null;
		GA_PropertyWare.concessionAddendumFlag = false;
		GA_PropertyWare.petSecurityDepositFlag = false;
		GA_PropertyWare.petFlag = false;
		GA_PropertyWare.portfolioType="";
		GA_PropertyWare.incrementRentFlag = false;
		GA_PropertyWare.proratedRentDateIsInMoveInMonthFlag=false;
		GA_PropertyWare.increasedRent_previousRentStartDate ="";
		GA_PropertyWare.increasedRent_previousRentEndDate ="";
		GA_PropertyWare.increasedRent_amount ="";
		GA_PropertyWare.increasedRent_newStartDate ="";
		GA_PropertyWare.increasedRent_newEndDate ="";
		GA_PropertyWare.serviceAnimalFlag = false;
		GA_PropertyWare.lateFeeType ="";
		GA_PropertyWare.flatFeeAmount ="";
		GA_PropertyWare.lateFeePercentage="";
		GA_PropertyWare.HVACFilterFlag = false;
		GA_PropertyWare.HVACFilterFlag = false;
		GA_PropertyWare.residentBenefitsPackageAvailabilityCheck = false;
	}
	
	
}
