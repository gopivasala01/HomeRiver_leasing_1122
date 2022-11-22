package Arkansas;

import java.io.File;
import java.io.FileInputStream;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import mainPackage.RunnerClass;


public class ExtractDataFromPDF_Format2 
{

	public static boolean petFlag;
	public boolean arizona() throws Exception
	//public static void main(String[] args) throws Exception 
	{
		// TODO Auto-generated method stub
		//File file = new File("D:\\Beetlerim Docs\\Property ware\\Lease Close outs\\Alabama\\Format 2\\Lease_0922_0923_248_S_Hillcrest_Rd_AL_Womac.pdf");
		File file = RunnerClass.getLastModified();
		FileInputStream fis = new FileInputStream(file);
		PDDocument document = PDDocument.load(fis);
	    String text = new PDFTextStripper().getText(document);
	    //System.out.println(text);
	    System.out.println("------------------------------------------------------------------");
	    
	    try
	    {
	    	AL_PropertyWare.commensementDate = text.substring(text.indexOf(PDFAppConfig_Format2.commensementDate_Prior)+PDFAppConfig_Format2.commensementDate_Prior.length()+1,text.indexOf(PDFAppConfig_Format2.commensementDate_After)).trim();
		    System.out.println("Commensement Date = "+AL_PropertyWare.commensementDate);//.substring(commensementDate.lastIndexOf(":")+1));
	    }
	    catch(Exception e)
	    {
	    	AL_PropertyWare.commensementDate = "Error";
	    	e.printStackTrace();
	    }
	    
	    try
	    {
	    	AL_PropertyWare.expirationDate = text.substring(text.indexOf(PDFAppConfig_Format2.expirationDate_Prior)+PDFAppConfig_Format2.expirationDate_Prior.length(),text.indexOf(PDFAppConfig_Format2.expirationDate_After)).trim();
		    System.out.println("Expiration Date = "+AL_PropertyWare.expirationDate);//.substring(commensementDate.lastIndexOf(":")+1));
	    }
	    catch(Exception e)
	    {
	    	AL_PropertyWare.expirationDate = "Error";
	    	e.printStackTrace();
	    }
	    
	    try
	    {
	    	AL_PropertyWare.proratedRentDate = text.substring(text.indexOf(PDFAppConfig_Format2.proratedRentDate_Prior)+PDFAppConfig_Format2.proratedRentDate_Prior.length()+1,text.indexOf(PDFAppConfig_Format2.proratedRentDate_After)).trim();
		    System.out.println("prorated Rent Date = "+AL_PropertyWare.proratedRentDate);//.substring(commensementDate.lastIndexOf(":")+1));
	    }
	    catch(Exception e)
	    {
	    	AL_PropertyWare.proratedRentDate = "Error";
	    	e.printStackTrace();
	    }
	    
	    try
	    {
	    	AL_PropertyWare.proratedRent = text.substring(text.indexOf(PDFAppConfig_Format2.proratedRent_Prior)+PDFAppConfig_Format2.proratedRent_Prior.length()).split(" ")[0].trim();
		    System.out.println("prorated Rent = "+AL_PropertyWare.proratedRent);//.substring(commensementDate.lastIndexOf(":")+1));
	    }
	    catch(Exception e)
	    {
	    	AL_PropertyWare.proratedRent = "Error";
	    	e.printStackTrace();
	    }
	    
	    try
	    {
	    	AL_PropertyWare.monthlyRent = text.substring(text.indexOf(PDFAppConfig_Format2.monthlyRent_Prior)+PDFAppConfig_Format2.monthlyRent_Prior.length()).split(" ")[0].trim();
	    	if(!AL_PropertyWare.monthlyRent.contains("."))
	    		AL_PropertyWare.monthlyRent = text.substring(text.indexOf(PDFAppConfig_Format2.monthlyRent_Prior2)+PDFAppConfig_Format2.monthlyRent_Prior2.length()).split(" ")[0].trim();
		    System.out.println("Monthly Rent = "+AL_PropertyWare.monthlyRent);//.substring(commensementDate.lastIndexOf(":")+1));
	    }
	    catch(Exception e)
	    {
	    	AL_PropertyWare.monthlyRent = "Error";
	    	e.printStackTrace();
	    }
	   
	    try
	    {
	    	AL_PropertyWare.adminFee = text.substring(text.indexOf(PDFAppConfig_Format2.adminFee_prior)+PDFAppConfig_Format2.adminFee_prior.length()).split(" ")[0].trim();
		    System.out.println("Admin Fee = "+AL_PropertyWare.adminFee);//.substring(commensementDate.lastIndexOf(":")+1));
	    }
	    catch(Exception e)
	    {
	    	AL_PropertyWare.adminFee = "Error";
	    	e.printStackTrace();
	    }
	   
	    try
	    {
	    	AL_PropertyWare.airFilterFee = text.substring(text.indexOf(PDFAppConfig_Format2.HVACAirFilter_prior)+PDFAppConfig_Format2.HVACAirFilter_prior.length()).split(" ")[0].trim();
		    System.out.println("HVAC Air Filter Fee = "+AL_PropertyWare.airFilterFee);//.substring(commensementDate.lastIndexOf(":")+1));
	    }
	    catch(Exception e)
	    {
	    	AL_PropertyWare.airFilterFee = "Error";
	    	e.printStackTrace();
	    }
	    
	    try
	    {
	    	AL_PropertyWare.occupants = text.substring(text.indexOf(PDFAppConfig_Format2.occupants_Prior)+PDFAppConfig_Format2.occupants_Prior.length(),text.indexOf(PDFAppConfig_Format2.occupants_After)).trim();
		    System.out.println("Occupants = "+AL_PropertyWare.occupants);//.substring(commensementDate.lastIndexOf(":")+1));
	    }
	    catch(Exception e)
	    {
	    	AL_PropertyWare.occupants = "Error";
	    	e.printStackTrace();
	    }
	    
	    try
	    {
	    	AL_PropertyWare.lateChargeDay = text.substring(text.indexOf(PDFAppConfig_Format2.lateFeeDay_Prior)+PDFAppConfig_Format2.lateFeeDay_Prior.length()).split("th")[0].trim();
		    System.out.println("Late Charge Day = "+AL_PropertyWare.lateChargeDay);//.substring(commensementDate.lastIndexOf(":")+1));
	    }
	    catch(Exception e)
	    {
	    	AL_PropertyWare.lateChargeDay = "Error";
	    	e.printStackTrace();
	    }
	    
	    try
	    {
	    	AL_PropertyWare.lateChargeFee = text.substring(text.indexOf(PDFAppConfig_Format2.initialLateChargeFee_Prior)+PDFAppConfig_Format2.initialLateChargeFee_Prior.length()).split(" ")[0].trim();
		    System.out.println("Initital Late Charge  = "+AL_PropertyWare.lateChargeFee);//.substring(commensementDate.lastIndexOf(":")+1));
	    }
	    catch(Exception e)
	    {
	    	AL_PropertyWare.lateChargeFee = "Error";
	    	e.printStackTrace();
	    }
	    
	    try
	    {
	    	AL_PropertyWare.additionalLateChargesLimit = text.substring(text.indexOf(PDFAppConfig_Format2.additionaLateCharge_Prior)+PDFAppConfig_Format2.additionaLateCharge_Prior.length()).split(" ")[0].trim();
		    System.out.println("Additional Late Charges  = "+AL_PropertyWare.additionalLateChargesLimit);//.substring(commensementDate.lastIndexOf(":")+1));
	    }
	    catch(Exception e)
	    {
	    	AL_PropertyWare.additionalLateChargesLimit = "Error";
	    	e.printStackTrace();
	    }
	    
	    try
    	{
    	AL_PropertyWare.securityDeposit = text.substring(text.indexOf(PDFAppConfig_Format2.securityDeposit_Prior)+PDFAppConfig_Format2.securityDeposit_Prior.length(),text.indexOf(PDFAppConfig_Format2.securityDeposit_After));
	    System.out.println("Security Deposit = "+AL_PropertyWare.securityDeposit.trim());
	    if(RunnerClass.onlyDigits(AL_PropertyWare.securityDeposit)==true)
	    {
	    	System.out.println("Security Deposit is checked and has value");
	    }
    	}
    	catch(Exception e)
    	{
    	AL_PropertyWare.securityDeposit = "Error";	
    	e.printStackTrace();
    	}
	    
	    
	    
	    // Checking Pet Addendum is available or not
	    
	    petFlag = text.contains(PDFAppConfig_Format2.petAgreementAvailabilityCheck);
	    System.out.println("Pet Addendum Available = "+petFlag);
	    if(petFlag ==true)
	    {
	    	try
    		{
    			AL_PropertyWare.petRent = text.substring(text.indexOf(PDFAppConfig_Format2.petRent_Prior)+PDFAppConfig_Format2.petRent_Prior.length()).split(" ")[0].trim();
    			
				 System.out.println("Pet rent = "+AL_PropertyWare.petRent.trim());
    		}
    		
    		catch(Exception e1)
		    {
		    	AL_PropertyWare.petRent = "Error";  
		    	e1.printStackTrace();
		    }
	    	
	    	String newText = text.replace("Type:","");
		    AL_PropertyWare.countOfTypeWordInText = ((text.length() - newText.length())/"Type:".length());
		    System.out.println("Type: occurences = "+AL_PropertyWare.countOfTypeWordInText);
		    
		    if(AL_PropertyWare.countOfTypeWordInText==2)
		    {
			    try
			    {
			    AL_PropertyWare.pet1Type = text.substring(text.indexOf(PDFAppConfig_Format2.petType_Prior)+PDFAppConfig_Format2.petType_Prior.length(),text.indexOf(PDFAppConfig_Format2.petType_After)).trim();
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
			    AL_PropertyWare.pet2Type = text.substring(RunnerClass.nthOccurrence(text, "Type:", 2)+PDFAppConfig_Format2.petType_Prior.length(),RunnerClass.nthOccurrence(text, "Breed:", 2));
			    System.out.println("Pet 2 Type = "+AL_PropertyWare.pet2Type);
			    }
			    catch(Exception e) 
			    {
			    	AL_PropertyWare.pet2Type =	 "Error";
			    	e.printStackTrace();
			    }
			    try
			    {
				    int pet1Breedindex1 = RunnerClass.nthOccurrence(text, "Breed:", 1)+PDFAppConfig_Format2.petType_Prior.length()+1;
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
				    AL_PropertyWare.pet1Type = text.substring(text.indexOf(PDFAppConfig_Format2.petType_Prior)+PDFAppConfig_Format2.petType_Prior.length(),text.indexOf(PDFAppConfig_Format2.petType_After)).trim();
				    System.out.println("Pet 1 Type = "+AL_PropertyWare.pet1Type.trim());
				    }
				    catch(Exception e)
				    {
				    	AL_PropertyWare.pet1Type = "Error";
				    	e.printStackTrace();
				    }
			    	try
				    {
					    int pet1Breedindex1 = RunnerClass.nthOccurrence(text, "Breed:", 1)+PDFAppConfig_Format2.petType_Prior.length()+1;
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
			    AL_PropertyWare.serviceAnimalType = text.substring(RunnerClass.nthOccurrence(text, "Type:", 3)+PDFAppConfig_Format2.petType_Prior.length(),RunnerClass.nthOccurrence(text, "Breed:", 3));
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
		    	AL_PropertyWare.petOneTimeNonRefundableFee = text.substring(text.indexOf(PDFAppConfig_Format2.petOneTimeNonRefundable_Prior)+PDFAppConfig_Format2.petOneTimeNonRefundable_Prior.length(),text.indexOf(PDFAppConfig_Format2.petOneTimeNonRefundable_After)).trim();
			    System.out.println("pet one time non refundable = "+AL_PropertyWare.petOneTimeNonRefundableFee.trim());
		    }
		    catch(Exception e)
		    {
		    	AL_PropertyWare.petOneTimeNonRefundableFee =  "Error";
		    	e.printStackTrace();
		    }  
		    
		    
		    
		    
	    }
		return true;
	    
	    
	    
	}
}
