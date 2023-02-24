package Tulsa;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class Tulsa_GetDataFromDB 
{
	public static  void  getChargesFromConfigurationTable() throws Exception
	//public static void main(String[] args)  throws Exception
	{
		String connectionUrl = "jdbc:sqlserver://azrsrv001.database.windows.net;databaseName=HomeRiverDB;user=service_sql02;password=xzqcoK7T;encrypt=true;trustServerCertificate=true;";
        Connection con = null;
        Statement stmt = null;
        ResultSet rs = null;
            //Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            con = DriverManager.getConnection(connectionUrl);
            String SQL = "Select Charge from "+Tulsa_RunnerClass.chargeCodesTable+"";
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
            Tulsa_InsertDataIntoPropertyWare.charges = new String[rows];
           int  i=0;
            while(rs.next())
            {
            	
            	String 	charge =  (String) rs.getObject(1);
                //String portfolio = (String) rs.getObject(2);
               // String buildingName = (String) rs.getObject(3);
                //String ownerName = (String) rs.getObject(4);
                //System.out.println(charge);
    				//Company
                Tulsa_InsertDataIntoPropertyWare.charges[i] = charge;
    				//Port folio
    				//inProgressLeases[i][1] = portfolio;
    				//Lease Name
    				//inProgressLeases[i][2] = buildingName;
    				//Lease Owner 
    				//inProgressLeases[i][3] = ownerName;
    				i++;
            }	
            System.out.println("Total Pending Leases  = " +Tulsa_InsertDataIntoPropertyWare.charges.length);
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
		String connectionUrl = "jdbc:sqlserver://azrsrv001.database.windows.net;databaseName=HomeRiverDB;user=service_sql02;password=xzqcoK7T;encrypt=true;trustServerCertificate=true;";
        Connection con = null;
        Statement stmt = null;
        ResultSet rs = null;
            //Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            con = DriverManager.getConnection(connectionUrl);
            String SQL = "Select Charge, ChargeCode,Description,StartDate,Amount from "+Tulsa_RunnerClass.chargeCodesTable+" where MoveInCharge = 1";
            
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
            Tulsa_InsertDataIntoPropertyWare.moveInCharges = new String[rows][5];
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
                Tulsa_InsertDataIntoPropertyWare.moveInCharges[i][0] = charge;
    				//Charge Code
                Tulsa_InsertDataIntoPropertyWare.moveInCharges[i][1] = chargeCode;
    				//Description
                Tulsa_InsertDataIntoPropertyWare.moveInCharges[i][2] = Description;
    				//Start Date
                Tulsa_InsertDataIntoPropertyWare.moveInCharges[i][3] = startDate;
                //Amount
                Tulsa_InsertDataIntoPropertyWare.moveInCharges[i][4] = amount;
    				i++;
            }	
            System.out.println("Total Pending Leases  = " +Tulsa_InsertDataIntoPropertyWare.moveInCharges.length);
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
		String connectionUrl = "jdbc:sqlserver://azrsrv001.database.windows.net;databaseName=HomeRiverDB;user=service_sql02;password=xzqcoK7T;encrypt=true;trustServerCertificate=true;";
        Connection con = null;
        Statement stmt = null;
        ResultSet rs = null;
            //Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            con = DriverManager.getConnection(connectionUrl);
            String SQL = "Select Charge, ChargeCode,Description,autoCharge_StartDate,Amount,endDate from "+Tulsa_RunnerClass.chargeCodesTable+" where AutoCharge = 1";
            
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
            Tulsa_InsertDataIntoPropertyWare.autoCharges = new String[rows][6];
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
                Tulsa_InsertDataIntoPropertyWare.autoCharges[i][0] = charge;
    				//Charge Code
                Tulsa_InsertDataIntoPropertyWare.autoCharges[i][1] = chargeCode;
    				//Description
                Tulsa_InsertDataIntoPropertyWare.autoCharges[i][2] = Description;
    				//Start Date
                Tulsa_InsertDataIntoPropertyWare.autoCharges[i][3] = autoCharge_staDate;
                //Amount
                Tulsa_InsertDataIntoPropertyWare.autoCharges[i][4] = amount;
                //End Date
                Tulsa_InsertDataIntoPropertyWare.autoCharges[i][5] = endDate;
    				i++;
            }	
            //System.out.println("Total Pending Leases  = " +Tulsa_InsertDataIntoPropertyWare.moveInCharges.length);
            //for(int j=0;j<inProgressLeases.length;j++)
            //{
            //	System.out.println(inProgressLeases[j][j]);
           // }
            rs.close();
            stmt.close();
            con.close();
            
	}
	 public static boolean assignChargeCodes(String moveInChargesIDs, String autoChargesIDs)
		{
		  String connectionUrl = "jdbc:sqlserver://azrsrv001.database.windows.net;databaseName=HomeRiverDB;user=service_sql02;password=xzqcoK7T;encrypt=true;trustServerCertificate=true;";
		    String sql = "update "+Tulsa_RunnerClass.chargeCodesTable+" Set MoveInCharge ='1' where ID in  ("+moveInChargesIDs+")\n"
		    		+ "update "+Tulsa_RunnerClass.chargeCodesTable+" Set AutoCharge ='1' where ID in  ("+autoChargesIDs+")";

		    try (Connection conn = DriverManager.getConnection(connectionUrl);
		        Statement stmt = conn.createStatement();) 
		    {
		      stmt.executeUpdate(sql);
		      System.out.println("Charge Codes are assigned");
		      stmt.close();
	            conn.close();
	            return true;
		    } catch (SQLException e) 
		    {
		      e.printStackTrace();
		      return false;
		    }
		}



}
