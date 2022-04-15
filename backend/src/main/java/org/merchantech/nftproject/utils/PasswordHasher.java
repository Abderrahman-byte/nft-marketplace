package org.merchantech.nftproject.utils;

import at.favre.lib.crypto.bcrypt.BCrypt;

public class PasswordHasher {
    public static String hashPassword (String password) {
        return BCrypt.withDefaults().hashToString(10, password.toCharArray());
    }

    public static boolean checkHash (String hash, String password) {
        return BCrypt.verifyer().verify(password.toCharArray(), hash.toCharArray()).verified;
    }  
}
