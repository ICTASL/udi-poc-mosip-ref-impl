package io.mosip.kernel.smsserviceprovider.govsms.dto;

import lombok.Data;

/**
 * The DTO class for sms server response.
 *
 * @author Haren Senevirathna
 * @since 1.0.0
 *
 */
@Data
public class GovSmsServerResponseDto {

	private String message;
	private Boolean status;
	private String timestamp;
}
