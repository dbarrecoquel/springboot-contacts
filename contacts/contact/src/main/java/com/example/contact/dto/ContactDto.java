package com.example.contact.dto;

import java.time.LocalDateTime;

public record ContactDto(
		Long id,
		String firstName,
		String lastName,
		String phoneHome,
		String phoneMobile,
		String email,
		String street,
		String street2,
		Integer postalCode,
		String city,
		String country,
		LocalDateTime createdAt,
		LocalDateTime updatedAt) {
	
	public static ContactDto from(Long id,
		String firstName,
		String lastName,
		String phoneHome,
		String phoneMobile,
		String email,
		String street,
		String street2,
		Integer postalCode,
		String city,
		String country,
		LocalDateTime createdAt,
		LocalDateTime updatedAt) {
		
		return new ContactDto(id, firstName, lastName, phoneHome, phoneMobile, email, street, street2, postalCode, city, country, createdAt, updatedAt);
	}

}
