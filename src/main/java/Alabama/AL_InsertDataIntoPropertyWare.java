package Alabama;

import java.awt.event.KeyEvent;
import java.util.List;

import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import Arizona.AZ_RunnerClass;
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
			//Check if Concession Addendum is available and update NotAutomatedField if it is available
			if(AL_PropertyWare.concessionAddendumFlag==true)
			{
				InsertDataIntoDatabase.notAutomatedFields(RunnerClass.leaseName, "Concession Addendum is available"+'\n');
			}
			
			//Move In Charge Array
			
			Thread.sleep(2000);
			int moveInChargesDimensionLength;
			if(ExtractDataFromPDF.petFlag==true)
			{
				moveInChargesDimensionLength=5;
			}
			else moveInChargesDimensionLength =3;
			String[][] moveInCharges = new String[moveInChargesDimensionLength][4]; 
			System.out.println(moveInCharges.length);
			
			//Prorated Rent
			moveInCharges[0][0]=AppConfig.proratedRent_AccountCode;
			moveInCharges[0][1]=AppConfig.proratedRent_AccountRef;
			moveInCharges[0][2]=AL_PropertyWare.proratedRent;
			moveInCharges[0][3]=RunnerClass.convertDate(AL_PropertyWare.commensementDate);
			
			//Calculate Prepayment charge i.e MonthlyRent - Prorated Rent
			
			try
			{
			AL_PropertyWare.prepaymentCharge =String.valueOf(Double.parseDouble(AL_PropertyWare.monthlyRent.replace(",", "")) - Double.parseDouble(AL_PropertyWare.proratedRent.replace(",", ""))); 
			System.out.println("Prepayment Charge = "+AL_PropertyWare.prepaymentCharge);
			}
			catch(Exception e)
			{
				AL_PropertyWare.prepaymentCharge ="Error";
			}
			//Prepayments
			moveInCharges[1][0]=AppConfig.fullMonthRent_AccountCode;
			moveInCharges[1][1]=AppConfig.fullMonthRent_AccountRef;
			moveInCharges[1][2]=String.valueOf(AL_PropertyWare.prepaymentCharge);
			moveInCharges[1][3]=RunnerClass.convertDate(AL_PropertyWare.commensementDate);
			
			//Admin Fee
			moveInCharges[2][0]=AppConfig.adminFee_AccountCode;
			moveInCharges[2][1]=AppConfig.adminFee_AccountRef;
			moveInCharges[2][2]=AL_PropertyWare.adminFee;
			moveInCharges[2][3]=RunnerClass.convertDate(AL_PropertyWare.commensementDate);
			
			if(ExtractDataFromPDF.petFlag==true)
			{
				
				//Pet Rent
			moveInCharges[3][0]=AppConfig.proratedPetRent_AccountCode;
			moveInCharges[3][1]=AppConfig.proratedPetRent_AccountRef;
			moveInCharges[3][2]=AL_PropertyWare.proratedPetRent;
			moveInCharges[3][3]=RunnerClass.convertDate(AL_PropertyWare.commensementDate);
			
				if(RunnerClass.onlyDigits(AL_PropertyWare.petSecurityDeposit)==true)
				{
					//Security Deposit if checked
					moveInCharges[4][0]=AppConfig.petSecurityDeposit_AccountCode;
					moveInCharges[4][1]=AppConfig.petSecurityDeposit_AccountRef;
					moveInCharges[4][2]=AL_PropertyWare.petSecurityDeposit;
					moveInCharges[4][3]=RunnerClass.convertDate(AL_PropertyWare.commensementDate);
				}
				else
				{
					//Pet Fee - Pet one time non refundable
					moveInCharges[4][0]=AppConfig.petRent_AccountCode;
					moveInCharges[4][1]=AppConfig.petRent_AccountRef;
					moveInCharges[4][2]=AL_PropertyWare.petOneTimeNonRefundableFee;
					moveInCharges[4][3]=RunnerClass.convertDate(AL_PropertyWare.commensementDate);
				}
			}
			
			
			int autoChargesDimensionLength;
			if(ExtractDataFromPDF.petFlag==true)
			{
				autoChargesDimensionLength=3;
			}
			else autoChargesDimensionLength =2;
			//Auto Charges values
			String[][] autoCharges = new String[autoChargesDimensionLength][4]; 
			System.out.println(autoCharges.length);
			
			//HVAC Air Filter Fee
			autoCharges[0][0]=AppConfig.airFilterFee_AccountCode;
			autoCharges[0][1]=AppConfig.airFilterFee_Description;
			autoCharges[0][2]=AL_PropertyWare.airFilterFee;
			//if(AL_PropertyWare.proratedRentDate.contains("n/a"))
				//autoCharges[0][3]= " ";
			//else
			autoCharges[0][3]=RunnerClass.firstDayOfFullMonth(RunnerClass.convertDate(AL_PropertyWare.commensementDate));
			
			//Monthly Rent
			autoCharges[1][0]=AppConfig.proratedRent_AccountCode;
			autoCharges[1][1]="Rent";
			autoCharges[1][2]=AL_PropertyWare.monthlyRent;
			//if(AL_PropertyWare.proratedRentDate.contains("n/a"))
				//autoCharges[1][3]= " ";
			//else
			autoCharges[1][3]=RunnerClass.firstDayOfFullMonth(RunnerClass.convertDate(AL_PropertyWare.commensementDate));
			
			if(ExtractDataFromPDF.petFlag==true)
			{
			autoCharges[2][0]=AppConfig.PetRent_AccountCode;
			autoCharges[2][1]=AppConfig.petRent_AccountRef;
			autoCharges[2][2]=AL_PropertyWare.petRent;
			//if(AL_PropertyWare.proratedRentDate.contains("n/a"))
				//autoCharges[2][3]= " ";
			//else
			autoCharges[2][3]=RunnerClass.firstDayOfFullMonth(RunnerClass.convertDate(AL_PropertyWare.commensementDate));
			}
			
			System.out.println("------Move In Charges-------");
			for(int i=0;i<moveInCharges.length;i++)
			{
				int flagToCheckIfMoveInChargeAlreadyAvailable=0;
				//for(int j=0;j<=i;j++)
				//{
				//Check if the Move In Charge already available
				System.out.println(moveInCharges[i][0]+" "+moveInCharges[i][1]+" "+moveInCharges[i][2]+" "+moveInCharges[i][3]);
				List<WebElement> existingAutoCharges = AL_RunnerClass.AZ_driver.findElements(Locators.moveInCharges_List);
				for(int k=0;k<existingAutoCharges.size();k++)
				{
					String autoChargeDescription = existingAutoCharges.get(k).getText();
					if(autoChargeDescription.trim().contains(moveInCharges[i][1].trim()))
					{
						System.out.println(moveInCharges[i][1]+"   is already available");
						flagToCheckIfMoveInChargeAlreadyAvailable=1;
						break;
						
					}
				}
				if(flagToCheckIfMoveInChargeAlreadyAvailable==1)
					continue;
				
				try
				{
				if(moveInCharges[i][2]==null||moveInCharges[i][2]=="Error"||RunnerClass.onlyDigits(moveInCharges[i][2])==false)
				{
					InsertDataIntoDatabase.notAutomatedFields(RunnerClass.leaseName, "Move In Charge - "+moveInCharges[i][1]+'\n');
					temp=1;
					continue;
				}
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}
					try
					{
				AL_RunnerClass.AZ_driver.findElement(Locators.newCharge).click();
				Thread.sleep(2000);
				Select AutoChargesDropdown = new Select(AL_RunnerClass.AZ_driver.findElement(Locators.accountDropdown));
				try
				{
				if(moveInCharges[i][1].equalsIgnoreCase("Error"))
				{
					InsertDataIntoDatabase.notAutomatedFields(RunnerClass.leaseName, "Move In Charge Account Code"+'\n');
					temp=1;
				}
				else 
				{
				AutoChargesDropdown.selectByVisibleText(moveInCharges[i][0]);
				}
				}
				catch(Exception e)
				{
					InsertDataIntoDatabase.notAutomatedFields(RunnerClass.leaseName, "Move In Charge Account Code"+'\n');
					temp=1;
				}
				try
				{
				AL_RunnerClass.AZ_driver.findElement(Locators.referenceName).sendKeys(moveInCharges[i][1]);
				Thread.sleep(2000);
				}
				catch(Exception e)
				{
					InsertDataIntoDatabase.notAutomatedFields(RunnerClass.leaseName, "Move In Charge Account Description"+'\n');
					temp=1;
				}
				try
				{
					if(moveInCharges[i][2].equalsIgnoreCase("Error"))
					{
						InsertDataIntoDatabase.notAutomatedFields(RunnerClass.leaseName, "Move In Charge Amount"+'\n');
						temp=1;
					}
					else
					{
						AL_RunnerClass.AZ_driver.findElement(Locators.moveInChargeAmount).click();
						AL_RunnerClass.AZ_actions.sendKeys(Keys.BACK_SPACE).sendKeys(Keys.BACK_SPACE).sendKeys(Keys.BACK_SPACE).sendKeys(Keys.BACK_SPACE).sendKeys(Keys.BACK_SPACE).build().perform();
						Thread.sleep(2000);
						AL_RunnerClass.AZ_driver.findElement(Locators.moveInChargeAmount).sendKeys(moveInCharges[i][2]);
						Thread.sleep(2000);
					}
				}
				catch(Exception e)
				{
					InsertDataIntoDatabase.notAutomatedFields(RunnerClass.leaseName, "Move In Charge Amount"+'\n');
					temp=1;
				}
				try
				{
					if(moveInCharges[i][3].equalsIgnoreCase("Error"))
					{
						InsertDataIntoDatabase.notAutomatedFields(RunnerClass.leaseName, "Move In Charge Date"+'\n');
						temp=1;
					}
					else
					{
					AL_RunnerClass.AZ_driver.findElement(Locators.moveInChargeDate).clear();
					Thread.sleep(2000);
					AL_RunnerClass.AZ_driver.findElement(Locators.moveInChargeDate).sendKeys(moveInCharges[i][3]);
					Thread.sleep(2000);
					}
				}
				catch(Exception e)
				{
					InsertDataIntoDatabase.notAutomatedFields(RunnerClass.leaseName, "Move In Charge Date"+'\n');
					temp=1;
				}
				Thread.sleep(2000);
				//AL_RunnerClass.AZ_driver.findElement(Locators.moveInChargeCancel).click();
				AL_RunnerClass.AZ_driver.findElement(Locators.moveInChargeSaveButton).click();
				Thread.sleep(2000);
				AL_RunnerClass.AZ_driver.navigate().refresh();
				}
				catch(Exception e)
				{
				InsertDataIntoDatabase.notAutomatedFields(RunnerClass.leaseName, moveInCharges[i][1]+"Is not saved"+'\n');
				temp=1;
				}
				//break;
				//}
			}
			
			System.out.println("------Auto Charges-------");
			// Auto Charges\
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
				//for(int j=0;j<=i;j++)
				//{
				//Check if the Auto Charge is already available
				
				List<WebElement> existingAutoCharges = AL_RunnerClass.AZ_driver.findElements(Locators.autoCharge_List);
				for(int k=0;k<existingAutoCharges.size();k++)
				{
					String autoChargeDescription = existingAutoCharges.get(k).getText();
					if(autoChargeDescription.trim().contains(autoCharges[i][1]))
					{
						System.out.println(autoCharges[i][1]+"   is already available");
						flagToCheckIfAutoChargeAvailable=1;
						break;
					}
				}
				if(flagToCheckIfAutoChargeAvailable==1)
				continue;
				
				try
				{
				if(autoCharges[i][2]==null||autoCharges[i][2]=="Error"||RunnerClass.onlyDigits(autoCharges[i][2])==false)
				{
					InsertDataIntoDatabase.notAutomatedFields(RunnerClass.leaseName, "Auto Charge - "+autoCharges[i][1]+'\n');
					temp=1;
					continue;
				}
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}
				
				try
				{
					System.out.println(autoCharges[i][0]+" "+autoCharges[i][1]+" "+autoCharges[i][2]+" "+autoCharges[i][3]);
					try
					{
						if(autoCharges[i][0].equalsIgnoreCase("Error"))
						{
							InsertDataIntoDatabase.notAutomatedFields(RunnerClass.leaseName, "Auto Charge Account code"+'\n');
							temp=1;
						}
						else
						{
						AL_RunnerClass.AZ_driver.findElement(Locators.newAutoCharge).click();
						Thread.sleep(2000);
						
						Select autoChargesDropdown = new Select(AL_RunnerClass.AZ_driver.findElement(Locators.accountDropdown));
						autoChargesDropdown.selectByVisibleText(autoCharges[i][0]);
						}
					}
					catch(Exception e)
					{
						InsertDataIntoDatabase.notAutomatedFields(RunnerClass.leaseName, "Auto Charge Account code"+'\n');
						temp=1;
						autoChargesSaveFlag=1;
					}
					try
					{
						if(autoCharges[i][3].equalsIgnoreCase("Error"))
						{
							InsertDataIntoDatabase.notAutomatedFields(RunnerClass.leaseName, "Auto Charge Start Date"+'\n');
							temp=1;
						}
						else
					{
					AL_RunnerClass.AZ_driver.findElement(Locators.autoCharge_StartDate).clear();
					Thread.sleep(2000);
					AL_RunnerClass.AZ_driver.findElement(Locators.autoCharge_StartDate).sendKeys(autoCharges[i][3]);
					}
					}
					catch(Exception e)
					{
						InsertDataIntoDatabase.notAutomatedFields(RunnerClass.leaseName, "Auto Charge Start Date"+'\n');
						temp=1;
						autoChargesSaveFlag=1;
					}
					try
					{
					AL_RunnerClass.AZ_driver.findElement(Locators.autoCharge_refField).click();
					}
					catch(Exception e)
					{
						InsertDataIntoDatabase.notAutomatedFields(RunnerClass.leaseName, "Auto Charge Referance Field"+'\n');
						temp=1;
						autoChargesSaveFlag=1;
					}
					try
					{
						if(autoCharges[i][2].equalsIgnoreCase("Error"))
						{
							InsertDataIntoDatabase.notAutomatedFields(RunnerClass.leaseName, "Auto Charge Amount"+'\n');
							temp=1;
						}
						else
						{
						AL_RunnerClass.AZ_driver.findElement(Locators.autoCharge_Amount).click();
						AL_RunnerClass.AZ_actions.sendKeys(Keys.BACK_SPACE).sendKeys(Keys.BACK_SPACE).sendKeys(Keys.BACK_SPACE).sendKeys(Keys.BACK_SPACE).sendKeys(Keys.BACK_SPACE).build().perform();
						AL_RunnerClass.AZ_driver.findElement(Locators.autoCharge_Amount).sendKeys(autoCharges[i][2]);
						Thread.sleep(2000);
						}
					}
					catch(Exception e)
					{
						InsertDataIntoDatabase.notAutomatedFields(RunnerClass.leaseName, "Auto Charge Amount"+'\n');
						temp=1;
						autoChargesSaveFlag=1;
					}
					
					try
					{
					AL_RunnerClass.AZ_driver.findElement(Locators.autoCharge_Description).sendKeys(autoCharges[i][1]);
					}
					catch(Exception e)
					{
						InsertDataIntoDatabase.notAutomatedFields(RunnerClass.leaseName, "Auto Charge Description"+'\n');
						temp=1;
						autoChargesSaveFlag=1;
					}
					Thread.sleep(2000);
					if(autoChargesSaveFlag==1)
	                {
						AL_RunnerClass.AZ_driver.findElement(Locators.autoCharge_CancelButton).click();
		            }
					else
					{
						AL_RunnerClass.AZ_driver.findElement(Locators.autoCharge_SaveButton).click();
						//AL_RunnerClass.AZ_driver.findElement(Locators.autoCharge_CancelButton).click();
					Thread.sleep(2000);
					}
					
				}
				catch(Exception e)
				{
					InsertDataIntoDatabase.notAutomatedFields(RunnerClass.leaseName, autoCharges[i][1]+"Is not saved"+'\n');	
					temp=1;
				}
				//break;
				//}
				
			}
			
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
				}
			}
			catch(Exception e)
			{
				InsertDataIntoDatabase.notAutomatedFields(RunnerClass.leaseName, "Early Termination"+'\n');
				e.printStackTrace();
				temp=1;
			}
			//Enrolled in FilterEasy
			Thread.sleep(2000);
			try
			{
			AL_RunnerClass.AZ_actions.moveToElement(AL_RunnerClass.AZ_driver.findElement(Locators.enrolledInFilterEasy)).build().perform();
			AL_RunnerClass.AZ_driver.findElement(Locators.enrolledInFilterEasy).click();
			Select enrolledInFilterEasyList = new Select(AL_RunnerClass.AZ_driver.findElement(Locators.enrolledInFilterEasy_List));
			enrolledInFilterEasyList.selectByVisibleText("YES");
			}
			catch(Exception e)
			{
				InsertDataIntoDatabase.notAutomatedFields(RunnerClass.leaseName, "Enrolled in FilterEasy"+'\n');
				temp=1;
				e.printStackTrace();
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
			if(ExtractDataFromPDF.petFlag==true)
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
				Thread.sleep(2000);
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
					InsertDataIntoDatabase.notAutomatedFields(RunnerClass.leaseName, "Pet Weight"+'\n');
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
					AL_PropertyWare.clearTextField();
					Thread.sleep(1000);
					//AZ_RunnerClass.AZ_actions.click(AL_RunnerClass.AZ_driver.findElement(Locators.petAmount)).sendKeys(Keys.SHIFT).sendKeys(Keys.HOME).sendKeys(Keys.BACK_SPACE).build().perform();
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
					AL_PropertyWare.clearTextField();
					//AZ_RunnerClass.AZ_actions.click(AL_RunnerClass.AZ_driver.findElement(Locators.tenantOneTimePetFee)).sendKeys(Keys.SHIFT).sendKeys(Keys.HOME).sendKeys(Keys.BACK_SPACE).build().perform();
					AL_RunnerClass.AZ_driver.findElement(Locators.tenantOneTimePetFee).sendKeys(AL_PropertyWare.petOneTimeNonRefundableFee);
					}
				}
				catch(Exception e)
				{
					InsertDataIntoDatabase.notAutomatedFields(RunnerClass.leaseName, "pet One Time Non-Refundable Fee"+'\n');
					temp=1;
				}
				/*
			if(AL_PropertyWare.countOfTypeWordInText>2)
			{
				Thread.sleep(2000);
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
			*/
			
			}

			//Late Charges
			Thread.sleep(2000);
			try
			{
				if(AL_PropertyWare.lateChargeDay.equalsIgnoreCase("Error"))
				{
					InsertDataIntoDatabase.notAutomatedFields(RunnerClass.leaseName, "Late Charge Day"+'\n');
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
				InsertDataIntoDatabase.notAutomatedFields(RunnerClass.leaseName, "Late Charge Day"+'\n');
				temp=1;
			}
			// Initial Fee
			Thread.sleep(2000);
			try
			{
				if(AL_PropertyWare.lateChargeFee.equalsIgnoreCase("Error"))
				{
					InsertDataIntoDatabase.notAutomatedFields(RunnerClass.leaseName, "Late Charge Fee"+'\n');
					temp=1;
				}
				else
				{
				AL_RunnerClass.AZ_actions.moveToElement(AL_RunnerClass.AZ_driver.findElement(Locators.initialFee)).build().perform();
				AL_RunnerClass.AZ_driver.findElement(Locators.initialFee).click();
				Thread.sleep(1000);
				AL_PropertyWare.clearTextField();
				//AZ_RunnerClass.AZ_actions.click(AL_RunnerClass.AZ_driver.findElement(Locators.initialFee)).sendKeys(Keys.SHIFT).sendKeys(Keys.HOME).sendKeys(Keys.BACK_SPACE).build().perform();
				AL_RunnerClass.AZ_driver.findElement(Locators.initialFee).sendKeys(AL_PropertyWare.lateChargeFee);
				}
			}
			catch(Exception e)
			{
				InsertDataIntoDatabase.notAutomatedFields(RunnerClass.leaseName, "Late Charge Fee"+'\n');
				temp=1;
			}
			Thread.sleep(2000);
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
				InsertDataIntoDatabase.notAutomatedFields(RunnerClass.leaseName, "Initial fee Dropdown"+'\n');
				temp=1;
			}
			
			//Per Day Fee
			Thread.sleep(2000);
			try
			{
				if(AL_PropertyWare.additionalLateCharges.equalsIgnoreCase("Error"))
				{
					InsertDataIntoDatabase.notAutomatedFields(RunnerClass.leaseName, "Late Charge Fee Per Day"+'\n');
					temp=1;
				}
				else
				{
				AL_RunnerClass.AZ_actions.moveToElement(AL_RunnerClass.AZ_driver.findElement(Locators.perDayFee)).build().perform();
				AL_RunnerClass.AZ_driver.findElement(Locators.perDayFee).click();
				Thread.sleep(1000);
				AL_PropertyWare.clearTextField();
				//AZ_RunnerClass.AZ_actions.click(AL_RunnerClass.AZ_driver.findElement(Locators.perDayFee)).sendKeys(Keys.SHIFT).sendKeys(Keys.HOME).sendKeys(Keys.BACK_SPACE).build().perform();
				AL_RunnerClass.AZ_driver.findElement(Locators.perDayFee).sendKeys(AL_PropertyWare.additionalLateCharges);
				}
			}
			catch(Exception e)
			{
				e.printStackTrace();
				InsertDataIntoDatabase.notAutomatedFields(RunnerClass.leaseName, "Late Charge Fee Per Day"+'\n');
				temp=1;
			}

			Thread.sleep(2000);
			try
			{
			Select perDayFeeDropdown = new Select(AL_RunnerClass.AZ_driver.findElement(Locators.perDayFeeDropdown)) ;
			perDayFeeDropdown.selectByVisibleText("Fixed Amount");
			}
			catch(Exception e)
			{
				InsertDataIntoDatabase.notAutomatedFields(RunnerClass.leaseName, "Late Charge Fee Per Day Dropdown"+'\n');
				temp=1;
			}
			
			//Maximum
			Thread.sleep(2000);
			try
			{
			Select maximumDropdown = new Select(AL_RunnerClass.AZ_driver.findElement(Locators.maximumYesNoDropdown)) ;
			maximumDropdown.selectByVisibleText("Yes");
			}
			catch(Exception e)
			{
				InsertDataIntoDatabase.notAutomatedFields(RunnerClass.leaseName, "Late Fee Maximum dropdown"+'\n');
				temp=1;
			}
			// Additional Late charges Limit
			
			String maximumLimitDropdown = "";
			if(AL_PropertyWare.additionalLateChargesLimit.contains("30"))
			{
				AL_PropertyWare.additionalLateChargesLimit = "12";
			    maximumLimitDropdown = "% of Rent Charges";
			}
			Thread.sleep(2000);
			try
			{
				if(AL_PropertyWare.additionalLateChargesLimit.equalsIgnoreCase("Error"))
				{
					InsertDataIntoDatabase.notAutomatedFields(RunnerClass.leaseName, "Late Charge Fee Limit"+'\n');
					temp=1;
				}
				else
				{
					AL_RunnerClass.AZ_driver.findElement(Locators.maximumDatField).clear();
					AL_RunnerClass.AZ_driver.findElement(Locators.maximumDatField).click();
					Thread.sleep(1000);
					AL_PropertyWare.clearTextField();
					//AZ_RunnerClass.AZ_actions.click(AL_RunnerClass.AZ_driver.findElement(Locators.maximumDatField)).sendKeys(Keys.SHIFT).sendKeys(Keys.HOME).sendKeys(Keys.BACK_SPACE).build().perform();
					AL_RunnerClass.AZ_driver.findElement(Locators.maximumDatField).sendKeys(AL_PropertyWare.additionalLateChargesLimit);
				}
			}
			catch(Exception e)
			{
				InsertDataIntoDatabase.notAutomatedFields(RunnerClass.leaseName, "Late Charge Fee Limit"+'\n');
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
				InsertDataIntoDatabase.notAutomatedFields(RunnerClass.leaseName, "Maximum Limit Dropdown 2"+'\n');
				temp=1;
			}
			
			Thread.sleep(2000);
			AL_RunnerClass.AZ_js.executeScript("window.scrollTo(0,document.body.scrollHeight)");
			try
			{
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
			RunnerClass.leaseCompletedStatus = 2;
			e.printStackTrace();
			return false;
		}
		
	}
	
	

	

}
