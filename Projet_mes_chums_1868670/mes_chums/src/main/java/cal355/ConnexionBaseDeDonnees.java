package cal355;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnexionBaseDeDonnees {
    public static Connection obtenirConnexion() throws SQLException {
        String url = "jdbc:sqlite:C:/Users/mathi/Documents/java/Mes_chums/Projet_mes_chums_1868670/mes_chums/src/main/resources/mes_chums.db";
        System.out.println("Connexion à la base : " + url);
        return DriverManager.getConnection(url);
    }
}
