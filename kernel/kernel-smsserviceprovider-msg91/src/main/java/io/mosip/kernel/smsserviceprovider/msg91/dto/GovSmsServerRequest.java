package io.mosip.kernel.smsserviceprovider.msg91.dto;

import lombok.Data;

@Data
public class GovSmsServerRequest {

    private String message;
    private String contact;
    private String sidcode;
    private String username;
    private String password;

    public GovSmsServerRequest() {
    }
}
