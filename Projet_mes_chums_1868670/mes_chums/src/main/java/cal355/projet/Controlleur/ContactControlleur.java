package cal355.projet.Controlleur;

import cal355.projet.DTO.ContactDTO;
import cal355.projet.DTO.AdresseDTO;
import cal355.projet.Modèles.Adresse;
import cal355.projet.Mapper.ContactMapper;
import cal355.projet.Modèles.Contact;
import cal355.projet.Service.CacheService;
import cal355.projet.Service.ContactService;
import cal355.projet.Service.GeolocalisationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class ContactControlleur implements HttpHandler {
    private static final Logger LOGGER = Logger.getLogger(ContactControlleur.class.getName());
    private final ContactService contactService;
    private final GeolocalisationService geolocalisationService;
    private final CacheService cacheService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public ContactControlleur(ContactService contactService, GeolocalisationService geolocalisationService, CacheService cacheService) {
        this.contactService = contactService;
        this.geolocalisationService = geolocalisationService;
        this.cacheService = cacheService;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        String path = exchange.getRequestURI().getPath();
        LOGGER.info("Requête reçue : " + method + " " + path);

        try {
            switch (method) {
                case "GET":
                    handleGet(exchange, path);
                    break;
                case "POST":
                    handlePost(exchange, path);
                    break;
                case "PUT":
                    handlePut(exchange, path);
                    break;
                case "DELETE":
                    handleDelete(exchange, path);
                    break;
                default:
                    exchange.sendResponseHeaders(405, -1); // Method Not Allowed
                    break;
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Erreur lors du traitement de la requête", e);
            exchange.sendResponseHeaders(500, -1); // Internal Server Error
        }
    }

    private void handleGet(HttpExchange exchange, String path) throws IOException {
        if (path.equals("/contact")) {
            List<Contact> contacts = contactService.trouverTous();
            List<ContactDTO> contactDTOs = contacts.stream().map(ContactMapper::toDTO).collect(Collectors.toList());
            String response = objectMapper.writeValueAsString(contactDTOs);
            sendResponse(exchange, response, 200);
        } else if (path.startsWith("/contact/")) {
            Integer id = Integer.parseInt(path.split("/")[2]);
            Contact contact = contactService.trouverParId(id);
            if (contact != null) {
                ContactDTO contactDTO = ContactMapper.toDTO(contact);
                String response = objectMapper.writeValueAsString(contactDTO);
                sendResponse(exchange, response, 200);
            } else {
                exchange.sendResponseHeaders(404, -1); // Not Found
            }
        }
    }

    private void handlePost(HttpExchange exchange, String path) throws IOException {
        if (path.equals("/contact")) {
            ContactDTO contactDTO = objectMapper.readValue(exchange.getRequestBody(), ContactDTO.class);
            Contact contact = ContactMapper.toEntity(contactDTO);
            contactService.ajouterContact(contact.getId_contact(), contact.getNom(), contact.getPrenom(), contact.isFavoris());
            exchange.sendResponseHeaders(201, -1); // Created
        } else if (path.startsWith("/contact/") && path.endsWith("/adresse")) {
            Integer id = Integer.parseInt(path.split("/")[2]);
            AdresseDTO adresseDTO = objectMapper.readValue(exchange.getRequestBody(), AdresseDTO.class);
            Adresse adresse = new Adresse(
                null, adresseDTO.getRue(), adresseDTO.getVille(), adresseDTO.getCodePostal(), adresseDTO.getPays(), adresseDTO.getCoordonnees(), id
            );
            contactService.ajouterAdresse(id, adresse);
            exchange.sendResponseHeaders(201, -1); // Created
        }
    }

    private void handlePut(HttpExchange exchange, String path) throws IOException {
        if (path.startsWith("/contact/")) {
            Integer id = Integer.parseInt(path.split("/")[2]);
            ContactDTO contactDTO = objectMapper.readValue(exchange.getRequestBody(), ContactDTO.class);
            Contact contact = ContactMapper.toEntity(contactDTO);
            contact.setId_contact(id);
            contactService.marquerCommeFavori(id, contact.isFavoris());
            exchange.sendResponseHeaders(204, -1); // No Content
        }
    }

    private void handleDelete(HttpExchange exchange, String path) throws IOException {
        if (path.startsWith("/contact/")) {
            Integer id = Integer.parseInt(path.split("/")[2]);
            Contact contact = contactService.trouverParId(id);
            if (contact != null) {
                contactService.supprimerContact(contact);
                exchange.sendResponseHeaders(204, -1); // No Content
            } else {
                exchange.sendResponseHeaders(404, -1); // Not Found
            }
        }
    }

    private void sendResponse(HttpExchange exchange, String response, int statusCode) throws IOException {
        exchange.getResponseHeaders().set("Content-Type", "application/json; charset=UTF-8");
        exchange.sendResponseHeaders(statusCode, response.getBytes(StandardCharsets.UTF_8).length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(response.getBytes(StandardCharsets.UTF_8));
        }
    }
}