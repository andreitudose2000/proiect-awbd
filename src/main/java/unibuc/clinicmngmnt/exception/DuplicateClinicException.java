package unibuc.clinicmngmnt.exception;

public class DuplicateClinicException extends RuntimeException {
    public DuplicateClinicException() {
        super("Un service cu acelasi nume deja exista.");
    }
}
