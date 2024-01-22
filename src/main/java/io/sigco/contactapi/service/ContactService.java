package io.sigco.contactapi.service;

import io.sigco.contactapi.domain.Contact;
import io.sigco.contactapi.repo.ContactRepo;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;

import static io.sigco.contactapi.constant.Constant.PHOTO_DIRECTORY;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

// Anotación que marca la clase como un servicio de Spring
@Service
@Slf4j
@Transactional(rollbackOn = Exception.class)
@RequiredArgsConstructor
public class ContactService {

    // Inyección de dependencia del repositorio de contactos
    private final ContactRepo contactRepo;

    // Método para obtener todos los contactos paginados y filtrados por título
    public Page<Contact> getAllContacts(int page, int size, String titleFilter) {
        if (StringUtils.hasText(titleFilter)) {
            // Si hay un filtro de título, realiza la búsqueda filtrada
            return contactRepo.findByTitleContainingIgnoreCase(titleFilter, PageRequest.of(page, size,
                    Sort.by("name")));
        } else {
            // Si no hay filtro de título, obtén todos los contactos
            return contactRepo.findAll(PageRequest.of(page, size, Sort.by("name")));
        }
    }

    // Método para obtener un contacto por su ID
    public Contact getContact(String id) {
        return contactRepo.findById(id).orElseThrow(() -> new RuntimeException("Contact not found"));
    }

    // Método para crear un nuevo contacto
    public Contact createContact(Contact contact) {
        return contactRepo.save(contact);
    }

    // Método para eliminar un contacto por su ID
    public void deleteContact(String id) {
        log.info("Eliminando contacto con ID: {}", id);
        contactRepo.deleteById(id);
        log.info("Contacto eliminado exitosamente");
    }

    // Método para cargar una foto y actualizar la URL de la foto en un contacto
    public String uploadPhoto(String id, MultipartFile file) {
        log.info("Saving picture for user ID: {}", id);
        Contact contact = getContact(id);
        String photoUrl = photoFunction.apply(id, file);
        contact.setPhotoUrl(photoUrl);
        contactRepo.save(contact);
        return photoUrl;
    }

    // Función para obtener la extensión del archivo a partir del nombre del archivo
    private final Function<String, String> fileExtension =
            filename -> Optional.of(filename).filter(name -> name.contains("."))
                    .map(name -> "." + name.substring(filename.lastIndexOf(".") + 1)).orElse(".png");

    // Función para procesar la carga de una foto y devolver la URL de la foto
    private final BiFunction<String, MultipartFile, String> photoFunction = (id, image) -> {
        String filename = id + fileExtension.apply(image.getOriginalFilename());
        try {
            Path fileStorageLocation = Paths.get(PHOTO_DIRECTORY).toAbsolutePath().normalize();
            if (!Files.exists(fileStorageLocation)) {
                Files.createDirectories(fileStorageLocation);
            }
            Files.copy(image.getInputStream(), fileStorageLocation.resolve(filename), REPLACE_EXISTING);
            return ServletUriComponentsBuilder
                    .fromCurrentContextPath()
                    .path("/contacts/image/" + filename).toUriString();
        } catch (Exception exception) {
            throw new RuntimeException("Unable to save image");
        }
    };

    // Método para actualizar un contacto
    public Contact updateContact(String id, Contact updatedContact) {
        Contact existingContact = getContact(id);
        existingContact.setName(updatedContact.getName());
        existingContact.setEmail(updatedContact.getEmail());
        existingContact.setTitle(updatedContact.getTitle());
        existingContact.setPhone(updatedContact.getPhone());
        existingContact.setAddress(updatedContact.getAddress());
        existingContact.setStatus(updatedContact.getStatus());

        return contactRepo.save(existingContact);
    }
}
