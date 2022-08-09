package messages.catalogue;

import messages.MySerializable;

public class IncrementAvailabilityRequest implements MySerializable {
    String libraryRef;
    int bookId;

    public IncrementAvailabilityRequest() {}

    public IncrementAvailabilityRequest(String libraryRef, int bookId) {
        this.libraryRef = libraryRef;
        this.bookId = bookId;
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
}
