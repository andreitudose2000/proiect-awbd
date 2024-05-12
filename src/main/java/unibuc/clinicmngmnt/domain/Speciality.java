package unibuc.clinicmngmnt.domain;

public enum Speciality {
    ELECTRICS("Electrica"),
    AUTOBODY("Tinichigerie"),
    ENGINE("Motorizare"),
    SUSPENSION("Suspensii");

    private final String displayName;

    Speciality(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
    }
