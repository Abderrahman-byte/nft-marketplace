package org.stibits.rnft.converters;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.stibits.rnft.entities.Account;
import org.stibits.rnft.entities.Profile;

@Component
public class ProfileDetailsConverter implements Converter<Profile, Map<String, Object>> {
    @Autowired
    private DateTimestampConverter timestampConverter;

    @Override
    public Map<String, Object> convert(Profile source) {
        Map<String, Object> data = new HashMap<>();

        data.put("id", source.getId());
        data.put("displayName", source.getDisplayName());
        data.put("bio", source.getBio());
        data.put("customUrl", source.getCustomUrl());
        data.put("avatarUrl", source.getAvatarUrl());
        data.put("cover", source.getCoverUrl());
        data.put("createdDate", timestampConverter.convert(source.getAccount().getCreatedDate()));

        // Username may also be shown in the frontend along side the display name
        data.put("username", source.getAccount().getUsername()); 

        return data;
    }

    public Map<String, Object> convert(Account account) {
        return this.convert(account.getProfile());
    }

    public List<Map<String, Object>> convertAccountList (List<Account> source) {
        return source.stream().map(profile -> this.convert(profile.getProfile())).toList();
    }

    public List<Map<String, Object>> convert (List<Profile> source) {
        return source.stream().map(profile -> this.convert(profile)).toList();
    }
}
