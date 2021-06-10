package io.mosip.authentication.demo.dto;

import lombok.Data;

@Data
public class JWTRequestDTO {

    private String applicationId;

    private String certificateUrl;

    private String dataToSign;

    private boolean includeCertHash;

    private boolean includeCertificate;

    private boolean includePayload;

    private String referenceId;
}
