/* Question1.java
 * Notes:
 *   - 'bid' (as in start_bid) refers to business_id
 *   - if there are multiple businesses found for start or finish, the first one is picked
 *   - Note to self: to use LinkedList as a queue, use add() and poll() as enqueue() and dequeue()
*/

import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList; // used as a queue here
import javafx.util.Pair;

public class Question1 {
  // a hyperparameter for limit on queries:
  private static final String LIMIT_STR = " LIMIT 3";
  // basically space-efficient 'enums' to make code more readable:
  private static final boolean USER = true;
  private static final boolean BUSINESS = false;

  // some useful constants:
  private static final String GET_BUSINESS_ID_QUERY = "SELECT service.business_id FROM service INNER JOIN location ON service.business_id = location.business_id WHERE service.name = '%s' AND location.city = '%s' AND location.state = '%s';";
  private static final String GET_USERS_AT_BUSINESS_QUERY = "SELECT user_id FROM review WHERE business_id = '%s' AND stars > 3" + LIMIT_STR + ";";
  private static final String GET_BUSINESSES_FROM_USER_QUERY = "SELECT business_id FROM review WHERE user_id = '%s'" + LIMIT_STR + ";";

  // private variables:
  private String start_bid;
  private String end_bid;
  private HashMap<String, Boolean> visited = new HashMap<>();
  private HashMap<Pair<String, Boolean>, Pair<String, Boolean>> predecessor = new HashMap<>();
  private LinkedList<Pair<String, Boolean>> to_search = new LinkedList<>();
  private dbSetupExample my;
  private Connection conn = null;

  // the constructor for this solver class. takes in strings in the format
  // "Business name, City State"
  public Question1(String start_business, String end_business) {
    // temporary Strings used for parsing:
    String start_business_name = "";
    String end_business_name = "";
    String start_business_city = "";
    String end_business_city = "";
    String start_business_state = "";
    String end_business_state = "";

    // parse data out of start_business and end_business:
    try {
      start_business_name = start_business.substring(0, start_business.indexOf(","));
      end_business_name = end_business.substring(0, end_business.indexOf(","));
      start_business_city = start_business.substring(
             start_business.indexOf(",") + 2, start_business.lastIndexOf(" "));
      end_business_city = end_business.substring(
             end_business.indexOf(",") + 2, end_business.lastIndexOf(" "));
      start_business_state = start_business.substring(start_business.lastIndexOf(" ") + 1);
      end_business_state = end_business.substring(end_business.lastIndexOf(" ") + 1);

      // sanitize the data (carl's jr., for example, escapes the single quote in the query):
      start_business_name = start_business_name.replace("'", "''");
      end_business_name = end_business_name.replace("'", "''");
      start_business_city = start_business_city.replace("'", "''");
      end_business_city = end_business_city.replace("'", "''");
      start_business_state = start_business_state.replace("'", "''");
      end_business_state = end_business_state.replace("'", "''");
    } catch (Exception e) {
      System.out.println("Invalid input.");
    }

    // establish connection to the database:
    my = new dbSetupExample();
    try {
        Class.forName("org.postgresql.Driver");
        conn = DriverManager.getConnection("jdbc:postgresql://csce-315-db.engr.tamu.edu/team910_d10_db", my.user,my.pswd);
        System.out.println("Connection Successful.");
    } catch (final Exception f) {
        f.printStackTrace();
        System.err.println(f.getClass().getName() + ": " + f.getMessage());
        System.exit(0);
        System.out.println("Connection Unsuccessful.");
    }

    // easy way to get the business ids:
    String get_start_bid_query = String.format(GET_BUSINESS_ID_QUERY,
                                               start_business_name,
                                               start_business_city,
                                               start_business_state);
    String get_end_bid_query = String.format(GET_BUSINESS_ID_QUERY,
                                               end_business_name,
                                               end_business_city,
                                               end_business_state);

    try {
      start_bid = runQuery(get_start_bid_query).get(0);
      end_bid = runQuery(get_end_bid_query).get(0);
    } catch (Exception e) {
      System.out.println("No such business found.");
    }
  }

  // slows things down but allows for abstraction while writing this
  private ArrayList<String> runQuery(String query) {
    ArrayList<String> out = new ArrayList<>();
    try {
        // create a statement object
        final Statement stmt = conn.createStatement();

        // send statement to DBMS
        final ResultSet result = stmt.executeQuery(query);
        while (result.next()) {
            out.add(result.getString(1));
        }
    } catch (final Exception f) {
        System.out.println("Error Accessing Database...");
    }

    return out;
  }

  public ArrayList<String> solve() {
    // declare the shortest path ArrayList (which is what we'll return):
    ArrayList<String> shortest_path = new ArrayList<>();

    // add the start business id to our search queue:
    to_search.add(new Pair<String, Boolean>(start_bid, BUSINESS));
    visited.put(start_bid, BUSINESS);

    // this is a temporary pair to store the current node we're exploring:
    Pair<String, Boolean> cur;

    // breadth-first search:
    while ((cur = to_search.poll()) != null) {
      // if cur is a business:
      if (cur.getValue() == BUSINESS) {
        // get a list of all users that have been to this business:
        ArrayList<String> results = runQuery(String.format(GET_USERS_AT_BUSINESS_QUERY, cur.getKey()));

        // add each user id to the queue:
        for (String user_id : results) {
          // if this user has not been visited:
          if (visited.get(user_id) == null) {
            // mark this user as visited:
            visited.put(user_id, USER);

            // mark the predecessor of this user:
            predecessor.put(new Pair<String, Boolean>(user_id, USER), cur);

            // push this user to our search queue:
            to_search.add(new Pair<String, Boolean>(user_id, USER));
            System.out.println("DEBUG: added user " + user_id + " to queue.");
          }
        }
      }
      // if cur is a user:
      else {
        // get a list of all businesses that this user has been to:
        ArrayList<String> results = runQuery(String.format(GET_BUSINESSES_FROM_USER_QUERY, cur.getKey()));

        // add each business id to the queue:
        for (String business_id : results) {
          // if this business has not been visited:
          if (visited.get(business_id) == null) {
            // mark this business as visited:
            visited.put(business_id, BUSINESS);

            // mark the predecessor of this business:
            predecessor.put(new Pair<String, Boolean>(business_id, BUSINESS), cur);

            // push this business to our search queue:
            to_search.add(new Pair<String, Boolean>(business_id, BUSINESS));
            System.out.println("DEBUG: added business " + business_id + " to queue.");

            // if this business is the end goal:
            if (business_id.equals(end_bid)) {
              // backtrack to find the shortest path:
              String cur_bid = end_bid;
              boolean cur_status = BUSINESS;

              // add the end business id to the shortest path:
              shortest_path.add(end_bid);

              while (predecessor.get(new Pair<String, Boolean>(cur_bid, cur_status)).getKey() != start_bid) {
                cur_bid = predecessor.get(new Pair<String, Boolean>(cur_bid, cur_status)).getKey();
                cur_status = !cur_status;

                // log this path:
                shortest_path.add(cur_bid);
              }

              // add the start business id to the shortest path:
              shortest_path.add(start_bid);

              // the shortest path list is now backwards, so we reverse it:
              Collections.reverse(shortest_path);

              // return the shortest path:
              return shortest_path;
            }
          }
        }
      }
    }

    // otherwise, nothing was found:
    shortest_path.add("NO PATH");
    return shortest_path;
  }
}
