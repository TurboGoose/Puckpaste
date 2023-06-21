package ru.turbogoose.servlets;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.turbogoose.dao.PostDao;
import ru.turbogoose.dto.ErrorDto;
import ru.turbogoose.dto.StatisticsDto;
import ru.turbogoose.services.StatisticsService;

import java.io.Writer;

@WebServlet("/stats")
public class StatisticsServlet extends HttpServlet {
    private StatisticsService statisticsService;
    private ObjectMapper objectMapper;

    @Override
    public void init() {
        objectMapper = (ObjectMapper) getServletContext().getAttribute("objectMapper");
        PostDao dao = (PostDao) getServletContext().getAttribute("dao");
        statisticsService = new StatisticsService(dao);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        try (Writer writer = resp.getWriter()) {
            resp.setContentType("application/json");
            try {
                StatisticsDto statisticsDto = statisticsService.getTotalPostCount();
                objectMapper.writeValue(writer, statisticsDto);
                resp.setStatus(200);
            } catch (JacksonException exc) {
                exc.printStackTrace();
                objectMapper.writeValue(writer, new ErrorDto(exc.getMessage()));
                resp.setStatus(404);
            }
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            resp.setStatus(500);
        }
    }
}
