package DallasFortWorth;

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

public class DFW_RunnerClass 
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
	public static String chargeCodesTable ="Automation.ChargeCodesConfiguration";

		public boolean runAutomation(String portfolio, String leaseName, String leaseOwnername)  throws Exception
		{
			
			//DFW_RunnerClass DFW_RunnerClass = new DFW_RunnerClass();
			DFW_RunnerClass.openBrowser();
			//Login to Propertyware
			DFW_PropertyWare downloadLeaseAgreement =new  DFW_PropertyWare();
			downloadLeaseAgreement.login();
			
			boolean selectLeaseResult = downloadLeaseAgreement.selectLease(leaseName);
			if(selectLeaseResult==false)
				return false;
			//Empty all static variable values
			DFW_RunnerClass.emptyAllValues();
			
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
				DFW_PropertyWare.portfolioType = "MCH";
			else DFW_PropertyWare.portfolioType = "Others";
			
			/*
			if(RunnerClass.portfolio.contains("MAN")||RunnerClass.portfolio.contains("HS")||RunnerClass.portfolio.contains("MCH"))
			{
			DFW_PropertyWare.portfolioType = "MCH";
			}
			else
			DFW_PropertyWare.portfolioType = "Others";
			*/
			// Decide the PDF Format
	        pdfFormatType = DFW_RunnerClass.decidePDFFormat();
	        try
	        {
	        DFW_RunnerClass.document.close();
	        }
	        catch(Exception e) {}
	        
	        RunnerClass.lateFeeRuleValues.clear();
	        
	        if(pdfFormatType.equalsIgnoreCase("Format1"))
	        {
	        	RunnerClass.PDFFormatType = "Format1";
	        	System.out.println("PDF Type = Format 1");
	        	DFW_ExtractDataFromPDF getDataFromPDF = new DFW_ExtractDataFromPDF();
	    		boolean getDataFromPDFResult =  getDataFromPDF.arizona();
	    		if(getDataFromPDFResult == false)
	    			return false;
	        }
	        else if(pdfFormatType.equalsIgnoreCase("Format2"))
	             {
	        	    RunnerClass.PDFFormatType = "Format2";
	        	    System.out.println("PDF Type = Format 2");
	        	    DFW_ExtractDataFromPDF_Format2 getDataFromPDF_format2 = new DFW_ExtractDataFromPDF_Format2();
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
	        	startDate= RunnerClass.convertDate(DFW_PropertyWare.commensementDate).trim();
	        }
	        catch(Exception e)
	        {
	        	InsertDataIntoDatabase.notAutomatedFields(RunnerClass.leaseName, "Unable to get Start Date"+'\n');
	        	//return false;
	        }
	        String endDate = RunnerClass.convertDate(DFW_PropertyWare.expirationDate).trim();
			//Check if the Start Date, End Date and Move In Date matches in both PW and Lease Agreement
	        if(!DFW_PropertyWare.leaseStartDate_PW.trim().equalsIgnoreCase(startDate))
	        {
	        	System.out.println("Start Date doesn't Match");
	 	    	InsertDataIntoDatabase.notAutomatedFields(RunnerClass.leaseName, "Start Date is not matched"+'\n');
	        }
	        if(!DFW_PropertyWare.leaseEndDate_PW.trim().equalsIgnoreCase(endDate))
	        {
	        	System.out.println("End Date doesn't Match");
	 	    	InsertDataIntoDatabase.notAutomatedFields(RunnerClass.leaseName, "End Date is not matched"+'\n');
	        }
			
	        DFW_InsertDataIntoPropertyWare.insertData();
	        try
	        {
	        DFW_RunnerClass.document.close();
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
	        FL_wait = new WebDriverWait(RunnerClass.driver, Duration.ofSeconds(50));
	       // RunnerClass.driver.get(AppConfig.propertyWareURL);
	        
		}
		
		public static String decidePDFFormat() throws Exception
		{
			try
			{
			File file = RunnerClass.getLastModified();
			FileInputStream fis = new FileInputStream(file);
			DFW_RunnerClass.document = PDDocument.load(fis);
		    String text = new PDFTextStripper().getText(document);
		    DFW_PropertyWare.pdfText  = text;
		    if(text.contains(DFW_AppConfig.PDFFormatConfirmationText)) 
		    {
		    	document.close();
				return "Format1";
		    	
		    }
		    else if(text.contains(DFW_AppConfig.PDFFormat2ConfirmationText))
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
			DFW_PropertyWare.commensementDate ="";
			DFW_PropertyWare.expirationDate ="";
			DFW_PropertyWare.proratedRent ="";
			DFW_PropertyWare.proratedRentDate ="";
			DFW_PropertyWare.monthlyRent="";
			DFW_PropertyWare.monthlyRentDate="";
			DFW_PropertyWare.adminFee="";
			DFW_PropertyWare.airFilterFee="";
			DFW_PropertyWare.earlyTermination="";
			DFW_PropertyWare.occupants="";
			DFW_PropertyWare.lateChargeDay="";
			DFW_PropertyWare.lateChargeFee="";
			DFW_PropertyWare.proratedPetRent="";
			DFW_PropertyWare.petRentWithTax="";
			DFW_PropertyWare.proratedPetRentDate="";
			DFW_PropertyWare.petSecurityDeposit="";
			DFW_PropertyWare.RCDetails="";
			DFW_PropertyWare.petRent="";
			DFW_PropertyWare.petFee="";
			DFW_PropertyWare.pet1Type="";
			DFW_PropertyWare.pet2Type="";
			DFW_PropertyWare.serviceAnimalType="";
			DFW_PropertyWare.pet1Breed="";
			DFW_PropertyWare.pet2Breed="";
			DFW_PropertyWare.serviceAnimalBreed="";
			DFW_PropertyWare.pet1Weight="";
			DFW_PropertyWare.pet2Weight="";
			DFW_PropertyWare.serviceAnimalWeight="";
			DFW_PropertyWare.petOneTimeNonRefundableFee="";
			DFW_PropertyWare.countOfTypeWordInText=0;
			DFW_PropertyWare.lateFeeChargeDay="";
			DFW_PropertyWare.lateFeeAmount="";
			DFW_PropertyWare.lateFeeChargePerDay="";
			DFW_PropertyWare.additionalLateCharges="";
			DFW_PropertyWare.additionalLateChargesLimit="";
			DFW_PropertyWare.CDEType="";
			DFW_PropertyWare.monthlyTenantAdminFee_Amount=0.00;
			DFW_PropertyWare.calculatedPetRent=0.00;
			DFW_PropertyWare.df = new DecimalFormat("0.00");
			DFW_PropertyWare.pdfText="";
			DFW_PropertyWare.securityDeposit="";
			DFW_PropertyWare.leaseStartDate_PW="";
			DFW_PropertyWare.leaseEndDate_PW="";
			DFW_PropertyWare.prepaymentCharge="";
			DFW_PropertyWare.petType=null;
			DFW_PropertyWare.petBreed=null;
			DFW_PropertyWare.petWeight=null;
			DFW_PropertyWare.robot=null;
			DFW_PropertyWare.concessionAddendumFlag = false;
			DFW_PropertyWare.petSecurityDepositFlag = false;
			DFW_PropertyWare.petFlag = false;
			DFW_PropertyWare.portfolioType="";
			DFW_PropertyWare.incrementRentFlag = false;
			DFW_PropertyWare.proratedRentDateIsInMoveInMonthFlag=false;
			DFW_PropertyWare.increasedRent_previousRentStartDate ="";
			DFW_PropertyWare.increasedRent_previousRentEndDate ="";
			DFW_PropertyWare.increasedRent_amount ="";
			DFW_PropertyWare.increasedRent_newStartDate ="";
			DFW_PropertyWare.increasedRent_newEndDate ="";
			DFW_PropertyWare.serviceAnimalFlag = false;
			DFW_PropertyWare.lateFeeType ="";
			DFW_PropertyWare.flatFeeAmount ="";
			DFW_PropertyWare.lateFeePercentage="";
			DFW_PropertyWare.HVACFilterFlag = false;
			DFW_PropertyWare.HVACFilterFlag = false;
			DFW_PropertyWare.residentBenefitsPackageAvailabilityCheck = false;
		}
		


}
