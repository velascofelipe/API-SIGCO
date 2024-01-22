package io.sigco.contactapi.resource;

import io.sigco.contactapi.domain.Contact;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ContactTest {

    @Test
    void testContactProperties() {
        // Crear un objeto Contact de prueba
        Contact contact = new Contact("123", "John Doe", "john@example.com", "Developer",
                "123456789", "123 Main St", "Active", "http://example.com/photo");

        // Asegurarse de que las propiedades se establezcan correctamente
        assertThat(contact.getId()).isEqualTo("123");
        assertThat(contact.getName()).isEqualTo("John Doe");
        assertThat(contact.getEmail()).isEqualTo("john@example.com");
        assertThat(contact.getTitle()).isEqualTo("Developer");
        assertThat(contact.getPhone()).isEqualTo("123456789");
        assertThat(contact.getAddress()).isEqualTo("123 Main St");
        assertThat(contact.getStatus()).isEqualTo("Active");
        assertThat(contact.getPhotoUrl()).isEqualTo("http://example.com/photo");
    }

    @Test
    void testContactToString() {
        // Crear un objeto Contact de prueba
        Contact contact = new Contact("123", "John Doe", "john@example.com", "Developer",
                "123456789", "123 Main St", "Active", "http://example.com/photo");

        // Verificar que el método toString funcione correctamente
        assertThat(contact.toString()).contains("John Doe", "john@example.com", "Developer");
    }
}
