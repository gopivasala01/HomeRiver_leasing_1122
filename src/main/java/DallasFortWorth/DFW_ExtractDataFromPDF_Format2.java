package DallasFortWorth;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import mainPackage.RunnerClass;

public class DFW_ExtractDataFromPDF_Format2 
{
	public static boolean petFlag;
	public static String text ="";
	public boolean arizona() throws Exception
	//public static void main(String[] args) throws Exception 
	{
		DFW_PropertyWare.petFlag = false;
		//File file = new File("C:\\Gopi\\Projects\\Property ware\\Lease Close Outs\\PDFS\\Tennessee\\Format 2\\Lease_031.22_05.23_1327_Everwood_Dr_Ashland_C_(1).pdf");
		File file = RunnerClass.getLastModified();
		FileInputStream fis = new FileInputStream(file);
		DFW_RunnerClass.document = PDDocument.load(fis);
	    text = new PDFTextStripper().getText(DFW_RunnerClass.document);
	    text = text.replaceAll(System.lineSeparator(), " ");
	    text = text.trim().replaceAll(" +", " ");
	    System.out.println(text);
	    System.out.println("------------------------------------------------------------------");
	    
	    try
	    {
	    	String commensementRaw = text.substring(text.indexOf(DFW_PDFAppConfig_Format2.commensementDate_Prior)+DFW_PDFAppConfig_Format2.commensementDate_Prior.length()+1).trim();//,text.indexOf(DFW_PDFAppConfig_Format2.commensementDate_After)).trim();
	    	 DFW_PropertyWare.commensementDate = commensementRaw.substring(0, commensementRaw.indexOf('(')).trim();
	    	 DFW_PropertyWare.commensementDate = DFW_PropertyWare.commensementDate.trim().replaceAll(" +", " ");
		    System.out.println("Commensement Date = "+DFW_PropertyWare.commensementDate);//.substring(commensementDate.lastIndexOf(":")+1));
	    }
	    catch(Exception e)
	    {
	    	DFW_PropertyWare.commensementDate = "Error";
	    	e.printStackTrace();
	    }
	    
	    try
	    {
	    	String expirationDateRaw  = text.substring(text.indexOf(DFW_PDFAppConfig_Format2.expirationDate_Prior)+DFW_PDFAppConfig_Format2.expirationDate_Prior.length()).trim();//,text.indexOf(DFW_PDFAppConfig_Format2.expirationDate_After)).trim();
	    	DFW_PropertyWare.expirationDate = expirationDateRaw.substring(0,expirationDateRaw.indexOf('(')).trim();
	    	DFW_PropertyWare.expirationDate = DFW_PropertyWare.expirationDate.trim().replaceAll(" +", " ");
	    	System.out.println("Expiration Date = "+DFW_PropertyWare.expirationDate);//.substring(commensementDate.lastIndexOf(":")+1));
	    }
	    catch(Exception e)
	    {
	    	DFW_PropertyWare.expirationDate = "Error";
	    	e.printStackTrace();
	    }
	    
	    try
	    {
	    	DFW_PropertyWare.proratedRentDate = text.substring(text.indexOf(DFW_PDFAppConfig_Format2.proratedRentDate_Prior)+DFW_PDFAppConfig_Format2.proratedRentDate_Prior.length()+1,text.indexOf(DFW_PDFAppConfig_Format2.proratedRentDate_After)).trim();
		    System.out.println("prorated Rent Date = "+DFW_PropertyWare.proratedRentDate);//.substring(commensementDate.lastIndexOf(":")+1));
	    }
	    catch(Exception e)
	    {
	    	DFW_PropertyWare.proratedRentDate = "Error";
	    	e.printStackTrace();
	    }
	    
	    try
	    {
	    	DFW_PropertyWare.proratedRent = text.substring(text.indexOf(DFW_PDFAppConfig_Format2.proratedRent_Prior)+DFW_PDFAppConfig_Format2.proratedRent_Prior.length()).split(" ")[0].trim();
	    	if(DFW_PropertyWare.proratedRent.matches(".*[a-zA-Z]+.*"))
		    {
		    	DFW_PropertyWare.proratedRent = "Error";
		    }
	    }
	    catch(Exception e)
	    {
	    	DFW_PropertyWare.proratedRent = "Error";
	    	e.printStackTrace();
	    }
	    System.out.println("prorated Rent = "+DFW_PropertyWare.proratedRent);//.substring(commensementDate.lastIndexOf(":")+1));
	    
	    try
	    {
	    	DFW_PropertyWare.monthlyRent = text.substring(text.indexOf(DFW_PDFAppConfig_Format2.monthlyRent_Prior)+DFW_PDFAppConfig_Format2.monthlyRent_Prior.length()).split(" ")[0].trim();
	    	if(!DFW_PropertyWare.monthlyRent.contains("."))
	    		DFW_PropertyWare.monthlyRent = text.substring(text.indexOf(DFW_PDFAppConfig_Format2.monthlyRent_Prior2)+DFW_PDFAppConfig_Format2.monthlyRent_Prior2.length()).split(" ")[0].trim();
	    	if(DFW_PropertyWare.monthlyRent.matches(".*[a-zA-Z]+.*"))
		    {
		    	DFW_PropertyWare.monthlyRent = "Error";
		    }
	    	if(DFW_PropertyWare.monthlyRent.contains("*")||text.contains(DFW_PDFAppConfig_Format2.monthlyRentAvailabilityCheck)==true)
	    	{
	    		DFW_PropertyWare.incrementRentFlag = true;
	    		DFW_PropertyWare.monthlyRent = DFW_PropertyWare.monthlyRent.replace("*", "");
	    		System.out.println("Monthly Rent has Asterick *");
	    		
	    		//DFW_PropertyWare.increasedRent_amount = text.substring(text.indexOf(". $")+". $".length()).trim().split(" ")[0];
	    		String increasedRent_ProviousRentEndDate = "Per the Landlord, Monthly Rent from "+DFW_PropertyWare.commensementDate.trim()+" through ";
	    		 String endDateArray[] = text.substring(text.indexOf(". $")+". $".length()).split(" ");
	    		if(endDateArray[2].trim().length()==4)//&&RunnerClass.onlyDigits(endDateArray[2]))
	    		 {
	    		  DFW_PropertyWare.increasedRent_previousRentEndDate = endDateArray[0]+" "+endDateArray[1]+" "+endDateArray[2];
	    		  System.out.println("Increased Rent - Previous rent end date = "+DFW_PropertyWare.increasedRent_previousRentEndDate);
	    		 
	    		  String newRentStartDate[] = text.substring(text.indexOf(DFW_PDFAppConfig_Format2.increasedRent_newStartDate_Prior)+DFW_PDFAppConfig_Format2.increasedRent_newStartDate_Prior.length()).trim().split(" ");
	    		  DFW_PropertyWare.increasedRent_newStartDate = newRentStartDate[0]+" "+newRentStartDate[1]+" "+newRentStartDate[2];
	    		  System.out.println("Increased Rent - New Rent Start date = "+DFW_PropertyWare.increasedRent_newStartDate);
	    		  
	    		  String increasedRentRaw = text.substring(text.indexOf(DFW_PDFAppConfig_Format2.increasedRent_newStartDate_Prior)+DFW_PDFAppConfig_Format2.increasedRent_newStartDate_Prior.length()).trim();
	    		  DFW_PropertyWare.increasedRent_amount = increasedRentRaw.substring(increasedRentRaw.indexOf("shall be $")+"shall be $".length()).trim().split(" ")[0];
	    		  System.out.println("Increased Rent - Amount = "+DFW_PropertyWare.increasedRent_amount); 
	    		}
	    		else 
	    		 {
	    			 String adding0toMonth = "0"+DFW_PropertyWare.commensementDate.trim().split(" ")[1];
	    			 String commeseDate = DFW_PropertyWare.commensementDate.trim().replace(DFW_PropertyWare.commensementDate.trim().split(" ")[1], adding0toMonth);
	    			 increasedRent_ProviousRentEndDate = "Per the Landlord, Monthly Rent from "+commeseDate+" through ";
		    		 String endDateArray2[] = text.substring(text.indexOf(increasedRent_ProviousRentEndDate)+increasedRent_ProviousRentEndDate.length()).split(" ");
		    		 if(endDateArray2[2].trim().length()==4)//&&RunnerClass.onlyDigits(endDateArray[2]))
		    		 {
		    		  DFW_PropertyWare.increasedRent_previousRentEndDate = endDateArray2[0]+" "+endDateArray2[1]+" "+endDateArray2[2];
		    		  System.out.println("Increased Rent - Previous rent end date = "+DFW_PropertyWare.increasedRent_previousRentEndDate);
		    		 
		    		  String newRentStartDate[] = text.substring(text.indexOf(DFW_PDFAppConfig_Format2.increasedRent_newStartDate_Prior)+DFW_PDFAppConfig_Format2.increasedRent_newStartDate_Prior.length()).trim().split(" ");
		    		  DFW_PropertyWare.increasedRent_newStartDate = newRentStartDate[0]+" "+newRentStartDate[1]+" "+newRentStartDate[2];
		    		  System.out.println("Increased Rent - New Rent Start date = "+DFW_PropertyWare.increasedRent_newStartDate);
		    		  
		    		  String increasedRentRaw = text.substring(text.indexOf(DFW_PDFAppConfig_Format2.increasedRent_newStartDate_Prior)+DFW_PDFAppConfig_Format2.increasedRent_newStartDate_Prior.length()).trim();
		    		  DFW_PropertyWare.increasedRent_amount = increasedRentRaw.substring(increasedRentRaw.indexOf("shall be $")+"shall be $".length()).trim().split(" ")[0];
		    		  System.out.println("Increased Rent - Amount = "+DFW_PropertyWare.increasedRent_amount); 
		    		 }
	    		 }
	    	}
	    }
	    catch(Exception e)
	    {
	    	DFW_PropertyWare.monthlyRent = "Error";
	    	e.printStackTrace();
	    }
	    System.out.println("Monthly Rent = "+DFW_PropertyWare.monthlyRent);//.substring(commensementDate.lastIndexOf(":")+1));
	   
	    try
	    {
	    	DFW_PropertyWare.adminFee = text.substring(text.indexOf(DFW_PDFAppConfig_Format2.adminFee_prior)+DFW_PDFAppConfig_Format2.adminFee_prior.length()).split(" ")[0].trim();
	    	if(DFW_PropertyWare.adminFee.matches(".*[a-zA-Z]+.*"))
		    {
		    	DFW_PropertyWare.adminFee = "Error";
		    }
	    }
	    catch(Exception e)
	    {
	    	DFW_PropertyWare.adminFee = "Error";
	    	e.printStackTrace();
	    }
	    System.out.println("Admin Fee = "+DFW_PropertyWare.adminFee);//.substring(commensementDate.lastIndexOf(":")+1));
	  //Resident Benefits Package 
	    if(text.contains(DFW_PDFAppConfig.residentBenefitsPackageAddendumCheck))
	    {
	    	DFW_PropertyWare.residentBenefitsPackageAvailabilityCheck = true;
	    	 try
	 	    {
	 		    DFW_PropertyWare.residentBenefitsPackage = text.substring(text.indexOf(DFW_PDFAppConfig_Format2.AB1_residentBenefitsPackage_Prior)+DFW_PDFAppConfig_Format2.AB1_residentBenefitsPackage_Prior.length()).split(" ")[0].replaceAll("[^0-9a-zA-Z.]", "");
	 		    if(DFW_PropertyWare.residentBenefitsPackage.matches(".*[a-zA-Z]+.*"))
	 		    {
	 		    	DFW_PropertyWare.residentBenefitsPackage = "Error";
	 		    }
	 	    }
	 	    catch(Exception e)
	 	    {
	 		    DFW_PropertyWare.residentBenefitsPackage = "Error";
	 		    e.printStackTrace();
	 	    }
	    	 System.out.println("Resident Benefits Package  = "+DFW_PropertyWare.residentBenefitsPackage.trim());
	    	//DFW_PDFAppConfig.AB1_residentBenefitsPackage_Prior
	    }
	    else
	    {
		    if(text.contains(DFW_PDFAppConfig_Format2.HVACFilterAddendumTextAvailabilityCheck)==true)
		    {
		    	DFW_PropertyWare.HVACFilterFlag =true;
		    }
		    else
		    {
		    try
		    {
		    	DFW_PropertyWare.airFilterFee = text.substring(text.indexOf(DFW_PDFAppConfig_Format2.HVACAirFilter_prior)+DFW_PDFAppConfig_Format2.HVACAirFilter_prior.length()).split(" ")[0].trim();
		    	if(DFW_PropertyWare.airFilterFee.matches(".*[a-zA-Z]+.*"))
			    {
			    	DFW_PropertyWare.airFilterFee = "Error";
			    }
		    }
		    catch(Exception e)
		    {
		    	DFW_PropertyWare.airFilterFee = "Error";
		    	e.printStackTrace();
		    }
		    }
		    System.out.println("HVAC Air Filter Fee = "+DFW_PropertyWare.airFilterFee);//.substring(commensementDate.lastIndexOf(":")+1));
	    }
	    try
	    {
	    	DFW_PropertyWare.occupants = text.substring(text.indexOf(DFW_PDFAppConfig_Format2.occupants_Prior)+DFW_PDFAppConfig_Format2.occupants_Prior.length(),text.indexOf(DFW_PDFAppConfig_Format2.occupants_After)).trim();
		    System.out.println("Occupants = "+DFW_PropertyWare.occupants);//.substring(commensementDate.lastIndexOf(":")+1));
	    }
	    catch(Exception e)
	    {
	    	DFW_PropertyWare.occupants = "Error";
	    	e.printStackTrace();
	    }
	    
	    //Late Fee Rule
	    DFW_ExtractDataFromPDF_Format2.lateFeeRule();
	    
	    
	  //Prepayment Charge
	    try
	    {
		if(DFW_PropertyWare.portfolioType.contains("MCH"))
		{
		try
		{
		DFW_PropertyWare.prepaymentCharge =String.valueOf(Double.parseDouble(DFW_PropertyWare.monthlyRent.replace(",", "")) - Double.parseDouble(DFW_PropertyWare.proratedRent.replace(",", ""))); 
		}
		catch(Exception e)
		{
			DFW_PropertyWare.prepaymentCharge ="Error";
		}
		System.out.println("Prepayment Charge = "+DFW_PropertyWare.prepaymentCharge);
		}
	    }
	    catch(Exception e) {}
	    //Early Termination
		try
	    {
	    	String[] earlyTerminationRaw = text.substring(text.indexOf(DFW_PDFAppConfig_Format2.earlyTermination_Prior)+DFW_PDFAppConfig_Format2.earlyTermination_Prior.length()).split(" ");
	    	
		    DFW_PropertyWare.earlyTermination = earlyTerminationRaw[0]+earlyTerminationRaw[1]; //text.substring(text.indexOf(DFW_PDFAppConfig.AB_earlyTerminationFee_Prior)+DFW_PDFAppConfig.AB_earlyTerminationFee_Prior.length(),text.indexOf(DFW_PDFAppConfig.AB_earlyTerminationFee_After));
	    }
	    catch(Exception e)
	    {
	    	DFW_PropertyWare.earlyTermination = "Error";	
	    	e.printStackTrace();
	    }
		System.out.println("Early Termination = "+DFW_PropertyWare.earlyTermination);
	    // Checking Pet Addendum is available or not
	    
	    if(text.contains(DFW_PDFAppConfig_Format2.petAgreementAvailabilityCheck)==true)
	    	petFlag =  true;
	    else if(petFlag = text.contains(DFW_PDFAppConfig_Format2.petAgreementAvailabilityCheck2)==true)
	    	petFlag =  true;
	    else petFlag =  false;
	    
	    System.out.println("Pet Addendum Available = "+petFlag);
	    if(petFlag ==true)
	    {
	    	DFW_PropertyWare.petFlag = true;
			    	try
			    	{
			    		 String proratedPetRaw = "Prorated Pet Rent: On or before "+DFW_PropertyWare.commensementDate.trim()+" Tenant will pay Landlord $";
				    		DFW_PropertyWare.proratedPetRent = text.substring(text.indexOf(proratedPetRaw)+proratedPetRaw.length()).trim().split(" ")[0].trim();//.replaceAll("[a-ZA-Z,]", "");
				    		if(DFW_PropertyWare.proratedPetRent.matches(".*[a-zA-Z]+.*")||DFW_PropertyWare.proratedPetRent.trim().equals("1."))
						    {
						    	DFW_PropertyWare.proratedPetRent = "Error";
						    }
				    		//DFW_PropertyWare.proratedPetRent = proratedPetRentRaw.substring(proratedPetRentRaw.indexOf("Tenant will pay Landlord $")+"Tenant will pay Landlord $".length());//,proratedPetRentRaw.indexOf(AppConfig.AR_proratedPetRent_After));
			        }
				    catch(Exception e)
				    {
				    DFW_PropertyWare.proratedPetRent = "Error";	
				    e.printStackTrace();
				    }
	    	System.out.println("Prorated Pet rent= "+DFW_PropertyWare.proratedPetRent.trim());
	    	try
    		{
    			DFW_PropertyWare.petRent = text.substring(text.indexOf(DFW_PDFAppConfig_Format2.petRent_Prior)+DFW_PDFAppConfig_Format2.petRent_Prior.length()).split(" ")[0].trim();
				 //System.out.println("Pet rent = "+DFW_PropertyWare.petRent.trim());
				 if(RunnerClass.onlyDigits(DFW_PropertyWare.petRent)==false)
				    {
				    	 DFW_PropertyWare.petRent = text.substring(text.indexOf(DFW_PDFAppConfig_Format2.petRent_Prior2)+DFW_PDFAppConfig_Format2.petRent_Prior2.length()).trim().split(" ")[0];
				    }
				 if(DFW_PropertyWare.petRent.matches(".*[a-zA-Z]+.*"))
				    {
				    	DFW_PropertyWare.petRent = "Error";
				    }
    		}
    		
    		catch(Exception e1)
		    {
		    	DFW_PropertyWare.petRent = "Error";  
		    	e1.printStackTrace();
		    }
	    	System.out.println("Pet rent= "+DFW_PropertyWare.petRent.trim());
	    	
	    	//Pet Security Deposit -- Need to find the text of Pet Security Deposit for the format PDF, until then, it is commented
	    	/*
	    	try
	    	{
	    	DFW_PropertyWare.petSecurityDeposit = text.substring(text.indexOf(DFW_PDFAppConfig_Format2.securityDeposit_Prior)+DFW_PDFAppConfig_Format2.securityDeposit_Prior.length()).trim().split(" ")[0];//,text.indexOf(DFW_PDFAppConfig_Format2.securityDeposit_After));
		    System.out.println("Security Deposit = "+DFW_PropertyWare.securityDeposit.trim());
		    if(RunnerClass.onlyDigits(DFW_PropertyWare.petSecurityDeposit)==true)
		    {
		    	DFW_PropertyWare.petSecurityDepositFlag = true;
		    	System.out.println("Security Deposit is checked and has value");
		    }
	    	}
	    	catch(Exception e)
	    	{
	    	DFW_PropertyWare.securityDeposit = "Error";	
	    	e.printStackTrace();
	    	}
	    	*/
	    	String typeSubString = "";
	    	try
	    	{
	    	typeSubString = text.substring(text.indexOf(DFW_PDFAppConfig_Format2.typeWord_Prior)+DFW_PDFAppConfig_Format2.typeWord_Prior.length(),text.indexOf(DFW_PDFAppConfig_Format2.typeWord_After));
	    	}
	    	catch(Exception e)
	    	{
	    		try
	    		{
	    		typeSubString = text.substring(text.indexOf(DFW_PDFAppConfig_Format2.typeWord_Prior)+DFW_PDFAppConfig_Format2.typeWord_Prior.length(),text.indexOf(DFW_PDFAppConfig_Format2.typeWord_After2));
	    		}
	    		catch(Exception e2)
	    		{
	    			try
	    			{
	    				typeSubString = text.substring(text.indexOf(DFW_PDFAppConfig_Format2.typeWord_Prior2)+DFW_PDFAppConfig_Format2.typeWord_Prior2.length(),text.indexOf(DFW_PDFAppConfig_Format2.typeWord_After3));
	    			}
	    			catch(Exception e3)
	    			{
	    		    typeSubString = "";
	    			}
	    		}
	    	}
	    	 String newText = typeSubString.replace("Type:","");
			    DFW_PropertyWare.countOfTypeWordInText = ((typeSubString.length() - newText.length())/"Type:".length());
			    System.out.println("Type: occurences = "+DFW_PropertyWare.countOfTypeWordInText);
	    	
	    	DFW_PropertyWare.petType = new ArrayList();
		    DFW_PropertyWare.petBreed = new ArrayList();
		    DFW_PropertyWare.petWeight = new ArrayList();
		    for(int i =0;i<DFW_PropertyWare.countOfTypeWordInText;i++)
		    {
		    	String type = typeSubString.substring(RunnerClass.nthOccurrence(typeSubString, "Type:", i+1)+DFW_PDFAppConfig.AB_pet1Type_Prior.length(),RunnerClass.nthOccurrence(typeSubString, "Breed:", i+1)).trim();
		    	if(type.contains("N/A")||type.contains("n/a"))
		    		break;
		    	System.out.println(type);
		    	DFW_PropertyWare.petType.add(type);
		    	int pet1Breedindex1 = RunnerClass.nthOccurrence(typeSubString, "Breed:", i+1)+"Breed:".length()+1;
			    String subString = typeSubString.substring(pet1Breedindex1);
			    //int pet1Breedindex2 = RunnerClass.nthOccurrence(subString,"Name:",i+1);
			   // System.out.println("Index 2 = "+(index2+index1));
			    String breed = subString.split("Name:")[0].trim();//typeSubString.substring(pet1Breedindex1,(pet1Breedindex2+pet1Breedindex1));
			    System.out.println(breed);
			    DFW_PropertyWare.petBreed.add(breed);
			    int pet1Weightindex1 = RunnerClass.nthOccurrence(typeSubString, "Weight:", i+1)+"Weight:".length()+1;
			    String pet1WeightSubstring = typeSubString.substring(pet1Weightindex1);
			    //int pet1WeightIndex2 = RunnerClass.nthOccurrence(pet1WeightSubstring,"Age:",i+1);
			   // System.out.println("Index 2 = "+(index2+index1));
			    String weight = pet1WeightSubstring.split("Age:")[0].trim(); //typeSubString.substring(pet1Weightindex1,(pet1WeightIndex2+pet1Weightindex1));
			    System.out.println(weight);
			    DFW_PropertyWare.petWeight.add(weight);
		    }
		    
	    	
		    try
		    {
		    	DFW_PropertyWare.petOneTimeNonRefundableFee = text.substring(text.indexOf(DFW_PDFAppConfig_Format2.petOneTimeNonRefundable_Prior)+DFW_PDFAppConfig_Format2.petOneTimeNonRefundable_Prior.length(),text.indexOf(DFW_PDFAppConfig_Format2.petOneTimeNonRefundable_After)).trim();
		    	if(DFW_PropertyWare.petOneTimeNonRefundableFee.matches(".*[a-zA-Z]+.*"))
			    {
			    	DFW_PropertyWare.petOneTimeNonRefundableFee = "Error";
			    }
		    }
		    catch(Exception e)
		    {
		    	try
		    	{
		    		DFW_PropertyWare.petOneTimeNonRefundableFee = text.substring(text.indexOf(DFW_PDFAppConfig_Format2.petOneTimeNonRefundable_Prior2)+DFW_PDFAppConfig_Format2.petOneTimeNonRefundable_Prior2.length()).trim().split(",")[0];
				    //System.out.println("pet one time non refundable = "+DFW_PropertyWare.petOneTimeNonRefundableFee.trim());
		    		if(DFW_PropertyWare.petOneTimeNonRefundableFee.matches(".*[a-zA-Z]+.*"))
				    {
				    	DFW_PropertyWare.petOneTimeNonRefundableFee = "Error";
				    }
		    	}
		    	catch(Exception e2)
		    	{
		    	DFW_PropertyWare.petOneTimeNonRefundableFee =  "Error";
		    	e2.printStackTrace();
		    	}
		    }  
		    System.out.println("pet one time non refundable = "+DFW_PropertyWare.petOneTimeNonRefundableFee.trim());
		    
		  
		    
		    
	    }
	    
	  //Service Animal Addendum check
	    try
	    {
	    if(text.contains(DFW_AppConfig.serviceAnimalText))
	    {
	    	DFW_PropertyWare.serviceAnimalFlag = true;
    		System.out.println("Service Animal Addendum is available");
    		
            String typeSubString = text.substring(text.indexOf(DFW_PDFAppConfig_Format2.AB_serviceAnimal_typeWord_Prior)+DFW_PDFAppConfig_Format2.AB_serviceAnimal_typeWord_Prior.length(),text.indexOf(DFW_PDFAppConfig_Format2.AB_serviceAnimal_typeWord_After));
	    	
	    	String newText = typeSubString.replace("Type:","");
		    int  countOftypeWords_ServiceAnimal = ((typeSubString.length() - newText.length())/"Type:".length());
		    System.out.println("Service Animal - Type: occurences = "+countOftypeWords_ServiceAnimal);
		    
		    DFW_PropertyWare.serviceAnimalPetType = new ArrayList();
		    DFW_PropertyWare.serviceAnimalPetBreed = new ArrayList();
		    DFW_PropertyWare.serviceAnimalPetWeight = new ArrayList();
		    for(int i =0;i<countOftypeWords_ServiceAnimal;i++)
		    {
		    	String type = typeSubString.substring(RunnerClass.nthOccurrence(typeSubString, "Type:", i+1)+DFW_PDFAppConfig.AB_pet1Type_Prior.length(),RunnerClass.nthOccurrence(typeSubString, "Breed:", i+1)).trim();
		    	if(type.contains("N/A")||type.contains("n/a"))
		    		break;
		    	System.out.println(type);
		    	DFW_PropertyWare.serviceAnimalPetType.add(type);
		    	int pet1Breedindex1 = RunnerClass.nthOccurrence(typeSubString, "Breed:", i+1)+"Breed:".length()+1;
			    String subString = typeSubString.substring(pet1Breedindex1);
			    //int pet1Breedindex2 = RunnerClass.nthOccurrence(subString,"Name:",i+1);
			   // System.out.println("Index 2 = "+(index2+index1));
			    String breed = subString.split("Name:")[0].trim();//typeSubString.substring(pet1Breedindex1,(pet1Breedindex2+pet1Breedindex1));
			    System.out.println(breed);
			    DFW_PropertyWare.serviceAnimalPetBreed.add(breed);
			    int pet1Weightindex1 = RunnerClass.nthOccurrence(typeSubString, "Weight:", i+1)+"Weight:".length()+1;
			    String pet1WeightSubstring = typeSubString.substring(pet1Weightindex1);
			    //int pet1WeightIndex2 = RunnerClass.nthOccurrence(pet1WeightSubstring,"Age:",i+1);
			   // System.out.println("Index 2 = "+(index2+index1));
			    String weight = pet1WeightSubstring.split("Age:")[0].trim(); //typeSubString.substring(pet1Weightindex1,(pet1WeightIndex2+pet1Weightindex1));
			    System.out.println(weight);
			    DFW_PropertyWare.serviceAnimalPetWeight.add(weight);
		    }
    		
    		
	    }
	    }
	    catch(Exception e)
	    {
	    	DFW_PropertyWare.serviceAnimalFlag = false;
	    }
	  //Concession Addendum
	    
	    try
	    {
	    	if(text.contains(DFW_PDFAppConfig.concessionAddendumText))
	    	{
	    		DFW_PropertyWare.concessionAddendumFlag = true;
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
		 lateFeeRuleText = text.substring(text.indexOf(DFW_PDFAppConfig_Format2.lateFeeRuleText_Prior)+DFW_PDFAppConfig_Format2.lateFeeRuleText_Prior.length(),text.indexOf(DFW_PDFAppConfig_Format2.lateFeeRuleText_After));
		}
		catch(Exception e)
		{
			try
			{
			lateFeeRuleText = text.substring(text.indexOf(DFW_PDFAppConfig_Format2.lateFeeRuleText_Prior)+DFW_PDFAppConfig_Format2.lateFeeRuleText_Prior.length(),text.indexOf(DFW_PDFAppConfig_Format2.lateFeeRuleText_After2));
			}
			catch(Exception e2)
			{
			return false;
			}
		}
		if(lateFeeRuleText.contains(DFW_PDFAppConfig.lateFeeRule_whicheverIsGreater))
		{
			RunnerClass.lateFeeRuleType = "GreaterOfFlatFeeOrPercentage";
			RunnerClass.lateFeeType = "GreaterOfFlatFeeOrPercentage";
			//DFW_PropertyWare.lateFeeType ="Greater of Flat Fee or Percentage"; 
		//Late charge day
			try
			{
		   // DFW_PropertyWare.lateChargeDay =  lateFeeRuleText.substring(lateFeeRuleText.indexOf(DFW_PDFAppConfig.lateFeeRule_whicheverIsGreater_dueDay_Prior)+DFW_PDFAppConfig.lateFeeRule_whicheverIsGreater_dueDay_Prior.length()).trim().split(" ")[0];
				DFW_PropertyWare.lateChargeDay =  lateFeeRuleText.split(DFW_PDFAppConfig.lateFeeRule_whicheverIsGreater_dueDay_After)[0].trim();
				DFW_PropertyWare.lateChargeDay =DFW_PropertyWare.lateChargeDay.substring(DFW_PropertyWare.lateChargeDay.lastIndexOf(" ")+1);
		    DFW_PropertyWare.lateChargeDay =  DFW_PropertyWare.lateChargeDay.replaceAll("[^0-9]", "");
			}
			catch(Exception e)
			{
				DFW_PropertyWare.lateChargeDay = "Error";
			}
         System.out.println("Late Charge Day = "+DFW_PropertyWare.lateChargeDay);
			RunnerClass.dueDay_GreaterOf = DFW_PropertyWare.lateChargeDay;
		//Late Fee Percentage
			try
			{
		    DFW_PropertyWare.lateFeePercentage =  lateFeeRuleText.substring(lateFeeRuleText.indexOf(DFW_PDFAppConfig.lateFeeRule_whicheverIsGreater_lateFeePercentage)+DFW_PDFAppConfig.lateFeeRule_whicheverIsGreater_lateFeePercentage.length()).trim().split(" ")[0];
		    DFW_PropertyWare.lateFeePercentage = DFW_PropertyWare.lateFeePercentage.replaceAll("[^0-9]", "");
			}
			catch(Exception e)
			{
				DFW_PropertyWare.lateFeePercentage = "Error";
			}
         System.out.println("Late Fee Percentage = "+DFW_PropertyWare.lateFeePercentage);
         RunnerClass.percentage = DFW_PropertyWare.lateFeePercentage;
         //Late Fee Amount
         try
         {
         String lateFeeAmount  = lateFeeRuleText.substring(lateFeeRuleText.indexOf(DFW_PDFAppConfig.lateFeeRule_whicheverIsGreater_lateFeeAmount)+DFW_PDFAppConfig.lateFeeRule_whicheverIsGreater_lateFeeAmount.length()).trim().split(" ")[0];
         DFW_PropertyWare.flatFeeAmount = lateFeeAmount.replaceAll("[^.0-9]", "");
         }
         catch(Exception e)
         {
        	 DFW_PropertyWare.flatFeeAmount ="Error";
         }
         System.out.println("Late Fee Amount = "+DFW_PropertyWare.flatFeeAmount);
        RunnerClass.flatFee = DFW_PropertyWare.flatFeeAmount;
         return true;
		}
		else 
		if(lateFeeRuleText.contains(DFW_PDFAppConfig.lateFeeRule_mayNotExceedMoreThan30Days))
		{
			RunnerClass.lateFeeRuleType = "initialFeePluPerDayFee";
			//RunnerClass.lateFeeRuleType = "Initial Fee + Per Day Fee";
			
			DFW_PropertyWare.lateFeeType ="initialFeePluPerDayFee"; 
	         try
	 	    {
	 		    DFW_PropertyWare.lateChargeFee = text.substring(text.indexOf(DFW_PDFAppConfig.AB_lateFee_Prior)+DFW_PDFAppConfig.AB_lateFee_Prior.length()).trim().split(" ")[0];
	 		    //DFW_PropertyWare.lateChargeFee =  DFW_PropertyWare.lateChargeFee.substring(0,DFW_PropertyWare.lateChargeFee.length()-1);
	 	    }
	 	    catch(Exception e)
	 	    {
	 		    DFW_PropertyWare.lateChargeFee ="Error";	
	 		    e.printStackTrace();
	 	    }
	 	    System.out.println("Late Charge Fee = "+DFW_PropertyWare.lateChargeFee.trim());
	 	   RunnerClass.initialFeeAmount = DFW_PropertyWare.lateChargeFee;
	 	    //Per Day Fee
	 	    try
	 	    {
	 	    	DFW_PropertyWare.lateFeeChargePerDay = text.substring(text.indexOf(DFW_PDFAppConfig.AB_additionalLateChargesPerDay_Prior)+DFW_PDFAppConfig.AB_additionalLateChargesPerDay_Prior.length()).split(" ")[0].trim();//,text.indexOf(DFW_PDFAppConfig.AB_additionalLateChargesPerDay_After));
	 	    }
	 	    catch(Exception e)
	 	    {
	 	    	DFW_PropertyWare.lateFeeChargePerDay =  "Error";	
	 	    	e.printStackTrace();
	 	    }
	 	    System.out.println("Per Day Fee = "+DFW_PropertyWare.lateFeeChargePerDay.trim());
	 	    RunnerClass.perDayFeeAmount = DFW_PropertyWare.lateFeeChargePerDay;
	 	    //Additional Late Charges Limit
	 	    try
	 	    {
	 	    	DFW_PropertyWare.additionalLateChargesLimit = text.substring(text.indexOf(DFW_PDFAppConfig.AB_additionalLateChargesLimit_Prior)+DFW_PDFAppConfig.AB_additionalLateChargesLimit_Prior.length()).trim().split(" ")[0]; //,text.indexOf(DFW_PDFAppConfig.AB_additionalLateChargesLimit_After));
	 	    }
	 	    catch(Exception e)
	 	    {
	 	    	DFW_PropertyWare.additionalLateChargesLimit =  "Error";	
	 	    	e.printStackTrace();
	 	    }
	 	    System.out.println("additional Late Charges Limit = "+DFW_PropertyWare.additionalLateChargesLimit.trim());
	 	    RunnerClass.additionalLateChargesLimit = DFW_PropertyWare.additionalLateChargesLimit;
	 	 //Late Charge Day
			try
	 	    {
			DFW_PropertyWare.lateChargeDay = lateFeeRuleText.substring(lateFeeRuleText.indexOf("p.m. on the ")+"p.m. on the ".length()).trim().split(" ")[0];
			DFW_PropertyWare.lateChargeDay = DFW_PropertyWare.lateChargeDay.replaceAll("[^0-9]", "");
	 	    }
			catch(Exception e)
	 	    {
	 	    	DFW_PropertyWare.lateChargeDay =  "Error";	
	 	    	e.printStackTrace();
	 	    }
	 	    System.out.println("Late Charge Due Day = "+DFW_PropertyWare.lateChargeDay.trim());
	 	    RunnerClass.dueDay_initialFee = DFW_PropertyWare.lateChargeDay;
	 	   return true;
		}
		else if(lateFeeRuleText.contains(DFW_PDFAppConfig.lateFeeRule_mayNotExceedAmount))
			{
			RunnerClass.lateFeeRuleType = "initialFeePluPerDayFee";
			RunnerClass.lateFeeRuleType = "Initial Fee + Per Day Fee";
			
			//Late Charge Day
			try
	 	    {
			DFW_PropertyWare.lateChargeDay = lateFeeRuleText.substring(lateFeeRuleText.indexOf("an initial late charge on the")+"an initial late charge on the".length()).trim().split(" ")[0];
			DFW_PropertyWare.lateChargeDay = DFW_PropertyWare.lateChargeDay.replaceAll("[^0-9]", "");
	 	    }
			catch(Exception e)
	 	    {
	 	    	DFW_PropertyWare.lateChargeDay =  "Error";	
	 	    	e.printStackTrace();
	 	    }
	 	    System.out.println("Late Charge Due Day = "+DFW_PropertyWare.lateChargeDay.trim());
	 	   RunnerClass.dueDay_initialFee = DFW_PropertyWare.lateChargeDay;
	 	    // initial Late Charge
	 	   try
	 	    {
			DFW_PropertyWare.lateChargeFee = lateFeeRuleText.substring(lateFeeRuleText.indexOf("day of the month equal to $")+"day of the month equal to $".length()).trim().split(" ")[0];
			DFW_PropertyWare.lateChargeFee = DFW_PropertyWare.lateChargeFee.replaceAll("[^0-9.]", "");
	 	    }
			catch(Exception e)
	 	    {
	 	    	DFW_PropertyWare.lateChargeFee =  "Error";	
	 	    	e.printStackTrace();
	 	    }
	 	    System.out.println("Late Charge Fee = "+DFW_PropertyWare.lateChargeFee.trim());
	 	   RunnerClass.initialFeeAmount = DFW_PropertyWare.lateChargeFee;
	 	    // Additional Late Charges
	 	   try
	 	    {
			DFW_PropertyWare.additionalLateCharges = lateFeeRuleText.substring(lateFeeRuleText.indexOf("and additional late charge of $")+"and additional late charge of $".length()).trim().split(" ")[0];
			DFW_PropertyWare.additionalLateCharges = DFW_PropertyWare.additionalLateCharges.replaceAll("[^0-9.]", "");
	 	    }
			catch(Exception e)
	 	    {
	 	    	DFW_PropertyWare.additionalLateCharges =  "Error";	
	 	    	e.printStackTrace();
	 	    }
	 	    System.out.println("Additional Late Charges = "+DFW_PropertyWare.additionalLateCharges.trim());
	 	   RunnerClass.maximumAmount = DFW_PropertyWare.additionalLateCharges;
	 	    //Additional Late Charges Limit
	 	   try
	 	    {
			DFW_PropertyWare.additionalLateChargesLimit = lateFeeRuleText.substring(lateFeeRuleText.indexOf("(initial and additional) may not exceed $")+"(initial and additional) may not exceed $".length()).trim().split(" ")[0];
			DFW_PropertyWare.additionalLateChargesLimit = DFW_PropertyWare.additionalLateChargesLimit.replaceAll("[^0-9.]", "");
	 	    }
			catch(Exception e)
	 	    {
	 	    	DFW_PropertyWare.additionalLateChargesLimit =  "Error";	
	 	    	e.printStackTrace();
	 	    }
	 	    System.out.println("Additional Late Charges Limit = "+DFW_PropertyWare.additionalLateChargesLimit.trim());
	 	   RunnerClass.additionalLateChargesLimit = DFW_PropertyWare.additionalLateChargesLimit;
			return true;
			}
		else 
			if(lateFeeRuleText.contains(DFW_PDFAppConfig.lateFeeRule_totalDelinquentRentDueToTheTenantAccount))
			{
				RunnerClass.lateFeeRuleType = "GreaterOfFlatFeeOrPercentage";
				RunnerClass.lateFeeType = "GreaterOfFlatFeeOrPercentage";
				
			//Late Charge Day
			try
	 	    {
			DFW_PropertyWare.lateChargeDay = lateFeeRuleText.substring(lateFeeRuleText.indexOf("11:59 PM of the ")+"11:59 PM of the ".length()).trim().split(" ")[0];
			DFW_PropertyWare.lateChargeDay = DFW_PropertyWare.lateChargeDay.replaceAll("[^0-9]", "");
	 	    }
			catch(Exception e)
	 	    {
	 	    	DFW_PropertyWare.lateChargeDay =  "Error";	
	 	    	e.printStackTrace();
	 	    }
	 	    System.out.println("Late Charge Due Day = "+DFW_PropertyWare.lateChargeDay.trim());
	 	   RunnerClass.dueDay_GreaterOf = DFW_PropertyWare.lateChargeDay;
	 	    // initial Late Charge
	 	   try
	 	    {
			DFW_PropertyWare.lateChargeFee = lateFeeRuleText.substring(lateFeeRuleText.indexOf("an initial late charge equal to ")+"an initial late charge equal to ".length()).trim().split(" ")[0];
			//DFW_PropertyWare.lateChargeFee = DFW_PropertyWare.lateChargeFee.replaceAll("[^0-9.]", "");
	 	    }
			catch(Exception e)
	 	    {
	 	    	DFW_PropertyWare.lateChargeFee =  "Error";	
	 	    	e.printStackTrace();
	 	    }
	 	    System.out.println("Late Charge Fee = "+DFW_PropertyWare.lateChargeFee.trim());
	 	   RunnerClass.percentage = DFW_PropertyWare.lateChargeFee;
	 	   /*
	 	    // Additional Late Charges
	 	   try
	 	    {
			DFW_PropertyWare.additionalLateCharges = lateFeeRuleText.substring(lateFeeRuleText.indexOf("and additional late charge of $")+"and additional late charge of $".length()).trim().split(" ")[0];
			DFW_PropertyWare.additionalLateCharges = DFW_PropertyWare.additionalLateCharges.replaceAll("[^0-9.]", "");
	 	    }
			catch(Exception e)
	 	    {
	 	    	DFW_PropertyWare.additionalLateCharges =  "Error";	
	 	    	e.printStackTrace();
	 	    }
	 	    System.out.println("Additional Late Charges = "+DFW_PropertyWare.additionalLateCharges.trim());
	 	    RunnerClass.maximumAmount = DFW_PropertyWare.additionalLateCharges;
	 	    //Additional Late Charges Limit
	 	   try
	 	    {
			DFW_PropertyWare.additionalLateChargesLimit = lateFeeRuleText.substring(lateFeeRuleText.indexOf("(initial and additional) may not exceed $")+"(initial and additional) may not exceed $".length()).trim().split(" ")[0];
			DFW_PropertyWare.additionalLateChargesLimit = DFW_PropertyWare.additionalLateChargesLimit.replaceAll("[^0-9.]", "");
	 	    }
			catch(Exception e)
	 	    {
	 	    	DFW_PropertyWare.additionalLateChargesLimit =  "Error";	
	 	    	e.printStackTrace();
	 	    }
	 	    System.out.println("Additional Late Charges Limit = "+DFW_PropertyWare.additionalLateChargesLimit.trim());
	 	    RunnerClass.additionalLateChargesLimit = DFW_PropertyWare.additionalLateChargesLimit;
			return true;
			}
			else
		   {
			DFW_PropertyWare.lateFeeType ="";
			return false;
	 	    */
		   }
		return true;
		
	}
		
		
		/*
		//Late Charge Day
		try
	    {
	    	DFW_PropertyWare.lateChargeDay = text.substring(text.indexOf(DFW_PDFAppConfig_Format2.lateFeeDay_Prior)+DFW_PDFAppConfig_Format2.lateFeeDay_Prior.length()).trim().split(" ")[0].trim().replace("[^0-9]", "");
	    	DFW_PropertyWare.lateChargeDay = DFW_PropertyWare.lateChargeDay.replaceAll("[^\\d]", "");
	    	System.out.println("Late Charge Day = "+DFW_PropertyWare.lateChargeDay);//.substring(commensementDate.lastIndexOf(":")+1));
	    }
	    catch(Exception e)
	    {
	    	DFW_PropertyWare.lateChargeDay = "Error";
	    	e.printStackTrace();
	    }
	    
	    //Late Charge Fee
	    try
	    {
	    	DFW_PropertyWare.lateChargeFee = text.substring(text.indexOf(DFW_PDFAppConfig_Format2.lateChargePerDayFee)+DFW_PDFAppConfig_Format2.lateChargePerDayFee.length()).trim().split(" ")[0].trim();
	    	//DFW_PropertyWare.lateChargeFee = DFW_PropertyWare.lateChargeFee.replaceAll("[^.0-9]", "");
	    }
	    catch(Exception e)
	    {
	    	DFW_PropertyWare.lateChargeFee = "Error";
	    	e.printStackTrace();
	    }
	    System.out.println("Late Fee = "+DFW_PropertyWare.lateChargeFee);//.substring(commensementDate.lastIndexOf(":")+1));
	    /*
	    //Per Day Fee
	    try
	    {
	    	DFW_PropertyWare.additionalLateCharges = text.substring(text.indexOf(DFW_PDFAppConfig_Format2.additionaLateCharge_Prior)+DFW_PDFAppConfig_Format2.additionaLateCharge_Prior.length()).trim().split(" ")[0].trim();
	    	DFW_PropertyWare.additionalLateCharges = DFW_PropertyWare.additionalLateCharges.replaceAll("[^.0-9]", "");
	    }
	    catch(Exception e)
	    {
	    	DFW_PropertyWare.additionalLateCharges = "Error";
	    	e.printStackTrace();
	    }
	    System.out.println("Additional Late Charges  = "+DFW_PropertyWare.additionalLateCharges);//.substring(commensementDate.lastIndexOf(":")+1));
	    
	    try
	    {
	    if(DFW_RunnerClass.pdfFormatType.equalsIgnoreCase("Format1"))
	    {
	    	DFW_PropertyWare.additionalLateChargesLimit = text.substring(text.indexOf(DFW_PDFAppConfig_Format2.additionaLateCharge_Prior)+DFW_PDFAppConfig_Format2.additionaLateCharge_Prior.length()).split(" ")[0].trim();
		    System.out.println("Additional Late Charges  = "+DFW_PropertyWare.additionalLateChargesLimit);//.substring(commensementDate.lastIndexOf(":")+1));
	    }
	    }
	    catch(Exception e)
	    {
	    	DFW_PropertyWare.additionalLateChargesLimit = "Error";
	    	e.printStackTrace();
	    }
	    */
	


}
