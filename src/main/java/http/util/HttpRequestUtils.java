package http.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class HttpRequestUtils {
    public static Map<String, String> parseQueryParameter(String queryString) {
        try {
            String[] queryStrings = queryString.split("&");
            return Arrays.stream(queryStrings)
                    .map(q -> q.split("="))
                    .collect(Collectors.toMap(queries -> queries[0], queries -> queries[1]));
        } catch (Exception e) {
            return new HashMap<>();
        }
    }

    // 쿠키 파싱 메서드
    public static Map<String, String> parseCookies(String cookieValue) {
        if (cookieValue == null || cookieValue.isEmpty()) {
            return new HashMap<>();
        }
        try {
            return Arrays.stream(cookieValue.split("; "))
                    .map(pair -> pair.split("="))
                    .filter(parts -> parts.length == 2)
                    .collect(Collectors.toMap(parts -> parts[0], parts -> parts[1]));
        } catch (Exception e) {
            return new HashMap<>();
        }
    }

    public static Map<String, String> parseHeaders(BufferedReader br) throws IOException {
        Map<String, String> headers = new HashMap<>();
        String line = br.readLine();

        // HTTP 헤더는 빈 줄("")이 나오기 전까지가 한 묶음입니다.
        while (line != null && !line.equals("")) {
            String[] tokens = line.split(": ");
            if (tokens.length == 2) {
                // 예: "Content-Length: 40" -> key: "Content-Length", value: "40"
                headers.put(tokens[0], tokens[1]);
            }
            line = br.readLine();
        }
        return headers;
    }
}