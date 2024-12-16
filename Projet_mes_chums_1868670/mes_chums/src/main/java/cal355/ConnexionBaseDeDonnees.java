package cal355;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnexionBaseDeDonnees {
    private static final String URL;

    static {
        Path cheminVersBase = Paths.get("src", "main", "resources", "mes_chums.db");
        URL = "jdbc:sqlite:" + cheminVersBase.toAbsolutePath().toString();
    }

    public static Connection obtenirConnexion() throws SQLException {
        return DriverManager.getConnection(URL);
    }
}