package SanAntonio;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import mainPackage.InsertDataIntoDatabase;
import mainPackage.RunnerClass;

public class SA_ExtractDataFromPDF 
{
	public static boolean petFlag;
	public static String text="";
	public boolean arizona() throws Exception
	//public static void main(String args[]) throws Exception
	{
		
		SA_PropertyWare.petFlag = false;
		File file = RunnerClass.getLastModified();
		//File file = new File("C:\\Gopi\\Projects\\Property ware\\Lease Close Outs\\PDFS\\5503SeahorseDr\\Lease_323_324_5503_Seahorse_Dr_SATX_Garza-L.pdf");
		FileInputStream fis = new FileInputStream(file);
		SA_RunnerClass.document = PDDocument.load(fis);
	    text = new PDFTextStripper().getText(SA_RunnerClass.document);
	    SA_PropertyWare.pdfText  = text;
	    if(!text.contains(SA_AppConfig.PDFFormatConfirmationText)) 
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
	    	SA_PropertyWare.commensementDate = text.substring(text.indexOf(SA_PDFAppConfig.AB_commencementDate_Prior)+SA_PDFAppConfig.AB_commencementDate_Prior.length(),text.indexOf(SA_PDFAppConfig.AB_expirationDate_Prior));
	    	SA_PropertyWare.commensementDate = SA_PropertyWare.commensementDate.trim().replaceAll(" +", " ");
	    }
	    catch(Exception e)
	    {
	    	SA_PropertyWare.commensementDate = "Error";
	    	e.printStackTrace();
	    }
	    System.out.println("Commensement Date = "+SA_PropertyWare.commensementDate);
	   try
	    {
		   String expirationDateWaw = text.substring(text.indexOf(SA_PDFAppConfig.AB_expirationDate_Prior)+SA_PDFAppConfig.AB_expirationDate_Prior.length());
		   SA_PropertyWare.expirationDate =expirationDateWaw.trim().split(" ")[0]+" "+expirationDateWaw.trim().split(" ")[1]+" "+expirationDateWaw.trim().split(" ")[2];
		   SA_PropertyWare.expirationDate = SA_PropertyWare.expirationDate.trim().replaceAll(" +", " ");
	    }
	    catch(Exception e)
	    {
	    	 SA_PropertyWare.expirationDate = "Error";
	    	 e.printStackTrace();
	    }
	   System.out.println("Expiration Date = "+SA_PropertyWare.expirationDate);
	   try
	    {
		    SA_PropertyWare.proratedRent = text.substring(text.indexOf(SA_PDFAppConfig.AB_proratedRent_Prior)+SA_PDFAppConfig.AB_proratedRent_Prior.length(),text.indexOf(SA_PDFAppConfig.AB_proratedRent_After));
		    if(SA_PropertyWare.proratedRent.matches(".*[a-zA-Z]+.*"))
		    {
		    	SA_PropertyWare.proratedRent = "Error";
		    }
		    		
	    }
	    catch(Exception e)
	    {
	    	SA_PropertyWare.proratedRent = "Error";
	    	e.printStackTrace();
	    }
	   System.out.println("Prorated Rent = "+SA_PropertyWare.proratedRent);
	    try
	    {
		    SA_PropertyWare.proratedRentDate = text.substring(text.indexOf(SA_PDFAppConfig.AB_proratedRentDate_Prior)+SA_PDFAppConfig.AB_proratedRentDate_Prior.length(),text.indexOf(SA_PDFAppConfig.AB_proratedRentDate_After)).trim();
	    }
	    catch(Exception e)
	    {
	    	SA_PropertyWare.proratedRentDate = "Error";
	    	e.printStackTrace();
	    }
	    System.out.println("Prorated Rent Date= "+SA_PropertyWare.proratedRentDate.trim());
	    /*
	    try
	    {
		    SA_PropertyWare.monthlyRentDate = text.substring(text.indexOf(SA_PDFAppConfig.AB_fullRentDate_Prior)+SA_PDFAppConfig.AB_fullRentDate_Prior.length(),text.indexOf(SA_PDFAppConfig.AB_fullRentDate_After));
		    System.out.println("Monthly Rent Date= "+SA_PropertyWare.monthlyRentDate.trim());
	    }
	    catch(Exception e)
	    {
	    	try
	    	{
	    		SA_PropertyWare.monthlyRentDate = text.substring(text.indexOf(SA_PDFAppConfig.AB_fullRentDate_Prior)+SA_PDFAppConfig.AB_fullRentDate_Prior.length(),text.indexOf(SA_PDFAppConfig.AB_fullRentDate1_After));
			   	System.out.println("Monthly Rent Date= "+SA_PropertyWare.monthlyRentDate.trim());
	    	}
	    	catch(Exception e1)
		    {
		    	SA_PropertyWare.monthlyRentDate = "Error";  
		    	e1.printStackTrace();
		    }
	    }*/
	    try
	    {
		    SA_PropertyWare.monthlyRent = text.substring(text.indexOf(SA_PDFAppConfig.AB_fullRent_Prior)+SA_PDFAppConfig.AB_fullRent_Prior.length()).trim().split(" ")[0].trim();//,text.indexOf(SA_PDFAppConfig.AB_fullRent_After)).substring(1).replaceAll("[^.0-9]", "");;
		    if(RunnerClass.onlyDigits(SA_PropertyWare.monthlyRent.replace(".", "").replace(",", ""))==false)
		    {
		    	SA_PropertyWare.monthlyRent = text.substring(text.indexOf(SA_PDFAppConfig.AB_fullRent2_Prior)+SA_PDFAppConfig.AB_fullRent2_Prior.length()).trim().split(" ")[0].trim();
		    }
		    if(SA_PropertyWare.monthlyRent.contains("*"))
		    {
		    	SA_PropertyWare.monthlyRent = SA_PropertyWare.monthlyRent.replace("*","");
		    }
		    if(SA_PropertyWare.monthlyRent.matches(".*[a-zA-Z]+.*"))
		    {
		    	SA_PropertyWare.monthlyRent = "Error";
		    }
		    if(SA_PropertyWare.monthlyRent.endsWith(","))
		    {
		    	SA_PropertyWare.monthlyRent = SA_PropertyWare.monthlyRent.substring(0,SA_PropertyWare.monthlyRent.length()-1);
		    }
	    }
	    catch(Exception e)
	    {
	    	 SA_PropertyWare.monthlyRent = "Error";
	    	 e.printStackTrace();
	    }
	    System.out.println("Monthly Rent "+SA_PropertyWare.monthlyRent.trim());
	    try
	    {
		    SA_PropertyWare.adminFee = text.substring(text.indexOf(SA_PDFAppConfig.AB_adminFee_Prior)+SA_PDFAppConfig.AB_adminFee_Prior.length()).trim().split(" ")[0];
		    if(SA_PropertyWare.adminFee.matches(".*[a-zA-Z]+.*"))
		    {
		    	try
			    {
				    SA_PropertyWare.adminFee = text.substring(text.indexOf(SA_PDFAppConfig.AB_adminFee_Prior2)+SA_PDFAppConfig.AB_adminFee_Prior2.length()).trim().split(" ")[0];
				    if(SA_PropertyWare.adminFee.matches(".*[a-zA-Z]+.*"))
				    {
				    	SA_PropertyWare.adminFee = "Error";
				    }
			    }
			    catch(Exception e)
			    {
				    SA_PropertyWare.adminFee = "Error";
				    e.printStackTrace();
			    }
		    	
		    }
	    }
	    catch(Exception e)
	    {
	    	SA_PropertyWare.adminFee = "Error";
	    }
	    System.out.println("Admin Fee = "+SA_PropertyWare.adminFee.trim());
	    
	  //Resident Benefits Package 
	    if(text.contains(SA_PDFAppConfig.residentBenefitsPackageAddendumCheck))
	    {
	    	SA_PropertyWare.residentBenefitsPackageAvailabilityCheck = true;
	    	 try
	 	    {
	 		    SA_PropertyWare.residentBenefitsPackage = text.substring(text.indexOf(SA_PDFAppConfig.AB1_residentBenefitsPackage_Prior)+SA_PDFAppConfig.AB1_residentBenefitsPackage_Prior.length()).split(" ")[0].replaceAll("[^0-9a-zA-Z.]", "");
	 		    if(SA_PropertyWare.residentBenefitsPackage.matches(".*[a-zA-Z]+.*"))
	 		    {
	 		    	try
			 	    {
			 		    SA_PropertyWare.residentBenefitsPackage = text.substring(text.indexOf(SA_PDFAppConfig.AB1_residentBenefitsPackage_Prior)+SA_PDFAppConfig.AB1_residentBenefitsPackage_Prior.length());
			 		    SA_PropertyWare.residentBenefitsPackage = SA_PropertyWare.residentBenefitsPackage.substring(0, SA_PropertyWare.residentBenefitsPackage.indexOf("/month")).trim();
			 		    if(SA_PropertyWare.residentBenefitsPackage.matches(".*[a-zA-Z]+.*"))
			 		    {
			 		    	SA_PropertyWare.residentBenefitsPackage = "Error";
			 		    }
			 	    }
			 	    catch(Exception e2)
			 	    {
			 		    SA_PropertyWare.residentBenefitsPackage = "Error";
			 		    e2.printStackTrace();
			 	    }
	 		    }
	 	    }
	 	    catch(Exception e)
	 	    {
	 	    	 SA_PropertyWare.residentBenefitsPackage = "Error";
		 		    e.printStackTrace();
	 	    }
	    	 System.out.println("Resident Benefits Package  = "+SA_PropertyWare.residentBenefitsPackage.trim());
	    	//SA_PDFAppConfig.AB1_residentBenefitsPackage_Prior
	    }
	    else
	    {
		    if(text.contains(SA_PDFAppConfig_Format2.HVACFilterAddendumTextAvailabilityCheck)==true)
		    {
		    	SA_PropertyWare.HVACFilterFlag =true;
		    }
		    else
		    {
		    try
		    {
			   String[] airFilterFeeArray = text.substring(text.indexOf(SA_PDFAppConfig.AB_airFilterFee_Prior)+SA_PDFAppConfig.AB_airFilterFee_Prior.length()).split(" ");
			   SA_PropertyWare.airFilterFee = airFilterFeeArray[0];
			   if(SA_PropertyWare.airFilterFee.matches(".*[a-zA-Z]+.*"))
			    {
			    	SA_PropertyWare.airFilterFee = "Error";
			    }
		    }
		    catch(Exception e)
		    {
		    SA_PropertyWare.airFilterFee = "Error";
		    e.printStackTrace();
		    }
		    }
		    System.out.println("Air Filter Fee = "+SA_PropertyWare.airFilterFee.trim());
	    }
	    try
	    {
	    	String[] earlyTerminationRaw = text.substring(text.indexOf(SA_PDFAppConfig.AB_earlyTerminationFee_Prior)+SA_PDFAppConfig.AB_earlyTerminationFee_Prior.length()).split(" ");
	    	
		    SA_PropertyWare.earlyTermination = earlyTerminationRaw[0]+earlyTerminationRaw[1]; //text.substring(text.indexOf(SA_PDFAppConfig.AB_earlyTerminationFee_Prior)+SA_PDFAppConfig.AB_earlyTerminationFee_Prior.length(),text.indexOf(SA_PDFAppConfig.AB_earlyTerminationFee_After));
	    }
	    catch(Exception e)
	    {
	    	SA_PropertyWare.earlyTermination = "Error";	
	    	e.printStackTrace();
	    }
	    System.out.println("Early Termination  = "+SA_PropertyWare.earlyTermination.trim());
	    try
	    {
	    	
		    SA_PropertyWare.occupants = text.substring(text.indexOf(SA_PDFAppConfig.AB_occupants_Prior)+SA_PDFAppConfig.AB_occupants_Prior.length(),text.indexOf(SA_PDFAppConfig.AB_occupants_After));
	    }
	    catch(Exception e)
	    {
		    SA_PropertyWare.occupants ="Error";	
		    e.printStackTrace();
	    }
	    System.out.println("Occupants = "+SA_PropertyWare.occupants.trim());
	    
	    //Late charges 
	    //Decide Late Fee Rule
	   SA_ExtractDataFromPDF.lateFeeRule();
	    
	  //Prepayment Charge
  		if(SA_PropertyWare.portfolioType.contains("MCH"))
  		{
  			if(SA_PropertyWare.proratedRent.equalsIgnoreCase("n/a")||SA_PropertyWare.proratedRent.equalsIgnoreCase("Error")||SA_PropertyWare.proratedRent.equalsIgnoreCase(""))
  			{
  				SA_PropertyWare.prepaymentCharge = "Error";
  			}
  			else
  			{
	  		try
	  		{
	  		SA_PropertyWare.prepaymentCharge =String.valueOf(Double.parseDouble(SA_PropertyWare.monthlyRent.trim().replace(",", "")) - Double.parseDouble(SA_PropertyWare.proratedRent.trim().replace(",", ""))); 
	  		}
	  		catch(Exception e)
	  		{
	  			SA_PropertyWare.prepaymentCharge ="Error";
	  		}
	  		}
  			System.out.println("Prepayment Charge = "+SA_PropertyWare.prepaymentCharge);
  		 }
	    petFlag = text.contains(SA_PDFAppConfig.AB_petAgreementAvailabilityCheck);
	    System.out.println("Pet Addendum Available = "+petFlag);
	    if(petFlag ==true)
	    {
	    	SA_PropertyWare.petFlag = true;
	    	try
	    	{
	    	SA_PropertyWare.petSecurityDeposit = text.substring(text.indexOf(SA_PDFAppConfig.AB_securityDeposity_Prior)+SA_PDFAppConfig.AB_securityDeposity_Prior.length(),text.indexOf(SA_PDFAppConfig.AB_securityDeposity_After));
	    	if(SA_PropertyWare.petSecurityDeposit.matches(".*[a-zA-Z]+.*"))
		    {
		    	SA_PropertyWare.petSecurityDeposit = "Error";
		    }
	    	}
	    	catch(Exception e)
	    	{
	    	SA_PropertyWare.petSecurityDeposit = "Error";	
	    	e.printStackTrace();
	    	}
	    	System.out.println("Pet Security Deposit = "+SA_PropertyWare.petSecurityDeposit.trim());
	    	if(RunnerClass.onlyDigits(SA_PropertyWare.petSecurityDeposit.replace(".", ""))==true)
		    {
		    	System.out.println("Security Deposit is checked");
		    }
	    	//TODO Check
	    	  try
			    {
	    		  String proratedPetRaw = "Prorated Pet Rent: On or before "+SA_PropertyWare.commensementDate.trim()+" Tenant will pay Landlord $";
	    		SA_PropertyWare.proratedPetRent = text.substring(text.indexOf(proratedPetRaw)+proratedPetRaw.length()).trim().split(" ")[0];//.replaceAll("[a-ZA-Z,]", "");
			    //AR_PropertyWare.proratedPetRent = proratedPetRentRaw.substring(proratedPetRentRaw.indexOf("Tenant will pay Landlord $")+"Tenant will pay Landlord $".length());//,proratedPetRentRaw.indexOf(AppConfig.AR_proratedPetRent_After));
			    if(SA_PropertyWare.proratedPetRent.matches(".*[a-zA-Z]+.*"))
			    {
			    	SA_PropertyWare.proratedPetRent = "Error";
			    }
			    }
			    catch(Exception e)
			    {
			   
			    SA_PropertyWare.proratedPetRent = "Error";	
			    e.printStackTrace();
			    }
	    	  System.out.println("Prorated Pet Rent = "+SA_PropertyWare.proratedPetRent.trim());
	    	
	    	try
		    {
	    		 SA_PropertyWare.petRent = text.substring(text.indexOf(SA_PDFAppConfig.AB_petRent_Prior)+SA_PDFAppConfig.AB_petRent_Prior.length()).trim().split(" ")[0];
	    		 if(SA_PropertyWare.petRent.contains(",for"))
	    		 {
	    			 SA_PropertyWare.petRent = SA_PropertyWare.petRent.split(",")[0].trim();
	    		 }
	    		 else
	    		 {
		    		 if(SA_PropertyWare.petRent.matches(".*[a-zA-Z]+.*")==true)
		    			 SA_PropertyWare.petRent = text.substring(text.indexOf(SA_PDFAppConfig.AB_petRent1_Prior)+SA_PDFAppConfig.AB_petRent1_Prior.length()).trim().split(" ")[0];
		    		 else 
		    		 SA_PropertyWare.petRent = RunnerClass.extractNumber(SA_PropertyWare.petRent);
	    		 }
		    }
	    	catch(Exception e)
		    {
	    		try
	    		{
	    			e.printStackTrace();
	    			SA_PropertyWare.petRent = text.substring(text.indexOf(SA_PDFAppConfig.AB_petRent1_Prior)+SA_PDFAppConfig.AB_petRent1_Prior.length()).trim().split(" ")[0];
//					 System.out.println("Pet rent = "+SA_PropertyWare.petRent.trim());
	    			if(SA_PropertyWare.petRent.matches(".*[a-zA-Z]+.*"))
	    		    {
	    		    	SA_PropertyWare.petRent = "Error";
	    		    }
	    		}
	    		
	    		catch(Exception e1)
			    {
			    	SA_PropertyWare.petRent = "Error";  
			    	e1.printStackTrace();
			    }
		    }
	    	System.out.println("Pet rent = "+SA_PropertyWare.petRent.trim());
		    	//SA_PropertyWare.petRent = "Error";  
		    	//e.printStackTrace();
		   /* 
	    	try
    		{
    			//String petFeeRaw1 = text.substring(text.indexOf(SA_PDFAppConfig.AB_petFee_Prior));
    			SA_PropertyWare.petFee = text.substring(text.indexOf(SA_PDFAppConfig.AB_petFee_Prior)+SA_PDFAppConfig.AB_petFee_Prior.length()).trim().split(" ")[0].trim();
    			//SA_PropertyWare.petFee =  petFeeRaw[petFeeRaw.length-2].trim();
    			//if(SA_PropertyWare.petFee.matches(".*[a-zA-Z]+.*"))
    			//{
    				//SA_PropertyWare.petFee = text.substring(text.indexOf(SA_PDFAppConfig.AB_petFee2_Prior)+SA_PDFAppConfig.AB_petFee2_Prior.length()).trim().split(" ")[0].trim();
    			//}
    			//System.out.println(petFeeRaw.length);
    		}
    		
    		catch(Exception e1)
		    {
		    	SA_PropertyWare.petFee = "Error";  
		    	e1.printStackTrace();
		    }
	    	System.out.println("Pet Fee = "+SA_PropertyWare.petFee);
	    	*/
	    	// Get text between Type: word
	    	
	    	String typeSubString = text.substring(text.indexOf(SA_PDFAppConfig.AB_typeWord_Prior)+SA_PDFAppConfig.AB_typeWord_Prior.length(),text.indexOf(SA_PDFAppConfig.AB_typeWord_After));
	    	
	    	String newText = typeSubString.replace("Type:","");
		    SA_PropertyWare.countOfTypeWordInText = ((typeSubString.length() - newText.length())/"Type:".length());
		    System.out.println("Type: occurences = "+SA_PropertyWare.countOfTypeWordInText);
		    
		    SA_PropertyWare.petType = new ArrayList();
		    SA_PropertyWare.petBreed = new ArrayList();
		    SA_PropertyWare.petWeight = new ArrayList();
		    for(int i =0;i<SA_PropertyWare.countOfTypeWordInText;i++)
		    {
		    	String type = typeSubString.substring(RunnerClass.nthOccurrence(typeSubString, "Type:", i+1)+SA_PDFAppConfig.AB_pet1Type_Prior.length(),RunnerClass.nthOccurrence(typeSubString, "Breed:", i+1)).trim();
		    	if(type.contains("N/A")||type.contains("n/a"))
		    		break;
		    	System.out.println(type);
		    	SA_PropertyWare.petType.add(type);
		    	int pet1Breedindex1 = RunnerClass.nthOccurrence(typeSubString, "Breed:", i+1)+"Breed:".length()+1;
			    String subString = typeSubString.substring(pet1Breedindex1);
			    //int pet1Breedindex2 = RunnerClass.nthOccurrence(subString,"Name:",i+1);
			   // System.out.println("Index 2 = "+(index2+index1));
			    String breed = subString.split("Name:")[0].trim();//typeSubString.substring(pet1Breedindex1,(pet1Breedindex2+pet1Breedindex1));
			    System.out.println(breed);
			    SA_PropertyWare.petBreed.add(breed);
			    int pet1Weightindex1 = RunnerClass.nthOccurrence(typeSubString, "Weight:", i+1)+"Weight:".length()+1;
			    String pet1WeightSubstring = typeSubString.substring(pet1Weightindex1);
			    //int pet1WeightIndex2 = RunnerClass.nthOccurrence(pet1WeightSubstring,"Age:",i+1);
			   // System.out.println("Index 2 = "+(index2+index1));
			    String weight = pet1WeightSubstring.split("Age:")[0].trim(); //typeSubString.substring(pet1Weightindex1,(pet1WeightIndex2+pet1Weightindex1));
			    System.out.println(weight);
			    SA_PropertyWare.petWeight.add(weight);
		    }
		    
		    
		    
		    try
		    {
		    	SA_PropertyWare.petOneTimeNonRefundableFee = text.substring(text.indexOf(SA_PDFAppConfig.AB_petFeeOneTime_Prior)+SA_PDFAppConfig.AB_petFeeOneTime_Prior.length()).split(" ")[0];//,text.indexOf(SA_PDFAppConfig.AB_petFeeOneTime_After));
		    	if(SA_PropertyWare.petOneTimeNonRefundableFee.matches(".*[a-zA-Z]+.*"))
    		    {
    		    	SA_PropertyWare.petOneTimeNonRefundableFee = "Error";
    		    }
		    }
		    catch(Exception e)
		    {
		    	SA_PropertyWare.petOneTimeNonRefundableFee =  "Error";
		    	e.printStackTrace();
		    }  
		    System.out.println("pet one time non refundable = "+SA_PropertyWare.petOneTimeNonRefundableFee.trim());
		   
	    }
	    
	    //Service Animal Addendum check
	    try
	    {
	    if(text.contains(SA_AppConfig.serviceAnimalText))
	    {
	    	SA_PropertyWare.serviceAnimalFlag = true;
    		System.out.println("Service Animal Addendum is available");
    		
            String typeSubString = text.substring(text.indexOf(SA_PDFAppConfig.AB_serviceAnimal_typeWord_Prior)+SA_PDFAppConfig.AB_serviceAnimal_typeWord_Prior.length(),text.indexOf(SA_PDFAppConfig.AB_serviceAnimal_typeWord_After));
	    	
	    	String newText = typeSubString.replace("Type:","");
		    int  countOftypeWords_ServiceAnimal = ((typeSubString.length() - newText.length())/"Type:".length());
		    System.out.println("Service Animal - Type: occurences = "+countOftypeWords_ServiceAnimal);
		    
		    SA_PropertyWare.serviceAnimalPetType = new ArrayList();
		    SA_PropertyWare.serviceAnimalPetBreed = new ArrayList();
		    SA_PropertyWare.serviceAnimalPetWeight = new ArrayList();
		    for(int i =0;i<countOftypeWords_ServiceAnimal;i++)
		    {
		    	String type = typeSubString.substring(RunnerClass.nthOccurrence(typeSubString, "Type:", i+1)+SA_PDFAppConfig.AB_pet1Type_Prior.length(),RunnerClass.nthOccurrence(typeSubString, "Breed:", i+1)).trim();
		    	if(type.contains("N/A")||type.contains("n/a"))
		    		break;
		    	System.out.println(type);
		    	SA_PropertyWare.serviceAnimalPetType.add(type);
		    	int pet1Breedindex1 = RunnerClass.nthOccurrence(typeSubString, "Breed:", i+1)+"Breed:".length()+1;
			    String subString = typeSubString.substring(pet1Breedindex1);
			    //int pet1Breedindex2 = RunnerClass.nthOccurrence(subString,"Name:",i+1);
			   // System.out.println("Index 2 = "+(index2+index1));
			    String breed = subString.split("Name:")[0].trim();//typeSubString.substring(pet1Breedindex1,(pet1Breedindex2+pet1Breedindex1));
			    System.out.println(breed);
			    SA_PropertyWare.serviceAnimalPetBreed.add(breed);
			    int pet1Weightindex1 = RunnerClass.nthOccurrence(typeSubString, "Weight:", i+1)+"Weight:".length()+1;
			    String pet1WeightSubstring = typeSubString.substring(pet1Weightindex1);
			    //int pet1WeightIndex2 = RunnerClass.nthOccurrence(pet1WeightSubstring,"Age:",i+1);
			   // System.out.println("Index 2 = "+(index2+index1));
			    String weight = pet1WeightSubstring.split("Age:")[0].trim(); //typeSubString.substring(pet1Weightindex1,(pet1WeightIndex2+pet1Weightindex1));
			    System.out.println(weight);
			    SA_PropertyWare.serviceAnimalPetWeight.add(weight);
		    }
    		
    		
	    }
	    }
	    catch(Exception e)
	    {
	    	SA_PropertyWare.serviceAnimalFlag = false;
	    }
	    //Concession Addendum
	    
	    try
	    {
	    	if(text.contains(SA_PDFAppConfig.concessionAddendumText))
	    	{
	    		SA_PropertyWare.concessionAddendumFlag = true;
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
		 lateFeeRuleText = text.substring(text.indexOf(SA_PDFAppConfig.lateFeeRuleText_Prior)+SA_PDFAppConfig.lateFeeRuleText_Prior.length(),text.indexOf(SA_PDFAppConfig.lateFeeRuleText_After));
		}
		catch(Exception e)
		{
			try
			{
			lateFeeRuleText = text.substring(text.indexOf(SA_PDFAppConfig.lateFeeRuleText_Prior)+SA_PDFAppConfig.lateFeeRuleText_Prior.length(),text.indexOf(SA_PDFAppConfig.lateFeeRuleText_After2));
			}
			catch(Exception e2)
			{
			return false;
			}
		}
		if(lateFeeRuleText.contains(SA_PDFAppConfig.lateFeeRule_whicheverIsGreater))
		{
			RunnerClass.lateFeeRuleType = "GreaterOfFlatFeeOrPercentage";
			RunnerClass.lateFeeType = "GreaterOfFlatFeeOrPercentage";
			//SA_PropertyWare.lateFeeType ="Greater of Flat Fee or Percentage"; 
		//Late charge day
			try
			{
		   // SA_PropertyWare.lateChargeDay =  lateFeeRuleText.substring(lateFeeRuleText.indexOf(SA_PDFAppConfig.lateFeeRule_whicheverIsGreater_dueDay_Prior)+SA_PDFAppConfig.lateFeeRule_whicheverIsGreater_dueDay_Prior.length()).trim().split(" ")[0];
				SA_PropertyWare.lateChargeDay =  lateFeeRuleText.split(SA_PDFAppConfig.lateFeeRule_whicheverIsGreater_dueDay_After)[0].trim();
				SA_PropertyWare.lateChargeDay =SA_PropertyWare.lateChargeDay.substring(SA_PropertyWare.lateChargeDay.lastIndexOf(" ")+1);
		    SA_PropertyWare.lateChargeDay =  SA_PropertyWare.lateChargeDay.replaceAll("[^0-9]", "");
			}
			catch(Exception e)
			{
				SA_PropertyWare.lateChargeDay = "Error";
			}
         System.out.println("Late Charge Day = "+SA_PropertyWare.lateChargeDay);
			RunnerClass.dueDay_GreaterOf = SA_PropertyWare.lateChargeDay;
		//Late Fee Percentage
			try
			{
		    SA_PropertyWare.lateFeePercentage =  lateFeeRuleText.substring(lateFeeRuleText.indexOf(SA_PDFAppConfig.lateFeeRule_whicheverIsGreater_lateFeePercentage)+SA_PDFAppConfig.lateFeeRule_whicheverIsGreater_lateFeePercentage.length()).trim().split(" ")[0];
		    SA_PropertyWare.lateFeePercentage = SA_PropertyWare.lateFeePercentage.replaceAll("[^0-9]", "");
			}
			catch(Exception e)
			{
				SA_PropertyWare.lateFeePercentage = "Error";
			}
         System.out.println("Late Fee Percentage = "+SA_PropertyWare.lateFeePercentage);
         RunnerClass.percentage = SA_PropertyWare.lateFeePercentage;
         //Late Fee Amount
         try
         {
         String lateFeeAmount  = lateFeeRuleText.substring(lateFeeRuleText.indexOf(SA_PDFAppConfig.lateFeeRule_whicheverIsGreater_lateFeeAmount)+SA_PDFAppConfig.lateFeeRule_whicheverIsGreater_lateFeeAmount.length()).trim().split(" ")[0];
         SA_PropertyWare.flatFeeAmount = lateFeeAmount.replaceAll("[^.0-9]", "");
         }
         catch(Exception e)
         {
        	 SA_PropertyWare.flatFeeAmount ="Error";
         }
         System.out.println("Late Fee Amount = "+SA_PropertyWare.flatFeeAmount);
        RunnerClass.flatFee = SA_PropertyWare.flatFeeAmount;
         return true;
		}
		else 
		if(lateFeeRuleText.contains(SA_PDFAppConfig.lateFeeRule_mayNotExceedMoreThan30Days))
		{
			RunnerClass.lateFeeRuleType = "initialFeePluPerDayFee";
			//RunnerClass.lateFeeRuleType = "Initial Fee + Per Day Fee";
			
			SA_PropertyWare.lateFeeType ="initialFeePluPerDayFee"; 
	         try
	 	    {
	 		    SA_PropertyWare.lateChargeFee = text.substring(text.indexOf(SA_PDFAppConfig.AB_lateFee_Prior)+SA_PDFAppConfig.AB_lateFee_Prior.length()).trim().split(" ")[0];
	 		    //SA_PropertyWare.lateChargeFee =  SA_PropertyWare.lateChargeFee.substring(0,SA_PropertyWare.lateChargeFee.length()-1);
	 	    }
	 	    catch(Exception e)
	 	    {
	 		    SA_PropertyWare.lateChargeFee ="Error";	
	 		    e.printStackTrace();
	 	    }
	 	    System.out.println("Late Charge Fee = "+SA_PropertyWare.lateChargeFee.trim());
	 	   RunnerClass.initialFeeAmount = SA_PropertyWare.lateChargeFee;
	 	    //Per Day Fee
	 	    try
	 	    {
	 	    	SA_PropertyWare.lateFeeChargePerDay = text.substring(text.indexOf(SA_PDFAppConfig.AB_additionalLateChargesPerDay_Prior)+SA_PDFAppConfig.AB_additionalLateChargesPerDay_Prior.length()).split(" ")[0].trim();//,text.indexOf(SA_PDFAppConfig.AB_additionalLateChargesPerDay_After));
	 	    }
	 	    catch(Exception e)
	 	    {
	 	    	SA_PropertyWare.lateFeeChargePerDay =  "Error";	
	 	    	e.printStackTrace();
	 	    }
	 	    System.out.println("Per Day Fee = "+SA_PropertyWare.lateFeeChargePerDay.trim());
	 	    RunnerClass.perDayFeeAmount = SA_PropertyWare.lateFeeChargePerDay;
	 	    //Additional Late Charges Limit
	 	    try
	 	    {
	 	    	SA_PropertyWare.additionalLateChargesLimit = text.substring(text.indexOf(SA_PDFAppConfig.AB_additionalLateChargesLimit_Prior)+SA_PDFAppConfig.AB_additionalLateChargesLimit_Prior.length()).trim().split(" ")[0]; //,text.indexOf(SA_PDFAppConfig.AB_additionalLateChargesLimit_After));
	 	    }
	 	    catch(Exception e)
	 	    {
	 	    	SA_PropertyWare.additionalLateChargesLimit =  "Error";	
	 	    	e.printStackTrace();
	 	    }
	 	    System.out.println("additional Late Charges Limit = "+SA_PropertyWare.additionalLateChargesLimit.trim());
	 	    RunnerClass.additionalLateChargesLimit = SA_PropertyWare.additionalLateChargesLimit;
	 	 //Late Charge Day
			try
	 	    {
			SA_PropertyWare.lateChargeDay = lateFeeRuleText.substring(lateFeeRuleText.indexOf("p.m. on the ")+"p.m. on the ".length()).trim().split(" ")[0];
			SA_PropertyWare.lateChargeDay = SA_PropertyWare.lateChargeDay.replaceAll("[^0-9]", "");
	 	    }
			catch(Exception e)
	 	    {
	 	    	SA_PropertyWare.lateChargeDay =  "Error";	
	 	    	e.printStackTrace();
	 	    }
	 	    System.out.println("Late Charge Due Day = "+SA_PropertyWare.lateChargeDay.trim());
	 	    RunnerClass.dueDay_initialFee = SA_PropertyWare.lateChargeDay;
	 	   return true;
		}
		else if(lateFeeRuleText.contains(SA_PDFAppConfig.lateFeeRule_mayNotExceedAmount))
			{
			RunnerClass.lateFeeRuleType = "initialFeePluPerDayFee";
			RunnerClass.lateFeeRuleType = "Initial Fee + Per Day Fee";
			
			//Late Charge Day
			try
	 	    {
			SA_PropertyWare.lateChargeDay = lateFeeRuleText.substring(lateFeeRuleText.indexOf("an initial late charge on the")+"an initial late charge on the".length()).trim().split(" ")[0];
			SA_PropertyWare.lateChargeDay = SA_PropertyWare.lateChargeDay.replaceAll("[^0-9]", "");
	 	    }
			catch(Exception e)
	 	    {
	 	    	SA_PropertyWare.lateChargeDay =  "Error";	
	 	    	e.printStackTrace();
	 	    }
	 	    System.out.println("Late Charge Due Day = "+SA_PropertyWare.lateChargeDay.trim());
	 	   RunnerClass.dueDay_initialFee = SA_PropertyWare.lateChargeDay;
	 	    // initial Late Charge
	 	   try
	 	    {
			SA_PropertyWare.lateChargeFee = lateFeeRuleText.substring(lateFeeRuleText.indexOf("day of the month equal to $")+"day of the month equal to $".length()).trim().split(" ")[0];
			SA_PropertyWare.lateChargeFee = SA_PropertyWare.lateChargeFee.replaceAll("[^0-9.]", "");
	 	    }
			catch(Exception e)
	 	    {
	 	    	SA_PropertyWare.lateChargeFee =  "Error";	
	 	    	e.printStackTrace();
	 	    }
	 	    System.out.println("Late Charge Fee = "+SA_PropertyWare.lateChargeFee.trim());
	 	   RunnerClass.initialFeeAmount = SA_PropertyWare.lateChargeFee;
	 	    // Additional Late Charges
	 	   try
	 	    {
			SA_PropertyWare.additionalLateCharges = lateFeeRuleText.substring(lateFeeRuleText.indexOf("and additional late charge of $")+"and additional late charge of $".length()).trim().split(" ")[0];
			SA_PropertyWare.additionalLateCharges = SA_PropertyWare.additionalLateCharges.replaceAll("[^0-9.]", "");
	 	    }
			catch(Exception e)
	 	    {
	 	    	SA_PropertyWare.additionalLateCharges =  "Error";	
	 	    	e.printStackTrace();
	 	    }
	 	    System.out.println("Additional Late Charges = "+SA_PropertyWare.additionalLateCharges.trim());
	 	   RunnerClass.maximumAmount = SA_PropertyWare.additionalLateCharges;
	 	    //Additional Late Charges Limit
	 	   try
	 	    {
			SA_PropertyWare.additionalLateChargesLimit = lateFeeRuleText.substring(lateFeeRuleText.indexOf("(initial and additional) may not exceed $")+"(initial and additional) may not exceed $".length()).trim().split(" ")[0];
			SA_PropertyWare.additionalLateChargesLimit = SA_PropertyWare.additionalLateChargesLimit.replaceAll("[^0-9.]", "");
	 	    }
			catch(Exception e)
	 	    {
	 	    	SA_PropertyWare.additionalLateChargesLimit =  "Error";	
	 	    	e.printStackTrace();
	 	    }
	 	    System.out.println("Additional Late Charges Limit = "+SA_PropertyWare.additionalLateChargesLimit.trim());
	 	   RunnerClass.additionalLateChargesLimit = SA_PropertyWare.additionalLateChargesLimit;
			return true;
			}
		else 
			if(lateFeeRuleText.contains(SA_PDFAppConfig.lateFeeRule_totalDelinquentRentDueToTheTenantAccount))
			{
				RunnerClass.lateFeeRuleType = "GreaterOfFlatFeeOrPercentage";
				RunnerClass.lateFeeType = "GreaterOfFlatFeeOrPercentage";
				
			//Late Charge Day
			try
	 	    {
			SA_PropertyWare.lateChargeDay = lateFeeRuleText.substring(lateFeeRuleText.indexOf("place of payment on the ")+"place of payment on the ".length()).trim().split(" ")[0];
			SA_PropertyWare.lateChargeDay = SA_PropertyWare.lateChargeDay.replaceAll("[^0-9]", "");
	 	    }
			catch(Exception e)
	 	    {
	 	    	SA_PropertyWare.lateChargeDay =  "Error";	
	 	    	e.printStackTrace();
	 	    }
	 	    System.out.println("Late Charge Due Day = "+SA_PropertyWare.lateChargeDay.trim());
	 	   RunnerClass.dueDay_GreaterOf = SA_PropertyWare.lateChargeDay;
	 	    // initial Late Charge
	 	   try
	 	    {
			SA_PropertyWare.lateChargeFee = lateFeeRuleText.substring(lateFeeRuleText.indexOf("an initial late charge equal to ")+"an initial late charge equal to ".length()).trim().split(" ")[0];
			//SA_PropertyWare.lateChargeFee = SA_PropertyWare.lateChargeFee.replaceAll("[^0-9.]", "");
	 	    }
			catch(Exception e)
	 	    {
	 	    	SA_PropertyWare.lateChargeFee =  "Error";	
	 	    	e.printStackTrace();
	 	    }
	 	    System.out.println("Late Charge Fee = "+SA_PropertyWare.lateChargeFee.trim());
	 	   RunnerClass.percentage = SA_PropertyWare.lateChargeFee;
	 	   /*
	 	    // Additional Late Charges
	 	   try
	 	    {
			SA_PropertyWare.additionalLateCharges = lateFeeRuleText.substring(lateFeeRuleText.indexOf("and additional late charge of $")+"and additional late charge of $".length()).trim().split(" ")[0];
			SA_PropertyWare.additionalLateCharges = SA_PropertyWare.additionalLateCharges.replaceAll("[^0-9.]", "");
	 	    }
			catch(Exception e)
	 	    {
	 	    	SA_PropertyWare.additionalLateCharges =  "Error";	
	 	    	e.printStackTrace();
	 	    }
	 	    System.out.println("Additional Late Charges = "+SA_PropertyWare.additionalLateCharges.trim());
	 	    RunnerClass.maximumAmount = SA_PropertyWare.additionalLateCharges;
	 	    //Additional Late Charges Limit
	 	   try
	 	    {
			SA_PropertyWare.additionalLateChargesLimit = lateFeeRuleText.substring(lateFeeRuleText.indexOf("(initial and additional) may not exceed $")+"(initial and additional) may not exceed $".length()).trim().split(" ")[0];
			SA_PropertyWare.additionalLateChargesLimit = SA_PropertyWare.additionalLateChargesLimit.replaceAll("[^0-9.]", "");
	 	    }
			catch(Exception e)
	 	    {
	 	    	SA_PropertyWare.additionalLateChargesLimit =  "Error";	
	 	    	e.printStackTrace();
	 	    }
	 	    System.out.println("Additional Late Charges Limit = "+SA_PropertyWare.additionalLateChargesLimit.trim());
	 	    RunnerClass.additionalLateChargesLimit = SA_PropertyWare.additionalLateChargesLimit;
			return true;
			}
			else
		   {
			SA_PropertyWare.lateFeeType ="";
		   }
		   */
			}
		return true;		
	}



}
