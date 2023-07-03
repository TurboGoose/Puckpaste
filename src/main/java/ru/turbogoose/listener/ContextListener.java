package ru.turbogoose.listener;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import org.quartz.SchedulerException;
import ru.turbogoose.dao.DaoSingletonFactory;
import ru.turbogoose.dao.PostDao;
import ru.turbogoose.service.CleanupService;
import ru.turbogoose.util.PropertyReader;

import java.io.IOException;
import java.util.Optional;
import java.util.Properties;

@WebListener
public class ContextListener implements ServletContextListener {
    private static final String ENV_PROFILE = "ENV_PROFILE";
    private CleanupService cleanupService;

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        try {
            Class.forName("org.sqlite.JDBC");

            String sysEnv = System.getenv(ENV_PROFILE);
            String profile = Optional.ofNullable(sysEnv).orElse("dev");
            Properties dbProps = PropertyReader.fromFile(profile + "/application.properties");

            DaoSingletonFactory.init(dbProps);
            PostDao dao = DaoSingletonFactory.getInstance();

            cleanupService = new CleanupService(dao);
            cleanupService.start();
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
