package cal355.projet.Mapper;

import cal355.projet.DTO.AdresseDTO;
import cal355.projet.Modèles.Adresse;
import cal355.projet.Modèles.Coordonnees;

public class AdresseMapper {



    public static AdresseDTO toDTO(Adresse adresse) {
        if (adresse == null) return null;
        
        AdresseDTO dto = new AdresseDTO();
        dto.setRue(adresse.getRue());
        dto.setVille(adresse.getVille());
        dto.setCodePostal(adresse.getCodePostal());
        dto.setPays(adresse.getPays());
        dto.setCoordonnees(adresse.getCoordonnees()); 
        return dto;
    }
    


    public static Adresse toEntiter(AdresseDTO dto) {
        // Méthode simple qui ne prend que le dto
        if (dto == null) return null;

        Adresse adresse = new Adresse();
        adresse.setRue(dto.getRue());
        adresse.setVille(dto.getVille());
        adresse.setCodePostal(dto.getCodePostal());
        adresse.setPays(dto.getPays());
        adresse.setCoordonnees(dto.getCoordonnees());
        return adresse;
    }

    public static Adresse toEntiter(AdresseDTO dto, Coordonnees coord, Integer idContact) {
        if (dto == null) return null;

        Adresse adresse = new Adresse();
        adresse.setRue(dto.getRue());
        adresse.setVille(dto.getVille());
        adresse.setCodePostal(dto.getCodePostal());
        adresse.setPays(dto.getPays());
        adresse.setCoordonnees(coord);
        adresse.setId_contact(idContact); 

        return adresse;
    }
}
