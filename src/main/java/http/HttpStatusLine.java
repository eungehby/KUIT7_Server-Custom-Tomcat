package http;

import enums.HttpStatus;

public class HttpStatusLine {
    private final String version = "HTTP/1.1";
    private final HttpStatus status;

    public HttpStatusLine(HttpStatus status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return version + " " + status.getCode() + " " + status.getMessage() + " \r\n";
    }
}
