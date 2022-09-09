/*Help from GeeksForGeeks.com (https://www.geeksforgeeks.org/sorting-a-hashmap-according-to-values/) */
public class Question_3 {

  static double computeSpread(String lat1, String longi1, String lat2, String longi2) {
    double latitude1 = Double.parseDouble(lat1);
    double longitude1 = Double.parseDouble(longi1);
    double latitude2 = Double.parseDouble(lat2);
    double longitude2 = Double.parseDouble(longi2);
    double spreadVal = 0;
    double a = Math.abs(latitude1 - latitude2);
    double b = Math.abs(longitude1 - longitude2);
    spreadVal = Math.sqrt((a * a) + (b * b));

    // Compute the distance
    return spreadVal;
  }

  static double returnMaxSpread(double curSpread, double newSpread) {
    if (curSpread <= newSpread) {
      curSpread = newSpread; // Set current spread to the max
    }
    return curSpread;
  }

  // Sort hashmap
  public static HashMap<String, Double> sortByValue(HashMap<String, Double> hm) {
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

  public static void main(String args[]) {

    // dbSetup hides my username and password
    dbSetupExample my = new dbSetupExample();
    HashMap<String, ArrayList<ArrayList<String>>> validPlaces = new HashMap<String, ArrayList<ArrayList<String>>>();
    HashMap<String, Double> validPlaceSpreads = new HashMap<String, Double>();
    String state = "AZ";

    // Building the connection
    Connection conn = null;
    try {
      Class.forName("org.postgresql.Driver");
      conn = DriverManager.getConnection("jdbc:postgresql://csce-315-db.engr.tamu.edu/team910_d10_db", my.user,
          my.pswd);
    } catch (Exception e) {
      e.printStackTrace();
      System.err.println(e.getClass().getName() + ": " + e.getMessage());
      System.exit(0);
    } // end try catch

    System.out.println("Opened database successfully");
    // String columnName = "business_id";
    String fullResult = "";

    try {
      // create a statement object
      Statement stmt = conn.createStatement();

      // create an SQL statement
      String sqlStatement = "SELECT name, COUNT (name) FROM ( SELECT * FROM( SELECT service.name, service.stars, full_location.state, full_location.latitude, full_location.longitude FROM service INNER JOIN full_location ON service.business_id = full_location.business_id) AS getLocations WHERE state = '"+ state +"' AND stars >=3.5) AS filteredLocations GROUP BY name HAVING COUNT(name) > 5 ORDER BY COUNT(name) DESC";

      System.out.println("Executing first statment...");

      // send statement to DBMS
      ResultSet result = stmt.executeQuery(sqlStatement);

      // OUTPUT
      int num_columns = 2;
      while (result.next()) {
        for (int c = 1; c <= num_columns; c++) {
          String piece = result.getString(c);
          if (c == num_columns) {
            fullResult += piece + "\n";
          } else {
            validPlaces.put(piece, new ArrayList<ArrayList<String>>());
            validPlaceSpreads.put(piece, 0.0); // new
            fullResult += piece + " | ";
          }
        }
      }
      System.out.println();
      //System.out.print(fullResult);
      System.out.println();

      // Find location differences
      sqlStatement = "SELECT * FROM(	SELECT service.name, service.stars, full_location.state, full_location.latitude, full_location.longitude FROM service INNER JOIN full_location ON service.business_id = full_location.business_id) AS getLocations WHERE state = 'AZ' AND stars >=3.5";
      result = null;
      System.out.println("Executing second statment...");
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

    } catch (Exception e) {
      System.out.println("Error accessing Database.");
    }

    // closing the connection
    try {
      conn.close();
      System.out.println("Connection Closed.");
    } catch (Exception e) {
      System.out.println("Connection NOT Closed.");
    } // end try catch

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
      //System.out.println(validPlaceSpreads.get(key));
    }

    System.out.println();
    Map<String, Double> sortedMap = sortByValue(validPlaceSpreads);
    int topFive = 0;
    for (Map.Entry<String, Double> en : sortedMap.entrySet()) { 
      if(topFive >=5 ){ break; }
      System.out.println("Key: " + en.getKey() +  ", Value: " + en.getValue()); 
      topFive++;
    }
    

  }//end main
}//end Class
