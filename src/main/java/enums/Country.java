package enums;

public enum Country {
    UNDEFINED("Не выбрано"),
    RUSSIA("Россия"),
    BELARUS("Республика Беларусь"),
    KAZAKHSTAN("Казахстан"),
    UKRAINE("Украина");

    private final String name;

    private Country(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}