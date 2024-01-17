package io.sigco.contactapi.repo;

import io.sigco.contactapi.domain.Contact;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface ContactRepo extends JpaRepository<Contact, String> {
    Optional<Contact> findById(String id);

    Page<Contact> findByTitleContainingIgnoreCase(String title, Pageable pageable);


}

