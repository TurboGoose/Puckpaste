package ru.turbogoose.servlets;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletResponse;
import ru.turbogoose.dto.ErrorDto;
import ru.turbogoose.json.JsonMapper;
import ru.turbogoose.json.JsonMapperFactory;

import java.io.IOException;

public class CustomHttpServlet extends HttpServlet {
    protected JsonMapper jsonMapper = JsonMapperFactory.getMapper();

    public void sendErrorMessageWithCode(HttpServletResponse resp, int code, String message) throws IOException {
        resp.setStatus(code);
        resp.setContentType("application/json");
        jsonMapper.serialize(resp.getWriter(), new ErrorDto(message));
    }
}
