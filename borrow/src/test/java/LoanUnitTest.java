import akka.actor.ActorSystem;
import akka.testkit.javadsl.TestKit;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.testkit.javadsl.TestKit;
import borrow.LoanService;
import messages.OperationStatusResponse;
import messages.borrow.*;
import org.junit.*;

import java.time.Duration;

public class LoanUnitTest {
    static ActorSystem loanSystem;
    static ActorRef loanService;

    @BeforeClass
    public static void setup() {
        loanSystem = ActorSystem.create();
        // Create an actor for this ActorSystem for this class
        loanService = loanSystem.actorOf(Props.create(LoanService.class), "testLoan");
    }

    @AfterClass
    public static void teardown() {
        TestKit.shutdownActorSystem(loanSystem);
        loanSystem = null;
    }

//    @Test
//    public void testLoanAddition() {
//        TestKit bookLoanActor = new TestKit(loanSystem);
//        LoanBookRequest bookLoan = new LoanBookRequest(1, 43, "a", "b", "c");
//        loanService.tell(bookLoan, bookLoanActor.getRef());

//        bookLoanActor.awaitCond(bookLoanActor::msgAvailable);
//        bookLoanActor.expectMsgClass(Duration.ZERO, OperationStatusResponse.class);
//        return;
//    }

//    @Test
//    public void testBookSearch() {
//        TestKit loanSearchActor = new TestKit(loanSystem);
//        RetrieveLoan loanSearch = new RetrieveLoan("tall_lib", 1, 1);
//        loanService.tell(loanSearch, loanSearchActor.getRef());

//        loanSearchActor.awaitCond(loanSearchActor::msgAvailable);
//        loanSearchActor.expectMsgClass(Duration.ofSeconds(60), LoanSearchResponse.class);

//        return;
//    }

//    @Test
//    public void testRetrieveLoan() {
//        TestKit retrieveLoanActor = new TestKit(loanSystem);
//        RetrieveLoan retrieveLoan = new RetrieveLoan("tall_lib", 1, 1);
//        loanService.tell(retrieveLoan, retrieveLoanActor.getRef());

//        retrieveLoanActor.awaitCond(retrieveLoanActor::msgAvailable);
//        retrieveLoanActor.expectMsgClass(Duration.ZERO, LoanSearchResponse.class);

//        return;
//    }

//    @Test
//    public void testCalculateFines() {
//        TestKit calculateFinesActor = new TestKit(loanSystem);
//        CalculateFinesRequest calculateFines = new CalculateFinesRequest("tall_lib", 1);
//        loanService.tell(calculateFines, calculateFinesActor.getRef());

//        calculateFinesActor.awaitCond(calculateFinesActor::msgAvailable);
//        calculateFinesActor.expectMsgClass(Duration.ZERO, CalculateFinesResponse.class);

//        return;
//    }

//    @Test
//    public void testReturnBook() {
//        TestKit returnBookActor = new TestKit(loanSystem);
//        ReturnBookRequest returnBook = new ReturnBookRequest(1, "tall_lib", 1, 1);
//        loanService.tell(returnBook, returnBookActor.getRef());

//        returnBookActor.awaitCond(returnBookActor::msgAvailable);
//        returnBookActor.expectMsgClass(Duration.ZERO, OperationStatusResponse.class);

//        return;
//    }
}
