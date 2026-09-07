package com.example.contact;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import com.example.contact.model.Contact;
import com.example.contact.repository.ContactRepository;

@DataJpaTest
@Testcontainers
@ActiveProfiles("test")
@ContextConfiguration(classes = TestApplication.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Sql(scripts = "/clean-db.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@DisplayName("ContactRepository — Integration Tests")
public class ContactRepositoryTest {
	@Container
	static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16-alpine")
	    .withDatabaseName("contact_test")
	    .withUsername("testuser")
	    .withPassword("testpass")
	    .withReuse(true);
	
	@DynamicPropertySource
	static void overideDataSource(DynamicPropertyRegistry registry) {
		registry.add("spring.datasource.url", postgres::getJdbcUrl);
		registry.add("spring.datasource.username", postgres::getUsername);
		registry.add("spring.datasource.password", postgres::getPassword);
		registry.add("spring.datasource.hikari.auto-commit", () -> "false");
		registry.add("spring.jpa.hibernate.ddl-auto", () -> "create");
	}
	
	@Autowired ContactRepository contactRepository;
	@Autowired TestEntityManager em;
	
	private Contact contact;
	
	@BeforeEach
	void setUp() {
		
		contact = new Contact();
		contact.setFirstName("test");
		contact.setLastName("test");
		contact.setPhoneHome("0123456789");
		contact.setPhoneMobile("0612345789");
		contact.setEmail("test@test.fr");
		
		em.persistAndFlush(contact);
		em.clear();
	}
	
	@Nested
	@DisplayName("Recherches simples")
	class FindTest {
	
		@Test
		@DisplayName("findWithFilters_found")
		void findWithFilters_found() {
		
			Pageable pageable = PageRequest.of(0, 10);
			
			Page<Contact> contacts = contactRepository.findWithFilters("test", "test", null, null, null, pageable);
			
			assertThat(contacts).isNotNull();
			assertThat(contacts.getContent()).isNotEmpty();
			assertThat(contacts.getContent()).hasSize(1);
			
		}
		@Test
		@DisplayName("findWithFilters_notfound")
		void findWithFilters_notfound() {
		
			Pageable pageable = PageRequest.of(0, 10);
			
			Page<Contact> contacts = contactRepository.findWithFilters("test2", "test", null, null, null, pageable);
			
			assertThat(contacts.getContent()).isEmpty();
			
		}
	}
}
