package com.shd.cloud.iot.sevices;

import java.util.Date;
import java.util.List;
import java.util.Objects;

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
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
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

    public Sensor Edit(EditSensorRequest dto, Long id, Long user_id) {
        Sensor sensor = this.getOneByUser(id, user_id);
        if (dto.getName() != null) {
            sensor.setName(dto.getName());
        }
        if (dto.getType() != null) {
            sensor.setType(dto.getType());
        }
        if (dto.getLocation_id() != null) {
            // if (!dto.getLocation_id().equals(sensor.getLocation().getId())) {
            Location location = locationService.get(dto.getLocation_id());
            sensor.setLocation(location);
            // }
        }
        sensorHistoryRepository.save(new SensorHistory("10", new Date().getTime(), sensor));
        sensorHistoryRepository.save(new SensorHistory("11", new Date().getTime(), sensor));
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

    /**
     * @param id history id
     * @param sRequest include (key, startDate, endDate)
     * @return List<SensorHistory>
     */
    public List<SensorHistory> searchHistory(Long id, SearchRequest sRequest) {
        if (sRequest == null)
            return sensorHistoryRepository.findBySensor_id(id);
        else {
            if (sRequest.getStartDate() != null) {
                if (sRequest.getEndDate() != null) {
                    return sensorHistoryRepository.findAllWithBetweenDate(sRequest.getStartDate(),
                            sRequest.getEndDate());
                }
                return sensorHistoryRepository.findAllWithStartDate(sRequest.getStartDate());
            } else if (sRequest.getEndDate() != null) {
                return sensorHistoryRepository.findAllWithEndDate(sRequest.getEndDate());
            } else {
                return sensorHistoryRepository.findBySensor_id(id);
            }
        }
    }

    /**
     * @param sid sensor id
     * @param shr include (data, token)
     * @return SensorHistory
     */
    public SensorHistory saveSensorHistory(Long sid, SensorHistoryRequest shr) {
        Sensor sensor = this.get(sid);
        if (!sensor.getUser().getToken().equals(shr.getToken())) {
            throw new BadRequestException("not valid request");
        }
        List<ScenarioSensors> scenarioSensors = scenarioSensorsRepository.findBySensor_id(sensor.getId());
        if (scenarioSensors != null && scenarioSensors.size() > 0) {
            for (ScenarioSensors s_sensor : scenarioSensors) {
                if (checkModality(shr.getData(), s_sensor)) {
                    manageScenario(s_sensor);
                }

            }
        }
        SensorHistory sensorHistory = new SensorHistory(shr.getData(), new Date().getTime(), sensor);
        sensorHistoryRepository.saveAndFlush(sensorHistory);
        return sensorHistory;
    }

    private boolean checkModality(String data, ScenarioSensors s_sensor) {
        float firstPoint = Float.parseFloat(s_sensor.getPoints().split(",")[0]);
        switch (s_sensor.getModality()) {
            case BETWEEN:
                float secondPoint = Float.parseFloat(s_sensor.getPoints().split(",")[1]);
                if (Float.parseFloat(data) >= firstPoint && Float.parseFloat(data) <= secondPoint) {
                    System.out.println("data " + data + " is between " + firstPoint + " and " + secondPoint);
                    return true;
                } else {
                    return false;
                }
            case EQUAL:
                if (Float.valueOf(data).equals(firstPoint)) {
                    System.out.println("data " + data + " is equal to " + firstPoint);
                    return true;
                } else {
                    return false;
                }
            case GREATER:
                if (Float.parseFloat(data) >= firstPoint) {
                    System.out.println("data " + data + " is greater than " + firstPoint);
                    return true;
                } else {
                    return false;
                }
            case SMALLER:
                if (Float.parseFloat(data) <= firstPoint) {
                    System.out.println("data " + data + " is smaller than " + firstPoint);
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
        boolean flag = false;
        if (scenarioSensors.size() > 0) {
            for (ScenarioSensors scenarioSensor : scenarioSensors) {
                if (Objects.equals(scenarioSensor.getId(), s_sensor.getId()))
                    continue;
                int hsize = scenarioSensor.getSensor().getHistories().size();
                String data = scenarioSensor.getSensor().getHistories().get(hsize - 1).getData();
                if (!checkModality(data, scenarioSensor)) {
                    // if one sensor scenario return false then this scenario is incomplete
                    // we change state of scenario operator only when all of scenario sensors return
                    // true
                    flag = true;
                    break;
                }

            }
            if (!flag) {
                List<ScenarioOperators> scenarioOperators = scenario.getTarget_operators();
                for (ScenarioOperators s_operator : scenarioOperators) {
                    operatorService.changeState(s_operator.getOperator().getId(), s_operator.getOperator_state());
                }
            }
        }
    }
}
