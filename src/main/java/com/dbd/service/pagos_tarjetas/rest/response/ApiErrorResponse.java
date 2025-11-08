package com.dbd.service.pagos_tarjetas.rest.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.http.HttpStatus;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ApiErrorResponse(
        @JsonProperty("status_code") HttpStatus statusCode,
        @JsonProperty("error_message") String errorMessage) {}
