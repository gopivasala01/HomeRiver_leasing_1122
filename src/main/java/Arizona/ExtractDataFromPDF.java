package Arizona;

import java.io.File;
import java.io.FileInputStream;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import mainPackage.RunnerClass;


public class ExtractDataFromPDF
{
    public static boolean petFlag;
	public boolean arizona() throws Exception
	//public static void main(String args[]) throws Exception
	{
		try
		{
		File file = RunnerClass.getLastModified();
		//File file = new File("C:\\Gopi\\Projects\\Property ware\\Lease Close Outs\\PDFS\\Arizona\\Lease_922_823_949_N_California_St_AZ_Abner_.pdf");
		FileInputStream fis = new FileInputStream(file);
		PDDocument document = PDDocument.load(fis);
	    String text = new PDFTextStripper().getText(document);
	    //System.out.println(text);
	    System.out.println("------------------------------------------------------------------");
	   // String dates = text.substring(text.indexOf("Commencement Date"), )
	    try
	    {
	    AZ_PropertyWare.commensementDate = text.substring(text.indexOf(PDFAppConfig.AZ_commencementDate_Prior)+PDFAppConfig.AZ_commencementDate_Prior.length(),text.indexOf(PDFAppConfig.AZ_commencementDate_After));
	    System.out.println("Commensement Date = "+AZ_PropertyWare.commensementDate.trim());//.substring(commensementDate.lastIndexOf(":")+1));
	    }
	    catch(Exception e)
	    {
	    	AZ_PropertyWare.commensementDate = "Error";
	    }
	    try
	    {
	    AZ_PropertyWare.expirationDate = text.substring(text.indexOf(PDFAppConfig.AZ_expirationDate_Prior)+PDFAppConfig.AZ_expirationDate_Prior.length(),text.indexOf(PDFAppConfig.AZ_expirationDate_After));
	    System.out.println("Expiration Date = "+AZ_PropertyWare.expirationDate.trim());
	    }
	    catch(Exception e)
	    {
	    	 AZ_PropertyWare.expirationDate = "Error";
	    }
	    try
	    {
	    AZ_PropertyWare.proratedRent = text.substring(text.indexOf(PDFAppConfig.AZ_proratedRent_Prior)+PDFAppConfig.AZ_proratedRent_Prior.length(),text.indexOf(PDFAppConfig.AZ_proratedRent_After));
	    //String tenantNames = tenantRaw.substring(tenantRaw.indexOf("Tenant(s):")+10,tenantRaw.indexOf("."));
	    System.out.println("Prorated Rent = "+AZ_PropertyWare.proratedRent);
	    }
	    catch(Exception e)
	    {
	    	AZ_PropertyWare.proratedRent = "Error";
	    }
	    try
	    {
	    AZ_PropertyWare.proratedRentDate = text.substring(text.indexOf(PDFAppConfig.AZ_proratedRentDate_Prior)+PDFAppConfig.AZ_proratedRentDate_Prior.length(),text.indexOf(PDFAppConfig.AZ_proratedRentDate_After));
	    System.out.println("Prorated Rent Date= "+AZ_PropertyWare.proratedRentDate.trim());
	    }
	    catch(Exception e)
	    {
	    	AZ_PropertyWare.proratedRentDate = "Error";
	    }
	    try
	    {
	    AZ_PropertyWare.monthlyRent = text.substring(text.indexOf(PDFAppConfig.AZ_fullRent_Prior)+PDFAppConfig.AZ_fullRent_Prior.length(),text.indexOf(PDFAppConfig.AZ_fullRent_After)).trim().substring(1).trim();
	    System.out.println("Monthly Rent "+AZ_PropertyWare.monthlyRent.trim());
	    }
	    catch(Exception e)
	    {
	    	 AZ_PropertyWare.monthlyRent = "Error";
	    }
	    try
	    {
	    AZ_PropertyWare.monthlyRentDate = text.substring(text.indexOf(PDFAppConfig.AZ_fullRentDate_Prior)+PDFAppConfig.AZ_fullRentDate_Prior.length(),text.indexOf(PDFAppConfig.AZ_fullRentDate_After));
	    System.out.println("Monthly Rent Date "+AZ_PropertyWare.monthlyRentDate.trim());
	    }
	    catch(Exception e)
	    {
	    	AZ_PropertyWare.monthlyRentDate = "Error";
	    }
	   //Float fullMonthRent = Float.parseFloat(monthlyRent.trim().replaceAll("[-+$^:,]",""))- Float.parseFloat(proratedRentDate);
	    
	    //System.out.println("Full Monthly Rent "+monthlyRent);
	    //PDFCheckboxFinder pdfcheckbox = new PdfCheckboxFinder(document);
	    try
	    {
	    AZ_PropertyWare.adminFee = text.substring(text.indexOf(PDFAppConfig.AZ_adminFee_Prior)+PDFAppConfig.AZ_adminFee_Prior.length()).split(" ")[0];
	    System.out.println("Admin Fee = "+AZ_PropertyWare.adminFee.trim());
	    }
	    catch(Exception e)
	    {
	    AZ_PropertyWare.adminFee = "Error";
	    }
	    try
	    {
	    AZ_PropertyWare.airFilterFee = text.substring(text.indexOf(PDFAppConfig.AZ_airFilterFee_Prior)+PDFAppConfig.AZ_airFilterFee_Prior.length(),text.indexOf(PDFAppConfig.AZ_airFilterFee_After));
	    System.out.println("Air Filter Fee = "+AZ_PropertyWare.airFilterFee.trim());
	    }
	    catch(Exception e)
	    {
	    AZ_PropertyWare.airFilterFee = "Error";
	    }
	    try
	    {
	    AZ_PropertyWare.earlyTermiantion = text.substring(text.indexOf(PDFAppConfig.AZ_earlyTerminationFee_Prior)+PDFAppConfig.AZ_earlyTerminationFee_Prior.length(),text.indexOf(PDFAppConfig.AZ_earlyTerminationFee_After));
	    System.out.println("Early Termination  = "+AZ_PropertyWare.earlyTermiantion.trim());
	    }
	    catch(Exception e)
	    {
	    	AZ_PropertyWare.earlyTermiantion = "Error";	
	    }
	    try
	    {
	    AZ_PropertyWare.occupants = text.substring(text.indexOf(PDFAppConfig.AZ_occupants_Prior)+PDFAppConfig.AZ_occupants_Prior.length(),text.indexOf(PDFAppConfig.AZ_occupants_After));
	    System.out.println("Occupants = "+AZ_PropertyWare.occupants.trim());
	    }
	    catch(Exception e)
	    {
	    AZ_PropertyWare.occupants ="Error";	
	    }
	    try
	    {
	    AZ_PropertyWare.lateChargeDay = text.substring(text.indexOf(PDFAppConfig.AZ_lateChargeDay_Prior)+PDFAppConfig.AZ_lateChargeDay_Prior.length(),text.indexOf(PDFAppConfig.AZ_lateChargeDay_After));
	    System.out.println("Late Charge Day = "+AZ_PropertyWare.lateChargeDay.trim());
	    }
	    catch(Exception e)
	    {
	    	AZ_PropertyWare.lateChargeDay = "Error";	
	    }
	    try
	    {
	    AZ_PropertyWare.lateChargeFee = text.substring(text.indexOf(PDFAppConfig.AZ_lateFee_Prior)+PDFAppConfig.AZ_lateFee_Prior.length(),text.indexOf(PDFAppConfig.AZ_lateFee_After));
	    System.out.println("Late Charge Fee = "+AZ_PropertyWare.lateChargeFee.trim());
	    // Check if Pet Agreement is available or not
	    }
	    catch(Exception e)
	    {
	    AZ_PropertyWare.lateChargeFee ="Error";	
	    }
	    petFlag = text.contains(PDFAppConfig.AZ_petAgreementAvailabilityCheck);
	    System.out.println("Pet Addendum Available = "+petFlag);
	    
	    if(petFlag ==true)
	    {
	    	try
	    	{
	    	AZ_PropertyWare.petSecurityDeposit = text.substring(text.indexOf(PDFAppConfig.AZ_securityDeposity_Prior)+PDFAppConfig.AZ_securityDeposity_Prior.length(),text.indexOf(PDFAppConfig.AZ_securityDeposity_After));
		    System.out.println("Security Deposit = "+AZ_PropertyWare.petSecurityDeposit.trim());
	    	}
	    	catch(Exception e)
	    	{
	    	AZ_PropertyWare.petSecurityDeposit = "Error";	
	    	}
		    if(RunnerClass.onlyDigits(AZ_PropertyWare.petSecurityDeposit)==true)
		    {
		    	System.out.println("Security Deposit is checked");
		    }
		    try
		    {
		    	String proratedPetRentRaw = text.substring(text.indexOf(PDFAppConfig.AZ_proratedPetRent_Prior));//+AppConfig.AZ_proratedPetRent_Prior.length(),text.indexOf(AppConfig.AZ_proratedPetRent_After));
			    AZ_PropertyWare.proratedPetRent = proratedPetRentRaw.substring(proratedPetRentRaw.trim().indexOf("Tenant will pay Landlord $")+"Tenant will pay Landlord $".length()).trim().split(" ")[0];//,proratedPetRentRaw.indexOf(AppConfig.AZ_proratedPetRent_After));
			    System.out.println("Prorated Pet Rent = "+AZ_PropertyWare.proratedPetRent.trim());
		    }
		    catch(Exception e)
		    {
		    	e.printStackTrace();
		    AZ_PropertyWare.proratedPetRent = "Error";	
		    }
		    try
		    {
		    String petRentRaw = text.substring(text.indexOf(PDFAppConfig.AZ_petRent_Prior)+PDFAppConfig.AZ_petRent_Prior.length());
		    AZ_PropertyWare.petRent = petRentRaw.trim().split(" ")[0];
		    System.out.println("Pet Rent = "+AZ_PropertyWare.petRent.trim());
		    }
		    catch(Exception e)
		    {
		    AZ_PropertyWare.petRent = "Error";  	
		    }
		    
		    try
		    {
		    	String petRentWithTaxRaw = text.substring(text.indexOf(PDFAppConfig.AZ_petRentWithTax_Prior)+PDFAppConfig.AZ_petRentWithTax_Prior.length());
			    AZ_PropertyWare.petRentWithTax = petRentWithTaxRaw.split(" ")[0];
			    System.out.println("Pet Rent with Tax = "+AZ_PropertyWare.petRentWithTax.trim());
		    }
		    catch(Exception e)
		    {
		    	e.printStackTrace();
		    	AZ_PropertyWare.petRentWithTax = "Error";
		    }
		    
		    String newText = text.replace("Type:","");
		    AZ_PropertyWare.countOfTypeWordInText = ((text.length() - newText.length())/"Type:".length());
		    System.out.println("Type: occurences = "+AZ_PropertyWare.countOfTypeWordInText);
		    
		    try
		    {
		    AZ_PropertyWare.pet1Type = text.substring(text.indexOf(PDFAppConfig.AZ_pet1Type_Prior)+PDFAppConfig.AZ_pet1Type_Prior.length(),text.indexOf(PDFAppConfig.AZ_pet1Type_After));
		    System.out.println("Pet 1 Type = "+AZ_PropertyWare.pet1Type.trim());
		    }
		    catch(Exception e)
		    {
		    	AZ_PropertyWare.pet1Type = "Error";
		    }
		    //Check if service animal is there
		    //PropertyWare.pet2Type = text.substring(text.indexOf("Type:", text.indexOf("Type:")+1)+AppConfig.AZ_pet1Type_Prior.length(),text.indexOf("Breed:", text.indexOf("Breed:")+1));
		    try
		    {
		    AZ_PropertyWare.pet2Type = text.substring(RunnerClass.nthOccurrence(text, "Type:", 2)+PDFAppConfig.AZ_pet1Type_Prior.length(),RunnerClass.nthOccurrence(text, "Breed:", 2));
		    System.out.println("Pet 2 Type = "+AZ_PropertyWare.pet2Type);
		    }
		    catch(Exception e) 
		    {
		    	AZ_PropertyWare.pet2Type =	 "Error";
		    }
		    
		   // System.out.println("Index 1 = "+Arizona.nthOccurrence(text, "Breed:", 1));
		    try
		    {
		    int pet1Breedindex1 = RunnerClass.nthOccurrence(text, "Breed:", 1)+PDFAppConfig.AZ_pet1Type_Prior.length()+1;
		    String subString = text.substring(pet1Breedindex1);
		    int pet1Breedindex2 = RunnerClass.nthOccurrence(subString,"Name:",1);
		   // System.out.println("Index 2 = "+(index2+index1));
		    AZ_PropertyWare.pet1Breed = text.substring(pet1Breedindex1,(pet1Breedindex2+pet1Breedindex1));
		    System.out.println("Pet 1 Breed = "+text.substring(pet1Breedindex1,(pet1Breedindex2+pet1Breedindex1)));
		    }
		    catch(Exception e)
		    {
		    	 AZ_PropertyWare.pet1Breed = "Error";
		    }
		    try
		    {
		    int pet2Breedindex1 = RunnerClass.nthOccurrence(text, "Breed:", 2)+"Breed:".length()+1;
		    String subString2 = text.substring(pet2Breedindex1);
		    int pet2Breedindex2 = RunnerClass.nthOccurrence(subString2,"Name:",1);
		   // System.out.println("Index 2 = "+(index2+index1));
		    AZ_PropertyWare.pet2Breed = text.substring(pet2Breedindex1,(pet2Breedindex2+pet2Breedindex1));
		    System.out.println("Pet 2 Breed = "+text.substring(pet2Breedindex1,(pet2Breedindex2+pet2Breedindex1)));
		    }
		    catch(Exception e)
		    {
		    	AZ_PropertyWare.pet2Breed = "Error";
		    }
	        if(AZ_PropertyWare.countOfTypeWordInText>2)
		        {
			    try
			    {
			    AZ_PropertyWare.serviceAnimalType = text.substring(RunnerClass.nthOccurrence(text, "Type:", 3)+PDFAppConfig.AZ_pet1Type_Prior.length(),RunnerClass.nthOccurrence(text, "Breed:", 3));
			    System.out.println("Service Animal Type = "+AZ_PropertyWare.serviceAnimalType);
			    }
			    catch(Exception e)
			    {
			    	 AZ_PropertyWare.serviceAnimalType = "Error";
			    }
			    try
				    {
				    int serviceAnimalBreedindex1 = RunnerClass.nthOccurrence(text, "Breed:", 3)+"Breed:".length()+1;
				    String serviceAnimalsubString = text.substring(serviceAnimalBreedindex1);
				    int serviceAnimalBreedindex2 = RunnerClass.nthOccurrence(serviceAnimalsubString,"Name:",1);
				   // System.out.println("Index 2 = "+(index2+index1));
				    AZ_PropertyWare.serviceAnimalBreed = text.substring(serviceAnimalBreedindex1,(serviceAnimalBreedindex2+serviceAnimalBreedindex1));
				    System.out.println("Service Animal Breed = "+text.substring(serviceAnimalBreedindex1,(serviceAnimalBreedindex2+serviceAnimalBreedindex1)));
				    }
			    catch(Exception e)
				    {
				    	AZ_PropertyWare.serviceAnimalBreed = "Error";
				    }
			    try
			    {
			    int serviceAnimalWeightindex1 = RunnerClass.nthOccurrence(text, "Weight:", 3)+"Weight:".length()+1;
			    String serviceAnimalWeightSubstring = text.substring(serviceAnimalWeightindex1);
			    int serviceAnimalWeightIndex2 = RunnerClass.nthOccurrence(serviceAnimalWeightSubstring,"Age:",1);
			   // System.out.println("Index 2 = "+(index2+index1));
			    AZ_PropertyWare.serviceAnimalWeight = text.substring(serviceAnimalWeightindex1,(serviceAnimalWeightIndex2+serviceAnimalWeightindex1));
			    System.out.println("Service Animal Weight = "+text.substring(serviceAnimalWeightindex1,(serviceAnimalWeightIndex2+serviceAnimalWeightindex1)));
			    }
			    catch(Exception e)
			    {
			    	AZ_PropertyWare.serviceAnimalWeight = "Error";
			    }
		        }
		    try
		    {
		    int pet1Weightindex1 = RunnerClass.nthOccurrence(text, "Weight:", 1)+"Weight:".length()+1;
		    String pet1WeightSubstring = text.substring(pet1Weightindex1);
		    int pet1WeightIndex2 = RunnerClass.nthOccurrence(pet1WeightSubstring,"Age:",1);
		   // System.out.println("Index 2 = "+(index2+index1));
		    AZ_PropertyWare.pet1Weight = text.substring(pet1Weightindex1,(pet1WeightIndex2+pet1Weightindex1));
		    System.out.println("Pet 1 Weight = "+text.substring(pet1Weightindex1,(pet1WeightIndex2+pet1Weightindex1)));
		    }
		    catch(Exception e)
		    {
		    	AZ_PropertyWare.pet1Weight = "Error";
		    }
		    try
		    {
		    int pet2Weightindex1 = RunnerClass.nthOccurrence(text, "Weight:", 2)+"Weight:".length()+1;
		    String pet2WeightSubstring = text.substring(pet2Weightindex1);
		    int pet2WeightIndex2 = RunnerClass.nthOccurrence(pet2WeightSubstring,"Age:",1);
		   // System.out.println("Index 2 = "+(index2+index1));
		    AZ_PropertyWare.pet2Weight = text.substring(pet2Weightindex1,(pet2WeightIndex2+pet2Weightindex1));
		    System.out.println("Pet 2 Weight = "+text.substring(pet2Weightindex1,(pet2WeightIndex2+pet2Weightindex1)));
		    }
		    catch(Exception e)
		    {
		    	AZ_PropertyWare.pet2Weight = "Error";
		    }
		    
		    
		    try
		    {
		    	AZ_PropertyWare.petOneTimeNonRefundableFee = text.substring(text.indexOf(PDFAppConfig.AZ_petFeeOneTime_Prior)+PDFAppConfig.AZ_petFeeOneTime_Prior.length(),text.indexOf(PDFAppConfig.AZ_petFeeOneTime_After));
			    System.out.println("pet one time non refundable = "+AZ_PropertyWare.petOneTimeNonRefundableFee.trim());
		    }
		    catch(Exception e)
		    {
		    	AZ_PropertyWare.petOneTimeNonRefundableFee =  "Error";	
		    }
	        }
		    try
		    {
		    	AZ_PropertyWare.lateFeeChargePerDay = text.substring(text.indexOf(PDFAppConfig.AZ_additionalLateChargesPerDay_Prior)+PDFAppConfig.AZ_additionalLateChargesPerDay_Prior.length(),text.indexOf(PDFAppConfig.AZ_additionalLateChargesPerDay_After));
			    System.out.println("Late Fee Charge Per Day = "+AZ_PropertyWare.lateFeeChargePerDay.trim());
		    }
		    catch(Exception e)
		    {
		    	AZ_PropertyWare.lateFeeChargePerDay =  "Error";	
		    }
		    try
		    {
		    	AZ_PropertyWare.additionalLateCharges = text.substring(text.indexOf(PDFAppConfig.AZ_lateChargesPerDay_Prior)+PDFAppConfig.AZ_lateChargesPerDay_Prior.length(),text.indexOf(PDFAppConfig.AZ_lateChargesPerDay_After));
			    System.out.println("pet one time non refundable = "+AZ_PropertyWare.additionalLateCharges.trim());
		    }
		    catch(Exception e)
		    {
		    	AZ_PropertyWare.additionalLateCharges =  "Error";	
		    }
		    try
		    {
		    	AZ_PropertyWare.additionalLateChargesLimit = text.substring(text.indexOf(PDFAppConfig.AZ_additionalLateChargesLimit_Prior)+PDFAppConfig.AZ_additionalLateChargesLimit_Prior.length(),text.indexOf(PDFAppConfig.AZ_additionalLateChargesLimit_After));
			    System.out.println("pet one time non refundable Limit= "+AZ_PropertyWare.additionalLateChargesLimit.trim());
		    }
		    catch(Exception e)
		    {
		    	AZ_PropertyWare.additionalLateChargesLimit =  "Error";	
		    }
		    
	    
	
	    
	    return true;
		}
		
		catch(Exception e)
		{
			e.printStackTrace();
			RunnerClass.leaseCompletedStatus =2;
			return false;
		}
	}
}
