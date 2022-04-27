package org.stibits.rnft.converters;

import java.util.HashMap;
import java.util.Map;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.stibits.rnft.entities.Profile;

@Component
public class ProfileDetailsConverter implements Converter<Profile, Map<String, Object>> {
    @Override
    public Map<String, Object> convert(Profile source) {
        Map<String, Object> data = new HashMap<>();
        data.put("id", source.getId());
        data.put("displayName", source.getDisplayName());
        data.put("bio", source.getBio());
        data.put("customUrl", source.getCustomUrl());
        data.put("avatarUrl", source.getAvatarUrl());
        data.put("cover", source.getCoverUrl());

        return data;
    }
}
