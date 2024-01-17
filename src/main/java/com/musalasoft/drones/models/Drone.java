package com.musalasoft.drones.models;

import com.musalasoft.drones.constants.Message;
import com.musalasoft.drones.enums.Model;
import com.musalasoft.drones.enums.State;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.HashSet;
import java.util.Set;

@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
@Document(collection = "drones")
public class Drone extends BaseDocument {

    @Size(max = 100, message = Message.SERIAL_NUMBER_CHARACTERS)
    @Field(name = "serial_number")
    private String serialNumber;

    @NotNull(message = Message.MODEL_IS_REQUIRED)
    @Field(name = "model")
    private Model model;

    @NotNull(message = Message.WEIGHT_IS_REQUIRED)
    @Field(name = "weight_limit")
    private Double weightLimit;

    @PositiveOrZero
    @Max(value = 100, message = Message.INVALID_BATTERY_CAPACITY)
    @Field(name = "battery_capacity")
    private int batteryCapacity;

    @NotNull(message = Message.STATE_IS_REQUIRED)
    @Field(name = "state")
    private State state;

    @Field(name = "medication_ids")
    private Set<String> medicationIds = new HashSet<>();
}
