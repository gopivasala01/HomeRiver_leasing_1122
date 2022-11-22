package Alabama;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;

import org.apache.commons.codec.binary.StringUtils;
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
		AL_PropertyWare.petFlag = false;
		//AL_RunnerClass.emptyAllValues();
		File file = RunnerClass.getLastModified();
		//File file = new File("C:\\Gopi\\Projects\\Property ware\\Lease Close Outs\\PDFS\\Lease_03.22_03.23_4850_Woodford_Way_AL_Spigne.pdf");
		FileInputStream fis = new FileInputStream(file);
		PDDocument document = PDDocument.load(fis);
	    String text = new PDFTextStripper().getText(document);
	    AL_PropertyWare.pdfText  = text;
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
	    	AL_PropertyWare.commensementDate = text.substring(text.indexOf(PDFAppConfig.AB_commencementDate_Prior)+PDFAppConfig.AB_commencementDate_Prior.length(),text.indexOf(PDFAppConfig.AB_expirationDate_Prior));
	    	AL_PropertyWare.commensementDate = AL_PropertyWare.commensementDate.trim().replaceAll(" +", " ");
	    }
	    catch(Exception e)
	    {
	    	AL_PropertyWare.commensementDate = "Error";
	    	e.printStackTrace();
	    }
	    System.out.println("Commensement Date = "+AL_PropertyWare.commensementDate);
	   try
	    {
		   String expirationDateWaw = text.substring(text.indexOf(PDFAppConfig.AB_expirationDate_Prior)+PDFAppConfig.AB_expirationDate_Prior.length());
		   AL_PropertyWare.expirationDate =expirationDateWaw.trim().split(" ")[0]+" "+expirationDateWaw.trim().split(" ")[1]+" "+expirationDateWaw.trim().split(" ")[2];
		   AL_PropertyWare.expirationDate = AL_PropertyWare.expirationDate.trim().replaceAll(" +", " ");
	    }
	    catch(Exception e)
	    {
	    	 AL_PropertyWare.expirationDate = "Error";
	    	 e.printStackTrace();
	    }
	   System.out.println("Expiration Date = "+AL_PropertyWare.expirationDate);
	   try
	    {
		    AL_PropertyWare.proratedRent = text.substring(text.indexOf(PDFAppConfig.AB_proratedRent_Prior)+PDFAppConfig.AB_proratedRent_Prior.length(),text.indexOf(PDFAppConfig.AB_proratedRent_After));
		    if(AL_PropertyWare.proratedRent.matches(".*[a-zA-Z]+.*"))
		    {
		    	AL_PropertyWare.proratedRent = "Error";
		    }
		    		
	    }
	    catch(Exception e)
	    {
	    	AL_PropertyWare.proratedRent = "Error";
	    	e.printStackTrace();
	    }
	   System.out.println("Prorated Rent = "+AL_PropertyWare.proratedRent);
	    try
	    {
		    AL_PropertyWare.proratedRentDate = text.substring(text.indexOf(PDFAppConfig.AB_proratedRentDate_Prior)+PDFAppConfig.AB_proratedRentDate_Prior.length(),text.indexOf(PDFAppConfig.AB_proratedRentDate_After)).trim();
	    }
	    catch(Exception e)
	    {
	    	AL_PropertyWare.proratedRentDate = "Error";
	    	e.printStackTrace();
	    }
	    System.out.println("Prorated Rent Date= "+AL_PropertyWare.proratedRentDate.trim());
	    /*
	    try
	    {
		    AL_PropertyWare.monthlyRentDate = text.substring(text.indexOf(PDFAppConfig.AB_fullRentDate_Prior)+PDFAppConfig.AB_fullRentDate_Prior.length(),text.indexOf(PDFAppConfig.AB_fullRentDate_After));
		    System.out.println("Monthly Rent Date= "+AL_PropertyWare.monthlyRentDate.trim());
	    }
	    catch(Exception e)
	    {
	    	try
	    	{
	    		AL_PropertyWare.monthlyRentDate = text.substring(text.indexOf(PDFAppConfig.AB_fullRentDate_Prior)+PDFAppConfig.AB_fullRentDate_Prior.length(),text.indexOf(PDFAppConfig.AB_fullRentDate1_After));
			   	System.out.println("Monthly Rent Date= "+AL_PropertyWare.monthlyRentDate.trim());
	    	}
	    	catch(Exception e1)
		    {
		    	AL_PropertyWare.monthlyRentDate = "Error";  
		    	e1.printStackTrace();
		    }
	    }*/
	    try
	    {
		    AL_PropertyWare.monthlyRent = text.substring(text.indexOf(PDFAppConfig.AB_fullRent_Prior)+PDFAppConfig.AB_fullRent_Prior.length()).trim().split(" ")[0].trim();//,text.indexOf(PDFAppConfig.AB_fullRent_After)).substring(1).replaceAll("[^.0-9]", "");;
		    if(RunnerClass.onlyDigits(AL_PropertyWare.monthlyRent.replace(".", "").replace(",", ""))==false)
		    {
		    	AL_PropertyWare.monthlyRent = text.substring(text.indexOf(PDFAppConfig.AB_fullRent2_Prior)+PDFAppConfig.AB_fullRent2_Prior.length()).trim().split(" ")[0].trim();
		    }
		    if(AL_PropertyWare.monthlyRent.contains("*"))
		    {
		    	AL_PropertyWare.monthlyRent = AL_PropertyWare.monthlyRent.replace("*","");
		    }
		    if(AL_PropertyWare.monthlyRent.matches(".*[a-zA-Z]+.*"))
		    {
		    	AL_PropertyWare.monthlyRent = "Error";
		    }
	    }
	    catch(Exception e)
	    {
	    	 AL_PropertyWare.monthlyRent = "Error";
	    	 e.printStackTrace();
	    }
	    System.out.println("Monthly Rent "+AL_PropertyWare.monthlyRent.trim());
	    try
	    {
		    AL_PropertyWare.adminFee = text.substring(text.indexOf(PDFAppConfig.AB_adminFee_Prior)+PDFAppConfig.AB_adminFee_Prior.length()).split(" ")[0];
		    if(AL_PropertyWare.adminFee.matches(".*[a-zA-Z]+.*"))
		    {
		    	AL_PropertyWare.adminFee = "Error";
		    }
	    }
	    catch(Exception e)
	    {
		    AL_PropertyWare.adminFee = "Error";
		    e.printStackTrace();
	    }
	    System.out.println("Admin Fee = "+AL_PropertyWare.adminFee.trim());
	    try
	    {
		   String[] airFilterFeeArray = text.substring(text.indexOf(PDFAppConfig.AB_airFilterFee_Prior)+PDFAppConfig.AB_airFilterFee_Prior.length()).split(" ");
		   AL_PropertyWare.airFilterFee = airFilterFeeArray[0];
		   if(AL_PropertyWare.airFilterFee.matches(".*[a-zA-Z]+.*"))
		    {
		    	AL_PropertyWare.airFilterFee = "Error";
		    }
	    }
	    catch(Exception e)
	    {
	    AL_PropertyWare.airFilterFee = "Error";
	    e.printStackTrace();
	    }
	    System.out.println("Air Filter Fee = "+AL_PropertyWare.airFilterFee.trim());
	    try
	    {
	    	String[] earlyTerminationRaw = text.substring(text.indexOf(PDFAppConfig.AB_earlyTerminationFee_Prior)+PDFAppConfig.AB_earlyTerminationFee_Prior.length()).split(" ");
	    	
		    AL_PropertyWare.earlyTermination = earlyTerminationRaw[0]+earlyTerminationRaw[1]; //text.substring(text.indexOf(PDFAppConfig.AB_earlyTerminationFee_Prior)+PDFAppConfig.AB_earlyTerminationFee_Prior.length(),text.indexOf(PDFAppConfig.AB_earlyTerminationFee_After));
	    }
	    catch(Exception e)
	    {
	    	AL_PropertyWare.earlyTermination = "Error";	
	    	e.printStackTrace();
	    }
	    System.out.println("Early Termination  = "+AL_PropertyWare.earlyTermination.trim());
	    try
	    {
	    	
		    AL_PropertyWare.occupants = text.substring(text.indexOf(PDFAppConfig.AB_occupants_Prior)+PDFAppConfig.AB_occupants_Prior.length()).split("\\. B.")[0];
	    }
	    catch(Exception e)
	    {
		    AL_PropertyWare.occupants ="Error";	
		    e.printStackTrace();
	    }
	    System.out.println("Occupants = "+AL_PropertyWare.occupants.trim());
	    try
	    {
	    	 AL_PropertyWare.lateChargeDay = text.substring(text.indexOf(PDFAppConfig.AB_lateChargeDay_Prior)+PDFAppConfig.AB_lateChargeDay_Prior.length(),text.indexOf(PDFAppConfig.AB_lateChargeDay_After));
		        if(AL_PropertyWare.lateChargeDay.contains("rd"))
		        {
		        	AL_PropertyWare.lateChargeDay=AL_PropertyWare.lateChargeDay.trim().replace("rd", "");
		        }
		        else if(AL_PropertyWare.lateChargeDay.contains("st"))
		        {
		        	AL_PropertyWare.lateChargeDay=AL_PropertyWare.lateChargeDay.trim().replace("st", "");
		        }
	    	//AL_PropertyWare.lateChargeDay = text.substring(text.indexOf(PDFAppConfig.AB_lateChargeDay_Prior)+PDFAppConfig.AB_lateChargeDay_Prior.length(),text.indexOf(PDFAppConfig.AB_lateChargeDay_After));
	    }
	    catch(Exception e)
	    {
	    	AL_PropertyWare.lateChargeDay = "Error";	
	    	e.printStackTrace();
	    }
	    System.out.println("Late Charge Day = "+AL_PropertyWare.lateChargeDay.trim());
	    try
	    {
		    AL_PropertyWare.lateChargeFee = text.substring(text.indexOf(PDFAppConfig.AB_lateFee_Prior)+PDFAppConfig.AB_lateFee_Prior.length()).split(" ")[0];
		    //AL_PropertyWare.lateChargeFee =  AL_PropertyWare.lateChargeFee.substring(0,AL_PropertyWare.lateChargeFee.length()-1);
	    }
	    catch(Exception e)
	    {
		    AL_PropertyWare.lateChargeFee ="Error";	
		    e.printStackTrace();
	    }
	    System.out.println("Late Charge Fee = "+AL_PropertyWare.lateChargeFee.trim());
	    //Per Day Fee
	    try
	    {
	    	AL_PropertyWare.lateFeeChargePerDay = text.substring(text.indexOf(PDFAppConfig.AB_additionalLateChargesPerDay_Prior)+PDFAppConfig.AB_additionalLateChargesPerDay_Prior.length()).split(" ")[0].trim();//,text.indexOf(PDFAppConfig.AB_additionalLateChargesPerDay_After));
	    }
	    catch(Exception e)
	    {
	    	AL_PropertyWare.lateFeeChargePerDay =  "Error";	
	    	e.printStackTrace();
	    }
	    System.out.println("Per Day Fee = "+AL_PropertyWare.lateFeeChargePerDay.trim());
	    //Additional Late Charges Limit
	    try
	    {
	    	AL_PropertyWare.additionalLateChargesLimit = text.substring(text.indexOf(PDFAppConfig.AB_additionalLateChargesLimit_Prior)+PDFAppConfig.AB_additionalLateChargesLimit_Prior.length()).split(" ")[0]; //,text.indexOf(PDFAppConfig.AB_additionalLateChargesLimit_After));
	    }
	    catch(Exception e)
	    {
	    	AL_PropertyWare.additionalLateChargesLimit =  "Error";	
	    	e.printStackTrace();
	    }
	    System.out.println("additional Late Charges Limit = "+AL_PropertyWare.additionalLateChargesLimit.trim());
	    
	    
	    
	  //Prepayment Charge
  		if(AL_PropertyWare.portfolioType.contains("MCH"))
  		{
  			if(AL_PropertyWare.proratedRent.equalsIgnoreCase("n/a")||AL_PropertyWare.proratedRent.equalsIgnoreCase("Error")||AL_PropertyWare.proratedRent.equalsIgnoreCase(""))
  			{
  				AL_PropertyWare.prepaymentCharge = "Error";
  			}
  			else
  			{
	  		try
	  		{
	  		AL_PropertyWare.prepaymentCharge =String.valueOf(Double.parseDouble(AL_PropertyWare.monthlyRent.replace(",", "")) - Double.parseDouble(AL_PropertyWare.proratedRent.replace(",", ""))); 
	  		}
	  		catch(Exception e)
	  		{
	  			AL_PropertyWare.prepaymentCharge ="Error";
	  		}
	  		}
  			System.out.println("Prepayment Charge = "+AL_PropertyWare.prepaymentCharge);
  		 }
	    petFlag = text.contains(PDFAppConfig.AB_petAgreementAvailabilityCheck);
	    System.out.println("Pet Addendum Available = "+petFlag);
	    if(petFlag ==true)
	    {
	    	AL_PropertyWare.petFlag = true;
	    	try
	    	{
	    	AL_PropertyWare.petSecurityDeposit = text.substring(text.indexOf(PDFAppConfig.AB_securityDeposity_Prior)+PDFAppConfig.AB_securityDeposity_Prior.length(),text.indexOf(PDFAppConfig.AB_securityDeposity_After));
	    	if(AL_PropertyWare.petSecurityDeposit.matches(".*[a-zA-Z]+.*"))
		    {
		    	AL_PropertyWare.petSecurityDeposit = "Error";
		    }
	    	}
	    	catch(Exception e)
	    	{
	    	AL_PropertyWare.petSecurityDeposit = "Error";	
	    	e.printStackTrace();
	    	}
	    	System.out.println("Security Deposit = "+AL_PropertyWare.petSecurityDeposit.trim());
	    	if(RunnerClass.onlyDigits(AL_PropertyWare.petSecurityDeposit)==true)
		    {
		    	System.out.println("Security Deposit is checked");
		    }
	    	//TODO Check
	    	  try
			    {
	    		  String proratedPetRaw = "Prorated Pet Rent: On or before "+AL_PropertyWare.commensementDate.trim()+" Tenant will pay Landlord $";
	    		AL_PropertyWare.proratedPetRent = text.substring(text.indexOf(proratedPetRaw)+proratedPetRaw.length()).trim().split(" ")[0];//.replaceAll("[a-ZA-Z,]", "");
			    //AR_PropertyWare.proratedPetRent = proratedPetRentRaw.substring(proratedPetRentRaw.indexOf("Tenant will pay Landlord $")+"Tenant will pay Landlord $".length());//,proratedPetRentRaw.indexOf(AppConfig.AR_proratedPetRent_After));
			    if(AL_PropertyWare.proratedPetRent.matches(".*[a-zA-Z]+.*"))
			    {
			    	AL_PropertyWare.proratedPetRent = "Error";
			    }
			    }
			    catch(Exception e)
			    {
			   
			    AL_PropertyWare.proratedPetRent = "Error";	
			    e.printStackTrace();
			    }
	    	  System.out.println("Prorated Pet Rent = "+AL_PropertyWare.proratedPetRent.trim());
	    	
	    	try
		    {
	    		 AL_PropertyWare.petRent = text.substring(text.indexOf(PDFAppConfig.AB_petRent_Prior)+PDFAppConfig.AB_petRent_Prior.length()).trim().split(" ")[0];
	    		 if(AL_PropertyWare.petRent.contains(",for"))
	    		 {
	    			 AL_PropertyWare.petRent = AL_PropertyWare.petRent.split(",")[0].trim();
	    		 }
	    		 else
	    		 {
		    		 if(AL_PropertyWare.petRent.matches(".*[a-zA-Z]+.*")==true)
		    			 AL_PropertyWare.petRent = text.substring(text.indexOf(PDFAppConfig.AB_petRent1_Prior)+PDFAppConfig.AB_petRent1_Prior.length()).trim().split(" ")[0];
		    		 else 
		    		 AL_PropertyWare.petRent = RunnerClass.extractNumber(AL_PropertyWare.petRent);
	    		 }
		    }
	    	catch(Exception e)
		    {
	    		try
	    		{
	    			e.printStackTrace();
	    			AL_PropertyWare.petRent = text.substring(text.indexOf(PDFAppConfig.AB_petRent1_Prior)+PDFAppConfig.AB_petRent1_Prior.length()).trim().split(" ")[0];
//					 System.out.println("Pet rent = "+AL_PropertyWare.petRent.trim());
	    			if(AL_PropertyWare.petRent.matches(".*[a-zA-Z]+.*"))
	    		    {
	    		    	AL_PropertyWare.petRent = "Error";
	    		    }
	    		}
	    		
	    		catch(Exception e1)
			    {
			    	AL_PropertyWare.petRent = "Error";  
			    	e1.printStackTrace();
			    }
		    }
	    	System.out.println("Pet rent = "+AL_PropertyWare.petRent.trim());
		    	//AL_PropertyWare.petRent = "Error";  
		    	//e.printStackTrace();
		   /* 
	    	try
    		{
    			//String petFeeRaw1 = text.substring(text.indexOf(PDFAppConfig.AB_petFee_Prior));
    			AL_PropertyWare.petFee = text.substring(text.indexOf(PDFAppConfig.AB_petFee_Prior)+PDFAppConfig.AB_petFee_Prior.length()).trim().split(" ")[0].trim();
    			//AL_PropertyWare.petFee =  petFeeRaw[petFeeRaw.length-2].trim();
    			//if(AL_PropertyWare.petFee.matches(".*[a-zA-Z]+.*"))
    			//{
    				//AL_PropertyWare.petFee = text.substring(text.indexOf(PDFAppConfig.AB_petFee2_Prior)+PDFAppConfig.AB_petFee2_Prior.length()).trim().split(" ")[0].trim();
    			//}
    			//System.out.println(petFeeRaw.length);
    		}
    		
    		catch(Exception e1)
		    {
		    	AL_PropertyWare.petFee = "Error";  
		    	e1.printStackTrace();
		    }
	    	System.out.println("Pet Fee = "+AL_PropertyWare.petFee);
	    	*/
	    	// Get text between Type: word
	    	
	    	String typeSubString = text.substring(text.indexOf(PDFAppConfig.AB_typeWord_Prior)+PDFAppConfig.AB_typeWord_Prior.length(),text.indexOf(PDFAppConfig.AB_typeWord_After));
	    	
	    	String newText = typeSubString.replace("Type:","");
		    AL_PropertyWare.countOfTypeWordInText = ((typeSubString.length() - newText.length())/"Type:".length());
		    System.out.println("Type: occurences = "+AL_PropertyWare.countOfTypeWordInText);
		    
		    AL_PropertyWare.petType = new ArrayList();
		    AL_PropertyWare.petBreed = new ArrayList();
		    AL_PropertyWare.petWeight = new ArrayList();
		    for(int i =0;i<AL_PropertyWare.countOfTypeWordInText;i++)
		    {
		    	String type = typeSubString.substring(RunnerClass.nthOccurrence(typeSubString, "Type:", i+1)+PDFAppConfig.AB_pet1Type_Prior.length(),RunnerClass.nthOccurrence(typeSubString, "Breed:", i+1)).trim();
		    	if(type.contains("N/A")||type.contains("n/a"))
		    		break;
		    	System.out.println(type);
		    	AL_PropertyWare.petType.add(type);
		    	int pet1Breedindex1 = RunnerClass.nthOccurrence(typeSubString, "Breed:", i+1)+"Breed:".length()+1;
			    String subString = typeSubString.substring(pet1Breedindex1);
			    //int pet1Breedindex2 = RunnerClass.nthOccurrence(subString,"Name:",i+1);
			   // System.out.println("Index 2 = "+(index2+index1));
			    String breed = subString.split("Name:")[0].trim();//typeSubString.substring(pet1Breedindex1,(pet1Breedindex2+pet1Breedindex1));
			    System.out.println(breed);
			    AL_PropertyWare.petBreed.add(breed);
			    int pet1Weightindex1 = RunnerClass.nthOccurrence(typeSubString, "Weight:", i+1)+"Weight:".length()+1;
			    String pet1WeightSubstring = typeSubString.substring(pet1Weightindex1);
			    //int pet1WeightIndex2 = RunnerClass.nthOccurrence(pet1WeightSubstring,"Age:",i+1);
			   // System.out.println("Index 2 = "+(index2+index1));
			    String weight = pet1WeightSubstring.split("Age:")[0].trim(); //typeSubString.substring(pet1Weightindex1,(pet1WeightIndex2+pet1Weightindex1));
			    System.out.println(weight);
			    AL_PropertyWare.petWeight.add(weight);
		    }
		    
		    
		    
		    /*
		    if(AL_PropertyWare.countOfTypeWordInText>2)
	        {
			    try
			    {
			    AL_PropertyWare.serviceAnimalType = text.substring(RunnerClass.nthOccurrence(text, "Type:", 3)+PDFAppConfig.AB_pet1Type_Prior.length(),RunnerClass.nthOccurrence(text, "Breed:", 3));
			    }
			    catch(Exception e)
			    {
			    	 AL_PropertyWare.serviceAnimalType = "Error";
			    	 e.printStackTrace();
			    }
			    System.out.println("Service Animal Type = "+AL_PropertyWare.serviceAnimalType);
			    try
			    {
				    int serviceAnimalBreedindex1 = RunnerClass.nthOccurrence(text, "Breed:", 3)+"Breed:".length()+1;
				    String serviceAnimalsubString = text.substring(serviceAnimalBreedindex1);
				    int serviceAnimalBreedindex2 = RunnerClass.nthOccurrence(serviceAnimalsubString,"Name:",1);
				   // System.out.println("Index 2 = "+(index2+index1));
				    AL_PropertyWare.serviceAnimalBreed = text.substring(serviceAnimalBreedindex1,(serviceAnimalBreedindex2+serviceAnimalBreedindex1));
			    }
			    catch(Exception e)
			    {
			    	AL_PropertyWare.serviceAnimalBreed = "Error";
			    	e.printStackTrace();
			    }
			    System.out.println("Service Animal Breed = "+AL_PropertyWare.serviceAnimalBreed);
		  
			    try
			    {
			    int serviceAnimalWeightindex1 = RunnerClass.nthOccurrence(text, "Weight:", 3)+"Weight:".length()+1;
			    String serviceAnimalWeightSubstring = text.substring(serviceAnimalWeightindex1);
			    int serviceAnimalWeightIndex2 = RunnerClass.nthOccurrence(serviceAnimalWeightSubstring,"Age:",1);
			   // System.out.println("Index 2 = "+(index2+index1));
			    AL_PropertyWare.serviceAnimalWeight = text.substring(serviceAnimalWeightindex1,(serviceAnimalWeightIndex2+serviceAnimalWeightindex1));
			    }
			    catch(Exception e)
			    {
			    	AL_PropertyWare.serviceAnimalWeight = "Error";
			    	e.printStackTrace();
			    }  
			    System.out.println("Service Animal Weight = "+AL_PropertyWare.serviceAnimalWeight);
	        }
	        */
		    try
		    {
		    	AL_PropertyWare.petOneTimeNonRefundableFee = text.substring(text.indexOf(PDFAppConfig.AB_petFeeOneTime_Prior)+PDFAppConfig.AB_petFeeOneTime_Prior.length()).split(" ")[0];//,text.indexOf(PDFAppConfig.AB_petFeeOneTime_After));
		    	if(AL_PropertyWare.petOneTimeNonRefundableFee.matches(".*[a-zA-Z]+.*"))
    		    {
    		    	AL_PropertyWare.petOneTimeNonRefundableFee = "Error";
    		    }
		    }
		    catch(Exception e)
		    {
		    	AL_PropertyWare.petOneTimeNonRefundableFee =  "Error";
		    	e.printStackTrace();
		    }  
		    System.out.println("pet one time non refundable = "+AL_PropertyWare.petOneTimeNonRefundableFee.trim());
		   
	    }
	    
	    try
	    {
	  //Service Animal Addendum check
	    if(text.contains(AppConfig.serviceAnimalText))
	    {
	    	AL_PropertyWare.serviceAnimalFlag = true;
    		System.out.println("Service Animal Addendum is available");
    		
            String typeSubString = text.substring(text.indexOf(PDFAppConfig.AB1_serviceAnimal_typeWord_Prior)+PDFAppConfig.AB1_serviceAnimal_typeWord_Prior.length(),text.indexOf(PDFAppConfig.AB1_serviceAnimal_typeWord_After));
	    	
	    	String newText = typeSubString.replace("Type:","");
		    int  countOftypeWords_ServiceAnimal = ((typeSubString.length() - newText.length())/"Type:".length());
		    System.out.println("Service Animal - Type: occurences = "+countOftypeWords_ServiceAnimal);
		    
		    AL_PropertyWare.serviceAnimalPetType = new ArrayList();
		    AL_PropertyWare.serviceAnimalPetBreed = new ArrayList();
		    AL_PropertyWare.serviceAnimalPetWeight = new ArrayList();
		    for(int i =0;i<countOftypeWords_ServiceAnimal;i++)
		    {
		    	String type = typeSubString.substring(RunnerClass.nthOccurrence(typeSubString, "Type:", i+1)+PDFAppConfig.AB_pet1Type_Prior.length(),RunnerClass.nthOccurrence(typeSubString, "Breed:", i+1)).trim();
		    	if(type.contains("N/A")||type.contains("n/a"))
		    		break;
		    	System.out.println(type);
		    	AL_PropertyWare.serviceAnimalPetType.add(type);
		    	int pet1Breedindex1 = RunnerClass.nthOccurrence(typeSubString, "Breed:", i+1)+"Breed:".length()+1;
			    String subString = typeSubString.substring(pet1Breedindex1);
			    //int pet1Breedindex2 = RunnerClass.nthOccurrence(subString,"Name:",i+1);
			   // System.out.println("Index 2 = "+(index2+index1));
			    String breed = subString.split("Name:")[0].trim();//typeSubString.substring(pet1Breedindex1,(pet1Breedindex2+pet1Breedindex1));
			    System.out.println(breed);
			    AL_PropertyWare.serviceAnimalPetBreed.add(breed);
			    int pet1Weightindex1 = RunnerClass.nthOccurrence(typeSubString, "Weight:", i+1)+"Weight:".length()+1;
			    String pet1WeightSubstring = typeSubString.substring(pet1Weightindex1);
			    //int pet1WeightIndex2 = RunnerClass.nthOccurrence(pet1WeightSubstring,"Age:",i+1);
			   // System.out.println("Index 2 = "+(index2+index1));
			    String weight = pet1WeightSubstring.split("Age:")[0].trim(); //typeSubString.substring(pet1Weightindex1,(pet1WeightIndex2+pet1Weightindex1));
			    System.out.println(weight);
			    AL_PropertyWare.serviceAnimalPetWeight.add(weight);
		    }
    		
    		
	    }
	    }
	    catch(Exception e) 
	    {
	    	AL_PropertyWare.serviceAnimalFlag = false;
	    }
	    //Concession Addendum
	    
	    try
	    {
	    	if(text.contains(PDFAppConfig.concessionAddendumText))
	    	{
	    		AL_PropertyWare.concessionAddendumFlag = true;
	    		System.out.println("Concession Addendum is available");
	    	}
	    }
	    catch(Exception e)
	    {}
	    
	  document.close();
	    return true;
    }

}
