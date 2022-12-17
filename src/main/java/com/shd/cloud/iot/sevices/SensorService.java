package com.shd.cloud.iot.sevices;

import java.util.Date;
import java.util.List;
import java.util.Objects;

import com.shd.cloud.iot.enums.DeviceStatus;
import com.shd.cloud.iot.exception.BadRequestException;
import com.shd.cloud.iot.exception.DuplicatException;
import com.shd.cloud.iot.exception.NotFoundException;
import com.shd.cloud.iot.models.Location;
import com.shd.cloud.iot.models.Scenario;
import com.shd.cloud.iot.models.ScenarioOperators;
import com.shd.cloud.iot.models.ScenarioSensors;
import com.shd.cloud.iot.models.Sensor;
import com.shd.cloud.iot.models.SensorHistory;
import com.shd.cloud.iot.models.User;
import com.shd.cloud.iot.payload.request.EditSensorRequest;
import com.shd.cloud.iot.payload.request.SearchRequest;
import com.shd.cloud.iot.payload.request.SensorHistoryRequest;
import com.shd.cloud.iot.payload.request.SensorRequest;
import com.shd.cloud.iot.repositorys.ScenarioSensorsRepository;
import com.shd.cloud.iot.repositorys.SensorHistoryRepository;
import com.shd.cloud.iot.repositorys.SensorRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class SensorService {
    private final SensorRepository sensorRepository;
    private final LocationService locationService;
    private final UserService userService;
    private final SensorHistoryRepository sensorHistoryRepository;
    private final ScenarioSensorsRepository scenarioSensorsRepository;
    private final OperatorService operatorService;


    public Sensor create(SensorRequest dto, Long user_id) {
        User user = userService.get(user_id);
        if (sensorRepository.existsByNameAndUser_id(dto.getName(), user.getId())) {
            throw new DuplicatException(dto.getName() + " with user id " + user.getId());
        }
        Sensor sensor = new Sensor(dto.getName(), dto.getType());
        if (dto.getLocation_id() != null) {
            Location loc = locationService.get(dto.getLocation_id());
            sensor.setLocation(loc);
        }
        sensor.setUser(user);
        sensor.setStatus(DeviceStatus.RED);
        return sensorRepository.save(sensor);
    }

    public Sensor get(Long id) {
        return sensorRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Sensor Not Found with id: " + id));
    }

    public Sensor getOneByUser(Long id, Long user_id) {
        userService.get(user_id);
        return sensorRepository.findByIdAndUser_id(id, user_id)
                .orElseThrow(() -> new NotFoundException("Sensor Not Found with id: " + id));
    }

    public List<Sensor> getAllByUser(Long user_id, String key) {
        if (key != null) {
            return sensorRepository.search(key, user_id);
        } else
            return sensorRepository.findByUser_id(user_id);
    }

    public Sensor Edit(EditSensorRequest editSensorRequest, Long id, Long user_id) {
        Sensor sensor = this.getOneByUser(id, user_id);
        if (editSensorRequest.getName() != null) {
            sensor.setName(editSensorRequest.getName());
        }
        if (editSensorRequest.getType() != null) {
            sensor.setType(editSensorRequest.getType());
        }
        if (editSensorRequest.getLocation_id() != null) {
            // if (!editSensorRequest.getLocation_id().equals(sensor.getLocation().getId())) {
            Location location = locationService.get(editSensorRequest.getLocation_id());
            sensor.setLocation(location);
            // }
        }
        return sensorRepository.save(sensor);

    }

    public String delete(Long id, Long user_id) {
        Sensor sensor = this.getOneByUser(id, user_id);
        try {
            sensorRepository.delete(sensor);
            return "sensor with id " + id + " successfully deleted";
        } catch (Exception e) {
            throw new BadRequestException("sensor with id " + id + " can not be deleted! ");
        }

    }

    public List<SensorHistory> searchHistory(Long id, SearchRequest sRequest) {
            if (sRequest.getStartDate() != null) {
                if (sRequest.getEndDate() != null) {
                    return sensorHistoryRepository.findAllWithBetweenDate(new Date(sRequest.getStartDate()),
                            new Date(sRequest.getEndDate()));
                }
                return sensorHistoryRepository.findAllWithStartDate(new Date(sRequest.getStartDate()));
            } else if (sRequest.getEndDate() != null) {
                return sensorHistoryRepository.findAllWithEndDate(new Date(sRequest.getEndDate()));
            } else {
                return sensorHistoryRepository.findBySensorId(id);
            }
    }
    public List<SensorHistory> searchHistory(Long id){
        log.info("in search all history sensor");
        return sensorHistoryRepository.findBySensorId(id);
    }


    public SensorHistory saveSensorHistory(Long sid, SensorHistoryRequest shr) {
        Sensor sensor = this.get(sid);
        if (!sensor.getUser().getToken().equals(shr.getToken())) {
            throw new BadRequestException("not valid request");
        }
        log.info("in save history");
        List<ScenarioSensors> scenarioSensors = scenarioSensorsRepository.findBySensor_id(sensor.getId());
        if (scenarioSensors != null && scenarioSensors.size() > 0) {
            for (ScenarioSensors s_sensor : scenarioSensors) {
                if (checkModality(shr.getData(), s_sensor)) {
                    manageScenario(s_sensor);
                }

            }
        }
        SensorHistory sensorHistory = new SensorHistory(shr.getData(), sensor.getId());
        sensor.setStatus(DeviceStatus.YELLOW);
        sensorRepository.save(sensor);
        return sensorHistoryRepository.save(sensorHistory);
    }
    // health check of sensor list
    public void sensorHealthCheck(Long userId){
        List<Sensor> sensors = sensorRepository.findByUser_id(userId);
        if(!sensors.isEmpty()) {
            for (Sensor sensor : sensors) {
                Date lastUpdate = sensorHistoryRepository.findFirstBySensorIdOrderByLastUpdateDesc(sensor.getId()).getLastUpdate();
                if (new Date().getTime() - lastUpdate.getTime() > 31000) {
                    sensor.setStatus(DeviceStatus.RED);
                    sensorRepository.save(sensor);
                }
            }
        }
    }
    public void changeLastUpdateHistory(String id){
        SensorHistory sensorHistory = sensorHistoryRepository.findById(id).orElseThrow(()-> new RuntimeException("sensor with id "+ id + " not found"));
        Date now = new Date();
        // if next sensor data is less than 31 second sensor status change to green
        if(now.getTime() - sensorHistory.getLastUpdate().getTime() < 31000){
            Sensor sensor = this.get(sensorHistory.getSensorId());
            sensor.setStatus(DeviceStatus.GREEN);
            sensorRepository.save(sensor);
        }
        sensorHistory.setLastUpdate(now);
        sensorHistoryRepository.save(sensorHistory);
    }
    private boolean checkModality(String data, ScenarioSensors s_sensor) {
        float firstPoint = Float.parseFloat(s_sensor.getPoints().split(",")[0]);
        switch (s_sensor.getModality()) {
            case BETWEEN:
                float secondPoint = Float.parseFloat(s_sensor.getPoints().split(",")[1]);
                if (Float.parseFloat(data) >= firstPoint && Float.parseFloat(data) <= secondPoint) {
                    log.info("data  {}  is between {} and {}" ,data,firstPoint,secondPoint);
                    return true;
                } else {
                    return false;
                }
            case EQUAL:
                if (Float.valueOf(data).equals(firstPoint)) {
                    log.info("data {} is equal to {}",data,firstPoint);
                    return true;
                } else {
                    return false;
                }
            case GREATER:
                if (Float.parseFloat(data) >= firstPoint) {
                    log.info("data {} is greater than {}" ,data,firstPoint);
                    return true;
                } else {
                    return false;
                }
            case SMALLER:
                if (Float.parseFloat(data) <= firstPoint) {
                    log.info("data {} is smaller than {}" ,data, firstPoint);
                    return true;
                } else {
                    return false;
                }
            default:
                return false;

        }
    }

    private void manageScenario(ScenarioSensors s_sensor) {
        Scenario scenario = s_sensor.getScenario();
        List<ScenarioSensors> scenarioSensors = scenario.getEffective_sensors();
        boolean flag = true;
        if (scenarioSensors.size() > 0) {
            for (ScenarioSensors scenarioSensor : scenarioSensors) {
                if (Objects.equals(scenarioSensor.getId(), s_sensor.getId()))
                    continue;
                List<SensorHistory> sensorHistories = sensorHistoryRepository.findBySensorId(scenarioSensor.getSensor().getId());
                int hsize = sensorHistories.size();
                String data = sensorHistories.get(hsize - 1).getData();
                if (!checkModality(data, scenarioSensor)) {
                    // if one sensor scenario return false then this scenario is incomplete
                    // we change state of scenario operator only when all scenario sensors return
                    // true
                    flag = false;
                    break;
                }

            }
            if (flag) {
                List<ScenarioOperators> scenarioOperators = scenario.getTarget_operators();
                for (ScenarioOperators s_operator : scenarioOperators) {
                    operatorService.changeState(s_operator.getOperator().getId(), s_operator.getOperator_state());
                }
            }
        }
    }
}
