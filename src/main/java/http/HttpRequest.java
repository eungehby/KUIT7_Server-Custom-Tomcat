package http;

import enums.HttpMethod;
import http.util.HttpRequestUtils; // 파싱을 위해 필요해요!
import http.util.IOUtils;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.Map; // Map을 쓰기 위해 추가!

public class HttpRequest {
    private final HttpStartLine startLine;
    private final HttpHeader header;
    private final String body;
    private final Map<String, String> params;

    private HttpRequest(HttpStartLine startLine, HttpHeader header, String body, Map<String, String> params) {
        this.startLine = startLine;
        this.header = header;
        this.body = body;
        this.params = params;
    }

    public static HttpRequest from(BufferedReader br) throws IOException {
        HttpStartLine startLine = HttpStartLine.from(br.readLine());
        HttpHeader header = HttpHeader.from(br);

        String body = "";
        int length = header.getContentLength();
        if (length > 0) {
            body = IOUtils.readData(br, length);
        }

        Map<String, String> params = HttpRequestUtils.parseQueryParameter(body);

        return new HttpRequest(startLine, header, body, params);
    }

    public String getParameter(String name) {
        return params.get(name);
    }

    public HttpMethod getMethod() { return startLine.getMethod(); }
    public String getUrl() { return startLine.getPath(); }
    public String getHeader(String key) { return header.get(key); }
}