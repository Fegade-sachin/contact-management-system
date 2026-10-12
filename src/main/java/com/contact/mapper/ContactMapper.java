
package com.contact.mapper;

import com.contact.dto.request.CreateContactRequest;
import com.contact.dto.response.ContactResponse;
import com.contact.entity.Contact;
import org.springframework.stereotype.Component;

@Component
public class ContactMapper {

    public Contact toEntity(CreateContactRequest request) {
        Contact contact = new Contact();

        contact.setFirstName(request.getFirstName().trim());
        contact.setLastName(request.getLastName().trim());
        contact.setEmail(request.getEmail().trim().toLowerCase());
        contact.setPhoneNumber(request.getPhoneNumber().trim());
        contact.setCompany(
            request.getCompany() == null
                ? null
                : request.getCompany().trim()
        );

        return contact;
    }

    public ContactResponse toResponse(Contact contact) {
        ContactResponse response = new ContactResponse();

        response.setId(contact.getId());
        response.setFirstName(contact.getFirstName());
        response.setLastName(contact.getLastName());
        response.setEmail(contact.getEmail());
        response.setPhoneNumber(contact.getPhoneNumber());
        response.setCompany(contact.getCompany());
        response.setCreatedAt(contact.getCreatedAt());
        response.setUpdatedAt(contact.getUpdatedAt());

        return response;
    }
}
