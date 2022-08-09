package messages.catalogue;
import messages.MySerializable;

public class SearchRequest implements MySerializable {
    // Class to represent a search request to the Catalogue Service containing a book ID
    private int bookId;
    private int userId;
    private String libraryRef;

    public SearchRequest() {}

    public SearchRequest(String libraryRef, int bookId, int userId) {
        this.bookId = bookId;
        this.userId = userId;
        this.libraryRef = libraryRef;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public int getBookId() {
        return bookId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getUserId() {
        return userId;
    }

    public String getLibraryRef() {
        return this.libraryRef;
    }

    public void setLibraryRef(String libraryRef) {
        this.libraryRef = libraryRef;
    }

}
