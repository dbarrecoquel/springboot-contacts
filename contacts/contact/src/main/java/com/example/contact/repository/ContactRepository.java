package com.example.contact.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.contact.model.Contact;

public interface ContactRepository extends JpaRepository<Contact, Long> {
	
	@Query("""
            SELECT c FROM Contact c
            WHERE (:firstname IS NULL OR LOWER(c.firstName) LIKE LOWER(CONCAT('%', CAST(:firstname AS string), '%')))
              AND (:lastname IS NULL OR LOWER(c.lastName) LIKE LOWER(CONCAT('%', CAST(:lastname AS string), '%')))
              AND (:phonemobile IS NULL OR LOWER(c.phoneMobile) LIKE LOWER(CONCAT('%', CAST(:phonemobile AS string), '%')))
              AND (:phonehome IS NULL OR LOWER(c.phoneHome) LIKE LOWER(CONCAT('%', CAST(:phonehome AS string), '%')))
              AND (:email IS NULL OR LOWER(c.email) LIKE LOWER(CONCAT('%', CAST(:email AS string), '%')))
        """)
    Page<Contact> findWithFilters(
        @Param("firstname") String firstname,
        @Param("lastname") String lastname,
        @Param("phonemobile") String phonemobile,
        @Param("phonehome") String phonehome,
        @Param("email") String email,
        Pageable pageable
    );

}
