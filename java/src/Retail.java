/*
 * Template JAVA User Interface
 * =============================
 *
 * Database Management Systems
 * Department of Computer Science &amp; Engineering
 * University of California - Riverside
 *
 * Target DBMS: 'Postgres'
 *
 */


import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;
import java.util.ArrayList;
import java.lang.Math;

/**
 * This class defines a simple embedded SQL utility class that is designed to
 * work with PostgreSQL JDBC drivers.
 */
public class Retail {
   // reference to physical database connection.
   private Connection _connection = null;

   // handling the keyboard inputs through a BufferedReader
   // This variable can be global for convenience.
   static BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

   /**
    * Creates a new instance of Retail shop
    *
    * @param hostname the MySQL or PostgreSQL server hostname
    * @param database the name of the database
    * @param username the user name used to login to the database
    * @param password the user login password
    * @throws java.sql.SQLException when failed to make a connection.
    */
   public Retail(String dbname, String dbport, String user, String passwd) throws SQLException {
      System.out.print("Connecting to database...");
      try{
         // constructs the connection URL
         String url = "jdbc:postgresql://localhost:" + dbport + "/" + dbname;
         System.out.println ("Connection URL: " + url + "\n");

         // obtain a physical connection
         this._connection = DriverManager.getConnection(url, user, passwd);
         System.out.println("Done");
      }catch (Exception e){
         System.err.println("Error - Unable to Connect to Database: " + e.getMessage() );
         System.out.println("Make sure you started postgres on this machine");
         System.exit(-1);
      }//end catch
   }//end Retail

   // Method to calculate euclidean distance between two latitude, longitude pairs. 
   public static double calculateDistance (double lat1, double long1, double lat2, double long2){
      double t1 = (lat1 - lat2) * (lat1 - lat2);
      double t2 = (long1 - long2) * (long1 - long2);
      return Math.sqrt(t1 + t2); 
   }

   /**
    * Method to execute an update SQL statement.  Update SQL instructions
    * includes CREATE, INSERT, UPDATE, DELETE, and DROP.
    *
    * @param sql the input SQL string
    * @throws java.sql.SQLException when update failed
    */
   public void executeUpdate (String sql) throws SQLException {
      // creates a statement object
      Statement stmt = this._connection.createStatement ();

      // issues the update instruction
      stmt.executeUpdate (sql);

      // close the instruction
      stmt.close ();
   }//end executeUpdate

   /**
    * Method to execute an input query SQL instruction (i.e. SELECT).  This
    * method issues the query to the DBMS and outputs the results to
    * standard out.
    *
    * @param query the input query string
    * @return the number of rows returned
    * @throws java.sql.SQLException when failed to execute the query
    */
   public int executeQueryAndPrintResult (String query) throws SQLException {
      // creates a statement object
      Statement stmt = this._connection.createStatement ();

      // issues the query instruction
      ResultSet rs = stmt.executeQuery (query);

      /*
       ** obtains the metadata object for the returned result set.  The metadata
       ** contains row and column info.
       */
      ResultSetMetaData rsmd = rs.getMetaData ();
      int numCol = rsmd.getColumnCount ();
      int rowCount = 0;

      // iterates through the result set and output them to standard out.
      boolean outputHeader = true;
      while (rs.next()){
         if(outputHeader){
            for(int i = 1; i <= numCol; i++) {
	       System.out.print(rsmd.getColumnName(i) + "\t");
	    }
	    System.out.println();
	    outputHeader = false;
	 }
         for (int i=1; i<=numCol; ++i) {
            System.out.print (rs.getString (i) + "\t");
         }
         System.out.println ();
         ++rowCount;
      }//end while
      stmt.close ();
      return rowCount;
   }//end executeQuery

   /**
    * Method to execute an input query SQL instruction (i.e. SELECT).  This
    * method issues the query to the DBMS and returns the results as
    * a list of records. Each record in turn is a list of attribute values
    *
    * @param query the input query string
    * @return the query result as a list of records
    * @throws java.sql.SQLException when failed to execute the query
    */
   public List<List<String>> executeQueryAndReturnResult (String query) throws SQLException {
      // creates a statement object
      Statement stmt = this._connection.createStatement ();

      // issues the query instruction
      ResultSet rs = stmt.executeQuery (query);

      /*
       ** obtains the metadata object for the returned result set.  The metadata
       ** contains row and column info.
       */
      ResultSetMetaData rsmd = rs.getMetaData ();
      int numCol = rsmd.getColumnCount ();
      int rowCount = 0;

      // iterates through the result set and saves the data returned by the query.
      boolean outputHeader = false;
      List<List<String>> result  = new ArrayList<List<String>>();
      while (rs.next()) {
         List<String> record = new ArrayList<String>();
	 for (int i=1; i<=numCol; ++i) {
	    record.add(rs.getString (i));
         }
         result.add(record);
      }//end while
      stmt.close ();
      return result;
   }//end executeQueryAndReturnResult

   /**
    * Method to execute an input query SQL instruction (i.e. SELECT).  This
    * method issues the query to the DBMS and returns the number of results
    *
    * @param query the input query string
    * @return the number of rows returned
    * @throws java.sql.SQLException when failed to execute the query
    */
   public int executeQuery (String query) throws SQLException {
       // creates a statement object
       Statement stmt = this._connection.createStatement ();

       // issues the query instruction
       ResultSet rs = stmt.executeQuery (query);

       int rowCount = 0;

       // iterates through the result set and count nuber of results.
       while (rs.next()){
          rowCount++;
       }//end while
       stmt.close ();
       return rowCount;
   }

   /**
    * Method to fetch the last value from sequence. This
    * method issues the query to the DBMS and returns the current
    * value of sequence used for autogenerated keys
    *
    * @param sequence name of the DB sequence
    * @return current value of a sequence
    * @throws java.sql.SQLException when failed to execute the query
    */
   public int getCurrSeqVal(String sequence) throws SQLException {
	Statement stmt = this._connection.createStatement ();

	ResultSet rs = stmt.executeQuery (String.format("Select currval('%s')", sequence));
	if (rs.next()) {
	   return rs.getInt(1);
        }
	return -1;
   }

   /**
    * Method to close the physical connection if it is open.
    */
   public void cleanup(){
      try{
         if (this._connection != null){
            this._connection.close ();
         }//end if
      }catch (SQLException e){
         // ignored.
      }//end try
   }//end cleanup


   /**
    * user variables for main function
    */

   static String userName = null;
   static String userId = null;
   static String userType = null;
   static float userLongitude = 0;
   static float userLatitude = 0;

   /**
    * The main execution method
    *
    * @param args the command line arguments this inclues the <mysql|pgsql> <login file>
    */
   public static void main (String[] args) {
      if (args.length != 3) {
         System.err.println (
            "Usage: " +
            "java [-classpath <classpath>] " +
            Retail.class.getName () +
            " <dbname> <port> <user>");
         return;
      }//end if

      Greeting();
      Retail esql = null;
      try{
         // use postgres JDBC driver.
         Class.forName ("org.postgresql.Driver").newInstance ();
         // instantiate the Retail object and creates a physical
         // connection.
         String dbname = args[0];
         String dbport = args[1];
         String user = args[2];
         esql = new Retail (dbname, dbport, user, "");

         boolean keepon = true;
         while(keepon) {
	    // These are sample SQL statements
            if(userName == null && userType == null) {
               System.out.println("MAIN MENU");
               System.out.println("---------");
               System.out.println("1. Create user");
               System.out.println("2. Log in");
               System.out.println("9. < EXIT");
	       String authorisedUser = null;
               switch (readChoice()){
                  case 1: CreateUser(esql); break;
                  case 2: authorisedUser = LogIn(esql); break;
                  case 9: keepon = false; break;
                  default : System.out.println("Unrecognized choice!"); break;
               }//end switch
	    }
            if (userName != null && userType != null) { // if the user has logged in, print usermenu based on user type
               boolean usermenu = true;
               while(usermenu) {
		  if(userType.matches(".*manager.*") || userType.matches(".*customer.*")) { // if the user is either a customer or a manager
                     System.out.println("MAIN MENU");
                     System.out.println("---------");
                     System.out.println("1. View Stores Within 30 Miles");
                     System.out.println("2. View Product List");
                     System.out.println("3. Place an Order");
                     System.out.println("4. View 5 Recent Orders");

		     if(userType.matches(".*manager.*")) { // if the user is a manager, print additional menu items for manager
                        //the following functionalities basically used by managers
                        System.out.println("5. View Managed Stores");
			System.out.println("6. Update Product");
                        System.out.println("7. View 5 Recent Product Updates Info");
                        System.out.println("8. View 5 Popular Items");
                        System.out.println("9. View 5 Popular Customers");
                        System.out.println("10. Place Product Supply Request to Warehouse");
                     }
                     System.out.println(".........................");
                     System.out.println("20. Log out");
		     if(userType.matches(".*manager.*")) { // execute methods based on user type
                        switch (readChoice()) {
                           case 1: viewStores(esql); break;
                           case 2: viewProducts(esql); break;
                           case 3: placeOrder(esql); break;
                           case 4: viewRecentOrders(esql); break;
			   case 5: viewManagedStores(esql); break;
                           case 6: updateProduct(esql); break;
                           case 7: viewRecentUpdates(esql); break;
                           case 8: viewPopularProducts(esql); break;
                           case 9: viewPopularCustomers(esql); break;
                           case 10: placeProductSupplyRequests(esql); break;

                           case 20: 
			      usermenu = false; 
                              userName = null;
                              userId = null;
                              userType = null;
                              userLatitude = 0;
                              userLongitude = 0;
			      break;
                           default : System.out.println("Unrecognized choice!"); break;
                        }
	             } else {
		        switch(readChoice()) {
                           case 1: viewStores(esql); break;
                           case 2: viewProducts(esql); break;
                           case 3: placeOrder(esql); break;
                           case 4: viewRecentOrders(esql); break;

                           case 20: 
			      usermenu = false;
			      userName = null;
			      userId = null;
			      userType = null;
			      userLatitude = 0;
			      userLongitude = 0;
			      break;
                           default : System.out.println("Unrecognized choice!"); break;
		        }
		     }
                  } else { // if the user is an admin, print separate menu with separate function calls
                     System.out.println("MAIN MENU");
                     System.out.println("---------");
                     System.out.println("1. View All Stores");
		     System.out.println("2. View All Customers");
                     System.out.println("3. View Product List");
                     System.out.println("4. Place an Order");
                     System.out.println("5. View All Recent Orders");
                     System.out.println("6. Update Product");
		     System.out.println("7. Update User");
		     System.out.println("8. Delete User");
                     System.out.println("9. View All Recent Product Updates Info");
		     System.out.println("10. View All Recent Product Supply Requests Info");
                     System.out.println(".........................");
                     System.out.println("20. Log out");
                     switch (readChoice()) {
                        case 1: viewAllStores(esql); break;
                        case 2: viewAllCustomers(esql); break;
			case 3: viewProducts(esql); break;
                        case 4: placeOrder(esql); break;
                        case 5: viewAllRecentOrders(esql); break;
                        case 6: updateProductAdmin(esql); break;
			case 7: updateUserAdmin(esql); break;
			case 8: deleteUserAdmin(esql); break;
                        case 9: viewAllRecentUpdates(esql); break;
			case 10: viewAllRecentRequests(esql); break;

                        case 20:
                           usermenu = false;
                           userName = null;
                           userId = null;
                           userType = null;
                           userLatitude = 0;
                           userLongitude = 0;
                           break;
                        default : System.out.println("Unrecognized choice!"); break;
                     }
                  }
               } //end while
            }
         }//end while
      }catch(Exception e) {
         System.err.println (e.getMessage ());
      }finally{
         // make sure to cleanup the created table and close the connection.
         try{
            if(esql != null) {
               System.out.print("Disconnecting from database...");
               esql.cleanup ();
               System.out.println("Done\n\nBye !");
            }//end if
         }catch (Exception e) {
            // ignored.
         }//end try
      }//end try
   }//end main

   public static void Greeting(){
      System.out.println(
         "\n\n*******************************************************\n" +
         "              User Interface      	               \n" +
         "*******************************************************\n");
   }//end Greeting

   /*
    * Reads the users choice given from the keyboard
    * @int
    **/
   public static int readChoice() {
      int input;
      // returns only if a correct value is given.
      do {
         System.out.print("Please make your choice: ");
         try { // read the integer, parse it and break.
            input = Integer.parseInt(in.readLine());
            break;
         }catch (Exception e) {
            System.out.println("Your input is invalid!");
            continue;
         }//end try
      }while (true);
      return input;
   }//end readChoice

   /*
    * Creates a new user
    **/
   public static void CreateUser(Retail esql){
      try{
         System.out.print("\tEnter name: ");
         String name = in.readLine();
         System.out.print("\tEnter password: ");
         String password = in.readLine();
         System.out.print("\tEnter latitude: ");   
         String latitude = in.readLine(); //enter lat value between [0.0, 100.0]
         System.out.print("\tEnter longitude: "); //enter long value between [0.0, 100.0]
         String longitude = in.readLine();
         
         String type ="customer";

	 String query = String.format("INSERT INTO Users (name, password, latitude, longitude, type) VALUES ('%s','%s', %s, %s,'%s')", name, password, latitude, longitude, type);
         esql.executeUpdate(query);

         System.out.println ("User successfully created!");
      } catch(Exception e) {
         System.err.println (e.getMessage());
      }
   }//end CreateUser

   /*
    * Check log in credentials for an existing user
    * @return User login or null is the user does not exist
    **/
   public static String LogIn(Retail esql){
      try{
         System.out.print("\tEnter name: ");
         String name = in.readLine();
         System.out.print("\tEnter password: ");
         String password = in.readLine();

         String query = String.format("SELECT * FROM Users WHERE name = '%s' AND password = '%s'", name, password);
	 List<List<String>> executedQuery = esql.executeQueryAndReturnResult(query);
	 if (userName == null) {
            userId = executedQuery.get(0).get(0);
            System.out.print("    User ID: ");
            System.out.println(userId);
            userName = executedQuery.get(0).get(1);
            System.out.print("    User Name: ");
            System.out.println(userName);
            userLatitude = Float.parseFloat(executedQuery.get(0).get(3));
            System.out.print("    User Latitude: ");
            System.out.println(userLatitude);
            userLongitude = Float.parseFloat(executedQuery.get(0).get(4));
            System.out.print("    User Longitude: ");
            System.out.println(userLongitude);
            userType = executedQuery.get(0).get(5);
            System.out.print("    User Type: ");
            System.out.println(userType);
	    String welcome = String.format("Welcome, %s", userName);
	    System.out.println(welcome);
	    return name;
	 }
         return null;
      } catch(Exception e){
	 System.out.println("Invalid credentials");
         System.err.println (e.getMessage());
         return null;
      }
   }//end

// Rest of the functions definition go in here
   /*
   * Prints a list of available stores based on the users location 
   **/
   public static void viewStores(Retail esql) {
      try {
         String query = String.format("SELECT * FROM Store");
	 List<List<String>> executedQuery = esql.executeQueryAndReturnResult(query);
	 List<Integer> withinRadius = new ArrayList<Integer>();
	 for(int i = 0; i < executedQuery.size(); i++) {
	    float sLatitude = Float.parseFloat(executedQuery.get(i).get(2));
	    float sLongitude = Float.parseFloat(executedQuery.get(i).get(3));
	    if(calculateDistance(userLatitude, userLongitude, sLatitude, sLongitude) <= 30) {
	       withinRadius.add(i);
	    }
	 }
	 System.out.println("Available stores within 30 miles of your location: ");
	 if(withinRadius.size() == 0) {
	    System.out.println("There are no stores within a 30 mile radius of your location.");
	 }
	 for(int i = 0; i < withinRadius.size(); i++) {
	    System.out.print(i + 1);
	    System.out.println(". ");
	    System.out.print("    Store Name: ");
	    System.out.println(executedQuery.get(withinRadius.get(i)).get(1));
	    System.out.print("    Store ID: ");
	    System.out.println(executedQuery.get(withinRadius.get(i)).get(0));
	    System.out.print("    Store latitude: ");
	    System.out.println(executedQuery.get(withinRadius.get(i)).get(2));
	    System.out.print("    Store longitude: ");
	    System.out.println(executedQuery.get(withinRadius.get(i)).get(3));
	 }
      } catch(Exception e) {
         System.err.println(e.getMessage());
      }   
   }

   /*
    * Prints a list of stores that the user manager manages
    **/
   public static void viewManagedStores(Retail esql) {
      try {
         String query = String.format("SELECT * FROM Store WHERE managerID = '%s'", userId);
         List<List<String>> executedQuery = esql.executeQueryAndReturnResult(query);
         System.out.println("Managed stores: ");
         for(int i = 0; i < executedQuery.size(); i++) {
            System.out.print(i + 1);
            System.out.println(". ");
            System.out.print("    Store Name: ");
            System.out.println(executedQuery.get(i).get(1));
            System.out.print("    Store ID: ");
            System.out.println(executedQuery.get(i).get(0));
            System.out.print("    Store latitude: ");
            System.out.println(executedQuery.get(i).get(2));
            System.out.print("    Store longitude: ");
            System.out.println(executedQuery.get(i).get(3));
         }
      } catch(Exception e) {
         System.err.println(e.getMessage());
      }
   }

   /*
    * Prints a list of products based on the input store
    **/
   public static void viewProducts(Retail esql) {
      try {
         System.out.print("\tEnter store ID: ");
	 String storeID = in.readLine();
	 String query = String.format("SELECT P.productName, P.numberOfUnits, P.pricePerUnit FROM Store S, Product P WHERE S.storeID = '%s' AND S.storeID = P.storeID", storeID);
	 System.out.print("Available products in ");
	 System.out.print(storeID);
	 System.out.println(": ");
	 int num = esql.executeQueryAndPrintResult(query);	 
      } catch(Exception e) {
	 System.err.println(e.getMessage());
      }  
   }

   /*
    * Allows a user to place an order given the store ID, product name, and number of units
    **/ 
   public static void placeOrder(Retail esql) {
      try {
         System.out.print("\tEnter store ID: ");
	 String storeID = in.readLine();
	 System.out.print("\tEnter product name: ");
	 String productName = in.readLine();
	 System.out.print("\tEnter number of units: ");
	 String numberOfUnits = in.readLine();
	 String query1 = String.format("UPDATE Product SET numberOfUnits = (numberOfUnits - %s) WHERE storeID = '%s' AND productName = '%s'", numberOfUnits, storeID, productName);
	 String query2 = String.format("INSERT INTO Orders (customerID, storeID, productName, unitsOrdered) VALUES ('%s', '%s', '%s', %s)", userId, storeID, productName, numberOfUnits);
	 esql.executeUpdate(query1);
	 esql.executeUpdate(query2);
	 System.out.println("Order placed.");
      } catch(Exception e) {
	 System.err.println(e.getMessage());
      }
   }

   /*
    * Allows a user to view their last five orders
    **/ 
   public static void viewRecentOrders(Retail esql) {
      try {
	 String query = String.format("SELECT * FROM Orders WHERE customerID = '%s' ORDER BY orderNumber DESC LIMIT 5", userId);
	 if(userType.matches(".*manager.*") || userType.matches(".*admin.*")) {
	    query = String.format("SELECT * FROM Orders O, Store S WHERE S.managerID = '%s' AND O.storeID = S.storeID ORDER BY orderNumber", userId);
	 }
	 esql.executeQueryAndPrintResult(query);
      } catch(Exception e) {
         System.err.println(e.getMessage());
      }
   }

   /*
    * Allows a manager to update a product's number of units and price per unit given the store ID and product name
    **/ 
   public static void updateProduct(Retail esql) {
      try {
	 System.out.print("\tEnter store ID: ");
	 String storeID = in.readLine();
	 System.out.print("\tEnter product: ");
	 String product = in.readLine();
	 System.out.print("\tEnter new number of units: ");
	 String numberOfUnits = in.readLine();
	 System.out.print("\tEnter new price per unit: ");
	 String pricePerUnit = in.readLine();
	 String query1 = String.format("UPDATE Product SET numberOfUnits = '%s', pricePerUnit = '%s' WHERE productName = '%s' AND '%s' IN (SELECT S.storeID FROM Store S, Users U WHERE U.userID = S.managerID AND U.userID = '%s')", numberOfUnits, pricePerUnit, product, storeID, userId);
	 String query2 = String.format("INSERT INTO productUpdates(managerID, storeID, productName, updatedOn) VALUES('%s', '%s', '%s', now())", userId, storeID, product);
	 esql.executeUpdate(query1);
	 esql.executeUpdate(query2);
	 System.out.println("Item updated.");	 
      } catch(Exception e) {
	 System.err.println(e.getMessage());
      }
   }
   
   /*
    * Allows a manager to view the last five recent updates to a store
    **/ 
   public static void viewRecentUpdates(Retail esql) {
      try {
	 String query = String.format("SELECT P.* FROM ProductUpdates P, Store S WHERE P.storeID = S.storeID AND S.managerID = '%s' ORDER BY P.updatedOn DESC LIMIT 5", userId);
	 esql.executeQueryAndPrintResult(query);
      } catch(Exception e) {
	 System.err.println(e.getMessage());
      }
   }

   /*
    * Allows a manager to view the top five most popular products of their managed stores
    **/ 
   public static void viewPopularProducts(Retail esql) {
      try {
	 String query = String.format("SELECT O.productName, SUM(O.unitsOrdered) AS numberOfOrders FROM Store S, Orders O WHERE S.managerID = '%s' AND S.storeID = O.storeID GROUP BY O.productName ORDER BY SUM(O.unitsOrdered) DESC LIMIT 5", userId);
	 esql.executeQueryAndPrintResult(query);	 
      } catch(Exception e) {
	 System.err.println(e.getMessage());
      }
   }

   /*
    * Allows a manager to view the top five most popular customers of their managed stores
    **/ 
   public static void viewPopularCustomers(Retail esql) {
      try {
	 String query = String.format("SELECT U.name, O.customerID, COUNT(*) AS numberOfOrders FROM Store S, Orders O, Users U WHERE S.managerID = '%s' AND S.storeID = O.storeID AND O.customerID = U.userID GROUP BY U.name, O.customerID ORDER BY COUNT(O.customerID) DESC LIMIT 5", userId);
	 esql.executeQueryAndPrintResult(query);
      } catch(Exception e) {
	 System.err.println(e.getMessage());
      }
   }

   /*
    * Allows a manager or admin to place a product supply request
    **/ 
   public static void placeProductSupplyRequests(Retail esql) {
      try {
	 System.out.print("\tEnter store ID: ");
	 String storeID = in.readLine();
	 System.out.print("\tEnter product name: ");
	 String product = in.readLine();
	 System.out.print("\tEnter new number of units: ");
	 String numberOfUnits = in.readLine();
	 System.out.print("Enter warehouse ID: ");
	 String warehouseID = in.readLine();
	 String query1 = String.format("INSERT INTO ProductSupplyRequests(managerID, warehouseID, storeID, productName, unitsRequested) VALUES('%s', '%s', '%s', '%s', '%s')", userId, warehouseID, storeID, product, numberOfUnits);
	 String query2 = String.format("UPDATE Product SET numberOfUnits = numberOfUnits + %s WHERE productName = '%s' AND storeID = '%s'", numberOfUnits, product, storeID);
	 esql.executeUpdate(query1);
	 esql.executeUpdate(query2);
	 System.out.println("Product supply request placed.");
      } catch(Exception e) {
	 System.err.println(e.getMessage());
      }
   }

   /*
    * Allows an admin to view all of the stores within the database
    **/ 
   public static void viewAllStores(Retail esql) {
      try {
         String query = String.format("SELECT * FROM Store");
	 List<List<String>> executedQuery = esql.executeQueryAndReturnResult(query);
	 System.out.println("All stores: ");
	 for(int i = 0; i < executedQuery.size(); i++) {
	    System.out.print(i + 1);
	    System.out.println(". ");
	    System.out.print("    Store Name: ");
	    System.out.println(executedQuery.get(i).get(1));
	    System.out.print("    Store ID: ");
	    System.out.println(executedQuery.get(i).get(0));
	    System.out.print("    Store latitude: ");
	    System.out.println(executedQuery.get(i).get(2));
	    System.out.print("    Store longitude: ");
	    System.out.println(executedQuery.get(i).get(3));
	 }
      } catch(Exception e) {
         System.err.println(e.getMessage());
      }   
   }

   /*
    * Allows an admin to view all of the customers within the database
    **/ 
   public static void viewAllCustomers(Retail esql) {
      try {
         String query = String.format("SELECT * FROM Users WHERE type = 'customer' OR type = 'manager' ORDER BY userID");
	 esql.executeQueryAndPrintResult(query);
      } catch(Exception e) {
         System.err.println(e.getMessage());
      }   
   }

   /*
    * Allows an admin to view all of the recent orders made by users within the database
    **/ 
   public static void viewAllRecentOrders(Retail esql) {
      try {
	 String query = String.format("SELECT * FROM Orders");
	 esql.executeQueryAndPrintResult(query);
      } catch(Exception e) {
         System.err.println(e.getMessage());
      }
   }

   /*
    * Allows an admin to update a products name, number of units, and price per unit of any product at any store given the store ID
    **/ 
   public static void updateProductAdmin(Retail esql) {
      try {
	 System.out.print("\tEnter store ID: ");
	 String storeID = in.readLine();
	 System.out.print("\tEnter product: ");
	 String product = in.readLine();
	 System.out.print("\tEnter new product name: ");
	 String productName = in.readLine();
	 System.out.print("\tEnter new number of units: ");
	 String numberOfUnits = in.readLine();
	 System.out.print("\tEnter new price per unit: ");
	 String pricePerUnit = in.readLine();
	 String query1 = String.format("INSERT INTO productUpdates(managerID, storeID, productName, updatedOn) VALUES('%s', '%s', '%s', now())", userId, storeID, product);
	 String query2 = String.format("UPDATE Product SET productName = '%s', numberOfUnits = '%s', pricePerUnit = '%s' WHERE productName = '%s' AND storeID = '%s'", productName, numberOfUnits, pricePerUnit, product, storeID);
	 esql.executeUpdate(query1);
	 esql.executeUpdate(query2);
	 System.out.println("Item updated.");	 
      } catch(Exception e) {
	 System.err.println(e.getMessage());
      }
   }

   /*
    * Allows an admin to update any user's user ID, name, password, latitude, longitude, and user type given the user ID
    **/ 
   public static void updateUserAdmin(Retail esql) {
      try {
	 System.out.print("\tEnter user ID: ");
	 String userID = in.readLine();
	 System.out.print("\tEnter new name: ");
	 String name = in.readLine();
	 System.out.print("\tEnter new password: ");
	 String password = in.readLine();
	 System.out.print("\tEnter new latitude: ");
	 String latitude = in.readLine();
	 System.out.print("\tEnter new longitude: ");
	 String longitude = in.readLine();
	 System.out.print("\tEnter new user type: ");
	 String type = in.readLine();
	 String query = String.format("UPDATE Users SET name = '%s', password = '%s', latitude = '%s', longitude = '%s', type = '%s' WHERE userID = '%s'", name, password, latitude, longitude, type, userID);
	 esql.executeUpdate(query);
	 System.out.println("User updated.");	 
      } catch(Exception e) {
	 System.err.println(e.getMessage());
      }
   }

   /*
    * Allows an admin to delete any user
    **/ 
   public static void deleteUserAdmin(Retail esql) {
      try {
         System.out.print("\tEnter user ID: ");
         String userID = in.readLine();
         String query = String.format("DELETE FROM Users WHERE userID = '%s'", userID);
         esql.executeUpdate(query);
         System.out.println("User deleted.");
      } catch(Exception e) {
         System.err.println(e.getMessage());
      }
   }

   /*
    * Allows a manager to view the top five most popular products
    **/ 
   public static void viewAllRecentUpdates(Retail esql) {
      try {
	 String query = String.format("SELECT P.* FROM ProductUpdates P ORDER BY P.updatedOn");
	 esql.executeQueryAndPrintResult(query);
      } catch(Exception e) {
	 System.err.println(e.getMessage());
      }
   }

   /*
    * Allows a manager to view the top five most popular products
    **/ 
   public static void viewAllRecentRequests(Retail esql) {
      try {
	 String query = String.format("SELECT P.* FROM ProductSupplyRequests P ORDER BY P.requestNumber");
	 esql.executeQueryAndPrintResult(query);
      } catch(Exception e) {
	 System.err.println(e.getMessage());
      }
   }

}//end Retail

