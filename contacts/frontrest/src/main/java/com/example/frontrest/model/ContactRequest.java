package com.example.frontrest.model;

public record ContactRequest(
		String firstName,
		String lastName,
		String phoneHome,
		String phoneMobile,
		String email,
		String street,
		String street2,
		Integer postalCode,
		String city,
		String country) {

}
