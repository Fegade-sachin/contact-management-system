
package com.contact.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.contact.dto.request.CreateContactRequest;
import com.contact.dto.request.UpdateContactRequest;
import com.contact.dto.response.ContactResponse;
import com.contact.service.ContactService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/contacts")
public class ContactController {

    private final ContactService contactService;

    public ContactController(ContactService contactService) {
        this.contactService = contactService;
    }

    @PostMapping
    public ResponseEntity<ContactResponse> createContact(
            @Valid @RequestBody CreateContactRequest request) {

        ContactResponse response =
                contactService.createContact(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

@GetMapping
public ResponseEntity<List<ContactResponse>> getAllContacts() {
    return ResponseEntity.ok(contactService.getAllContacts());
}


@PutMapping("/{id}")
public ResponseEntity<ContactResponse> updateContact(
        @PathVariable Long id,
        @Valid @RequestBody UpdateContactRequest request) {

    ContactResponse response =
            contactService.updateContact(id, request);

    return ResponseEntity.ok(response);
}
	
@DeleteMapping("/{id}")
public ResponseEntity<Void> deleteContact(@PathVariable Long id) {
    contactService.deleteContact(id);
    return ResponseEntity.noContent().build();
}
@GetMapping("/page")
public ResponseEntity<Page<ContactResponse>> getContactsPage(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size,
        @RequestParam(defaultValue = "id") String sortBy,
        @RequestParam(defaultValue = "asc") String direction) {

    if (page < 0 || size < 1 || size > 100) {
        throw new IllegalArgumentException(
                "Page must be >= 0 and size must be between 1 and 100");
    }

    Sort sort = direction.equalsIgnoreCase("desc")
            ? Sort.by(sortBy).descending()
            : Sort.by(sortBy).ascending();

    Pageable pageable = PageRequest.of(page, size, sort);

    return ResponseEntity.ok(contactService.getAllContacts(pageable));
}
@GetMapping("/search")
public ResponseEntity<Page<ContactResponse>> searchContacts(
        @RequestParam String keyword,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size,
        @RequestParam(defaultValue = "firstName") String sortBy,
        @RequestParam(defaultValue = "asc") String direction) {

    if (keyword.isBlank()) {
        throw new IllegalArgumentException(
                "Search keyword must not be blank");
    }

    if (page < 0 || size < 1 || size > 100) {
        throw new IllegalArgumentException(
                "Page must be >= 0 and size must be between 1 and 100");
    }

    Sort sort = direction.equalsIgnoreCase("desc")
            ? Sort.by(sortBy).descending()
            : Sort.by(sortBy).ascending();

    Pageable pageable = PageRequest.of(page, size, sort);

    return ResponseEntity.ok(
            contactService.searchContacts(keyword, pageable));
}
private String validateSortField(String sortBy) {
    List<String> allowedFields = List.of(
            "id", "firstName", "lastName",
            "email", "company", "createdAt");

    if (!allowedFields.contains(sortBy)) {
        throw new IllegalArgumentException(
                "Invalid sort field: " + sortBy);
    }

    return sortBy;
}
}
