package org.merchantech.nftproject;

import org.merchantech.nftproject.model.bo.Account;
import org.merchantech.nftproject.model.dao.AccountDAO;
import org.merchantech.nftproject.model.dao.NFTCollectionDAO;
import org.merchantech.nftproject.model.dao.NFTDAO;
import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class NftProjectApplication  implements CommandLineRunner{
	
	
	public static void main(String[] args) {
		SpringApplication.run(NftProjectApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
	
		
	}
}
