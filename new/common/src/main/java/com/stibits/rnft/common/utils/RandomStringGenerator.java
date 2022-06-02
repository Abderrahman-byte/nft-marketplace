package com.stibits.rnft.common.utils;

import java.io.Serializable;
import java.util.Random;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;

public class RandomStringGenerator implements IdentifierGenerator {
    private static final int DEFAULT_LENGTH = 25;
    private static final String CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

    @Override
    public Serializable generate(SharedSessionContractImplementor session, Object object) throws HibernateException {
        return generateStr();
    }

    public static String generateStr () {
        return generateStr(DEFAULT_LENGTH);
    }

    public static String generateStr (int length) {
        StringBuilder sb = new StringBuilder();
        Random random = new Random();

        for (int i = 0; i < length; i++) {
            int index = random.nextInt(CHARS.length());
            sb.append(CHARS.charAt(index));
        }

        return sb.toString();
    }
}
