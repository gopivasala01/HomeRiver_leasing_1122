package Arkansas;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import mainPackage.InsertDataIntoDatabase;
import mainPackage.RunnerClass;

public class AR_ExtractDataFromPDF 
{

	public static boolean petFlag;
	public static String text="";
	public boolean arizona() throws Exception
	//public static void main(String args[]) throws Exception
	{
		
		AR_PropertyWare.petFlag = false;
		File file = RunnerClass.getLastModified();
		//File file = new File("C:\\Gopi\\Projects\\Property ware\\Lease Close Outs\\PDFS\\Tennessee\\Pet agreement\\Lease_0722_0224_3305_Richland_View_Ln_TN_Gap.pdf");
		FileInputStream fis = new FileInputStream(file);
		AR_RunnerClass.document = PDDocument.load(fis);
	    text = new PDFTextStripper().getText(AR_RunnerClass.document);
	    AR_PropertyWare.pdfText  = text;
	    if(!text.contains(AR_AppConfig.PDFFormatConfirmationText)) 
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
	    	AR_PropertyWare.commensementDate = text.substring(text.indexOf(AR_PDFAppConfig.AB_commencementDate_Prior)+AR_PDFAppConfig.AB_commencementDate_Prior.length(),text.indexOf(AR_PDFAppConfig.AB_expirationDate_Prior));
	    	AR_PropertyWare.commensementDate = AR_PropertyWare.commensementDate.trim().replaceAll(" +", " ");
	    }
	    catch(Exception e)
	    {
	    	AR_PropertyWare.commensementDate = "Error";
	    	e.printStackTrace();
	    }
	    System.out.println("Commensement Date = "+AR_PropertyWare.commensementDate);
	   try
	    {
		   String expirationDateWaw = text.substring(text.indexOf(AR_PDFAppConfig.AB_expirationDate_Prior)+AR_PDFAppConfig.AB_expirationDate_Prior.length());
		   AR_PropertyWare.expirationDate =expirationDateWaw.trim().split(" ")[0]+" "+expirationDateWaw.trim().split(" ")[1]+" "+expirationDateWaw.trim().split(" ")[2];
		   AR_PropertyWare.expirationDate = AR_PropertyWare.expirationDate.trim().replaceAll(" +", " ");
	    }
	    catch(Exception e)
	    {
	    	 AR_PropertyWare.expirationDate = "Error";
	    	 e.printStackTrace();
	    }
	   System.out.println("Expiration Date = "+AR_PropertyWare.expirationDate);
	   try
	    {
		    AR_PropertyWare.proratedRent = text.substring(text.indexOf(AR_PDFAppConfig.AB_proratedRent_Prior)+AR_PDFAppConfig.AB_proratedRent_Prior.length(),text.indexOf(AR_PDFAppConfig.AB_proratedRent_After));
		    if(AR_PropertyWare.proratedRent.matches(".*[a-zA-Z]+.*"))
		    {
		    	AR_PropertyWare.proratedRent = "Error";
		    }
		    		
	    }
	    catch(Exception e)
	    {
	    	AR_PropertyWare.proratedRent = "Error";
	    	e.printStackTrace();
	    }
	   System.out.println("Prorated Rent = "+AR_PropertyWare.proratedRent);
	    try
	    {
		    AR_PropertyWare.proratedRentDate = text.substring(text.indexOf(AR_PDFAppConfig.AB_proratedRentDate_Prior)+AR_PDFAppConfig.AB_proratedRentDate_Prior.length(),text.indexOf(AR_PDFAppConfig.AB_proratedRentDate_After)).trim();
	    }
	    catch(Exception e)
	    {
	    	AR_PropertyWare.proratedRentDate = "Error";
	    	e.printStackTrace();
	    }
	    System.out.println("Prorated Rent Date= "+AR_PropertyWare.proratedRentDate.trim());
	    /*
	    try
	    {
		    AR_PropertyWare.monthlyRentDate = text.substring(text.indexOf(AR_PDFAppConfig.AB_fullRentDate_Prior)+AR_PDFAppConfig.AB_fullRentDate_Prior.length(),text.indexOf(AR_PDFAppConfig.AB_fullRentDate_After));
		    System.out.println("Monthly Rent Date= "+AR_PropertyWare.monthlyRentDate.trim());
	    }
	    catch(Exception e)
	    {
	    	try
	    	{
	    		AR_PropertyWare.monthlyRentDate = text.substring(text.indexOf(AR_PDFAppConfig.AB_fullRentDate_Prior)+AR_PDFAppConfig.AB_fullRentDate_Prior.length(),text.indexOf(AR_PDFAppConfig.AB_fullRentDate1_After));
			   	System.out.println("Monthly Rent Date= "+AR_PropertyWare.monthlyRentDate.trim());
	    	}
	    	catch(Exception e1)
		    {
		    	AR_PropertyWare.monthlyRentDate = "Error";  
		    	e1.printStackTrace();
		    }
	    }*/
	    try
	    {
		    AR_PropertyWare.monthlyRent = text.substring(text.indexOf(AR_PDFAppConfig.AB_fullRent_Prior)+AR_PDFAppConfig.AB_fullRent_Prior.length()).trim().split(" ")[0].trim();//,text.indexOf(AR_PDFAppConfig.AB_fullRent_After)).substring(1).replaceAll("[^.0-9]", "");;
		    if(RunnerClass.onlyDigits(AR_PropertyWare.monthlyRent.replace(".", "").replace(",", ""))==false)
		    {
		    	AR_PropertyWare.monthlyRent = text.substring(text.indexOf(AR_PDFAppConfig.AB_fullRent2_Prior)+AR_PDFAppConfig.AB_fullRent2_Prior.length()).trim().split(" ")[0].trim();
		    }
		    if(AR_PropertyWare.monthlyRent.contains("*"))
		    {
		    	AR_PropertyWare.monthlyRent = AR_PropertyWare.monthlyRent.replace("*","");
		    }
		    if(AR_PropertyWare.monthlyRent.matches(".*[a-zA-Z]+.*"))
		    {
		    	AR_PropertyWare.monthlyRent = "Error";
		    }
		    if(AR_PropertyWare.monthlyRent.endsWith(","))
		    {
		    	AR_PropertyWare.monthlyRent = AR_PropertyWare.monthlyRent.substring(0,AR_PropertyWare.monthlyRent.length()-1);
		    }
	    }
	    catch(Exception e)
	    {
	    	 AR_PropertyWare.monthlyRent = "Error";
	    	 e.printStackTrace();
	    }
	    System.out.println("Monthly Rent "+AR_PropertyWare.monthlyRent.trim());
	    try
	    {
		    AR_PropertyWare.adminFee = text.substring(text.indexOf(AR_PDFAppConfig.AB_adminFee_Prior)+AR_PDFAppConfig.AB_adminFee_Prior.length()).trim().split(" ")[0];
		    if(AR_PropertyWare.adminFee.matches(".*[a-zA-Z]+.*"))
		    {
		    	AR_PropertyWare.adminFee = "Error";
		    }
	    }
	    catch(Exception e)
	    {
		    AR_PropertyWare.adminFee = "Error";
		    e.printStackTrace();
	    }
	    System.out.println("Admin Fee = "+AR_PropertyWare.adminFee.trim());
	    
	  //Resident Benefits Package 
	    if(text.contains(AR_PDFAppConfig.residentBenefitsPackageAddendumCheck))
	    {
	    	AR_PropertyWare.residentBenefitsPackageAvailabilityCheck = true;
	    	 try
	 	    {
	 		    AR_PropertyWare.residentBenefitsPackage = text.substring(text.indexOf(AR_PDFAppConfig.AB1_residentBenefitsPackage_Prior)+AR_PDFAppConfig.AB1_residentBenefitsPackage_Prior.length()).split(" ")[0].replaceAll("[^0-9a-zA-Z.]", "");
	 		    if(AR_PropertyWare.residentBenefitsPackage.matches(".*[a-zA-Z]+.*"))
	 		    {
	 		    	AR_PropertyWare.residentBenefitsPackage = "Error";
	 		    }
	 	    }
	 	    catch(Exception e)
	 	    {
	 		    AR_PropertyWare.residentBenefitsPackage = "Error";
	 		    e.printStackTrace();
	 	    }
	    	 System.out.println("Resident Benefits Package  = "+AR_PropertyWare.residentBenefitsPackage.trim());
	    	//AR_PDFAppConfig.AB1_residentBenefitsPackage_Prior
	    }
	    else
	    {
		    if(text.contains(AR_PDFAppConfig_Format2.HVACFilterAddendumTextAvailabilityCheck)==true)
		    {
		    	AR_PropertyWare.HVACFilterFlag =true;
		    }
		    else
		    {
		    try
		    {
			   String[] airFilterFeeArray = text.substring(text.indexOf(AR_PDFAppConfig.AB_airFilterFee_Prior)+AR_PDFAppConfig.AB_airFilterFee_Prior.length()).split(" ");
			   AR_PropertyWare.airFilterFee = airFilterFeeArray[0];
			   if(AR_PropertyWare.airFilterFee.matches(".*[a-zA-Z]+.*"))
			    {
			    	AR_PropertyWare.airFilterFee = "Error";
			    }
		    }
		    catch(Exception e)
		    {
		    AR_PropertyWare.airFilterFee = "Error";
		    e.printStackTrace();
		    }
		    }
		    System.out.println("Air Filter Fee = "+AR_PropertyWare.airFilterFee.trim());
	    }
	    try
	    {
	    	String[] earlyTerminationRaw = text.substring(text.indexOf(AR_PDFAppConfig.AB_earlyTerminationFee_Prior)+AR_PDFAppConfig.AB_earlyTerminationFee_Prior.length()).split(" ");
	    	
		    AR_PropertyWare.earlyTermination = earlyTerminationRaw[0]+earlyTerminationRaw[1]; //text.substring(text.indexOf(AR_PDFAppConfig.AB_earlyTerminationFee_Prior)+AR_PDFAppConfig.AB_earlyTerminationFee_Prior.length(),text.indexOf(AR_PDFAppConfig.AB_earlyTerminationFee_After));
	    }
	    catch(Exception e)
	    {
	    	AR_PropertyWare.earlyTermination = "Error";	
	    	e.printStackTrace();
	    }
	    System.out.println("Early Termination  = "+AR_PropertyWare.earlyTermination.trim());
	    try
	    {
	    	
		    AR_PropertyWare.occupants = text.substring(text.indexOf(AR_PDFAppConfig.AB_occupants_Prior)+AR_PDFAppConfig.AB_occupants_Prior.length(),text.indexOf(AR_PDFAppConfig.AB_occupants_After));
	    }
	    catch(Exception e)
	    {
		    AR_PropertyWare.occupants ="Error";	
		    e.printStackTrace();
	    }
	    System.out.println("Occupants = "+AR_PropertyWare.occupants.trim());
	    
	    //Late charges 
	    //Decide Late Fee Rule
	   AR_ExtractDataFromPDF.lateFeeRule();
	    
	  //Prepayment Charge
  		if(AR_PropertyWare.portfolioType.contains("MCH"))
  		{
  			if(AR_PropertyWare.proratedRent.equalsIgnoreCase("n/a")||AR_PropertyWare.proratedRent.equalsIgnoreCase("Error")||AR_PropertyWare.proratedRent.equalsIgnoreCase(""))
  			{
  				AR_PropertyWare.prepaymentCharge = "Error";
  			}
  			else
  			{
	  		try
	  		{
	  		AR_PropertyWare.prepaymentCharge =String.valueOf(Double.parseDouble(AR_PropertyWare.monthlyRent.trim().replace(",", "")) - Double.parseDouble(AR_PropertyWare.proratedRent.trim().replace(",", ""))); 
	  		}
	  		catch(Exception e)
	  		{
	  			AR_PropertyWare.prepaymentCharge ="Error";
	  		}
	  		}
  			System.out.println("Prepayment Charge = "+AR_PropertyWare.prepaymentCharge);
  		 }
	    petFlag = text.contains(AR_PDFAppConfig.AB_petAgreementAvailabilityCheck);
	    System.out.println("Pet Addendum Available = "+petFlag);
	    if(petFlag ==true)
	    {
	    	AR_PropertyWare.petFlag = true;
	    	try
	    	{
	    	AR_PropertyWare.petSecurityDeposit = text.substring(text.indexOf(AR_PDFAppConfig.AB_securityDeposity_Prior)+AR_PDFAppConfig.AB_securityDeposity_Prior.length(),text.indexOf(AR_PDFAppConfig.AB_securityDeposity_After));
	    	if(AR_PropertyWare.petSecurityDeposit.matches(".*[a-zA-Z]+.*"))
		    {
		    	AR_PropertyWare.petSecurityDeposit = "Error";
		    }
	    	}
	    	catch(Exception e)
	    	{
	    	AR_PropertyWare.petSecurityDeposit = "Error";	
	    	e.printStackTrace();
	    	}
	    	System.out.println("Pet Security Deposit = "+AR_PropertyWare.petSecurityDeposit.trim());
	    	if(RunnerClass.onlyDigits(AR_PropertyWare.petSecurityDeposit.replace(".", ""))==true)
		    {
		    	System.out.println("Security Deposit is checked");
		    }
	    	//TODO Check
	    	  try
			    {
	    		  String proratedPetRaw = "Prorated Pet Rent: On or before "+AR_PropertyWare.commensementDate.trim()+" Tenant will pay Landlord $";
	    		AR_PropertyWare.proratedPetRent = text.substring(text.indexOf(proratedPetRaw)+proratedPetRaw.length()).trim().split(" ")[0];//.replaceAll("[a-ZA-Z,]", "");
			    //AR_PropertyWare.proratedPetRent = proratedPetRentRaw.substring(proratedPetRentRaw.indexOf("Tenant will pay Landlord $")+"Tenant will pay Landlord $".length());//,proratedPetRentRaw.indexOf(AppConfig.AR_proratedPetRent_After));
			    if(AR_PropertyWare.proratedPetRent.matches(".*[a-zA-Z]+.*"))
			    {
			    	AR_PropertyWare.proratedPetRent = "Error";
			    }
			    }
			    catch(Exception e)
			    {
			   
			    AR_PropertyWare.proratedPetRent = "Error";	
			    e.printStackTrace();
			    }
	    	  System.out.println("Prorated Pet Rent = "+AR_PropertyWare.proratedPetRent.trim());
	    	
	    	try
		    {
	    		 AR_PropertyWare.petRent = text.substring(text.indexOf(AR_PDFAppConfig.AB_petRent_Prior)+AR_PDFAppConfig.AB_petRent_Prior.length()).trim().split(" ")[0];
	    		 if(AR_PropertyWare.petRent.contains(",for"))
	    		 {
	    			 AR_PropertyWare.petRent = AR_PropertyWare.petRent.split(",")[0].trim();
	    		 }
	    		 else
	    		 {
		    		 if(AR_PropertyWare.petRent.matches(".*[a-zA-Z]+.*")==true)
		    			 AR_PropertyWare.petRent = text.substring(text.indexOf(AR_PDFAppConfig.AB_petRent1_Prior)+AR_PDFAppConfig.AB_petRent1_Prior.length()).trim().split(" ")[0];
		    		 else 
		    		 AR_PropertyWare.petRent = RunnerClass.extractNumber(AR_PropertyWare.petRent);
	    		 }
		    }
	    	catch(Exception e)
		    {
	    		try
	    		{
	    			e.printStackTrace();
	    			AR_PropertyWare.petRent = text.substring(text.indexOf(AR_PDFAppConfig.AB_petRent1_Prior)+AR_PDFAppConfig.AB_petRent1_Prior.length()).trim().split(" ")[0];
//					 System.out.println("Pet rent = "+AR_PropertyWare.petRent.trim());
	    			if(AR_PropertyWare.petRent.matches(".*[a-zA-Z]+.*"))
	    		    {
	    		    	AR_PropertyWare.petRent = "Error";
	    		    }
	    		}
	    		
	    		catch(Exception e1)
			    {
			    	AR_PropertyWare.petRent = "Error";  
			    	e1.printStackTrace();
			    }
		    }
	    	System.out.println("Pet rent = "+AR_PropertyWare.petRent.trim());
		    	//AR_PropertyWare.petRent = "Error";  
		    	//e.printStackTrace();
		   /* 
	    	try
    		{
    			//String petFeeRaw1 = text.substring(text.indexOf(AR_PDFAppConfig.AB_petFee_Prior));
    			AR_PropertyWare.petFee = text.substring(text.indexOf(AR_PDFAppConfig.AB_petFee_Prior)+AR_PDFAppConfig.AB_petFee_Prior.length()).trim().split(" ")[0].trim();
    			//AR_PropertyWare.petFee =  petFeeRaw[petFeeRaw.length-2].trim();
    			//if(AR_PropertyWare.petFee.matches(".*[a-zA-Z]+.*"))
    			//{
    				//AR_PropertyWare.petFee = text.substring(text.indexOf(AR_PDFAppConfig.AB_petFee2_Prior)+AR_PDFAppConfig.AB_petFee2_Prior.length()).trim().split(" ")[0].trim();
    			//}
    			//System.out.println(petFeeRaw.length);
    		}
    		
    		catch(Exception e1)
		    {
		    	AR_PropertyWare.petFee = "Error";  
		    	e1.printStackTrace();
		    }
	    	System.out.println("Pet Fee = "+AR_PropertyWare.petFee);
	    	*/
	    	// Get text between Type: word
	    	
	    	String typeSubString = text.substring(text.indexOf(AR_PDFAppConfig.AB_typeWord_Prior)+AR_PDFAppConfig.AB_typeWord_Prior.length(),text.indexOf(AR_PDFAppConfig.AB_typeWord_After));
	    	
	    	String newText = typeSubString.replace("Type:","");
		    AR_PropertyWare.countOfTypeWordInText = ((typeSubString.length() - newText.length())/"Type:".length());
		    System.out.println("Type: occurences = "+AR_PropertyWare.countOfTypeWordInText);
		    
		    AR_PropertyWare.petType = new ArrayList();
		    AR_PropertyWare.petBreed = new ArrayList();
		    AR_PropertyWare.petWeight = new ArrayList();
		    for(int i =0;i<AR_PropertyWare.countOfTypeWordInText;i++)
		    {
		    	String type = typeSubString.substring(RunnerClass.nthOccurrence(typeSubString, "Type:", i+1)+AR_PDFAppConfig.AB_pet1Type_Prior.length(),RunnerClass.nthOccurrence(typeSubString, "Breed:", i+1)).trim();
		    	if(type.contains("N/A")||type.contains("n/a"))
		    		break;
		    	System.out.println(type);
		    	AR_PropertyWare.petType.add(type);
		    	int pet1Breedindex1 = RunnerClass.nthOccurrence(typeSubString, "Breed:", i+1)+"Breed:".length()+1;
			    String subString = typeSubString.substring(pet1Breedindex1);
			    //int pet1Breedindex2 = RunnerClass.nthOccurrence(subString,"Name:",i+1);
			   // System.out.println("Index 2 = "+(index2+index1));
			    String breed = subString.split("Name:")[0].trim();//typeSubString.substring(pet1Breedindex1,(pet1Breedindex2+pet1Breedindex1));
			    System.out.println(breed);
			    AR_PropertyWare.petBreed.add(breed);
			    int pet1Weightindex1 = RunnerClass.nthOccurrence(typeSubString, "Weight:", i+1)+"Weight:".length()+1;
			    String pet1WeightSubstring = typeSubString.substring(pet1Weightindex1);
			    //int pet1WeightIndex2 = RunnerClass.nthOccurrence(pet1WeightSubstring,"Age:",i+1);
			   // System.out.println("Index 2 = "+(index2+index1));
			    String weight = pet1WeightSubstring.split("Age:")[0].trim(); //typeSubString.substring(pet1Weightindex1,(pet1WeightIndex2+pet1Weightindex1));
			    System.out.println(weight);
			    AR_PropertyWare.petWeight.add(weight);
		    }
		    
		    
		    
		    try
		    {
		    	AR_PropertyWare.petOneTimeNonRefundableFee = text.substring(text.indexOf(AR_PDFAppConfig.AB_petFeeOneTime_Prior)+AR_PDFAppConfig.AB_petFeeOneTime_Prior.length()).split(" ")[0];//,text.indexOf(AR_PDFAppConfig.AB_petFeeOneTime_After));
		    	if(AR_PropertyWare.petOneTimeNonRefundableFee.matches(".*[a-zA-Z]+.*"))
    		    {
    		    	AR_PropertyWare.petOneTimeNonRefundableFee = "Error";
    		    }
		    }
		    catch(Exception e)
		    {
		    	AR_PropertyWare.petOneTimeNonRefundableFee =  "Error";
		    	e.printStackTrace();
		    }  
		    System.out.println("pet one time non refundable = "+AR_PropertyWare.petOneTimeNonRefundableFee.trim());
		   
	    }
	    
	    //Service Animal Addendum check
	    try
	    {
	    if(text.contains(AR_AppConfig.serviceAnimalText))
	    {
	    	AR_PropertyWare.serviceAnimalFlag = true;
    		System.out.println("Service Animal Addendum is available");
    		
            String typeSubString = text.substring(text.indexOf(AR_PDFAppConfig.AB_serviceAnimal_typeWord_Prior)+AR_PDFAppConfig.AB_serviceAnimal_typeWord_Prior.length(),text.indexOf(AR_PDFAppConfig.AB_serviceAnimal_typeWord_After));
	    	
	    	String newText = typeSubString.replace("Type:","");
		    int  countOftypeWords_ServiceAnimal = ((typeSubString.length() - newText.length())/"Type:".length());
		    System.out.println("Service Animal - Type: occurences = "+countOftypeWords_ServiceAnimal);
		    
		    AR_PropertyWare.serviceAnimalPetType = new ArrayList();
		    AR_PropertyWare.serviceAnimalPetBreed = new ArrayList();
		    AR_PropertyWare.serviceAnimalPetWeight = new ArrayList();
		    for(int i =0;i<countOftypeWords_ServiceAnimal;i++)
		    {
		    	String type = typeSubString.substring(RunnerClass.nthOccurrence(typeSubString, "Type:", i+1)+AR_PDFAppConfig.AB_pet1Type_Prior.length(),RunnerClass.nthOccurrence(typeSubString, "Breed:", i+1)).trim();
		    	if(type.contains("N/A")||type.contains("n/a"))
		    		break;
		    	System.out.println(type);
		    	AR_PropertyWare.serviceAnimalPetType.add(type);
		    	int pet1Breedindex1 = RunnerClass.nthOccurrence(typeSubString, "Breed:", i+1)+"Breed:".length()+1;
			    String subString = typeSubString.substring(pet1Breedindex1);
			    //int pet1Breedindex2 = RunnerClass.nthOccurrence(subString,"Name:",i+1);
			   // System.out.println("Index 2 = "+(index2+index1));
			    String breed = subString.split("Name:")[0].trim();//typeSubString.substring(pet1Breedindex1,(pet1Breedindex2+pet1Breedindex1));
			    System.out.println(breed);
			    AR_PropertyWare.serviceAnimalPetBreed.add(breed);
			    int pet1Weightindex1 = RunnerClass.nthOccurrence(typeSubString, "Weight:", i+1)+"Weight:".length()+1;
			    String pet1WeightSubstring = typeSubString.substring(pet1Weightindex1);
			    //int pet1WeightIndex2 = RunnerClass.nthOccurrence(pet1WeightSubstring,"Age:",i+1);
			   // System.out.println("Index 2 = "+(index2+index1));
			    String weight = pet1WeightSubstring.split("Age:")[0].trim(); //typeSubString.substring(pet1Weightindex1,(pet1WeightIndex2+pet1Weightindex1));
			    System.out.println(weight);
			    AR_PropertyWare.serviceAnimalPetWeight.add(weight);
		    }
    		
    		
	    }
	    }
	    catch(Exception e)
	    {
	    	AR_PropertyWare.serviceAnimalFlag = false;
	    }
	    //Concession Addendum
	    
	    try
	    {
	    	if(text.contains(AR_PDFAppConfig.concessionAddendumText))
	    	{
	    		AR_PropertyWare.concessionAddendumFlag = true;
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
		 lateFeeRuleText = text.substring(text.indexOf(AR_PDFAppConfig.lateFeeRuleText_Prior)+AR_PDFAppConfig.lateFeeRuleText_Prior.length(),text.indexOf(AR_PDFAppConfig.lateFeeRuleText_After));
		}
		catch(Exception e)
		{
			try
			{
			lateFeeRuleText = text.substring(text.indexOf(AR_PDFAppConfig.lateFeeRuleText_Prior)+AR_PDFAppConfig.lateFeeRuleText_Prior.length(),text.indexOf(AR_PDFAppConfig.lateFeeRuleText_After2));
			}
			catch(Exception e2)
			{
			return false;
			}
		}
		if(lateFeeRuleText.contains(AR_PDFAppConfig.lateFeeRule_whicheverIsGreater))
		{
			AR_PropertyWare.lateFeeType ="GreaterOfFlatFeeOrPercentage"; 
		//Late charge day
			try
			{
		   // AR_PropertyWare.lateChargeDay =  lateFeeRuleText.substring(lateFeeRuleText.indexOf(AR_PDFAppConfig.lateFeeRule_whicheverIsGreater_dueDay_Prior)+AR_PDFAppConfig.lateFeeRule_whicheverIsGreater_dueDay_Prior.length()).trim().split(" ")[0];
				AR_PropertyWare.lateChargeDay =  lateFeeRuleText.split(AR_PDFAppConfig.lateFeeRule_whicheverIsGreater_dueDay_After)[0].trim();
				AR_PropertyWare.lateChargeDay =AR_PropertyWare.lateChargeDay.substring(AR_PropertyWare.lateChargeDay.lastIndexOf(" ")+1);
		    AR_PropertyWare.lateChargeDay =  AR_PropertyWare.lateChargeDay.replaceAll("[^0-9]", "");
			}
			catch(Exception e)
			{
				AR_PropertyWare.lateChargeDay = "Error";
			}
         System.out.println("Late Charge Day = "+AR_PropertyWare.lateChargeDay);
			
		//Late Fee Percentage
			try
			{
		    AR_PropertyWare.lateFeePercentage =  lateFeeRuleText.substring(lateFeeRuleText.indexOf(AR_PDFAppConfig.lateFeeRule_whicheverIsGreater_lateFeePercentage)+AR_PDFAppConfig.lateFeeRule_whicheverIsGreater_lateFeePercentage.length()).trim().split(" ")[0];
		    AR_PropertyWare.lateFeePercentage = AR_PropertyWare.lateFeePercentage.replaceAll("[^0-9]", "");
			}
			catch(Exception e)
			{
				AR_PropertyWare.lateFeePercentage = "Error";
			}
         System.out.println("Late Fee Percentage = "+AR_PropertyWare.lateFeePercentage);
         
         //Late Fee Amount
         try
         {
         String lateFeeAmount  = lateFeeRuleText.substring(lateFeeRuleText.indexOf(AR_PDFAppConfig.lateFeeRule_whicheverIsGreater_lateFeeAmount)+AR_PDFAppConfig.lateFeeRule_whicheverIsGreater_lateFeeAmount.length()).trim().split(" ")[0];
         AR_PropertyWare.flatFeeAmount = lateFeeAmount.replaceAll("[^.0-9]", "");
         }
         catch(Exception e)
         {
        	 AR_PropertyWare.flatFeeAmount ="Error";
         }
         System.out.println("Late Fee Amount = "+AR_PropertyWare.flatFeeAmount);
         
        
         return true;
		}
		else 
		if(lateFeeRuleText.contains(AR_PDFAppConfig.lateFeeRule_mayNotExceedMoreThan30Days))
		{
			AR_PropertyWare.lateFeeType ="initialFeePluPerDayFee"; 
	         try
	 	    {
	 		    AR_PropertyWare.lateChargeFee = text.substring(text.indexOf(AR_PDFAppConfig.AB_lateFee_Prior)+AR_PDFAppConfig.AB_lateFee_Prior.length()).trim().split(" ")[0];
	 		    //AR_PropertyWare.lateChargeFee =  AR_PropertyWare.lateChargeFee.substring(0,AR_PropertyWare.lateChargeFee.length()-1);
	 	    }
	 	    catch(Exception e)
	 	    {
	 		    AR_PropertyWare.lateChargeFee ="Error";	
	 		    e.printStackTrace();
	 	    }
	 	    System.out.println("Late Charge Fee = "+AR_PropertyWare.lateChargeFee.trim());
	 	    //Per Day Fee
	 	    try
	 	    {
	 	    	AR_PropertyWare.lateFeeChargePerDay = text.substring(text.indexOf(AR_PDFAppConfig.AB_additionalLateChargesPerDay_Prior)+AR_PDFAppConfig.AB_additionalLateChargesPerDay_Prior.length()).split(" ")[0].trim();//,text.indexOf(AR_PDFAppConfig.AB_additionalLateChargesPerDay_After));
	 	    }
	 	    catch(Exception e)
	 	    {
	 	    	AR_PropertyWare.lateFeeChargePerDay =  "Error";	
	 	    	e.printStackTrace();
	 	    }
	 	    System.out.println("Per Day Fee = "+AR_PropertyWare.lateFeeChargePerDay.trim());
	 	    //Additional Late Charges Limit
	 	    try
	 	    {
	 	    	AR_PropertyWare.additionalLateChargesLimit = text.substring(text.indexOf(AR_PDFAppConfig.AB_additionalLateChargesLimit_Prior)+AR_PDFAppConfig.AB_additionalLateChargesLimit_Prior.length()).trim().split(" ")[0]; //,text.indexOf(AR_PDFAppConfig.AB_additionalLateChargesLimit_After));
	 	    }
	 	    catch(Exception e)
	 	    {
	 	    	AR_PropertyWare.additionalLateChargesLimit =  "Error";	
	 	    	e.printStackTrace();
	 	    }
	 	    System.out.println("additional Late Charges Limit = "+AR_PropertyWare.additionalLateChargesLimit.trim());
	 	   return true;
		}
		else if(lateFeeRuleText.contains(AR_PDFAppConfig.lateFeeRule_mayNotExceedAmount))
			{
			//Late Charge Day
			try
	 	    {
			AR_PropertyWare.lateChargeDay = lateFeeRuleText.substring(lateFeeRuleText.indexOf("an initial late charge on the")+"an initial late charge on the".length()).trim().split(" ")[0];
			AR_PropertyWare.lateChargeDay = AR_PropertyWare.lateChargeDay.replaceAll("[^0-9]", "");
	 	    }
			catch(Exception e)
	 	    {
	 	    	AR_PropertyWare.lateChargeDay =  "Error";	
	 	    	e.printStackTrace();
	 	    }
	 	    System.out.println("Late Charge Due Day = "+AR_PropertyWare.lateChargeDay.trim());
	 	    // initial Late Charge
	 	   try
	 	    {
			AR_PropertyWare.lateChargeFee = lateFeeRuleText.substring(lateFeeRuleText.indexOf("day of the month equal to $")+"day of the month equal to $".length()).trim().split(" ")[0];
			AR_PropertyWare.lateChargeFee = AR_PropertyWare.lateChargeFee.replaceAll("[^0-9.]", "");
	 	    }
			catch(Exception e)
	 	    {
	 	    	AR_PropertyWare.lateChargeFee =  "Error";	
	 	    	e.printStackTrace();
	 	    }
	 	    System.out.println("Late Charge Fee = "+AR_PropertyWare.lateChargeFee.trim());
	 	    // Additional Late Charges
	 	   try
	 	    {
			AR_PropertyWare.additionalLateCharges = lateFeeRuleText.substring(lateFeeRuleText.indexOf("and additional late charge of $")+"and additional late charge of $".length()).trim().split(" ")[0];
			AR_PropertyWare.additionalLateCharges = AR_PropertyWare.additionalLateCharges.replaceAll("[^0-9.]", "");
	 	    }
			catch(Exception e)
	 	    {
	 	    	AR_PropertyWare.additionalLateCharges =  "Error";	
	 	    	e.printStackTrace();
	 	    }
	 	    System.out.println("Additional Late Charges = "+AR_PropertyWare.additionalLateCharges.trim());
	 	    //Additional Late Charges Limit
	 	   try
	 	    {
			AR_PropertyWare.additionalLateChargesLimit = lateFeeRuleText.substring(lateFeeRuleText.indexOf("(initial and additional) may not exceed $")+"(initial and additional) may not exceed $".length()).trim().split(" ")[0];
			AR_PropertyWare.additionalLateChargesLimit = AR_PropertyWare.additionalLateChargesLimit.replaceAll("[^0-9.]", "");
	 	    }
			catch(Exception e)
	 	    {
	 	    	AR_PropertyWare.additionalLateChargesLimit =  "Error";	
	 	    	e.printStackTrace();
	 	    }
	 	    System.out.println("Additional Late Charges Limit = "+AR_PropertyWare.additionalLateChargesLimit.trim());
			return true;
			}
		else 
			if(lateFeeRuleText.contains(AR_PDFAppConfig.lateFeeRule_totalDelinquentRentDueToTheTenantAccount))
			{
			//Late Charge Day
			try
	 	    {
			AR_PropertyWare.lateChargeDay = lateFeeRuleText.substring(lateFeeRuleText.indexOf("place of payment on the ")+"place of payment on the ".length()).trim().split(" ")[0];
			AR_PropertyWare.lateChargeDay = AR_PropertyWare.lateChargeDay.replaceAll("[^0-9]", "");
	 	    }
			catch(Exception e)
	 	    {
	 	    	AR_PropertyWare.lateChargeDay =  "Error";	
	 	    	e.printStackTrace();
	 	    }
	 	    System.out.println("Late Charge Due Day = "+AR_PropertyWare.lateChargeDay.trim());
	 	    // initial Late Charge
	 	   try
	 	    {
			AR_PropertyWare.lateChargeFee = lateFeeRuleText.substring(lateFeeRuleText.indexOf("an initial late charge equal to ")+"an initial late charge equal to ".length()).trim().split(" ")[0];
			//AR_PropertyWare.lateChargeFee = AR_PropertyWare.lateChargeFee.replaceAll("[^0-9.]", "");
	 	    }
			catch(Exception e)
	 	    {
	 	    	AR_PropertyWare.lateChargeFee =  "Error";	
	 	    	e.printStackTrace();
	 	    }
	 	    System.out.println("Late Charge Fee = "+AR_PropertyWare.lateChargeFee.trim());
	 	    // Additional Late Charges
	 	   try
	 	    {
			AR_PropertyWare.additionalLateCharges = lateFeeRuleText.substring(lateFeeRuleText.indexOf("and additional late charge of $")+"and additional late charge of $".length()).trim().split(" ")[0];
			AR_PropertyWare.additionalLateCharges = AR_PropertyWare.additionalLateCharges.replaceAll("[^0-9.]", "");
	 	    }
			catch(Exception e)
	 	    {
	 	    	AR_PropertyWare.additionalLateCharges =  "Error";	
	 	    	e.printStackTrace();
	 	    }
	 	    System.out.println("Additional Late Charges = "+AR_PropertyWare.additionalLateCharges.trim());
	 	    //Additional Late Charges Limit
	 	   try
	 	    {
			AR_PropertyWare.additionalLateChargesLimit = lateFeeRuleText.substring(lateFeeRuleText.indexOf("(initial and additional) may not exceed $")+"(initial and additional) may not exceed $".length()).trim().split(" ")[0];
			AR_PropertyWare.additionalLateChargesLimit = AR_PropertyWare.additionalLateChargesLimit.replaceAll("[^0-9.]", "");
	 	    }
			catch(Exception e)
	 	    {
	 	    	AR_PropertyWare.additionalLateChargesLimit =  "Error";	
	 	    	e.printStackTrace();
	 	    }
	 	    System.out.println("Additional Late Charges Limit = "+AR_PropertyWare.additionalLateChargesLimit.trim());
			return true;
			}
			else
		   {
			AR_PropertyWare.lateFeeType ="";
			return false;
		   }
		
	}


}
