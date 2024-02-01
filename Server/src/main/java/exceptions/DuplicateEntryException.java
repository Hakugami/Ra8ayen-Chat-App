package exceptions;

import java.sql.SQLException;

public class DuplicateEntryException extends SQLException {

    private String duplicateColumn;
    private String duplicateValue;

    public DuplicateEntryException(String message, Throwable cause) {
        super(message, cause);
        parseErrorMessage(message);
    }

    private void parseErrorMessage(String message) {
        String[] parts = message.split("'");
        if (parts.length >= 4) {
            this.duplicateValue = parts[1];
            this.duplicateColumn = parts[3].split("\\.")[1];
        }
    }

    public String getDuplicateColumn() {
        return duplicateColumn;
    }

    public String getDuplicateValue() {
        return duplicateValue;
    }
}