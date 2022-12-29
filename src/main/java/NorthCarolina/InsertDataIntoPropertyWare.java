package NorthCarolina;

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

public class InsertDataIntoPropertyWare
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
		String query = "Update automation.ChargeCodesConfiguration Set Amount=NULL,StartDate=NULL,EndDate=NULL,MoveINCharge=NULL,autoCharge=NULL,AutoCharge_StartDate=NULL";
		InsertDataIntoDatabase.updateTable(query);

		//Check if Prorated Rent Date is in Move in Date month
		NC_PropertyWare.proratedRentDateIsInMoveInMonthFlag =  NC_PropertyWare.checkProratedRentDateIsInMoveInMonth(); 
		System.out.println("Prorated Rent is in move in month = "+NC_PropertyWare.proratedRentDateIsInMoveInMonthFlag);
		if(NC_PropertyWare.proratedRentDateIsInMoveInMonthFlag==true)
		prepaymentChargeOrMonthlyRent = "2";
		else 
		prepaymentChargeOrMonthlyRent = "9";
		//If Prorated Rent date is move in Month and Portfolio type is MCH
				if(NC_PropertyWare.proratedRentDateIsInMoveInMonthFlag==true&&NC_PropertyWare.portfolioType=="MCH")
					prepaymentChargeOrMonthlyRent = "9";
		// Assign Charge codes based on conditions (Portfolio, Company etc)
		int temp=0;
		//If Consession addendum is available, skip Rents and Prorated Rents
		if(NC_PropertyWare.portfolioType=="Others")
		{
			if(NC_PropertyWare.concessionAddendumFlag==true)
			{
				InsertDataIntoPropertyWare.OtherPortfolios_chargesWhenConsessionAddendumIsAvailable();
			}
			else
			{
				InsertDataIntoPropertyWare.OtherPortfolios_chargesWhenConsessionAddendumIsNotAvailable();
			}
		}
		//MCH type
		if(NC_PropertyWare.portfolioType=="MCH")
		{
			if(NC_PropertyWare.concessionAddendumFlag==true)
			{
				InsertDataIntoPropertyWare.MCHPortfolios_chargesWhenConsessionAddendumIsAvailable();
			}
			else
			{
				InsertDataIntoPropertyWare.MCHPortfolios_chargesWhenConsessionAddendumIsNotAvailable();
			}
		}
		//Update other fields for charges
		InsertDataIntoPropertyWare.updateOtherFieldsInConfigurationTable();
		
		try
		{
		NC_RunnerClass.FL_driver.navigate().refresh();
		NC_RunnerClass.FL_driver.findElement(Locators.ledgerTab).click();
		}
		catch(Exception e)
		{
			try
			{
			if(NC_RunnerClass.FL_driver.findElement(Locators.popUpAfterClickingLeaseName).isDisplayed())
			{
				NC_RunnerClass.FL_driver.findElement(Locators.popupClose).click();
				//NC_RunnerClass.FL_driver.navigate().refresh();
				NC_RunnerClass.FL_js.executeScript("window.scrollBy(document.body.scrollHeight,0)");
				NC_RunnerClass.FL_driver.findElement(Locators.ledgerTab).click();
			}
			}
			catch(Exception e2) {}
		}
		//Check if Concession Addendum is available and update NotAutomatedField if it is available
		if(NC_PropertyWare.concessionAddendumFlag==true)
		{
			InsertDataIntoDatabase.notAutomatedFields(RunnerClass.leaseName, "Concession Addendum is available"+'\n');
		}
		
		 System.out.println("------Move In Charges-------");
		// Get Move in charges
		 try
		 {
		GetDataFromDB.getMoveInCharges();
		for(int i=0;i<moveInCharges.length;i++)
		{
			System.out.println(moveInCharges[i][0]+"   "+moveInCharges[i][1]+"   "+moveInCharges[i][2]+"   "+moveInCharges[i][3]+"  "+moveInCharges[i][4]);
			int flagToCheckIfMoveInChargeAlreadyAvailable =0;
			NC_RunnerClass.FL_driver.manage().timeouts().implicitlyWait(5,TimeUnit.SECONDS);
			NC_RunnerClass.FL_wait = new WebDriverWait(NC_RunnerClass.FL_driver, Duration.ofSeconds(5));
			//Check if the Move In Charge is already available
			
			try
			{
			List<WebElement> existingMoveInCharges_ChargeCodes = NC_RunnerClass.FL_driver.findElements(Locators.moveInCharges_List);
			List<WebElement> existingMoveInCharges_Amount = NC_RunnerClass.FL_driver.findElements(Locators.moveInCharge_List_Amount);
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
				if(moveInCharges[i][4]==null||moveInCharges[i][4].equalsIgnoreCase("n/a")||moveInCharges[i][4]=="Error"||RunnerClass.onlyDigits(moveInCharges[i][4].replace(",", "").replace(".", ""))==false)
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
			
			NC_RunnerClass.FL_driver.findElement(Locators.newCharge).click();
			Thread.sleep(2000);
			//Account code
			Select AutoChargesDropdown = new Select(NC_RunnerClass.FL_driver.findElement(Locators.accountDropdown));
			AutoChargesDropdown.selectByVisibleText(moveInCharges[i][1]);
			//Reference
			Thread.sleep(2000);
			NC_RunnerClass.FL_driver.findElement(Locators.referenceName).sendKeys(moveInCharges[i][2]);
			Thread.sleep(2000);
			//Amount
			NC_RunnerClass.FL_driver.findElement(Locators.moveInChargeAmount).click();
			NC_RunnerClass.FL_actions.sendKeys(Keys.BACK_SPACE).sendKeys(Keys.BACK_SPACE).sendKeys(Keys.BACK_SPACE).sendKeys(Keys.BACK_SPACE).sendKeys(Keys.BACK_SPACE).build().perform();
			Thread.sleep(2000);
			NC_RunnerClass.FL_driver.findElement(Locators.moveInChargeAmount).sendKeys(moveInCharges[i][4]); 
			Thread.sleep(2000);
			//Start Date
			NC_RunnerClass.FL_driver.findElement(Locators.moveInChargeDate).clear();
			Thread.sleep(2000);
			NC_RunnerClass.FL_driver.findElement(Locators.moveInChargeDate).sendKeys(moveInCharges[i][3]);
			//Save or Cancel button
			Thread.sleep(2000);
			if(RunnerClass.saveButtonOnAndOff==false)
			NC_RunnerClass.FL_driver.findElement(Locators.moveInChargeCancel).click();
			else 
			NC_RunnerClass.FL_driver.findElement(Locators.moveInChargeSaveButton).click();
			Thread.sleep(2000);
			try
			{
				if(NC_RunnerClass.FL_driver.findElement(Locators.somethingWrongInSavingCharge).isDisplayed())
				{
					NC_RunnerClass.FL_driver.findElement(Locators.moveInChargeCancel).click();
				}
				
			}
			catch(Exception e)
			{}
			NC_RunnerClass.FL_driver.navigate().refresh();
		}
		 }
		 catch(Exception e) 
		 {
			 e.printStackTrace();
			 System.out.println("Something went wrong in Move In charges");
		 }
		
		// Get Auto charges
	     GetDataFromDB.getAutoCharges();
	     System.out.println("------Auto Charges-------");
			// Auto Charges
			Thread.sleep(3000);
			try
			{
			NC_RunnerClass.FL_driver.findElement(Locators.summaryTab).click();
			}
			catch(Exception e)
			{
				NC_RunnerClass.FL_driver.navigate().refresh();
				NC_RunnerClass.FL_driver.findElement(Locators.summaryTab).click();
			}
			Thread.sleep(5000);
			NC_RunnerClass.FL_driver.findElement(Locators.summaryEditButton).click();
			NC_RunnerClass.FL_actions.moveToElement(NC_RunnerClass.FL_driver.findElement(Locators.newAutoCharge)).build().perform();
			
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
				List<WebElement> existingAutoCharges = NC_RunnerClass.FL_driver.findElements(Locators.autoCharge_List);
				List<WebElement> existingAutoChargeAmounts = NC_RunnerClass.FL_driver.findElements(Locators.autoCharge_List_Amounts);
				
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
				NC_RunnerClass.FL_driver.findElement(Locators.newAutoCharge).click();
				}
				catch(Exception e)
				{
					NC_RunnerClass.FL_driver.findElement(Locators.autoCharge_CancelButton).click();
					
				}
				Thread.sleep(2000);
				
				//Charge Code
				Select autoChargesDropdown = new Select(NC_RunnerClass.FL_driver.findElement(Locators.accountDropdown));
				autoChargesDropdown.selectByVisibleText(autoCharges[i][1]);
				
				//Start Date
				NC_RunnerClass.FL_driver.findElement(Locators.autoCharge_StartDate).clear();
				Thread.sleep(2000);
				NC_RunnerClass.FL_driver.findElement(Locators.autoCharge_StartDate).sendKeys(autoCharges[i][3]);
				
				//click this to hide calendar UI
				NC_RunnerClass.FL_driver.findElement(Locators.autoCharge_refField).click();
				//Amount
				NC_RunnerClass.FL_driver.findElement(Locators.autoCharge_Amount).click();
				NC_RunnerClass.FL_actions.sendKeys(Keys.BACK_SPACE).sendKeys(Keys.BACK_SPACE).sendKeys(Keys.BACK_SPACE).sendKeys(Keys.BACK_SPACE).sendKeys(Keys.BACK_SPACE).build().perform();
				NC_RunnerClass.FL_driver.findElement(Locators.autoCharge_Amount).sendKeys(autoCharges[i][4]);
				Thread.sleep(2000);
				
				//Description
				NC_RunnerClass.FL_driver.findElement(Locators.autoCharge_Description).sendKeys(autoCharges[i][2]);
				
				//End Date
				if(autoCharges[i][5]!=null&&autoCharges[i][5]!="") //NC_PropertyWare.portfolioType=="Others"&&
				{
				NC_RunnerClass.FL_driver.findElement(Locators.autoCharge_EndDate).clear();
				Thread.sleep(2000);
				NC_RunnerClass.FL_driver.findElement(Locators.autoCharge_EndDate).sendKeys(autoCharges[i][5]);
				}
				
				try
				{
					NC_RunnerClass.FL_driver.findElement(By.xpath("//*[text()='New Auto Charge']")).click();
				}
				catch(Exception e) {}
				//Save and Cancel
				Thread.sleep(2000);
				if(RunnerClass.saveButtonOnAndOff==false)
				NC_RunnerClass.FL_driver.findElement(Locators.autoCharge_CancelButton).click();
				else
				NC_RunnerClass.FL_driver.findElement(Locators.autoCharge_SaveButton).click();
				Thread.sleep(2000);
				try
				{
					if(NC_RunnerClass.FL_driver.findElement(Locators.somethingWrongInSavingCharge).isDisplayed())
					{
						NC_RunnerClass.FL_driver.findElement(Locators.moveInChargeCancel).click();
					}
					
				}
				catch(Exception e)
				{}
			}
			
			//Other Fields
            Thread.sleep(4000);
			
			// RC Field
		/*	try
			{
				if(NC_PropertyWare.RCDetails.equalsIgnoreCase("Error"))
				{
					InsertDataIntoDatabase.notAutomatedFields(RunnerClass.leaseName, "RC Details"+'\n');
					temp=1;
				}
				else
				{
				NC_RunnerClass.FL_actions.moveToElement(NC_RunnerClass.FL_driver.findElement(Locators.rcField)).build().perform();
				NC_RunnerClass.FL_driver.findElement(Locators.rcField).clear();
				Thread.sleep(1000);
				NC_RunnerClass.FL_driver.findElement(Locators.rcField).sendKeys(NC_PropertyWare.RCDetails);
				}
			}
			catch(Exception e)
			{
				InsertDataIntoDatabase.notAutomatedFields(RunnerClass.leaseName, "RC Details"+'\n');
				temp=1;
			}
			//Early Termination
			Thread.sleep(2000);
			
			try
			{
				if(NC_PropertyWare.earlyTermination.equalsIgnoreCase("Error"))
				{
					InsertDataIntoDatabase.notAutomatedFields(RunnerClass.leaseName, "Early Termination"+'\n');
					temp=1;
				}
				else
				{
				if(NC_PropertyWare.earlyTermination.contains("2"))
				{
					NC_RunnerClass.FL_actions.moveToElement(NC_RunnerClass.FL_driver.findElement(Locators.earlyTermFee2x)).build().perform();
					NC_RunnerClass.FL_driver.findElement(Locators.earlyTermFee2x).click();
					Select earlyTermination_List = new Select(NC_RunnerClass.FL_driver.findElement(Locators.earlyTermination_List));
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
			}*/
			if(NC_PropertyWare.residentBenefitsPackageAvailabilityCheck==true)
			{
				if(NC_PropertyWare.residentBenefitsPackage!="Error")
				{
				Thread.sleep(2000);
				try
				{
				NC_RunnerClass.FL_actions.moveToElement(NC_RunnerClass.FL_driver.findElement(Locators.residentBenefitsPackage)).build().perform();
				NC_RunnerClass.FL_driver.findElement(Locators.residentBenefitsPackage).click();
				Select residentBenefitsPackageList = new Select(NC_RunnerClass.FL_driver.findElement(Locators.residentBenefitsPackage));
				//if(NC_PropertyWare.HVACFilterFlag==false)
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
				if(NC_PropertyWare.airFilterFee!="Error")
				{
				Thread.sleep(2000);
				try
				{
				NC_RunnerClass.FL_actions.moveToElement(NC_RunnerClass.FL_driver.findElement(Locators.enrolledInFilterEasy)).build().perform();
				NC_RunnerClass.FL_driver.findElement(Locators.enrolledInFilterEasy).click();
				Select enrolledInFilterEasyList = new Select(NC_RunnerClass.FL_driver.findElement(Locators.enrolledInFilterEasy_List));
				if(NC_PropertyWare.HVACFilterFlag==false)
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
			NC_RunnerClass.FL_actions.moveToElement(NC_RunnerClass.FL_driver.findElement(Locators.needsNewLease)).build().perform();
			NC_RunnerClass.FL_driver.findElement(Locators.needsNewLease).click();
			Select needsNewLease_List = new Select(NC_RunnerClass.FL_driver.findElement(Locators.needsNewLease_List));
			needsNewLease_List.selectByVisibleText("NO");
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
				if(NC_PropertyWare.occupants.equalsIgnoreCase("Error"))
				{
					InsertDataIntoDatabase.notAutomatedFields(RunnerClass.leaseName, "Lease Occupants"+'\n');
					temp=1;
				}
				else
				{
				NC_RunnerClass.FL_actions.moveToElement(NC_RunnerClass.FL_driver.findElement(Locators.leaseOccupants)).build().perform();
				NC_RunnerClass.FL_driver.findElement(Locators.leaseOccupants).clear();
				Thread.sleep(1000);
				NC_RunnerClass.FL_driver.findElement(Locators.leaseOccupants).sendKeys(NC_PropertyWare.occupants);
				}
			}
			catch(Exception e)
			{
				InsertDataIntoDatabase.notAutomatedFields(RunnerClass.leaseName, "Lease Occupants"+'\n');
				temp=1;
			}
			if(NC_PropertyWare.petFlag==true)
			{
			//pet information
				Thread.sleep(2000);
				
				//Pet Type
				String petType = String.join(",", NC_PropertyWare.petType);
				try
				{
					NC_RunnerClass.FL_actions.moveToElement(NC_RunnerClass.FL_driver.findElement(Locators.pet1Type)).build().perform();
					NC_RunnerClass.FL_driver.findElement(Locators.pet1Type).clear();
					Thread.sleep(1000);
					NC_RunnerClass.FL_driver.findElement(Locators.pet1Type).sendKeys(petType);
				}
				catch(Exception e)
				{
					InsertDataIntoDatabase.notAutomatedFields(RunnerClass.leaseName, "Pet Types"+'\n');
					temp=1;
				}
				Thread.sleep(2000);
				//Pet Breed
				String petBreed = String.join(",", NC_PropertyWare.petBreed);
				try
				{
					NC_RunnerClass.FL_actions.moveToElement(NC_RunnerClass.FL_driver.findElement(Locators.pet1Breed)).build().perform();
					NC_RunnerClass.FL_driver.findElement(Locators.pet1Breed).clear();
					Thread.sleep(1000);
					NC_RunnerClass.FL_driver.findElement(Locators.pet1Breed).sendKeys(petBreed);
				}
				catch(Exception e)
				{
					InsertDataIntoDatabase.notAutomatedFields(RunnerClass.leaseName, "Pet Breed"+'\n');
					temp=1;
				}
				
				//Pet Weight
				String petWeight = String.join(",", NC_PropertyWare.petWeight);
				try
				{
					NC_RunnerClass.FL_actions.moveToElement(NC_RunnerClass.FL_driver.findElement(Locators.pet1Weight)).build().perform();
					NC_RunnerClass.FL_driver.findElement(Locators.pet1Weight).clear();
					Thread.sleep(1000);
					NC_RunnerClass.FL_driver.findElement(Locators.pet1Weight).sendKeys(petWeight);
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
					if(NC_PropertyWare.petRent.equalsIgnoreCase("Error"))
					{
						InsertDataIntoDatabase.notAutomatedFields(RunnerClass.leaseName, "pet Rent"+'\n');
						temp=1;
					}
					else
					{
						try
						{
					NC_RunnerClass.FL_actions.moveToElement(NC_RunnerClass.FL_driver.findElement(Locators.petAmount)).build().perform();
					//NC_RunnerClass.FL_driver.findElement(Locators.petAmount).clear();
					NC_RunnerClass.FL_driver.findElement(Locators.petAmount).click();
					//NC_PropertyWare.clearTextField();
					NC_RunnerClass.FL_driver.findElement(Locators.petAmount).sendKeys(Keys.chord(Keys.CONTROL,"a", Keys.DELETE));
					Thread.sleep(1000);
					//NC_RunnerClass.FL_actions.click(NC_RunnerClass.FL_driver.findElement(Locators.petAmount)).sendKeys(Keys.SHIFT).sendKeys(Keys.HOME).sendKeys(Keys.BACK_SPACE).build().perform();
					NC_RunnerClass.FL_driver.findElement(Locators.petAmount).sendKeys(NC_PropertyWare.petRent);
						}
						catch(Exception e)
						{
							NC_RunnerClass.FL_actions.moveToElement(NC_RunnerClass.FL_driver.findElement(Locators.petAmount2)).build().perform();
							//NC_RunnerClass.FL_driver.findElement(Locators.petAmount).clear();
							NC_RunnerClass.FL_driver.findElement(Locators.petAmount2).click();
							//NC_PropertyWare.clearTextField();
							NC_RunnerClass.FL_driver.findElement(Locators.petAmount2).sendKeys(Keys.chord(Keys.CONTROL,"a", Keys.DELETE));
							Thread.sleep(1000);
							//NC_RunnerClass.FL_actions.click(NC_RunnerClass.FL_driver.findElement(Locators.petAmount)).sendKeys(Keys.SHIFT).sendKeys(Keys.HOME).sendKeys(Keys.BACK_SPACE).build().perform();
							NC_RunnerClass.FL_driver.findElement(Locators.petAmount2).sendKeys(NC_PropertyWare.petRent);
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
					if(NC_PropertyWare.petOneTimeNonRefundableFee.equalsIgnoreCase("Error"))
					{
						InsertDataIntoDatabase.notAutomatedFields(RunnerClass.leaseName, "pet One Time Non-Refundable Fee"+'\n');
						temp=1;
					}
					else
					{
						try
						{
					NC_RunnerClass.FL_actions.moveToElement(NC_RunnerClass.FL_driver.findElement(Locators.tenantOneTimePetFee)).build().perform();
					NC_RunnerClass.FL_driver.findElement(Locators.tenantOneTimePetFee).click();
					Thread.sleep(1000);
					NC_RunnerClass.FL_driver.findElement(Locators.tenantOneTimePetFee).sendKeys(Keys.chord(Keys.CONTROL,"a", Keys.DELETE));
					//NC_PropertyWare.clearTextField();
					//NC_RunnerClass.FL_actions.click(NC_RunnerClass.FL_driver.findElement(Locators.tenantOneTimePetFee)).sendKeys(Keys.SHIFT).sendKeys(Keys.HOME).sendKeys(Keys.BACK_SPACE).build().perform();
					NC_RunnerClass.FL_driver.findElement(Locators.tenantOneTimePetFee).sendKeys(NC_PropertyWare.petOneTimeNonRefundableFee);
						}
						catch(Exception e)
						{
							NC_RunnerClass.FL_actions.moveToElement(NC_RunnerClass.FL_driver.findElement(Locators.tenantOneTimePetFee2)).build().perform();
							NC_RunnerClass.FL_driver.findElement(Locators.tenantOneTimePetFee2).click();
							Thread.sleep(1000);
							NC_RunnerClass.FL_driver.findElement(Locators.tenantOneTimePetFee2).sendKeys(Keys.chord(Keys.CONTROL,"a", Keys.DELETE));
							//NC_PropertyWare.clearTextField();
							//NC_RunnerClass.FL_actions.click(NC_RunnerClass.FL_driver.findElement(Locators.tenantOneTimePetFee)).sendKeys(Keys.SHIFT).sendKeys(Keys.HOME).sendKeys(Keys.BACK_SPACE).build().perform();
							NC_RunnerClass.FL_driver.findElement(Locators.tenantOneTimePetFee2).sendKeys(NC_PropertyWare.petOneTimeNonRefundableFee);
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
				if(NC_PropertyWare.serviceAnimalFlag==true)
				{
					Thread.sleep(2000);
					
					//Pet Type
					String ServiceAnimal_petType = String.join(",", NC_PropertyWare.serviceAnimalPetType);
					try
					{
						NC_RunnerClass.FL_actions.moveToElement(NC_RunnerClass.FL_driver.findElement(Locators.serviceAnimal_pet2Type)).build().perform();
						NC_RunnerClass.FL_driver.findElement(Locators.serviceAnimal_pet2Type).clear();
						Thread.sleep(1000);
						NC_RunnerClass.FL_driver.findElement(Locators.serviceAnimal_pet2Type).sendKeys("Service "+ServiceAnimal_petType);
					}
					catch(Exception e)
					{
						InsertDataIntoDatabase.notAutomatedFields(RunnerClass.leaseName, "Pet 2 Types"+'\n');
						temp=1;
					}
					Thread.sleep(2000);
					//Pet Breed
					String serviceAnimal_petBreed = String.join(",", NC_PropertyWare.serviceAnimalPetBreed);
					try
					{
						NC_RunnerClass.FL_actions.moveToElement(NC_RunnerClass.FL_driver.findElement(Locators.serviceAnimal_pet2Breed)).build().perform();
						NC_RunnerClass.FL_driver.findElement(Locators.serviceAnimal_pet2Breed).clear();
						Thread.sleep(1000);
						NC_RunnerClass.FL_driver.findElement(Locators.serviceAnimal_pet2Breed).sendKeys(serviceAnimal_petBreed);
					}
					catch(Exception e)
					{
						InsertDataIntoDatabase.notAutomatedFields(RunnerClass.leaseName, "Pet 2 Breed"+'\n');
						temp=1;
					}
					
					
					Thread.sleep(2000);
					//Pet Weight
					String serviceAnimal_petWeight = String.join(",", NC_PropertyWare.serviceAnimalPetWeight);
					try
					{
						NC_RunnerClass.FL_actions.moveToElement(NC_RunnerClass.FL_driver.findElement(Locators.serviceAnimal_pet2Weight)).build().perform();
						NC_RunnerClass.FL_driver.findElement(Locators.serviceAnimal_pet2Weight).clear();
						Thread.sleep(1000);
						NC_RunnerClass.FL_driver.findElement(Locators.serviceAnimal_pet2Weight).sendKeys(serviceAnimal_petWeight);
					}
					catch(Exception e)
					{
						InsertDataIntoDatabase.notAutomatedFields(RunnerClass.leaseName, "Pet 2 Weight"+'\n');
						temp=1;
					}
					
					//Pet Special Provisions
					try
					{
						NC_RunnerClass.FL_actions.moveToElement(NC_RunnerClass.FL_driver.findElement(Locators.petSpecialProvisions)).build().perform();
						NC_RunnerClass.FL_driver.findElement(Locators.petSpecialProvisions).clear();
						Thread.sleep(1000);
						NC_RunnerClass.FL_driver.findElement(Locators.petSpecialProvisions).sendKeys("Service animals, no deposit required");
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
					//if(!NC_PropertyWare.petSecurityDeposit.equalsIgnoreCase("Error")||!NC_PropertyWare.petSecurityDeposit.trim().equalsIgnoreCase(" ")||!NC_PropertyWare.petSecurityDeposit.trim().equalsIgnoreCase(""))
					if(RunnerClass.onlyDigits(NC_PropertyWare.petSecurityDeposit.trim())==true)
					{
					NC_RunnerClass.FL_actions.moveToElement(NC_RunnerClass.FL_driver.findElement(Locators.petDepositAmount)).build().perform();
					//NC_RunnerClass.FL_driver.findElement(Locators.petAmount).clear();
					NC_RunnerClass.FL_driver.findElement(Locators.petDepositAmount).click();
					NC_RunnerClass.FL_driver.findElement(Locators.petDepositAmount).sendKeys(Keys.chord(Keys.CONTROL,"a", Keys.DELETE));
					//NC_PropertyWare.clearTextField();
					Thread.sleep(1000);
					//NC_RunnerClass.FL_actions.click(NC_RunnerClass.FL_driver.findElement(Locators.petAmount)).sendKeys(Keys.SHIFT).sendKeys(Keys.HOME).sendKeys(Keys.BACK_SPACE).build().perform();
					NC_RunnerClass.FL_driver.findElement(Locators.petDepositAmount).sendKeys(NC_PropertyWare.petSecurityDeposit);
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
				if(NC_PropertyWare.monthlyRent.equalsIgnoreCase("Error"))
				{
					InsertDataIntoDatabase.notAutomatedFields(RunnerClass.leaseName, "Intial Monthly Rent"+'\n');
					temp=1;
				}
				else
				{
				NC_RunnerClass.FL_actions.moveToElement(NC_RunnerClass.FL_driver.findElement(Locators.initialMonthlyRent)).build().perform();
				//NC_RunnerClass.FL_driver.findElement(Locators.initialMonthlyRent).clear();
				NC_RunnerClass.FL_driver.findElement(Locators.initialMonthlyRent).sendKeys(Keys.chord(Keys.CONTROL,"a", Keys.DELETE));
				NC_RunnerClass.FL_driver.findElement(Locators.initialMonthlyRent).sendKeys(NC_PropertyWare.monthlyRent);
				
				}
			}
			catch(Exception e)
			{
				InsertDataIntoDatabase.notAutomatedFields(RunnerClass.leaseName, "Intial Monthly Rent"+'\n');
				temp=1;
			}
			
			//Late Fee Rule
			InsertDataIntoPropertyWare.lateFeeRuleMethod(NC_PropertyWare.lateFeeType);
			
			Thread.sleep(2000);
			NC_RunnerClass.FL_js.executeScript("window.scrollTo(0,document.body.scrollHeight)");
			try
			{
				Thread.sleep(2000);
				if(RunnerClass.saveButtonOnAndOff==true)
				NC_RunnerClass.FL_actions.moveToElement(NC_RunnerClass.FL_driver.findElement(Locators.saveLease)).click(NC_RunnerClass.FL_driver.findElement(Locators.saveLease)).build().perform();
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
			NC_RunnerClass.FL_actions.moveToElement(NC_RunnerClass.FL_driver.findElement(Locators.saveLease)).click(NC_RunnerClass.FL_driver.findElement(Locators.saveLease)).build().perform();
			return false;
		}
			
	}
	
	public static void updateOtherFieldsInConfigurationTable() throws Exception
	{
		// Get List of Charges from table
		GetDataFromDB.getChargesFromConfigurationTable();
		String DayInCommensementDate = RunnerClass.convertDate(NC_PropertyWare.commensementDate).split("/")[1].trim();
		//Update Start date and End Date
		String firstFullMonth=null;
		String secondFullMonth = null;
		if(DayInCommensementDate.equalsIgnoreCase("01")||DayInCommensementDate.equalsIgnoreCase("1"))
		{
			firstFullMonth = RunnerClass.convertDate(NC_PropertyWare.commensementDate).trim();
			secondFullMonth = RunnerClass.firstDayOfFullMonth(RunnerClass.convertDate(NC_PropertyWare.commensementDate));
		}
		else 
		{
		firstFullMonth = RunnerClass.firstDayOfFullMonth(RunnerClass.convertDate(NC_PropertyWare.commensementDate));
		secondFullMonth = RunnerClass.NextMonthOffirstDayOfFullMonth(RunnerClass.convertDate(NC_PropertyWare.commensementDate));
		}
		String updateStartDateAndEndDate = "Update [Automation].[ChargeCodesConfiguration] Set StartDate='"+RunnerClass.convertDate(NC_PropertyWare.commensementDate)+"' where moveInCharge =1 \n"
				+ "Update [Automation].[ChargeCodesConfiguration] Set autoCharge_StartDate='"+firstFullMonth+"' where AutoCharge =1 \n"
						+ "Update [Automation].[ChargeCodesConfiguration] Set endDate='"+RunnerClass.DateModified(firstFullMonth)+"' where Charge ='Pro Rate Rent' ";
		InsertDataIntoDatabase.updateTable(updateStartDateAndEndDate);
		//If there is an increased rent, add add date to previous monthly rent in auto charges
		
		if(RunnerClass.onlyDigits(NC_PropertyWare.increasedRent_amount.trim().replace(",", "").replace(".", ""))==true)
		{
		String updateMonthlyRentStartDateToNextMonthOfFirstFullMonth = "Update [Automation].[ChargeCodesConfiguration] Set autoCharge_StartDate='"+RunnerClass.firstDayOfFullMonth(RunnerClass.convertDate(NC_PropertyWare.commensementDate))+"',EndDate ='"+RunnerClass.convertDate(NC_PropertyWare.increasedRent_previousRentEndDate.trim())+"'  where ID=2";
		InsertDataIntoDatabase.updateTable(updateMonthlyRentStartDateToNextMonthOfFirstFullMonth);
		}
		else {
		//If Prorate Rent is under 200$, Monthly Rent Start date should be next month of First Full Month
		try
		{
		if(NC_PropertyWare.portfolioType=="Others"||NC_PropertyWare.proratedRentDateIsInMoveInMonthFlag==true) //Double.parseDouble(NC_PropertyWare.proratedRent.trim())<=200.00||
		{
			String updateMonthlyRentStartDateWhenProrateRentIsUnder200Dollers = "Update [Automation].[ChargeCodesConfiguration] Set autoCharge_StartDate='"+secondFullMonth+"' where ID=2";
			InsertDataIntoDatabase.updateTable(updateMonthlyRentStartDateWhenProrateRentIsUnder200Dollers);
		}
		}
		catch(Exception e) {}
		}
		try
		{
		if(NC_PropertyWare.proratedRentDateIsInMoveInMonthFlag==true&&(NC_PropertyWare.proratedPetRent!=""||NC_PropertyWare.proratedPetRent!=null||!NC_PropertyWare.proratedPetRent.equalsIgnoreCase("na")||!NC_PropertyWare.proratedPetRent.equalsIgnoreCase("n/a"))&&(DayInCommensementDate.equalsIgnoreCase("01")||DayInCommensementDate.equalsIgnoreCase("1"))) //Double.parseDouble(NC_PropertyWare.proratedRent.trim())<=200.00||
		{
			String updateMonthlyRentStartDateWhenProrateRentIsUnder200Dollers = "Update [Automation].[ChargeCodesConfiguration] Set autoCharge_StartDate='"+secondFullMonth+"' where ID=8";
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
				query = "Update [Automation].[ChargeCodesConfiguration] Set Amount ='"+NC_PropertyWare.proratedRent+"' where charge ='Pro Rate Rent'";
				//InsertDataIntoDatabase.updateTable(query1);
				continue;
				
			case "Monthly Rent":
				query = query+"\nUpdate [Automation].[ChargeCodesConfiguration] Set Amount ='"+NC_PropertyWare.monthlyRent+"' where charge ='Monthly Rent'";
				//InsertDataIntoDatabase.updateTable(query2);
				continue;
			case "Tenant Admin Revenue":
				query = query+"\nUpdate [Automation].[ChargeCodesConfiguration] Set Amount ='"+NC_PropertyWare.adminFee+"' where charge ='Tenant Admin Revenue'";
				//InsertDataIntoDatabase.updateTable(query3);
				continue;
			case "Pro Rated Pet Rent":
				query = query+"\nUpdate [Automation].[ChargeCodesConfiguration] Set Amount ='"+NC_PropertyWare.proratedPetRent+"' where charge ='Pro Rated Pet Rent'";
				//InsertDataIntoDatabase.updateTable(query4);
				continue;
			case "Pet Security Deposit":
				query = query+"\nUpdate [Automation].[ChargeCodesConfiguration] Set Amount ='"+NC_PropertyWare.petSecurityDeposit+"' where charge ='Pet Security Deposit'";
				//InsertDataIntoDatabase.updateTable(query5);
				continue;
			case "Pet One Time Non Refundable":
				query = query+"\nUpdate [Automation].[ChargeCodesConfiguration] Set Amount ='"+NC_PropertyWare.petOneTimeNonRefundableFee+"' where charge ='Pet One Time Non Refundable'";
				//InsertDataIntoDatabase.updateTable(query6);
				continue;
			case "HVAC Filter Fee":
				query = query+"\nUpdate [Automation].[ChargeCodesConfiguration] Set Amount ='"+NC_PropertyWare.airFilterFee+"' where charge ='HVAC Filter Fee'";
				//InsertDataIntoDatabase.updateTable(query7);
				continue;
			case "Pet Rent":
				query = query+"\nUpdate [Automation].[ChargeCodesConfiguration] Set Amount ='"+NC_PropertyWare.petRent+"' where charge ='Pet Rent'";
				//InsertDataIntoDatabase.updateTable(query8);
				continue;
			case "Pre Payment Charge":
				query = query+"\nUpdate [Automation].[ChargeCodesConfiguration] Set Amount ='"+NC_PropertyWare.prepaymentCharge+"' where charge ='Pre Payment Charge'";
				//InsertDataIntoDatabase.updateTable(query9);
				continue;
			case "Increased Rent":
				try {
				query = query+"\nUpdate [Automation].[ChargeCodesConfiguration] Set Amount ='"+NC_PropertyWare.increasedRent_amount+"',autoCharge_StartDate ='"+RunnerClass.convertDate(NC_PropertyWare.increasedRent_newStartDate)+"' where charge ='Increased Rent'";
				//InsertDataIntoDatabase.updateTable(query10);
				}
				catch(Exception e) {}
				continue;
			case "Resident Benefits Package":
				query = query+"\nUpdate [Automation].[ChargeCodesConfiguration] Set Amount ='"+NC_PropertyWare.residentBenefitsPackage+"',startDate ='"+RunnerClass.convertDate(NC_PropertyWare.commensementDate).trim()+"',autoCharge_startDate='"+firstFullMonth+"'  where charge ='Resident Benefits Package'";
				//InsertDataIntoDatabase.updateTable(query9);
				continue;
			}
			
		}
		InsertDataIntoDatabase.updateTable(query);
	}
	public static void OtherPortfolios_chargesWhenConsessionAddendumIsAvailable()
	{
		if(NC_PropertyWare.portfolioType=="Others"&&NC_PropertyWare.petFlag==false)
		{
			if(NC_PropertyWare.residentBenefitsPackageAvailabilityCheck==true)
			{
				moveInCharges_1 = "3,11";
				autoCharges_1 = "11";
			}
			else
			{
			moveInCharges_1 = "3";
			autoCharges_1 = "7";
			}
			InsertDataIntoDatabase.assignChargeCodes(moveInCharges_1, autoCharges_1);
		}
		else
		{
			if(NC_PropertyWare.portfolioType=="Others"&&NC_PropertyWare.petFlag==true&&NC_PropertyWare.petSecurityDepositFlag==false)
			{
				if(NC_PropertyWare.residentBenefitsPackageAvailabilityCheck==true)
				{
					moveInCharges_1 = "3,6,4,11";
					autoCharges_1 = "11,8";
				}
				else
				{
				moveInCharges_1 = "3,6,4";
				autoCharges_1 = "7,8";
				}
				InsertDataIntoDatabase.assignChargeCodes(moveInCharges_1, autoCharges_1);
			}
			else//(NC_PropertyWare.portfolioType=="Others"&&NC_PropertyWare.petFlag==true&&NC_PropertyWare.petSecurityDepositFlag==true)
			{
				if(NC_PropertyWare.residentBenefitsPackageAvailabilityCheck==true)
				{
					moveInCharges_1 = "3,5,4,11";
					autoCharges_1 = "11,8";
				}
				else
				{
				moveInCharges_1 = "3,5,4";
				autoCharges_1 = "7,8";
				}
				InsertDataIntoDatabase.assignChargeCodes(moveInCharges_1, autoCharges_1);
			}
		}
	}
	public static void OtherPortfolios_chargesWhenConsessionAddendumIsNotAvailable()
	{
		if(NC_PropertyWare.portfolioType=="Others"&&NC_PropertyWare.petFlag==false)
		{
			if(NC_PropertyWare.proratedRentDateIsInMoveInMonthFlag == true)
			{
				if(NC_PropertyWare.residentBenefitsPackageAvailabilityCheck==true)
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
				if(NC_PropertyWare.residentBenefitsPackageAvailabilityCheck==true)
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
			InsertDataIntoDatabase.assignChargeCodes(moveInCharges_1, autoCharges_1);
		}
		else
		{
			if(NC_PropertyWare.portfolioType=="Others"&&NC_PropertyWare.petFlag==true&&NC_PropertyWare.petSecurityDepositFlag==false)
			{
				if(NC_PropertyWare.proratedRentDateIsInMoveInMonthFlag == true)
				{
					if(NC_PropertyWare.residentBenefitsPackageAvailabilityCheck==true)
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
					if(NC_PropertyWare.residentBenefitsPackageAvailabilityCheck==true)
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
				InsertDataIntoDatabase.assignChargeCodes(moveInCharges_1, autoCharges_1);
			}
			else//(NC_PropertyWare.portfolioType=="Others"&&NC_PropertyWare.petFlag==true&&NC_PropertyWare.petSecurityDepositFlag==true)
			{
				if(NC_PropertyWare.residentBenefitsPackageAvailabilityCheck==true)
				{
					moveInCharges_1 = "2,3,4,5,11";
					autoCharges_1 = "1,2,11,8";
				}
				else
				{
				moveInCharges_1 = "2,3,4,5";
				autoCharges_1 = "1,2,7,8";
				}
				InsertDataIntoDatabase.assignChargeCodes(moveInCharges_1, autoCharges_1);
			}
		}
	}
	public static void MCHPortfolios_chargesWhenConsessionAddendumIsNotAvailable()
	{
		if(NC_PropertyWare.portfolioType=="MCH"&&NC_PropertyWare.petFlag==false)
		{
			if(NC_PropertyWare.residentBenefitsPackageAvailabilityCheck==true)
			{
				moveInCharges_1 = "1,"+prepaymentChargeOrMonthlyRent+",3,11";
				if(NC_PropertyWare.incrementRentFlag == true)
				autoCharges_1 = "2,11,10";
				else autoCharges_1 = "2,11";
			}
			else
			{
			moveInCharges_1 = "1,"+prepaymentChargeOrMonthlyRent+",3";
			if(NC_PropertyWare.incrementRentFlag == true)
			autoCharges_1 = "2,7,10";
			else autoCharges_1 = "2,7";
			}
			InsertDataIntoDatabase.assignChargeCodes(moveInCharges_1, autoCharges_1);	
		}
		else
		{
			if(NC_PropertyWare.portfolioType=="MCH"&&NC_PropertyWare.petFlag==true&&NC_PropertyWare.petSecurityDepositFlag==false)
			{
				if(NC_PropertyWare.residentBenefitsPackageAvailabilityCheck==true)
				{
				moveInCharges_1 = "1,"+prepaymentChargeOrMonthlyRent+",3,4,6,11";
				if(NC_PropertyWare.incrementRentFlag == true)
				autoCharges_1 = "2,11,8,10";
				else autoCharges_1 = "2,11,8";
				}
				else
				{
					moveInCharges_1 = "1,"+prepaymentChargeOrMonthlyRent+",3,4,6";
					if(NC_PropertyWare.incrementRentFlag == true)
					autoCharges_1 = "2,7,8,10";
					else autoCharges_1 = "2,7,8";
				}
				InsertDataIntoDatabase.assignChargeCodes(moveInCharges_1, autoCharges_1);
			}
		    else
		    {
				if(NC_PropertyWare.portfolioType=="MCH"&&NC_PropertyWare.petFlag==true&&NC_PropertyWare.petSecurityDepositFlag==true)
				{
					if(NC_PropertyWare.residentBenefitsPackageAvailabilityCheck==true)
					{
						moveInCharges_1 = "1,"+prepaymentChargeOrMonthlyRent+",3,4,5,11";
						if(NC_PropertyWare.incrementRentFlag == true)
						autoCharges_1 = "2,11,8,10";
						else autoCharges_1 = "2,11,8";
					}
					else
					{
					moveInCharges_1 = "1,"+prepaymentChargeOrMonthlyRent+",3,4,5";
					if(NC_PropertyWare.incrementRentFlag == true)
					autoCharges_1 = "2,7,8,10";
					else autoCharges_1 = "2,7,8";
					}
					InsertDataIntoDatabase.assignChargeCodes(moveInCharges_1, autoCharges_1);
				}
		    }
		}
	}

	public static void MCHPortfolios_chargesWhenConsessionAddendumIsAvailable()
	{
		if(NC_PropertyWare.portfolioType=="MCH"&&NC_PropertyWare.petFlag==false)
		{
			if(NC_PropertyWare.residentBenefitsPackageAvailabilityCheck==true)
			{
				moveInCharges_1 = "3,11";
				autoCharges_1 = "11";
			}
			else
			{
			moveInCharges_1 = "3";
			autoCharges_1 = "7";
			}
			InsertDataIntoDatabase.assignChargeCodes(moveInCharges_1, autoCharges_1);	
		}
		else
		{
			if(NC_PropertyWare.portfolioType=="MCH"&&NC_PropertyWare.petFlag==true&&NC_PropertyWare.petSecurityDepositFlag==false)
			{
				if(NC_PropertyWare.residentBenefitsPackageAvailabilityCheck==true)
				{
					moveInCharges_1 = "3,11,6,4";
					autoCharges_1 = "11,8";
				}
				else
				{
				moveInCharges_1 = "3,6,4";
				autoCharges_1 = "7,8";
				}
				InsertDataIntoDatabase.assignChargeCodes(moveInCharges_1, autoCharges_1);
			}
		    else
		    {
				if(NC_PropertyWare.portfolioType=="MCH"&&NC_PropertyWare.petFlag==true&&NC_PropertyWare.petSecurityDepositFlag==true)
				{
					if(NC_PropertyWare.residentBenefitsPackageAvailabilityCheck==true)
					{
						moveInCharges_1 = "3,5,4,11";
						autoCharges_1 = "11,8";
					}
					else
					{
					moveInCharges_1 = "3,5,4";
					autoCharges_1 = "7,8";
					}
					InsertDataIntoDatabase.assignChargeCodes(moveInCharges_1, autoCharges_1);
				}
		    }
		}
	}
	
	public static void lateFeeRule() throws Exception
	{
		//Late Charges
		Thread.sleep(2000);
		if(NC_PropertyWare.additionalLateChargesLimit.contains("375"))
		{
			NC_PropertyWare.lateChargeDay = "2";
		}
		else NC_PropertyWare.lateChargeDay = String.valueOf(NC_PropertyWare.lateChargeDay.trim().charAt(0));

		try
		{
			if(NC_PropertyWare.lateChargeDay.equalsIgnoreCase("Error"))
			{
				InsertDataIntoDatabase.notAutomatedFields(RunnerClass.leaseName, "Late Charges - Late Charge Day"+'\n');
				//temp=1;
			}
			else
			{
			NC_RunnerClass.FL_actions.moveToElement(NC_RunnerClass.FL_driver.findElement(Locators.lateFeeDueDay)).build().perform();
			Select dueDayList = new Select(NC_RunnerClass.FL_driver.findElement(Locators.lateFeeDueDay)) ;
			dueDayList.selectByVisibleText(NC_PropertyWare.lateChargeDay);
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
			if(NC_PropertyWare.lateChargeFee.equalsIgnoreCase("Error"))
			{
				InsertDataIntoDatabase.notAutomatedFields(RunnerClass.leaseName, "Late Charges - Late Charge Fee"+'\n');
				//temp=1;
			}
			else
			{
			NC_RunnerClass.FL_actions.moveToElement(NC_RunnerClass.FL_driver.findElement(Locators.initialFee)).build().perform();
			NC_RunnerClass.FL_driver.findElement(Locators.initialFee).click();
			Thread.sleep(1000);
			NC_RunnerClass.FL_driver.findElement(Locators.initialFee).sendKeys(Keys.chord(Keys.CONTROL,"a", Keys.DELETE));
			//NC_PropertyWare.clearTextField();
			//NC_RunnerClass.FL_actions.click(NC_RunnerClass.FL_driver.findElement(Locators.initialFee)).sendKeys(Keys.SHIFT).sendKeys(Keys.HOME).sendKeys(Keys.BACK_SPACE).build().perform();
			NC_RunnerClass.FL_driver.findElement(Locators.initialFee).sendKeys(NC_PropertyWare.lateChargeFee);
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
			if(NC_PropertyWare.lateChargeFee.contains("%"))
			{
			Select initialDropdown = new Select(NC_RunnerClass.FL_driver.findElement(Locators.initialFeeDropdown)) ;
			initialDropdown.selectByVisibleText("% of Rent Charges");
			}
			else 
			{
				Select initialDropdown = new Select(NC_RunnerClass.FL_driver.findElement(Locators.initialFeeDropdown)) ;
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
			if(NC_PropertyWare.lateFeeChargePerDay.equalsIgnoreCase("Error"))
			{
				InsertDataIntoDatabase.notAutomatedFields(RunnerClass.leaseName, "Late Charges - Late Charge Fee Per Day"+'\n');
				//temp=1;
			}
			else
			{
			NC_RunnerClass.FL_actions.moveToElement(NC_RunnerClass.FL_driver.findElement(Locators.perDayFee)).build().perform();
			NC_RunnerClass.FL_driver.findElement(Locators.perDayFee).click();
			Thread.sleep(1000);
			NC_RunnerClass.FL_driver.findElement(Locators.perDayFee).sendKeys(Keys.chord(Keys.CONTROL,"a", Keys.DELETE));
			//NC_PropertyWare.clearTextField();
			//NC_RunnerClass.FL_actions.click(NC_RunnerClass.FL_driver.findElement(Locators.perDayFee)).sendKeys(Keys.SHIFT).sendKeys(Keys.HOME).sendKeys(Keys.BACK_SPACE).build().perform();
			NC_RunnerClass.FL_driver.findElement(Locators.perDayFee).sendKeys(NC_PropertyWare.lateFeeChargePerDay);
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
		Select perDayFeeDropdown = new Select(NC_RunnerClass.FL_driver.findElement(Locators.perDayFeeDropdown)) ;
		perDayFeeDropdown.selectByVisibleText("Fixed Amount");
		}
		catch(Exception e)
		{
			InsertDataIntoDatabase.notAutomatedFields(RunnerClass.leaseName, "Late Charges - Late Charge Fee Per Day Dropdown"+'\n');
			//temp=1;
		}
		if(NC_RunnerClass.pdfFormatType.equalsIgnoreCase("Format1"))
	    {
		
			//Maximum
			Thread.sleep(2000);
			try
			{
			Select maximumDropdown = new Select(NC_RunnerClass.FL_driver.findElement(Locators.maximumYesNoDropdown)) ;
			maximumDropdown.selectByVisibleText("Yes");
			}
			catch(Exception e)
			{
				InsertDataIntoDatabase.notAutomatedFields(RunnerClass.leaseName, "Late Charges - Late Fee Maximum dropdown"+'\n');
				//temp=1;
			}
			// Additional Late charges Limit
			
			String maximumLimitDropdown = "";
			if(NC_PropertyWare.additionalLateChargesLimit.contains("30"))
			{
				NC_PropertyWare.additionalLateChargesLimit = "12";
			    maximumLimitDropdown = "% of Rent Charges";
			}
			else
				maximumLimitDropdown = "Fixed Amount";
			Thread.sleep(2000);
			try
			{
				if(NC_PropertyWare.additionalLateChargesLimit.equalsIgnoreCase("Error"))
				{
					InsertDataIntoDatabase.notAutomatedFields(RunnerClass.leaseName, "Late Charges - Late Charge Fee Limit"+'\n');
					//temp=1;
				}
				else
				{
					NC_RunnerClass.FL_driver.findElement(Locators.maximumDatField).sendKeys(Keys.chord(Keys.CONTROL,"a", Keys.DELETE));
					NC_RunnerClass.FL_driver.findElement(Locators.maximumDatField).clear();
					NC_RunnerClass.FL_driver.findElement(Locators.maximumDatField).click();
					Thread.sleep(1000);
					//NC_PropertyWare.clearTextField();
					//NC_RunnerClass.FL_actions.click(NC_RunnerClass.FL_driver.findElement(Locators.maximumDatField)).sendKeys(Keys.SHIFT).sendKeys(Keys.HOME).sendKeys(Keys.BACK_SPACE).build().perform();
					NC_RunnerClass.FL_driver.findElement(Locators.maximumDatField).sendKeys(NC_PropertyWare.additionalLateChargesLimit);
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
			Select maximumDropdown2 = new Select(NC_RunnerClass.FL_driver.findElement(Locators.maximumDropdown2)) ;
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
		
		//if(NC_PropertyWare.lateFeeType =="GreaterOfFlatFeeOrPercentage")
		//{
			try
			{
		    NC_RunnerClass.FL_actions.moveToElement(NC_RunnerClass.FL_driver.findElement(Locators.lateFeeType)).build().perform();
			Select feeType = new Select(NC_RunnerClass.FL_driver.findElement(Locators.lateFeeType));
			feeType.selectByVisibleText("Greater of Flat Fee or Percentage");
			}
			catch(Exception e)
			{
				InsertDataIntoDatabase.notAutomatedFields(RunnerClass.leaseName, "Late Charges - Late Fee Rule"+'\n');
				return false;
			}
			Thread.sleep(2000);
			try
			{
			NC_RunnerClass.FL_actions.moveToElement(NC_RunnerClass.FL_driver.findElement(Locators.lateFeeDueDay)).build().perform();
			Select dueDayList = new Select(NC_RunnerClass.FL_driver.findElement(Locators.lateFeeDueDay)) ;
			dueDayList.selectByVisibleText(NC_PropertyWare.lateChargeDay);
			}
			catch(Exception e)
			{
				InsertDataIntoDatabase.notAutomatedFields(RunnerClass.leaseName, "Late Charges - Late Fee Due Day"+'\n');
			}
			Thread.sleep(2000);
			try
			{
			NC_RunnerClass.FL_actions.moveToElement(NC_RunnerClass.FL_driver.findElement(Locators.flatFee)).build().perform();
			NC_RunnerClass.FL_driver.findElement(Locators.flatFee).click();
			//NC_RunnerClass.FL_driver.findElement(Locators.flatFee).clear();
			RunnerClass.keyBoardActions();
			NC_RunnerClass.FL_driver.findElement(Locators.flatFee).sendKeys(NC_PropertyWare.flatFeeAmount);
			}
			catch(Exception e)
			{
				InsertDataIntoDatabase.notAutomatedFields(RunnerClass.leaseName, "Late Charges - Flat Fee"+'\n');
			}
			Thread.sleep(2000);
			try
			{
			NC_RunnerClass.FL_actions.moveToElement(NC_RunnerClass.FL_driver.findElement(Locators.lateFeePercentage)).build().perform();
			NC_RunnerClass.FL_driver.findElement(Locators.lateFeePercentage).click();
			RunnerClass.keyBoardActions();
			//NC_RunnerClass.FL_actions.sendKeys(Keys.END).sendKeys(Keys.SHIFT).sendKeys(Keys.HOME).sendKeys(Keys.BACK_SPACE).build().perform();
			NC_RunnerClass.FL_driver.findElement(Locators.lateFeePercentage).sendKeys(NC_PropertyWare.lateFeePercentage);
			}
			catch(Exception e)
			{
				InsertDataIntoDatabase.notAutomatedFields(RunnerClass.leaseName, "Late Charges - Percentage"+'\n');
			}
		//}
			return true;
		
	}
	
	

}

