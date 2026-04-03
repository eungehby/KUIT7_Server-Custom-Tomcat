package http.util;

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
}