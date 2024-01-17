package com.musalasoft.drones.enums;

import com.musalasoft.drones.constants.Message;
import com.musalasoft.drones.exceptions.InvalidRequestException;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum Model {
    LIGHTWEIGHT(0, 50),
    MIDDLEWEIGHT(51, 150),
    CRUISERWEIGHT(151, 300),
    HEAVYWEIGHT(301, 500);

    private final int lowerLimit;
    private final int upperLimit;

    public static Model getModelByWeight(double weight) {
        if (weight < 0 || weight > 500) {
            throw new InvalidRequestException(Message.INVALID_DRONE_WEIGHT_LIMIT);
        }

        for (Model model : Model.values()) {
            if (weight >= model.lowerLimit && weight <= model.upperLimit) return model;
        }

        throw new InvalidRequestException(Message.WEIGHT_NOT_IN_RANGE);
    }
}
