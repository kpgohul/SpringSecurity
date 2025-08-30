package com.gohul.springSecurity.contact;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ContactRepo extends JpaRepository<Contact,Long> {

    Contact findByContactId(Long id);

}
