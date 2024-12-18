package cal355.projet.Service;

import cal355.projet.DAO.ContactDAO;
import cal355.projet.DTO.AdresseDTO;
import cal355.projet.DTO.ContactDTO;
import cal355.projet.Mapper.AdresseMapper;
import cal355.projet.CalculateurDistance;
import cal355.projet.API.GeoCodageApi;
import cal355.projet.DAO.AdresseDAO;
import cal355.projet.Modèles.Adresse;
import cal355.projet.Modèles.Contact;
import cal355.projet.Modèles.Coordonnees;

import java.util.ArrayList;
import java.util.List;
import java.lang.System.Logger;
import java.lang.System.Logger.Level;

public class ContactService {
    private ContactDAO contactDAO;
    private AdresseDAO adresseDAO;
    private GeoCodageApi geocodageAPI;
    private static final Logger logger = System.getLogger(ContactService.class.getName());

    public ContactService(ContactDAO contactDAO, AdresseDAO adresseDAO, GeoCodageApi geocodageAPI) {
        this.contactDAO = contactDAO;
        this.adresseDAO = adresseDAO;
        this.geocodageAPI = geocodageAPI;
    }

    public Contact ajouterContact(ContactDTO dto) {
        if (dto == null || dto.getNom() == null || dto.getPrenom() == null || dto.getAdresses() == null) {
            throw new IllegalArgumentException("Le ContactDTO est invalide");
        }

        try {
            // Créer contact
            Contact contact = new Contact();
            contact.setNom(dto.getNom());
            contact.setPrenom(dto.getPrenom());
            contact.setFavoris(dto.isFavoris());

            // Ajout du contact dans la BDD 
            contactDAO.ajouter(contact);

            Integer contactId = contact.getId_contact();
            if (contactId == null) {
                throw new RuntimeException("Impossible de récupérer l'ID du contact après insertion.");
            }

            // Ajout des adresses
            for (AdresseDTO adresseDTO : dto.getAdresses()) {
                // Obtenir les coordonnées de l'adresse
                Coordonnees coord = geocodageAPI.obtenirCoordonnees(adresseDTO);
                
                Adresse adresse = AdresseMapper.toEntiter(adresseDTO, coord, contactId);
                
                // Ajout de l'adresse dans la BDD
                adresseDAO.ajouter(adresse);
            }

            return contact;
        } catch (Exception e) {
            logger.log(Level.ERROR, "Erreur lors de l'ajout d'un contact", e);
            throw new RuntimeException("Erreur lors de l'ajout d'un contact", e);
        }
    }

    public void ajouterAdresse(Integer id_contact, Adresse adresse) {
        if (id_contact == null || adresse == null) {
            throw new IllegalArgumentException("L'id_contact ou l'adresse est invalide");
        }
    
        //Un DTO temporaire à partir de l'adresse
        AdresseDTO tempDTO = new AdresseDTO();
        tempDTO.setRue(adresse.getRue());
        tempDTO.setVille(adresse.getVille());
        tempDTO.setCodePostal(adresse.getCodePostal());
        tempDTO.setPays(adresse.getPays());
    
        Coordonnees coord = geocodageAPI.obtenirCoordonnees(tempDTO);
    
        adresse.setCoordonnees(coord);
        adresse.setId_contact(id_contact);
    
        adresseDAO.ajouter(adresse);
    }
    
    
    //Méthode pour marquer un contact comme favori
    public void marquerCommeFavori(Integer id_contact, boolean isFavoris) {
        Contact contact = contactDAO.trouverParId(id_contact);
        if (contact != null) {
            contact.setFavoris(isFavoris);
            contactDAO.mettreAJour(contact);
        } else {
            logger.log(Level.WARNING, "Contact non trouvé pour id_contact=" + id_contact);
        }
    }
    //Méthode pour trouver tous les contacts
    public List<Contact> trouverTous() {
        List<Contact> contacts = contactDAO.trouverTous();
        for (Contact contact : contacts) {
            List<Adresse> adresses = adresseDAO.trouverTous().stream().filter(a -> a.getId_contact() == contact.getId_contact()).toList();
            contact.setAdresses(adresses);
        }
        return contacts;
    }
    
    //Méthode pour trouver un contact par son id
    public Contact trouverParId(Integer id) {
        return contactDAO.trouverParId(id);
    }

    public void supprimerContact(Contact contact) {
        if (contact != null) {
            contactDAO.supprimer(contact);
        } else {
            logger.log(Level.WARNING, "Tentative de suppression d'un contact null");
        }
    }
    //Méthode pour trouver les contacts favoris
    public List<Contact> trouverContactsFavoris() {
        List<Contact> favoris = contactDAO.trouverContactsFavoris();
        for (Contact contact : favoris) {
            List<Adresse> adresses = adresseDAO.trouverTous().stream()
                    .filter(a -> a.getId_contact() == contact.getId_contact())
                    .toList();
            contact.setAdresses(adresses);
        }
        return favoris;
    }
    
    //Méthode pour trouver les contacts proches
    public List<Contact> trouverContactsProches(Coordonnees centre, double rayonKm) {
        List<Contact> contactsProches = new ArrayList<>();
        List<Contact> tousLesContacts = contactDAO.trouverTous();
    
        System.out.println("Centre : Latitude = " + centre.getLatitude() + ", Longitude = " + centre.getLongitude());
    
        for (Contact contact : tousLesContacts) {
            for (Adresse adresse : contact.getAdresses()) {
                if (adresse.getCoordonnees() != null) {
                    double distance = CalculateurDistance.calculerDistance(centre, adresse.getCoordonnees());
                    System.out.println("Distance calculée pour " + adresse.getRue() + " : " + distance + " km");
    
                    if (distance <= rayonKm) {
                        System.out.println("Contact ajouté : " + contact.getNom() + " à " + distance + " km");
                        contactsProches.add(contact);
                        break; 
                    }
                } else {
                    System.out.println("Coordonnées nulles pour l'adresse : " + adresse.getRue());
                }
            }
        }
        return contactsProches;
    }
    
    

}
