import java.sql.*;
import javax.swing.*;
import java.util.Scanner;
//import java.sql.DriverManager;
/*
CSCE 315
9-25-2019
 */
public class jdbcpostgreSQLGUI {
  public static void main(String args[]) {
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
     
     JOptionPane.showMessageDialog(null,"Opened database successfully");
     String columnName = "business_id";
     //JOptionPane.showMessageDialog(null,"Enter column name: ");
     columnName = JOptionPane.showInputDialog(null, "Enter columnName: ");
     //Scanner myObj = new Scanner(System.in);  // Create a Scanner object
     //columnName = myObj.nextLine();
     //myObj.close();
     
     String fullResult = "---" + columnName + "---\n";
     
     try{
     //create a statement object
       Statement stmt = conn.createStatement();
       //create an SQL statement
       //String sqlStatement = "SELECT " + columnName + " FROM attributes LIMIT 10";
       String sqlStatement = JOptionPane.showInputDialog(null, "Enter SQL Query: ");
       //send statement to DBMS
       ResultSet result = stmt.executeQuery(sqlStatement);

       //OUTPUT
       JOptionPane.showMessageDialog(null,"Output: ");
       //System.out.println("______________________________________");
       while (result.next()) {
         //System.out.println(result.getString(columnName));
         fullResult += result.getString(columnName)+"\n";
         //JOptionPane.showMessageDialog(null, fullResult);
         //columnName += result.getString(columnName)+"\n";
       }
   } catch (Exception e){
     JOptionPane.showMessageDialog(null,"Error accessing Database.");
   }
   JOptionPane.showMessageDialog(null, fullResult);
    //closing the connection
    try {
      conn.close();
      JOptionPane.showMessageDialog(null,"Connection Closed.");
    } catch(Exception e) {
      JOptionPane.showMessageDialog(null,"Connection NOT Closed.");
    }//end try catch
  }//end main
}//end Class
