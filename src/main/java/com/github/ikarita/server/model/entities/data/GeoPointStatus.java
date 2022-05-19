package com.github.ikarita.server.model.entities.data;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum GeoPointStatus {
    SUBMITTED("submitted"),
    ACCEPTED("accepted"),
    DELETED("deleted");

    private final String status;
}
