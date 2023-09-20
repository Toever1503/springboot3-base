package com.springboot3base.common.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
public class CommonConfig {
    @Value("${web.mail.changePwURL}")
    private String chgPasswordUrl;

}
