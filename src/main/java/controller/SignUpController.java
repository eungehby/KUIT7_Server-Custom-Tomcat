package controller;

import http.HttpRequest;
import http.HttpResponse;
import model.User;
import db.MemoryUserRepository;

public class SignUpController implements Controller {
    @Override
    public void execute(HttpRequest request, HttpResponse response) {
        User user = new User(
                request.getParameter("userId"),
                request.getParameter("password"),
                request.getParameter("name"),
                request.getParameter("email")
        );

        MemoryUserRepository.getInstance().addUser(user);

        response.sendRedirect("/index.html");
    }
}