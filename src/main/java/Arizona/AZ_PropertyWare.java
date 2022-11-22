package Arizona;

import java.io.File;
import java.text.DecimalFormat;
import java.time.Duration;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;

import mainPackage.RunnerClass;
import mainPackage.Locators;
import mainPackage.AppConfig;
import mainPackage.InsertDataIntoDatabase;


public class AZ_PropertyWare 
{
	//static String leaseName = "CALI949 - (949 N CALIFORNIA ST)_7436";
    //static String leaseOwner = "Abner - Rozan - Rozan";
    public static String commensementDate;
    public static String expirationDate;
    public static String proratedRent;
    public static String proratedRentDate;
    public static String monthlyRent;
    public static String monthlyRentDate;
    public static String adminFee;
    public static String airFilterFee;
    public static String earlyTermiantion;
    public static String occupants;
    public static String lateChargeDay;
    public static String lateChargeFee;
    public static String proratedPetRent;
    public static String petRentWithTax;
    public static String proratedPetRentDate;
    public static String petSecurityDeposit;
    public static String RCDetails;
    public static String petRent;
    public static String pet1Type;
    public static String pet2Type;
    public static String serviceAnimalType;
    public static String pet1Breed;
    public static String pet2Breed;
    public static String serviceAnimalBreed;
    public static String pet1Weight;
    public static String pet2Weight;
    public static String serviceAnimalWeight;
    public static String petOneTimeNonRefundableFee;
    public static int countOfTypeWordInText;
    public static String lateFeeChargeDay;
    public static String lateFeeAmount;
    public static String lateFeeChargePerDay;
    public static String additionalLateCharges;
    public static String additionalLateChargesLimit;
    public static String CDEType;
    public static double monthlyTenantAdminFee_Amount;
    public static double calculatedPetRent;
    public static DecimalFormat df = new DecimalFormat("0.00");
    
    public void login()
	{
		AZ_RunnerClass.AZ_driver.get(AppConfig.propertyWareURL);
		AZ_RunnerClass.AZ_driver.findElement(Locators.userName).sendKeys(AppConfig.userName);
		AZ_RunnerClass.AZ_driver.findElement(Locators.password).sendKeys(AppConfig.password);
		AZ_RunnerClass.AZ_driver.findElement(Locators.signMeIn).click();
		AZ_RunnerClass.AZ_driver.manage().window().maximize();
	}
	
	public boolean selectLease(String leaseName) throws Exception
	{
		try
		{
		AZ_RunnerClass.AZ_driver.findElement(Locators.dashboardsTab).click();
		AZ_RunnerClass.AZ_driver.findElement(Locators.searchbox).sendKeys("EARL5002 - (5002 W EARLL DR)_7517");//leaseName
		AZ_RunnerClass.AZ_wait.until(ExpectedConditions.invisibilityOf(AZ_RunnerClass.AZ_driver.findElement(Locators.searchingLoader)));
		AZ_RunnerClass.AZ_driver.findElement(Locators.selectSearchedLease).click();
		Thread.sleep(5000); 
		// Get CDE Type details
		try
		{
		String CDETypeRaw = AZ_RunnerClass.AZ_driver.findElement(Locators.getLeaseCDEType).getText();
		System.out.println(CDETypeRaw);
		CDEType = CDETypeRaw.split("\n")[1].trim().split(" ")[0];
		//System.out.println("Test CDE "+A);
		//CDEType =CDETypeRaw.split(System.lineSeparator())[1].trim().split(" ")[0];// CDETypeRaw.split("[(]+")[0].trim().split(" ")[CDETypeRaw.split("[(]+")[0].trim().split(" ").length-1];
		System.out.println("CDE Type : "+CDEType);
		return true;
		}
		catch(Exception e)
		{
			System.out.println("CDE Type Locating issue");
			e.printStackTrace();
			return true;
		}
		}
		catch(Exception e)
		{
			System.out.println("Lease Name is not found");
			e.printStackTrace();
			RunnerClass.leaseCompletedStatus =2;
			return false;
		}
	}
	public boolean validateSelectedLease(String leaseOwner) throws Exception
	{
		// Get RC Details
		int temp=0;
		while(temp==0)
		{
			try
			{
			AZ_RunnerClass.AZ_js.executeScript("window.scrollBy(0,300)");
			AZ_PropertyWare.RCDetails = AZ_RunnerClass.AZ_driver.findElement(Locators.RCDetails).getText();
			//Click Leases Tab
			AZ_RunnerClass.AZ_js.executeScript("window.scrollBy(0,document.body.scrollHeight)");
			Thread.sleep(2000);
			AZ_RunnerClass.AZ_driver.findElement(Locators.leasesTab).click();
			AZ_RunnerClass.AZ_driver.findElement(By.linkText(leaseOwner)).click();
			String leaseStartDate_PW =AZ_RunnerClass.AZ_driver.findElement(Locators.leaseStartDate_PW).getText();
			String leaseEndDate_PW =AZ_RunnerClass.AZ_driver.findElement(Locators.leaseEndDate_PW).getText();
			AZ_RunnerClass.AZ_js.executeScript("window.scrollBy(0,document.body.scrollHeight)");
			
			// Get PropertyWare URL.
			
			String propertyWareURL = AZ_RunnerClass.AZ_driver.getCurrentUrl();
			System.out.println("Current Lease URL = "+propertyWareURL);
			InsertDataIntoDatabase.insertPropertyWareURL(RunnerClass.leaseName, propertyWareURL);
			Thread.sleep(2000);
			try
			{
			AZ_RunnerClass.AZ_driver.findElement(Locators.notesAndDocs).click();
			}
			catch(Exception e)
			{
				if(AZ_RunnerClass.AZ_driver.findElement(Locators.popUpAfterClickingLeaseName).isDisplayed())
				{
					AZ_RunnerClass.AZ_driver.findElement(Locators.popupClose).click();
					AZ_RunnerClass.AZ_js.executeScript("window.scrollBy(0,document.body.scrollHeight)");
					Thread.sleep(2000);
					AZ_RunnerClass.AZ_driver.findElement(Locators.notesAndDocs).click();
				}
			}
			String leaseFirstName = leaseOwner.split(" ")[0];
			String leaseNamePartial = RunnerClass.leaseName.split("[(]+")[1].split(" ")[0];;
			List<WebElement> documents = AZ_RunnerClass.AZ_driver.findElements(Locators.documentsList);
			
			for(int i =0;i<documents.size();i++)
			{
				if(documents.get(i).getText().startsWith("Lease_"))//&&documents.get(i).getText().contains(leaseFirstName))
				{
					documents.get(i).click();
					break;
				}
			}
			Thread.sleep(20000);
			File file = RunnerClass.getLastModified();
			FluentWait<WebDriver> wait = new FluentWait<WebDriver>(AZ_RunnerClass.AZ_driver).withTimeout(Duration.ofSeconds(25)).pollingEvery(Duration.ofMillis(100));
			wait.until( x -> file.exists());
			Thread.sleep(10000);
			break;
			}
		catch(Exception e)
		{
				System.out.println("Unable to download Lease Agreement");
				RunnerClass.leaseCompletedStatus = 2;
				temp =1;
		}
		}
		if(temp==0)
		return true;
		else return false;
		
	}
	
	public static double calculatTaxes(String monthlyRent)
	{
		
      // Monthly Tenant Admin Fee _Amount
		monthlyTenantAdminFee_Amount = Double.parseDouble(monthlyRent)*0.01;
		System.out.println("Monthly Tenant Admin Fee - Amount = "+monthlyTenantAdminFee_Amount);
		return monthlyTenantAdminFee_Amount;
		
	}
	
	public static Double calculatPetRent(String petRent, String proratedPetRent,String totalRent)
	{
		
      // Monthly Tenant Admin Fee _Amount
		try
		{
		double petRentLocal = Double.parseDouble(petRent);
		double proratedPetRentLocal = Double.parseDouble(proratedPetRent);
		double totalRentLocal = Double.parseDouble(totalRent);
		double calculate = (petRentLocal*proratedPetRentLocal)/totalRentLocal;
		calculatedPetRent = Double.parseDouble(df.format(calculate));
		System.out.println("Calculated Pet Rent = "+calculatedPetRent);
		return calculatedPetRent;
		}
		catch(Exception e)
		{
			System.out.println("Error in calculated Pet Rent");
			e.printStackTrace();
			return calculatedPetRent;
		}
		
	}

}
