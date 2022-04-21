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
	public Profile insertProfile(Account acc, String display_name, String bio, String custom_url, String avatar_url) {
		Profile profile = entityManager.find(Profile.class, acc.getId());
		if(profile == null) {
			profile = new Profile();
			profile.setDisplayName(display_name);
		}
			System.out.println(display_name);
			profile.setId(acc.getId());
		    profile.setDisplayName(display_name);
			  if(bio!=null)
			  profile.setBio(bio);
			  if(custom_url!=null)
			  profile.setCustomUrl(custom_url);
			  if(avatar_url!=null)
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
			(String) data.get("display_name"),
			(String) data.get("bio"),
			(String) data.get("customUrl"),
			(String) data.get("avatarUrl")
		);
	}
	
	

}





