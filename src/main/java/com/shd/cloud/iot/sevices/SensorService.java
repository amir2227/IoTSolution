package com.shd.cloud.iot.sevices;

import java.util.List;

import com.shd.cloud.iot.exception.DuplicatException;
import com.shd.cloud.iot.exception.NotFoundException;
import com.shd.cloud.iot.models.Location;
import com.shd.cloud.iot.models.Sensor;
import com.shd.cloud.iot.models.User;
import com.shd.cloud.iot.payload.request.SensorRequest;
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

    public Sensor create(SensorRequest dto, Long user_id) {
        User user = userService.get(user_id);
        if (sensorRepository.existsByNameAndUser_id(dto.getName(), user.getId())) {
            throw new DuplicatException(dto.getName() + " with user id " + user.getId());
        }
        Sensor sensor = new Sensor(dto.getName(), dto.getName());
        if (dto.getLocation_id() != null) {
            Location loc = locationService.get(dto.getLocation_id())
                    .orElseThrow(() -> new NotFoundException("Location Not Found with id " + dto.getLocation_id()));
            sensor.setLocation(loc);
        }
        sensor.setUser(user);

        return sensorRepository.save(sensor);
    }

    public Sensor get(Long id) {
        return sensorRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Sensor Not Found with id " + id));
    }

    public Sensor getOneByUser(Long id, Long user_id) {
        userService.get(user_id);
        return sensorRepository.findByIdAndUser_id(id, user_id)
                .orElseThrow(() -> new NotFoundException("Sensor Not Found or Not this user operator"));
    }

    public List<Sensor> getAllByUser(Long user_id) {
        userService.get(user_id);
        return sensorRepository.findByUser_id(user_id);
    }

    public void delete(Long id, Long user_id) {
        Sensor sensor = this.getOneByUser(id, user_id);
        sensorRepository.delete(sensor);

    }
}
