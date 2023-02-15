package OKC;

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

public class OKC_RunnerClass 
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
	public static String chargeCodesTable ="automation.chargeCodesConfiguration_OKC";

		public boolean runAutomation(String portfolio, String leaseName, String leaseOwnername)  throws Exception
		{
			
			//OKC_RunnerClass OKC_RunnerClass = new OKC_RunnerClass();
			OKC_RunnerClass.openBrowser();
			//Login to Propertyware
			OKC_PropertyWare downloadLeaseAgreement =new  OKC_PropertyWare();
			downloadLeaseAgreement.login();
			
			boolean selectLeaseResult = downloadLeaseAgreement.selectLease(leaseName);
			if(selectLeaseResult==false)
				return false;
			//Empty all static variable values
			OKC_RunnerClass.emptyAllValues();
			
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
				OKC_PropertyWare.portfolioType = "MCH";
			else OKC_PropertyWare.portfolioType = "Others";
			
			/*
			if(RunnerClass.portfolio.contains("MAN")||RunnerClass.portfolio.contains("HS")||RunnerClass.portfolio.contains("MCH"))
			{
			OKC_PropertyWare.portfolioType = "MCH";
			}
			else
			OKC_PropertyWare.portfolioType = "Others";
			*/
			// Decide the PDF Format
	        pdfFormatType = OKC_RunnerClass.decidePDFFormat();
	        try
	        {
	        OKC_RunnerClass.document.close();
	        }
	        catch(Exception e) {}
	        
	        RunnerClass.lateFeeRuleValues.clear();
	        
	        if(pdfFormatType.equalsIgnoreCase("Format1"))
	        {
	        	RunnerClass.PDFFormatType = "Format1";
	        	System.out.println("PDF Type = Format 1");
	        	OKC_ExtractDataFromPDF getDataFromPDF = new OKC_ExtractDataFromPDF();
	    		boolean getDataFromPDFResult =  getDataFromPDF.arizona();
	    		if(getDataFromPDFResult == false)
	    			return false;
	        }
	        else if(pdfFormatType.equalsIgnoreCase("Format2"))
	             {
	        	    RunnerClass.PDFFormatType = "Format2";
	        	    System.out.println("PDF Type = Format 2");
	        	    OKC_ExtractDataFromPDF_Format2 getDataFromPDF_format2 = new OKC_ExtractDataFromPDF_Format2();
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
	        	startDate= RunnerClass.convertDate(OKC_PropertyWare.commensementDate).trim();
	        }
	        catch(Exception e)
	        {
	        	InsertDataIntoDatabase.notAutomatedFields(RunnerClass.leaseName, "Unable to get Start Date"+'\n');
	        	//return false;
	        }
	        String endDate = RunnerClass.convertDate(OKC_PropertyWare.expirationDate).trim();
			//Check if the Start Date, End Date and Move In Date matches in both PW and Lease Agreement
	        if(!OKC_PropertyWare.leaseStartDate_PW.trim().equalsIgnoreCase(startDate))
	        {
	        	System.out.println("Start Date doesn't Match");
	 	    	InsertDataIntoDatabase.notAutomatedFields(RunnerClass.leaseName, "Start Date is not matched"+'\n');
	        }
	        if(!OKC_PropertyWare.leaseEndDate_PW.trim().equalsIgnoreCase(endDate))
	        {
	        	System.out.println("End Date doesn't Match");
	 	    	InsertDataIntoDatabase.notAutomatedFields(RunnerClass.leaseName, "End Date is not matched"+'\n');
	        }
			
	        OKC_InsertDataIntoPropertyWare.insertData();
	        try
	        {
	        OKC_RunnerClass.document.close();
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
			OKC_RunnerClass.document = PDDocument.load(fis);
		    String text = new PDFTextStripper().getText(document);
		    OKC_PropertyWare.pdfText  = text;
		    if(text.contains(OKC_AppConfig.PDFFormatConfirmationText)) 
		    {
		    	document.close();
				return "Format1";
		    	
		    }
		    else if(text.contains(OKC_AppConfig.PDFFormat2ConfirmationText))
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
			OKC_PropertyWare.commensementDate ="";
			OKC_PropertyWare.expirationDate ="";
			OKC_PropertyWare.proratedRent ="";
			OKC_PropertyWare.proratedRentDate ="";
			OKC_PropertyWare.monthlyRent="";
			OKC_PropertyWare.monthlyRentDate="";
			OKC_PropertyWare.adminFee="";
			OKC_PropertyWare.airFilterFee="";
			OKC_PropertyWare.earlyTermination="";
			OKC_PropertyWare.occupants="";
			OKC_PropertyWare.lateChargeDay="";
			OKC_PropertyWare.lateChargeFee="";
			OKC_PropertyWare.proratedPetRent="";
			OKC_PropertyWare.petRentWithTax="";
			OKC_PropertyWare.proratedPetRentDate="";
			OKC_PropertyWare.petSecurityDeposit="";
			OKC_PropertyWare.RCDetails="";
			OKC_PropertyWare.petRent="";
			OKC_PropertyWare.petFee="";
			OKC_PropertyWare.pet1Type="";
			OKC_PropertyWare.pet2Type="";
			OKC_PropertyWare.serviceAnimalType="";
			OKC_PropertyWare.pet1Breed="";
			OKC_PropertyWare.pet2Breed="";
			OKC_PropertyWare.serviceAnimalBreed="";
			OKC_PropertyWare.pet1Weight="";
			OKC_PropertyWare.pet2Weight="";
			OKC_PropertyWare.serviceAnimalWeight="";
			OKC_PropertyWare.petOneTimeNonRefundableFee="";
			OKC_PropertyWare.countOfTypeWordInText=0;
			OKC_PropertyWare.lateFeeChargeDay="";
			OKC_PropertyWare.lateFeeAmount="";
			OKC_PropertyWare.lateFeeChargePerDay="";
			OKC_PropertyWare.additionalLateCharges="";
			OKC_PropertyWare.additionalLateChargesLimit="";
			OKC_PropertyWare.CDEType="";
			OKC_PropertyWare.monthlyTenantAdminFee_Amount=0.00;
			OKC_PropertyWare.calculatedPetRent=0.00;
			OKC_PropertyWare.df = new DecimalFormat("0.00");
			OKC_PropertyWare.pdfText="";
			OKC_PropertyWare.securityDeposit="";
			OKC_PropertyWare.leaseStartDate_PW="";
			OKC_PropertyWare.leaseEndDate_PW="";
			OKC_PropertyWare.prepaymentCharge="";
			OKC_PropertyWare.petType=null;
			OKC_PropertyWare.petBreed=null;
			OKC_PropertyWare.petWeight=null;
			OKC_PropertyWare.robot=null;
			OKC_PropertyWare.concessionAddendumFlag = false;
			OKC_PropertyWare.petSecurityDepositFlag = false;
			OKC_PropertyWare.petFlag = false;
			OKC_PropertyWare.portfolioType="";
			OKC_PropertyWare.incrementRentFlag = false;
			OKC_PropertyWare.proratedRentDateIsInMoveInMonthFlag=false;
			OKC_PropertyWare.increasedRent_previousRentStartDate ="";
			OKC_PropertyWare.increasedRent_previousRentEndDate ="";
			OKC_PropertyWare.increasedRent_amount ="";
			OKC_PropertyWare.increasedRent_newStartDate ="";
			OKC_PropertyWare.increasedRent_newEndDate ="";
			OKC_PropertyWare.serviceAnimalFlag = false;
			OKC_PropertyWare.lateFeeType ="";
			OKC_PropertyWare.flatFeeAmount ="";
			OKC_PropertyWare.lateFeePercentage="";
			OKC_PropertyWare.HVACFilterFlag = false;
			OKC_PropertyWare.HVACFilterFlag = false;
			OKC_PropertyWare.residentBenefitsPackageAvailabilityCheck = false;
		}
		


}
