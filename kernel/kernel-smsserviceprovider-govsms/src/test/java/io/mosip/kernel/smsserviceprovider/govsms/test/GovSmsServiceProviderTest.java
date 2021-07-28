package io.mosip.kernel.smsserviceprovider.govsms.test;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.mosip.kernel.core.notification.exception.InvalidNumberException;
import io.mosip.kernel.core.notification.model.SMSResponseDto;
import io.mosip.kernel.smsserviceprovider.govsms.SMSServiceProviderBootApplication;
import io.mosip.kernel.smsserviceprovider.govsms.dto.GovSmsServerResponseDto;
import io.mosip.kernel.smsserviceprovider.govsms.impl.GovSMSServiceProviderImpl;
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

import java.util.Arrays;
import java.util.HashMap;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {SMSServiceProviderBootApplication.class})
public class GovSmsServiceProviderTest {

    @Autowired
    private GovSMSServiceProviderImpl service;

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

        HashMap<String, String> govSmsServerRequest = new HashMap<String, String>();
        govSmsServerRequest.put("data", "your otp is 4646");
        govSmsServerRequest.put("phoneNumber", "8987876473");
        govSmsServerRequest.put("sIDCode", govsmssidcode);
        govSmsServerRequest.put("userName", govsmsusername);
        govSmsServerRequest.put("password", govsmspassword);

        GovSmsServerResponseDto expectServerResponse = new GovSmsServerResponseDto();
        expectServerResponse.setMessage("success");

        SMSResponseDto dto = new SMSResponseDto();
        dto.setStatus(expectServerResponse.getMessage());
        dto.setMessage("Sms Request Sent");

        try {
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
            httpHeaders.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<HashMap<String, String>> entity = new HttpEntity<HashMap<String, String>>(govSmsServerRequest, httpHeaders);
            ResponseEntity<String> responseEntity = restTemplate.exchange(govsmsapi, HttpMethod.POST, entity, String.class);
            if (responseEntity.getStatusCode() == HttpStatus.OK) {
                GovSmsServerResponseDto govSmsServerResponseDto = new GovSmsServerResponseDto();
                String testserverResponse = responseEntity.getBody();
                ObjectMapper objectMapper = new ObjectMapper();
                govSmsServerResponseDto = objectMapper.readValue(testserverResponse, GovSmsServerResponseDto.class);

                when(govSmsServerResponseDto).thenReturn(expectServerResponse);
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