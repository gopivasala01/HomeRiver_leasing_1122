package Arkansas;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

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
import mainPackage.InsertDataIntoDatabase;

public class AL_RunnerClass 
{
public static ChromeDriver AZ_driver;
public static Actions AZ_actions;
public static JavascriptExecutor AZ_js;
public static File AZ_file;
public static FileInputStream AZ_fis;
public static StringBuilder AZ_stringBuilder = new StringBuilder() ;
public static WebDriverWait AZ_wait;
public static FileOutputStream AZ_fos;

	public boolean runAutomation(String portfolio, String leaseName, String leaseOwnername)  throws Exception
	{
		
		//AZ_RunnerClass AZ_runnerClass = new AZ_RunnerClass();
		AL_RunnerClass.openBrowser();
		//Login to Propertyware
		AL_PropertyWare downloadLeaseAgreement =new  AL_PropertyWare();
		downloadLeaseAgreement.login();
		
		boolean selectLeaseResult = downloadLeaseAgreement.selectLease(leaseName);
		if(selectLeaseResult==false)
			return false;
		
		boolean downloadLeaseAgreementResult =  downloadLeaseAgreement.validateSelectedLease(leaseOwnername);//leaseOwnername
		if(downloadLeaseAgreementResult==false)
			return false;
		//Extract data from PDF
		
		// Decide the PDF Format
        String pdfFormatType = AL_RunnerClass.decidePDFFormat();
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
        String startDate = RunnerClass.convertDate(AL_PropertyWare.commensementDate).trim();
        String endDate = RunnerClass.convertDate(AL_PropertyWare.expirationDate).trim();
		//Check if the Start Date, End Date and Move In Date matches in both PW and Lease Agreement
        if(!AL_PropertyWare.leaseStartDate_PW.trim().equalsIgnoreCase(startDate))
        {
        	System.out.println("Start Date doesn't Match");
 	    	InsertDataIntoDatabase.notAutomatedFields(RunnerClass.leaseName, "Start Date is not matched");
        }
        if(!AL_PropertyWare.leaseEndDate_PW.trim().equalsIgnoreCase(endDate))
        {
        	System.out.println("End Date doesn't Match");
 	    	InsertDataIntoDatabase.notAutomatedFields(RunnerClass.leaseName, "End Date is not matched");
        }
        /*
        if(!AL_PropertyWare.leaseStartDate_PW.trim().equalsIgnoreCase(RunnerClass.convertDate(AL_PropertyWare.commensementDate).trim()))
        {
        	System.out.println("Start Data doesn't Match");
 	    	InsertDataIntoDatabase.notAutomatedFields(RunnerClass.leaseName, "Start Date is not matched in PropertyWare and Lease Agreement");
        }
        */
        
		//Insert data into propertyware
		//Check the Portfolio Type - MCH&HS or Other personal portfolios
		
		if(RunnerClass.portfolio.contains("MAN")||RunnerClass.portfolio.contains("HS"))
		{
			AL_InsertDataIntoPropertyWare insertDataInPW_MCH_HS = new AL_InsertDataIntoPropertyWare();
			boolean insertingDataResult =  insertDataInPW_MCH_HS.insertData();
			//AZ_driver.close();
			return true;
		}
		else
		{
			AL_InsertDataIntoPropertyWare_OtherPortfolios insertDataInPW_Other = new AL_InsertDataIntoPropertyWare_OtherPortfolios();
		    boolean insertingDataResult =  insertDataInPW_Other.insertData();
		    //AZ_driver.close();
		    return true;
		}
		
	}

	public  static void openBrowser()
	{
		Map<String, Object> prefs = new HashMap<String, Object>();
        // Use File.separator as it will work on any OS
        prefs.put("download.default_directory",
                "C:\\Gopi\\Projects\\Property ware\\Lease Close Outs\\PDFS");
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
	    	
			return "Format1";
	    	
	    }
	    else if(text.contains(AppConfig.PDFFormat2ConfirmationText))
	         {
            return "Format2";	
	         }
	         else 
	         {
	        	System.out.println("Wrong PDF Format");
	 	    	InsertDataIntoDatabase.notAutomatedFields(RunnerClass.leaseName, "Wrong Lease Agreement PDF Format");
	 			RunnerClass.leaseCompletedStatus = 3;
	 			return "Others";
	          }
		}
		catch(Exception e)
		{
			System.out.println("Lease Agreement was not downloaded, Bad Network");
 	    	InsertDataIntoDatabase.notAutomatedFields(RunnerClass.leaseName, "Lease Agreement was not downloaded, Bad Network");
 			RunnerClass.leaseCompletedStatus = 3;
 			return "Others";
		}
	}
	
	
	

}
