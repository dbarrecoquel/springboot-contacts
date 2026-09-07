package com.example.contact.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "contact")
@Getter
@Setter
public class Contact {
	
	@Id
	@GeneratedValue(strategy =  GenerationType.IDENTITY)
	private Long id;
	
	@NotBlank
	@Column(name = "firstname", nullable = false)
	private String firstName;
	
	@NotBlank
	@Column(name = "lastname", nullable = false)
	private String lastName;
	
	@Column(name = "phonehome")
	@Pattern(
		    regexp = "^\\+?[0-9\\s\\-\\.\\(\\)]{7,20}$", 
		    message = "Le numéro de téléphone domicile n'est pas valide"
	)
	private String phoneHome;
	
	@Column(name = "phonemobile")
	@Pattern(
		    regexp = "^\\+?[0-9\\s\\-\\.\\(\\)]{7,20}$", 
		    message = "Le numéro de téléphone mobile n'est pas valide"
	)
	private String phoneMobile;
	
	@Column(name = "email")
	private String email;
	
	@Column(name = "street")
	private String street;
	
	@Column(name = "street2")
	private String street2;
	
	@Column(name = "postalcode")
	private Integer postalCode;
	
	@Column(name = "city")
	private String city;
	
	@Column(name = "country")
	private String country;
	
	@Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
