package messages.catalogue;

import messages.MySerializable;

public class AvailableLocallyResponse implements MySerializable {
    private String libraryRef;
    private int bookId;
    private int copiesAvailable;
    private int userId;

    public AvailableLocallyResponse() {}

    public AvailableLocallyResponse(String libraryRef, int BookId, int copiesAvailable, int userId) {
        this.libraryRef = libraryRef;
        this.bookId = BookId;
        this.copiesAvailable = copiesAvailable;
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

    public int getCopiesAvailable() {
        return this.copiesAvailable;
    }

    public void setCopiesAvailable(int copiesAvailable) {
        this.copiesAvailable = copiesAvailable;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getUserId() {
        return userId;
    }
}
