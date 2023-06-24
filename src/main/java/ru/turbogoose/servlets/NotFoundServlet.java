package ru.turbogoose.servlets;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/*")
public class NotFoundServlet extends CustomHttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        processNotFoundError(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        processNotFoundError(req, resp);
    }

    private void processNotFoundError(HttpServletRequest req, HttpServletResponse resp) {
        try {
            String message = "Resource not found: " + req.getRequestURI();
            sendErrorMessageWithCode(resp, 404, message);
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            resp.setStatus(500);
        }
    }
}
