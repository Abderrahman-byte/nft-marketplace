package org.merchantech.nftproject;

import org.merchantech.nftproject.model.bo.Account;
import org.merchantech.nftproject.model.dao.AccountDAO;
import org.merchantech.nftproject.model.dao.ProfileDAO;
import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class NftProjectApplication implements CommandLineRunner {
	@Autowired
	AccountDAO accountDao;

	@Autowired
	ProfileDAO profileDao;

	public static void main(String[] args) {
		SpringApplication.run(NftProjectApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		Account acc = accountDao.getAccountById("wa0K5Rusc4k5soVdEOXLJkRHn");

		profileDao.insertProfile(acc, "Account1", "Bio", "costumurl", "avatarurl");
	}
}
