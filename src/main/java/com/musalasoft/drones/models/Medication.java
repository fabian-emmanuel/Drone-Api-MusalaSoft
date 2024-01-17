package com.musalasoft.drones.models;

import com.musalasoft.drones.constants.Message;
import com.musalasoft.drones.constants.Regex;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
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
@Getter @Setter
@Document(collection = "medications")
public class Medication extends BaseDocument {

    @NotBlank(message = Message.NAME_IS_REQUIRED)
    @Pattern(regexp = Regex.MED_NAME, message = Message.MED_NAME_ALLOWABLE_VALUES)
    @Field(name = "name")
    private String name;

    @NotNull(message = Message.WEIGHT_IS_REQUIRED)
    @Positive
    @Field(name = "weight")
    private Double weight;

    @NotBlank(message = Message.CODE_IS_REQUIRED)
    @Pattern(regexp = Regex.MED_CODE, message = Message.MED_CODE_ALLOWABLE_VALUES)
    @Field(name = "code")
    private String code;

    @NotNull(message = Message.IMAGE_IS_REQUIRED)
    @Field(name = "image_url")
    private String imageUrl;

    @NotNull(message = Message.LOADED_IS_REQUIRED)
    @Field(name = "loaded")
    private boolean loaded;
}
