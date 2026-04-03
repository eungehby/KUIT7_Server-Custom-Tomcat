package http;

import http.util.HttpRequestUtils;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap; // ⭐️ 추가: HashMap 임포트
import java.util.Map;

public class HttpHeader {
    private final Map<String, String> headers;

    public HttpHeader() {
        this.headers = new HashMap<>();
    }

    public HttpHeader(Map<String, String> headers) {
        this.headers = headers;
    }

    public void add(String key, String value) {
        headers.put(key, value);
    }

    public static HttpHeader from(BufferedReader br) throws IOException {
        Map<String, String> headers = HttpRequestUtils.parseHeaders(br);
        return new HttpHeader(headers);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, String> entry : headers.entrySet()) {
            sb.append(entry.getKey()).append(": ").append(entry.getValue()).append("\r\n");
        }
        return sb.toString();
    }

    public String get(String key) {
        return headers.get(key);
    }

    public int getContentLength() {
        String length = headers.getOrDefault("Content-Length", "0");
        return Integer.parseInt(length);
    }
}