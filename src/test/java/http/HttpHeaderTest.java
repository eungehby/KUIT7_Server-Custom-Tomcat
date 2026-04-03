package http;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;

public class HttpHeaderTest {
    @Test
    void 헤더_분석_테스트() throws IOException {
        // Given: 여러 줄의 헤더 문자열 (끝에 빈 줄 포함)
        String headerString = "Host: localhost:8080\r\n" +
                "Content-Length: 40\r\n" +
                "\r\n";
        BufferedReader br = new BufferedReader(new StringReader(headerString));

        // When: 헤더 객체 생성
        HttpHeader header = HttpHeader.from(br);

        // Then: 값 확인
        assertEquals("localhost:8080", header.get("Host"));
        assertEquals(40, header.getContentLength());
    }
}
