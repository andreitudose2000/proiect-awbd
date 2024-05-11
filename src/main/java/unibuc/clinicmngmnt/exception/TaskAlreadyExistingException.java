package unibuc.clinicmngmnt.exception;

public class TaskAlreadyExistingException extends RuntimeException{
    public TaskAlreadyExistingException(long appointmentId) {
        super("Programarea cu ID " + appointmentId + " contine deja aceasta interventie");
    }
}
