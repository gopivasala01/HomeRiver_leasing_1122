package mainPackage;

public class AppConfig 
{

	public static String PDFFilePath = "C:\\Gopi\\Projects\\Property ware\\Lease Close Outs\\PDFS";
	public static String propertyWareURL = "https://app.propertyware.com/pw/login.jsp";
	public static String userName = "mds0418@gmail.com";
	public static String password = "HomeRiver1#";
	public static String leaseInfo = "C:\\Gopi\\Projects\\Property ware\\Lease Close Outs\\LeaseInfo.xlsx";
	public static String browserType = "webdriver.chrome.driver";
	public static String browserPath = "C:\\Gopi\\Automation\\Chrome Drivers\\chromedriver_103.exe"; 
	public static String sqlQueryToFetchInProgressLeases = "Select Company, [Portfolio ],BuildingName,OwnerName from  Automation.LeaseInfo where Status ='In Progress' order by SNO asc";

	public static String[] IAGClientList = {"510","AVE","BTH","CAP","FOR","HRG","HS","MAN","MCH","OFF","PIN","RF","SFR3","TH","HH","Lofty","TA"};  //SOFFERIRA
	
	public static String getCompanyCode(String company)
	{
		switch(company)
		{
		case "Alabama":
			return "AL";
		case "Arizona":
			return "AZ";
		case "Arkansas":
			return "AR";
		case "Dallas/Fort Worth":
			return "DFW";
		case "Florida":
			return "FL";
		case "Georgia":
			return "GA";
		case "Indiana":
			return "IN";
		case "Little Rock":
			return "LR";
		case "North Carolina":
			return "NC";
		case "OKC":
			return "OKC";
		case "San Antonio":
			return "SATX";
		case "South Carolina":
			return "SC";
		case "Tennessee":
			return "TN";
		case "Tulsa":
			return "TUL";
			
		}
		return "";
	}

}
