package messages.registry;

import messages.MySerializable;

public class RetrieveMemberDetailsRequest implements MySerializable {
    private String libraryRef;
    private int id;

    public RetrieveMemberDetailsRequest() {}

    public RetrieveMemberDetailsRequest(String libraryRef, int id) {
        this.libraryRef = libraryRef;
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLibraryRef() {
        return this.libraryRef;
    }

    public void setLibraryRef(String libraryRef) {
        this.libraryRef = libraryRef;
    }
   
}
