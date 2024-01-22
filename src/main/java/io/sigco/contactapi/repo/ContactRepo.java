package io.sigco.contactapi.repo;

import io.sigco.contactapi.domain.Contact;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

// Repositorio de datos para la entidad Contact
@Repository
public interface ContactRepo extends JpaRepository<Contact, String> {

    // Método para buscar un contacto por su ID
    Optional<Contact> findById(String id);

    // Método para buscar contactos por título, ignorando mayúsculas y minúsculas
    Page<Contact> findByTitleContainingIgnoreCase(String title, Pageable pageable);
}
