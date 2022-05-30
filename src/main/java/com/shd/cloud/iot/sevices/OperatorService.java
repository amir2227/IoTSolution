package com.shd.cloud.iot.sevices;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import com.shd.cloud.iot.exception.BadRequestException;
import com.shd.cloud.iot.exception.DuplicatException;
import com.shd.cloud.iot.exception.NotFoundException;
import com.shd.cloud.iot.models.Location;
import com.shd.cloud.iot.models.Operator;
import com.shd.cloud.iot.models.OperatorHistory;
import com.shd.cloud.iot.models.User;
import com.shd.cloud.iot.mqtt.config.MqttGateway;
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

    @Resource
    private MqttGateway mqttGateway;
    // @Autowired
    // private MqttService mqttService;

    /**
     * @param dto
     * @param user_id
     * @return Operator
     */
    public Operator create(OperatorRequest dto, Long user_id) {
        User user = userService.get(user_id);
        System.out.println(user);
        if (operatorRepository.existsByNameAndUser_id(dto.getName(), user.getId())) {
            throw new DuplicatException(dto.getName() + " with user id " + user.getId());
        }
        Operator operator = new Operator(dto.getName(), false, dto.getType());
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

    /**
     * @param dto
     * @param id
     * @param user_id
     * @return Operator
     */
    public Operator Edit(EditOperator dto, Long id, Long user_id) {
        Operator operator = this.getOneByUser(id, user_id);
        if (dto.getState() != null) {
            String topic = "operator/" + operator.getId() + "/" + operator.getUser().getToken();
            try {
                if (dto.getState())
                    mqttGateway.sendToMqtt(topic, 1, "1");
                else
                    mqttGateway.sendToMqtt(topic, 1, "0");
            } catch (Exception e) {
                System.out.println("mqtt exception: " + e.getMessage());
                e.printStackTrace();
            }
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
            Location location = locationService.get(dto.getLocation_id());
            operator.setLocation(location);
            // }
        }

        return operatorRepository.save(operator);
    }

    /**
     * @param oid operator id
     * @param uid shared user id
     * @return boolean
     */
    public boolean changeStatebySharedUser(Long oid, Long uid, boolean state) {
        User user = userService.get(uid);
        Operator operator = this.get(oid);
        boolean res = false;

        if (!operator.getShared().getTarget_users().contains(user))
            throw new BadRequestException("access denied");

        String topic = "operator/" + operator.getUser().getToken();
        try {
            mqttGateway.sendToMqtt(topic, 1, String.valueOf(state));
            operator.setState(state);
            operatorRepository.save(operator);
            res = true;
        } catch (Exception e) {
            res = false;
            throw new BadRequestException(e.getMessage());

        }
        return res;
    }

    /**
     * @param id
     * @return Operator
     */
    public Operator get(Long id) {
        return operatorRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Operator Not Found with id " + id));
    }

    /**
     * @param user_id
     * @param key
     * @return List<Operator>
     */
    public List<Operator> getAllByUser(Long user_id, String key) {
        // userService.get(user_id);
        if (key != null) {
            return operatorRepository.search(key, user_id);
        } else {
            return operatorRepository.findByUser_id(user_id);
        }
    }

    /**
     * @param id
     * @param user_id
     * @return Operator
     */
    public Operator getOneByUser(Long id, Long user_id) {
        // userService.get(user_id);
        return operatorRepository.findByIdAndUser_id(id, user_id)
                .orElseThrow(() -> new NotFoundException("Operator Not Found or Not this user operator"));
    }

    /**
     * @param id
     * @param token
     * @return Operator
     */
    public Operator getByToken(Long id, String token) {
        Operator op = this.get(id);
        if (!op.getUser().getToken().equals(token)) {
            throw new NotFoundException("Operator Not Found. Invalid token");
        }
        return op;
    }

    /**
     * @param id
     * @param searchRequest
     * @return List<OperatorHistory>
     */
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

    /**
     * @param id
     * @param user_id
     * @return String
     */
    public String delete(Long id, Long user_id) {
        Operator operator = this.getOneByUser(id, user_id);
        // if (operator.getHistories().size() > 0) {
        // throw new BadRequestException("this operator have some history. cannot be
        // deleted!");
        // }
        if (operator.getScenario_Operators().size() > 0 || operator.getScenario_Sensors().size() > 0) {
            throw new BadRequestException("this operator have some scenario. cannot be deleted!");
        }
        try {
            operatorRepository.delete(operator);
            return "operator with id " + id + " successfully deleted";
        } catch (Exception e) {
            return e.getMessage();
        }

    }

}
