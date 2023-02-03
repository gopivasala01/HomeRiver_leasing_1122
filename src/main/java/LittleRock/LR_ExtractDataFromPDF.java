package LittleRock;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import mainPackage.InsertDataIntoDatabase;
import mainPackage.RunnerClass;

public class LR_ExtractDataFromPDF 
{
	public static boolean petFlag;
	public static String text="";
	public boolean arizona() throws Exception
	//public static void main(String args[]) throws Exception
	{
		
		LR_PropertyWare.petFlag = false;
		File file = RunnerClass.getLastModified();
		//File file = new File("C:\\Gopi\\Projects\\Property ware\\Lease Close Outs\\PDFS\\Tennessee\\Pet agreement\\Lease_0722_0224_3305_Richland_View_Ln_TN_Gap.pdf");
		FileInputStream fis = new FileInputStream(file);
		LR_RunnerClass.document = PDDocument.load(fis);
	    text = new PDFTextStripper().getText(LR_RunnerClass.document);
	    LR_PropertyWare.pdfText  = text;
	    if(!text.contains(LR_AppConfig.PDFFormatConfirmationText)) 
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
	    	LR_PropertyWare.commensementDate = text.substring(text.indexOf(LR_PDFAppConfig.AB_commencementDate_Prior)+LR_PDFAppConfig.AB_commencementDate_Prior.length(),text.indexOf(LR_PDFAppConfig.AB_expirationDate_Prior));
	    	LR_PropertyWare.commensementDate = LR_PropertyWare.commensementDate.trim().replaceAll(" +", " ");
	    }
	    catch(Exception e)
	    {
	    	LR_PropertyWare.commensementDate = "Error";
	    	e.printStackTrace();
	    }
	    System.out.println("Commensement Date = "+LR_PropertyWare.commensementDate);
	   try
	    {
		   String expirationDateWaw = text.substring(text.indexOf(LR_PDFAppConfig.AB_expirationDate_Prior)+LR_PDFAppConfig.AB_expirationDate_Prior.length());
		   LR_PropertyWare.expirationDate =expirationDateWaw.trim().split(" ")[0]+" "+expirationDateWaw.trim().split(" ")[1]+" "+expirationDateWaw.trim().split(" ")[2];
		   LR_PropertyWare.expirationDate = LR_PropertyWare.expirationDate.trim().replaceAll(" +", " ");
	    }
	    catch(Exception e)
	    {
	    	 LR_PropertyWare.expirationDate = "Error";
	    	 e.printStackTrace();
	    }
	   System.out.println("Expiration Date = "+LR_PropertyWare.expirationDate);
	   try
	    {
		    LR_PropertyWare.proratedRent = text.substring(text.indexOf(LR_PDFAppConfig.AB_proratedRent_Prior)+LR_PDFAppConfig.AB_proratedRent_Prior.length(),text.indexOf(LR_PDFAppConfig.AB_proratedRent_After));
		    if(LR_PropertyWare.proratedRent.matches(".*[a-zA-Z]+.*"))
		    {
		    	LR_PropertyWare.proratedRent = "Error";
		    }
		    		
	    }
	    catch(Exception e)
	    {
	    	LR_PropertyWare.proratedRent = "Error";
	    	e.printStackTrace();
	    }
	   System.out.println("Prorated Rent = "+LR_PropertyWare.proratedRent);
	    try
	    {
		    LR_PropertyWare.proratedRentDate = text.substring(text.indexOf(LR_PDFAppConfig.AB_proratedRentDate_Prior)+LR_PDFAppConfig.AB_proratedRentDate_Prior.length(),text.indexOf(LR_PDFAppConfig.AB_proratedRentDate_After)).trim();
	    }
	    catch(Exception e)
	    {
	    	LR_PropertyWare.proratedRentDate = "Error";
	    	e.printStackTrace();
	    }
	    System.out.println("Prorated Rent Date= "+LR_PropertyWare.proratedRentDate.trim());
	    /*
	    try
	    {
		    LR_PropertyWare.monthlyRentDate = text.substring(text.indexOf(LR_PDFAppConfig.AB_fullRentDate_Prior)+LR_PDFAppConfig.AB_fullRentDate_Prior.length(),text.indexOf(LR_PDFAppConfig.AB_fullRentDate_After));
		    System.out.println("Monthly Rent Date= "+LR_PropertyWare.monthlyRentDate.trim());
	    }
	    catch(Exception e)
	    {
	    	try
	    	{
	    		LR_PropertyWare.monthlyRentDate = text.substring(text.indexOf(LR_PDFAppConfig.AB_fullRentDate_Prior)+LR_PDFAppConfig.AB_fullRentDate_Prior.length(),text.indexOf(LR_PDFAppConfig.AB_fullRentDate1_After));
			   	System.out.println("Monthly Rent Date= "+LR_PropertyWare.monthlyRentDate.trim());
	    	}
	    	catch(Exception e1)
		    {
		    	LR_PropertyWare.monthlyRentDate = "Error";  
		    	e1.printStackTrace();
		    }
	    }*/
	    try
	    {
		    LR_PropertyWare.monthlyRent = text.substring(text.indexOf(LR_PDFAppConfig.AB_fullRent_Prior)+LR_PDFAppConfig.AB_fullRent_Prior.length()).trim().split(" ")[0].trim();//,text.indexOf(LR_PDFAppConfig.AB_fullRent_After)).substring(1).replaceAll("[^.0-9]", "");;
		    if(RunnerClass.onlyDigits(LR_PropertyWare.monthlyRent.replace(".", "").replace(",", ""))==false)
		    {
		    	LR_PropertyWare.monthlyRent = text.substring(text.indexOf(LR_PDFAppConfig.AB_fullRent2_Prior)+LR_PDFAppConfig.AB_fullRent2_Prior.length()).trim().split(" ")[0].trim();
		    }
		    if(LR_PropertyWare.monthlyRent.contains("*"))
		    {
		    	LR_PropertyWare.monthlyRent = LR_PropertyWare.monthlyRent.replace("*","");
		    }
		    if(LR_PropertyWare.monthlyRent.matches(".*[a-zA-Z]+.*"))
		    {
		    	LR_PropertyWare.monthlyRent = "Error";
		    }
		    if(LR_PropertyWare.monthlyRent.endsWith(","))
		    {
		    	LR_PropertyWare.monthlyRent = LR_PropertyWare.monthlyRent.substring(0,LR_PropertyWare.monthlyRent.length()-1);
		    }
	    }
	    catch(Exception e)
	    {
	    	 LR_PropertyWare.monthlyRent = "Error";
	    	 e.printStackTrace();
	    }
	    System.out.println("Monthly Rent "+LR_PropertyWare.monthlyRent.trim());
	    try
	    {
		    LR_PropertyWare.adminFee = text.substring(text.indexOf(LR_PDFAppConfig.AB_adminFee_Prior)+LR_PDFAppConfig.AB_adminFee_Prior.length()).trim().split(" ")[0];
		    if(LR_PropertyWare.adminFee.matches(".*[a-zA-Z]+.*"))
		    {
		    	LR_PropertyWare.adminFee = "Error";
		    }
	    }
	    catch(Exception e)
	    {
		    LR_PropertyWare.adminFee = "Error";
		    e.printStackTrace();
	    }
	    System.out.println("Admin Fee = "+LR_PropertyWare.adminFee.trim());
	    
	  //Resident Benefits Package 
	    if(text.contains(LR_PDFAppConfig.residentBenefitsPackageAddendumCheck))
	    {
	    	LR_PropertyWare.residentBenefitsPackageAvailabilityCheck = true;
	    	 try
	 	    {
	 		    LR_PropertyWare.residentBenefitsPackage = text.substring(text.indexOf(LR_PDFAppConfig.AB1_residentBenefitsPackage_Prior)+LR_PDFAppConfig.AB1_residentBenefitsPackage_Prior.length()).split(" ")[0].replaceAll("[^0-9a-zA-Z.]", "");
	 		    if(LR_PropertyWare.residentBenefitsPackage.matches(".*[a-zA-Z]+.*"))
	 		    {
	 		    	LR_PropertyWare.residentBenefitsPackage = "Error";
	 		    }
	 	    }
	 	    catch(Exception e)
	 	    {
	 		    LR_PropertyWare.residentBenefitsPackage = "Error";
	 		    e.printStackTrace();
	 	    }
	    	 System.out.println("Resident Benefits Package  = "+LR_PropertyWare.residentBenefitsPackage.trim());
	    	//LR_PDFAppConfig.AB1_residentBenefitsPackage_Prior
	    }
	    else
	    {
		    if(text.contains(LR_PDFAppConfig_Format2.HVACFilterAddendumTextAvailabilityCheck)==true)
		    {
		    	LR_PropertyWare.HVACFilterFlag =true;
		    }
		    else
		    {
		    try
		    {
			   String[] airFilterFeeArray = text.substring(text.indexOf(LR_PDFAppConfig.AB_airFilterFee_Prior)+LR_PDFAppConfig.AB_airFilterFee_Prior.length()).split(" ");
			   LR_PropertyWare.airFilterFee = airFilterFeeArray[0];
			   if(LR_PropertyWare.airFilterFee.matches(".*[a-zA-Z]+.*"))
			    {
			    	LR_PropertyWare.airFilterFee = "Error";
			    }
		    }
		    catch(Exception e)
		    {
		    LR_PropertyWare.airFilterFee = "Error";
		    e.printStackTrace();
		    }
		    }
		    System.out.println("Air Filter Fee = "+LR_PropertyWare.airFilterFee.trim());
	    }
	    try
	    {
	    	String[] earlyTerminationRaw = text.substring(text.indexOf(LR_PDFAppConfig.AB_earlyTerminationFee_Prior)+LR_PDFAppConfig.AB_earlyTerminationFee_Prior.length()).split(" ");
	    	
		    LR_PropertyWare.earlyTermination = earlyTerminationRaw[0]+earlyTerminationRaw[1]; //text.substring(text.indexOf(LR_PDFAppConfig.AB_earlyTerminationFee_Prior)+LR_PDFAppConfig.AB_earlyTerminationFee_Prior.length(),text.indexOf(LR_PDFAppConfig.AB_earlyTerminationFee_After));
	    }
	    catch(Exception e)
	    {
	    	LR_PropertyWare.earlyTermination = "Error";	
	    	e.printStackTrace();
	    }
	    System.out.println("Early Termination  = "+LR_PropertyWare.earlyTermination.trim());
	    try
	    {
	    	
		    LR_PropertyWare.occupants = text.substring(text.indexOf(LR_PDFAppConfig.AB_occupants_Prior)+LR_PDFAppConfig.AB_occupants_Prior.length(),text.indexOf(LR_PDFAppConfig.AB_occupants_After));
	    }
	    catch(Exception e)
	    {
		    LR_PropertyWare.occupants ="Error";	
		    e.printStackTrace();
	    }
	    System.out.println("Occupants = "+LR_PropertyWare.occupants.trim());
	    
	    //Late charges 
	    //Decide Late Fee Rule
	   LR_ExtractDataFromPDF.lateFeeRule();
	    
	  //Prepayment Charge
  		if(LR_PropertyWare.portfolioType.contains("MCH"))
  		{
  			if(LR_PropertyWare.proratedRent.equalsIgnoreCase("n/a")||LR_PropertyWare.proratedRent.equalsIgnoreCase("Error")||LR_PropertyWare.proratedRent.equalsIgnoreCase(""))
  			{
  				LR_PropertyWare.prepaymentCharge = "Error";
  			}
  			else
  			{
	  		try
	  		{
	  		LR_PropertyWare.prepaymentCharge =String.valueOf(Double.parseDouble(LR_PropertyWare.monthlyRent.trim().replace(",", "")) - Double.parseDouble(LR_PropertyWare.proratedRent.trim().replace(",", ""))); 
	  		}
	  		catch(Exception e)
	  		{
	  			LR_PropertyWare.prepaymentCharge ="Error";
	  		}
	  		}
  			System.out.println("Prepayment Charge = "+LR_PropertyWare.prepaymentCharge);
  		 }
	    petFlag = text.contains(LR_PDFAppConfig.AB_petAgreementAvailabilityCheck);
	    System.out.println("Pet Addendum Available = "+petFlag);
	    if(petFlag ==true)
	    {
	    	LR_PropertyWare.petFlag = true;
	    	try
	    	{
	    	LR_PropertyWare.petSecurityDeposit = text.substring(text.indexOf(LR_PDFAppConfig.AB_securityDeposity_Prior)+LR_PDFAppConfig.AB_securityDeposity_Prior.length(),text.indexOf(LR_PDFAppConfig.AB_securityDeposity_After));
	    	if(LR_PropertyWare.petSecurityDeposit.matches(".*[a-zA-Z]+.*"))
		    {
		    	LR_PropertyWare.petSecurityDeposit = "Error";
		    }
	    	}
	    	catch(Exception e)
	    	{
	    	LR_PropertyWare.petSecurityDeposit = "Error";	
	    	e.printStackTrace();
	    	}
	    	System.out.println("Pet Security Deposit = "+LR_PropertyWare.petSecurityDeposit.trim());
	    	if(RunnerClass.onlyDigits(LR_PropertyWare.petSecurityDeposit.replace(".", ""))==true)
		    {
		    	System.out.println("Security Deposit is checked");
		    }
	    	//TODO Check
	    	  try
			    {
	    		  String proratedPetRaw = "Prorated Pet Rent: On or before "+LR_PropertyWare.commensementDate.trim()+" Tenant will pay Landlord $";
	    		LR_PropertyWare.proratedPetRent = text.substring(text.indexOf(proratedPetRaw)+proratedPetRaw.length()).trim().split(" ")[0];//.replaceAll("[a-ZA-Z,]", "");
			    //LR_PropertyWare.proratedPetRent = proratedPetRentRaw.substring(proratedPetRentRaw.indexOf("Tenant will pay Landlord $")+"Tenant will pay Landlord $".length());//,proratedPetRentRaw.indexOf(AppConfig.AR_proratedPetRent_After));
			    if(LR_PropertyWare.proratedPetRent.matches(".*[a-zA-Z]+.*"))
			    {
			    	LR_PropertyWare.proratedPetRent = "Error";
			    }
			    }
			    catch(Exception e)
			    {
			   
			    LR_PropertyWare.proratedPetRent = "Error";	
			    e.printStackTrace();
			    }
	    	  System.out.println("Prorated Pet Rent = "+LR_PropertyWare.proratedPetRent.trim());
	    	
	    	try
		    {
	    		 LR_PropertyWare.petRent = text.substring(text.indexOf(LR_PDFAppConfig.AB_petRent_Prior)+LR_PDFAppConfig.AB_petRent_Prior.length()).trim().split(" ")[0];
	    		 if(LR_PropertyWare.petRent.contains(",for"))
	    		 {
	    			 LR_PropertyWare.petRent = LR_PropertyWare.petRent.split(",")[0].trim();
	    		 }
	    		 else
	    		 {
		    		 if(LR_PropertyWare.petRent.matches(".*[a-zA-Z]+.*")==true)
		    			 LR_PropertyWare.petRent = text.substring(text.indexOf(LR_PDFAppConfig.AB_petRent1_Prior)+LR_PDFAppConfig.AB_petRent1_Prior.length()).trim().split(" ")[0];
		    		 else 
		    		 LR_PropertyWare.petRent = RunnerClass.extractNumber(LR_PropertyWare.petRent);
	    		 }
		    }
	    	catch(Exception e)
		    {
	    		try
	    		{
	    			e.printStackTrace();
	    			LR_PropertyWare.petRent = text.substring(text.indexOf(LR_PDFAppConfig.AB_petRent1_Prior)+LR_PDFAppConfig.AB_petRent1_Prior.length()).trim().split(" ")[0];
//					 System.out.println("Pet rent = "+LR_PropertyWare.petRent.trim());
	    			if(LR_PropertyWare.petRent.matches(".*[a-zA-Z]+.*"))
	    		    {
	    		    	LR_PropertyWare.petRent = "Error";
	    		    }
	    		}
	    		
	    		catch(Exception e1)
			    {
			    	LR_PropertyWare.petRent = "Error";  
			    	e1.printStackTrace();
			    }
		    }
	    	System.out.println("Pet rent = "+LR_PropertyWare.petRent.trim());
		    	//LR_PropertyWare.petRent = "Error";  
		    	//e.printStackTrace();
		   /* 
	    	try
    		{
    			//String petFeeRaw1 = text.substring(text.indexOf(LR_PDFAppConfig.AB_petFee_Prior));
    			LR_PropertyWare.petFee = text.substring(text.indexOf(LR_PDFAppConfig.AB_petFee_Prior)+LR_PDFAppConfig.AB_petFee_Prior.length()).trim().split(" ")[0].trim();
    			//LR_PropertyWare.petFee =  petFeeRaw[petFeeRaw.length-2].trim();
    			//if(LR_PropertyWare.petFee.matches(".*[a-zA-Z]+.*"))
    			//{
    				//LR_PropertyWare.petFee = text.substring(text.indexOf(LR_PDFAppConfig.AB_petFee2_Prior)+LR_PDFAppConfig.AB_petFee2_Prior.length()).trim().split(" ")[0].trim();
    			//}
    			//System.out.println(petFeeRaw.length);
    		}
    		
    		catch(Exception e1)
		    {
		    	LR_PropertyWare.petFee = "Error";  
		    	e1.printStackTrace();
		    }
	    	System.out.println("Pet Fee = "+LR_PropertyWare.petFee);
	    	*/
	    	// Get text between Type: word
	    	
	    	String typeSubString = text.substring(text.indexOf(LR_PDFAppConfig.AB_typeWord_Prior)+LR_PDFAppConfig.AB_typeWord_Prior.length(),text.indexOf(LR_PDFAppConfig.AB_typeWord_After));
	    	
	    	String newText = typeSubString.replace("Type:","");
		    LR_PropertyWare.countOfTypeWordInText = ((typeSubString.length() - newText.length())/"Type:".length());
		    System.out.println("Type: occurences = "+LR_PropertyWare.countOfTypeWordInText);
		    
		    LR_PropertyWare.petType = new ArrayList();
		    LR_PropertyWare.petBreed = new ArrayList();
		    LR_PropertyWare.petWeight = new ArrayList();
		    for(int i =0;i<LR_PropertyWare.countOfTypeWordInText;i++)
		    {
		    	String type = typeSubString.substring(RunnerClass.nthOccurrence(typeSubString, "Type:", i+1)+LR_PDFAppConfig.AB_pet1Type_Prior.length(),RunnerClass.nthOccurrence(typeSubString, "Breed:", i+1)).trim();
		    	if(type.contains("N/A")||type.contains("n/a"))
		    		break;
		    	System.out.println(type);
		    	LR_PropertyWare.petType.add(type);
		    	int pet1Breedindex1 = RunnerClass.nthOccurrence(typeSubString, "Breed:", i+1)+"Breed:".length()+1;
			    String subString = typeSubString.substring(pet1Breedindex1);
			    //int pet1Breedindex2 = RunnerClass.nthOccurrence(subString,"Name:",i+1);
			   // System.out.println("Index 2 = "+(index2+index1));
			    String breed = subString.split("Name:")[0].trim();//typeSubString.substring(pet1Breedindex1,(pet1Breedindex2+pet1Breedindex1));
			    System.out.println(breed);
			    LR_PropertyWare.petBreed.add(breed);
			    int pet1Weightindex1 = RunnerClass.nthOccurrence(typeSubString, "Weight:", i+1)+"Weight:".length()+1;
			    String pet1WeightSubstring = typeSubString.substring(pet1Weightindex1);
			    //int pet1WeightIndex2 = RunnerClass.nthOccurrence(pet1WeightSubstring,"Age:",i+1);
			   // System.out.println("Index 2 = "+(index2+index1));
			    String weight = pet1WeightSubstring.split("Age:")[0].trim(); //typeSubString.substring(pet1Weightindex1,(pet1WeightIndex2+pet1Weightindex1));
			    System.out.println(weight);
			    LR_PropertyWare.petWeight.add(weight);
		    }
		    
		    
		    
		    try
		    {
		    	LR_PropertyWare.petOneTimeNonRefundableFee = text.substring(text.indexOf(LR_PDFAppConfig.AB_petFeeOneTime_Prior)+LR_PDFAppConfig.AB_petFeeOneTime_Prior.length()).split(" ")[0];//,text.indexOf(LR_PDFAppConfig.AB_petFeeOneTime_After));
		    	if(LR_PropertyWare.petOneTimeNonRefundableFee.matches(".*[a-zA-Z]+.*"))
    		    {
    		    	LR_PropertyWare.petOneTimeNonRefundableFee = "Error";
    		    }
		    }
		    catch(Exception e)
		    {
		    	LR_PropertyWare.petOneTimeNonRefundableFee =  "Error";
		    	e.printStackTrace();
		    }  
		    System.out.println("pet one time non refundable = "+LR_PropertyWare.petOneTimeNonRefundableFee.trim());
		   
	    }
	    
	    //Service Animal Addendum check
	    try
	    {
	    if(text.contains(LR_AppConfig.serviceAnimalText))
	    {
	    	LR_PropertyWare.serviceAnimalFlag = true;
    		System.out.println("Service Animal Addendum is available");
    		
            String typeSubString = text.substring(text.indexOf(LR_PDFAppConfig.AB_serviceAnimal_typeWord_Prior)+LR_PDFAppConfig.AB_serviceAnimal_typeWord_Prior.length(),text.indexOf(LR_PDFAppConfig.AB_serviceAnimal_typeWord_After));
	    	
	    	String newText = typeSubString.replace("Type:","");
		    int  countOftypeWords_ServiceAnimal = ((typeSubString.length() - newText.length())/"Type:".length());
		    System.out.println("Service Animal - Type: occurences = "+countOftypeWords_ServiceAnimal);
		    
		    LR_PropertyWare.serviceAnimalPetType = new ArrayList();
		    LR_PropertyWare.serviceAnimalPetBreed = new ArrayList();
		    LR_PropertyWare.serviceAnimalPetWeight = new ArrayList();
		    for(int i =0;i<countOftypeWords_ServiceAnimal;i++)
		    {
		    	String type = typeSubString.substring(RunnerClass.nthOccurrence(typeSubString, "Type:", i+1)+LR_PDFAppConfig.AB_pet1Type_Prior.length(),RunnerClass.nthOccurrence(typeSubString, "Breed:", i+1)).trim();
		    	if(type.contains("N/A")||type.contains("n/a"))
		    		break;
		    	System.out.println(type);
		    	LR_PropertyWare.serviceAnimalPetType.add(type);
		    	int pet1Breedindex1 = RunnerClass.nthOccurrence(typeSubString, "Breed:", i+1)+"Breed:".length()+1;
			    String subString = typeSubString.substring(pet1Breedindex1);
			    //int pet1Breedindex2 = RunnerClass.nthOccurrence(subString,"Name:",i+1);
			   // System.out.println("Index 2 = "+(index2+index1));
			    String breed = subString.split("Name:")[0].trim();//typeSubString.substring(pet1Breedindex1,(pet1Breedindex2+pet1Breedindex1));
			    System.out.println(breed);
			    LR_PropertyWare.serviceAnimalPetBreed.add(breed);
			    int pet1Weightindex1 = RunnerClass.nthOccurrence(typeSubString, "Weight:", i+1)+"Weight:".length()+1;
			    String pet1WeightSubstring = typeSubString.substring(pet1Weightindex1);
			    //int pet1WeightIndex2 = RunnerClass.nthOccurrence(pet1WeightSubstring,"Age:",i+1);
			   // System.out.println("Index 2 = "+(index2+index1));
			    String weight = pet1WeightSubstring.split("Age:")[0].trim(); //typeSubString.substring(pet1Weightindex1,(pet1WeightIndex2+pet1Weightindex1));
			    System.out.println(weight);
			    LR_PropertyWare.serviceAnimalPetWeight.add(weight);
		    }
    		
    		
	    }
	    }
	    catch(Exception e)
	    {
	    	LR_PropertyWare.serviceAnimalFlag = false;
	    }
	    //Concession Addendum
	    
	    try
	    {
	    	if(text.contains(LR_PDFAppConfig.concessionAddendumText))
	    	{
	    		LR_PropertyWare.concessionAddendumFlag = true;
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
		 lateFeeRuleText = text.substring(text.indexOf(LR_PDFAppConfig.lateFeeRuleText_Prior)+LR_PDFAppConfig.lateFeeRuleText_Prior.length(),text.indexOf(LR_PDFAppConfig.lateFeeRuleText_After));
		}
		catch(Exception e)
		{
			try
			{
			lateFeeRuleText = text.substring(text.indexOf(LR_PDFAppConfig.lateFeeRuleText_Prior)+LR_PDFAppConfig.lateFeeRuleText_Prior.length(),text.indexOf(LR_PDFAppConfig.lateFeeRuleText_After2));
			}
			catch(Exception e2)
			{
			return false;
			}
		}
		if(lateFeeRuleText.contains(LR_PDFAppConfig.lateFeeRule_whicheverIsGreater))
		{
			LR_PropertyWare.lateFeeType ="GreaterOfFlatFeeOrPercentage"; 
		//Late charge day
			try
			{
		   // LR_PropertyWare.lateChargeDay =  lateFeeRuleText.substring(lateFeeRuleText.indexOf(LR_PDFAppConfig.lateFeeRule_whicheverIsGreater_dueDay_Prior)+LR_PDFAppConfig.lateFeeRule_whicheverIsGreater_dueDay_Prior.length()).trim().split(" ")[0];
				LR_PropertyWare.lateChargeDay =  lateFeeRuleText.split(LR_PDFAppConfig.lateFeeRule_whicheverIsGreater_dueDay_After)[0].trim();
				LR_PropertyWare.lateChargeDay =LR_PropertyWare.lateChargeDay.substring(LR_PropertyWare.lateChargeDay.lastIndexOf(" ")+1);
		    LR_PropertyWare.lateChargeDay =  LR_PropertyWare.lateChargeDay.replaceAll("[^0-9]", "");
			}
			catch(Exception e)
			{
				LR_PropertyWare.lateChargeDay = "Error";
			}
         System.out.println("Late Charge Day = "+LR_PropertyWare.lateChargeDay);
			
		//Late Fee Percentage
			try
			{
		    LR_PropertyWare.lateFeePercentage =  lateFeeRuleText.substring(lateFeeRuleText.indexOf(LR_PDFAppConfig.lateFeeRule_whicheverIsGreater_lateFeePercentage)+LR_PDFAppConfig.lateFeeRule_whicheverIsGreater_lateFeePercentage.length()).trim().split(" ")[0];
		    LR_PropertyWare.lateFeePercentage = LR_PropertyWare.lateFeePercentage.replaceAll("[^0-9]", "");
			}
			catch(Exception e)
			{
				LR_PropertyWare.lateFeePercentage = "Error";
			}
         System.out.println("Late Fee Percentage = "+LR_PropertyWare.lateFeePercentage);
         
         //Late Fee Amount
         try
         {
         String lateFeeAmount  = lateFeeRuleText.substring(lateFeeRuleText.indexOf(LR_PDFAppConfig.lateFeeRule_whicheverIsGreater_lateFeeAmount)+LR_PDFAppConfig.lateFeeRule_whicheverIsGreater_lateFeeAmount.length()).trim().split(" ")[0];
         LR_PropertyWare.flatFeeAmount = lateFeeAmount.replaceAll("[^.0-9]", "");
         }
         catch(Exception e)
         {
        	 LR_PropertyWare.flatFeeAmount ="Error";
         }
         System.out.println("Late Fee Amount = "+LR_PropertyWare.flatFeeAmount);
         
        
         return true;
		}
		else 
		if(lateFeeRuleText.contains(LR_PDFAppConfig.lateFeeRule_mayNotExceedMoreThan30Days))
		{
			LR_PropertyWare.lateFeeType ="initialFeePluPerDayFee"; 
	         try
	 	    {
	 		    LR_PropertyWare.lateChargeFee = text.substring(text.indexOf(LR_PDFAppConfig.AB_lateFee_Prior)+LR_PDFAppConfig.AB_lateFee_Prior.length()).trim().split(" ")[0];
	 		    //LR_PropertyWare.lateChargeFee =  LR_PropertyWare.lateChargeFee.substring(0,LR_PropertyWare.lateChargeFee.length()-1);
	 	    }
	 	    catch(Exception e)
	 	    {
	 		    LR_PropertyWare.lateChargeFee ="Error";	
	 		    e.printStackTrace();
	 	    }
	 	    System.out.println("Late Charge Fee = "+LR_PropertyWare.lateChargeFee.trim());
	 	    //Per Day Fee
	 	    try
	 	    {
	 	    	LR_PropertyWare.lateFeeChargePerDay = text.substring(text.indexOf(LR_PDFAppConfig.AB_additionalLateChargesPerDay_Prior)+LR_PDFAppConfig.AB_additionalLateChargesPerDay_Prior.length()).split(" ")[0].trim();//,text.indexOf(LR_PDFAppConfig.AB_additionalLateChargesPerDay_After));
	 	    }
	 	    catch(Exception e)
	 	    {
	 	    	LR_PropertyWare.lateFeeChargePerDay =  "Error";	
	 	    	e.printStackTrace();
	 	    }
	 	    System.out.println("Per Day Fee = "+LR_PropertyWare.lateFeeChargePerDay.trim());
	 	    //Additional Late Charges Limit
	 	    try
	 	    {
	 	    	LR_PropertyWare.additionalLateChargesLimit = text.substring(text.indexOf(LR_PDFAppConfig.AB_additionalLateChargesLimit_Prior)+LR_PDFAppConfig.AB_additionalLateChargesLimit_Prior.length()).trim().split(" ")[0]; //,text.indexOf(LR_PDFAppConfig.AB_additionalLateChargesLimit_After));
	 	    }
	 	    catch(Exception e)
	 	    {
	 	    	LR_PropertyWare.additionalLateChargesLimit =  "Error";	
	 	    	e.printStackTrace();
	 	    }
	 	    System.out.println("additional Late Charges Limit = "+LR_PropertyWare.additionalLateChargesLimit.trim());
	 	 //Late Charge Day
			try
	 	    {
			LR_PropertyWare.lateChargeDay = lateFeeRuleText.substring(lateFeeRuleText.indexOf("p.m. on the ")+"p.m. on the ".length()).trim().split(" ")[0];
			LR_PropertyWare.lateChargeDay = LR_PropertyWare.lateChargeDay.replaceAll("[^0-9]", "");
	 	    }
			catch(Exception e)
	 	    {
	 	    	LR_PropertyWare.lateChargeDay =  "Error";	
	 	    	e.printStackTrace();
	 	    }
	 	    System.out.println("Late Charge Due Day = "+LR_PropertyWare.lateChargeDay.trim());
	 	   return true;
		}
		else if(lateFeeRuleText.contains(LR_PDFAppConfig.lateFeeRule_mayNotExceedAmount))
			{
			//Late Charge Day
			try
	 	    {
			LR_PropertyWare.lateChargeDay = lateFeeRuleText.substring(lateFeeRuleText.indexOf("an initial late charge on the")+"an initial late charge on the".length()).trim().split(" ")[0];
			LR_PropertyWare.lateChargeDay = LR_PropertyWare.lateChargeDay.replaceAll("[^0-9]", "");
	 	    }
			catch(Exception e)
	 	    {
	 	    	LR_PropertyWare.lateChargeDay =  "Error";	
	 	    	e.printStackTrace();
	 	    }
	 	    System.out.println("Late Charge Due Day = "+LR_PropertyWare.lateChargeDay.trim());
	 	    // initial Late Charge
	 	   try
	 	    {
			LR_PropertyWare.lateChargeFee = lateFeeRuleText.substring(lateFeeRuleText.indexOf("day of the month equal to $")+"day of the month equal to $".length()).trim().split(" ")[0];
			LR_PropertyWare.lateChargeFee = LR_PropertyWare.lateChargeFee.replaceAll("[^0-9.]", "");
	 	    }
			catch(Exception e)
	 	    {
	 	    	LR_PropertyWare.lateChargeFee =  "Error";	
	 	    	e.printStackTrace();
	 	    }
	 	    System.out.println("Late Charge Fee = "+LR_PropertyWare.lateChargeFee.trim());
	 	    // Additional Late Charges
	 	   try
	 	    {
			LR_PropertyWare.additionalLateCharges = lateFeeRuleText.substring(lateFeeRuleText.indexOf("and additional late charge of $")+"and additional late charge of $".length()).trim().split(" ")[0];
			LR_PropertyWare.additionalLateCharges = LR_PropertyWare.additionalLateCharges.replaceAll("[^0-9.]", "");
	 	    }
			catch(Exception e)
	 	    {
	 	    	LR_PropertyWare.additionalLateCharges =  "Error";	
	 	    	e.printStackTrace();
	 	    }
	 	    System.out.println("Additional Late Charges = "+LR_PropertyWare.additionalLateCharges.trim());
	 	    //Additional Late Charges Limit
	 	   try
	 	    {
			LR_PropertyWare.additionalLateChargesLimit = lateFeeRuleText.substring(lateFeeRuleText.indexOf("(initial and additional) may not exceed $")+"(initial and additional) may not exceed $".length()).trim().split(" ")[0];
			LR_PropertyWare.additionalLateChargesLimit = LR_PropertyWare.additionalLateChargesLimit.replaceAll("[^0-9.]", "");
	 	    }
			catch(Exception e)
	 	    {
	 	    	LR_PropertyWare.additionalLateChargesLimit =  "Error";	
	 	    	e.printStackTrace();
	 	    }
	 	    System.out.println("Additional Late Charges Limit = "+LR_PropertyWare.additionalLateChargesLimit.trim());
			return true;
			}
		else 
			if(lateFeeRuleText.contains(LR_PDFAppConfig.lateFeeRule_totalDelinquentRentDueToTheTenantAccount))
			{
			//Late Charge Day
			try
	 	    {
			LR_PropertyWare.lateChargeDay = lateFeeRuleText.substring(lateFeeRuleText.indexOf("place of payment on the ")+"place of payment on the ".length()).trim().split(" ")[0];
			LR_PropertyWare.lateChargeDay = LR_PropertyWare.lateChargeDay.replaceAll("[^0-9]", "");
	 	    }
			catch(Exception e)
	 	    {
	 	    	LR_PropertyWare.lateChargeDay =  "Error";	
	 	    	e.printStackTrace();
	 	    }
	 	    System.out.println("Late Charge Due Day = "+LR_PropertyWare.lateChargeDay.trim());
	 	    // initial Late Charge
	 	   try
	 	    {
			LR_PropertyWare.lateChargeFee = lateFeeRuleText.substring(lateFeeRuleText.indexOf("an initial late charge equal to ")+"an initial late charge equal to ".length()).trim().split(" ")[0];
			//LR_PropertyWare.lateChargeFee = LR_PropertyWare.lateChargeFee.replaceAll("[^0-9.]", "");
	 	    }
			catch(Exception e)
	 	    {
	 	    	LR_PropertyWare.lateChargeFee =  "Error";	
	 	    	e.printStackTrace();
	 	    }
	 	    System.out.println("Late Charge Fee = "+LR_PropertyWare.lateChargeFee.trim());
	 	    // Additional Late Charges
	 	   try
	 	    {
			LR_PropertyWare.additionalLateCharges = lateFeeRuleText.substring(lateFeeRuleText.indexOf("and additional late charge of $")+"and additional late charge of $".length()).trim().split(" ")[0];
			LR_PropertyWare.additionalLateCharges = LR_PropertyWare.additionalLateCharges.replaceAll("[^0-9.]", "");
	 	    }
			catch(Exception e)
	 	    {
	 	    	LR_PropertyWare.additionalLateCharges =  "Error";	
	 	    	e.printStackTrace();
	 	    }
	 	    System.out.println("Additional Late Charges = "+LR_PropertyWare.additionalLateCharges.trim());
	 	    //Additional Late Charges Limit
	 	   try
	 	    {
			LR_PropertyWare.additionalLateChargesLimit = lateFeeRuleText.substring(lateFeeRuleText.indexOf("(initial and additional) may not exceed $")+"(initial and additional) may not exceed $".length()).trim().split(" ")[0];
			LR_PropertyWare.additionalLateChargesLimit = LR_PropertyWare.additionalLateChargesLimit.replaceAll("[^0-9.]", "");
	 	    }
			catch(Exception e)
	 	    {
	 	    	LR_PropertyWare.additionalLateChargesLimit =  "Error";	
	 	    	e.printStackTrace();
	 	    }
	 	    System.out.println("Additional Late Charges Limit = "+LR_PropertyWare.additionalLateChargesLimit.trim());
			return true;
			}
			else
		   {
			LR_PropertyWare.lateFeeType ="";
			return false;
		   }
		
	}



}
