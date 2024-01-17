package com.musalasoft.drones.models;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;

@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
public abstract class BaseDocument {

    @Id
    private String id;

    @CreatedDate
    @Field(name = "created_at")
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Field(name = "modified_at")
    private LocalDateTime modifiedAt;
}

