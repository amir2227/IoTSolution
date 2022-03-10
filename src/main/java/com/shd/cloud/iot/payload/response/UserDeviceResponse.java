package com.shd.cloud.iot.payload.response;

import java.util.List;

import com.shd.cloud.iot.models.Operator;
import com.shd.cloud.iot.models.Sensor;

public class UserDeviceResponse {

    private List<Operator> operators;
    private List<Sensor> sensors;

    public UserDeviceResponse(List<Operator> operators, List<Sensor> sensors) {
        this.operators = operators;
        this.sensors = sensors;
    }

    public List<Operator> getOperators() {
        return operators;
    }

    public void setOperators(List<Operator> operators) {
        this.operators = operators;
    }

    public List<Sensor> getSensors() {
        return sensors;
    }

    public void setSensors(List<Sensor> sensors) {
        this.sensors = sensors;
    }

    @Override
    public String toString() {
        return "UserDeviceResponse [operators=" + operators + ", sensors=" + sensors + "]";
    }

}
