package io.mosip.kernel.smsserviceprovider.msg91.dto;

import lombok.Data;

@Data
public class SmsServerResponseDto {
    /**
     * Status for request.
     */
    private String type;
    /**
     * Response message
     */
    private String message;
    /**
     * Response code.
     */
    private String code;
}
