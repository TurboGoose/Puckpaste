package ru.turbogoose.quartzjobs;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import ru.turbogoose.dao.PostDao;

public class CleanupJob implements Job {
    @Override
    public void execute(JobExecutionContext jobExecutionContext) {
        JobDataMap dataMap = jobExecutionContext.getJobDetail().getJobDataMap();
        PostDao dao = (PostDao) dataMap.get("dao");
        dao.deleteExpired();
    }
}
