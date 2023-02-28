package Georgia;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import mainPackage.RunnerClass;

public class GA_ExtractDataFromPDF_Format2 {
	public static boolean petFlag;
	public static String text ="";
	public boolean arizona() throws Exception
	//public static void main(String[] args) throws Exception 
	{
		GA_PropertyWare.petFlag = false;
		//File file = new File("C:\\Gopi\\Projects\\Property ware\\Lease Close Outs\\PDFS\\VILL3640\\Lease_323_924_3640_Village_Pkwy_GA_Goodson.pdf");
		File file = RunnerClass.getLastModified();
		FileInputStream fis = new FileInputStream(file);
		GA_RunnerClass.document = PDDocument.load(fis);
	    text = new PDFTextStripper().getText(GA_RunnerClass.document);
	    text = text.replaceAll(System.lineSeparator(), " ");
	    text = text.trim().replaceAll(" +", " ");
	    System.out.println(text);
	    System.out.println("------------------------------------------------------------------");
	    
	    try
	    {
	    	String commensementRaw = text.substring(text.indexOf(PDFAppConfig_Format2.commensementDate_Prior)+PDFAppConfig_Format2.commensementDate_Prior.length()+1).trim();//,text.indexOf(PDFAppConfig_Format2.commensementDate_After)).trim();
	    	 GA_PropertyWare.commensementDate = commensementRaw.substring(0, commensementRaw.indexOf('(')).trim();
	    	 GA_PropertyWare.commensementDate = GA_PropertyWare.commensementDate.trim().replaceAll(" +", " ");
		    System.out.println("Commensement Date = "+GA_PropertyWare.commensementDate);//.substring(commensementDate.lastIndexOf(":")+1));
	    }
	    catch(Exception e)
	    {
	    	GA_PropertyWare.commensementDate = "Error";
	    	e.printStackTrace();
	    }
	    
	    try
	    {
	    	String expirationDateRaw  = text.substring(text.indexOf(PDFAppConfig_Format2.expirationDate_Prior)+PDFAppConfig_Format2.expirationDate_Prior.length()).trim();//,text.indexOf(PDFAppConfig_Format2.expirationDate_After)).trim();
	    	GA_PropertyWare.expirationDate = expirationDateRaw.substring(0,expirationDateRaw.indexOf('(')).trim();
	    	GA_PropertyWare.expirationDate = GA_PropertyWare.expirationDate.trim().replaceAll(" +", " ");
	    	System.out.println("Expiration Date = "+GA_PropertyWare.expirationDate);//.substring(commensementDate.lastIndexOf(":")+1));
	    }
	    catch(Exception e)
	    {
	    	GA_PropertyWare.expirationDate = "Error";
	    	e.printStackTrace();
	    }
	    
	    try
	    {
	    	GA_PropertyWare.proratedRentDate = text.substring(text.indexOf(PDFAppConfig_Format2.proratedRentDate_Prior)+PDFAppConfig_Format2.proratedRentDate_Prior.length()+1,text.indexOf(PDFAppConfig_Format2.proratedRentDate_After)).trim();
		    System.out.println("prorated Rent Date = "+GA_PropertyWare.proratedRentDate);//.substring(commensementDate.lastIndexOf(":")+1));
	    }
	    catch(Exception e)
	    {
	    	GA_PropertyWare.proratedRentDate = "Error";
	    	e.printStackTrace();
	    }
	    
	    try
	    {
	    	GA_PropertyWare.proratedRent = text.substring(text.indexOf(PDFAppConfig_Format2.proratedRent_Prior)+PDFAppConfig_Format2.proratedRent_Prior.length()).split(" ")[0].trim();
	    	if(GA_PropertyWare.proratedRent.matches(".*[a-zA-Z]+.*"))
		    {
		    	GA_PropertyWare.proratedRent = "Error";
		    }
	    }
	    catch(Exception e)
	    {
	    	GA_PropertyWare.proratedRent = "Error";
	    	e.printStackTrace();
	    }
	    System.out.println("prorated Rent = "+GA_PropertyWare.proratedRent);//.substring(commensementDate.lastIndexOf(":")+1));
	    
	    try
	    {
	    	GA_PropertyWare.monthlyRent = text.substring(text.indexOf(PDFAppConfig_Format2.monthlyRent_Prior)+PDFAppConfig_Format2.monthlyRent_Prior.length()).split(" ")[0].trim();
	    	if(!GA_PropertyWare.monthlyRent.contains("."))
	    		GA_PropertyWare.monthlyRent = text.substring(text.indexOf(PDFAppConfig_Format2.monthlyRent_Prior2)+PDFAppConfig_Format2.monthlyRent_Prior2.length()).split(" ")[0].trim();
	    	if(GA_PropertyWare.monthlyRent.matches(".*[a-zA-Z]+.*"))
		    {
		    	GA_PropertyWare.monthlyRent = "Error";
		    }
	    	if(GA_PropertyWare.monthlyRent.contains("*")||text.contains(PDFAppConfig_Format2.monthlyRentAvailabilityCheck)==true)
	    	{
	    		GA_PropertyWare.incrementRentFlag = true;
	    		GA_PropertyWare.monthlyRent = GA_PropertyWare.monthlyRent.replace("*", "");
	    		System.out.println("Monthly Rent has Asterick *");
	    		String increasedRent_ProviousRentEndDate = "Per the Landlord, Monthly Rent from "+GA_PropertyWare.commensementDate.trim()+" through ";
	    		 String endDateArray[] = text.substring(text.indexOf(increasedRent_ProviousRentEndDate)+increasedRent_ProviousRentEndDate.length()).split(" ");
	    		 if(endDateArray[2].trim().length()==4)//&&RunnerClass.onlyDigits(endDateArray[2]))
	    		 {
	    		  GA_PropertyWare.increasedRent_previousRentEndDate = endDateArray[0]+" "+endDateArray[1]+" "+endDateArray[2];
	    		  System.out.println("Increased Rent - Previous rent end date = "+GA_PropertyWare.increasedRent_previousRentEndDate);
	    		 
	    		  String newRentStartDate[] = text.substring(text.indexOf(PDFAppConfig_Format2.increasedRent_newStartDate_Prior)+PDFAppConfig_Format2.increasedRent_newStartDate_Prior.length()).trim().split(" ");
	    		  GA_PropertyWare.increasedRent_newStartDate = newRentStartDate[0]+" "+newRentStartDate[1]+" "+newRentStartDate[2];
	    		  System.out.println("Increased Rent - New Rent Start date = "+GA_PropertyWare.increasedRent_newStartDate);
	    		  
	    		  String increasedRentRaw = text.substring(text.indexOf(PDFAppConfig_Format2.increasedRent_newStartDate_Prior)+PDFAppConfig_Format2.increasedRent_newStartDate_Prior.length()).trim();
	    		  GA_PropertyWare.increasedRent_amount = increasedRentRaw.substring(increasedRentRaw.indexOf("shall be")+"shall be".length()).trim().split(" ")[0];
	    		  GA_PropertyWare.increasedRent_amount = GA_PropertyWare.increasedRent_amount.trim().replace("$", "");
	    		  System.out.println("Increased Rent - Amount = "+GA_PropertyWare.increasedRent_amount); 
	    		 }
	    		 else 
	       		 {
	       			 String adding0toMonth = "0"+GA_PropertyWare.commensementDate.trim().split(" ")[1];
	       			 String commeseDate = GA_PropertyWare.commensementDate.trim().replace(GA_PropertyWare.commensementDate.trim().split(" ")[1], adding0toMonth);
	       			 increasedRent_ProviousRentEndDate = "Per the Landlord, Monthly Rent from "+commeseDate+" through ";
	    	    		 String endDateArray2[] = text.substring(text.indexOf(increasedRent_ProviousRentEndDate)+increasedRent_ProviousRentEndDate.length()).split(" ");
	    	    		 if(endDateArray2[2].trim().length()==4)//&&RunnerClass.onlyDigits(endDateArray[2]))
	    	    		 {
	    	    		  GA_PropertyWare.increasedRent_previousRentEndDate = endDateArray2[0]+" "+endDateArray2[1]+" "+endDateArray2[2];
	    	    		  System.out.println("Increased Rent - Previous rent end date = "+GA_PropertyWare.increasedRent_previousRentEndDate);
	    	    		 
	    	    		  String newRentStartDate[] = text.substring(text.indexOf(PDFAppConfig_Format2.increasedRent_newStartDate_Prior)+PDFAppConfig_Format2.increasedRent_newStartDate_Prior.length()).trim().split(" ");
	    	    		  GA_PropertyWare.increasedRent_newStartDate = newRentStartDate[0]+" "+newRentStartDate[1]+" "+newRentStartDate[2];
	    	    		  System.out.println("Increased Rent - New Rent Start date = "+GA_PropertyWare.increasedRent_newStartDate);
	    	    		  
	    	    		  String increasedRentRaw = text.substring(text.indexOf(PDFAppConfig_Format2.increasedRent_newStartDate_Prior)+PDFAppConfig_Format2.increasedRent_newStartDate_Prior.length()).trim();
	    	    		  GA_PropertyWare.increasedRent_amount = increasedRentRaw.substring(increasedRentRaw.indexOf("shall be $")+"shall be $".length()).trim().split(" ")[0];
	    	    		  System.out.println("Increased Rent - Amount = "+GA_PropertyWare.increasedRent_amount); 
	    	    		 }
	       		 }
	    	}
	    }
	    catch(Exception e)
	    {
	    	GA_PropertyWare.monthlyRent = "Error";
	    	e.printStackTrace();
	    }
	    System.out.println("Monthly Rent = "+GA_PropertyWare.monthlyRent);//.substring(commensementDate.lastIndexOf(":")+1));
	   
	    try
	    {
	    	GA_PropertyWare.adminFee = text.substring(text.indexOf(PDFAppConfig_Format2.adminFee_prior)+PDFAppConfig_Format2.adminFee_prior.length()).split(" ")[0].trim();
	    	if(GA_PropertyWare.adminFee.matches(".*[a-zA-Z]+.*"))
		    {
		    	GA_PropertyWare.adminFee = "Error";
		    }
	    }
	    catch(Exception e)
	    {
	    	GA_PropertyWare.adminFee = "Error";
	    	e.printStackTrace();
	    }
	    System.out.println("Admin Fee = "+GA_PropertyWare.adminFee);//.substring(commensementDate.lastIndexOf(":")+1));
	  //Resident Benefits Package 
	    if(text.contains(PDFAppConfig.residentBenefitsPackageAddendumCheck))
	    {
	    	GA_PropertyWare.residentBenefitsPackageAvailabilityCheck = true;
	    	 try
	 	    {
	 		    GA_PropertyWare.residentBenefitsPackage = text.substring(text.indexOf(PDFAppConfig_Format2.AB1_residentBenefitsPackage_Prior)+PDFAppConfig_Format2.AB1_residentBenefitsPackage_Prior.length()).split(" ")[0].replaceAll("[^0-9a-zA-Z.]", "");
	 		    if(GA_PropertyWare.residentBenefitsPackage.matches(".*[a-zA-Z]+.*"))
	 		    {
	 		    	GA_PropertyWare.residentBenefitsPackage = "Error";
	 		    }
	 	    }
	 	    catch(Exception e)
	 	    {
	 		    GA_PropertyWare.residentBenefitsPackage = "Error";
	 		    e.printStackTrace();
	 	    }
	    	 System.out.println("Resident Benefits Package  = "+GA_PropertyWare.residentBenefitsPackage.trim());
	    	//PDFAppConfig.AB1_residentBenefitsPackage_Prior
	    }
	    else
	    {
		    if(text.contains(PDFAppConfig_Format2.HVACFilterAddendumTextAvailabilityCheck)==true)
		    {
		    	GA_PropertyWare.HVACFilterFlag =true;
		    }
		    else
		    {
		    try
		    {
		    	GA_PropertyWare.airFilterFee = text.substring(text.indexOf(PDFAppConfig_Format2.HVACAirFilter_prior)+PDFAppConfig_Format2.HVACAirFilter_prior.length()).split(" ")[0].trim();
		    	if(GA_PropertyWare.airFilterFee.matches(".*[a-zA-Z]+.*"))
			    {
			    	GA_PropertyWare.airFilterFee = "Error";
			    }
		    }
		    catch(Exception e)
		    {
		    	GA_PropertyWare.airFilterFee = "Error";
		    	e.printStackTrace();
		    }
		    }
		    System.out.println("HVAC Air Filter Fee = "+GA_PropertyWare.airFilterFee);//.substring(commensementDate.lastIndexOf(":")+1));
	    }
	    try
	    {
	    	GA_PropertyWare.occupants = text.substring(text.indexOf(PDFAppConfig_Format2.occupants_Prior)+PDFAppConfig_Format2.occupants_Prior.length(),text.indexOf(PDFAppConfig_Format2.occupants_After)).trim();
		    System.out.println("Occupants = "+GA_PropertyWare.occupants);//.substring(commensementDate.lastIndexOf(":")+1));
	    }
	    catch(Exception e)
	    {
	    	GA_PropertyWare.occupants = "Error";
	    	e.printStackTrace();
	    }
	    
	    //Late Fee Rule
	    GA_ExtractDataFromPDF_Format2.lateFeeRule();
	    
	    
	  //Prepayment Charge
	    try
	    {
		if(GA_PropertyWare.portfolioType.contains("MCH"))
		{
		try
		{
		GA_PropertyWare.prepaymentCharge =String.valueOf(Double.parseDouble(GA_PropertyWare.monthlyRent.replace(",", "")) - Double.parseDouble(GA_PropertyWare.proratedRent.replace(",", ""))); 
		}
		catch(Exception e)
		{
			GA_PropertyWare.prepaymentCharge ="Error";
		}
		System.out.println("Prepayment Charge = "+GA_PropertyWare.prepaymentCharge);
		}
	    }
	    catch(Exception e) {}
	    //Early Termination
		try
	    {
	    	String[] earlyTerminationRaw = text.substring(text.indexOf(PDFAppConfig_Format2.earlyTermination_Prior)+PDFAppConfig_Format2.earlyTermination_Prior.length()).split(" ");
	    	
		    GA_PropertyWare.earlyTermination = earlyTerminationRaw[0]+earlyTerminationRaw[1]; //text.substring(text.indexOf(PDFAppConfig.AB_earlyTerminationFee_Prior)+PDFAppConfig.AB_earlyTerminationFee_Prior.length(),text.indexOf(PDFAppConfig.AB_earlyTerminationFee_After));
	    }
	    catch(Exception e)
	    {
	    	GA_PropertyWare.earlyTermination = "Error";	
	    	e.printStackTrace();
	    }
		System.out.println("Early Termination = "+GA_PropertyWare.earlyTermination);
	    // Checking Pet Addendum is available or not
	    
	    if(text.contains(PDFAppConfig_Format2.petAgreementAvailabilityCheck)==true)
	    	petFlag =  true;
	    else if(petFlag = text.contains(PDFAppConfig_Format2.petAgreementAvailabilityCheck2)==true)
	    	petFlag =  true;
	    else petFlag =  false;
	    
	    System.out.println("Pet Addendum Available = "+petFlag);
	    if(petFlag ==true)
	    {
	    	GA_PropertyWare.petFlag = true;
			    	try
			    	{
			    		 String proratedPetRaw = "Prorated Pet Rent: On or before "+GA_PropertyWare.commensementDate.trim()+" Tenant will pay Landlord $";
				    		GA_PropertyWare.proratedPetRent = text.substring(text.indexOf(proratedPetRaw)+proratedPetRaw.length()).trim().split(" ")[0].trim();//.replaceAll("[a-ZA-Z,]", "");
				    		if(GA_PropertyWare.proratedPetRent.matches(".*[a-zA-Z]+.*")||GA_PropertyWare.proratedPetRent.trim().equals("1."))
						    {
						    	GA_PropertyWare.proratedPetRent = "Error";
						    }
				    		//AR_PropertyWare.proratedPetRent = proratedPetRentRaw.substring(proratedPetRentRaw.indexOf("Tenant will pay Landlord $")+"Tenant will pay Landlord $".length());//,proratedPetRentRaw.indexOf(AppConfig.AR_proratedPetRent_After));
			        }
				    catch(Exception e)
				    {
				    GA_PropertyWare.proratedPetRent = "Error";	
				    e.printStackTrace();
				    }
	    	System.out.println("Prorated Pet rent= "+GA_PropertyWare.proratedPetRent.trim());
	    	try
    		{
    			GA_PropertyWare.petRent = text.substring(text.indexOf(PDFAppConfig_Format2.petRent_Prior)+PDFAppConfig_Format2.petRent_Prior.length()).split(" ")[0].trim();
				 //System.out.println("Pet rent = "+GA_PropertyWare.petRent.trim());
				 if(RunnerClass.onlyDigits(GA_PropertyWare.petRent)==false)
				    {
				    	 GA_PropertyWare.petRent = text.substring(text.indexOf(PDFAppConfig_Format2.petRent_Prior2)+PDFAppConfig_Format2.petRent_Prior2.length()).trim().split(" ")[0];
				    }
				 if(GA_PropertyWare.petRent.matches(".*[a-zA-Z]+.*"))
				    {
				    	GA_PropertyWare.petRent = "Error";
				    }
    		}
    		
    		catch(Exception e1)
		    {
		    	GA_PropertyWare.petRent = "Error";  
		    	e1.printStackTrace();
		    }
	    	System.out.println("Pet rent= "+GA_PropertyWare.petRent.trim());
	    	
	    	//Pet Security Deposit -- Need to find the text of Pet Security Deposit for the format PDF, until then, it is commented
	    	/*
	    	try
	    	{
	    	GA_PropertyWare.petSecurityDeposit = text.substring(text.indexOf(PDFAppConfig_Format2.securityDeposit_Prior)+PDFAppConfig_Format2.securityDeposit_Prior.length()).trim().split(" ")[0];//,text.indexOf(PDFAppConfig_Format2.securityDeposit_After));
		    System.out.println("Security Deposit = "+GA_PropertyWare.securityDeposit.trim());
		    if(RunnerClass.onlyDigits(GA_PropertyWare.petSecurityDeposit)==true)
		    {
		    	GA_PropertyWare.petSecurityDepositFlag = true;
		    	System.out.println("Security Deposit is checked and has value");
		    }
	    	}
	    	catch(Exception e)
	    	{
	    	GA_PropertyWare.securityDeposit = "Error";	
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
			    GA_PropertyWare.countOfTypeWordInText = ((typeSubString.length() - newText.length())/"Type:".length());
			    System.out.println("Type: occurences = "+GA_PropertyWare.countOfTypeWordInText);
	    	
	    	GA_PropertyWare.petType = new ArrayList();
		    GA_PropertyWare.petBreed = new ArrayList();
		    GA_PropertyWare.petWeight = new ArrayList();
		    for(int i =0;i<GA_PropertyWare.countOfTypeWordInText;i++)
		    {
		    	String type = typeSubString.substring(RunnerClass.nthOccurrence(typeSubString, "Type:", i+1)+PDFAppConfig.AB_pet1Type_Prior.length(),RunnerClass.nthOccurrence(typeSubString, "Breed:", i+1)).trim();
		    	if(type.contains("N/A")||type.contains("n/a"))
		    		break;
		    	System.out.println(type);
		    	GA_PropertyWare.petType.add(type);
		    	int pet1Breedindex1 = RunnerClass.nthOccurrence(typeSubString, "Breed:", i+1)+"Breed:".length()+1;
			    String subString = typeSubString.substring(pet1Breedindex1);
			    //int pet1Breedindex2 = RunnerClass.nthOccurrence(subString,"Name:",i+1);
			   // System.out.println("Index 2 = "+(index2+index1));
			    String breed = subString.split("Name:")[0].trim();//typeSubString.substring(pet1Breedindex1,(pet1Breedindex2+pet1Breedindex1));
			    System.out.println(breed);
			    GA_PropertyWare.petBreed.add(breed);
			    int pet1Weightindex1 = RunnerClass.nthOccurrence(typeSubString, "Weight:", i+1)+"Weight:".length()+1;
			    String pet1WeightSubstring = typeSubString.substring(pet1Weightindex1);
			    //int pet1WeightIndex2 = RunnerClass.nthOccurrence(pet1WeightSubstring,"Age:",i+1);
			   // System.out.println("Index 2 = "+(index2+index1));
			    String weight = pet1WeightSubstring.split("Age:")[0].trim(); //typeSubString.substring(pet1Weightindex1,(pet1WeightIndex2+pet1Weightindex1));
			    System.out.println(weight);
			    GA_PropertyWare.petWeight.add(weight);
		    }
		    
	    	
		    try
		    {
		    	GA_PropertyWare.petOneTimeNonRefundableFee = text.substring(text.indexOf(PDFAppConfig_Format2.petOneTimeNonRefundable_Prior)+PDFAppConfig_Format2.petOneTimeNonRefundable_Prior.length(),text.indexOf(PDFAppConfig_Format2.petOneTimeNonRefundable_After)).trim();
		    	if(GA_PropertyWare.petOneTimeNonRefundableFee.matches(".*[a-zA-Z]+.*"))
			    {
			    	GA_PropertyWare.petOneTimeNonRefundableFee = "Error";
			    }
		    }
		    catch(Exception e)
		    {
		    	try
		    	{
		    		GA_PropertyWare.petOneTimeNonRefundableFee = text.substring(text.indexOf(PDFAppConfig_Format2.petOneTimeNonRefundable_Prior2)+PDFAppConfig_Format2.petOneTimeNonRefundable_Prior2.length()).trim().split(",")[0];
				    //System.out.println("pet one time non refundable = "+GA_PropertyWare.petOneTimeNonRefundableFee.trim());
		    		if(GA_PropertyWare.petOneTimeNonRefundableFee.matches(".*[a-zA-Z]+.*"))
				    {
				    	GA_PropertyWare.petOneTimeNonRefundableFee = "Error";
				    }
		    	}
		    	catch(Exception e2)
		    	{
		    	GA_PropertyWare.petOneTimeNonRefundableFee =  "Error";
		    	e2.printStackTrace();
		    	}
		    }  
		    System.out.println("pet one time non refundable = "+GA_PropertyWare.petOneTimeNonRefundableFee.trim());
		    
		  
		    
		    
	    }
	    
	  //Service Animal Addendum check
	    try
	    {
	    if(text.contains(AppConfig.serviceAnimalText))
	    {
	    	GA_PropertyWare.serviceAnimalFlag = true;
    		System.out.println("Service Animal Addendum is available");
    		
            String typeSubString = text.substring(text.indexOf(PDFAppConfig_Format2.AB_serviceAnimal_typeWord_Prior)+PDFAppConfig_Format2.AB_serviceAnimal_typeWord_Prior.length(),text.indexOf(PDFAppConfig_Format2.AB_serviceAnimal_typeWord_After));
	    	
	    	String newText = typeSubString.replace("Type:","");
		    int  countOftypeWords_ServiceAnimal = ((typeSubString.length() - newText.length())/"Type:".length());
		    System.out.println("Service Animal - Type: occurences = "+countOftypeWords_ServiceAnimal);
		    
		    GA_PropertyWare.serviceAnimalPetType = new ArrayList();
		    GA_PropertyWare.serviceAnimalPetBreed = new ArrayList();
		    GA_PropertyWare.serviceAnimalPetWeight = new ArrayList();
		    for(int i =0;i<countOftypeWords_ServiceAnimal;i++)
		    {
		    	String type = typeSubString.substring(RunnerClass.nthOccurrence(typeSubString, "Type:", i+1)+PDFAppConfig.AB_pet1Type_Prior.length(),RunnerClass.nthOccurrence(typeSubString, "Breed:", i+1)).trim();
		    	if(type.contains("N/A")||type.contains("n/a"))
		    		break;
		    	System.out.println(type);
		    	GA_PropertyWare.serviceAnimalPetType.add(type);
		    	int pet1Breedindex1 = RunnerClass.nthOccurrence(typeSubString, "Breed:", i+1)+"Breed:".length()+1;
			    String subString = typeSubString.substring(pet1Breedindex1);
			    //int pet1Breedindex2 = RunnerClass.nthOccurrence(subString,"Name:",i+1);
			   // System.out.println("Index 2 = "+(index2+index1));
			    String breed = subString.split("Name:")[0].trim();//typeSubString.substring(pet1Breedindex1,(pet1Breedindex2+pet1Breedindex1));
			    System.out.println(breed);
			    GA_PropertyWare.serviceAnimalPetBreed.add(breed);
			    int pet1Weightindex1 = RunnerClass.nthOccurrence(typeSubString, "Weight:", i+1)+"Weight:".length()+1;
			    String pet1WeightSubstring = typeSubString.substring(pet1Weightindex1);
			    //int pet1WeightIndex2 = RunnerClass.nthOccurrence(pet1WeightSubstring,"Age:",i+1);
			   // System.out.println("Index 2 = "+(index2+index1));
			    String weight = pet1WeightSubstring.split("Age:")[0].trim(); //typeSubString.substring(pet1Weightindex1,(pet1WeightIndex2+pet1Weightindex1));
			    System.out.println(weight);
			    GA_PropertyWare.serviceAnimalPetWeight.add(weight);
		    }
    		
    		
	    }
	    }
	    catch(Exception e)
	    {
	    	GA_PropertyWare.serviceAnimalFlag = false;
	    }
	  //Concession Addendum
	    
	    try
	    {
	    	if(text.contains(PDFAppConfig.concessionAddendumText))
	    	{
	    		GA_PropertyWare.concessionAddendumFlag = true;
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
	    	GA_PropertyWare.lateChargeDay = text.substring(text.indexOf(PDFAppConfig_Format2.lateFeeDay_Prior)+PDFAppConfig_Format2.lateFeeDay_Prior.length()).trim().split(" ")[0].trim().replace("[^0-9]", "");
	    	GA_PropertyWare.lateChargeDay = GA_PropertyWare.lateChargeDay.replaceAll("[^\\d]", "");
	    	System.out.println("Late Charge Day = "+GA_PropertyWare.lateChargeDay);//.substring(commensementDate.lastIndexOf(":")+1));
	    }
	    catch(Exception e)
	    {
	    	GA_PropertyWare.lateChargeDay = "Error";
	    	e.printStackTrace();
	    }
	    
	    //Late Charge Fee
	    try
	    {
	    	GA_PropertyWare.lateChargeFee = text.substring(text.indexOf(PDFAppConfig_Format2.initialLateChargeFee_Prior)+PDFAppConfig_Format2.initialLateChargeFee_Prior.length()).trim().split(" ")[0].trim();
	    	GA_PropertyWare.lateChargeFee = GA_PropertyWare.lateChargeFee.replaceAll("[^.0-9]", "");
	    }
	    catch(Exception e)
	    {
	    	GA_PropertyWare.lateChargeFee = "Error";
	    	e.printStackTrace();
	    }
	    System.out.println("Late Fee = "+GA_PropertyWare.lateChargeFee);//.substring(commensementDate.lastIndexOf(":")+1));
	    //Per Day Fee
	    try
	    {
	    	GA_PropertyWare.additionalLateCharges = text.substring(text.indexOf(PDFAppConfig_Format2.additionaLateCharge_Prior)+PDFAppConfig_Format2.additionaLateCharge_Prior.length()).trim().split(" ")[0].trim();
	    	GA_PropertyWare.additionalLateCharges = GA_PropertyWare.additionalLateCharges.replaceAll("[^.0-9]", "");
	    }
	    catch(Exception e)
	    {
	    	GA_PropertyWare.additionalLateCharges = "Error";
	    	e.printStackTrace();
	    }
	    System.out.println("Additional Late Charges  = "+GA_PropertyWare.additionalLateCharges);//.substring(commensementDate.lastIndexOf(":")+1));
	    /*
	    try
	    {
	    if(GA_RunnerClass.pdfFormatType.equalsIgnoreCase("Format1"))
	    {
	    	GA_PropertyWare.additionalLateChargesLimit = text.substring(text.indexOf(PDFAppConfig_Format2.additionaLateCharge_Prior)+PDFAppConfig_Format2.additionaLateCharge_Prior.length()).split(" ")[0].trim();
		    System.out.println("Additional Late Charges  = "+GA_PropertyWare.additionalLateChargesLimit);//.substring(commensementDate.lastIndexOf(":")+1));
	    }
	    }
	    catch(Exception e)
	    {
	    	GA_PropertyWare.additionalLateChargesLimit = "Error";
	    	e.printStackTrace();
	    }
	    */
	}
	
}
