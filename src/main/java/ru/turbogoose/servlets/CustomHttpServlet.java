package ru.turbogoose.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletResponse;
import ru.turbogoose.dto.ErrorDto;

import java.io.IOException;

public class CustomHttpServlet extends HttpServlet {
    protected ObjectMapper objectMapper;

    @Override
    public void init() throws ServletException {
        super.init();
        objectMapper = (ObjectMapper) getServletContext().getAttribute("objectMapper");
    }

    public void sendErrorMessageWithCode(HttpServletResponse resp, int code, String message) throws IOException {
        resp.setStatus(code);
        resp.setContentType("application/json");
        objectMapper.writeValue(resp.getWriter(), new ErrorDto(message));
    }
}
