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
import javax.swing.JTextField;

import java.sql.*;
import java.io.*; 
//import javax.swing.*;


public class GUI_FULL extends JFrame {

	/**
     *
     */
    private static final long serialVersionUID = 1L;

    private final JPanel contentPane;
    private final JTextField queryTextInput;

    private static final String[] options = { "question 0", "question 1", "question 2", "question 3" };
    private static final String[] output = { "Text", "File"};
    
    int id = -1;
    int outID = 0;

    // The GUI object:
    public GUI_FULL() {

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

        // File Menu
        final JComboBox output_menu = new JComboBox<>(output);
        JPup_options.add(output_menu);
        output_menu.setSelectedIndex(0);

        // Create and add the drop-down menu of query options
        final JComboBox query_options = new JComboBox<>(options);
        JPup_options.add(query_options);
        query_options.setSelectedIndex(-1);

        // Create and add the query input text box:
        queryTextInput = new JTextField();
        JPup_options.add(queryTextInput);
        queryTextInput.setColumns(10);

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

        //Action Listener for Menu. Used to select a query.
        output_menu.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent e){
                //resultBox.setText(output_menu.getSelectedItem().toString());
                
                //Get the ID of query in the list
                for(int i = 0; i < output.length; i++) {
                    if (output[i] == output_menu.getSelectedItem().toString()){
                        outID = i;
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
			    //JOptionPane.showMessageDialog(null, "Sending Query!");
			  
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
                String user_input = queryTextInput.getText();
                try {
                    // create a statement object
                    final Statement stmt = conn.createStatement();

                    // create an SQL statement
                    if(id != -1){
                        if(id == 0){
                            fullResult = "question 0";
                        } else if(id == 1){
                            fullResult = "question 1";
                        } else if (id == 2){
                            fullResult = "question 2";
                        } else if (id == 3){
                            fullResult = "question 3";
                        } // Add more ifs for however many things in the dropdown
                    }

                } catch (final Exception f) {
                    resultBox.setText("Error Accessing Database...");
                }

                if(outID == 0){
                    resultBox.setText(outID + " " + fullResult + " " + user_input);
                }

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
