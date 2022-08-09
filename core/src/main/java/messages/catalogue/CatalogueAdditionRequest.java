package messages.catalogue;

import core.Book;
import messages.MySerializable;

public class CatalogueAdditionRequest implements MySerializable {
    Book book;
    int userId;

    public CatalogueAdditionRequest() {}

    public CatalogueAdditionRequest(Book book, int userId) {
        this.book = book;
        this.userId = userId;
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
}
