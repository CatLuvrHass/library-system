package messages.borrow;

import messages.MySerializable;

public class CalculateFinesResponse implements MySerializable {
    private String libraryRef;
    private int id;
    private int total;
    
    public CalculateFinesResponse() {}

    public CalculateFinesResponse(String libraryRef, int id, int total) {
        this.libraryRef = libraryRef;
        this.id = id;
        this.total = total;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTotal() {
        return this.total;
    }

    public void setTotal(int total) {
        this.total = total;
    }
    
    public String getLibraryRef() {
        return this.libraryRef;
    }

    public void setLibraryRef(String libraryRef) {
        this.libraryRef = libraryRef;
    }
}
