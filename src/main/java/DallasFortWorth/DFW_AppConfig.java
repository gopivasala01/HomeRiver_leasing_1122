package DallasFortWorth;

public class DFW_AppConfig
{
	/*
	public static String PDFFilePath = "C:\\Gopi\\Projects\\Property ware\\Lease Close Outs\\PDFS";
	public static String propertyWareURL = "https://app.propertyware.com/pw/login.jsp";
	public static String userName = "mds0418@gmail.com";
	public static String password = "HomeRiver1#";
	public static String leaseInfo = "C:\\Gopi\\Projects\\Property ware\\Lease Close Outs\\LeaseInfo.xlsx";
	public static String browserType = "webdriver.chrome.driver";
	public static String browserPath = "C:\\Gopi\\Automation\\Chrome Drivers\\chromedriver_103.exe"; 
	*/
	// **************************** Account Codes - Move In Charges *******************************
	public static String proratedRent_AccountCode = "4000 - Rent";
	public static String proratedRent_AccountRef = "Pro Rate Rent";
	
	//Monthly Tenant Admin fee
	public static String monthlyTenantAdminFee_AccountCode = "4027 - Monthly Tenant Admin Fee";
	public static String monthlyTenantAdminFee_Ref = "4027 - Monthly Tenant Admin Fee";
	
	//Tenant Admin revenue
	public static String tenantAdminRevenue_AccountCode = "4303 - Tenant Admin Revenue";
	public static String tenantAdminRevenue_Ref = "Admin Fee";
	
	public static String petSpecialProvisions = "1 Service animal, no deposit required";
	public static String fullMonthRent_AccountCode = "2017 - Prepayments";
	public static String fullMonthRent_AccountRef = "Full Month's Rent";
	
	public static String adminFee_AccountCode = "4303 - Tenant Admin Revenue";
	public static String adminFee_AccountRef = "Admin Fee";
	
	public static String proratedPetRent_AccountCode = "4311 - Pet Rent";
	public static String proratedPetRent_AccountRef = "Pro Rated Pet Rent";
	
	public static String petRent_AccountCode = "4005 - Pet Fee";
	public static String petRent_AccountRef = "Pet Rent";
	
	public static String petSecurityDeposit_AccountCode = "2050 - Security Deposit"; // Only if first check box is checked in Pet Addendum
	public static String petSecurityDeposit_AccountRef = "Pet Security Deposit";
	
	// **************************** Account Codes - Auto Charges *******************************
	public static String airFilterFee_AccountCode = "4102 - Air Filter Fee";
	public static String airFilterFee_Description = "HVAC Filter Fee";
	
	public static String Rent_AccountCode = "4000 - Rent";
	public static String Rent_AccountRef = "Rent";
	
	public static String PetRent_AccountCode = "4311 - Pet Rent";
	public static String PetRent_Description = "Additional Rent";
	
	
	// **************PDF Format ************************
	
	public static  String PDFFormatConfirmationText = "SINGLE FAMILY RESIDENCE OR CONDOMINIUM LEASE";
	public static  String PDFFormat2ConfirmationText = "RESIDENTIAL LEASE AGREEMENT";
	public static String serviceAnimalText = "SERVICE/SUPPORT ANIMAL AGREEMENT";

}
