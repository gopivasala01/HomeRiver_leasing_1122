package mainPackage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.WebDriverWait;

import Alabama.AL_PropertyWare;
import Alabama.AL_RunnerClass;
import Arizona.AZ_RunnerClass;
import Arkansas.AR_RunnerClass;
import DallasFortWorth.DFW_RunnerClass;
import Florida.FL_RunnerClass;
import Georgia.GA_RunnerClass;
import Indiana.IN_RunnerClass;
import LittleRock.LR_RunnerClass;
import NorthCarolina.NC_RunnerClass;
import OKC.OKC_RunnerClass;
import SanAntonio.SA_RunnerClass;
import SouthCarolina.SC_RunnerClass;
import Tennessee.TN_RunnerClass;
import Tulsa.Tulsa_RunnerClass;
//import Arizona.AZ_PropertyWare;
//import Arizona.AZ_RunnerClass;
import io.github.bonigarcia.wdm.WebDriverManager;

public class RunnerClass
{
	public static	ChromeDriver driver;
	public static Actions actions;
	public static JavascriptExecutor js;
	public static File file;
	public static FileInputStream fis;
	public static StringBuilder stringBuilder = new StringBuilder();
	public static WebDriverWait wait;
	public static FileOutputStream fos;
	public static RunnerClass runnerClassObject;
	public static String market;
	public static String leaseName;
	public static int leaseCompletedStatus = 0;
	public static String portfolio;
	public static String downloadFilePath;
	public static boolean saveButtonOnAndOff =false; 
	public static Map<String, String> lateFeeRuleValues = new HashMap();
	public static String lateFeeRuleType;
	
	// All fields required for Late Fee Rule
	public static String lateFeeType ="";
	public static String PDFFormatType= "";
	// Initial Fee + Per Day Fee
	public static String dueDay_initialFee="";
	public static String initialFeeAmount="";
	public static String initialFeeDropdown="";
	public static String perDayFeeAmount ="";
	public static String perDayFeeDropdown ="";
	public static String maximumDropdown1 ="";
	public static String maximumAmount ="";
	public static String maximumDropdown2 ="";
	public static String minimumDue ="";
	public static String additionalLateChargesLimit ="";
	
	// Greater of Flat Fee or Percentage
	public static String dueDay_GreaterOf="";
	public static String flatFee = "";
	public static String percentage = "";
	public static String maximumDropdown1_GreaterOf ="";
	public static String maximumAmount_GreaterOf ="";
	public static String maximumDropdown2_GreaterOf ="";
	public static String minimumDue_GreaterOf ="";
	
	//Company Code
	public static String companyCode ="";
	
	public static void main(String[] args) throws Exception
	{
		RunnerClass.DateModified("02/05/2023");
		GetDataFromDataBase getData = new GetDataFromDataBase();
		getData.getInProgressLeasesFromDatabase();
		//distribute inprogress leases to each company
		RunnerClass runnerClass = new RunnerClass();
		runnerClass.splitLeasesByCompany(GetDataFromDataBase.inProgressLeases);
		InsertDataIntoDatabase updateStatusInDB = new InsertDataIntoDatabase();
		AL_PropertyWare propertyWare = new AL_PropertyWare();
	}
	public void splitLeasesByCompany(String[][] leasesList) throws Exception
	{
		// split leases by market
		saveButtonOnAndOff = true;
		for(int i=0;i<leasesList.length;i++)
		{
			market = leasesList[i][0];
			portfolio = leasesList[i][1];
			leaseName = leasesList[i][2]; 
			companyCode =AppConfig.getCompanyCode(market);
			
			if("Florida,Georgia,South Carolina,Tennessee,OKC".contains(market))
			{
			
			if(market.equals("Tennessee")||market.equals("OKC"))
			{
			Map<String, Object> prefs = new HashMap<String, Object>();
	        // Use File.separator as it will work on any OS
			RunnerClass.downloadFilePath = "C:\\Gopi\\Projects\\Property ware\\Lease Close Outs\\PDFS\\"+RunnerClass.leaseName.replaceAll("[^a-zA-Z0-9]+","");
		    // Use File.separator as it will work on any OS
			File file = new File(RunnerClass.downloadFilePath);
			//file.mkdir();
			if(file.exists())
			{
				FileUtils.cleanDirectory(file);
				FileUtils.deleteDirectory(file);
			}
			FileUtils.forceMkdir(file);
		    prefs.put("download.default_directory",
		    		RunnerClass.downloadFilePath);
	        // Adding cpabilities to ChromeOptions
	        ChromeOptions options = new ChromeOptions();
	        options.setExperimentalOption("prefs", prefs);
	        options.addArguments("--remote-allow-origins=*");
	        // Printing set download directory
	        // Launching browser with desired capabilities
	        WebDriverManager.chromedriver().setup();
	        RunnerClass.driver= new ChromeDriver(options);
	        RunnerClass.actions = new Actions( RunnerClass.driver);
	        RunnerClass.js = (JavascriptExecutor) RunnerClass.driver;
			}
			
			// Change status of In progress to  a temporary
			InsertDataIntoDatabase.insertData(leaseName, "Started", 6);
			// Split the Lease Name with "-" and search with first name
			if(leaseName.split("-")[0].trim().contains(" "))
		     leaseName = leaseName;
			else 
			leaseName = leaseName.split("-")[0].trim();
			String leaseOwnerName = leasesList[i][3];
			try
			{				
			switch(market)
			{
			case "Arizona":
			AZ_RunnerClass arizona = new AZ_RunnerClass();
			arizona.runAutomation(portfolio,leaseName,leaseOwnerName);
			break;
			case "Alabama":
					AL_RunnerClass alabama = new AL_RunnerClass();
					alabama.runAutomation(portfolio,leaseName,leaseOwnerName);
					RunnerClass.updateLeaseStatus();
					AL_RunnerClass.AZ_driver.quit();
					RunnerClass.deleteDirectory(RunnerClass.downloadFilePath);
					break;
			case "Florida":
					FL_RunnerClass florida = new FL_RunnerClass();
					florida.runAutomation(portfolio,leaseName,leaseOwnerName);
					RunnerClass.updateLeaseStatus();
					FL_RunnerClass.FL_driver.quit();
					RunnerClass.deleteDirectory(RunnerClass.downloadFilePath);
					break;
				
			case "North Carolina":
				//case "Arkansas":
					NC_RunnerClass northCarolina = new NC_RunnerClass();
					northCarolina.runAutomation(portfolio,leaseName,leaseOwnerName);
					RunnerClass.updateLeaseStatus();
					NC_RunnerClass.FL_driver.quit();
					RunnerClass.deleteDirectory(RunnerClass.downloadFilePath);
					break;
			case "Georgia":
				//case "Arkansas":
					GA_RunnerClass georgia = new GA_RunnerClass();
					georgia.runAutomation(portfolio,leaseName,leaseOwnerName);
					RunnerClass.updateLeaseStatus();
					GA_RunnerClass.FL_driver.quit();
					RunnerClass.deleteDirectory(RunnerClass.downloadFilePath);
					break;
			case "South Carolina":
				//case "Arkansas":
					SC_RunnerClass southCarolina = new SC_RunnerClass();
					southCarolina.runAutomation(portfolio,leaseName,leaseOwnerName);
					RunnerClass.updateLeaseStatus();
					SC_RunnerClass.FL_driver.quit();
					RunnerClass.deleteDirectory(RunnerClass.downloadFilePath);
					break;
			case "Indiana":
				//case "Arkansas":
					IN_RunnerClass Indiana = new IN_RunnerClass();
					Indiana.runAutomation(portfolio,leaseName,leaseOwnerName);
					RunnerClass.updateLeaseStatus();
					IN_RunnerClass.FL_driver.quit();
					RunnerClass.deleteDirectory(RunnerClass.downloadFilePath);
					break;
			case "Tennessee":
				//case "Arkansas":
					TN_RunnerClass Tennessee = new TN_RunnerClass();
					Tennessee.runAutomation(portfolio,leaseName,leaseOwnerName);
					RunnerClass.updateLeaseStatus();
					RunnerClass.driver.quit();
					RunnerClass.deleteDirectory(RunnerClass.downloadFilePath);
					break;
			case "Arkansas":
				//case "Arkansas":
					AR_RunnerClass Arkansas = new AR_RunnerClass();
					Arkansas.runAutomation(portfolio,leaseName,leaseOwnerName);
					RunnerClass.updateLeaseStatus();
					AR_RunnerClass.FL_driver.quit();
					RunnerClass.deleteDirectory(RunnerClass.downloadFilePath);
					break;
			case "Little Rock":
				//case "Arkansas":
					LR_RunnerClass LittleRock = new LR_RunnerClass();
					LittleRock.runAutomation(portfolio,leaseName,leaseOwnerName);
					RunnerClass.updateLeaseStatus();
					LR_RunnerClass.FL_driver.quit();
					RunnerClass.deleteDirectory(RunnerClass.downloadFilePath);
					break;
			case "OKC":
				//case "Arkansas":
					OKC_RunnerClass OKC = new OKC_RunnerClass();
					OKC.runAutomation(portfolio,leaseName,leaseOwnerName);
					RunnerClass.updateLeaseStatus();
					RunnerClass.driver.quit();
					RunnerClass.deleteDirectory(RunnerClass.downloadFilePath);
					break;
			case "Dallas/Fort Worth":
				//case "Arkansas":
					DFW_RunnerClass DallasFortWorth = new DFW_RunnerClass();
					DallasFortWorth.runAutomation(portfolio,leaseName,leaseOwnerName);
					RunnerClass.updateLeaseStatus();
					//RunnerClass.driver.quit();
					RunnerClass.deleteDirectory(RunnerClass.downloadFilePath);
					break;
			case "Tulsa":
				//case "Arkansas":
				Tulsa_RunnerClass Tulsa = new Tulsa_RunnerClass();
				Tulsa.runAutomation(portfolio,leaseName,leaseOwnerName);
					RunnerClass.updateLeaseStatus();
					RunnerClass.driver.quit();
					RunnerClass.deleteDirectory(RunnerClass.downloadFilePath);
					break;
			case "San Antonio":
				//case "Arkansas":
					SA_RunnerClass SanAntonio = new SA_RunnerClass();
					SanAntonio.runAutomation(portfolio,leaseName,leaseOwnerName);
					RunnerClass.updateLeaseStatus();
					RunnerClass.driver.quit();
					RunnerClass.deleteDirectory(RunnerClass.downloadFilePath);
					break;
			
			}
			
			}
			catch(Exception e)
			{
				//AL_RunnerClass.AZ_driver.close();
			}
			}
			
		}
	}
	public static void deleteDirectory(String filePath) throws Exception
	{
		File fin = new File(filePath);
	    File[] finlist = fin.listFiles();       
	    for (int n = 0; n < finlist.length; n++) {
	        if (finlist[n].isFile()) {
	        System.gc();
	        Thread.sleep(2000);
	            finlist[n].delete();
	        }
	    } 
	    FileUtils.forceDelete(fin);
	}
	
	public static void updateLeaseStatus() throws Exception
	{
		//Test
		if(leaseCompletedStatus==1)
		InsertDataIntoDatabase.insertData(leaseName, "Completed", 4);
		else if(leaseCompletedStatus==2)InsertDataIntoDatabase.insertData(leaseName, "Failed", 3);
		     else InsertDataIntoDatabase.insertData(leaseName, "Review",5);
		
		System.out.println(market +" ---- " + leaseName+" ---- "+ leaseCompletedStatus);
	}
	public static void keyBoardActions()
	{
		NC_RunnerClass.FL_actions.sendKeys(Keys.BACK_SPACE).sendKeys(Keys.BACK_SPACE).sendKeys(Keys.BACK_SPACE).sendKeys(Keys.BACK_SPACE).sendKeys(Keys.BACK_SPACE).build().perform();
	}
	public static boolean onlyDigits(String str)
    {
		str = str.replace(",", "").replace(".", "").trim();
		if(str=="")
			return false;
		int numberCount =0;
        for (int i = 0; i < str.length(); i++) 
        {
            if (Character.isDigit(str.charAt(i))) 
            {
            	numberCount++;
            	//return true;
            }
        }
        if(numberCount==str.length())
        return true;
        else
        return false;
    }
	public static int nthIndexOf(String str, String subStr, int count) 
	{
	    int ind = -1;
	    while(count > 0) {
	        ind = str.indexOf(subStr, ind + 1);
	        if(ind == -1) return -1;
	        count--;
	    }
	    return ind;
	}
	/*
	public static String convertDate(String date)
	{
		String month="";
		try
		{
		String[] d = date.trim().split(" ");
		month = RunnerClass.convertMonth(d[0].trim());
		if(month.length()==1)
			month = "0"+month;
		String[] yearAndDate = date.trim().split(",");
		String day = yearAndDate[0].split(" ")[1].trim();
		if(day.length()==1)
			day = "0"+day;
		String dateIn = month +"/"+day +"/"+yearAndDate[1].trim();
		//System.out.println(dateIn);
		return dateIn;
		}
		catch(Exception e) 
		{
			//e.printStackTrace();
			try
			{
			String[] yearAndDate = date.trim().split(" ");
			String day = yearAndDate[1].trim();
			if(day.length()==1)
				day = "0"+day;
			String dateIn = month +"/"+day +"/"+yearAndDate[2].trim();
			System.out.println();
			return dateIn;
			}catch(Exception e2) 
			{
			//	e2.printStackTrace();
				return null;}
		}
	}
	*/
	public static String convertDate(String dateRaw) throws Exception
	{
		try
		{
		SimpleDateFormat format1 = new SimpleDateFormat("MMMM dd, yyyy");
	    SimpleDateFormat format2 = new SimpleDateFormat("MM/dd/yyyy");
	    Date date = format1.parse(dateRaw.trim().replaceAll(" +", " "));
	    System.out.println(format2.format(date));
		return format2.format(date).toString();
		}
		catch(Exception e)
		{
			try
			{
			SimpleDateFormat format1 = new SimpleDateFormat("MMMM dd yyyy");
		    SimpleDateFormat format2 = new SimpleDateFormat("MM/dd/yyyy");
		    Date date = format1.parse(dateRaw.trim().replaceAll(" +", " "));
		    System.out.println(format2.format(date));
			return format2.format(date).toString();
			}
			catch(Exception e2)
			{
		    return "Error";
			}
		}
	}
	
	public static String dateToMonthAndYear_FirstFullMonth(String date) throws Exception
	{
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
		Date d = sdf.parse(date);
		System.out.println(d);
		SimpleDateFormat sdf2 = new SimpleDateFormat("MMMMM yyyy");
		System.out.println(sdf2.format(d));
		return sdf2.format(d);
		
	}
	
	public static String DateModified(String date) throws Exception
	{
		DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
	    Calendar calendar = Calendar.getInstance();
	    calendar.setTime(dateFormat.parse(date));
	    String d  =  String.valueOf(calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
	    System.out.println("Last Day of the Month "+d);
	    /*
		String newDay;
		int dayInDate =Integer.parseInt(date.split("/")[1]);
		if(dayInDate<=25)
		{
			newDay = "25";
		}
		else newDay = String.valueOf(dayInDate);
	     */
		String dateArray[] = date.split("/"); //.replaceFirst(date.split("/")[1], newDay);
		date = dateArray[0]+"/"+d+"/"+dateArray[2];
		return date;
	}
	public static String extractNumber(String str) 
	{
	    //String str = "26.23,for";
	    StringBuilder myNumbers = new StringBuilder();
	    for (int i = 0; i < str.length(); i++) 
	    {
	    	char c = str.charAt(i);
	    	
	        if (Character.isDigit(str.charAt(i))||(String.valueOf(c).equals(".")&&i!=str.length()-1)) 
	        {
	            myNumbers.append(str.charAt(i));
	            //System.out.println(str.charAt(i) + " is a digit.");
	        } else {
	            //System.out.println(str.charAt(i) + " not a digit.");
	        }
	    }
	   // System.out.println("Your numbers: " + myNumbers.toString());
	    return myNumbers.toString();
	}
	
	public static String firstDayOfFullMonth(String date)
	{
		String day,month,year;
		String[] splitDate =date.split("/");
		day = "01";
		String monthInText = splitDate[0];
		if(monthInText.trim().equalsIgnoreCase("12"))
		{	
			month="01";
		    year = String.valueOf((Integer.parseInt(splitDate[2])+1));
		}
		else 
		{
			month =String.valueOf(Integer.parseInt(monthInText)+1);
		    year = splitDate[2];
		}
		return month+"/"+day+"/"+year;
		
		
	}
	
	public static String NextMonthOffirstDayOfFullMonth(String date)
	{
		String day,month,year;
		String[] splitDate =date.split("/");
		day = "01";
		String monthInText = splitDate[0];
		if(monthInText.trim().equalsIgnoreCase("12"))
		{	
			month="02";
		    year = String.valueOf((Integer.parseInt(splitDate[2])+1));
		}
		else if(monthInText.trim().equalsIgnoreCase("11"))
		{	
			month="01";
		    year = String.valueOf((Integer.parseInt(splitDate[2])+1));
		} 
		else
		{
			month =String.valueOf(Integer.parseInt(monthInText)+2);
		    year = splitDate[2];
		}
		return month+"/"+day+"/"+year;
		
		
	}
	
	public static String convertMonth(String month)
	{
		switch (month) {
		  case "January":
		    month =  "1";
		    break;
		  case "February":
			  month = "2";
			  break;
		  case "March":
			  month = "3";
			  break;
		  case "April":
			  month =  "4"; 
			  break;
		  case "May":
			  month =  "5";
			  break;
		  case "June":
			  month =  "6";
			  break;
		  case "July":
			  month =  "7";
			  break;
		  case "August":
			  month =  "8";
			  break;
		  case "September":
			  month =  "9";
			  break;
		  case "October":
			  month =  "10";
			  break;
		  case "November":
			  month =  "11";
			  break;
		  case "December":
			  month =  "12";
			  break;
		               }
          return month;		
	}
	public static File getLastModified() throws Exception
	{
		
	    File directory = new File(RunnerClass.downloadFilePath);
	    File[] files = directory.listFiles(File::isFile);
	    long lastModifiedTime = Long.MIN_VALUE;
	    File chosenFile = null;

	    if (files != null)
	    {
	        for (File file : files)
	        {
	            if (file.lastModified() > lastModifiedTime)
	            {
	                chosenFile = file;
	                lastModifiedTime = file.lastModified();
	            }
	        }
	    }

	    return chosenFile;
	}
	
public static int nthOccurrence(String str1, String str2, int n) 
{
	    
        String tempStr = str1;
        int tempIndex = -1;
        int finalIndex = 0;
        for(int occurrence = 0; occurrence < n ; ++occurrence)
        {
            tempIndex = tempStr.indexOf(str2);
            if(tempIndex==-1){
                finalIndex = 0;
                break;
            }
            tempStr = tempStr.substring(++tempIndex);
            finalIndex+=tempIndex;
        }
        return --finalIndex;
    }

public static String PDFDateToMonthAndYear(String date) throws Exception
{
	String nextMonthInText;
	String year;
	String dateInNumbers = RunnerClass.convertDate(date);
	int nextMonth = Integer.parseInt(dateInNumbers.split("/")[0])+1;
	int yearInDate = Integer.parseInt(dateInNumbers.split("/")[2]);
	switch(nextMonth)
	{
	case 1:
		nextMonthInText = "January";
		year =String.valueOf(yearInDate);
		return nextMonthInText +" "+year; 
	case 2:
		nextMonthInText = "February";
		year =String.valueOf(yearInDate);
		return nextMonthInText +" "+year; 
	case 3:
		nextMonthInText = "March";
		year =String.valueOf(yearInDate);
		return nextMonthInText +" "+year; 
	case 4:
		nextMonthInText = "April";
		year =String.valueOf(yearInDate);
		return nextMonthInText +" "+year; 
	case 5:
		nextMonthInText = "May";
		year =String.valueOf(yearInDate);
		return nextMonthInText +" "+year; 
	case 6:
		nextMonthInText = "June";
		year =String.valueOf(yearInDate);
		return nextMonthInText +" "+year;
	case 7:
		nextMonthInText = "July";
		year =String.valueOf(yearInDate);
		return nextMonthInText +" "+year;
	case 8:
		nextMonthInText = "August";
		year =String.valueOf(yearInDate);
		return nextMonthInText +" "+year; 
	case 9:
		nextMonthInText = "September";
		year =String.valueOf(yearInDate);
		return nextMonthInText +" "+year; 
	case 10:
		nextMonthInText = "October";
		year =String.valueOf(yearInDate);
		return nextMonthInText +" "+year; 
	case 11:
		nextMonthInText = "November";
		year =String.valueOf(yearInDate);
		return nextMonthInText +" "+year; 
	case 12:
		nextMonthInText = "January";
		year =String.valueOf(yearInDate);
		return nextMonthInText +" "+String.valueOf(yearInDate+1); 
	}
	return "";
}
	
	/*
	RunnerClass()
	{
		        //System.setProperty(AppConfig.browserType,AppConfig.browserPath);
				Map<String, Object> prefs = new HashMap<String, Object>();
		        // Use File.separator as it will work on any OS
		        prefs.put("download.default_directory",
		                "C:\\Gopi\\Projects\\Property ware\\Lease Close Outs\\PDFS");
		        // Adding cpabilities to ChromeOptions
		        ChromeOptions options = new ChromeOptions();
		        options.setExperimentalOption("prefs", prefs);
		        // Printing set download directory
		         
		        // Launching browser with desired capabilities
		        WebDriverManager.chromedriver().setup();
		        driver= new ChromeDriver(options);
				actions = new Actions(driver);
				js = (JavascriptExecutor)driver;
				driver.manage().timeouts().implicitlyWait(30,TimeUnit.SECONDS);
				wait = new WebDriverWait(driver, Duration.ofSeconds(50));
				
	}
*/
}
