package cal355.projet.DTO;
import cal355.projet.Modèles.Adresse;
import cal355.projet.Modèles.Contact;
import java.util.ArrayList;
import java.util.List;

public class ContactDTO {
    private Integer id_contact;
    private String nom;
    private String prenom;

    private boolean favoris;

    private List<AdresseDTO> adresses;

    public ContactDTO() {
    }

    public ContactDTO(Integer id_contact, String nom, String prenom, boolean favoris, List<AdresseDTO> adresses) {
        this.id_contact = id_contact;
        this.nom = nom;
        this.prenom = prenom;
        this.favoris = favoris;
        this.adresses = adresses;
    }

    public Integer getId_contact() {
        return id_contact;
    }

    public void setId_contact(Integer id_contact) {
        this.id_contact = id_contact;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public boolean isFavoris() {
        return favoris;
    }

    public void setFavoris(boolean favoris) {
        this.favoris = favoris;
    }

    public List<AdresseDTO> getAdresses() {
        return adresses;
    }

    public void setAdresses(List<AdresseDTO> adresses) {
        this.adresses = adresses;
    }

    public Contact toEntiter() {
        Contact contact = new Contact();
        contact.setId_contact(this.id_contact);
        contact.setNom(this.nom);
        contact.setPrenom(this.prenom);
        contact.setFavoris(this.favoris);

        if (this.adresses != null) {
            List<Adresse> listeAdresses = new ArrayList<>();
            for (AdresseDTO aDto : this.adresses) {
                listeAdresses.add(aDto.toEntiter());
            }
            contact.setAdresses(listeAdresses);
        } else {
            contact.setAdresses(new ArrayList<>());
        }

        return contact;
    }
}