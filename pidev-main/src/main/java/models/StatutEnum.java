package models;

public enum StatutEnum {

    EN_ATTENTE("en attente"),
    CONFIRME("confirme"),
    ANNULE("annule");

    private final String value;

    StatutEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
