package http;

import org.junit.jupiter.api.Test;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

public class HttpResponseTest {
    // 테스트 결과가 저장될 경로 (src/test/resources 폴더가 있어야 해요!)
    private String testDirectory = "./src/test/resources/";

    @Test
    void forward_테스트() throws IOException {
        // 1. Given: 응답 결과를 기록할 파일 스트림 준비
        // (파일 이름: response_forward.txt)
        OutputStream out = Files.newOutputStream(Paths.get(testDirectory + "response_forward.txt"));
        HttpResponse response = new HttpResponse(out);

        // 2. When: index.html로 forward 실행
        // (실제로 ./webapp/index.html 파일이 있어야 에러가 안 나요!)
        response.forward("/index.html");

        // 3. Then: 테스트 종료 후 해당 파일을 열어서 눈으로 확인합니다.
    }

    @Test
    void redirect_테스트() throws IOException {
        // 1. Given: 응답 결과를 기록할 파일 스트림 준비
        OutputStream out = Files.newOutputStream(Paths.get(testDirectory + "response_redirect.txt"));
        HttpResponse response = new HttpResponse(out);

        // 2. When: /index.html로 redirect 실행
        response.sendRedirect("/index.html");

        // 3. Then: response_redirect.txt 파일을 확인합니다.
    }
}