package ru.turbogoose.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.turbogoose.dto.ErrorDto;

@WebServlet("/*")
public class NotFoundServlet extends HttpServlet {
    private ObjectMapper objectMapper;

    @Override
    public void init() {
        objectMapper = (ObjectMapper) getServletContext().getAttribute("objectMapper");
    }

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
            resp.setStatus(404);
            String uri = req.getRequestURI();
            String message = "Resource not found: " + uri;
            objectMapper.writeValue(resp.getWriter(), new ErrorDto(message));
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            resp.setStatus(500);
        }
    }
}
