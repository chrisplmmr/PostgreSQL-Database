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

public class GUI extends JFrame {

    private JPanel contentPane;
    private JTextField queryTextInput;

    int id = -1;
    int outID = 0;

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

        // File Menu
        final JComboBox output_menu = new JComboBox<>(Consts.OUTPUT);
        JPup_options.add(output_menu);
        output_menu.setSelectedIndex(0);

        // Create and add the drop-down menu of query options
        final JComboBox query_options = new JComboBox<>(Consts.OPTIONS);
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
                for(int i = 0; i < Consts.NUM_OPTIONS; i++) {
                    if (Consts.OPTIONS[i] == query_options.getSelectedItem().toString()){
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
                for (int i = 0; i < Consts.NUM_OUTPUTS; i++) {
                    if (Consts.OUTPUT[i] == output_menu.getSelectedItem().toString()){
                        outID = i;
                        //id is used later when pressitng the enter button to get the query
                    }
                }
            }
        });

        // Action listener for Button. Establishes connection to the database and runs query
        enterButton.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                // If no option selected, there's nothing to do:
                if (id == -1) return;

			          // clear the JTextPanes:
			          resultBox.setText("");
			          JOptionPane.showMessageDialog(null, "Sending Query!");

                // Establish DB connection:
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

                //Build the sql statement:
                String columnName = "";
                String sqlStatement = Consts.SQL_STATEMENTS[id];
                StringBuffer fullResult = new StringBuffer(Consts.RESULT_STRINGS[id]);
                int num_columns = Consts.NUM_COLUMNS[id];
                String limit_number = queryTextInput.getText();
                try {
                    // create a statement object
                    final Statement stmt = conn.createStatement();

                    // create an SQL statement:
                    limit_number = "0" + limit_number;
                    if (Integer.parseInt(limit_number) > 0) {
                        sqlStatement += " LIMIT " + Integer.parseInt(limit_number);
                    }

                    // send statement to DBMS
                    final ResultSet result = stmt.executeQuery(sqlStatement);
                    while (result.next()) {
                        //fullResult += result.getString(columnName) + result.getString("bikeparking") + "\n";
                        for(int c = 1; c <= num_columns; c++){
                            String piece = result.getString(c);
                            if(c == num_columns){
                                //f = new Formatter();
                                //fullResult += String.format("%-50s", piece) + "\n";
                                fullResult.append(piece + "\n");
                            } else {
                                //f = new Formatter();
                                //fullResult += String.format("%-50s", piece) + " ";
                                fullResult.append(piece + " | ");
                            }
                        }
                    }

                } catch (final Exception f) {
                    resultBox.setText("Error Accessing Database...");
                }

                //Output Decider
                // System.out.println(fullResult);
                //if ((Integer.parseInt(limit_number) <= 100) && (Integer.parseInt(limit_number)>0))
                //    resultBox.setText(fullResult);
                if(outID == 0){
                    resultBox.setText(fullResult.toString());
                }
                else {
                    try {
                        PrintStream o = new PrintStream(new File("DB_Output.txt"));
                        System.setOut(o);
                        System.out.println(fullResult);
                        resultBox.setText("Write to file successful");
                    } catch (final Exception f) {
                        resultBox.setText("Cannot write to file");
                    }
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
