package http;

import static org.junit.jupiter.api.Assertions.assertEquals; // assertEquals를 쓰기 위한 static import
import enums.HttpMethod; // 우리가 만든 Enum 가져오기
import org.junit.jupiter.api.Test; // @Test 어노테이션을 쓰기 위함

public class HttpStartLineTest {
    @Test
    void 시작줄_분석_테스트() {
        // Given: 날것의 문자열 한 줄
        String line = "GET /index.html HTTP/1.1";

        // When: 객체 생성
        HttpStartLine startLine = HttpStartLine.from(line);

        // Then: 각 값이 잘 들어갔는지 확인
        assertEquals(HttpMethod.GET, startLine.getMethod());
        assertEquals("/index.html", startLine.getPath());
        assertEquals("HTTP/1.1", startLine.getVersion());
    }
}
