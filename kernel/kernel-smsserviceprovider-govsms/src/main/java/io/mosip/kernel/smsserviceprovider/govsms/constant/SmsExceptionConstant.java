package io.mosip.kernel.smsserviceprovider.govsms.constant;

/**
 * This enum provides all the exception constants for sms notification.
 * 
 * @author Haren Senevirathna
 * @since 1.0.0
 *
 */
public enum SmsExceptionConstant {

	SMS_INVALID_CONTACT_NUMBER("KER-NOS-002", "Contact number cannot contains alphabet,special character or less than or more than ");
	/**
	 * The error code.
	 */
	private String errorCode;

	/**
	 * The error message.
	 */
	private String errorMessage;

	/**
	 * @param errorCode    The error code to be set.
	 * @param errorMessage The error message to be set.
	 */
	private SmsExceptionConstant(String errorCode, String errorMessage) {
		this.errorCode = errorCode;
		this.errorMessage = errorMessage;
	}

	/**
	 * @return the error code.
	 */
	public String getErrorCode() {
		return errorCode;
	}

	/**
	 * @return the error message.
	 */
	public String getErrorMessage() {
		return errorMessage;
	}

}
