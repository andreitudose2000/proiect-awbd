package unibuc.clinicmngmnt.exception;

public class SpecialityException extends RuntimeException {
    public SpecialityException() {
        super("Speciality must be one of the following: ENDODONTIST, ORTHODONTIST, PERIODONTIST, SURGEON");
    }

}
