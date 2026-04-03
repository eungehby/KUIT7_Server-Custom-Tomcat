package enums;

public enum ContentType {
    HTML("text/html;charset=utf-8"),
    CSS("text/css"),
    JS("application/javascript"),
    NONE("*/*");

    private final String value;

    ContentType(String value) { this.value = value; }

    public String getValue() { return value; }

    public static ContentType from(String url) {
        if (url.endsWith(".css")) return CSS;
        if (url.endsWith(".js")) return JS;
        if (url.endsWith(".html")) return HTML;
        return NONE;
    }
}
