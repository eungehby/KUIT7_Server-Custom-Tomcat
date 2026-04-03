package http;

import enums.HttpMethod;

public class HttpStartLine {
    private final HttpMethod method;
    private final String path;
    private final String version;

    private HttpStartLine(HttpMethod method, String path, String version) {
        this.method = method;
        this.path = path;
        this.version = version;
    }

    public static HttpStartLine from(String startLine) {
        if (startLine == null || startLine.isEmpty()) {
            throw new IllegalArgumentException("유효하지 않은 Start Line입니다.");
        }

        String[] tokens = startLine.split(" ");
        return new HttpStartLine(
                HttpMethod.valueOf(tokens[0].toUpperCase()),
                tokens[1],
                tokens[2]
        );
    }

    public HttpMethod getMethod() { return method; }
    public String getPath() { return path; }
    public String getVersion() { return version; }
}