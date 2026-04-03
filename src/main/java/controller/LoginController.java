package controller;

import db.MemoryUserRepository;
import http.HttpRequest;
import http.HttpResponse;
import model.User;

public class LoginController implements Controller {
    @Override
    public void execute(HttpRequest request, HttpResponse response) {
        User user = MemoryUserRepository.getInstance().findUserById(request.getParameter("userId"));

        if (user != null && user.getPassword().equals(request.getParameter("password"))) {
            response.sendRedirect("/index.html");
        } else {
            response.sendRedirect("/user/login_failed.html");
        }
    }
}