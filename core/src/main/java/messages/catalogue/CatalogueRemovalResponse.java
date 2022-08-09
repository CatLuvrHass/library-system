package messages.catalogue;

import messages.MySerializable;

public class CatalogueRemovalResponse implements MySerializable {
    private boolean successfulAction = false;
    private int userId;
    private String libraryRef;

    public CatalogueRemovalResponse () {}

    public CatalogueRemovalResponse (int userId, boolean successfulAction, String libraryRef) {
        this.userId = userId;
        this.successfulAction = successfulAction;
        this.libraryRef = libraryRef;
    }

    public boolean isSuccessfulAction() {
        return successfulAction;
    }

    public void setSuccessfulAction(boolean successfulAction) {
        this.successfulAction = successfulAction;
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
