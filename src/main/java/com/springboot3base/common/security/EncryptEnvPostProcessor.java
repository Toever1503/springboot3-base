package com.springboot3base.common.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.PropertiesPropertySource;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Base64;
import java.util.Properties;

@Slf4j
public class EncryptEnvPostProcessor implements EnvironmentPostProcessor {

    private String key;
    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
        Properties props = new Properties();
        try {
            key = System.getenv("PTIS_CMS_BE");
            String[] params = {
                    "spring.datasource.url",
                    "spring.datasource.username",
                    "spring.datasource.password",
                    "spring.jwt.secret",
                    "cloud.aws.s3.bucket",
                    "cloud.aws.credentials.access-key",
                    "cloud.aws.credentials.secret-key",
                    "photoism.encrypt.key",
                    "photoism.mail.smtp.pass",
                    "photoism.file-pw"
            };
            for(String param : params) {
                String sData = environment.getProperty(param);
                if (sData != null)
                    props.put(param, decAES(sData));
            }
        } catch (Exception e) {
            System.out.println("DB id/password decrypt fail !");
        }

        environment.getPropertySources().addFirst(new PropertiesPropertySource("bizProps", props));

    }

    public Key getAESKey() throws Exception {
        Key keySpec;

        byte[] keyBytes = new byte[16];
        byte[] b = key.getBytes(StandardCharsets.UTF_8);

        int len = b.length;
        if (len > keyBytes.length) {
            len = keyBytes.length;
        }

        System.arraycopy(b, 0, keyBytes, 0, len);
        keySpec = new SecretKeySpec(keyBytes, "AES");

        return keySpec;
    }

    // 암호화
    public String encAES(String str) throws Exception {
        Key keySpec = getAESKey();
        String iv = "0987654321654321";
        Cipher c = Cipher.getInstance("AES/CBC/PKCS5Padding");
        c.init(Cipher.ENCRYPT_MODE, keySpec, new IvParameterSpec(iv.getBytes(StandardCharsets.UTF_8)));
        byte[] encrypted = c.doFinal(str.getBytes(StandardCharsets.UTF_8));

        return new String(Base64.getEncoder().encode(encrypted));
    }

    // 복호화
    public String decAES(String enStr) throws Exception {
        Key keySpec = getAESKey();
        String iv = "0987654321654321";
        Cipher c = Cipher.getInstance("AES/CBC/PKCS5Padding");
        c.init(Cipher.DECRYPT_MODE, keySpec, new IvParameterSpec(iv.getBytes(StandardCharsets.UTF_8)));
        byte[] byteStr = Base64.getDecoder().decode(enStr.getBytes(StandardCharsets.UTF_8));

        return new String(c.doFinal(byteStr), StandardCharsets.UTF_8);
    }

}