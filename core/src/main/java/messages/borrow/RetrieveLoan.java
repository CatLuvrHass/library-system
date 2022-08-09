package messages.borrow;

import messages.MySerializable;

public class RetrieveLoan implements MySerializable {
    private String libraryRef;
    private int loanID;
    private int userID;

    public RetrieveLoan(String libraryRef, int loanID, int userID) {
        this.libraryRef = libraryRef;
        this.loanID = loanID;
        this.userID = userID;
    }

    public String getLibraryRef() {
        return this.libraryRef;
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
}
