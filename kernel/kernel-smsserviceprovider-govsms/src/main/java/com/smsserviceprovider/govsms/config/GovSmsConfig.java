package com.smsserviceprovider.govsms.config;

import com.smsserviceprovider.govsms.impl.GovSMSServiceProviderImpl;
import io.mosip.kernel.core.notification.spi.SMSServiceProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GovSmsConfig {

    @Bean
    private SMSServiceProvider smsServiceProvider() {
        return new GovSMSServiceProviderImpl();
    }
}
