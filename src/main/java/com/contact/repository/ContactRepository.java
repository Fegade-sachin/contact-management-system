
package com.contact.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.contact.entity.Contact;

public interface ContactRepository
        extends JpaRepository<Contact, Long> {

    boolean existsByEmailIgnoreCase(String email);
    Optional<Contact> findByEmailIgnoreCase(String email);
    @Query("""
    	    SELECT c FROM Contact c
    	    WHERE LOWER(c.firstName) LIKE LOWER(CONCAT('%', :keyword, '%'))
    	       OR LOWER(c.lastName) LIKE LOWER(CONCAT('%', :keyword, '%'))
    	       OR LOWER(c.email) LIKE LOWER(CONCAT('%', :keyword, '%'))
    	       OR LOWER(c.company) LIKE LOWER(CONCAT('%', :keyword, '%'))
    	    """)
    	Page<Contact> searchContacts(
    	        @Param("keyword") String keyword,
    	        Pageable pageable);
}
