package unibuc.carServiceManagement.exception;

public class SpecialityException extends RuntimeException {
    public SpecialityException() {
        super("Specialitatea trebuie sa fie una dintre urmatoarele: ELECTRICS, AUTOBODY, ENGINE, SUSPENSION");
    }

}
