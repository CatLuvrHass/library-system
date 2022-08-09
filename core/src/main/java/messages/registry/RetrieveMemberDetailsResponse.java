package messages.registry;

import core.Member;
import messages.MySerializable;

public class RetrieveMemberDetailsResponse implements MySerializable {
    String libraryRef;
    Member memberDetails;

    public RetrieveMemberDetailsResponse () {}

    public RetrieveMemberDetailsResponse (String libraryRef, Member memberDetails) {
        this.libraryRef = libraryRef;
        this.memberDetails = memberDetails;
    }

    public Member getMember() {
        return this.memberDetails;
    }

    public void setMember(Member memberDetails) {
        this.memberDetails = memberDetails;
    }

    public String getLibraryRef() {
        return this.libraryRef;
    }

    public void setLibraryRef(String libraryRef) {
        this.libraryRef = libraryRef;
    }
    
}
