package io.sigco.contactapi.service;

import io.sigco.contactapi.domain.Contact;
import io.sigco.contactapi.repo.ContactRepo;
import io.sigco.contactapi.service.ContactService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.jdbc.Sql;
import static org.junit.jupiter.api.Assertions.assertEquals;
@DataJpaTest
public class ContactServiceTest {

    @Autowired
    private ContactRepo contactRepo;

    @Autowired
    private ContactService contactService;

    @Test
    public void testGetAllContacts() {
        // Insertar algunos datos de prueba en la base de datos
        Contact contact1 = new Contact("ID1", "Contacto1", "Correo1", "Titulo1", "Telefono1", "Direccion1", "Activo", "URL1");
        Contact contact2 = new Contact("ID2", "Contacto2", "Correo2", "Titulo2", "Telefono2", "Direccion2", "Activo", "URL2");
        contactRepo.save(contact1);
        contactRepo.save(contact2);

        // Llamar al método de servicio que quieres probar
        Page<Contact> resultPage = contactService.getAllContacts(0, 10, "Contacto");

        // Verificar que la página de resultados no sea nula
        assertEquals(2, resultPage.getTotalElements()); // Ajusta esto según tu caso
    }

}
