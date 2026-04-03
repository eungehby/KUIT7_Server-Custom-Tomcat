package webserver;

import http.HttpRequest;
import http.HttpResponse;
import org.junit.jupiter.api.Test;
import java.io.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class RequestMapperTest {

    @Test
    void 메인_페이지_매핑_및_응답_테스트() throws IOException {
        // 1. Given: "/" 경로로 들어오는 요청을 가짜로 만듭니다.
        String requestString = "GET / HTTP/1.1\r\n" +
                "Host: localhost:8080\r\n" +
                "\r\n";
        BufferedReader br = new BufferedReader(new StringReader(requestString));
        HttpRequest request = HttpRequest.from(br);

        // 응답을 담을 바구니(메모리 스트림)를 준비합니다.
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        HttpResponse response = new HttpResponse(out);

        // 2. When: Mapper를 통해 로직을 수행합니다.
        RequestMapper mapper = new RequestMapper(request, response);
        mapper.proceed();

        // 3. Then: HomeController가 실행되어 "200 OK" 응답이 생성되었는지 확인합니다.
        String result = out.toString();
        assertTrue(result.contains("HTTP/1.1 200 OK"));
        // index.html의 내용 일부가 포함되어 있는지 확인 (파일 내용에 따라 수정)
        assertTrue(result.contains("<html") || result.contains("<!DOCTYPE"));
    }

    @Test
    void 회원가입_리다이렉트_매핑_테스트() throws IOException {
        // Given: 회원가입 요청 (POST 데이터 포함)
        String requestString = "POST /user/signup HTTP/1.1\r\n" +
                "Content-Length: 40\r\n" +
                "\r\n" +
                "userId=tester&password=1234&name=lee&email=a@a.com";
        BufferedReader br = new BufferedReader(new StringReader(requestString));
        HttpRequest request = HttpRequest.from(br);

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        HttpResponse response = new HttpResponse(out);

        // When
        RequestMapper mapper = new RequestMapper(request, response);
        mapper.proceed();

        // Then: SignUpController가 실행되어 "302 Found"와 리다이렉트 경로가 찍혔는지 확인
        String result = out.toString();
        assertTrue(result.contains("HTTP/1.1 302 Found"));
        assertTrue(result.contains("Location: /index.html"));
    }
}