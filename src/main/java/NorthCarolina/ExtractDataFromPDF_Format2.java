package NorthCarolina;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;


import mainPackage.RunnerClass;

public class ExtractDataFromPDF_Format2 
{
	public static boolean petFlag;
	public boolean arizona() throws Exception
	//public static void main(String[] args) throws Exception 
	{
		NC_PropertyWare.petFlag = false;
		//File file = new File("C:\\Gopi\\Projects\\Property ware\\Lease Close Outs\\PDFS\\North Carolinas format 1- 2\\Lease_9.21_9.22_92_E_Painted_Way_NC_Kerns_Dowell.pdf");
		File file = RunnerClass.getLastModified();
		FileInputStream fis = new FileInputStream(file);
		PDDocument document = PDDocument.load(fis);
	    String text = new PDFTextStripper().getText(document);
	    text = text.replaceAll(System.lineSeparator(), " ");
	    text = text.trim().replaceAll(" +", " ");
	    System.out.println(text);
	    System.out.println("------------------------------------------------------------------");
	    
	    try
	    {
	    	String commensementRaw = text.substring(text.indexOf(PDFAppConfig_Format2.commensementDate_Prior)+PDFAppConfig_Format2.commensementDate_Prior.length()+1).trim();//,text.indexOf(PDFAppConfig_Format2.commensementDate_After)).trim();
	    	 NC_PropertyWare.commensementDate = commensementRaw.substring(0, commensementRaw.indexOf('(')).trim();
	    	 NC_PropertyWare.commensementDate = NC_PropertyWare.commensementDate.trim().replaceAll(" +", " ");
		    System.out.println("Commensement Date = "+NC_PropertyWare.commensementDate);//.substring(commensementDate.lastIndexOf(":")+1));
	    }
	    catch(Exception e)
	    {
	    	NC_PropertyWare.commensementDate = "Error";
	    	e.printStackTrace();
	    }
	    
	    try
	    {
	    	String expirationDateRaw  = text.substring(text.indexOf(PDFAppConfig_Format2.expirationDate_Prior)+PDFAppConfig_Format2.expirationDate_Prior.length()).trim();//,text.indexOf(PDFAppConfig_Format2.expirationDate_After)).trim();
	    	NC_PropertyWare.expirationDate = expirationDateRaw.substring(0,expirationDateRaw.indexOf('(')).trim();
	    	NC_PropertyWare.expirationDate = NC_PropertyWare.expirationDate.trim().replaceAll(" +", " ");
	    	System.out.println("Expiration Date = "+NC_PropertyWare.expirationDate);//.substring(commensementDate.lastIndexOf(":")+1));
	    }
	    catch(Exception e)
	    {
	    	NC_PropertyWare.expirationDate = "Error";
	    	e.printStackTrace();
	    }
	    
	    try
	    {
	    	NC_PropertyWare.proratedRentDate = text.substring(text.indexOf(PDFAppConfig_Format2.proratedRentDate_Prior)+PDFAppConfig_Format2.proratedRentDate_Prior.length()+1,text.indexOf(PDFAppConfig_Format2.proratedRentDate_After)).trim();
		    System.out.println("prorated Rent Date = "+NC_PropertyWare.proratedRentDate);//.substring(commensementDate.lastIndexOf(":")+1));
	    }
	    catch(Exception e)
	    {
	    	NC_PropertyWare.proratedRentDate = "Error";
	    	e.printStackTrace();
	    }
	    
	    try
	    {
	    	NC_PropertyWare.proratedRent = text.substring(text.indexOf(PDFAppConfig_Format2.proratedRent_Prior)+PDFAppConfig_Format2.proratedRent_Prior.length()).split(" ")[0].trim();
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
	    System.out.println("prorated Rent = "+NC_PropertyWare.proratedRent);//.substring(commensementDate.lastIndexOf(":")+1));
	    
	    try
	    {
	    	NC_PropertyWare.monthlyRent = text.substring(text.indexOf(PDFAppConfig_Format2.monthlyRent_Prior)+PDFAppConfig_Format2.monthlyRent_Prior.length()).split(" ")[0].trim();
	    	if(!NC_PropertyWare.monthlyRent.contains("."))
	    		NC_PropertyWare.monthlyRent = text.substring(text.indexOf(PDFAppConfig_Format2.monthlyRent_Prior2)+PDFAppConfig_Format2.monthlyRent_Prior2.length()).split(" ")[0].trim();
	    	if(NC_PropertyWare.monthlyRent.matches(".*[a-zA-Z]+.*"))
		    {
		    	NC_PropertyWare.monthlyRent = "Error";
		    }
	    	if(NC_PropertyWare.monthlyRent.contains("*"))
	    	{
	    		NC_PropertyWare.incrementRentFlag = true;
	    		NC_PropertyWare.monthlyRent = NC_PropertyWare.monthlyRent.replace("*", "");
	    		System.out.println("Monthly Rent has Asterick *");
	    		String increasedRent_ProviousRentEndDate = "Per the Landlord, Monthly Rent from "+NC_PropertyWare.commensementDate.trim()+" through ";
	    		 String endDateArray[] = text.substring(text.indexOf(increasedRent_ProviousRentEndDate)+increasedRent_ProviousRentEndDate.length()).split(" ");
	    		 if(endDateArray[2].trim().length()==4&&RunnerClass.onlyDigits(endDateArray[2]))
	    		 {
	    		  NC_PropertyWare.increasedRent_previousRentEndDate = endDateArray[0]+" "+endDateArray[1]+" "+endDateArray[2];
	    		  System.out.println("Increased Rent - Previous rent end date = "+NC_PropertyWare.increasedRent_previousRentEndDate);
	    		 
	    		  String newRentStartDate[] = text.substring(text.indexOf(PDFAppConfig_Format2.increasedRent_newStartDate_Prior)+PDFAppConfig_Format2.increasedRent_newStartDate_Prior.length()).trim().split(" ");
	    		  NC_PropertyWare.increasedRent_newStartDate = newRentStartDate[0]+" "+newRentStartDate[1]+" "+newRentStartDate[2];
	    		  System.out.println("Increased Rent - New Rent Start date = "+NC_PropertyWare.increasedRent_newStartDate);
	    		  
	    		  String increasedRentRaw = text.substring(text.indexOf(PDFAppConfig_Format2.increasedRent_newStartDate_Prior)+PDFAppConfig_Format2.increasedRent_newStartDate_Prior.length()).trim();
	    		  NC_PropertyWare.increasedRent_amount = increasedRentRaw.substring(increasedRentRaw.indexOf("shall be $")+"shall be $".length()).trim().split(" ")[0];
	    		  System.out.println("Increased Rent - Amount = "+NC_PropertyWare.increasedRent_amount); 
	    		 }
	    	}
	    }
	    catch(Exception e)
	    {
	    	NC_PropertyWare.monthlyRent = "Error";
	    	e.printStackTrace();
	    }
	    System.out.println("Monthly Rent = "+NC_PropertyWare.monthlyRent);//.substring(commensementDate.lastIndexOf(":")+1));
	   
	    try
	    {
	    	NC_PropertyWare.adminFee = text.substring(text.indexOf(PDFAppConfig_Format2.adminFee_prior)+PDFAppConfig_Format2.adminFee_prior.length()).split(" ")[0].trim();
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
	    System.out.println("Admin Fee = "+NC_PropertyWare.adminFee);//.substring(commensementDate.lastIndexOf(":")+1));
	   
	    try
	    {
	    	NC_PropertyWare.airFilterFee = text.substring(text.indexOf(PDFAppConfig_Format2.HVACAirFilter_prior)+PDFAppConfig_Format2.HVACAirFilter_prior.length()).split(" ")[0].trim();
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
	    
	    System.out.println("HVAC Air Filter Fee = "+NC_PropertyWare.airFilterFee);//.substring(commensementDate.lastIndexOf(":")+1));
	    try
	    {
	    	NC_PropertyWare.occupants = text.substring(text.indexOf(PDFAppConfig_Format2.occupants_Prior)+PDFAppConfig_Format2.occupants_Prior.length(),text.indexOf(PDFAppConfig_Format2.occupants_After)).trim();
		    System.out.println("Occupants = "+NC_PropertyWare.occupants);//.substring(commensementDate.lastIndexOf(":")+1));
	    }
	    catch(Exception e)
	    {
	    	NC_PropertyWare.occupants = "Error";
	    	e.printStackTrace();
	    }
	    
	    try
	    {
	    	NC_PropertyWare.lateChargeDay = text.substring(text.indexOf(PDFAppConfig_Format2.lateFeeDay_Prior)+PDFAppConfig_Format2.lateFeeDay_Prior.length()).split("th")[0].trim();
		    System.out.println("Late Charge Day = "+NC_PropertyWare.lateChargeDay);//.substring(commensementDate.lastIndexOf(":")+1));
	    }
	    catch(Exception e)
	    {
	    	NC_PropertyWare.lateChargeDay = "Error";
	    	e.printStackTrace();
	    }
	    
	    
	    try
	    {
	    	NC_PropertyWare.lateChargeFee = text.substring(text.indexOf(PDFAppConfig_Format2.initialLateChargeFee_Prior)+PDFAppConfig_Format2.initialLateChargeFee_Prior.length()).split(" ")[0].trim();
		    System.out.println("Initital Late Charge  = "+NC_PropertyWare.lateChargeFee);//.substring(commensementDate.lastIndexOf(":")+1));
	    }
	    catch(Exception e)
	    {
	    	NC_PropertyWare.lateChargeFee = "Error";
	    	e.printStackTrace();
	    }
	    //Per Day Fee
	    try
	    {
	    	NC_PropertyWare.lateFeeChargePerDay = text.substring(text.indexOf(PDFAppConfig_Format2.lateChargePerDayFee)+PDFAppConfig_Format2.lateChargePerDayFee.length()).split(" ")[0].trim();
		    System.out.println("Per Day Fee  = "+NC_PropertyWare.lateFeeChargePerDay);//.substring(commensementDate.lastIndexOf(":")+1));
	    }
	    catch(Exception e)
	    {
	    	NC_PropertyWare.additionalLateChargesLimit = "Error";
	    	e.printStackTrace();
	    }
	    
	    try
	    {
	    if(NC_RunnerClass.pdfFormatType.equalsIgnoreCase("Format1"))
	    {
	    	NC_PropertyWare.additionalLateChargesLimit = text.substring(text.indexOf(PDFAppConfig_Format2.additionaLateCharge_Prior)+PDFAppConfig_Format2.additionaLateCharge_Prior.length()).split(" ")[0].trim();
		    System.out.println("Additional Late Charges  = "+NC_PropertyWare.additionalLateChargesLimit);//.substring(commensementDate.lastIndexOf(":")+1));
	    }
	    }
	    catch(Exception e)
	    {
	    	NC_PropertyWare.additionalLateChargesLimit = "Error";
	    	e.printStackTrace();
	    }
	    
	    
	  //Prepayment Charge
	    try
	    {
		if(NC_PropertyWare.portfolioType.contains("MCH"))
		{
		try
		{
		NC_PropertyWare.prepaymentCharge =String.valueOf(Double.parseDouble(NC_PropertyWare.monthlyRent.replace(",", "")) - Double.parseDouble(NC_PropertyWare.proratedRent.replace(",", ""))); 
		}
		catch(Exception e)
		{
			NC_PropertyWare.prepaymentCharge ="Error";
		}
		System.out.println("Prepayment Charge = "+NC_PropertyWare.prepaymentCharge);
		}
	    }
	    catch(Exception e) {}
	    //Early Termination
		try
	    {
	    	String[] earlyTerminationRaw = text.substring(text.indexOf(PDFAppConfig_Format2.earlyTermination_Prior)+PDFAppConfig_Format2.earlyTermination_Prior.length()).split(" ");
	    	
		    NC_PropertyWare.earlyTermination = earlyTerminationRaw[0]+earlyTerminationRaw[1]; //text.substring(text.indexOf(PDFAppConfig.AB_earlyTerminationFee_Prior)+PDFAppConfig.AB_earlyTerminationFee_Prior.length(),text.indexOf(PDFAppConfig.AB_earlyTerminationFee_After));
	    }
	    catch(Exception e)
	    {
	    	NC_PropertyWare.earlyTermination = "Error";	
	    	e.printStackTrace();
	    }
		System.out.println("Early Termination = "+NC_PropertyWare.earlyTermination);
	    // Checking Pet Addendum is available or not
	    
	    petFlag = text.contains(PDFAppConfig_Format2.petAgreementAvailabilityCheck);
	    System.out.println("Pet Addendum Available = "+petFlag);
	    if(petFlag ==true)
	    {
	    	NC_PropertyWare.petFlag = true;
			    	try
			    	{
			    		 String proratedPetRaw = "Prorated Pet Rent: On or before "+NC_PropertyWare.commensementDate.trim()+" Tenant will pay Landlord $";
				    		NC_PropertyWare.proratedPetRent = text.substring(text.indexOf(proratedPetRaw)+proratedPetRaw.length()).trim().split(" ")[0].trim();//.replaceAll("[a-ZA-Z,]", "");
				    		if(NC_PropertyWare.proratedPetRent.matches(".*[a-zA-Z]+.*")||NC_PropertyWare.proratedPetRent.trim().equals("1."))
						    {
						    	NC_PropertyWare.proratedPetRent = "Error";
						    }
				    		//AR_PropertyWare.proratedPetRent = proratedPetRentRaw.substring(proratedPetRentRaw.indexOf("Tenant will pay Landlord $")+"Tenant will pay Landlord $".length());//,proratedPetRentRaw.indexOf(AppConfig.AR_proratedPetRent_After));
			        }
				    catch(Exception e)
				    {
				    NC_PropertyWare.proratedPetRent = "Error";	
				    e.printStackTrace();
				    }
	    	System.out.println("Prorated Pet rent= "+NC_PropertyWare.proratedPetRent.trim());
	    	try
    		{
    			NC_PropertyWare.petRent = text.substring(text.indexOf(PDFAppConfig_Format2.petRent_Prior)+PDFAppConfig_Format2.petRent_Prior.length()).split(" ")[0].trim();
				 //System.out.println("Pet rent = "+NC_PropertyWare.petRent.trim());
				 if(RunnerClass.onlyDigits(NC_PropertyWare.petRent)==false)
				    {
				    	 NC_PropertyWare.petRent = text.substring(text.indexOf(PDFAppConfig_Format2.petRent_Prior2)+PDFAppConfig_Format2.petRent_Prior2.length()).trim().split(" ")[0];
				    }
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
	    	System.out.println("Pet rent= "+NC_PropertyWare.petRent.trim());
	    	
	    	//Pet Security Deposit -- Need to find the text of Pet Security Deposit for the format PDF, until then, it is commented
	    	/*
	    	try
	    	{
	    	NC_PropertyWare.petSecurityDeposit = text.substring(text.indexOf(PDFAppConfig_Format2.securityDeposit_Prior)+PDFAppConfig_Format2.securityDeposit_Prior.length()).trim().split(" ")[0];//,text.indexOf(PDFAppConfig_Format2.securityDeposit_After));
		    System.out.println("Security Deposit = "+NC_PropertyWare.securityDeposit.trim());
		    if(RunnerClass.onlyDigits(NC_PropertyWare.petSecurityDeposit)==true)
		    {
		    	NC_PropertyWare.petSecurityDepositFlag = true;
		    	System.out.println("Security Deposit is checked and has value");
		    }
	    	}
	    	catch(Exception e)
	    	{
	    	NC_PropertyWare.securityDeposit = "Error";	
	    	e.printStackTrace();
	    	}
	    	*/
	    	String typeSubString = "";
	    	try
	    	{
	    	typeSubString = text.substring(text.indexOf(PDFAppConfig_Format2.typeWord_Prior)+PDFAppConfig_Format2.typeWord_Prior.length(),text.indexOf(PDFAppConfig_Format2.typeWord_After));
	    	}
	    	catch(Exception e)
	    	{
	    		try
	    		{
	    		typeSubString = text.substring(text.indexOf(PDFAppConfig_Format2.typeWord_Prior)+PDFAppConfig_Format2.typeWord_Prior.length(),text.indexOf(PDFAppConfig_Format2.typeWord_After2));
	    		}
	    		catch(Exception e2)
	    		{
	    		typeSubString = "";
	    		}
	    	}
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
		    
	    	
		    try
		    {
		    	NC_PropertyWare.petOneTimeNonRefundableFee = text.substring(text.indexOf(PDFAppConfig_Format2.petOneTimeNonRefundable_Prior)+PDFAppConfig_Format2.petOneTimeNonRefundable_Prior.length(),text.indexOf(PDFAppConfig_Format2.petOneTimeNonRefundable_After)).trim();
		    	if(NC_PropertyWare.petOneTimeNonRefundableFee.matches(".*[a-zA-Z]+.*"))
			    {
			    	NC_PropertyWare.petOneTimeNonRefundableFee = "Error";
			    }
		    }
		    catch(Exception e)
		    {
		    	try
		    	{
		    		NC_PropertyWare.petOneTimeNonRefundableFee = text.substring(text.indexOf(PDFAppConfig_Format2.petOneTimeNonRefundable_Prior2)+PDFAppConfig_Format2.petOneTimeNonRefundable_Prior2.length()).trim().split(",")[0];
				    //System.out.println("pet one time non refundable = "+NC_PropertyWare.petOneTimeNonRefundableFee.trim());
		    		if(NC_PropertyWare.petOneTimeNonRefundableFee.matches(".*[a-zA-Z]+.*"))
				    {
				    	NC_PropertyWare.petOneTimeNonRefundableFee = "Error";
				    }
		    	}
		    	catch(Exception e2)
		    	{
		    	NC_PropertyWare.petOneTimeNonRefundableFee =  "Error";
		    	e2.printStackTrace();
		    	}
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
    		
            String typeSubString = text.substring(text.indexOf(PDFAppConfig_Format2.AB_serviceAnimal_typeWord_Prior)+PDFAppConfig_Format2.AB_serviceAnimal_typeWord_Prior.length(),text.indexOf(PDFAppConfig_Format2.AB_serviceAnimal_typeWord_After));
	    	
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
