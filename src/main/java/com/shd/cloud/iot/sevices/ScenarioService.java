package com.shd.cloud.iot.sevices;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.shd.cloud.iot.exception.BadRequestException;
import com.shd.cloud.iot.models.EModality;
import com.shd.cloud.iot.models.Operator;
import com.shd.cloud.iot.models.Scenario;
import com.shd.cloud.iot.models.Sensor;
import com.shd.cloud.iot.payload.request.ScenarioRequest;
import com.shd.cloud.iot.repositorys.ScenarioRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ScenarioService {

    @Autowired
    private ScenarioRepository scenarioRepository;

    @Autowired
    private OperatorService operatorService;

    @Autowired
    private SensorService sensorService;

    public List<Scenario> create(List<ScenarioRequest> dtoList, Long user_id) {

        List<Scenario> scenarios = new ArrayList<>();
        // test for 1 object to n id
        if (dtoList == null || dtoList.size() < 1) {
            throw new BadRequestException("there is no scenario");
        }
        for (ScenarioRequest dto : dtoList) {
            Scenario scenario = new Scenario();
            if (dto.getOperator_id() != null) {
                Operator operator = operatorService.get(dto.getOperator_id());
                scenario.setOperator(operator);
            }
            if (dto.getSensor_id() != null) {
                Sensor sensor = sensorService.get(dto.getSensor_id());
                scenario.setSensor(sensor);
            }
            if (dto.getOperator_state() != null) {
                scenario.setOperator_state(dto.getOperator_state());
            }
            if (dto.getModality() != null) {
                scenario.setModality(dto.getModality());
                if (dto.getOperator2_id() != null) {
                    if (dto.getSensor_id() != null) {
                        throw new BadRequestException("in this scenario sensor id must be null");
                    }
                    if (dto.getPoints() != null) {
                        throw new BadRequestException("in this scenario points must be null");
                    }
                    if (dto.getModality().equals(EModality.ON) || dto.getModality().equals(EModality.OFF)) {
                        Operator operator = operatorService.get(dto.getOperator2_id());
                        scenario.setOperator2(operator);
                    } else {
                        throw new BadRequestException("modality must be ON or OFF");
                    }
                }
                if (dto.getPoints() != null && dto.getPoints().length > 0) {

                    if (dto.getModality().equals(EModality.BETWEEN)) {
                        if (dto.getPoints().length == 2) {
                            Arrays.sort(dto.getPoints());
                            scenario.setPoints(dto.getPoints()[1] + "&" + dto.getPoints()[0]);
                        } else {
                            throw new BadRequestException("when modality is BETWEEN poinsts must have 2 value");
                        }
                    } else {
                        if (dto.getPoints().length == 1) {
                            scenario.setPoints(String.valueOf(dto.getPoints()[0]));
                        } else {
                            throw new BadRequestException("when modality isn't BETWEEN poinsts must have 1 value");
                        }
                    }

                }
            }
            scenarios.add(scenario);

        }

        return scenarioRepository.saveAll(scenarios);

    }
}
