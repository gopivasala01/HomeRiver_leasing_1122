package OKC;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import mainPackage.RunnerClass;

public class OKC_ExtractDataFromPDF_Format2 
{
	public static boolean petFlag;
	public static String text ="";
	public boolean arizona() throws Exception
	//public static void main(String[] args) throws Exception 
	{
		OKC_PropertyWare.petFlag = false;
		//File file = new File("C:\\Gopi\\Projects\\Property ware\\Lease Close Outs\\PDFS\\Tennessee\\Format 2\\Lease_031.22_05.23_1327_Everwood_Dr_Ashland_C_(1).pdf");
		File file = RunnerClass.getLastModified();
		FileInputStream fis = new FileInputStream(file);
		OKC_RunnerClass.document = PDDocument.load(fis);
	    text = new PDFTextStripper().getText(OKC_RunnerClass.document);
	    text = text.replaceAll(System.lineSeparator(), " ");
	    text = text.trim().replaceAll(" +", " ");
	    System.out.println(text);
	    System.out.println("------------------------------------------------------------------");
	    
	    try
	    {
	    	String commensementRaw = text.substring(text.indexOf(OKC_PDFAppConfig_Format2.commensementDate_Prior)+OKC_PDFAppConfig_Format2.commensementDate_Prior.length()+1).trim();//,text.indexOf(OKC_PDFAppConfig_Format2.commensementDate_After)).trim();
	    	 OKC_PropertyWare.commensementDate = commensementRaw.substring(0, commensementRaw.indexOf('(')).trim();
	    	 OKC_PropertyWare.commensementDate = OKC_PropertyWare.commensementDate.trim().replaceAll(" +", " ");
		    System.out.println("Commensement Date = "+OKC_PropertyWare.commensementDate);//.substring(commensementDate.lastIndexOf(":")+1));
	    }
	    catch(Exception e)
	    {
	    	OKC_PropertyWare.commensementDate = "Error";
	    	e.printStackTrace();
	    }
	    
	    try
	    {
	    	String expirationDateRaw  = text.substring(text.indexOf(OKC_PDFAppConfig_Format2.expirationDate_Prior)+OKC_PDFAppConfig_Format2.expirationDate_Prior.length()).trim();//,text.indexOf(OKC_PDFAppConfig_Format2.expirationDate_After)).trim();
	    	OKC_PropertyWare.expirationDate = expirationDateRaw.substring(0,expirationDateRaw.indexOf('(')).trim();
	    	OKC_PropertyWare.expirationDate = OKC_PropertyWare.expirationDate.trim().replaceAll(" +", " ");
	    	System.out.println("Expiration Date = "+OKC_PropertyWare.expirationDate);//.substring(commensementDate.lastIndexOf(":")+1));
	    }
	    catch(Exception e)
	    {
	    	OKC_PropertyWare.expirationDate = "Error";
	    	e.printStackTrace();
	    }
	    
	    try
	    {
	    	OKC_PropertyWare.proratedRentDate = text.substring(text.indexOf(OKC_PDFAppConfig_Format2.proratedRentDate_Prior)+OKC_PDFAppConfig_Format2.proratedRentDate_Prior.length()+1,text.indexOf(OKC_PDFAppConfig_Format2.proratedRentDate_After)).trim();
		    System.out.println("prorated Rent Date = "+OKC_PropertyWare.proratedRentDate);//.substring(commensementDate.lastIndexOf(":")+1));
	    }
	    catch(Exception e)
	    {
	    	OKC_PropertyWare.proratedRentDate = "Error";
	    	e.printStackTrace();
	    }
	    
	    try
	    {
	    	OKC_PropertyWare.proratedRent = text.substring(text.indexOf(OKC_PDFAppConfig_Format2.proratedRent_Prior)+OKC_PDFAppConfig_Format2.proratedRent_Prior.length()).split(" ")[0].trim();
	    	if(OKC_PropertyWare.proratedRent.matches(".*[a-zA-Z]+.*"))
		    {
		    	OKC_PropertyWare.proratedRent = "Error";
		    }
	    }
	    catch(Exception e)
	    {
	    	OKC_PropertyWare.proratedRent = "Error";
	    	e.printStackTrace();
	    }
	    System.out.println("prorated Rent = "+OKC_PropertyWare.proratedRent);//.substring(commensementDate.lastIndexOf(":")+1));
	    
	    try
	    {
	    	OKC_PropertyWare.monthlyRent = text.substring(text.indexOf(OKC_PDFAppConfig_Format2.monthlyRent_Prior)+OKC_PDFAppConfig_Format2.monthlyRent_Prior.length()).split(" ")[0].trim();
	    	if(!OKC_PropertyWare.monthlyRent.contains("."))
	    		OKC_PropertyWare.monthlyRent = text.substring(text.indexOf(OKC_PDFAppConfig_Format2.monthlyRent_Prior2)+OKC_PDFAppConfig_Format2.monthlyRent_Prior2.length()).split(" ")[0].trim();
	    	if(OKC_PropertyWare.monthlyRent.matches(".*[a-zA-Z]+.*"))
		    {
		    	OKC_PropertyWare.monthlyRent = "Error";
		    }
	    	if(OKC_PropertyWare.monthlyRent.contains("*")||text.contains(OKC_PDFAppConfig_Format2.monthlyRentAvailabilityCheck)==true)
	    	{
	    		OKC_PropertyWare.incrementRentFlag = true;
	    		OKC_PropertyWare.monthlyRent = OKC_PropertyWare.monthlyRent.replace("*", "");
	    		System.out.println("Monthly Rent has Asterick *");
	    		
	    		//OKC_PropertyWare.increasedRent_amount = text.substring(text.indexOf(". $")+". $".length()).trim().split(" ")[0];
	    		String increasedRent_ProviousRentEndDate = "Per the Landlord, Monthly Rent from "+OKC_PropertyWare.commensementDate.trim()+" through ";
	    		 String endDateArray[] = text.substring(text.indexOf(". $")+". $".length()).split(" ");
	    		if(endDateArray[2].trim().length()==4)//&&RunnerClass.onlyDigits(endDateArray[2]))
	    		 {
	    		  OKC_PropertyWare.increasedRent_previousRentEndDate = endDateArray[0]+" "+endDateArray[1]+" "+endDateArray[2];
	    		  System.out.println("Increased Rent - Previous rent end date = "+OKC_PropertyWare.increasedRent_previousRentEndDate);
	    		 
	    		  String newRentStartDate[] = text.substring(text.indexOf(OKC_PDFAppConfig_Format2.increasedRent_newStartDate_Prior)+OKC_PDFAppConfig_Format2.increasedRent_newStartDate_Prior.length()).trim().split(" ");
	    		  OKC_PropertyWare.increasedRent_newStartDate = newRentStartDate[0]+" "+newRentStartDate[1]+" "+newRentStartDate[2];
	    		  System.out.println("Increased Rent - New Rent Start date = "+OKC_PropertyWare.increasedRent_newStartDate);
	    		  
	    		  String increasedRentRaw = text.substring(text.indexOf(OKC_PDFAppConfig_Format2.increasedRent_newStartDate_Prior)+OKC_PDFAppConfig_Format2.increasedRent_newStartDate_Prior.length()).trim();
	    		  OKC_PropertyWare.increasedRent_amount = increasedRentRaw.substring(increasedRentRaw.indexOf("shall be $")+"shall be $".length()).trim().split(" ")[0];
	    		  System.out.println("Increased Rent - Amount = "+OKC_PropertyWare.increasedRent_amount); 
	    		}
	    		else 
	    		 {
	    			 String adding0toMonth = "0"+OKC_PropertyWare.commensementDate.trim().split(" ")[1];
	    			 String commeseDate = OKC_PropertyWare.commensementDate.trim().replace(OKC_PropertyWare.commensementDate.trim().split(" ")[1], adding0toMonth);
	    			 increasedRent_ProviousRentEndDate = "Per the Landlord, Monthly Rent from "+commeseDate+" through ";
		    		 String endDateArray2[] = text.substring(text.indexOf(increasedRent_ProviousRentEndDate)+increasedRent_ProviousRentEndDate.length()).split(" ");
		    		 if(endDateArray2[2].trim().length()==4)//&&RunnerClass.onlyDigits(endDateArray[2]))
		    		 {
		    		  OKC_PropertyWare.increasedRent_previousRentEndDate = endDateArray2[0]+" "+endDateArray2[1]+" "+endDateArray2[2];
		    		  System.out.println("Increased Rent - Previous rent end date = "+OKC_PropertyWare.increasedRent_previousRentEndDate);
		    		 
		    		  String newRentStartDate[] = text.substring(text.indexOf(OKC_PDFAppConfig_Format2.increasedRent_newStartDate_Prior)+OKC_PDFAppConfig_Format2.increasedRent_newStartDate_Prior.length()).trim().split(" ");
		    		  OKC_PropertyWare.increasedRent_newStartDate = newRentStartDate[0]+" "+newRentStartDate[1]+" "+newRentStartDate[2];
		    		  System.out.println("Increased Rent - New Rent Start date = "+OKC_PropertyWare.increasedRent_newStartDate);
		    		  
		    		  String increasedRentRaw = text.substring(text.indexOf(OKC_PDFAppConfig_Format2.increasedRent_newStartDate_Prior)+OKC_PDFAppConfig_Format2.increasedRent_newStartDate_Prior.length()).trim();
		    		  OKC_PropertyWare.increasedRent_amount = increasedRentRaw.substring(increasedRentRaw.indexOf("shall be $")+"shall be $".length()).trim().split(" ")[0];
		    		  System.out.println("Increased Rent - Amount = "+OKC_PropertyWare.increasedRent_amount); 
		    		 }
	    		 }
	    	}
	    }
	    catch(Exception e)
	    {
	    	OKC_PropertyWare.monthlyRent = "Error";
	    	e.printStackTrace();
	    }
	    System.out.println("Monthly Rent = "+OKC_PropertyWare.monthlyRent);//.substring(commensementDate.lastIndexOf(":")+1));
	   
	    try
	    {
	    	OKC_PropertyWare.adminFee = text.substring(text.indexOf(OKC_PDFAppConfig_Format2.adminFee_prior)+OKC_PDFAppConfig_Format2.adminFee_prior.length()).split(" ")[0].trim();
	    	if(OKC_PropertyWare.adminFee.matches(".*[a-zA-Z]+.*"))
		    {
		    	OKC_PropertyWare.adminFee = "Error";
		    }
	    }
	    catch(Exception e)
	    {
	    	OKC_PropertyWare.adminFee = "Error";
	    	e.printStackTrace();
	    }
	    System.out.println("Admin Fee = "+OKC_PropertyWare.adminFee);//.substring(commensementDate.lastIndexOf(":")+1));
	  //Resident Benefits Package 
	    if(text.contains(OKC_PDFAppConfig.residentBenefitsPackageAddendumCheck))
	    {
	    	OKC_PropertyWare.residentBenefitsPackageAvailabilityCheck = true;
	    	 try
	 	    {
	 		    OKC_PropertyWare.residentBenefitsPackage = text.substring(text.indexOf(OKC_PDFAppConfig_Format2.AB1_residentBenefitsPackage_Prior)+OKC_PDFAppConfig_Format2.AB1_residentBenefitsPackage_Prior.length()).split(" ")[0].replaceAll("[^0-9a-zA-Z.]", "");
	 		    if(OKC_PropertyWare.residentBenefitsPackage.matches(".*[a-zA-Z]+.*"))
	 		    {
	 		    	OKC_PropertyWare.residentBenefitsPackage = "Error";
	 		    }
	 	    }
	 	    catch(Exception e)
	 	    {
	 		    OKC_PropertyWare.residentBenefitsPackage = "Error";
	 		    e.printStackTrace();
	 	    }
	    	 System.out.println("Resident Benefits Package  = "+OKC_PropertyWare.residentBenefitsPackage.trim());
	    	//OKC_PDFAppConfig.AB1_residentBenefitsPackage_Prior
	    }
	    else
	    {
		    if(text.contains(OKC_PDFAppConfig_Format2.HVACFilterAddendumTextAvailabilityCheck)==true)
		    {
		    	OKC_PropertyWare.HVACFilterFlag =true;
		    }
		    else
		    {
		    try
		    {
		    	OKC_PropertyWare.airFilterFee = text.substring(text.indexOf(OKC_PDFAppConfig_Format2.HVACAirFilter_prior)+OKC_PDFAppConfig_Format2.HVACAirFilter_prior.length()).split(" ")[0].trim();
		    	if(OKC_PropertyWare.airFilterFee.matches(".*[a-zA-Z]+.*"))
			    {
			    	OKC_PropertyWare.airFilterFee = "Error";
			    }
		    }
		    catch(Exception e)
		    {
		    	OKC_PropertyWare.airFilterFee = "Error";
		    	e.printStackTrace();
		    }
		    }
		    System.out.println("HVAC Air Filter Fee = "+OKC_PropertyWare.airFilterFee);//.substring(commensementDate.lastIndexOf(":")+1));
	    }
	    try
	    {
	    	OKC_PropertyWare.occupants = text.substring(text.indexOf(OKC_PDFAppConfig_Format2.occupants_Prior)+OKC_PDFAppConfig_Format2.occupants_Prior.length(),text.indexOf(OKC_PDFAppConfig_Format2.occupants_After)).trim();
		    System.out.println("Occupants = "+OKC_PropertyWare.occupants);//.substring(commensementDate.lastIndexOf(":")+1));
	    }
	    catch(Exception e)
	    {
	    	OKC_PropertyWare.occupants = "Error";
	    	e.printStackTrace();
	    }
	    
	    //Late Fee Rule
	    OKC_ExtractDataFromPDF_Format2.lateFeeRule();
	    
	    
	  //Prepayment Charge
	    try
	    {
		if(OKC_PropertyWare.portfolioType.contains("MCH"))
		{
		try
		{
		OKC_PropertyWare.prepaymentCharge =String.valueOf(Double.parseDouble(OKC_PropertyWare.monthlyRent.replace(",", "")) - Double.parseDouble(OKC_PropertyWare.proratedRent.replace(",", ""))); 
		}
		catch(Exception e)
		{
			OKC_PropertyWare.prepaymentCharge ="Error";
		}
		System.out.println("Prepayment Charge = "+OKC_PropertyWare.prepaymentCharge);
		}
	    }
	    catch(Exception e) {}
	    //Early Termination
		try
	    {
	    	String[] earlyTerminationRaw = text.substring(text.indexOf(OKC_PDFAppConfig_Format2.earlyTermination_Prior)+OKC_PDFAppConfig_Format2.earlyTermination_Prior.length()).split(" ");
	    	
		    OKC_PropertyWare.earlyTermination = earlyTerminationRaw[0]+earlyTerminationRaw[1]; //text.substring(text.indexOf(OKC_PDFAppConfig.AB_earlyTerminationFee_Prior)+OKC_PDFAppConfig.AB_earlyTerminationFee_Prior.length(),text.indexOf(OKC_PDFAppConfig.AB_earlyTerminationFee_After));
	    }
	    catch(Exception e)
	    {
	    	OKC_PropertyWare.earlyTermination = "Error";	
	    	e.printStackTrace();
	    }
		System.out.println("Early Termination = "+OKC_PropertyWare.earlyTermination);
	    // Checking Pet Addendum is available or not
	    
	    if(text.contains(OKC_PDFAppConfig_Format2.petAgreementAvailabilityCheck)==true)
	    	petFlag =  true;
	    else if(petFlag = text.contains(OKC_PDFAppConfig_Format2.petAgreementAvailabilityCheck2)==true)
	    	petFlag =  true;
	    else petFlag =  false;
	    
	    System.out.println("Pet Addendum Available = "+petFlag);
	    if(petFlag ==true)
	    {
	    	OKC_PropertyWare.petFlag = true;
			    	try
			    	{
			    		 String proratedPetRaw = "Prorated Pet Rent: On or before "+OKC_PropertyWare.commensementDate.trim()+" Tenant will pay Landlord $";
				    		OKC_PropertyWare.proratedPetRent = text.substring(text.indexOf(proratedPetRaw)+proratedPetRaw.length()).trim().split(" ")[0].trim();//.replaceAll("[a-ZA-Z,]", "");
				    		if(OKC_PropertyWare.proratedPetRent.matches(".*[a-zA-Z]+.*")||OKC_PropertyWare.proratedPetRent.trim().equals("1."))
						    {
						    	OKC_PropertyWare.proratedPetRent = "Error";
						    }
				    		//OKC_PropertyWare.proratedPetRent = proratedPetRentRaw.substring(proratedPetRentRaw.indexOf("Tenant will pay Landlord $")+"Tenant will pay Landlord $".length());//,proratedPetRentRaw.indexOf(AppConfig.AR_proratedPetRent_After));
			        }
				    catch(Exception e)
				    {
				    OKC_PropertyWare.proratedPetRent = "Error";	
				    e.printStackTrace();
				    }
	    	System.out.println("Prorated Pet rent= "+OKC_PropertyWare.proratedPetRent.trim());
	    	try
    		{
    			OKC_PropertyWare.petRent = text.substring(text.indexOf(OKC_PDFAppConfig_Format2.petRent_Prior)+OKC_PDFAppConfig_Format2.petRent_Prior.length()).split(" ")[0].trim();
				 //System.out.println("Pet rent = "+OKC_PropertyWare.petRent.trim());
				 if(RunnerClass.onlyDigits(OKC_PropertyWare.petRent)==false)
				    {
				    	 OKC_PropertyWare.petRent = text.substring(text.indexOf(OKC_PDFAppConfig_Format2.petRent_Prior2)+OKC_PDFAppConfig_Format2.petRent_Prior2.length()).trim().split(" ")[0];
				    }
				 if(OKC_PropertyWare.petRent.matches(".*[a-zA-Z]+.*"))
				    {
				    	OKC_PropertyWare.petRent = "Error";
				    }
    		}
    		
    		catch(Exception e1)
		    {
		    	OKC_PropertyWare.petRent = "Error";  
		    	e1.printStackTrace();
		    }
	    	System.out.println("Pet rent= "+OKC_PropertyWare.petRent.trim());
	    	
	    	//Pet Security Deposit -- Need to find the text of Pet Security Deposit for the format PDF, until then, it is commented
	    	/*
	    	try
	    	{
	    	OKC_PropertyWare.petSecurityDeposit = text.substring(text.indexOf(OKC_PDFAppConfig_Format2.securityDeposit_Prior)+OKC_PDFAppConfig_Format2.securityDeposit_Prior.length()).trim().split(" ")[0];//,text.indexOf(OKC_PDFAppConfig_Format2.securityDeposit_After));
		    System.out.println("Security Deposit = "+OKC_PropertyWare.securityDeposit.trim());
		    if(RunnerClass.onlyDigits(OKC_PropertyWare.petSecurityDeposit)==true)
		    {
		    	OKC_PropertyWare.petSecurityDepositFlag = true;
		    	System.out.println("Security Deposit is checked and has value");
		    }
	    	}
	    	catch(Exception e)
	    	{
	    	OKC_PropertyWare.securityDeposit = "Error";	
	    	e.printStackTrace();
	    	}
	    	*/
	    	String typeSubString = "";
	    	try
	    	{
	    	typeSubString = text.substring(text.indexOf(OKC_PDFAppConfig_Format2.typeWord_Prior)+OKC_PDFAppConfig_Format2.typeWord_Prior.length(),text.indexOf(OKC_PDFAppConfig_Format2.typeWord_After));
	    	}
	    	catch(Exception e)
	    	{
	    		try
	    		{
	    		typeSubString = text.substring(text.indexOf(OKC_PDFAppConfig_Format2.typeWord_Prior)+OKC_PDFAppConfig_Format2.typeWord_Prior.length(),text.indexOf(OKC_PDFAppConfig_Format2.typeWord_After2));
	    		}
	    		catch(Exception e2)
	    		{
	    			try
	    			{
	    				typeSubString = text.substring(text.indexOf(OKC_PDFAppConfig_Format2.typeWord_Prior2)+OKC_PDFAppConfig_Format2.typeWord_Prior2.length(),text.indexOf(OKC_PDFAppConfig_Format2.typeWord_After3));
	    			}
	    			catch(Exception e3)
	    			{
	    		    typeSubString = "";
	    			}
	    		}
	    	}
	    	 String newText = typeSubString.replace("Type:","");
			    OKC_PropertyWare.countOfTypeWordInText = ((typeSubString.length() - newText.length())/"Type:".length());
			    System.out.println("Type: occurences = "+OKC_PropertyWare.countOfTypeWordInText);
	    	
	    	OKC_PropertyWare.petType = new ArrayList();
		    OKC_PropertyWare.petBreed = new ArrayList();
		    OKC_PropertyWare.petWeight = new ArrayList();
		    for(int i =0;i<OKC_PropertyWare.countOfTypeWordInText;i++)
		    {
		    	String type = typeSubString.substring(RunnerClass.nthOccurrence(typeSubString, "Type:", i+1)+OKC_PDFAppConfig.AB_pet1Type_Prior.length(),RunnerClass.nthOccurrence(typeSubString, "Breed:", i+1)).trim();
		    	if(type.contains("N/A")||type.contains("n/a"))
		    		break;
		    	System.out.println(type);
		    	OKC_PropertyWare.petType.add(type);
		    	int pet1Breedindex1 = RunnerClass.nthOccurrence(typeSubString, "Breed:", i+1)+"Breed:".length()+1;
			    String subString = typeSubString.substring(pet1Breedindex1);
			    //int pet1Breedindex2 = RunnerClass.nthOccurrence(subString,"Name:",i+1);
			   // System.out.println("Index 2 = "+(index2+index1));
			    String breed = subString.split("Name:")[0].trim();//typeSubString.substring(pet1Breedindex1,(pet1Breedindex2+pet1Breedindex1));
			    System.out.println(breed);
			    OKC_PropertyWare.petBreed.add(breed);
			    int pet1Weightindex1 = RunnerClass.nthOccurrence(typeSubString, "Weight:", i+1)+"Weight:".length()+1;
			    String pet1WeightSubstring = typeSubString.substring(pet1Weightindex1);
			    //int pet1WeightIndex2 = RunnerClass.nthOccurrence(pet1WeightSubstring,"Age:",i+1);
			   // System.out.println("Index 2 = "+(index2+index1));
			    String weight = pet1WeightSubstring.split("Age:")[0].trim(); //typeSubString.substring(pet1Weightindex1,(pet1WeightIndex2+pet1Weightindex1));
			    System.out.println(weight);
			    OKC_PropertyWare.petWeight.add(weight);
		    }
		    
	    	
		    try
		    {
		    	OKC_PropertyWare.petOneTimeNonRefundableFee = text.substring(text.indexOf(OKC_PDFAppConfig_Format2.petOneTimeNonRefundable_Prior)+OKC_PDFAppConfig_Format2.petOneTimeNonRefundable_Prior.length(),text.indexOf(OKC_PDFAppConfig_Format2.petOneTimeNonRefundable_After)).trim();
		    	if(OKC_PropertyWare.petOneTimeNonRefundableFee.matches(".*[a-zA-Z]+.*"))
			    {
			    	OKC_PropertyWare.petOneTimeNonRefundableFee = "Error";
			    }
		    }
		    catch(Exception e)
		    {
		    	try
		    	{
		    		OKC_PropertyWare.petOneTimeNonRefundableFee = text.substring(text.indexOf(OKC_PDFAppConfig_Format2.petOneTimeNonRefundable_Prior2)+OKC_PDFAppConfig_Format2.petOneTimeNonRefundable_Prior2.length()).trim().split(",")[0];
				    //System.out.println("pet one time non refundable = "+OKC_PropertyWare.petOneTimeNonRefundableFee.trim());
		    		if(OKC_PropertyWare.petOneTimeNonRefundableFee.matches(".*[a-zA-Z]+.*"))
				    {
				    	OKC_PropertyWare.petOneTimeNonRefundableFee = "Error";
				    }
		    	}
		    	catch(Exception e2)
		    	{
		    	OKC_PropertyWare.petOneTimeNonRefundableFee =  "Error";
		    	e2.printStackTrace();
		    	}
		    }  
		    System.out.println("pet one time non refundable = "+OKC_PropertyWare.petOneTimeNonRefundableFee.trim());
		    
		  
		    
		    
	    }
	    
	  //Service Animal Addendum check
	    try
	    {
	    if(text.contains(OKC_AppConfig.serviceAnimalText))
	    {
	    	OKC_PropertyWare.serviceAnimalFlag = true;
    		System.out.println("Service Animal Addendum is available");
    		
            String typeSubString = text.substring(text.indexOf(OKC_PDFAppConfig_Format2.AB_serviceAnimal_typeWord_Prior)+OKC_PDFAppConfig_Format2.AB_serviceAnimal_typeWord_Prior.length(),text.indexOf(OKC_PDFAppConfig_Format2.AB_serviceAnimal_typeWord_After));
	    	
	    	String newText = typeSubString.replace("Type:","");
		    int  countOftypeWords_ServiceAnimal = ((typeSubString.length() - newText.length())/"Type:".length());
		    System.out.println("Service Animal - Type: occurences = "+countOftypeWords_ServiceAnimal);
		    
		    OKC_PropertyWare.serviceAnimalPetType = new ArrayList();
		    OKC_PropertyWare.serviceAnimalPetBreed = new ArrayList();
		    OKC_PropertyWare.serviceAnimalPetWeight = new ArrayList();
		    for(int i =0;i<countOftypeWords_ServiceAnimal;i++)
		    {
		    	String type = typeSubString.substring(RunnerClass.nthOccurrence(typeSubString, "Type:", i+1)+OKC_PDFAppConfig.AB_pet1Type_Prior.length(),RunnerClass.nthOccurrence(typeSubString, "Breed:", i+1)).trim();
		    	if(type.contains("N/A")||type.contains("n/a"))
		    		break;
		    	System.out.println(type);
		    	OKC_PropertyWare.serviceAnimalPetType.add(type);
		    	int pet1Breedindex1 = RunnerClass.nthOccurrence(typeSubString, "Breed:", i+1)+"Breed:".length()+1;
			    String subString = typeSubString.substring(pet1Breedindex1);
			    //int pet1Breedindex2 = RunnerClass.nthOccurrence(subString,"Name:",i+1);
			   // System.out.println("Index 2 = "+(index2+index1));
			    String breed = subString.split("Name:")[0].trim();//typeSubString.substring(pet1Breedindex1,(pet1Breedindex2+pet1Breedindex1));
			    System.out.println(breed);
			    OKC_PropertyWare.serviceAnimalPetBreed.add(breed);
			    int pet1Weightindex1 = RunnerClass.nthOccurrence(typeSubString, "Weight:", i+1)+"Weight:".length()+1;
			    String pet1WeightSubstring = typeSubString.substring(pet1Weightindex1);
			    //int pet1WeightIndex2 = RunnerClass.nthOccurrence(pet1WeightSubstring,"Age:",i+1);
			   // System.out.println("Index 2 = "+(index2+index1));
			    String weight = pet1WeightSubstring.split("Age:")[0].trim(); //typeSubString.substring(pet1Weightindex1,(pet1WeightIndex2+pet1Weightindex1));
			    System.out.println(weight);
			    OKC_PropertyWare.serviceAnimalPetWeight.add(weight);
		    }
    		
    		
	    }
	    }
	    catch(Exception e)
	    {
	    	OKC_PropertyWare.serviceAnimalFlag = false;
	    }
	  //Concession Addendum
	    
	    try
	    {
	    	if(text.contains(OKC_PDFAppConfig.concessionAddendumText))
	    	{
	    		OKC_PropertyWare.concessionAddendumFlag = true;
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
		 lateFeeRuleText = text.substring(text.indexOf(OKC_PDFAppConfig_Format2.lateFeeRuleText_Prior)+OKC_PDFAppConfig_Format2.lateFeeRuleText_Prior.length(),text.indexOf(OKC_PDFAppConfig_Format2.lateFeeRuleText_After));
		}
		catch(Exception e)
		{
			try
			{
			lateFeeRuleText = text.substring(text.indexOf(OKC_PDFAppConfig_Format2.lateFeeRuleText_Prior)+OKC_PDFAppConfig_Format2.lateFeeRuleText_Prior.length(),text.indexOf(OKC_PDFAppConfig_Format2.lateFeeRuleText_After2));
			}
			catch(Exception e2)
			{
			return false;
			}
		}
		if(lateFeeRuleText.contains(OKC_PDFAppConfig.lateFeeRule_whicheverIsGreater))
		{
			RunnerClass.lateFeeRuleType = "GreaterOfFlatFeeOrPercentage";
			RunnerClass.lateFeeType = "GreaterOfFlatFeeOrPercentage";
			//OKC_PropertyWare.lateFeeType ="Greater of Flat Fee or Percentage"; 
		//Late charge day
			try
			{
		   // OKC_PropertyWare.lateChargeDay =  lateFeeRuleText.substring(lateFeeRuleText.indexOf(OKC_PDFAppConfig.lateFeeRule_whicheverIsGreater_dueDay_Prior)+OKC_PDFAppConfig.lateFeeRule_whicheverIsGreater_dueDay_Prior.length()).trim().split(" ")[0];
				OKC_PropertyWare.lateChargeDay =  lateFeeRuleText.split(OKC_PDFAppConfig.lateFeeRule_whicheverIsGreater_dueDay_After)[0].trim();
				OKC_PropertyWare.lateChargeDay =OKC_PropertyWare.lateChargeDay.substring(OKC_PropertyWare.lateChargeDay.lastIndexOf(" ")+1);
		    OKC_PropertyWare.lateChargeDay =  OKC_PropertyWare.lateChargeDay.replaceAll("[^0-9]", "");
			}
			catch(Exception e)
			{
				OKC_PropertyWare.lateChargeDay = "Error";
			}
         System.out.println("Late Charge Day = "+OKC_PropertyWare.lateChargeDay);
			RunnerClass.dueDay_GreaterOf = OKC_PropertyWare.lateChargeDay;
		//Late Fee Percentage
			try
			{
		    OKC_PropertyWare.lateFeePercentage =  lateFeeRuleText.substring(lateFeeRuleText.indexOf(OKC_PDFAppConfig.lateFeeRule_whicheverIsGreater_lateFeePercentage)+OKC_PDFAppConfig.lateFeeRule_whicheverIsGreater_lateFeePercentage.length()).trim().split(" ")[0];
		    OKC_PropertyWare.lateFeePercentage = OKC_PropertyWare.lateFeePercentage.replaceAll("[^0-9]", "");
			}
			catch(Exception e)
			{
				OKC_PropertyWare.lateFeePercentage = "Error";
			}
         System.out.println("Late Fee Percentage = "+OKC_PropertyWare.lateFeePercentage);
         RunnerClass.percentage = OKC_PropertyWare.lateFeePercentage;
         //Late Fee Amount
         try
         {
         String lateFeeAmount  = lateFeeRuleText.substring(lateFeeRuleText.indexOf(OKC_PDFAppConfig.lateFeeRule_whicheverIsGreater_lateFeeAmount)+OKC_PDFAppConfig.lateFeeRule_whicheverIsGreater_lateFeeAmount.length()).trim().split(" ")[0];
         OKC_PropertyWare.flatFeeAmount = lateFeeAmount.replaceAll("[^.0-9]", "");
         }
         catch(Exception e)
         {
        	 OKC_PropertyWare.flatFeeAmount ="Error";
         }
         System.out.println("Late Fee Amount = "+OKC_PropertyWare.flatFeeAmount);
        RunnerClass.flatFee = OKC_PropertyWare.flatFeeAmount;
         return true;
		}
		else 
		if(lateFeeRuleText.contains(OKC_PDFAppConfig.lateFeeRule_mayNotExceedMoreThan30Days))
		{
			RunnerClass.lateFeeRuleType = "initialFeePluPerDayFee";
			//RunnerClass.lateFeeRuleType = "Initial Fee + Per Day Fee";
			
			OKC_PropertyWare.lateFeeType ="initialFeePluPerDayFee"; 
	         try
	 	    {
	 		    OKC_PropertyWare.lateChargeFee = text.substring(text.indexOf(OKC_PDFAppConfig.AB_lateFee_Prior)+OKC_PDFAppConfig.AB_lateFee_Prior.length()).trim().split(" ")[0];
	 		    //OKC_PropertyWare.lateChargeFee =  OKC_PropertyWare.lateChargeFee.substring(0,OKC_PropertyWare.lateChargeFee.length()-1);
	 	    }
	 	    catch(Exception e)
	 	    {
	 		    OKC_PropertyWare.lateChargeFee ="Error";	
	 		    e.printStackTrace();
	 	    }
	 	    System.out.println("Late Charge Fee = "+OKC_PropertyWare.lateChargeFee.trim());
	 	   RunnerClass.initialFeeAmount = OKC_PropertyWare.lateChargeFee;
	 	    //Per Day Fee
	 	    try
	 	    {
	 	    	OKC_PropertyWare.lateFeeChargePerDay = text.substring(text.indexOf(OKC_PDFAppConfig.AB_additionalLateChargesPerDay_Prior)+OKC_PDFAppConfig.AB_additionalLateChargesPerDay_Prior.length()).split(" ")[0].trim();//,text.indexOf(OKC_PDFAppConfig.AB_additionalLateChargesPerDay_After));
	 	    }
	 	    catch(Exception e)
	 	    {
	 	    	OKC_PropertyWare.lateFeeChargePerDay =  "Error";	
	 	    	e.printStackTrace();
	 	    }
	 	    System.out.println("Per Day Fee = "+OKC_PropertyWare.lateFeeChargePerDay.trim());
	 	    RunnerClass.perDayFeeAmount = OKC_PropertyWare.lateFeeChargePerDay;
	 	    //Additional Late Charges Limit
	 	    try
	 	    {
	 	    	OKC_PropertyWare.additionalLateChargesLimit = text.substring(text.indexOf(OKC_PDFAppConfig.AB_additionalLateChargesLimit_Prior)+OKC_PDFAppConfig.AB_additionalLateChargesLimit_Prior.length()).trim().split(" ")[0]; //,text.indexOf(OKC_PDFAppConfig.AB_additionalLateChargesLimit_After));
	 	    }
	 	    catch(Exception e)
	 	    {
	 	    	OKC_PropertyWare.additionalLateChargesLimit =  "Error";	
	 	    	e.printStackTrace();
	 	    }
	 	    System.out.println("additional Late Charges Limit = "+OKC_PropertyWare.additionalLateChargesLimit.trim());
	 	    RunnerClass.additionalLateChargesLimit = OKC_PropertyWare.additionalLateChargesLimit;
	 	 //Late Charge Day
			try
	 	    {
			OKC_PropertyWare.lateChargeDay = lateFeeRuleText.substring(lateFeeRuleText.indexOf("p.m. on the ")+"p.m. on the ".length()).trim().split(" ")[0];
			OKC_PropertyWare.lateChargeDay = OKC_PropertyWare.lateChargeDay.replaceAll("[^0-9]", "");
	 	    }
			catch(Exception e)
	 	    {
	 	    	OKC_PropertyWare.lateChargeDay =  "Error";	
	 	    	e.printStackTrace();
	 	    }
	 	    System.out.println("Late Charge Due Day = "+OKC_PropertyWare.lateChargeDay.trim());
	 	    RunnerClass.dueDay_initialFee = OKC_PropertyWare.lateChargeDay;
	 	   return true;
		}
		else if(lateFeeRuleText.contains(OKC_PDFAppConfig.lateFeeRule_mayNotExceedAmount))
			{
			RunnerClass.lateFeeRuleType = "initialFeePluPerDayFee";
			RunnerClass.lateFeeRuleType = "Initial Fee + Per Day Fee";
			
			//Late Charge Day
			try
	 	    {
			OKC_PropertyWare.lateChargeDay = lateFeeRuleText.substring(lateFeeRuleText.indexOf("an initial late charge on the")+"an initial late charge on the".length()).trim().split(" ")[0];
			OKC_PropertyWare.lateChargeDay = OKC_PropertyWare.lateChargeDay.replaceAll("[^0-9]", "");
	 	    }
			catch(Exception e)
	 	    {
	 	    	OKC_PropertyWare.lateChargeDay =  "Error";	
	 	    	e.printStackTrace();
	 	    }
	 	    System.out.println("Late Charge Due Day = "+OKC_PropertyWare.lateChargeDay.trim());
	 	   RunnerClass.dueDay_initialFee = OKC_PropertyWare.lateChargeDay;
	 	    // initial Late Charge
	 	   try
	 	    {
			OKC_PropertyWare.lateChargeFee = lateFeeRuleText.substring(lateFeeRuleText.indexOf("day of the month equal to $")+"day of the month equal to $".length()).trim().split(" ")[0];
			OKC_PropertyWare.lateChargeFee = OKC_PropertyWare.lateChargeFee.replaceAll("[^0-9.]", "");
	 	    }
			catch(Exception e)
	 	    {
	 	    	OKC_PropertyWare.lateChargeFee =  "Error";	
	 	    	e.printStackTrace();
	 	    }
	 	    System.out.println("Late Charge Fee = "+OKC_PropertyWare.lateChargeFee.trim());
	 	   RunnerClass.initialFeeAmount = OKC_PropertyWare.lateChargeFee;
	 	    // Additional Late Charges
	 	   try
	 	    {
			OKC_PropertyWare.additionalLateCharges = lateFeeRuleText.substring(lateFeeRuleText.indexOf("and additional late charge of $")+"and additional late charge of $".length()).trim().split(" ")[0];
			OKC_PropertyWare.additionalLateCharges = OKC_PropertyWare.additionalLateCharges.replaceAll("[^0-9.]", "");
	 	    }
			catch(Exception e)
	 	    {
	 	    	OKC_PropertyWare.additionalLateCharges =  "Error";	
	 	    	e.printStackTrace();
	 	    }
	 	    System.out.println("Additional Late Charges = "+OKC_PropertyWare.additionalLateCharges.trim());
	 	   RunnerClass.maximumAmount = OKC_PropertyWare.additionalLateCharges;
	 	    //Additional Late Charges Limit
	 	   try
	 	    {
			OKC_PropertyWare.additionalLateChargesLimit = lateFeeRuleText.substring(lateFeeRuleText.indexOf("(initial and additional) may not exceed $")+"(initial and additional) may not exceed $".length()).trim().split(" ")[0];
			OKC_PropertyWare.additionalLateChargesLimit = OKC_PropertyWare.additionalLateChargesLimit.replaceAll("[^0-9.]", "");
	 	    }
			catch(Exception e)
	 	    {
	 	    	OKC_PropertyWare.additionalLateChargesLimit =  "Error";	
	 	    	e.printStackTrace();
	 	    }
	 	    System.out.println("Additional Late Charges Limit = "+OKC_PropertyWare.additionalLateChargesLimit.trim());
	 	   RunnerClass.additionalLateChargesLimit = OKC_PropertyWare.additionalLateChargesLimit;
			return true;
			}
		else 
			if(lateFeeRuleText.contains(OKC_PDFAppConfig.lateFeeRule_totalDelinquentRentDueToTheTenantAccount))
			{
				RunnerClass.lateFeeRuleType = "GreaterOfFlatFeeOrPercentage";
				RunnerClass.lateFeeType = "GreaterOfFlatFeeOrPercentage";
				
			//Late Charge Day
			try
	 	    {
			OKC_PropertyWare.lateChargeDay = lateFeeRuleText.substring(lateFeeRuleText.indexOf("11:59 PM of the ")+"11:59 PM of the ".length()).trim().split(" ")[0];
			OKC_PropertyWare.lateChargeDay = OKC_PropertyWare.lateChargeDay.replaceAll("[^0-9]", "");
	 	    }
			catch(Exception e)
	 	    {
	 	    	OKC_PropertyWare.lateChargeDay =  "Error";	
	 	    	e.printStackTrace();
	 	    }
	 	    System.out.println("Late Charge Due Day = "+OKC_PropertyWare.lateChargeDay.trim());
	 	   RunnerClass.dueDay_GreaterOf = OKC_PropertyWare.lateChargeDay;
	 	    // initial Late Charge
	 	   try
	 	    {
			OKC_PropertyWare.lateChargeFee = lateFeeRuleText.substring(lateFeeRuleText.indexOf("an initial late charge equal to ")+"an initial late charge equal to ".length()).trim().split(" ")[0];
			//OKC_PropertyWare.lateChargeFee = OKC_PropertyWare.lateChargeFee.replaceAll("[^0-9.]", "");
	 	    }
			catch(Exception e)
	 	    {
	 	    	OKC_PropertyWare.lateChargeFee =  "Error";	
	 	    	e.printStackTrace();
	 	    }
	 	    System.out.println("Late Charge Fee = "+OKC_PropertyWare.lateChargeFee.trim());
	 	   RunnerClass.percentage = OKC_PropertyWare.lateChargeFee;
	 	   /*
	 	    // Additional Late Charges
	 	   try
	 	    {
			OKC_PropertyWare.additionalLateCharges = lateFeeRuleText.substring(lateFeeRuleText.indexOf("and additional late charge of $")+"and additional late charge of $".length()).trim().split(" ")[0];
			OKC_PropertyWare.additionalLateCharges = OKC_PropertyWare.additionalLateCharges.replaceAll("[^0-9.]", "");
	 	    }
			catch(Exception e)
	 	    {
	 	    	OKC_PropertyWare.additionalLateCharges =  "Error";	
	 	    	e.printStackTrace();
	 	    }
	 	    System.out.println("Additional Late Charges = "+OKC_PropertyWare.additionalLateCharges.trim());
	 	    RunnerClass.maximumAmount = OKC_PropertyWare.additionalLateCharges;
	 	    //Additional Late Charges Limit
	 	   try
	 	    {
			OKC_PropertyWare.additionalLateChargesLimit = lateFeeRuleText.substring(lateFeeRuleText.indexOf("(initial and additional) may not exceed $")+"(initial and additional) may not exceed $".length()).trim().split(" ")[0];
			OKC_PropertyWare.additionalLateChargesLimit = OKC_PropertyWare.additionalLateChargesLimit.replaceAll("[^0-9.]", "");
	 	    }
			catch(Exception e)
	 	    {
	 	    	OKC_PropertyWare.additionalLateChargesLimit =  "Error";	
	 	    	e.printStackTrace();
	 	    }
	 	    System.out.println("Additional Late Charges Limit = "+OKC_PropertyWare.additionalLateChargesLimit.trim());
	 	    RunnerClass.additionalLateChargesLimit = OKC_PropertyWare.additionalLateChargesLimit;
			return true;
			}
			else
		   {
			OKC_PropertyWare.lateFeeType ="";
			return false;
	 	    */
		   }
		return true;
		
	}
		
		
		/*
		//Late Charge Day
		try
	    {
	    	OKC_PropertyWare.lateChargeDay = text.substring(text.indexOf(OKC_PDFAppConfig_Format2.lateFeeDay_Prior)+OKC_PDFAppConfig_Format2.lateFeeDay_Prior.length()).trim().split(" ")[0].trim().replace("[^0-9]", "");
	    	OKC_PropertyWare.lateChargeDay = OKC_PropertyWare.lateChargeDay.replaceAll("[^\\d]", "");
	    	System.out.println("Late Charge Day = "+OKC_PropertyWare.lateChargeDay);//.substring(commensementDate.lastIndexOf(":")+1));
	    }
	    catch(Exception e)
	    {
	    	OKC_PropertyWare.lateChargeDay = "Error";
	    	e.printStackTrace();
	    }
	    
	    //Late Charge Fee
	    try
	    {
	    	OKC_PropertyWare.lateChargeFee = text.substring(text.indexOf(OKC_PDFAppConfig_Format2.lateChargePerDayFee)+OKC_PDFAppConfig_Format2.lateChargePerDayFee.length()).trim().split(" ")[0].trim();
	    	//OKC_PropertyWare.lateChargeFee = OKC_PropertyWare.lateChargeFee.replaceAll("[^.0-9]", "");
	    }
	    catch(Exception e)
	    {
	    	OKC_PropertyWare.lateChargeFee = "Error";
	    	e.printStackTrace();
	    }
	    System.out.println("Late Fee = "+OKC_PropertyWare.lateChargeFee);//.substring(commensementDate.lastIndexOf(":")+1));
	    /*
	    //Per Day Fee
	    try
	    {
	    	OKC_PropertyWare.additionalLateCharges = text.substring(text.indexOf(OKC_PDFAppConfig_Format2.additionaLateCharge_Prior)+OKC_PDFAppConfig_Format2.additionaLateCharge_Prior.length()).trim().split(" ")[0].trim();
	    	OKC_PropertyWare.additionalLateCharges = OKC_PropertyWare.additionalLateCharges.replaceAll("[^.0-9]", "");
	    }
	    catch(Exception e)
	    {
	    	OKC_PropertyWare.additionalLateCharges = "Error";
	    	e.printStackTrace();
	    }
	    System.out.println("Additional Late Charges  = "+OKC_PropertyWare.additionalLateCharges);//.substring(commensementDate.lastIndexOf(":")+1));
	    
	    try
	    {
	    if(OKC_RunnerClass.pdfFormatType.equalsIgnoreCase("Format1"))
	    {
	    	OKC_PropertyWare.additionalLateChargesLimit = text.substring(text.indexOf(OKC_PDFAppConfig_Format2.additionaLateCharge_Prior)+OKC_PDFAppConfig_Format2.additionaLateCharge_Prior.length()).split(" ")[0].trim();
		    System.out.println("Additional Late Charges  = "+OKC_PropertyWare.additionalLateChargesLimit);//.substring(commensementDate.lastIndexOf(":")+1));
	    }
	    }
	    catch(Exception e)
	    {
	    	OKC_PropertyWare.additionalLateChargesLimit = "Error";
	    	e.printStackTrace();
	    }
	    */

}
