package messages.catalogue;
import messages.MySerializable;

import core.Book;

public class SearchResponse implements MySerializable {
    private Book book;
    private int userId;
    private String libraryRef;

    public SearchResponse() {}

    public SearchResponse(String libraryRef, Book book, int userId) {
        this.book = book;
        this.userId = userId;
        this.libraryRef = libraryRef;
    }

    public Book getBook() {
        return this.book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getLibraryRef() {
        return this.libraryRef;
    }

    public void setLibraryRef(String libraryRef) {
        this.libraryRef = libraryRef;
    }
}
