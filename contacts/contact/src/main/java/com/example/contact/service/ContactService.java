package com.example.contact.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.contact.dto.ContactDto;
import com.example.contact.mapper.ContactMapper;
import com.example.contact.model.Contact;
import com.example.contact.repository.ContactRepository;

@Service
@Transactional
public class ContactService {
	
	private final ContactRepository contactRepository;
	private final ContactMapper contactMapper;
	
	public ContactService(ContactRepository contactRepository, ContactMapper contactMapper) {
		this.contactRepository = contactRepository;
		this.contactMapper = contactMapper;
	}
	
	public Page<Contact> getAllContact(Pageable pageable){
		
		return contactRepository.findAll(pageable);
	}
	
	public Page<Contact> findWithFilters(String firstname, String lastname, String phonemobile, String phonehome, String email, Pageable pageable){
		
		return contactRepository.findWithFilters(firstname,lastname,phonemobile,phonehome,email,pageable);
	}
	
	public List<ContactDto> getAllContact(){
		
		return contactMapper.toDtoList(contactRepository.findAll());
	}
	
	public ContactDto getContactById(Long id) {
		
		Contact contact = contactRepository.findById(id).orElseThrow(() -> new RuntimeException("Contact introuvable"));
		
		return contactMapper.toDto(contact);
		
	}
	
	public void deleteContact(Long id) {
		
		contactRepository.deleteById(id);
	}
	
	public ContactDto createContact(String firstname,
									String lastname,
									String phonemobile,
									String phonehome,
									String email,
									String street,
									String street2,
									Integer postalcode,
									String city,
									String country) {
		
		Contact contact = new Contact();
		contact.setFirstName(firstname);
		contact.setLastName(lastname);
		contact.setPhoneMobile(phonemobile);
		contact.setPhoneHome(phonehome);
		contact.setEmail(email);
		contact.setStreet(street);
		contact.setStreet2(street2);
		contact.setPostalCode(postalcode);
		contact.setCity(city);
		contact.setCountry(country);
		contact.setCreatedAt(LocalDateTime.now());
		contact.setUpdatedAt(LocalDateTime.now());
		
		Contact saved = contactRepository.save(contact);
		
		return contactMapper.toDto(saved);
		
	}
	
	public ContactDto updateContact(Long id,
			String firstname,
			String lastname,
			String phonemobile,
			String phonehome,
			String email,
			String street,
			String street2,
			Integer postalcode,
			String city,
			String country) {
		
		
		Contact contact = contactRepository.findById(id).orElseThrow(() -> new RuntimeException("Contact introuvable"));
		
		contact.setFirstName(firstname);
		contact.setLastName(lastname);
		contact.setPhoneMobile(phonemobile);
		contact.setPhoneHome(phonehome);
		contact.setEmail(email);
		contact.setStreet(street);
		contact.setStreet2(street2);
		contact.setPostalCode(postalcode);
		contact.setCity(city);
		contact.setCountry(country);
		contact.setUpdatedAt(LocalDateTime.now());
		
		Contact saved = contactRepository.save(contact);
		
		return contactMapper.toDto(saved);
	}
}
