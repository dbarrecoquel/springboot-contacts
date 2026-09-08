package com.example.contact;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.example.contact.dto.ContactDto;
import com.example.contact.mapper.ContactMapper;
import com.example.contact.model.Contact;
import com.example.contact.repository.ContactRepository;
import com.example.contact.service.ContactService;

@ExtendWith(MockitoExtension.class)
public class ContactServiceTest {
	
	@Mock
	private ContactRepository contactRepository;
	
	@Mock
	private ContactMapper contactMapper;
	
	@InjectMocks
	private ContactService contactService;
	
	private Contact contact;
	private ContactDto contactDto;
	
	@BeforeEach
	void setUp() {
		
		contact = new Contact();
		contact.setId(1L);
		contact.setFirstName("test");
		contact.setLastName("test");
		contact.setPhoneHome("0123456789");
		contact.setPhoneMobile("0612345789");
		contact.setEmail("test@test.fr");
		
		contactDto = ContactDto.from(1L, "test", "test", "0123456789", "0612345789", "test@test.fr", null, null, null, null, null, null, null);
		
	}
	
	@Nested
	@DisplayName("Recherches simples")
	class FindTests {
		
		@Test
		@DisplayName("shouldGetAllContact pageable")
		void shouldGetAllContactPageable() {
			
			Pageable pageable = PageRequest.of(0, 10);
			Page<Contact> page = new PageImpl<Contact>(List.of(contact));
			
			when(contactRepository.findAll(pageable)).thenReturn(page);
			
			Page<Contact> result = contactService.getAllContact(pageable);
			
			assertThat(result.getContent()).hasSize(1);
			verify(contactRepository, times(1)).findAll(pageable);
		}
		@Test
		@DisplayName("shouldGetAllContact list")
		void shouldGetAllContactList() {
			
			List<Contact> contacts = List.of(contact);
			List<ContactDto> contactsDto = List.of(contactDto);
			
			when(contactRepository.findAll()).thenReturn(contacts);
			when(contactMapper.toDtoList(contacts)).thenReturn(contactsDto);
			
			List<ContactDto> result = contactService.getAllContact();
			
			assertThat(result).hasSize(1);
			verify(contactRepository, times(1)).findAll();
		}
		
		@Test
		@DisplayName("shouldGetContactById")
		void shouldGetContactById() {
			
			when(contactRepository.findById(1L)).thenReturn(Optional.of(contact));
			when(contactMapper.toDto(contact)).thenReturn(contactDto);
			
			ContactDto result = contactService.getContactById(1L);
			
			assertThat(result).isNotNull();
			assertThat(result.firstName()).isEqualTo("test");
			assertThat(result.lastName()).isEqualTo("test");
			
			verify(contactRepository, times(1)).findById(1L);
		}
		
		@Test
		@DisplayName("shouldFindWithFilters")
		void shouldFindWithFilters() {
			Pageable pageable = PageRequest.of(0, 10);
			Page<Contact> page = new PageImpl<Contact>(List.of(contact));
			
			when(contactRepository.findWithFilters("test", "test", null, null, null, pageable)).thenReturn(page);
			
			Page<Contact> result = contactService.findWithFilters("test", "test", null, null, null, pageable);
			
			assertThat(result.getContent()).hasSize(1);
			verify(contactRepository, times(1)).findWithFilters("test", "test", null, null, null, pageable);
			
		}
	}
	
	@Nested
	@DisplayName("Save Test")
	class SaveTests {
		
		@Test
		@DisplayName("shouldCreateContact")
		void shouldCreateContact() {
			
			when(contactRepository.save(any(Contact.class))).thenReturn(contact);
			when(contactMapper.toDto(contact)).thenReturn(contactDto);
			
			ContactDto result = contactService.createContact("test", "test", "0612345789", "0123456789", "test@test.fr", null, null, null, null, null);
			
			assertThat(result).isEqualTo(contactDto);
			verify(contactRepository).save(any(Contact.class));
		}
		
		@Test
		@DisplayName("shouldCreateContact")
		void shouldUpdateContact() {
			
			when(contactRepository.findById(1L)).thenReturn(Optional.of(contact));
			when(contactRepository.save(any(Contact.class))).thenReturn(contact);
			when(contactMapper.toDto(contact)).thenReturn(contactDto);
			
			ContactDto result = contactService.updateContact(1L,"test", "test", "0612345789", "0123456789", "test@test.fr", null, null, null, null, null);
			
			assertThat(result).isEqualTo(contactDto);
			verify(contactRepository).save(any(Contact.class));
		}
	}
	
	@Nested
	@DisplayName("Remove tests")
	class RemoveTests {
		
		@Test
		@DisplayName("shouldRemoveContact")
		void shouldRemoveContact() {
			contactService.deleteContact(1L);
			verify(contactRepository,times(1)).deleteById(1L);
		}
	}
}
