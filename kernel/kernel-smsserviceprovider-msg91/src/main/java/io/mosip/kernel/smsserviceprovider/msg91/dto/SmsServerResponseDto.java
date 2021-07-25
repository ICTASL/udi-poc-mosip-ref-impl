package io.mosip.kernel.smsserviceprovider.msg91.dto;

import lombok.Data;

/**
 * The DTO class for sms server response.
 * 
 * @author Ritesh Sinha
 * @since 1.0.0
 *
 */
@Data
public class SmsServerResponseDto {

	private String message;
	private Boolean status;
	private String timestamp;
}
