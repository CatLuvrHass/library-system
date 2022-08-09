package messages.borrow;

import messages.MySerializable;

public class CalculateFinesRequest implements MySerializable {
    private String libraryRef;
    private int memberId;

    public CalculateFinesRequest() {}

    public CalculateFinesRequest(String libraryRef, int memberId) {
        this.memberId = memberId;
        this.libraryRef = libraryRef;
    }

    public int getMemberId() {
        return memberId;
    }

    public void setMemberId(int memberId) {
        this.memberId = memberId;
    }

    public String getLibraryRef() {
        return this.libraryRef;
    }

    public void setLibraryRef(String libraryRef) {
        this.libraryRef = libraryRef;
    }
    
}
