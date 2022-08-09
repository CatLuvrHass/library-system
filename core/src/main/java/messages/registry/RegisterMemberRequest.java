package messages.registry;

import core.Member;
import messages.MySerializable;

public class RegisterMemberRequest implements MySerializable {
    private String libraryRef;
    private Member member;

    public RegisterMemberRequest() {}

    public RegisterMemberRequest(String libraryRef, Member member) {
        this.libraryRef = libraryRef;
        this.member = member;
    }

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }
    
    public String getLibraryRef() {
        return this.libraryRef;
    }

    public void setLibraryRef(String libraryRef) {
        this.libraryRef = libraryRef;
    }
    
}
