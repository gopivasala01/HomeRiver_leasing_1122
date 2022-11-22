package mainPackage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import Alabama.InsertDataIntoPropertyWare_UsingConfigTable;

public class GetDataFromDataBase 
{
	public static int inprogressLeaseLength=0;
	public static String[][] inProgressLeases;
	
	public void extractInProgressLeases()
	{
		// Calculate how many leases are inprogress and assigned it to below variable to define String array length.
		inprogressLeaseLength =2; //Sample value
		inProgressLeases = new String[inprogressLeaseLength][4];
		
		/*for(int i=0;i<inprogressLeaseLength;i++)
		{
			for(int j=0;j<=i;j++)
			{
				//Company
				inProgressLeases[i][j] = "Arizona";
				//Port folio
				inProgressLeases[i][j+1] = "MCH.HS";
				//Lease Name
				inProgressLeases[i][j+2] = "CALI949 - (949 N CALIFORNIA ST)_7436";
				//Lease Owner 
				inProgressLeases[i][j+3] = "Abner - Rozan - Rozan";
				
				
				break;
			}
		}*/
		
		inProgressLeases[0][0] = "Arizona";
		//Port folio
		inProgressLeases[0][1] = "MCH.HS";
		//Lease Name
		inProgressLeases[0][2] = "CALI949 - (949 N CALIFORNIA ST)_7436";
		//Lease Owner 
		inProgressLeases[0][3] = "Nelson - Jones";
		
		/*
		inProgressLeases[1][0] = "Arizona";
		//Port folio
		inProgressLeases[1][1] = "MCH.HS";
		//Lease Name
		inProgressLeases[1][2] = "CALI949 - (949 N CALIFORNIA ST)_7436"; //31ST7116 - (7116 N 31ST DR)_7406
		//Lease Owner 
		inProgressLeases[1][3] = "Nelson - Jones"; //Abner - Rozan - Rozan
		*/
	}
	
	public void getInProgressLeasesFromDatabase() throws Exception
	//public static void main(String[] args)  throws Exception
	{
		String connectionUrl = "jdbc:sqlserver://azrsrv001.database.windows.net;databaseName=HomeRiverDB;user=service_sql02;password=xzqcoK7T";
        Connection con = null;
        Statement stmt = null;
        ResultSet rs = null;
            //Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            con = DriverManager.getConnection(connectionUrl);
            String SQL = AppConfig.sqlQueryToFetchInProgressLeases;
            stmt = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
           // stmt = con.createStatement();
            rs = stmt.executeQuery(SQL);
            int rows =0;
            if (rs.last()) {
            	rows = rs.getRow();
            	// Move to beginning
            	rs.beforeFirst();
            }
            System.out.println("No of Rows = "+rows);
            inProgressLeases = new String[rows][4];
           int  i=0;
            while(rs.next())
            {
            	
            	String 	company =  (String) rs.getObject(1);
                String portfolio = (String) rs.getObject(2);
                String buildingName = (String) rs.getObject(3);
                String ownerName = (String) rs.getObject(4);
                System.out.println(company +" ----  "+portfolio+" ---- "+buildingName+" ---- "+ownerName);
    				//Company
    				inProgressLeases[i][0] = company;
    				//Port folio
    				inProgressLeases[i][1] = portfolio;
    				//Lease Name
    				inProgressLeases[i][2] = buildingName;
    				//Lease Owner 
    				inProgressLeases[i][3] = ownerName;
    				i++;
            }	
            System.out.println("Total Pending Leases  = " +inProgressLeases.length);
            //for(int j=0;j<inProgressLeases.length;j++)
            //{
            //	System.out.println(inProgressLeases[j][j]);
           // }
            rs.close();
            stmt.close();
            con.close();
	}
	
	public static  void  getChargesFromConfigurationTable() throws Exception
	//public static void main(String[] args)  throws Exception
	{
		String connectionUrl = "jdbc:sqlserver://azrsrv001.database.windows.net;databaseName=HomeRiverDB;user=service_sql02;password=xzqcoK7T";
        Connection con = null;
        Statement stmt = null;
        ResultSet rs = null;
            //Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            con = DriverManager.getConnection(connectionUrl);
            String SQL = "Select Charge from [Automation].[ChargeCodesConfiguration]";
            stmt = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
           // stmt = con.createStatement();
            rs = stmt.executeQuery(SQL);
            int rows =0;
            if (rs.last()) {
            	rows = rs.getRow();
            	// Move to beginning
            	rs.beforeFirst();
            }
            System.out.println("No of Rows - Charges = "+rows);
            InsertDataIntoPropertyWare_UsingConfigTable.charges = new String[rows];
           int  i=0;
            while(rs.next())
            {
            	
            	String 	charge =  (String) rs.getObject(1);
                //String portfolio = (String) rs.getObject(2);
               // String buildingName = (String) rs.getObject(3);
                //String ownerName = (String) rs.getObject(4);
                //System.out.println(charge);
    				//Company
                InsertDataIntoPropertyWare_UsingConfigTable.charges[i] = charge;
    				//Port folio
    				//inProgressLeases[i][1] = portfolio;
    				//Lease Name
    				//inProgressLeases[i][2] = buildingName;
    				//Lease Owner 
    				//inProgressLeases[i][3] = ownerName;
    				i++;
            }	
            System.out.println("Total Pending Leases  = " +InsertDataIntoPropertyWare_UsingConfigTable.charges.length);
            //for(int j=0;j<inProgressLeases.length;j++)
            //{
            //	System.out.println(inProgressLeases[j][j]);
           // }
            rs.close();
            stmt.close();
            con.close();
            
	}
	public static  void  getMoveInCharges() throws Exception
	//public static void main(String[] args)  throws Exception
	{
		String connectionUrl = "jdbc:sqlserver://azrsrv001.database.windows.net;databaseName=HomeRiverDB;user=service_sql02;password=xzqcoK7T";
        Connection con = null;
        Statement stmt = null;
        ResultSet rs = null;
            //Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            con = DriverManager.getConnection(connectionUrl);
            String SQL = "Select Charge, ChargeCode,Description,StartDate,Amount from [Automation].[ChargeCodesConfiguration] where MoveInCharge = 1";
            
            stmt = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
           // stmt = con.createStatement();
            rs = stmt.executeQuery(SQL);
            int rows =0;
            if (rs.last()) {
            	rows = rs.getRow();
            	// Move to beginning
            	rs.beforeFirst();
            }
            System.out.println("No of Rows - Move In Charges = "+rows);
            InsertDataIntoPropertyWare_UsingConfigTable.moveInCharges = new String[rows][5];
           int  i=0;
            while(rs.next())
            {
            	
            	String 	charge =  (String) rs.getObject(1);
                String chargeCode = (String) rs.getObject(2);
                String Description = (String) rs.getObject(3);
                String startDate = (String) rs.getObject(4);
                String amount = (String) rs.getObject(5);
                System.out.println(charge);
    				//Charge
                InsertDataIntoPropertyWare_UsingConfigTable.moveInCharges[i][0] = charge;
    				//Charge Code
                InsertDataIntoPropertyWare_UsingConfigTable.moveInCharges[i][1] = chargeCode;
    				//Description
                InsertDataIntoPropertyWare_UsingConfigTable.moveInCharges[i][2] = Description;
    				//Start Date
                InsertDataIntoPropertyWare_UsingConfigTable.moveInCharges[i][3] = startDate;
                //Amount
                InsertDataIntoPropertyWare_UsingConfigTable.moveInCharges[i][4] = amount;
    				i++;
            }	
            System.out.println("Total Pending Leases  = " +InsertDataIntoPropertyWare_UsingConfigTable.moveInCharges.length);
            //for(int j=0;j<inProgressLeases.length;j++)
            //{
            //	System.out.println(inProgressLeases[j][j]);
           // }
            rs.close();
            stmt.close();
            con.close();
            
	}
	
	public static  void  getAutoCharges() throws Exception
	//public static void main(String[] args)  throws Exception
	{
		String connectionUrl = "jdbc:sqlserver://azrsrv001.database.windows.net;databaseName=HomeRiverDB;user=service_sql02;password=xzqcoK7T";
        Connection con = null;
        Statement stmt = null;
        ResultSet rs = null;
            //Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            con = DriverManager.getConnection(connectionUrl);
            String SQL = "Select Charge, ChargeCode,Description,autoCharge_StartDate,Amount,endDate from [Automation].[ChargeCodesConfiguration] where AutoCharge = 1";
            
            stmt = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
           // stmt = con.createStatement();
            rs = stmt.executeQuery(SQL);
            int rows =0;
            if (rs.last()) {
            	rows = rs.getRow();
            	// Move to beginning
            	rs.beforeFirst();
            }
            System.out.println("No of Rows - Auto Charges = "+rows);
            InsertDataIntoPropertyWare_UsingConfigTable.autoCharges = new String[rows][6];
           int  i=0;
            while(rs.next())
            {
            	
            	String 	charge =  (String) rs.getObject(1);
                String chargeCode = (String) rs.getObject(2);
                String Description = (String) rs.getObject(3);
                String autoCharge_staDate = (String) rs.getObject(4);
                String amount = (String) rs.getObject(5);
                String endDate = (String) rs.getObject(6);
                //System.out.println(charge);
    				//Charge
                InsertDataIntoPropertyWare_UsingConfigTable.autoCharges[i][0] = charge;
    				//Charge Code
                InsertDataIntoPropertyWare_UsingConfigTable.autoCharges[i][1] = chargeCode;
    				//Description
                InsertDataIntoPropertyWare_UsingConfigTable.autoCharges[i][2] = Description;
    				//Start Date
                InsertDataIntoPropertyWare_UsingConfigTable.autoCharges[i][3] = autoCharge_staDate;
                //Amount
                InsertDataIntoPropertyWare_UsingConfigTable.autoCharges[i][4] = amount;
                //End Date
                InsertDataIntoPropertyWare_UsingConfigTable.autoCharges[i][5] = endDate;
    				i++;
            }	
            //System.out.println("Total Pending Leases  = " +InsertDataIntoPropertyWare_UsingConfigTable.moveInCharges.length);
            //for(int j=0;j<inProgressLeases.length;j++)
            //{
            //	System.out.println(inProgressLeases[j][j]);
           // }
            rs.close();
            stmt.close();
            con.close();
            
	}
	
	
	
	

}
