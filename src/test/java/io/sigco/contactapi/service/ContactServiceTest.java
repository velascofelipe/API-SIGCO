package io.sigco.contactapi.service;

import io.sigco.contactapi.domain.Contact;
import lombok.Data;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;



public class ContactServiceTest {

    @Test
    void getContact() {
        ContactService contactServiceMock = Mockito.mock(ContactService.class);

        // Definir un contacto esperado con ciertos atributos
        Contact expectedContact = new Contact();
        expectedContact.setId("12ce6b61-4e9d-45d8-adee-ecbda68c66e5");
        expectedContact.setName("rtyrtyrtyrty");
        expectedContact.setEmail("smyxone@gmail.com");
        // Puedes definir otros atributos según tu lógica

        // Configurar el mock para devolver el contacto esperado
        Mockito.when(contactServiceMock.getContact(expectedContact.getId())).thenReturn(expectedContact);

        // Llamar al método getContact del mock
        Contact actualContact = contactServiceMock.getContact(expectedContact.getId());

        // Verificar si el contacto obtenido es igual al esperado
        assertEquals(expectedContact, actualContact);

        // Verificar que el método getContact se llamó una vez
        Mockito.verify(contactServiceMock, Mockito.times(1)).getContact(expectedContact.getId());
    }

    @Test
    void createContact() {
        // Crear un mock del servicio
        ContactService contactServiceMock = Mockito.mock(ContactService.class);

        // Crear un nuevo contacto que se espera crear
        Contact newContact = new Contact();
        newContact.setName("Jane Doe");
        newContact.setEmail("jane.doe@example.com");
        newContact.setTitle("test");
        newContact.setAddress("test");
        // Puedes definir otros atributos según tu lógica

        // Configurar el mock para devolver el nuevo contacto cuando se cree
        Mockito.when(contactServiceMock.createContact(newContact)).thenReturn(newContact);

        // Llamar al método createContact del mock
        Contact createdContact = contactServiceMock.createContact(newContact);

        // Verificar si el contacto creado es igual al esperado
        assertEquals(newContact, createdContact);

        // Verificar que el método createContact se llamó una vez con el nuevo contacto como argumento
        Mockito.verify(contactServiceMock, Mockito.times(1)).createContact(newContact);
    }
}
