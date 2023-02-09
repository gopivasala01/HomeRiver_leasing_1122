package Tennessee;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import Alabama.Locators;
import mainPackage.InsertDataIntoDatabase;
import mainPackage.RunnerClass;

public class TN_InsertDataIntoPropertyWare 
{
	public static String[] charges;
    public static String[][] moveInCharges;
    public static String[][] autoCharges;
    static String moveInCharges_1 =null;
	static String autoCharges_1 =null;
	static String prepaymentChargeOrMonthlyRent =null;
	public static boolean insertData() throws Exception 
	{
		
		try
		{
		//Change all values in ChargeCodesConfiguration table to NULL
		String query = "Update "+TN_RunnerClass.chargeCodesTable+" Set Amount=NULL,StartDate=NULL,EndDate=NULL,MoveINCharge=NULL,autoCharge=NULL,AutoCharge_StartDate=NULL";
		InsertDataIntoDatabase.updateTable(query);

		//Check if Prorated Rent Date is in Move in Date month
		TN_PropertyWare.proratedRentDateIsInMoveInMonthFlag =  TN_PropertyWare.checkProratedRentDateIsInMoveInMonth(); 
		System.out.println("Prorated Rent is in move in month = "+TN_PropertyWare.proratedRentDateIsInMoveInMonthFlag);
		if(TN_PropertyWare.proratedRentDateIsInMoveInMonthFlag==true)
		{
			if(TN_PropertyWare.proratedRentDate.equalsIgnoreCase("n/a")||TN_PropertyWare.proratedRentDate.equalsIgnoreCase("na")||TN_PropertyWare.proratedRentDate.equalsIgnoreCase("N/A")||TN_PropertyWare.proratedRentDate.equalsIgnoreCase("NA"))
			{
				prepaymentChargeOrMonthlyRent = "2";
				TN_PropertyWare.proratedPetRent = TN_PropertyWare.petRent;
			}
			else
			prepaymentChargeOrMonthlyRent = "12";
			
		}
		else 
		prepaymentChargeOrMonthlyRent = "9";
		//If Prorated Rent date is move in Month and Portfolio type is MCH
		//if(TN_PropertyWare.proratedRentDateIsInMoveInMonthFlag==true&&TN_PropertyWare.portfolioType=="MCH")
			//prepaymentChargeOrMonthlyRent = "9";
			//else 
			//prepaymentChargeOrMonthlyRent = "9";
		// Assign Charge codes based on conditions (Portfolio, Company etc)
		int temp=0;
		//If Consession addendum is available, skip Rents and Prorated Rents
		if(TN_PropertyWare.portfolioType=="Others")
		{
			/*
			if(TN_PropertyWare.concessionAddendumFlag==true)
			{
				TN_InsertDataIntoPropertyWare.OtherPortfolios_chargesWhenConsessionAddendumIsAvailable();
			}
			else
			{
				TN_InsertDataIntoPropertyWare.OtherPortfolios_chargesWhenConsessionAddendumIsNotAvailable();
			}
			*/
			TN_InsertDataIntoPropertyWare.OtherPortfolios_chargesWhenConsessionAddendumIsNotAvailable();
		}
		//MCH type
		if(TN_PropertyWare.portfolioType=="MCH")
		{
			/*
			if(TN_PropertyWare.concessionAddendumFlag==true)
			{
				TN_InsertDataIntoPropertyWare.MCHPortfolios_chargesWhenConsessionAddendumIsAvailable();
			}
			else
			{
				TN_InsertDataIntoPropertyWare.MCHPortfolios_chargesWhenConsessionAddendumIsNotAvailable();
			}
			*/
			TN_InsertDataIntoPropertyWare.MCHPortfolios_chargesWhenConsessionAddendumIsNotAvailable();
		}
		//Update other fields for charges
		TN_InsertDataIntoPropertyWare.updateOtherFieldsInConfigurationTable();
		
		try
		{
		TN_RunnerClass.FL_driver.navigate().refresh();
		TN_RunnerClass.FL_driver.findElement(Locators.ledgerTab).click();
		}
		catch(Exception e)
		{
			try
			{
			if(TN_RunnerClass.FL_driver.findElement(Locators.popUpAfterClickingLeaseName).isDisplayed())
			{
				TN_RunnerClass.FL_driver.findElement(Locators.popupClose).click();
				//TN_RunnerClass.FL_driver.navigate().refresh();
				TN_RunnerClass.FL_js.executeScript("window.scrollBy(document.body.scrollHeight,0)");
				TN_RunnerClass.FL_driver.findElement(Locators.ledgerTab).click();
			}
			}
			catch(Exception e2) {}
		}
		//Check if Concession Addendum is available and update NotAutomatedField if it is available
		if(TN_PropertyWare.concessionAddendumFlag==true)
		{
			InsertDataIntoDatabase.notAutomatedFields(RunnerClass.leaseName, "Concession Addendum is available"+'\n');
		}
		
		 System.out.println("------Move In Charges-------");
		// Get Move in charges
		TN_GetDataFromDB.getMoveInCharges();
		for(int i=0;i<moveInCharges.length;i++)
		{
			try
			{
			System.out.println(moveInCharges[i][0]+"   "+moveInCharges[i][1]+"   "+moveInCharges[i][2]+"   "+moveInCharges[i][3]+"  "+moveInCharges[i][4]);
			int flagToCheckIfMoveInChargeAlreadyAvailable =0;
			TN_RunnerClass.FL_driver.manage().timeouts().implicitlyWait(5,TimeUnit.SECONDS);
			TN_RunnerClass.FL_wait = new WebDriverWait(TN_RunnerClass.FL_driver, Duration.ofSeconds(5));
			//Check if the Move In Charge is already available
			
			try
			{
			List<WebElement> existingMoveInCharges_ChargeCodes = TN_RunnerClass.FL_driver.findElements(Locators.moveInCharges_List);
			List<WebElement> existingMoveInCharges_Amount = TN_RunnerClass.FL_driver.findElements(Locators.moveInCharge_List_Amount);
			for(int k=0;k<existingMoveInCharges_ChargeCodes.size();k++)
			{
				String moveInChargeChargeCode = existingMoveInCharges_ChargeCodes.get(k).getText();
				String moveInChargeAmount = existingMoveInCharges_Amount.get(k).getText();
				if(moveInChargeChargeCode.trim().contains(moveInCharges[i][1].trim())&&moveInChargeAmount.trim().replace(",", "").contains(moveInCharges[i][4].replace(",", "").trim()))
				{
					System.out.println(moveInCharges[i][0]+"   is already available");
					flagToCheckIfMoveInChargeAlreadyAvailable=1;
					break;
					
				}
			}
			if(flagToCheckIfMoveInChargeAlreadyAvailable==1)
				continue;
			}
			catch(Exception e) 
			{}
			 
			
			//Check if there is any amount has error
			try
			{
				if(moveInCharges[i][4].trim().equalsIgnoreCase("0.00")||moveInCharges[i][4]==null||moveInCharges[i][4].equalsIgnoreCase("n/a")||moveInCharges[i][4]=="Error"||RunnerClass.onlyDigits(moveInCharges[i][4].replace(",", "").replace(".", ""))==false)
				{
					InsertDataIntoDatabase.notAutomatedFields(RunnerClass.leaseName, "Move In Charge - "+moveInCharges[i][0]+'\n');
					temp=1;
					continue;
				}
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
			
			TN_RunnerClass.FL_driver.findElement(Locators.newCharge).click();
			Thread.sleep(2000);
			//Account code
			Select AutoChargesDropdown = new Select(TN_RunnerClass.FL_driver.findElement(Locators.accountDropdown));
			AutoChargesDropdown.selectByVisibleText(moveInCharges[i][1]);
			//Reference
			Thread.sleep(2000);
			TN_RunnerClass.FL_driver.findElement(Locators.referenceName).sendKeys(moveInCharges[i][2]);
			Thread.sleep(2000);
			//Amount
			TN_RunnerClass.FL_driver.findElement(Locators.moveInChargeAmount).click();
			TN_RunnerClass.FL_actions.sendKeys(Keys.BACK_SPACE).sendKeys(Keys.BACK_SPACE).sendKeys(Keys.BACK_SPACE).sendKeys(Keys.BACK_SPACE).sendKeys(Keys.BACK_SPACE).build().perform();
			Thread.sleep(2000);
			TN_RunnerClass.FL_driver.findElement(Locators.moveInChargeAmount).sendKeys(moveInCharges[i][4]); 
			Thread.sleep(2000);
			//Start Date
			TN_RunnerClass.FL_driver.findElement(Locators.moveInChargeDate).clear();
			Thread.sleep(2000);
			TN_RunnerClass.FL_driver.findElement(Locators.moveInChargeDate).sendKeys(moveInCharges[i][3]);
			//Save or Cancel button
			Thread.sleep(2000);
			if(RunnerClass.saveButtonOnAndOff==false)
			TN_RunnerClass.FL_driver.findElement(Locators.moveInChargeCancel).click();
			else 
			TN_RunnerClass.FL_driver.findElement(Locators.moveInChargeSaveButton).click();
			Thread.sleep(2000);
			try
			{
				if(TN_RunnerClass.FL_driver.findElement(Locators.somethingWrongInSavingCharge).isDisplayed())
				{
					TN_RunnerClass.FL_driver.findElement(Locators.moveInChargeCancel).click();
				}
				
			}
			catch(Exception e)
			{}
			TN_RunnerClass.FL_driver.navigate().refresh();
		}
		catch(Exception e) 
		{
			e.printStackTrace();
			System.out.println("Something went wrong in Move In charges");
		}
		
		 }
		
		// Get Auto charges
	     TN_GetDataFromDB.getAutoCharges();
	     System.out.println("------Auto Charges-------");
			// Auto Charges
			Thread.sleep(3000);
			try
			{
			TN_RunnerClass.FL_driver.findElement(Locators.summaryTab).click();
			}
			catch(Exception e)
			{
				TN_RunnerClass.FL_driver.navigate().refresh();
				TN_RunnerClass.FL_driver.findElement(Locators.summaryTab).click();
			}
			Thread.sleep(5000);
			TN_RunnerClass.FL_driver.findElement(Locators.summaryEditButton).click();
			TN_RunnerClass.FL_actions.moveToElement(TN_RunnerClass.FL_driver.findElement(Locators.newAutoCharge)).build().perform();
			
			for(int i=0;i<autoCharges.length;i++)
			{
				int flagToCheckIfAutoChargeAvailable =0;
				int autoChargesSaveFlag =0;
				System.out.println(autoCharges[i][0]+"  "+autoCharges[i][1]+"  "+autoCharges[i][2]+"  "+autoCharges[i][3]+"  "+autoCharges[i][4]);
				//for(int j=0;j<=i;j++)
				//{
				//Check if the Auto Charge is already available
				
				
				try
				{
				List<WebElement> existingAutoCharges = TN_RunnerClass.FL_driver.findElements(Locators.autoCharge_List);
				List<WebElement> existingAutoChargeAmounts = TN_RunnerClass.FL_driver.findElements(Locators.autoCharge_List_Amounts);
				
				for(int k=0;k<existingAutoCharges.size();k++)
				{
					String autoChargeCodes = existingAutoCharges.get(k).getText();
					String autoChargeAmount = existingAutoChargeAmounts.get(k).getText();
					if(autoChargeCodes.trim().contains(autoCharges[i][1])&&autoChargeAmount.trim().replace(",", "").contains(autoCharges[i][4].trim().replace(",", "")))
					{
						System.out.println(autoCharges[i][1]+"   is already available");
						flagToCheckIfAutoChargeAvailable=1;
						break;
					}
					if(autoCharges[i][1].contains(autoChargeCodes.trim().replace(".",""))&&autoChargeAmount.trim().replace(",", "").contains(autoCharges[i][4].trim().replace(",", "")))
					{
						System.out.println(autoCharges[i][1]+"   is already available");
						flagToCheckIfAutoChargeAvailable=1;
						break;
					}
				}
				}
				catch(Exception e)
				{}
				
				if(flagToCheckIfAutoChargeAvailable==1)
				continue;
				
				try
				{
				if(autoCharges[i][4]==null||autoCharges[i][4].equalsIgnoreCase("n/a")||autoCharges[i][4].trim().equalsIgnoreCase("")||autoCharges[i][4]=="Error"||RunnerClass.onlyDigits(autoCharges[i][4].replace(",", "").replace(".", ""))==false)
				{
					InsertDataIntoDatabase.notAutomatedFields(RunnerClass.leaseName, "Auto Charge - "+autoCharges[i][0]+'\n');
					temp=1;
					continue;
				}
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}
				Thread.sleep(2000);
				try
				{
				TN_RunnerClass.FL_driver.findElement(Locators.newAutoCharge).click();
				}
				catch(Exception e)
				{
					TN_RunnerClass.FL_driver.findElement(Locators.autoCharge_CancelButton).click();
					
				}
				Thread.sleep(3000);
				
				//Charge Code
				Select autoChargesDropdown = new Select(TN_RunnerClass.FL_driver.findElement(Locators.accountDropdown));
				autoChargesDropdown.selectByVisibleText(autoCharges[i][1]);
				
				//Start Date
				TN_RunnerClass.FL_driver.findElement(Locators.autoCharge_StartDate).clear();
				Thread.sleep(2000);
				TN_RunnerClass.FL_driver.findElement(Locators.autoCharge_StartDate).sendKeys(autoCharges[i][3]);
				
				//click this to hide calendar UI
				TN_RunnerClass.FL_driver.findElement(Locators.autoCharge_refField).click();
				//Amount
				TN_RunnerClass.FL_driver.findElement(Locators.autoCharge_Amount).click();
				TN_RunnerClass.FL_actions.sendKeys(Keys.BACK_SPACE).sendKeys(Keys.BACK_SPACE).sendKeys(Keys.BACK_SPACE).sendKeys(Keys.BACK_SPACE).sendKeys(Keys.BACK_SPACE).build().perform();
				TN_RunnerClass.FL_driver.findElement(Locators.autoCharge_Amount).sendKeys(autoCharges[i][4]);
				Thread.sleep(2000);
				
				//Description
				TN_RunnerClass.FL_driver.findElement(Locators.autoCharge_Description).sendKeys(autoCharges[i][2]);
				
				//End Date
				if(autoCharges[i][5]!=null&&autoCharges[i][5]!="") //TN_PropertyWare.portfolioType=="Others"&&
				{
				TN_RunnerClass.FL_driver.findElement(Locators.autoCharge_EndDate).clear();
				Thread.sleep(2000);
				TN_RunnerClass.FL_driver.findElement(Locators.autoCharge_EndDate).sendKeys(autoCharges[i][5]);
				}
				try
				{
					TN_RunnerClass.FL_driver.findElement(By.xpath("//*[text()='New Auto Charge']")).click();
				}
				catch(Exception e) {}
				//Save and Cancel
				Thread.sleep(2000);
				if(RunnerClass.saveButtonOnAndOff==false)
				TN_RunnerClass.FL_driver.findElement(Locators.autoCharge_CancelButton).click();
				else
				TN_RunnerClass.FL_driver.findElement(Locators.autoCharge_SaveButton).click();
				Thread.sleep(2000);
				try
				{
					if(TN_RunnerClass.FL_driver.findElement(Locators.somethingWrongInSavingCharge).isDisplayed())
					{
						TN_RunnerClass.FL_driver.findElement(Locators.moveInChargeCancel).click();
					}
					
				}
				catch(Exception e)
				{}
			}
			//Check If Auto Charges were entered
			
			
			
			//Other Fields
            Thread.sleep(4000);
		
			// RC Field
			try
			{
				if(TN_PropertyWare.RCDetails.equalsIgnoreCase("Error"))
				{
					InsertDataIntoDatabase.notAutomatedFields(RunnerClass.leaseName, "RC Details"+'\n');
					temp=1;
				}
				else
				{
				TN_RunnerClass.FL_actions.moveToElement(TN_RunnerClass.FL_driver.findElement(Locators.RCDetails)).build().perform();
				TN_RunnerClass.FL_driver.findElement(Locators.rcField).clear();
				Thread.sleep(1000);
				TN_RunnerClass.FL_driver.findElement(Locators.rcField).sendKeys(TN_PropertyWare.RCDetails);
				}
			}
			catch(Exception e)
			{
				try
				{
					TN_RunnerClass.FL_actions.moveToElement(TN_RunnerClass.FL_driver.findElement(Locators.APMField)).build().perform();
					TN_RunnerClass.FL_driver.findElement(Locators.APMField).clear();
					Thread.sleep(1000);
					TN_RunnerClass.FL_driver.findElement(Locators.APMField).sendKeys(TN_PropertyWare.RCDetails);
				}
				catch(Exception e2)
				{
				InsertDataIntoDatabase.notAutomatedFields(RunnerClass.leaseName, "RC Details"+'\n');
				temp=1;
				}
			}
			/*
            //Early Termination
            
			Thread.sleep(2000);
			
			try
			{
				if(TN_PropertyWare.earlyTermination.equalsIgnoreCase("Error"))
				{
					InsertDataIntoDatabase.notAutomatedFields(RunnerClass.leaseName, "Early Termination"+'\n');
					temp=1;
				}
				else
				{
				if(TN_PropertyWare.earlyTermination.contains("2"))
				{
					TN_RunnerClass.FL_actions.moveToElement(TN_RunnerClass.FL_driver.findElement(Locators.earlyTermFee2x)).build().perform();
					TN_RunnerClass.FL_driver.findElement(Locators.earlyTermFee2x).click();
					Select earlyTermination_List = new Select(TN_RunnerClass.FL_driver.findElement(Locators.earlyTermination_List));
					earlyTermination_List.selectByVisibleText("YES");
				}
				else
				{
					InsertDataIntoDatabase.notAutomatedFields(RunnerClass.leaseName, "Early Termination"+'\n');
					temp=1;
				}
				}
			}
			catch(Exception e)
			{
				InsertDataIntoDatabase.notAutomatedFields(RunnerClass.leaseName, "Early Termination"+'\n');
				e.printStackTrace();
				temp=1;
			}
			*/
			if(TN_PropertyWare.residentBenefitsPackageAvailabilityCheck==true)
			{
				if(TN_PropertyWare.residentBenefitsPackage!="Error")
				{
				Thread.sleep(2000);
				try
				{
				TN_RunnerClass.FL_actions.moveToElement(TN_RunnerClass.FL_driver.findElement(Locators.residentBenefitsPackage)).build().perform();
				TN_RunnerClass.FL_driver.findElement(Locators.residentBenefitsPackage).click();
				Select residentBenefitsPackageList = new Select(TN_RunnerClass.FL_driver.findElement(Locators.residentBenefitsPackage));
				//if(TN_PropertyWare.HVACFilterFlag==false)
				residentBenefitsPackageList.selectByVisibleText("YES");
				//else enrolledInFilterEasyList.selectByVisibleText("NO");
				}
				catch(Exception e)
				{
					InsertDataIntoDatabase.notAutomatedFields(RunnerClass.leaseName, "Resident Benefits Package"+'\n');
					temp=1;
					e.printStackTrace();
				}
				}
			}
			else
			{
				//Enrolled in FilterEasy
				if(TN_PropertyWare.airFilterFee!="Error")
				{
				Thread.sleep(2000);
				try
				{
				TN_RunnerClass.FL_actions.moveToElement(TN_RunnerClass.FL_driver.findElement(Locators.enrolledInFilterEasy)).build().perform();
				TN_RunnerClass.FL_driver.findElement(Locators.enrolledInFilterEasy).click();
				Select enrolledInFilterEasyList = new Select(TN_RunnerClass.FL_driver.findElement(Locators.enrolledInFilterEasy_List));
				if(TN_PropertyWare.HVACFilterFlag==false)
				enrolledInFilterEasyList.selectByVisibleText("YES");
				else enrolledInFilterEasyList.selectByVisibleText("NO");
				}
				catch(Exception e)
				{
					InsertDataIntoDatabase.notAutomatedFields(RunnerClass.leaseName, "Enrolled in FilterEasy"+'\n');
					temp=1;
					e.printStackTrace();
				}
				}
			}
			//Needs New Lease - No by default
			Thread.sleep(2000);
			try
			{
			TN_RunnerClass.FL_actions.moveToElement(TN_RunnerClass.FL_driver.findElement(Locators.needsNewLease)).build().perform();
			TN_RunnerClass.FL_driver.findElement(Locators.needsNewLease).click();
			Select needsNewLease_List = new Select(TN_RunnerClass.FL_driver.findElement(Locators.needsNewLease_List));
			needsNewLease_List.selectByVisibleText("No");
			}
			catch(Exception e)
			{
				InsertDataIntoDatabase.notAutomatedFields(RunnerClass.leaseName, "Needs New Lease"+'\n');
				temp=1;
			}
			//Lease Occupants
			Thread.sleep(2000);
			try
			{
				if(TN_PropertyWare.occupants.equalsIgnoreCase("Error"))
				{
					InsertDataIntoDatabase.notAutomatedFields(RunnerClass.leaseName, "Lease Occupants"+'\n');
					temp=1;
				}
				else
				{
				TN_RunnerClass.FL_actions.moveToElement(TN_RunnerClass.FL_driver.findElement(Locators.leaseOccupants)).build().perform();
				TN_RunnerClass.FL_driver.findElement(Locators.leaseOccupants).clear();
				Thread.sleep(1000);
				TN_RunnerClass.FL_driver.findElement(Locators.leaseOccupants).sendKeys(TN_PropertyWare.occupants);
				}
			}
			catch(Exception e)
			{
				InsertDataIntoDatabase.notAutomatedFields(RunnerClass.leaseName, "Lease Occupants"+'\n');
				temp=1;
			}
			if(TN_PropertyWare.petFlag==true)
			{
			//pet information
				Thread.sleep(2000);
				
				//Pet Type
				String petType = String.join(",", TN_PropertyWare.petType);
				try
				{
					TN_RunnerClass.FL_actions.moveToElement(TN_RunnerClass.FL_driver.findElement(Locators.pet1Type)).build().perform();
					TN_RunnerClass.FL_driver.findElement(Locators.pet1Type).clear();
					Thread.sleep(1000);
					TN_RunnerClass.FL_driver.findElement(Locators.pet1Type).sendKeys(petType);
				}
				catch(Exception e)
				{
					InsertDataIntoDatabase.notAutomatedFields(RunnerClass.leaseName, "Pet Types"+'\n');
					temp=1;
				}
				Thread.sleep(2000);
				//Pet Breed
				String petBreed = String.join(",", TN_PropertyWare.petBreed);
				try
				{
					TN_RunnerClass.FL_actions.moveToElement(TN_RunnerClass.FL_driver.findElement(Locators.pet1Breed)).build().perform();
					TN_RunnerClass.FL_driver.findElement(Locators.pet1Breed).clear();
					Thread.sleep(1000);
					TN_RunnerClass.FL_driver.findElement(Locators.pet1Breed).sendKeys(petBreed);
				}
				catch(Exception e)
				{
					InsertDataIntoDatabase.notAutomatedFields(RunnerClass.leaseName, "Pet Breed"+'\n');
					temp=1;
				}
				
				//Pet Weight
				String petWeight = String.join(",", TN_PropertyWare.petWeight);
				try
				{
					TN_RunnerClass.FL_actions.moveToElement(TN_RunnerClass.FL_driver.findElement(Locators.pet1Weight)).build().perform();
					TN_RunnerClass.FL_driver.findElement(Locators.pet1Weight).clear();
					Thread.sleep(1000);
					TN_RunnerClass.FL_driver.findElement(Locators.pet1Weight).sendKeys(petWeight);
				}
				catch(Exception e)
				{
					InsertDataIntoDatabase.notAutomatedFields(RunnerClass.leaseName, "Pet 2 Weight"+'\n');
					temp=1;
				}
				//Pet Rent
				Thread.sleep(2000);
				try
				{
					if(TN_PropertyWare.petRent.equalsIgnoreCase("Error"))
					{
						InsertDataIntoDatabase.notAutomatedFields(RunnerClass.leaseName, "pet Rent"+'\n');
						temp=1;
					}
					else
					{
						try
						{
					TN_RunnerClass.FL_actions.moveToElement(TN_RunnerClass.FL_driver.findElement(Locators.petAmount)).build().perform();
					//TN_RunnerClass.FL_driver.findElement(Locators.petAmount).clear();
					TN_RunnerClass.FL_driver.findElement(Locators.petAmount).click();
					//TN_PropertyWare.clearTextField();
					TN_RunnerClass.FL_driver.findElement(Locators.petAmount).sendKeys(Keys.chord(Keys.CONTROL,"a", Keys.DELETE));
					Thread.sleep(1000);
					//TN_RunnerClass.FL_actions.click(TN_RunnerClass.FL_driver.findElement(Locators.petAmount)).sendKeys(Keys.SHIFT).sendKeys(Keys.HOME).sendKeys(Keys.BACK_SPACE).build().perform();
					TN_RunnerClass.FL_driver.findElement(Locators.petAmount).sendKeys(TN_PropertyWare.petRent);
						}
						catch(Exception e)
						{
							TN_RunnerClass.FL_actions.moveToElement(TN_RunnerClass.FL_driver.findElement(Locators.petAmount2)).build().perform();
							//TN_RunnerClass.FL_driver.findElement(Locators.petAmount).clear();
							TN_RunnerClass.FL_driver.findElement(Locators.petAmount2).click();
							//TN_PropertyWare.clearTextField();
							TN_RunnerClass.FL_driver.findElement(Locators.petAmount2).sendKeys(Keys.chord(Keys.CONTROL,"a", Keys.DELETE));
							Thread.sleep(1000);
							//TN_RunnerClass.FL_actions.click(TN_RunnerClass.FL_driver.findElement(Locators.petAmount)).sendKeys(Keys.SHIFT).sendKeys(Keys.HOME).sendKeys(Keys.BACK_SPACE).build().perform();
							TN_RunnerClass.FL_driver.findElement(Locators.petAmount2).sendKeys(TN_PropertyWare.petRent);
						}
					}
				}
				catch(Exception e)
				{
					InsertDataIntoDatabase.notAutomatedFields(RunnerClass.leaseName, "pet Rent"+'\n');
					temp=1;
				}
				try
				{
					if(TN_PropertyWare.petOneTimeNonRefundableFee.equalsIgnoreCase("Error"))
					{
						InsertDataIntoDatabase.notAutomatedFields(RunnerClass.leaseName, "pet One Time Non-Refundable Fee"+'\n');
						temp=1;
					}
					else
					{
						try
						{
					TN_RunnerClass.FL_actions.moveToElement(TN_RunnerClass.FL_driver.findElement(Locators.tenantOneTimePetFee)).build().perform();
					TN_RunnerClass.FL_driver.findElement(Locators.tenantOneTimePetFee).click();
					Thread.sleep(1000);
					TN_RunnerClass.FL_driver.findElement(Locators.tenantOneTimePetFee).sendKeys(Keys.chord(Keys.CONTROL,"a", Keys.DELETE));
					//TN_PropertyWare.clearTextField();
					//TN_RunnerClass.FL_actions.click(TN_RunnerClass.FL_driver.findElement(Locators.tenantOneTimePetFee)).sendKeys(Keys.SHIFT).sendKeys(Keys.HOME).sendKeys(Keys.BACK_SPACE).build().perform();
					TN_RunnerClass.FL_driver.findElement(Locators.tenantOneTimePetFee).sendKeys(TN_PropertyWare.petOneTimeNonRefundableFee);
						}
						catch(Exception e)
						{
							TN_RunnerClass.FL_actions.moveToElement(TN_RunnerClass.FL_driver.findElement(Locators.petDepositAmount)).build().perform();
							TN_RunnerClass.FL_driver.findElement(Locators.petDepositAmount).click();
							Thread.sleep(1000);
							TN_RunnerClass.FL_driver.findElement(Locators.petDepositAmount).sendKeys(Keys.chord(Keys.CONTROL,"a", Keys.DELETE));
							//TN_PropertyWare.clearTextField();
							//TN_RunnerClass.FL_actions.click(TN_RunnerClass.FL_driver.findElement(Locators.tenantOneTimePetFee)).sendKeys(Keys.SHIFT).sendKeys(Keys.HOME).sendKeys(Keys.BACK_SPACE).build().perform();
							TN_RunnerClass.FL_driver.findElement(Locators.petDepositAmount).sendKeys(TN_PropertyWare.petOneTimeNonRefundableFee);
						}
					}
				}
				catch(Exception e)
				{
					InsertDataIntoDatabase.notAutomatedFields(RunnerClass.leaseName, "pet One Time Non-Refundable Fee"+'\n');
					temp=1;
				}
			
			}
				//Service Animal Information
				if(TN_PropertyWare.serviceAnimalFlag==true)
				{
					Thread.sleep(2000);
					
					//Pet Type
					String ServiceAnimal_petType = String.join(",", TN_PropertyWare.serviceAnimalPetType);
					try
					{
						TN_RunnerClass.FL_actions.moveToElement(TN_RunnerClass.FL_driver.findElement(Locators.serviceAnimal_pet2Type)).build().perform();
						TN_RunnerClass.FL_driver.findElement(Locators.serviceAnimal_pet2Type).clear();
						Thread.sleep(1000);
						TN_RunnerClass.FL_driver.findElement(Locators.serviceAnimal_pet2Type).sendKeys("Service "+ServiceAnimal_petType);
					}
					catch(Exception e)
					{
						InsertDataIntoDatabase.notAutomatedFields(RunnerClass.leaseName, "Pet 2 Types"+'\n');
						temp=1;
					}
					Thread.sleep(2000);
					//Pet Breed
					String serviceAnimal_petBreed = String.join(",", TN_PropertyWare.serviceAnimalPetBreed);
					try
					{
						TN_RunnerClass.FL_actions.moveToElement(TN_RunnerClass.FL_driver.findElement(Locators.serviceAnimal_pet2Breed)).build().perform();
						TN_RunnerClass.FL_driver.findElement(Locators.serviceAnimal_pet2Breed).clear();
						Thread.sleep(1000);
						TN_RunnerClass.FL_driver.findElement(Locators.serviceAnimal_pet2Breed).sendKeys(serviceAnimal_petBreed);
					}
					catch(Exception e)
					{
						InsertDataIntoDatabase.notAutomatedFields(RunnerClass.leaseName, "Pet 2 Breed"+'\n');
						temp=1;
					}
					
					
					Thread.sleep(2000);
					//Pet Weight
					String serviceAnimal_petWeight = String.join(",", TN_PropertyWare.serviceAnimalPetWeight);
					try
					{
						TN_RunnerClass.FL_actions.moveToElement(TN_RunnerClass.FL_driver.findElement(Locators.serviceAnimal_pet2Weight)).build().perform();
						TN_RunnerClass.FL_driver.findElement(Locators.serviceAnimal_pet2Weight).clear();
						Thread.sleep(1000);
						TN_RunnerClass.FL_driver.findElement(Locators.serviceAnimal_pet2Weight).sendKeys(serviceAnimal_petWeight);
					}
					catch(Exception e)
					{
						InsertDataIntoDatabase.notAutomatedFields(RunnerClass.leaseName, "Pet 2 Weight"+'\n');
						temp=1;
					}
					
					//Pet Special Provisions
					try
					{
						TN_RunnerClass.FL_actions.moveToElement(TN_RunnerClass.FL_driver.findElement(Locators.petSpecialProvisions)).build().perform();
						TN_RunnerClass.FL_driver.findElement(Locators.petSpecialProvisions).clear();
						Thread.sleep(1000);
						TN_RunnerClass.FL_driver.findElement(Locators.petSpecialProvisions).sendKeys("Service animals, no deposit required");
					}
					catch(Exception e)
					{
						InsertDataIntoDatabase.notAutomatedFields(RunnerClass.leaseName, "Pet Special Provisions"+'\n');
						temp=1;
					}
					
				} //Service Animal block
				else
				{
				//Pet Security Deposit
				Thread.sleep(2000);
				try
				{
					//if(!TN_PropertyWare.petSecurityDeposit.equalsIgnoreCase("Error")||!TN_PropertyWare.petSecurityDeposit.trim().equalsIgnoreCase(" ")||!TN_PropertyWare.petSecurityDeposit.trim().equalsIgnoreCase(""))
					if(RunnerClass.onlyDigits(TN_PropertyWare.petSecurityDeposit.trim())==true)
					{
					TN_RunnerClass.FL_actions.moveToElement(TN_RunnerClass.FL_driver.findElement(Locators.petDepositAmount)).build().perform();
					//TN_RunnerClass.FL_driver.findElement(Locators.petAmount).clear();
					TN_RunnerClass.FL_driver.findElement(Locators.petDepositAmount).click();
					TN_RunnerClass.FL_driver.findElement(Locators.petDepositAmount).sendKeys(Keys.chord(Keys.CONTROL,"a", Keys.DELETE));
					//TN_PropertyWare.clearTextField();
					Thread.sleep(1000);
					//TN_RunnerClass.FL_actions.click(TN_RunnerClass.FL_driver.findElement(Locators.petAmount)).sendKeys(Keys.SHIFT).sendKeys(Keys.HOME).sendKeys(Keys.BACK_SPACE).build().perform();
					TN_RunnerClass.FL_driver.findElement(Locators.petDepositAmount).sendKeys(TN_PropertyWare.petSecurityDeposit);
					}
				}
				catch(Exception e)
				{
					InsertDataIntoDatabase.notAutomatedFields(RunnerClass.leaseName, "Pet Security Deposit"+'\n');
					temp=1;
				}
				
				
				}
				
				
				
			

			//Initial Monthly Payment
			try
			{
				if(TN_PropertyWare.monthlyRent.equalsIgnoreCase("Error"))
				{
					InsertDataIntoDatabase.notAutomatedFields(RunnerClass.leaseName, "Intial Monthly Rent"+'\n');
					temp=1;
				}
				else
				{
				TN_RunnerClass.FL_actions.moveToElement(TN_RunnerClass.FL_driver.findElement(Locators.initialMonthlyRent)).build().perform();
				//TN_RunnerClass.FL_driver.findElement(Locators.initialMonthlyRent).clear();
				TN_RunnerClass.FL_driver.findElement(Locators.initialMonthlyRent).sendKeys(Keys.chord(Keys.CONTROL,"a", Keys.DELETE));
				TN_RunnerClass.FL_driver.findElement(Locators.initialMonthlyRent).sendKeys(TN_PropertyWare.monthlyRent);
				
				}
			}
			catch(Exception e)
			{
				InsertDataIntoDatabase.notAutomatedFields(RunnerClass.leaseName, "Intial Monthly Rent"+'\n');
				temp=1;
			}
			
			//Late Fee Rule
			TN_InsertDataIntoPropertyWare.lateFeeRuleMethod(TN_PropertyWare.lateFeeType);
			
			Thread.sleep(2000);
			TN_RunnerClass.FL_js.executeScript("window.scrollTo(0,document.body.scrollHeight)");
			try
			{
				Thread.sleep(2000);
				if(RunnerClass.saveButtonOnAndOff==true)
				TN_RunnerClass.FL_actions.moveToElement(TN_RunnerClass.FL_driver.findElement(Locators.saveLease)).click(TN_RunnerClass.FL_driver.findElement(Locators.saveLease)).build().perform();
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
			Thread.sleep(5000);
			if(temp==0)
			RunnerClass.leaseCompletedStatus = 1;
			else RunnerClass.leaseCompletedStatus = 3;
			return true;
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
			RunnerClass.leaseCompletedStatus = 2;
			Thread.sleep(2000);
			if(RunnerClass.saveButtonOnAndOff==true)
			TN_RunnerClass.FL_actions.moveToElement(TN_RunnerClass.FL_driver.findElement(Locators.saveLease)).click(TN_RunnerClass.FL_driver.findElement(Locators.saveLease)).build().perform();
			return false;
		}
			
	}
	
	public static void updateOtherFieldsInConfigurationTable() throws Exception
	{
		// Get List of Charges from table
		TN_GetDataFromDB.getChargesFromConfigurationTable();
		String DayInCommensementDate = RunnerClass.convertDate(TN_PropertyWare.commensementDate).split("/")[1].trim();
		//Update Start date and End Date
		String firstFullMonth=null;
		String secondFullMonth = null;
		if(DayInCommensementDate.equalsIgnoreCase("01")||DayInCommensementDate.equalsIgnoreCase("1"))
		{
			firstFullMonth = RunnerClass.convertDate(TN_PropertyWare.commensementDate).trim();
			secondFullMonth = RunnerClass.firstDayOfFullMonth(RunnerClass.convertDate(TN_PropertyWare.commensementDate));
			String updateStartDateAndEndDate = "Update "+TN_RunnerClass.chargeCodesTable+" Set StartDate='"+RunnerClass.convertDate(TN_PropertyWare.commensementDate)+"' where moveInCharge =1 \n"
					+ "Update "+TN_RunnerClass.chargeCodesTable+" Set autoCharge_StartDate='"+secondFullMonth+"' where AutoCharge =1 \n"
							+ "Update "+TN_RunnerClass.chargeCodesTable+" Set endDate='"+RunnerClass.DateModified(firstFullMonth)+"' where Charge ='Pro Rate Rent' ";
			InsertDataIntoDatabase.updateTable(updateStartDateAndEndDate);
		}
		else 
		{
		firstFullMonth = RunnerClass.firstDayOfFullMonth(RunnerClass.convertDate(TN_PropertyWare.commensementDate));
		secondFullMonth = RunnerClass.NextMonthOffirstDayOfFullMonth(RunnerClass.convertDate(TN_PropertyWare.commensementDate));
		String updateStartDateAndEndDate = "Update "+TN_RunnerClass.chargeCodesTable+" Set StartDate='"+RunnerClass.convertDate(TN_PropertyWare.commensementDate)+"' where moveInCharge =1 \n"
				+ "Update "+TN_RunnerClass.chargeCodesTable+" Set autoCharge_StartDate='"+firstFullMonth+"' where AutoCharge =1 \n"
						+ "Update "+TN_RunnerClass.chargeCodesTable+" Set endDate='"+RunnerClass.DateModified(firstFullMonth)+"' where Charge ='Pro Rate Rent' ";
		InsertDataIntoDatabase.updateTable(updateStartDateAndEndDate);
		}
		
		//If there is an increased rent, add add date to previous monthly rent in auto charges
		
		if(RunnerClass.onlyDigits(TN_PropertyWare.increasedRent_amount.trim().replace(",", "").replace(".", ""))==true)
		{
		String updateMonthlyRentStartDateToNextMonthOfFirstFullMonth = "Update "+TN_RunnerClass.chargeCodesTable+" Set autoCharge_StartDate='"+RunnerClass.firstDayOfFullMonth(RunnerClass.convertDate(TN_PropertyWare.commensementDate))+"',EndDate ='"+RunnerClass.convertDate(TN_PropertyWare.increasedRent_previousRentEndDate.trim())+"'  where ID=2";
		InsertDataIntoDatabase.updateTable(updateMonthlyRentStartDateToNextMonthOfFirstFullMonth);
		}
		else {
		//If Prorate Rent is under 200$, Monthly Rent Start date should be next month of First Full Month
		try
		{
		if(TN_PropertyWare.portfolioType=="Others") //Double.parseDouble(TN_PropertyWare.proratedRent.trim())<=200.00|| //||TN_PropertyWare.proratedRentDateIsInMoveInMonthFlag==true
		{
			String updateMonthlyRentStartDateWhenProrateRentIsUnder200Dollers = "Update "+TN_RunnerClass.chargeCodesTable+" Set autoCharge_StartDate='"+secondFullMonth+"' where ID=2";
			InsertDataIntoDatabase.updateTable(updateMonthlyRentStartDateWhenProrateRentIsUnder200Dollers);
		}
		}
		catch(Exception e) {}
		}
		
		try
		{
		if(TN_PropertyWare.proratedRentDateIsInMoveInMonthFlag==true&&(TN_PropertyWare.proratedPetRent!=""||TN_PropertyWare.proratedPetRent!=null||!TN_PropertyWare.proratedPetRent.equalsIgnoreCase("na")||!TN_PropertyWare.proratedPetRent.equalsIgnoreCase("n/a"))&&(DayInCommensementDate.equalsIgnoreCase("01")||DayInCommensementDate.equalsIgnoreCase("1"))) //Double.parseDouble(TN_PropertyWare.proratedRent.trim())<=200.00||
		{
			String updateMonthlyRentStartDateWhenProrateRentIsUnder200Dollers = "Update "+TN_RunnerClass.chargeCodesTable+" Set autoCharge_StartDate='"+secondFullMonth+"' where ID=8";
			InsertDataIntoDatabase.updateTable(updateMonthlyRentStartDateWhenProrateRentIsUnder200Dollers);
		}
		}
		catch(Exception e) {}
		String query =null;
		for(int i=0;i<charges.length;i++)
		{
			String charge = charges[i].toString();
			
			switch(charge)
			{
			case "Pro Rate Rent":
				query = "Update "+TN_RunnerClass.chargeCodesTable+" Set Amount ='"+TN_PropertyWare.proratedRent+"' where charge ='Pro Rate Rent'";
				//InsertDataIntoDatabase.updateTable(query1);
				continue;
				
			case "Monthly Rent":
				query = query+"\nUpdate "+TN_RunnerClass.chargeCodesTable+" Set Amount ='"+TN_PropertyWare.monthlyRent+"' where charge ='Monthly Rent'";
				//InsertDataIntoDatabase.updateTable(query2);
				continue;
			case "Tenant Admin Revenue":
				query = query+"\nUpdate "+TN_RunnerClass.chargeCodesTable+" Set Amount ='"+TN_PropertyWare.adminFee+"' where charge ='Tenant Admin Revenue'";
				//InsertDataIntoDatabase.updateTable(query3);
				continue;
			case "Pro Rated Pet Rent":
				query = query+"\nUpdate "+TN_RunnerClass.chargeCodesTable+" Set Amount ='"+TN_PropertyWare.proratedPetRent+"' where charge ='Pro Rated Pet Rent'";
				//InsertDataIntoDatabase.updateTable(query4);
				continue;
			case "Pet Security Deposit":
				query = query+"\nUpdate "+TN_RunnerClass.chargeCodesTable+" Set Amount ='"+TN_PropertyWare.petSecurityDeposit+"' where charge ='Pet Security Deposit'";
				//InsertDataIntoDatabase.updateTable(query5);
				continue;
			case "Pet One Time Non Refundable":
				query = query+"\nUpdate "+TN_RunnerClass.chargeCodesTable+" Set Amount ='"+TN_PropertyWare.petOneTimeNonRefundableFee+"' where charge ='Pet One Time Non Refundable'";
				//InsertDataIntoDatabase.updateTable(query6);
				continue;
			case "HVAC Filter Fee":
				query = query+"\nUpdate "+TN_RunnerClass.chargeCodesTable+" Set Amount ='"+TN_PropertyWare.airFilterFee+"' where charge ='HVAC Filter Fee'";
				//InsertDataIntoDatabase.updateTable(query7);
				continue;
			case "Pet Rent":
				query = query+"\nUpdate "+TN_RunnerClass.chargeCodesTable+" Set Amount ='"+TN_PropertyWare.petRent+"' where charge ='Pet Rent'";
				//InsertDataIntoDatabase.updateTable(query8);
				continue;
			case "Pre Payment Charge":
				query = query+"\nUpdate "+TN_RunnerClass.chargeCodesTable+" Set Amount ='"+TN_PropertyWare.prepaymentCharge+"' where charge ='Pre Payment Charge'";
				//InsertDataIntoDatabase.updateTable(query9);
				continue;
			case "Increased Rent":
				try {
				query = query+"\nUpdate "+TN_RunnerClass.chargeCodesTable+" Set Amount ='"+TN_PropertyWare.increasedRent_amount+"',autoCharge_StartDate ='"+RunnerClass.convertDate(TN_PropertyWare.increasedRent_newStartDate)+"' where charge ='Increased Rent'";
				//InsertDataIntoDatabase.updateTable(query10);
				}
				catch(Exception e) {}
				continue;
			case "Resident Benefits Package":
				if(TN_PropertyWare.proratedRentDate.equalsIgnoreCase("n/a")||TN_PropertyWare.proratedRentDate.equalsIgnoreCase("na")||TN_PropertyWare.proratedRentDate.equalsIgnoreCase("N/A")||TN_PropertyWare.proratedRentDate.equalsIgnoreCase("NA"))
				query = query+"\nUpdate "+TN_RunnerClass.chargeCodesTable+" Set Amount ='"+TN_PropertyWare.residentBenefitsPackage+"',startDate ='"+RunnerClass.convertDate(TN_PropertyWare.commensementDate).trim()+"',autoCharge_startDate='"+secondFullMonth+"'  where charge ='Resident Benefits Package'";
				else
					query = query+"\nUpdate "+TN_RunnerClass.chargeCodesTable+" Set Amount ='"+TN_PropertyWare.residentBenefitsPackage+"',startDate ='"+RunnerClass.convertDate(TN_PropertyWare.commensementDate).trim()+"',autoCharge_startDate='"+firstFullMonth+"'  where charge ='Resident Benefits Package'";
				//InsertDataIntoDatabase.updateTable(query9);
				continue;
			case "Monthly Rent - New MCH":
				query = query+"\nUpdate "+TN_RunnerClass.chargeCodesTable+" Set Amount ='"+TN_PropertyWare.monthlyRent+"',startDate ='"+RunnerClass.convertDate(TN_PropertyWare.commensementDate).trim()+"',autoCharge_startDate='"+firstFullMonth+"'  where ID = 12";
				//InsertDataIntoDatabase.updateTable(query9);
				continue;
			}
			
		}
		InsertDataIntoDatabase.updateTable(query);
	}
	public static void OtherPortfolios_chargesWhenConsessionAddendumIsAvailable()
	{
		if(TN_PropertyWare.portfolioType=="Others"&&TN_PropertyWare.petFlag==false)
		{
			if(TN_PropertyWare.residentBenefitsPackageAvailabilityCheck==true)
			{
				moveInCharges_1 = "3,11";
				autoCharges_1 = "11";
			}
			else
			{
			moveInCharges_1 = "3";
			autoCharges_1 = "7";
			}
			TN_GetDataFromDB.assignChargeCodes(moveInCharges_1, autoCharges_1);
		}
		else
		{
			if(TN_PropertyWare.portfolioType=="Others"&&TN_PropertyWare.petFlag==true&&TN_PropertyWare.petSecurityDepositFlag==false)
			{
				if(TN_PropertyWare.residentBenefitsPackageAvailabilityCheck==true)
				{
					moveInCharges_1 = "3,6,4,11";
					autoCharges_1 = "11,8";
				}
				else
				{
				moveInCharges_1 = "3,6,4";
				autoCharges_1 = "7,8";
				}
				TN_GetDataFromDB.assignChargeCodes(moveInCharges_1, autoCharges_1);
			}
			else//(TN_PropertyWare.portfolioType=="Others"&&TN_PropertyWare.petFlag==true&&TN_PropertyWare.petSecurityDepositFlag==true)
			{
				if(TN_PropertyWare.residentBenefitsPackageAvailabilityCheck==true)
				{
					moveInCharges_1 = "3,5,4,11";
					autoCharges_1 = "11,8";
				}
				else
				{
				moveInCharges_1 = "3,5,4";
				autoCharges_1 = "7,8";
				}
				TN_GetDataFromDB.assignChargeCodes(moveInCharges_1, autoCharges_1);
			}
		}
	}
	public static void OtherPortfolios_chargesWhenConsessionAddendumIsNotAvailable()
	{
		if(TN_PropertyWare.portfolioType=="Others"&&TN_PropertyWare.petFlag==false)
		{
			if(TN_PropertyWare.proratedRentDateIsInMoveInMonthFlag == true)
			{
				if(TN_PropertyWare.residentBenefitsPackageAvailabilityCheck==true)
				{
					moveInCharges_1 = "1,2,3,11";
					autoCharges_1 = "2,11";	
				}
				else
				{
				moveInCharges_1 = "1,2,3";
				autoCharges_1 = "2,7";
				}
			}
			else
			{
				if(TN_PropertyWare.residentBenefitsPackageAvailabilityCheck==true)
				{
			     moveInCharges_1 = "2,3,11";
			     autoCharges_1 = "1,2,11";
				}
				else
				{
					moveInCharges_1 = "2,3";
				     autoCharges_1 = "1,2,7";
				}
			}
			//TN_GetDataFromDB.assignChargeCodes(moveInCharges_1, autoCharges_1);
			TN_GetDataFromDB.assignChargeCodes(moveInCharges_1, autoCharges_1);
		}
		else
		{
			if(TN_PropertyWare.portfolioType=="Others"&&TN_PropertyWare.petFlag==true&&TN_PropertyWare.petSecurityDepositFlag==false)
			{
				if(TN_PropertyWare.proratedRentDateIsInMoveInMonthFlag == true)
				{
					if(TN_PropertyWare.residentBenefitsPackageAvailabilityCheck==true)
					{
						moveInCharges_1 = "1,2,3,4,6,11";
						autoCharges_1 = "2,11,8";
					}
					else
					{
					moveInCharges_1 = "1,2,3,4,6";
					autoCharges_1 = "2,7,8";
					}
				}
				else
				{
					if(TN_PropertyWare.residentBenefitsPackageAvailabilityCheck==true)
					{
						moveInCharges_1 = "2,3,4,6,11";
						autoCharges_1 = "1,2,11,8";
					}
					else
					{
				      moveInCharges_1 = "2,3,4,6";
				      autoCharges_1 = "1,2,7,8";
					}
				}
				TN_GetDataFromDB.assignChargeCodes(moveInCharges_1, autoCharges_1);
			}
			else//(TN_PropertyWare.portfolioType=="Others"&&TN_PropertyWare.petFlag==true&&TN_PropertyWare.petSecurityDepositFlag==true)
			{
				if(TN_PropertyWare.residentBenefitsPackageAvailabilityCheck==true)
				{
					moveInCharges_1 = "2,3,4,5,11";
					autoCharges_1 = "1,2,11,8";
				}
				else
				{
				moveInCharges_1 = "2,3,4,5";
				autoCharges_1 = "1,2,7,8";
				}
				TN_GetDataFromDB.assignChargeCodes(moveInCharges_1, autoCharges_1);
			}
		}
	}
	public static void MCHPortfolios_chargesWhenConsessionAddendumIsNotAvailable()
	{
		if(TN_PropertyWare.portfolioType=="MCH"&&TN_PropertyWare.petFlag==false)
		{
			if(TN_PropertyWare.residentBenefitsPackageAvailabilityCheck==true)
			{
				moveInCharges_1 = "1,"+prepaymentChargeOrMonthlyRent+",3,11";
				if(TN_PropertyWare.incrementRentFlag == true)
				autoCharges_1 = "2,11,10";
				else autoCharges_1 = "2,11";
			}
			else
			{
			moveInCharges_1 = "1,"+prepaymentChargeOrMonthlyRent+",3";
			if(TN_PropertyWare.incrementRentFlag == true)
			autoCharges_1 = "2,7,10";
			else autoCharges_1 = "2,7";
			}
			TN_GetDataFromDB.assignChargeCodes(moveInCharges_1, autoCharges_1);	
		}
		else
		{
			if(TN_PropertyWare.portfolioType=="MCH"&&TN_PropertyWare.petFlag==true&&TN_PropertyWare.petSecurityDepositFlag==false)
			{
				if(TN_PropertyWare.residentBenefitsPackageAvailabilityCheck==true)
				{
				moveInCharges_1 = "1,"+prepaymentChargeOrMonthlyRent+",3,4,6,11";
				if(TN_PropertyWare.incrementRentFlag == true)
				autoCharges_1 = "2,11,8,10";
				else autoCharges_1 = "2,11,8";
				}
				else
				{
					moveInCharges_1 = "1,"+prepaymentChargeOrMonthlyRent+",3,4,6";
					if(TN_PropertyWare.incrementRentFlag == true)
					autoCharges_1 = "2,7,8,10";
					else autoCharges_1 = "2,7,8";
				}
				TN_GetDataFromDB.assignChargeCodes(moveInCharges_1, autoCharges_1);
			}
		    else
		    {
				if(TN_PropertyWare.portfolioType=="MCH"&&TN_PropertyWare.petFlag==true&&TN_PropertyWare.petSecurityDepositFlag==true)
				{
					if(TN_PropertyWare.residentBenefitsPackageAvailabilityCheck==true)
					{
						moveInCharges_1 = "1,"+prepaymentChargeOrMonthlyRent+",3,4,5,11";
						if(TN_PropertyWare.incrementRentFlag == true)
						autoCharges_1 = "2,11,8,10";
						else autoCharges_1 = "2,11,8";
					}
					else
					{
					moveInCharges_1 = "1,"+prepaymentChargeOrMonthlyRent+",3,4,5";
					if(TN_PropertyWare.incrementRentFlag == true)
					autoCharges_1 = "2,7,8,10";
					else autoCharges_1 = "2,7,8";
					}
					TN_GetDataFromDB.assignChargeCodes(moveInCharges_1, autoCharges_1);
				}
		    }
		}
	}

	public static void MCHPortfolios_chargesWhenConsessionAddendumIsAvailable()
	{
		if(TN_PropertyWare.portfolioType=="MCH"&&TN_PropertyWare.petFlag==false)
		{
			if(TN_PropertyWare.residentBenefitsPackageAvailabilityCheck==true)
			{
				moveInCharges_1 = "3,11";
				autoCharges_1 = "11";
			}
			else
			{
			moveInCharges_1 = "3";
			autoCharges_1 = "7";
			}
			TN_GetDataFromDB.assignChargeCodes(moveInCharges_1, autoCharges_1);	
		}
		else
		{
			if(TN_PropertyWare.portfolioType=="MCH"&&TN_PropertyWare.petFlag==true&&TN_PropertyWare.petSecurityDepositFlag==false)
			{
				if(TN_PropertyWare.residentBenefitsPackageAvailabilityCheck==true)
				{
					moveInCharges_1 = "3,11,6,4";
					autoCharges_1 = "11,8";
				}
				else
				{
				moveInCharges_1 = "3,6,4";
				autoCharges_1 = "7,8";
				}
				TN_GetDataFromDB.assignChargeCodes(moveInCharges_1, autoCharges_1);
			}
		    else
		    {
				if(TN_PropertyWare.portfolioType=="MCH"&&TN_PropertyWare.petFlag==true&&TN_PropertyWare.petSecurityDepositFlag==true)
				{
					if(TN_PropertyWare.residentBenefitsPackageAvailabilityCheck==true)
					{
						moveInCharges_1 = "3,5,4,11";
						autoCharges_1 = "11,8";
					}
					else
					{
					moveInCharges_1 = "3,5,4";
					autoCharges_1 = "7,8";
					}
					TN_GetDataFromDB.assignChargeCodes(moveInCharges_1, autoCharges_1);
				}
		    }
		}
	}
	
	public static void lateFeeRule() throws Exception
	{
		//Late Charges
		Thread.sleep(2000);
		if(TN_PropertyWare.additionalLateChargesLimit.contains("375"))
		{
			TN_PropertyWare.lateChargeDay = "2";
		}
		else TN_PropertyWare.lateChargeDay = String.valueOf(TN_PropertyWare.lateChargeDay.trim().charAt(0));

		try
		{
			if(TN_PropertyWare.lateChargeDay.equalsIgnoreCase("Error"))
			{
				InsertDataIntoDatabase.notAutomatedFields(RunnerClass.leaseName, "Late Charges - Late Charge Day"+'\n');
				//temp=1;
			}
			else
			{
			TN_RunnerClass.FL_actions.moveToElement(TN_RunnerClass.FL_driver.findElement(Locators.lateFeeDueDay)).build().perform();
			Select dueDayList = new Select(TN_RunnerClass.FL_driver.findElement(Locators.lateFeeDueDay)) ;
			dueDayList.selectByVisibleText(TN_PropertyWare.lateChargeDay);
			}
		}
		catch(Exception e)
		{
			InsertDataIntoDatabase.notAutomatedFields(RunnerClass.leaseName, "Late Charges - Late Charge Day"+'\n');
			//temp=1;
		}
		// Initial Fee
		Thread.sleep(2000);
		try
		{
			if(TN_PropertyWare.lateChargeFee.equalsIgnoreCase("Error"))
			{
				InsertDataIntoDatabase.notAutomatedFields(RunnerClass.leaseName, "Late Charges - Late Charge Fee"+'\n');
				//temp=1;
			}
			else
			{
			TN_RunnerClass.FL_actions.moveToElement(TN_RunnerClass.FL_driver.findElement(Locators.initialFee)).build().perform();
			TN_RunnerClass.FL_driver.findElement(Locators.initialFee).click();
			Thread.sleep(1000);
			TN_RunnerClass.FL_driver.findElement(Locators.initialFee).sendKeys(Keys.chord(Keys.CONTROL,"a", Keys.DELETE));
			//TN_PropertyWare.clearTextField();
			//TN_RunnerClass.FL_actions.click(TN_RunnerClass.FL_driver.findElement(Locators.initialFee)).sendKeys(Keys.SHIFT).sendKeys(Keys.HOME).sendKeys(Keys.BACK_SPACE).build().perform();
			TN_RunnerClass.FL_driver.findElement(Locators.initialFee).sendKeys(TN_PropertyWare.lateChargeFee);
			}
		}
		catch(Exception e)
		{
			InsertDataIntoDatabase.notAutomatedFields(RunnerClass.leaseName, "Late Charges - Late Charge Fee"+'\n');
			//temp=1;
		}
		Thread.sleep(2000);
		//Initial Fee Dropdown
		
		try
		{
			if(TN_PropertyWare.lateChargeFee.contains("%"))
			{
			Select initialDropdown = new Select(TN_RunnerClass.FL_driver.findElement(Locators.initialFeeDropdown)) ;
			initialDropdown.selectByVisibleText("% of Rent Charges");
			}
			else 
			{
				Select initialDropdown = new Select(TN_RunnerClass.FL_driver.findElement(Locators.initialFeeDropdown)) ;
				initialDropdown.selectByVisibleText("Fixed Amount");
			}
		}
		catch(Exception e)
		{
			InsertDataIntoDatabase.notAutomatedFields(RunnerClass.leaseName, "Late Charges - Initial fee Dropdown"+'\n');
			//temp=1;
		}
		
		//Per Day Fee
		Thread.sleep(2000);
		try
		{
			if(TN_PropertyWare.lateFeeChargePerDay.equalsIgnoreCase("Error"))
			{
				InsertDataIntoDatabase.notAutomatedFields(RunnerClass.leaseName, "Late Charges - Late Charge Fee Per Day"+'\n');
				//temp=1;
			}
			else
			{
			TN_RunnerClass.FL_actions.moveToElement(TN_RunnerClass.FL_driver.findElement(Locators.perDayFee)).build().perform();
			TN_RunnerClass.FL_driver.findElement(Locators.perDayFee).click();
			Thread.sleep(1000);
			TN_RunnerClass.FL_driver.findElement(Locators.perDayFee).sendKeys(Keys.chord(Keys.CONTROL,"a", Keys.DELETE));
			//TN_PropertyWare.clearTextField();
			//TN_RunnerClass.FL_actions.click(TN_RunnerClass.FL_driver.findElement(Locators.perDayFee)).sendKeys(Keys.SHIFT).sendKeys(Keys.HOME).sendKeys(Keys.BACK_SPACE).build().perform();
			TN_RunnerClass.FL_driver.findElement(Locators.perDayFee).sendKeys(TN_PropertyWare.lateFeeChargePerDay);
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
			InsertDataIntoDatabase.notAutomatedFields(RunnerClass.leaseName, "Late Charges - Late Charge Fee Per Day"+'\n');
			//temp=1;
		}
        //Per Day Fee Dropdown
		Thread.sleep(2000);
		try
		{
		Select perDayFeeDropdown = new Select(TN_RunnerClass.FL_driver.findElement(Locators.perDayFeeDropdown)) ;
		perDayFeeDropdown.selectByVisibleText("Fixed Amount");
		}
		catch(Exception e)
		{
			InsertDataIntoDatabase.notAutomatedFields(RunnerClass.leaseName, "Late Charges - Late Charge Fee Per Day Dropdown"+'\n');
			//temp=1;
		}
		if(TN_RunnerClass.pdfFormatType.equalsIgnoreCase("Format1"))
	    {
		
			//Maximum
			Thread.sleep(2000);
			try
			{
			Select maximumDropdown = new Select(TN_RunnerClass.FL_driver.findElement(Locators.maximumYesNoDropdown)) ;
			maximumDropdown.selectByVisibleText("Yes");
			}
			catch(Exception e)
			{
				InsertDataIntoDatabase.notAutomatedFields(RunnerClass.leaseName, "Late Charges - Late Fee Maximum dropdown"+'\n');
				//temp=1;
			}
			// Additional Late charges Limit
			
			String maximumLimitDropdown = "";
			if(TN_PropertyWare.additionalLateChargesLimit.contains("30"))
			{
				TN_PropertyWare.additionalLateChargesLimit = "12";
			    maximumLimitDropdown = "% of Rent Charges";
			}
			else
				maximumLimitDropdown = "Fixed Amount";
			Thread.sleep(2000);
			try
			{
				if(TN_PropertyWare.additionalLateChargesLimit.equalsIgnoreCase("Error"))
				{
					InsertDataIntoDatabase.notAutomatedFields(RunnerClass.leaseName, "Late Charges - Late Charge Fee Limit"+'\n');
					//temp=1;
				}
				else
				{
					TN_RunnerClass.FL_driver.findElement(Locators.maximumDatField).sendKeys(Keys.chord(Keys.CONTROL,"a", Keys.DELETE));
					TN_RunnerClass.FL_driver.findElement(Locators.maximumDatField).clear();
					TN_RunnerClass.FL_driver.findElement(Locators.maximumDatField).click();
					Thread.sleep(1000);
					//TN_PropertyWare.clearTextField();
					//TN_RunnerClass.FL_actions.click(TN_RunnerClass.FL_driver.findElement(Locators.maximumDatField)).sendKeys(Keys.SHIFT).sendKeys(Keys.HOME).sendKeys(Keys.BACK_SPACE).build().perform();
					TN_RunnerClass.FL_driver.findElement(Locators.maximumDatField).sendKeys(TN_PropertyWare.additionalLateChargesLimit);
				}
			}
			catch(Exception e)
			{
				InsertDataIntoDatabase.notAutomatedFields(RunnerClass.leaseName, "Late Charges - Late Charge Fee Limit"+'\n');
				//temp=1;
			}
			Thread.sleep(2000);
			try
			{
			Select maximumDropdown2 = new Select(TN_RunnerClass.FL_driver.findElement(Locators.maximumDropdown2)) ;
			maximumDropdown2.selectByVisibleText(maximumLimitDropdown);
			}
			catch(Exception e)
			{
				InsertDataIntoDatabase.notAutomatedFields(RunnerClass.leaseName, "Late Charges - Maximum Limit Dropdown 2"+'\n');
				//temp=1;
			}
	    }
	}
	
		
		public static boolean lateFeeRuleMethod(String type) throws Exception
		{
			
			//if(TN_PropertyWare.lateFeeType =="GreaterOfFlatFeeOrPercentage")
			//{
				try
				{
			    TN_RunnerClass.FL_actions.moveToElement(TN_RunnerClass.FL_driver.findElement(Locators.lateFeeType)).build().perform();
				Select feeType = new Select(TN_RunnerClass.FL_driver.findElement(Locators.lateFeeType));
				feeType.selectByVisibleText("Initial Fee + Per Day Fee");
				}
				catch(Exception e)
				{
					InsertDataIntoDatabase.notAutomatedFields(RunnerClass.leaseName, "Late Charges - Late Fee Rule"+'\n');
					return false;
				}
				Thread.sleep(2000);
				//Late Charges
//				Thread.sleep(2000);
				try
				{
				if(TN_PropertyWare.additionalLateChargesLimit.contains("375"))
				{
					TN_PropertyWare.lateChargeDay = "2";
				}
				else TN_PropertyWare.lateChargeDay = String.valueOf(TN_PropertyWare.lateChargeDay.trim().charAt(0));
				}
				catch(Exception e) {}
				try
				{
					if(TN_PropertyWare.lateChargeDay.equalsIgnoreCase("Error"))
					{
						InsertDataIntoDatabase.notAutomatedFields(RunnerClass.leaseName, "Late Charges - Late Charge Day"+'\n');
						//temp=1;
					}
					else
					{
					TN_RunnerClass.FL_actions.moveToElement(TN_RunnerClass.FL_driver.findElement(Locators.lateFeeDueDay)).build().perform();
					Select dueDayList = new Select(TN_RunnerClass.FL_driver.findElement(Locators.lateFeeDueDay)) ;
					dueDayList.selectByVisibleText(TN_PropertyWare.lateChargeDay.trim());
					}
				}
				catch(Exception e)
				{
					InsertDataIntoDatabase.notAutomatedFields(RunnerClass.leaseName, "Late Charges - Late Charge Day"+'\n');
					//temp=1;
				}
				// Initial Fee
				Thread.sleep(2000);
				try
				{
					if(TN_PropertyWare.lateChargeFee.equalsIgnoreCase("Error"))
					{
						InsertDataIntoDatabase.notAutomatedFields(RunnerClass.leaseName, "Late Charges - Late Charge Fee"+'\n');
						//temp=1;
					}
					else
					{
					TN_RunnerClass.FL_actions.moveToElement(TN_RunnerClass.FL_driver.findElement(Locators.initialFee)).build().perform();
					TN_RunnerClass.FL_driver.findElement(Locators.initialFee).click();
					Thread.sleep(1000);
					TN_RunnerClass.FL_driver.findElement(Locators.initialFee).sendKeys(Keys.chord(Keys.CONTROL,"a", Keys.DELETE));
					//TN_PropertyWare.clearTextField();
					//TN_RunnerClass.FL_actions.click(TN_RunnerClass.FL_driver.findElement(Locators.initialFee)).sendKeys(Keys.SHIFT).sendKeys(Keys.HOME).sendKeys(Keys.BACK_SPACE).build().perform();
					TN_RunnerClass.FL_driver.findElement(Locators.initialFee).sendKeys(TN_PropertyWare.lateChargeFee);
					}
				}
				catch(Exception e)
				{
					InsertDataIntoDatabase.notAutomatedFields(RunnerClass.leaseName, "Late Charges - Late Charge Fee"+'\n');
					//temp=1;
				}
				Thread.sleep(2000);
				//Initial Fee Dropdown
				
				try
				{
					if(TN_PropertyWare.lateChargeFee.contains("%"))
					{
					Select initialDropdown = new Select(TN_RunnerClass.FL_driver.findElement(Locators.initialFeeDropdown)) ;
					initialDropdown.selectByVisibleText("% of Rent Charges");
					}
					else 
					{
						Select initialDropdown = new Select(TN_RunnerClass.FL_driver.findElement(Locators.initialFeeDropdown)) ;
						initialDropdown.selectByVisibleText("Fixed Amount");
					}
				}
				catch(Exception e)
				{
					InsertDataIntoDatabase.notAutomatedFields(RunnerClass.leaseName, "Late Charges - Initial fee Dropdown"+'\n');
					//temp=1;
				}
				
				//Per Day Fee
				Thread.sleep(2000);
				try
				{
					if(TN_PropertyWare.lateFeeChargePerDay.equalsIgnoreCase("Error"))
					{
						InsertDataIntoDatabase.notAutomatedFields(RunnerClass.leaseName, "Late Charges - Late Charge Fee Per Day"+'\n');
						//temp=1;
					}
					else
					{
					TN_RunnerClass.FL_actions.moveToElement(TN_RunnerClass.FL_driver.findElement(Locators.perDayFee)).build().perform();
					TN_RunnerClass.FL_driver.findElement(Locators.perDayFee).click();
					Thread.sleep(1000);
					TN_RunnerClass.FL_driver.findElement(Locators.perDayFee).sendKeys(Keys.chord(Keys.CONTROL,"a", Keys.DELETE));
					//TN_PropertyWare.clearTextField();
					//TN_RunnerClass.FL_actions.click(TN_RunnerClass.FL_driver.findElement(Locators.perDayFee)).sendKeys(Keys.SHIFT).sendKeys(Keys.HOME).sendKeys(Keys.BACK_SPACE).build().perform();
					TN_RunnerClass.FL_driver.findElement(Locators.perDayFee).sendKeys(TN_PropertyWare.lateFeeChargePerDay);
					}
				}
				catch(Exception e)
				{
					e.printStackTrace();
					InsertDataIntoDatabase.notAutomatedFields(RunnerClass.leaseName, "Late Charges - Late Charge Fee Per Day"+'\n');
					//temp=1;
				}
	            //Per Day Fee Dropdown
				Thread.sleep(2000);
				try
				{
				Select perDayFeeDropdown = new Select(TN_RunnerClass.FL_driver.findElement(Locators.perDayFeeDropdown)) ;
				perDayFeeDropdown.selectByVisibleText("Fixed Amount");
				}
				catch(Exception e)
				{
					InsertDataIntoDatabase.notAutomatedFields(RunnerClass.leaseName, "Late Charges - Late Charge Fee Per Day Dropdown"+'\n');
					//temp=1;
				}
				if(TN_RunnerClass.pdfFormatType.equalsIgnoreCase("Format1"))
			    {
				
					//Maximum
					Thread.sleep(2000);
					try
					{
					Select maximumDropdown = new Select(TN_RunnerClass.FL_driver.findElement(Locators.maximumYesNoDropdown)) ;
					maximumDropdown.selectByVisibleText("Yes");
					}
					catch(Exception e)
					{
						InsertDataIntoDatabase.notAutomatedFields(RunnerClass.leaseName, "Late Charges - Late Fee Maximum dropdown"+'\n');
						//temp=1;
					}
					// Additional Late charges Limit
					
					String maximumLimitDropdown = "";
					if(TN_PropertyWare.additionalLateChargesLimit.contains("30"))
					{
						TN_PropertyWare.additionalLateChargesLimit = "12";
					    maximumLimitDropdown = "% of Rent Charges";
					}
					else
						maximumLimitDropdown = "Fixed Amount";
					Thread.sleep(2000);
					try
					{
						if(TN_PropertyWare.additionalLateChargesLimit.equalsIgnoreCase("Error"))
						{
							InsertDataIntoDatabase.notAutomatedFields(RunnerClass.leaseName, "Late Charges - Late Charge Fee Limit"+'\n');
							//temp=1;
						}
						else
						{
							TN_RunnerClass.FL_driver.findElement(Locators.maximumDatField).sendKeys(Keys.chord(Keys.CONTROL,"a", Keys.DELETE));
							TN_RunnerClass.FL_driver.findElement(Locators.maximumDatField).clear();
							TN_RunnerClass.FL_driver.findElement(Locators.maximumDatField).click();
							Thread.sleep(1000);
							//TN_PropertyWare.clearTextField();
							//TN_RunnerClass.FL_actions.click(TN_RunnerClass.FL_driver.findElement(Locators.maximumDatField)).sendKeys(Keys.SHIFT).sendKeys(Keys.HOME).sendKeys(Keys.BACK_SPACE).build().perform();
							TN_RunnerClass.FL_driver.findElement(Locators.maximumDatField).sendKeys(TN_PropertyWare.additionalLateChargesLimit);
						}
					}
					catch(Exception e)
					{
						InsertDataIntoDatabase.notAutomatedFields(RunnerClass.leaseName, "Late Charges - Late Charge Fee Limit"+'\n');
						//temp=1;
					}
					Thread.sleep(2000);
					try
					{
					Select maximumDropdown2 = new Select(TN_RunnerClass.FL_driver.findElement(Locators.maximumDropdown2)) ;
					maximumDropdown2.selectByVisibleText(maximumLimitDropdown);
					}
					catch(Exception e)
					{
						InsertDataIntoDatabase.notAutomatedFields(RunnerClass.leaseName, "Late Charges - Maximum Limit Dropdown 2"+'\n');
						//temp=1;
					}
			    }
				Thread.sleep(2000);
				return true;
		}
}
