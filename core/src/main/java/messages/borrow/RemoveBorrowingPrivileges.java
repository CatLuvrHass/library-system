package messages.borrow;

import messages.MySerializable;

public class RemoveBorrowingPrivileges implements MySerializable {
    private int memberId;

    public RemoveBorrowingPrivileges() {}

    public RemoveBorrowingPrivileges(int memberId) {
        this.memberId = memberId;
    }

    public int getMemberId() {
        return this.memberId;
    }

    public void setMemberId(int memberId) {
        this.memberId = memberId;
    }
    
}
