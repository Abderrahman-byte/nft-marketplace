package org.stibits.rnft.converters;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class DateTimestampConverter implements Converter<Date, Long> {
    @Override
    public Long convert(Date source) {
        return new Timestamp(source.getTime()).getTime();
    }

    public Long convert(Calendar source) {
        return this.convert(source.getTime());
    }
}
