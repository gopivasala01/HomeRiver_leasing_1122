package Tennessee;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import mainPackage.InsertDataIntoDatabase;
import mainPackage.RunnerClass;

public class TN_ExtractDataFromPDF 
{
	public static boolean petFlag;
	public static String text="";
	public boolean arizona() throws Exception
	//public static void main(String args[]) throws Exception
	{
		
		TN_PropertyWare.petFlag = false;
		File file = RunnerClass.getLastModified();
		//File file = new File("C:\\Gopi\\Projects\\Property ware\\Lease Close Outs\\PDFS\\Tennessee\\Pet agreement\\Lease_0722_0224_3305_Richland_View_Ln_TN_Gap.pdf");
		FileInputStream fis = new FileInputStream(file);
		TN_RunnerClass.document = PDDocument.load(fis);
	    text = new PDFTextStripper().getText(TN_RunnerClass.document);
	    TN_PropertyWare.pdfText  = text;
	    if(!text.contains(TN_AppConfig.PDFFormatConfirmationText)) 
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
	    	TN_PropertyWare.commensementDate = text.substring(text.indexOf(TN_PDFAppConfig.AB_commencementDate_Prior)+TN_PDFAppConfig.AB_commencementDate_Prior.length(),text.indexOf(TN_PDFAppConfig.AB_expirationDate_Prior));
	    	TN_PropertyWare.commensementDate = TN_PropertyWare.commensementDate.trim().replaceAll(" +", " ");
	    }
	    catch(Exception e)
	    {
	    	TN_PropertyWare.commensementDate = "Error";
	    	e.printStackTrace();
	    }
	    System.out.println("Commensement Date = "+TN_PropertyWare.commensementDate);
	   try
	    {
		   String expirationDateWaw = text.substring(text.indexOf(TN_PDFAppConfig.AB_expirationDate_Prior)+TN_PDFAppConfig.AB_expirationDate_Prior.length());
		   TN_PropertyWare.expirationDate =expirationDateWaw.trim().split(" ")[0]+" "+expirationDateWaw.trim().split(" ")[1]+" "+expirationDateWaw.trim().split(" ")[2];
		   TN_PropertyWare.expirationDate = TN_PropertyWare.expirationDate.trim().replaceAll(" +", " ");
	    }
	    catch(Exception e)
	    {
	    	 TN_PropertyWare.expirationDate = "Error";
	    	 e.printStackTrace();
	    }
	   System.out.println("Expiration Date = "+TN_PropertyWare.expirationDate);
	   try
	    {
		    TN_PropertyWare.proratedRent = text.substring(text.indexOf(TN_PDFAppConfig.AB_proratedRent_Prior)+TN_PDFAppConfig.AB_proratedRent_Prior.length(),text.indexOf(TN_PDFAppConfig.AB_proratedRent_After));
		    if(TN_PropertyWare.proratedRent.matches(".*[a-zA-Z]+.*"))
		    {
		    	TN_PropertyWare.proratedRent = "Error";
		    }
		    		
	    }
	    catch(Exception e)
	    {
	    	TN_PropertyWare.proratedRent = "Error";
	    	e.printStackTrace();
	    }
	   System.out.println("Prorated Rent = "+TN_PropertyWare.proratedRent);
	    try
	    {
		    TN_PropertyWare.proratedRentDate = text.substring(text.indexOf(TN_PDFAppConfig.AB_proratedRentDate_Prior)+TN_PDFAppConfig.AB_proratedRentDate_Prior.length(),text.indexOf(TN_PDFAppConfig.AB_proratedRentDate_After)).trim();
	    }
	    catch(Exception e)
	    {
	    	TN_PropertyWare.proratedRentDate = "Error";
	    	e.printStackTrace();
	    }
	    System.out.println("Prorated Rent Date= "+TN_PropertyWare.proratedRentDate.trim());
	    /*
	    try
	    {
		    TN_PropertyWare.monthlyRentDate = text.substring(text.indexOf(TN_PDFAppConfig.AB_fullRentDate_Prior)+TN_PDFAppConfig.AB_fullRentDate_Prior.length(),text.indexOf(TN_PDFAppConfig.AB_fullRentDate_After));
		    System.out.println("Monthly Rent Date= "+TN_PropertyWare.monthlyRentDate.trim());
	    }
	    catch(Exception e)
	    {
	    	try
	    	{
	    		TN_PropertyWare.monthlyRentDate = text.substring(text.indexOf(TN_PDFAppConfig.AB_fullRentDate_Prior)+TN_PDFAppConfig.AB_fullRentDate_Prior.length(),text.indexOf(TN_PDFAppConfig.AB_fullRentDate1_After));
			   	System.out.println("Monthly Rent Date= "+TN_PropertyWare.monthlyRentDate.trim());
	    	}
	    	catch(Exception e1)
		    {
		    	TN_PropertyWare.monthlyRentDate = "Error";  
		    	e1.printStackTrace();
		    }
	    }*/
	    try
	    {
		    TN_PropertyWare.monthlyRent = text.substring(text.indexOf(TN_PDFAppConfig.AB_fullRent_Prior)+TN_PDFAppConfig.AB_fullRent_Prior.length()).trim().split(" ")[0].trim();//,text.indexOf(TN_PDFAppConfig.AB_fullRent_After)).substring(1).replaceAll("[^.0-9]", "");;
		    if(RunnerClass.onlyDigits(TN_PropertyWare.monthlyRent.replace(".", "").replace(",", ""))==false)
		    {
		    	TN_PropertyWare.monthlyRent = text.substring(text.indexOf(TN_PDFAppConfig.AB_fullRent2_Prior)+TN_PDFAppConfig.AB_fullRent2_Prior.length()).trim().split(" ")[0].trim();
		    }
		    if(TN_PropertyWare.monthlyRent.contains("*"))
		    {
		    	TN_PropertyWare.monthlyRent = TN_PropertyWare.monthlyRent.replace("*","");
		    }
		    if(TN_PropertyWare.monthlyRent.matches(".*[a-zA-Z]+.*"))
		    {
		    	TN_PropertyWare.monthlyRent = "Error";
		    }
		    if(TN_PropertyWare.monthlyRent.endsWith(","))
		    {
		    	TN_PropertyWare.monthlyRent = TN_PropertyWare.monthlyRent.substring(0,TN_PropertyWare.monthlyRent.length()-1);
		    }
	    }
	    catch(Exception e)
	    {
	    	 TN_PropertyWare.monthlyRent = "Error";
	    	 e.printStackTrace();
	    }
	    System.out.println("Monthly Rent "+TN_PropertyWare.monthlyRent.trim());
	    try
	    {
		    TN_PropertyWare.adminFee = text.substring(text.indexOf(TN_PDFAppConfig.AB_adminFee_Prior)+TN_PDFAppConfig.AB_adminFee_Prior.length()).trim().split(" ")[0];
		    if(TN_PropertyWare.adminFee.matches(".*[a-zA-Z]+.*"))
		    {
		    	TN_PropertyWare.adminFee = "Error";
		    }
	    }
	    catch(Exception e)
	    {
		    TN_PropertyWare.adminFee = "Error";
		    e.printStackTrace();
	    }
	    System.out.println("Admin Fee = "+TN_PropertyWare.adminFee.trim());
	    
	  //Resident Benefits Package 
	    if(text.contains(TN_PDFAppConfig.residentBenefitsPackageAddendumCheck))
	    {
	    	TN_PropertyWare.residentBenefitsPackageAvailabilityCheck = true;
	    	 try
	 	    {
	 		    TN_PropertyWare.residentBenefitsPackage = text.substring(text.indexOf(TN_PDFAppConfig.AB1_residentBenefitsPackage_Prior)+TN_PDFAppConfig.AB1_residentBenefitsPackage_Prior.length()).split(" ")[0].replaceAll("[^0-9a-zA-Z.]", "");
	 		    if(TN_PropertyWare.residentBenefitsPackage.matches(".*[a-zA-Z]+.*"))
	 		    {
	 		    	TN_PropertyWare.residentBenefitsPackage = "Error";
	 		    }
	 	    }
	 	    catch(Exception e)
	 	    {
	 		    TN_PropertyWare.residentBenefitsPackage = "Error";
	 		    e.printStackTrace();
	 	    }
	    	 System.out.println("Resident Benefits Package  = "+TN_PropertyWare.residentBenefitsPackage.trim());
	    	//TN_PDFAppConfig.AB1_residentBenefitsPackage_Prior
	    }
	    else
	    {
		    if(text.contains(TN_PDFAppConfig_Format2.HVACFilterAddendumTextAvailabilityCheck)==true)
		    {
		    	TN_PropertyWare.HVACFilterFlag =true;
		    }
		    else
		    {
		    try
		    {
			   String[] airFilterFeeArray = text.substring(text.indexOf(TN_PDFAppConfig.AB_airFilterFee_Prior)+TN_PDFAppConfig.AB_airFilterFee_Prior.length()).split(" ");
			   TN_PropertyWare.airFilterFee = airFilterFeeArray[0];
			   if(TN_PropertyWare.airFilterFee.matches(".*[a-zA-Z]+.*"))
			    {
			    	TN_PropertyWare.airFilterFee = "Error";
			    }
		    }
		    catch(Exception e)
		    {
		    TN_PropertyWare.airFilterFee = "Error";
		    e.printStackTrace();
		    }
		    }
		    System.out.println("Air Filter Fee = "+TN_PropertyWare.airFilterFee.trim());
	    }
	    try
	    {
	    	String[] earlyTerminationRaw = text.substring(text.indexOf(TN_PDFAppConfig.AB_earlyTerminationFee_Prior)+TN_PDFAppConfig.AB_earlyTerminationFee_Prior.length()).split(" ");
	    	
		    TN_PropertyWare.earlyTermination = earlyTerminationRaw[0]+earlyTerminationRaw[1]; //text.substring(text.indexOf(TN_PDFAppConfig.AB_earlyTerminationFee_Prior)+TN_PDFAppConfig.AB_earlyTerminationFee_Prior.length(),text.indexOf(TN_PDFAppConfig.AB_earlyTerminationFee_After));
	    }
	    catch(Exception e)
	    {
	    	TN_PropertyWare.earlyTermination = "Error";	
	    	e.printStackTrace();
	    }
	    System.out.println("Early Termination  = "+TN_PropertyWare.earlyTermination.trim());
	    try
	    {
	    	
		    TN_PropertyWare.occupants = text.substring(text.indexOf(TN_PDFAppConfig.AB_occupants_Prior)+TN_PDFAppConfig.AB_occupants_Prior.length(),text.indexOf(TN_PDFAppConfig.AB_occupants_After));
	    }
	    catch(Exception e)
	    {
		    TN_PropertyWare.occupants ="Error";	
		    e.printStackTrace();
	    }
	    System.out.println("Occupants = "+TN_PropertyWare.occupants.trim());
	    
	    //Late charges 
	    //Decide Late Fee Rule
	   TN_ExtractDataFromPDF.lateFeeRule();
	    
	  //Prepayment Charge
  		if(TN_PropertyWare.portfolioType.contains("MCH"))
  		{
  			if(TN_PropertyWare.proratedRent.equalsIgnoreCase("n/a")||TN_PropertyWare.proratedRent.equalsIgnoreCase("Error")||TN_PropertyWare.proratedRent.equalsIgnoreCase(""))
  			{
  				TN_PropertyWare.prepaymentCharge = "Error";
  			}
  			else
  			{
	  		try
	  		{
	  		TN_PropertyWare.prepaymentCharge =String.valueOf(Double.parseDouble(TN_PropertyWare.monthlyRent.trim().replace(",", "")) - Double.parseDouble(TN_PropertyWare.proratedRent.trim().replace(",", ""))); 
	  		}
	  		catch(Exception e)
	  		{
	  			TN_PropertyWare.prepaymentCharge ="Error";
	  		}
	  		}
  			System.out.println("Prepayment Charge = "+TN_PropertyWare.prepaymentCharge);
  		 }
	    petFlag = text.contains(TN_PDFAppConfig.AB_petAgreementAvailabilityCheck);
	    System.out.println("Pet Addendum Available = "+petFlag);
	    if(petFlag ==true)
	    {
	    	TN_PropertyWare.petFlag = true;
	    	try
	    	{
	    	TN_PropertyWare.petSecurityDeposit = text.substring(text.indexOf(TN_PDFAppConfig.AB_securityDeposity_Prior)+TN_PDFAppConfig.AB_securityDeposity_Prior.length(),text.indexOf(TN_PDFAppConfig.AB_securityDeposity_After));
	    	if(TN_PropertyWare.petSecurityDeposit.matches(".*[a-zA-Z]+.*"))
		    {
		    	TN_PropertyWare.petSecurityDeposit = "Error";
		    }
	    	}
	    	catch(Exception e)
	    	{
	    	TN_PropertyWare.petSecurityDeposit = "Error";	
	    	e.printStackTrace();
	    	}
	    	System.out.println("Pet Security Deposit = "+TN_PropertyWare.petSecurityDeposit.trim());
	    	if(RunnerClass.onlyDigits(TN_PropertyWare.petSecurityDeposit.replace(".", ""))==true)
		    {
		    	System.out.println("Security Deposit is checked");
		    }
	    	//TODO Check
	    	  try
			    {
	    		  String proratedPetRaw = "Prorated Pet Rent: On or before "+TN_PropertyWare.commensementDate.trim()+" Tenant will pay Landlord $";
	    		TN_PropertyWare.proratedPetRent = text.substring(text.indexOf(proratedPetRaw)+proratedPetRaw.length()).trim().split(" ")[0];//.replaceAll("[a-ZA-Z,]", "");
			    //AR_PropertyWare.proratedPetRent = proratedPetRentRaw.substring(proratedPetRentRaw.indexOf("Tenant will pay Landlord $")+"Tenant will pay Landlord $".length());//,proratedPetRentRaw.indexOf(AppConfig.AR_proratedPetRent_After));
			    if(TN_PropertyWare.proratedPetRent.matches(".*[a-zA-Z]+.*"))
			    {
			    	TN_PropertyWare.proratedPetRent = "Error";
			    }
			    }
			    catch(Exception e)
			    {
			   
			    TN_PropertyWare.proratedPetRent = "Error";	
			    e.printStackTrace();
			    }
	    	  System.out.println("Prorated Pet Rent = "+TN_PropertyWare.proratedPetRent.trim());
	    	
	    	try
		    {
	    		 TN_PropertyWare.petRent = text.substring(text.indexOf(TN_PDFAppConfig.AB_petRent_Prior)+TN_PDFAppConfig.AB_petRent_Prior.length()).trim().split(" ")[0];
	    		 if(TN_PropertyWare.petRent.contains(",for"))
	    		 {
	    			 TN_PropertyWare.petRent = TN_PropertyWare.petRent.split(",")[0].trim();
	    		 }
	    		 else
	    		 {
		    		 if(TN_PropertyWare.petRent.matches(".*[a-zA-Z]+.*")==true)
		    			 TN_PropertyWare.petRent = text.substring(text.indexOf(TN_PDFAppConfig.AB_petRent1_Prior)+TN_PDFAppConfig.AB_petRent1_Prior.length()).trim().split(" ")[0];
		    		 else 
		    		 TN_PropertyWare.petRent = RunnerClass.extractNumber(TN_PropertyWare.petRent);
	    		 }
		    }
	    	catch(Exception e)
		    {
	    		try
	    		{
	    			e.printStackTrace();
	    			TN_PropertyWare.petRent = text.substring(text.indexOf(TN_PDFAppConfig.AB_petRent1_Prior)+TN_PDFAppConfig.AB_petRent1_Prior.length()).trim().split(" ")[0];
//					 System.out.println("Pet rent = "+TN_PropertyWare.petRent.trim());
	    			if(TN_PropertyWare.petRent.matches(".*[a-zA-Z]+.*"))
	    		    {
	    		    	TN_PropertyWare.petRent = "Error";
	    		    }
	    		}
	    		
	    		catch(Exception e1)
			    {
			    	TN_PropertyWare.petRent = "Error";  
			    	e1.printStackTrace();
			    }
		    }
	    	System.out.println("Pet rent = "+TN_PropertyWare.petRent.trim());
		    	//TN_PropertyWare.petRent = "Error";  
		    	//e.printStackTrace();
		   /* 
	    	try
    		{
    			//String petFeeRaw1 = text.substring(text.indexOf(TN_PDFAppConfig.AB_petFee_Prior));
    			TN_PropertyWare.petFee = text.substring(text.indexOf(TN_PDFAppConfig.AB_petFee_Prior)+TN_PDFAppConfig.AB_petFee_Prior.length()).trim().split(" ")[0].trim();
    			//TN_PropertyWare.petFee =  petFeeRaw[petFeeRaw.length-2].trim();
    			//if(TN_PropertyWare.petFee.matches(".*[a-zA-Z]+.*"))
    			//{
    				//TN_PropertyWare.petFee = text.substring(text.indexOf(TN_PDFAppConfig.AB_petFee2_Prior)+TN_PDFAppConfig.AB_petFee2_Prior.length()).trim().split(" ")[0].trim();
    			//}
    			//System.out.println(petFeeRaw.length);
    		}
    		
    		catch(Exception e1)
		    {
		    	TN_PropertyWare.petFee = "Error";  
		    	e1.printStackTrace();
		    }
	    	System.out.println("Pet Fee = "+TN_PropertyWare.petFee);
	    	*/
	    	// Get text between Type: word
	    	
	    	String typeSubString = text.substring(text.indexOf(TN_PDFAppConfig.AB_typeWord_Prior)+TN_PDFAppConfig.AB_typeWord_Prior.length(),text.indexOf(TN_PDFAppConfig.AB_typeWord_After));
	    	
	    	String newText = typeSubString.replace("Type:","");
		    TN_PropertyWare.countOfTypeWordInText = ((typeSubString.length() - newText.length())/"Type:".length());
		    System.out.println("Type: occurences = "+TN_PropertyWare.countOfTypeWordInText);
		    
		    TN_PropertyWare.petType = new ArrayList();
		    TN_PropertyWare.petBreed = new ArrayList();
		    TN_PropertyWare.petWeight = new ArrayList();
		    for(int i =0;i<TN_PropertyWare.countOfTypeWordInText;i++)
		    {
		    	String type = typeSubString.substring(RunnerClass.nthOccurrence(typeSubString, "Type:", i+1)+TN_PDFAppConfig.AB_pet1Type_Prior.length(),RunnerClass.nthOccurrence(typeSubString, "Breed:", i+1)).trim();
		    	if(type.contains("N/A")||type.contains("n/a"))
		    		break;
		    	System.out.println(type);
		    	TN_PropertyWare.petType.add(type);
		    	int pet1Breedindex1 = RunnerClass.nthOccurrence(typeSubString, "Breed:", i+1)+"Breed:".length()+1;
			    String subString = typeSubString.substring(pet1Breedindex1);
			    //int pet1Breedindex2 = RunnerClass.nthOccurrence(subString,"Name:",i+1);
			   // System.out.println("Index 2 = "+(index2+index1));
			    String breed = subString.split("Name:")[0].trim();//typeSubString.substring(pet1Breedindex1,(pet1Breedindex2+pet1Breedindex1));
			    System.out.println(breed);
			    TN_PropertyWare.petBreed.add(breed);
			    int pet1Weightindex1 = RunnerClass.nthOccurrence(typeSubString, "Weight:", i+1)+"Weight:".length()+1;
			    String pet1WeightSubstring = typeSubString.substring(pet1Weightindex1);
			    //int pet1WeightIndex2 = RunnerClass.nthOccurrence(pet1WeightSubstring,"Age:",i+1);
			   // System.out.println("Index 2 = "+(index2+index1));
			    String weight = pet1WeightSubstring.split("Age:")[0].trim(); //typeSubString.substring(pet1Weightindex1,(pet1WeightIndex2+pet1Weightindex1));
			    System.out.println(weight);
			    TN_PropertyWare.petWeight.add(weight);
		    }
		    
		    
		    
		    try
		    {
		    	TN_PropertyWare.petOneTimeNonRefundableFee = text.substring(text.indexOf(TN_PDFAppConfig.AB_petFeeOneTime_Prior)+TN_PDFAppConfig.AB_petFeeOneTime_Prior.length()).split(" ")[0];//,text.indexOf(TN_PDFAppConfig.AB_petFeeOneTime_After));
		    	if(TN_PropertyWare.petOneTimeNonRefundableFee.matches(".*[a-zA-Z]+.*"))
    		    {
    		    	TN_PropertyWare.petOneTimeNonRefundableFee = "Error";
    		    }
		    }
		    catch(Exception e)
		    {
		    	TN_PropertyWare.petOneTimeNonRefundableFee =  "Error";
		    	e.printStackTrace();
		    }  
		    System.out.println("pet one time non refundable = "+TN_PropertyWare.petOneTimeNonRefundableFee.trim());
		   
	    }
	    
	    //Service Animal Addendum check
	    try
	    {
	    if(text.contains(TN_AppConfig.serviceAnimalText))
	    {
	    	TN_PropertyWare.serviceAnimalFlag = true;
    		System.out.println("Service Animal Addendum is available");
    		
            String typeSubString = text.substring(text.indexOf(TN_PDFAppConfig.AB_serviceAnimal_typeWord_Prior)+TN_PDFAppConfig.AB_serviceAnimal_typeWord_Prior.length(),text.indexOf(TN_PDFAppConfig.AB_serviceAnimal_typeWord_After));
	    	
	    	String newText = typeSubString.replace("Type:","");
		    int  countOftypeWords_ServiceAnimal = ((typeSubString.length() - newText.length())/"Type:".length());
		    System.out.println("Service Animal - Type: occurences = "+countOftypeWords_ServiceAnimal);
		    
		    TN_PropertyWare.serviceAnimalPetType = new ArrayList();
		    TN_PropertyWare.serviceAnimalPetBreed = new ArrayList();
		    TN_PropertyWare.serviceAnimalPetWeight = new ArrayList();
		    for(int i =0;i<countOftypeWords_ServiceAnimal;i++)
		    {
		    	String type = typeSubString.substring(RunnerClass.nthOccurrence(typeSubString, "Type:", i+1)+TN_PDFAppConfig.AB_pet1Type_Prior.length(),RunnerClass.nthOccurrence(typeSubString, "Breed:", i+1)).trim();
		    	if(type.contains("N/A")||type.contains("n/a"))
		    		break;
		    	System.out.println(type);
		    	TN_PropertyWare.serviceAnimalPetType.add(type);
		    	int pet1Breedindex1 = RunnerClass.nthOccurrence(typeSubString, "Breed:", i+1)+"Breed:".length()+1;
			    String subString = typeSubString.substring(pet1Breedindex1);
			    //int pet1Breedindex2 = RunnerClass.nthOccurrence(subString,"Name:",i+1);
			   // System.out.println("Index 2 = "+(index2+index1));
			    String breed = subString.split("Name:")[0].trim();//typeSubString.substring(pet1Breedindex1,(pet1Breedindex2+pet1Breedindex1));
			    System.out.println(breed);
			    TN_PropertyWare.serviceAnimalPetBreed.add(breed);
			    int pet1Weightindex1 = RunnerClass.nthOccurrence(typeSubString, "Weight:", i+1)+"Weight:".length()+1;
			    String pet1WeightSubstring = typeSubString.substring(pet1Weightindex1);
			    //int pet1WeightIndex2 = RunnerClass.nthOccurrence(pet1WeightSubstring,"Age:",i+1);
			   // System.out.println("Index 2 = "+(index2+index1));
			    String weight = pet1WeightSubstring.split("Age:")[0].trim(); //typeSubString.substring(pet1Weightindex1,(pet1WeightIndex2+pet1Weightindex1));
			    System.out.println(weight);
			    TN_PropertyWare.serviceAnimalPetWeight.add(weight);
		    }
    		
    		
	    }
	    }
	    catch(Exception e)
	    {
	    	TN_PropertyWare.serviceAnimalFlag = false;
	    }
	    //Concession Addendum
	    
	    try
	    {
	    	if(text.contains(TN_PDFAppConfig.concessionAddendumText))
	    	{
	    		TN_PropertyWare.concessionAddendumFlag = true;
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
		 lateFeeRuleText = text.substring(text.indexOf(TN_PDFAppConfig.lateFeeRuleText_Prior)+TN_PDFAppConfig.lateFeeRuleText_Prior.length(),text.indexOf(TN_PDFAppConfig.lateFeeRuleText_After));
		}
		catch(Exception e)
		{
			try
			{
			lateFeeRuleText = text.substring(text.indexOf(TN_PDFAppConfig.lateFeeRuleText_Prior)+TN_PDFAppConfig.lateFeeRuleText_Prior.length(),text.indexOf(TN_PDFAppConfig.lateFeeRuleText_After2));
			}
			catch(Exception e2)
			{
			return false;
			}
		}
		if(lateFeeRuleText.contains(TN_PDFAppConfig.lateFeeRule_whicheverIsGreater))
		{
			RunnerClass.lateFeeRuleType = "GreaterOfFlatFeeOrPercentage";
			RunnerClass.lateFeeType = "GreaterOfFlatFeeOrPercentage";
			//TN_PropertyWare.lateFeeType ="Greater of Flat Fee or Percentage"; 
		//Late charge day
			try
			{
		   // TN_PropertyWare.lateChargeDay =  lateFeeRuleText.substring(lateFeeRuleText.indexOf(TN_PDFAppConfig.lateFeeRule_whicheverIsGreater_dueDay_Prior)+TN_PDFAppConfig.lateFeeRule_whicheverIsGreater_dueDay_Prior.length()).trim().split(" ")[0];
				TN_PropertyWare.lateChargeDay =  lateFeeRuleText.split(TN_PDFAppConfig.lateFeeRule_whicheverIsGreater_dueDay_After)[0].trim();
				TN_PropertyWare.lateChargeDay =TN_PropertyWare.lateChargeDay.substring(TN_PropertyWare.lateChargeDay.lastIndexOf(" ")+1);
		    TN_PropertyWare.lateChargeDay =  TN_PropertyWare.lateChargeDay.replaceAll("[^0-9]", "");
			}
			catch(Exception e)
			{
				TN_PropertyWare.lateChargeDay = "Error";
			}
         System.out.println("Late Charge Day = "+TN_PropertyWare.lateChargeDay);
			RunnerClass.dueDay_GreaterOf = TN_PropertyWare.lateChargeDay;
		//Late Fee Percentage
			try
			{
		    TN_PropertyWare.lateFeePercentage =  lateFeeRuleText.substring(lateFeeRuleText.indexOf(TN_PDFAppConfig.lateFeeRule_whicheverIsGreater_lateFeePercentage)+TN_PDFAppConfig.lateFeeRule_whicheverIsGreater_lateFeePercentage.length()).trim().split(" ")[0];
		    TN_PropertyWare.lateFeePercentage = TN_PropertyWare.lateFeePercentage.replaceAll("[^0-9]", "");
			}
			catch(Exception e)
			{
				TN_PropertyWare.lateFeePercentage = "Error";
			}
         System.out.println("Late Fee Percentage = "+TN_PropertyWare.lateFeePercentage);
         RunnerClass.percentage = TN_PropertyWare.lateFeePercentage;
         //Late Fee Amount
         try
         {
         String lateFeeAmount  = lateFeeRuleText.substring(lateFeeRuleText.indexOf(TN_PDFAppConfig.lateFeeRule_whicheverIsGreater_lateFeeAmount)+TN_PDFAppConfig.lateFeeRule_whicheverIsGreater_lateFeeAmount.length()).trim().split(" ")[0];
         TN_PropertyWare.flatFeeAmount = lateFeeAmount.replaceAll("[^.0-9]", "");
         }
         catch(Exception e)
         {
        	 TN_PropertyWare.flatFeeAmount ="Error";
         }
         System.out.println("Late Fee Amount = "+TN_PropertyWare.flatFeeAmount);
        RunnerClass.flatFee = TN_PropertyWare.flatFeeAmount;
         return true;
		}
		else 
		if(lateFeeRuleText.contains(TN_PDFAppConfig.lateFeeRule_mayNotExceedMoreThan30Days))
		{
			RunnerClass.lateFeeRuleType = "initialFeePluPerDayFee";
			//RunnerClass.lateFeeRuleType = "Initial Fee + Per Day Fee";
			
			TN_PropertyWare.lateFeeType ="initialFeePluPerDayFee"; 
	         try
	 	    {
	 		    TN_PropertyWare.lateChargeFee = text.substring(text.indexOf(TN_PDFAppConfig.AB_lateFee_Prior)+TN_PDFAppConfig.AB_lateFee_Prior.length()).trim().split(" ")[0];
	 		    //TN_PropertyWare.lateChargeFee =  TN_PropertyWare.lateChargeFee.substring(0,TN_PropertyWare.lateChargeFee.length()-1);
	 	    }
	 	    catch(Exception e)
	 	    {
	 		    TN_PropertyWare.lateChargeFee ="Error";	
	 		    e.printStackTrace();
	 	    }
	 	    System.out.println("Late Charge Fee = "+TN_PropertyWare.lateChargeFee.trim());
	 	   RunnerClass.initialFeeAmount = TN_PropertyWare.lateChargeFee;
	 	    //Per Day Fee
	 	    try
	 	    {
	 	    	TN_PropertyWare.lateFeeChargePerDay = text.substring(text.indexOf(TN_PDFAppConfig.AB_additionalLateChargesPerDay_Prior)+TN_PDFAppConfig.AB_additionalLateChargesPerDay_Prior.length()).split(" ")[0].trim();//,text.indexOf(TN_PDFAppConfig.AB_additionalLateChargesPerDay_After));
	 	    }
	 	    catch(Exception e)
	 	    {
	 	    	TN_PropertyWare.lateFeeChargePerDay =  "Error";	
	 	    	e.printStackTrace();
	 	    }
	 	    System.out.println("Per Day Fee = "+TN_PropertyWare.lateFeeChargePerDay.trim());
	 	    RunnerClass.perDayFeeAmount = TN_PropertyWare.lateFeeChargePerDay;
	 	    //Additional Late Charges Limit
	 	    try
	 	    {
	 	    	TN_PropertyWare.additionalLateChargesLimit = text.substring(text.indexOf(TN_PDFAppConfig.AB_additionalLateChargesLimit_Prior)+TN_PDFAppConfig.AB_additionalLateChargesLimit_Prior.length()).trim().split(" ")[0]; //,text.indexOf(TN_PDFAppConfig.AB_additionalLateChargesLimit_After));
	 	    }
	 	    catch(Exception e)
	 	    {
	 	    	TN_PropertyWare.additionalLateChargesLimit =  "Error";	
	 	    	e.printStackTrace();
	 	    }
	 	    System.out.println("additional Late Charges Limit = "+TN_PropertyWare.additionalLateChargesLimit.trim());
	 	    RunnerClass.additionalLateChargesLimit = TN_PropertyWare.additionalLateChargesLimit;
	 	 //Late Charge Day
			try
	 	    {
			TN_PropertyWare.lateChargeDay = lateFeeRuleText.substring(lateFeeRuleText.indexOf("p.m. on the ")+"p.m. on the ".length()).trim().split(" ")[0];
			TN_PropertyWare.lateChargeDay = TN_PropertyWare.lateChargeDay.replaceAll("[^0-9]", "");
	 	    }
			catch(Exception e)
	 	    {
	 	    	TN_PropertyWare.lateChargeDay =  "Error";	
	 	    	e.printStackTrace();
	 	    }
	 	    System.out.println("Late Charge Due Day = "+TN_PropertyWare.lateChargeDay.trim());
	 	    RunnerClass.dueDay_initialFee = TN_PropertyWare.lateChargeDay;
	 	   return true;
		}
		else if(lateFeeRuleText.contains(TN_PDFAppConfig.lateFeeRule_mayNotExceedAmount))
			{
			RunnerClass.lateFeeRuleType = "initialFeePluPerDayFee";
			RunnerClass.lateFeeRuleType = "Initial Fee + Per Day Fee";
			
			//Late Charge Day
			try
	 	    {
			TN_PropertyWare.lateChargeDay = lateFeeRuleText.substring(lateFeeRuleText.indexOf("an initial late charge on the")+"an initial late charge on the".length()).trim().split(" ")[0];
			TN_PropertyWare.lateChargeDay = TN_PropertyWare.lateChargeDay.replaceAll("[^0-9]", "");
	 	    }
			catch(Exception e)
	 	    {
	 	    	TN_PropertyWare.lateChargeDay =  "Error";	
	 	    	e.printStackTrace();
	 	    }
	 	    System.out.println("Late Charge Due Day = "+TN_PropertyWare.lateChargeDay.trim());
	 	   RunnerClass.dueDay_initialFee = TN_PropertyWare.lateChargeDay;
	 	    // initial Late Charge
	 	   try
	 	    {
			TN_PropertyWare.lateChargeFee = lateFeeRuleText.substring(lateFeeRuleText.indexOf("day of the month equal to $")+"day of the month equal to $".length()).trim().split(" ")[0];
			TN_PropertyWare.lateChargeFee = TN_PropertyWare.lateChargeFee.replaceAll("[^0-9.]", "");
	 	    }
			catch(Exception e)
	 	    {
	 	    	TN_PropertyWare.lateChargeFee =  "Error";	
	 	    	e.printStackTrace();
	 	    }
	 	    System.out.println("Late Charge Fee = "+TN_PropertyWare.lateChargeFee.trim());
	 	   RunnerClass.initialFeeAmount = TN_PropertyWare.lateChargeFee;
	 	    // Additional Late Charges
	 	   try
	 	    {
			TN_PropertyWare.additionalLateCharges = lateFeeRuleText.substring(lateFeeRuleText.indexOf("and additional late charge of $")+"and additional late charge of $".length()).trim().split(" ")[0];
			TN_PropertyWare.additionalLateCharges = TN_PropertyWare.additionalLateCharges.replaceAll("[^0-9.]", "");
	 	    }
			catch(Exception e)
	 	    {
	 	    	TN_PropertyWare.additionalLateCharges =  "Error";	
	 	    	e.printStackTrace();
	 	    }
	 	    System.out.println("Additional Late Charges = "+TN_PropertyWare.additionalLateCharges.trim());
	 	   RunnerClass.maximumAmount = TN_PropertyWare.additionalLateCharges;
	 	    //Additional Late Charges Limit
	 	   try
	 	    {
			TN_PropertyWare.additionalLateChargesLimit = lateFeeRuleText.substring(lateFeeRuleText.indexOf("(initial and additional) may not exceed $")+"(initial and additional) may not exceed $".length()).trim().split(" ")[0];
			TN_PropertyWare.additionalLateChargesLimit = TN_PropertyWare.additionalLateChargesLimit.replaceAll("[^0-9.]", "");
	 	    }
			catch(Exception e)
	 	    {
	 	    	TN_PropertyWare.additionalLateChargesLimit =  "Error";	
	 	    	e.printStackTrace();
	 	    }
	 	    System.out.println("Additional Late Charges Limit = "+TN_PropertyWare.additionalLateChargesLimit.trim());
	 	   RunnerClass.additionalLateChargesLimit = TN_PropertyWare.additionalLateChargesLimit;
			return true;
			}
		else 
			if(lateFeeRuleText.contains(TN_PDFAppConfig.lateFeeRule_totalDelinquentRentDueToTheTenantAccount))
			{
				RunnerClass.lateFeeRuleType = "GreaterOfFlatFeeOrPercentage";
				RunnerClass.lateFeeType = "GreaterOfFlatFeeOrPercentage";
				
			//Late Charge Day
			try
	 	    {
			TN_PropertyWare.lateChargeDay = lateFeeRuleText.substring(lateFeeRuleText.indexOf("place of payment on the ")+"place of payment on the ".length()).trim().split(" ")[0];
			TN_PropertyWare.lateChargeDay = TN_PropertyWare.lateChargeDay.replaceAll("[^0-9]", "");
	 	    }
			catch(Exception e)
	 	    {
	 	    	TN_PropertyWare.lateChargeDay =  "Error";	
	 	    	e.printStackTrace();
	 	    }
	 	    System.out.println("Late Charge Due Day = "+TN_PropertyWare.lateChargeDay.trim());
	 	   RunnerClass.dueDay_GreaterOf = TN_PropertyWare.lateChargeDay;
	 	    // initial Late Charge
	 	   try
	 	    {
			TN_PropertyWare.lateChargeFee = lateFeeRuleText.substring(lateFeeRuleText.indexOf("an initial late charge equal to ")+"an initial late charge equal to ".length()).trim().split(" ")[0];
			//TN_PropertyWare.lateChargeFee = TN_PropertyWare.lateChargeFee.replaceAll("[^0-9.]", "");
	 	    }
			catch(Exception e)
	 	    {
	 	    	TN_PropertyWare.lateChargeFee =  "Error";	
	 	    	e.printStackTrace();
	 	    }
	 	    System.out.println("Late Charge Fee = "+TN_PropertyWare.lateChargeFee.trim());
	 	   RunnerClass.percentage = TN_PropertyWare.lateChargeFee;
	 	   /*
	 	    // Additional Late Charges
	 	   try
	 	    {
			TN_PropertyWare.additionalLateCharges = lateFeeRuleText.substring(lateFeeRuleText.indexOf("and additional late charge of $")+"and additional late charge of $".length()).trim().split(" ")[0];
			TN_PropertyWare.additionalLateCharges = TN_PropertyWare.additionalLateCharges.replaceAll("[^0-9.]", "");
	 	    }
			catch(Exception e)
	 	    {
	 	    	TN_PropertyWare.additionalLateCharges =  "Error";	
	 	    	e.printStackTrace();
	 	    }
	 	    System.out.println("Additional Late Charges = "+TN_PropertyWare.additionalLateCharges.trim());
	 	    RunnerClass.maximumAmount = TN_PropertyWare.additionalLateCharges;
	 	    //Additional Late Charges Limit
	 	   try
	 	    {
			TN_PropertyWare.additionalLateChargesLimit = lateFeeRuleText.substring(lateFeeRuleText.indexOf("(initial and additional) may not exceed $")+"(initial and additional) may not exceed $".length()).trim().split(" ")[0];
			TN_PropertyWare.additionalLateChargesLimit = TN_PropertyWare.additionalLateChargesLimit.replaceAll("[^0-9.]", "");
	 	    }
			catch(Exception e)
	 	    {
	 	    	TN_PropertyWare.additionalLateChargesLimit =  "Error";	
	 	    	e.printStackTrace();
	 	    }
	 	    System.out.println("Additional Late Charges Limit = "+TN_PropertyWare.additionalLateChargesLimit.trim());
	 	    RunnerClass.additionalLateChargesLimit = TN_PropertyWare.additionalLateChargesLimit;
			return true;
			}
			else
		   {
			TN_PropertyWare.lateFeeType ="";
		   }
		   */
			}
		return true;		
	}


}
