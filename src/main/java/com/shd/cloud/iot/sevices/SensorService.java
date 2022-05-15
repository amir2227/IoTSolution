package com.shd.cloud.iot.sevices;

import java.util.Date;
import java.util.List;

import com.shd.cloud.iot.exception.BadRequestException;
import com.shd.cloud.iot.exception.DuplicatException;
import com.shd.cloud.iot.exception.NotFoundException;
import com.shd.cloud.iot.models.Location;
import com.shd.cloud.iot.models.Sensor;
import com.shd.cloud.iot.models.SensorHistory;
import com.shd.cloud.iot.models.User;
import com.shd.cloud.iot.payload.request.EditSensorRequest;
import com.shd.cloud.iot.payload.request.SearchRequest;
import com.shd.cloud.iot.payload.request.SensorHistoryRequest;
import com.shd.cloud.iot.payload.request.SensorRequest;
import com.shd.cloud.iot.repositorys.SensorHistoryRepository;
import com.shd.cloud.iot.repositorys.SensorRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SensorService {

    @Autowired
    private SensorRepository sensorRepository;

    @Autowired
    private LocationService locationService;

    @Autowired
    private UserService userService;

    @Autowired
    private SensorHistoryRepository sensorHistoryRepository;

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

    public List<Sensor> getAllByUser(Long user_id) {
        userService.get(user_id);
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

    public SensorHistory saveSensorHistory(Long sid, SensorHistoryRequest shr) {
        Sensor sensor = this.get(sid);
        if (!sensor.getUser().getToken().equals(shr.getToken())) {
            throw new BadRequestException("not valid request");
        }
        SensorHistory sensorHistory = new SensorHistory(shr.getData(), new Date().getTime(), sensor);
        return sensorHistoryRepository.save(sensorHistory);
    }
}
