package cal355.projet;
import com.sun.net.httpserver.HttpServer;

import cal355.ConnexionBaseDeDonnees;
import cal355.projet.Controlleur.ContactControlleur;
import cal355.projet.DAO.ContactDAO;
import cal355.projet.DAO.AdresseDAO;
import cal355.projet.Service.ContactService;
import cal355.projet.Service.GeolocalisationService;
import cal355.projet.Service.CacheService;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ApplicationMesChums {
    private static final int PORT = 8080;
    private static final Logger LOGGER = Logger.getLogger(ApplicationMesChums.class.getName());

    public static void main(String[] args) {
        try {
            Connection connection = ConnexionBaseDeDonnees.obtenirConnexion();

            ContactDAO contactDAO = new ContactDAO(connection);
            AdresseDAO adresseDAO = new AdresseDAO(connection);
            GeolocalisationService geolocalisationService = new GeolocalisationService();
            CacheService cacheService = new CacheService();
            ContactService contactService = new ContactService(contactDAO, adresseDAO);

            HttpServer serveur = HttpServer.create(new InetSocketAddress(PORT), 0);
            ContactControlleur contactControlleur = new ContactControlleur(contactService, geolocalisationService, cacheService);

            serveur.createContext("/contact", contactControlleur);
            serveur.setExecutor(null);
            serveur.start();

            LOGGER.info("Serveur démarré sur le port " + PORT);
        } catch (IOException | SQLException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors du démarrage du serveur", e);
        }
    }
}