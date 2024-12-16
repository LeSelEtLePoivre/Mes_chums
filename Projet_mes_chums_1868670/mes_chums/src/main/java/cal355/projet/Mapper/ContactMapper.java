package cal355.projet.Mapper;
import cal355.projet.DTO.ContactDTO;
import cal355.projet.Modèles.Contact;

public class ContactMapper {
    public static ContactDTO toDTO(Contact contact) {
        return new ContactDTO(
            contact.getId_contact(),
            contact.getNom(),
            contact.getPrenom(),
            contact.isFavoris(),
            null // adresses
        );
    }

    public static Contact toEntity(ContactDTO contactDTO) {
        return new Contact(
            contactDTO.getId_contact(),
            contactDTO.getNom(),
            contactDTO.getPrenom(),
            contactDTO.isFavoris()
        );
    }
}