package cal355.projet.Service;
import cal355.projet.DAO.ContactDAO;
import cal355.projet.Modèles.Contact;
import cal355.projet.Modèles.Coordonnees;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CacheService {
    private final ContactDAO contactDAO;
    private final Map<Contact, List<Coordonnees>> contactsFavoris = new HashMap<>();

    public CacheService(ContactDAO contactDAO) {
        this.contactDAO = contactDAO;
    }
    // Initialiser le cache
    public void initialiserCache() {
        // Récupérer les contacts favoris
        List<Contact> favoris = trouverContactsFavoris();
        for (Contact f : favoris) {
            // Extraire leurs coordonnées
            List<Coordonnees> coords = f.getAdresses().stream()
                    .map(a -> a.getCoordonnees())
                    .toList();
            contactsFavoris.put(f, coords);
        }
    }
    // Ajouter un contact favori au cache
    public void ajouterContactFavori(Contact c) {
        List<Coordonnees> coords = c.getAdresses().stream()
                .map(a -> a.getCoordonnees())
                .toList();
        contactsFavoris.put(c, coords);
    }
    // Retirer un contact favori du cache
    public void retirerContactFavori(Contact c) {
        contactsFavoris.remove(c);
    }
    // Obtenir les coordonnées d'un contact favori
    private List<Contact> trouverContactsFavoris() {
        return contactDAO.trouverContactsFavoris();
    }
    
}