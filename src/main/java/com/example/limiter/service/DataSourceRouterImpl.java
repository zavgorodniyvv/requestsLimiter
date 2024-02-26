package com.example.limiter.service;

import com.example.limiter.dao.UserElasticSearchRepository;
import com.example.limiter.dao.UserMySQLRepository;
import com.example.limiter.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.time.OffsetTime;
import java.time.ZoneOffset;

@Service
public class DataSourceRouterImpl implements DataSourceRouter{
    private final UserMySQLRepository mySQLRepository;
    private final UserElasticSearchRepository elasticSearchRepository;
    private static final Logger logger = LoggerFactory.getLogger(DataSourceRouterImpl.class);

    public DataSourceRouterImpl(UserMySQLRepository mySQLRepository, UserElasticSearchRepository elasticSearchRepository) {
        this.mySQLRepository = mySQLRepository;
        this.elasticSearchRepository = elasticSearchRepository;
    }

    @Override
    public CrudRepository<User, String> getRepository() {
        LocalTime currentTimeUTC = LocalTime.now(ZoneOffset.UTC);
        LocalTime mySQLWorkTimeStartUTC = TimeUtils.getTimeInUTC(LocalTime.of(7, 0));
        LocalTime mySQLWorkTimeEndUTC = TimeUtils.getTimeInUTC(LocalTime.of(17, 0));

        if(currentTimeUTC.isAfter(mySQLWorkTimeStartUTC) && currentTimeUTC.isBefore(mySQLWorkTimeEndUTC)) {
            logger.debug("Current time is within MySQL work hours");
            return mySQLRepository;
        }
        logger.debug("Current time is within Elasticsearch work hours");
        return elasticSearchRepository;
    }
}
