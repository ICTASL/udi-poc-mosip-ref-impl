/**
 *
 */
package com.smsserviceprovider.govsms.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.smsserviceprovider.govsms.constant.SmsExceptionConstant;
import com.smsserviceprovider.govsms.constant.SmsPropertyConstant;
import com.smsserviceprovider.govsms.dto.response.GovSmsServerResponseDto;
import io.mosip.kernel.core.notification.exception.InvalidNumberException;
import io.mosip.kernel.core.notification.model.SMSResponseDto;
import io.mosip.kernel.core.notification.spi.SMSServiceProvider;
import io.mosip.kernel.core.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.HashMap;

/**
 * @author Haren Senevirathna
 * @since 1.0.0
 */
@Service
public class GovSMSServiceProviderImpl implements SMSServiceProvider {

    private static final Logger logger = LoggerFactory.getLogger(GovSMSServiceProviderImpl.class);

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

        HashMap<String, String> govSmsServerRequest = new HashMap<String, String>();
        govSmsServerRequest.put("data", message);
        govSmsServerRequest.put("phoneNumber", contactNumber);
        govSmsServerRequest.put("sIDCode", govsmssidcode);
        govSmsServerRequest.put("userName", govsmsusername);
        govSmsServerRequest.put("password", govsmspassword);

        try {
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
            httpHeaders.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<HashMap<String, String>> entity = new HttpEntity<HashMap<String, String>>(govSmsServerRequest, httpHeaders);
            ResponseEntity<String> responseEntity = restTemplate.exchange(govsmsapi, HttpMethod.POST, entity, String.class);
            if (responseEntity.getStatusCode() == HttpStatus.OK) {
                GovSmsServerResponseDto govSmsServerResponseDto = new GovSmsServerResponseDto();
                String serverResponse = responseEntity.getBody();
                ObjectMapper objectMapper = new ObjectMapper();
                govSmsServerResponseDto = objectMapper.readValue(serverResponse, GovSmsServerResponseDto.class);

                if (govSmsServerResponseDto.getStatus()) {
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