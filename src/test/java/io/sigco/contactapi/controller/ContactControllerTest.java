package io.sigco.contactapi.controller;

import io.sigco.contactapi.domain.Contact;
import io.sigco.contactapi.repo.ContactRepo;
import io.sigco.contactapi.service.ContactService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class ContactServiceTest {

    @Mock
    private ContactRepo contactRepo;

    @InjectMocks
    private ContactService contactService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testObtenerTodosLosContactos() {
        // Mocking
        List<Contact> contactos = new ArrayList<>();
        contactos.add(new Contact("1", "Juan Pérez", "juan@example.com", "Desarrollador", "123456", "Dirección", "Activo", null));
        contactos.add(new Contact("2", "Ana Gómez", "ana@example.com", "Tester", "654321", "Otra Dirección", "Inactivo", null));
        Page<Contact> paginaContacto= new PageImpl<>(contactos);
        when(contactRepo.findAll(any(PageRequest.class))).thenReturn(paginaContacto);

        // Testing
        Page<Contact> resultado = contactService.getAllContacts(0, 10, "");
        assertEquals(contactos.size(), resultado.getContent().size());
    }

    @Test
    void testObtenerContacto() {
        // Mocking
        when(contactRepo.findById("1")).thenReturn(Optional.of(new Contact("1", "Juan Pérez", "juan@example.com", "Desarrollador", "123456", "Dirección", "Activo", null)));

        // Testing
        Contact resultado = contactService.getContact("1");
        assertEquals("Juan Pérez", resultado.getName());
    }

    @Test
    void testCreateContact() {
        // Mocking
        Contact newContact = new Contact("3", "New User", "newuser@example.com", "Tester", "987654", "New Address", "Active", null);
        when(contactRepo.save(any(Contact.class))).thenReturn(newContact);

        // Testing
        Contact result = contactService.createContact(newContact);
        assertEquals(newContact.getId(), result.getId());
    }


    @Test
    void testDeleteContact() {
        // Testing
        contactService.deleteContact("1");

        // Verification
        verify(contactRepo, times(1)).deleteById("1");
    }

    @Test
    void testUploadPhotoContactNotFound() {
        // Mocking
        MultipartFile file = mock(MultipartFile.class);
        when(contactRepo.findById("1")).thenReturn(Optional.empty());

        // Testing
        assertThrows(RuntimeException.class, () -> contactService.uploadPhoto("1", file));
    }

    @Test
    void testUpdateContactSuccess() {
        // Mocking
        Contact existingContact = new Contact("1", "John Doe", "john@example.com", "Developer", "123456", "Address", "Active", null);
        when(contactRepo.findById("1")).thenReturn(Optional.of(existingContact));
        when(contactRepo.save(any(Contact.class))).thenReturn(existingContact);

        // Testing
        Contact updatedContact = new Contact("1", "Updated Doe", "updated@example.com", "Updated Developer", "654321", "Updated Address", "Inactive", null);
        Contact result = contactService.updateContact("1", updatedContact);

        // Verification
        verify(contactRepo, times(1)).save(any(Contact.class));
        assertEquals("Updated Doe", result.getName());
    }

    @Test
    void testUploadPhotoException() {
        // Mocking
        MultipartFile file = mock(MultipartFile.class);
        when(contactRepo.findById("1")).thenReturn(Optional.of(new Contact("1", "John Doe", "john@example.com", "Developer", "123456", "Address", "Active", null)));
        doThrow(new RuntimeException("Unable to save image")).when(contactRepo).save(any(Contact.class));

        // Testing
        assertThrows(RuntimeException.class, () -> contactService.uploadPhoto("1", file));
    }



    @Test
    void testIncorrectUploadPhoto() {
        // Mocking
        MultipartFile file = mock(MultipartFile.class);
        when(contactRepo.findById("1")).thenReturn(Optional.empty());  // Devuelve Optional.empty() en lugar de un Contact existente

        // Testing
        // Esto debería fallar porque el contacto no existe, pero la prueba podría pasar si no se maneja correctamente en el servicio.
        assertThrows(RuntimeException.class, () -> contactService.uploadPhoto("1", file));
    }


}
