package unibuc.clinicmngmnt.exception;

public class WrongDatesOrderException extends RuntimeException {
    public WrongDatesOrderException() {
        super("End date must be greater than start date");
    }
}
