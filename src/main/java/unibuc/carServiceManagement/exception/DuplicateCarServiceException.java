package unibuc.carServiceManagement.exception;

public class DuplicateCarServiceException extends RuntimeException {
    public DuplicateCarServiceException() {
        super("Un service cu acelasi nume deja exista.");
    }
}
