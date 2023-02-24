package Tulsa;

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

public class Tulsa_RunnerClass 
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
				
				//Tulsa_RunnerClass Tulsa_RunnerClass = new Tulsa_RunnerClass();
				Tulsa_RunnerClass.openBrowser();
				//Login to Propertyware
				Tulsa_PropertyWare downloadLeaseAgreement =new  Tulsa_PropertyWare();
				downloadLeaseAgreement.login();
				
				boolean selectLeaseResult = downloadLeaseAgreement.selectLease(leaseName);
				if(selectLeaseResult==false)
					return false;
				//Empty all static variable values
				Tulsa_RunnerClass.emptyAllValues();
				
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
					Tulsa_PropertyWare.portfolioType = "MCH";
				else Tulsa_PropertyWare.portfolioType = "Others";
				
				/*
				if(RunnerClass.portfolio.contains("MAN")||RunnerClass.portfolio.contains("HS")||RunnerClass.portfolio.contains("MCH"))
				{
				Tulsa_PropertyWare.portfolioType = "MCH";
				}
				else
				Tulsa_PropertyWare.portfolioType = "Others";
				*/
				// Decide the PDF Format
		        pdfFormatType = Tulsa_RunnerClass.decidePDFFormat();
		        try
		        {
		        Tulsa_RunnerClass.document.close();
		        }
		        catch(Exception e) {}
		        
		        RunnerClass.lateFeeRuleValues.clear();
		        
		        if(pdfFormatType.equalsIgnoreCase("Format1"))
		        {
		        	RunnerClass.PDFFormatType = "Format1";
		        	System.out.println("PDF Type = Format 1");
		        	Tulsa_ExtractDataFromPDF getDataFromPDF = new Tulsa_ExtractDataFromPDF();
		    		boolean getDataFromPDFResult =  getDataFromPDF.arizona();
		    		if(getDataFromPDFResult == false)
		    			return false;
		        }
		        else if(pdfFormatType.equalsIgnoreCase("Format2"))
		             {
		        	    RunnerClass.PDFFormatType = "Format2";
		        	    System.out.println("PDF Type = Format 2");
		        	    Tulsa_ExtractDataFromPDF_Format2 getDataFromPDF_format2 = new Tulsa_ExtractDataFromPDF_Format2();
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
		        	startDate= RunnerClass.convertDate(Tulsa_PropertyWare.commensementDate).trim();
		        }
		        catch(Exception e)
		        {
		        	InsertDataIntoDatabase.notAutomatedFields(RunnerClass.leaseName, "Unable to get Start Date"+'\n');
		        	//return false;
		        }
		        String endDate = RunnerClass.convertDate(Tulsa_PropertyWare.expirationDate).trim();
				//Check if the Start Date, End Date and Move In Date matches in both PW and Lease Agreement
		        if(!Tulsa_PropertyWare.leaseStartDate_PW.trim().equalsIgnoreCase(startDate))
		        {
		        	System.out.println("Start Date doesn't Match");
		 	    	InsertDataIntoDatabase.notAutomatedFields(RunnerClass.leaseName, "Start Date is not matched"+'\n');
		        }
		        if(!Tulsa_PropertyWare.leaseEndDate_PW.trim().equalsIgnoreCase(endDate))
		        {
		        	System.out.println("End Date doesn't Match");
		 	    	InsertDataIntoDatabase.notAutomatedFields(RunnerClass.leaseName, "End Date is not matched"+'\n');
		        }
				
		        Tulsa_InsertDataIntoPropertyWare.insertData();
		        try
		        {
		        Tulsa_RunnerClass.document.close();
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
				Tulsa_RunnerClass.document = PDDocument.load(fis);
			    String text = new PDFTextStripper().getText(document);
			    Tulsa_PropertyWare.pdfText  = text;
			    if(text.contains(Tulsa_AppConfig.PDFFormatConfirmationText)) 
			    {
			    	document.close();
					return "Format1";
			    	
			    }
			    else if(text.contains(Tulsa_AppConfig.PDFFormat2ConfirmationText))
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
				Tulsa_PropertyWare.commensementDate ="";
				Tulsa_PropertyWare.expirationDate ="";
				Tulsa_PropertyWare.proratedRent ="";
				Tulsa_PropertyWare.proratedRentDate ="";
				Tulsa_PropertyWare.monthlyRent="";
				Tulsa_PropertyWare.monthlyRentDate="";
				Tulsa_PropertyWare.adminFee="";
				Tulsa_PropertyWare.airFilterFee="";
				Tulsa_PropertyWare.earlyTermination="";
				Tulsa_PropertyWare.occupants="";
				Tulsa_PropertyWare.lateChargeDay="";
				Tulsa_PropertyWare.lateChargeFee="";
				Tulsa_PropertyWare.proratedPetRent="";
				Tulsa_PropertyWare.petRentWithTax="";
				Tulsa_PropertyWare.proratedPetRentDate="";
				Tulsa_PropertyWare.petSecurityDeposit="";
				Tulsa_PropertyWare.RCDetails="";
				Tulsa_PropertyWare.petRent="";
				Tulsa_PropertyWare.petFee="";
				Tulsa_PropertyWare.pet1Type="";
				Tulsa_PropertyWare.pet2Type="";
				Tulsa_PropertyWare.serviceAnimalType="";
				Tulsa_PropertyWare.pet1Breed="";
				Tulsa_PropertyWare.pet2Breed="";
				Tulsa_PropertyWare.serviceAnimalBreed="";
				Tulsa_PropertyWare.pet1Weight="";
				Tulsa_PropertyWare.pet2Weight="";
				Tulsa_PropertyWare.serviceAnimalWeight="";
				Tulsa_PropertyWare.petOneTimeNonRefundableFee="";
				Tulsa_PropertyWare.countOfTypeWordInText=0;
				Tulsa_PropertyWare.lateFeeChargeDay="";
				Tulsa_PropertyWare.lateFeeAmount="";
				Tulsa_PropertyWare.lateFeeChargePerDay="";
				Tulsa_PropertyWare.additionalLateCharges="";
				Tulsa_PropertyWare.additionalLateChargesLimit="";
				Tulsa_PropertyWare.CDEType="";
				Tulsa_PropertyWare.monthlyTenantAdminFee_Amount=0.00;
				Tulsa_PropertyWare.calculatedPetRent=0.00;
				Tulsa_PropertyWare.df = new DecimalFormat("0.00");
				Tulsa_PropertyWare.pdfText="";
				Tulsa_PropertyWare.securityDeposit="";
				Tulsa_PropertyWare.leaseStartDate_PW="";
				Tulsa_PropertyWare.leaseEndDate_PW="";
				Tulsa_PropertyWare.prepaymentCharge="";
				Tulsa_PropertyWare.petType=null;
				Tulsa_PropertyWare.petBreed=null;
				Tulsa_PropertyWare.petWeight=null;
				Tulsa_PropertyWare.robot=null;
				Tulsa_PropertyWare.concessionAddendumFlag = false;
				Tulsa_PropertyWare.petSecurityDepositFlag = false;
				Tulsa_PropertyWare.petFlag = false;
				Tulsa_PropertyWare.portfolioType="";
				Tulsa_PropertyWare.incrementRentFlag = false;
				Tulsa_PropertyWare.proratedRentDateIsInMoveInMonthFlag=false;
				Tulsa_PropertyWare.increasedRent_previousRentStartDate ="";
				Tulsa_PropertyWare.increasedRent_previousRentEndDate ="";
				Tulsa_PropertyWare.increasedRent_amount ="";
				Tulsa_PropertyWare.increasedRent_newStartDate ="";
				Tulsa_PropertyWare.increasedRent_newEndDate ="";
				Tulsa_PropertyWare.serviceAnimalFlag = false;
				Tulsa_PropertyWare.lateFeeType ="";
				Tulsa_PropertyWare.flatFeeAmount ="";
				Tulsa_PropertyWare.lateFeePercentage="";
				Tulsa_PropertyWare.HVACFilterFlag = false;
				Tulsa_PropertyWare.HVACFilterFlag = false;
				Tulsa_PropertyWare.residentBenefitsPackageAvailabilityCheck = false;
			}
			



}
