package ru.turbogoose.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.turbogoose.dto.ErrorDto;
import ru.turbogoose.exception.PathMappingNotFoundException;
import ru.turbogoose.json.JsonMapper;
import ru.turbogoose.json.JsonMapperSingletonFactory;

import java.io.IOException;

@WebServlet("/*")
public class JsonServlet extends DispatcherServlet {
    protected final JsonMapper jsonMapper = JsonMapperSingletonFactory.getInstance();

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            super.service(req, resp);
        } catch (PathMappingNotFoundException exc) {
            sendError(resp, 404, exc.getMessage());
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            resp.setStatus(500);
        }
    }

    protected void sendResponse(HttpServletResponse resp, int code, Object obj) throws IOException {
        resp.setStatus(code);
        resp.setContentType("application/json");
        jsonMapper.serialize(resp.getWriter(), obj);
    }

    protected void sendError(HttpServletResponse resp, int code, String message) throws IOException {
        sendResponse(resp, code, new ErrorDto(message));
    }
}
