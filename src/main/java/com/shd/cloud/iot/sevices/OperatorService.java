package com.shd.cloud.iot.sevices;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.UUID;

import javax.annotation.Resource;

import com.shd.cloud.iot.enums.DeviceStatus;
import com.shd.cloud.iot.exception.BadRequestException;
import com.shd.cloud.iot.exception.DuplicatException;
import com.shd.cloud.iot.exception.NotFoundException;
import com.shd.cloud.iot.models.Location;
import com.shd.cloud.iot.models.Operator;
import com.shd.cloud.iot.models.OperatorHistory;
import com.shd.cloud.iot.models.User;
import com.shd.cloud.iot.config.mqtt.config.MqttGateway;
import com.shd.cloud.iot.payload.request.EditOperator;
import com.shd.cloud.iot.payload.request.OperatorRequest;
import com.shd.cloud.iot.payload.request.SearchRequest;
import com.shd.cloud.iot.repositorys.OperatorHistoryRepository;
import com.shd.cloud.iot.repositorys.OperatorRepository;

import com.shd.cloud.iot.utils.Utils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OperatorService {
    private final OperatorRepository operatorRepository;
    private final OperatorHistoryRepository operatorHRepo;
    private final LocationService locationService;
    private final UserService userService;
    @Resource
    private MqttGateway mqttGateway;


    public Operator create(OperatorRequest dto, Long user_id) {
        User user = userService.get(user_id);
        if (operatorRepository.existsByNameAndUser_id(dto.getName(), user.getId())) {
            throw new DuplicatException(dto.getName() + " with user id " + user.getId());
        }
        Operator operator = new Operator(dto.getName(), false, dto.getType());
        if (dto.getLocation_id() != null) {
            Location loc = locationService.get(dto.getLocation_id());
            operator.setLocation(loc);
        }
        operator.setUser(user);
        operator.setStatus(DeviceStatus.RED);
        operator = operatorRepository.save(operator);
        OperatorHistory oh = new OperatorHistory(operator.getState(), operator,user);
        operatorHRepo.save(oh);
        return operator;
    }
    public Operator Edit(EditOperator dto, Long id, Long user_id) {
        Operator operator = this.getOneByUser(id, user_id);
        if (dto.getState() != null) {
            String topic = "device/" + operator.getUser().getToken() + "/" + operator.getDeviceId() +"/operator";
            try {
                if (dto.getState()) {
                    mqttGateway.sendToMqtt(topic, 1, "1");
                }
                else {
                    mqttGateway.sendToMqtt(topic, 1, "0");
                }
            } catch (Exception e) {
                System.out.println("mqtt exception: " + e.getMessage());
                e.printStackTrace();
            }
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
    public void changeStateByMqtt(UUID deviceId, boolean state){
        Operator operator = this.getByDeviceId(deviceId);
        if(operator.getState() != state){
            operator.setState(state);
            OperatorHistory oh = new OperatorHistory(operator.getState(), operator,operator.getUser());
            operatorRepository.save(operator);
            operatorHRepo.save(oh);
        }
    }
    public void changeStateByApi(Long operatorId, boolean operatorState){
        Operator operator = this.get(operatorId);
        String topic = "device/" + operator.getUser().getToken() + "/" + operator.getDeviceId() + "/operator";
        try {
            if (operatorState) {
                mqttGateway.sendToMqtt(topic, 1, "1");
            }else {
                mqttGateway.sendToMqtt(topic, 1, "0");
            }
        } catch (Exception e) {
            System.out.println("mqtt exception: " + e.getMessage());
            e.printStackTrace();
        }
    }
    public void operatorHealthCheck(UUID deviceId, String token, String payload){
        Operator operator = this.getByDeviceId(deviceId);
        if(operator.getUser().getToken().equals(token)){
            long difference = Utils.calculateDiffTime(System.currentTimeMillis() , operator.getLastHealthCheckDate());
            if(difference < 31000){
                operator.setStatus(DeviceStatus.GREEN);
            }else if (difference < 61000){
                operator.setStatus(DeviceStatus.YELLOW);
            }
            else {
                operator.setStatus(DeviceStatus.RED);
            }
            operator.setLastHealthCheckDate(LocalDateTime.now());
            if(payload.equals("1")){
                operator.setState(true);
            }
            else if (payload.equals("0")) {
                operator.setState(false);
            }
            operatorRepository.save(operator);
        }
    }
    public void intervalHealthCheck(){
        List<Operator> operators = operatorRepository.findByLastHealthCheckDateBefore(LocalDateTime.now().minusSeconds(31));
        if(!operators.isEmpty()) {
            for (Operator operator : operators) {
                    operator.setStatus(DeviceStatus.RED);
                    operatorRepository.save(operator);
            }
        }
    }
    public boolean changeStateBySharedUser(Long oid, Long uid, boolean state) {
        User user = userService.get(uid);
        Operator operator = this.get(oid);

        if (!operator.getShared().getTargetUsers().contains(user))
            throw new BadRequestException("access denied");

        String topic = "device/" + operator.getUser().getToken() + "/" + operator.getDeviceId();
        try {
            mqttGateway.sendToMqtt(topic, 1, String.valueOf(state));
            operator.setState(state);
            operatorRepository.save(operator);
            operatorHRepo.save(new OperatorHistory(state, operator,user));
        } catch (Exception e) {
            throw new BadRequestException(e.getMessage());

        }
        return true;
    }

    public Operator get(Long id) {
        return operatorRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Operator Not Found with id " + id));
    }
    public Operator getByDeviceId(UUID dviceId){
        return operatorRepository.findByDeviceId(dviceId)
                .orElseThrow(() -> new NotFoundException("not found"));
    }

    public List<Operator> getAllByUser(Long user_id, String key) {
        // userService.get(user_id);
        if (key != null) {
            return operatorRepository.search(key, user_id);
        } else {
            return operatorRepository.findByUser_id(user_id);
        }
    }

    public Operator getOneByUser(Long id, Long user_id) {
        return operatorRepository.findByIdAndUser_id(id, user_id)
                .orElseThrow(() -> new NotFoundException("Operator Not Found or Not this user operator"));
    }
    public List<OperatorHistory> searchHistories(Long id, SearchRequest searchRequest) {
        this.get(id);
        if (searchRequest.getStartDate() != null) {
            if (searchRequest.getEndDate() != null) {
                return operatorHRepo
                        .findAllWithBetweenDate(
                                LocalDateTime.ofInstant(Instant.ofEpochMilli(searchRequest.getStartDate()), ZoneId.systemDefault()),
                                LocalDateTime.ofInstant(Instant.ofEpochMilli(searchRequest.getEndDate()), ZoneId.systemDefault()));
            }
            return operatorHRepo
                    .findAllWithStartDate(LocalDateTime.ofInstant(Instant.ofEpochMilli(searchRequest.getStartDate()), ZoneId.systemDefault()));
        } else if (searchRequest.getEndDate() != null) {
            return operatorHRepo
                    .findAllWithEndDate(LocalDateTime.ofInstant(Instant.ofEpochMilli(searchRequest.getEndDate()), ZoneId.systemDefault()));
        } else {
            return operatorHRepo.findByOperator_id(id);
        }

    }

    public String delete(Long id, Long user_id) {
        Operator operator = this.getOneByUser(id, user_id);
        // if (operator.getHistories().size() > 0) {
        // throw new BadRequestException("this operator have some history. cannot be
        // deleted!");
        // }
        if (operator.getScenario_Operators().size() > 0) {
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
