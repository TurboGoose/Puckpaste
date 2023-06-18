package ru.turbogoose.servlets;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.turbogoose.dao.PostDao;
import ru.turbogoose.dao.SqlitePostDao;
import ru.turbogoose.dto.ErrorDto;
import ru.turbogoose.dto.StatisticsDto;
import ru.turbogoose.services.StatisticsService;
import ru.turbogoose.utils.PropertyReader;

import java.io.IOException;
import java.io.Writer;
import java.util.Properties;

@WebServlet("/stats")
public class StatisticsServlet extends HttpServlet {
    private StatisticsService statisticsService;
    private ObjectMapper objectMapper;

    @Override
    public void init() {
        objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();

        // TODO: move this duplicated code to application startup logic? (but it stateless, mb pohui?)
        try {
            Class.forName("org.sqlite.JDBC");
            Properties dbProps = PropertyReader.fromFile("database.properties");
            PostDao dao = new SqlitePostDao(dbProps);
            statisticsService = new StatisticsService(dao);
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
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
