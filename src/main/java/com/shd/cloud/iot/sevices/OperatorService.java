package com.shd.cloud.iot.sevices;

import java.util.List;

import com.shd.cloud.iot.exception.DuplicatException;
import com.shd.cloud.iot.exception.NotFoundException;
import com.shd.cloud.iot.models.Location;
import com.shd.cloud.iot.models.Operator;
import com.shd.cloud.iot.models.User;
import com.shd.cloud.iot.payload.request.OperatorRequest;
import com.shd.cloud.iot.repositorys.OperatorRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OperatorService {

    @Autowired
    private OperatorRepository operatorRepository;

    @Autowired
    private LocationService locationService;

    @Autowired
    private UserService userService;

    public Operator create(OperatorRequest dto, Long user_id) {
        User user = userService.get(user_id);
        System.out.println(user);
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

    public Operator get(Long id) {
        return operatorRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Operator Not Found with id " + id));
    }

    public List<Operator> getAllByUser(Long user_id) {
        // userService.get(user_id);
        List<Operator> l = operatorRepository.findByUser_id(user_id);
        return l;
    }

    public Operator getOneByUser(Long id, Long user_id) {
        // userService.get(user_id);
        return operatorRepository.findByIdAndUser_id(id, user_id)
                .orElseThrow(() -> new NotFoundException("Operator Not Found or Not this user operator"));
    }

    public void delete(Long id, Long user_id) {
        Operator operator = this.getOneByUser(id, user_id);
        operatorRepository.delete(operator);

    }

}
