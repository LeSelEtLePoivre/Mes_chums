package cal355.projet.Service;
import cal355.projet.DAO.ContactDAO;

import java.lang.System.Logger;
import java.lang.System.Logger.Level;
import java.lang.System.Logger;
import java.sql.SQLException;
import java.util.List;

import cal355.projet.DAO.AdresseDAO;
import cal355.projet.Modèles.Adresse;
import cal355.projet.Modèles.Contact;

public class ContactService {
    private ContactDAO contactDAO;
    private AdresseDAO adresseDAO;
    private static final Logger logger = System.getLogger(ContactService.class.getName());

    public ContactService(ContactDAO contactDAO, AdresseDAO adresseDAO) {
        this.contactDAO = contactDAO;
        this.adresseDAO = adresseDAO;
    }

    public void ajouterContact(Integer id_contact, String nom, String prenom, boolean isFavoris) {
        try {
            Contact contact = new Contact(id_contact, nom, prenom, isFavoris);
            contactDAO.ajouter(contact);
        } catch (Exception e) {
            logger.log(Level.ALL, "Erreur lors de l'ajout du contact", e);
        }
    }


    public void ajouterAdresse (Integer id_contact, Adresse adresse){
        adresse.setId_contact(id_contact);
        adresseDAO.ajouter(adresse);
    }

    public void marquerCommeFavori(Integer id_contact, boolean isFavoris){
        Contact contact = contactDAO.trouverParId(id_contact);
        if(contact != null){
            contact.setFavoris(isFavoris);
            contactDAO.mettreAJour(contact);
        }
    }

    public List<Contact> trouverTous() {
        return contactDAO.trouverTous();
    }

    public Contact trouverParId(Integer id) {
        return contactDAO.trouverParId(id);
    }

    public void supprimerContact(Contact contact) {
        contactDAO.supprimer(contact);
    }
}
