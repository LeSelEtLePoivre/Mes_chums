package cal355.projet.DAO;

import cal355.projet.Modèles.Adresse;
import cal355.projet.Modèles.Coordonnees;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AdresseDAO implements GeneriqueDAO<Adresse> {
    private Connection connection;

    public AdresseDAO(Connection connection) {
        this.connection = connection;
    }
    //Méthode pour ajouter une adresse
    @Override
    public void ajouter(Adresse adresse) {
        String sql = "INSERT INTO Adresse (rue, ville, codePostal, pays, latitude, longitude, id_contact) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, adresse.getRue());
            preparedStatement.setString(2, adresse.getVille());
            preparedStatement.setString(3, adresse.getCodePostal());
            preparedStatement.setString(4, adresse.getPays());
            preparedStatement.setDouble(5, adresse.getCoordonnees().getLatitude());
            preparedStatement.setDouble(6, adresse.getCoordonnees().getLongitude());
            preparedStatement.setInt(7, adresse.getId_contact());

            preparedStatement.executeUpdate();

            // Récupération de l'ID généré
            try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    adresse.setId_adresse(generatedKeys.getInt(1));
                    System.out.println("Adresse ajoutée avec ID : " + adresse.getId_adresse());
                } else {
                    throw new SQLException("L'ajout de l'adresse a échoué, aucun ID généré.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //Méthode pour supprimer une adresse
    @Override
    public void supprimer(Adresse adresse) {
        String sql = "DELETE FROM Adresse WHERE id_adresse = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, adresse.getId_adresse());
            preparedStatement.executeUpdate();
            System.out.println("Adresse supprimée avec ID : " + adresse.getId_adresse());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    //Méthode pour mettre à jour une adresse
    @Override
    public void mettreAJour(Adresse adresse) {
        String sql = "UPDATE Adresse SET rue = ?, ville = ?, codePostal = ?, pays = ?, latitude = ?, longitude = ?, id_contact = ? WHERE id_adresse = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, adresse.getRue());
            preparedStatement.setString(2, adresse.getVille());
            preparedStatement.setString(3, adresse.getCodePostal());
            preparedStatement.setString(4, adresse.getPays());
            preparedStatement.setDouble(5, adresse.getCoordonnees().getLatitude());
            preparedStatement.setDouble(6, adresse.getCoordonnees().getLongitude());
            preparedStatement.setInt(7, adresse.getId_contact());
            preparedStatement.setInt(8, adresse.getId_adresse());

            int rows = preparedStatement.executeUpdate();
            if (rows > 0) {
                System.out.println("Adresse mise à jour avec ID : " + adresse.getId_adresse());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    //Méthode pour trouver une adresse par son ID
    @Override
    public Adresse trouverParId(Integer id) {
        String sql = "SELECT * FROM Adresse WHERE id_adresse = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return construireAdresse(resultSet);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    //Méthode pour trouver toutes les adresses
    @Override
    public List<Adresse> trouverTous() {
        String sql = "SELECT * FROM Adresse";
        List<Adresse> adresses = new ArrayList<>();
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    adresses.add(construireAdresse(resultSet));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return adresses;
    }

    private Adresse construireAdresse(ResultSet resultSet) throws SQLException {
        double lat = resultSet.getDouble("latitude");
        double lng = resultSet.getDouble("longitude");
        int id_adresse = resultSet.getInt("id_adresse");
    
        System.out.println("Adresse lue : ID = " + id_adresse + ", Latitude = " + lat + ", Longitude = " + lng);
    
        return new Adresse(
            id_adresse,
            resultSet.getString("rue"),
            resultSet.getString("ville"),
            resultSet.getString("codePostal"),
            resultSet.getString("pays"),
            new Coordonnees(lat, lng),
            resultSet.getInt("id_contact")
        );
    }
    
}
