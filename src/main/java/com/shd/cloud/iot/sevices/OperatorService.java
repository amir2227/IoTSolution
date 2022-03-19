package com.shd.cloud.iot.sevices;

import java.util.Date;
import java.util.List;

import com.shd.cloud.iot.exception.DuplicatException;
import com.shd.cloud.iot.exception.NotFoundException;
import com.shd.cloud.iot.models.Location;
import com.shd.cloud.iot.models.Operator;
import com.shd.cloud.iot.models.OperatorHistory;
import com.shd.cloud.iot.models.User;
import com.shd.cloud.iot.payload.request.EditOperator;
import com.shd.cloud.iot.payload.request.OperatorRequest;
import com.shd.cloud.iot.payload.request.SearchRequest;
import com.shd.cloud.iot.repositorys.OperatorHistoryRepository;
import com.shd.cloud.iot.repositorys.OperatorRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OperatorService {

    @Autowired
    private OperatorRepository operatorRepository;

    @Autowired
    private OperatorHistoryRepository operatorHRepo;

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
            Location loc = locationService.get(dto.getLocation_id());
            operator.setLocation(loc);
        }
        operator.setUser(user);
        operator = operatorRepository.save(operator);
        Date date = new Date();
        OperatorHistory oh = new OperatorHistory(operator.getState(), date.getTime(), operator);
        operatorHRepo.save(oh);
        return operator;

    }

    public Operator Edit(EditOperator dto, Long id, Long user_id) {
        Operator operator = this.getOneByUser(id, user_id);
        if (dto.getState() != null) {
            operator.setState(dto.getState());
            Date date = new Date();
            OperatorHistory oh = new OperatorHistory(operator.getState(), date.getTime(), operator);
            operatorHRepo.save(oh);
        }
        if (dto.getName() != null) {
            operator.setName(dto.getName());
        }
        if (dto.getType() != null) {
            operator.setType(dto.getType());
        }
        if (dto.getLocation_id() != null) {
            if (dto.getLocation_id().equals(operator.getLocation().getId())) {
                Location location = locationService.get(dto.getLocation_id());
                operator.setLocation(location);
            }
        }

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

    public Operator getByToken(Long id, String token) {
        Operator op = this.get(id);
        if (!op.getUser().getToken().equals(token)) {
            throw new NotFoundException("Operator Not Found. Invalid token");
        }
        return op;
    }

    public List<OperatorHistory> searchHistories(Long id, SearchRequest searchRequest) {
        this.get(id);
        if (searchRequest.getStartDate() != null) {
            if (searchRequest.getEndDate() != null) {
                return operatorHRepo.findAllWithBetweenDate(searchRequest.getStartDate(), searchRequest.getEndDate());
            }
            return operatorHRepo.findAllWithStartDate(searchRequest.getStartDate());
        } else if (searchRequest.getEndDate() != null) {
            return operatorHRepo.findAllWithEndDate(searchRequest.getEndDate());
        } else {
            return operatorHRepo.findByOperator_id(id);
        }

    }

    public String delete(Long id, Long user_id) {
        Operator operator = this.getOneByUser(id, user_id);
        try {
            operatorRepository.delete(operator);
            return "operator with id " + id + " successfully deleted";
        } catch (Exception e) {
            return e.getMessage();
        }

    }

}
