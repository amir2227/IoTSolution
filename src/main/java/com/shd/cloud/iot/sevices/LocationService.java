package com.shd.cloud.iot.sevices;

import java.util.List;

import com.shd.cloud.iot.exception.NotFoundException;
import com.shd.cloud.iot.models.Location;
import com.shd.cloud.iot.models.User;
import com.shd.cloud.iot.payload.request.EditLocationRequest;
import com.shd.cloud.iot.payload.request.LocationRequest;
import com.shd.cloud.iot.repositorys.LocationRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LocationService {
    private final LocationRepository locationRepository;
    private final UserService userService;

    public Location create(LocationRequest dto, Long user_id) {
        User user = userService.get(user_id);
        Location location = new Location(dto.getName(), dto.getType(), user);
        location.setGeometric(dto.getGeometric());
        return locationRepository.save(location);
    }

    public Location get(Long id) {
        return locationRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Location Not Found with id: " + id));
    }

    public Location getByUser(Long id, Long user_id) {
        return locationRepository.findByIdAndUser_id(id, user_id)
                .orElseThrow(() -> new NotFoundException("Location Not Found with id: " + id));
    }

    public List<Location> search(Long user_id, String key) {
        if (key != null) {
            return locationRepository.search(key, user_id);
        } else {
            return locationRepository.findByUser_id(user_id);
        }
    }

    public Location edit(Long id, Long user_id, EditLocationRequest request) {
        Location location = this.getByUser(id, user_id);
        if (request.getName() != null) {
            location.setName(request.getName());
        }
        if (request.getType() != null) {
            location.setType(location.getType());
        }
        if(request.getGeometric() != null){
            location.setGeometric(request.getGeometric());
        }
        return locationRepository.save(location);
    }

    public String delete(Long id, Long user_id) {
        Location location = this.getByUser(id, user_id);
        try {
            locationRepository.delete(location);
            return "location with id " + id + " successfully deleted";
        } catch (Exception e) {
            return e.getMessage();
        }
    }

}
