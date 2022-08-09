import static akka.pattern.Patterns.ask;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.junit.Assert.assertEquals;

import java.sql.SQLException;
import java.time.Duration;
import java.util.concurrent.TimeUnit;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.testkit.javadsl.TestKit;
import core.Member;
import messages.OperationStatusResponse;
import messages.registry.DeleteMemberRequest;
import messages.registry.RegisterMemberRequest;
import messages.registry.RetrieveMemberDetailsRequest;
import messages.registry.RetrieveMemberDetailsResponse;
import messages.registry.UpdatePasswordRequest;
import registry.RegistryService;
import registry.RegistryService.DeleteMemberChild;
import registry.RegistryService.RegisterMemberChild;
import registry.RegistryService.RetrieveMemberDetailsChild;
import registry.RegistryService.UpdatePasswordChild;
import scala.concurrent.Await;

public class RegistryUnitTest {
    static ActorSystem registrySystem;
    static ActorRef registryService;

    scala.concurrent.duration.Duration timeout = scala.concurrent.duration.Duration.create(5, SECONDS);

    @BeforeClass
    public static void setup() {
        registrySystem = ActorSystem.create();

        // registryService = registrySystem.actorOf(Props.create(RegistryService.class),
        // "testRegistry");

        registryService = registrySystem.actorOf(Props.create(RegistryService.class), "testRegistry");
        registryService.tell("createChildren", ActorRef.noSender());

    }

    @AfterClass
    public static void teardown() {
        TestKit.shutdownActorSystem(registrySystem);
        registrySystem = null;
    }

    @Test
    public void testRegisterMemberChild() throws Exception {
        // registryService = registrySystem.actorOf(Props.create(RegistryService.class),
        // "testRegistry");
        ActorRef child = (ActorRef) Await.result(ask(registryService, Props.create(RegisterMemberChild.class), 5000),
                timeout);

        child.tell(5, ActorRef.noSender());
        assertEquals(5, Await.result(ask(child, "get", 5000), timeout));

        child.tell(new SQLException(), ActorRef.noSender());
        assertEquals(0, Await.result(ask(child, "get", 5000), timeout));

    }

    @Test
    public void testDeleteMemberChild() throws Exception {
        // registryService = registrySystem.actorOf(Props.create(RegistryService.class),
        // "testRegistry");
        ActorRef child = (ActorRef) Await.result(ask(registryService, Props.create(DeleteMemberChild.class), 5000),
                timeout);

        child.tell(5, ActorRef.noSender());
        assertEquals(5, Await.result(ask(child, "get", 5000), timeout));

        child.tell(new SQLException(), ActorRef.noSender());
        assertEquals(0, Await.result(ask(child, "get", 5000), timeout));

    }

    @Test
    public void testRetrieveMemberDetailsChild() throws Exception {
        // registryService = registrySystem.actorOf(Props.create(RegistryService.class),
        // "testRegistry");
        ActorRef child = (ActorRef) Await
                .result(ask(registryService, Props.create(RetrieveMemberDetailsChild.class), 5000), timeout);

        child.tell(5, ActorRef.noSender());
        assertEquals(5, Await.result(ask(child, "get", 5000), timeout));

        child.tell(new SQLException(), ActorRef.noSender());
        assertEquals(0, Await.result(ask(child, "get", 5000), timeout));

    }

    @Test
    public void updatePasswordChild() throws Exception {
        // registryService = registrySystem.actorOf(Props.create(RegistryService.class),
        // "testRegistry");
        ActorRef child = (ActorRef) Await.result(ask(registryService, Props.create(UpdatePasswordChild.class), 5000),
                timeout);

        child.tell(5, ActorRef.noSender());
        assertEquals(5, Await.result(ask(child, "get", 5000), timeout));

        child.tell(new SQLException(), ActorRef.noSender());
        assertEquals(0, Await.result(ask(child, "get", 5000), timeout));

    }

    // @Test
    // public void testMemberRegistration() throws Exception {
    //     ActorRef child = (ActorRef) Await.result(ask(registryService, Props.create(RegisterMemberChild.class), 5000),
    //             timeout);

    //     TestKit memberRegistrationActor = new TestKit(registrySystem);

    //     Member member = new Member("Jane Slevin", Member.getFemaleChar(), 1994,
    //             "123", 700, "tall_lib", "12345678", "jane@email.com");

    //     child.tell(new RegisterMemberRequest(member.getHomeLibrary(), member), memberRegistrationActor.getRef());

    //     memberRegistrationActor.awaitCond(memberRegistrationActor::msgAvailable);
    //     memberRegistrationActor.expectMsgClass(Duration.ZERO,
    //             OperationStatusResponse.class);
    //     return;
    // }

    // @Test
    // public void testMemberDeletion() {

    // TestKit memberDeletionActor = new TestKit(registrySystem);
    // DeleteMemberRequest memberDeletion = new DeleteMemberRequest("tall_lib",
    // 700);

    // registryService.tell(memberDeletion, memberDeletionActor.getRef());

    // memberDeletionActor.awaitCond(memberDeletionActor::msgAvailable);
    // memberDeletionActor.expectMsgClass(Duration.ZERO,
    // OperationStatusResponse.class);
    // return;
    // }

    // @Test
    // public void testMemberDeletion() throws Exception {

    //     ActorRef child = (ActorRef) Await.result(ask(registryService, Props.create(DeleteMemberChild.class), 5000),
    //             timeout);

    //     TestKit memberDeletionActor = new TestKit(registrySystem);
    //     child.tell(new DeleteMemberRequest("tall_lib", 500), memberDeletionActor.getRef());

    //     memberDeletionActor.awaitCond(memberDeletionActor::msgAvailable);
    //     memberDeletionActor.expectMsgClass(Duration.ZERO,
    //             OperationStatusResponse.class);
    //     return;
    // }

    // @Test
    // public void testRetrieveMemberDetails() throws Exception {
    //     ActorRef child = (ActorRef) Await
    //             .result(ask(registryService, Props.create(RetrieveMemberDetailsChild.class), 5000), timeout);

    //     TestKit memberRetrievalActor = new TestKit(registrySystem);

    //     child.tell(new RetrieveMemberDetailsRequest("tall_lib", 1), memberRetrievalActor.getRef());

    //     memberRetrievalActor.awaitCond(memberRetrievalActor::msgAvailable);
    //     memberRetrievalActor.expectMsgClass(Duration.ZERO,
    //             RetrieveMemberDetailsResponse.class);
    //     return;
    // }

    // @Test
    // public void testPasswordUpdate() throws Exception {
    //     ActorRef child = (ActorRef) Await.result(ask(registryService, Props.create(UpdatePasswordChild.class), 5000),
    //             timeout);

    //     TestKit passwordUpdateActor = new TestKit(registrySystem);

    //     child.tell(new UpdatePasswordRequest("tall_lib",
    //             604, "123", "456"), passwordUpdateActor.getRef());

    //     passwordUpdateActor.awaitCond(passwordUpdateActor::msgAvailable);
    //     passwordUpdateActor.expectMsgClass(Duration.ZERO,
    //             OperationStatusResponse.class);

    //     return;
    // }

}
