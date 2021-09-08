/**
 *
 */
package io.mosip.kernel.smsserviceprovider.govsms.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.mosip.kernel.core.notification.exception.InvalidNumberException;
import io.mosip.kernel.core.notification.model.SMSResponseDto;
import io.mosip.kernel.core.notification.spi.SMSServiceProvider;
import io.mosip.kernel.core.util.StringUtils;
import io.mosip.kernel.smsserviceprovider.govsms.constant.SmsExceptionConstant;
import io.mosip.kernel.smsserviceprovider.govsms.constant.SmsPropertyConstant;
import io.mosip.kernel.smsserviceprovider.govsms.dto.GovSmsServerResponseDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Haren Senevirathna
 * @since 1.0.0
 */
@Component
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
        logger.info("GOVSMS EXECUTED===============");
        SMSResponseDto smsResponseDTO = new SMSResponseDto();
        validateInput(contactNumber);
        logger.info("contact number validated===============");
        HashMap<String, String> govSmsServerRequest = new HashMap<String, String>();
        govSmsServerRequest.put("data", message);
        govSmsServerRequest.put("phoneNumber", contactNumber);
        govSmsServerRequest.put("sIDCode", govsmssidcode);
        govSmsServerRequest.put("userName", govsmsusername);
        govSmsServerRequest.put("password", govsmspassword);
        logger.info("govSmsServerRequest===============" + govSmsServerRequest);
        try {
            ResponseEntity<String> responseEntity = null;
            if (message.length() > 200) {
                logger.info("Message content length is grater than 200===============");
                List<String> messagesList = splitMessages(message);

                for (String subMessage : messagesList) {
                    govSmsServerRequest.put("data", subMessage);
                    logger.info("Sub Message content ===============" + subMessage);
                    responseEntity = sendSmsApi(govSmsServerRequest);
                    if (responseEntity.getStatusCode() != HttpStatus.OK) {
                        logger.info("Process break ===============");
                        break;
                    }
                    Thread.sleep(4000);
                }
            } else {
                logger.info("Message content length is less than 200===============");
                responseEntity = sendSmsApi(govSmsServerRequest);
            }

            logger.info("responseEntity===============" + responseEntity.getStatusCode());
            if (responseEntity.getStatusCode() == HttpStatus.OK) {
                logger.info("request HttpStatus Ok===============");
                GovSmsServerResponseDto govSmsServerResponseDto = new GovSmsServerResponseDto();
                String serverResponse = responseEntity.getBody();
                logger.info("serverResponse===============" + responseEntity.getBody());
                ObjectMapper objectMapper = new ObjectMapper();
                govSmsServerResponseDto = objectMapper.readValue(serverResponse, GovSmsServerResponseDto.class);
                logger.info("govSmsServerResponseDto===============" + govSmsServerResponseDto);
                if (govSmsServerResponseDto.getStatus()) {
                    logger.info("server response status true===========");
                    smsResponseDTO.setMessage(SmsPropertyConstant.SUCCESS_RESPONSE.getProperty());
                    smsResponseDTO.setStatus("success");
                    logger.info("smsResponseDTO===========" + smsResponseDTO);
                    return smsResponseDTO;
                } else {
                    logger.info("server response status false===========");
                    smsResponseDTO.setMessage(SmsPropertyConstant.ERROR_RESPONSE.getProperty());
                    smsResponseDTO.setStatus("failure");
                    logger.info("smsResponseDTO===========" + smsResponseDTO);
                    return smsResponseDTO;
                }
            } else {
                logger.info("request HttpStatus Not Ok===============");
                smsResponseDTO.setMessage(SmsPropertyConstant.ERROR_RESPONSE.getProperty());
                smsResponseDTO.setStatus("failure");
                return smsResponseDTO;
            }
        } catch (Exception e) {
            logger.info("Exception");
            logger.error("Exception coming========>", e);
            smsResponseDTO.setMessage(SmsPropertyConstant.ERROR_RESPONSE.getProperty());
            smsResponseDTO.setStatus("failure");
            return smsResponseDTO;
        }
    }

    private List<String> splitMessages(String message) {
        List<String> messagesList = new ArrayList<>();
        Pattern pattern = Pattern.compile("(.|\\s){1,200}(\\s+|$)");
        Matcher matcher = pattern.matcher(message);

        while (matcher.find()) {
            messagesList.add(matcher.group().trim());
        }
        return messagesList;
    }

    private ResponseEntity<String> sendSmsApi(HashMap<String, String> govSmsServerRequest) throws Exception {
        logger.info("sendSmsApi ===============");
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        System.out.println(govSmsServerRequest);
        HttpEntity<HashMap<String, String>> entity = new HttpEntity<HashMap<String, String>>(govSmsServerRequest, httpHeaders);
        ResponseEntity<String> responseEntity = restTemplate.exchange(govsmsapi, HttpMethod.POST, entity, String.class);
        return responseEntity;
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