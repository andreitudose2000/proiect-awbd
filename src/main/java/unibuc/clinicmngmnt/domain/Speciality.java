package unibuc.clinicmngmnt.domain;

public enum Speciality {
    ENDODONTIST("Endodontist"),
    ORTHODONTIST("Orthodontist"),
    PERIODONTIST("Periodontist"),
    SURGEON("Surgeon");

    private final String displayName;

    Speciality(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
    }
