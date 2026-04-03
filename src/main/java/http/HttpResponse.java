package http;

import enums.ContentType;
import enums.HttpStatus;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;

public class HttpResponse {
    private final DataOutputStream dos;
    private HttpStatusLine statusLine;
    private final HttpHeader header = new HttpHeader();

    public HttpResponse(OutputStream out) {
        this.dos = new DataOutputStream(out);
    }

    public void forward(String url) {
        try {
            byte[] body = Files.readAllBytes(new File("./webapp" + url).toPath());

            this.statusLine = new HttpStatusLine(HttpStatus.OK);
            header.add("Content-Type", ContentType.from(url).getValue());
            header.add("Content-Length", String.valueOf(body.length));

            sendResponse(body);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendRedirect(String url) {
        this.statusLine = new HttpStatusLine(HttpStatus.FOUND);
        header.add("Location", url);
        try {
            sendResponse(new byte[0]);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sendResponse(byte[] body) throws IOException {
        dos.writeBytes(statusLine.toString());
        dos.writeBytes(header.toString());
        dos.writeBytes("\r\n");
        if (body.length > 0) {
            dos.write(body, 0, body.length);
        }
        dos.flush();
    }
}