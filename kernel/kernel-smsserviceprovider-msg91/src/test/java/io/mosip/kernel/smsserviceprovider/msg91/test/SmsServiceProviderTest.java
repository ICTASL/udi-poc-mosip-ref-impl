package io.mosip.kernel.smsserviceprovider.msg91.test;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.mosip.kernel.core.notification.exception.InvalidNumberException;
import io.mosip.kernel.core.notification.model.SMSResponseDto;
import io.mosip.kernel.smsserviceprovider.msg91.SMSServiceProviderBootApplication;
import io.mosip.kernel.smsserviceprovider.msg91.dto.GovSmsServerRequest;
import io.mosip.kernel.smsserviceprovider.msg91.dto.SmsServerResponseDto;
import io.mosip.kernel.smsserviceprovider.msg91.impl.SMSServiceProviderImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {SMSServiceProviderBootApplication.class})
public class SmsServiceProviderTest {

    @Autowired
    SMSServiceProviderImpl service;

    @MockBean
    RestTemplate restTemplate;

    @Value("${ict.govsms.sms.api}")
    String govsmsapi;

    @Value("${ict.govsms.sms.sidcode}")
    String govsmssidcode;

    @Value("${ict.govsms.sms.username}")
    String govsmsusername;

    @Value("${ict.govsms.sms.password}")
    String govsmspassword;

    @Test
    public void sendSmsTest() throws Exception {

        GovSmsServerRequest govSmsServerRequest = new GovSmsServerRequest();
        govSmsServerRequest.setContact("8987876473");
        govSmsServerRequest.setMessage("your otp is 4646");
        govSmsServerRequest.setSidcode(govsmssidcode);
        govSmsServerRequest.setUsername(govsmsusername);
        govSmsServerRequest.setPassword(govsmspassword);

        SmsServerResponseDto expectServerResponse = new SmsServerResponseDto();
        expectServerResponse.setMessage("success");

        SMSResponseDto dto = new SMSResponseDto();
        dto.setStatus(expectServerResponse.getMessage());
        dto.setMessage("Sms Request Sent");

        try {
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<GovSmsServerRequest> entity = new HttpEntity<GovSmsServerRequest>(govSmsServerRequest, httpHeaders);
            ResponseEntity<String> responseEntity = restTemplate.exchange(govsmsapi, HttpMethod.POST, entity, String.class);
            if (responseEntity.getStatusCode() == HttpStatus.OK) {
                SmsServerResponseDto smsServerResponseDto = new SmsServerResponseDto();
                String testserverResponse = responseEntity.getBody();
                ObjectMapper objectMapper = new ObjectMapper();
                smsServerResponseDto = objectMapper.readValue(testserverResponse, SmsServerResponseDto.class);

                when(smsServerResponseDto).thenReturn(expectServerResponse);
                SMSResponseDto actualServerResponse = service.sendSms("8987876473", "your otp is 4646");
                assertEquals(expectServerResponse, actualServerResponse);
            }

            when(restTemplate.postForEntity(Mockito.anyString(), Mockito.eq(Mockito.any()), Object.class)).thenReturn(new ResponseEntity<>(expectServerResponse, HttpStatus.OK));
        } catch (Exception e) {
            System.out.println(e.getStackTrace());
        }

    }

    @Test(expected = InvalidNumberException.class)
    public void invalidContactNumberTest() {
        service.sendSms("jsbchb", "hello your otp is 45373");
    }

    @Test(expected = InvalidNumberException.class)
    public void contactNumberMinimumThresholdTest() {
        service.sendSms("78978976", "hello your otp is 45373");
    }

    @Test(expected = InvalidNumberException.class)
    public void contactNumberMaximumThresholdTest() {
        service.sendSms("7897897458673484376", "hello your otp is 45373");
    }

    @Test
    public void validGateWayTest() {
        service.sendSms("1234567890", "hello your otp is 45373");
    }

}