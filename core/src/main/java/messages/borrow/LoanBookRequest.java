package messages.borrow;

import messages.MySerializable;

public class LoanBookRequest implements MySerializable {
    private String libraryRef;
    private int userID;
    private int bookID;

    public LoanBookRequest(int userID, int bookID, String libraryRef) {
        this.libraryRef = libraryRef;
        this.userID = userID;
        this.bookID = bookID;
    }

    public String getLibraryRef() {
        return this.libraryRef;
    }

    public void setLibraryRef(String libraryRef) {
        this.libraryRef = libraryRef;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public int getBookID() {
        return bookID;
    }

    public void setBookID(int bookID) {
        this.bookID = bookID;
    }
}

