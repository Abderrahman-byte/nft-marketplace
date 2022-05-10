package org.stibits.rnft;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.stibits.rnft.repositories.TransactionDAO;


@SpringBootApplication
public class NftProjectApplication {
	
	
	public static void main(String[] args) {
		SpringApplication.run(NftProjectApplication.class, args);
	}

	
}
