package com.example.frontrest.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.contact.dto.ContactDto;
import com.example.contact.mapper.ContactMapper;
import com.example.contact.model.Contact;
import com.example.contact.service.ContactService;
import com.example.frontrest.model.ContactRequest;

@RestController
@RequestMapping("/api/contacts")
@CrossOrigin(origins = "*")
public class ContactController {

	private final ContactService contactService;
	private final ContactMapper contactMapper;
	
	public ContactController(ContactService contactService, ContactMapper contactMapper) {
		this.contactService = contactService;
		this.contactMapper = contactMapper;
	}
	
	@GetMapping
	public ResponseEntity<Map<String, Object>> getAllNote(
	        @RequestParam(defaultValue = "0") int page,
	        @RequestParam(defaultValue = "10") int size,
	        @RequestParam(defaultValue = "id") String sortBy,
	        @RequestParam(defaultValue = "asc") String direction,
	        @RequestParam(required = false) String firstName,
	        @RequestParam(required = false) String lastName,
	        @RequestParam(required = false) String phoneMobile,
	        @RequestParam(required = false) String phoneHome,
	        @RequestParam(required = false) String email) {

	    Sort sort = direction.equalsIgnoreCase("desc")
	            ? Sort.by(sortBy).descending()
	            : Sort.by(sortBy).ascending();

	    Pageable pageable = PageRequest.of(page, size, sort);

	    Page<Contact> pageResult = contactService.findWithFilters(firstName, lastName, phoneMobile, phoneHome, email, pageable);

	    List<ContactDto> content = contactMapper.toDtoList(pageResult.getContent());
	            

	    Map<String, Object> response = new HashMap<>();
	    response.put("content", content);
	    response.put("page", pageResult.getNumber());
	    response.put("size", pageResult.getSize());
	    response.put("totalElements", pageResult.getTotalElements());
	    response.put("totalPages", pageResult.getTotalPages());
	    response.put("last", pageResult.isLast());

	    return ResponseEntity.ok(response);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<ContactDto> getContactById(@PathVariable Long id){
		
		return ResponseEntity.ok(contactService.getContactById(id));
	}
	
	@PostMapping
	public ResponseEntity<ContactDto> createContact(@RequestBody ContactRequest body){
		
		ContactDto contact = contactService.createContact(body.firstName(), body.lastName(), body.phoneMobile(), body.phoneHome(), body.email(), body.street(), body.street2(), body.postalCode(), body.city(), body.country());
		return ResponseEntity.ok(contact);
		
	}
	@PutMapping("/{id}")
	public ResponseEntity<ContactDto> updateContact(@PathVariable Long id, @RequestBody ContactRequest body){
		
		ContactDto contact = contactService.updateContact(id,body.firstName(), body.lastName(), body.phoneMobile(), body.phoneHome(), body.email(), body.street(), body.street2(), body.postalCode(), body.city(), body.country());
		return ResponseEntity.ok(contact);
		
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteContact(@PathVariable Long id){
		
		contactService.deleteContact(id);
		return ResponseEntity.noContent().build();
	}
}
