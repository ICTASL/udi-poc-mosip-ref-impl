package com.smsserviceprovider.govsms.constant;

/**
 * This enum provides all the constant for sms notification.
 * 
 * @author Haren Senevirathna
 * @since 1.0.0
 *
 */
public enum SmsPropertyConstant {

	SUCCESS_RESPONSE("Sms Request Sent"),
	SUFFIX_MESSAGE(" digits"),
	ERROR_RESPONSE("Sms Request Fail");

	/**
	 * The property for sms notification.
	 */
	private String property;

	/**
	 * The constructor to set sms property.
	 * 
	 * @param property the property to set.
	 */
	private SmsPropertyConstant(String property) {
		this.property = property;
	}

	/**
	 * Getter for sms property.
	 * 
	 * @return the sms property.
	 */
	public String getProperty() {
		return property;
	}

}
