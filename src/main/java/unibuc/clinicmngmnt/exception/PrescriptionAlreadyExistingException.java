package unibuc.clinicmngmnt.exception;

public class PrescriptionAlreadyExistingException extends RuntimeException{
    public PrescriptionAlreadyExistingException(long appointmentId) {
        super("Appointment with ID " + appointmentId + " already has a prescription");
    }
}
