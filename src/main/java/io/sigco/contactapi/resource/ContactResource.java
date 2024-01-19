package io.sigco.contactapi.resource;

import io.sigco.contactapi.domain.Contact;
import io.sigco.contactapi.domain.LoginRequest;
import io.sigco.contactapi.service.AuthService;
import io.sigco.contactapi.service.ContactService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
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


@RestController
@RequestMapping("/contacts")
@RequiredArgsConstructor
public class ContactResource {
    private final ContactService contactService;
    private final AuthService authService;


    @PostMapping
    public ResponseEntity<Contact> createContact(@RequestBody Contact contact) {
        //return ResponseEntity.ok().body(contactService.createContact(contact));
        return ResponseEntity.created(URI.create("/contacts/userID")).body(contactService.createContact(contact));
    }

    @GetMapping
    public ResponseEntity<Page<Contact>> getContacts(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "title", defaultValue = "") String title) {
        return ResponseEntity.ok().body(contactService.getAllContacts(page, size, title));
    }


    @GetMapping("/{id}")
    public ResponseEntity<Contact> getContact(@PathVariable(value = "id") String id) {
        return ResponseEntity.ok().body(contactService.getContact(id));
    }

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

    @PutMapping("/photo")
    public ResponseEntity<String> uploadPhoto(@RequestParam("id") String id, @RequestParam("file") MultipartFile file) {
        return ResponseEntity.ok().body(contactService.uploadPhoto(id, file));
    }


    @GetMapping(path = "/image/{filename}", produces = {IMAGE_PNG_VALUE, IMAGE_JPEG_VALUE})
    public byte[] getPhoto(@PathVariable("filename") String filename) throws IOException {
        return Files.readAllBytes(Paths.get(PHOTO_DIRECTORY + filename));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateContact(@PathVariable(value = "id") String id,
                                           @RequestBody Contact updatedContact) {
        try {
            // Validar la entrada
            if (id == null || updatedContact == null) {
                return ResponseEntity.badRequest().body("ID del contacto o datos actualizados no válidos");
            }

            Contact updated = contactService.updateContact(id, updatedContact);
            return ResponseEntity.ok().body("Datos actualizado correctamente!" );
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Error de integridad de datos al actualizar el contacto: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al actualizar el contacto: " + e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest loginRequest) {
        try {
            // Validar las credenciales (usando tu lógica existente)
            String username = loginRequest.getUsername();
            String password = loginRequest.getPassword();

            // Aquí deberías validar las credenciales con tu lógica existente
            // Puedes usar un servicio dedicado de autenticación si es más complejo

            // Si las credenciales son válidas, genera un token JWT usando la instancia de AuthService
            String token = authService.generateToken(username);

            // Devuelve el token como respuesta
            return ResponseEntity.ok().body(token);
        } catch (Exception e) {
            // Maneja cualquier error durante el proceso de inicio de sesión
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Error durante el inicio de sesión: " + e.getMessage());
        }
    }
}