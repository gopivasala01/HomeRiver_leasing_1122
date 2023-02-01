package Alabama;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
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
import mainPackage.RunnerClass;
//import mainPackage.AppConfig;
import Alabama.AppConfig;
import NorthCarolina.NC_PropertyWare;
import mainPackage.InsertDataIntoDatabase;

public class AL_RunnerClass 
{
public static ChromeDriver AZ_driver;
public static Actions AZ_actions;
public static JavascriptExecutor AZ_js;
public static File AZ_file;
public static FileInputStream AZ_fis;
public static StringBuilder AZ_stringBuilder = new StringBuilder();
public static WebDriverWait AZ_wait;
public static FileOutputStream AZ_fos;
public static String pdfFormatType;

public boolean runAutomation(String portfolio, String leaseName, String leaseOwnername)  throws Exception
{
	
	//AL_RunnerClass AL_RunnerClass = new AL_RunnerClass();
	AL_RunnerClass.openBrowser();
	//Login to Propertyware
	AL_PropertyWare downloadLeaseAgreement =new  AL_PropertyWare();
	downloadLeaseAgreement.login();
	
	boolean selectLeaseResult = downloadLeaseAgreement.selectLease(leaseName);
	if(selectLeaseResult==false)
		return false;
	//Empty all static variable values
	AL_RunnerClass.emptyAllValues();
	
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
		AL_PropertyWare.portfolioType = "MCH";
	else AL_PropertyWare.portfolioType = "Others";
	/*
	if(RunnerClass.portfolio.contains("MAN")||RunnerClass.portfolio.contains("HS")||RunnerClass.portfolio.contains("MCH"))
	{
	AL_PropertyWare.portfolioType = "MCH";
	}
	else
	AL_PropertyWare.portfolioType = "Others";
	*/
	// Decide the PDF Format
    pdfFormatType = AL_RunnerClass.decidePDFFormat();
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
    	startDate= RunnerClass.convertDate(AL_PropertyWare.commensementDate).trim();
    }
    catch(Exception e)
    {
    	InsertDataIntoDatabase.notAutomatedFields(RunnerClass.leaseName, "Unable to get Start Date"+'\n');
    	//return false;
    }
    String endDate = RunnerClass.convertDate(AL_PropertyWare.expirationDate).trim();
	//Check if the Start Date, End Date and Move In Date matches in both PW and Lease Agreement
    if(!AL_PropertyWare.leaseStartDate_PW.trim().equalsIgnoreCase(startDate))
    {
    	System.out.println("Start Date doesn't Match");
	    	InsertDataIntoDatabase.notAutomatedFields(RunnerClass.leaseName, "Start Date is not matched"+'\n');
    }
    if(!AL_PropertyWare.leaseEndDate_PW.trim().equalsIgnoreCase(endDate))
    {
    	System.out.println("End Date doesn't Match");
	    	InsertDataIntoDatabase.notAutomatedFields(RunnerClass.leaseName, "End Date is not matched"+'\n');
    }
	
    InsertDataIntoPropertyWare_UsingConfigTable.insertData();
	return true;
}

public  static void openBrowser() throws Exception
{
	Map<String, Object> prefs = new HashMap<String, Object>();
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
    AZ_driver= new ChromeDriver(options);
    AZ_actions = new Actions(AZ_driver);
    AZ_js = (JavascriptExecutor)AZ_driver;
    AZ_driver.manage().timeouts().implicitlyWait(30,TimeUnit.SECONDS);
    AZ_wait = new WebDriverWait(AZ_driver, Duration.ofSeconds(50));
   // AZ_driver.get(AppConfig.propertyWareURL);
    
}

public static String decidePDFFormat() throws Exception
{
	try
	{
	File file = RunnerClass.getLastModified();
	FileInputStream fis = new FileInputStream(file);
	PDDocument document = PDDocument.load(fis);
    String text = new PDFTextStripper().getText(document);
    AL_PropertyWare.pdfText  = text;
    
    if(text.contains(AppConfig.PDFFormatConfirmationText)) 
    {
    	document.close();
		return "Format1";
    	
    }
    else 
    	{if(text.contains(AppConfig.PDFFormat2ConfirmationText))
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
          }}
    
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
	AL_PropertyWare.commensementDate ="";
	AL_PropertyWare.expirationDate ="";
	AL_PropertyWare.proratedRent ="";
	AL_PropertyWare.proratedRentDate ="";
	AL_PropertyWare.monthlyRent="";
	AL_PropertyWare.monthlyRentDate="";
	AL_PropertyWare.adminFee="";
	AL_PropertyWare.airFilterFee="";
	AL_PropertyWare.earlyTermination="";
	AL_PropertyWare.occupants="";
	AL_PropertyWare.lateChargeDay="";
	AL_PropertyWare.lateChargeFee="";
	AL_PropertyWare.proratedPetRent="";
	AL_PropertyWare.petRentWithTax="";
	AL_PropertyWare.proratedPetRentDate="";
	AL_PropertyWare.petSecurityDeposit="";
	AL_PropertyWare.RCDetails="";
	AL_PropertyWare.petRent="";
	AL_PropertyWare.petFee="";
	AL_PropertyWare.pet1Type="";
	AL_PropertyWare.pet2Type="";
	AL_PropertyWare.serviceAnimalType="";
	AL_PropertyWare.pet1Breed="";
	AL_PropertyWare.pet2Breed="";
	AL_PropertyWare.serviceAnimalBreed="";
	AL_PropertyWare.pet1Weight="";
	AL_PropertyWare.pet2Weight="";
	AL_PropertyWare.serviceAnimalWeight="";
	AL_PropertyWare.petOneTimeNonRefundableFee="";
	AL_PropertyWare.countOfTypeWordInText=0;
	AL_PropertyWare.lateFeeChargeDay="";
	AL_PropertyWare.lateFeeAmount="";
	AL_PropertyWare.lateFeeChargePerDay="";
	AL_PropertyWare.additionalLateCharges="";
	AL_PropertyWare.additionalLateChargesLimit="";
	AL_PropertyWare.CDEType="";
	AL_PropertyWare.monthlyTenantAdminFee_Amount=0.00;
	AL_PropertyWare.calculatedPetRent=0.00;
	AL_PropertyWare.df = new DecimalFormat("0.00");
	AL_PropertyWare.pdfText="";
	AL_PropertyWare.securityDeposit="";
	AL_PropertyWare.leaseStartDate_PW="";
	AL_PropertyWare.leaseEndDate_PW="";
	AL_PropertyWare.prepaymentCharge="";
	AL_PropertyWare.petType=null;
	AL_PropertyWare.petBreed=null;
	AL_PropertyWare.petWeight=null;
	AL_PropertyWare.robot=null;
	AL_PropertyWare.concessionAddendumFlag = false;
	AL_PropertyWare.petSecurityDepositFlag = false;
	AL_PropertyWare.petFlag = false;
	AL_PropertyWare.portfolioType="";
	AL_PropertyWare.incrementRentFlag = false;
	AL_PropertyWare.proratedRentDateIsInMoveInMonthFlag=false;
	AL_PropertyWare.increasedRent_previousRentStartDate ="";
	AL_PropertyWare.increasedRent_previousRentEndDate ="";
	AL_PropertyWare.increasedRent_amount ="";
	AL_PropertyWare.increasedRent_newStartDate ="";
	AL_PropertyWare.increasedRent_newEndDate ="";
	AL_PropertyWare.serviceAnimalFlag = false;
	AL_PropertyWare.HVACFilterFlag = false;
	NC_PropertyWare.HVACFilterFlag = false;
	NC_PropertyWare.residentBenefitsPackageAvailabilityCheck = false;
}


}
