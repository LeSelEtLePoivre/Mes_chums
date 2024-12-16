package cal355.projet.DAO;

import java.util.List;

public interface GeneriqueDAO<T> {
   void ajouter(T objet);
    void supprimer(T objet);
    void mettreAJour(T objet);
    T trouverParId(Integer id);
    List<T> trouverTous();
}

