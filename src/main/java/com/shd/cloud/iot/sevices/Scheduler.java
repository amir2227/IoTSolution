package com.shd.cloud.iot.sevices;

import com.shd.cloud.iot.models.CronJob;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;

@Slf4j
public class Scheduler {
    private String cronExpression;
    private CronJob cronJob;

    public Scheduler(CronJob cronJob){
        this.cronExpression = cronJob.getExpression();
        this.cronJob = cronJob;
    }
    @Scheduled(cron = "#{cronExpression}")
    private void scheduledTask(){
      log.info("expression: {}" , cronExpression);
      log.info("cron: {} and id: {}" , cronJob.getTaskName(), cronJob.getId());
    }
}
