package unibuc.carServiceManagement.exception;

public class AppointmentsOverlappingException extends RuntimeException {
    public AppointmentsOverlappingException() {
        super("Programarea se suprapune cu o alta programare existenta.");
    }
}
