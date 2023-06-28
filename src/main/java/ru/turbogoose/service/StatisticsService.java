package ru.turbogoose.service;

import ru.turbogoose.dao.PostDao;
import ru.turbogoose.dto.StatisticsDto;

public class StatisticsService {
    private final PostDao dao;

    public StatisticsService(PostDao dao) {
        this.dao = dao;
    }

    public StatisticsDto getTotalPostCount() {
        StatisticsDto stats = new StatisticsDto();
        stats.setPostsTotalCount(dao.getCount());
        return stats;
    }
}
