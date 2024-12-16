package cal355.projet.DTO;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ContactDTO {
    private Integer id_contact;
    private String nom;
    private String prenom;

    @JsonProperty("isFavoris")
    private boolean favoris;

    private List<AdresseDTO> adresses;

    // Constructeur par défaut
    public ContactDTO() {
    }

    // Constructeur avec arguments
    public ContactDTO(Integer id_contact, String nom, String prenom, boolean favoris, List<AdresseDTO> adresses) {
        this.id_contact = id_contact;
        this.nom = nom;
        this.prenom = prenom;
        this.favoris = favoris;
        this.adresses = adresses;
    }

    // Getters et setters
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
}