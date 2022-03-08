package com.shd.cloud.iot.sevices;

import java.util.Optional;

import com.shd.cloud.iot.dtos.OperatorDto;
import com.shd.cloud.iot.dtos.payload.response.MessageResponse;
import com.shd.cloud.iot.models.Location;
import com.shd.cloud.iot.models.Operator;
import com.shd.cloud.iot.models.User;
import com.shd.cloud.iot.repositorys.OperatorRepository;
import com.shd.cloud.iot.repositorys.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class OperatorService {

    @Autowired
    private OperatorRepository operatorRepository;

    @Autowired
    private LocationService locationService;

    @Autowired
    private UserService userService;

    public ResponseEntity<?> create(OperatorDto dto) {
        Optional<User> user = userService.get(dto.getUser_id());
        if (!user.isPresent()) {
            return ResponseEntity.badRequest()
                    .body(new MessageResponse("user with id " + dto.getUser_id() + " Not Found!"));
        }

        Operator operator = new Operator(dto.getName(), false, dto.getType());
        if (dto.getLocation_id() != null) {
            Optional<Location> loc = locationService.get(dto.getLocation_id());
            if (!loc.isPresent()) {
                return ResponseEntity.badRequest()
                        .body(new MessageResponse("location with id " + dto.getLocation_id() + " Not Found!"));
            }
            operator.setLocation(loc.get());
        }
        operator.setUser(user.get());

        return ResponseEntity.ok(operatorRepository.save(operator));
    }
}
