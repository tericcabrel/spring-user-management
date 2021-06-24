package com.tericcabrel.authorization.models.response;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import java.util.Map;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class InvalidDataResponse {
    private Map<String, Map<String, List<String>>> data;

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public InvalidDataResponse(@JsonProperty("data") Map<String, Map<String, List<String>>> data) {
        this.data = data;
    }
}
