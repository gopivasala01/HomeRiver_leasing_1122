package Arkansas;

import org.openqa.selenium.Keys;
import org.openqa.selenium.support.ui.Select;

import mainPackage.InsertDataIntoDatabase;
import mainPackage.Locators;
import mainPackage.RunnerClass;

public class AZ_InsertDataIntoPW_New 
{
	public static String CDEType;
	public boolean insertData() throws Exception
	{
		try
		{
		String CDETypeRaw = AL_RunnerClass.AZ_driver.findElement(Locators.getLeaseCDEType).getText();
		CDEType =CDETypeRaw.split(System.lineSeparator())[1].split(" ")[0];// CDETypeRaw.split("[(]+")[0].trim().split(" ")[CDETypeRaw.split("[(]+")[0].trim().split(" ").length-1];
		System.out.println("CDE Type : "+CDEType);
		}
		catch(Exception e)
		{
			RunnerClass.leaseCompletedStatus =2;
			return false;
		}
		
		// Move In Charges
		
		
		
		return true;

	}

}



/*
int temp=0;
try
{
	try
	{
	AZ_RunnerClass.AZ_driver.navigate().refresh();
	AZ_RunnerClass.AZ_driver.findElement(Locators.ledgerTab).click();
	}
	catch(Exception e)
	{
		if(AZ_RunnerClass.AZ_driver.findElement(Locators.popUpAfterClickingLeaseName).isDisplayed())
		{
			AZ_RunnerClass.AZ_driver.findElement(Locators.popupClose).click();
			//AZ_RunnerClass.AZ_driver.navigate().refresh();
			AZ_RunnerClass.AZ_js.executeScript("window.scrollBy(document.body.scrollHeight,0)");
			AZ_RunnerClass.AZ_driver.findElement(Locators.ledgerTab).click();
		}
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
	moveInCharges[0][2]=AZ_PropertyWare.proratedRent;
	moveInCharges[0][3]=RunnerClass.convertDate(AZ_PropertyWare.commensementDate);
	
	moveInCharges[1][0]=AppConfig.fullMonthRent_AccountCode;
	moveInCharges[1][1]=AppConfig.fullMonthRent_AccountRef;
	moveInCharges[1][2]=AZ_PropertyWare.monthlyRent;
	moveInCharges[1][3]=RunnerClass.convertDate(AZ_PropertyWare.commensementDate);
	
	moveInCharges[2][0]=AppConfig.adminFee_AccountCode;
	moveInCharges[2][1]=AppConfig.adminFee_AccountRef;
	moveInCharges[2][2]=AZ_PropertyWare.adminFee;
	moveInCharges[2][3]=RunnerClass.convertDate(AZ_PropertyWare.commensementDate);
	
	if(ExtractDataFromPDF.petFlag==true)
	{
	moveInCharges[3][0]=AppConfig.proratedPetRent_AccountCode;
	moveInCharges[3][1]=AppConfig.proratedPetRent_AccountRef;
	moveInCharges[3][2]=AZ_PropertyWare.proratedPetRent;
	moveInCharges[3][3]=RunnerClass.convertDate(AZ_PropertyWare.commensementDate);
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
	autoCharges[0][2]=AZ_PropertyWare.airFilterFee;
	if(AZ_PropertyWare.proratedRentDate.contains("n/a"))
		autoCharges[0][3]= " ";
	else
	autoCharges[0][3]=RunnerClass.convertDate(AZ_PropertyWare.proratedRentDate);
	
	if(ExtractDataFromPDF.petFlag==true)
	{
	autoCharges[1][0]=AppConfig.proratedPetRent_AccountCode;
	autoCharges[1][1]=AppConfig.proratedPetRent_AccountRef;
	autoCharges[1][2]=AZ_PropertyWare.monthlyRent;
	if(AZ_PropertyWare.proratedRentDate.contains("n/a"))
		autoCharges[1][3]= " ";
	else
	autoCharges[1][3]=RunnerClass.convertDate(AZ_PropertyWare.proratedRentDate);
	
	autoCharges[2][0]=AppConfig.petRent_AccountCode;
	autoCharges[2][1]=AppConfig.petRent_AccountRef;
	autoCharges[2][2]=AZ_PropertyWare.petRent;
	if(AZ_PropertyWare.proratedRentDate.contains("n/a"))
		autoCharges[2][3]= " ";
	else
	autoCharges[2][3]=RunnerClass.convertDate(AZ_PropertyWare.proratedRentDate);
	}
	
	System.out.println("------Move In Charges-------");
	for(int i=0;i<moveInCharges.length;i++)
	{
		for(int j=0;j<=i;j++)
		{
			try
			{
		System.out.println(moveInCharges[i][j]+" "+moveInCharges[i][j+1]+" "+moveInCharges[i][j+2]+" "+moveInCharges[i][j+3]);
		AZ_RunnerClass.AZ_driver.findElement(Locators.newCharge).click();
		Thread.sleep(2000);
		Select AutoChargesDropdown = new Select(AZ_RunnerClass.AZ_driver.findElement(Locators.accountDropdown));
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
		AZ_RunnerClass.AZ_driver.findElement(Locators.referenceName).sendKeys(moveInCharges[i][j+1]);
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
				AZ_RunnerClass.AZ_driver.findElement(Locators.moveInChargeAmount).click();
				AZ_RunnerClass.AZ_actions.sendKeys(Keys.BACK_SPACE).sendKeys(Keys.BACK_SPACE).sendKeys(Keys.BACK_SPACE).sendKeys(Keys.BACK_SPACE).sendKeys(Keys.BACK_SPACE).build().perform();
				Thread.sleep(2000);
				AZ_RunnerClass.AZ_driver.findElement(Locators.moveInChargeAmount).sendKeys(moveInCharges[i][j+2]);
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
			AZ_RunnerClass.AZ_driver.findElement(Locators.moveInChargeDate).clear();
			Thread.sleep(2000);
			AZ_RunnerClass.AZ_driver.findElement(Locators.moveInChargeDate).sendKeys(moveInCharges[i][j+3]);
			Thread.sleep(2000);
			}
		}
		catch(Exception e)
		{
			InsertDataIntoDatabase.notAutomatedFields(RunnerClass.leaseName, "Move In Charge Date");
			temp=1;
		}
		AZ_RunnerClass.AZ_driver.findElement(Locators.moveInChargeCancel).click();
		AZ_RunnerClass.AZ_driver.navigate().refresh();
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
	AZ_RunnerClass.AZ_driver.findElement(Locators.summaryTab).click();
	Thread.sleep(5000);
	AZ_RunnerClass.AZ_driver.findElement(Locators.summaryEditButton).click();
	AZ_RunnerClass.AZ_actions.moveToElement(AZ_RunnerClass.AZ_driver.findElement(Locators.newAutoCharge)).build().perform();
	
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
				AZ_RunnerClass.AZ_driver.findElement(Locators.newAutoCharge).click();
				Thread.sleep(2000);
				
				Select autoChargesDropdown = new Select(AZ_RunnerClass.AZ_driver.findElement(Locators.accountDropdown));
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
			AZ_RunnerClass.AZ_driver.findElement(Locators.autoCharge_StartDate).clear();
			Thread.sleep(2000);
			AZ_RunnerClass.AZ_driver.findElement(Locators.autoCharge_StartDate).sendKeys(autoCharges[i][j+3]);
			}
			}
			catch(Exception e)
			{
				InsertDataIntoDatabase.notAutomatedFields(RunnerClass.leaseName, "Auto Charge Start Date");
				temp=1;
			}
			try
			{
			AZ_RunnerClass.AZ_driver.findElement(Locators.autoCharge_refField).click();
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
				AZ_RunnerClass.AZ_driver.findElement(Locators.autoCharge_Amount).click();
				AZ_RunnerClass.AZ_actions.sendKeys(Keys.BACK_SPACE).sendKeys(Keys.BACK_SPACE).sendKeys(Keys.BACK_SPACE).sendKeys(Keys.BACK_SPACE).sendKeys(Keys.BACK_SPACE).build().perform();
				AZ_RunnerClass.AZ_driver.findElement(Locators.autoCharge_Amount).sendKeys(autoCharges[i][j+2]);
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
			AZ_RunnerClass.AZ_driver.findElement(Locators.autoCharge_Description).sendKeys(autoCharges[i][j+1]);
			}
			catch(Exception e)
			{
				InsertDataIntoDatabase.notAutomatedFields(RunnerClass.leaseName, "Auto Charge Description");
				temp=1;
			}
			Thread.sleep(2000);
			AZ_RunnerClass.AZ_driver.findElement(Locators.autoCharge_CancelButton).click();
			
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
		if(AZ_PropertyWare.RCDetails.equalsIgnoreCase("Error"))
		{
			InsertDataIntoDatabase.notAutomatedFields(RunnerClass.leaseName, "RC Details");
			temp=1;
		}
		else
		{
		AZ_RunnerClass.AZ_actions.moveToElement(AZ_RunnerClass.AZ_driver.findElement(Locators.rcField)).build().perform();
		AZ_RunnerClass.AZ_driver.findElement(Locators.rcField).sendKeys(AZ_PropertyWare.RCDetails);
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
		if(AZ_PropertyWare.earlyTermiantion.equalsIgnoreCase("Error"))
		{
			InsertDataIntoDatabase.notAutomatedFields(RunnerClass.leaseName, "Early Termination");
			temp=1;
		}
		else
		{
		if(AZ_PropertyWare.earlyTermiantion.contains("2"))
		{
			AZ_RunnerClass.AZ_actions.moveToElement(AZ_RunnerClass.AZ_driver.findElement(Locators.earlyTermFee2x)).build().perform();
			AZ_RunnerClass.AZ_driver.findElement(Locators.earlyTermFee2x).click();
			AZ_RunnerClass.AZ_driver.findElement(Locators.earlyTermination_Yes).click();
		}
		}
	}
	catch(Exception e)
	{
		InsertDataIntoDatabase.notAutomatedFields(RunnerClass.leaseName, "Early Termination");
		temp=1;
	}
	//Enrolled in FilterEasy
	try
	{
	AZ_RunnerClass.AZ_actions.moveToElement(AZ_RunnerClass.AZ_driver.findElement(Locators.enrolledInFilterEasy)).build().perform();
	AZ_RunnerClass.AZ_driver.findElement(Locators.enrolledInFilterEasy).click();
	AZ_RunnerClass.AZ_driver.findElement(Locators.enrolledInFilterEasy_Yes).click();
	}
	catch(Exception e)
	{
		InsertDataIntoDatabase.notAutomatedFields(RunnerClass.leaseName, "Enrolled in FilterEasy");
		temp=1;
	}
	//Needs New Lease - No by default
	try
	{
	AZ_RunnerClass.AZ_actions.moveToElement(AZ_RunnerClass.AZ_driver.findElement(Locators.needsNewLease)).build().perform();
	AZ_RunnerClass.AZ_driver.findElement(Locators.needsNewLease).click();
	AZ_RunnerClass.AZ_driver.findElement(Locators.needsNewLease_No).click();
	}
	catch(Exception e)
	{
		InsertDataIntoDatabase.notAutomatedFields(RunnerClass.leaseName, "Needs New Lease");
		temp=1;
	}
	//Lease Occupants
	try
	{
		if(AZ_PropertyWare.occupants.equalsIgnoreCase("Error"))
		{
			InsertDataIntoDatabase.notAutomatedFields(RunnerClass.leaseName, "Lease Occupants");
			temp=1;
		}
		else
		{
		AZ_RunnerClass.AZ_actions.moveToElement(AZ_RunnerClass.AZ_driver.findElement(Locators.leaseOccupants)).build().perform();
		AZ_RunnerClass.AZ_driver.findElement(Locators.leaseOccupants).sendKeys(AZ_PropertyWare.occupants);
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
			if(AZ_PropertyWare.pet1Type.equalsIgnoreCase("Error")||AZ_PropertyWare.pet2Type.equalsIgnoreCase("Error"))
			{
				InsertDataIntoDatabase.notAutomatedFields(RunnerClass.leaseName, "Pet Types");
				temp=1;
			}
			else 
			{
				AZ_RunnerClass.AZ_actions.moveToElement(AZ_RunnerClass.AZ_driver.findElement(Locators.pet1Type)).build().perform();
				AZ_RunnerClass.AZ_driver.findElement(Locators.pet1Type).sendKeys(AZ_PropertyWare.pet2Type+","+AZ_PropertyWare.pet2Type);
			}
		}
		catch(Exception e)
		{
			InsertDataIntoDatabase.notAutomatedFields(RunnerClass.leaseName, "Pet Types");
			temp=1;
		}
		try
		{
			if(AZ_PropertyWare.pet1Breed.equalsIgnoreCase("Error")||AZ_PropertyWare.pet2Breed.equalsIgnoreCase("Error"))
			{
				InsertDataIntoDatabase.notAutomatedFields(RunnerClass.leaseName, "Pet Breeds");
				temp=1;
			}
			else
			{
				AZ_RunnerClass.AZ_actions.moveToElement(AZ_RunnerClass.AZ_driver.findElement(Locators.pet1Breed)).build().perform();
				AZ_RunnerClass.AZ_driver.findElement(Locators.pet1Breed).sendKeys(AZ_PropertyWare.pet1Breed+","+AZ_PropertyWare.pet1Breed);
			}
		}
		catch(Exception e)
		{
			InsertDataIntoDatabase.notAutomatedFields(RunnerClass.leaseName, "Pet Breeds");
			temp=1;
		}
		try
		{
			if(AZ_PropertyWare.pet1Weight.equalsIgnoreCase("Error")||AZ_PropertyWare.pet2Weight.equalsIgnoreCase("Error"))
			{
				InsertDataIntoDatabase.notAutomatedFields(RunnerClass.leaseName, "Pet Weights");
				temp=1;
			}
			else
			{
			AZ_RunnerClass.AZ_actions.moveToElement(AZ_RunnerClass.AZ_driver.findElement(Locators.pet1Weight)).build().perform();
			AZ_RunnerClass.AZ_driver.findElement(Locators.pet1Weight).sendKeys(AZ_PropertyWare.pet1Weight+","+AZ_PropertyWare.pet2Weight);
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
			if(AZ_PropertyWare.serviceAnimalType.equalsIgnoreCase("Error"))
			{
				InsertDataIntoDatabase.notAutomatedFields(RunnerClass.leaseName, "Service Animal Type");
				temp=1;
			}
			else
			{
			AZ_RunnerClass.AZ_actions.moveToElement(AZ_RunnerClass.AZ_driver.findElement(Locators.serviceAnimalType)).build().perform();
			AZ_RunnerClass.AZ_driver.findElement(Locators.serviceAnimalType).sendKeys(AZ_PropertyWare.serviceAnimalType);
			}
		}
		catch(Exception e)
		{
			InsertDataIntoDatabase.notAutomatedFields(RunnerClass.leaseName, "Service Animal Type");
			temp=1;
		}
		try
		{
			if(AZ_PropertyWare.serviceAnimalBreed.equalsIgnoreCase("Error"))
			{
				InsertDataIntoDatabase.notAutomatedFields(RunnerClass.leaseName, "Service Animal Breed");
				temp=1;
			}
			else
			{
			AZ_RunnerClass.AZ_actions.moveToElement(AZ_RunnerClass.AZ_driver.findElement(Locators.serviceAnimalBreed)).build().perform();
			AZ_RunnerClass.AZ_driver.findElement(Locators.serviceAnimalBreed).sendKeys(AZ_PropertyWare.serviceAnimalBreed);
			}
		}
		catch(Exception e)
		{
			InsertDataIntoDatabase.notAutomatedFields(RunnerClass.leaseName, "Service Animal Breed");
			temp=1;
		}
		try
		{
			if(AZ_PropertyWare.serviceAnimalWeight.equalsIgnoreCase("Error"))
			{
				InsertDataIntoDatabase.notAutomatedFields(RunnerClass.leaseName, "Service Animal Weight");
				temp=1;
			}
			else
			{
			AZ_RunnerClass.AZ_actions.moveToElement(AZ_RunnerClass.AZ_driver.findElement(Locators.serviceAnimalWeight)).build().perform();
			AZ_RunnerClass.AZ_driver.findElement(Locators.serviceAnimalWeight).sendKeys(AZ_PropertyWare.serviceAnimalWeight);
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
			if(AZ_PropertyWare.petRent.equalsIgnoreCase("Error"))
			{
				InsertDataIntoDatabase.notAutomatedFields(RunnerClass.leaseName, "pet Rent");
				temp=1;
			}
			else
			{
			AZ_RunnerClass.AZ_actions.moveToElement(AZ_RunnerClass.AZ_driver.findElement(Locators.petAmount)).build().perform();
			AZ_RunnerClass.AZ_driver.findElement(Locators.petAmount).sendKeys(AZ_PropertyWare.petRent);
			}
		}
		catch(Exception e)
		{
			InsertDataIntoDatabase.notAutomatedFields(RunnerClass.leaseName, "pet Rent");
			temp=1;
		}
		try
		{
			if(AZ_PropertyWare.petRent.equalsIgnoreCase("Error"))
			{
				InsertDataIntoDatabase.notAutomatedFields(RunnerClass.leaseName, "pet One Time Non-Refundable Fee");
				temp=1;
			}
			else
			{
			AZ_RunnerClass.AZ_actions.moveToElement(AZ_RunnerClass.AZ_driver.findElement(Locators.tenantOneTimePetFee)).build().perform();
			AZ_RunnerClass.AZ_driver.findElement(Locators.tenantOneTimePetFee).sendKeys(AZ_PropertyWare.petOneTimeNonRefundableFee);
			}
		}
		catch(Exception e)
		{
			InsertDataIntoDatabase.notAutomatedFields(RunnerClass.leaseName, "pet One Time Non-Refundable Fee");
			temp=1;
		}
	if(AZ_PropertyWare.countOfTypeWordInText>2)
	{
		try
		{
			AZ_RunnerClass.AZ_actions.moveToElement(AZ_RunnerClass.AZ_driver.findElement(Locators.petSpecialProvisions)).build().perform();
			AZ_RunnerClass.AZ_driver.findElement(Locators.petSpecialProvisions).sendKeys(AppConfig.petSpecialProvisions);
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
		if(AZ_PropertyWare.lateChargeDay.equalsIgnoreCase("Error"))
		{
			InsertDataIntoDatabase.notAutomatedFields(RunnerClass.leaseName, "Late Charge Day");
			temp=1;
		}
		else
		{
		AZ_RunnerClass.AZ_actions.moveToElement(AZ_RunnerClass.AZ_driver.findElement(Locators.lateFeeDueDay)).build().perform();
		Select dueDayList = new Select(AZ_RunnerClass.AZ_driver.findElement(Locators.lateFeeDueDay)) ;
		dueDayList.selectByVisibleText(AZ_PropertyWare.lateChargeDay.trim());
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
		if(AZ_PropertyWare.lateChargeFee.equalsIgnoreCase("Error"))
		{
			InsertDataIntoDatabase.notAutomatedFields(RunnerClass.leaseName, "Late Charge Fee");
			temp=1;
		}
		else
		{
		AZ_RunnerClass.AZ_actions.moveToElement(AZ_RunnerClass.AZ_driver.findElement(Locators.initialFee)).build().perform();
		AZ_RunnerClass.AZ_driver.findElement(Locators.initialFee).sendKeys(AZ_PropertyWare.lateChargeFee);
		}
	}
	catch(Exception e)
	{
		InsertDataIntoDatabase.notAutomatedFields(RunnerClass.leaseName, "Late Charge Fee");
		temp=1;
	}
	try
	{
	Select initialDropdown = new Select(AZ_RunnerClass.AZ_driver.findElement(Locators.initialFeeDropdown)) ;
	initialDropdown.selectByVisibleText("Fixed Amount");
	}
	catch(Exception e)
	{
		InsertDataIntoDatabase.notAutomatedFields(RunnerClass.leaseName, "Initial fee Dropdown");
		temp=1;
	}
	
	//Per Day Fee
	try
	{
		if(AZ_PropertyWare.lateFeeChargePerDay.equalsIgnoreCase("Error"))
		{
			InsertDataIntoDatabase.notAutomatedFields(RunnerClass.leaseName, "Late Charge Fee Per Day");
			temp=1;
		}
		else
		{
		AZ_RunnerClass.AZ_actions.moveToElement(AZ_RunnerClass.AZ_driver.findElement(Locators.perDayFee)).build().perform();
		AZ_RunnerClass.AZ_driver.findElement(Locators.perDayFee).sendKeys(AZ_PropertyWare.lateFeeChargePerDay);
		}
	}
	catch(Exception e)
	{
		InsertDataIntoDatabase.notAutomatedFields(RunnerClass.leaseName, "Late Charge Fee Per Day");
		temp=1;
	}

	try
	{
	Select perDayFeeDropdown = new Select(AZ_RunnerClass.AZ_driver.findElement(Locators.perDayFeeDropdown)) ;
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
	Select maximumDropdown = new Select(AZ_RunnerClass.AZ_driver.findElement(Locators.maximumYesNoDropdown)) ;
	maximumDropdown.selectByVisibleText("Yes");
	}
	catch(Exception e)
	{
		InsertDataIntoDatabase.notAutomatedFields(RunnerClass.leaseName, "Late Fee Maximum dropdown");
		temp=1;
	}
	try
	{
		if(AZ_PropertyWare.additionalLateChargesLimit.equalsIgnoreCase("Error"))
		{
			InsertDataIntoDatabase.notAutomatedFields(RunnerClass.leaseName, "Late Charge Fee Limit");
			temp=1;
		}
		else
		{
			AZ_RunnerClass.AZ_driver.findElement(Locators.maximumDatField).sendKeys(AZ_PropertyWare.additionalLateChargesLimit);
		}
	}
	catch(Exception e)
	{
		InsertDataIntoDatabase.notAutomatedFields(RunnerClass.leaseName, "Late Charge Fee Limit");
		temp=1;
	}
	try
	{
	Select maximumDropdown2 = new Select(AZ_RunnerClass.AZ_driver.findElement(Locators.maximumDropdown2)) ;
	maximumDropdown2.selectByVisibleText("Fixed Amount");
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

*/