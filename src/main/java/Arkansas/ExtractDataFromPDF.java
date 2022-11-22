package Arkansas;

import java.io.File;
import java.io.FileInputStream;

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
		File file = RunnerClass.getLastModified();
		//File file = new File("C:\\Users\\user\\Downloads\\Alabama\\Lease_11.21_10.22_4970_Deer_Foot_Cv_AL_Osborn.pdf");
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
	   // System.out.println(text);
	    System.out.println("------------------------------------------------------------------");
	    try
	    {
	    	AL_PropertyWare.commensementDate = text.substring(text.indexOf(PDFAppConfig.AB_commencementDate_Prior)+PDFAppConfig.AB_commencementDate_Prior.length(),text.indexOf(PDFAppConfig.AB_expirationDate_Prior));
	    	System.out.println("Commensement Date = "+AL_PropertyWare.commensementDate.substring(AL_PropertyWare.commensementDate.lastIndexOf(":")+1));
	    }
	    catch(Exception e)
	    {
	    	AL_PropertyWare.commensementDate = "Error";
	    	e.printStackTrace();
	    }
	   try
	    {
		   String expirationDateWaw = text.substring(text.indexOf(PDFAppConfig.AB_expirationDate_Prior)+PDFAppConfig.AB_expirationDate_Prior.length());
		   AL_PropertyWare.expirationDate =expirationDateWaw.trim().split(" ")[0]+" "+expirationDateWaw.trim().split(" ")[1]+" "+expirationDateWaw.trim().split(" ")[2];
		   System.out.println("Expiration Date = "+AL_PropertyWare.expirationDate);
	    }
	    catch(Exception e)
	    {
	    	 AL_PropertyWare.expirationDate = "Error";
	    	 e.printStackTrace();
	    }
	   try
	    {
		    AL_PropertyWare.proratedRent = text.substring(text.indexOf(PDFAppConfig.AB_proratedRent_Prior)+PDFAppConfig.AB_proratedRent_Prior.length(),text.indexOf(PDFAppConfig.AB_proratedRent_After));
		    System.out.println("Prorated Rent = "+AL_PropertyWare.proratedRent);
	    }
	    catch(Exception e)
	    {
	    	AL_PropertyWare.proratedRent = "Error";
	    	e.printStackTrace();
	    }
	    try
	    {
		    AL_PropertyWare.proratedRentDate = text.substring(text.indexOf(PDFAppConfig.AB_proratedRentDate_Prior)+PDFAppConfig.AB_proratedRentDate_Prior.length(),text.indexOf(PDFAppConfig.AB_proratedRentDate_After)).trim();
		    System.out.println("Prorated Rent Date= "+AL_PropertyWare.proratedRentDate.trim());
	    }
	    catch(Exception e)
	    {
	    	AL_PropertyWare.proratedRentDate = "Error";
	    	e.printStackTrace();
	    }
	    
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
	    }
	    try
	    {
		    AL_PropertyWare.monthlyRent = text.substring(text.indexOf(PDFAppConfig.AB_fullRent_Prior)+PDFAppConfig.AB_fullRent_Prior.length(),text.indexOf(PDFAppConfig.AB_fullRent_After)).substring(1).replaceAll("[^.0-9]", "");;
		    System.out.println("Monthly Rent "+AL_PropertyWare.monthlyRent.trim());
	    }
	    catch(Exception e)
	    {
	    	 AL_PropertyWare.monthlyRent = "Error";
	    	 e.printStackTrace();
	    }
	    try
	    {
		    AL_PropertyWare.adminFee = text.substring(text.indexOf(PDFAppConfig.AB_adminFee_Prior)+PDFAppConfig.AB_adminFee_Prior.length()).split(" ")[0];
		    System.out.println("Admin Fee = "+AL_PropertyWare.adminFee.trim());
	    }
	    catch(Exception e)
	    {
		    AL_PropertyWare.adminFee = "Error";
		    e.printStackTrace();
	    }
	    try
	    {
		    AL_PropertyWare.airFilterFee = text.substring(text.indexOf(PDFAppConfig.AB_airFilterFee_Prior)+PDFAppConfig.AB_airFilterFee_Prior.length(),text.indexOf(PDFAppConfig.AB_airFilterFee_After));
		    System.out.println("Air Filter Fee = "+AL_PropertyWare.airFilterFee.trim());
	    }
	    catch(Exception e)
	    {
	    AL_PropertyWare.airFilterFee = "Error";
	    e.printStackTrace();
	    }
	    try
	    {
		    AL_PropertyWare.earlyTermination = text.substring(text.indexOf(PDFAppConfig.AB_earlyTerminationFee_Prior)+PDFAppConfig.AB_earlyTerminationFee_Prior.length(),text.indexOf(PDFAppConfig.AB_earlyTerminationFee_After));
		    System.out.println("Early Termination  = "+AL_PropertyWare.earlyTermination.trim());
	    }
	    catch(Exception e)
	    {
	    	AL_PropertyWare.earlyTermination = "Error";	
	    	e.printStackTrace();
	    }
	    try
	    {
		    AL_PropertyWare.occupants = text.substring(text.indexOf(PDFAppConfig.AB_occupants_Prior)+PDFAppConfig.AB_occupants_Prior.length(),text.indexOf(PDFAppConfig.AB_occupants_After));
		    System.out.println("Occupants = "+AL_PropertyWare.occupants.trim());
	    }
	    catch(Exception e)
	    {
		    AL_PropertyWare.occupants ="Error";	
		    e.printStackTrace();
	    }
	    try
	    {
		    AL_PropertyWare.lateChargeDay = text.substring(text.indexOf(PDFAppConfig.AB_lateChargeDay_Prior)+PDFAppConfig.AB_lateChargeDay_Prior.length(),text.indexOf(PDFAppConfig.AB_lateChargeDay_After));
		    System.out.println("Late Charge Day = "+AL_PropertyWare.lateChargeDay.trim());
	    }
	    catch(Exception e)
	    {
	    	AL_PropertyWare.lateChargeDay = "Error";	
	    	e.printStackTrace();
	    }
	    try
	    {
		    AL_PropertyWare.lateChargeFee = text.substring(text.indexOf(PDFAppConfig.AB_lateFee_Prior)+PDFAppConfig.AB_lateFee_Prior.length(),text.indexOf(PDFAppConfig.AB_lateFee_After));
		    //AL_PropertyWare.lateChargeFee =  AL_PropertyWare.lateChargeFee.substring(0,AL_PropertyWare.lateChargeFee.length()-1);
		    System.out.println("Late Charge Fee = "+AL_PropertyWare.lateChargeFee.trim());
	    }
	    catch(Exception e)
	    {
		    AL_PropertyWare.lateChargeFee ="Error";	
		    e.printStackTrace();
	    }
	    
	    petFlag = text.contains(PDFAppConfig.AB_petAgreementAvailabilityCheck);
	    System.out.println("Pet Addendum Available = "+petFlag);
	    if(petFlag ==true)
	    {
	    	try
	    	{
	    	AL_PropertyWare.petSecurityDeposit = text.substring(text.indexOf(PDFAppConfig.AB_securityDeposity_Prior)+PDFAppConfig.AB_securityDeposity_Prior.length(),text.indexOf(PDFAppConfig.AB_securityDeposity_After));
		    System.out.println("Security Deposit = "+AL_PropertyWare.petSecurityDeposit.trim());
	    	}
	    	catch(Exception e)
	    	{
	    	AL_PropertyWare.petSecurityDeposit = "Error";	
	    	e.printStackTrace();
	    	}
	    	if(RunnerClass.onlyDigits(AL_PropertyWare.petSecurityDeposit)==true)
		    {
		    	System.out.println("Security Deposit is checked");
		    }
	    	//TODO Check
	    	/*   try
			    {
	    		AR_PropertyWare.proratedPetRent = text.substring(text.indexOf(PDFAppConfig.AR_proratedPetRent_Prior)+PDFAppConfig.AR_proratedPetRent_Prior.length());
			    //AR_PropertyWare.proratedPetRent = proratedPetRentRaw.substring(proratedPetRentRaw.indexOf("Tenant will pay Landlord $")+"Tenant will pay Landlord $".length());//,proratedPetRentRaw.indexOf(AppConfig.AR_proratedPetRent_After));
			    System.out.println("Prorated Pet Rent = "+AR_PropertyWare.proratedPetRent.trim());
			    }
			    catch(Exception e)
			    {
			    AR_PropertyWare.proratedPetRent = "Error";	
			    }*/
	    	
	    	try
		    {
	    		 AL_PropertyWare.petRent = text.substring(text.indexOf(PDFAppConfig.AB_petRent_Prior)+PDFAppConfig.AB_petRent_Prior.length(),text.indexOf(PDFAppConfig.AB_petRent_After));
				 System.out.println("Pet rent = "+AL_PropertyWare.petRent.trim());
		    }
	    	catch(Exception e)
		    {
	    		try
	    		{
	    			AL_PropertyWare.petRent = text.substring(text.indexOf(PDFAppConfig.AB_petRent1_Prior)+PDFAppConfig.AB_petRent1_Prior.length(),text.indexOf(PDFAppConfig.AB_petRent1_After));
					 System.out.println("Pet rent = "+AL_PropertyWare.petRent.trim());
	    		}
	    		
	    		catch(Exception e1)
			    {
			    	AL_PropertyWare.petRent = "Error";  
			    	e1.printStackTrace();
			    }
		    }
		    	//AL_PropertyWare.petRent = "Error";  
		    	//e.printStackTrace();
		    
	    	
	    	String newText = text.replace("Type:","");
		    AL_PropertyWare.countOfTypeWordInText = ((text.length() - newText.length())/"Type:".length());
		    System.out.println("Type: occurences = "+AL_PropertyWare.countOfTypeWordInText);
		    
		    if(AL_PropertyWare.countOfTypeWordInText==2)
		    {
		    try
		    {
		    AL_PropertyWare.pet1Type = text.substring(text.indexOf(PDFAppConfig.AB_pet1Type_Prior)+PDFAppConfig.AB_pet1Type_Prior.length(),text.indexOf(PDFAppConfig.AB_pet1Type_After));
		    System.out.println("Pet 1 Type = "+AL_PropertyWare.pet1Type.trim());
		    }
		    catch(Exception e)
		    {
		    	AL_PropertyWare.pet1Type = "Error";
		    	e.printStackTrace();
		    }
		    //Check if service animal is there
		    //PropertyWare.pet2Type = text.substring(text.indexOf("Type:", text.indexOf("Type:")+1)+AppConfig.AB_pet1Type_Prior.length(),text.indexOf("Breed:", text.indexOf("Breed:")+1));
		    try
		    {
		    AL_PropertyWare.pet2Type = text.substring(RunnerClass.nthOccurrence(text, "Type:", 2)+PDFAppConfig.AB_pet1Type_Prior.length(),RunnerClass.nthOccurrence(text, "Breed:", 2));
		    System.out.println("Pet 2 Type = "+AL_PropertyWare.pet2Type);
		    }
		    catch(Exception e) 
		    {
		    	AL_PropertyWare.pet2Type =	 "Error";
		    	e.printStackTrace();
		    }
		    try
		    {
			    int pet1Breedindex1 = RunnerClass.nthOccurrence(text, "Breed:", 1)+PDFAppConfig.AB_pet1Type_Prior.length()+1;
			    String subString = text.substring(pet1Breedindex1);
			    int pet1Breedindex2 = RunnerClass.nthOccurrence(subString,"Name:",1);
			   // System.out.println("Index 2 = "+(index2+index1));
			    AL_PropertyWare.pet1Breed = text.substring(pet1Breedindex1,(pet1Breedindex2+pet1Breedindex1));
			    System.out.println("Pet 1 Breed = "+text.substring(pet1Breedindex1,(pet1Breedindex2+pet1Breedindex1)));
		    }
		    catch(Exception e)
		    {
		    	 AL_PropertyWare.pet1Breed = "Error";
		    	 e.printStackTrace();
		    }
		    try
		    {
			    int pet2Breedindex1 = RunnerClass.nthOccurrence(text, "Breed:", 2)+"Breed:".length()+1;
			    String subString2 = text.substring(pet2Breedindex1);
			    int pet2Breedindex2 = RunnerClass.nthOccurrence(subString2,"Name:",1);
			   // System.out.println("Index 2 = "+(index2+index1));
			    AL_PropertyWare.pet2Breed = text.substring(pet2Breedindex1,(pet2Breedindex2+pet2Breedindex1));
			    System.out.println("Pet 2 Breed = "+text.substring(pet2Breedindex1,(pet2Breedindex2+pet2Breedindex1)));
		    }
		    catch(Exception e)
		    {
		    	AL_PropertyWare.pet2Breed = "Error";
		    	e.printStackTrace();
		    }
		    try
		    {
			    int pet1Weightindex1 = RunnerClass.nthOccurrence(text, "Weight:", 1)+"Weight:".length()+1;
			    String pet1WeightSubstring = text.substring(pet1Weightindex1);
			    int pet1WeightIndex2 = RunnerClass.nthOccurrence(pet1WeightSubstring,"Age:",1);
			   // System.out.println("Index 2 = "+(index2+index1));
			    AL_PropertyWare.pet1Weight = text.substring(pet1Weightindex1,(pet1WeightIndex2+pet1Weightindex1));
			    System.out.println("Pet 1 Weight = "+text.substring(pet1Weightindex1,(pet1WeightIndex2+pet1Weightindex1)));
		    }
		    catch(Exception e)
		    {
		    	AL_PropertyWare.pet1Weight = "Error";
		    	e.printStackTrace();
		    }
		    try
		    {
			    int pet2Weightindex1 = RunnerClass.nthOccurrence(text, "Weight:", 2)+"Weight:".length()+1;
			    String pet2WeightSubstring = text.substring(pet2Weightindex1);
			    int pet2WeightIndex2 = RunnerClass.nthOccurrence(pet2WeightSubstring,"Age:",1);
			   // System.out.println("Index 2 = "+(index2+index1));
			    AL_PropertyWare.pet2Weight = text.substring(pet2Weightindex1,(pet2WeightIndex2+pet2Weightindex1));
			    System.out.println("Pet 2 Weight = "+text.substring(pet2Weightindex1,(pet2WeightIndex2+pet2Weightindex1)));
		    }
		    catch(Exception e)
		    {
		    	AL_PropertyWare.pet2Weight = "Error";
		    	e.printStackTrace();
		    }
		    }
		    else if(AL_PropertyWare.countOfTypeWordInText<2&&AL_PropertyWare.countOfTypeWordInText>0)
		    {
		    	try
			    {
			    AL_PropertyWare.pet1Type = text.substring(text.indexOf(PDFAppConfig.AB_pet1Type_Prior)+PDFAppConfig.AB_pet1Type_Prior.length(),text.indexOf(PDFAppConfig.AB_pet1Type_After));
			    System.out.println("Pet 1 Type = "+AL_PropertyWare.pet1Type.trim());
			    }
			    catch(Exception e)
			    {
			    	AL_PropertyWare.pet1Type = "Error";
			    	e.printStackTrace();
			    }
		    	
		    	try
			    {
				    int pet1Breedindex1 = RunnerClass.nthOccurrence(text, "Breed:", 1)+PDFAppConfig.AB_pet1Type_Prior.length()+1;
				    String subString = text.substring(pet1Breedindex1);
				    int pet1Breedindex2 = RunnerClass.nthOccurrence(subString,"Name:",1);
				   // System.out.println("Index 2 = "+(index2+index1));
				    AL_PropertyWare.pet1Breed = text.substring(pet1Breedindex1,(pet1Breedindex2+pet1Breedindex1));
				    System.out.println("Pet 1 Breed = "+text.substring(pet1Breedindex1,(pet1Breedindex2+pet1Breedindex1)));
			    }
			    catch(Exception e)
			    {
			    	 AL_PropertyWare.pet1Breed = "Error";
			    	 e.printStackTrace();
			    }
		    	
		    	 try
				    {
					    int pet1Weightindex1 = RunnerClass.nthOccurrence(text, "Weight:", 1)+"Weight:".length()+1;
					    String pet1WeightSubstring = text.substring(pet1Weightindex1);
					    int pet1WeightIndex2 = RunnerClass.nthOccurrence(pet1WeightSubstring,"Age:",1);
					   // System.out.println("Index 2 = "+(index2+index1));
					    AL_PropertyWare.pet1Weight = text.substring(pet1Weightindex1,(pet1WeightIndex2+pet1Weightindex1));
					    System.out.println("Pet 1 Weight = "+text.substring(pet1Weightindex1,(pet1WeightIndex2+pet1Weightindex1)));
				    }
				    catch(Exception e)
				    {
				    	AL_PropertyWare.pet1Weight = "Error";
				    	e.printStackTrace();
				    }
		    	
		    }
		    
		    
		    if(AL_PropertyWare.countOfTypeWordInText>2)
	        {
			    try
			    {
			    AL_PropertyWare.serviceAnimalType = text.substring(RunnerClass.nthOccurrence(text, "Type:", 3)+PDFAppConfig.AB_pet1Type_Prior.length(),RunnerClass.nthOccurrence(text, "Breed:", 3));
			    System.out.println("Service Animal Type = "+AL_PropertyWare.serviceAnimalType);
			    }
			    catch(Exception e)
			    {
			    	 AL_PropertyWare.serviceAnimalType = "Error";
			    	 e.printStackTrace();
			    }
			    try
			    {
				    int serviceAnimalBreedindex1 = RunnerClass.nthOccurrence(text, "Breed:", 3)+"Breed:".length()+1;
				    String serviceAnimalsubString = text.substring(serviceAnimalBreedindex1);
				    int serviceAnimalBreedindex2 = RunnerClass.nthOccurrence(serviceAnimalsubString,"Name:",1);
				   // System.out.println("Index 2 = "+(index2+index1));
				    AL_PropertyWare.serviceAnimalBreed = text.substring(serviceAnimalBreedindex1,(serviceAnimalBreedindex2+serviceAnimalBreedindex1));
				    System.out.println("Service Animal Breed = "+text.substring(serviceAnimalBreedindex1,(serviceAnimalBreedindex2+serviceAnimalBreedindex1)));
			    }
			    catch(Exception e)
			    {
			    	AL_PropertyWare.serviceAnimalBreed = "Error";
			    	e.printStackTrace();
			    }
		  
			    try
			    {
			    int serviceAnimalWeightindex1 = RunnerClass.nthOccurrence(text, "Weight:", 3)+"Weight:".length()+1;
			    String serviceAnimalWeightSubstring = text.substring(serviceAnimalWeightindex1);
			    int serviceAnimalWeightIndex2 = RunnerClass.nthOccurrence(serviceAnimalWeightSubstring,"Age:",1);
			   // System.out.println("Index 2 = "+(index2+index1));
			    AL_PropertyWare.serviceAnimalWeight = text.substring(serviceAnimalWeightindex1,(serviceAnimalWeightIndex2+serviceAnimalWeightindex1));
			    System.out.println("Service Animal Weight = "+text.substring(serviceAnimalWeightindex1,(serviceAnimalWeightIndex2+serviceAnimalWeightindex1)));
			    }
			    catch(Exception e)
			    {
			    	AL_PropertyWare.serviceAnimalWeight = "Error";
			    	e.printStackTrace();
			    }  
	        }
		    try
		    {
		    	AL_PropertyWare.petOneTimeNonRefundableFee = text.substring(text.indexOf(PDFAppConfig.AB_petFeeOneTime_Prior)+PDFAppConfig.AB_petFeeOneTime_Prior.length(),text.indexOf(PDFAppConfig.AB_petFeeOneTime_After));
			    System.out.println("pet one time non refundable = "+AL_PropertyWare.petOneTimeNonRefundableFee.trim());
		    }
		    catch(Exception e)
		    {
		    	AL_PropertyWare.petOneTimeNonRefundableFee =  "Error";
		    	e.printStackTrace();
		    }  
		   
	    }
	    try
	    {
	    	AL_PropertyWare.additionalLateCharges = text.substring(text.indexOf(PDFAppConfig.AB_additionalLateChargesPerDay_Prior)+PDFAppConfig.AB_additionalLateChargesPerDay_Prior.length(),text.indexOf(PDFAppConfig.AB_additionalLateChargesPerDay_After));
		    System.out.println("Additional late charges = "+AL_PropertyWare.additionalLateCharges.trim());
	    }
	    catch(Exception e)
	    {
	    	AL_PropertyWare.additionalLateCharges =  "Error";	
	    	e.printStackTrace();
	    }
	    try
	    {
	    	AL_PropertyWare.additionalLateChargesLimit = text.substring(text.indexOf(PDFAppConfig.AB_additionalLateChargesLimit_Prior)+PDFAppConfig.AB_additionalLateChargesLimit_Prior.length(),text.indexOf(PDFAppConfig.AB_additionalLateChargesLimit_After));
		    System.out.println("additional Late Charges Limit = "+AL_PropertyWare.additionalLateChargesLimit.trim());
	    }
	    catch(Exception e)
	    {
	    	AL_PropertyWare.additionalLateChargesLimit =  "Error";	
	    	e.printStackTrace();
	    }
		return true;
    }

}
