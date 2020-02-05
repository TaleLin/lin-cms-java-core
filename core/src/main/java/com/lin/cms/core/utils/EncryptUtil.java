package com.lin.cms.core.utils;

import com.amdelamar.jhash.Hash;
import com.amdelamar.jhash.algorithms.Type;
import com.amdelamar.jhash.exception.InvalidHashException;

public class EncryptUtil {

    /**
     * 设置密文密码
     *
     * @param password 原始密码
     * @return 加密密码
     */
    public static String encrypt(String password) {
        char[] chars = password.toCharArray();
        return Hash.password(chars).algorithm(Type.PBKDF2_SHA256).create();
    }

    /**
     * 验证加密密码
     *
     * @param encryptedPassword 密文密码
     * @param plainPassword     明文密码
     * @return 验证是否成功
     */
    public static boolean verify(String encryptedPassword, String plainPassword) {
        char[] chars = plainPassword.toCharArray();
        try {
            return Hash.password(chars).algorithm(Type.PBKDF2_SHA256).verify(encryptedPassword);
        } catch (InvalidHashException e) {
            return false;
        }
    }
}
