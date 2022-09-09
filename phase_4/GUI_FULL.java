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
import java.util.*;
//import javax.swing.*;




public class GUI_FULL extends JFrame {


    public static HashMap<String, Double> sortByValue(HashMap<String, Double> hm) { 
        /*Help from GeeksForGeeks.com (https://www.geeksforgeeks.org/sorting-a-hashmap-according-to-values/) */
        // Create a list from elements of HashMap
        List<Map.Entry<String, Double>> list = new LinkedList<Map.Entry<String, Double>>(hm.entrySet());

        // Sort the list
        Collections.sort(list, new Comparator<Map.Entry<String, Double>>() {
            public int compare(Map.Entry<String, Double> spread1, Map.Entry<String, Double> spread2) {
            return (spread2.getValue()).compareTo(spread1.getValue());
            }
        });

        // put data from sorted list to hashmap
        HashMap<String, Double> temp = new LinkedHashMap<String, Double>();
        for (Map.Entry<String, Double> aa : list) {
            temp.put(aa.getKey(), aa.getValue());
        }
        return temp;
    }


    static double computeSpread(String lat1, String longi1, String lat2, String longi2) {
        double latitude1 = Double.parseDouble(lat1);
        double longitude1 = Double.parseDouble(longi1);
        double latitude2 = Double.parseDouble(lat2);
        double longitude2 = Double.parseDouble(longi2);
        double spreadVal = 0;
        double a = Math.abs(latitude1 - latitude2);
        double b = Math.abs(longitude1 - longitude2);
        spreadVal = Math.sqrt((a * a) + (b * b));
        return spreadVal;
    }

    static double returnMaxSpread(double curSpread, double newSpread) {
        if (curSpread <= newSpread) {
            curSpread = newSpread; // Set current spread to the max
        }
        return curSpread;
    }


	/**
     *
     */
    private static final long serialVersionUID = 1L;

    private final JPanel contentPane;
    private final JTextField queryTextInput;

    private static final String[] options = { "question 1 (not implement yet)", "question 2", "question 3", "question 4", "question 5"};
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
                String finalResult = "";
                int num_columns = 1;
                String user_input = queryTextInput.getText();
                try {
                    // create a statement object
                    final Statement stmt = conn.createStatement();

                    // create an SQL statement
                    if(id != -1){
                        if(id == 0){ //question 1

                            // String[] arr_input = user_input.split("->", 2);
                            // if (arr_input[1].charAt(0)==' ') {
                            //     arr_input[1] = arr_input[1].substring(1);
                            // }
                            // Question1 q = new Question1(arr_input[0], arr_input[1]);
                            // ArrayList<String> shortest_path = q.solve();
                            ;


                            // question 1
                            // put whatever you want to put to the GUI to (string) finalResult
                        } else if(id == 1){ //question 2
                            String[] arr_input = user_input.split(",", 2);
                            if (arr_input[1].charAt(0)==' ') {
                                arr_input[1] = arr_input[1].substring(1);
                            }

                            if (Integer.parseInt(arr_input[0])==1) {
                                sqlStatement = "SELECT * FROM customer where review_count > 5 AND name=\'"+ arr_input[1] +"\' limit 100";
                                finalResult = " user_id | name | review_count | yelping_since | fans | average_stars \n";
                                num_columns = 6;
                                final ResultSet result = stmt.executeQuery(sqlStatement);
                                finalResult+= resultSetToString(result, num_columns);
                            }
                            else {
                                sqlStatement = "SELECT * FROM review where user_id=\'"+ arr_input[1] +"\'";
                                finalResult = " review_id | user_id | business_id | stars | text | data | useful | funny | cool | \n";
                                num_columns = 9;
                                final ResultSet result = stmt.executeQuery(sqlStatement);
                                int total_cool = 0;
                                int total_funny= 0;
                                int total_useful = 0;
                                int total_char = 0;
                                int count_line = 0;
                                while (result.next()) {
                                    total_cool += Integer.parseInt(result.getString(9)); //get string then change to int (this is dumb, fix later)
                                    total_funny += Integer.parseInt(result.getString(8));
                                    total_useful += Integer.parseInt(result.getString(7));
                                    total_char += result.getString(5).length();
                                    count_line += 1;
                                }
                                // n_UcypOj7YW8gdR43XIUSQ user id used for testing
                                finalResult += "Total review found: " + Integer.toString(count_line) + "\n";
                                finalResult += "Total cool reactions: " + Integer.toString(total_cool) + "\n";
                                finalResult += "Total funny reactions: " + Integer.toString(total_funny) + "\n";
                                finalResult += "Total useful reactions: " + Integer.toString(total_useful) + "\n";
                                finalResult += "Average length of each comment: " + Double.toString(total_char*1.0/count_line) + "\n";
                            }

                        } else if (id == 2){ //question 3
                            HashMap<String, ArrayList<ArrayList<String>>> validPlaces = new HashMap<String, ArrayList<ArrayList<String>>>();
                            HashMap<String, Double> validPlaceSpreads = new HashMap<String, Double>();
                            String state = user_input;
                            sqlStatement = "SELECT name, COUNT (name) FROM ( SELECT * FROM( SELECT service.name, service.stars, full_location.state, full_location.latitude, full_location.longitude FROM service INNER JOIN full_location ON service.business_id = full_location.business_id) AS getLocations WHERE state = '"+ state +"' AND stars >=3.5) AS filteredLocations GROUP BY name HAVING COUNT(name) > 5 ORDER BY COUNT(name) DESC";
                            ResultSet result = stmt.executeQuery(sqlStatement);
                            num_columns = 2;
                            while (result.next()) {
                                for (int c = 1; c <= num_columns; c++) {
                                    String piece = result.getString(c);
                                    if (c == num_columns) {
                                        // finalResult += piece + "\n";
                                        ;
                                    } else {
                                        validPlaces.put(piece, new ArrayList<ArrayList<String>>());
                                        validPlaceSpreads.put(piece, 0.0); // new
                                        // finalResult += piece + " | ";
                                    }
                                }
                            }
                            sqlStatement = "SELECT * FROM(	SELECT service.name, service.stars, full_location.state, full_location.latitude, full_location.longitude FROM service INNER JOIN full_location ON service.business_id = full_location.business_id) AS getLocations WHERE state = '"+ state +"' AND stars >=3.5";
                            result = null;
                            result = stmt.executeQuery(sqlStatement);
                            num_columns = 5;
                            String piece2 = "";
                            while (result.next()) {
                                String key = "";

                                ArrayList<String> pair = new ArrayList<>();
                                pair.add("");
                                pair.add("");

                                for (int c = 1; c <= num_columns; c++) {

                                    piece2 = result.getString(c);

                                    if (c == 1) { // Adds a location for the restraunt
                                        if (validPlaces.containsKey(piece2)) {
                                            validPlaces.get(piece2).add(pair);
                                            key = piece2;
                                        }
                                    } else if (c == 4) { // Get latitude and add
                                        if (validPlaces.containsKey(key)) {
                                            validPlaces.get(key).get(validPlaces.get(key).size() - 1).set(0, piece2);
                                            // System.out.println(piece2);
                                        }
                                    } else if (c == 5) { // Get longitude then add
                                        if (validPlaces.containsKey(key)) {
                                            validPlaces.get(key).get(validPlaces.get(key).size() - 1).set(1, piece2);
                                            // System.out.println(piece2);
                                        }
                                    }

                                }
                            } // End hashing
                            Iterator hashIT = validPlaces.entrySet().iterator();
                            
                            while (hashIT.hasNext()) {
                                Map.Entry mentry = (Map.Entry) hashIT.next();
                                String key = mentry.getKey().toString();

                                //System.out.print("key is: " + key + ", Spread is: ");

                                for (int i = 0; i < validPlaces.get(key).size(); i++) {
                                    for (int j = i + 1; j < validPlaces.get(key).size(); j++) {
                                        double spread = computeSpread(validPlaces.get(key).get(i).get(0), validPlaces.get(key).get(i).get(1),
                                        validPlaces.get(key).get(j).get(0), validPlaces.get(key).get(j).get(1));
                                        double finalSpread = returnMaxSpread(validPlaceSpreads.get(key), spread);
                                        validPlaceSpreads.replace(key, finalSpread);
                                    }
                                }
                            }

                            Map<String, Double> sortedMap = sortByValue(validPlaceSpreads);
                            int topFive = 0;
                            for (Map.Entry<String, Double> en : sortedMap.entrySet()) { 
                                if(topFive >=5 ){ break; }
                                topFive++;
                                finalResult += topFive + ". Franchise: " + en.getKey() +  ", spread: " + en.getValue() + "\n"; 
                                
                            }




                        } else if (id == 3){ //question 4
                            String cityname = user_input;
                            sqlStatement = "select name, address, count from (local_restaurant INNER JOIN tip_summary on local_restaurant.business_id = tip_summary.business_id) Where city ='" + cityname + "' ORDER BY count DESC LIMIT 10;";
                            num_columns = 3;
                            finalResult = "--Business Name, Address, Tips--\n";
                            final ResultSet tip_review = stmt.executeQuery(sqlStatement);
                            finalResult += resultSetToString(tip_review, num_columns);

                        } else if (id == 4) {  //question 5
                            String cityname = user_input;
                            sqlStatement = "WITH businesses AS (SELECT service.name, COUNT(service.name) " +
                                "FROM (((full_location INNER JOIN service " +
                                "ON full_location.business_id = service.business_id) " +
                                "INNER JOIN hours ON full_location.business_id = hours.business_id) " +
                                "INNER JOIN attributes ON full_location.business_id = attributes.business_id) " +
                                "WHERE city = '" + cityname + "' " +
                                "and hours.monday IS NOT NULL " +
                                "and hours.tuesday IS NOT NULL " +
                                "and hours.wednesday IS NOT NULL " +
                                "and hours.thursday IS NOT NULL " +
                                "and hours.friday IS NOT NULL " +
                                "and hours.saturday IS NOT NULL " +
                                "and hours.sunday IS NOT NULL " +
                                "and char_length(attributes.goodforkids) = 4 " +
                                "GROUP BY service.name ORDER BY COUNT ASC), " +
                                "loc_businesses AS (SELECT businesses.name FROM businesses WHERE businesses.count = 1) " +
                                "SELECT service.name, AVG(service.stars) FROM (service " +
                                "INNER JOIN loc_businesses ON service.name = loc_businesses.name) " +
                                "GROUP BY service.name ORDER BY AVG DESC LIMIT 10";

                            num_columns = 2;
                            finalResult = "--Business Name, Rating--\n";

                            // send statement to DBMS
                            final ResultSet bus_names = stmt.executeQuery(sqlStatement);
                            finalResult += resultSetToString(bus_names, num_columns);
                        }
                    }

                } catch (final Exception f) {
                    resultBox.setText("Error Accessing Database...");
                }

                //Output Decider
                // System.out.println(finalResult);
                //if ((Integer.parseInt(limit_number) <= 100) && (Integer.parseInt(limit_number)>0))
                //    resultBox.setText(finalResult);
                if(outID == 0){
                    resultBox.setText(finalResult);
                }
                else {
                    try {
                        PrintStream o = new PrintStream(new File("DB_Output.txt"));
                        System.setOut(o); 
                        System.out.println(finalResult);
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

    public String resultSetToString(ResultSet result, int num_columns) throws SQLException{
        String finalResult = "";
        while (result.next()) {
            for (int c = 1; c <= num_columns; c++) {
                String piece = result.getString(c);
                if (c == num_columns) {
                    finalResult += piece + "\n";
                } else {
                    finalResult += piece + " | ";
                }
            }
        }
        return finalResult;
    }
}//end class