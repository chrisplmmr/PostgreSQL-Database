import java.sql.*;
//import javax.swing.JOptionPane;

/*
CSCE 315
9-25-2019 Original
2/7/2020 Update for AWS
 */
public class jdbcpostgreSQL {
  public static void main(String args[]) {
    //dbSetup hides my username and password
    dbSetupExample my = new dbSetupExample();
    //Building the connection
     Connection conn = null;
     try {
        Class.forName("org.postgresql.Driver");
        conn = DriverManager.getConnection("jdbc:postgresql://csce-315-db.engr.tamu.edu/team910_d10_db", my.user, my.pswd);
     } catch (Exception e) {
        e.printStackTrace();
        System.err.println(e.getClass().getName()+": "+e.getMessage());
        System.exit(0);
     }//end try catch
     System.out.println("Opened database successfully");
     String columnName = "business_id";
     try{
     //create a statement object
       Statement stmt = conn.createStatement();
       //create an SQL statement
       String sqlStatement = "SELECT " + columnName + " FROM attributes LIMIT 10";
       System.out.println(sqlStatement);
       //send statement to DBMS
       ResultSet result = stmt.executeQuery(sqlStatement);

       //OUTPUT
       System.out.println("Business ID's from the Database.");
       System.out.println("______________________________________");
       while (result.next()) {
         System.out.println(result.getString(columnName));
       }
     } catch (Exception e){
       System.out.println("Error accessing Database.");
     }
    //closing the connection
    try {
      conn.close();
      System.out.println("Connection Closed.");
    } catch(Exception e) {
      System.out.println("Connection NOT Closed.");
    }//end try catch
  }//end main
}//end Class