package enums;

public enum CommunicationMethod {
    UNDEFINED("Не выбрано"),
    FACEBOOK("Facebook"),
    VK("VK"),
    OK("OK"),
    SKYPE("Skype"),
    VIBER("Viber"),
    TELEGRAM("Тelegram"),
    WHATSAPP("WhatsApp");

    private final String name;

    private CommunicationMethod(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}