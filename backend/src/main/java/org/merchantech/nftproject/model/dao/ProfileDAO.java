package org.merchantech.nftproject.model.dao;

import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.merchantech.nftproject.model.bo.Account;
import org.merchantech.nftproject.model.bo.Profile;
import org.merchantech.nftproject.utils.RandomGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.*;

@Repository
public class ProfileDAO {
	 @PersistenceContext
	    private EntityManager entityManager;
	 @Autowired
	    private RandomGenerator randomGenerator;
     public Profile getProfilebyId(String id) {
    	 return entityManager.find(Profile.class, id);
     }
     
     @Transactional
     public Profile insertProfile(Account acc , String display_name, String bio, String custom_url, String avatar_url)
     {
    	  Profile profile = new Profile();
    	  profile.setId(acc.getId());
    	  profile.setDisplay_name(display_name);
    	  profile.setBio(bio);
    	  profile.setCustom_url(custom_url);
    	  profile.setAvatar_url(avatar_url);
    	  profile.setAccount(acc);
    	  entityManager.persist(profile);

    	 return profile;
     }
    
    /* @Transactional
     public Profile updateProfile(String id,String display_name, String bio, String custom_url, String avatar_url) {
    	 Profile profile = entityManager.find(Profile.class, id);
    	  profile.setAccount_id(id);
    	  if(display_name!=null)
    	  profile.setDisplay_name(display_name);
    	  if(bio!=null)
    	  profile.setBio(bio);
    	  if(custom_url!=null)
    	  profile.setCustom_url(custom_url);
    	  if(avatar_url!=null)
    	  profile.setAvatar_url(avatar_url);
    	  
    	return entityManager.merge(profile);

     }*/

     @Transactional
	public Profile insertprofile(Map<String, Object> data,Account account) {

		//if(this.getProfilebyId(AccountId)==null)
	    return this.insertProfile(
					account,
		            (String) data.get("display_name"),
		            (String) data.get("bio"), 
		            (String) data.get("custom_url"),
		            (String) data.get("avatar_url")
	        );
		/*else return this.updateProfile(
				    AccountId,
		            (String) data.get("display_name"),
		            (String) data.get("bio"), 
		            (String) data.get("custom_url"),
		            (String) data.get("avatar_url")
		    );*/
	}
     
     
     
     
}
