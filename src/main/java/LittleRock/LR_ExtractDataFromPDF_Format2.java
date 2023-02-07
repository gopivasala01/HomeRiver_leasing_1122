package LittleRock;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import mainPackage.RunnerClass;

public class LR_ExtractDataFromPDF_Format2 
{
	public static boolean petFlag;
	public static String text ="";
	public boolean arizona() throws Exception
	//public static void main(String[] args) throws Exception 
	{
		LR_PropertyWare.petFlag = false;
		//File file = new File("C:\\Gopi\\Projects\\Property ware\\Lease Close Outs\\PDFS\\Tennessee\\Format 2\\Lease_031.22_05.23_1327_Everwood_Dr_Ashland_C_(1).pdf");
		File file = RunnerClass.getLastModified();
		FileInputStream fis = new FileInputStream(file);
		LR_RunnerClass.document = PDDocument.load(fis);
	    text = new PDFTextStripper().getText(LR_RunnerClass.document);
	    text = text.replaceAll(System.lineSeparator(), " ");
	    text = text.trim().replaceAll(" +", " ");
	    System.out.println(text);
	    System.out.println("------------------------------------------------------------------");
	    
	    try
	    {
	    	String commensementRaw = text.substring(text.indexOf(LR_PDFAppConfig_Format2.commensementDate_Prior)+LR_PDFAppConfig_Format2.commensementDate_Prior.length()+1).trim();//,text.indexOf(LR_PDFAppConfig_Format2.commensementDate_After)).trim();
	    	 LR_PropertyWare.commensementDate = commensementRaw.substring(0, commensementRaw.indexOf('(')).trim();
	    	 LR_PropertyWare.commensementDate = LR_PropertyWare.commensementDate.trim().replaceAll(" +", " ");
		    System.out.println("Commensement Date = "+LR_PropertyWare.commensementDate);//.substring(commensementDate.lastIndexOf(":")+1));
	    }
	    catch(Exception e)
	    {
	    	LR_PropertyWare.commensementDate = "Error";
	    	e.printStackTrace();
	    }
	    
	    try
	    {
	    	String expirationDateRaw  = text.substring(text.indexOf(LR_PDFAppConfig_Format2.expirationDate_Prior)+LR_PDFAppConfig_Format2.expirationDate_Prior.length()).trim();//,text.indexOf(LR_PDFAppConfig_Format2.expirationDate_After)).trim();
	    	LR_PropertyWare.expirationDate = expirationDateRaw.substring(0,expirationDateRaw.indexOf('(')).trim();
	    	LR_PropertyWare.expirationDate = LR_PropertyWare.expirationDate.trim().replaceAll(" +", " ");
	    	System.out.println("Expiration Date = "+LR_PropertyWare.expirationDate);//.substring(commensementDate.lastIndexOf(":")+1));
	    }
	    catch(Exception e)
	    {
	    	LR_PropertyWare.expirationDate = "Error";
	    	e.printStackTrace();
	    }
	    
	    try
	    {
	    	LR_PropertyWare.proratedRentDate = text.substring(text.indexOf(LR_PDFAppConfig_Format2.proratedRentDate_Prior)+LR_PDFAppConfig_Format2.proratedRentDate_Prior.length()+1,text.indexOf(LR_PDFAppConfig_Format2.proratedRentDate_After)).trim();
		    System.out.println("prorated Rent Date = "+LR_PropertyWare.proratedRentDate);//.substring(commensementDate.lastIndexOf(":")+1));
	    }
	    catch(Exception e)
	    {
	    	LR_PropertyWare.proratedRentDate = "Error";
	    	e.printStackTrace();
	    }
	    
	    try
	    {
	    	LR_PropertyWare.proratedRent = text.substring(text.indexOf(LR_PDFAppConfig_Format2.proratedRent_Prior)+LR_PDFAppConfig_Format2.proratedRent_Prior.length()).split(" ")[0].trim();
	    	if(LR_PropertyWare.proratedRent.matches(".*[a-zA-Z]+.*"))
		    {
		    	LR_PropertyWare.proratedRent = "Error";
		    }
	    }
	    catch(Exception e)
	    {
	    	LR_PropertyWare.proratedRent = "Error";
	    	e.printStackTrace();
	    }
	    System.out.println("prorated Rent = "+LR_PropertyWare.proratedRent);//.substring(commensementDate.lastIndexOf(":")+1));
	    
	    try
	    {
	    	LR_PropertyWare.monthlyRent = text.substring(text.indexOf(LR_PDFAppConfig_Format2.monthlyRent_Prior)+LR_PDFAppConfig_Format2.monthlyRent_Prior.length()).split(" ")[0].trim();
	    	if(!LR_PropertyWare.monthlyRent.contains("."))
	    		LR_PropertyWare.monthlyRent = text.substring(text.indexOf(LR_PDFAppConfig_Format2.monthlyRent_Prior2)+LR_PDFAppConfig_Format2.monthlyRent_Prior2.length()).split(" ")[0].trim();
	    	if(LR_PropertyWare.monthlyRent.matches(".*[a-zA-Z]+.*"))
		    {
		    	LR_PropertyWare.monthlyRent = "Error";
		    }
	    	if(LR_PropertyWare.monthlyRent.contains("*")||text.contains(LR_PDFAppConfig_Format2.monthlyRentAvailabilityCheck)==true)
	    	{
	    		LR_PropertyWare.incrementRentFlag = true;
	    		LR_PropertyWare.monthlyRent = LR_PropertyWare.monthlyRent.replace("*", "");
	    		System.out.println("Monthly Rent has Asterick *");
	    		
	    		//LR_PropertyWare.increasedRent_amount = text.substring(text.indexOf(". $")+". $".length()).trim().split(" ")[0];
	    		String increasedRent_ProviousRentEndDate = "Per the Landlord, Monthly Rent from "+LR_PropertyWare.commensementDate.trim()+" through ";
	    		 String endDateArray[] = text.substring(text.indexOf(". $")+". $".length()).split(" ");
	    		if(endDateArray[2].trim().length()==4)//&&RunnerClass.onlyDigits(endDateArray[2]))
	    		 {
	    		  LR_PropertyWare.increasedRent_previousRentEndDate = endDateArray[0]+" "+endDateArray[1]+" "+endDateArray[2];
	    		  System.out.println("Increased Rent - Previous rent end date = "+LR_PropertyWare.increasedRent_previousRentEndDate);
	    		 
	    		  String newRentStartDate[] = text.substring(text.indexOf(LR_PDFAppConfig_Format2.increasedRent_newStartDate_Prior)+LR_PDFAppConfig_Format2.increasedRent_newStartDate_Prior.length()).trim().split(" ");
	    		  LR_PropertyWare.increasedRent_newStartDate = newRentStartDate[0]+" "+newRentStartDate[1]+" "+newRentStartDate[2];
	    		  System.out.println("Increased Rent - New Rent Start date = "+LR_PropertyWare.increasedRent_newStartDate);
	    		  
	    		  String increasedRentRaw = text.substring(text.indexOf(LR_PDFAppConfig_Format2.increasedRent_newStartDate_Prior)+LR_PDFAppConfig_Format2.increasedRent_newStartDate_Prior.length()).trim();
	    		  LR_PropertyWare.increasedRent_amount = increasedRentRaw.substring(increasedRentRaw.indexOf("shall be $")+"shall be $".length()).trim().split(" ")[0];
	    		  System.out.println("Increased Rent - Amount = "+LR_PropertyWare.increasedRent_amount); 
	    		}
	    		else 
	    		 {
	    			 String adding0toMonth = "0"+LR_PropertyWare.commensementDate.trim().split(" ")[1];
	    			 String commeseDate = LR_PropertyWare.commensementDate.trim().replace(LR_PropertyWare.commensementDate.trim().split(" ")[1], adding0toMonth);
	    			 increasedRent_ProviousRentEndDate = "Per the Landlord, Monthly Rent from "+commeseDate+" through ";
		    		 String endDateArray2[] = text.substring(text.indexOf(increasedRent_ProviousRentEndDate)+increasedRent_ProviousRentEndDate.length()).split(" ");
		    		 if(endDateArray2[2].trim().length()==4)//&&RunnerClass.onlyDigits(endDateArray[2]))
		    		 {
		    		  LR_PropertyWare.increasedRent_previousRentEndDate = endDateArray2[0]+" "+endDateArray2[1]+" "+endDateArray2[2];
		    		  System.out.println("Increased Rent - Previous rent end date = "+LR_PropertyWare.increasedRent_previousRentEndDate);
		    		 
		    		  String newRentStartDate[] = text.substring(text.indexOf(LR_PDFAppConfig_Format2.increasedRent_newStartDate_Prior)+LR_PDFAppConfig_Format2.increasedRent_newStartDate_Prior.length()).trim().split(" ");
		    		  LR_PropertyWare.increasedRent_newStartDate = newRentStartDate[0]+" "+newRentStartDate[1]+" "+newRentStartDate[2];
		    		  System.out.println("Increased Rent - New Rent Start date = "+LR_PropertyWare.increasedRent_newStartDate);
		    		  
		    		  String increasedRentRaw = text.substring(text.indexOf(LR_PDFAppConfig_Format2.increasedRent_newStartDate_Prior)+LR_PDFAppConfig_Format2.increasedRent_newStartDate_Prior.length()).trim();
		    		  LR_PropertyWare.increasedRent_amount = increasedRentRaw.substring(increasedRentRaw.indexOf("shall be $")+"shall be $".length()).trim().split(" ")[0];
		    		  System.out.println("Increased Rent - Amount = "+LR_PropertyWare.increasedRent_amount); 
		    		 }
	    		 }
	    	}
	    }
	    catch(Exception e)
	    {
	    	LR_PropertyWare.monthlyRent = "Error";
	    	e.printStackTrace();
	    }
	    System.out.println("Monthly Rent = "+LR_PropertyWare.monthlyRent);//.substring(commensementDate.lastIndexOf(":")+1));
	   
	    try
	    {
	    	LR_PropertyWare.adminFee = text.substring(text.indexOf(LR_PDFAppConfig_Format2.adminFee_prior)+LR_PDFAppConfig_Format2.adminFee_prior.length()).split(" ")[0].trim();
	    	if(LR_PropertyWare.adminFee.matches(".*[a-zA-Z]+.*"))
		    {
		    	LR_PropertyWare.adminFee = "Error";
		    }
	    }
	    catch(Exception e)
	    {
	    	LR_PropertyWare.adminFee = "Error";
	    	e.printStackTrace();
	    }
	    System.out.println("Admin Fee = "+LR_PropertyWare.adminFee);//.substring(commensementDate.lastIndexOf(":")+1));
	  //Resident Benefits Package 
	    if(text.contains(LR_PDFAppConfig.residentBenefitsPackageAddendumCheck))
	    {
	    	LR_PropertyWare.residentBenefitsPackageAvailabilityCheck = true;
	    	 try
	 	    {
	 		    LR_PropertyWare.residentBenefitsPackage = text.substring(text.indexOf(LR_PDFAppConfig_Format2.AB1_residentBenefitsPackage_Prior)+LR_PDFAppConfig_Format2.AB1_residentBenefitsPackage_Prior.length()).split(" ")[0].replaceAll("[^0-9a-zA-Z.]", "");
	 		    if(LR_PropertyWare.residentBenefitsPackage.matches(".*[a-zA-Z]+.*"))
	 		    {
	 		    	LR_PropertyWare.residentBenefitsPackage = "Error";
	 		    }
	 	    }
	 	    catch(Exception e)
	 	    {
	 		    LR_PropertyWare.residentBenefitsPackage = "Error";
	 		    e.printStackTrace();
	 	    }
	    	 System.out.println("Resident Benefits Package  = "+LR_PropertyWare.residentBenefitsPackage.trim());
	    	//AR_PDFAppConfig.AB1_residentBenefitsPackage_Prior
	    }
	    else
	    {
		    if(text.contains(LR_PDFAppConfig_Format2.HVACFilterAddendumTextAvailabilityCheck)==true)
		    {
		    	LR_PropertyWare.HVACFilterFlag =true;
		    }
		    else
		    {
		    try
		    {
		    	LR_PropertyWare.airFilterFee = text.substring(text.indexOf(LR_PDFAppConfig_Format2.HVACAirFilter_prior)+LR_PDFAppConfig_Format2.HVACAirFilter_prior.length()).split(" ")[0].trim();
		    	if(LR_PropertyWare.airFilterFee.matches(".*[a-zA-Z]+.*"))
			    {
			    	LR_PropertyWare.airFilterFee = "Error";
			    }
		    }
		    catch(Exception e)
		    {
		    	LR_PropertyWare.airFilterFee = "Error";
		    	e.printStackTrace();
		    }
		    }
		    System.out.println("HVAC Air Filter Fee = "+LR_PropertyWare.airFilterFee);//.substring(commensementDate.lastIndexOf(":")+1));
	    }
	    try
	    {
	    	LR_PropertyWare.occupants = text.substring(text.indexOf(LR_PDFAppConfig_Format2.occupants_Prior)+LR_PDFAppConfig_Format2.occupants_Prior.length(),text.indexOf(LR_PDFAppConfig_Format2.occupants_After)).trim();
		    System.out.println("Occupants = "+LR_PropertyWare.occupants);//.substring(commensementDate.lastIndexOf(":")+1));
	    }
	    catch(Exception e)
	    {
	    	LR_PropertyWare.occupants = "Error";
	    	e.printStackTrace();
	    }
	    
	    //Late Fee Rule
	    LR_ExtractDataFromPDF_Format2.lateFeeRule();
	    
	    
	  //Prepayment Charge
	    try
	    {
		if(LR_PropertyWare.portfolioType.contains("MCH"))
		{
		try
		{
		LR_PropertyWare.prepaymentCharge =String.valueOf(Double.parseDouble(LR_PropertyWare.monthlyRent.replace(",", "")) - Double.parseDouble(LR_PropertyWare.proratedRent.replace(",", ""))); 
		}
		catch(Exception e)
		{
			LR_PropertyWare.prepaymentCharge ="Error";
		}
		System.out.println("Prepayment Charge = "+LR_PropertyWare.prepaymentCharge);
		}
	    }
	    catch(Exception e) {}
	    //Early Termination
		try
	    {
	    	String[] earlyTerminationRaw = text.substring(text.indexOf(LR_PDFAppConfig_Format2.earlyTermination_Prior)+LR_PDFAppConfig_Format2.earlyTermination_Prior.length()).split(" ");
	    	
		    LR_PropertyWare.earlyTermination = earlyTerminationRaw[0]+earlyTerminationRaw[1]; //text.substring(text.indexOf(AR_PDFAppConfig.AB_earlyTerminationFee_Prior)+AR_PDFAppConfig.AB_earlyTerminationFee_Prior.length(),text.indexOf(AR_PDFAppConfig.AB_earlyTerminationFee_After));
	    }
	    catch(Exception e)
	    {
	    	LR_PropertyWare.earlyTermination = "Error";	
	    	e.printStackTrace();
	    }
		System.out.println("Early Termination = "+LR_PropertyWare.earlyTermination);
	    // Checking Pet Addendum is available or not
	    
	    if(text.contains(LR_PDFAppConfig_Format2.petAgreementAvailabilityCheck)==true)
	    	petFlag =  true;
	    else if(petFlag = text.contains(LR_PDFAppConfig_Format2.petAgreementAvailabilityCheck2)==true)
	    	petFlag =  true;
	    else petFlag =  false;
	    
	    System.out.println("Pet Addendum Available = "+petFlag);
	    if(petFlag ==true)
	    {
	    	LR_PropertyWare.petFlag = true;
			    	try
			    	{
			    		 String proratedPetRaw = "Prorated Pet Rent: On or before "+LR_PropertyWare.commensementDate.trim()+" Tenant will pay Landlord $";
				    		LR_PropertyWare.proratedPetRent = text.substring(text.indexOf(proratedPetRaw)+proratedPetRaw.length()).trim().split(" ")[0].trim();//.replaceAll("[a-ZA-Z,]", "");
				    		if(LR_PropertyWare.proratedPetRent.matches(".*[a-zA-Z]+.*")||LR_PropertyWare.proratedPetRent.trim().equals("1."))
						    {
						    	LR_PropertyWare.proratedPetRent = "Error";
						    }
				    		//LR_PropertyWare.proratedPetRent = proratedPetRentRaw.substring(proratedPetRentRaw.indexOf("Tenant will pay Landlord $")+"Tenant will pay Landlord $".length());//,proratedPetRentRaw.indexOf(AppConfig.AR_proratedPetRent_After));
			        }
				    catch(Exception e)
				    {
				    LR_PropertyWare.proratedPetRent = "Error";	
				    e.printStackTrace();
				    }
	    	System.out.println("Prorated Pet rent= "+LR_PropertyWare.proratedPetRent.trim());
	    	try
    		{
    			LR_PropertyWare.petRent = text.substring(text.indexOf(LR_PDFAppConfig_Format2.petRent_Prior)+LR_PDFAppConfig_Format2.petRent_Prior.length()).split(" ")[0].trim();
				 //System.out.println("Pet rent = "+LR_PropertyWare.petRent.trim());
				 if(RunnerClass.onlyDigits(LR_PropertyWare.petRent)==false)
				    {
				    	 LR_PropertyWare.petRent = text.substring(text.indexOf(LR_PDFAppConfig_Format2.petRent_Prior2)+LR_PDFAppConfig_Format2.petRent_Prior2.length()).trim().split(" ")[0];
				    }
				 if(LR_PropertyWare.petRent.matches(".*[a-zA-Z]+.*"))
				    {
				    	LR_PropertyWare.petRent = "Error";
				    }
    		}
    		
    		catch(Exception e1)
		    {
		    	LR_PropertyWare.petRent = "Error";  
		    	e1.printStackTrace();
		    }
	    	System.out.println("Pet rent= "+LR_PropertyWare.petRent.trim());
	    	
	    	//Pet Security Deposit -- Need to find the text of Pet Security Deposit for the format PDF, until then, it is commented
	    	/*
	    	try
	    	{
	    	LR_PropertyWare.petSecurityDeposit = text.substring(text.indexOf(LR_PDFAppConfig_Format2.securityDeposit_Prior)+LR_PDFAppConfig_Format2.securityDeposit_Prior.length()).trim().split(" ")[0];//,text.indexOf(LR_PDFAppConfig_Format2.securityDeposit_After));
		    System.out.println("Security Deposit = "+LR_PropertyWare.securityDeposit.trim());
		    if(RunnerClass.onlyDigits(LR_PropertyWare.petSecurityDeposit)==true)
		    {
		    	LR_PropertyWare.petSecurityDepositFlag = true;
		    	System.out.println("Security Deposit is checked and has value");
		    }
	    	}
	    	catch(Exception e)
	    	{
	    	LR_PropertyWare.securityDeposit = "Error";	
	    	e.printStackTrace();
	    	}
	    	*/
	    	String typeSubString = "";
	    	try
	    	{
	    	typeSubString = text.substring(text.indexOf(LR_PDFAppConfig_Format2.typeWord_Prior)+LR_PDFAppConfig_Format2.typeWord_Prior.length(),text.indexOf(LR_PDFAppConfig_Format2.typeWord_After));
	    	}
	    	catch(Exception e)
	    	{
	    		try
	    		{
	    		typeSubString = text.substring(text.indexOf(LR_PDFAppConfig_Format2.typeWord_Prior)+LR_PDFAppConfig_Format2.typeWord_Prior.length(),text.indexOf(LR_PDFAppConfig_Format2.typeWord_After2));
	    		}
	    		catch(Exception e2)
	    		{
	    			try
	    			{
	    				typeSubString = text.substring(text.indexOf(LR_PDFAppConfig_Format2.typeWord_Prior2)+LR_PDFAppConfig_Format2.typeWord_Prior2.length(),text.indexOf(LR_PDFAppConfig_Format2.typeWord_After3));
	    			}
	    			catch(Exception e3)
	    			{
	    		    typeSubString = "";
	    			}
	    		}
	    	}
	    	 String newText = typeSubString.replace("Type:","");
			    LR_PropertyWare.countOfTypeWordInText = ((typeSubString.length() - newText.length())/"Type:".length());
			    System.out.println("Type: occurences = "+LR_PropertyWare.countOfTypeWordInText);
	    	
	    	LR_PropertyWare.petType = new ArrayList();
		    LR_PropertyWare.petBreed = new ArrayList();
		    LR_PropertyWare.petWeight = new ArrayList();
		    for(int i =0;i<LR_PropertyWare.countOfTypeWordInText;i++)
		    {
		    	String type = typeSubString.substring(RunnerClass.nthOccurrence(typeSubString, "Type:", i+1)+LR_PDFAppConfig.AB_pet1Type_Prior.length(),RunnerClass.nthOccurrence(typeSubString, "Breed:", i+1)).trim();
		    	if(type.contains("N/A")||type.contains("n/a"))
		    		break;
		    	System.out.println(type);
		    	LR_PropertyWare.petType.add(type);
		    	int pet1Breedindex1 = RunnerClass.nthOccurrence(typeSubString, "Breed:", i+1)+"Breed:".length()+1;
			    String subString = typeSubString.substring(pet1Breedindex1);
			    //int pet1Breedindex2 = RunnerClass.nthOccurrence(subString,"Name:",i+1);
			   // System.out.println("Index 2 = "+(index2+index1));
			    String breed = subString.split("Name:")[0].trim();//typeSubString.substring(pet1Breedindex1,(pet1Breedindex2+pet1Breedindex1));
			    System.out.println(breed);
			    LR_PropertyWare.petBreed.add(breed);
			    int pet1Weightindex1 = RunnerClass.nthOccurrence(typeSubString, "Weight:", i+1)+"Weight:".length()+1;
			    String pet1WeightSubstring = typeSubString.substring(pet1Weightindex1);
			    //int pet1WeightIndex2 = RunnerClass.nthOccurrence(pet1WeightSubstring,"Age:",i+1);
			   // System.out.println("Index 2 = "+(index2+index1));
			    String weight = pet1WeightSubstring.split("Age:")[0].trim(); //typeSubString.substring(pet1Weightindex1,(pet1WeightIndex2+pet1Weightindex1));
			    System.out.println(weight);
			    LR_PropertyWare.petWeight.add(weight);
		    }
		    
	    	
		    try
		    {
		    	LR_PropertyWare.petOneTimeNonRefundableFee = text.substring(text.indexOf(LR_PDFAppConfig_Format2.petOneTimeNonRefundable_Prior)+LR_PDFAppConfig_Format2.petOneTimeNonRefundable_Prior.length(),text.indexOf(LR_PDFAppConfig_Format2.petOneTimeNonRefundable_After)).trim();
		    	if(LR_PropertyWare.petOneTimeNonRefundableFee.matches(".*[a-zA-Z]+.*"))
			    {
			    	LR_PropertyWare.petOneTimeNonRefundableFee = "Error";
			    }
		    }
		    catch(Exception e)
		    {
		    	try
		    	{
		    		LR_PropertyWare.petOneTimeNonRefundableFee = text.substring(text.indexOf(LR_PDFAppConfig_Format2.petOneTimeNonRefundable_Prior2)+LR_PDFAppConfig_Format2.petOneTimeNonRefundable_Prior2.length()).trim().split(",")[0];
				    //System.out.println("pet one time non refundable = "+LR_PropertyWare.petOneTimeNonRefundableFee.trim());
		    		if(LR_PropertyWare.petOneTimeNonRefundableFee.matches(".*[a-zA-Z]+.*"))
				    {
				    	LR_PropertyWare.petOneTimeNonRefundableFee = "Error";
				    }
		    	}
		    	catch(Exception e2)
		    	{
		    	LR_PropertyWare.petOneTimeNonRefundableFee =  "Error";
		    	e2.printStackTrace();
		    	}
		    }  
		    System.out.println("pet one time non refundable = "+LR_PropertyWare.petOneTimeNonRefundableFee.trim());
		    
		  
		    
		    
	    }
	    
	  //Service Animal Addendum check
	    try
	    {
	    if(text.contains(LR_AppConfig.serviceAnimalText))
	    {
	    	LR_PropertyWare.serviceAnimalFlag = true;
    		System.out.println("Service Animal Addendum is available");
    		
            String typeSubString = text.substring(text.indexOf(LR_PDFAppConfig_Format2.AB_serviceAnimal_typeWord_Prior)+LR_PDFAppConfig_Format2.AB_serviceAnimal_typeWord_Prior.length(),text.indexOf(LR_PDFAppConfig_Format2.AB_serviceAnimal_typeWord_After));
	    	
	    	String newText = typeSubString.replace("Type:","");
		    int  countOftypeWords_ServiceAnimal = ((typeSubString.length() - newText.length())/"Type:".length());
		    System.out.println("Service Animal - Type: occurences = "+countOftypeWords_ServiceAnimal);
		    
		    LR_PropertyWare.serviceAnimalPetType = new ArrayList();
		    LR_PropertyWare.serviceAnimalPetBreed = new ArrayList();
		    LR_PropertyWare.serviceAnimalPetWeight = new ArrayList();
		    for(int i =0;i<countOftypeWords_ServiceAnimal;i++)
		    {
		    	String type = typeSubString.substring(RunnerClass.nthOccurrence(typeSubString, "Type:", i+1)+LR_PDFAppConfig.AB_pet1Type_Prior.length(),RunnerClass.nthOccurrence(typeSubString, "Breed:", i+1)).trim();
		    	if(type.contains("N/A")||type.contains("n/a"))
		    		break;
		    	System.out.println(type);
		    	LR_PropertyWare.serviceAnimalPetType.add(type);
		    	int pet1Breedindex1 = RunnerClass.nthOccurrence(typeSubString, "Breed:", i+1)+"Breed:".length()+1;
			    String subString = typeSubString.substring(pet1Breedindex1);
			    //int pet1Breedindex2 = RunnerClass.nthOccurrence(subString,"Name:",i+1);
			   // System.out.println("Index 2 = "+(index2+index1));
			    String breed = subString.split("Name:")[0].trim();//typeSubString.substring(pet1Breedindex1,(pet1Breedindex2+pet1Breedindex1));
			    System.out.println(breed);
			    LR_PropertyWare.serviceAnimalPetBreed.add(breed);
			    int pet1Weightindex1 = RunnerClass.nthOccurrence(typeSubString, "Weight:", i+1)+"Weight:".length()+1;
			    String pet1WeightSubstring = typeSubString.substring(pet1Weightindex1);
			    //int pet1WeightIndex2 = RunnerClass.nthOccurrence(pet1WeightSubstring,"Age:",i+1);
			   // System.out.println("Index 2 = "+(index2+index1));
			    String weight = pet1WeightSubstring.split("Age:")[0].trim(); //typeSubString.substring(pet1Weightindex1,(pet1WeightIndex2+pet1Weightindex1));
			    System.out.println(weight);
			    LR_PropertyWare.serviceAnimalPetWeight.add(weight);
		    }
    		
    		
	    }
	    }
	    catch(Exception e)
	    {
	    	LR_PropertyWare.serviceAnimalFlag = false;
	    }
	  //Concession Addendum
	    
	    try
	    {
	    	if(text.contains(LR_PDFAppConfig.concessionAddendumText))
	    	{
	    		LR_PropertyWare.concessionAddendumFlag = true;
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
	    	LR_PropertyWare.lateChargeDay = text.substring(text.indexOf(LR_PDFAppConfig_Format2.lateFeeDay_Prior)+LR_PDFAppConfig_Format2.lateFeeDay_Prior.length()).trim().split(" ")[0].trim().replace("[^0-9]", "");
	    	LR_PropertyWare.lateChargeDay = LR_PropertyWare.lateChargeDay.replaceAll("[^\\d]", "");
	    	System.out.println("Late Charge Day = "+LR_PropertyWare.lateChargeDay);//.substring(commensementDate.lastIndexOf(":")+1));
	    }
	    catch(Exception e)
	    {
	    	LR_PropertyWare.lateChargeDay = "Error";
	    	e.printStackTrace();
	    }
	    
	    //Late Charge Fee
	    try
	    {
	    	LR_PropertyWare.lateChargeFee = text.substring(text.indexOf(LR_PDFAppConfig_Format2.lateChargePerDayFee)+LR_PDFAppConfig_Format2.lateChargePerDayFee.length()).trim().split(" ")[0].trim();
	    	//LR_PropertyWare.lateChargeFee = LR_PropertyWare.lateChargeFee.replaceAll("[^.0-9]", "");
	    }
	    catch(Exception e)
	    {
	    	LR_PropertyWare.lateChargeFee = "Error";
	    	e.printStackTrace();
	    }
	    System.out.println("Late Fee = "+LR_PropertyWare.lateChargeFee);//.substring(commensementDate.lastIndexOf(":")+1));
	    /*
	    //Per Day Fee
	    try
	    {
	    	LR_PropertyWare.additionalLateCharges = text.substring(text.indexOf(LR_PDFAppConfig_Format2.additionaLateCharge_Prior)+LR_PDFAppConfig_Format2.additionaLateCharge_Prior.length()).trim().split(" ")[0].trim();
	    	LR_PropertyWare.additionalLateCharges = LR_PropertyWare.additionalLateCharges.replaceAll("[^.0-9]", "");
	    }
	    catch(Exception e)
	    {
	    	LR_PropertyWare.additionalLateCharges = "Error";
	    	e.printStackTrace();
	    }
	    System.out.println("Additional Late Charges  = "+LR_PropertyWare.additionalLateCharges);//.substring(commensementDate.lastIndexOf(":")+1));
	    
	    try
	    {
	    if(LR_RunnerClass.pdfFormatType.equalsIgnoreCase("Format1"))
	    {
	    	LR_PropertyWare.additionalLateChargesLimit = text.substring(text.indexOf(LR_PDFAppConfig_Format2.additionaLateCharge_Prior)+LR_PDFAppConfig_Format2.additionaLateCharge_Prior.length()).split(" ")[0].trim();
		    System.out.println("Additional Late Charges  = "+LR_PropertyWare.additionalLateChargesLimit);//.substring(commensementDate.lastIndexOf(":")+1));
	    }
	    }
	    catch(Exception e)
	    {
	    	LR_PropertyWare.additionalLateChargesLimit = "Error";
	    	e.printStackTrace();
	    }
	    */
	}
	

}
