package mainPackage;

import org.openqa.selenium.Keys;
import org.openqa.selenium.support.ui.Select;

import Alabama.Locators;

public class LateFeeRule 
{
	public static void lateFeeRule(String lateFeeRuleType) throws Exception
	{
		switch(lateFeeRuleType)
		{
		case "GreaterOfFlatFeeOrPercentage":
			LateFeeRule.greaterOfFlatFeeOrPercentage();
			break;
		case "initialFeePluPerDayFee":
			LateFeeRule.initialFeePluPerDayFee();
			break;
			
		}
	}
	
	public static boolean greaterOfFlatFeeOrPercentage() throws Exception
	{
		try
		{
	    RunnerClass.actions.moveToElement(RunnerClass.driver.findElement(Locators.lateFeeType)).build().perform();
		Select feeType = new Select(RunnerClass.driver.findElement(Locators.lateFeeType));
		feeType.selectByVisibleText("Greater of Flat Fee or Percentage");
		}
		catch(Exception e)
		{
			InsertDataIntoDatabase.notAutomatedFields(RunnerClass.leaseName, "Late Charges - Late Fee Rule"+'\n');
			return false;
		}
		Thread.sleep(500);
		try
		{
		RunnerClass.actions.moveToElement(RunnerClass.driver.findElement(Locators.lateFeeDueDay)).build().perform();
		Select dueDayList = new Select(RunnerClass.driver.findElement(Locators.lateFeeDueDay)) ;
		dueDayList.selectByVisibleText(RunnerClass.dueDay_GreaterOf);
		}
		catch(Exception e)
		{
			InsertDataIntoDatabase.notAutomatedFields(RunnerClass.leaseName, "Late Charges - Late Fee Due Day"+'\n');
		}
		Thread.sleep(500);
		try
		{
		RunnerClass.actions.moveToElement(RunnerClass.driver.findElement(Locators.flatFee)).build().perform();
		RunnerClass.driver.findElement(Locators.flatFee).click();
		//RunnerClass.driver.findElement(Locators.flatFee).clear();
		//RunnerClass.keyBoardActions();
		RunnerClass.driver.findElement(Locators.flatFee).sendKeys(Keys.chord(Keys.CONTROL,"a", Keys.DELETE));
		RunnerClass.driver.findElement(Locators.flatFee).sendKeys(RunnerClass.flatFee);
		}
		catch(Exception e)
		{
			InsertDataIntoDatabase.notAutomatedFields(RunnerClass.leaseName, "Late Charges - Flat Fee"+'\n');
		}
		Thread.sleep(500);
		try
		{
		RunnerClass.actions.moveToElement(RunnerClass.driver.findElement(Locators.lateFeePercentage)).build().perform();
		RunnerClass.driver.findElement(Locators.lateFeePercentage).click();
		//RunnerClass.keyBoardActions();
		RunnerClass.driver.findElement(Locators.lateFeePercentage).sendKeys(Keys.chord(Keys.CONTROL,"a", Keys.DELETE));
		RunnerClass.actions.sendKeys(Keys.BACK_SPACE).sendKeys(Keys.BACK_SPACE).sendKeys(Keys.BACK_SPACE).sendKeys(Keys.BACK_SPACE).sendKeys(Keys.BACK_SPACE).build().perform();
		//RunnerClass.actions.sendKeys(Keys.END).sendKeys(Keys.SHIFT).sendKeys(Keys.HOME).sendKeys(Keys.BACK_SPACE).build().perform();
		RunnerClass.driver.findElement(Locators.lateFeePercentage).sendKeys(RunnerClass.percentage);
		}
		catch(Exception e)
		{
			InsertDataIntoDatabase.notAutomatedFields(RunnerClass.leaseName, "Late Charges - Percentage"+'\n');
		}
		return true;
	}
	
	public static boolean initialFeePluPerDayFee() throws Exception
	{
		try
		{
	    RunnerClass.actions.moveToElement(RunnerClass.driver.findElement(Locators.lateFeeType)).build().perform();
		Select feeType = new Select(RunnerClass.driver.findElement(Locators.lateFeeType));
		feeType.selectByVisibleText("Initial Fee + Per Day Fee");
		}
		catch(Exception e)
		{
			InsertDataIntoDatabase.notAutomatedFields(RunnerClass.leaseName, "Late Charges - Late Fee Rule"+'\n');
			return false;
		}
		Thread.sleep(500);
		//Late Charges
//		Thread.sleep(2000);
		try
		{
		if(RunnerClass.additionalLateChargesLimit.contains("375"))
		{
			RunnerClass.dueDay_initialFee = "2";
		}
		else RunnerClass.dueDay_initialFee = String.valueOf(RunnerClass.dueDay_initialFee.trim().charAt(0));
		}
		catch(Exception e) {}
		try
		{
			if(RunnerClass.dueDay_initialFee.equalsIgnoreCase("Error"))
			{
				InsertDataIntoDatabase.notAutomatedFields(RunnerClass.leaseName, "Late Charges - Late Charge Day"+'\n');
				//temp=1;
			}
			else
			{
			RunnerClass.actions.moveToElement(RunnerClass.driver.findElement(Locators.lateFeeDueDay)).build().perform();
			Select dueDayList = new Select(RunnerClass.driver.findElement(Locators.lateFeeDueDay)) ;
			dueDayList.selectByVisibleText(RunnerClass.dueDay_initialFee.trim());
			}
		}
		catch(Exception e)
		{
			InsertDataIntoDatabase.notAutomatedFields(RunnerClass.leaseName, "Late Charges - Late Charge Day"+'\n');
			//temp=1;
		}
		// Initial Fee
		Thread.sleep(500);
		try
		{
			if(RunnerClass.initialFeeAmount.equalsIgnoreCase("Error"))
			{
				InsertDataIntoDatabase.notAutomatedFields(RunnerClass.leaseName, "Late Charges - Late Charge Fee"+'\n');
				//temp=1;
			}
			else
			{
			RunnerClass.actions.moveToElement(RunnerClass.driver.findElement(Locators.initialFee)).build().perform();
			RunnerClass.driver.findElement(Locators.initialFee).click();
			Thread.sleep(1000);
			RunnerClass.driver.findElement(Locators.initialFee).sendKeys(Keys.chord(Keys.CONTROL,"a", Keys.DELETE));
			//TN_PropertyWare.clearTextField();
			//RunnerClass.actions.click(RunnerClass.driver.findElement(Locators.initialFee)).sendKeys(Keys.SHIFT).sendKeys(Keys.HOME).sendKeys(Keys.BACK_SPACE).build().perform();
			RunnerClass.driver.findElement(Locators.initialFee).sendKeys(RunnerClass.initialFeeAmount);
			}
		}
		catch(Exception e)
		{
			InsertDataIntoDatabase.notAutomatedFields(RunnerClass.leaseName, "Late Charges - Late Charge Fee"+'\n');
			//temp=1;
		}
		Thread.sleep(500);
		//Initial Fee Dropdown
		
		try
		{
			if(RunnerClass.initialFeeAmount.contains("%"))
			{
			Select initialDropdown = new Select(RunnerClass.driver.findElement(Locators.initialFeeDropdown)) ;
			initialDropdown.selectByVisibleText("% of Rent Charges");
			}
			else 
			{
				Select initialDropdown = new Select(RunnerClass.driver.findElement(Locators.initialFeeDropdown)) ;
				initialDropdown.selectByVisibleText("Fixed Amount");
			}
		}
		catch(Exception e)
		{
			InsertDataIntoDatabase.notAutomatedFields(RunnerClass.leaseName, "Late Charges - Initial fee Dropdown"+'\n');
			//temp=1;
		}
		
		//Per Day Fee
		Thread.sleep(500);
		try
		{
			if(RunnerClass.perDayFeeAmount.equalsIgnoreCase("Error"))
			{
				InsertDataIntoDatabase.notAutomatedFields(RunnerClass.leaseName, "Late Charges - Late Charge Fee Per Day"+'\n');
				//temp=1;
			}
			else
			{
			RunnerClass.actions.moveToElement(RunnerClass.driver.findElement(Locators.perDayFee)).build().perform();
			RunnerClass.driver.findElement(Locators.perDayFee).click();
			Thread.sleep(1000);
			RunnerClass.driver.findElement(Locators.perDayFee).sendKeys(Keys.chord(Keys.CONTROL,"a", Keys.DELETE));
			//TN_PropertyWare.clearTextField();
			//RunnerClass.actions.click(RunnerClass.driver.findElement(Locators.perDayFee)).sendKeys(Keys.SHIFT).sendKeys(Keys.HOME).sendKeys(Keys.BACK_SPACE).build().perform();
			RunnerClass.driver.findElement(Locators.perDayFee).sendKeys(RunnerClass.perDayFeeAmount);
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
			InsertDataIntoDatabase.notAutomatedFields(RunnerClass.leaseName, "Late Charges - Late Charge Fee Per Day"+'\n');
			//temp=1;
		}
        //Per Day Fee Dropdown
		Thread.sleep(500);
		try
		{
		Select perDayFeeDropdown = new Select(RunnerClass.driver.findElement(Locators.perDayFeeDropdown)) ;
		perDayFeeDropdown.selectByVisibleText("Fixed Amount");
		}
		catch(Exception e)
		{
			InsertDataIntoDatabase.notAutomatedFields(RunnerClass.leaseName, "Late Charges - Late Charge Fee Per Day Dropdown"+'\n');
			//temp=1;
		}
		if(RunnerClass.PDFFormatType.equalsIgnoreCase("Format1"))
	    {
		
			//Maximum
			Thread.sleep(500);
			try
			{
			Select maximumDropdown = new Select(RunnerClass.driver.findElement(Locators.maximumYesNoDropdown)) ;
			maximumDropdown.selectByVisibleText("Yes");
			}
			catch(Exception e)
			{
				InsertDataIntoDatabase.notAutomatedFields(RunnerClass.leaseName, "Late Charges - Late Fee Maximum dropdown"+'\n');
				//temp=1;
			}
			// Additional Late charges Limit
			
			String maximumLimitDropdown = "";
			if(RunnerClass.additionalLateChargesLimit.contains("30"))
			{
				RunnerClass.additionalLateChargesLimit = "12";
			    maximumLimitDropdown = "% of Rent Charges";
			}
			else
				maximumLimitDropdown = "Fixed Amount";
			Thread.sleep(500);
			try
			{
				if(RunnerClass.additionalLateChargesLimit.equalsIgnoreCase("Error"))
				{
					InsertDataIntoDatabase.notAutomatedFields(RunnerClass.leaseName, "Late Charges - Late Charge Fee Limit"+'\n');
					//temp=1;
				}
				else
				{
					RunnerClass.driver.findElement(Locators.maximumDatField).sendKeys(Keys.chord(Keys.CONTROL,"a", Keys.DELETE));
					RunnerClass.driver.findElement(Locators.maximumDatField).clear();
					RunnerClass.driver.findElement(Locators.maximumDatField).click();
					Thread.sleep(1000);
					//TN_PropertyWare.clearTextField();
					//RunnerClass.actions.click(RunnerClass.driver.findElement(Locators.maximumDatField)).sendKeys(Keys.SHIFT).sendKeys(Keys.HOME).sendKeys(Keys.BACK_SPACE).build().perform();
					RunnerClass.driver.findElement(Locators.maximumDatField).sendKeys(RunnerClass.additionalLateChargesLimit);
				}
			}
			catch(Exception e)
			{
				InsertDataIntoDatabase.notAutomatedFields(RunnerClass.leaseName, "Late Charges - Late Charge Fee Limit"+'\n');
				//temp=1;
			}
			Thread.sleep(500);
			try
			{
			Select maximumDropdown2 = new Select(RunnerClass.driver.findElement(Locators.maximumDropdown2)) ;
			maximumDropdown2.selectByVisibleText(maximumLimitDropdown);
			}
			catch(Exception e)
			{
				InsertDataIntoDatabase.notAutomatedFields(RunnerClass.leaseName, "Late Charges - Maximum Limit Dropdown 2"+'\n');
				//temp=1;
			}
	    }
		Thread.sleep(500);
		return true;
	}

}
