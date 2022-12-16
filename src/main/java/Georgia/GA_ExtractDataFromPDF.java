package Georgia;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import mainPackage.InsertDataIntoDatabase;
import mainPackage.RunnerClass;

public class GA_ExtractDataFromPDF {

	public static boolean petFlag;
	public static String text="";
	public boolean arizona() throws Exception
	//public static void main(String args[]) throws Exception
	{
		
		GA_PropertyWare.petFlag = false;
		File file = RunnerClass.getLastModified();
		//File file = new File("C:\\Gopi\\Projects\\Property ware\\Lease Close Outs\\PDFS\\Georgia Format 1\\Georgia Format 2\\Lease_9.21_1.23_2554_Dolostone_Way_GA_Richman.pdf");
		FileInputStream fis = new FileInputStream(file);
		GA_RunnerClass.document = PDDocument.load(fis);
	    text = new PDFTextStripper().getText(GA_RunnerClass.document);
	    GA_PropertyWare.pdfText  = text;
	    if(!text.contains(AppConfig.PDFFormatConfirmationText)) 
	    {
	    	System.out.println("Wrong PDF Format");
	    	InsertDataIntoDatabase.notAutomatedFields(RunnerClass.leaseName, "Wrong Lease Agreement PDF Format");
			RunnerClass.leaseCompletedStatus = 3;
			return false;
	    	
	    }
	    text = text.replaceAll(System.lineSeparator(), " ");
	    text = text.trim().replaceAll(" +", " ");
	    System.out.println(text);
	    System.out.println("------------------------------------------------------------------");
	    try
	    {
	    	GA_PropertyWare.commensementDate = text.substring(text.indexOf(PDFAppConfig.AB_commencementDate_Prior)+PDFAppConfig.AB_commencementDate_Prior.length(),text.indexOf(PDFAppConfig.AB_expirationDate_Prior));
	    	GA_PropertyWare.commensementDate = GA_PropertyWare.commensementDate.trim().replaceAll(" +", " ");
	    }
	    catch(Exception e)
	    {
	    	GA_PropertyWare.commensementDate = "Error";
	    	e.printStackTrace();
	    }
	    System.out.println("Commensement Date = "+GA_PropertyWare.commensementDate);
	   try
	    {
		   String expirationDateWaw = text.substring(text.indexOf(PDFAppConfig.AB_expirationDate_Prior)+PDFAppConfig.AB_expirationDate_Prior.length());
		   GA_PropertyWare.expirationDate =expirationDateWaw.trim().split(" ")[0]+" "+expirationDateWaw.trim().split(" ")[1]+" "+expirationDateWaw.trim().split(" ")[2];
		   GA_PropertyWare.expirationDate = GA_PropertyWare.expirationDate.trim().replaceAll(" +", " ");
	    }
	    catch(Exception e)
	    {
	    	 GA_PropertyWare.expirationDate = "Error";
	    	 e.printStackTrace();
	    }
	   System.out.println("Expiration Date = "+GA_PropertyWare.expirationDate);
	   try
	    {
		    GA_PropertyWare.proratedRent = text.substring(text.indexOf(PDFAppConfig.AB_proratedRent_Prior)+PDFAppConfig.AB_proratedRent_Prior.length(),text.indexOf(PDFAppConfig.AB_proratedRent_After));
		    if(GA_PropertyWare.proratedRent.matches(".*[a-zA-Z]+.*"))
		    {
		    	GA_PropertyWare.proratedRent = "Error";
		    }
		    		
	    }
	    catch(Exception e)
	    {
	    	GA_PropertyWare.proratedRent = "Error";
	    	e.printStackTrace();
	    }
	   System.out.println("Prorated Rent = "+GA_PropertyWare.proratedRent);
	    try
	    {
		    GA_PropertyWare.proratedRentDate = text.substring(text.indexOf(PDFAppConfig.AB_proratedRentDate_Prior)+PDFAppConfig.AB_proratedRentDate_Prior.length(),text.indexOf(PDFAppConfig.AB_proratedRentDate_After)).trim();
	    }
	    catch(Exception e)
	    {
	    	GA_PropertyWare.proratedRentDate = "Error";
	    	e.printStackTrace();
	    }
	    System.out.println("Prorated Rent Date= "+GA_PropertyWare.proratedRentDate.trim());
	    /*
	    try
	    {
		    GA_PropertyWare.monthlyRentDate = text.substring(text.indexOf(PDFAppConfig.AB_fullRentDate_Prior)+PDFAppConfig.AB_fullRentDate_Prior.length(),text.indexOf(PDFAppConfig.AB_fullRentDate_After));
		    System.out.println("Monthly Rent Date= "+GA_PropertyWare.monthlyRentDate.trim());
	    }
	    catch(Exception e)
	    {
	    	try
	    	{
	    		GA_PropertyWare.monthlyRentDate = text.substring(text.indexOf(PDFAppConfig.AB_fullRentDate_Prior)+PDFAppConfig.AB_fullRentDate_Prior.length(),text.indexOf(PDFAppConfig.AB_fullRentDate1_After));
			   	System.out.println("Monthly Rent Date= "+GA_PropertyWare.monthlyRentDate.trim());
	    	}
	    	catch(Exception e1)
		    {
		    	GA_PropertyWare.monthlyRentDate = "Error";  
		    	e1.printStackTrace();
		    }
	    }*/
	    try
	    {
		    GA_PropertyWare.monthlyRent = text.substring(text.indexOf(PDFAppConfig.AB_fullRent_Prior)+PDFAppConfig.AB_fullRent_Prior.length()).trim().split(" ")[0].trim();//,text.indexOf(PDFAppConfig.AB_fullRent_After)).substring(1).replaceAll("[^.0-9]", "");;
		    if(RunnerClass.onlyDigits(GA_PropertyWare.monthlyRent.replace(".", "").replace(",", ""))==false)
		    {
		    	GA_PropertyWare.monthlyRent = text.substring(text.indexOf(PDFAppConfig.AB_fullRent2_Prior)+PDFAppConfig.AB_fullRent2_Prior.length()).trim().split(" ")[0].trim();
		    }
		    if(GA_PropertyWare.monthlyRent.contains("*"))
		    {
		    	GA_PropertyWare.monthlyRent = GA_PropertyWare.monthlyRent.replace("*","");
		    }
		    if(GA_PropertyWare.monthlyRent.matches(".*[a-zA-Z]+.*"))
		    {
		    	GA_PropertyWare.monthlyRent = "Error";
		    }
	    }
	    catch(Exception e)
	    {
	    	 GA_PropertyWare.monthlyRent = "Error";
	    	 e.printStackTrace();
	    }
	    System.out.println("Monthly Rent "+GA_PropertyWare.monthlyRent.trim());
	    try
	    {
		    GA_PropertyWare.adminFee = text.substring(text.indexOf(PDFAppConfig.AB_adminFee_Prior)+PDFAppConfig.AB_adminFee_Prior.length()).split(" ")[0];
		    if(GA_PropertyWare.adminFee.matches(".*[a-zA-Z]+.*"))
		    {
		    	GA_PropertyWare.adminFee = "Error";
		    }
	    }
	    catch(Exception e)
	    {
		    GA_PropertyWare.adminFee = "Error";
		    e.printStackTrace();
	    }
	    System.out.println("Admin Fee = "+GA_PropertyWare.adminFee.trim());
	    
	  //Resident Benefits Package 
	    if(text.contains(PDFAppConfig.residentBenefitsPackageAddendumCheck))
	    {
	    	GA_PropertyWare.residentBenefitsPackageAvailabilityCheck = true;
	    	 try
	 	    {
	 		    GA_PropertyWare.residentBenefitsPackage = text.substring(text.indexOf(PDFAppConfig.AB1_residentBenefitsPackage_Prior)+PDFAppConfig.AB1_residentBenefitsPackage_Prior.length()).split(" ")[0];
	 		    if(GA_PropertyWare.residentBenefitsPackage.matches(".*[a-zA-Z]+.*"))
	 		    {
	 		    	GA_PropertyWare.residentBenefitsPackage = "Error";
	 		    }
	 	    }
	 	    catch(Exception e)
	 	    {
	 		    GA_PropertyWare.residentBenefitsPackage = "Error";
	 		    e.printStackTrace();
	 	    }
	    	 System.out.println("Resident Benefits Package  = "+GA_PropertyWare.residentBenefitsPackage.trim());
	    	//PDFAppConfig.AB1_residentBenefitsPackage_Prior
	    }
	    else
	    {
		    if(text.contains(PDFAppConfig_Format2.HVACFilterAddendumTextAvailabilityCheck)==true)
		    {
		    	GA_PropertyWare.HVACFilterFlag =true;
		    }
		    else
		    {
		    try
		    {
			   String[] airFilterFeeArray = text.substring(text.indexOf(PDFAppConfig.AB_airFilterFee_Prior)+PDFAppConfig.AB_airFilterFee_Prior.length()).split(" ");
			   GA_PropertyWare.airFilterFee = airFilterFeeArray[0];
			   if(GA_PropertyWare.airFilterFee.matches(".*[a-zA-Z]+.*"))
			    {
			    	GA_PropertyWare.airFilterFee = "Error";
			    }
		    }
		    catch(Exception e)
		    {
		    GA_PropertyWare.airFilterFee = "Error";
		    e.printStackTrace();
		    }
		    }
		    System.out.println("Air Filter Fee = "+GA_PropertyWare.airFilterFee.trim());
	    }
	    try
	    {
	    	String[] earlyTerminationRaw = text.substring(text.indexOf(PDFAppConfig.AB_earlyTerminationFee_Prior)+PDFAppConfig.AB_earlyTerminationFee_Prior.length()).split(" ");
	    	
		    GA_PropertyWare.earlyTermination = earlyTerminationRaw[0]+earlyTerminationRaw[1]; //text.substring(text.indexOf(PDFAppConfig.AB_earlyTerminationFee_Prior)+PDFAppConfig.AB_earlyTerminationFee_Prior.length(),text.indexOf(PDFAppConfig.AB_earlyTerminationFee_After));
	    }
	    catch(Exception e)
	    {
	    	GA_PropertyWare.earlyTermination = "Error";	
	    	e.printStackTrace();
	    }
	    System.out.println("Early Termination  = "+GA_PropertyWare.earlyTermination.trim());
	    try
	    {
	    	
		    GA_PropertyWare.occupants = text.substring(text.indexOf(PDFAppConfig.AB_occupants_Prior)+PDFAppConfig.AB_occupants_Prior.length(),text.indexOf(PDFAppConfig.AB_occupants_After));
	    }
	    catch(Exception e)
	    {
		    GA_PropertyWare.occupants ="Error";	
		    e.printStackTrace();
	    }
	    System.out.println("Occupants = "+GA_PropertyWare.occupants.trim());
	    
	    //Late charges 
	    //Decide Late Fee Rule
	   GA_ExtractDataFromPDF.lateFeeRule();
	    
	  //Prepayment Charge
  		if(GA_PropertyWare.portfolioType.contains("MCH"))
  		{
  			if(GA_PropertyWare.proratedRent.equalsIgnoreCase("n/a")||GA_PropertyWare.proratedRent.equalsIgnoreCase("Error")||GA_PropertyWare.proratedRent.equalsIgnoreCase(""))
  			{
  				GA_PropertyWare.prepaymentCharge = "Error";
  			}
  			else
  			{
	  		try
	  		{
	  		GA_PropertyWare.prepaymentCharge =String.valueOf(Double.parseDouble(GA_PropertyWare.monthlyRent.replace(",", "")) - Double.parseDouble(GA_PropertyWare.proratedRent.replace(",", ""))); 
	  		}
	  		catch(Exception e)
	  		{
	  			GA_PropertyWare.prepaymentCharge ="Error";
	  		}
	  		}
  			System.out.println("Prepayment Charge = "+GA_PropertyWare.prepaymentCharge);
  		 }
	    petFlag = text.contains(PDFAppConfig.AB_petAgreementAvailabilityCheck);
	    System.out.println("Pet Addendum Available = "+petFlag);
	    if(petFlag ==true)
	    {
	    	GA_PropertyWare.petFlag = true;
	    	try
	    	{
	    	GA_PropertyWare.petSecurityDeposit = text.substring(text.indexOf(PDFAppConfig.AB_securityDeposity_Prior)+PDFAppConfig.AB_securityDeposity_Prior.length(),text.indexOf(PDFAppConfig.AB_securityDeposity_After));
	    	if(GA_PropertyWare.petSecurityDeposit.matches(".*[a-zA-Z]+.*"))
		    {
		    	GA_PropertyWare.petSecurityDeposit = "Error";
		    }
	    	}
	    	catch(Exception e)
	    	{
	    	GA_PropertyWare.petSecurityDeposit = "Error";	
	    	e.printStackTrace();
	    	}
	    	System.out.println("Pet Security Deposit = "+GA_PropertyWare.petSecurityDeposit.trim());
	    	if(RunnerClass.onlyDigits(GA_PropertyWare.petSecurityDeposit.replace(".", ""))==true)
		    {
		    	System.out.println("Security Deposit is checked");
		    }
	    	//TODO Check
	    	  try
			    {
	    		  String proratedPetRaw = "Prorated Pet Rent: On or before "+GA_PropertyWare.commensementDate.trim()+" Tenant will pay Landlord $";
	    		GA_PropertyWare.proratedPetRent = text.substring(text.indexOf(proratedPetRaw)+proratedPetRaw.length()).trim().split(" ")[0];//.replaceAll("[a-ZA-Z,]", "");
			    //AR_PropertyWare.proratedPetRent = proratedPetRentRaw.substring(proratedPetRentRaw.indexOf("Tenant will pay Landlord $")+"Tenant will pay Landlord $".length());//,proratedPetRentRaw.indexOf(AppConfig.AR_proratedPetRent_After));
			    if(GA_PropertyWare.proratedPetRent.matches(".*[a-zA-Z]+.*"))
			    {
			    	GA_PropertyWare.proratedPetRent = "Error";
			    }
			    }
			    catch(Exception e)
			    {
			   
			    GA_PropertyWare.proratedPetRent = "Error";	
			    e.printStackTrace();
			    }
	    	  System.out.println("Prorated Pet Rent = "+GA_PropertyWare.proratedPetRent.trim());
	    	
	    	try
		    {
	    		 GA_PropertyWare.petRent = text.substring(text.indexOf(PDFAppConfig.AB_petRent_Prior)+PDFAppConfig.AB_petRent_Prior.length()).trim().split(" ")[0];
	    		 if(GA_PropertyWare.petRent.contains(",for"))
	    		 {
	    			 GA_PropertyWare.petRent = GA_PropertyWare.petRent.split(",")[0].trim();
	    		 }
	    		 else
	    		 {
		    		 if(GA_PropertyWare.petRent.matches(".*[a-zA-Z]+.*")==true)
		    			 GA_PropertyWare.petRent = text.substring(text.indexOf(PDFAppConfig.AB_petRent1_Prior)+PDFAppConfig.AB_petRent1_Prior.length()).trim().split(" ")[0];
		    		 else 
		    		 GA_PropertyWare.petRent = RunnerClass.extractNumber(GA_PropertyWare.petRent);
	    		 }
		    }
	    	catch(Exception e)
		    {
	    		try
	    		{
	    			e.printStackTrace();
	    			GA_PropertyWare.petRent = text.substring(text.indexOf(PDFAppConfig.AB_petRent1_Prior)+PDFAppConfig.AB_petRent1_Prior.length()).trim().split(" ")[0];
//					 System.out.println("Pet rent = "+GA_PropertyWare.petRent.trim());
	    			if(GA_PropertyWare.petRent.matches(".*[a-zA-Z]+.*"))
	    		    {
	    		    	GA_PropertyWare.petRent = "Error";
	    		    }
	    		}
	    		
	    		catch(Exception e1)
			    {
			    	GA_PropertyWare.petRent = "Error";  
			    	e1.printStackTrace();
			    }
		    }
	    	System.out.println("Pet rent = "+GA_PropertyWare.petRent.trim());
		    	//GA_PropertyWare.petRent = "Error";  
		    	//e.printStackTrace();
		   /* 
	    	try
    		{
    			//String petFeeRaw1 = text.substring(text.indexOf(PDFAppConfig.AB_petFee_Prior));
    			GA_PropertyWare.petFee = text.substring(text.indexOf(PDFAppConfig.AB_petFee_Prior)+PDFAppConfig.AB_petFee_Prior.length()).trim().split(" ")[0].trim();
    			//GA_PropertyWare.petFee =  petFeeRaw[petFeeRaw.length-2].trim();
    			//if(GA_PropertyWare.petFee.matches(".*[a-zA-Z]+.*"))
    			//{
    				//GA_PropertyWare.petFee = text.substring(text.indexOf(PDFAppConfig.AB_petFee2_Prior)+PDFAppConfig.AB_petFee2_Prior.length()).trim().split(" ")[0].trim();
    			//}
    			//System.out.println(petFeeRaw.length);
    		}
    		
    		catch(Exception e1)
		    {
		    	GA_PropertyWare.petFee = "Error";  
		    	e1.printStackTrace();
		    }
	    	System.out.println("Pet Fee = "+GA_PropertyWare.petFee);
	    	*/
	    	// Get text between Type: word
	    	
	    	String typeSubString = text.substring(text.indexOf(PDFAppConfig.AB_typeWord_Prior)+PDFAppConfig.AB_typeWord_Prior.length(),text.indexOf(PDFAppConfig.AB_typeWord_After));
	    	
	    	String newText = typeSubString.replace("Type:","");
		    GA_PropertyWare.countOfTypeWordInText = ((typeSubString.length() - newText.length())/"Type:".length());
		    System.out.println("Type: occurences = "+GA_PropertyWare.countOfTypeWordInText);
		    
		    GA_PropertyWare.petType = new ArrayList();
		    GA_PropertyWare.petBreed = new ArrayList();
		    GA_PropertyWare.petWeight = new ArrayList();
		    for(int i =0;i<GA_PropertyWare.countOfTypeWordInText;i++)
		    {
		    	String type = typeSubString.substring(RunnerClass.nthOccurrence(typeSubString, "Type:", i+1)+PDFAppConfig.AB_pet1Type_Prior.length(),RunnerClass.nthOccurrence(typeSubString, "Breed:", i+1)).trim();
		    	if(type.contains("N/A")||type.contains("n/a"))
		    		break;
		    	System.out.println(type);
		    	GA_PropertyWare.petType.add(type);
		    	int pet1Breedindex1 = RunnerClass.nthOccurrence(typeSubString, "Breed:", i+1)+"Breed:".length()+1;
			    String subString = typeSubString.substring(pet1Breedindex1);
			    //int pet1Breedindex2 = RunnerClass.nthOccurrence(subString,"Name:",i+1);
			   // System.out.println("Index 2 = "+(index2+index1));
			    String breed = subString.split("Name:")[0].trim();//typeSubString.substring(pet1Breedindex1,(pet1Breedindex2+pet1Breedindex1));
			    System.out.println(breed);
			    GA_PropertyWare.petBreed.add(breed);
			    int pet1Weightindex1 = RunnerClass.nthOccurrence(typeSubString, "Weight:", i+1)+"Weight:".length()+1;
			    String pet1WeightSubstring = typeSubString.substring(pet1Weightindex1);
			    //int pet1WeightIndex2 = RunnerClass.nthOccurrence(pet1WeightSubstring,"Age:",i+1);
			   // System.out.println("Index 2 = "+(index2+index1));
			    String weight = pet1WeightSubstring.split("Age:")[0].trim(); //typeSubString.substring(pet1Weightindex1,(pet1WeightIndex2+pet1Weightindex1));
			    System.out.println(weight);
			    GA_PropertyWare.petWeight.add(weight);
		    }
		    
		    
		    
		    try
		    {
		    	GA_PropertyWare.petOneTimeNonRefundableFee = text.substring(text.indexOf(PDFAppConfig.AB_petFeeOneTime_Prior)+PDFAppConfig.AB_petFeeOneTime_Prior.length()).split(" ")[0];//,text.indexOf(PDFAppConfig.AB_petFeeOneTime_After));
		    	if(GA_PropertyWare.petOneTimeNonRefundableFee.matches(".*[a-zA-Z]+.*"))
    		    {
    		    	GA_PropertyWare.petOneTimeNonRefundableFee = "Error";
    		    }
		    }
		    catch(Exception e)
		    {
		    	GA_PropertyWare.petOneTimeNonRefundableFee =  "Error";
		    	e.printStackTrace();
		    }  
		    System.out.println("pet one time non refundable = "+GA_PropertyWare.petOneTimeNonRefundableFee.trim());
		   
	    }
	    
	    //Service Animal Addendum check
	    try
	    {
	    if(text.contains(AppConfig.serviceAnimalText))
	    {
	    	GA_PropertyWare.serviceAnimalFlag = true;
    		System.out.println("Service Animal Addendum is available");
    		
            String typeSubString = text.substring(text.indexOf(PDFAppConfig.AB_serviceAnimal_typeWord_Prior)+PDFAppConfig.AB_serviceAnimal_typeWord_Prior.length(),text.indexOf(PDFAppConfig.AB_serviceAnimal_typeWord_After));
	    	
	    	String newText = typeSubString.replace("Type:","");
		    int  countOftypeWords_ServiceAnimal = ((typeSubString.length() - newText.length())/"Type:".length());
		    System.out.println("Service Animal - Type: occurences = "+countOftypeWords_ServiceAnimal);
		    
		    GA_PropertyWare.serviceAnimalPetType = new ArrayList();
		    GA_PropertyWare.serviceAnimalPetBreed = new ArrayList();
		    GA_PropertyWare.serviceAnimalPetWeight = new ArrayList();
		    for(int i =0;i<countOftypeWords_ServiceAnimal;i++)
		    {
		    	String type = typeSubString.substring(RunnerClass.nthOccurrence(typeSubString, "Type:", i+1)+PDFAppConfig.AB_pet1Type_Prior.length(),RunnerClass.nthOccurrence(typeSubString, "Breed:", i+1)).trim();
		    	if(type.contains("N/A")||type.contains("n/a"))
		    		break;
		    	System.out.println(type);
		    	GA_PropertyWare.serviceAnimalPetType.add(type);
		    	int pet1Breedindex1 = RunnerClass.nthOccurrence(typeSubString, "Breed:", i+1)+"Breed:".length()+1;
			    String subString = typeSubString.substring(pet1Breedindex1);
			    //int pet1Breedindex2 = RunnerClass.nthOccurrence(subString,"Name:",i+1);
			   // System.out.println("Index 2 = "+(index2+index1));
			    String breed = subString.split("Name:")[0].trim();//typeSubString.substring(pet1Breedindex1,(pet1Breedindex2+pet1Breedindex1));
			    System.out.println(breed);
			    GA_PropertyWare.serviceAnimalPetBreed.add(breed);
			    int pet1Weightindex1 = RunnerClass.nthOccurrence(typeSubString, "Weight:", i+1)+"Weight:".length()+1;
			    String pet1WeightSubstring = typeSubString.substring(pet1Weightindex1);
			    //int pet1WeightIndex2 = RunnerClass.nthOccurrence(pet1WeightSubstring,"Age:",i+1);
			   // System.out.println("Index 2 = "+(index2+index1));
			    String weight = pet1WeightSubstring.split("Age:")[0].trim(); //typeSubString.substring(pet1Weightindex1,(pet1WeightIndex2+pet1Weightindex1));
			    System.out.println(weight);
			    GA_PropertyWare.serviceAnimalPetWeight.add(weight);
		    }
    		
    		
	    }
	    }
	    catch(Exception e)
	    {
	    	GA_PropertyWare.serviceAnimalFlag = false;
	    }
	    //Concession Addendum
	    
	    try
	    {
	    	if(text.contains(PDFAppConfig.concessionAddendumText))
	    	{
	    		GA_PropertyWare.concessionAddendumFlag = true;
	    		System.out.println("Concession Addendum is available");
	    	}
	    }
	    catch(Exception e)
	    {}
	    
	 // document.close();
	  return true;
    }
	public static boolean lateFeeRule()
	{
		String lateFeeRuleText ="";
		try
		{
		 lateFeeRuleText = text.substring(text.indexOf(PDFAppConfig.lateFeeRuleText_Prior)+PDFAppConfig.lateFeeRuleText_Prior.length(),text.indexOf(PDFAppConfig.lateFeeRuleText_After));
		}
		catch(Exception e)
		{
			try
			{
			lateFeeRuleText = text.substring(text.indexOf(PDFAppConfig.lateFeeRuleText_Prior)+PDFAppConfig.lateFeeRuleText_Prior.length(),text.indexOf(PDFAppConfig.lateFeeRuleText_After2));
			}
			catch(Exception e2)
			{
			return false;
			}
		}
		if(lateFeeRuleText.contains(PDFAppConfig.lateFeeRule_whicheverIsGreater))
		{
			GA_PropertyWare.lateFeeType ="GreaterOfFlatFeeOrPercentage"; 
		//Late charge day
			try
			{
		   // GA_PropertyWare.lateChargeDay =  lateFeeRuleText.substring(lateFeeRuleText.indexOf(PDFAppConfig.lateFeeRule_whicheverIsGreater_dueDay_Prior)+PDFAppConfig.lateFeeRule_whicheverIsGreater_dueDay_Prior.length()).trim().split(" ")[0];
				GA_PropertyWare.lateChargeDay =  lateFeeRuleText.split(PDFAppConfig.lateFeeRule_whicheverIsGreater_dueDay_After)[0].trim();
				GA_PropertyWare.lateChargeDay =GA_PropertyWare.lateChargeDay.substring(GA_PropertyWare.lateChargeDay.lastIndexOf(" ")+1);
		    GA_PropertyWare.lateChargeDay =  GA_PropertyWare.lateChargeDay.replaceAll("[^0-9]", "");
			}
			catch(Exception e)
			{
				GA_PropertyWare.lateChargeDay = "Error";
			}
         System.out.println("Late Charge Day = "+GA_PropertyWare.lateChargeDay);
			
		//Late Fee Percentage
			try
			{
		    GA_PropertyWare.lateFeePercentage =  lateFeeRuleText.substring(lateFeeRuleText.indexOf(PDFAppConfig.lateFeeRule_whicheverIsGreater_lateFeePercentage)+PDFAppConfig.lateFeeRule_whicheverIsGreater_lateFeePercentage.length()).trim().split(" ")[0];
		    GA_PropertyWare.lateFeePercentage = GA_PropertyWare.lateFeePercentage.replaceAll("[^0-9]", "");
			}
			catch(Exception e)
			{
				GA_PropertyWare.lateFeePercentage = "Error";
			}
         System.out.println("Late Fee Percentage = "+GA_PropertyWare.lateFeePercentage);
         
         //Late Fee Amount
         try
         {
         String lateFeeAmount  = lateFeeRuleText.substring(lateFeeRuleText.indexOf(PDFAppConfig.lateFeeRule_whicheverIsGreater_lateFeeAmount)+PDFAppConfig.lateFeeRule_whicheverIsGreater_lateFeeAmount.length()).trim().split(" ")[0];
         GA_PropertyWare.flatFeeAmount = lateFeeAmount.replaceAll("[^.0-9]", "");
         }
         catch(Exception e)
         {
        	 GA_PropertyWare.flatFeeAmount ="Error";
         }
         System.out.println("Late Fee Amount = "+GA_PropertyWare.flatFeeAmount);
         
        
         return true;
		}
		else 
		if(lateFeeRuleText.contains(PDFAppConfig.lateFeeRule_mayNotExceedMoreThan30Days))
		{
			GA_PropertyWare.lateFeeType ="initialFeePluPerDayFee"; 
	         try
	 	    {
	 		    GA_PropertyWare.lateChargeFee = text.substring(text.indexOf(PDFAppConfig.AB_lateFee_Prior)+PDFAppConfig.AB_lateFee_Prior.length()).trim().split(" ")[0];
	 		    //GA_PropertyWare.lateChargeFee =  GA_PropertyWare.lateChargeFee.substring(0,GA_PropertyWare.lateChargeFee.length()-1);
	 	    }
	 	    catch(Exception e)
	 	    {
	 		    GA_PropertyWare.lateChargeFee ="Error";	
	 		    e.printStackTrace();
	 	    }
	 	    System.out.println("Late Charge Fee = "+GA_PropertyWare.lateChargeFee.trim());
	 	    //Per Day Fee
	 	    try
	 	    {
	 	    	GA_PropertyWare.lateFeeChargePerDay = text.substring(text.indexOf(PDFAppConfig.AB_additionalLateChargesPerDay_Prior)+PDFAppConfig.AB_additionalLateChargesPerDay_Prior.length()).split(" ")[0].trim();//,text.indexOf(PDFAppConfig.AB_additionalLateChargesPerDay_After));
	 	    }
	 	    catch(Exception e)
	 	    {
	 	    	GA_PropertyWare.lateFeeChargePerDay =  "Error";	
	 	    	e.printStackTrace();
	 	    }
	 	    System.out.println("Per Day Fee = "+GA_PropertyWare.lateFeeChargePerDay.trim());
	 	    //Additional Late Charges Limit
	 	    try
	 	    {
	 	    	GA_PropertyWare.additionalLateChargesLimit = text.substring(text.indexOf(PDFAppConfig.AB_additionalLateChargesLimit_Prior)+PDFAppConfig.AB_additionalLateChargesLimit_Prior.length()).trim().split(" ")[0]; //,text.indexOf(PDFAppConfig.AB_additionalLateChargesLimit_After));
	 	    }
	 	    catch(Exception e)
	 	    {
	 	    	GA_PropertyWare.additionalLateChargesLimit =  "Error";	
	 	    	e.printStackTrace();
	 	    }
	 	    System.out.println("additional Late Charges Limit = "+GA_PropertyWare.additionalLateChargesLimit.trim());
	 	   return true;
		}
		else if(lateFeeRuleText.contains(PDFAppConfig.lateFeeRule_mayNotExceedAmount))
			{
			//Late Charge Day
			try
	 	    {
			GA_PropertyWare.lateChargeDay = lateFeeRuleText.substring(lateFeeRuleText.indexOf("an initial late charge on the")+"an initial late charge on the".length()).trim().split(" ")[0];
			GA_PropertyWare.lateChargeDay = GA_PropertyWare.lateChargeDay.replaceAll("[^0-9]", "");
	 	    }
			catch(Exception e)
	 	    {
	 	    	GA_PropertyWare.lateChargeDay =  "Error";	
	 	    	e.printStackTrace();
	 	    }
	 	    System.out.println("Late Charge Due Day = "+GA_PropertyWare.lateChargeDay.trim());
	 	    // initial Late Charge
	 	   try
	 	    {
			GA_PropertyWare.lateChargeFee = lateFeeRuleText.substring(lateFeeRuleText.indexOf("day of the month equal to $")+"day of the month equal to $".length()).trim().split(" ")[0];
			GA_PropertyWare.lateChargeFee = GA_PropertyWare.lateChargeFee.replaceAll("[^0-9.]", "");
	 	    }
			catch(Exception e)
	 	    {
	 	    	GA_PropertyWare.lateChargeFee =  "Error";	
	 	    	e.printStackTrace();
	 	    }
	 	    System.out.println("Late Charge Fee = "+GA_PropertyWare.lateChargeFee.trim());
	 	    // Additional Late Charges
	 	   try
	 	    {
			GA_PropertyWare.additionalLateCharges = lateFeeRuleText.substring(lateFeeRuleText.indexOf("and additional late charge of $")+"and additional late charge of $".length()).trim().split(" ")[0];
			GA_PropertyWare.additionalLateCharges = GA_PropertyWare.additionalLateCharges.replaceAll("[^0-9.]", "");
	 	    }
			catch(Exception e)
	 	    {
	 	    	GA_PropertyWare.additionalLateCharges =  "Error";	
	 	    	e.printStackTrace();
	 	    }
	 	    System.out.println("Additional Late Charges = "+GA_PropertyWare.additionalLateCharges.trim());
	 	    //Additional Late Charges Limit
	 	   try
	 	    {
			GA_PropertyWare.additionalLateChargesLimit = lateFeeRuleText.substring(lateFeeRuleText.indexOf("(initial and additional) may not exceed $")+"(initial and additional) may not exceed $".length()).trim().split(" ")[0];
			GA_PropertyWare.additionalLateChargesLimit = GA_PropertyWare.additionalLateChargesLimit.replaceAll("[^0-9.]", "");
	 	    }
			catch(Exception e)
	 	    {
	 	    	GA_PropertyWare.additionalLateChargesLimit =  "Error";	
	 	    	e.printStackTrace();
	 	    }
	 	    System.out.println("Additional Late Charges Limit = "+GA_PropertyWare.additionalLateChargesLimit.trim());
			return true;
			}
		else {
			GA_PropertyWare.lateFeeType ="";
			return false;
		}
		
	}

}

