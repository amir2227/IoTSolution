package com.shd.cloud.iot.sevices;

import com.shd.cloud.iot.exception.NotFoundException;
import com.shd.cloud.iot.models.Location;
import com.shd.cloud.iot.payload.request.LocationRequest;
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
            Location parent = this.get(dto.getParent_id());

            location.setParent(parent);
        }

        return ResponseEntity.ok(locationRepository.save(location));
    }

    public Location get(Long id) {
        return locationRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Location Not Found with id: " + id));
    }

}
