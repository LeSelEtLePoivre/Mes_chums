package cal355.projet.DAO;
import cal355.projet.Modèles.Adresse;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class AdresseDAO implements GeneriqueDAO<Adresse> {
    private Connection connection;

    public AdresseDAO(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void ajouter(Adresse adresse) {
        String sql = "INSERT INTO Adresse (rue, ville, codePostal, pays, id_contact) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)){
            preparedStatement.setString(1, adresse.getRue());
            preparedStatement.setString(2, adresse.getVille());
            preparedStatement.setString(3, adresse.getCodePostal());
            preparedStatement.setString(4, adresse.getPays());
            preparedStatement.setInt(5, adresse.getId_contact());
            preparedStatement.executeUpdate();
            try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    adresse.setId_adresse(generatedKeys.getInt(1));
                } else {
                    throw new SQLException("Creating adresse failed, no ID obtained.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void supprimer(Adresse adresse) {
        String sql = "DELETE FROM Adresse WHERE id_adresse = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)){
            preparedStatement.setInt(1, adresse.getId_adresse());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void mettreAJour(Adresse adresse) {
        String sql = "UPDATE Adresse SET rue = ?, ville = ?, codePostal = ?, pays = ?, id_contact = ? WHERE id_adresse = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)){
            preparedStatement.setString(1, adresse.getRue());
            preparedStatement.setString(2, adresse.getVille());
            preparedStatement.setString(3, adresse.getCodePostal());
            preparedStatement.setString(4, adresse.getPays());
            preparedStatement.setInt(5, adresse.getId_contact());
            preparedStatement.setInt(6, adresse.getId_adresse());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Adresse trouverParId(Integer id) {
        String sql = "SELECT * FROM Adresse WHERE id_adresse = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)){
            preparedStatement.setInt(1, id);
            try (ResultSet resultSet = preparedStatement.executeQuery()){
                if (resultSet.next()){
                    return new Adresse(
                        resultSet.getInt("id_adresse"),
                        resultSet.getString("rue"),
                        resultSet.getString("ville"),
                        resultSet.getString("codePostal"),
                        resultSet.getString("pays"),
                        null,
                        resultSet.getInt("id_contact")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Adresse> trouverTous() {
        String sql = "SELECT * FROM Adresse";
        List<Adresse> adresses = new ArrayList<>();
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)){
            try (ResultSet resultSet = preparedStatement.executeQuery()){
                while (resultSet.next()){
                    Adresse adresse = new Adresse(
                        resultSet.getInt("id_adresse"),
                        resultSet.getString("rue"),
                        resultSet.getString("ville"),
                        resultSet.getString("codePostal"),
                        resultSet.getString("pays"),
                        null,
                        resultSet.getInt("id_contact")
                    );
                    adresses.add(adresse);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return adresses;
    }
}