package com.reds2.school.enc;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class Encryption {
 
    static public byte[] getOut(byte[] data, String time) throws NoSuchAlgorithmException, InvalidKeyException {

        SecretKeySpec spec = new SecretKeySpec(time.getBytes(), "HmacSHA512");
        Mac mac = Mac.getInstance("HmacSHA512");
        mac.init(spec);

        return mac.doFinal(data); 
    }

    static public String getString(byte[] data, String time) throws NoSuchAlgorithmException, InvalidKeyException {
        byte[] out;

        SecretKeySpec spec = new SecretKeySpec(time.getBytes(), "HmacSHA512");
        Mac mac = Mac.getInstance("HmacSHA512");
        mac.init(spec);

        out = mac.doFinal(data);
        
        StringBuilder sb = new StringBuilder();
        for (byte b : out) {
            sb.append(String.format("%2x", b).replaceAll(" ", "0"));
        }
        return sb.toString();
    }
}
