
public enum enumTemplateName {

    RESET_PASSWORD("reset-password.html"),
    NOTIFICATION_TRANSACTION("transfer.html");

    private final String templateName;

    enumTemplateName(String templateName) {
        this.templateName = templateName;
    }

    @Override
    public String toString() {
        return templateName;
    }

    public static String get(enumTemplateName templateName) {
        return templateName.toString(); // hoặc templateName.fileName;
    }

}
