package messages.catalogue;

import messages.MySerializable;

public class CheckAvailabilityRequest implements MySerializable {
    private String libraryRef;
    private int bookId;
    private int userId;

    public CheckAvailabilityRequest() {}

    public CheckAvailabilityRequest(String libraryRef, int bookId, int userId) {
        this.libraryRef = libraryRef;
        this.bookId = bookId;
        this.userId = userId;
    }

    public String getLibraryRef() {
        return this.libraryRef;
    }

    public void setLibraryRef(String libraryRef) {
        this.libraryRef = libraryRef;
    }

    public int getBookId() {
        return this.bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getUserId() {
        return userId;
    }

    
}
