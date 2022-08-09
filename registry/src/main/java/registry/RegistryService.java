package registry;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Statement;
import java.time.Duration;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.ActorSelection;
import akka.actor.ActorSystem;
import akka.actor.OneForOneStrategy;
import akka.actor.Props;
import akka.actor.SupervisorStrategy;
import akka.japi.pf.DeciderBuilder;
import core.Member;
import messages.OperationStatusResponse;
import messages.registry.DeleteMemberRequest;
import messages.registry.RegisterMemberRequest;
import messages.registry.RetrieveMemberDetailsRequest;
import messages.registry.RetrieveMemberDetailsResponse;
import messages.registry.UpdatePasswordRequest;
import registry.RegistryService;

public class RegistryService extends AbstractActor {
    private final static String dBURL = "jdbc:mysql://registry.cayveqvorwmz.eu-west-1.rds.amazonaws.com/registry";
    private final static String dbUsername = "admin";
    private final static String dbPassword = "Passw0rd1";
    private static ActorSystem registrySystem;
    private static ActorRef registryActorRef;
    private static ActorRef brokerRef;

    private static ActorRef registerMemberChild;
    private static ActorRef deleteMemberChild;
    private static ActorRef retrieveMemberDetailsChild;
    private static ActorRef updatePasswordChild;

    public static void main(String[] args) {

        registrySystem = ActorSystem.create();

        registryActorRef = registrySystem.actorOf(Props.create(RegistryService.class), "registry");
        registryActorRef.tell("createChildren", registryActorRef);

        ActorSelection brokerSelection = registrySystem.actorSelection("akka.tcp://default@127.0.0.1:2551/user/broker");
        brokerSelection.tell("registerRegistry", registryActorRef);

        // Open a connection
        try (Connection conn = DriverManager.getConnection(dBURL, dbUsername, dbPassword);
                Statement stmt = conn.createStatement();) {
            String sql = "CREATE TABLE IF NOT EXISTS REGISTRATION " +
                    "(id INTEGER NOT NULL, " +
                    " name VARCHAR(255) NOT NULL, " +
                    " gender VARCHAR(255), " +
                    " year_of_birth INTEGER, " +
                    " password VARCHAR(255) NOT NULL, " +
                    " library_ref VARCHAR(255) NOT NULL, " +
                    " email VARCHAR(255), " +
                    " phone_no VARCHAR(255), " +
                    " PRIMARY KEY ( id ))";
            stmt.executeUpdate(sql);
//            System.out.println("Created REGISTRATION in given database...");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static SupervisorStrategy strategy = new OneForOneStrategy(
            10,
            Duration.ofMinutes(1),
            DeciderBuilder
            .match(SQLException.class, e -> SupervisorStrategy.restart())
            .match(SQLIntegrityConstraintViolationException.class, e -> SupervisorStrategy.resume())
            .build());

    @Override
    public SupervisorStrategy supervisorStrategy() {
        return strategy;
    }

    public static class RegisterMemberChild extends AbstractActor {
        int checkState = 0;

        @Override
        public Receive createReceive() {
            return receiveBuilder()
                    .match(SQLException.class, exception -> {
                        throw exception;
                    })
                    // Added for testing, to check value resets to 0 after restart
                    .match(Integer.class, i -> {
                        checkState = i;
                        System.out.println(checkState);
                    })
                    .matchEquals("get", msg -> getSender().tell(checkState, getSelf()))
                    .match(RegisterMemberRequest.class,
                            Request -> {

                                int rowsAffected = 0;
                                try (Connection conn = DriverManager.getConnection(dBURL, dbUsername, dbPassword)) {
                                    String query = "INSERT INTO REGISTRATION (id, name, gender, year_of_birth, password, library_ref, email, phone_no) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
                                    PreparedStatement statement = conn.prepareStatement(query,
                                            Statement.RETURN_GENERATED_KEYS);
                                    statement.setInt(1, Request.getMember().getId());
                                    statement.setString(2, Request.getMember().getName());
                                    statement.setString(3, String.valueOf(Request.getMember().getGender()));
                                    statement.setInt(4, Request.getMember().getYearOfBirth());
                                    statement.setString(5, Request.getMember().getPassword());
                                    statement.setString(6, Request.getMember().getHomeLibrary());
                                    statement.setString(7, Request.getMember().getEmail());
                                    statement.setString(8, Request.getMember().getPhoneNumber());

                                    rowsAffected = statement.executeUpdate();

                                    if (rowsAffected > 0) {
                                        getSender().tell(
                                                new OperationStatusResponse(Request.getLibraryRef(),
                                                        Request.getMember().getId(), "Member added successfully"),
                                                getSelf());
                                    } else {
                                        getSender().tell(new OperationStatusResponse(Request.getLibraryRef(),
                                                Request.getMember().getId(), "Register member operation unsuccessful"), getSelf());
                                    }
                                } catch (SQLIntegrityConstraintViolationException e) {
                                    getSelf().tell(new SQLIntegrityConstraintViolationException(), getSelf());
                                    // e.printStackTrace();
                                } catch (SQLException e) {
                                    getSelf().tell(new SQLException(), getSelf());
                                }
                            })
                    .build();
        }
    }

    public static class DeleteMemberChild extends AbstractActor {
        int checkState = 0;

        @Override
        public Receive createReceive() {
            return receiveBuilder()
                    .match(SQLException.class, exception -> {
                        throw exception;
                    })
                    .match(Integer.class, i -> {
                        checkState = i;
                        System.out.println(checkState);
                    })
                    .matchEquals("get", s -> getSender().tell(checkState, getSelf()))
                    .match(DeleteMemberRequest.class,
                            Request -> {

                                try (Connection conn = DriverManager.getConnection(dBURL, dbUsername, dbPassword)) {
                                    String query = "DELETE FROM REGISTRATION WHERE id=?";
                                    PreparedStatement statement = conn.prepareStatement(query,
                                            Statement.RETURN_GENERATED_KEYS);
                                    statement.setInt(1, Request.getId());

                                    int rowsAffected = statement.executeUpdate();
                                    if (rowsAffected > 0) {
                                        getSender().tell(new OperationStatusResponse(Request.getLibraryRef(),
                                                Request.getId(), "Member deleted successfully"), getSelf());
                                    } else {
                                        getSender().tell(new OperationStatusResponse(Request.getLibraryRef(),
                                                Request.getId(), "Delete member operation unsuccessful"), getSelf());
                                    }

                                } catch (SQLException e) {
                                    getSelf().tell(new SQLException(), getSelf());
                                    // e.printStackTrace();
                                }
                            })
                    .build();
        }
    }

    public static class RetrieveMemberDetailsChild extends AbstractActor {
        int checkState = 0;

        @Override
        public Receive createReceive() {
            return receiveBuilder()
                    .match(SQLException.class, exception -> {
                        throw exception;
                    })
                    .match(Integer.class, i -> {
                        checkState = i;
                        System.out.println(checkState);
                    })
                    .matchEquals("get", s -> getSender().tell(checkState, getSelf()))
                    .match(RetrieveMemberDetailsRequest.class,
                            Request -> {

                                try (Connection conn = DriverManager.getConnection(dBURL, dbUsername, dbPassword)) {
                                    String query = "SELECT * FROM REGISTRATION WHERE id=" + Request.getId();

                                    Statement statement = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
                                            ResultSet.CONCUR_UPDATABLE);
                                    ResultSet resultSet = statement.executeQuery(query);
                                    resultSet.absolute(1);

                                    Member member = new Member(resultSet.getString("name"),
                                            resultSet.getString("gender").charAt(0),
                                            resultSet.getInt("year_of_birth"), resultSet.getString("password"),
                                            resultSet.getInt("id"),
                                            resultSet.getString("library_ref"), resultSet.getString("phone_no"),
                                            resultSet.getString("email"));

                                    getSender().tell(new RetrieveMemberDetailsResponse(Request.getLibraryRef(), member),
                                            getSelf());

                                } catch (SQLException e) {
                                    getSelf().tell(new SQLException(), getSelf());
                                    // e.printStackTrace();
                                }
                            })
                    .build();
        }
    }

    public static class UpdatePasswordChild extends AbstractActor {
        int checkState = 0;

        @Override
        public Receive createReceive() {
            return receiveBuilder()
                    .match(SQLException.class, exception -> {
                        throw exception;
                    })
                    .match(Integer.class, i -> {
                        checkState = i;
                        System.out.println(checkState);
                    })
                    .matchEquals("get", s -> getSender().tell(checkState, getSelf()))
                    .match(UpdatePasswordRequest.class,
                            Request -> {

                                try (Connection conn = DriverManager.getConnection(dBURL, dbUsername, dbPassword)) {
                                    String query = "SELECT password FROM REGISTRATION WHERE id=" + Request.getId();

                                    Statement statement = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
                                            ResultSet.CONCUR_UPDATABLE);
                                    ResultSet resultSet = statement.executeQuery(query);
                                    resultSet.absolute(1);

                                    if (resultSet.getString("password").equals(Request.getOldPassword())) {
                                        String query2 = "UPDATE REGISTRATION SET password = ? WHERE id=?";
                                        PreparedStatement statement2 = conn.prepareStatement(query2,
                                                Statement.RETURN_GENERATED_KEYS);
                                        statement2.setString(1, Request.getNewPassword());
                                        statement2.setInt(2, Request.getId());
                                        int rowsAffected = statement2.executeUpdate();

                                        if (rowsAffected > 0) {
                                            getSender().tell(new OperationStatusResponse(Request.getLibraryRef(),
                                                    Request.getId(), "Password changed"), getSelf());
                                        } else {
                                            getSender().tell(new OperationStatusResponse(Request.getLibraryRef(),
                                                    Request.getId(), "Operation unsuccessful, password was not updated"), getSelf());
                                        }
                                    } else {
                                        getSender().tell(new OperationStatusResponse(Request.getLibraryRef(),
                                                    Request.getId(), "Operation unsuccessful, old password provided is incorrect"), getSelf());
                                    }
                                } catch (SQLException e) {
                                    getSelf().tell(new SQLException(), getSelf());
                                    // e.printStackTrace();
                                }
                            })
                    .build();
        }
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .matchEquals("sendBrokerRef", msg -> brokerRef = getSender())
                .match(OperationStatusResponse.class, Response -> {
                    brokerRef.tell(Response, getSelf());
                })

                .match(RetrieveMemberDetailsResponse.class, Response -> {
                    brokerRef.tell(Response, getSelf());
                })
                .match(
                        Props.class,
                        props -> {
                            getSender().tell(getContext().actorOf(props), getSelf());
                        })

                .matchEquals(
                        "createChildren", msg -> {
                            registerMemberChild = getContext().actorOf(Props.create(RegisterMemberChild.class),
                                    "registerMemberChild");
                            deleteMemberChild = getContext().actorOf(Props.create(DeleteMemberChild.class),
                                    "deleteMemberChild");
                            retrieveMemberDetailsChild = getContext().actorOf(
                                    Props.create(RetrieveMemberDetailsChild.class),
                                    "retrieveMemberDetailsChild");
                            updatePasswordChild = getContext().actorOf(Props.create(UpdatePasswordChild.class),
                                    "updatePasswordChild");
                        })

                .match(RegisterMemberRequest.class,
                        Request -> {
                            brokerRef = getSender();
                            registerMemberChild.tell(Request, getSelf());
                        })

                .match(DeleteMemberRequest.class,
                        Request -> {
                            deleteMemberChild.tell(Request, getSelf());
                        })

                .match(RetrieveMemberDetailsRequest.class,
                        Request -> {
                            retrieveMemberDetailsChild.tell(Request, getSelf());

                        })

                .match(UpdatePasswordRequest.class,
                        Request -> {
                            updatePasswordChild.tell(Request, getSelf());
                        })

                .build();
    }

}