package com.springboot3base.common.security;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Base64;

@Getter
@Configuration
public class EncryptProvider {

    @Value("${web.encrypt.key}")
    private String key;

    public Key getAESKey() throws Exception {
        String iv;
        Key keySpec;

        iv = key.substring(0, 16);
        byte[] keyBytes = new byte[16];
        byte[] b = iv.getBytes(StandardCharsets.UTF_8);

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
