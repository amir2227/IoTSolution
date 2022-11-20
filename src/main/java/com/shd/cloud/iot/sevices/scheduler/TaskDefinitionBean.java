package com.shd.cloud.iot.sevices.scheduler;

import com.shd.cloud.iot.models.Operator;
import com.shd.cloud.iot.payload.request.CronRequest;
import com.shd.cloud.iot.sevices.OperatorService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TaskDefinitionBean implements Runnable {

    private CronRequest cronRequest;

    private final OperatorService operatorService;

    @Override
    public void run() {
        Operator op = operatorService.get(cronRequest.getOperator_id());
        System.out.println("Running action: " + cronRequest.getCron());
        System.out.println("With state: " + cronRequest.getState());
        System.out.println("With operator: " + op.getName());
    }

    public CronRequest getCronRequest() {
        return cronRequest;
    }

    public void setCronRequest(CronRequest taskDefinition) {
        this.cronRequest = taskDefinition;
    }
}