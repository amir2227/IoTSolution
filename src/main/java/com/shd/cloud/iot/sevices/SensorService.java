package com.shd.cloud.iot.sevices;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import com.shd.cloud.iot.enums.DeviceStatus;
import com.shd.cloud.iot.exception.BadRequestException;
import com.shd.cloud.iot.exception.DuplicatException;
import com.shd.cloud.iot.exception.NotFoundException;
import com.shd.cloud.iot.models.*;
import com.shd.cloud.iot.payload.request.EditSensorRequest;
import com.shd.cloud.iot.payload.request.SearchRequest;
import com.shd.cloud.iot.payload.request.SensorRequest;
import com.shd.cloud.iot.repositorys.ScenarioSensorsRepository;
import com.shd.cloud.iot.repositorys.SensorHistoryRepository;
import com.shd.cloud.iot.repositorys.SensorRepository;

import com.shd.cloud.iot.utils.Utils;
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
        sensor.setDeviceId(UUID.randomUUID());
        sensorHistoryRepository.save(new SensorHistory("0",sensor.getDeviceId()));
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

    public Sensor getByDeviceId(UUID deviceId){
        return sensorRepository.findByDeviceId(deviceId)
                .orElseThrow(()-> new RuntimeException("sensor not found"));
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

    public List<SensorHistory> searchHistory(UUID deviceId, SearchRequest sRequest) {
        if(sRequest != null){
            if (sRequest.getStartDate() != null) {
                if (sRequest.getEndDate() != null) {
                    return sensorHistoryRepository
                            .findAllWithBetweenDate(
                                LocalDateTime.ofInstant(Instant.ofEpochMilli(sRequest.getStartDate()), ZoneId.systemDefault()),
                                LocalDateTime.ofInstant(Instant.ofEpochMilli(sRequest.getEndDate()), ZoneId.systemDefault()));
                }
                return sensorHistoryRepository
                        .findAllWithStartDate(LocalDateTime.ofInstant(Instant.ofEpochMilli(sRequest.getStartDate()), ZoneId.systemDefault()));
            } else if (sRequest.getEndDate() != null) {
                return sensorHistoryRepository
                        .findAllWithEndDate(LocalDateTime.ofInstant(Instant.ofEpochMilli(sRequest.getEndDate()), ZoneId.systemDefault()));
            }else return sensorHistoryRepository.findByDeviceId(deviceId);
        } else {
                return sensorHistoryRepository.findByDeviceId(deviceId);
            }
    }
    public void saveSensorHistory(UUID deviceId, String token, String data) {
        Sensor sensor = this.getByDeviceId(deviceId);
        if (!sensor.getUser().getToken().equals(token)) {
           throw new BadRequestException("access denied");
        }
        log.info("in save history");
        List<ScenarioSensors> scenarioSensors = scenarioSensorsRepository.findBySensor_id(sensor.getId());
        if (scenarioSensors != null && scenarioSensors.size() > 0) {
            for (ScenarioSensors s_sensor : scenarioSensors) {
                if (checkModality(data, s_sensor)) {
                    manageScenario(s_sensor);
                }

            }
        }
        SensorHistory sensorHistory = new SensorHistory(data, deviceId);
        sensorHistoryRepository.save(sensorHistory);
    }
    public boolean existHistory(UUID deviceId){
        return sensorHistoryRepository.existsByDeviceId(deviceId);
    }
    public SensorHistory findLastSensorHistory(UUID deviceId){
    return sensorHistoryRepository.findFirstByDeviceIdOrderByLastUpdateDesc(deviceId)
            .orElseThrow(() -> new RuntimeException("sensor history "+deviceId+" not found"));
    }
    // health check of sensor
    public void sensorHealthCheck(UUID deviceId, String token){
            Sensor sensor = getByDeviceId(deviceId);
            if (sensor.getUser().getToken().equals(token)) {
                long difference = Utils.calculateDiffTime(System.currentTimeMillis(), sensor.getLastHealthCheckDate());
                if (difference < 31000) {
                    sensor.setStatus(DeviceStatus.GREEN);
                } else if (difference < 61000) {
                    sensor.setStatus(DeviceStatus.YELLOW);
                } else {
                    sensor.setStatus(DeviceStatus.RED);
                }
                sensor.setLastHealthCheckDate(LocalDateTime.now());
            }
            sensorRepository.save(sensor);

    }
    public void changeLastUpdateHistory(String id){
        SensorHistory sensorHistory = sensorHistoryRepository.findById(id)
                .orElseThrow(()-> new RuntimeException("sensor with id "+ id + " not found"));

        Sensor sensor = this.getByDeviceId(sensorHistory.getDeviceId());
        // if next sensor data is less than 31 second sensor status change to green
        if(Utils.calculateDiffTime(System.currentTimeMillis(),sensor.getLastHealthCheckDate())< 31000){
            sensor.setStatus(DeviceStatus.GREEN);
        }else {
            sensor.setStatus(DeviceStatus.YELLOW);
        }
        sensorRepository.save(sensor);
        sensorHistory.setLastUpdate(LocalDateTime.now());
        sensorHistoryRepository.save(sensorHistory);
    }
    public void intervalHealthCheck(){
        List<Sensor> sensors = sensorRepository.findByLastHealthCheckDateBefore(LocalDateTime.now().minusSeconds(31));
        if (!sensors.isEmpty()){
            for (Sensor sensor : sensors){
                sensor.setStatus(DeviceStatus.RED);
                sensorRepository.save(sensor);
            }
        }
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
        List<ScenarioSensors> scenarioSensors = scenario.getEffectiveSensors();
        boolean flag = true;
        if (scenarioSensors.size() > 0) {
            for (ScenarioSensors scenarioSensor : scenarioSensors) {
                if (Objects.equals(scenarioSensor.getId(), s_sensor.getId()))
                    continue;
                List<SensorHistory> sensorHistories = sensorHistoryRepository
                        .findByDeviceId(scenarioSensor.getSensor().getDeviceId());
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
                List<ScenarioOperators> scenarioOperators = scenario.getTargetOperators();
                for (ScenarioOperators s_operator : scenarioOperators) {
                    operatorService.changeStateByApi(s_operator.getOperator().getId(), s_operator.getOperator_state());
                }
            }
        }
    }
}
