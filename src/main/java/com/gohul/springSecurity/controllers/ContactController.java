package com.gohul.springSecurity.controllers;


import com.gohul.springSecurity.contact.Contact;
import com.gohul.springSecurity.contact.ContactRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Random;

@RestController
@RequestMapping("contacts")
@RequiredArgsConstructor
public class ContactController {
    private final ContactRepo repo;

    @PostMapping("/contact")
    public Contact getMyAccount(@RequestBody Contact contact)
    {
        contact.setContactId(getServiceReqNumber());
        return repo.save(contact);
    }

    @GetMapping("/contact")
    public List<Contact> getContacts()
    {
        return repo.findAll();
    }


    public Long getServiceReqNumber() {
        Random random = new Random();
        return (long) (random.nextInt(999999999 - 9999) + 9999);
    }

}
