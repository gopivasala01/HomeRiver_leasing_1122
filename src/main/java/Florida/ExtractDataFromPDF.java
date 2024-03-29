package Florida;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import mainPackage.InsertDataIntoDatabase;
import mainPackage.RunnerClass;

public class ExtractDataFromPDF 
{
	public static boolean petFlag;
	   
	public boolean arizona() throws Exception
	//public static void main(String args[]) throws Exception
	{
		
		FL_PropertyWare.petFlag = false;
		//FL_RunnerClass.emptyAllValues();
		File file = RunnerClass.getLastModified();
		//File file = new File("C:\\Gopi\\Projects\\Property ware\\Lease Close Outs\\PDFS\\Florida\\SINGLE FAMILY RESIDENCE OR CONDOMINIUM LEASE\\SINGLE FAMILY RESIDENCE OR CONDOMINIUM LEASE\\Lease_10.21_09.22_1556_SE_Crowberry_Dr_FL_Mullings_-_Burton (2).pdf");
		FileInputStream fis = new FileInputStream(file);
		PDDocument document = PDDocument.load(fis);
	    String text = new PDFTextStripper().getText(document);
	    FL_PropertyWare.pdfText  = text;
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
	    	FL_PropertyWare.commensementDate = text.substring(text.indexOf(PDFAppConfig.AB_commencementDate_Prior)+PDFAppConfig.AB_commencementDate_Prior.length(),text.indexOf(PDFAppConfig.AB_expirationDate_Prior));
	    	FL_PropertyWare.commensementDate = FL_PropertyWare.commensementDate.trim().replaceAll(" +", " ");
	    }
	    catch(Exception e)
	    {
	    	FL_PropertyWare.commensementDate = "Error";
	    	e.printStackTrace();
	    }
	    System.out.println("Commensement Date = "+FL_PropertyWare.commensementDate);
	   try
	    {
		   String expirationDateWaw = text.substring(text.indexOf(PDFAppConfig.AB_expirationDate_Prior)+PDFAppConfig.AB_expirationDate_Prior.length());
		   FL_PropertyWare.expirationDate =expirationDateWaw.trim().split(" ")[0]+" "+expirationDateWaw.trim().split(" ")[1]+" "+expirationDateWaw.trim().split(" ")[2];
		   FL_PropertyWare.expirationDate = FL_PropertyWare.expirationDate.trim().replaceAll(" +", " ");
	    }
	    catch(Exception e)
	    {
	    	 FL_PropertyWare.expirationDate = "Error";
	    	 e.printStackTrace();
	    }
	   System.out.println("Expiration Date = "+FL_PropertyWare.expirationDate);
	   try
	    {
		    FL_PropertyWare.proratedRent = text.substring(text.indexOf(PDFAppConfig.AB_proratedRent_Prior)+PDFAppConfig.AB_proratedRent_Prior.length(),text.indexOf(PDFAppConfig.AB_proratedRent_After));
		    if(FL_PropertyWare.proratedRent.matches(".*[a-zA-Z]+.*"))
		    {
		    	FL_PropertyWare.proratedRent = "Error";
		    }
		    		
	    }
	    catch(Exception e)
	    {
	    	FL_PropertyWare.proratedRent = "Error";
	    	e.printStackTrace();
	    }
	   System.out.println("Prorated Rent = "+FL_PropertyWare.proratedRent);
	    try
	    {
		    FL_PropertyWare.proratedRentDate = text.substring(text.indexOf(PDFAppConfig.AB_proratedRentDate_Prior)+PDFAppConfig.AB_proratedRentDate_Prior.length(),text.indexOf(PDFAppConfig.AB_proratedRentDate_After)).trim();
	    }
	    catch(Exception e)
	    {
	    	FL_PropertyWare.proratedRentDate = "Error";
	    	e.printStackTrace();
	    }
	    System.out.println("Prorated Rent Date= "+FL_PropertyWare.proratedRentDate.trim());
	    /*
	    try
	    {
		    FL_PropertyWare.monthlyRentDate = text.substring(text.indexOf(PDFAppConfig.AB_fullRentDate_Prior)+PDFAppConfig.AB_fullRentDate_Prior.length(),text.indexOf(PDFAppConfig.AB_fullRentDate_After));
		    System.out.println("Monthly Rent Date= "+FL_PropertyWare.monthlyRentDate.trim());
	    }
	    catch(Exception e)
	    {
	    	try
	    	{
	    		FL_PropertyWare.monthlyRentDate = text.substring(text.indexOf(PDFAppConfig.AB_fullRentDate_Prior)+PDFAppConfig.AB_fullRentDate_Prior.length(),text.indexOf(PDFAppConfig.AB_fullRentDate1_After));
			   	System.out.println("Monthly Rent Date= "+FL_PropertyWare.monthlyRentDate.trim());
	    	}
	    	catch(Exception e1)
		    {
		    	FL_PropertyWare.monthlyRentDate = "Error";  
		    	e1.printStackTrace();
		    }
	    }*/
	    try
	    {
		    FL_PropertyWare.monthlyRent = text.substring(text.indexOf(PDFAppConfig.AB_fullRent_Prior)+PDFAppConfig.AB_fullRent_Prior.length()).trim().split(" ")[0].trim();//,text.indexOf(PDFAppConfig.AB_fullRent_After)).substring(1).replaceAll("[^.0-9]", "");;
		    if(RunnerClass.onlyDigits(FL_PropertyWare.monthlyRent.replace(".", "").replace(",", ""))==false)
		    {
		    	FL_PropertyWare.monthlyRent = text.substring(text.indexOf(PDFAppConfig.AB_fullRent2_Prior)+PDFAppConfig.AB_fullRent2_Prior.length()).trim().split(" ")[0].trim();
		    }
		    if(FL_PropertyWare.monthlyRent.contains("*"))
		    {
		    	FL_PropertyWare.monthlyRent = FL_PropertyWare.monthlyRent.replace("*","");
		    }
		    if(FL_PropertyWare.monthlyRent.matches(".*[a-zA-Z]+.*"))
		    {
		    	FL_PropertyWare.monthlyRent = "Error";
		    }
	    }
	    catch(Exception e)
	    {
	    	 FL_PropertyWare.monthlyRent = "Error";
	    	 e.printStackTrace();
	    }
	    System.out.println("Monthly Rent "+FL_PropertyWare.monthlyRent.trim());
	    try
	    {
		    FL_PropertyWare.adminFee = text.substring(text.indexOf(PDFAppConfig.AB_adminFee_Prior)+PDFAppConfig.AB_adminFee_Prior.length()).split(" ")[0];
		    if(FL_PropertyWare.adminFee.matches(".*[a-zA-Z]+.*"))
		    {
		    	FL_PropertyWare.adminFee = "Error";
		    }
	    }
	    catch(Exception e)
	    {
		    FL_PropertyWare.adminFee = "Error";
		    e.printStackTrace();
	    }
	    System.out.println("Admin Fee = "+FL_PropertyWare.adminFee.trim());
	    
	  //Resident Benefits Package 
	    if(text.contains(PDFAppConfig.residentBenefitsPackageAddendumCheck))
	    {
	    	FL_PropertyWare.residentBenefitsPackageAvailabilityCheck = true;
	    	 try
	 	    {
	 		    FL_PropertyWare.residentBenefitsPackage = text.substring(text.indexOf(PDFAppConfig.AB1_residentBenefitsPackage_Prior)+PDFAppConfig.AB1_residentBenefitsPackage_Prior.length()).split(" ")[0].replaceAll("[^0-9a-zA-Z.]", "");
	 		    if(FL_PropertyWare.residentBenefitsPackage.matches(".*[a-zA-Z]+.*"))
	 		    {
	 		    	FL_PropertyWare.residentBenefitsPackage = "Error";
	 		    }
	 	    }
	 	    catch(Exception e)
	 	    {
	 		    FL_PropertyWare.residentBenefitsPackage = "Error";
	 		    e.printStackTrace();
	 	    }
	    	 System.out.println("Resident Benefits Package  = "+FL_PropertyWare.residentBenefitsPackage.trim());
	    }
	    else
	    {
	    
		    if(text.contains(PDFAppConfig_Format2.HVACFilterAddendumTextAvailabilityCheck)==true)
		    {
		    	FL_PropertyWare.HVACFilterFlag =true;
		    }
		    else
		    {
		    try
		    {
			   String[] airFilterFeeArray = text.substring(text.indexOf(PDFAppConfig.AB_airFilterFee_Prior)+PDFAppConfig.AB_airFilterFee_Prior.length()).split(" ");
			   FL_PropertyWare.airFilterFee = airFilterFeeArray[0];
			   if(FL_PropertyWare.airFilterFee.matches(".*[a-zA-Z]+.*"))
			    {
			    	FL_PropertyWare.airFilterFee = "Error";
			    }
		    }
		    catch(Exception e)
		    {
		    FL_PropertyWare.airFilterFee = "Error";
		    e.printStackTrace();
		    }
		    }
		    System.out.println("Air Filter Fee = "+FL_PropertyWare.airFilterFee.trim());
	    }
	    try
	    {
	    	String[] earlyTerminationRaw = text.substring(text.indexOf(PDFAppConfig.AB_earlyTerminationFee_Prior)+PDFAppConfig.AB_earlyTerminationFee_Prior.length()).split(" ");
	    	
		    FL_PropertyWare.earlyTermination = earlyTerminationRaw[0]+earlyTerminationRaw[1]; //text.substring(text.indexOf(PDFAppConfig.AB_earlyTerminationFee_Prior)+PDFAppConfig.AB_earlyTerminationFee_Prior.length(),text.indexOf(PDFAppConfig.AB_earlyTerminationFee_After));
	    }
	    catch(Exception e)
	    {
	    	FL_PropertyWare.earlyTermination = "Error";	
	    	e.printStackTrace();
	    }
	    System.out.println("Early Termination  = "+FL_PropertyWare.earlyTermination.trim());
	    try
	    {
	    	
		    FL_PropertyWare.occupants = text.substring(text.indexOf(PDFAppConfig.AB_occupants_Prior)+PDFAppConfig.AB_occupants_Prior.length(),text.indexOf(PDFAppConfig.AB_occupants_After));
	    }
	    catch(Exception e)
	    {
		    FL_PropertyWare.occupants ="Error";	
		    e.printStackTrace();
	    }
	    System.out.println("Occupants = "+FL_PropertyWare.occupants.trim());
	    try
	    {
		    FL_PropertyWare.lateChargeDay = text.substring(text.indexOf(PDFAppConfig.AB_lateChargeDay_Prior)+PDFAppConfig.AB_lateChargeDay_Prior.length(),text.indexOf(PDFAppConfig.AB_lateChargeDay_After));
	        if(FL_PropertyWare.lateChargeDay.contains("rd"))
	        {
	        	FL_PropertyWare.lateChargeDay=FL_PropertyWare.lateChargeDay.trim().replace("rd", "");
	        }
	        else if(FL_PropertyWare.lateChargeDay.contains("st"))
	        {
	        	FL_PropertyWare.lateChargeDay=FL_PropertyWare.lateChargeDay.trim().replace("st", "");
	        }
	    }
	    catch(Exception e)
	    {
	    	FL_PropertyWare.lateChargeDay = "Error";	
	    	e.printStackTrace();
	    }
	    System.out.println("Late Charge Day = "+FL_PropertyWare.lateChargeDay.trim());
	    try
	    {
		    FL_PropertyWare.lateChargeFee = text.substring(text.indexOf(PDFAppConfig.AB_lateFee_Prior)+PDFAppConfig.AB_lateFee_Prior.length()).split(" ")[0];
		    //FL_PropertyWare.lateChargeFee =  FL_PropertyWare.lateChargeFee.substring(0,FL_PropertyWare.lateChargeFee.length()-1);
	    }
	    catch(Exception e)
	    {
		    FL_PropertyWare.lateChargeFee ="Error";	
		    e.printStackTrace();
	    }
	    System.out.println("Late Charge Fee = "+FL_PropertyWare.lateChargeFee.trim());
	    //Per Day Fee
	    try
	    {
	    	FL_PropertyWare.lateFeeChargePerDay = text.substring(text.indexOf(PDFAppConfig.AB_additionalLateChargesPerDay_Prior)+PDFAppConfig.AB_additionalLateChargesPerDay_Prior.length()).split(" ")[0].trim();//,text.indexOf(PDFAppConfig.AB_additionalLateChargesPerDay_After));
	    }
	    catch(Exception e)
	    {
	    	FL_PropertyWare.lateFeeChargePerDay =  "Error";	
	    	e.printStackTrace();
	    }
	    System.out.println("Per Day Fee = "+FL_PropertyWare.lateFeeChargePerDay.trim());
	    //Additional Late Charges Limit
	    try
	    {
	    	FL_PropertyWare.additionalLateChargesLimit = text.substring(text.indexOf(PDFAppConfig.AB_additionalLateChargesLimit_Prior)+PDFAppConfig.AB_additionalLateChargesLimit_Prior.length()).split(" ")[0]; //,text.indexOf(PDFAppConfig.AB_additionalLateChargesLimit_After));
	    }
	    catch(Exception e)
	    {
	    	FL_PropertyWare.additionalLateChargesLimit =  "Error";	
	    	e.printStackTrace();
	    }
	    System.out.println("additional Late Charges Limit = "+FL_PropertyWare.additionalLateChargesLimit.trim());
	    
	    
	    
	  //Prepayment Charge
  		if(FL_PropertyWare.portfolioType.contains("MCH"))
  		{
  			if(FL_PropertyWare.proratedRent.equalsIgnoreCase("n/a")||FL_PropertyWare.proratedRent.equalsIgnoreCase("Error")||FL_PropertyWare.proratedRent.equalsIgnoreCase(""))
  			{
  				FL_PropertyWare.prepaymentCharge = "Error";
  			}
  			else
  			{
	  		try
	  		{
	  		FL_PropertyWare.prepaymentCharge =String.valueOf(Double.parseDouble(FL_PropertyWare.monthlyRent.replace(",", "")) - Double.parseDouble(FL_PropertyWare.proratedRent.replace(",", ""))); 
	  		}
	  		catch(Exception e)
	  		{
	  			FL_PropertyWare.prepaymentCharge ="Error";
	  		}
	  		}
  			System.out.println("Prepayment Charge = "+FL_PropertyWare.prepaymentCharge);
  		 }
	    petFlag = text.contains(PDFAppConfig.AB_petAgreementAvailabilityCheck);
	    System.out.println("Pet Addendum Available = "+petFlag);
	    if(petFlag ==true)
	    {
	    	FL_PropertyWare.petFlag = true;
	    	try
	    	{
	    	FL_PropertyWare.petSecurityDeposit = text.substring(text.indexOf(PDFAppConfig.AB_securityDeposity_Prior)+PDFAppConfig.AB_securityDeposity_Prior.length(),text.indexOf(PDFAppConfig.AB_securityDeposity_After));
	    	if(FL_PropertyWare.petSecurityDeposit.matches(".*[a-zA-Z]+.*"))
		    {
		    	FL_PropertyWare.petSecurityDeposit = "Error";
		    }
	    	}
	    	catch(Exception e)
	    	{
	    	FL_PropertyWare.petSecurityDeposit = "Error";	
	    	e.printStackTrace();
	    	}
	    	System.out.println("Pet Security Deposit = "+FL_PropertyWare.petSecurityDeposit.trim());
	    	if(RunnerClass.onlyDigits(FL_PropertyWare.petSecurityDeposit.replace(".", ""))==true)
		    {
		    	System.out.println("Security Deposit is checked");
		    }
	    	//TODO Check
	    	  try
			    {
	    		  String proratedPetRaw = "Prorated Pet Rent: On or before "+FL_PropertyWare.commensementDate.trim()+" Tenant will pay Landlord $";
	    		FL_PropertyWare.proratedPetRent = text.substring(text.indexOf(proratedPetRaw)+proratedPetRaw.length()).trim().split(" ")[0];//.replaceAll("[a-ZA-Z,]", "");
			    //AR_PropertyWare.proratedPetRent = proratedPetRentRaw.substring(proratedPetRentRaw.indexOf("Tenant will pay Landlord $")+"Tenant will pay Landlord $".length());//,proratedPetRentRaw.indexOf(AppConfig.AR_proratedPetRent_After));
			    if(FL_PropertyWare.proratedPetRent.matches(".*[a-zA-Z]+.*"))
			    {
			    	FL_PropertyWare.proratedPetRent = "Error";
			    }
			    }
			    catch(Exception e)
			    {
			   
			    FL_PropertyWare.proratedPetRent = "Error";	
			    e.printStackTrace();
			    }
	    	  System.out.println("Prorated Pet Rent = "+FL_PropertyWare.proratedPetRent.trim());
	    	
	    	try
		    {
	    		 FL_PropertyWare.petRent = text.substring(text.indexOf(PDFAppConfig.AB_petRent_Prior)+PDFAppConfig.AB_petRent_Prior.length()).trim().split(" ")[0];
	    		 if(FL_PropertyWare.petRent.contains(",for"))
	    		 {
	    			 FL_PropertyWare.petRent = FL_PropertyWare.petRent.split(",")[0].trim();
	    		 }
	    		 else
	    		 {
		    		 if(FL_PropertyWare.petRent.matches(".*[a-zA-Z]+.*")==true)
		    			 FL_PropertyWare.petRent = text.substring(text.indexOf(PDFAppConfig.AB_petRent1_Prior)+PDFAppConfig.AB_petRent1_Prior.length()).trim().split(" ")[0];
		    		 else 
		    		 FL_PropertyWare.petRent = RunnerClass.extractNumber(FL_PropertyWare.petRent);
	    		 }
		    }
	    	catch(Exception e)
		    {
	    		try
	    		{
	    			e.printStackTrace();
	    			FL_PropertyWare.petRent = text.substring(text.indexOf(PDFAppConfig.AB_petRent1_Prior)+PDFAppConfig.AB_petRent1_Prior.length()).trim().split(" ")[0];
//					 System.out.println("Pet rent = "+FL_PropertyWare.petRent.trim());
	    			if(FL_PropertyWare.petRent.matches(".*[a-zA-Z]+.*"))
	    		    {
	    		    	FL_PropertyWare.petRent = "Error";
	    		    }
	    		}
	    		
	    		catch(Exception e1)
			    {
			    	FL_PropertyWare.petRent = "Error";  
			    	e1.printStackTrace();
			    }
		    }
	    	System.out.println("Pet rent = "+FL_PropertyWare.petRent.trim());
		    	//FL_PropertyWare.petRent = "Error";  
		    	//e.printStackTrace();
		   /* 
	    	try
    		{
    			//String petFeeRaw1 = text.substring(text.indexOf(PDFAppConfig.AB_petFee_Prior));
    			FL_PropertyWare.petFee = text.substring(text.indexOf(PDFAppConfig.AB_petFee_Prior)+PDFAppConfig.AB_petFee_Prior.length()).trim().split(" ")[0].trim();
    			//FL_PropertyWare.petFee =  petFeeRaw[petFeeRaw.length-2].trim();
    			//if(FL_PropertyWare.petFee.matches(".*[a-zA-Z]+.*"))
    			//{
    				//FL_PropertyWare.petFee = text.substring(text.indexOf(PDFAppConfig.AB_petFee2_Prior)+PDFAppConfig.AB_petFee2_Prior.length()).trim().split(" ")[0].trim();
    			//}
    			//System.out.println(petFeeRaw.length);
    		}
    		
    		catch(Exception e1)
		    {
		    	FL_PropertyWare.petFee = "Error";  
		    	e1.printStackTrace();
		    }
	    	System.out.println("Pet Fee = "+FL_PropertyWare.petFee);
	    	*/
	    	// Get text between Type: word
	    	
	    	String typeSubString = text.substring(text.indexOf(PDFAppConfig.AB_typeWord_Prior)+PDFAppConfig.AB_typeWord_Prior.length(),text.indexOf(PDFAppConfig.AB_typeWord_After));
	    	
	    	String newText = typeSubString.replace("Type:","");
		    FL_PropertyWare.countOfTypeWordInText = ((typeSubString.length() - newText.length())/"Type:".length());
		    System.out.println("Type: occurences = "+FL_PropertyWare.countOfTypeWordInText);
		    
		    FL_PropertyWare.petType = new ArrayList();
		    FL_PropertyWare.petBreed = new ArrayList();
		    FL_PropertyWare.petWeight = new ArrayList();
		    for(int i =0;i<FL_PropertyWare.countOfTypeWordInText;i++)
		    {
		    	String type = typeSubString.substring(RunnerClass.nthOccurrence(typeSubString, "Type:", i+1)+PDFAppConfig.AB_pet1Type_Prior.length(),RunnerClass.nthOccurrence(typeSubString, "Breed:", i+1)).trim();
		    	if(type.contains("N/A")||type.contains("n/a"))
		    		break;
		    	System.out.println(type);
		    	FL_PropertyWare.petType.add(type);
		    	int pet1Breedindex1 = RunnerClass.nthOccurrence(typeSubString, "Breed:", i+1)+"Breed:".length()+1;
			    String subString = typeSubString.substring(pet1Breedindex1);
			    //int pet1Breedindex2 = RunnerClass.nthOccurrence(subString,"Name:",i+1);
			   // System.out.println("Index 2 = "+(index2+index1));
			    String breed = subString.split("Name:")[0].trim();//typeSubString.substring(pet1Breedindex1,(pet1Breedindex2+pet1Breedindex1));
			    System.out.println(breed);
			    FL_PropertyWare.petBreed.add(breed);
			    int pet1Weightindex1 = RunnerClass.nthOccurrence(typeSubString, "Weight:", i+1)+"Weight:".length()+1;
			    String pet1WeightSubstring = typeSubString.substring(pet1Weightindex1);
			    //int pet1WeightIndex2 = RunnerClass.nthOccurrence(pet1WeightSubstring,"Age:",i+1);
			   // System.out.println("Index 2 = "+(index2+index1));
			    String weight = pet1WeightSubstring.split("Age:")[0].trim(); //typeSubString.substring(pet1Weightindex1,(pet1WeightIndex2+pet1Weightindex1));
			    System.out.println(weight);
			    FL_PropertyWare.petWeight.add(weight);
		    }
		    
		    
		    
		    /*
		    if(FL_PropertyWare.countOfTypeWordInText>2)
	        {
			    try
			    {
			    FL_PropertyWare.serviceAnimalType = text.substring(RunnerClass.nthOccurrence(text, "Type:", 3)+PDFAppConfig.AB_pet1Type_Prior.length(),RunnerClass.nthOccurrence(text, "Breed:", 3));
			    }
			    catch(Exception e)
			    {
			    	 FL_PropertyWare.serviceAnimalType = "Error";
			    	 e.printStackTrace();
			    }
			    System.out.println("Service Animal Type = "+FL_PropertyWare.serviceAnimalType);
			    try
			    {
				    int serviceAnimalBreedindex1 = RunnerClass.nthOccurrence(text, "Breed:", 3)+"Breed:".length()+1;
				    String serviceAnimalsubString = text.substring(serviceAnimalBreedindex1);
				    int serviceAnimalBreedindex2 = RunnerClass.nthOccurrence(serviceAnimalsubString,"Name:",1);
				   // System.out.println("Index 2 = "+(index2+index1));
				    FL_PropertyWare.serviceAnimalBreed = text.substring(serviceAnimalBreedindex1,(serviceAnimalBreedindex2+serviceAnimalBreedindex1));
			    }
			    catch(Exception e)
			    {
			    	FL_PropertyWare.serviceAnimalBreed = "Error";
			    	e.printStackTrace();
			    }
			    System.out.println("Service Animal Breed = "+FL_PropertyWare.serviceAnimalBreed);
		  
			    try
			    {
			    int serviceAnimalWeightindex1 = RunnerClass.nthOccurrence(text, "Weight:", 3)+"Weight:".length()+1;
			    String serviceAnimalWeightSubstring = text.substring(serviceAnimalWeightindex1);
			    int serviceAnimalWeightIndex2 = RunnerClass.nthOccurrence(serviceAnimalWeightSubstring,"Age:",1);
			   // System.out.println("Index 2 = "+(index2+index1));
			    FL_PropertyWare.serviceAnimalWeight = text.substring(serviceAnimalWeightindex1,(serviceAnimalWeightIndex2+serviceAnimalWeightindex1));
			    }
			    catch(Exception e)
			    {
			    	FL_PropertyWare.serviceAnimalWeight = "Error";
			    	e.printStackTrace();
			    }  
			    System.out.println("Service Animal Weight = "+FL_PropertyWare.serviceAnimalWeight);
	        }
	        */
		    try
		    {
		    	FL_PropertyWare.petOneTimeNonRefundableFee = text.substring(text.indexOf(PDFAppConfig.AB_petFeeOneTime_Prior)+PDFAppConfig.AB_petFeeOneTime_Prior.length()).split(" ")[0];//,text.indexOf(PDFAppConfig.AB_petFeeOneTime_After));
		    	if(FL_PropertyWare.petOneTimeNonRefundableFee.matches(".*[a-zA-Z]+.*"))
    		    {
    		    	FL_PropertyWare.petOneTimeNonRefundableFee = "Error";
    		    }
		    }
		    catch(Exception e)
		    {
		    	FL_PropertyWare.petOneTimeNonRefundableFee =  "Error";
		    	e.printStackTrace();
		    }  
		    System.out.println("pet one time non refundable = "+FL_PropertyWare.petOneTimeNonRefundableFee.trim());
		   
	    }
	    
	    //Service Animal Addendum check
	    try
	    {
	    if(text.contains(AppConfig.serviceAnimalText))
	    {
	    	FL_PropertyWare.serviceAnimalFlag = true;
    		System.out.println("Service Animal Addendum is available");
    		
            String typeSubString = text.substring(text.indexOf(PDFAppConfig.AB_serviceAnimal_typeWord_Prior)+PDFAppConfig.AB_serviceAnimal_typeWord_Prior.length(),text.indexOf(PDFAppConfig.AB_serviceAnimal_typeWord_After));
	    	
	    	String newText = typeSubString.replace("Type:","");
		    int  countOftypeWords_ServiceAnimal = ((typeSubString.length() - newText.length())/"Type:".length());
		    System.out.println("Service Animal - Type: occurences = "+countOftypeWords_ServiceAnimal);
		    
		    FL_PropertyWare.serviceAnimalPetType = new ArrayList();
		    FL_PropertyWare.serviceAnimalPetBreed = new ArrayList();
		    FL_PropertyWare.serviceAnimalPetWeight = new ArrayList();
		    for(int i =0;i<countOftypeWords_ServiceAnimal;i++)
		    {
		    	String type = typeSubString.substring(RunnerClass.nthOccurrence(typeSubString, "Type:", i+1)+PDFAppConfig.AB_pet1Type_Prior.length(),RunnerClass.nthOccurrence(typeSubString, "Breed:", i+1)).trim();
		    	if(type.contains("N/A")||type.contains("n/a"))
		    		break;
		    	System.out.println(type);
		    	FL_PropertyWare.serviceAnimalPetType.add(type);
		    	int pet1Breedindex1 = RunnerClass.nthOccurrence(typeSubString, "Breed:", i+1)+"Breed:".length()+1;
			    String subString = typeSubString.substring(pet1Breedindex1);
			    //int pet1Breedindex2 = RunnerClass.nthOccurrence(subString,"Name:",i+1);
			   // System.out.println("Index 2 = "+(index2+index1));
			    String breed = subString.split("Name:")[0].trim();//typeSubString.substring(pet1Breedindex1,(pet1Breedindex2+pet1Breedindex1));
			    System.out.println(breed);
			    FL_PropertyWare.serviceAnimalPetBreed.add(breed);
			    int pet1Weightindex1 = RunnerClass.nthOccurrence(typeSubString, "Weight:", i+1)+"Weight:".length()+1;
			    String pet1WeightSubstring = typeSubString.substring(pet1Weightindex1);
			    //int pet1WeightIndex2 = RunnerClass.nthOccurrence(pet1WeightSubstring,"Age:",i+1);
			   // System.out.println("Index 2 = "+(index2+index1));
			    String weight = pet1WeightSubstring.split("Age:")[0].trim(); //typeSubString.substring(pet1Weightindex1,(pet1WeightIndex2+pet1Weightindex1));
			    System.out.println(weight);
			    FL_PropertyWare.serviceAnimalPetWeight.add(weight);
		    }
    		
    		
	    }
	    }
	    catch(Exception e)
	    {
	    	FL_PropertyWare.serviceAnimalFlag = false;
	    }
	    //Concession Addendum
	    
	    try
	    {
	    	if(text.contains(PDFAppConfig.concessionAddendumText))
	    	{
	    		FL_PropertyWare.concessionAddendumFlag = true;
	    		System.out.println("Concession Addendum is available");
	    	}
	    }
	    catch(Exception e)
	    {}
	    
	  document.close();
	    return true;
    }

}
