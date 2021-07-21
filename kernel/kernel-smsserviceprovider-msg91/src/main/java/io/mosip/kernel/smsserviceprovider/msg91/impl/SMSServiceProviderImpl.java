/**
 *
 */
package io.mosip.kernel.smsserviceprovider.msg91.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.mosip.kernel.core.notification.exception.InvalidNumberException;
import io.mosip.kernel.core.notification.model.SMSResponseDto;
import io.mosip.kernel.core.notification.spi.SMSServiceProvider;
import io.mosip.kernel.core.util.StringUtils;
import io.mosip.kernel.smsserviceprovider.msg91.constant.SmsExceptionConstant;
import io.mosip.kernel.smsserviceprovider.msg91.constant.SmsPropertyConstant;
import io.mosip.kernel.smsserviceprovider.msg91.dto.GovSmsServerRequest;
import io.mosip.kernel.smsserviceprovider.msg91.dto.SmsServerResponseDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/**
 * @author Ritesh Sinha
 * @since 1.0.0
 */
@Component
public class SMSServiceProviderImpl implements SMSServiceProvider {

    private static final Logger logger = LoggerFactory.getLogger(SMSServiceProviderImpl.class);

    @Autowired
    RestTemplate restTemplate;

    @Value("${mosip.kernel.sms.number.length}")
    int numberLength;

    @Value("${ict.govsms.sms.api}")
    String govsmsapi;

    @Value("${ict.govsms.sms.sidcode}")
    String govsmssidcode;

    @Value("${ict.govsms.sms.username}")
    String govsmsusername;

    @Value("${ict.govsms.sms.password}")
    String govsmspassword;

    @Override
    public SMSResponseDto sendSms(String contactNumber, String message) {
        SMSResponseDto smsResponseDTO = new SMSResponseDto();
        validateInput(contactNumber);

        GovSmsServerRequest govSmsServerRequest = new GovSmsServerRequest();
        govSmsServerRequest.setContact(contactNumber);
        govSmsServerRequest.setMessage(message);
        govSmsServerRequest.setSidcode(govsmssidcode);
        govSmsServerRequest.setUsername(govsmsusername);
        govSmsServerRequest.setPassword(govsmspassword);

        try {
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<GovSmsServerRequest> entity = new HttpEntity<GovSmsServerRequest>(govSmsServerRequest, httpHeaders);
            ResponseEntity<String> responseEntity = restTemplate.exchange(govsmsapi, HttpMethod.POST, entity, String.class);
            if (responseEntity.getStatusCode() == HttpStatus.OK) {
                SmsServerResponseDto smsServerResponseDto = new SmsServerResponseDto();
                String serverResponse = responseEntity.getBody();
                ObjectMapper objectMapper = new ObjectMapper();
                smsServerResponseDto = objectMapper.readValue(serverResponse, SmsServerResponseDto.class);

                if (smsServerResponseDto.getStatus()) {
                    smsResponseDTO.setMessage(SmsPropertyConstant.SUCCESS_RESPONSE.getProperty());
                    smsResponseDTO.setStatus("success");
                    return smsResponseDTO;
                } else {
                    logger.error("failure");
                    smsResponseDTO.setMessage(SmsPropertyConstant.ERROR_RESPONSE.getProperty());
                    smsResponseDTO.setStatus("failure");
                    return smsResponseDTO;
                }
            } else {
                logger.error("failure");
                smsResponseDTO.setMessage(SmsPropertyConstant.ERROR_RESPONSE.getProperty());
                smsResponseDTO.setStatus("failure");
                return smsResponseDTO;
            }
        } catch (Exception e) {
            logger.error("failure");
            smsResponseDTO.setMessage(SmsPropertyConstant.ERROR_RESPONSE.getProperty());
            smsResponseDTO.setStatus("failure");
            return smsResponseDTO;
        }
    }

    private void validateInput(String contactNumber) {
        if (!StringUtils.isNumeric(contactNumber) || contactNumber.length() < numberLength
                || contactNumber.length() > numberLength) {
            throw new InvalidNumberException(SmsExceptionConstant.SMS_INVALID_CONTACT_NUMBER.getErrorCode(),
                    SmsExceptionConstant.SMS_INVALID_CONTACT_NUMBER.getErrorMessage() + numberLength
                            + SmsPropertyConstant.SUFFIX_MESSAGE.getProperty());
        }
    }

}