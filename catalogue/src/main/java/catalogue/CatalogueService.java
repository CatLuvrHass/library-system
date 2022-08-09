package catalogue;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import org.json.JSONArray;
import org.json.JSONObject;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.ActorSelection;
import akka.actor.ActorSystem;
import akka.actor.Cancellable;
import akka.actor.Props;
import core.Book;
import messages.catalogue.AvailableLocallyResponse;
import messages.catalogue.AvailableRemotelyResponse;
import messages.catalogue.CatalogueAdditionRequest;
import messages.catalogue.CatalogueAdditionResponse;
import messages.catalogue.CatalogueRemovalRequest;
import messages.catalogue.CatalogueRemovalResponse;
import messages.catalogue.CheckAvailabilityRequest;
import messages.catalogue.DecrementAvailabilityRequest;
import messages.catalogue.IncrementAvailabilityRequest;
import messages.catalogue.SearchRequest;
import messages.catalogue.SearchResponse;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class CatalogueService extends AbstractActor {
    static ActorSystem catalogueSystem;
    private final static String dBURL = "jdbc:mysql://catalogue.cxh5lwdlsqiw.eu-west-1.rds.amazonaws.com:3306/catalogue";
    private final static String dbUsername = "root";
    private final static String dbPassword = "Passw0rd1";
    private static ActorRef catalogueActorRef;
    private static ActorRef brokerRef;

    public static void main(String[] args) {
        // Set up actor system, this method should be called initially before anything
        // else in the class
        catalogueSystem = ActorSystem.create();
        // Create an actor for this ActorSystem for this class
        catalogueActorRef = catalogueSystem.actorOf(Props.create(CatalogueService.class), "catalogue");

        // Register this with the broker
        ActorSelection brokerSelection = catalogueSystem
                .actorSelection("akka.tcp://default@127.0.0.1:2551/user/broker");
        brokerSelection.tell("registerCatalogue", catalogueActorRef);

        // Open a connection
        try (Connection conn = DriverManager.getConnection(dBURL, dbUsername, dbPassword);
             Statement stmt = conn.createStatement();
        ) {
            String sql = "CREATE TABLE IF NOT EXISTS catalogue " +
                    "(id INTEGER NOT NULL AUTO_INCREMENT, " +
                    " book_id INTEGER NOT NULL, " +
                    " book_title VARCHAR(255), " +
                    " book_author VARCHAR(255), " +
                    " available_copies INTEGER NOT NULL, " +
                    " total_copies INTEGER NOT NULL, " +
                    " library_ref VARCHAR(255), " +
                    " PRIMARY KEY ( id ))";
            stmt.executeUpdate(sql);
//            System.out.println("Created CATALOGUE in given database...");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // CREATE LIBRARY TABLE
        try (Connection conn = DriverManager.getConnection(dBURL, dbUsername, dbPassword);
             Statement stmt = conn.createStatement();
        ) {
            String sql = "CREATE TABLE IF NOT EXISTS libraries " +
                    "(id INTEGER NOT NULL, " +
                    " library_ref VARCHAR(45), " +
                    " place_id VARCHAR(255) NOT NULL, " +
                    " library_name VARCHAR(45) NOT NULL, " +
                    " PRIMARY KEY ( id ))";
            stmt.executeUpdate(sql);
//            System.out.println("Created LIBRARIES in given database...");
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(SearchRequest.class,
                        // Do a lookup in the database for the book by the book id and send a
                        // SearchResponse message back to the broker containing a Book object

                        searchRequest -> {
                            // try with block to instantiate database stuff so it will close itself when
                            // finished
                            try (Connection conn = DriverManager.getConnection(dBURL, dbUsername, dbPassword)) {
                                // Search for the book in the catalogue table
                                String SQL = "SELECT * FROM catalogue WHERE book_id =?";
                                PreparedStatement statement = conn.prepareStatement(SQL, Statement.RETURN_GENERATED_KEYS);
                                statement.setInt(1, searchRequest.getBookId());
                                ResultSet res = statement.executeQuery();
                                // Create a SearchResponse object with the result and send it back to the broker
                                // Return n/a for library name and 0 for num copies as this info won't be shown to user
                                res.next();
                                Book bookRetrieved = new Book(res.getInt("book_id"), res.getString("book_title"), res.getString("book_author"), "n/a", 0);

                                SearchResponse response = new SearchResponse(searchRequest.getLibraryRef(), bookRetrieved, searchRequest.getUserId());
                                getSender().tell(response, getSelf());

                            } catch (SQLException e) {
                                e.printStackTrace();
                            }

                        })
                .match(CatalogueAdditionRequest.class,
                        bookAddition -> {
//                            System.out.println("In Catalogue Addition Request");
                            // Get the library this book is being added to so we add it to the right table
                            Book book = bookAddition.getBook();
                            String libraryName = book.getLibraryName();

                            // try with block to instantiate database stuff so it will close itself when
                            // finished
                            try (Connection conn = DriverManager.getConnection(dBURL, dbUsername, dbPassword)) {
//                                System.out.println("In try statement");
                                String SQL = "INSERT into catalogue (book_id, book_title, book_author, available_copies, total_copies, library_ref) VALUES (?,?,?,?,?,?)";
//                                System.out.println("SQL STATEMENT: " + SQL);
                                PreparedStatement statement = conn.prepareStatement(SQL, Statement.RETURN_GENERATED_KEYS);
                                statement.setInt(1, book.getBookID());
                                statement.setString(2, book.getBookTitle());
                                statement.setString(3, book.getBookAuthor());
                                statement.setInt(4, book.getNumCopies());
                                statement.setInt(5, book.getNumCopies());
                                statement.setString(6, libraryName);

                                // Execute the sql query (returns the rows affected by the query
                                int rowsAffected = statement.executeUpdate();
                                // if at least one row was affected send a string back to the sender to indicate
                                // success
                                // this is only a temporary fix for the unit test as it waits for a string after
                                // sending a
                                // test bookAddition to this service
                                if (rowsAffected > 0) {
                                    CatalogueAdditionResponse response = new CatalogueAdditionResponse(bookAddition.getUserId(), true, libraryName);
                                    getSender().tell(response, getSelf());
                                } else {
                                    CatalogueAdditionResponse response = new CatalogueAdditionResponse(bookAddition.getUserId(), false, libraryName);
                                    getSender().tell(response, getSelf());
                                }
                            } catch (SQLException e) {
//                                System.out.println("Tried to add book " + bookAddition.getBook() + "to " + libraryName + " but it already existed");
                                CatalogueAdditionResponse response = new CatalogueAdditionResponse(bookAddition.getUserId(), false, libraryName);
                                getSender().tell(response, getSelf());
                            }
                        })

                .match(CatalogueRemovalRequest.class,
                        bookRemoval -> {
                            // bookID is a unique value in the table so we can remove the row from
                            // the appropriate table with matching bookID
//                            System.out.println("IN CATALOGUE REMOVAL");
                            String libraryName = bookRemoval.getLibraryRef();

                            // try with block to instantiate database stuff so it will close itself when
                            // finished
                            try (Connection conn = DriverManager.getConnection(dBURL, dbUsername, dbPassword)) {

                                String SQL = "DELETE FROM catalogue WHERE book_id = ? AND library_ref=?";
                                PreparedStatement statement = conn.prepareStatement(SQL, Statement.RETURN_GENERATED_KEYS);
                                statement.setInt(1, bookRemoval.getBookID());
                                statement.setString(2, libraryName);

                                // Execute the sql query (returns the rows affected by the query
                                int rowsAffected = statement.executeUpdate();
                                // if at least one row was affected send a string back to the sender to indicate success
                                if (rowsAffected > 0) {
                                    CatalogueRemovalResponse response = new CatalogueRemovalResponse(bookRemoval.getBookID(), true, libraryName);
                                    getSender().tell(response, getSelf());
                                } else {
                                    CatalogueRemovalResponse response = new CatalogueRemovalResponse(bookRemoval.getBookID(), false, libraryName);
                                    getSender().tell(response, getSelf());
                                }
                            } catch (SQLException e) {
                                CatalogueRemovalResponse response = new CatalogueRemovalResponse(bookRemoval.getBookID(), false, libraryName);
                                getSender().tell(response, getSelf());
                            }
                        })

                .match(CheckAvailabilityRequest.class,
                        Request -> {
//                            System.out.println("In CheckAvailabilityRequest");
                            HashMap<String, Integer> inStock = new HashMap<>();
                            HashMap<String, String> libraryLocations = new HashMap<>();
                            String currentLibrary = Request.getLibraryRef();

                            try (Connection conn = DriverManager.getConnection(dBURL, dbUsername, dbPassword)) {
                                String SQL = "SELECT library_ref, available_copies FROM catalogue WHERE book_id = ?";
                                PreparedStatement statement = conn.prepareStatement(SQL,
                                        Statement.RETURN_GENERATED_KEYS);
                                statement.setInt(1, Request.getBookId());
                                ResultSet res = statement.executeQuery();

                                while (res.next()) {
                                    String library = res.getString("library_ref");
                                    int availableCopies = res.getInt("available_copies");

                                    if (availableCopies > 0) {
                                        inStock.put(library, availableCopies);
                                    }
                                }

                                if (inStock.containsKey(Request.getLibraryRef())) {
                                    getSender().tell(new AvailableLocallyResponse(Request.getLibraryRef(),
                                            Request.getBookId(), inStock.get(Request.getLibraryRef()), Request.getUserId()), getSelf());
                                } else {
                                    // Add current library
                                    inStock.put(Request.getLibraryRef(), 0);

                                    String SQL2 = "SELECT place_id, library_ref, library_name FROM libraries WHERE library_ref IN ("
                                            + String.join(", ", Collections.nCopies(inStock.keySet().size(), "?"))
                                            + ")";

                                    PreparedStatement statement2 = conn.prepareStatement(SQL2,
                                            Statement.RETURN_GENERATED_KEYS);

                                    int index = 1;
                                    for (String library : inStock.keySet()) {
                                        statement2.setString(index++, library);
                                    }

                                    ResultSet res2 = statement2.executeQuery();

                                    while (res2.next()) {
                                        String libraryRef = res2.getString("library_ref");
                                        String libraryName = res2.getString("library_name");
                                        String libraryLocation = res2.getString("place_id");

                                        if (libraryRef.equals(currentLibrary)) {
                                            currentLibrary = libraryName;
                                        }
                                        libraryLocations.put(libraryName, libraryLocation);
                                    }

                                    String currentLibraryLocation = libraryLocations.get(currentLibrary);
                                    libraryLocations.remove(currentLibrary);

                                    String locationString = String.join("%7Cplace_id:", libraryLocations.values());

                                    OkHttpClient client = new OkHttpClient().newBuilder().build();
                                    Request request = new Request.Builder()
                                            .url("https://maps.googleapis.com/maps/api/distancematrix/json?origins=place_id:"
                                                    + currentLibraryLocation + "&destinations=place_id:"
                                                    + locationString
                                                    + "&units=metric&key=AIzaSyCRymHLwxDVVK0e48AbTd1mJ8Yf1Hc2eVQ")
                                            .method("GET", null)
                                            .build();

                                    Response response = client.newCall(request).execute();
                                    String responseBody = response.body().string();

                                    ArrayList<String> libraryNames = new ArrayList<>(libraryLocations.keySet());
                                    HashMap<String, Integer> distances = new HashMap<>();

                                    JSONArray libraryElements = new JSONObject(responseBody).getJSONArray("rows")
                                            .getJSONObject(0)
                                            .getJSONArray("elements");

                                    for (int i = 0; i < libraryElements.length(); i++) {
                                        int distance = libraryElements.getJSONObject(i).getJSONObject("distance")
                                                .getInt("value");
                                        distances.put(libraryNames.get(i), distance);
                                    }

                                    getSender().tell(new AvailableRemotelyResponse(Request.getLibraryRef(),
                                            Request.getBookId(), distances, Request.getUserId()), getSelf());
                                }
                            }
                        })

                .match(String.class,
                        msg -> {
                            if (msg.equals("registerBroker")) {
                                brokerRef = getSender();
//                                startScheduler();
//                                System.out.println("registered broker in catalogue service");
                            }
                        })

                .match(IncrementAvailabilityRequest.class,
                        msg -> {
                            try (Connection conn = DriverManager.getConnection(dBURL, dbUsername, dbPassword)) {
                                String SQL = "UPDATE catalogue SET available_copies=available_copies + 1 WHERE library_ref=\"" + msg.getLibraryRef() + "\" AND book_id="
                                        + msg.getBookId();
                                PreparedStatement statement = conn.prepareStatement(SQL,
                                        Statement.RETURN_GENERATED_KEYS);

                                int rowsAffected = statement.executeUpdate();

                                if (rowsAffected > 0) {
                                    getSender().tell(rowsAffected, getSelf());
                                }
                            }
                        })

                .match(DecrementAvailabilityRequest.class,
                        msg -> {
                            try (Connection conn = DriverManager.getConnection(dBURL, dbUsername, dbPassword)) {
                                String SQL = "UPDATE catalogue SET available_copies = available_copies - 1 WHERE library_ref=\"" + msg.getLibraryRef() + "\" AND book_id="
                                        + msg.getBookId();
                                PreparedStatement statement = conn.prepareStatement(SQL,
                                        Statement.RETURN_GENERATED_KEYS);

                                int rowsAffected = statement.executeUpdate();

                                if (rowsAffected > 0) {
                                    getSender().tell(rowsAffected, getSelf());
                                }
                            }

                        })

                .build();
    }

//    public static void startScheduler() {
//        // This method starts a scheduler that will be started one the broker starts up
//        // it should send a string to the broker every 3 seconds after an initial 5
//        // second wait.
//        Cancellable cancellable = catalogueSystem
//                .scheduler()
//                .schedule(
//                        Duration.ofMillis(5000), Duration.ofMillis(3000), brokerRef, "testScheduler",
//                        catalogueSystem.dispatcher(), null);
//    }

    // This cancels further Ticks to be sent
    // cancellable.cancel();
    // #schedule-recurring
    // system.stop(tickActor);

}