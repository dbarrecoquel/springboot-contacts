package com.example.contact.dto;

import java.time.LocalDateTime;

public record ContactDto(
		Long id,
		String firstname,
		String lastname,
		String phonehome,
		String phonemobile,
		String email,
		String street,
		String street2,
		Integer postalCode,
		String city,
		String country,
		LocalDateTime createdAt,
		LocalDateTime updatedAt) {
	
	public static ContactDto from(Long id,
		String firstname,
		String lastname,
		String phonehome,
		String phonemobile,
		String email,
		String street,
		String street2,
		Integer postalCode,
		String city,
		String country,
		LocalDateTime createdAt,
		LocalDateTime updatedAt) {
		
		return new ContactDto(id, firstname, lastname, phonehome, phonemobile, email, street, street2, postalCode, city, country, createdAt, updatedAt);
	}

}
