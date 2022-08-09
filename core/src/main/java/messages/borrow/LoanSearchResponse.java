package messages.borrow;

import messages.MySerializable;

public class LoanSearchResponse implements MySerializable {
    private int loanID;
    private int userID;
    private int bookID;
    private String loanDate;
    private String returnDate;
    private String actualReturnDate;
    private String libraryRef;

    public LoanSearchResponse(int loanID, int userID, int bookID, String loanDate, String returnDate,
            String actualReturnDate, String libraryRef) {
        this.loanID = loanID;
        this.userID = userID;
        this.bookID = bookID;
        this.loanDate = loanDate;
        this.returnDate = returnDate;
        this.actualReturnDate = actualReturnDate;
        this.libraryRef = libraryRef;
    }

    public String getActualReturnDate() {
        return this.actualReturnDate;
    }

    public void setActualReturnDate(String actualReturnDate) {
        this.actualReturnDate = actualReturnDate;
    }

    public String getLibraryRef() {
        return libraryRef;
    }

    public void setLibraryRef(String libraryRef) {
        this.libraryRef = libraryRef;
    }

    public int getLoanID() {
        return loanID;
    }

    public void setLoanID(int loanID) {
        this.loanID = loanID;
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

    public String getLoanDate() {
        return loanDate;
    }

    public void setLoanDate(String loanDate) {
        this.loanDate = loanDate;
    }

    public String getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(String returnDate) {
        this.returnDate = returnDate;
    }
}
