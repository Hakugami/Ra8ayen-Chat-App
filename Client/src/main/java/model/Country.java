package model;

public class Country {

    String name;
    String dial_code;
    String code ;

    public Country(String name, String dial_code, String code) {
        this.name = name;
        this.dial_code = dial_code;
        this.code = code;
    }

    public Country(String name) {
        this.name = name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDial_code(String dial_code) {
        this.dial_code = dial_code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public String getDial_code() {
        return dial_code;
    }

    public String getCode() {
        return code;
    }

    @Override
    public String toString() {
        return " (" + dial_code + ") "+name  ;
    }
}