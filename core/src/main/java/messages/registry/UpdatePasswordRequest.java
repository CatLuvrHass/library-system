package messages.registry;

import messages.MySerializable;

public class UpdatePasswordRequest implements MySerializable {
    String libraryRef;
    int id;
    String oldPassword;
    String newPassword;

    public UpdatePasswordRequest() {}

    public UpdatePasswordRequest(String libraryRef, int id, String oldPassword, String newPassword) {
        this.libraryRef = libraryRef;
        this.id = id;
        this.oldPassword = oldPassword;
        this.newPassword = newPassword;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOldPassword() {
        return this.oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public String getNewPassword() {
        return this.newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public void setLibraryRef(String libraryRef) {
        this.libraryRef = libraryRef;
    }

    public String getLibraryRef() {
        return libraryRef;
    }
    
}
