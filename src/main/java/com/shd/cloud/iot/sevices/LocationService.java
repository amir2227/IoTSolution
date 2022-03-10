package com.shd.cloud.iot.sevices;

import java.util.Optional;

import com.shd.cloud.iot.models.Location;
import com.shd.cloud.iot.payload.request.LocationRequest;
import com.shd.cloud.iot.payload.response.MessageResponse;
import com.shd.cloud.iot.repositorys.LocationRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class LocationService {
    @Autowired
    private LocationRepository locationRepository;

    public ResponseEntity<?> create(LocationRequest dto) {
        Location location = new Location(dto.getName(), dto.getType());
        if (dto.getParent_id() != null) {
            Optional<Location> parent = this.get(dto.getParent_id());
            if (!parent.isPresent()) {
                return ResponseEntity.badRequest()
                        .body(new MessageResponse("location with id " + dto.getParent_id() + " Not Found!"));
            }
            location.setParent(parent.get());
        }

        return ResponseEntity.ok(locationRepository.save(location));
    }

    public Optional<Location> get(Long id) {
        return locationRepository.findById(id);
    }

}
