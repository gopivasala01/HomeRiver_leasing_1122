package SanAntonio;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.text.DecimalFormat;
import java.time.Duration;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.openqa.selenium.support.ui.WebDriverWait;

import mainPackage.InsertDataIntoDatabase;
import mainPackage.RunnerClass;

public class SA_RunnerClass 
{
	//public static ChromeDriver RunnerClass.driver;
	//public static Actions FL_actions;
	//public static JavascriptExecutor FL_js;
	public static File FL_file;
	public static FileInputStream FL_fis;
	public static StringBuilder FL_stringBuilder = new StringBuilder() ;
	public static WebDriverWait FL_wait;
	public static FileOutputStream FL_fos;
	public static String pdfFormatType;
	public static PDDocument document;
	public static String chargeCodesTable ="Automation.ChargeCodesConfiguration_SA";

		public boolean runAutomation(String portfolio, String leaseName, String leaseOwnername)  throws Exception
		{
			
			//SA_RunnerClass SA_RunnerClass = new SA_RunnerClass();
			SA_RunnerClass.openBrowser();
			//Login to Propertyware
			SA_PropertyWare downloadLeaseAgreement =new  SA_PropertyWare();
			downloadLeaseAgreement.login();
			
			boolean selectLeaseResult = downloadLeaseAgreement.selectLease(leaseName);
			if(selectLeaseResult==false)
				return false;
			//Empty all static variable values
			SA_RunnerClass.emptyAllValues();
			
			boolean downloadLeaseAgreementResult =  downloadLeaseAgreement.validateSelectedLease(leaseOwnername);//leaseOwnername
			if(downloadLeaseAgreementResult==false)
				return false;
			//Extract data from PDF
			//Decide Portfolio Type
			int portfolioFlag =0;
			for(int i=0;i<mainPackage.AppConfig.IAGClientList.length;i++)
			{
				String portfolioStarting = mainPackage.AppConfig.IAGClientList[i].toLowerCase();
				if(RunnerClass.portfolio.toLowerCase().startsWith(portfolioStarting))
				{
					portfolioFlag =1;
					break;
					//AL_PropertyWare.portfolioType = "MCH";
				}
			}
			
			if(portfolioFlag==1)
				SA_PropertyWare.portfolioType = "MCH";
			else SA_PropertyWare.portfolioType = "Others";
			
			/*
			if(RunnerClass.portfolio.contains("MAN")||RunnerClass.portfolio.contains("HS")||RunnerClass.portfolio.contains("MCH"))
			{
			SA_PropertyWare.portfolioType = "MCH";
			}
			else
			SA_PropertyWare.portfolioType = "Others";
			*/
			// Decide the PDF Format
	        pdfFormatType = SA_RunnerClass.decidePDFFormat();
	        try
	        {
	        SA_RunnerClass.document.close();
	        }
	        catch(Exception e) {}
	        
	        RunnerClass.lateFeeRuleValues.clear();
	        
	        if(pdfFormatType.equalsIgnoreCase("Format1"))
	        {
	        	RunnerClass.PDFFormatType = "Format1";
	        	System.out.println("PDF Type = Format 1");
	        	SA_ExtractDataFromPDF getDataFromPDF = new SA_ExtractDataFromPDF();
	    		boolean getDataFromPDFResult =  getDataFromPDF.arizona();
	    		if(getDataFromPDFResult == false)
	    			return false;
	        }
	        else if(pdfFormatType.equalsIgnoreCase("Format2"))
	             {
	        	    RunnerClass.PDFFormatType = "Format2";
	        	    System.out.println("PDF Type = Format 2");
	        	    SA_ExtractDataFromPDF_Format2 getDataFromPDF_format2 = new SA_ExtractDataFromPDF_Format2();
		    		boolean getDataFromPDFResult =  getDataFromPDF_format2.arizona();
		    		if(getDataFromPDFResult == false)
	    			return false;
	             }
	             else
	             {
	            	 System.out.println("PDF Type = Not Supported Format");
	            	 return false;
	             }
	        String startDate="";
	        try
	        {
	        	startDate= RunnerClass.convertDate(SA_PropertyWare.commensementDate).trim();
	        }
	        catch(Exception e)
	        {
	        	InsertDataIntoDatabase.notAutomatedFields(RunnerClass.leaseName, "Unable to get Start Date"+'\n');
	        	//return false;
	        }
	        String endDate = RunnerClass.convertDate(SA_PropertyWare.expirationDate).trim();
			//Check if the Start Date, End Date and Move In Date matches in both PW and Lease Agreement
	        if(!SA_PropertyWare.leaseStartDate_PW.trim().equalsIgnoreCase(startDate))
	        {
	        	System.out.println("Start Date doesn't Match");
	 	    	InsertDataIntoDatabase.notAutomatedFields(RunnerClass.leaseName, "Start Date is not matched"+'\n');
	        }
	        if(!SA_PropertyWare.leaseEndDate_PW.trim().equalsIgnoreCase(endDate))
	        {
	        	System.out.println("End Date doesn't Match");
	 	    	InsertDataIntoDatabase.notAutomatedFields(RunnerClass.leaseName, "End Date is not matched"+'\n');
	        }
			
	        SA_InsertDataIntoPropertyWare.insertData();
	        try
	        {
	        SA_RunnerClass.document.close();
	        }
	        catch(Exception e) {}
			return true;
		}

		public  static void openBrowser() throws Exception
		{
			/*
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
	        // Printing set download directory
	         
	        // Launching browser with desired capabilities
	        WebDriverManager.chromedriver().setup();
	        RunnerClass.driver= new ChromeDriver(options);
	        RunnerClass.actions = new Actions( RunnerClass.driver);
	        RunnerClass.js = (JavascriptExecutor) RunnerClass.driver;
	        RunnerClass.driver.manage().timeouts().implicitlyWait(30,TimeUnit.SECONDS);
	        */
	        FL_wait = new WebDriverWait(RunnerClass.driver, Duration.ofSeconds(150));
	       // RunnerClass.driver.get(AppConfig.propertyWareURL);
	        
		}
		
		public static String decidePDFFormat() throws Exception
		{
			try
			{
			File file = RunnerClass.getLastModified();
			FileInputStream fis = new FileInputStream(file);
			SA_RunnerClass.document = PDDocument.load(fis);
		    String text = new PDFTextStripper().getText(document);
		    SA_PropertyWare.pdfText  = text;
		    if(text.contains(SA_AppConfig.PDFFormatConfirmationText)) 
		    {
		    	document.close();
				return "Format1";
		    	
		    }
		    else if(text.contains(SA_AppConfig.PDFFormat2ConfirmationText))
		         {
		    	document.close();
	            return "Format2";	
		         }
		         else 
		         {
		        	System.out.println("Wrong PDF Format");
		 	    	InsertDataIntoDatabase.notAutomatedFields(RunnerClass.leaseName, "Wrong Lease Agreement PDF Format"+'\n');
		 			RunnerClass.leaseCompletedStatus = 3;
		 			document.close();
		 			return "Others";
		          }
			}
			catch(Exception e)
			{
				System.out.println("Lease Agreement was not downloaded, Bad Network");
	 	    	InsertDataIntoDatabase.notAutomatedFields(RunnerClass.leaseName, "Lease Agreement was not downloaded, Bad Network"+'\n');
	 			RunnerClass.leaseCompletedStatus = 3;
	 			return "Others";
			}
		}
		public static void emptyAllValues()
		{
			SA_PropertyWare.commensementDate ="";
			SA_PropertyWare.expirationDate ="";
			SA_PropertyWare.proratedRent ="";
			SA_PropertyWare.proratedRentDate ="";
			SA_PropertyWare.monthlyRent="";
			SA_PropertyWare.monthlyRentDate="";
			SA_PropertyWare.adminFee="";
			SA_PropertyWare.airFilterFee="";
			SA_PropertyWare.earlyTermination="";
			SA_PropertyWare.occupants="";
			SA_PropertyWare.lateChargeDay="";
			SA_PropertyWare.lateChargeFee="";
			SA_PropertyWare.proratedPetRent="";
			SA_PropertyWare.petRentWithTax="";
			SA_PropertyWare.proratedPetRentDate="";
			SA_PropertyWare.petSecurityDeposit="";
			SA_PropertyWare.RCDetails="";
			SA_PropertyWare.petRent="";
			SA_PropertyWare.petFee="";
			SA_PropertyWare.pet1Type="";
			SA_PropertyWare.pet2Type="";
			SA_PropertyWare.serviceAnimalType="";
			SA_PropertyWare.pet1Breed="";
			SA_PropertyWare.pet2Breed="";
			SA_PropertyWare.serviceAnimalBreed="";
			SA_PropertyWare.pet1Weight="";
			SA_PropertyWare.pet2Weight="";
			SA_PropertyWare.serviceAnimalWeight="";
			SA_PropertyWare.petOneTimeNonRefundableFee="";
			SA_PropertyWare.countOfTypeWordInText=0;
			SA_PropertyWare.lateFeeChargeDay="";
			SA_PropertyWare.lateFeeAmount="";
			SA_PropertyWare.lateFeeChargePerDay="";
			SA_PropertyWare.additionalLateCharges="";
			SA_PropertyWare.additionalLateChargesLimit="";
			SA_PropertyWare.CDEType="";
			SA_PropertyWare.monthlyTenantAdminFee_Amount=0.00;
			SA_PropertyWare.calculatedPetRent=0.00;
			SA_PropertyWare.df = new DecimalFormat("0.00");
			SA_PropertyWare.pdfText="";
			SA_PropertyWare.securityDeposit="";
			SA_PropertyWare.leaseStartDate_PW="";
			SA_PropertyWare.leaseEndDate_PW="";
			SA_PropertyWare.prepaymentCharge="";
			SA_PropertyWare.petType=null;
			SA_PropertyWare.petBreed=null;
			SA_PropertyWare.petWeight=null;
			SA_PropertyWare.robot=null;
			SA_PropertyWare.concessionAddendumFlag = false;
			SA_PropertyWare.petSecurityDepositFlag = false;
			SA_PropertyWare.petFlag = false;
			SA_PropertyWare.portfolioType="";
			SA_PropertyWare.incrementRentFlag = false;
			SA_PropertyWare.proratedRentDateIsInMoveInMonthFlag=false;
			SA_PropertyWare.increasedRent_previousRentStartDate ="";
			SA_PropertyWare.increasedRent_previousRentEndDate ="";
			SA_PropertyWare.increasedRent_amount ="";
			SA_PropertyWare.increasedRent_newStartDate ="";
			SA_PropertyWare.increasedRent_newEndDate ="";
			SA_PropertyWare.serviceAnimalFlag = false;
			SA_PropertyWare.lateFeeType ="";
			SA_PropertyWare.flatFeeAmount ="";
			SA_PropertyWare.lateFeePercentage="";
			SA_PropertyWare.HVACFilterFlag = false;
			SA_PropertyWare.HVACFilterFlag = false;
			SA_PropertyWare.residentBenefitsPackageAvailabilityCheck = false;
		}
		


}
