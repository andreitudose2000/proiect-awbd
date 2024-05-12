package unibuc.carServiceManagement.exception;

public class WrongDatesOrderException extends RuntimeException {
    public WrongDatesOrderException() {
        super("Data si ora final trebuie sa fie mai mare decat data si ora inceput");
    }
}
