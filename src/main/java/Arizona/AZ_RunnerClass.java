package Arizona;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.WebDriverWait;

import io.github.bonigarcia.wdm.WebDriverManager;
import mainPackage.RunnerClass;
import mainPackage.AppConfig;

public class AZ_RunnerClass 
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
		//RunnerClass.driver.get("https://www.propertyware.com/");
		//System.setProperty("wedriver.chrome.driver","C:\\Gopi\\Automation\\Chrome Drivers\\chromedriver_103.exe");
		//ChromeDriver AZ_driver = new ChromeDriver();
		//AZ_driver.get("https://www.propertyware.com/");
		
		//AZ_RunnerClass AZ_runnerClass = new AZ_RunnerClass();
		AZ_RunnerClass.openBrowser();
		//Login to Propertyware
		AZ_PropertyWare downloadLeaseAgreement =new  AZ_PropertyWare();
		downloadLeaseAgreement.login();
		downloadLeaseAgreement.selectLease(leaseName);
		downloadLeaseAgreement.validateSelectedLease("Fousel - Fousel - Fousel - Fousel");//leaseOwnername
		
		//Extract data from PDF
		
		ExtractDataFromPDF getDataFromPDF = new ExtractDataFromPDF();
		boolean getDataFromPDFResult =  getDataFromPDF.arizona();
		if(getDataFromPDFResult == false)
			return false;
		//Insert Data into PropertyWare
		
		AZ_InsertDataIntoPropertyWare insertDataInPW = new AZ_InsertDataIntoPropertyWare();
		boolean insertingDataResult =  insertDataInPW.insertData();
		AZ_driver.close();
		return true;
	    
		//AZ_InsertDataIntoPW_New insertDataPW2  = new AZ_InsertDataIntoPW_New();
		//return insertDataPW2.insertData();
		
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
	
	
	

}
