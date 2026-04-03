package controller;

import http.HttpRequest;
import http.HttpResponse;

public class ListController implements Controller {
    @Override
    public void execute(HttpRequest request, HttpResponse response) {
        response.forward("/user/list.html");
    }
}