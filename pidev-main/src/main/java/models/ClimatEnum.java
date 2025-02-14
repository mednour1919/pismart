package models;

public enum ClimatEnum {

    TROPICAL("tropical"),
    CHAUD("chaud"),
    FROID("froid"),
    DESERT("desert");

    private final String value;

    ClimatEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
