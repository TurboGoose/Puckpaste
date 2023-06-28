package ru.turbogoose.listener;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import org.quartz.SchedulerException;
import ru.turbogoose.dao.DaoFactory;
import ru.turbogoose.dao.PostDao;
import ru.turbogoose.service.CleanupService;
import ru.turbogoose.util.PropertyReader;

import java.io.IOException;
import java.util.Optional;
import java.util.Properties;

@WebListener
public class ContextListener implements ServletContextListener {
    private static final String envProfile = Optional.ofNullable(System.getenv("PUCKPASTE_ENV")).orElse("dev");
    private CleanupService cleanupService;

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        try {
            Class.forName("org.sqlite.JDBC");
            Properties dbProps = PropertyReader.fromFile(envProfile + "/application.properties");
            PostDao dao = DaoFactory.getPostDao(dbProps);

            cleanupService = new CleanupService(dao);
            cleanupService.start();

            ServletContext context = sce.getServletContext();
            context.setAttribute("dao", dao);
        } catch (IOException | ClassNotFoundException | SchedulerException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        try {
            cleanupService.shutdown();
        } catch (SchedulerException e) {
            throw new RuntimeException(e);
        }
    }
}
