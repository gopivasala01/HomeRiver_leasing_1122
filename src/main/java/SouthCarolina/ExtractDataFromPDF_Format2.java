package SouthCarolina;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import mainPackage.RunnerClass;

public class ExtractDataFromPDF_Format2 
{
	public static boolean petFlag;
	public static String text ="";
	public boolean arizona() throws Exception
	//public static void main(String[] args) throws Exception 
	{
		SC_PropertyWare.petFlag = false;
		//File file = new File("C:\\Gopi\\Projects\\Property ware\\Lease Close Outs\\PDFS\\MAPL421\\Lease__10.21_10.22_5150_Rock_Place_Dr_GA_Mitchell_-_Robinson.pdf");
		File file = RunnerClass.getLastModified();
		FileInputStream fis = new FileInputStream(file);
		SC_RunnerClass.document = PDDocument.load(fis);
	    text = new PDFTextStripper().getText(SC_RunnerClass.document);
	    text = text.replaceAll(System.lineSeparator(), " ");
	    text = text.trim().replaceAll(" +", " ");
	    System.out.println(text);
	    System.out.println("------------------------------------------------------------------");
	    
	    try
	    {
	    	String commensementRaw = text.substring(text.indexOf(PDFAppConfig_Format2.commensementDate_Prior)+PDFAppConfig_Format2.commensementDate_Prior.length()+1).trim();//,text.indexOf(PDFAppConfig_Format2.commensementDate_After)).trim();
	    	 SC_PropertyWare.commensementDate = commensementRaw.substring(0, commensementRaw.indexOf('(')).trim();
	    	 SC_PropertyWare.commensementDate = SC_PropertyWare.commensementDate.trim().replaceAll(" +", " ");
		    System.out.println("Commensement Date = "+SC_PropertyWare.commensementDate);//.substring(commensementDate.lastIndexOf(":")+1));
	    }
	    catch(Exception e)
	    {
	    	SC_PropertyWare.commensementDate = "Error";
	    	e.printStackTrace();
	    }
	    
	    try
	    {
	    	String expirationDateRaw  = text.substring(text.indexOf(PDFAppConfig_Format2.expirationDate_Prior)+PDFAppConfig_Format2.expirationDate_Prior.length()).trim();//,text.indexOf(PDFAppConfig_Format2.expirationDate_After)).trim();
	    	SC_PropertyWare.expirationDate = expirationDateRaw.substring(0,expirationDateRaw.indexOf('(')).trim();
	    	SC_PropertyWare.expirationDate = SC_PropertyWare.expirationDate.trim().replaceAll(" +", " ");
	    	System.out.println("Expiration Date = "+SC_PropertyWare.expirationDate);//.substring(commensementDate.lastIndexOf(":")+1));
	    }
	    catch(Exception e)
	    {
	    	SC_PropertyWare.expirationDate = "Error";
	    	e.printStackTrace();
	    }
	    
	    try
	    {
	    	SC_PropertyWare.proratedRentDate = text.substring(text.indexOf(PDFAppConfig_Format2.proratedRentDate_Prior)+PDFAppConfig_Format2.proratedRentDate_Prior.length()+1,text.indexOf(PDFAppConfig_Format2.proratedRentDate_After)).trim();
		    System.out.println("prorated Rent Date = "+SC_PropertyWare.proratedRentDate);//.substring(commensementDate.lastIndexOf(":")+1));
	    }
	    catch(Exception e)
	    {
	    	SC_PropertyWare.proratedRentDate = "Error";
	    	e.printStackTrace();
	    }
	    
	    try
	    {
	    	SC_PropertyWare.proratedRent = text.substring(text.indexOf(PDFAppConfig_Format2.proratedRent_Prior)+PDFAppConfig_Format2.proratedRent_Prior.length()).split(" ")[0].trim();
	    	if(SC_PropertyWare.proratedRent.matches(".*[a-zA-Z]+.*"))
		    {
		    	SC_PropertyWare.proratedRent = "Error";
		    }
	    }
	    catch(Exception e)
	    {
	    	SC_PropertyWare.proratedRent = "Error";
	    	e.printStackTrace();
	    }
	    System.out.println("prorated Rent = "+SC_PropertyWare.proratedRent);//.substring(commensementDate.lastIndexOf(":")+1));
	    
	    try
	    {
	    	SC_PropertyWare.monthlyRent = text.substring(text.indexOf(PDFAppConfig_Format2.monthlyRent_Prior)+PDFAppConfig_Format2.monthlyRent_Prior.length()).split(" ")[0].trim();
	    	if(!SC_PropertyWare.monthlyRent.contains("."))
	    		SC_PropertyWare.monthlyRent = text.substring(text.indexOf(PDFAppConfig_Format2.monthlyRent_Prior2)+PDFAppConfig_Format2.monthlyRent_Prior2.length()).split(" ")[0].trim();
	    	if(SC_PropertyWare.monthlyRent.matches(".*[a-zA-Z]+.*"))
		    {
		    	SC_PropertyWare.monthlyRent = "Error";
		    }
	    	if(SC_PropertyWare.monthlyRent.contains("*")||text.contains(PDFAppConfig_Format2.monthlyRentAvailabilityCheck)==true)
	    	{
	    		SC_PropertyWare.incrementRentFlag = true;
	    		SC_PropertyWare.monthlyRent = SC_PropertyWare.monthlyRent.replace("*", "");
	    		System.out.println("Monthly Rent has Asterick *");
	    		String increasedRent_ProviousRentEndDate = "Per the Landlord, Monthly Rent from "+SC_PropertyWare.commensementDate.trim()+" through ";
	    		 String endDateArray[] = text.substring(text.indexOf(increasedRent_ProviousRentEndDate)+increasedRent_ProviousRentEndDate.length()).split(" ");
	    		 if(endDateArray[2].trim().length()==4&&RunnerClass.onlyDigits(endDateArray[2]))
	    		 {
	    		  SC_PropertyWare.increasedRent_previousRentEndDate = endDateArray[0]+" "+endDateArray[1]+" "+endDateArray[2];
	    		  System.out.println("Increased Rent - Previous rent end date = "+SC_PropertyWare.increasedRent_previousRentEndDate);
	    		 
	    		  String newRentStartDate[] = text.substring(text.indexOf(PDFAppConfig_Format2.increasedRent_newStartDate_Prior)+PDFAppConfig_Format2.increasedRent_newStartDate_Prior.length()).trim().split(" ");
	    		  SC_PropertyWare.increasedRent_newStartDate = newRentStartDate[0]+" "+newRentStartDate[1]+" "+newRentStartDate[2];
	    		  System.out.println("Increased Rent - New Rent Start date = "+SC_PropertyWare.increasedRent_newStartDate);
	    		  
	    		  String increasedRentRaw = text.substring(text.indexOf(PDFAppConfig_Format2.increasedRent_newStartDate_Prior)+PDFAppConfig_Format2.increasedRent_newStartDate_Prior.length()).trim();
	    		  SC_PropertyWare.increasedRent_amount = increasedRentRaw.substring(increasedRentRaw.indexOf("shall be $")+"shall be $".length()).trim().split(" ")[0];
	    		  System.out.println("Increased Rent - Amount = "+SC_PropertyWare.increasedRent_amount); 
	    		 }
	    	}
	    }
	    catch(Exception e)
	    {
	    	SC_PropertyWare.monthlyRent = "Error";
	    	e.printStackTrace();
	    }
	    System.out.println("Monthly Rent = "+SC_PropertyWare.monthlyRent);//.substring(commensementDate.lastIndexOf(":")+1));
	   
	    try
	    {
	    	SC_PropertyWare.adminFee = text.substring(text.indexOf(PDFAppConfig_Format2.adminFee_prior)+PDFAppConfig_Format2.adminFee_prior.length()).split(" ")[0].trim();
	    	if(SC_PropertyWare.adminFee.matches(".*[a-zA-Z]+.*"))
		    {
		    	SC_PropertyWare.adminFee = "Error";
		    }
	    }
	    catch(Exception e)
	    {
	    	SC_PropertyWare.adminFee = "Error";
	    	e.printStackTrace();
	    }
	    System.out.println("Admin Fee = "+SC_PropertyWare.adminFee);//.substring(commensementDate.lastIndexOf(":")+1));
	    //Resident Benefits Package 
	    if(text.contains(PDFAppConfig.residentBenefitsPackageAddendumCheck))
	    {
	    	SC_PropertyWare.residentBenefitsPackageAvailabilityCheck = true;
	    	 try
	 	    {
	 		    SC_PropertyWare.residentBenefitsPackage = text.substring(text.indexOf(PDFAppConfig_Format2.AB1_residentBenefitsPackage_Prior)+PDFAppConfig_Format2.AB1_residentBenefitsPackage_Prior.length()).split(" ")[0];
	 		    if(SC_PropertyWare.residentBenefitsPackage.matches(".*[a-zA-Z]+.*"))
	 		    {
	 		    	SC_PropertyWare.residentBenefitsPackage = "Error";
	 		    }
	 	    }
	 	    catch(Exception e)
	 	    {
	 		    SC_PropertyWare.residentBenefitsPackage = "Error";
	 		    e.printStackTrace();
	 	    }
	    	//PDFAppConfig.AB1_residentBenefitsPackage_Prior
	    	 System.out.println("Resident Benefits Package  = "+SC_PropertyWare.residentBenefitsPackage);
	    }
	    else
	    {
	    if(text.contains(PDFAppConfig_Format2.HVACFilterAddendumTextAvailabilityCheck)==true)
	    {
	    	SC_PropertyWare.HVACFilterFlag =true;
	    }
	    else
	    {
	    try
	    {
	    	SC_PropertyWare.airFilterFee = text.substring(text.indexOf(PDFAppConfig_Format2.HVACAirFilter_prior)+PDFAppConfig_Format2.HVACAirFilter_prior.length()).split(" ")[0].trim();
	    	if(SC_PropertyWare.airFilterFee.matches(".*[a-zA-Z]+.*"))
		    {
		    	SC_PropertyWare.airFilterFee = "Error";
		    }
	    }
	    catch(Exception e)
	    {
	    	SC_PropertyWare.airFilterFee = "Error";
	    	e.printStackTrace();
	    }
	    }
	    System.out.println("HVAC Air Filter Fee = "+SC_PropertyWare.airFilterFee);//.substring(commensementDate.lastIndexOf(":")+1));
	    }
	    try
	    {
	    	SC_PropertyWare.occupants = text.substring(text.indexOf(PDFAppConfig_Format2.occupants_Prior)+PDFAppConfig_Format2.occupants_Prior.length(),text.indexOf(PDFAppConfig_Format2.occupants_After)).trim();
		    System.out.println("Occupants = "+SC_PropertyWare.occupants);//.substring(commensementDate.lastIndexOf(":")+1));
	    }
	    catch(Exception e)
	    {
	    	SC_PropertyWare.occupants = "Error";
	    	e.printStackTrace();
	    }
	    
	    //Late Fee Rule
	    ExtractDataFromPDF_Format2.lateFeeRule();
	    
	    
	  //Prepayment Charge
	    try
	    {
		if(SC_PropertyWare.portfolioType.contains("MCH"))
		{
		try
		{
		SC_PropertyWare.prepaymentCharge =String.valueOf(Double.parseDouble(SC_PropertyWare.monthlyRent.replace(",", "")) - Double.parseDouble(SC_PropertyWare.proratedRent.replace(",", ""))); 
		}
		catch(Exception e)
		{
			SC_PropertyWare.prepaymentCharge ="Error";
		}
		System.out.println("Prepayment Charge = "+SC_PropertyWare.prepaymentCharge);
		}
	    }
	    catch(Exception e) {}
	    //Early Termination
		try
	    {
	    	String[] earlyTerminationRaw = text.substring(text.indexOf(PDFAppConfig_Format2.earlyTermination_Prior)+PDFAppConfig_Format2.earlyTermination_Prior.length()).split(" ");
	    	
		    SC_PropertyWare.earlyTermination = earlyTerminationRaw[0]+earlyTerminationRaw[1]; //text.substring(text.indexOf(PDFAppConfig.AB_earlyTerminationFee_Prior)+PDFAppConfig.AB_earlyTerminationFee_Prior.length(),text.indexOf(PDFAppConfig.AB_earlyTerminationFee_After));
	    }
	    catch(Exception e)
	    {
	    	SC_PropertyWare.earlyTermination = "Error";	
	    	e.printStackTrace();
	    }
		System.out.println("Early Termination = "+SC_PropertyWare.earlyTermination);
	    // Checking Pet Addendum is available or not
	    
	    if(text.contains(PDFAppConfig_Format2.petAgreementAvailabilityCheck)==true)
	    	petFlag =  true;
	    else if(petFlag = text.contains(PDFAppConfig_Format2.petAgreementAvailabilityCheck2)==true)
	    	petFlag =  true;
	    else petFlag =  false;
	    
	    System.out.println("Pet Addendum Available = "+petFlag);
	    if(petFlag ==true)
	    {
	    	SC_PropertyWare.petFlag = true;
			    	try
			    	{
			    		 String proratedPetRaw = "Prorated Pet Rent: On or before "+SC_PropertyWare.commensementDate.trim()+" Tenant will pay Landlord $";
				    		SC_PropertyWare.proratedPetRent = text.substring(text.indexOf(proratedPetRaw)+proratedPetRaw.length()).trim().split(" ")[0].trim();//.replaceAll("[a-ZA-Z,]", "");
				    		if(SC_PropertyWare.proratedPetRent.matches(".*[a-zA-Z]+.*")||SC_PropertyWare.proratedPetRent.trim().equals("1."))
						    {
						    	SC_PropertyWare.proratedPetRent = "Error";
						    }
				    		//AR_PropertyWare.proratedPetRent = proratedPetRentRaw.substring(proratedPetRentRaw.indexOf("Tenant will pay Landlord $")+"Tenant will pay Landlord $".length());//,proratedPetRentRaw.indexOf(AppConfig.AR_proratedPetRent_After));
			        }
				    catch(Exception e)
				    {
				    SC_PropertyWare.proratedPetRent = "Error";	
				    e.printStackTrace();
				    }
	    	System.out.println("Prorated Pet rent= "+SC_PropertyWare.proratedPetRent.trim());
	    	try
    		{
    			SC_PropertyWare.petRent = text.substring(text.indexOf(PDFAppConfig_Format2.petRent_Prior)+PDFAppConfig_Format2.petRent_Prior.length()).split(" ")[0].trim();
				 //System.out.println("Pet rent = "+SC_PropertyWare.petRent.trim());
				 if(RunnerClass.onlyDigits(SC_PropertyWare.petRent)==false)
				    {
				    	 SC_PropertyWare.petRent = text.substring(text.indexOf(PDFAppConfig_Format2.petRent_Prior2)+PDFAppConfig_Format2.petRent_Prior2.length()).trim().split(" ")[0];
				    }
				 if(SC_PropertyWare.petRent.matches(".*[a-zA-Z]+.*"))
				    {
				    	SC_PropertyWare.petRent = "Error";
				    }
    		}
    		
    		catch(Exception e1)
		    {
		    	SC_PropertyWare.petRent = "Error";  
		    	e1.printStackTrace();
		    }
	    	System.out.println("Pet rent= "+SC_PropertyWare.petRent.trim());
	    	
	    	//Pet Security Deposit -- Need to find the text of Pet Security Deposit for the format PDF, until then, it is commented
	    	/*
	    	try
	    	{
	    	SC_PropertyWare.petSecurityDeposit = text.substring(text.indexOf(PDFAppConfig_Format2.securityDeposit_Prior)+PDFAppConfig_Format2.securityDeposit_Prior.length()).trim().split(" ")[0];//,text.indexOf(PDFAppConfig_Format2.securityDeposit_After));
		    System.out.println("Security Deposit = "+SC_PropertyWare.securityDeposit.trim());
		    if(RunnerClass.onlyDigits(SC_PropertyWare.petSecurityDeposit)==true)
		    {
		    	SC_PropertyWare.petSecurityDepositFlag = true;
		    	System.out.println("Security Deposit is checked and has value");
		    }
	    	}
	    	catch(Exception e)
	    	{
	    	SC_PropertyWare.securityDeposit = "Error";	
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
	    			try
	    			{
	    				typeSubString = text.substring(text.indexOf(PDFAppConfig_Format2.typeWord_Prior2)+PDFAppConfig_Format2.typeWord_Prior2.length(),text.indexOf(PDFAppConfig_Format2.typeWord_After3));
	    			}
	    			catch(Exception e3)
	    			{
	    		    typeSubString = "";
	    			}
	    		}
	    	}
	    	 String newText = typeSubString.replace("Type:","");
			    SC_PropertyWare.countOfTypeWordInText = ((typeSubString.length() - newText.length())/"Type:".length());
			    System.out.println("Type: occurences = "+SC_PropertyWare.countOfTypeWordInText);
	    	
	    	SC_PropertyWare.petType = new ArrayList();
		    SC_PropertyWare.petBreed = new ArrayList();
		    SC_PropertyWare.petWeight = new ArrayList();
		    for(int i =0;i<SC_PropertyWare.countOfTypeWordInText;i++)
		    {
		    	String type = typeSubString.substring(RunnerClass.nthOccurrence(typeSubString, "Type:", i+1)+PDFAppConfig.AB_pet1Type_Prior.length(),RunnerClass.nthOccurrence(typeSubString, "Breed:", i+1)).trim();
		    	if(type.contains("N/A")||type.contains("n/a"))
		    		break;
		    	System.out.println(type);
		    	SC_PropertyWare.petType.add(type);
		    	int pet1Breedindex1 = RunnerClass.nthOccurrence(typeSubString, "Breed:", i+1)+"Breed:".length()+1;
			    String subString = typeSubString.substring(pet1Breedindex1);
			    //int pet1Breedindex2 = RunnerClass.nthOccurrence(subString,"Name:",i+1);
			   // System.out.println("Index 2 = "+(index2+index1));
			    String breed = subString.split("Name:")[0].trim();//typeSubString.substring(pet1Breedindex1,(pet1Breedindex2+pet1Breedindex1));
			    System.out.println(breed);
			    SC_PropertyWare.petBreed.add(breed);
			    int pet1Weightindex1 = RunnerClass.nthOccurrence(typeSubString, "Weight:", i+1)+"Weight:".length()+1;
			    String pet1WeightSubstring = typeSubString.substring(pet1Weightindex1);
			    //int pet1WeightIndex2 = RunnerClass.nthOccurrence(pet1WeightSubstring,"Age:",i+1);
			   // System.out.println("Index 2 = "+(index2+index1));
			    String weight = pet1WeightSubstring.split("Age:")[0].trim(); //typeSubString.substring(pet1Weightindex1,(pet1WeightIndex2+pet1Weightindex1));
			    System.out.println(weight);
			    SC_PropertyWare.petWeight.add(weight);
		    }
		    
	    	
		    try
		    {
		    	SC_PropertyWare.petOneTimeNonRefundableFee = text.substring(text.indexOf(PDFAppConfig_Format2.petOneTimeNonRefundable_Prior)+PDFAppConfig_Format2.petOneTimeNonRefundable_Prior.length(),text.indexOf(PDFAppConfig_Format2.petOneTimeNonRefundable_After)).trim();
		    	if(SC_PropertyWare.petOneTimeNonRefundableFee.matches(".*[a-zA-Z]+.*"))
			    {
			    	SC_PropertyWare.petOneTimeNonRefundableFee = "Error";
			    }
		    }
		    catch(Exception e)
		    {
		    	try
		    	{
		    		SC_PropertyWare.petOneTimeNonRefundableFee = text.substring(text.indexOf(PDFAppConfig_Format2.petOneTimeNonRefundable_Prior2)+PDFAppConfig_Format2.petOneTimeNonRefundable_Prior2.length()).trim().split(",")[0];
				    //System.out.println("pet one time non refundable = "+SC_PropertyWare.petOneTimeNonRefundableFee.trim());
		    		if(SC_PropertyWare.petOneTimeNonRefundableFee.matches(".*[a-zA-Z]+.*"))
				    {
				    	SC_PropertyWare.petOneTimeNonRefundableFee = "Error";
				    }
		    	}
		    	catch(Exception e2)
		    	{
		    	SC_PropertyWare.petOneTimeNonRefundableFee =  "Error";
		    	e2.printStackTrace();
		    	}
		    }  
		    System.out.println("pet one time non refundable = "+SC_PropertyWare.petOneTimeNonRefundableFee.trim());
		    
		  
		    
		    
	    }
	    
	  //Service Animal Addendum check
	    try
	    {
	    if(text.contains(AppConfig.serviceAnimalText))
	    {
	    	SC_PropertyWare.serviceAnimalFlag = true;
    		System.out.println("Service Animal Addendum is available");
    		
            String typeSubString = text.substring(text.indexOf(PDFAppConfig_Format2.AB_serviceAnimal_typeWord_Prior)+PDFAppConfig_Format2.AB_serviceAnimal_typeWord_Prior.length(),text.indexOf(PDFAppConfig_Format2.AB_serviceAnimal_typeWord_After));
	    	
	    	String newText = typeSubString.replace("Type:","");
		    int  countOftypeWords_ServiceAnimal = ((typeSubString.length() - newText.length())/"Type:".length());
		    System.out.println("Service Animal - Type: occurences = "+countOftypeWords_ServiceAnimal);
		    
		    SC_PropertyWare.serviceAnimalPetType = new ArrayList();
		    SC_PropertyWare.serviceAnimalPetBreed = new ArrayList();
		    SC_PropertyWare.serviceAnimalPetWeight = new ArrayList();
		    for(int i =0;i<countOftypeWords_ServiceAnimal;i++)
		    {
		    	String type = typeSubString.substring(RunnerClass.nthOccurrence(typeSubString, "Type:", i+1)+PDFAppConfig.AB_pet1Type_Prior.length(),RunnerClass.nthOccurrence(typeSubString, "Breed:", i+1)).trim();
		    	if(type.contains("N/A")||type.contains("n/a"))
		    		break;
		    	System.out.println(type);
		    	SC_PropertyWare.serviceAnimalPetType.add(type);
		    	int pet1Breedindex1 = RunnerClass.nthOccurrence(typeSubString, "Breed:", i+1)+"Breed:".length()+1;
			    String subString = typeSubString.substring(pet1Breedindex1);
			    //int pet1Breedindex2 = RunnerClass.nthOccurrence(subString,"Name:",i+1);
			   // System.out.println("Index 2 = "+(index2+index1));
			    String breed = subString.split("Name:")[0].trim();//typeSubString.substring(pet1Breedindex1,(pet1Breedindex2+pet1Breedindex1));
			    System.out.println(breed);
			    SC_PropertyWare.serviceAnimalPetBreed.add(breed);
			    int pet1Weightindex1 = RunnerClass.nthOccurrence(typeSubString, "Weight:", i+1)+"Weight:".length()+1;
			    String pet1WeightSubstring = typeSubString.substring(pet1Weightindex1);
			    //int pet1WeightIndex2 = RunnerClass.nthOccurrence(pet1WeightSubstring,"Age:",i+1);
			   // System.out.println("Index 2 = "+(index2+index1));
			    String weight = pet1WeightSubstring.split("Age:")[0].trim(); //typeSubString.substring(pet1Weightindex1,(pet1WeightIndex2+pet1Weightindex1));
			    System.out.println(weight);
			    SC_PropertyWare.serviceAnimalPetWeight.add(weight);
		    }
    		
    		
	    }
	    }
	    catch(Exception e)
	    {
	    	SC_PropertyWare.serviceAnimalFlag = false;
	    }
	  //Concession Addendum
	    
	    try
	    {
	    	if(text.contains(PDFAppConfig.concessionAddendumText))
	    	{
	    		SC_PropertyWare.concessionAddendumFlag = true;
	    		System.out.println("Concession Addendum is available");
	    	}
	    }
	    catch(Exception e)
	    {}
	    //document.close();
		return true;
	}
	
	public static void lateFeeRule()
	{
		try
	    {
	    	SC_PropertyWare.lateChargeDay = text.substring(text.indexOf(PDFAppConfig_Format2.lateFeeDay_Prior)+PDFAppConfig_Format2.lateFeeDay_Prior.length()).trim().split(" ")[0].trim().replace("[^0-9]", "");
	    	SC_PropertyWare.lateChargeDay = SC_PropertyWare.lateChargeDay.replaceAll("[^\\d]", "");
	    	System.out.println("Late Charge Day = "+SC_PropertyWare.lateChargeDay);//.substring(commensementDate.lastIndexOf(":")+1));
	    }
	    catch(Exception e)
	    {
	    	SC_PropertyWare.lateChargeDay = "Error";
	    	e.printStackTrace();
	    }
	    
	    
	    try
	    {
	    	SC_PropertyWare.lateFeePercentage = text.substring(text.indexOf(PDFAppConfig_Format2.initialLateChargeFee_Prior)+PDFAppConfig_Format2.initialLateChargeFee_Prior.length()).trim().split(" ")[0].trim();
	    	SC_PropertyWare.lateFeePercentage = SC_PropertyWare.lateFeePercentage.replaceAll("[^0-9]", "");
	    	System.out.println("Late Fee Percentage  = "+SC_PropertyWare.lateFeePercentage);//.substring(commensementDate.lastIndexOf(":")+1));
	    }
	    catch(Exception e)
	    {
	    	SC_PropertyWare.lateFeePercentage = "Error";
	    	e.printStackTrace();
	    }
	    //Per Day Fee
	    try
	    {
	    	SC_PropertyWare.flatFeeAmount = text.substring(text.indexOf(PDFAppConfig_Format2.lateChargePerDayFee)+PDFAppConfig_Format2.lateChargePerDayFee.length()).trim().split(" ")[0].trim();
	    	SC_PropertyWare.flatFeeAmount = SC_PropertyWare.flatFeeAmount.replaceAll("[^.0-9]", "");
	    	System.out.println("Per Day Fee  = "+SC_PropertyWare.flatFeeAmount);//.substring(commensementDate.lastIndexOf(":")+1));
	    }
	    catch(Exception e)
	    {
	    	SC_PropertyWare.flatFeeAmount = "Error";
	    	e.printStackTrace();
	    }
	    /*
	    try
	    {
	    if(SC_RunnerClass.pdfFormatType.equalsIgnoreCase("Format1"))
	    {
	    	SC_PropertyWare.additionalLateChargesLimit = text.substring(text.indexOf(PDFAppConfig_Format2.additionaLateCharge_Prior)+PDFAppConfig_Format2.additionaLateCharge_Prior.length()).split(" ")[0].trim();
		    System.out.println("Additional Late Charges  = "+SC_PropertyWare.additionalLateChargesLimit);//.substring(commensementDate.lastIndexOf(":")+1));
	    }
	    }
	    catch(Exception e)
	    {
	    	SC_PropertyWare.additionalLateChargesLimit = "Error";
	    	e.printStackTrace();
	    }
	    */
	}

}
