package com.musalasoft.drones.models;

import com.musalasoft.drones.constants.Message;
import com.musalasoft.drones.enums.State;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Document(collection = "event_logs")
public class EventLog extends BaseDocument {

    @NotNull(message = Message.DRONE_ID_IS_REQUIRED)
    @Field(name = "drone_id")
    private String droneId;

    @PositiveOrZero
    @Max(value = 100, message = Message.INVALID_BATTERY_CAPACITY)
    @Field(name = "battery_capacity")
    private int batteryCapacity;

    @NotNull(message = Message.STATE_IS_REQUIRED)
    @Field(name = "state")
    private State state;

    @NotNull(message = Message.WEIGHT_IS_REQUIRED)
    @Field(name = "weight_limit")
    private Double weightLimit;

    @NotNull(message = Message.LOADED_ITEMS_WEIGHT_IS_REQUIRED)
    @Field(name = "loaded_items_weight")
    private Double loadedItemsWeight;
}
