package ru.turbogoose.services;

import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.impl.StdSchedulerFactory;
import ru.turbogoose.dao.PostDao;
import ru.turbogoose.quartzjobs.CleanupJob;

import static org.quartz.JobBuilder.newJob;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;

public class CleanupService {
    private final Scheduler scheduler;

    public CleanupService(PostDao dao) throws SchedulerException {
        scheduler = new StdSchedulerFactory().getScheduler();

        JobDetail job = newJob(CleanupJob.class)
                .withIdentity("cleanupJob")
                .build();
        job.getJobDataMap().put("dao", dao);

        Trigger trigger = newTrigger()
                .withIdentity("cleanupTrigger")
                .startNow()
                .withSchedule(simpleSchedule()
                        .withIntervalInHours(2)
                        .repeatForever())
                .build();

        scheduler.scheduleJob(job, trigger);
    }

    public void start() throws SchedulerException {
        scheduler.start();
    }

    public void shutdown() throws SchedulerException {
        scheduler.shutdown();
    }
}