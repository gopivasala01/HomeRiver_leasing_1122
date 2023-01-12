package Indiana;

import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.io.File;
import java.text.DecimalFormat;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;

import mainPackage.AppConfig;
import mainPackage.InsertDataIntoDatabase;
import mainPackage.Locators;
import mainPackage.RunnerClass;

public class IN_PropertyWare
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
    public static String petFee;
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
    public static String prepaymentCharge;
    public static ArrayList<String> petType;
    public static ArrayList<String> petBreed;
    public static ArrayList<String> petWeight;
    public static Robot robot;
    public static boolean concessionAddendumFlag = false;
    public static boolean petSecurityDepositFlag = false;
    public static boolean petFlag = false;
    public static String portfolioType="";
    public static boolean incrementRentFlag = false;
    public static boolean proratedRentDateIsInMoveInMonthFlag=false;
    public static String increasedRent_previousRentStartDate ="";
    public static String increasedRent_previousRentEndDate ="";
    public static String increasedRent_amount ="";
    public static String increasedRent_newStartDate ="";
    public static String increasedRent_newEndDate ="";
    public static boolean serviceAnimalFlag = false;
    public static ArrayList<String> serviceAnimalPetType;
    public static ArrayList<String> serviceAnimalPetBreed;
    public static ArrayList<String> serviceAnimalPetWeight;
    public static String lateFeeType ="";
    public static String flatFeeAmount ="";
    public static String lateFeePercentage="";
    public static boolean HVACFilterFlag = false;
    public static boolean residentBenefitsPackageAvailabilityCheck = false;
    public static String residentBenefitsPackage = "";
   
    public void login() throws Exception
	{
		IN_RunnerClass.FL_driver.get(AppConfig.propertyWareURL);
		IN_RunnerClass.FL_driver.findElement(Locators.userName).sendKeys(AppConfig.userName);
		IN_RunnerClass.FL_driver.findElement(Locators.password).sendKeys(AppConfig.password);
		IN_RunnerClass.FL_driver.findElement(Locators.signMeIn).click();
		IN_RunnerClass.FL_driver.manage().window().maximize();
		robot = new Robot();
	}
	
	public boolean selectLease(String leaseName) throws Exception
	{
		try
		{
		IN_RunnerClass.FL_driver.findElement(Locators.dashboardsTab).click();
			IN_RunnerClass.FL_driver.findElement(Locators.searchbox).clear();
		IN_RunnerClass.FL_driver.findElement(Locators.searchbox).sendKeys(leaseName);//leaseName EARL5002 - (5002 W EARLL DR)_7517
		try
		{
		IN_RunnerClass.FL_wait.until(ExpectedConditions.invisibilityOf(IN_RunnerClass.FL_driver.findElement(Locators.searchingLoader)));
		}
		catch(Exception e)
		{}
		Thread.sleep(10000);
		System.out.println(RunnerClass.leaseName);
		
		// Select Lease from multiple leases
				List<WebElement> displayedCompanies = IN_RunnerClass.FL_driver.findElements(Locators.searchedLeaseCompanyHeadings);
				boolean leaseSelected = false;
				for(int i =0;i<displayedCompanies.size();i++)
				{
					String companyName = displayedCompanies.get(i).getText();
					if(companyName.contains("Indiana")&&!companyName.contains("Legacy"))
					{
						//FL_RunnerClass.GA_driver.findElement(By.xpath("(//*[@class='searchCat4'])["+(i+1)+"]/a")).click();
						//break;
						//AL_RunnerClass.GA_driver.findElement(By.partialLinkText(leaseName)).click();
						
						List<WebElement> leaseList = IN_RunnerClass.FL_driver.findElements(By.xpath("(//*[@class='section'])["+(i+1)+"]/ul/li/a"));
						System.out.println(leaseList.size());
						for(int j=0;j<leaseList.size();j++)
						{
							String lease = leaseList.get(j).getText();
							if(lease.contains(RunnerClass.leaseName))
							{
								IN_RunnerClass.FL_driver.findElement(By.xpath("(//*[@class='section'])["+(i+1)+"]/ul/li["+(j+1)+"]/a")).click();
								leaseSelected = true;
								break;
							}
						}
						
					}
					if(leaseSelected==true) break;
				}
				
		//IN_RunnerClass.FL_driver.findElement(Locators.selectSearchedLease).click();
		Thread.sleep(5000); 
		
		// Get Portfolio Type
		try
		{
		RunnerClass.portfolio = IN_RunnerClass.FL_driver.findElement(Locators.checkPortfolioType).getText();
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
		String CDETypeRaw = IN_RunnerClass.FL_driver.findElement(Locators.getLeaseCDEType).getText();
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
			IN_RunnerClass.FL_js.executeScript("window.scrollBy(0,300)");
			try
			{
			IN_PropertyWare.RCDetails = IN_RunnerClass.FL_driver.findElement(Locators.RCDetails).getText();
			}
			catch(Exception e)
			{
				IN_PropertyWare.RCDetails = "Error";
			}
			System.out.println("RC Details = "+IN_PropertyWare.RCDetails);
			//Click Leases Tab
			IN_RunnerClass.FL_js.executeScript("window.scrollBy(0,document.body.scrollHeight)");
			Thread.sleep(2000);
			IN_RunnerClass.FL_driver.findElement(Locators.leasesTab).click();
			try
			{
			IN_RunnerClass.FL_driver.findElement(By.partialLinkText(leaseOwner.trim())).click();
			}
			catch(Exception e)
			{
				System.out.println("Unable to Click Lease Onwer Name");
				InsertDataIntoDatabase.notAutomatedFields(RunnerClass.leaseName, "Owner Name Could not find"+'\n');
				temp =1;
				RunnerClass.leaseCompletedStatus = 2;
				return false;
			}
			leaseStartDate_PW =IN_RunnerClass.FL_driver.findElement(Locators.leaseStartDate_PW).getText();
			System.out.println("Lease Start Date in PW = "+leaseStartDate_PW);
			leaseEndDate_PW =IN_RunnerClass.FL_driver.findElement(Locators.leaseEndDate_PW).getText();
			System.out.println("Lease End Date in PW = "+leaseEndDate_PW);
			//leaseStartDate_PW = RunnerClass.convertDate(leaseStartDate_PW);
			//leaseEndDate_PW = RunnerClass.convertDate(leaseEndDate_PW);
			
			IN_RunnerClass.FL_js.executeScript("window.scrollBy(0,document.body.scrollHeight)");
			
			// Get PropertyWare URL.
			
			String propertyWareURL = IN_RunnerClass.FL_driver.getCurrentUrl();
			System.out.println("Current Lease URL = "+propertyWareURL);
			InsertDataIntoDatabase.insertPropertyWareURL(RunnerClass.leaseName, propertyWareURL);
			Thread.sleep(2000);
			try
			{
			IN_RunnerClass.FL_driver.findElement(Locators.notesAndDocs).click();
			}
			catch(Exception e)
			{
				if(IN_RunnerClass.FL_driver.findElement(Locators.popUpAfterClickingLeaseName).isDisplayed())
				{
					IN_RunnerClass.FL_driver.findElement(Locators.popupClose).click();
					IN_RunnerClass.FL_js.executeScript("window.scrollBy(0,document.body.scrollHeight)");
					Thread.sleep(2000);
					IN_RunnerClass.FL_driver.findElement(Locators.notesAndDocs).click();
				}
			}
			//String leaseFirstName = leaseOwner.split(" ")[0];
			//String leaseNamePartial = RunnerClass.leaseName.split("[(]+")[1].split(" ")[0];;
			List<WebElement> documents = IN_RunnerClass.FL_driver.findElements(Locators.documentsList);
			boolean checkLeaseAgreementAvailable = false;
			for(int i =0;i<documents.size();i++)
			{
				if(documents.get(i).getText().contains("REVISED_Lease_"))//&&documents.get(i).getText().contains(leaseFirstName))
				{
					documents.get(i).click();
					checkLeaseAgreementAvailable = true;
					break;
				}
			}
			if(checkLeaseAgreementAvailable==false)
			{
			for(int i =0;i<documents.size();i++)
			{
				if(documents.get(i).getText().startsWith("Lease_"))//&&documents.get(i).getText().contains(leaseFirstName))
				{
					documents.get(i).click();
					checkLeaseAgreementAvailable = true;
					break;
				}
			}
			}
			if(checkLeaseAgreementAvailable==false)
			{
			for(int i =0;i<documents.size();i++)
			{
				if(documents.get(i).getText().contains("Lease_"))//&&documents.get(i).getText().contains(leaseFirstName))
				{
					documents.get(i).click();
					checkLeaseAgreementAvailable = true;
					break;
				}
			}
			}
			if(checkLeaseAgreementAvailable==false)
			{
				System.out.println("Unable to download Lease Agreement");
				RunnerClass.leaseCompletedStatus = 2;
				InsertDataIntoDatabase.notAutomatedFields(RunnerClass.leaseName, "Lease Agreement not found"+'\n');
				temp =1;
				return false;
			}
			Thread.sleep(30000);
			File file = RunnerClass.getLastModified();
			
			FluentWait<WebDriver> wait = new FluentWait<WebDriver>(IN_RunnerClass.FL_driver).withTimeout(Duration.ofSeconds(25)).pollingEvery(Duration.ofMillis(100));
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
	public static void clearTextField()
	{
		IN_PropertyWare.robot.keyPress(KeyEvent.VK_CONTROL);
		IN_PropertyWare.robot.keyPress(KeyEvent.VK_A);
		IN_PropertyWare.robot.keyPress(KeyEvent.VK_BACK_SPACE);
		IN_PropertyWare.robot.keyRelease(KeyEvent.VK_BACK_SPACE);
		IN_PropertyWare.robot.keyRelease(KeyEvent.VK_A);
		IN_PropertyWare.robot.keyRelease(KeyEvent.VK_CONTROL);
	}
	
	public static boolean checkProratedRentDateIsInMoveInMonth()
	{
		try
		{
		if(proratedRentDate.equalsIgnoreCase("n/a")||proratedRentDate.equalsIgnoreCase("na"))
			return true;
		if(proratedRentDate==null||proratedRentDate.equalsIgnoreCase("n/a")||proratedRentDate=="Error")
			return false;
		String proratedDate = RunnerClass.convertDate(proratedRentDate);
		String proratedMonth = proratedDate.split("/")[0];
		String moveInDate = RunnerClass.convertDate(commensementDate);
		String moveInMonth = moveInDate.split("/")[0];
		if(proratedMonth.equalsIgnoreCase(moveInMonth)||Double.parseDouble(IN_PropertyWare.proratedRent)<=200.00)
		{
			return true;
		}
		else return false;
		}
		catch(Exception e)
		{
			return false;
		}
	}

}
