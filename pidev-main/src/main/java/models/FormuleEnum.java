package models;

public enum FormuleEnum {

    REPAS_SEUL("repas"),
    BOISSONS_SEULES("boissons"),
    AUCUN("aucun"),
    LES_DEUX("les_deux");

    private final String value;

    FormuleEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
