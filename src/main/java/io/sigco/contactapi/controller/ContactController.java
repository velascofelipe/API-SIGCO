package io.sigco.contactapi.controller;

import io.sigco.contactapi.domain.Contact;
import io.sigco.contactapi.service.ContactService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;

import static io.sigco.contactapi.constant.Constant.PHOTO_DIRECTORY;
import static org.springframework.http.MediaType.IMAGE_JPEG_VALUE;
import static org.springframework.http.MediaType.IMAGE_PNG_VALUE;


// Anotación que marca la clase como un controlador REST de Spring
@RestController
@RequestMapping("/contacts")
@RequiredArgsConstructor
public class ContactController {

    // Inyección de dependencia del servicio de contactos
    private final ContactService contactService;

    // Método para crear un nuevo contacto
    @PostMapping
    public ResponseEntity<Contact> createContact(@RequestBody Contact contact) {
        // Se crea el contacto y se devuelve una respuesta con el código de estado 201 (Created)
        // y la URI del nuevo recurso creado
        return ResponseEntity.created(URI.create("/contacts/userID")).body(contactService.createContact(contact));
    }

    // Método para obtener todos los contactos paginados y, opcionalmente, filtrados por título
    @GetMapping
    public ResponseEntity<Page<Contact>> getContacts(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "title", defaultValue = "") String title) {
        return ResponseEntity.ok().body(contactService.getAllContacts(page, size, title));
    }

    // Método para obtener un contacto por su ID
    @GetMapping("/{id}")
    public ResponseEntity<Contact> getContact(@PathVariable(value = "id") String id) {
        return ResponseEntity.ok().body(contactService.getContact(id));
    }

    // Método para eliminar un contacto por su ID
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteContact(@PathVariable String id) {
        try {
            contactService.deleteContact(id);
            return new ResponseEntity<>("Contacto eliminado exitosamente", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Error al eliminar el contacto: " + e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Método para cargar una foto a un contacto
    @PutMapping("/photo")
    public ResponseEntity<String> uploadPhoto(@RequestParam("id") String id, @RequestParam("file") MultipartFile file) {
        return ResponseEntity.ok().body(contactService.uploadPhoto(id, file));
    }

    // Método para obtener la foto de un contacto por el nombre del archivo
    @GetMapping(path = "/image/{filename}", produces = {IMAGE_PNG_VALUE, IMAGE_JPEG_VALUE})
    public byte[] getPhoto(@PathVariable("filename") String filename) throws IOException {
        return Files.readAllBytes(Paths.get(PHOTO_DIRECTORY + filename));
    }

    // Método para actualizar un contacto por su ID
    @PutMapping("/{id}")
    public ResponseEntity<?> updateContact(@PathVariable(value = "id") String id,
                                           @RequestBody Contact updatedContact) {
        try {
            // Validar la entrada
            if (id == null || updatedContact == null) {
                return ResponseEntity.badRequest().body("ID del contacto o datos actualizados no válidos");
            }

            // Actualizar el contacto y devolver una respuesta exitosa
            Contact updated = contactService.updateContact(id, updatedContact);
            return ResponseEntity.ok().body("Datos actualizado correctamente!");
        } catch (DataIntegrityViolationException e) {
            // Manejar errores de integridad de datos
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Error de integridad de datos al actualizar el contacto: " + e.getMessage());
        } catch (Exception e) {
            // Manejar otros errores
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al actualizar el contacto: " + e.getMessage());
        }
    }
}
