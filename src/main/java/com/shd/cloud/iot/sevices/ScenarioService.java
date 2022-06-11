package com.shd.cloud.iot.sevices;

import java.util.ArrayList;
import java.util.List;

import com.shd.cloud.iot.exception.BadRequestException;
import com.shd.cloud.iot.models.EModality;
import com.shd.cloud.iot.models.Operator;
import com.shd.cloud.iot.models.Scenario;
import com.shd.cloud.iot.models.ScenarioOperators;
import com.shd.cloud.iot.models.ScenarioSensors;
import com.shd.cloud.iot.models.Sensor;
import com.shd.cloud.iot.payload.request.scenarioRequest.ScenarioOperatorRequest;
import com.shd.cloud.iot.payload.request.scenarioRequest.ScenarioRequest;
import com.shd.cloud.iot.payload.request.scenarioRequest.ScenarioSensorRequest;
import com.shd.cloud.iot.repositorys.ScenarioOperatorsRepository;
import com.shd.cloud.iot.repositorys.ScenarioRepository;
import com.shd.cloud.iot.repositorys.ScenarioSensorsRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ScenarioService {

    @Autowired
    private ScenarioRepository scenarioRepo;

    @Autowired
    private ScenarioOperatorsRepository scenarioOperatorsRepo;

    @Autowired
    private ScenarioSensorsRepository scenarioSensorsRepo;

    @Autowired
    private OperatorService operatorService;

    @Autowired
    private SensorService sensorService;

    /**
     * @param scenario
     * @param user_id
     * @return List<Scenario>
     */
    public Scenario create(ScenarioRequest scenario, Long user_id) {

        if (scenario == null) {
            throw new BadRequestException("there is no scenario");
        }
        if (scenario.getScenarioSensors() == null || scenario.getScenarioSensors().size() < 1)
            throw new BadRequestException("there is no scenarioSensor");
        if (scenario.getScenarioOperators() == null || scenario.getScenarioOperators().size() < 1)
            throw new BadRequestException("there is no scenarioOperator");

        Scenario sc = new Scenario();
        sc.setName(scenario.getName());
        sc = scenarioRepo.save(sc);
        List<ScenarioSensors> scenarioSensors = new ArrayList<>();
        List<ScenarioOperators> scenarioOperators = new ArrayList<>();
        for (ScenarioSensorRequest s_sensor : scenario.getScenarioSensors()) {

            ScenarioSensors scenarioSensor = new ScenarioSensors();
            if (s_sensor.getSensor_id() == null) {
                throw new BadRequestException("Sernsor id is null");
            }
            Sensor sensorModel = sensorService.get(s_sensor.getSensor_id());
            if (s_sensor.getPoints().length == 2) {
                if (!s_sensor.getModality().equals(EModality.BETWEEN))
                    throw new BadRequestException("use 1 point in this case");
                s_sensor.setModality(EModality.BETWEEN);
                if (s_sensor.getPoints()[0] >= s_sensor.getPoints()[1]) {
                    throw new BadRequestException("first point must smaller than second one");
                }
                scenarioSensor.setPoints(String.valueOf(s_sensor.getPoints()[0] + "," + s_sensor.getPoints()[1]));

            } else if (s_sensor.getPoints().length == 1) {
                if (s_sensor.getModality().equals(EModality.BETWEEN)) {
                    throw new BadRequestException("Can not use BETWEEN modality");
                }
                scenarioSensor.setPoints(String.valueOf(s_sensor.getPoints()[0]));
            } else {
                throw new BadRequestException("points size min=1 and max=2");
            }
            scenarioSensor.setSensor(sensorModel);
            scenarioSensor.setModality(s_sensor.getModality());
            scenarioSensor.setScenario(sc);
            scenarioSensors.add(scenarioSensorsRepo.save(scenarioSensor));

        }
        sc.setEffective_sensors(scenarioSensors);
        for (ScenarioOperatorRequest s_operator : scenario.getScenarioOperators()) {
            if (s_operator.getOperator_id() == null) {
                throw new BadRequestException("Operator id is null");
            }
            Operator operatorModel = operatorService.get(s_operator.getOperator_id());
            ScenarioOperators scenarioOperator = new ScenarioOperators(operatorModel,
                    s_operator.getOperator_state());
            scenarioOperator.setScenario(sc);
            scenarioOperators.add(scenarioOperatorsRepo.save(scenarioOperator));
        }
        sc.setTarget_operators(scenarioOperators);

        return scenarioRepo.save(sc);

    }
}
