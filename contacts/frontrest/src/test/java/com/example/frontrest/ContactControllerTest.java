package com.example.frontrest;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.example.contact.dto.ContactDto;
import com.example.contact.mapper.ContactMapper;
import com.example.contact.model.Contact;
import com.example.contact.service.ContactService;
import com.example.frontrest.controller.ContactController;
import com.example.frontrest.model.ContactRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
@WebMvcTest(ContactController.class)
public class ContactControllerTest {
	
	@Autowired
	private MockMvc mockMvc;
	
	private final ObjectMapper objectMapper = new ObjectMapper();
	
	@MockitoBean
	private ContactService contactService;
	
	@MockitoBean
	private ContactMapper contactMapper;
	
	private Contact contact;
	private ContactDto contactDto;
	private ContactRequest contactRequest;
	
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
		
		contactRequest = new ContactRequest("test", "test", "0123456789", "0612345789", "test@test.fr", null, null, null, null, null);
	}
	
	@Test
	@DisplayName("Get /api/contacts - success")
	void getAllContact_success() throws Exception {
		
		Page<Contact> page = new PageImpl<Contact>(List.of(contact));
		when(contactService.findWithFilters(eq("test"),eq("test"), eq("0612345789"), eq("0123456789"), eq("test@test.fr"), any(Pageable.class))).thenReturn(page);
		when(contactMapper.toDtoList(any())).thenReturn(List.of(contactDto));
		
		mockMvc.perform(get("/api/contacts")
                .param("page", "0")
                .param("size", "10")
                .param("sortBy", "id")
                .param("direction", "asc")
                .param("firstName", "test")
                .param("lastName", "test")
                .param("phoneMobile", "0612345789")
                .param("phoneHome", "0123456789")
                .param("email", "test@test.fr")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content[0].id").value(contactDto.id()))
                .andExpect(jsonPath("$.page").value(0))
                .andExpect(jsonPath("$.size").value(1))
                .andExpect(jsonPath("$.totalElements").value(1))
                .andExpect(jsonPath("$.totalPages").value(1))
                .andExpect(jsonPath("$.last").value(true));
		
		verify(contactService).findWithFilters(eq("test"),eq("test"), eq("0612345789"), eq("0123456789"), eq("test@test.fr"), any(Pageable.class));
	}
	@Test
	@DisplayName("Get /api/contacts/{id} - success")
	void getContactById_success() throws Exception {
		Long contactId = 1L;
		when(contactService.getContactById(contactId)).thenReturn(contactDto);
		
		mockMvc.perform(get("/api/contacts/{id}", contactId))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.id").value(contactDto.id()))
			.andExpect(jsonPath("$.firstName").value(contactDto.firstName()));
		
		verify(contactService).getContactById(contactId);
	}
	
	@Test
	@DisplayName("Post /api/contacts - success")
	void createContact_success() throws Exception {
		
		when(contactService.createContact(contactRequest.firstName(), contactRequest.lastName(), contactRequest.phoneMobile(), contactRequest.phoneHome(), contactRequest.email(), contactRequest.street(), contactRequest.street2(), contactRequest.postalCode(), contactRequest.city(),contactRequest.country())).thenReturn(contactDto);
		mockMvc.perform(post("/api/contacts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(contactRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(contactDto.id()))
                .andExpect(jsonPath("$.firstName").value(contactDto.firstName()));

        verify(contactService).createContact(contactRequest.firstName(), contactRequest.lastName(), contactRequest.phoneMobile(), contactRequest.phoneHome(), contactRequest.email(), contactRequest.street(), contactRequest.street2(), contactRequest.postalCode(), contactRequest.city(),contactRequest.country());
		
	}
	@Test
	@DisplayName("Put /api/notes/{id} - success")
	void updateContact_success() throws Exception {
		
		Long contactId = 1L;
        when(contactService.updateContact(contactId,contactRequest.firstName(), contactRequest.lastName(), contactRequest.phoneMobile(), contactRequest.phoneHome(), contactRequest.email(), contactRequest.street(), contactRequest.street2(), contactRequest.postalCode(), contactRequest.city(),contactRequest.country())).thenReturn(contactDto);

        mockMvc.perform(put("/api/contacts/{id}", contactId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(contactRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(contactDto.id()))
                .andExpect(jsonPath("$.firstName").value(contactDto.firstName()));

        verify(contactService).updateContact(contactId,contactRequest.firstName(), contactRequest.lastName(), contactRequest.phoneMobile(), contactRequest.phoneHome(), contactRequest.email(), contactRequest.street(), contactRequest.street2(), contactRequest.postalCode(), contactRequest.city(),contactRequest.country());
	}
	
	@Test
	@DisplayName("Delete /api/contacts/{id} - success")
	void deleteContact_success() throws Exception{
		
		Long contactId = 1L;
		doNothing().when(contactService).deleteContact(contactId);
		
		mockMvc.perform(delete("/api/contacts/{id}",contactId)).andExpect(status().isNoContent());
		
		verify(contactService).deleteContact(contactId);
	
	}
	
}
