package messages.borrow;

import messages.MySerializable;

public class AddBorrowingPrivileges implements MySerializable {
    private int memberId;

    public AddBorrowingPrivileges() {}

    public AddBorrowingPrivileges(int memberId) {
        this.memberId = memberId;
    }

    public int getMemberId() {
        return this.memberId;
    }

    public void setMemberId(int memberId) {
        this.memberId = memberId;
    }
    
}
