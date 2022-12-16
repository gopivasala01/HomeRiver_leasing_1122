package Alabama;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import mainPackage.GetDataFromDataBase;
import mainPackage.InsertDataIntoDatabase;
import mainPackage.RunnerClass;

public class InsertDataIntoPropertyWare_UsingConfigTable 
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
		AL_PropertyWare.proratedRentDateIsInMoveInMonthFlag =  AL_PropertyWare.checkProratedRentDateIsInMoveInMonth(); 
		System.out.println("Prorated Rent is in move in month = "+AL_PropertyWare.proratedRentDateIsInMoveInMonthFlag);
		if(AL_PropertyWare.proratedRentDateIsInMoveInMonthFlag==true)
		prepaymentChargeOrMonthlyRent = "2";
		else 
		prepaymentChargeOrMonthlyRent = "9";
		// Assign Charge codes based on conditions (Portfolio, Company etc)
		int temp=0;
		//If Consession addendum is available, skip Rents and Prorated Rents
		if(AL_PropertyWare.portfolioType=="Others")
		{
			if(AL_PropertyWare.concessionAddendumFlag==true)
			{
				InsertDataIntoPropertyWare_UsingConfigTable.OtherPortfolios_chargesWhenConsessionAddendumIsAvailable();
			}
			else
			{
				InsertDataIntoPropertyWare_UsingConfigTable.OtherPortfolios_chargesWhenConsessionAddendumIsNotAvailable();
			}
		}
		//MCH type
		if(AL_PropertyWare.portfolioType=="MCH")
		{
			if(AL_PropertyWare.concessionAddendumFlag==true)
			{
				InsertDataIntoPropertyWare_UsingConfigTable.MCHPortfolios_chargesWhenConsessionAddendumIsAvailable();
			}
			else
			{
				InsertDataIntoPropertyWare_UsingConfigTable.MCHPortfolios_chargesWhenConsessionAddendumIsNotAvailable();
			}
		}
		//Update other fields for charges
		InsertDataIntoPropertyWare_UsingConfigTable.updateOtherFieldsInConfigurationTable();
		
		try
		{
		AL_RunnerClass.AZ_driver.navigate().refresh();
		AL_RunnerClass.AZ_driver.findElement(Locators.ledgerTab).click();
		}
		catch(Exception e)
		{
			try
			{
			if(AL_RunnerClass.AZ_driver.findElement(Locators.popUpAfterClickingLeaseName).isDisplayed())
			{
				AL_RunnerClass.AZ_driver.findElement(Locators.popupClose).click();
				//AL_RunnerClass.AZ_driver.navigate().refresh();
				AL_RunnerClass.AZ_js.executeScript("window.scrollBy(document.body.scrollHeight,0)");
				AL_RunnerClass.AZ_driver.findElement(Locators.ledgerTab).click();
			}
			}
			catch(Exception e2) {}
		}
		//Check if Concession Addendum is available and update NotAutomatedField if it is available
		if(AL_PropertyWare.concessionAddendumFlag==true)
		{
			InsertDataIntoDatabase.notAutomatedFields(RunnerClass.leaseName, "Concession Addendum is available"+'\n');
		}
		
		 System.out.println("------Move In Charges-------");
		// Get Move in charges
		GetDataFromDataBase.getMoveInCharges();
		for(int i=0;i<moveInCharges.length;i++)
		{
			System.out.println(moveInCharges[i][0]+"   "+moveInCharges[i][1]+"   "+moveInCharges[i][2]+"   "+moveInCharges[i][3]+"  "+moveInCharges[i][4]);
			int flagToCheckIfMoveInChargeAlreadyAvailable =0;
			AL_RunnerClass.AZ_driver.manage().timeouts().implicitlyWait(15,TimeUnit.SECONDS);
			AL_RunnerClass.AZ_wait = new WebDriverWait(AL_RunnerClass.AZ_driver, Duration.ofSeconds(10));
			//Check if the Move In Charge is already available
			
			try
			{
			List<WebElement> existingMoveInCharges_ChargeCodes = AL_RunnerClass.AZ_driver.findElements(Locators.moveInCharges_List);
			List<WebElement> existingMoveInCharges_Amount = AL_RunnerClass.AZ_driver.findElements(Locators.moveInCharge_List_Amount);
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
			
			AL_RunnerClass.AZ_driver.findElement(Locators.newCharge).click();
			Thread.sleep(2000);
			//Account code
			Select AutoChargesDropdown = new Select(AL_RunnerClass.AZ_driver.findElement(Locators.accountDropdown));
			AutoChargesDropdown.selectByVisibleText(moveInCharges[i][1]);
			//Reference
			Thread.sleep(2000);
			AL_RunnerClass.AZ_driver.findElement(Locators.referenceName).sendKeys(moveInCharges[i][2]);
			Thread.sleep(2000);
			//Amount
			AL_RunnerClass.AZ_driver.findElement(Locators.moveInChargeAmount).click();
			AL_RunnerClass.AZ_actions.sendKeys(Keys.BACK_SPACE).sendKeys(Keys.BACK_SPACE).sendKeys(Keys.BACK_SPACE).sendKeys(Keys.BACK_SPACE).sendKeys(Keys.BACK_SPACE).build().perform();
			Thread.sleep(2000);
			AL_RunnerClass.AZ_driver.findElement(Locators.moveInChargeAmount).sendKeys(moveInCharges[i][4]); 
			Thread.sleep(2000);
			//Start Date
			AL_RunnerClass.AZ_driver.findElement(Locators.moveInChargeDate).clear();
			Thread.sleep(2000);
			AL_RunnerClass.AZ_driver.findElement(Locators.moveInChargeDate).sendKeys(moveInCharges[i][3]);
			//Save or Cancel button
			Thread.sleep(2000);
			if(RunnerClass.saveButtonOnAndOff==false)
			AL_RunnerClass.AZ_driver.findElement(Locators.moveInChargeCancel).click();
			else AL_RunnerClass.AZ_driver.findElement(Locators.moveInChargeSaveButton).click();
			Thread.sleep(2000);
			try
			{
				if(AL_RunnerClass.AZ_driver.findElement(Locators.somethingWrongInSavingCharge).isDisplayed())
				{
					AL_RunnerClass.AZ_driver.findElement(Locators.moveInChargeCancel).click();
				}
				
			}
			catch(Exception e)
			{}
			AL_RunnerClass.AZ_driver.navigate().refresh();
		}
		
		// Get Auto charges
	     GetDataFromDataBase.getAutoCharges();
	     System.out.println("------Auto Charges-------");
			// Auto Charges
			Thread.sleep(3000);
			try
			{
			AL_RunnerClass.AZ_driver.findElement(Locators.summaryTab).click();
			}
			catch(Exception e)
			{
				AL_RunnerClass.AZ_driver.navigate().refresh();
				AL_RunnerClass.AZ_driver.findElement(Locators.summaryTab).click();
			}
			Thread.sleep(5000);
			AL_RunnerClass.AZ_driver.findElement(Locators.summaryEditButton).click();
			AL_RunnerClass.AZ_actions.moveToElement(AL_RunnerClass.AZ_driver.findElement(Locators.newAutoCharge)).build().perform();
			
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
				List<WebElement> existingAutoCharges = AL_RunnerClass.AZ_driver.findElements(Locators.autoCharge_List);
				List<WebElement> existingAutoChargeAmounts = AL_RunnerClass.AZ_driver.findElements(Locators.autoCharge_List_Amounts);
				
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
				if(autoCharges[i][4]==null||autoCharges[i][4].equalsIgnoreCase("n/a")||autoCharges[i][4]=="Error"||autoCharges[i][4].trim().equalsIgnoreCase("")||RunnerClass.onlyDigits(autoCharges[i][4].replace(",", "").replace(".", ""))==false)
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
				AL_RunnerClass.AZ_driver.findElement(Locators.newAutoCharge).click();
				}
				catch(Exception e)
				{
					AL_RunnerClass.AZ_driver.findElement(Locators.autoCharge_CancelButton).click();
					
				}
				Thread.sleep(2000);
				
				//Charge Code
				Select autoChargesDropdown = new Select(AL_RunnerClass.AZ_driver.findElement(Locators.accountDropdown));
				autoChargesDropdown.selectByVisibleText(autoCharges[i][1]);
				
				//Start Date
				AL_RunnerClass.AZ_driver.findElement(Locators.autoCharge_StartDate).clear();
				Thread.sleep(2000);
				AL_RunnerClass.AZ_driver.findElement(Locators.autoCharge_StartDate).sendKeys(autoCharges[i][3]);
				
				//click this to hide calendar UI
				AL_RunnerClass.AZ_driver.findElement(Locators.autoCharge_refField).click();
				//Amount
				AL_RunnerClass.AZ_driver.findElement(Locators.autoCharge_Amount).click();
				AL_RunnerClass.AZ_actions.sendKeys(Keys.BACK_SPACE).sendKeys(Keys.BACK_SPACE).sendKeys(Keys.BACK_SPACE).sendKeys(Keys.BACK_SPACE).sendKeys(Keys.BACK_SPACE).build().perform();
				AL_RunnerClass.AZ_driver.findElement(Locators.autoCharge_Amount).sendKeys(autoCharges[i][4]);
				Thread.sleep(2000);
				
				//Description
				AL_RunnerClass.AZ_driver.findElement(Locators.autoCharge_Description).sendKeys(autoCharges[i][2]);
				
				//End Date
				if(autoCharges[i][5]!=null&&autoCharges[i][5]!="") //AL_PropertyWare.portfolioType=="Others"&&
				{
				AL_RunnerClass.AZ_driver.findElement(Locators.autoCharge_EndDate).clear();
				Thread.sleep(2000);
				AL_RunnerClass.AZ_driver.findElement(Locators.autoCharge_EndDate).sendKeys(autoCharges[i][5]);
				}
				try
				{
					AL_RunnerClass.AZ_driver.findElement(By.xpath("//*[text()='New Auto Charge']")).click();
				}
				catch(Exception e) {}
				//Save and Cancel
				Thread.sleep(2000);
				if(RunnerClass.saveButtonOnAndOff==false)
				AL_RunnerClass.AZ_driver.findElement(Locators.autoCharge_CancelButton).click();
				else AL_RunnerClass.AZ_driver.findElement(Locators.autoCharge_SaveButton).click();
				Thread.sleep(2000);
				try
				{
					if(AL_RunnerClass.AZ_driver.findElement(Locators.somethingWrongInSavingCharge).isDisplayed())
					{
						AL_RunnerClass.AZ_driver.findElement(Locators.moveInChargeCancel).click();
					}
					
				}
				catch(Exception e)
				{}
			}
			
			//Other Fields
            Thread.sleep(4000);
			
			// RC Field
			try
			{
				if(AL_PropertyWare.RCDetails.equalsIgnoreCase("Error"))
				{
					InsertDataIntoDatabase.notAutomatedFields(RunnerClass.leaseName, "RC Details"+'\n');
					temp=1;
				}
				else
				{
				AL_RunnerClass.AZ_actions.moveToElement(AL_RunnerClass.AZ_driver.findElement(Locators.rcField)).build().perform();
				AL_RunnerClass.AZ_driver.findElement(Locators.rcField).clear();
				Thread.sleep(1000);
				AL_RunnerClass.AZ_driver.findElement(Locators.rcField).sendKeys(AL_PropertyWare.RCDetails);
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
				if(AL_PropertyWare.earlyTermination.equalsIgnoreCase("Error"))
				{
					InsertDataIntoDatabase.notAutomatedFields(RunnerClass.leaseName, "Early Termination"+'\n');
					temp=1;
				}
				else
				{
				if(AL_PropertyWare.earlyTermination.contains("2"))
				{
					AL_RunnerClass.AZ_actions.moveToElement(AL_RunnerClass.AZ_driver.findElement(Locators.earlyTermFee2x)).build().perform();
					AL_RunnerClass.AZ_driver.findElement(Locators.earlyTermFee2x).click();
					Select earlyTermination_List = new Select(AL_RunnerClass.AZ_driver.findElement(Locators.earlyTermination_List));
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
			//Resident Benefits Package
			if(AL_PropertyWare.residentBenefitsPackageAvailabilityCheck==true)
			{
				if(AL_PropertyWare.residentBenefitsPackage!="Error")
				{
				Thread.sleep(2000);
				try
				{
				AL_RunnerClass.AZ_actions.moveToElement(AL_RunnerClass.AZ_driver.findElement(Locators.residentBenefitsPackage)).build().perform();
				AL_RunnerClass.AZ_driver.findElement(Locators.residentBenefitsPackage).click();
				Select residentBenefitsPackageList = new Select(AL_RunnerClass.AZ_driver.findElement(Locators.residentBenefitsPackage));
				//if(NC_PropertyWare.HVACFilterFlag==false)
				try
				{
				residentBenefitsPackageList.selectByVisibleText("YES");
				}
				catch(NoSuchElementException e)
				{
					
				}
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
				if(AL_PropertyWare.airFilterFee!="Error")
				{
				Thread.sleep(2000);
				try
				{
				AL_RunnerClass.AZ_actions.moveToElement(AL_RunnerClass.AZ_driver.findElement(Locators.enrolledInFilterEasy)).build().perform();
				AL_RunnerClass.AZ_driver.findElement(Locators.enrolledInFilterEasy).click();
				Select enrolledInFilterEasyList = new Select(AL_RunnerClass.AZ_driver.findElement(Locators.enrolledInFilterEasy_List));
				if(AL_PropertyWare.HVACFilterFlag==false)
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
			AL_RunnerClass.AZ_actions.moveToElement(AL_RunnerClass.AZ_driver.findElement(Locators.needsNewLease)).build().perform();
			AL_RunnerClass.AZ_driver.findElement(Locators.needsNewLease).click();
			Select needsNewLease_List = new Select(AL_RunnerClass.AZ_driver.findElement(Locators.needsNewLease_List));
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
				if(AL_PropertyWare.occupants.equalsIgnoreCase("Error"))
				{
					InsertDataIntoDatabase.notAutomatedFields(RunnerClass.leaseName, "Lease Occupants"+'\n');
					temp=1;
				}
				else
				{
				AL_RunnerClass.AZ_actions.moveToElement(AL_RunnerClass.AZ_driver.findElement(Locators.leaseOccupants)).build().perform();
				AL_RunnerClass.AZ_driver.findElement(Locators.leaseOccupants).clear();
				Thread.sleep(1000);
				AL_RunnerClass.AZ_driver.findElement(Locators.leaseOccupants).sendKeys(AL_PropertyWare.occupants);
				}
			}
			catch(Exception e)
			{
				InsertDataIntoDatabase.notAutomatedFields(RunnerClass.leaseName, "Lease Occupants"+'\n');
				temp=1;
			}
			if(AL_PropertyWare.petFlag==true)
			{
			//pet information
				Thread.sleep(2000);
				
				//Pet Type
				String petType = String.join(",", AL_PropertyWare.petType);
				try
				{
					AL_RunnerClass.AZ_actions.moveToElement(AL_RunnerClass.AZ_driver.findElement(Locators.pet1Type)).build().perform();
					AL_RunnerClass.AZ_driver.findElement(Locators.pet1Type).clear();
					Thread.sleep(1000);
					AL_RunnerClass.AZ_driver.findElement(Locators.pet1Type).sendKeys(petType);
				}
				catch(Exception e)
				{
					InsertDataIntoDatabase.notAutomatedFields(RunnerClass.leaseName, "Pet Types"+'\n');
					temp=1;
				}
				Thread.sleep(2000);
				//Pet Breed
				String petBreed = String.join(",", AL_PropertyWare.petBreed);
				try
				{
					AL_RunnerClass.AZ_actions.moveToElement(AL_RunnerClass.AZ_driver.findElement(Locators.pet1Breed)).build().perform();
					AL_RunnerClass.AZ_driver.findElement(Locators.pet1Breed).clear();
					Thread.sleep(1000);
					AL_RunnerClass.AZ_driver.findElement(Locators.pet1Breed).sendKeys(petBreed);
				}
				catch(Exception e)
				{
					InsertDataIntoDatabase.notAutomatedFields(RunnerClass.leaseName, "Pet Breed"+'\n');
					temp=1;
				}
				
				//Pet Weight
				String petWeight = String.join(",", AL_PropertyWare.petWeight);
				try
				{
					AL_RunnerClass.AZ_actions.moveToElement(AL_RunnerClass.AZ_driver.findElement(Locators.pet1Weight)).build().perform();
					AL_RunnerClass.AZ_driver.findElement(Locators.pet1Weight).clear();
					Thread.sleep(1000);
					AL_RunnerClass.AZ_driver.findElement(Locators.pet1Weight).sendKeys(petWeight);
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
					if(AL_PropertyWare.petRent.equalsIgnoreCase("Error"))
					{
						InsertDataIntoDatabase.notAutomatedFields(RunnerClass.leaseName, "pet Rent"+'\n');
						temp=1;
					}
					else
					{
					AL_RunnerClass.AZ_actions.moveToElement(AL_RunnerClass.AZ_driver.findElement(Locators.petAmount)).build().perform();
					//AL_RunnerClass.AZ_driver.findElement(Locators.petAmount).clear();
					AL_RunnerClass.AZ_driver.findElement(Locators.petAmount).click();
					//AL_PropertyWare.clearTextField();
					AL_RunnerClass.AZ_driver.findElement(Locators.petAmount).sendKeys(Keys.chord(Keys.CONTROL,"a", Keys.DELETE));
					Thread.sleep(1000);
					//AL_RunnerClass.AZ_actions.click(AL_RunnerClass.AZ_driver.findElement(Locators.petAmount)).sendKeys(Keys.SHIFT).sendKeys(Keys.HOME).sendKeys(Keys.BACK_SPACE).build().perform();
					AL_RunnerClass.AZ_driver.findElement(Locators.petAmount).sendKeys(AL_PropertyWare.petRent);
					}
				}
				catch(Exception e)
				{
					InsertDataIntoDatabase.notAutomatedFields(RunnerClass.leaseName, "pet Rent"+'\n');
					temp=1;
				}
				try
				{
					if(AL_PropertyWare.petOneTimeNonRefundableFee.equalsIgnoreCase("Error"))
					{
						InsertDataIntoDatabase.notAutomatedFields(RunnerClass.leaseName, "pet One Time Non-Refundable Fee"+'\n');
						temp=1;
					}
					else
					{
					AL_RunnerClass.AZ_actions.moveToElement(AL_RunnerClass.AZ_driver.findElement(Locators.tenantOneTimePetFee)).build().perform();
					AL_RunnerClass.AZ_driver.findElement(Locators.tenantOneTimePetFee).click();
					Thread.sleep(1000);
					AL_RunnerClass.AZ_driver.findElement(Locators.tenantOneTimePetFee).sendKeys(Keys.chord(Keys.CONTROL,"a", Keys.DELETE));
					//AL_PropertyWare.clearTextField();
					//AL_RunnerClass.AZ_actions.click(AL_RunnerClass.AZ_driver.findElement(Locators.tenantOneTimePetFee)).sendKeys(Keys.SHIFT).sendKeys(Keys.HOME).sendKeys(Keys.BACK_SPACE).build().perform();
					AL_RunnerClass.AZ_driver.findElement(Locators.tenantOneTimePetFee).sendKeys(AL_PropertyWare.petOneTimeNonRefundableFee);
					}
				}
				catch(Exception e)
				{
					InsertDataIntoDatabase.notAutomatedFields(RunnerClass.leaseName, "pet One Time Non-Refundable Fee"+'\n');
					temp=1;
				}
			
			}
				//Service Animal Information
				if(AL_PropertyWare.serviceAnimalFlag==true)
				{
					Thread.sleep(2000);
					
					//Pet Type
					String ServiceAnimal_petType = String.join(",", AL_PropertyWare.serviceAnimalPetType);
					try
					{
						AL_RunnerClass.AZ_actions.moveToElement(AL_RunnerClass.AZ_driver.findElement(Locators.serviceAnimal_pet2Type)).build().perform();
						AL_RunnerClass.AZ_driver.findElement(Locators.serviceAnimal_pet2Type).clear();
						Thread.sleep(1000);
						AL_RunnerClass.AZ_driver.findElement(Locators.serviceAnimal_pet2Type).sendKeys("Service "+ServiceAnimal_petType);
					}
					catch(Exception e)
					{
						InsertDataIntoDatabase.notAutomatedFields(RunnerClass.leaseName, "Pet 2 Types"+'\n');
						temp=1;
					}
					Thread.sleep(2000);
					//Pet Breed
					String serviceAnimal_petBreed = String.join(",", AL_PropertyWare.serviceAnimalPetBreed);
					try
					{
						AL_RunnerClass.AZ_actions.moveToElement(AL_RunnerClass.AZ_driver.findElement(Locators.serviceAnimal_pet2Breed)).build().perform();
						AL_RunnerClass.AZ_driver.findElement(Locators.serviceAnimal_pet2Breed).clear();
						Thread.sleep(1000);
						AL_RunnerClass.AZ_driver.findElement(Locators.serviceAnimal_pet2Breed).sendKeys(serviceAnimal_petBreed);
					}
					catch(Exception e)
					{
						InsertDataIntoDatabase.notAutomatedFields(RunnerClass.leaseName, "Pet 2 Breed"+'\n');
						temp=1;
					}
					
					
					Thread.sleep(2000);
					//Pet Weight
					String serviceAnimal_petWeight = String.join(",", AL_PropertyWare.serviceAnimalPetWeight);
					try
					{
						AL_RunnerClass.AZ_actions.moveToElement(AL_RunnerClass.AZ_driver.findElement(Locators.serviceAnimal_pet2Weight)).build().perform();
						AL_RunnerClass.AZ_driver.findElement(Locators.serviceAnimal_pet2Weight).clear();
						Thread.sleep(1000);
						AL_RunnerClass.AZ_driver.findElement(Locators.serviceAnimal_pet2Weight).sendKeys(serviceAnimal_petWeight);
					}
					catch(Exception e)
					{
						InsertDataIntoDatabase.notAutomatedFields(RunnerClass.leaseName, "Pet 2 Weight"+'\n');
						temp=1;
					}
					
					//Pet Special Provisions
					try
					{
						AL_RunnerClass.AZ_actions.moveToElement(AL_RunnerClass.AZ_driver.findElement(Locators.petSpecialProvisions)).build().perform();
						AL_RunnerClass.AZ_driver.findElement(Locators.petSpecialProvisions).clear();
						Thread.sleep(1000);
						AL_RunnerClass.AZ_driver.findElement(Locators.petSpecialProvisions).sendKeys("Service animals, no deposit required");
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
					//if(!AL_PropertyWare.petSecurityDeposit.equalsIgnoreCase("Error")||!AL_PropertyWare.petSecurityDeposit.trim().equalsIgnoreCase(" ")||!AL_PropertyWare.petSecurityDeposit.trim().equalsIgnoreCase(""))
					if(RunnerClass.onlyDigits(AL_PropertyWare.petSecurityDeposit.trim())==true)
					{
					AL_RunnerClass.AZ_actions.moveToElement(AL_RunnerClass.AZ_driver.findElement(Locators.petDepositAmount)).build().perform();
					//AL_RunnerClass.AZ_driver.findElement(Locators.petAmount).clear();
					AL_RunnerClass.AZ_driver.findElement(Locators.petDepositAmount).click();
					AL_RunnerClass.AZ_driver.findElement(Locators.petDepositAmount).sendKeys(Keys.chord(Keys.CONTROL,"a", Keys.DELETE));
					//AL_PropertyWare.clearTextField();
					Thread.sleep(1000);
					//AL_RunnerClass.AZ_actions.click(AL_RunnerClass.AZ_driver.findElement(Locators.petAmount)).sendKeys(Keys.SHIFT).sendKeys(Keys.HOME).sendKeys(Keys.BACK_SPACE).build().perform();
					AL_RunnerClass.AZ_driver.findElement(Locators.petDepositAmount).sendKeys(AL_PropertyWare.petSecurityDeposit);
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
				if(AL_PropertyWare.monthlyRent.equalsIgnoreCase("Error"))
				{
					InsertDataIntoDatabase.notAutomatedFields(RunnerClass.leaseName, "Intial Monthly Rent"+'\n');
					temp=1;
				}
				else
				{
				AL_RunnerClass.AZ_actions.moveToElement(AL_RunnerClass.AZ_driver.findElement(Locators.initialMonthlyRent)).build().perform();
				//AL_RunnerClass.AZ_driver.findElement(Locators.initialMonthlyRent).clear();
				AL_RunnerClass.AZ_driver.findElement(Locators.initialMonthlyRent).sendKeys(Keys.chord(Keys.CONTROL,"a", Keys.DELETE));
				AL_RunnerClass.AZ_driver.findElement(Locators.initialMonthlyRent).sendKeys(AL_PropertyWare.monthlyRent);
				
				}
			}
			catch(Exception e)
			{
				InsertDataIntoDatabase.notAutomatedFields(RunnerClass.leaseName, "Intial Monthly Rent"+'\n');
				temp=1;
			}
			
			//Late Charges
			Thread.sleep(2000);
			if(AL_PropertyWare.additionalLateChargesLimit.contains("375"))
			{
				AL_PropertyWare.lateChargeDay = "2";
			}
			else AL_PropertyWare.lateChargeDay = String.valueOf(AL_PropertyWare.lateChargeDay.trim().charAt(0));

			try
			{
				if(AL_PropertyWare.lateChargeDay.equalsIgnoreCase("Error"))
				{
					InsertDataIntoDatabase.notAutomatedFields(RunnerClass.leaseName, "Late Charges - Late Charge Day"+'\n');
					temp=1;
				}
				else
				{
				AL_RunnerClass.AZ_actions.moveToElement(AL_RunnerClass.AZ_driver.findElement(Locators.lateFeeDueDay)).build().perform();
				Select dueDayList = new Select(AL_RunnerClass.AZ_driver.findElement(Locators.lateFeeDueDay)) ;
				dueDayList.selectByVisibleText(AL_PropertyWare.lateChargeDay);
				}
			}
			catch(Exception e)
			{
				InsertDataIntoDatabase.notAutomatedFields(RunnerClass.leaseName, "Late Charges - Late Charge Day"+'\n');
				temp=1;
			}
			// Initial Fee
			Thread.sleep(2000);
			try
			{
				if(AL_PropertyWare.lateChargeFee.equalsIgnoreCase("Error"))
				{
					InsertDataIntoDatabase.notAutomatedFields(RunnerClass.leaseName, "Late Charges - Late Charge Fee"+'\n');
					temp=1;
				}
				else
				{
				AL_RunnerClass.AZ_actions.moveToElement(AL_RunnerClass.AZ_driver.findElement(Locators.initialFee)).build().perform();
				AL_RunnerClass.AZ_driver.findElement(Locators.initialFee).click();
				Thread.sleep(1000);
				AL_RunnerClass.AZ_driver.findElement(Locators.initialFee).sendKeys(Keys.chord(Keys.CONTROL,"a", Keys.DELETE));
				//AL_PropertyWare.clearTextField();
				//AL_RunnerClass.AZ_actions.click(AL_RunnerClass.AZ_driver.findElement(Locators.initialFee)).sendKeys(Keys.SHIFT).sendKeys(Keys.HOME).sendKeys(Keys.BACK_SPACE).build().perform();
				AL_RunnerClass.AZ_driver.findElement(Locators.initialFee).sendKeys(AL_PropertyWare.lateChargeFee);
				}
			}
			catch(Exception e)
			{
				InsertDataIntoDatabase.notAutomatedFields(RunnerClass.leaseName, "Late Charges - Late Charge Fee"+'\n');
				temp=1;
			}
			Thread.sleep(2000);
			//Initial Fee Dropdown
			
			try
			{
				if(AL_PropertyWare.lateChargeFee.contains("%"))
				{
				Select initialDropdown = new Select(AL_RunnerClass.AZ_driver.findElement(Locators.initialFeeDropdown)) ;
				initialDropdown.selectByVisibleText("% of Rent Charges");
				}
				else 
				{
					Select initialDropdown = new Select(AL_RunnerClass.AZ_driver.findElement(Locators.initialFeeDropdown)) ;
					initialDropdown.selectByVisibleText("Fixed Amount");
				}
			}
			catch(Exception e)
			{
				InsertDataIntoDatabase.notAutomatedFields(RunnerClass.leaseName, "Late Charges - Initial fee Dropdown"+'\n');
				temp=1;
			}
			
			//Per Day Fee
			Thread.sleep(2000);
			try
			{
				if(AL_PropertyWare.lateFeeChargePerDay.equalsIgnoreCase("Error"))
				{
					InsertDataIntoDatabase.notAutomatedFields(RunnerClass.leaseName, "Late Charges - Late Charge Fee Per Day"+'\n');
					temp=1;
				}
				else
				{
				AL_RunnerClass.AZ_actions.moveToElement(AL_RunnerClass.AZ_driver.findElement(Locators.perDayFee)).build().perform();
				AL_RunnerClass.AZ_driver.findElement(Locators.perDayFee).click();
				Thread.sleep(1000);
				AL_RunnerClass.AZ_driver.findElement(Locators.perDayFee).sendKeys(Keys.chord(Keys.CONTROL,"a", Keys.DELETE));
				//AL_PropertyWare.clearTextField();
				//AL_RunnerClass.AZ_actions.click(AL_RunnerClass.AZ_driver.findElement(Locators.perDayFee)).sendKeys(Keys.SHIFT).sendKeys(Keys.HOME).sendKeys(Keys.BACK_SPACE).build().perform();
				AL_RunnerClass.AZ_driver.findElement(Locators.perDayFee).sendKeys(AL_PropertyWare.lateFeeChargePerDay);
				}
			}
			catch(Exception e)
			{
				e.printStackTrace();
				InsertDataIntoDatabase.notAutomatedFields(RunnerClass.leaseName, "Late Charges - Late Charge Fee Per Day"+'\n');
				temp=1;
			}
            //Per Day Fee Dropdown
			Thread.sleep(2000);
			try
			{
			Select perDayFeeDropdown = new Select(AL_RunnerClass.AZ_driver.findElement(Locators.perDayFeeDropdown)) ;
			perDayFeeDropdown.selectByVisibleText("Fixed Amount");
			}
			catch(Exception e)
			{
				InsertDataIntoDatabase.notAutomatedFields(RunnerClass.leaseName, "Late Charges - Late Charge Fee Per Day Dropdown"+'\n');
				temp=1;
			}
			if(AL_RunnerClass.pdfFormatType.equalsIgnoreCase("Format1"))
		    {
			
				//Maximum
				Thread.sleep(2000);
				try
				{
				Select maximumDropdown = new Select(AL_RunnerClass.AZ_driver.findElement(Locators.maximumYesNoDropdown)) ;
				maximumDropdown.selectByVisibleText("Yes");
				}
				catch(Exception e)
				{
					InsertDataIntoDatabase.notAutomatedFields(RunnerClass.leaseName, "Late Charges - Late Fee Maximum dropdown"+'\n');
					temp=1;
				}
				// Additional Late charges Limit
				
				String maximumLimitDropdown = "";
				if(AL_PropertyWare.additionalLateChargesLimit.contains("30"))
				{
					AL_PropertyWare.additionalLateChargesLimit = "12";
				    maximumLimitDropdown = "% of Rent Charges";
				}
				else
					maximumLimitDropdown = "Fixed Amount";
				Thread.sleep(2000);
				try
				{
					if(AL_PropertyWare.additionalLateChargesLimit.equalsIgnoreCase("Error"))
					{
						InsertDataIntoDatabase.notAutomatedFields(RunnerClass.leaseName, "Late Charges - Late Charge Fee Limit"+'\n');
						temp=1;
					}
					else
					{
						AL_RunnerClass.AZ_driver.findElement(Locators.maximumDatField).sendKeys(Keys.chord(Keys.CONTROL,"a", Keys.DELETE));
						AL_RunnerClass.AZ_driver.findElement(Locators.maximumDatField).clear();
						AL_RunnerClass.AZ_driver.findElement(Locators.maximumDatField).click();
						Thread.sleep(1000);
						//AL_PropertyWare.clearTextField();
						//AL_RunnerClass.AZ_actions.click(AL_RunnerClass.AZ_driver.findElement(Locators.maximumDatField)).sendKeys(Keys.SHIFT).sendKeys(Keys.HOME).sendKeys(Keys.BACK_SPACE).build().perform();
						AL_RunnerClass.AZ_driver.findElement(Locators.maximumDatField).sendKeys(AL_PropertyWare.additionalLateChargesLimit);
					}
				}
				catch(Exception e)
				{
					InsertDataIntoDatabase.notAutomatedFields(RunnerClass.leaseName, "Late Charges - Late Charge Fee Limit"+'\n');
					temp=1;
				}
				Thread.sleep(2000);
				try
				{
				Select maximumDropdown2 = new Select(AL_RunnerClass.AZ_driver.findElement(Locators.maximumDropdown2)) ;
				maximumDropdown2.selectByVisibleText(maximumLimitDropdown);
				}
				catch(Exception e)
				{
					InsertDataIntoDatabase.notAutomatedFields(RunnerClass.leaseName, "Late Charges - Maximum Limit Dropdown 2"+'\n');
					temp=1;
				}
		    }
			Thread.sleep(2000);
			AL_RunnerClass.AZ_js.executeScript("window.scrollTo(0,document.body.scrollHeight)");
			try
			{
				Thread.sleep(2000);
				if(RunnerClass.saveButtonOnAndOff==true)
				AL_RunnerClass.AZ_actions.moveToElement(AL_RunnerClass.AZ_driver.findElement(Locators.saveLease)).click(AL_RunnerClass.AZ_driver.findElement(Locators.saveLease)).build().perform();
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
			AL_RunnerClass.AZ_js.executeScript("window.scrollTo(0,document.body.scrollHeight)");
			Thread.sleep(2000);
			if(RunnerClass.saveButtonOnAndOff==true)
			AL_RunnerClass.AZ_actions.moveToElement(AL_RunnerClass.AZ_driver.findElement(Locators.saveLease)).click(AL_RunnerClass.AZ_driver.findElement(Locators.saveLease)).build().perform();
			RunnerClass.leaseCompletedStatus = 2;
			return false;
		}
			
	}
	
	public static void updateOtherFieldsInConfigurationTable() throws Exception
	{
		// Get List of Charges from table
		GetDataFromDataBase.getChargesFromConfigurationTable();
		String DayInCommensementDate = RunnerClass.convertDate(AL_PropertyWare.commensementDate).split("/")[1].trim();
		//Update Start date and End Date
		String firstFullMonth=null;
		String secondFullMonth = null;
		if(DayInCommensementDate.equalsIgnoreCase("01")||DayInCommensementDate.equalsIgnoreCase("1"))
		{
			firstFullMonth = RunnerClass.convertDate(AL_PropertyWare.commensementDate).trim();
			secondFullMonth = RunnerClass.firstDayOfFullMonth(RunnerClass.convertDate(AL_PropertyWare.commensementDate));
		}
		else 
		{
		firstFullMonth = RunnerClass.firstDayOfFullMonth(RunnerClass.convertDate(AL_PropertyWare.commensementDate));
		secondFullMonth = RunnerClass.NextMonthOffirstDayOfFullMonth(RunnerClass.convertDate(AL_PropertyWare.commensementDate));
		}
		String updateStartDateAndEndDate = "Update [Automation].[ChargeCodesConfiguration] Set StartDate='"+RunnerClass.convertDate(AL_PropertyWare.commensementDate)+"' where moveInCharge =1 \n"
				+ "Update [Automation].[ChargeCodesConfiguration] Set autoCharge_StartDate='"+firstFullMonth+"' where AutoCharge =1 \n"
						+ "Update [Automation].[ChargeCodesConfiguration] Set endDate='"+RunnerClass.DateModified(firstFullMonth)+"' where Charge ='Pro Rate Rent' ";
		InsertDataIntoDatabase.updateTable(updateStartDateAndEndDate);
		//If there is an increased rent, add add date to previous monthly rent in auto charges
		
		if(RunnerClass.onlyDigits(AL_PropertyWare.increasedRent_amount.trim().replace(",", "").replace(".", ""))==true)
		{
		String updateMonthlyRentStartDateToNextMonthOfFirstFullMonth = "Update [Automation].[ChargeCodesConfiguration] Set autoCharge_StartDate='"+RunnerClass.firstDayOfFullMonth(RunnerClass.convertDate(AL_PropertyWare.commensementDate))+"',EndDate ='"+RunnerClass.convertDate(AL_PropertyWare.increasedRent_previousRentEndDate.trim())+"'  where ID=2";
		InsertDataIntoDatabase.updateTable(updateMonthlyRentStartDateToNextMonthOfFirstFullMonth);
		}
		else {
		//If Prorate Rent is under 200$, Monthly Rent Start date should be next month of First Full Month
		try
		{
		if(AL_PropertyWare.portfolioType=="Others"||AL_PropertyWare.proratedRentDateIsInMoveInMonthFlag==true) //Double.parseDouble(AL_PropertyWare.proratedRent.trim())<=200.00||
		{
			String updateMonthlyRentStartDateWhenProrateRentIsUnder200Dollers = "Update [Automation].[ChargeCodesConfiguration] Set autoCharge_StartDate='"+secondFullMonth+"' where ID=2";
			InsertDataIntoDatabase.updateTable(updateMonthlyRentStartDateWhenProrateRentIsUnder200Dollers);
		}
		}
		catch(Exception e) {}
		}
		try
		{
		if(AL_PropertyWare.proratedRentDateIsInMoveInMonthFlag==true&&(AL_PropertyWare.proratedPetRent!=""||AL_PropertyWare.proratedPetRent!=null||!AL_PropertyWare.proratedPetRent.equalsIgnoreCase("na")||!AL_PropertyWare.proratedPetRent.equalsIgnoreCase("n/a"))&&(DayInCommensementDate.equalsIgnoreCase("01")||DayInCommensementDate.equalsIgnoreCase("1"))) //Double.parseDouble(AL_PropertyWare.proratedRent.trim())<=200.00||
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
				query = "Update [Automation].[ChargeCodesConfiguration] Set Amount ='"+AL_PropertyWare.proratedRent+"' where charge ='Pro Rate Rent'";
				//InsertDataIntoDatabase.updateTable(query1);
				continue;
				
			case "Monthly Rent":
				query = query+"\nUpdate [Automation].[ChargeCodesConfiguration] Set Amount ='"+AL_PropertyWare.monthlyRent+"' where charge ='Monthly Rent'";
				//InsertDataIntoDatabase.updateTable(query2);
				continue;
			case "Tenant Admin Revenue":
				query = query+"\nUpdate [Automation].[ChargeCodesConfiguration] Set Amount ='"+AL_PropertyWare.adminFee+"' where charge ='Tenant Admin Revenue'";
				//InsertDataIntoDatabase.updateTable(query3);
				continue;
			case "Pro Rated Pet Rent":
				query = query+"\nUpdate [Automation].[ChargeCodesConfiguration] Set Amount ='"+AL_PropertyWare.proratedPetRent+"' where charge ='Pro Rated Pet Rent'";
				//InsertDataIntoDatabase.updateTable(query4);
				continue;
			case "Pet Security Deposit":
				query = query+"\nUpdate [Automation].[ChargeCodesConfiguration] Set Amount ='"+AL_PropertyWare.petSecurityDeposit+"' where charge ='Pet Security Deposit'";
				//InsertDataIntoDatabase.updateTable(query5);
				continue;
			case "Pet One Time Non Refundable":
				query = query+"\nUpdate [Automation].[ChargeCodesConfiguration] Set Amount ='"+AL_PropertyWare.petOneTimeNonRefundableFee+"' where charge ='Pet One Time Non Refundable'";
				//InsertDataIntoDatabase.updateTable(query6);
				continue;
			case "HVAC Filter Fee":
				query = query+"\nUpdate [Automation].[ChargeCodesConfiguration] Set Amount ='"+AL_PropertyWare.airFilterFee+"' where charge ='HVAC Filter Fee'";
				//InsertDataIntoDatabase.updateTable(query7);
				continue;
			case "Pet Rent":
				query = query+"\nUpdate [Automation].[ChargeCodesConfiguration] Set Amount ='"+AL_PropertyWare.petRent+"' where charge ='Pet Rent'";
				//InsertDataIntoDatabase.updateTable(query8);
				continue;
			case "Pre Payment Charge":
				query = query+"\nUpdate [Automation].[ChargeCodesConfiguration] Set Amount ='"+AL_PropertyWare.prepaymentCharge+"' where charge ='Pre Payment Charge'";
				//InsertDataIntoDatabase.updateTable(query9);
				continue;
			case "Increased Rent":
				try {
				query = query+"\nUpdate [Automation].[ChargeCodesConfiguration] Set Amount ='"+AL_PropertyWare.increasedRent_amount+"',autoCharge_StartDate ='"+RunnerClass.convertDate(AL_PropertyWare.increasedRent_newStartDate)+"' where charge ='Increased Rent'";
				//InsertDataIntoDatabase.updateTable(query10);
				}
				catch(Exception e) {}
				continue;
			case "Resident Benefits Package":
				query = query+"\nUpdate [Automation].[ChargeCodesConfiguration] Set Amount ='"+AL_PropertyWare.residentBenefitsPackage+"',startDate ='"+RunnerClass.convertDate(AL_PropertyWare.commensementDate).trim()+"',autoCharge_startDate='"+firstFullMonth+"'  where charge ='Resident Benefits Package'";
				//InsertDataIntoDatabase.updateTable(query9);
				continue;
			}
			
		}
		InsertDataIntoDatabase.updateTable(query);
	}
	public static void OtherPortfolios_chargesWhenConsessionAddendumIsAvailable()
	{
		if(AL_PropertyWare.portfolioType=="Others"&&AL_PropertyWare.petFlag==false)
		{
			if(AL_PropertyWare.residentBenefitsPackageAvailabilityCheck==true)
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
			if(AL_PropertyWare.portfolioType=="Others"&&AL_PropertyWare.petFlag==true&&AL_PropertyWare.petSecurityDepositFlag==false)
			{
				if(AL_PropertyWare.residentBenefitsPackageAvailabilityCheck==true)
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
			else//(AL_PropertyWare.portfolioType=="Others"&&AL_PropertyWare.petFlag==true&&AL_PropertyWare.petSecurityDepositFlag==true)
			{
				if(AL_PropertyWare.residentBenefitsPackageAvailabilityCheck==true)
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
		if(AL_PropertyWare.portfolioType=="Others"&&AL_PropertyWare.petFlag==false)
		{
			if(AL_PropertyWare.proratedRentDateIsInMoveInMonthFlag == true)
			{
				if(AL_PropertyWare.residentBenefitsPackageAvailabilityCheck==true)
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
				if(AL_PropertyWare.residentBenefitsPackageAvailabilityCheck==true)
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
			if(AL_PropertyWare.portfolioType=="Others"&&AL_PropertyWare.petFlag==true&&AL_PropertyWare.petSecurityDepositFlag==false)
			{
				if(AL_PropertyWare.proratedRentDateIsInMoveInMonthFlag == true)
				{
					if(AL_PropertyWare.residentBenefitsPackageAvailabilityCheck==true)
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
					if(AL_PropertyWare.residentBenefitsPackageAvailabilityCheck==true)
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
			else//(AL_PropertyWare.portfolioType=="Others"&&AL_PropertyWare.petFlag==true&&AL_PropertyWare.petSecurityDepositFlag==true)
			{
				if(AL_PropertyWare.residentBenefitsPackageAvailabilityCheck==true)
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
		if(AL_PropertyWare.portfolioType=="MCH"&&AL_PropertyWare.petFlag==false)
		{
			if(AL_PropertyWare.residentBenefitsPackageAvailabilityCheck==true)
			{
				moveInCharges_1 = "1,"+prepaymentChargeOrMonthlyRent+",3,11";
				if(AL_PropertyWare.incrementRentFlag == true)
				autoCharges_1 = "2,11,10";
				else autoCharges_1 = "2,11";
			}
			else
			{
			moveInCharges_1 = "1,"+prepaymentChargeOrMonthlyRent+",3";
			if(AL_PropertyWare.incrementRentFlag == true)
			autoCharges_1 = "2,7,10";
			else autoCharges_1 = "2,7";
			}
			InsertDataIntoDatabase.assignChargeCodes(moveInCharges_1, autoCharges_1);	
		}
		else
		{
			if(AL_PropertyWare.portfolioType=="MCH"&&AL_PropertyWare.petFlag==true&&AL_PropertyWare.petSecurityDepositFlag==false)
			{
				if(AL_PropertyWare.residentBenefitsPackageAvailabilityCheck==true)
				{
				moveInCharges_1 = "1,"+prepaymentChargeOrMonthlyRent+",3,4,6,11";
				if(AL_PropertyWare.incrementRentFlag == true)
				autoCharges_1 = "2,11,8,10";
				else autoCharges_1 = "2,11,8";
				}
				else
				{
					moveInCharges_1 = "1,"+prepaymentChargeOrMonthlyRent+",3,4,6";
					if(AL_PropertyWare.incrementRentFlag == true)
					autoCharges_1 = "2,7,8,10";
					else autoCharges_1 = "2,7,8";
				}
				InsertDataIntoDatabase.assignChargeCodes(moveInCharges_1, autoCharges_1);
			}
		    else
		    {
				if(AL_PropertyWare.portfolioType=="MCH"&&AL_PropertyWare.petFlag==true&&AL_PropertyWare.petSecurityDepositFlag==true)
				{
					if(AL_PropertyWare.residentBenefitsPackageAvailabilityCheck==true)
					{
						moveInCharges_1 = "1,"+prepaymentChargeOrMonthlyRent+",3,4,5,11";
						if(AL_PropertyWare.incrementRentFlag == true)
						autoCharges_1 = "2,11,8,10";
						else autoCharges_1 = "2,11,8";
					}
					else
					{
					moveInCharges_1 = "1,"+prepaymentChargeOrMonthlyRent+",3,4,5";
					if(AL_PropertyWare.incrementRentFlag == true)
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
		if(AL_PropertyWare.portfolioType=="MCH"&&AL_PropertyWare.petFlag==false)
		{
			if(AL_PropertyWare.residentBenefitsPackageAvailabilityCheck==true)
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
			if(AL_PropertyWare.portfolioType=="MCH"&&AL_PropertyWare.petFlag==true&&AL_PropertyWare.petSecurityDepositFlag==false)
			{
				if(AL_PropertyWare.residentBenefitsPackageAvailabilityCheck==true)
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
				if(AL_PropertyWare.portfolioType=="MCH"&&AL_PropertyWare.petFlag==true&&AL_PropertyWare.petSecurityDepositFlag==true)
				{
					if(AL_PropertyWare.residentBenefitsPackageAvailabilityCheck==true)
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
}
