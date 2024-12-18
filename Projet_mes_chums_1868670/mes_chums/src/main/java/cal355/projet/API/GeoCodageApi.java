package cal355.projet.API;

import cal355.projet.DTO.AdresseDTO;
import cal355.projet.Modèles.Coordonnees;

public interface GeoCodageApi {
    Coordonnees obtenirCoordonnees(AdresseDTO adresseDTO);
}
