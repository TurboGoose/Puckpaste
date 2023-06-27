package ru.turbogoose.servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.turbogoose.dao.PostDao;
import ru.turbogoose.dto.StatisticsDto;
import ru.turbogoose.services.StatisticsService;

@WebServlet("/stats")
public class StatisticsServlet extends CustomHttpServlet {
    private StatisticsService statisticsService;

    @Override
    public void init() throws ServletException {
        super.init();
        PostDao dao = (PostDao) getServletContext().getAttribute("dao");
        statisticsService = new StatisticsService(dao);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        try {
            StatisticsDto statisticsDto = statisticsService.getTotalPostCount();
            resp.setStatus(200);
            resp.setContentType("application/json");
            jsonMapper.serialize(resp.getWriter(), statisticsDto);
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            resp.setStatus(500);
        }
    }
}
