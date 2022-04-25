package org.stibits.rnft.converters;

import java.util.HashMap;
import java.util.Map;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.stibits.rnft.model.bo.Profile;

@Component
public class ProfileDetailsConverter implements Converter<Profile, Map<String, Object>> {
    @Override
    public Map<String, Object> convert(Profile source) {
        Map<String, Object> data = new HashMap<>();
        data.put("id", source.getId());
        data.put("username", source.getAccount().getUsername());
        data.put("bio", source.getBio());
        data.put("customUrl", source.getCustomUrl());
        data.put("avatarUrl", source.getAvatarUrl());
        data.put("cover", null);

        return data;
    }
}
