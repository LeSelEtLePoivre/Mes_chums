package cal355.projet.Service;
import cal355.projet.Modèles.Contact;
import cal355.projet.Modèles.Coordonnees;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CacheService {
    private Map<Contact, List<Coordonnees>> contactsFavoris = new HashMap<>();

    public void ajouterFavori(Contact contact, Coordonnees coordonnees) {
        List<Coordonnees> coordonneesList = contactsFavoris.getOrDefault(contact, new ArrayList<>());
        coordonneesList.add(coordonnees);
        contactsFavoris.put(contact, coordonneesList);
    }

    public void supprimerFavori(Contact contact) {
        contactsFavoris.remove(contact);
    }

    public Map<Contact, List<Coordonnees>> getContactsFavoris() {
        return contactsFavoris;
    }
}