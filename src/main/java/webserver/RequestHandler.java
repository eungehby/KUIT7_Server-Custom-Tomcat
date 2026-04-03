package webserver;

import db.MemoryUserRepository;
import http.util.HttpRequestUtils;
import model.User;

import java.io.*;
import java.net.Socket;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.Map;

public class RequestHandler implements Runnable {
    Socket connection;
    private static final Logger log = Logger.getLogger(RequestHandler.class.getName());

    public RequestHandler(Socket connection) {
        this.connection = connection;
    }@Override
    public void run() {
        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            DataOutputStream dos = new DataOutputStream(out);
            boolean logined = false;

            // Request Line 읽기
            String line = br.readLine();
            if (line == null) return;

            String[] tokens = line.split(" ");
            String method = tokens[0];
            String url = tokens[1];

            // Header 읽기 및 Content-Length 추출
            int contentLength = 0;
            while (line != null && !line.equals("")) {
                line = br.readLine();
                if (line != null) {
                    log.log(Level.INFO, "Header : " + line);

                    // Content-Length 확인
                    if (line.startsWith("Content-Length")) {
                        contentLength = Integer.parseInt(line.split(": ")[1]);
                    }

                    // Cookie 헤더에서 로그인 상태 확인
                    if (line.startsWith("Cookie")) {
                        logined = isLogined(line);
                    }
                }
            }

            // POST 방식의 회원가입 처리
            if ("POST".equals(method) && url.startsWith("/user/signup")) {
                String bodyData = http.util.IOUtils.readData(br, contentLength);
                Map<String, String> params = http.util.HttpRequestUtils.parseQueryParameter(bodyData);

                User user = new User(
                        params.get("userId"),
                        params.get("password"),
                        params.get("name"),
                        params.get("email")
                );

                db.MemoryUserRepository.getInstance().addUser(user);
                response302Header(dos, "/index.html");
                return;
            }

            // 로그인 처리 (POST /user/login)
            if ("POST".equals(method) && url.startsWith("/user/login")) {
                String bodyData = http.util.IOUtils.readData(br, contentLength);
                Map<String, String> params = http.util.HttpRequestUtils.parseQueryParameter(bodyData);

                // Repository에서 유저 찾기
                User user = db.MemoryUserRepository.getInstance().findUserById(params.get("userId"));

                // 로그인 검증: 유저가 존재하고 비밀번호가 일치하는지 확인
                if (user != null && user.getPassword().equals(params.get("password"))) {
                    log.log(Level.INFO, "로그인 성공: " + user.getUserId());
                    response302LoginSuccessHeader(dos);
                } else {
                    log.log(Level.INFO, "로그인 실패");
                    response302Header(dos, "/user/login_failed.html");
                }
                return;
            }

            if (url.equals("/user/userList")) {
                if (logined) {
                    url = "/user/list.html";
                } else {
                    response302Header(dos, "/user/login.html");
                    return;
                }
            }

            // 루트 경로 매핑
            if (url.equals("/")) {
                url = "/index.html";
            }

            // 정적 파일 읽기 및 예외 상황(404) 처리
            File file = new File("./webapp" + url);
            if (file.exists() && !file.isDirectory()) {
                byte[] body = Files.readAllBytes(file.toPath());

                // 확장자에 따라 Content-Type 결정
                String contentType = "text/html";
                if (url.endsWith(".css")) {
                    contentType = "text/css";
                } else if (url.endsWith(".js")) {
                    contentType = "application/javascript";
                }

                response200Header(dos, body.length, contentType);
                responseBody(dos, body);
            } else {
                log.log(Level.WARNING, "파일을 찾을 수 없음: " + url);
                dos.writeBytes("HTTP/1.1 404 Not Found \r\n\r\n");
                dos.flush();
            }
        } catch (IOException e) {
            log.log(Level.SEVERE, e.getMessage());
        }
    }

    private boolean isLogined(String cookieHeader) {
        String cookies = cookieHeader.split(": ")[1];
        Map<String, String> cookieMap = HttpRequestUtils.parseCookies(cookies);
        return Boolean.parseBoolean(cookieMap.get("logined"));
    }

    private void response200Header(DataOutputStream dos, int lengthOfBodyContent, String contentType) {
        try {
            dos.writeBytes("HTTP/1.1 200 OK \r\n");
            dos.writeBytes("Content-Type: " + contentType + ";charset=utf-8\r\n");
            dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            log.log(Level.SEVERE, e.getMessage());
        }
    }

    private void response302Header(DataOutputStream dos, String path) {
        try {
            dos.writeBytes("HTTP/1.1 302 Found \r\n");
            dos.writeBytes("Location: " + path + "\r\n");
            dos.writeBytes("\r\n");
            dos.flush();
        } catch (IOException e) {
            log.log(Level.SEVERE, e.getMessage());
        }
    }

    // 로그인 성공 시 쿠키를 포함하는 리다이렉트 응답
    private void response302LoginSuccessHeader(DataOutputStream dos) {
        try {
            dos.writeBytes("HTTP/1.1 302 Found \r\n");
            dos.writeBytes("Set-Cookie: logined=true; Path=/ \r\n");
            dos.writeBytes("Location: /index.html \r\n");
            dos.writeBytes("\r\n");
            dos.flush();
        } catch (IOException e) {
            log.log(Level.SEVERE, e.getMessage());
        }
    }

    private void responseBody(DataOutputStream dos, byte[] body) {
        try {
            dos.write(body, 0, body.length);
            dos.flush();
        } catch (IOException e) {
            log.log(Level.SEVERE, e.getMessage());
        }
    }
}