package com.shd.cloud.iot.sevices;

import java.util.Optional;

import com.shd.cloud.iot.dtos.OperatorDto;
import com.shd.cloud.iot.dtos.payload.response.MessageResponse;
import com.shd.cloud.iot.exception.DuplicatException;
import com.shd.cloud.iot.exception.NotFoundException;
import com.shd.cloud.iot.models.Location;
import com.shd.cloud.iot.models.Operator;
import com.shd.cloud.iot.models.User;
import com.shd.cloud.iot.repositorys.OperatorRepository;

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

    public Operator create(OperatorDto dto, String username) {
        User user = userService.getByUsername(username);
        if (operatorRepository.existsByNameAndUser_id(dto.getName(), user.getId())) {
            throw new DuplicatException(dto.getName() + " with user id " + user.getId());
        }
        Operator operator = new Operator(dto.getName(), dto.getState(), dto.getType());
        if (dto.getLocation_id() != null) {
            Location loc = locationService.get(dto.getLocation_id())
                    .orElseThrow(() -> new NotFoundException("location Not Found with id " + dto.getLocation_id()));
            operator.setLocation(loc);
        }
        operator.setUser(user);

        return operatorRepository.save(operator);
    }
}
