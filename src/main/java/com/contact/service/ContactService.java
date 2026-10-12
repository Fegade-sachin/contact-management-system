
package com.contact.service;

import java.util.List;

import com.contact.dto.request.CreateContactRequest;
import com.contact.dto.request.UpdateContactRequest;
import com.contact.dto.response.ContactResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
public interface ContactService {

    ContactResponse createContact(CreateContactRequest request);

    List<ContactResponse> getAllContacts();

    ContactResponse getContactById(Long id);
    
    ContactResponse updateContact(Long id, UpdateContactRequest request);

    void deleteContact(Long id);
    Page<ContactResponse> getAllContacts(Pageable pageable);
    Page<ContactResponse> searchContacts(
            String keyword, Pageable pageable);
}
