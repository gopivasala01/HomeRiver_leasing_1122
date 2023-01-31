package Arkansas;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import mainPackage.RunnerClass;

public class AR_ExtractDataFromPDF_Format2
{
	public static boolean petFlag;
	public static String text ="";
	public boolean arizona() throws Exception
	//public static void main(String[] args) throws Exception 
	{
		AR_PropertyWare.petFlag = false;
		//File file = new File("C:\\Gopi\\Projects\\Property ware\\Lease Close Outs\\PDFS\\Tennessee\\Format 2\\Lease_031.22_05.23_1327_Everwood_Dr_Ashland_C_(1).pdf");
		File file = RunnerClass.getLastModified();
		FileInputStream fis = new FileInputStream(file);
		AR_RunnerClass.document = PDDocument.load(fis);
	    text = new PDFTextStripper().getText(AR_RunnerClass.document);
	    text = text.replaceAll(System.lineSeparator(), " ");
	    text = text.trim().replaceAll(" +", " ");
	    System.out.println(text);
	    System.out.println("------------------------------------------------------------------");
	    
	    try
	    {
	    	String commensementRaw = text.substring(text.indexOf(AR_PDFAppConfig_Format2.commensementDate_Prior)+AR_PDFAppConfig_Format2.commensementDate_Prior.length()+1).trim();//,text.indexOf(AR_PDFAppConfig_Format2.commensementDate_After)).trim();
	    	 AR_PropertyWare.commensementDate = commensementRaw.substring(0, commensementRaw.indexOf('(')).trim();
	    	 AR_PropertyWare.commensementDate = AR_PropertyWare.commensementDate.trim().replaceAll(" +", " ");
		    System.out.println("Commensement Date = "+AR_PropertyWare.commensementDate);//.substring(commensementDate.lastIndexOf(":")+1));
	    }
	    catch(Exception e)
	    {
	    	AR_PropertyWare.commensementDate = "Error";
	    	e.printStackTrace();
	    }
	    
	    try
	    {
	    	String expirationDateRaw  = text.substring(text.indexOf(AR_PDFAppConfig_Format2.expirationDate_Prior)+AR_PDFAppConfig_Format2.expirationDate_Prior.length()).trim();//,text.indexOf(AR_PDFAppConfig_Format2.expirationDate_After)).trim();
	    	AR_PropertyWare.expirationDate = expirationDateRaw.substring(0,expirationDateRaw.indexOf('(')).trim();
	    	AR_PropertyWare.expirationDate = AR_PropertyWare.expirationDate.trim().replaceAll(" +", " ");
	    	System.out.println("Expiration Date = "+AR_PropertyWare.expirationDate);//.substring(commensementDate.lastIndexOf(":")+1));
	    }
	    catch(Exception e)
	    {
	    	AR_PropertyWare.expirationDate = "Error";
	    	e.printStackTrace();
	    }
	    
	    try
	    {
	    	AR_PropertyWare.proratedRentDate = text.substring(text.indexOf(AR_PDFAppConfig_Format2.proratedRentDate_Prior)+AR_PDFAppConfig_Format2.proratedRentDate_Prior.length()+1,text.indexOf(AR_PDFAppConfig_Format2.proratedRentDate_After)).trim();
		    System.out.println("prorated Rent Date = "+AR_PropertyWare.proratedRentDate);//.substring(commensementDate.lastIndexOf(":")+1));
	    }
	    catch(Exception e)
	    {
	    	AR_PropertyWare.proratedRentDate = "Error";
	    	e.printStackTrace();
	    }
	    
	    try
	    {
	    	AR_PropertyWare.proratedRent = text.substring(text.indexOf(AR_PDFAppConfig_Format2.proratedRent_Prior)+AR_PDFAppConfig_Format2.proratedRent_Prior.length()).split(" ")[0].trim();
	    	if(AR_PropertyWare.proratedRent.matches(".*[a-zA-Z]+.*"))
		    {
		    	AR_PropertyWare.proratedRent = "Error";
		    }
	    }
	    catch(Exception e)
	    {
	    	AR_PropertyWare.proratedRent = "Error";
	    	e.printStackTrace();
	    }
	    System.out.println("prorated Rent = "+AR_PropertyWare.proratedRent);//.substring(commensementDate.lastIndexOf(":")+1));
	    
	    try
	    {
	    	AR_PropertyWare.monthlyRent = text.substring(text.indexOf(AR_PDFAppConfig_Format2.monthlyRent_Prior)+AR_PDFAppConfig_Format2.monthlyRent_Prior.length()).split(" ")[0].trim();
	    	if(!AR_PropertyWare.monthlyRent.contains("."))
	    		AR_PropertyWare.monthlyRent = text.substring(text.indexOf(AR_PDFAppConfig_Format2.monthlyRent_Prior2)+AR_PDFAppConfig_Format2.monthlyRent_Prior2.length()).split(" ")[0].trim();
	    	if(AR_PropertyWare.monthlyRent.matches(".*[a-zA-Z]+.*"))
		    {
		    	AR_PropertyWare.monthlyRent = "Error";
		    }
	    	if(AR_PropertyWare.monthlyRent.contains("*")||text.contains(AR_PDFAppConfig_Format2.monthlyRentAvailabilityCheck)==true)
	    	{
	    		AR_PropertyWare.incrementRentFlag = true;
	    		AR_PropertyWare.monthlyRent = AR_PropertyWare.monthlyRent.replace("*", "");
	    		System.out.println("Monthly Rent has Asterick *");
	    		
	    		//AR_PropertyWare.increasedRent_amount = text.substring(text.indexOf(". $")+". $".length()).trim().split(" ")[0];
	    		String increasedRent_ProviousRentEndDate = "Per the Landlord, Monthly Rent from "+AR_PropertyWare.commensementDate.trim()+" through ";
	    		 String endDateArray[] = text.substring(text.indexOf(". $")+". $".length()).split(" ");
	    		if(endDateArray[2].trim().length()==4&&RunnerClass.onlyDigits(endDateArray[2]))
	    		 {
	    		  AR_PropertyWare.increasedRent_previousRentEndDate = endDateArray[0]+" "+endDateArray[1]+" "+endDateArray[2];
	    		  System.out.println("Increased Rent - Previous rent end date = "+AR_PropertyWare.increasedRent_previousRentEndDate);
	    		 
	    		  String newRentStartDate[] = text.substring(text.indexOf(AR_PDFAppConfig_Format2.increasedRent_newStartDate_Prior)+AR_PDFAppConfig_Format2.increasedRent_newStartDate_Prior.length()).trim().split(" ");
	    		  AR_PropertyWare.increasedRent_newStartDate = newRentStartDate[0]+" "+newRentStartDate[1]+" "+newRentStartDate[2];
	    		  System.out.println("Increased Rent - New Rent Start date = "+AR_PropertyWare.increasedRent_newStartDate);
	    		  
	    		  String increasedRentRaw = text.substring(text.indexOf(AR_PDFAppConfig_Format2.increasedRent_newStartDate_Prior)+AR_PDFAppConfig_Format2.increasedRent_newStartDate_Prior.length()).trim();
	    		  AR_PropertyWare.increasedRent_amount = increasedRentRaw.substring(increasedRentRaw.indexOf("shall be $")+"shall be $".length()).trim().split(" ")[0];
	    		  System.out.println("Increased Rent - Amount = "+AR_PropertyWare.increasedRent_amount); 
	    		}
	    	}
	    }
	    catch(Exception e)
	    {
	    	AR_PropertyWare.monthlyRent = "Error";
	    	e.printStackTrace();
	    }
	    System.out.println("Monthly Rent = "+AR_PropertyWare.monthlyRent);//.substring(commensementDate.lastIndexOf(":")+1));
	   
	    try
	    {
	    	AR_PropertyWare.adminFee = text.substring(text.indexOf(AR_PDFAppConfig_Format2.adminFee_prior)+AR_PDFAppConfig_Format2.adminFee_prior.length()).split(" ")[0].trim();
	    	if(AR_PropertyWare.adminFee.matches(".*[a-zA-Z]+.*"))
		    {
		    	AR_PropertyWare.adminFee = "Error";
		    }
	    }
	    catch(Exception e)
	    {
	    	AR_PropertyWare.adminFee = "Error";
	    	e.printStackTrace();
	    }
	    System.out.println("Admin Fee = "+AR_PropertyWare.adminFee);//.substring(commensementDate.lastIndexOf(":")+1));
	  //Resident Benefits Package 
	    if(text.contains(AR_PDFAppConfig.residentBenefitsPackageAddendumCheck))
	    {
	    	AR_PropertyWare.residentBenefitsPackageAvailabilityCheck = true;
	    	 try
	 	    {
	 		    AR_PropertyWare.residentBenefitsPackage = text.substring(text.indexOf(AR_PDFAppConfig_Format2.AB1_residentBenefitsPackage_Prior)+AR_PDFAppConfig_Format2.AB1_residentBenefitsPackage_Prior.length()).split(" ")[0].replaceAll("[^0-9a-zA-Z.]", "");
	 		    if(AR_PropertyWare.residentBenefitsPackage.matches(".*[a-zA-Z]+.*"))
	 		    {
	 		    	AR_PropertyWare.residentBenefitsPackage = "Error";
	 		    }
	 	    }
	 	    catch(Exception e)
	 	    {
	 		    AR_PropertyWare.residentBenefitsPackage = "Error";
	 		    e.printStackTrace();
	 	    }
	    	 System.out.println("Resident Benefits Package  = "+AR_PropertyWare.residentBenefitsPackage.trim());
	    	//AR_PDFAppConfig.AB1_residentBenefitsPackage_Prior
	    }
	    else
	    {
		    if(text.contains(AR_PDFAppConfig_Format2.HVACFilterAddendumTextAvailabilityCheck)==true)
		    {
		    	AR_PropertyWare.HVACFilterFlag =true;
		    }
		    else
		    {
		    try
		    {
		    	AR_PropertyWare.airFilterFee = text.substring(text.indexOf(AR_PDFAppConfig_Format2.HVACAirFilter_prior)+AR_PDFAppConfig_Format2.HVACAirFilter_prior.length()).split(" ")[0].trim();
		    	if(AR_PropertyWare.airFilterFee.matches(".*[a-zA-Z]+.*"))
			    {
			    	AR_PropertyWare.airFilterFee = "Error";
			    }
		    }
		    catch(Exception e)
		    {
		    	AR_PropertyWare.airFilterFee = "Error";
		    	e.printStackTrace();
		    }
		    }
		    System.out.println("HVAC Air Filter Fee = "+AR_PropertyWare.airFilterFee);//.substring(commensementDate.lastIndexOf(":")+1));
	    }
	    try
	    {
	    	AR_PropertyWare.occupants = text.substring(text.indexOf(AR_PDFAppConfig_Format2.occupants_Prior)+AR_PDFAppConfig_Format2.occupants_Prior.length(),text.indexOf(AR_PDFAppConfig_Format2.occupants_After)).trim();
		    System.out.println("Occupants = "+AR_PropertyWare.occupants);//.substring(commensementDate.lastIndexOf(":")+1));
	    }
	    catch(Exception e)
	    {
	    	AR_PropertyWare.occupants = "Error";
	    	e.printStackTrace();
	    }
	    
	    //Late Fee Rule
	    AR_ExtractDataFromPDF_Format2.lateFeeRule();
	    
	    
	  //Prepayment Charge
	    try
	    {
		if(AR_PropertyWare.portfolioType.contains("MCH"))
		{
		try
		{
		AR_PropertyWare.prepaymentCharge =String.valueOf(Double.parseDouble(AR_PropertyWare.monthlyRent.replace(",", "")) - Double.parseDouble(AR_PropertyWare.proratedRent.replace(",", ""))); 
		}
		catch(Exception e)
		{
			AR_PropertyWare.prepaymentCharge ="Error";
		}
		System.out.println("Prepayment Charge = "+AR_PropertyWare.prepaymentCharge);
		}
	    }
	    catch(Exception e) {}
	    //Early Termination
		try
	    {
	    	String[] earlyTerminationRaw = text.substring(text.indexOf(AR_PDFAppConfig_Format2.earlyTermination_Prior)+AR_PDFAppConfig_Format2.earlyTermination_Prior.length()).split(" ");
	    	
		    AR_PropertyWare.earlyTermination = earlyTerminationRaw[0]+earlyTerminationRaw[1]; //text.substring(text.indexOf(AR_PDFAppConfig.AB_earlyTerminationFee_Prior)+AR_PDFAppConfig.AB_earlyTerminationFee_Prior.length(),text.indexOf(AR_PDFAppConfig.AB_earlyTerminationFee_After));
	    }
	    catch(Exception e)
	    {
	    	AR_PropertyWare.earlyTermination = "Error";	
	    	e.printStackTrace();
	    }
		System.out.println("Early Termination = "+AR_PropertyWare.earlyTermination);
	    // Checking Pet Addendum is available or not
	    
	    if(text.contains(AR_PDFAppConfig_Format2.petAgreementAvailabilityCheck)==true)
	    	petFlag =  true;
	    else if(petFlag = text.contains(AR_PDFAppConfig_Format2.petAgreementAvailabilityCheck2)==true)
	    	petFlag =  true;
	    else petFlag =  false;
	    
	    System.out.println("Pet Addendum Available = "+petFlag);
	    if(petFlag ==true)
	    {
	    	AR_PropertyWare.petFlag = true;
			    	try
			    	{
			    		 String proratedPetRaw = "Prorated Pet Rent: On or before "+AR_PropertyWare.commensementDate.trim()+" Tenant will pay Landlord $";
				    		AR_PropertyWare.proratedPetRent = text.substring(text.indexOf(proratedPetRaw)+proratedPetRaw.length()).trim().split(" ")[0].trim();//.replaceAll("[a-ZA-Z,]", "");
				    		if(AR_PropertyWare.proratedPetRent.matches(".*[a-zA-Z]+.*")||AR_PropertyWare.proratedPetRent.trim().equals("1."))
						    {
						    	AR_PropertyWare.proratedPetRent = "Error";
						    }
				    		//AR_PropertyWare.proratedPetRent = proratedPetRentRaw.substring(proratedPetRentRaw.indexOf("Tenant will pay Landlord $")+"Tenant will pay Landlord $".length());//,proratedPetRentRaw.indexOf(AppConfig.AR_proratedPetRent_After));
			        }
				    catch(Exception e)
				    {
				    AR_PropertyWare.proratedPetRent = "Error";	
				    e.printStackTrace();
				    }
	    	System.out.println("Prorated Pet rent= "+AR_PropertyWare.proratedPetRent.trim());
	    	try
    		{
    			AR_PropertyWare.petRent = text.substring(text.indexOf(AR_PDFAppConfig_Format2.petRent_Prior)+AR_PDFAppConfig_Format2.petRent_Prior.length()).split(" ")[0].trim();
				 //System.out.println("Pet rent = "+AR_PropertyWare.petRent.trim());
				 if(RunnerClass.onlyDigits(AR_PropertyWare.petRent)==false)
				    {
				    	 AR_PropertyWare.petRent = text.substring(text.indexOf(AR_PDFAppConfig_Format2.petRent_Prior2)+AR_PDFAppConfig_Format2.petRent_Prior2.length()).trim().split(" ")[0];
				    }
				 if(AR_PropertyWare.petRent.matches(".*[a-zA-Z]+.*"))
				    {
				    	AR_PropertyWare.petRent = "Error";
				    }
    		}
    		
    		catch(Exception e1)
		    {
		    	AR_PropertyWare.petRent = "Error";  
		    	e1.printStackTrace();
		    }
	    	System.out.println("Pet rent= "+AR_PropertyWare.petRent.trim());
	    	
	    	//Pet Security Deposit -- Need to find the text of Pet Security Deposit for the format PDF, until then, it is commented
	    	/*
	    	try
	    	{
	    	AR_PropertyWare.petSecurityDeposit = text.substring(text.indexOf(AR_PDFAppConfig_Format2.securityDeposit_Prior)+AR_PDFAppConfig_Format2.securityDeposit_Prior.length()).trim().split(" ")[0];//,text.indexOf(AR_PDFAppConfig_Format2.securityDeposit_After));
		    System.out.println("Security Deposit = "+AR_PropertyWare.securityDeposit.trim());
		    if(RunnerClass.onlyDigits(AR_PropertyWare.petSecurityDeposit)==true)
		    {
		    	AR_PropertyWare.petSecurityDepositFlag = true;
		    	System.out.println("Security Deposit is checked and has value");
		    }
	    	}
	    	catch(Exception e)
	    	{
	    	AR_PropertyWare.securityDeposit = "Error";	
	    	e.printStackTrace();
	    	}
	    	*/
	    	String typeSubString = "";
	    	try
	    	{
	    	typeSubString = text.substring(text.indexOf(AR_PDFAppConfig_Format2.typeWord_Prior)+AR_PDFAppConfig_Format2.typeWord_Prior.length(),text.indexOf(AR_PDFAppConfig_Format2.typeWord_After));
	    	}
	    	catch(Exception e)
	    	{
	    		try
	    		{
	    		typeSubString = text.substring(text.indexOf(AR_PDFAppConfig_Format2.typeWord_Prior)+AR_PDFAppConfig_Format2.typeWord_Prior.length(),text.indexOf(AR_PDFAppConfig_Format2.typeWord_After2));
	    		}
	    		catch(Exception e2)
	    		{
	    			try
	    			{
	    				typeSubString = text.substring(text.indexOf(AR_PDFAppConfig_Format2.typeWord_Prior2)+AR_PDFAppConfig_Format2.typeWord_Prior2.length(),text.indexOf(AR_PDFAppConfig_Format2.typeWord_After3));
	    			}
	    			catch(Exception e3)
	    			{
	    		    typeSubString = "";
	    			}
	    		}
	    	}
	    	 String newText = typeSubString.replace("Type:","");
			    AR_PropertyWare.countOfTypeWordInText = ((typeSubString.length() - newText.length())/"Type:".length());
			    System.out.println("Type: occurences = "+AR_PropertyWare.countOfTypeWordInText);
	    	
	    	AR_PropertyWare.petType = new ArrayList();
		    AR_PropertyWare.petBreed = new ArrayList();
		    AR_PropertyWare.petWeight = new ArrayList();
		    for(int i =0;i<AR_PropertyWare.countOfTypeWordInText;i++)
		    {
		    	String type = typeSubString.substring(RunnerClass.nthOccurrence(typeSubString, "Type:", i+1)+AR_PDFAppConfig.AB_pet1Type_Prior.length(),RunnerClass.nthOccurrence(typeSubString, "Breed:", i+1)).trim();
		    	if(type.contains("N/A")||type.contains("n/a"))
		    		break;
		    	System.out.println(type);
		    	AR_PropertyWare.petType.add(type);
		    	int pet1Breedindex1 = RunnerClass.nthOccurrence(typeSubString, "Breed:", i+1)+"Breed:".length()+1;
			    String subString = typeSubString.substring(pet1Breedindex1);
			    //int pet1Breedindex2 = RunnerClass.nthOccurrence(subString,"Name:",i+1);
			   // System.out.println("Index 2 = "+(index2+index1));
			    String breed = subString.split("Name:")[0].trim();//typeSubString.substring(pet1Breedindex1,(pet1Breedindex2+pet1Breedindex1));
			    System.out.println(breed);
			    AR_PropertyWare.petBreed.add(breed);
			    int pet1Weightindex1 = RunnerClass.nthOccurrence(typeSubString, "Weight:", i+1)+"Weight:".length()+1;
			    String pet1WeightSubstring = typeSubString.substring(pet1Weightindex1);
			    //int pet1WeightIndex2 = RunnerClass.nthOccurrence(pet1WeightSubstring,"Age:",i+1);
			   // System.out.println("Index 2 = "+(index2+index1));
			    String weight = pet1WeightSubstring.split("Age:")[0].trim(); //typeSubString.substring(pet1Weightindex1,(pet1WeightIndex2+pet1Weightindex1));
			    System.out.println(weight);
			    AR_PropertyWare.petWeight.add(weight);
		    }
		    
	    	
		    try
		    {
		    	AR_PropertyWare.petOneTimeNonRefundableFee = text.substring(text.indexOf(AR_PDFAppConfig_Format2.petOneTimeNonRefundable_Prior)+AR_PDFAppConfig_Format2.petOneTimeNonRefundable_Prior.length(),text.indexOf(AR_PDFAppConfig_Format2.petOneTimeNonRefundable_After)).trim();
		    	if(AR_PropertyWare.petOneTimeNonRefundableFee.matches(".*[a-zA-Z]+.*"))
			    {
			    	AR_PropertyWare.petOneTimeNonRefundableFee = "Error";
			    }
		    }
		    catch(Exception e)
		    {
		    	try
		    	{
		    		AR_PropertyWare.petOneTimeNonRefundableFee = text.substring(text.indexOf(AR_PDFAppConfig_Format2.petOneTimeNonRefundable_Prior2)+AR_PDFAppConfig_Format2.petOneTimeNonRefundable_Prior2.length()).trim().split(",")[0];
				    //System.out.println("pet one time non refundable = "+AR_PropertyWare.petOneTimeNonRefundableFee.trim());
		    		if(AR_PropertyWare.petOneTimeNonRefundableFee.matches(".*[a-zA-Z]+.*"))
				    {
				    	AR_PropertyWare.petOneTimeNonRefundableFee = "Error";
				    }
		    	}
		    	catch(Exception e2)
		    	{
		    	AR_PropertyWare.petOneTimeNonRefundableFee =  "Error";
		    	e2.printStackTrace();
		    	}
		    }  
		    System.out.println("pet one time non refundable = "+AR_PropertyWare.petOneTimeNonRefundableFee.trim());
		    
		  
		    
		    
	    }
	    
	  //Service Animal Addendum check
	    try
	    {
	    if(text.contains(AR_AppConfig.serviceAnimalText))
	    {
	    	AR_PropertyWare.serviceAnimalFlag = true;
    		System.out.println("Service Animal Addendum is available");
    		
            String typeSubString = text.substring(text.indexOf(AR_PDFAppConfig_Format2.AB_serviceAnimal_typeWord_Prior)+AR_PDFAppConfig_Format2.AB_serviceAnimal_typeWord_Prior.length(),text.indexOf(AR_PDFAppConfig_Format2.AB_serviceAnimal_typeWord_After));
	    	
	    	String newText = typeSubString.replace("Type:","");
		    int  countOftypeWords_ServiceAnimal = ((typeSubString.length() - newText.length())/"Type:".length());
		    System.out.println("Service Animal - Type: occurences = "+countOftypeWords_ServiceAnimal);
		    
		    AR_PropertyWare.serviceAnimalPetType = new ArrayList();
		    AR_PropertyWare.serviceAnimalPetBreed = new ArrayList();
		    AR_PropertyWare.serviceAnimalPetWeight = new ArrayList();
		    for(int i =0;i<countOftypeWords_ServiceAnimal;i++)
		    {
		    	String type = typeSubString.substring(RunnerClass.nthOccurrence(typeSubString, "Type:", i+1)+AR_PDFAppConfig.AB_pet1Type_Prior.length(),RunnerClass.nthOccurrence(typeSubString, "Breed:", i+1)).trim();
		    	if(type.contains("N/A")||type.contains("n/a"))
		    		break;
		    	System.out.println(type);
		    	AR_PropertyWare.serviceAnimalPetType.add(type);
		    	int pet1Breedindex1 = RunnerClass.nthOccurrence(typeSubString, "Breed:", i+1)+"Breed:".length()+1;
			    String subString = typeSubString.substring(pet1Breedindex1);
			    //int pet1Breedindex2 = RunnerClass.nthOccurrence(subString,"Name:",i+1);
			   // System.out.println("Index 2 = "+(index2+index1));
			    String breed = subString.split("Name:")[0].trim();//typeSubString.substring(pet1Breedindex1,(pet1Breedindex2+pet1Breedindex1));
			    System.out.println(breed);
			    AR_PropertyWare.serviceAnimalPetBreed.add(breed);
			    int pet1Weightindex1 = RunnerClass.nthOccurrence(typeSubString, "Weight:", i+1)+"Weight:".length()+1;
			    String pet1WeightSubstring = typeSubString.substring(pet1Weightindex1);
			    //int pet1WeightIndex2 = RunnerClass.nthOccurrence(pet1WeightSubstring,"Age:",i+1);
			   // System.out.println("Index 2 = "+(index2+index1));
			    String weight = pet1WeightSubstring.split("Age:")[0].trim(); //typeSubString.substring(pet1Weightindex1,(pet1WeightIndex2+pet1Weightindex1));
			    System.out.println(weight);
			    AR_PropertyWare.serviceAnimalPetWeight.add(weight);
		    }
    		
    		
	    }
	    }
	    catch(Exception e)
	    {
	    	AR_PropertyWare.serviceAnimalFlag = false;
	    }
	  //Concession Addendum
	    
	    try
	    {
	    	if(text.contains(AR_PDFAppConfig.concessionAddendumText))
	    	{
	    		AR_PropertyWare.concessionAddendumFlag = true;
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
		//Late Charge Day
		try
	    {
	    	AR_PropertyWare.lateChargeDay = text.substring(text.indexOf(AR_PDFAppConfig_Format2.lateFeeDay_Prior)+AR_PDFAppConfig_Format2.lateFeeDay_Prior.length()).trim().split(" ")[0].trim().replace("[^0-9]", "");
	    	AR_PropertyWare.lateChargeDay = AR_PropertyWare.lateChargeDay.replaceAll("[^\\d]", "");
	    	System.out.println("Late Charge Day = "+AR_PropertyWare.lateChargeDay);//.substring(commensementDate.lastIndexOf(":")+1));
	    }
	    catch(Exception e)
	    {
	    	AR_PropertyWare.lateChargeDay = "Error";
	    	e.printStackTrace();
	    }
	    
	    //Late Charge Fee
	    try
	    {
	    	AR_PropertyWare.lateChargeFee = text.substring(text.indexOf(AR_PDFAppConfig_Format2.lateChargePerDayFee)+AR_PDFAppConfig_Format2.lateChargePerDayFee.length()).trim().split(" ")[0].trim();
	    	//AR_PropertyWare.lateChargeFee = AR_PropertyWare.lateChargeFee.replaceAll("[^.0-9]", "");
	    }
	    catch(Exception e)
	    {
	    	AR_PropertyWare.lateChargeFee = "Error";
	    	e.printStackTrace();
	    }
	    System.out.println("Late Fee = "+AR_PropertyWare.lateChargeFee);//.substring(commensementDate.lastIndexOf(":")+1));
	    /*
	    //Per Day Fee
	    try
	    {
	    	AR_PropertyWare.additionalLateCharges = text.substring(text.indexOf(AR_PDFAppConfig_Format2.additionaLateCharge_Prior)+AR_PDFAppConfig_Format2.additionaLateCharge_Prior.length()).trim().split(" ")[0].trim();
	    	AR_PropertyWare.additionalLateCharges = AR_PropertyWare.additionalLateCharges.replaceAll("[^.0-9]", "");
	    }
	    catch(Exception e)
	    {
	    	AR_PropertyWare.additionalLateCharges = "Error";
	    	e.printStackTrace();
	    }
	    System.out.println("Additional Late Charges  = "+AR_PropertyWare.additionalLateCharges);//.substring(commensementDate.lastIndexOf(":")+1));
	    
	    try
	    {
	    if(AR_RunnerClass.pdfFormatType.equalsIgnoreCase("Format1"))
	    {
	    	AR_PropertyWare.additionalLateChargesLimit = text.substring(text.indexOf(AR_PDFAppConfig_Format2.additionaLateCharge_Prior)+AR_PDFAppConfig_Format2.additionaLateCharge_Prior.length()).split(" ")[0].trim();
		    System.out.println("Additional Late Charges  = "+AR_PropertyWare.additionalLateChargesLimit);//.substring(commensementDate.lastIndexOf(":")+1));
	    }
	    }
	    catch(Exception e)
	    {
	    	AR_PropertyWare.additionalLateChargesLimit = "Error";
	    	e.printStackTrace();
	    }
	    */
	}
	


}
