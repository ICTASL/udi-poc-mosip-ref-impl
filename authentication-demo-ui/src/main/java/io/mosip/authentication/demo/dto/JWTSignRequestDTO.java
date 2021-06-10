package io.mosip.authentication.demo.dto;

import lombok.Data;
/**
 * The Class JWTSignRequestDTO.
 *
 * @author Afaz Deen
 */
@Data
public class JWTSignRequestDTO {
    private String id;

    private Object metadata;

    private String version;

    private String requestTime;

    private JWTRequestDTO request;
}

