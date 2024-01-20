package prototyping.models;

public class Country {
    private String name;
    private String dial_code;
    private String code; // Add this line

    // getters and setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDial_code() {
        return dial_code;
    }

    public void setDial_code(String dial_code) {
        this.dial_code = dial_code;
    }

    public String getCode() { // Add this method
        return code;
    }

    public void setCode(String code) { // Add this method
        this.code = code;
    }
}