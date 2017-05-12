package com.aisino.common.util;


import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class AesUtil {

    private final static String PASSWORD = "12345678";

    public static void main(String args[]) throws Exception {
        String content = "加密内容";
        String enStr = encrypt(content);
        //System.out.println(enStr);
        String result = decrypt("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");
        System.out.println(result);
    }

    /**
     * 加密
     *
     * @param content
     *            需要加密的内容
     * @param password
     *            加密密码
     * @return
     */
    public static byte[] encrypt(String content, String password) throws Exception{
        KeyGenerator kgen = KeyGenerator.getInstance("AES");
        SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
        random.setSeed(password.getBytes());
        kgen.init(128, random);
        SecretKey secretKey = kgen.generateKey();
        byte[] enCodeFormat = secretKey.getEncoded();
        SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");
        Cipher cipher = Cipher.getInstance("AES");// 创建密码器
        byte[] byteContent = content.getBytes("utf-8");
        cipher.init(Cipher.ENCRYPT_MODE, key);// 初始化
        byte[] result = cipher.doFinal(byteContent);
        return result; // 加密
    }

    /**
     * 解密
     *
     * @param content
     *            待解密内容
     * @param password
     *            解密密钥
     * @return
     */
    public static byte[] decrypt(byte[] content, String password) throws Exception{
        KeyGenerator kgen = KeyGenerator.getInstance("AES");
        SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
        random.setSeed(password.getBytes());
        kgen.init(128, random);
        SecretKey secretKey = kgen.generateKey();
        byte[] enCodeFormat = secretKey.getEncoded();
        SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");
        Cipher cipher = Cipher.getInstance("AES");// 创建密码器
        cipher.init(Cipher.DECRYPT_MODE, key);// 初始化
        byte[] result = cipher.doFinal(content);
        return result; // 加密
    }

    /**将二进制转换成16进制
     * @param buf
     * @return
     */
    public static String parseByte2HexStr(byte buf[]) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < buf.length; i++) {
            String hex = Integer.toHexString(buf[i] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            sb.append(hex.toUpperCase());
        }
        return sb.toString();
    }

    /**将16进制转换为二进制
     * @param hexStr
     * @return
     */
    public static byte[] parseHexStr2Byte(String hexStr) {
        if (hexStr.length() < 1)
            return null;
        byte[] result = new byte[hexStr.length()/2];
        for (int i = 0;i< hexStr.length()/2; i++) {
            int high = Integer.parseInt(hexStr.substring(i*2, i*2+1), 16);
            int low = Integer.parseInt(hexStr.substring(i*2+1, i*2+2), 16);
            result[i] = (byte) (high * 16 + low);
        }
        return result;
    }

    /**
     * 加密
     * @param content
     * @return
     * @throws Exception
     */
    public static String encrypt(String content){
        byte[] enResult = new byte[0];
        try {
            enResult = encrypt(content, PASSWORD);
        } catch (Exception e) {
            e.printStackTrace();
        }
        String hexStr = parseByte2HexStr(enResult);
        String baseStr = Base64.getBase64(hexStr);
        return baseStr;
    }

    /**
     * 解密
     * @param content
     * @return
     * @throws Exception
     */
    public static String decrypt(String content){
        String enResult = Base64.getFromBase64(content);
        String result = null;
        byte[] bt = parseHexStr2Byte(enResult);
        byte[] resultBt = new byte[0];
        try {
            resultBt = decrypt(bt, PASSWORD);
            result = new String(resultBt, "utf-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}
