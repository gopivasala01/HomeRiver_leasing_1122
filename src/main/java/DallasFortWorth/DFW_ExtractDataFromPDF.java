package DallasFortWorth;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import mainPackage.InsertDataIntoDatabase;
import mainPackage.RunnerClass;

public class DFW_ExtractDataFromPDF 
{
	public static boolean petFlag;
	public static String text="";
	public boolean arizona() throws Exception
	//public static void main(String args[]) throws Exception
	{
		
		DFW_PropertyWare.petFlag = false;
	    File file = RunnerClass.getLastModified();
		//File file = new File("C:\\Gopi\\Projects\\Property ware\\Lease Close Outs\\PDFS\\Lease_323_224_1933_Covey_Ct_DTX_Pinneke-Phi (2).pdf");
		FileInputStream fis = new FileInputStream(file);
		DFW_RunnerClass.document = PDDocument.load(fis);
	    text = new PDFTextStripper().getText(DFW_RunnerClass.document);
	    DFW_PropertyWare.pdfText  = text;
	    if(!text.contains(DFW_AppConfig.PDFFormatConfirmationText)) 
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
	    	DFW_PropertyWare.commensementDate = text.substring(text.indexOf(DFW_PDFAppConfig.AB_commencementDate_Prior)+DFW_PDFAppConfig.AB_commencementDate_Prior.length(),text.indexOf(DFW_PDFAppConfig.AB_expirationDate_Prior));
	    	DFW_PropertyWare.commensementDate = DFW_PropertyWare.commensementDate.trim().replaceAll(" +", " ");
	    }
	    catch(Exception e)
	    {
	    	DFW_PropertyWare.commensementDate = "Error";
	    	e.printStackTrace();
	    }
	    System.out.println("Commensement Date = "+DFW_PropertyWare.commensementDate);
	   try
	    {
		   String expirationDateWaw = text.substring(text.indexOf(DFW_PDFAppConfig.AB_expirationDate_Prior)+DFW_PDFAppConfig.AB_expirationDate_Prior.length());
		   DFW_PropertyWare.expirationDate =expirationDateWaw.trim().split(" ")[0]+" "+expirationDateWaw.trim().split(" ")[1]+" "+expirationDateWaw.trim().split(" ")[2];
		   DFW_PropertyWare.expirationDate = DFW_PropertyWare.expirationDate.trim().replaceAll(" +", " ");
	    }
	    catch(Exception e)
	    {
	    	 DFW_PropertyWare.expirationDate = "Error";
	    	 e.printStackTrace();
	    }
	   System.out.println("Expiration Date = "+DFW_PropertyWare.expirationDate);
	   try
	    {
		    DFW_PropertyWare.proratedRent = text.substring(text.indexOf(DFW_PDFAppConfig.AB_proratedRent_Prior)+DFW_PDFAppConfig.AB_proratedRent_Prior.length(),text.indexOf(DFW_PDFAppConfig.AB_proratedRent_After));
		    if(DFW_PropertyWare.proratedRent.matches(".*[a-zA-Z]+.*"))
		    {
		    	DFW_PropertyWare.proratedRent = "Error";
		    }
		    		
	    }
	    catch(Exception e)
	    {
	    	DFW_PropertyWare.proratedRent = "Error";
	    	e.printStackTrace();
	    }
	   System.out.println("Prorated Rent = "+DFW_PropertyWare.proratedRent);
	    try
	    {
		    DFW_PropertyWare.proratedRentDate = text.substring(text.indexOf(DFW_PDFAppConfig.AB_proratedRentDate_Prior)+DFW_PDFAppConfig.AB_proratedRentDate_Prior.length(),text.indexOf(DFW_PDFAppConfig.AB_proratedRentDate_After)).trim();
	    }
	    catch(Exception e)
	    {
	    	DFW_PropertyWare.proratedRentDate = "Error";
	    	e.printStackTrace();
	    }
	    System.out.println("Prorated Rent Date= "+DFW_PropertyWare.proratedRentDate.trim());
	    /*
	    try
	    {
		    DFW_PropertyWare.monthlyRentDate = text.substring(text.indexOf(DFW_PDFAppConfig.AB_fullRentDate_Prior)+DFW_PDFAppConfig.AB_fullRentDate_Prior.length(),text.indexOf(DFW_PDFAppConfig.AB_fullRentDate_After));
		    System.out.println("Monthly Rent Date= "+DFW_PropertyWare.monthlyRentDate.trim());
	    }
	    catch(Exception e)
	    {
	    	try
	    	{
	    		DFW_PropertyWare.monthlyRentDate = text.substring(text.indexOf(DFW_PDFAppConfig.AB_fullRentDate_Prior)+DFW_PDFAppConfig.AB_fullRentDate_Prior.length(),text.indexOf(DFW_PDFAppConfig.AB_fullRentDate1_After));
			   	System.out.println("Monthly Rent Date= "+DFW_PropertyWare.monthlyRentDate.trim());
	    	}
	    	catch(Exception e1)
		    {
		    	DFW_PropertyWare.monthlyRentDate = "Error";  
		    	e1.printStackTrace();
		    }
	    }*/
	    try
	    {
		    DFW_PropertyWare.monthlyRent = text.substring(text.indexOf(DFW_PDFAppConfig.AB_fullRent_Prior)+DFW_PDFAppConfig.AB_fullRent_Prior.length()).trim().split(" ")[0].trim();//,text.indexOf(DFW_PDFAppConfig.AB_fullRent_After)).substring(1).replaceAll("[^.0-9]", "");;
		    if(RunnerClass.onlyDigits(DFW_PropertyWare.monthlyRent.replace(".", "").replace(",", ""))==false)
		    {
		    	DFW_PropertyWare.monthlyRent = text.substring(text.indexOf(DFW_PDFAppConfig.AB_fullRent2_Prior)+DFW_PDFAppConfig.AB_fullRent2_Prior.length()).trim().split(" ")[0].trim();
		    }
		    if(DFW_PropertyWare.monthlyRent.contains("*"))
		    {
		    	DFW_PropertyWare.monthlyRent = DFW_PropertyWare.monthlyRent.replace("*","");
		    }
		    if(DFW_PropertyWare.monthlyRent.matches(".*[a-zA-Z]+.*"))
		    {
		    	DFW_PropertyWare.monthlyRent = "Error";
		    }
		    if(DFW_PropertyWare.monthlyRent.endsWith(","))
		    {
		    	DFW_PropertyWare.monthlyRent = DFW_PropertyWare.monthlyRent.substring(0,DFW_PropertyWare.monthlyRent.length()-1);
		    }
	    }
	    catch(Exception e)
	    {
	    	 DFW_PropertyWare.monthlyRent = "Error";
	    	 e.printStackTrace();
	    }
	    System.out.println("Monthly Rent "+DFW_PropertyWare.monthlyRent.trim());
	    try
	    {
		    DFW_PropertyWare.adminFee = text.substring(text.indexOf(DFW_PDFAppConfig.AB_adminFee_Prior)+DFW_PDFAppConfig.AB_adminFee_Prior.length()).trim().split(" ")[0];
		    if(DFW_PropertyWare.adminFee.matches(".*[a-zA-Z]+.*"))
		    {
		    	DFW_PropertyWare.adminFee = "Error";
		    }
	    }
	    catch(Exception e)
	    {
		    DFW_PropertyWare.adminFee = "Error";
		    e.printStackTrace();
	    }
	    System.out.println("Admin Fee = "+DFW_PropertyWare.adminFee.trim());
	    
	  //Resident Benefits Package 
	    if(text.contains(DFW_PDFAppConfig.residentBenefitsPackageAddendumCheck))
	    {
	    	DFW_PropertyWare.residentBenefitsPackageAvailabilityCheck = true;
	    	 try
	 	    {
	 		    DFW_PropertyWare.residentBenefitsPackage = text.substring(text.indexOf(DFW_PDFAppConfig.AB1_residentBenefitsPackage_Prior)+DFW_PDFAppConfig.AB1_residentBenefitsPackage_Prior.length()).split(" ")[0].replaceAll("[^0-9a-zA-Z.]", "");
	 		    if(DFW_PropertyWare.residentBenefitsPackage.matches(".*[a-zA-Z]+.*"))
	 		    {
	 		    	DFW_PropertyWare.residentBenefitsPackage = "Error";
	 		    }
	 	    }
	 	    catch(Exception e)
	 	    {
	 		    DFW_PropertyWare.residentBenefitsPackage = "Error";
	 		    e.printStackTrace();
	 	    }
	    	 System.out.println("Resident Benefits Package  = "+DFW_PropertyWare.residentBenefitsPackage.trim());
	    	//DFW_PDFAppConfig.AB1_residentBenefitsPackage_Prior
	    }
	    else
	    {
		    if(text.contains(DFW_PDFAppConfig_Format2.HVACFilterAddendumTextAvailabilityCheck)==true)
		    {
		    	DFW_PropertyWare.HVACFilterFlag =true;
		    }
		    else
		    {
		    try
		    {
			   String[] airFilterFeeArray = text.substring(text.indexOf(DFW_PDFAppConfig.AB_airFilterFee_Prior)+DFW_PDFAppConfig.AB_airFilterFee_Prior.length()).split(" ");
			   DFW_PropertyWare.airFilterFee = airFilterFeeArray[0];
			   if(DFW_PropertyWare.airFilterFee.matches(".*[a-zA-Z]+.*"))
			    {
			    	DFW_PropertyWare.airFilterFee = "Error";
			    }
		    }
		    catch(Exception e)
		    {
		    DFW_PropertyWare.airFilterFee = "Error";
		    e.printStackTrace();
		    }
		    }
		    System.out.println("Air Filter Fee = "+DFW_PropertyWare.airFilterFee.trim());
	    }
	    try
	    {
	    	String[] earlyTerminationRaw = text.substring(text.indexOf(DFW_PDFAppConfig.AB_earlyTerminationFee_Prior)+DFW_PDFAppConfig.AB_earlyTerminationFee_Prior.length()).split(" ");
	    	
		    DFW_PropertyWare.earlyTermination = earlyTerminationRaw[0]+earlyTerminationRaw[1]; //text.substring(text.indexOf(DFW_PDFAppConfig.AB_earlyTerminationFee_Prior)+DFW_PDFAppConfig.AB_earlyTerminationFee_Prior.length(),text.indexOf(DFW_PDFAppConfig.AB_earlyTerminationFee_After));
	    }
	    catch(Exception e)
	    {
	    	DFW_PropertyWare.earlyTermination = "Error";	
	    	e.printStackTrace();
	    }
	    System.out.println("Early Termination  = "+DFW_PropertyWare.earlyTermination.trim());
	    try
	    {
	    	
		    DFW_PropertyWare.occupants = text.substring(text.indexOf(DFW_PDFAppConfig.AB_occupants_Prior)+DFW_PDFAppConfig.AB_occupants_Prior.length(),text.indexOf(DFW_PDFAppConfig.AB_occupants_After));
	    }
	    catch(Exception e)
	    {
		    DFW_PropertyWare.occupants ="Error";	
		    e.printStackTrace();
	    }
	    System.out.println("Occupants = "+DFW_PropertyWare.occupants.trim());
	    
	    //Late charges 
	    //Decide Late Fee Rule
	   DFW_ExtractDataFromPDF.lateFeeRule();
	    
	  //Prepayment Charge
  		if(DFW_PropertyWare.portfolioType.contains("MCH"))
  		{
  			if(DFW_PropertyWare.proratedRent.equalsIgnoreCase("n/a")||DFW_PropertyWare.proratedRent.equalsIgnoreCase("Error")||DFW_PropertyWare.proratedRent.equalsIgnoreCase(""))
  			{
  				DFW_PropertyWare.prepaymentCharge = "Error";
  			}
  			else
  			{
	  		try
	  		{
	  		DFW_PropertyWare.prepaymentCharge =String.valueOf(Double.parseDouble(DFW_PropertyWare.monthlyRent.trim().replace(",", "")) - Double.parseDouble(DFW_PropertyWare.proratedRent.trim().replace(",", ""))); 
	  		}
	  		catch(Exception e)
	  		{
	  			DFW_PropertyWare.prepaymentCharge ="Error";
	  		}
	  		}
  			System.out.println("Prepayment Charge = "+DFW_PropertyWare.prepaymentCharge);
  		 }
	    petFlag = text.contains(DFW_PDFAppConfig.AB_petAgreementAvailabilityCheck);
	    System.out.println("Pet Addendum Available = "+petFlag);
	    if(petFlag ==true)
	    {
	    	DFW_PropertyWare.petFlag = true;
	    	try
	    	{
	    	DFW_PropertyWare.petSecurityDeposit = text.substring(text.indexOf(DFW_PDFAppConfig.AB_securityDeposity_Prior)+DFW_PDFAppConfig.AB_securityDeposity_Prior.length(),text.indexOf(DFW_PDFAppConfig.AB_securityDeposity_After));
	    	if(DFW_PropertyWare.petSecurityDeposit.matches(".*[a-zA-Z]+.*"))
		    {
		    	DFW_PropertyWare.petSecurityDeposit = "Error";
		    }
	    	}
	    	catch(Exception e)
	    	{
	    	DFW_PropertyWare.petSecurityDeposit = "Error";	
	    	e.printStackTrace();
	    	}
	    	System.out.println("Pet Security Deposit = "+DFW_PropertyWare.petSecurityDeposit.trim());
	    	if(RunnerClass.onlyDigits(DFW_PropertyWare.petSecurityDeposit.replace(".", ""))==true)
		    {
	    		DFW_PropertyWare.petSecurityDepositFlag=true;
		    	System.out.println("Security Deposit is checked");
		    }
	    	//TODO Check
	    	  try
			    {
	    		  String proratedPetRaw = "Prorated Pet Rent: On or before "+DFW_PropertyWare.commensementDate.trim()+" Tenant will pay Landlord $";
	    		DFW_PropertyWare.proratedPetRent = text.substring(text.indexOf(proratedPetRaw)+proratedPetRaw.length()).trim().split(" ")[0];//.replaceAll("[a-ZA-Z,]", "");
			    //AR_PropertyWare.proratedPetRent = proratedPetRentRaw.substring(proratedPetRentRaw.indexOf("Tenant will pay Landlord $")+"Tenant will pay Landlord $".length());//,proratedPetRentRaw.indexOf(AppConfig.AR_proratedPetRent_After));
			    if(DFW_PropertyWare.proratedPetRent.matches(".*[a-zA-Z]+.*"))
			    {
			    	DFW_PropertyWare.proratedPetRent = "Error";
			    }
			    }
			    catch(Exception e)
			    {
			   
			    DFW_PropertyWare.proratedPetRent = "Error";	
			    e.printStackTrace();
			    }
	    	  System.out.println("Prorated Pet Rent = "+DFW_PropertyWare.proratedPetRent.trim());
	    	
	    	try
		    {
	    		 DFW_PropertyWare.petRent = text.substring(text.indexOf(DFW_PDFAppConfig.AB_petRent_Prior)+DFW_PDFAppConfig.AB_petRent_Prior.length()).trim().split(" ")[0];
	    		 if(DFW_PropertyWare.petRent.contains(",for"))
	    		 {
	    			 DFW_PropertyWare.petRent = DFW_PropertyWare.petRent.split(",")[0].trim();
	    		 }
	    		 else
	    		 {
		    		 if(DFW_PropertyWare.petRent.matches(".*[a-zA-Z]+.*")==true)
		    		 {
		    			 DFW_PropertyWare.petRent = text.substring(text.indexOf(DFW_PDFAppConfig.AB_petRent1_Prior)+DFW_PDFAppConfig.AB_petRent1_Prior.length()).trim().split(" ")[0];
		    			 if(DFW_PropertyWare.petRent.matches(".*[a-zA-Z]+.*"))
			    		    {
			    		    	DFW_PropertyWare.petRent = "Error";
			    		    }
		    		 }
		    		 else 
		    		 DFW_PropertyWare.petRent = RunnerClass.extractNumber(DFW_PropertyWare.petRent);
	    		 }
		    }
	    	catch(Exception e)
		    {
	    		try
	    		{
	    			e.printStackTrace();
	    			DFW_PropertyWare.petRent = text.substring(text.indexOf(DFW_PDFAppConfig.AB_petRent1_Prior)+DFW_PDFAppConfig.AB_petRent1_Prior.length()).trim().split(" ")[0];
//					 System.out.println("Pet rent = "+DFW_PropertyWare.petRent.trim());
	    			if(DFW_PropertyWare.petRent.matches(".*[a-zA-Z]+.*"))
	    		    {
	    		    	DFW_PropertyWare.petRent = "Error";
	    		    }
	    		}
	    		
	    		catch(Exception e1)
			    {
			    	DFW_PropertyWare.petRent = "Error";  
			    	e1.printStackTrace();
			    }
		    }
	    	System.out.println("Pet rent = "+DFW_PropertyWare.petRent.trim());
		    	//DFW_PropertyWare.petRent = "Error";  
		    	//e.printStackTrace();
		   /* 
	    	try
    		{
    			//String petFeeRaw1 = text.substring(text.indexOf(DFW_PDFAppConfig.AB_petFee_Prior));
    			DFW_PropertyWare.petFee = text.substring(text.indexOf(DFW_PDFAppConfig.AB_petFee_Prior)+DFW_PDFAppConfig.AB_petFee_Prior.length()).trim().split(" ")[0].trim();
    			//DFW_PropertyWare.petFee =  petFeeRaw[petFeeRaw.length-2].trim();
    			//if(DFW_PropertyWare.petFee.matches(".*[a-zA-Z]+.*"))
    			//{
    				//DFW_PropertyWare.petFee = text.substring(text.indexOf(DFW_PDFAppConfig.AB_petFee2_Prior)+DFW_PDFAppConfig.AB_petFee2_Prior.length()).trim().split(" ")[0].trim();
    			//}
    			//System.out.println(petFeeRaw.length);
    		}
    		
    		catch(Exception e1)
		    {
		    	DFW_PropertyWare.petFee = "Error";  
		    	e1.printStackTrace();
		    }
	    	System.out.println("Pet Fee = "+DFW_PropertyWare.petFee);
	    	*/
	    	// Get text between Type: word
	    	
	    	String typeSubString = text.substring(text.indexOf(DFW_PDFAppConfig.AB_typeWord_Prior)+DFW_PDFAppConfig.AB_typeWord_Prior.length(),text.indexOf(DFW_PDFAppConfig.AB_typeWord_After));
	    	
	    	String newText = typeSubString.replace("Type:","");
		    DFW_PropertyWare.countOfTypeWordInText = ((typeSubString.length() - newText.length())/"Type:".length());
		    System.out.println("Type: occurences = "+DFW_PropertyWare.countOfTypeWordInText);
		    
		    DFW_PropertyWare.petType = new ArrayList();
		    DFW_PropertyWare.petBreed = new ArrayList();
		    DFW_PropertyWare.petWeight = new ArrayList();
		    for(int i =0;i<DFW_PropertyWare.countOfTypeWordInText;i++)
		    {
		    	String type = typeSubString.substring(RunnerClass.nthOccurrence(typeSubString, "Type:", i+1)+DFW_PDFAppConfig.AB_pet1Type_Prior.length(),RunnerClass.nthOccurrence(typeSubString, "Breed:", i+1)).trim();
		    	if(type.contains("N/A")||type.contains("n/a"))
		    		break;
		    	System.out.println(type);
		    	DFW_PropertyWare.petType.add(type);
		    	int pet1Breedindex1 = RunnerClass.nthOccurrence(typeSubString, "Breed:", i+1)+"Breed:".length()+1;
			    String subString = typeSubString.substring(pet1Breedindex1);
			    //int pet1Breedindex2 = RunnerClass.nthOccurrence(subString,"Name:",i+1);
			   // System.out.println("Index 2 = "+(index2+index1));
			    String breed = subString.split("Name:")[0].trim();//typeSubString.substring(pet1Breedindex1,(pet1Breedindex2+pet1Breedindex1));
			    System.out.println(breed);
			    DFW_PropertyWare.petBreed.add(breed);
			    int pet1Weightindex1 = RunnerClass.nthOccurrence(typeSubString, "Weight:", i+1)+"Weight:".length()+1;
			    String pet1WeightSubstring = typeSubString.substring(pet1Weightindex1);
			    //int pet1WeightIndex2 = RunnerClass.nthOccurrence(pet1WeightSubstring,"Age:",i+1);
			   // System.out.println("Index 2 = "+(index2+index1));
			    String weight = pet1WeightSubstring.split("Age:")[0].trim(); //typeSubString.substring(pet1Weightindex1,(pet1WeightIndex2+pet1Weightindex1));
			    System.out.println(weight);
			    DFW_PropertyWare.petWeight.add(weight);
		    }
		    
		    
		    
		    try
		    {
		    	DFW_PropertyWare.petOneTimeNonRefundableFee = text.substring(text.indexOf(DFW_PDFAppConfig.AB_petFeeOneTime_Prior)+DFW_PDFAppConfig.AB_petFeeOneTime_Prior.length()).split(" ")[0];//,text.indexOf(DFW_PDFAppConfig.AB_petFeeOneTime_After));
		    	if(DFW_PropertyWare.petOneTimeNonRefundableFee.matches(".*[a-zA-Z]+.*")||DFW_PropertyWare.petOneTimeNonRefundableFee.trim()=="")
    		    {
    		    	DFW_PropertyWare.petOneTimeNonRefundableFee = "Error";
    		    }
		    }
		    catch(Exception e)
		    {
		    	DFW_PropertyWare.petOneTimeNonRefundableFee =  "Error";
		    	e.printStackTrace();
		    }  
		    System.out.println("pet one time non refundable = "+DFW_PropertyWare.petOneTimeNonRefundableFee.trim());
		   
	    }
	    
	    //Service Animal Addendum check
	    try
	    {
	    if(text.contains(DFW_AppConfig.serviceAnimalText))
	    {
	    	DFW_PropertyWare.serviceAnimalFlag = true;
    		System.out.println("Service Animal Addendum is available");
    		
            String typeSubString = text.substring(text.indexOf(DFW_PDFAppConfig.AB_serviceAnimal_typeWord_Prior)+DFW_PDFAppConfig.AB_serviceAnimal_typeWord_Prior.length(),text.indexOf(DFW_PDFAppConfig.AB_serviceAnimal_typeWord_After));
	    	
	    	String newText = typeSubString.replace("Type:","");
		    int  countOftypeWords_ServiceAnimal = ((typeSubString.length() - newText.length())/"Type:".length());
		    System.out.println("Service Animal - Type: occurences = "+countOftypeWords_ServiceAnimal);
		    
		    DFW_PropertyWare.serviceAnimalPetType = new ArrayList();
		    DFW_PropertyWare.serviceAnimalPetBreed = new ArrayList();
		    DFW_PropertyWare.serviceAnimalPetWeight = new ArrayList();
		    for(int i =0;i<countOftypeWords_ServiceAnimal;i++)
		    {
		    	String type = typeSubString.substring(RunnerClass.nthOccurrence(typeSubString, "Type:", i+1)+DFW_PDFAppConfig.AB_pet1Type_Prior.length(),RunnerClass.nthOccurrence(typeSubString, "Breed:", i+1)).trim();
		    	if(type.contains("N/A")||type.contains("n/a"))
		    		break;
		    	System.out.println(type);
		    	DFW_PropertyWare.serviceAnimalPetType.add(type);
		    	int pet1Breedindex1 = RunnerClass.nthOccurrence(typeSubString, "Breed:", i+1)+"Breed:".length()+1;
			    String subString = typeSubString.substring(pet1Breedindex1);
			    //int pet1Breedindex2 = RunnerClass.nthOccurrence(subString,"Name:",i+1);
			   // System.out.println("Index 2 = "+(index2+index1));
			    String breed = subString.split("Name:")[0].trim();//typeSubString.substring(pet1Breedindex1,(pet1Breedindex2+pet1Breedindex1));
			    System.out.println(breed);
			    DFW_PropertyWare.serviceAnimalPetBreed.add(breed);
			    int pet1Weightindex1 = RunnerClass.nthOccurrence(typeSubString, "Weight:", i+1)+"Weight:".length()+1;
			    String pet1WeightSubstring = typeSubString.substring(pet1Weightindex1);
			    //int pet1WeightIndex2 = RunnerClass.nthOccurrence(pet1WeightSubstring,"Age:",i+1);
			   // System.out.println("Index 2 = "+(index2+index1));
			    String weight = pet1WeightSubstring.split("Age:")[0].trim(); //typeSubString.substring(pet1Weightindex1,(pet1WeightIndex2+pet1Weightindex1));
			    System.out.println(weight);
			    DFW_PropertyWare.serviceAnimalPetWeight.add(weight);
		    }
    		
    		
	    }
	    }
	    catch(Exception e)
	    {
	    	DFW_PropertyWare.serviceAnimalFlag = false;
	    }
	    //Concession Addendum
	    
	    try
	    {
	    	if(text.contains(DFW_PDFAppConfig.concessionAddendumText))
	    	{
	    		DFW_PropertyWare.concessionAddendumFlag = true;
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
		 lateFeeRuleText = text.substring(text.indexOf(DFW_PDFAppConfig.lateFeeRuleText_Prior)+DFW_PDFAppConfig.lateFeeRuleText_Prior.length(),text.indexOf(DFW_PDFAppConfig.lateFeeRuleText_After));
		}
		catch(Exception e)
		{
			try
			{
			lateFeeRuleText = text.substring(text.indexOf(DFW_PDFAppConfig.lateFeeRuleText_Prior)+DFW_PDFAppConfig.lateFeeRuleText_Prior.length(),text.indexOf(DFW_PDFAppConfig.lateFeeRuleText_After2));
			}
			catch(Exception e2)
			{
			return false;
			}
		}
		if(lateFeeRuleText.contains(DFW_PDFAppConfig.lateFeeRule_whicheverIsGreater))
		{
			RunnerClass.lateFeeRuleType = "GreaterOfFlatFeeOrPercentage";
			RunnerClass.lateFeeType = "GreaterOfFlatFeeOrPercentage";
			//DFW_PropertyWare.lateFeeType ="Greater of Flat Fee or Percentage"; 
		//Late charge day
			try
			{
		   // DFW_PropertyWare.lateChargeDay =  lateFeeRuleText.substring(lateFeeRuleText.indexOf(DFW_PDFAppConfig.lateFeeRule_whicheverIsGreater_dueDay_Prior)+DFW_PDFAppConfig.lateFeeRule_whicheverIsGreater_dueDay_Prior.length()).trim().split(" ")[0];
				DFW_PropertyWare.lateChargeDay =  lateFeeRuleText.split(DFW_PDFAppConfig.lateFeeRule_whicheverIsGreater_dueDay_After)[0].trim();
				DFW_PropertyWare.lateChargeDay =DFW_PropertyWare.lateChargeDay.substring(DFW_PropertyWare.lateChargeDay.lastIndexOf(" ")+1);
		    DFW_PropertyWare.lateChargeDay =  DFW_PropertyWare.lateChargeDay.replaceAll("[^0-9]", "");
			}
			catch(Exception e)
			{
				DFW_PropertyWare.lateChargeDay = "Error";
			}
         System.out.println("Late Charge Day = "+DFW_PropertyWare.lateChargeDay);
			RunnerClass.dueDay_GreaterOf = DFW_PropertyWare.lateChargeDay;
		//Late Fee Percentage
			try
			{
		    DFW_PropertyWare.lateFeePercentage =  lateFeeRuleText.substring(lateFeeRuleText.indexOf(DFW_PDFAppConfig.lateFeeRule_whicheverIsGreater_lateFeePercentage)+DFW_PDFAppConfig.lateFeeRule_whicheverIsGreater_lateFeePercentage.length()).trim().split(" ")[0];
		    DFW_PropertyWare.lateFeePercentage = DFW_PropertyWare.lateFeePercentage.replaceAll("[^0-9]", "");
			}
			catch(Exception e)
			{
				DFW_PropertyWare.lateFeePercentage = "Error";
			}
         System.out.println("Late Fee Percentage = "+DFW_PropertyWare.lateFeePercentage);
         RunnerClass.percentage = DFW_PropertyWare.lateFeePercentage;
         //Late Fee Amount
         try
         {
         String lateFeeAmount  = lateFeeRuleText.substring(lateFeeRuleText.indexOf(DFW_PDFAppConfig.lateFeeRule_whicheverIsGreater_lateFeeAmount)+DFW_PDFAppConfig.lateFeeRule_whicheverIsGreater_lateFeeAmount.length()).trim().split(" ")[0];
         DFW_PropertyWare.flatFeeAmount = lateFeeAmount.replaceAll("[^.0-9]", "");
         }
         catch(Exception e)
         {
        	 DFW_PropertyWare.flatFeeAmount ="Error";
         }
         System.out.println("Late Fee Amount = "+DFW_PropertyWare.flatFeeAmount);
        RunnerClass.flatFee = DFW_PropertyWare.flatFeeAmount;
         return true;
		}
		else 
		if(lateFeeRuleText.contains(DFW_PDFAppConfig.lateFeeRule_mayNotExceedMoreThan30Days))
		{
			RunnerClass.lateFeeRuleType = "initialFeePluPerDayFee";
			//RunnerClass.lateFeeRuleType = "Initial Fee + Per Day Fee";
			
			DFW_PropertyWare.lateFeeType ="initialFeePluPerDayFee"; 
	         try
	 	    {
	 		    DFW_PropertyWare.lateChargeFee = text.substring(text.indexOf(DFW_PDFAppConfig.AB_lateFee_Prior)+DFW_PDFAppConfig.AB_lateFee_Prior.length()).trim().split(" ")[0];
	 		    //DFW_PropertyWare.lateChargeFee =  DFW_PropertyWare.lateChargeFee.substring(0,DFW_PropertyWare.lateChargeFee.length()-1);
	 	    }
	 	    catch(Exception e)
	 	    {
	 		    DFW_PropertyWare.lateChargeFee ="Error";	
	 		    e.printStackTrace();
	 	    }
	 	    System.out.println("Late Charge Fee = "+DFW_PropertyWare.lateChargeFee.trim());
	 	   RunnerClass.initialFeeAmount = DFW_PropertyWare.lateChargeFee;
	 	    //Per Day Fee
	 	    try
	 	    {
	 	    	DFW_PropertyWare.lateFeeChargePerDay = text.substring(text.indexOf(DFW_PDFAppConfig.AB_additionalLateChargesPerDay_Prior)+DFW_PDFAppConfig.AB_additionalLateChargesPerDay_Prior.length()).split(" ")[0].trim();//,text.indexOf(DFW_PDFAppConfig.AB_additionalLateChargesPerDay_After));
	 	    }
	 	    catch(Exception e)
	 	    {
	 	    	DFW_PropertyWare.lateFeeChargePerDay =  "Error";	
	 	    	e.printStackTrace();
	 	    }
	 	    System.out.println("Per Day Fee = "+DFW_PropertyWare.lateFeeChargePerDay.trim());
	 	    RunnerClass.perDayFeeAmount = DFW_PropertyWare.lateFeeChargePerDay;
	 	    //Additional Late Charges Limit
	 	    try
	 	    {
	 	    	DFW_PropertyWare.additionalLateChargesLimit = text.substring(text.indexOf(DFW_PDFAppConfig.AB_additionalLateChargesLimit_Prior)+DFW_PDFAppConfig.AB_additionalLateChargesLimit_Prior.length()).trim().split(" ")[0]; //,text.indexOf(DFW_PDFAppConfig.AB_additionalLateChargesLimit_After));
	 	    }
	 	    catch(Exception e)
	 	    {
	 	    	DFW_PropertyWare.additionalLateChargesLimit =  "Error";	
	 	    	e.printStackTrace();
	 	    }
	 	    System.out.println("additional Late Charges Limit = "+DFW_PropertyWare.additionalLateChargesLimit.trim());
	 	    RunnerClass.additionalLateChargesLimit = DFW_PropertyWare.additionalLateChargesLimit;
	 	 //Late Charge Day
			try
	 	    {
			DFW_PropertyWare.lateChargeDay = lateFeeRuleText.substring(lateFeeRuleText.indexOf("p.m. on the ")+"p.m. on the ".length()).trim().split(" ")[0];
			DFW_PropertyWare.lateChargeDay = DFW_PropertyWare.lateChargeDay.replaceAll("[^0-9]", "");
	 	    }
			catch(Exception e)
	 	    {
	 	    	DFW_PropertyWare.lateChargeDay =  "Error";	
	 	    	e.printStackTrace();
	 	    }
	 	    System.out.println("Late Charge Due Day = "+DFW_PropertyWare.lateChargeDay.trim());
	 	    RunnerClass.dueDay_initialFee = DFW_PropertyWare.lateChargeDay;
	 	   return true;
		}
		else if(lateFeeRuleText.contains(DFW_PDFAppConfig.lateFeeRule_mayNotExceedAmount))
			{
			RunnerClass.lateFeeRuleType = "initialFeePluPerDayFee";
			RunnerClass.lateFeeRuleType = "Initial Fee + Per Day Fee";
			
			//Late Charge Day
			try
	 	    {
			DFW_PropertyWare.lateChargeDay = lateFeeRuleText.substring(lateFeeRuleText.indexOf("an initial late charge on the")+"an initial late charge on the".length()).trim().split(" ")[0];
			DFW_PropertyWare.lateChargeDay = DFW_PropertyWare.lateChargeDay.replaceAll("[^0-9]", "");
	 	    }
			catch(Exception e)
	 	    {
	 	    	DFW_PropertyWare.lateChargeDay =  "Error";	
	 	    	e.printStackTrace();
	 	    }
	 	    System.out.println("Late Charge Due Day = "+DFW_PropertyWare.lateChargeDay.trim());
	 	   RunnerClass.dueDay_initialFee = DFW_PropertyWare.lateChargeDay;
	 	    // initial Late Charge
	 	   try
	 	    {
			DFW_PropertyWare.lateChargeFee = lateFeeRuleText.substring(lateFeeRuleText.indexOf("day of the month equal to $")+"day of the month equal to $".length()).trim().split(" ")[0];
			DFW_PropertyWare.lateChargeFee = DFW_PropertyWare.lateChargeFee.replaceAll("[^0-9.]", "");
	 	    }
			catch(Exception e)
	 	    {
	 	    	DFW_PropertyWare.lateChargeFee =  "Error";	
	 	    	e.printStackTrace();
	 	    }
	 	    System.out.println("Late Charge Fee = "+DFW_PropertyWare.lateChargeFee.trim());
	 	   RunnerClass.initialFeeAmount = DFW_PropertyWare.lateChargeFee;
	 	    // Additional Late Charges
	 	   try
	 	    {
			DFW_PropertyWare.additionalLateCharges = lateFeeRuleText.substring(lateFeeRuleText.indexOf("and additional late charge of $")+"and additional late charge of $".length()).trim().split(" ")[0];
			DFW_PropertyWare.additionalLateCharges = DFW_PropertyWare.additionalLateCharges.replaceAll("[^0-9.]", "");
	 	    }
			catch(Exception e)
	 	    {
	 	    	DFW_PropertyWare.additionalLateCharges =  "Error";	
	 	    	e.printStackTrace();
	 	    }
	 	    System.out.println("Additional Late Charges = "+DFW_PropertyWare.additionalLateCharges.trim());
	 	   RunnerClass.maximumAmount = DFW_PropertyWare.additionalLateCharges;
	 	    //Additional Late Charges Limit
	 	   try
	 	    {
			DFW_PropertyWare.additionalLateChargesLimit = lateFeeRuleText.substring(lateFeeRuleText.indexOf("(initial and additional) may not exceed $")+"(initial and additional) may not exceed $".length()).trim().split(" ")[0];
			DFW_PropertyWare.additionalLateChargesLimit = DFW_PropertyWare.additionalLateChargesLimit.replaceAll("[^0-9.]", "");
	 	    }
			catch(Exception e)
	 	    {
	 	    	DFW_PropertyWare.additionalLateChargesLimit =  "Error";	
	 	    	e.printStackTrace();
	 	    }
	 	    System.out.println("Additional Late Charges Limit = "+DFW_PropertyWare.additionalLateChargesLimit.trim());
	 	   RunnerClass.additionalLateChargesLimit = DFW_PropertyWare.additionalLateChargesLimit;
			return true;
			}
		else 
			if(lateFeeRuleText.contains(DFW_PDFAppConfig.lateFeeRule_totalDelinquentRentDueToTheTenantAccount))
			{
				RunnerClass.lateFeeRuleType = "GreaterOfFlatFeeOrPercentage";
				RunnerClass.lateFeeType = "GreaterOfFlatFeeOrPercentage";
				
			//Late Charge Day
			try
	 	    {
			DFW_PropertyWare.lateChargeDay = lateFeeRuleText.substring(lateFeeRuleText.indexOf("place of payment on the ")+"place of payment on the ".length()).trim().split(" ")[0];
			DFW_PropertyWare.lateChargeDay = DFW_PropertyWare.lateChargeDay.replaceAll("[^0-9]", "");
	 	    }
			catch(Exception e)
	 	    {
	 	    	DFW_PropertyWare.lateChargeDay =  "Error";	
	 	    	e.printStackTrace();
	 	    }
	 	    System.out.println("Late Charge Due Day = "+DFW_PropertyWare.lateChargeDay.trim());
	 	   RunnerClass.dueDay_GreaterOf = DFW_PropertyWare.lateChargeDay;
	 	    // initial Late Charge
	 	   try
	 	    {
			DFW_PropertyWare.lateChargeFee = lateFeeRuleText.substring(lateFeeRuleText.indexOf("an initial late charge equal to ")+"an initial late charge equal to ".length()).trim().split(" ")[0];
			//DFW_PropertyWare.lateChargeFee = DFW_PropertyWare.lateChargeFee.replaceAll("[^0-9.]", "");
	 	    }
			catch(Exception e)
	 	    {
	 	    	DFW_PropertyWare.lateChargeFee =  "Error";	
	 	    	e.printStackTrace();
	 	    }
	 	    System.out.println("Late Charge Fee = "+DFW_PropertyWare.lateChargeFee.trim());
	 	   RunnerClass.percentage = DFW_PropertyWare.lateChargeFee;
	 	   /*
	 	    // Additional Late Charges
	 	   try
	 	    {
			DFW_PropertyWare.additionalLateCharges = lateFeeRuleText.substring(lateFeeRuleText.indexOf("and additional late charge of $")+"and additional late charge of $".length()).trim().split(" ")[0];
			DFW_PropertyWare.additionalLateCharges = DFW_PropertyWare.additionalLateCharges.replaceAll("[^0-9.]", "");
	 	    }
			catch(Exception e)
	 	    {
	 	    	DFW_PropertyWare.additionalLateCharges =  "Error";	
	 	    	e.printStackTrace();
	 	    }
	 	    System.out.println("Additional Late Charges = "+DFW_PropertyWare.additionalLateCharges.trim());
	 	    RunnerClass.maximumAmount = DFW_PropertyWare.additionalLateCharges;
	 	    //Additional Late Charges Limit
	 	   try
	 	    {
			DFW_PropertyWare.additionalLateChargesLimit = lateFeeRuleText.substring(lateFeeRuleText.indexOf("(initial and additional) may not exceed $")+"(initial and additional) may not exceed $".length()).trim().split(" ")[0];
			DFW_PropertyWare.additionalLateChargesLimit = DFW_PropertyWare.additionalLateChargesLimit.replaceAll("[^0-9.]", "");
	 	    }
			catch(Exception e)
	 	    {
	 	    	DFW_PropertyWare.additionalLateChargesLimit =  "Error";	
	 	    	e.printStackTrace();
	 	    }
	 	    System.out.println("Additional Late Charges Limit = "+DFW_PropertyWare.additionalLateChargesLimit.trim());
	 	    RunnerClass.additionalLateChargesLimit = DFW_PropertyWare.additionalLateChargesLimit;
			return true;
			}
			else
		   {
			DFW_PropertyWare.lateFeeType ="";
		   }
		   */
			}
		return true;		
	}



}
