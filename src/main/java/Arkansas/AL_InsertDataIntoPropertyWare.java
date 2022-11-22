package Arkansas;

import java.util.List;

import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import mainPackage.RunnerClass;
import mainPackage.InsertDataIntoDatabase;



public class AL_InsertDataIntoPropertyWare 
{
	public boolean insertData() throws Exception
	{
		
		//Check the PDF Format, if the PDF format is different, return to main method and make this Building status in Review
		
		int temp=0;
		/*
		if( !AL_PropertyWare.pdfText.contains(AppConfig.PDFFormatConfirmationText))
		{
			InsertDataIntoDatabase.notAutomatedFields(RunnerClass.leaseName, "Wrong Lease Agreement PDF Format");
			RunnerClass.leaseCompletedStatus = 3;
			return false;
		}*/
		try
		{
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
			Thread.sleep(2000);
			int moveInChargesDimensionLength;
			if(ExtractDataFromPDF.petFlag==true)
			{
				moveInChargesDimensionLength=4;
			}
			else moveInChargesDimensionLength =3;
			String[][] moveInCharges = new String[moveInChargesDimensionLength][4]; 
			System.out.println(moveInCharges.length);
			moveInCharges[0][0]=AppConfig.proratedRent_AccountCode;
			moveInCharges[0][1]=AppConfig.proratedRent_AccountRef;
			moveInCharges[0][2]=AL_PropertyWare.proratedRent;
			moveInCharges[0][3]=RunnerClass.convertDate(AL_PropertyWare.commensementDate);
			
			moveInCharges[1][0]=AppConfig.fullMonthRent_AccountCode;
			moveInCharges[1][1]=AppConfig.fullMonthRent_AccountRef;
			moveInCharges[1][2]=AL_PropertyWare.monthlyRent;
			moveInCharges[1][3]=RunnerClass.convertDate(AL_PropertyWare.commensementDate);
			
			moveInCharges[2][0]=AppConfig.adminFee_AccountCode;
			moveInCharges[2][1]=AppConfig.adminFee_AccountRef;
			moveInCharges[2][2]=AL_PropertyWare.adminFee;
			moveInCharges[2][3]=RunnerClass.convertDate(AL_PropertyWare.commensementDate);
			
			if(ExtractDataFromPDF.petFlag==true)
			{
			moveInCharges[3][0]=AppConfig.proratedPetRent_AccountCode;
			moveInCharges[3][1]=AppConfig.proratedPetRent_AccountRef;
			moveInCharges[3][2]=AL_PropertyWare.proratedPetRent;
			moveInCharges[3][3]=RunnerClass.convertDate(AL_PropertyWare.commensementDate);
			}
			
			int autoChargesDimensionLength;
			if(ExtractDataFromPDF.petFlag==true)
			{
				autoChargesDimensionLength=3;
			}
			else autoChargesDimensionLength =1;
			//Auto Charges values
			String[][] autoCharges = new String[autoChargesDimensionLength][4]; 
			System.out.println(autoCharges.length);
			autoCharges[0][0]=AppConfig.airFilterFee_AccountCode;
			autoCharges[0][1]=AppConfig.airFilterFee_Description;
			autoCharges[0][2]=AL_PropertyWare.airFilterFee;
			if(AL_PropertyWare.proratedRentDate.contains("n/a"))
				autoCharges[0][3]= " ";
			else
			autoCharges[0][3]=RunnerClass.convertDate(AL_PropertyWare.proratedRentDate);
			
			if(ExtractDataFromPDF.petFlag==true)
			{
			autoCharges[1][0]=AppConfig.proratedPetRent_AccountCode;
			autoCharges[1][1]=AppConfig.proratedPetRent_AccountRef;
			autoCharges[1][2]=AL_PropertyWare.monthlyRent;
			if(AL_PropertyWare.proratedRentDate.contains("n/a"))
				autoCharges[1][3]= " ";
			else
			autoCharges[1][3]=RunnerClass.convertDate(AL_PropertyWare.proratedRentDate);
			
			autoCharges[2][0]=AppConfig.petRent_AccountCode;
			autoCharges[2][1]=AppConfig.petRent_AccountRef;
			autoCharges[2][2]=AL_PropertyWare.petRent;
			if(AL_PropertyWare.proratedRentDate.contains("n/a"))
				autoCharges[2][3]= " ";
			else
			autoCharges[2][3]=RunnerClass.convertDate(AL_PropertyWare.proratedRentDate);
			}
			
			System.out.println("------Move In Charges-------");
			for(int i=0;i<moveInCharges.length;i++)
			{
				for(int j=0;j<=i;j++)
				{
					try
					{
				System.out.println(moveInCharges[i][j]+" "+moveInCharges[i][j+1]+" "+moveInCharges[i][j+2]+" "+moveInCharges[i][j+3]);
				AL_RunnerClass.AZ_driver.findElement(Locators.newCharge).click();
				Thread.sleep(2000);
				Select AutoChargesDropdown = new Select(AL_RunnerClass.AZ_driver.findElement(Locators.accountDropdown));
				try
				{
				if(moveInCharges[i][j+1].equalsIgnoreCase("Error"))
				{
					InsertDataIntoDatabase.notAutomatedFields(RunnerClass.leaseName, "Move In Charge Account Code");
					temp=1;
				}
				else 
				{
				AutoChargesDropdown.selectByVisibleText(moveInCharges[i][j]);
				}
				}
				catch(Exception e)
				{
					InsertDataIntoDatabase.notAutomatedFields(RunnerClass.leaseName, "Move In Charge Account Code");
					temp=1;
				}
				try
				{
				AL_RunnerClass.AZ_driver.findElement(Locators.referenceName).sendKeys(moveInCharges[i][j+1]);
				Thread.sleep(2000);
				}
				catch(Exception e)
				{
					InsertDataIntoDatabase.notAutomatedFields(RunnerClass.leaseName, "Move In Charge Account Description");
					temp=1;
				}
				try
				{
					if(moveInCharges[i][j+2].equalsIgnoreCase("Error"))
					{
						InsertDataIntoDatabase.notAutomatedFields(RunnerClass.leaseName, "Move In Charge Amount");
						temp=1;
					}
					else
					{
						AL_RunnerClass.AZ_driver.findElement(Locators.moveInChargeAmount).click();
						AL_RunnerClass.AZ_actions.sendKeys(Keys.BACK_SPACE).sendKeys(Keys.BACK_SPACE).sendKeys(Keys.BACK_SPACE).sendKeys(Keys.BACK_SPACE).sendKeys(Keys.BACK_SPACE).build().perform();
						Thread.sleep(2000);
						AL_RunnerClass.AZ_driver.findElement(Locators.moveInChargeAmount).sendKeys(moveInCharges[i][j+2]);
						Thread.sleep(2000);
					}
				}
				catch(Exception e)
				{
					InsertDataIntoDatabase.notAutomatedFields(RunnerClass.leaseName, "Move In Charge Amount");
					temp=1;
				}
				try
				{
					if(moveInCharges[i][j+3].equalsIgnoreCase("Error"))
					{
						InsertDataIntoDatabase.notAutomatedFields(RunnerClass.leaseName, "Move In Charge Date");
						temp=1;
					}
					else
					{
					AL_RunnerClass.AZ_driver.findElement(Locators.moveInChargeDate).clear();
					Thread.sleep(2000);
					AL_RunnerClass.AZ_driver.findElement(Locators.moveInChargeDate).sendKeys(moveInCharges[i][j+3]);
					Thread.sleep(2000);
					}
				}
				catch(Exception e)
				{
					InsertDataIntoDatabase.notAutomatedFields(RunnerClass.leaseName, "Move In Charge Date");
					temp=1;
				}
				AL_RunnerClass.AZ_driver.findElement(Locators.moveInChargeCancel).click();
				AL_RunnerClass.AZ_driver.navigate().refresh();
				}
				catch(Exception e)
				{
				InsertDataIntoDatabase.notAutomatedFields(RunnerClass.leaseName, moveInCharges[i][1]+"Is not saved");
				temp=1;
				}
				break;
				}
			}
			
			System.out.println("------Auto Charges-------");
			// Auto Charges\
			AL_RunnerClass.AZ_driver.findElement(Locators.summaryTab).click();
			Thread.sleep(5000);
			AL_RunnerClass.AZ_driver.findElement(Locators.summaryEditButton).click();
			AL_RunnerClass.AZ_actions.moveToElement(AL_RunnerClass.AZ_driver.findElement(Locators.newAutoCharge)).build().perform();
			
			for(int i=0;i<autoCharges.length;i++)
			{
				for(int j=0;j<=i;j++)
				{
				try
				{
					System.out.println(autoCharges[i][j]+" "+autoCharges[i][j+1]+" "+autoCharges[i][j+2]+" "+autoCharges[i][j+3]);
					try
					{
						if(autoCharges[i][j].equalsIgnoreCase("Error"))
						{
							InsertDataIntoDatabase.notAutomatedFields(RunnerClass.leaseName, "Auto Charge Account code");
							temp=1;
						}
						else
						{
						AL_RunnerClass.AZ_driver.findElement(Locators.newAutoCharge).click();
						Thread.sleep(2000);
						
						Select autoChargesDropdown = new Select(AL_RunnerClass.AZ_driver.findElement(Locators.accountDropdown));
						autoChargesDropdown.selectByVisibleText(autoCharges[i][j]);
						}
					}
					catch(Exception e)
					{
						InsertDataIntoDatabase.notAutomatedFields(RunnerClass.leaseName, "Auto Charge Account code");
						temp=1;
					}
					try
					{
						if(autoCharges[i][j+3].equalsIgnoreCase("Error"))
						{
							InsertDataIntoDatabase.notAutomatedFields(RunnerClass.leaseName, "Auto Charge Start Date");
							temp=1;
						}
						else
					{
					AL_RunnerClass.AZ_driver.findElement(Locators.autoCharge_StartDate).clear();
					Thread.sleep(2000);
					AL_RunnerClass.AZ_driver.findElement(Locators.autoCharge_StartDate).sendKeys(autoCharges[i][j+3]);
					}
					}
					catch(Exception e)
					{
						InsertDataIntoDatabase.notAutomatedFields(RunnerClass.leaseName, "Auto Charge Start Date");
						temp=1;
					}
					try
					{
					AL_RunnerClass.AZ_driver.findElement(Locators.autoCharge_refField).click();
					}
					catch(Exception e)
					{
						InsertDataIntoDatabase.notAutomatedFields(RunnerClass.leaseName, "Auto Charge Referance Field");
						temp=1;
					}
					try
					{
						if(autoCharges[i][j+2].equalsIgnoreCase("Error"))
						{
							InsertDataIntoDatabase.notAutomatedFields(RunnerClass.leaseName, "Auto Charge Amount");
							temp=1;
						}
						else
						{
						AL_RunnerClass.AZ_driver.findElement(Locators.autoCharge_Amount).click();
						AL_RunnerClass.AZ_actions.sendKeys(Keys.BACK_SPACE).sendKeys(Keys.BACK_SPACE).sendKeys(Keys.BACK_SPACE).sendKeys(Keys.BACK_SPACE).sendKeys(Keys.BACK_SPACE).build().perform();
						AL_RunnerClass.AZ_driver.findElement(Locators.autoCharge_Amount).sendKeys(autoCharges[i][j+2]);
						Thread.sleep(2000);
						}
					}
					catch(Exception e)
					{
						InsertDataIntoDatabase.notAutomatedFields(RunnerClass.leaseName, "Auto Charge Amount");
						temp=1;
					}
					
					try
					{
					AL_RunnerClass.AZ_driver.findElement(Locators.autoCharge_Description).sendKeys(autoCharges[i][j+1]);
					}
					catch(Exception e)
					{
						InsertDataIntoDatabase.notAutomatedFields(RunnerClass.leaseName, "Auto Charge Description");
						temp=1;
					}
					Thread.sleep(2000);
					AL_RunnerClass.AZ_driver.findElement(Locators.autoCharge_CancelButton).click();
					
				}
				catch(Exception e)
				{
					InsertDataIntoDatabase.notAutomatedFields(RunnerClass.leaseName, autoCharges[i][1]+"Is not saved");	
					temp=1;
				}
				break;
				}
			}
			
			Thread.sleep(4000);
			
			// RC Field
			try
			{
				if(AL_PropertyWare.RCDetails.equalsIgnoreCase("Error"))
				{
					InsertDataIntoDatabase.notAutomatedFields(RunnerClass.leaseName, "RC Details");
					temp=1;
				}
				else
				{
				AL_RunnerClass.AZ_actions.moveToElement(AL_RunnerClass.AZ_driver.findElement(Locators.rcField)).build().perform();
				AL_RunnerClass.AZ_driver.findElement(Locators.rcField).sendKeys(AL_PropertyWare.RCDetails);
				}
			}
			catch(Exception e)
			{
				InsertDataIntoDatabase.notAutomatedFields(RunnerClass.leaseName, "RC Details");
				temp=1;
			}
			//Early Termination
			
			try
			{
				if(AL_PropertyWare.earlyTermination.equalsIgnoreCase("Error"))
				{
					InsertDataIntoDatabase.notAutomatedFields(RunnerClass.leaseName, "Early Termination");
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
				}
			}
			catch(Exception e)
			{
				InsertDataIntoDatabase.notAutomatedFields(RunnerClass.leaseName, "Early Termination");
				e.printStackTrace();
				temp=1;
			}
			//Enrolled in FilterEasy
			try
			{
			AL_RunnerClass.AZ_actions.moveToElement(AL_RunnerClass.AZ_driver.findElement(Locators.enrolledInFilterEasy)).build().perform();
			AL_RunnerClass.AZ_driver.findElement(Locators.enrolledInFilterEasy).click();
			Select enrolledInFilterEasyList = new Select(AL_RunnerClass.AZ_driver.findElement(Locators.enrolledInFilterEasy_List));
			enrolledInFilterEasyList.selectByVisibleText("YES");
			}
			catch(Exception e)
			{
				InsertDataIntoDatabase.notAutomatedFields(RunnerClass.leaseName, "Enrolled in FilterEasy");
				temp=1;
				e.printStackTrace();
			}
			//Needs New Lease - No by default
			try
			{
			AL_RunnerClass.AZ_actions.moveToElement(AL_RunnerClass.AZ_driver.findElement(Locators.needsNewLease)).build().perform();
			AL_RunnerClass.AZ_driver.findElement(Locators.needsNewLease).click();
			Select needsNewLease_List = new Select(AL_RunnerClass.AZ_driver.findElement(Locators.needsNewLease_List));
			needsNewLease_List.selectByVisibleText("NO");
			}
			catch(Exception e)
			{
				InsertDataIntoDatabase.notAutomatedFields(RunnerClass.leaseName, "Needs New Lease");
				temp=1;
			}
			//Lease Occupants
			try
			{
				if(AL_PropertyWare.occupants.equalsIgnoreCase("Error"))
				{
					InsertDataIntoDatabase.notAutomatedFields(RunnerClass.leaseName, "Lease Occupants");
					temp=1;
				}
				else
				{
				AL_RunnerClass.AZ_actions.moveToElement(AL_RunnerClass.AZ_driver.findElement(Locators.leaseOccupants)).build().perform();
				AL_RunnerClass.AZ_driver.findElement(Locators.leaseOccupants).sendKeys(AL_PropertyWare.occupants);
				}
			}
			catch(Exception e)
			{
				InsertDataIntoDatabase.notAutomatedFields(RunnerClass.leaseName, "Lease Occupants");
				temp=1;
			}
			if(ExtractDataFromPDF.petFlag==true)
			{
			//pet information
				try
				{
					if(AL_PropertyWare.pet1Type.equalsIgnoreCase("Error")||AL_PropertyWare.pet2Type.equalsIgnoreCase("Error"))
					{
						InsertDataIntoDatabase.notAutomatedFields(RunnerClass.leaseName, "Pet Types");
						temp=1;
					}
					else 
					{
						AL_RunnerClass.AZ_actions.moveToElement(AL_RunnerClass.AZ_driver.findElement(Locators.pet1Type)).build().perform();
						AL_RunnerClass.AZ_driver.findElement(Locators.pet1Type).sendKeys(AL_PropertyWare.pet2Type+","+AL_PropertyWare.pet2Type);
					}
				}
				catch(Exception e)
				{
					InsertDataIntoDatabase.notAutomatedFields(RunnerClass.leaseName, "Pet Types");
					temp=1;
				}
				try
				{
					if(AL_PropertyWare.pet1Breed.equalsIgnoreCase("Error")||AL_PropertyWare.pet2Breed.equalsIgnoreCase("Error"))
					{
						InsertDataIntoDatabase.notAutomatedFields(RunnerClass.leaseName, "Pet Breeds");
						temp=1;
					}
					else
					{
						AL_RunnerClass.AZ_actions.moveToElement(AL_RunnerClass.AZ_driver.findElement(Locators.pet1Breed)).build().perform();
						AL_RunnerClass.AZ_driver.findElement(Locators.pet1Breed).sendKeys(AL_PropertyWare.pet1Breed+","+AL_PropertyWare.pet1Breed);
					}
				}
				catch(Exception e)
				{
					InsertDataIntoDatabase.notAutomatedFields(RunnerClass.leaseName, "Pet Breeds");
					temp=1;
				}
				try
				{
					if(AL_PropertyWare.pet1Weight.equalsIgnoreCase("Error")||AL_PropertyWare.pet2Weight.equalsIgnoreCase("Error"))
					{
						InsertDataIntoDatabase.notAutomatedFields(RunnerClass.leaseName, "Pet Weights");
						temp=1;
					}
					else
					{
					AL_RunnerClass.AZ_actions.moveToElement(AL_RunnerClass.AZ_driver.findElement(Locators.pet1Weight)).build().perform();
					AL_RunnerClass.AZ_driver.findElement(Locators.pet1Weight).sendKeys(AL_PropertyWare.pet1Weight+","+AL_PropertyWare.pet2Weight);
					}
				}
				catch(Exception e)
				{
					InsertDataIntoDatabase.notAutomatedFields(RunnerClass.leaseName, "Pet Weights");
					temp=1;
				}
			//Service Animal Information
				try
				{
					if(AL_PropertyWare.serviceAnimalType.equalsIgnoreCase("Error"))
					{
						InsertDataIntoDatabase.notAutomatedFields(RunnerClass.leaseName, "Service Animal Type");
						temp=1;
					}
					else
					{
					AL_RunnerClass.AZ_actions.moveToElement(AL_RunnerClass.AZ_driver.findElement(Locators.serviceAnimalType)).build().perform();
					AL_RunnerClass.AZ_driver.findElement(Locators.serviceAnimalType).sendKeys(AL_PropertyWare.serviceAnimalType);
					}
				}
				catch(Exception e)
				{
					InsertDataIntoDatabase.notAutomatedFields(RunnerClass.leaseName, "Service Animal Type");
					temp=1;
				}
				try
				{
					if(AL_PropertyWare.serviceAnimalBreed.equalsIgnoreCase("Error"))
					{
						InsertDataIntoDatabase.notAutomatedFields(RunnerClass.leaseName, "Service Animal Breed");
						temp=1;
					}
					else
					{
					AL_RunnerClass.AZ_actions.moveToElement(AL_RunnerClass.AZ_driver.findElement(Locators.serviceAnimalBreed)).build().perform();
					AL_RunnerClass.AZ_driver.findElement(Locators.serviceAnimalBreed).sendKeys(AL_PropertyWare.serviceAnimalBreed);
					}
				}
				catch(Exception e)
				{
					InsertDataIntoDatabase.notAutomatedFields(RunnerClass.leaseName, "Service Animal Breed");
					temp=1;
				}
				try
				{
					if(AL_PropertyWare.serviceAnimalWeight.equalsIgnoreCase("Error"))
					{
						InsertDataIntoDatabase.notAutomatedFields(RunnerClass.leaseName, "Service Animal Weight");
						temp=1;
					}
					else
					{
					AL_RunnerClass.AZ_actions.moveToElement(AL_RunnerClass.AZ_driver.findElement(Locators.serviceAnimalWeight)).build().perform();
					AL_RunnerClass.AZ_driver.findElement(Locators.serviceAnimalWeight).sendKeys(AL_PropertyWare.serviceAnimalWeight);
					}
				}
				catch(Exception e)
				{
					InsertDataIntoDatabase.notAutomatedFields(RunnerClass.leaseName, "Service Animal Weight");
					temp=1;
				}
			//Pet Rent
				try
				{
					if(AL_PropertyWare.petRent.equalsIgnoreCase("Error"))
					{
						InsertDataIntoDatabase.notAutomatedFields(RunnerClass.leaseName, "pet Rent");
						temp=1;
					}
					else
					{
					AL_RunnerClass.AZ_actions.moveToElement(AL_RunnerClass.AZ_driver.findElement(Locators.petAmount)).build().perform();
					AL_RunnerClass.AZ_driver.findElement(Locators.petAmount).sendKeys(AL_PropertyWare.petRent);
					}
				}
				catch(Exception e)
				{
					InsertDataIntoDatabase.notAutomatedFields(RunnerClass.leaseName, "pet Rent");
					temp=1;
				}
				try
				{
					if(AL_PropertyWare.petRent.equalsIgnoreCase("Error"))
					{
						InsertDataIntoDatabase.notAutomatedFields(RunnerClass.leaseName, "pet One Time Non-Refundable Fee");
						temp=1;
					}
					else
					{
					AL_RunnerClass.AZ_actions.moveToElement(AL_RunnerClass.AZ_driver.findElement(Locators.tenantOneTimePetFee)).build().perform();
					AL_RunnerClass.AZ_driver.findElement(Locators.tenantOneTimePetFee).sendKeys(AL_PropertyWare.petOneTimeNonRefundableFee);
					}
				}
				catch(Exception e)
				{
					InsertDataIntoDatabase.notAutomatedFields(RunnerClass.leaseName, "pet One Time Non-Refundable Fee");
					temp=1;
				}
			if(AL_PropertyWare.countOfTypeWordInText>2)
			{
				try
				{
					AL_RunnerClass.AZ_actions.moveToElement(AL_RunnerClass.AZ_driver.findElement(Locators.petSpecialProvisions)).build().perform();
					AL_RunnerClass.AZ_driver.findElement(Locators.petSpecialProvisions).sendKeys(AppConfig.petSpecialProvisions);
				}
				catch(Exception e)
				{
					InsertDataIntoDatabase.notAutomatedFields(RunnerClass.leaseName, "Pet Special Provisions");
					temp=1;
				}
				
			}
			
			}

			//Late Charges
			
			try
			{
				if(AL_PropertyWare.lateChargeDay.equalsIgnoreCase("Error"))
				{
					InsertDataIntoDatabase.notAutomatedFields(RunnerClass.leaseName, "Late Charge Day");
					temp=1;
				}
				else
				{
				AL_RunnerClass.AZ_actions.moveToElement(AL_RunnerClass.AZ_driver.findElement(Locators.lateFeeDueDay)).build().perform();
				Select dueDayList = new Select(AL_RunnerClass.AZ_driver.findElement(Locators.lateFeeDueDay)) ;
				dueDayList.selectByVisibleText(String.valueOf(AL_PropertyWare.lateChargeDay.trim().charAt(0)));
				}
			}
			catch(Exception e)
			{
				InsertDataIntoDatabase.notAutomatedFields(RunnerClass.leaseName, "Late Charge Day");
				temp=1;
			}
			// Initial Fee
			try
			{
				if(AL_PropertyWare.lateChargeFee.equalsIgnoreCase("Error"))
				{
					InsertDataIntoDatabase.notAutomatedFields(RunnerClass.leaseName, "Late Charge Fee");
					temp=1;
				}
				else
				{
				AL_RunnerClass.AZ_actions.moveToElement(AL_RunnerClass.AZ_driver.findElement(Locators.initialFee)).build().perform();
				AL_RunnerClass.AZ_driver.findElement(Locators.initialFee).sendKeys(AL_PropertyWare.lateChargeFee);
				}
			}
			catch(Exception e)
			{
				InsertDataIntoDatabase.notAutomatedFields(RunnerClass.leaseName, "Late Charge Fee");
				temp=1;
			}
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
				InsertDataIntoDatabase.notAutomatedFields(RunnerClass.leaseName, "Initial fee Dropdown");
				temp=1;
			}
			
			//Per Day Fee
			try
			{
				if(AL_PropertyWare.additionalLateCharges.equalsIgnoreCase("Error"))
				{
					InsertDataIntoDatabase.notAutomatedFields(RunnerClass.leaseName, "Late Charge Fee Per Day");
					temp=1;
				}
				else
				{
				AL_RunnerClass.AZ_actions.moveToElement(AL_RunnerClass.AZ_driver.findElement(Locators.perDayFee)).build().perform();
				AL_RunnerClass.AZ_driver.findElement(Locators.perDayFee).sendKeys(AL_PropertyWare.additionalLateCharges);
				}
			}
			catch(Exception e)
			{
				e.printStackTrace();
				InsertDataIntoDatabase.notAutomatedFields(RunnerClass.leaseName, "Late Charge Fee Per Day");
				temp=1;
			}

			try
			{
			Select perDayFeeDropdown = new Select(AL_RunnerClass.AZ_driver.findElement(Locators.perDayFeeDropdown)) ;
			perDayFeeDropdown.selectByVisibleText("Fixed Amount");
			}
			catch(Exception e)
			{
				InsertDataIntoDatabase.notAutomatedFields(RunnerClass.leaseName, "Late Charge Fee Per Day Dropdown");
				temp=1;
			}
			
			//Maximum
			try
			{
			Select maximumDropdown = new Select(AL_RunnerClass.AZ_driver.findElement(Locators.maximumYesNoDropdown)) ;
			maximumDropdown.selectByVisibleText("Yes");
			}
			catch(Exception e)
			{
				InsertDataIntoDatabase.notAutomatedFields(RunnerClass.leaseName, "Late Fee Maximum dropdown");
				temp=1;
			}
			// Additional Late charges Limit
			String maximumLimitDropdown = "";
			if(AL_PropertyWare.additionalLateChargesLimit.contains("30"))
			{
				AL_PropertyWare.additionalLateChargesLimit = "12";
			    maximumLimitDropdown = "% of Rent Charges";
			}
			try
			{
				if(AL_PropertyWare.additionalLateChargesLimit.equalsIgnoreCase("Error"))
				{
					InsertDataIntoDatabase.notAutomatedFields(RunnerClass.leaseName, "Late Charge Fee Limit");
					temp=1;
				}
				else
				{
					AL_RunnerClass.AZ_driver.findElement(Locators.maximumDatField).sendKeys(AL_PropertyWare.additionalLateChargesLimit);
				}
			}
			catch(Exception e)
			{
				InsertDataIntoDatabase.notAutomatedFields(RunnerClass.leaseName, "Late Charge Fee Limit");
				temp=1;
			}
			try
			{
			Select maximumDropdown2 = new Select(AL_RunnerClass.AZ_driver.findElement(Locators.maximumDropdown2)) ;
			maximumDropdown2.selectByVisibleText(maximumLimitDropdown);
			}
			catch(Exception e)
			{
				InsertDataIntoDatabase.notAutomatedFields(RunnerClass.leaseName, "Maximum Limit Dropdown 2");
				temp=1;
			}
			if(temp==0)
			RunnerClass.leaseCompletedStatus = 1;
			else RunnerClass.leaseCompletedStatus = 3;
			return true;
		}
		catch(Exception e)
		{
			RunnerClass.leaseCompletedStatus = 2;
			e.printStackTrace();
			return false;
		}
		
	}
	
	

	

}
