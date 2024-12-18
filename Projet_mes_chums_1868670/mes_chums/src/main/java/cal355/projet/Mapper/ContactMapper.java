package cal355.projet.Mapper;

import java.util.List;
import java.util.stream.Collectors;

import cal355.projet.DTO.AdresseDTO;
import cal355.projet.DTO.ContactDTO;
import cal355.projet.Modèles.Adresse;
import cal355.projet.Modèles.Contact;

public class ContactMapper {
    
    // Méthode qui transforme un Contact en ContactDTO
    public static ContactDTO toDTO(Contact contact) {
        if (contact == null) {
            return null;
        }
        
        ContactDTO dto = new ContactDTO();
        dto.setId_contact(contact.getId_contact());
        dto.setNom(contact.getNom());
        dto.setPrenom(contact.getPrenom());
        dto.setFavoris(contact.isFavoris());

        if (contact.getAdresses() != null) {
            List<AdresseDTO> adressesDTO = contact.getAdresses().stream()
                .map(AdresseMapper::toDTO)
                .collect(Collectors.toList());
            dto.setAdresses(adressesDTO);
        }
        return dto;
    }
    // Méthode qui transforme un ContactDTO en Contact
    public static Contact toEntiter(ContactDTO dto) {
        if (dto == null) {
            return null;
        }

        Contact contact = new Contact();
        contact.setId_contact(dto.getId_contact());
        contact.setNom(dto.getNom());
        contact.setPrenom(dto.getPrenom());
        contact.setFavoris(dto.isFavoris());

        if (dto.getAdresses() != null) {
            List<Adresse> adresses = dto.getAdresses().stream()
                .map(AdresseMapper::toEntiter) 
                .collect(Collectors.toList());
            contact.setAdresses(adresses);
        }

        return contact;
    }
}
