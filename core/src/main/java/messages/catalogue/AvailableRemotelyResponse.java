package messages.catalogue;

import java.util.HashMap;

import messages.MySerializable;

public class AvailableRemotelyResponse implements MySerializable {
    String libraryRef;
    int bookId;
    HashMap<String, Integer> whereAvailable;
    int userId;

    public AvailableRemotelyResponse() {}

    public AvailableRemotelyResponse(String libraryRef, int bookId, HashMap<String, Integer> whereAvailable, int userId) {
        this.libraryRef = libraryRef;
        this.bookId = bookId;
        this.whereAvailable = whereAvailable;
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

    public HashMap<String, Integer> getWhereAvailable() {
        return this.whereAvailable;
    }

    public void setWhereAvailable(HashMap<String, Integer> whereAvailable) {
        this.whereAvailable = whereAvailable;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getUserId() {
        return userId;
    }
}
