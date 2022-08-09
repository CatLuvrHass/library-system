package core;

public class Member {
    private static final char MALE = 'M';
    private static final char FEMALE = 'F';
    private static final char UNKNOWN = 'U';

    public static char getMaleChar() {
        return MALE;
    }

    public static char getFemaleChar() {
        return FEMALE;
    }

    public static char getUnknownChar() {
        return UNKNOWN;
    }

    // Constructor that does not include id -> id is assigned through auto-incrementation in database
    public Member(String name, char gender, int yearOfBirth, String password, String homeLibrary, String phoneNumber, String email) {
        this.name = name;
        this.gender = gender;
        this.yearOfBirth = yearOfBirth;
        this.password = password;
        this.homeLibrary = homeLibrary;
        this.phoneNumber = phoneNumber;
        this.email = email;
    }

    public Member(String name, char gender, int yearOfBirth, String password, int id, String homeLibrary, String phoneNumber, String email) {
        this.name = name;
        this.gender = gender;
        this.yearOfBirth = yearOfBirth;
        this.password = password;
        this.id = id;
        this.homeLibrary = homeLibrary;
        this.phoneNumber = phoneNumber;
        this.email = email;
    }

    public Member() {}

    private String name;
    private char gender;
    private int yearOfBirth;
    private String password;
    private int id;
    private String homeLibrary;
    private String phoneNumber;
    private String email;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public char getGender() {
        return gender;
    }

    public void setGender(char gender) {
        this.gender = gender;
    }

    public int getYearOfBirth() {
        return yearOfBirth;
    }

    public void setYearOfBirth(int yearOfBirth) {
        this.yearOfBirth = yearOfBirth;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getHomeLibrary() {
        return this.homeLibrary;
    }

    public void setId(String homeLibrary) {
        this.homeLibrary = homeLibrary;
    }

    public String getPhoneNumber() {
        return this.phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}

