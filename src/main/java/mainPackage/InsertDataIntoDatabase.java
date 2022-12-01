package mainPackage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

  public class InsertDataIntoDatabase
  {
	  public static void insertData(String buildingName, String status, int statusID) throws Exception
	  {

		  DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");  
		   LocalDateTime now = LocalDateTime.now();  
		   System.out.println(dtf.format(now));
		   String currentTime = dtf.format(now);
		  String connectionUrl = "jdbc:sqlserver://azrsrv001.database.windows.net;databaseName=HomeRiverDB;user=service_sql02;password=xzqcoK7T;encrypt=true;trustServerCertificate=true;";
		  String sql;
		  if(statusID==1)
		   sql = "Update [Automation].[LeaseInfo] Set Status ='"+status+"', StatusID="+statusID+",NotAutomatedFields=NULL,StartTime= "+currentTime+" where BuildingName like '%"+buildingName+"%'";
		  else 
			sql = "Update [Automation].[LeaseInfo] Set Status ='"+status+"', StatusID="+statusID+",StartTime= '"+currentTime+"' where BuildingName like '%"+buildingName+"%'";
            //String sql = "Update [Automation].[LeaseInfo] Set Status = 'Completed', StatusID =4 where OwnerName='Duff, V.'";
		  
		    try (Connection conn = DriverManager.getConnection(connectionUrl);
		        Statement stmt = conn.createStatement();) 
		    {
		      stmt.executeUpdate(sql);
		      System.out.println("Database updated successfully ");
		      stmt.close();
	            conn.close();
		    } catch (SQLException e) {
		      e.printStackTrace();
		    }
            
	  }
	  public static void notAutomatedFields(String buildingName, String nextValue) throws Exception
	  {

		  String connectionUrl = "jdbc:sqlserver://azrsrv001.database.windows.net;databaseName=HomeRiverDB;user=service_sql02;password=xzqcoK7T;encrypt=true;trustServerCertificate=true;";
		  String sql = "Update [Automation].[LeaseInfo] Set NotAutomatedFields =CONCAT((Select top 1 NotAutomatedFields from Automation.LeaseInfo where  BuildingName like '%"+buildingName+"%'),',"+nextValue+"') where BuildingName like '%"+buildingName+"%'";
		    

		    try (Connection conn = DriverManager.getConnection(connectionUrl);
		        Statement stmt = conn.createStatement();) 
		    {
		      stmt.executeUpdate(sql);
		      System.out.println("Not Automated Field Updates = "+nextValue);
		      stmt.close();
	            conn.close();
		    } catch (SQLException e) {
		      e.printStackTrace();
		    }
		    RunnerClass.leaseCompletedStatus = 3;
	  }
	  public static void insertPropertyWareURL(String buildingName, String URL) throws Exception
	  {

		  String connectionUrl = "jdbc:sqlserver://azrsrv001.database.windows.net;databaseName=HomeRiverDB;user=service_sql02;password=xzqcoK7T;encrypt=true;trustServerCertificate=true;";
		    String sql = "update [Automation].[LeaseInfo] Set PropertyWareURL ='"+URL+"' where BuildingName like  '%"+buildingName+"%'";

		    try (Connection conn = DriverManager.getConnection(connectionUrl);
		        Statement stmt = conn.createStatement();) 
		    {
		      stmt.executeUpdate(sql);
		      System.out.println("URL Added in the column");
		      stmt.close();
	            conn.close();
		    } catch (SQLException e) {
		      e.printStackTrace();
		    }
	  }
	  public static boolean assignChargeCodes(String moveInChargesIDs, String autoChargesIDs)
		{
		  String connectionUrl = "jdbc:sqlserver://azrsrv001.database.windows.net;databaseName=HomeRiverDB;user=service_sql02;password=xzqcoK7T;encrypt=true;trustServerCertificate=true;";
		    String sql = "update [Automation].[ChargeCodesConfiguration] Set MoveInCharge ='1' where ID in  ("+moveInChargesIDs+")\n"
		    		+ "update [Automation].[ChargeCodesConfiguration] Set AutoCharge ='1' where ID in  ("+autoChargesIDs+")";

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
	 public static void updateTable(String query)
	 {
		 String connectionUrl = "jdbc:sqlserver://azrsrv001.database.windows.net;databaseName=HomeRiverDB;user=service_sql02;password=xzqcoK7T;encrypt=true;trustServerCertificate=true;";

		    try (Connection conn = DriverManager.getConnection(connectionUrl);
		        Statement stmt = conn.createStatement();) 
		    {
		      stmt.executeUpdate(query);
		      System.out.println("Record Updated");
		      stmt.close();
	            conn.close();
		    } catch (SQLException e) 
		    {
		      e.printStackTrace();
		    }
	 }
  }
