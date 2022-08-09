package messages;

public class OperationStatusResponse implements MySerializable {
    String libraryRef;
    int memberId;
    String message;

    public OperationStatusResponse() {}

    public OperationStatusResponse(String libraryRef, int memberId, String message) {
        this.libraryRef = libraryRef;
        this.memberId = memberId;
        this.message = message;
    }

    public String getLibraryRef() {
        return libraryRef;
    }
    
    public void setLibraryRef(String libraryRef) {
        this.libraryRef = libraryRef;
    }

    public void setMemberId(int memberId) {
        this.memberId = memberId;
    }

    public int getMemberId() {
        return memberId;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return this.message;
    }
}
