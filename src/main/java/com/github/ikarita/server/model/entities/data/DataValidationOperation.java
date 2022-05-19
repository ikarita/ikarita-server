package com.github.ikarita.server.model.entities.data;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum DataValidationOperation {
    SUBMITTED("submitted"),
    ACCEPTED("accepted"),
    DELETED("deleted");

    private final String operation;
}