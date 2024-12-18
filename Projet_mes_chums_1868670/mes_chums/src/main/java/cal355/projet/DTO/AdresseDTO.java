package cal355.projet.DTO;
import cal355.projet.Modèles.Adresse;
import cal355.projet.Modèles.Coordonnees;

public class AdresseDTO {
    private Integer id_adresse;
    private String rue;
    private String ville;
    private String codePostal;
    private String pays;
    private Coordonnees coordonnees; 

    public AdresseDTO() {
    }

    public AdresseDTO(Integer id_adresse,String rue, String ville, String codePostal, String pays, Coordonnees coordonnees) {
        this.id_adresse = id_adresse;
        this.rue = rue;
        this.ville = ville;
        this.codePostal = codePostal;
        this.pays = pays;
        this.coordonnees = coordonnees;
    }

    public String getRue() {
        return rue;
    }

    public void setRue(String rue) {
        this.rue = rue;
    }

    public String getVille() {
        return ville;
    }

    public void setVille(String ville) {
        this.ville = ville;
    }

    public String getCodePostal() {
        return codePostal;
    }

    public void setCodePostal(String codePostal) {
        this.codePostal = codePostal;
    }

    public String getPays() {
        return pays;
    }

    public void setPays(String pays) {
        this.pays = pays;
    }

    public Coordonnees getCoordonnees() {
        return coordonnees;
    }

    public void setCoordonnees(Coordonnees coordonnees) {
        this.coordonnees = coordonnees;
    }

    public Integer getId_adresse() {
        return id_adresse;
    }

    public void setId_adresse(Integer id_adresse) {
        this.id_adresse = id_adresse;
    }

    public Adresse toEntiter() {
        Adresse adresse = new Adresse();
        adresse.setRue(this.rue);
        adresse.setVille(this.ville);
        adresse.setCodePostal(this.codePostal);
        adresse.setPays(this.pays);
        adresse.setCoordonnees(this.coordonnees); 
        return adresse;
    }
}
