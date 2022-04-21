package org.merchantech.nftproject.model.dao;

import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.merchantech.nftproject.model.bo.Account;
import org.merchantech.nftproject.model.bo.Profile;
import org.springframework.stereotype.*;

@Repository
public class ProfileDAO {

	@PersistenceContext
	private EntityManager entityManager;

	public Profile getProfilebyId(String id) {
		return entityManager.find(Profile.class, id);
	}

	@Transactional
	public Profile insertProfile(Account acc, String displayName, String bio, String customUrl, String avatarUrl) {
		Profile profile = entityManager.find(Profile.class, acc.getId());

		if (profile == null) {
			profile = new Profile();
			profile.setId(acc.getId());
			profile.setAccount(acc);
		}
		
		if (displayName != null) profile.setDisplayName(displayName);
		if (bio != null) profile.setBio(bio);
		if (customUrl != null) profile.setCustomUrl(customUrl);
		if (avatarUrl != null) profile.setAvatarUrl(avatarUrl);
		
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
				(String) data.get("avatarUrl"));
	}

}
