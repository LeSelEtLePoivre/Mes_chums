package cal355.projet.Modèles;
import java.util.List;

public class Contact {
    private Integer id_contact;
    private String nom;
    private String prenom;
    private boolean isFavoris;
    private List<Adresse> adresses;

    public Contact(Integer id_contact, String nom, String prenom, boolean isFavoris) {
        this.id_contact = id_contact;
        this.nom = nom;
        this.prenom = prenom;
        this.isFavoris = isFavoris;
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
        return isFavoris;
    }

    public void setFavoris(boolean isFavoris) {
        this.isFavoris = isFavoris;
    }

    public List<Adresse> getAdresses() {
        return adresses;
    }

    public void setAdresses(List<Adresse> adresses) {
        this.adresses = adresses;
    }

}
