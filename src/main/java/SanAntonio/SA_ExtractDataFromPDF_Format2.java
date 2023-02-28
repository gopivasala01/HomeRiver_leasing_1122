package SanAntonio;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import mainPackage.RunnerClass;

public class SA_ExtractDataFromPDF_Format2 
{
	public static boolean petFlag;
	public static String text ="";
	public boolean arizona() throws Exception
	//public static void main(String[] args) throws Exception 
	{
		SA_PropertyWare.petFlag = false;
		//File file = new File("C:\\Gopi\\Projects\\Property ware\\Lease Close Outs\\PDFS\\Tennessee\\Format 2\\Lease_031.22_05.23_1327_Everwood_Dr_Ashland_C_(1).pdf");
		File file = RunnerClass.getLastModified();
		FileInputStream fis = new FileInputStream(file);
		SA_RunnerClass.document = PDDocument.load(fis);
	    text = new PDFTextStripper().getText(SA_RunnerClass.document);
	    text = text.replaceAll(System.lineSeparator(), " ");
	    text = text.trim().replaceAll(" +", " ");
	    System.out.println(text);
	    System.out.println("------------------------------------------------------------------");
	    
	    try
	    {
	    	String commensementRaw = text.substring(text.indexOf(SA_PDFAppConfig_Format2.commensementDate_Prior)+SA_PDFAppConfig_Format2.commensementDate_Prior.length()+1).trim();//,text.indexOf(SA_PDFAppConfig_Format2.commensementDate_After)).trim();
	    	 SA_PropertyWare.commensementDate = commensementRaw.substring(0, commensementRaw.indexOf('(')).trim();
	    	 SA_PropertyWare.commensementDate = SA_PropertyWare.commensementDate.trim().replaceAll(" +", " ");
		    System.out.println("Commensement Date = "+SA_PropertyWare.commensementDate);//.substring(commensementDate.lastIndexOf(":")+1));
	    }
	    catch(Exception e)
	    {
	    	SA_PropertyWare.commensementDate = "Error";
	    	e.printStackTrace();
	    }
	    
	    try
	    {
	    	String expirationDateRaw  = text.substring(text.indexOf(SA_PDFAppConfig_Format2.expirationDate_Prior)+SA_PDFAppConfig_Format2.expirationDate_Prior.length()).trim();//,text.indexOf(SA_PDFAppConfig_Format2.expirationDate_After)).trim();
	    	SA_PropertyWare.expirationDate = expirationDateRaw.substring(0,expirationDateRaw.indexOf('(')).trim();
	    	SA_PropertyWare.expirationDate = SA_PropertyWare.expirationDate.trim().replaceAll(" +", " ");
	    	System.out.println("Expiration Date = "+SA_PropertyWare.expirationDate);//.substring(commensementDate.lastIndexOf(":")+1));
	    }
	    catch(Exception e)
	    {
	    	SA_PropertyWare.expirationDate = "Error";
	    	e.printStackTrace();
	    }
	    
	    try
	    {
	    	SA_PropertyWare.proratedRentDate = text.substring(text.indexOf(SA_PDFAppConfig_Format2.proratedRentDate_Prior)+SA_PDFAppConfig_Format2.proratedRentDate_Prior.length()+1,text.indexOf(SA_PDFAppConfig_Format2.proratedRentDate_After)).trim();
		    System.out.println("prorated Rent Date = "+SA_PropertyWare.proratedRentDate);//.substring(commensementDate.lastIndexOf(":")+1));
	    }
	    catch(Exception e)
	    {
	    	SA_PropertyWare.proratedRentDate = "Error";
	    	e.printStackTrace();
	    }
	    
	    try
	    {
	    	SA_PropertyWare.proratedRent = text.substring(text.indexOf(SA_PDFAppConfig_Format2.proratedRent_Prior)+SA_PDFAppConfig_Format2.proratedRent_Prior.length()).split(" ")[0].trim();
	    	if(SA_PropertyWare.proratedRent.matches(".*[a-zA-Z]+.*"))
		    {
		    	SA_PropertyWare.proratedRent = "Error";
		    }
	    }
	    catch(Exception e)
	    {
	    	SA_PropertyWare.proratedRent = "Error";
	    	e.printStackTrace();
	    }
	    System.out.println("prorated Rent = "+SA_PropertyWare.proratedRent);//.substring(commensementDate.lastIndexOf(":")+1));
	    
	    try
	    {
	    	SA_PropertyWare.monthlyRent = text.substring(text.indexOf(SA_PDFAppConfig_Format2.monthlyRent_Prior)+SA_PDFAppConfig_Format2.monthlyRent_Prior.length()).split(" ")[0].trim();
	    	if(!SA_PropertyWare.monthlyRent.contains("."))
	    		SA_PropertyWare.monthlyRent = text.substring(text.indexOf(SA_PDFAppConfig_Format2.monthlyRent_Prior2)+SA_PDFAppConfig_Format2.monthlyRent_Prior2.length()).split(" ")[0].trim();
	    	if(SA_PropertyWare.monthlyRent.matches(".*[a-zA-Z]+.*"))
		    {
		    	SA_PropertyWare.monthlyRent = "Error";
		    }
	    	if(SA_PropertyWare.monthlyRent.contains("*")||text.contains(SA_PDFAppConfig_Format2.monthlyRentAvailabilityCheck)==true)
	    	{
	    		SA_PropertyWare.incrementRentFlag = true;
	    		SA_PropertyWare.monthlyRent = SA_PropertyWare.monthlyRent.replace("*", "");
	    		System.out.println("Monthly Rent has Asterick *");
	    		
	    		//SA_PropertyWare.increasedRent_amount = text.substring(text.indexOf(". $")+". $".length()).trim().split(" ")[0];
	    		String increasedRent_ProviousRentEndDate = "Per the Landlord, Monthly Rent from "+SA_PropertyWare.commensementDate.trim()+" through ";
	    		 String endDateArray[] = text.substring(text.indexOf(". $")+". $".length()).split(" ");
	    		if(endDateArray[2].trim().length()==4)//&&RunnerClass.onlyDigits(endDateArray[2]))
	    		 {
	    		  SA_PropertyWare.increasedRent_previousRentEndDate = endDateArray[0]+" "+endDateArray[1]+" "+endDateArray[2];
	    		  System.out.println("Increased Rent - Previous rent end date = "+SA_PropertyWare.increasedRent_previousRentEndDate);
	    		 
	    		  String newRentStartDate[] = text.substring(text.indexOf(SA_PDFAppConfig_Format2.increasedRent_newStartDate_Prior)+SA_PDFAppConfig_Format2.increasedRent_newStartDate_Prior.length()).trim().split(" ");
	    		  SA_PropertyWare.increasedRent_newStartDate = newRentStartDate[0]+" "+newRentStartDate[1]+" "+newRentStartDate[2];
	    		  System.out.println("Increased Rent - New Rent Start date = "+SA_PropertyWare.increasedRent_newStartDate);
	    		  
	    		  String increasedRentRaw = text.substring(text.indexOf(SA_PDFAppConfig_Format2.increasedRent_newStartDate_Prior)+SA_PDFAppConfig_Format2.increasedRent_newStartDate_Prior.length()).trim();
	    		  SA_PropertyWare.increasedRent_amount = increasedRentRaw.substring(increasedRentRaw.indexOf("shall be $")+"shall be $".length()).trim().split(" ")[0];
	    		  System.out.println("Increased Rent - Amount = "+SA_PropertyWare.increasedRent_amount); 
	    		}
	    		else 
	    		 {
	    			 String adding0toMonth = "0"+SA_PropertyWare.commensementDate.trim().split(" ")[1];
	    			 String commeseDate = SA_PropertyWare.commensementDate.trim().replace(SA_PropertyWare.commensementDate.trim().split(" ")[1], adding0toMonth);
	    			 increasedRent_ProviousRentEndDate = "Per the Landlord, Monthly Rent from "+commeseDate+" through ";
		    		 String endDateArray2[] = text.substring(text.indexOf(increasedRent_ProviousRentEndDate)+increasedRent_ProviousRentEndDate.length()).split(" ");
		    		 if(endDateArray2[2].trim().length()==4)//&&RunnerClass.onlyDigits(endDateArray[2]))
		    		 {
		    		  SA_PropertyWare.increasedRent_previousRentEndDate = endDateArray2[0]+" "+endDateArray2[1]+" "+endDateArray2[2];
		    		  System.out.println("Increased Rent - Previous rent end date = "+SA_PropertyWare.increasedRent_previousRentEndDate);
		    		 
		    		  String newRentStartDate[] = text.substring(text.indexOf(SA_PDFAppConfig_Format2.increasedRent_newStartDate_Prior)+SA_PDFAppConfig_Format2.increasedRent_newStartDate_Prior.length()).trim().split(" ");
		    		  SA_PropertyWare.increasedRent_newStartDate = newRentStartDate[0]+" "+newRentStartDate[1]+" "+newRentStartDate[2];
		    		  System.out.println("Increased Rent - New Rent Start date = "+SA_PropertyWare.increasedRent_newStartDate);
		    		  
		    		  String increasedRentRaw = text.substring(text.indexOf(SA_PDFAppConfig_Format2.increasedRent_newStartDate_Prior)+SA_PDFAppConfig_Format2.increasedRent_newStartDate_Prior.length()).trim();
		    		  SA_PropertyWare.increasedRent_amount = increasedRentRaw.substring(increasedRentRaw.indexOf("shall be $")+"shall be $".length()).trim().split(" ")[0];
		    		  System.out.println("Increased Rent - Amount = "+SA_PropertyWare.increasedRent_amount); 
		    		 }
	    		 }
	    	}
	    }
	    catch(Exception e)
	    {
	    	SA_PropertyWare.monthlyRent = "Error";
	    	e.printStackTrace();
	    }
	    System.out.println("Monthly Rent = "+SA_PropertyWare.monthlyRent);//.substring(commensementDate.lastIndexOf(":")+1));
	   
	    try
	    {
	    	SA_PropertyWare.adminFee = text.substring(text.indexOf(SA_PDFAppConfig_Format2.adminFee_prior)+SA_PDFAppConfig_Format2.adminFee_prior.length()).split(" ")[0].trim();
	    	if(SA_PropertyWare.adminFee.matches(".*[a-zA-Z]+.*"))
		    {
		    	SA_PropertyWare.adminFee = "Error";
		    }
	    }
	    catch(Exception e)
	    {
	    	SA_PropertyWare.adminFee = "Error";
	    	e.printStackTrace();
	    }
	    System.out.println("Admin Fee = "+SA_PropertyWare.adminFee);//.substring(commensementDate.lastIndexOf(":")+1));
	  //Resident Benefits Package 
	    if(text.contains(SA_PDFAppConfig.residentBenefitsPackageAddendumCheck))
	    {
	    	SA_PropertyWare.residentBenefitsPackageAvailabilityCheck = true;
	    	 try
	 	    {
	 		    SA_PropertyWare.residentBenefitsPackage = text.substring(text.indexOf(SA_PDFAppConfig_Format2.AB1_residentBenefitsPackage_Prior)+SA_PDFAppConfig_Format2.AB1_residentBenefitsPackage_Prior.length()).split(" ")[0].replaceAll("[^0-9a-zA-Z.]", "");
	 		    if(SA_PropertyWare.residentBenefitsPackage.matches(".*[a-zA-Z]+.*"))
	 		    {
	 		    	SA_PropertyWare.residentBenefitsPackage = "Error";
	 		    }
	 	    }
	 	    catch(Exception e)
	 	    {
	 		    SA_PropertyWare.residentBenefitsPackage = "Error";
	 		    e.printStackTrace();
	 	    }
	    	 System.out.println("Resident Benefits Package  = "+SA_PropertyWare.residentBenefitsPackage.trim());
	    	//SA_PDFAppConfig.AB1_residentBenefitsPackage_Prior
	    }
	    else
	    {
		    if(text.contains(SA_PDFAppConfig_Format2.HVACFilterAddendumTextAvailabilityCheck)==true)
		    {
		    	SA_PropertyWare.HVACFilterFlag =true;
		    }
		    else
		    {
		    try
		    {
		    	SA_PropertyWare.airFilterFee = text.substring(text.indexOf(SA_PDFAppConfig_Format2.HVACAirFilter_prior)+SA_PDFAppConfig_Format2.HVACAirFilter_prior.length()).split(" ")[0].trim();
		    	if(SA_PropertyWare.airFilterFee.matches(".*[a-zA-Z]+.*"))
			    {
			    	SA_PropertyWare.airFilterFee = "Error";
			    }
		    }
		    catch(Exception e)
		    {
		    	SA_PropertyWare.airFilterFee = "Error";
		    	e.printStackTrace();
		    }
		    }
		    System.out.println("HVAC Air Filter Fee = "+SA_PropertyWare.airFilterFee);//.substring(commensementDate.lastIndexOf(":")+1));
	    }
	    try
	    {
	    	SA_PropertyWare.occupants = text.substring(text.indexOf(SA_PDFAppConfig_Format2.occupants_Prior)+SA_PDFAppConfig_Format2.occupants_Prior.length(),text.indexOf(SA_PDFAppConfig_Format2.occupants_After)).trim();
		    System.out.println("Occupants = "+SA_PropertyWare.occupants);//.substring(commensementDate.lastIndexOf(":")+1));
	    }
	    catch(Exception e)
	    {
	    	SA_PropertyWare.occupants = "Error";
	    	e.printStackTrace();
	    }
	    
	    //Late Fee Rule
	    SA_ExtractDataFromPDF_Format2.lateFeeRule();
	    
	    
	  //Prepayment Charge
	    try
	    {
		if(SA_PropertyWare.portfolioType.contains("MCH"))
		{
		try
		{
		SA_PropertyWare.prepaymentCharge =String.valueOf(Double.parseDouble(SA_PropertyWare.monthlyRent.replace(",", "")) - Double.parseDouble(SA_PropertyWare.proratedRent.replace(",", ""))); 
		}
		catch(Exception e)
		{
			SA_PropertyWare.prepaymentCharge ="Error";
		}
		System.out.println("Prepayment Charge = "+SA_PropertyWare.prepaymentCharge);
		}
	    }
	    catch(Exception e) {}
	    //Early Termination
		try
	    {
	    	String[] earlyTerminationRaw = text.substring(text.indexOf(SA_PDFAppConfig_Format2.earlyTermination_Prior)+SA_PDFAppConfig_Format2.earlyTermination_Prior.length()).split(" ");
	    	
		    SA_PropertyWare.earlyTermination = earlyTerminationRaw[0]+earlyTerminationRaw[1]; //text.substring(text.indexOf(SA_PDFAppConfig.AB_earlyTerminationFee_Prior)+SA_PDFAppConfig.AB_earlyTerminationFee_Prior.length(),text.indexOf(SA_PDFAppConfig.AB_earlyTerminationFee_After));
	    }
	    catch(Exception e)
	    {
	    	SA_PropertyWare.earlyTermination = "Error";	
	    	e.printStackTrace();
	    }
		System.out.println("Early Termination = "+SA_PropertyWare.earlyTermination);
	    // Checking Pet Addendum is available or not
	    
	    if(text.contains(SA_PDFAppConfig_Format2.petAgreementAvailabilityCheck)==true)
	    	petFlag =  true;
	    else if(petFlag = text.contains(SA_PDFAppConfig_Format2.petAgreementAvailabilityCheck2)==true)
	    	petFlag =  true;
	    else petFlag =  false;
	    
	    System.out.println("Pet Addendum Available = "+petFlag);
	    if(petFlag ==true)
	    {
	    	SA_PropertyWare.petFlag = true;
			    	try
			    	{
			    		 String proratedPetRaw = "Prorated Pet Rent: On or before "+SA_PropertyWare.commensementDate.trim()+" Tenant will pay Landlord $";
				    		SA_PropertyWare.proratedPetRent = text.substring(text.indexOf(proratedPetRaw)+proratedPetRaw.length()).trim().split(" ")[0].trim();//.replaceAll("[a-ZA-Z,]", "");
				    		if(SA_PropertyWare.proratedPetRent.matches(".*[a-zA-Z]+.*")||SA_PropertyWare.proratedPetRent.trim().equals("1."))
						    {
						    	SA_PropertyWare.proratedPetRent = "Error";
						    }
				    		//SA_PropertyWare.proratedPetRent = proratedPetRentRaw.substring(proratedPetRentRaw.indexOf("Tenant will pay Landlord $")+"Tenant will pay Landlord $".length());//,proratedPetRentRaw.indexOf(AppConfig.AR_proratedPetRent_After));
			        }
				    catch(Exception e)
				    {
				    SA_PropertyWare.proratedPetRent = "Error";	
				    e.printStackTrace();
				    }
	    	System.out.println("Prorated Pet rent= "+SA_PropertyWare.proratedPetRent.trim());
	    	try
    		{
    			SA_PropertyWare.petRent = text.substring(text.indexOf(SA_PDFAppConfig_Format2.petRent_Prior)+SA_PDFAppConfig_Format2.petRent_Prior.length()).split(" ")[0].trim();
				 //System.out.println("Pet rent = "+SA_PropertyWare.petRent.trim());
				 if(RunnerClass.onlyDigits(SA_PropertyWare.petRent)==false)
				    {
				    	 SA_PropertyWare.petRent = text.substring(text.indexOf(SA_PDFAppConfig_Format2.petRent_Prior2)+SA_PDFAppConfig_Format2.petRent_Prior2.length()).trim().split(" ")[0];
				    }
				 if(SA_PropertyWare.petRent.matches(".*[a-zA-Z]+.*"))
				    {
				    	SA_PropertyWare.petRent = "Error";
				    }
    		}
    		
    		catch(Exception e1)
		    {
		    	SA_PropertyWare.petRent = "Error";  
		    	e1.printStackTrace();
		    }
	    	System.out.println("Pet rent= "+SA_PropertyWare.petRent.trim());
	    	
	    	//Pet Security Deposit -- Need to find the text of Pet Security Deposit for the format PDF, until then, it is commented
	    	/*
	    	try
	    	{
	    	SA_PropertyWare.petSecurityDeposit = text.substring(text.indexOf(SA_PDFAppConfig_Format2.securityDeposit_Prior)+SA_PDFAppConfig_Format2.securityDeposit_Prior.length()).trim().split(" ")[0];//,text.indexOf(SA_PDFAppConfig_Format2.securityDeposit_After));
		    System.out.println("Security Deposit = "+SA_PropertyWare.securityDeposit.trim());
		    if(RunnerClass.onlyDigits(SA_PropertyWare.petSecurityDeposit)==true)
		    {
		    	SA_PropertyWare.petSecurityDepositFlag = true;
		    	System.out.println("Security Deposit is checked and has value");
		    }
	    	}
	    	catch(Exception e)
	    	{
	    	SA_PropertyWare.securityDeposit = "Error";	
	    	e.printStackTrace();
	    	}
	    	*/
	    	String typeSubString = "";
	    	try
	    	{
	    	typeSubString = text.substring(text.indexOf(SA_PDFAppConfig_Format2.typeWord_Prior)+SA_PDFAppConfig_Format2.typeWord_Prior.length(),text.indexOf(SA_PDFAppConfig_Format2.typeWord_After));
	    	}
	    	catch(Exception e)
	    	{
	    		try
	    		{
	    		typeSubString = text.substring(text.indexOf(SA_PDFAppConfig_Format2.typeWord_Prior)+SA_PDFAppConfig_Format2.typeWord_Prior.length(),text.indexOf(SA_PDFAppConfig_Format2.typeWord_After2));
	    		}
	    		catch(Exception e2)
	    		{
	    			try
	    			{
	    				typeSubString = text.substring(text.indexOf(SA_PDFAppConfig_Format2.typeWord_Prior2)+SA_PDFAppConfig_Format2.typeWord_Prior2.length(),text.indexOf(SA_PDFAppConfig_Format2.typeWord_After3));
	    			}
	    			catch(Exception e3)
	    			{
	    		    typeSubString = "";
	    			}
	    		}
	    	}
	    	 String newText = typeSubString.replace("Type:","");
			    SA_PropertyWare.countOfTypeWordInText = ((typeSubString.length() - newText.length())/"Type:".length());
			    System.out.println("Type: occurences = "+SA_PropertyWare.countOfTypeWordInText);
	    	
	    	SA_PropertyWare.petType = new ArrayList();
		    SA_PropertyWare.petBreed = new ArrayList();
		    SA_PropertyWare.petWeight = new ArrayList();
		    for(int i =0;i<SA_PropertyWare.countOfTypeWordInText;i++)
		    {
		    	String type = typeSubString.substring(RunnerClass.nthOccurrence(typeSubString, "Type:", i+1)+SA_PDFAppConfig.AB_pet1Type_Prior.length(),RunnerClass.nthOccurrence(typeSubString, "Breed:", i+1)).trim();
		    	if(type.contains("N/A")||type.contains("n/a"))
		    		break;
		    	System.out.println(type);
		    	SA_PropertyWare.petType.add(type);
		    	int pet1Breedindex1 = RunnerClass.nthOccurrence(typeSubString, "Breed:", i+1)+"Breed:".length()+1;
			    String subString = typeSubString.substring(pet1Breedindex1);
			    //int pet1Breedindex2 = RunnerClass.nthOccurrence(subString,"Name:",i+1);
			   // System.out.println("Index 2 = "+(index2+index1));
			    String breed = subString.split("Name:")[0].trim();//typeSubString.substring(pet1Breedindex1,(pet1Breedindex2+pet1Breedindex1));
			    System.out.println(breed);
			    SA_PropertyWare.petBreed.add(breed);
			    int pet1Weightindex1 = RunnerClass.nthOccurrence(typeSubString, "Weight:", i+1)+"Weight:".length()+1;
			    String pet1WeightSubstring = typeSubString.substring(pet1Weightindex1);
			    //int pet1WeightIndex2 = RunnerClass.nthOccurrence(pet1WeightSubstring,"Age:",i+1);
			   // System.out.println("Index 2 = "+(index2+index1));
			    String weight = pet1WeightSubstring.split("Age:")[0].trim(); //typeSubString.substring(pet1Weightindex1,(pet1WeightIndex2+pet1Weightindex1));
			    System.out.println(weight);
			    SA_PropertyWare.petWeight.add(weight);
		    }
		    
	    	
		    try
		    {
		    	SA_PropertyWare.petOneTimeNonRefundableFee = text.substring(text.indexOf(SA_PDFAppConfig_Format2.petOneTimeNonRefundable_Prior)+SA_PDFAppConfig_Format2.petOneTimeNonRefundable_Prior.length(),text.indexOf(SA_PDFAppConfig_Format2.petOneTimeNonRefundable_After)).trim();
		    	if(SA_PropertyWare.petOneTimeNonRefundableFee.matches(".*[a-zA-Z]+.*"))
			    {
			    	SA_PropertyWare.petOneTimeNonRefundableFee = "Error";
			    }
		    }
		    catch(Exception e)
		    {
		    	try
		    	{
		    		SA_PropertyWare.petOneTimeNonRefundableFee = text.substring(text.indexOf(SA_PDFAppConfig_Format2.petOneTimeNonRefundable_Prior2)+SA_PDFAppConfig_Format2.petOneTimeNonRefundable_Prior2.length()).trim().split(",")[0];
				    //System.out.println("pet one time non refundable = "+SA_PropertyWare.petOneTimeNonRefundableFee.trim());
		    		if(SA_PropertyWare.petOneTimeNonRefundableFee.matches(".*[a-zA-Z]+.*"))
				    {
				    	SA_PropertyWare.petOneTimeNonRefundableFee = "Error";
				    }
		    	}
		    	catch(Exception e2)
		    	{
		    	SA_PropertyWare.petOneTimeNonRefundableFee =  "Error";
		    	e2.printStackTrace();
		    	}
		    }  
		    System.out.println("pet one time non refundable = "+SA_PropertyWare.petOneTimeNonRefundableFee.trim());
		    
		  
		    
		    
	    }
	    
	  //Service Animal Addendum check
	    try
	    {
	    if(text.contains(SA_AppConfig.serviceAnimalText))
	    {
	    	SA_PropertyWare.serviceAnimalFlag = true;
    		System.out.println("Service Animal Addendum is available");
    		
            String typeSubString = text.substring(text.indexOf(SA_PDFAppConfig_Format2.AB_serviceAnimal_typeWord_Prior)+SA_PDFAppConfig_Format2.AB_serviceAnimal_typeWord_Prior.length(),text.indexOf(SA_PDFAppConfig_Format2.AB_serviceAnimal_typeWord_After));
	    	
	    	String newText = typeSubString.replace("Type:","");
		    int  countOftypeWords_ServiceAnimal = ((typeSubString.length() - newText.length())/"Type:".length());
		    System.out.println("Service Animal - Type: occurences = "+countOftypeWords_ServiceAnimal);
		    
		    SA_PropertyWare.serviceAnimalPetType = new ArrayList();
		    SA_PropertyWare.serviceAnimalPetBreed = new ArrayList();
		    SA_PropertyWare.serviceAnimalPetWeight = new ArrayList();
		    for(int i =0;i<countOftypeWords_ServiceAnimal;i++)
		    {
		    	String type = typeSubString.substring(RunnerClass.nthOccurrence(typeSubString, "Type:", i+1)+SA_PDFAppConfig.AB_pet1Type_Prior.length(),RunnerClass.nthOccurrence(typeSubString, "Breed:", i+1)).trim();
		    	if(type.contains("N/A")||type.contains("n/a"))
		    		break;
		    	System.out.println(type);
		    	SA_PropertyWare.serviceAnimalPetType.add(type);
		    	int pet1Breedindex1 = RunnerClass.nthOccurrence(typeSubString, "Breed:", i+1)+"Breed:".length()+1;
			    String subString = typeSubString.substring(pet1Breedindex1);
			    //int pet1Breedindex2 = RunnerClass.nthOccurrence(subString,"Name:",i+1);
			   // System.out.println("Index 2 = "+(index2+index1));
			    String breed = subString.split("Name:")[0].trim();//typeSubString.substring(pet1Breedindex1,(pet1Breedindex2+pet1Breedindex1));
			    System.out.println(breed);
			    SA_PropertyWare.serviceAnimalPetBreed.add(breed);
			    int pet1Weightindex1 = RunnerClass.nthOccurrence(typeSubString, "Weight:", i+1)+"Weight:".length()+1;
			    String pet1WeightSubstring = typeSubString.substring(pet1Weightindex1);
			    //int pet1WeightIndex2 = RunnerClass.nthOccurrence(pet1WeightSubstring,"Age:",i+1);
			   // System.out.println("Index 2 = "+(index2+index1));
			    String weight = pet1WeightSubstring.split("Age:")[0].trim(); //typeSubString.substring(pet1Weightindex1,(pet1WeightIndex2+pet1Weightindex1));
			    System.out.println(weight);
			    SA_PropertyWare.serviceAnimalPetWeight.add(weight);
		    }
    		
    		
	    }
	    }
	    catch(Exception e)
	    {
	    	SA_PropertyWare.serviceAnimalFlag = false;
	    }
	  //Concession Addendum
	    
	    try
	    {
	    	if(text.contains(SA_PDFAppConfig.concessionAddendumText))
	    	{
	    		SA_PropertyWare.concessionAddendumFlag = true;
	    		System.out.println("Concession Addendum is available");
	    	}
	    }
	    catch(Exception e)
	    {}
	    //document.close();
		return true;
	}
	
	public static boolean lateFeeRule()
	{
		String lateFeeRuleText ="";
		try
		{
		 lateFeeRuleText = text.substring(text.indexOf(SA_PDFAppConfig_Format2.lateFeeRuleText_Prior)+SA_PDFAppConfig_Format2.lateFeeRuleText_Prior.length(),text.indexOf(SA_PDFAppConfig_Format2.lateFeeRuleText_After));
		}
		catch(Exception e)
		{
			try
			{
			lateFeeRuleText = text.substring(text.indexOf(SA_PDFAppConfig_Format2.lateFeeRuleText_Prior)+SA_PDFAppConfig_Format2.lateFeeRuleText_Prior.length(),text.indexOf(SA_PDFAppConfig_Format2.lateFeeRuleText_After2));
			}
			catch(Exception e2)
			{
			return false;
			}
		}
		if(lateFeeRuleText.contains(SA_PDFAppConfig.lateFeeRule_whicheverIsGreater))
		{
			RunnerClass.lateFeeRuleType = "GreaterOfFlatFeeOrPercentage";
			RunnerClass.lateFeeType = "GreaterOfFlatFeeOrPercentage";
			//SA_PropertyWare.lateFeeType ="Greater of Flat Fee or Percentage"; 
		//Late charge day
			try
			{
		   // SA_PropertyWare.lateChargeDay =  lateFeeRuleText.substring(lateFeeRuleText.indexOf(SA_PDFAppConfig.lateFeeRule_whicheverIsGreater_dueDay_Prior)+SA_PDFAppConfig.lateFeeRule_whicheverIsGreater_dueDay_Prior.length()).trim().split(" ")[0];
				SA_PropertyWare.lateChargeDay =  lateFeeRuleText.split(SA_PDFAppConfig.lateFeeRule_whicheverIsGreater_dueDay_After)[0].trim();
				SA_PropertyWare.lateChargeDay =SA_PropertyWare.lateChargeDay.substring(SA_PropertyWare.lateChargeDay.lastIndexOf(" ")+1);
		    SA_PropertyWare.lateChargeDay =  SA_PropertyWare.lateChargeDay.replaceAll("[^0-9]", "");
			}
			catch(Exception e)
			{
				SA_PropertyWare.lateChargeDay = "Error";
			}
         System.out.println("Late Charge Day = "+SA_PropertyWare.lateChargeDay);
			RunnerClass.dueDay_GreaterOf = SA_PropertyWare.lateChargeDay;
		//Late Fee Percentage
			try
			{
		    SA_PropertyWare.lateFeePercentage =  lateFeeRuleText.substring(lateFeeRuleText.indexOf(SA_PDFAppConfig.lateFeeRule_whicheverIsGreater_lateFeePercentage)+SA_PDFAppConfig.lateFeeRule_whicheverIsGreater_lateFeePercentage.length()).trim().split(" ")[0];
		    SA_PropertyWare.lateFeePercentage = SA_PropertyWare.lateFeePercentage.replaceAll("[^0-9]", "");
			}
			catch(Exception e)
			{
				SA_PropertyWare.lateFeePercentage = "Error";
			}
         System.out.println("Late Fee Percentage = "+SA_PropertyWare.lateFeePercentage);
         RunnerClass.percentage = SA_PropertyWare.lateFeePercentage;
         //Late Fee Amount
         try
         {
         String lateFeeAmount  = lateFeeRuleText.substring(lateFeeRuleText.indexOf(SA_PDFAppConfig.lateFeeRule_whicheverIsGreater_lateFeeAmount)+SA_PDFAppConfig.lateFeeRule_whicheverIsGreater_lateFeeAmount.length()).trim().split(" ")[0];
         SA_PropertyWare.flatFeeAmount = lateFeeAmount.replaceAll("[^.0-9]", "");
         }
         catch(Exception e)
         {
        	 SA_PropertyWare.flatFeeAmount ="Error";
         }
         System.out.println("Late Fee Amount = "+SA_PropertyWare.flatFeeAmount);
        RunnerClass.flatFee = SA_PropertyWare.flatFeeAmount;
         return true;
		}
		else 
		if(lateFeeRuleText.contains(SA_PDFAppConfig.lateFeeRule_mayNotExceedMoreThan30Days))
		{
			RunnerClass.lateFeeRuleType = "initialFeePluPerDayFee";
			//RunnerClass.lateFeeRuleType = "Initial Fee + Per Day Fee";
			
			SA_PropertyWare.lateFeeType ="initialFeePluPerDayFee"; 
	         try
	 	    {
	 		    SA_PropertyWare.lateChargeFee = text.substring(text.indexOf(SA_PDFAppConfig.AB_lateFee_Prior)+SA_PDFAppConfig.AB_lateFee_Prior.length()).trim().split(" ")[0];
	 		    //SA_PropertyWare.lateChargeFee =  SA_PropertyWare.lateChargeFee.substring(0,SA_PropertyWare.lateChargeFee.length()-1);
	 	    }
	 	    catch(Exception e)
	 	    {
	 		    SA_PropertyWare.lateChargeFee ="Error";	
	 		    e.printStackTrace();
	 	    }
	 	    System.out.println("Late Charge Fee = "+SA_PropertyWare.lateChargeFee.trim());
	 	   RunnerClass.initialFeeAmount = SA_PropertyWare.lateChargeFee;
	 	    //Per Day Fee
	 	    try
	 	    {
	 	    	SA_PropertyWare.lateFeeChargePerDay = text.substring(text.indexOf(SA_PDFAppConfig.AB_additionalLateChargesPerDay_Prior)+SA_PDFAppConfig.AB_additionalLateChargesPerDay_Prior.length()).split(" ")[0].trim();//,text.indexOf(SA_PDFAppConfig.AB_additionalLateChargesPerDay_After));
	 	    }
	 	    catch(Exception e)
	 	    {
	 	    	SA_PropertyWare.lateFeeChargePerDay =  "Error";	
	 	    	e.printStackTrace();
	 	    }
	 	    System.out.println("Per Day Fee = "+SA_PropertyWare.lateFeeChargePerDay.trim());
	 	    RunnerClass.perDayFeeAmount = SA_PropertyWare.lateFeeChargePerDay;
	 	    //Additional Late Charges Limit
	 	    try
	 	    {
	 	    	SA_PropertyWare.additionalLateChargesLimit = text.substring(text.indexOf(SA_PDFAppConfig.AB_additionalLateChargesLimit_Prior)+SA_PDFAppConfig.AB_additionalLateChargesLimit_Prior.length()).trim().split(" ")[0]; //,text.indexOf(SA_PDFAppConfig.AB_additionalLateChargesLimit_After));
	 	    }
	 	    catch(Exception e)
	 	    {
	 	    	SA_PropertyWare.additionalLateChargesLimit =  "Error";	
	 	    	e.printStackTrace();
	 	    }
	 	    System.out.println("additional Late Charges Limit = "+SA_PropertyWare.additionalLateChargesLimit.trim());
	 	    RunnerClass.additionalLateChargesLimit = SA_PropertyWare.additionalLateChargesLimit;
	 	 //Late Charge Day
			try
	 	    {
			SA_PropertyWare.lateChargeDay = lateFeeRuleText.substring(lateFeeRuleText.indexOf("p.m. on the ")+"p.m. on the ".length()).trim().split(" ")[0];
			SA_PropertyWare.lateChargeDay = SA_PropertyWare.lateChargeDay.replaceAll("[^0-9]", "");
	 	    }
			catch(Exception e)
	 	    {
	 	    	SA_PropertyWare.lateChargeDay =  "Error";	
	 	    	e.printStackTrace();
	 	    }
	 	    System.out.println("Late Charge Due Day = "+SA_PropertyWare.lateChargeDay.trim());
	 	    RunnerClass.dueDay_initialFee = SA_PropertyWare.lateChargeDay;
	 	   return true;
		}
		else if(lateFeeRuleText.contains(SA_PDFAppConfig.lateFeeRule_mayNotExceedAmount))
			{
			RunnerClass.lateFeeRuleType = "initialFeePluPerDayFee";
			RunnerClass.lateFeeRuleType = "Initial Fee + Per Day Fee";
			
			//Late Charge Day
			try
	 	    {
			SA_PropertyWare.lateChargeDay = lateFeeRuleText.substring(lateFeeRuleText.indexOf("an initial late charge on the")+"an initial late charge on the".length()).trim().split(" ")[0];
			SA_PropertyWare.lateChargeDay = SA_PropertyWare.lateChargeDay.replaceAll("[^0-9]", "");
	 	    }
			catch(Exception e)
	 	    {
	 	    	SA_PropertyWare.lateChargeDay =  "Error";	
	 	    	e.printStackTrace();
	 	    }
	 	    System.out.println("Late Charge Due Day = "+SA_PropertyWare.lateChargeDay.trim());
	 	   RunnerClass.dueDay_initialFee = SA_PropertyWare.lateChargeDay;
	 	    // initial Late Charge
	 	   try
	 	    {
			SA_PropertyWare.lateChargeFee = lateFeeRuleText.substring(lateFeeRuleText.indexOf("day of the month equal to $")+"day of the month equal to $".length()).trim().split(" ")[0];
			SA_PropertyWare.lateChargeFee = SA_PropertyWare.lateChargeFee.replaceAll("[^0-9.]", "");
	 	    }
			catch(Exception e)
	 	    {
	 	    	SA_PropertyWare.lateChargeFee =  "Error";	
	 	    	e.printStackTrace();
	 	    }
	 	    System.out.println("Late Charge Fee = "+SA_PropertyWare.lateChargeFee.trim());
	 	   RunnerClass.initialFeeAmount = SA_PropertyWare.lateChargeFee;
	 	    // Additional Late Charges
	 	   try
	 	    {
			SA_PropertyWare.additionalLateCharges = lateFeeRuleText.substring(lateFeeRuleText.indexOf("and additional late charge of $")+"and additional late charge of $".length()).trim().split(" ")[0];
			SA_PropertyWare.additionalLateCharges = SA_PropertyWare.additionalLateCharges.replaceAll("[^0-9.]", "");
	 	    }
			catch(Exception e)
	 	    {
	 	    	SA_PropertyWare.additionalLateCharges =  "Error";	
	 	    	e.printStackTrace();
	 	    }
	 	    System.out.println("Additional Late Charges = "+SA_PropertyWare.additionalLateCharges.trim());
	 	   RunnerClass.maximumAmount = SA_PropertyWare.additionalLateCharges;
	 	    //Additional Late Charges Limit
	 	   try
	 	    {
			SA_PropertyWare.additionalLateChargesLimit = lateFeeRuleText.substring(lateFeeRuleText.indexOf("(initial and additional) may not exceed $")+"(initial and additional) may not exceed $".length()).trim().split(" ")[0];
			SA_PropertyWare.additionalLateChargesLimit = SA_PropertyWare.additionalLateChargesLimit.replaceAll("[^0-9.]", "");
	 	    }
			catch(Exception e)
	 	    {
	 	    	SA_PropertyWare.additionalLateChargesLimit =  "Error";	
	 	    	e.printStackTrace();
	 	    }
	 	    System.out.println("Additional Late Charges Limit = "+SA_PropertyWare.additionalLateChargesLimit.trim());
	 	   RunnerClass.additionalLateChargesLimit = SA_PropertyWare.additionalLateChargesLimit;
			return true;
			}
		else 
			if(lateFeeRuleText.contains(SA_PDFAppConfig.lateFeeRule_totalDelinquentRentDueToTheTenantAccount))
			{
				RunnerClass.lateFeeRuleType = "GreaterOfFlatFeeOrPercentage";
				RunnerClass.lateFeeType = "GreaterOfFlatFeeOrPercentage";
				
			//Late Charge Day
			try
	 	    {
			SA_PropertyWare.lateChargeDay = lateFeeRuleText.substring(lateFeeRuleText.indexOf("11:59 PM of the ")+"11:59 PM of the ".length()).trim().split(" ")[0];
			SA_PropertyWare.lateChargeDay = SA_PropertyWare.lateChargeDay.replaceAll("[^0-9]", "");
	 	    }
			catch(Exception e)
	 	    {
	 	    	SA_PropertyWare.lateChargeDay =  "Error";	
	 	    	e.printStackTrace();
	 	    }
	 	    System.out.println("Late Charge Due Day = "+SA_PropertyWare.lateChargeDay.trim());
	 	   RunnerClass.dueDay_GreaterOf = SA_PropertyWare.lateChargeDay;
	 	    // initial Late Charge
	 	   try
	 	    {
			SA_PropertyWare.lateChargeFee = lateFeeRuleText.substring(lateFeeRuleText.indexOf("an initial late charge equal to ")+"an initial late charge equal to ".length()).trim().split(" ")[0];
			//SA_PropertyWare.lateChargeFee = SA_PropertyWare.lateChargeFee.replaceAll("[^0-9.]", "");
	 	    }
			catch(Exception e)
	 	    {
	 	    	SA_PropertyWare.lateChargeFee =  "Error";	
	 	    	e.printStackTrace();
	 	    }
	 	    System.out.println("Late Charge Fee = "+SA_PropertyWare.lateChargeFee.trim());
	 	   RunnerClass.percentage = SA_PropertyWare.lateChargeFee;
	 	   /*
	 	    // Additional Late Charges
	 	   try
	 	    {
			SA_PropertyWare.additionalLateCharges = lateFeeRuleText.substring(lateFeeRuleText.indexOf("and additional late charge of $")+"and additional late charge of $".length()).trim().split(" ")[0];
			SA_PropertyWare.additionalLateCharges = SA_PropertyWare.additionalLateCharges.replaceAll("[^0-9.]", "");
	 	    }
			catch(Exception e)
	 	    {
	 	    	SA_PropertyWare.additionalLateCharges =  "Error";	
	 	    	e.printStackTrace();
	 	    }
	 	    System.out.println("Additional Late Charges = "+SA_PropertyWare.additionalLateCharges.trim());
	 	    RunnerClass.maximumAmount = SA_PropertyWare.additionalLateCharges;
	 	    //Additional Late Charges Limit
	 	   try
	 	    {
			SA_PropertyWare.additionalLateChargesLimit = lateFeeRuleText.substring(lateFeeRuleText.indexOf("(initial and additional) may not exceed $")+"(initial and additional) may not exceed $".length()).trim().split(" ")[0];
			SA_PropertyWare.additionalLateChargesLimit = SA_PropertyWare.additionalLateChargesLimit.replaceAll("[^0-9.]", "");
	 	    }
			catch(Exception e)
	 	    {
	 	    	SA_PropertyWare.additionalLateChargesLimit =  "Error";	
	 	    	e.printStackTrace();
	 	    }
	 	    System.out.println("Additional Late Charges Limit = "+SA_PropertyWare.additionalLateChargesLimit.trim());
	 	    RunnerClass.additionalLateChargesLimit = SA_PropertyWare.additionalLateChargesLimit;
			return true;
			}
			else
		   {
			SA_PropertyWare.lateFeeType ="";
			return false;
	 	    */
		   }
		return true;
		
	}
		
		
		/*
		//Late Charge Day
		try
	    {
	    	SA_PropertyWare.lateChargeDay = text.substring(text.indexOf(SA_PDFAppConfig_Format2.lateFeeDay_Prior)+SA_PDFAppConfig_Format2.lateFeeDay_Prior.length()).trim().split(" ")[0].trim().replace("[^0-9]", "");
	    	SA_PropertyWare.lateChargeDay = SA_PropertyWare.lateChargeDay.replaceAll("[^\\d]", "");
	    	System.out.println("Late Charge Day = "+SA_PropertyWare.lateChargeDay);//.substring(commensementDate.lastIndexOf(":")+1));
	    }
	    catch(Exception e)
	    {
	    	SA_PropertyWare.lateChargeDay = "Error";
	    	e.printStackTrace();
	    }
	    
	    //Late Charge Fee
	    try
	    {
	    	SA_PropertyWare.lateChargeFee = text.substring(text.indexOf(SA_PDFAppConfig_Format2.lateChargePerDayFee)+SA_PDFAppConfig_Format2.lateChargePerDayFee.length()).trim().split(" ")[0].trim();
	    	//SA_PropertyWare.lateChargeFee = SA_PropertyWare.lateChargeFee.replaceAll("[^.0-9]", "");
	    }
	    catch(Exception e)
	    {
	    	SA_PropertyWare.lateChargeFee = "Error";
	    	e.printStackTrace();
	    }
	    System.out.println("Late Fee = "+SA_PropertyWare.lateChargeFee);//.substring(commensementDate.lastIndexOf(":")+1));
	    /*
	    //Per Day Fee
	    try
	    {
	    	SA_PropertyWare.additionalLateCharges = text.substring(text.indexOf(SA_PDFAppConfig_Format2.additionaLateCharge_Prior)+SA_PDFAppConfig_Format2.additionaLateCharge_Prior.length()).trim().split(" ")[0].trim();
	    	SA_PropertyWare.additionalLateCharges = SA_PropertyWare.additionalLateCharges.replaceAll("[^.0-9]", "");
	    }
	    catch(Exception e)
	    {
	    	SA_PropertyWare.additionalLateCharges = "Error";
	    	e.printStackTrace();
	    }
	    System.out.println("Additional Late Charges  = "+SA_PropertyWare.additionalLateCharges);//.substring(commensementDate.lastIndexOf(":")+1));
	    
	    try
	    {
	    if(SA_RunnerClass.pdfFormatType.equalsIgnoreCase("Format1"))
	    {
	    	SA_PropertyWare.additionalLateChargesLimit = text.substring(text.indexOf(SA_PDFAppConfig_Format2.additionaLateCharge_Prior)+SA_PDFAppConfig_Format2.additionaLateCharge_Prior.length()).split(" ")[0].trim();
		    System.out.println("Additional Late Charges  = "+SA_PropertyWare.additionalLateChargesLimit);//.substring(commensementDate.lastIndexOf(":")+1));
	    }
	    }
	    catch(Exception e)
	    {
	    	SA_PropertyWare.additionalLateChargesLimit = "Error";
	    	e.printStackTrace();
	    }
	    */
	


}
