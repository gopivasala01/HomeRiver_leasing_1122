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
	public static String sqlQueryToFetchInProgressLeases = "Select Company, [Portfolio ],BuildingName,OwnerName from  Automation.LeaseInfo where Status ='In Progress' order by SNO desc";

	public static String[] IAGClientList = {"510","AVE","BTH","CAP","FOR","HRG","HS","MAN","MCH","OFF","PIN","RF","SFR3","TH","HH","Lofty.Ai"};
}
