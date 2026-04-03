package http;

import static org.junit.jupiter.api.Assertions.assertEquals;
import enums.HttpMethod; // HttpMethod.POST 등을 비교할 때 필요해요!
import org.junit.jupiter.api.Test;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files; // 파일을 읽을 때 필요해요!
import java.nio.file.Paths; // 파일 경로를 지정할 때 필요해요!

public class HttpRequestTest {
    @Test
    void 전체_요청_조립_테스트() throws IOException {
        // Given: 파일 준비
        InputStream in = Files.newInputStream(Paths.get("./src/test/resources/http_post.txt"));
        BufferedReader br = new BufferedReader(new InputStreamReader(in));

        // When: 전체 조립
        HttpRequest request = HttpRequest.from(br);

        // Then: 부품들이 잘 연동되는지 확인
        assertEquals(HttpMethod.POST, request.getMethod()); // startLine 부품 확인
        assertEquals(40, Integer.parseInt(request.getHeader("Content-Length"))); // header 부품 확인
        assertEquals("jw", request.getParameter("userId")); // body 분석 확인
    }
}
