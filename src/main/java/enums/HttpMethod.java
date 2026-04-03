package enums;

public enum HttpMethod {
    GET, POST, PUT, DELETE;

    public static HttpMethod of(String method) {
        return HttpMethod.valueOf(method.toUpperCase());
    }
}
