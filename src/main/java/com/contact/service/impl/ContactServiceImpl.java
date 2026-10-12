
package com.contact.service.impl;

import java.util.List;
 

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.contact.dto.request.CreateContactRequest;
import com.contact.dto.request.UpdateContactRequest;
import com.contact.dto.response.ContactResponse;
import com.contact.entity.Contact;
import com.contact.exception.ContactNotFoundException;
import com.contact.exception.DuplicateContactException;
import com.contact.mapper.ContactMapper;
import com.contact.repository.ContactRepository;
import com.contact.service.ContactService;

@Service
public class ContactServiceImpl implements ContactService {

    private final ContactRepository contactRepository;
    private final ContactMapper contactMapper;

    public ContactServiceImpl(
            ContactRepository contactRepository,
            ContactMapper contactMapper) {
        this.contactRepository = contactRepository;
        this.contactMapper = contactMapper;
    }

    @Override
    @Transactional
    public ContactResponse createContact(CreateContactRequest request) {

        String email = request.getEmail().trim().toLowerCase();

        if (contactRepository.existsByEmailIgnoreCase(email)) {
            throw new DuplicateContactException(
                    "A contact with this email already exists");
        }

        Contact contact = contactMapper.toEntity(request);

        try {
            Contact savedContact = contactRepository.saveAndFlush(contact);
            return contactMapper.toResponse(savedContact);
        } catch (DataIntegrityViolationException ex) {
            // The database unique constraint also protects against
            // concurrent requests attempting the same email.
            throw new DuplicateContactException(
                    "A contact with this email already exists");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<ContactResponse> getAllContacts() {
        return contactRepository.findAll()
                .stream()
                .map(contactMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public ContactResponse getContactById(Long id) {
        return contactRepository.findById(id)
                .map(contactMapper::toResponse)
                .orElseThrow(() ->
                        new ContactNotFoundException(
                                "Contact not found with ID: " + id));
    }

@Override
@Transactional
public ContactResponse updateContact(
        Long id, UpdateContactRequest request) {

    Contact contact = contactRepository.findById(id)
            .orElseThrow(() ->
                    new ContactNotFoundException(
                            "Contact not found with ID: " + id));

    String email = request.getEmail().trim().toLowerCase();

    contactRepository.findByEmailIgnoreCase(email)
            .filter(existing -> !existing.getId().equals(id))
            .ifPresent(existing -> {
                throw new DuplicateContactException(
                        "A contact with this email already exists");
            });

    contact.setFirstName(request.getFirstName().trim());
    contact.setLastName(request.getLastName().trim());
    contact.setEmail(email);
    contact.setPhoneNumber(request.getPhoneNumber().trim());
    contact.setCompany(
            request.getCompany() == null
                    ? null
                    : request.getCompany().trim());

    Contact updatedContact = contactRepository.saveAndFlush(contact);

    return contactMapper.toResponse(updatedContact);
}

@Override
@Transactional
public void deleteContact(Long id) {
    Contact contact = contactRepository.findById(id)
            .orElseThrow(() -> new ContactNotFoundException(
                    "Contact not found with ID: " + id));

    contactRepository.delete(contact);
}@Override
@Transactional(readOnly = true)
public Page<ContactResponse> getAllContacts(Pageable pageable) {
    return contactRepository.findAll(pageable)
            .map(contactMapper::toResponse);
    
}
@Override
@Transactional(readOnly = true)
public Page<ContactResponse> searchContacts(
        String keyword, Pageable pageable) {

    return contactRepository
            .searchContacts(keyword.trim(), pageable)
            .map(contactMapper::toResponse);
}

}
