package Tulsa;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import mainPackage.InsertDataIntoDatabase;
import mainPackage.RunnerClass;

public class Tulsa_ExtractDataFromPDF 
{
	public static boolean petFlag;
	public static String text="";
	public boolean arizona() throws Exception
	//public static void main(String args[]) throws Exception
	{
		
		Tulsa_PropertyWare.petFlag = false;
		File file = RunnerClass.getLastModified();
		//File file = new File("C:\\Gopi\\Projects\\Property ware\\Lease Close Outs\\PDFS\\PENN11171117PennLn\\Lease_0921_0123_1117_Penn_Ln_OKC_Smart.pdf");
		FileInputStream fis = new FileInputStream(file);
		Tulsa_RunnerClass.document = PDDocument.load(fis);
	    text = new PDFTextStripper().getText(Tulsa_RunnerClass.document);
	    Tulsa_PropertyWare.pdfText  = text;
	    if(!text.contains(Tulsa_AppConfig.PDFFormatConfirmationText)) 
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
	    	Tulsa_PropertyWare.commensementDate = text.substring(text.indexOf(Tulsa_PDFAppConfig.AB_commencementDate_Prior)+Tulsa_PDFAppConfig.AB_commencementDate_Prior.length());
	    	Tulsa_PropertyWare.commensementDate = Tulsa_PropertyWare.commensementDate.substring(0, Tulsa_PropertyWare.commensementDate.indexOf(" (")).trim();
	    	Tulsa_PropertyWare.commensementDate = Tulsa_PropertyWare.commensementDate.trim().replaceAll(" +", " ");
	    }
	    catch(Exception e)
	    {
	    	Tulsa_PropertyWare.commensementDate = "Error";
	    	e.printStackTrace();
	    }
	    System.out.println("Commensement Date = "+Tulsa_PropertyWare.commensementDate);
	   try
	    {
		   String expirationDateWaw = text.substring(text.indexOf(Tulsa_PDFAppConfig.AB_expirationDate_Prior)+Tulsa_PDFAppConfig.AB_expirationDate_Prior.length());
		   Tulsa_PropertyWare.expirationDate =expirationDateWaw.substring(0,expirationDateWaw.indexOf(" (")).trim();
		   Tulsa_PropertyWare.expirationDate = Tulsa_PropertyWare.expirationDate.trim().replaceAll(" +", " ");
	    }
	    catch(Exception e)
	    {
	    	 Tulsa_PropertyWare.expirationDate = "Error";
	    	 e.printStackTrace();
	    }
	   System.out.println("Expiration Date = "+Tulsa_PropertyWare.expirationDate);
	   try
	    {
		    Tulsa_PropertyWare.proratedRent = text.substring(text.indexOf(Tulsa_PDFAppConfig.AB_proratedRent_Prior)+Tulsa_PDFAppConfig.AB_proratedRent_Prior.length(),text.indexOf(Tulsa_PDFAppConfig.AB_proratedRent_After));
		    if(Tulsa_PropertyWare.proratedRent.matches(".*[a-zA-Z]+.*"))
		    {
		    	Tulsa_PropertyWare.proratedRent = "Error";
		    }
		    		
	    }
	    catch(Exception e)
	    {
	    	Tulsa_PropertyWare.proratedRent = "Error";
	    	e.printStackTrace();
	    }
	   System.out.println("Prorated Rent = "+Tulsa_PropertyWare.proratedRent);
	    try
	    {
		    Tulsa_PropertyWare.proratedRentDate = text.substring(text.indexOf(Tulsa_PDFAppConfig.AB_proratedRentDate_Prior)+Tulsa_PDFAppConfig.AB_proratedRentDate_Prior.length(),text.indexOf(Tulsa_PDFAppConfig.AB_proratedRentDate_After)).trim();
	    }
	    catch(Exception e)
	    {
	    	Tulsa_PropertyWare.proratedRentDate = "Error";
	    	e.printStackTrace();
	    }
	    System.out.println("Prorated Rent Date= "+Tulsa_PropertyWare.proratedRentDate.trim());
	    /*
	    try
	    {
		    Tulsa_PropertyWare.monthlyRentDate = text.substring(text.indexOf(Tulsa_PDFAppConfig.AB_fullRentDate_Prior)+Tulsa_PDFAppConfig.AB_fullRentDate_Prior.length(),text.indexOf(Tulsa_PDFAppConfig.AB_fullRentDate_After));
		    System.out.println("Monthly Rent Date= "+Tulsa_PropertyWare.monthlyRentDate.trim());
	    }
	    catch(Exception e)
	    {
	    	try
	    	{
	    		Tulsa_PropertyWare.monthlyRentDate = text.substring(text.indexOf(Tulsa_PDFAppConfig.AB_fullRentDate_Prior)+Tulsa_PDFAppConfig.AB_fullRentDate_Prior.length(),text.indexOf(Tulsa_PDFAppConfig.AB_fullRentDate1_After));
			   	System.out.println("Monthly Rent Date= "+Tulsa_PropertyWare.monthlyRentDate.trim());
	    	}
	    	catch(Exception e1)
		    {
		    	Tulsa_PropertyWare.monthlyRentDate = "Error";  
		    	e1.printStackTrace();
		    }
	    }*/
	    try
	    {
		    Tulsa_PropertyWare.monthlyRent = text.substring(text.indexOf(Tulsa_PDFAppConfig.AB_fullRent_Prior)+Tulsa_PDFAppConfig.AB_fullRent_Prior.length()).trim().split(" ")[0].trim();//,text.indexOf(Tulsa_PDFAppConfig.AB_fullRent_After)).substring(1).replaceAll("[^.0-9]", "");;
		    if(RunnerClass.onlyDigits(Tulsa_PropertyWare.monthlyRent.replace(".", "").replace(",", ""))==false)
		    {
		    	Tulsa_PropertyWare.monthlyRent = text.substring(text.indexOf(Tulsa_PDFAppConfig.AB_fullRent2_Prior)+Tulsa_PDFAppConfig.AB_fullRent2_Prior.length()).trim().split(" ")[0].trim();
		    }
		    if(Tulsa_PropertyWare.monthlyRent.contains("*"))
		    {
		    	Tulsa_PropertyWare.monthlyRent = Tulsa_PropertyWare.monthlyRent.replace("*","");
		    }
		    if(Tulsa_PropertyWare.monthlyRent.matches(".*[a-zA-Z]+.*"))
		    {
		    	Tulsa_PropertyWare.monthlyRent = "Error";
		    }
		    if(Tulsa_PropertyWare.monthlyRent.endsWith(","))
		    {
		    	Tulsa_PropertyWare.monthlyRent = Tulsa_PropertyWare.monthlyRent.substring(0,Tulsa_PropertyWare.monthlyRent.length()-1);
		    }
	    }
	    catch(Exception e)
	    {
	    	 Tulsa_PropertyWare.monthlyRent = "Error";
	    	 e.printStackTrace();
	    }
	    System.out.println("Monthly Rent "+Tulsa_PropertyWare.monthlyRent.trim());
	    try
	    {
		    Tulsa_PropertyWare.adminFee = text.substring(text.indexOf(Tulsa_PDFAppConfig.AB_adminFee_Prior)+Tulsa_PDFAppConfig.AB_adminFee_Prior.length()).trim().split(" ")[0];
		    if(Tulsa_PropertyWare.adminFee.matches(".*[a-zA-Z]+.*"))
		    {
		    	Tulsa_PropertyWare.adminFee = "Error";
		    }
	    }
	    catch(Exception e)
	    {
		    Tulsa_PropertyWare.adminFee = "Error";
		    e.printStackTrace();
	    }
	    System.out.println("Admin Fee = "+Tulsa_PropertyWare.adminFee.trim());
	    
	  //Resident Benefits Package 
	    if(text.contains(Tulsa_PDFAppConfig.residentBenefitsPackageAddendumCheck))
	    {
	    	Tulsa_PropertyWare.residentBenefitsPackageAvailabilityCheck = true;
	    	 try
	 	    {
	 		    Tulsa_PropertyWare.residentBenefitsPackage = text.substring(text.indexOf(Tulsa_PDFAppConfig.AB1_residentBenefitsPackage_Prior)+Tulsa_PDFAppConfig.AB1_residentBenefitsPackage_Prior.length()).split(" ")[0].replaceAll("[^0-9a-zA-Z.]", "");
	 		    if(Tulsa_PropertyWare.residentBenefitsPackage.matches(".*[a-zA-Z]+.*"))
	 		    {
	 		    	Tulsa_PropertyWare.residentBenefitsPackage = "Error";
	 		    }
	 	    }
	 	    catch(Exception e)
	 	    {
	 		    Tulsa_PropertyWare.residentBenefitsPackage = "Error";
	 		    e.printStackTrace();
	 	    }
	    	 System.out.println("Resident Benefits Package  = "+Tulsa_PropertyWare.residentBenefitsPackage.trim());
	    	//Tulsa_PDFAppConfig.AB1_residentBenefitsPackage_Prior
	    }
	    else
	    {
		    if(text.contains(Tulsa_PDFAppConfig_Format2.HVACFilterAddendumTextAvailabilityCheck)==true)
		    {
		    	Tulsa_PropertyWare.HVACFilterFlag =true;
		    }
		    else
		    {
		    try
		    {
			   String[] airFilterFeeArray = text.substring(text.indexOf(Tulsa_PDFAppConfig.AB_airFilterFee_Prior)+Tulsa_PDFAppConfig.AB_airFilterFee_Prior.length()).split(" ");
			   Tulsa_PropertyWare.airFilterFee = airFilterFeeArray[0];
			   if(Tulsa_PropertyWare.airFilterFee.matches(".*[a-zA-Z]+.*"))
			    {
			    	Tulsa_PropertyWare.airFilterFee = "Error";
			    }
		    }
		    catch(Exception e)
		    {
		    Tulsa_PropertyWare.airFilterFee = "Error";
		    e.printStackTrace();
		    }
		    }
		    System.out.println("Air Filter Fee = "+Tulsa_PropertyWare.airFilterFee.trim());
	    }
	    try
	    {
	    	String[] earlyTerminationRaw = text.substring(text.indexOf(Tulsa_PDFAppConfig.AB_earlyTerminationFee_Prior)+Tulsa_PDFAppConfig.AB_earlyTerminationFee_Prior.length()).split(" ");
	    	
		    Tulsa_PropertyWare.earlyTermination = earlyTerminationRaw[0]+earlyTerminationRaw[1]; //text.substring(text.indexOf(Tulsa_PDFAppConfig.AB_earlyTerminationFee_Prior)+Tulsa_PDFAppConfig.AB_earlyTerminationFee_Prior.length(),text.indexOf(Tulsa_PDFAppConfig.AB_earlyTerminationFee_After));
	    }
	    catch(Exception e)
	    {
	    	Tulsa_PropertyWare.earlyTermination = "Error";	
	    	e.printStackTrace();
	    }
	    System.out.println("Early Termination  = "+Tulsa_PropertyWare.earlyTermination.trim());
	    try
	    {
	    	
		    Tulsa_PropertyWare.occupants = text.substring(text.indexOf(Tulsa_PDFAppConfig.AB_occupants_Prior)+Tulsa_PDFAppConfig.AB_occupants_Prior.length(),text.indexOf(Tulsa_PDFAppConfig.AB_occupants_After));
	    }
	    catch(Exception e)
	    {
		    Tulsa_PropertyWare.occupants ="Error";	
		    e.printStackTrace();
	    }
	    System.out.println("Occupants = "+Tulsa_PropertyWare.occupants.trim());
	    
	    //Late charges 
	    //Decide Late Fee Rule
	   Tulsa_ExtractDataFromPDF.lateFeeRule();
	    
	  //Prepayment Charge
  		if(Tulsa_PropertyWare.portfolioType.contains("MCH"))
  		{
  			if(Tulsa_PropertyWare.proratedRent.equalsIgnoreCase("n/a")||Tulsa_PropertyWare.proratedRent.equalsIgnoreCase("Error")||Tulsa_PropertyWare.proratedRent.equalsIgnoreCase(""))
  			{
  				Tulsa_PropertyWare.prepaymentCharge = "Error";
  			}
  			else
  			{
	  		try
	  		{
	  		Tulsa_PropertyWare.prepaymentCharge =String.valueOf(Double.parseDouble(Tulsa_PropertyWare.monthlyRent.trim().replace(",", "")) - Double.parseDouble(Tulsa_PropertyWare.proratedRent.trim().replace(",", ""))); 
	  		}
	  		catch(Exception e)
	  		{
	  			Tulsa_PropertyWare.prepaymentCharge ="Error";
	  		}
	  		}
  			System.out.println("Prepayment Charge = "+Tulsa_PropertyWare.prepaymentCharge);
  		 }
	    petFlag = text.contains(Tulsa_PDFAppConfig.AB_petAgreementAvailabilityCheck);
	    System.out.println("Pet Addendum Available = "+petFlag);
	    if(petFlag ==true)
	    {
	    	Tulsa_PropertyWare.petFlag = true;
	    	try
	    	{
	    	Tulsa_PropertyWare.petSecurityDeposit = text.substring(text.indexOf(Tulsa_PDFAppConfig.AB_securityDeposity_Prior)+Tulsa_PDFAppConfig.AB_securityDeposity_Prior.length(),text.indexOf(Tulsa_PDFAppConfig.AB_securityDeposity_After));
	    	if(Tulsa_PropertyWare.petSecurityDeposit.matches(".*[a-zA-Z]+.*"))
		    {
		    	Tulsa_PropertyWare.petSecurityDeposit = "Error";
		    }
	    	}
	    	catch(Exception e)
	    	{
	    	Tulsa_PropertyWare.petSecurityDeposit = "Error";	
	    	e.printStackTrace();
	    	}
	    	System.out.println("Pet Security Deposit = "+Tulsa_PropertyWare.petSecurityDeposit.trim());
	    	if(RunnerClass.onlyDigits(Tulsa_PropertyWare.petSecurityDeposit.replace(".", ""))==true)
		    {
		    	System.out.println("Security Deposit is checked");
		    }
	    	//TODO Check
	    	  try
			    {
	    		  String proratedPetRaw = "Prorated Pet Rent: On or before "+Tulsa_PropertyWare.commensementDate.trim()+" Tenant will pay Landlord $";
	    		Tulsa_PropertyWare.proratedPetRent = text.substring(text.indexOf(proratedPetRaw)+proratedPetRaw.length()).trim().split(" ")[0];//.replaceAll("[a-ZA-Z,]", "");
			    //AR_PropertyWare.proratedPetRent = proratedPetRentRaw.substring(proratedPetRentRaw.indexOf("Tenant will pay Landlord $")+"Tenant will pay Landlord $".length());//,proratedPetRentRaw.indexOf(AppConfig.AR_proratedPetRent_After));
			    if(Tulsa_PropertyWare.proratedPetRent.matches(".*[a-zA-Z]+.*"))
			    {
			    	Tulsa_PropertyWare.proratedPetRent = "Error";
			    }
			    }
			    catch(Exception e)
			    {
			   
			    Tulsa_PropertyWare.proratedPetRent = "Error";	
			    e.printStackTrace();
			    }
	    	  System.out.println("Prorated Pet Rent = "+Tulsa_PropertyWare.proratedPetRent.trim());
	    	
	    	try
		    {
	    		 Tulsa_PropertyWare.petRent = text.substring(text.indexOf(Tulsa_PDFAppConfig.AB_petRent_Prior)+Tulsa_PDFAppConfig.AB_petRent_Prior.length()).trim().split(" ")[0];
	    		 if(Tulsa_PropertyWare.petRent.contains(",for"))
	    		 {
	    			 Tulsa_PropertyWare.petRent = Tulsa_PropertyWare.petRent.split(",")[0].trim();
	    		 }
	    		 else
	    		 {
		    		 if(Tulsa_PropertyWare.petRent.matches(".*[a-zA-Z]+.*")==true)
		    			 Tulsa_PropertyWare.petRent = text.substring(text.indexOf(Tulsa_PDFAppConfig.AB_petRent1_Prior)+Tulsa_PDFAppConfig.AB_petRent1_Prior.length()).trim().split(" ")[0];
		    		 else 
		    		 Tulsa_PropertyWare.petRent = RunnerClass.extractNumber(Tulsa_PropertyWare.petRent);
	    		 }
		    }
	    	catch(Exception e)
		    {
	    		try
	    		{
	    			e.printStackTrace();
	    			Tulsa_PropertyWare.petRent = text.substring(text.indexOf(Tulsa_PDFAppConfig.AB_petRent1_Prior)+Tulsa_PDFAppConfig.AB_petRent1_Prior.length()).trim().split(" ")[0];
//					 System.out.println("Pet rent = "+Tulsa_PropertyWare.petRent.trim());
	    			if(Tulsa_PropertyWare.petRent.matches(".*[a-zA-Z]+.*"))
	    		    {
	    		    	Tulsa_PropertyWare.petRent = "Error";
	    		    }
	    		}
	    		
	    		catch(Exception e1)
			    {
			    	Tulsa_PropertyWare.petRent = "Error";  
			    	e1.printStackTrace();
			    }
		    }
	    	System.out.println("Pet rent = "+Tulsa_PropertyWare.petRent.trim());
		    	//Tulsa_PropertyWare.petRent = "Error";  
		    	//e.printStackTrace();
		   /* 
	    	try
    		{
    			//String petFeeRaw1 = text.substring(text.indexOf(Tulsa_PDFAppConfig.AB_petFee_Prior));
    			Tulsa_PropertyWare.petFee = text.substring(text.indexOf(Tulsa_PDFAppConfig.AB_petFee_Prior)+Tulsa_PDFAppConfig.AB_petFee_Prior.length()).trim().split(" ")[0].trim();
    			//Tulsa_PropertyWare.petFee =  petFeeRaw[petFeeRaw.length-2].trim();
    			//if(Tulsa_PropertyWare.petFee.matches(".*[a-zA-Z]+.*"))
    			//{
    				//Tulsa_PropertyWare.petFee = text.substring(text.indexOf(Tulsa_PDFAppConfig.AB_petFee2_Prior)+Tulsa_PDFAppConfig.AB_petFee2_Prior.length()).trim().split(" ")[0].trim();
    			//}
    			//System.out.println(petFeeRaw.length);
    		}
    		
    		catch(Exception e1)
		    {
		    	Tulsa_PropertyWare.petFee = "Error";  
		    	e1.printStackTrace();
		    }
	    	System.out.println("Pet Fee = "+Tulsa_PropertyWare.petFee);
	    	*/
	    	// Get text between Type: word
	    	
	    	String typeSubString = text.substring(text.indexOf(Tulsa_PDFAppConfig.AB_typeWord_Prior)+Tulsa_PDFAppConfig.AB_typeWord_Prior.length(),text.indexOf(Tulsa_PDFAppConfig.AB_typeWord_After));
	    	
	    	String newText = typeSubString.replace("Type:","");
		    Tulsa_PropertyWare.countOfTypeWordInText = ((typeSubString.length() - newText.length())/"Type:".length());
		    System.out.println("Type: occurences = "+Tulsa_PropertyWare.countOfTypeWordInText);
		    
		    Tulsa_PropertyWare.petType = new ArrayList();
		    Tulsa_PropertyWare.petBreed = new ArrayList();
		    Tulsa_PropertyWare.petWeight = new ArrayList();
		    for(int i =0;i<Tulsa_PropertyWare.countOfTypeWordInText;i++)
		    {
		    	String type = typeSubString.substring(RunnerClass.nthOccurrence(typeSubString, "Type:", i+1)+Tulsa_PDFAppConfig.AB_pet1Type_Prior.length(),RunnerClass.nthOccurrence(typeSubString, "Breed:", i+1)).trim();
		    	if(type.contains("N/A")||type.contains("n/a"))
		    		break;
		    	System.out.println(type);
		    	Tulsa_PropertyWare.petType.add(type);
		    	int pet1Breedindex1 = RunnerClass.nthOccurrence(typeSubString, "Breed:", i+1)+"Breed:".length()+1;
			    String subString = typeSubString.substring(pet1Breedindex1);
			    //int pet1Breedindex2 = RunnerClass.nthOccurrence(subString,"Name:",i+1);
			   // System.out.println("Index 2 = "+(index2+index1));
			    String breed = subString.split("Name:")[0].trim();//typeSubString.substring(pet1Breedindex1,(pet1Breedindex2+pet1Breedindex1));
			    System.out.println(breed);
			    Tulsa_PropertyWare.petBreed.add(breed);
			    int pet1Weightindex1 = RunnerClass.nthOccurrence(typeSubString, "Weight:", i+1)+"Weight:".length()+1;
			    String pet1WeightSubstring = typeSubString.substring(pet1Weightindex1);
			    //int pet1WeightIndex2 = RunnerClass.nthOccurrence(pet1WeightSubstring,"Age:",i+1);
			   // System.out.println("Index 2 = "+(index2+index1));
			    String weight = pet1WeightSubstring.split("Age:")[0].trim(); //typeSubString.substring(pet1Weightindex1,(pet1WeightIndex2+pet1Weightindex1));
			    System.out.println(weight);
			    Tulsa_PropertyWare.petWeight.add(weight);
		    }
		    
		    
		    
		    try
		    {
		    	Tulsa_PropertyWare.petOneTimeNonRefundableFee = text.substring(text.indexOf(Tulsa_PDFAppConfig.AB_petFeeOneTime_Prior)+Tulsa_PDFAppConfig.AB_petFeeOneTime_Prior.length()).split(" ")[0];//,text.indexOf(Tulsa_PDFAppConfig.AB_petFeeOneTime_After));
		    	if(Tulsa_PropertyWare.petOneTimeNonRefundableFee.matches(".*[a-zA-Z]+.*"))
    		    {
    		    	Tulsa_PropertyWare.petOneTimeNonRefundableFee = "Error";
    		    }
		    }
		    catch(Exception e)
		    {
		    	Tulsa_PropertyWare.petOneTimeNonRefundableFee =  "Error";
		    	e.printStackTrace();
		    }  
		    System.out.println("pet one time non refundable = "+Tulsa_PropertyWare.petOneTimeNonRefundableFee.trim());
		   
	    }
	    
	    //Service Animal Addendum check
	    try
	    {
	    if(text.contains(Tulsa_AppConfig.serviceAnimalText))
	    {
	    	Tulsa_PropertyWare.serviceAnimalFlag = true;
    		System.out.println("Service Animal Addendum is available");
    		
            String typeSubString = text.substring(text.indexOf(Tulsa_PDFAppConfig.AB_serviceAnimal_typeWord_Prior)+Tulsa_PDFAppConfig.AB_serviceAnimal_typeWord_Prior.length(),text.indexOf(Tulsa_PDFAppConfig.AB_serviceAnimal_typeWord_After));
	    	
	    	String newText = typeSubString.replace("Type:","");
		    int  countOftypeWords_ServiceAnimal = ((typeSubString.length() - newText.length())/"Type:".length());
		    System.out.println("Service Animal - Type: occurences = "+countOftypeWords_ServiceAnimal);
		    
		    Tulsa_PropertyWare.serviceAnimalPetType = new ArrayList();
		    Tulsa_PropertyWare.serviceAnimalPetBreed = new ArrayList();
		    Tulsa_PropertyWare.serviceAnimalPetWeight = new ArrayList();
		    for(int i =0;i<countOftypeWords_ServiceAnimal;i++)
		    {
		    	String type = typeSubString.substring(RunnerClass.nthOccurrence(typeSubString, "Type:", i+1)+Tulsa_PDFAppConfig.AB_pet1Type_Prior.length(),RunnerClass.nthOccurrence(typeSubString, "Breed:", i+1)).trim();
		    	if(type.contains("N/A")||type.contains("n/a"))
		    		break;
		    	System.out.println(type);
		    	Tulsa_PropertyWare.serviceAnimalPetType.add(type);
		    	int pet1Breedindex1 = RunnerClass.nthOccurrence(typeSubString, "Breed:", i+1)+"Breed:".length()+1;
			    String subString = typeSubString.substring(pet1Breedindex1);
			    //int pet1Breedindex2 = RunnerClass.nthOccurrence(subString,"Name:",i+1);
			   // System.out.println("Index 2 = "+(index2+index1));
			    String breed = subString.split("Name:")[0].trim();//typeSubString.substring(pet1Breedindex1,(pet1Breedindex2+pet1Breedindex1));
			    System.out.println(breed);
			    Tulsa_PropertyWare.serviceAnimalPetBreed.add(breed);
			    int pet1Weightindex1 = RunnerClass.nthOccurrence(typeSubString, "Weight:", i+1)+"Weight:".length()+1;
			    String pet1WeightSubstring = typeSubString.substring(pet1Weightindex1);
			    //int pet1WeightIndex2 = RunnerClass.nthOccurrence(pet1WeightSubstring,"Age:",i+1);
			   // System.out.println("Index 2 = "+(index2+index1));
			    String weight = pet1WeightSubstring.split("Age:")[0].trim(); //typeSubString.substring(pet1Weightindex1,(pet1WeightIndex2+pet1Weightindex1));
			    System.out.println(weight);
			    Tulsa_PropertyWare.serviceAnimalPetWeight.add(weight);
		    }
    		
    		
	    }
	    }
	    catch(Exception e)
	    {
	    	Tulsa_PropertyWare.serviceAnimalFlag = false;
	    }
	    //Concession Addendum
	    
	    try
	    {
	    	if(text.contains(Tulsa_PDFAppConfig.concessionAddendumText))
	    	{
	    		Tulsa_PropertyWare.concessionAddendumFlag = true;
	    		System.out.println("Concession Addendum is available");
	    	}
	    }
	    catch(Exception e)
	    {}
		return true;
	    
	 // document.close();
	  //return true;
    }
	public static boolean lateFeeRule()
	{
		String lateFeeRuleText ="";
		try
		{
		 lateFeeRuleText = text.substring(text.indexOf(Tulsa_PDFAppConfig.lateFeeRuleText_Prior)+Tulsa_PDFAppConfig.lateFeeRuleText_Prior.length(),text.indexOf(Tulsa_PDFAppConfig.lateFeeRuleText_After));
		}
		catch(Exception e)
		{
			try
			{
			lateFeeRuleText = text.substring(text.indexOf(Tulsa_PDFAppConfig.lateFeeRuleText_Prior)+Tulsa_PDFAppConfig.lateFeeRuleText_Prior.length(),text.indexOf(Tulsa_PDFAppConfig.lateFeeRuleText_After2));
			}
			catch(Exception e2)
			{
				try
				{
				lateFeeRuleText = text.substring(text.indexOf(Tulsa_PDFAppConfig.lateFeeRuleText_Prior3)+Tulsa_PDFAppConfig.lateFeeRuleText_Prior3.length(),text.indexOf(Tulsa_PDFAppConfig.lateFeeRuleText_After3));
				}
				catch(Exception e3)
				{
				return false;
				}
			}
		}
		if(lateFeeRuleText.contains(Tulsa_PDFAppConfig.lateFeeRule_whicheverIsGreater))
		{
			RunnerClass.lateFeeRuleType = "GreaterOfFlatFeeOrPercentage";
			RunnerClass.lateFeeType = "GreaterOfFlatFeeOrPercentage";
			//Tulsa_PropertyWare.lateFeeType ="Greater of Flat Fee or Percentage"; 
		//Late charge day
			try
			{
		   // Tulsa_PropertyWare.lateChargeDay =  lateFeeRuleText.substring(lateFeeRuleText.indexOf(Tulsa_PDFAppConfig.lateFeeRule_whicheverIsGreater_dueDay_Prior)+Tulsa_PDFAppConfig.lateFeeRule_whicheverIsGreater_dueDay_Prior.length()).trim().split(" ")[0];
				Tulsa_PropertyWare.lateChargeDay =  lateFeeRuleText.split(Tulsa_PDFAppConfig.lateFeeRule_whicheverIsGreater_dueDay_After)[0].trim();
				Tulsa_PropertyWare.lateChargeDay =Tulsa_PropertyWare.lateChargeDay.substring(Tulsa_PropertyWare.lateChargeDay.lastIndexOf(" ")+1);
		    Tulsa_PropertyWare.lateChargeDay =  Tulsa_PropertyWare.lateChargeDay.replaceAll("[^0-9]", "");
			}
			catch(Exception e)
			{
				Tulsa_PropertyWare.lateChargeDay = "Error";
			}
         System.out.println("Late Charge Day = "+Tulsa_PropertyWare.lateChargeDay);
			RunnerClass.dueDay_GreaterOf = Tulsa_PropertyWare.lateChargeDay;
		//Late Fee Percentage
			try
			{
		    Tulsa_PropertyWare.lateFeePercentage =  lateFeeRuleText.substring(lateFeeRuleText.indexOf(Tulsa_PDFAppConfig.lateFeeRule_whicheverIsGreater_lateFeePercentage)+Tulsa_PDFAppConfig.lateFeeRule_whicheverIsGreater_lateFeePercentage.length()).trim().split(" ")[0];
		    Tulsa_PropertyWare.lateFeePercentage = Tulsa_PropertyWare.lateFeePercentage.replaceAll("[^0-9]", "");
			}
			catch(Exception e)
			{
				Tulsa_PropertyWare.lateFeePercentage = "Error";
			}
         System.out.println("Late Fee Percentage = "+Tulsa_PropertyWare.lateFeePercentage);
         RunnerClass.percentage = Tulsa_PropertyWare.lateFeePercentage;
         //Late Fee Amount
         try
         {
         String lateFeeAmount  = lateFeeRuleText.substring(lateFeeRuleText.indexOf(Tulsa_PDFAppConfig.lateFeeRule_whicheverIsGreater_lateFeeAmount)+Tulsa_PDFAppConfig.lateFeeRule_whicheverIsGreater_lateFeeAmount.length()).trim().split(" ")[0];
         Tulsa_PropertyWare.flatFeeAmount = lateFeeAmount.replaceAll("[^.0-9]", "");
         }
         catch(Exception e)
         {
        	 Tulsa_PropertyWare.flatFeeAmount ="Error";
         }
         System.out.println("Late Fee Amount = "+Tulsa_PropertyWare.flatFeeAmount);
        RunnerClass.flatFee = Tulsa_PropertyWare.flatFeeAmount;
         return true;
		}
		else 
		if(lateFeeRuleText.contains(Tulsa_PDFAppConfig.lateFeeRule_mayNotExceedMoreThan30Days))
		{
			RunnerClass.lateFeeRuleType = "initialFeePluPerDayFee";
			//RunnerClass.lateFeeRuleType = "Initial Fee + Per Day Fee";
			
			Tulsa_PropertyWare.lateFeeType ="initialFeePluPerDayFee"; 
	         try
	 	    {
	 		    Tulsa_PropertyWare.lateChargeFee = text.substring(text.indexOf(Tulsa_PDFAppConfig.AB_lateFee_Prior)+Tulsa_PDFAppConfig.AB_lateFee_Prior.length()).trim().split(" ")[0];
	 		    //Tulsa_PropertyWare.lateChargeFee =  Tulsa_PropertyWare.lateChargeFee.substring(0,Tulsa_PropertyWare.lateChargeFee.length()-1);
	 	    }
	 	    catch(Exception e)
	 	    {
	 		    Tulsa_PropertyWare.lateChargeFee ="Error";	
	 		    e.printStackTrace();
	 	    }
	 	    System.out.println("Late Charge Fee = "+Tulsa_PropertyWare.lateChargeFee.trim());
	 	   RunnerClass.initialFeeAmount = Tulsa_PropertyWare.lateChargeFee;
	 	    //Per Day Fee
	 	    try
	 	    {
	 	    	Tulsa_PropertyWare.lateFeeChargePerDay = text.substring(text.indexOf(Tulsa_PDFAppConfig.AB_additionalLateChargesPerDay_Prior)+Tulsa_PDFAppConfig.AB_additionalLateChargesPerDay_Prior.length()).split(" ")[0].trim();//,text.indexOf(Tulsa_PDFAppConfig.AB_additionalLateChargesPerDay_After));
	 	    }
	 	    catch(Exception e)
	 	    {
	 	    	Tulsa_PropertyWare.lateFeeChargePerDay =  "Error";	
	 	    	e.printStackTrace();
	 	    }
	 	    System.out.println("Per Day Fee = "+Tulsa_PropertyWare.lateFeeChargePerDay.trim());
	 	    RunnerClass.perDayFeeAmount = Tulsa_PropertyWare.lateFeeChargePerDay;
	 	    //Additional Late Charges Limit
	 	    try
	 	    {
	 	    	Tulsa_PropertyWare.additionalLateChargesLimit = text.substring(text.indexOf(Tulsa_PDFAppConfig.AB_additionalLateChargesLimit_Prior)+Tulsa_PDFAppConfig.AB_additionalLateChargesLimit_Prior.length()).trim().split(" ")[0]; //,text.indexOf(Tulsa_PDFAppConfig.AB_additionalLateChargesLimit_After));
	 	    }
	 	    catch(Exception e)
	 	    {
	 	    	Tulsa_PropertyWare.additionalLateChargesLimit =  "Error";	
	 	    	e.printStackTrace();
	 	    }
	 	    System.out.println("additional Late Charges Limit = "+Tulsa_PropertyWare.additionalLateChargesLimit.trim());
	 	    RunnerClass.additionalLateChargesLimit = Tulsa_PropertyWare.additionalLateChargesLimit;
	 	 //Late Charge Day
			try
	 	    {
			Tulsa_PropertyWare.lateChargeDay = lateFeeRuleText.substring(lateFeeRuleText.indexOf("p.m. on the ")+"p.m. on the ".length()).trim().split(" ")[0];
			Tulsa_PropertyWare.lateChargeDay = Tulsa_PropertyWare.lateChargeDay.replaceAll("[^0-9]", "");
	 	    }
			catch(Exception e)
	 	    {
	 	    	Tulsa_PropertyWare.lateChargeDay =  "Error";	
	 	    	e.printStackTrace();
	 	    }
	 	    System.out.println("Late Charge Due Day = "+Tulsa_PropertyWare.lateChargeDay.trim());
	 	    RunnerClass.dueDay_initialFee = Tulsa_PropertyWare.lateChargeDay;
	 	   return true;
		}
		else if(lateFeeRuleText.contains(Tulsa_PDFAppConfig.lateFeeRule_mayNotExceedAmount)||lateFeeRuleText.contains(Tulsa_PDFAppConfig.lateFeeRule_mayNotExceed375))
			{
			RunnerClass.lateFeeRuleType = "initialFeePluPerDayFee";
			//RunnerClass.lateFeeRuleType = "Initial Fee + Per Day Fee";
			
			//Late Charge Day
			try
	 	    {
			Tulsa_PropertyWare.lateChargeDay = lateFeeRuleText.substring(lateFeeRuleText.indexOf("an initial late charge on the")+"an initial late charge on the".length()).trim().split(" ")[0];
			Tulsa_PropertyWare.lateChargeDay = Tulsa_PropertyWare.lateChargeDay.replaceAll("[^0-9]", "");
	 	    }
			catch(Exception e)
	 	    {
	 	    	Tulsa_PropertyWare.lateChargeDay =  "Error";	
	 	    	e.printStackTrace();
	 	    }
	 	    System.out.println("Late Charge Due Day = "+Tulsa_PropertyWare.lateChargeDay.trim());
	 	   RunnerClass.dueDay_initialFee = Tulsa_PropertyWare.lateChargeDay;
	 	    // initial Late Charge
	 	   try
	 	    {
			Tulsa_PropertyWare.lateChargeFee = lateFeeRuleText.substring(lateFeeRuleText.indexOf("day of the month equal to $")+"day of the month equal to $".length()).trim().split(" ")[0];
			Tulsa_PropertyWare.lateChargeFee = Tulsa_PropertyWare.lateChargeFee.replaceAll("[^0-9.]", "").substring(0, Tulsa_PropertyWare.lateChargeFee.length()-1);
	 	    }
			catch(Exception e)
	 	    {
	 	    	Tulsa_PropertyWare.lateChargeFee =  "Error";	
	 	    	e.printStackTrace();
	 	    }
	 	    System.out.println("Late Charge Fee = "+Tulsa_PropertyWare.lateChargeFee.trim());
	 	   RunnerClass.initialFeeAmount = Tulsa_PropertyWare.lateChargeFee;
	 	    // Additional Late Charges
	 	   try
	 	    {
			Tulsa_PropertyWare.additionalLateCharges = lateFeeRuleText.substring(lateFeeRuleText.indexOf("additional late charges of $")+"additional late charges of $".length()).trim().split(" ")[0];
			Tulsa_PropertyWare.additionalLateCharges = Tulsa_PropertyWare.additionalLateCharges.replaceAll("[^0-9.]", "");
	 	    }
			catch(Exception e)
	 	    {
	 	    	Tulsa_PropertyWare.additionalLateCharges =  "Error";	
	 	    	e.printStackTrace();
	 	    }
	 	    System.out.println("Additional Late Charges = "+Tulsa_PropertyWare.additionalLateCharges.trim());
	 	   RunnerClass.perDayFeeAmount = Tulsa_PropertyWare.additionalLateCharges;
	 	    //Additional Late Charges Limit
	 	   try
	 	    {
			Tulsa_PropertyWare.additionalLateChargesLimit = lateFeeRuleText.substring(lateFeeRuleText.indexOf("Additional late charges may not exceed $")+"Additional late charges may not exceed $".length()).trim().split(" ")[0];
			Tulsa_PropertyWare.additionalLateChargesLimit = Tulsa_PropertyWare.additionalLateChargesLimit.replaceAll("[^0-9.]", "");
	 	    }
			catch(Exception e)
	 	    {
	 	    	Tulsa_PropertyWare.additionalLateChargesLimit =  "Error";	
	 	    	e.printStackTrace();
	 	    }
	 	    System.out.println("Additional Late Charges Limit = "+Tulsa_PropertyWare.additionalLateChargesLimit.trim());
	 	   RunnerClass.additionalLateChargesLimit = Tulsa_PropertyWare.additionalLateChargesLimit;
			return true;
			}
		else 
			if(lateFeeRuleText.contains(Tulsa_PDFAppConfig.lateFeeRule_totalDelinquentRentDueToTheTenantAccount))
			{
				RunnerClass.lateFeeRuleType = "GreaterOfFlatFeeOrPercentage";
				RunnerClass.lateFeeType = "GreaterOfFlatFeeOrPercentage";
				
			//Late Charge Day
			try
	 	    {
			Tulsa_PropertyWare.lateChargeDay = lateFeeRuleText.substring(lateFeeRuleText.indexOf("place of payment on the ")+"place of payment on the ".length()).trim().split(" ")[0];
			Tulsa_PropertyWare.lateChargeDay = Tulsa_PropertyWare.lateChargeDay.replaceAll("[^0-9]", "");
	 	    }
			catch(Exception e)
	 	    {
	 	    	Tulsa_PropertyWare.lateChargeDay =  "Error";	
	 	    	e.printStackTrace();
	 	    }
	 	    System.out.println("Late Charge Due Day = "+Tulsa_PropertyWare.lateChargeDay.trim());
	 	   RunnerClass.dueDay_GreaterOf = Tulsa_PropertyWare.lateChargeDay;
	 	    // initial Late Charge
	 	   try
	 	    {
			Tulsa_PropertyWare.lateChargeFee = lateFeeRuleText.substring(lateFeeRuleText.indexOf("an initial late charge equal to ")+"an initial late charge equal to ".length()).trim().split(" ")[0];
			//Tulsa_PropertyWare.lateChargeFee = Tulsa_PropertyWare.lateChargeFee.replaceAll("[^0-9.]", "");
	 	    }
			catch(Exception e)
	 	    {
	 	    	Tulsa_PropertyWare.lateChargeFee =  "Error";	
	 	    	e.printStackTrace();
	 	    }
	 	    System.out.println("Late Charge Fee = "+Tulsa_PropertyWare.lateChargeFee.trim());
	 	   RunnerClass.percentage = Tulsa_PropertyWare.lateChargeFee;
	 	   /*
	 	    // Additional Late Charges
	 	   try
	 	    {
			Tulsa_PropertyWare.additionalLateCharges = lateFeeRuleText.substring(lateFeeRuleText.indexOf("and additional late charge of $")+"and additional late charge of $".length()).trim().split(" ")[0];
			Tulsa_PropertyWare.additionalLateCharges = Tulsa_PropertyWare.additionalLateCharges.replaceAll("[^0-9.]", "");
	 	    }
			catch(Exception e)
	 	    {
	 	    	Tulsa_PropertyWare.additionalLateCharges =  "Error";	
	 	    	e.printStackTrace();
	 	    }
	 	    System.out.println("Additional Late Charges = "+Tulsa_PropertyWare.additionalLateCharges.trim());
	 	    RunnerClass.maximumAmount = Tulsa_PropertyWare.additionalLateCharges;
	 	    //Additional Late Charges Limit
	 	   try
	 	    {
			Tulsa_PropertyWare.additionalLateChargesLimit = lateFeeRuleText.substring(lateFeeRuleText.indexOf("(initial and additional) may not exceed $")+"(initial and additional) may not exceed $".length()).trim().split(" ")[0];
			Tulsa_PropertyWare.additionalLateChargesLimit = Tulsa_PropertyWare.additionalLateChargesLimit.replaceAll("[^0-9.]", "");
	 	    }
			catch(Exception e)
	 	    {
	 	    	Tulsa_PropertyWare.additionalLateChargesLimit =  "Error";	
	 	    	e.printStackTrace();
	 	    }
	 	    System.out.println("Additional Late Charges Limit = "+Tulsa_PropertyWare.additionalLateChargesLimit.trim());
	 	    RunnerClass.additionalLateChargesLimit = Tulsa_PropertyWare.additionalLateChargesLimit;
			return true;
			}
			else
		   {
			Tulsa_PropertyWare.lateFeeType ="";
		   }
		   */
			}
		return true;		
	}




}
