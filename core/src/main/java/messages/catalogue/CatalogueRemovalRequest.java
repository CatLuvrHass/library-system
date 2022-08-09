package messages.catalogue;

import messages.MySerializable;

public class CatalogueRemovalRequest implements MySerializable {
    private String libraryRef;
    private int bookID;
    private int userId;
    
    public CatalogueRemovalRequest() {}

    public CatalogueRemovalRequest(int bookID, String libraryRef, int userId) {
        this.bookID = bookID;
        this.libraryRef = libraryRef;
        this.userId = userId;
    }

    public void setBookID(int bookID) {
        this.bookID = bookID;
    }

    public int getBookID() {
        return bookID;
    }

    public void setLibraryRef(String libraryRef) {
        this.libraryRef = libraryRef;
    }

    public String getLibraryRef() {
        return libraryRef;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getUserId() {
        return userId;
    }
}
