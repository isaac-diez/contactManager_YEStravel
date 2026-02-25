package com.yestravel.contactsmanager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ContactsManagerApplication {

	public static void main(String[] args) {
		SpringApplication.run(ContactsManagerApplication.class, args);
	}

}
