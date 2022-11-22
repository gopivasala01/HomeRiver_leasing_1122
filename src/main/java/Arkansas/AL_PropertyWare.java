package Arkansas;

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


public class AL_PropertyWare 
{
	//static String leaseName = "CALI949 - (949 N CALIFORNIA ST)_7436";
    //static String leaseOwner = "Abner - Rozan - Rozan";
    public static String commensementDate ="";
    public static String expirationDate ="";
    public static String proratedRent ="";
    public static String proratedRentDate ="";
    public static String monthlyRent="";
    public static String monthlyRentDate="";
    public static String adminFee="";
    public static String airFilterFee="";
    public static String earlyTermination="";
    public static String occupants="";
    public static String lateChargeDay="";
    public static String lateChargeFee="";
    public static String proratedPetRent="";
    public static String petRentWithTax="";
    public static String proratedPetRentDate="";
    public static String petSecurityDeposit="";
    public static String RCDetails="";
    public static String petRent="";
    public static String pet1Type="";
    public static String pet2Type="";
    public static String serviceAnimalType="";
    public static String pet1Breed="";
    public static String pet2Breed="";
    public static String serviceAnimalBreed="";
    public static String pet1Weight="";
    public static String pet2Weight="";
    public static String serviceAnimalWeight="";
    public static String petOneTimeNonRefundableFee="";
    public static int countOfTypeWordInText;
    public static String lateFeeChargeDay="";
    public static String lateFeeAmount="";
    public static String lateFeeChargePerDay="";
    public static String additionalLateCharges="";
    public static String additionalLateChargesLimit="";
    public static String CDEType="";
    public static double monthlyTenantAdminFee_Amount;
    public static double calculatedPetRent;
    public static DecimalFormat df = new DecimalFormat("0.00");
    public static String pdfText="";
    public static String securityDeposit="";
    public static String leaseStartDate_PW="";
    public static String leaseEndDate_PW="";
    
    public void login()
	{
		AL_RunnerClass.AZ_driver.get(AppConfig.propertyWareURL);
		AL_RunnerClass.AZ_driver.findElement(Locators.userName).sendKeys(AppConfig.userName);
		AL_RunnerClass.AZ_driver.findElement(Locators.password).sendKeys(AppConfig.password);
		AL_RunnerClass.AZ_driver.findElement(Locators.signMeIn).click();
		AL_RunnerClass.AZ_driver.manage().window().maximize();
	}
	
	public boolean selectLease(String leaseName) throws Exception
	{
		try
		{
		AL_RunnerClass.AZ_driver.findElement(Locators.dashboardsTab).click();
		AL_RunnerClass.AZ_driver.findElement(Locators.searchbox).sendKeys(leaseName);//leaseName EARL5002 - (5002 W EARLL DR)_7517
		try
		{
		AL_RunnerClass.AZ_wait.until(ExpectedConditions.invisibilityOf(AL_RunnerClass.AZ_driver.findElement(Locators.searchingLoader)));
		}
		catch(Exception e)
		{}
		Thread.sleep(10000);
		System.out.println(RunnerClass.leaseName);
		
		// Select Lease from multiple leases
		List<WebElement> displayedCompanies = AL_RunnerClass.AZ_driver.findElements(Locators.searchedLeaseCompanyHeadings);
		for(int i =0;i<=displayedCompanies.size();i++)
		{
			String company = displayedCompanies.get(i).getText();
			if(company.contains("Alabama"))
			{
				AL_RunnerClass.AZ_driver.findElement(By.xpath("(//*[@class='searchCat4'])["+(i+1)+"]/a")).click();
				break;
				//AL_RunnerClass.AZ_driver.findElement(By.partialLinkText(leaseName)).click();
			}
		}
		
		//AL_RunnerClass.AZ_driver.findElement(Locators.selectSearchedLease).click();
		Thread.sleep(5000); 
		
		// Get Portfolio Type
		try
		{
		RunnerClass.portfolio = AL_RunnerClass.AZ_driver.findElement(Locators.checkPortfolioType).getText();
		System.out.println("Portfolio Type = "+RunnerClass.portfolio);
		}
		catch(Exception e)
		{
			System.out.println("Unable to get Portfolio Type");
			e.printStackTrace();
			InsertDataIntoDatabase.notAutomatedFields(RunnerClass.leaseName, "Unable to Get Portfolio Type");
			RunnerClass.leaseCompletedStatus =2;
			return false;
		}
		// Get CDE Type details
		try
		{
		String CDETypeRaw = AL_RunnerClass.AZ_driver.findElement(Locators.getLeaseCDEType).getText();
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
			InsertDataIntoDatabase.notAutomatedFields(RunnerClass.leaseName, "Lease Name Not Found");
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
			AL_RunnerClass.AZ_js.executeScript("window.scrollBy(0,300)");
			AL_PropertyWare.RCDetails = AL_RunnerClass.AZ_driver.findElement(Locators.RCDetails).getText();
			//Click Leases Tab
			AL_RunnerClass.AZ_js.executeScript("window.scrollBy(0,document.body.scrollHeight)");
			Thread.sleep(2000);
			AL_RunnerClass.AZ_driver.findElement(Locators.leasesTab).click();
			AL_RunnerClass.AZ_driver.findElement(By.partialLinkText(leaseOwner.trim())).click();
			leaseStartDate_PW =AL_RunnerClass.AZ_driver.findElement(Locators.leaseStartDate_PW).getText();
			System.out.println("Lease Start Date in PW = "+leaseStartDate_PW);
			leaseEndDate_PW =AL_RunnerClass.AZ_driver.findElement(Locators.leaseEndDate_PW).getText();
			System.out.println("Lease End Date in PW = "+leaseEndDate_PW);
			//leaseStartDate_PW = RunnerClass.convertDate(leaseStartDate_PW);
			//leaseEndDate_PW = RunnerClass.convertDate(leaseEndDate_PW);
			
			AL_RunnerClass.AZ_js.executeScript("window.scrollBy(0,document.body.scrollHeight)");
			
			// Get PropertyWare URL.
			
			String propertyWareURL = AL_RunnerClass.AZ_driver.getCurrentUrl();
			System.out.println("Current Lease URL = "+propertyWareURL);
			InsertDataIntoDatabase.insertPropertyWareURL(RunnerClass.leaseName, propertyWareURL);
			Thread.sleep(2000);
			try
			{
			AL_RunnerClass.AZ_driver.findElement(Locators.notesAndDocs).click();
			}
			catch(Exception e)
			{
				if(AL_RunnerClass.AZ_driver.findElement(Locators.popUpAfterClickingLeaseName).isDisplayed())
				{
					AL_RunnerClass.AZ_driver.findElement(Locators.popupClose).click();
					AL_RunnerClass.AZ_js.executeScript("window.scrollBy(0,document.body.scrollHeight)");
					Thread.sleep(2000);
					AL_RunnerClass.AZ_driver.findElement(Locators.notesAndDocs).click();
				}
			}
			//String leaseFirstName = leaseOwner.split(" ")[0];
			//String leaseNamePartial = RunnerClass.leaseName.split("[(]+")[1].split(" ")[0];;
			List<WebElement> documents = AL_RunnerClass.AZ_driver.findElements(Locators.documentsList);
			
			for(int i =0;i<documents.size();i++)
			{
				if(documents.get(i).getText().startsWith("Lease_"))//&&documents.get(i).getText().contains(leaseFirstName))
				{
					documents.get(i).click();
					break;
				}
			}
			Thread.sleep(30000);
			File file = RunnerClass.getLastModified();
			FluentWait<WebDriver> wait = new FluentWait<WebDriver>(AL_RunnerClass.AZ_driver).withTimeout(Duration.ofSeconds(25)).pollingEvery(Duration.ofMillis(100));
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
