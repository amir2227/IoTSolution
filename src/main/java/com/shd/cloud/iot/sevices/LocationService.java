package com.shd.cloud.iot.sevices;

import java.util.List;

import com.shd.cloud.iot.exception.NotFoundException;
import com.shd.cloud.iot.models.Location;
import com.shd.cloud.iot.models.User;
import com.shd.cloud.iot.payload.request.EditLocationRequest;
import com.shd.cloud.iot.payload.request.LocationRequest;
import com.shd.cloud.iot.repositorys.LocationRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LocationService {
    @Autowired
    private LocationRepository locationRepository;

    @Autowired
    private UserService userService;

    public Location create(LocationRequest dto, Long user_id) {
        User user = userService.get(user_id);
        Location location = new Location(dto.getName(), dto.getType(), user);
        if (dto.getParent_id() != null) {
            Location parent = this.get(dto.getParent_id());
            location.setParent(parent);
        }
        return locationRepository.save(location);
    }

    public Location get(Long id) {
        return locationRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Location Not Found with id: " + id));
    }

    public List<Location> search(Long user_id) {
        return locationRepository.findByUser_id(user_id);
    }

    public Location edit(Long id, EditLocationRequest request) {
        Location location = this.get(id);
        if (request.getName() != null) {
            location.setName(request.getName());
        }
        if (request.getType() != null) {
            location.setType(location.getType());
        }
        return locationRepository.save(location);
    }

    public String delete(Long id) {
        Location location = this.get(id);
        try {
            locationRepository.delete(location);
            return "location with id " + id + " successfully deleted";
        } catch (Exception e) {
            return e.getMessage();
        }
    }

}
