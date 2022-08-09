package messages.registry;

import messages.MySerializable;

public class DeleteMemberRequest implements MySerializable {
    String libraryRef;
    private int id;

    public DeleteMemberRequest() {}

    public DeleteMemberRequest(String libraryRef, int id) {
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
