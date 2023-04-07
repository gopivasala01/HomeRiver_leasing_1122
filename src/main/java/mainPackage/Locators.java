package mainPackage;

import org.openqa.selenium.By;

public class Locators 
{
	public static By userName = By.id("loginEmail");
	public static By password = By.name("password");
	public static By signMeIn = By.xpath("//*[@value='Sign Me In']");
	
	public static By searchbox = By.name("eqsSearchText");
	public static By dashboardsTab = By.linkText("Dashboards");
	public static By searchingLoader = By.xpath("//*[@id='eqsResult']/h1");
	public static By noItemsFoundMessagewhenLeaseNotFound = By.xpath("//*[text()='No Items Found']");
	public static By searchedLeaseCompanyHeadings = By.xpath("//*[@id='eqsResult']/div/div/h1");
	public static By selectSearchedLease = By.partialLinkText(RunnerClass.leaseName);
	public static By confirmLeaseIsClicked = By.xpath("//*[text()='Back To Properties Home']");
	public static By getLeaseCDEType = By.xpath("//*[@id='summary']/table[1]/tbody/tr[3]/td");
    public static By leasesTab = By.xpath("(//a[text()='Leases'])[2]");	
    public static By RCDetails = By.xpath("//*[contains(text(),'Resident Coordinator [Name/Phone/Email]')]/following::td[1]/div");
    public static By leaseStartDate_PW = By.xpath("//*[@id='infoTable']/tbody/tr[3]/td[1]");
    public static By leaseEndDate_PW = By.xpath("//*[@id='infoTable']/tbody/tr[3]/td[2]");
    public static By popUpAfterClickingLeaseName = By.id("viewStickyNoteForm");
    public static By popupClose = By.xpath("//*[@id='editStickyBtnDiv']/input[2]");
    public static By notesAndDocs = By.id("notesAndDocuments");
    public static By documentsList = By.xpath("//*[@id='documentHolderBody']/tr/td[1]/a"); 
    
    public static By ledgerTab = By.id("tab2");
    public static By newCharge = By.xpath("//*[@value='New Charge']");
    public static By accountDropdown = By.name("charge.GLAccountID");
    public static By accountList = By.xpath("(//*[@class='edit'])[9]/descendant::select[1]/optgroup/option");
    public static By referenceName = By.name("charge.refNo");
    public static By moveInChargeAmount = By.name("charge.editAmountAsString");
    public static By moveInChargeDate = By.name("charge.dateAsString");
    public static By moveInChargeSave = By.xpath("//*[@value='Save']");
    public static By moveInChargeCancel = By.xpath("//*[@value='Cancel']");
    public static By summaryTab = By.id("tab1");
    public static By summaryEditButton = By.xpath("//*[@value='Edit']");
    public static By newAutoCharge = By.xpath("//*[@value='New Auto Charge']");
    public static By rcField = By.xpath("//*[text()='RC']/following::input[1] or  //*[text()='APM']/following::input[1]");
    public static By autoCharge_Description = By.name("charge.description");
    public static By autoCharge_StartDate = By.name("charge.startDateAsString");
    public static By autoCharge_Amount = By.name("charge.amountAsString");
    public static By autoCharge_CancelButton = By.xpath("//*[@id='editAutoChargeForm']/descendant::div[4]/input[2]");
    public static By autoCharge_refField = By.name("charge.refNo");
    public static By earlyTermFee2x = By.name("field_1184595968");
    public static By enrolledInFilterEasy = By.name("field_1184595968");
    public static By enrolledInFilterEasy_Yes = By.xpath("//*[@name='field_1184595968']/option[2]");
    public static By earlyTermination_Yes = By.xpath("//*[@name='field_755499009']/option[2]");
    public static By needsNewLease = By.xpath("//*[@name='field_711655481']");
    public static By needsNewLease_No = By.xpath("//*[@name='field_711655481']/option[2]");
    public static By leaseOccupants = By.name("field_887062549");
    public static By pet1Type = By.name("field_887062557");
    public static By pet1Breed = By.name("field_887062553");
    public static By pet1Weight = By.name("field_887062558");
    
    public static By serviceAnimalType = By.name("field_887062564");
    public static By serviceAnimalBreed = By.name("field_887062560");
    public static By serviceAnimalWeight = By.name("field_887062565");
    public static By petAmount = By.name("field_890699780");
    public static By tenantOneTimePetFee = By.name("field_771227673");
    public static By petSpecialProvisions = By.name("field_771227674");
    public static By lateFeeDueDay = By.name("entity.lateFee.dueDay");
    public static By initialFee = By.name("entity.lateFee.initial");
    public static By perDayFee = By.name("entity.lateFee.initialDailyAmount");
    public static By initialFeeDropdown = By.name("entity.lateFee.feeTypeMethodInitial");
    public static By perDayFeeDropdown = By.name("entity.lateFee.feeTypeMethod");
    public static By maximumYesNoDropdown = By.name("entity.lateFee.limitAmount");
    public static By maximumDropdown2 = By.name("entity.lateFee.limitTypeMethod");
    public static By maximumDatField = By.name("entity.lateFee.maxAmountAsString");
    public static By checkPortfolioType = By.xpath("//*[@title='Click to jump to portfolio']");
    
    public static By communicationPopup = By.id("communicationDiv");
    public static By communicationPoupOkButton = By.xpath("//*[@value='OK - I GOT IT!']");
    
    
    
    

}
