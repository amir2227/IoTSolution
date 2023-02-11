package com.shd.cloud.iot.sevices;

import com.shd.cloud.iot.models.CronJob;
import com.shd.cloud.iot.repositorys.CronJobsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.support.CronExpression;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;


@Slf4j
@Component
@RequiredArgsConstructor
public class CronJobScheduler {
    private final CronJobsRepository cronJobsRepository;
    private final OperatorService operatorService;
    private final SensorService sensorService;
    private final List<CronJob> catchedCrons = new ArrayList<>();

    @Scheduled(fixedRate = 30000) // run every 30 second
    public void ScheduleCronJobs() {
        sensorService.intervalHealthCheck();
        operatorService.intervalHealthCheck();
        boolean flag = true;
        List<CronJob> cronJobs = cronJobsRepository.findAll();
        if (!cronJobs.isEmpty()) {
            if (catchedCrons.isEmpty()) {
                catchedCrons.addAll(cronJobs);
                for (CronJob cronJob : cronJobs) {
                    scheduleCronJob(cronJob);
                }
                flag = false;
            }
            if (flag) {
                List<CronJob> difference = cronJobs.stream()
                        .filter(job2 -> !catchedCrons.contains(job2))
                        .toList();
                if (!difference.isEmpty()) {
                    catchedCrons.addAll(difference);
                    for (CronJob cronJob : difference) {
                        scheduleCronJob(cronJob);
                    }
                }
            }
        }
    }
    private void scheduleCronJob(CronJob cronJob) {
        String cronExpression = cronJob.getExpression();
        try {
            CronExpression.isValidExpression(cronExpression);
        }catch (Exception e){
            log.error(e.getMessage());
        }
        new Scheduler(cronJob);
        // Schedule the task using the cron expression
        // The task name is passed as a parameter to the method

    }
}
