package unibuc.clinicmngmnt.exception;

public class DuplicateClinicException extends RuntimeException {
    public DuplicateClinicException() {
        super("A clinic with the same name already exists.");
    }
}
