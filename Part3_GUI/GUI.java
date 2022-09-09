import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.FlowLayout;
import java.awt.Font;

import javax.swing.border.EmptyBorder;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
//import javax.swing.JTextField;

import java.sql.*;
//import javax.swing.*;


public class GUI extends JFrame {

	/**
     *
     */
    private static final long serialVersionUID = 1L;

    private final JPanel contentPane;
    //private final JTextField queryTextInput;

    private static final String[] options = { "Get Businesses and Details", "Get Business IDs and BikeParking" };
    int id = -1;

    // The GUI object:
    public GUI() {

        // Setting the title and general window maintenance:
        setTitle("Night Owl Database");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.setLayout(new BorderLayout(0, 0));
        setContentPane(contentPane);

        // Set the layout and size of this JPanel:
        getContentPane().setLayout(new BorderLayout(0, 0));
        setSize(new Dimension(900, 500));

        // Declaring the left and right sub-panels:
        final JPanel JPup = new JPanel();
        final JPanel JPdown = new JPanel();

        // Add the JPanels to the window:
        getContentPane().add(JPup, BorderLayout.NORTH);
        getContentPane().add(JPdown, BorderLayout.CENTER);
        JPdown.setLayout(new BorderLayout(0, 0));

        // The title JLabel (and centering & bolding it):
        final JLabel titleLabel = new JLabel("*** Welcome to Night Owl Database! ***");
        final Font f = titleLabel.getFont();
        JPup.setLayout(new BorderLayout(0, 0));
        titleLabel.setFont(f.deriveFont(f.getStyle() ^ Font.BOLD));
        titleLabel.setHorizontalAlignment(JLabel.CENTER);
        JPup.add(titleLabel, BorderLayout.NORTH);

        // The 'select a query' JLabel:
        final JLabel selectAQueryLabel = new JLabel("Select a query:");
        JPup.add(selectAQueryLabel, BorderLayout.WEST);

        // The 'enter' button:
        final JButton enterButton = new JButton("Enter");
        JPup.add(enterButton, BorderLayout.EAST);

        // Create and add the sub-JPanel for the top JPanel
        // (to organize the top window elements)
        final JPanel JPup_options = new JPanel();
        JPup.add(JPup_options, BorderLayout.CENTER);
        JPup_options.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

        // Create and add the drop-down menu of query options
        final JComboBox query_options = new JComboBox<>(options);
        JPup_options.add(query_options);
        query_options.setSelectedIndex(0);

        // Create and add the query input text box:
        //queryTextInput = new JTextField();
        //JPup_options.add(queryTextInput);
        //queryTextInput.setColumns(10);

        // Create the main results text box w scroll bar(where we display query results):
        final JTextArea resultBox = new JTextArea();
        final JScrollPane scroll = new JScrollPane(resultBox, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        resultBox.setText("Results will display here.");
        resultBox.setEditable(false);
        JPdown.add(scroll);

        //Action Listener for Menu. Used to select a query.
        query_options.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent e){
                resultBox.setText(query_options.getSelectedItem().toString());
                
                //Get the ID of query in the list
                for(int i = 0; i < options.length; i++) {
                    if (options[i] == query_options.getSelectedItem().toString()){
                        id = i;
                        //id is used later when pressitng the enter button to get the query
                    }
                }
            }
        });

        // Action listener for Button. Established connection to the database and runs query
        enterButton.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
			    // clear the JTextPanes:
			    resultBox.setText("");
			    JOptionPane.showMessageDialog(null, "Sending Query!");
			  
                // Establish DB connection
                final dbSetupExample my = new dbSetupExample();
                Connection conn = null;

                try {
                    Class.forName("org.postgresql.Driver");
                    conn = DriverManager.getConnection("jdbc:postgresql://csce-315-db.engr.tamu.edu/team910_d10_db", my.user,my.pswd);
                    resultBox.setText("Connection Successful...");
                } catch (final Exception f) {
                    f.printStackTrace();
                    System.err.println(f.getClass().getName() + ": " + f.getMessage());
                    System.exit(0);
                    resultBox.setText("Connection Unsuccessful...");
                } // end try catch

                //Build the sql statement\
                String columnName = "";
                String sqlStatement = "";
                String fullResult = "";
                int num_columns = 1;

                try {
                    // create a statement object
                    final Statement stmt = conn.createStatement();

                    // create an SQL statement
                    if(id != -1){
                        if(id == 0){
                            //columnName = "business_id";
                            sqlStatement = "SELECT business_id, bikeparking FROM attributes LIMIT 10";
                            fullResult = "--Business_id, BikeParking--\n";
                            num_columns = 2;

                        } else if(id == 1){
                            //columnName = "business_id";
                            sqlStatement = "SELECT business_id, bikeparking FROM attributes LIMIT 10";
                            fullResult = "--Business_id, BikeParking--\n";
                            num_columns = 2;

                        } else if (id == 2){
                            sqlStatement = "";

                        } else if (id == 3){
                            sqlStatement = "";

                        } // Add more ifs for however many things in the dropdown
                    }

                    // send statement to DBMS
                    final ResultSet result = stmt.executeQuery(sqlStatement);
                    while (result.next()) {
                        //fullResult += result.getString(columnName) + result.getString("bikeparking") + "\n";
                        for(int c = 1; c <= num_columns; c++){
                            if(c == num_columns){
                                fullResult += result.getString(c) + "\n"; 
                            } else {
                                fullResult += result.getString(c) + " ";
                            }
                        }
                    }

                } catch (final Exception f) {
                    resultBox.setText("Error Accessing Database...");
                }
                System.out.println(fullResult);
                resultBox.setText(fullResult);

                // closing the connection
                try {
                    conn.close();
                    //resultBox.setText("Connection Closed.");
                } catch (final Exception f) {
                    resultBox.setText("Connection NOT Closed.");
                }//end try catch
            }
        }); //end of action listener for button

    } //end GUI

}//end class
