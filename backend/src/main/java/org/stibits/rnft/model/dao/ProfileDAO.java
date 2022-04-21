package org.stibits.rnft.model.dao;

import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.stibits.rnft.model.bo.Account;
import org.stibits.rnft.model.bo.Profile;
import org.springframework.stereotype.*;

@Repository
public class ProfileDAO {
	@PersistenceContext
	private EntityManager entityManager;

	public Profile getProfilebyId(String id) {
		return entityManager.find(Profile.class, id);
	}

	@Transactional
	public Profile insertProfile(Account acc, String display_name, String bio, String custom_url, String avatar_url) {
		Profile profile = new Profile();
		
		profile.setId(acc.getId());
		profile.setDisplayName(display_name);
		profile.setBio(bio);
		profile.setCustomUrl(custom_url);
		profile.setAvatarUrl(avatar_url);
		profile.setAccount(acc);
		acc.setProfile(profile);

		entityManager.merge(acc);

		return profile;
	}

	@Transactional
	public Profile insertprofile(Map<String, Object> data, Account account) {
		return this.insertProfile(
			account,
			(String) data.get("displayName"),
			(String) data.get("bio"),
			(String) data.get("customUrl"),
			(String) data.get("avatarUrl")
		);
	}
}
