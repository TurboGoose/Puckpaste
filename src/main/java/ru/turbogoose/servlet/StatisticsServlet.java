package ru.turbogoose.servlet;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.turbogoose.dao.DaoSingletonFactory;
import ru.turbogoose.dto.StatisticsDto;
import ru.turbogoose.service.StatisticsService;

import java.io.IOException;

@WebServlet("/stats")
public class StatisticsServlet extends JsonServlet {
    private final StatisticsService statisticsService = new StatisticsService(DaoSingletonFactory.getInstance());

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        StatisticsDto statisticsDto = statisticsService.getTotalPostCount();
        sendResponse(resp, 200, statisticsDto);
    }
}
