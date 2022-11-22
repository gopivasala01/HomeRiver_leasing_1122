package NorthCarolina;

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
		
		NC_PropertyWare.petFlag = false;
		//FL_RunnerClass.emptyAllValues();
		File file = RunnerClass.getLastModified();
		//File file = new File("C:\\Gopi\\Projects\\Property ware\\Lease Close Outs\\PDFS\\Florida\\SINGLE FAMILY RESIDENCE OR CONDOMINIUM LEASE\\SINGLE FAMILY RESIDENCE OR CONDOMINIUM LEASE\\Lease_10.21_09.22_1556_SE_Crowberry_Dr_FL_Mullings_-_Burton (2).pdf");
		FileInputStream fis = new FileInputStream(file);
		PDDocument document = PDDocument.load(fis);
	    String text = new PDFTextStripper().getText(document);
	    NC_PropertyWare.pdfText  = text;
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
	    	NC_PropertyWare.commensementDate = text.substring(text.indexOf(PDFAppConfig.AB_commencementDate_Prior)+PDFAppConfig.AB_commencementDate_Prior.length(),text.indexOf(PDFAppConfig.AB_expirationDate_Prior));
	    	NC_PropertyWare.commensementDate = NC_PropertyWare.commensementDate.trim().replaceAll(" +", " ");
	    }
	    catch(Exception e)
	    {
	    	NC_PropertyWare.commensementDate = "Error";
	    	e.printStackTrace();
	    }
	    System.out.println("Commensement Date = "+NC_PropertyWare.commensementDate);
	   try
	    {
		   String expirationDateWaw = text.substring(text.indexOf(PDFAppConfig.AB_expirationDate_Prior)+PDFAppConfig.AB_expirationDate_Prior.length());
		   NC_PropertyWare.expirationDate =expirationDateWaw.trim().split(" ")[0]+" "+expirationDateWaw.trim().split(" ")[1]+" "+expirationDateWaw.trim().split(" ")[2];
		   NC_PropertyWare.expirationDate = NC_PropertyWare.expirationDate.trim().replaceAll(" +", " ");
	    }
	    catch(Exception e)
	    {
	    	 NC_PropertyWare.expirationDate = "Error";
	    	 e.printStackTrace();
	    }
	   System.out.println("Expiration Date = "+NC_PropertyWare.expirationDate);
	   try
	    {
		    NC_PropertyWare.proratedRent = text.substring(text.indexOf(PDFAppConfig.AB_proratedRent_Prior)+PDFAppConfig.AB_proratedRent_Prior.length(),text.indexOf(PDFAppConfig.AB_proratedRent_After));
		    if(NC_PropertyWare.proratedRent.matches(".*[a-zA-Z]+.*"))
		    {
		    	NC_PropertyWare.proratedRent = "Error";
		    }
		    		
	    }
	    catch(Exception e)
	    {
	    	NC_PropertyWare.proratedRent = "Error";
	    	e.printStackTrace();
	    }
	   System.out.println("Prorated Rent = "+NC_PropertyWare.proratedRent);
	    try
	    {
		    NC_PropertyWare.proratedRentDate = text.substring(text.indexOf(PDFAppConfig.AB_proratedRentDate_Prior)+PDFAppConfig.AB_proratedRentDate_Prior.length(),text.indexOf(PDFAppConfig.AB_proratedRentDate_After)).trim();
	    }
	    catch(Exception e)
	    {
	    	NC_PropertyWare.proratedRentDate = "Error";
	    	e.printStackTrace();
	    }
	    System.out.println("Prorated Rent Date= "+NC_PropertyWare.proratedRentDate.trim());
	    /*
	    try
	    {
		    NC_PropertyWare.monthlyRentDate = text.substring(text.indexOf(PDFAppConfig.AB_fullRentDate_Prior)+PDFAppConfig.AB_fullRentDate_Prior.length(),text.indexOf(PDFAppConfig.AB_fullRentDate_After));
		    System.out.println("Monthly Rent Date= "+NC_PropertyWare.monthlyRentDate.trim());
	    }
	    catch(Exception e)
	    {
	    	try
	    	{
	    		NC_PropertyWare.monthlyRentDate = text.substring(text.indexOf(PDFAppConfig.AB_fullRentDate_Prior)+PDFAppConfig.AB_fullRentDate_Prior.length(),text.indexOf(PDFAppConfig.AB_fullRentDate1_After));
			   	System.out.println("Monthly Rent Date= "+NC_PropertyWare.monthlyRentDate.trim());
	    	}
	    	catch(Exception e1)
		    {
		    	NC_PropertyWare.monthlyRentDate = "Error";  
		    	e1.printStackTrace();
		    }
	    }*/
	    try
	    {
		    NC_PropertyWare.monthlyRent = text.substring(text.indexOf(PDFAppConfig.AB_fullRent_Prior)+PDFAppConfig.AB_fullRent_Prior.length()).trim().split(" ")[0].trim();//,text.indexOf(PDFAppConfig.AB_fullRent_After)).substring(1).replaceAll("[^.0-9]", "");;
		    if(RunnerClass.onlyDigits(NC_PropertyWare.monthlyRent.replace(".", "").replace(",", ""))==false)
		    {
		    	NC_PropertyWare.monthlyRent = text.substring(text.indexOf(PDFAppConfig.AB_fullRent2_Prior)+PDFAppConfig.AB_fullRent2_Prior.length()).trim().split(" ")[0].trim();
		    }
		    if(NC_PropertyWare.monthlyRent.contains("*"))
		    {
		    	NC_PropertyWare.monthlyRent = NC_PropertyWare.monthlyRent.replace("*","");
		    }
		    if(NC_PropertyWare.monthlyRent.matches(".*[a-zA-Z]+.*"))
		    {
		    	NC_PropertyWare.monthlyRent = "Error";
		    }
	    }
	    catch(Exception e)
	    {
	    	 NC_PropertyWare.monthlyRent = "Error";
	    	 e.printStackTrace();
	    }
	    System.out.println("Monthly Rent "+NC_PropertyWare.monthlyRent.trim());
	    try
	    {
		    NC_PropertyWare.adminFee = text.substring(text.indexOf(PDFAppConfig.AB_adminFee_Prior)+PDFAppConfig.AB_adminFee_Prior.length()).split(" ")[0];
		    if(NC_PropertyWare.adminFee.matches(".*[a-zA-Z]+.*"))
		    {
		    	NC_PropertyWare.adminFee = "Error";
		    }
	    }
	    catch(Exception e)
	    {
		    NC_PropertyWare.adminFee = "Error";
		    e.printStackTrace();
	    }
	    System.out.println("Admin Fee = "+NC_PropertyWare.adminFee.trim());
	    try
	    {
		   String[] airFilterFeeArray = text.substring(text.indexOf(PDFAppConfig.AB_airFilterFee_Prior)+PDFAppConfig.AB_airFilterFee_Prior.length()).split(" ");
		   NC_PropertyWare.airFilterFee = airFilterFeeArray[0];
		   if(NC_PropertyWare.airFilterFee.matches(".*[a-zA-Z]+.*"))
		    {
		    	NC_PropertyWare.airFilterFee = "Error";
		    }
	    }
	    catch(Exception e)
	    {
	    NC_PropertyWare.airFilterFee = "Error";
	    e.printStackTrace();
	    }
	    System.out.println("Air Filter Fee = "+NC_PropertyWare.airFilterFee.trim());
	    try
	    {
	    	String[] earlyTerminationRaw = text.substring(text.indexOf(PDFAppConfig.AB_earlyTerminationFee_Prior)+PDFAppConfig.AB_earlyTerminationFee_Prior.length()).split(" ");
	    	
		    NC_PropertyWare.earlyTermination = earlyTerminationRaw[0]+earlyTerminationRaw[1]; //text.substring(text.indexOf(PDFAppConfig.AB_earlyTerminationFee_Prior)+PDFAppConfig.AB_earlyTerminationFee_Prior.length(),text.indexOf(PDFAppConfig.AB_earlyTerminationFee_After));
	    }
	    catch(Exception e)
	    {
	    	NC_PropertyWare.earlyTermination = "Error";	
	    	e.printStackTrace();
	    }
	    System.out.println("Early Termination  = "+NC_PropertyWare.earlyTermination.trim());
	    try
	    {
	    	
		    NC_PropertyWare.occupants = text.substring(text.indexOf(PDFAppConfig.AB_occupants_Prior)+PDFAppConfig.AB_occupants_Prior.length(),text.indexOf(PDFAppConfig.AB_occupants_After));
	    }
	    catch(Exception e)
	    {
		    NC_PropertyWare.occupants ="Error";	
		    e.printStackTrace();
	    }
	    System.out.println("Occupants = "+NC_PropertyWare.occupants.trim());
	    try
	    {
		    NC_PropertyWare.lateChargeDay = text.substring(text.indexOf(PDFAppConfig.AB_lateChargeDay_Prior)+PDFAppConfig.AB_lateChargeDay_Prior.length(),text.indexOf(PDFAppConfig.AB_lateChargeDay_After));
	        if(NC_PropertyWare.lateChargeDay.contains("rd"))
	        {
	        	NC_PropertyWare.lateChargeDay=NC_PropertyWare.lateChargeDay.trim().replace("rd", "");
	        }
	        else if(NC_PropertyWare.lateChargeDay.contains("st"))
	        {
	        	NC_PropertyWare.lateChargeDay=NC_PropertyWare.lateChargeDay.trim().replace("st", "");
	        }
	    }
	    catch(Exception e)
	    {
	    	NC_PropertyWare.lateChargeDay = "Error";	
	    	e.printStackTrace();
	    }
	    System.out.println("Late Charge Day = "+NC_PropertyWare.lateChargeDay.trim());
	    try
	    {
		    NC_PropertyWare.lateChargeFee = text.substring(text.indexOf(PDFAppConfig.AB_lateFee_Prior)+PDFAppConfig.AB_lateFee_Prior.length()).split(" ")[0];
		    //NC_PropertyWare.lateChargeFee =  NC_PropertyWare.lateChargeFee.substring(0,NC_PropertyWare.lateChargeFee.length()-1);
	    }
	    catch(Exception e)
	    {
		    NC_PropertyWare.lateChargeFee ="Error";	
		    e.printStackTrace();
	    }
	    System.out.println("Late Charge Fee = "+NC_PropertyWare.lateChargeFee.trim());
	    //Per Day Fee
	    try
	    {
	    	NC_PropertyWare.lateFeeChargePerDay = text.substring(text.indexOf(PDFAppConfig.AB_additionalLateChargesPerDay_Prior)+PDFAppConfig.AB_additionalLateChargesPerDay_Prior.length()).split(" ")[0].trim();//,text.indexOf(PDFAppConfig.AB_additionalLateChargesPerDay_After));
	    }
	    catch(Exception e)
	    {
	    	NC_PropertyWare.lateFeeChargePerDay =  "Error";	
	    	e.printStackTrace();
	    }
	    System.out.println("Per Day Fee = "+NC_PropertyWare.lateFeeChargePerDay.trim());
	    //Additional Late Charges Limit
	    try
	    {
	    	NC_PropertyWare.additionalLateChargesLimit = text.substring(text.indexOf(PDFAppConfig.AB_additionalLateChargesLimit_Prior)+PDFAppConfig.AB_additionalLateChargesLimit_Prior.length()).split(" ")[0]; //,text.indexOf(PDFAppConfig.AB_additionalLateChargesLimit_After));
	    }
	    catch(Exception e)
	    {
	    	NC_PropertyWare.additionalLateChargesLimit =  "Error";	
	    	e.printStackTrace();
	    }
	    System.out.println("additional Late Charges Limit = "+NC_PropertyWare.additionalLateChargesLimit.trim());
	    
	    
	    
	  //Prepayment Charge
  		if(NC_PropertyWare.portfolioType.contains("MCH"))
  		{
  			if(NC_PropertyWare.proratedRent.equalsIgnoreCase("n/a")||NC_PropertyWare.proratedRent.equalsIgnoreCase("Error")||NC_PropertyWare.proratedRent.equalsIgnoreCase(""))
  			{
  				NC_PropertyWare.prepaymentCharge = "Error";
  			}
  			else
  			{
	  		try
	  		{
	  		NC_PropertyWare.prepaymentCharge =String.valueOf(Double.parseDouble(NC_PropertyWare.monthlyRent.replace(",", "")) - Double.parseDouble(NC_PropertyWare.proratedRent.replace(",", ""))); 
	  		}
	  		catch(Exception e)
	  		{
	  			NC_PropertyWare.prepaymentCharge ="Error";
	  		}
	  		}
  			System.out.println("Prepayment Charge = "+NC_PropertyWare.prepaymentCharge);
  		 }
	    petFlag = text.contains(PDFAppConfig.AB_petAgreementAvailabilityCheck);
	    System.out.println("Pet Addendum Available = "+petFlag);
	    if(petFlag ==true)
	    {
	    	NC_PropertyWare.petFlag = true;
	    	try
	    	{
	    	NC_PropertyWare.petSecurityDeposit = text.substring(text.indexOf(PDFAppConfig.AB_securityDeposity_Prior)+PDFAppConfig.AB_securityDeposity_Prior.length(),text.indexOf(PDFAppConfig.AB_securityDeposity_After));
	    	if(NC_PropertyWare.petSecurityDeposit.matches(".*[a-zA-Z]+.*"))
		    {
		    	NC_PropertyWare.petSecurityDeposit = "Error";
		    }
	    	}
	    	catch(Exception e)
	    	{
	    	NC_PropertyWare.petSecurityDeposit = "Error";	
	    	e.printStackTrace();
	    	}
	    	System.out.println("Pet Security Deposit = "+NC_PropertyWare.petSecurityDeposit.trim());
	    	if(RunnerClass.onlyDigits(NC_PropertyWare.petSecurityDeposit.replace(".", ""))==true)
		    {
		    	System.out.println("Security Deposit is checked");
		    }
	    	//TODO Check
	    	  try
			    {
	    		  String proratedPetRaw = "Prorated Pet Rent: On or before "+NC_PropertyWare.commensementDate.trim()+" Tenant will pay Landlord $";
	    		NC_PropertyWare.proratedPetRent = text.substring(text.indexOf(proratedPetRaw)+proratedPetRaw.length()).trim().split(" ")[0];//.replaceAll("[a-ZA-Z,]", "");
			    //AR_PropertyWare.proratedPetRent = proratedPetRentRaw.substring(proratedPetRentRaw.indexOf("Tenant will pay Landlord $")+"Tenant will pay Landlord $".length());//,proratedPetRentRaw.indexOf(AppConfig.AR_proratedPetRent_After));
			    if(NC_PropertyWare.proratedPetRent.matches(".*[a-zA-Z]+.*"))
			    {
			    	NC_PropertyWare.proratedPetRent = "Error";
			    }
			    }
			    catch(Exception e)
			    {
			   
			    NC_PropertyWare.proratedPetRent = "Error";	
			    e.printStackTrace();
			    }
	    	  System.out.println("Prorated Pet Rent = "+NC_PropertyWare.proratedPetRent.trim());
	    	
	    	try
		    {
	    		 NC_PropertyWare.petRent = text.substring(text.indexOf(PDFAppConfig.AB_petRent_Prior)+PDFAppConfig.AB_petRent_Prior.length()).trim().split(" ")[0];
	    		 if(NC_PropertyWare.petRent.contains(",for"))
	    		 {
	    			 NC_PropertyWare.petRent = NC_PropertyWare.petRent.split(",")[0].trim();
	    		 }
	    		 else
	    		 {
		    		 if(NC_PropertyWare.petRent.matches(".*[a-zA-Z]+.*")==true)
		    			 NC_PropertyWare.petRent = text.substring(text.indexOf(PDFAppConfig.AB_petRent1_Prior)+PDFAppConfig.AB_petRent1_Prior.length()).trim().split(" ")[0];
		    		 else 
		    		 NC_PropertyWare.petRent = RunnerClass.extractNumber(NC_PropertyWare.petRent);
	    		 }
		    }
	    	catch(Exception e)
		    {
	    		try
	    		{
	    			e.printStackTrace();
	    			NC_PropertyWare.petRent = text.substring(text.indexOf(PDFAppConfig.AB_petRent1_Prior)+PDFAppConfig.AB_petRent1_Prior.length()).trim().split(" ")[0];
//					 System.out.println("Pet rent = "+NC_PropertyWare.petRent.trim());
	    			if(NC_PropertyWare.petRent.matches(".*[a-zA-Z]+.*"))
	    		    {
	    		    	NC_PropertyWare.petRent = "Error";
	    		    }
	    		}
	    		
	    		catch(Exception e1)
			    {
			    	NC_PropertyWare.petRent = "Error";  
			    	e1.printStackTrace();
			    }
		    }
	    	System.out.println("Pet rent = "+NC_PropertyWare.petRent.trim());
		    	//NC_PropertyWare.petRent = "Error";  
		    	//e.printStackTrace();
		   /* 
	    	try
    		{
    			//String petFeeRaw1 = text.substring(text.indexOf(PDFAppConfig.AB_petFee_Prior));
    			NC_PropertyWare.petFee = text.substring(text.indexOf(PDFAppConfig.AB_petFee_Prior)+PDFAppConfig.AB_petFee_Prior.length()).trim().split(" ")[0].trim();
    			//NC_PropertyWare.petFee =  petFeeRaw[petFeeRaw.length-2].trim();
    			//if(NC_PropertyWare.petFee.matches(".*[a-zA-Z]+.*"))
    			//{
    				//NC_PropertyWare.petFee = text.substring(text.indexOf(PDFAppConfig.AB_petFee2_Prior)+PDFAppConfig.AB_petFee2_Prior.length()).trim().split(" ")[0].trim();
    			//}
    			//System.out.println(petFeeRaw.length);
    		}
    		
    		catch(Exception e1)
		    {
		    	NC_PropertyWare.petFee = "Error";  
		    	e1.printStackTrace();
		    }
	    	System.out.println("Pet Fee = "+NC_PropertyWare.petFee);
	    	*/
	    	// Get text between Type: word
	    	
	    	String typeSubString = text.substring(text.indexOf(PDFAppConfig.AB_typeWord_Prior)+PDFAppConfig.AB_typeWord_Prior.length(),text.indexOf(PDFAppConfig.AB_typeWord_After));
	    	
	    	String newText = typeSubString.replace("Type:","");
		    NC_PropertyWare.countOfTypeWordInText = ((typeSubString.length() - newText.length())/"Type:".length());
		    System.out.println("Type: occurences = "+NC_PropertyWare.countOfTypeWordInText);
		    
		    NC_PropertyWare.petType = new ArrayList();
		    NC_PropertyWare.petBreed = new ArrayList();
		    NC_PropertyWare.petWeight = new ArrayList();
		    for(int i =0;i<NC_PropertyWare.countOfTypeWordInText;i++)
		    {
		    	String type = typeSubString.substring(RunnerClass.nthOccurrence(typeSubString, "Type:", i+1)+PDFAppConfig.AB_pet1Type_Prior.length(),RunnerClass.nthOccurrence(typeSubString, "Breed:", i+1)).trim();
		    	if(type.contains("N/A")||type.contains("n/a"))
		    		break;
		    	System.out.println(type);
		    	NC_PropertyWare.petType.add(type);
		    	int pet1Breedindex1 = RunnerClass.nthOccurrence(typeSubString, "Breed:", i+1)+"Breed:".length()+1;
			    String subString = typeSubString.substring(pet1Breedindex1);
			    //int pet1Breedindex2 = RunnerClass.nthOccurrence(subString,"Name:",i+1);
			   // System.out.println("Index 2 = "+(index2+index1));
			    String breed = subString.split("Name:")[0].trim();//typeSubString.substring(pet1Breedindex1,(pet1Breedindex2+pet1Breedindex1));
			    System.out.println(breed);
			    NC_PropertyWare.petBreed.add(breed);
			    int pet1Weightindex1 = RunnerClass.nthOccurrence(typeSubString, "Weight:", i+1)+"Weight:".length()+1;
			    String pet1WeightSubstring = typeSubString.substring(pet1Weightindex1);
			    //int pet1WeightIndex2 = RunnerClass.nthOccurrence(pet1WeightSubstring,"Age:",i+1);
			   // System.out.println("Index 2 = "+(index2+index1));
			    String weight = pet1WeightSubstring.split("Age:")[0].trim(); //typeSubString.substring(pet1Weightindex1,(pet1WeightIndex2+pet1Weightindex1));
			    System.out.println(weight);
			    NC_PropertyWare.petWeight.add(weight);
		    }
		    
		    
		    
		    /*
		    if(NC_PropertyWare.countOfTypeWordInText>2)
	        {
			    try
			    {
			    NC_PropertyWare.serviceAnimalType = text.substring(RunnerClass.nthOccurrence(text, "Type:", 3)+PDFAppConfig.AB_pet1Type_Prior.length(),RunnerClass.nthOccurrence(text, "Breed:", 3));
			    }
			    catch(Exception e)
			    {
			    	 NC_PropertyWare.serviceAnimalType = "Error";
			    	 e.printStackTrace();
			    }
			    System.out.println("Service Animal Type = "+NC_PropertyWare.serviceAnimalType);
			    try
			    {
				    int serviceAnimalBreedindex1 = RunnerClass.nthOccurrence(text, "Breed:", 3)+"Breed:".length()+1;
				    String serviceAnimalsubString = text.substring(serviceAnimalBreedindex1);
				    int serviceAnimalBreedindex2 = RunnerClass.nthOccurrence(serviceAnimalsubString,"Name:",1);
				   // System.out.println("Index 2 = "+(index2+index1));
				    NC_PropertyWare.serviceAnimalBreed = text.substring(serviceAnimalBreedindex1,(serviceAnimalBreedindex2+serviceAnimalBreedindex1));
			    }
			    catch(Exception e)
			    {
			    	NC_PropertyWare.serviceAnimalBreed = "Error";
			    	e.printStackTrace();
			    }
			    System.out.println("Service Animal Breed = "+NC_PropertyWare.serviceAnimalBreed);
		  
			    try
			    {
			    int serviceAnimalWeightindex1 = RunnerClass.nthOccurrence(text, "Weight:", 3)+"Weight:".length()+1;
			    String serviceAnimalWeightSubstring = text.substring(serviceAnimalWeightindex1);
			    int serviceAnimalWeightIndex2 = RunnerClass.nthOccurrence(serviceAnimalWeightSubstring,"Age:",1);
			   // System.out.println("Index 2 = "+(index2+index1));
			    NC_PropertyWare.serviceAnimalWeight = text.substring(serviceAnimalWeightindex1,(serviceAnimalWeightIndex2+serviceAnimalWeightindex1));
			    }
			    catch(Exception e)
			    {
			    	NC_PropertyWare.serviceAnimalWeight = "Error";
			    	e.printStackTrace();
			    }  
			    System.out.println("Service Animal Weight = "+NC_PropertyWare.serviceAnimalWeight);
	        }
	        */
		    try
		    {
		    	NC_PropertyWare.petOneTimeNonRefundableFee = text.substring(text.indexOf(PDFAppConfig.AB_petFeeOneTime_Prior)+PDFAppConfig.AB_petFeeOneTime_Prior.length()).split(" ")[0];//,text.indexOf(PDFAppConfig.AB_petFeeOneTime_After));
		    	if(NC_PropertyWare.petOneTimeNonRefundableFee.matches(".*[a-zA-Z]+.*"))
    		    {
    		    	NC_PropertyWare.petOneTimeNonRefundableFee = "Error";
    		    }
		    }
		    catch(Exception e)
		    {
		    	NC_PropertyWare.petOneTimeNonRefundableFee =  "Error";
		    	e.printStackTrace();
		    }  
		    System.out.println("pet one time non refundable = "+NC_PropertyWare.petOneTimeNonRefundableFee.trim());
		   
	    }
	    
	    //Service Animal Addendum check
	    try
	    {
	    if(text.contains(AppConfig.serviceAnimalText))
	    {
	    	NC_PropertyWare.serviceAnimalFlag = true;
    		System.out.println("Service Animal Addendum is available");
    		
            String typeSubString = text.substring(text.indexOf(PDFAppConfig.AB_serviceAnimal_typeWord_Prior)+PDFAppConfig.AB_serviceAnimal_typeWord_Prior.length(),text.indexOf(PDFAppConfig.AB_serviceAnimal_typeWord_After));
	    	
	    	String newText = typeSubString.replace("Type:","");
		    int  countOftypeWords_ServiceAnimal = ((typeSubString.length() - newText.length())/"Type:".length());
		    System.out.println("Service Animal - Type: occurences = "+countOftypeWords_ServiceAnimal);
		    
		    NC_PropertyWare.serviceAnimalPetType = new ArrayList();
		    NC_PropertyWare.serviceAnimalPetBreed = new ArrayList();
		    NC_PropertyWare.serviceAnimalPetWeight = new ArrayList();
		    for(int i =0;i<countOftypeWords_ServiceAnimal;i++)
		    {
		    	String type = typeSubString.substring(RunnerClass.nthOccurrence(typeSubString, "Type:", i+1)+PDFAppConfig.AB_pet1Type_Prior.length(),RunnerClass.nthOccurrence(typeSubString, "Breed:", i+1)).trim();
		    	if(type.contains("N/A")||type.contains("n/a"))
		    		break;
		    	System.out.println(type);
		    	NC_PropertyWare.serviceAnimalPetType.add(type);
		    	int pet1Breedindex1 = RunnerClass.nthOccurrence(typeSubString, "Breed:", i+1)+"Breed:".length()+1;
			    String subString = typeSubString.substring(pet1Breedindex1);
			    //int pet1Breedindex2 = RunnerClass.nthOccurrence(subString,"Name:",i+1);
			   // System.out.println("Index 2 = "+(index2+index1));
			    String breed = subString.split("Name:")[0].trim();//typeSubString.substring(pet1Breedindex1,(pet1Breedindex2+pet1Breedindex1));
			    System.out.println(breed);
			    NC_PropertyWare.serviceAnimalPetBreed.add(breed);
			    int pet1Weightindex1 = RunnerClass.nthOccurrence(typeSubString, "Weight:", i+1)+"Weight:".length()+1;
			    String pet1WeightSubstring = typeSubString.substring(pet1Weightindex1);
			    //int pet1WeightIndex2 = RunnerClass.nthOccurrence(pet1WeightSubstring,"Age:",i+1);
			   // System.out.println("Index 2 = "+(index2+index1));
			    String weight = pet1WeightSubstring.split("Age:")[0].trim(); //typeSubString.substring(pet1Weightindex1,(pet1WeightIndex2+pet1Weightindex1));
			    System.out.println(weight);
			    NC_PropertyWare.serviceAnimalPetWeight.add(weight);
		    }
    		
    		
	    }
	    }
	    catch(Exception e)
	    {
	    	NC_PropertyWare.serviceAnimalFlag = false;
	    }
	    //Concession Addendum
	    
	    try
	    {
	    	if(text.contains(PDFAppConfig.concessionAddendumText))
	    	{
	    		NC_PropertyWare.concessionAddendumFlag = true;
	    		System.out.println("Concession Addendum is available");
	    	}
	    }
	    catch(Exception e)
	    {}
	    
	  document.close();
	    return true;
    }

}

