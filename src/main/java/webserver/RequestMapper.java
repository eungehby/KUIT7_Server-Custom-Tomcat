package webserver;

import controller.*;
import http.HttpRequest;
import http.HttpResponse;
import java.util.HashMap;
import java.util.Map;

public class RequestMapper {
    private static final Map<String, Controller> controllers = new HashMap<>();
    private final HttpRequest request;
    private final HttpResponse response;

    static {
        controllers.put("/", new HomeController());
        controllers.put("/user/signup", new SignUpController());
        controllers.put("/user/login", new LoginController());
        controllers.put("/user/userList", new ListController());
    }

    public RequestMapper(HttpRequest request, HttpResponse response) {
        this.request = request;
        this.response = response;
    }

    public void proceed() {
        String url = request.getUrl();

        Controller controller = controllers.getOrDefault(url, new ForwardController());

        controller.execute(request, response);
    }
}