package cal355.projet.DAO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import cal355.projet.Modèles.Contact;

public class ContactDAO implements GeneriqueDAO<Contact> {
    private Connection connection;

    public ContactDAO(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void ajouter(Contact contact) {
        String sql = "INSERT INTO Contact (nom, prenom, is_favoris) VALUES (?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)){
            preparedStatement.setString(1, contact.getNom());
            preparedStatement.setString(2, contact.getPrenom());
            preparedStatement.setBoolean(3, contact.isFavoris());
            preparedStatement.executeUpdate();
            try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    contact.setId_contact(generatedKeys.getInt(1));
                } else {
                    throw new SQLException("Creating contact failed, no ID obtained.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void supprimer(Contact contact) {
        String sql = "DELETE FROM Contact WHERE id_contact = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)){
            preparedStatement.setInt(1, contact.getId_contact());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void mettreAJour(Contact contact) {
        String sql = "UPDATE Contact SET nom = ?, prenom = ?, is_favoris = ? WHERE id_contact = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)){
            preparedStatement.setString(1, contact.getNom());
            preparedStatement.setString(2, contact.getPrenom());
            preparedStatement.setBoolean(3, contact.isFavoris());
            preparedStatement.setInt(4, contact.getId_contact());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Contact trouverParId(Integer id) {
        String sql = "SELECT * FROM Contact WHERE id_contact = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)){
            preparedStatement.setInt(1, id);
            try (ResultSet resultSet = preparedStatement.executeQuery()){
                if (resultSet.next()){
                    return new Contact(
                        resultSet.getInt("id_contact"),
                        resultSet.getString("nom"),
                        resultSet.getString("prenom"),
                        resultSet.getBoolean("is_favoris")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Contact> trouverTous() {
        String sql = "SELECT * FROM Contact";
        List<Contact> contacts = new ArrayList<>();
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)){
            try (ResultSet resultSet = preparedStatement.executeQuery()){
                while (resultSet.next()){
                    contacts.add(new Contact(
                        resultSet.getInt("id_contact"),
                        resultSet.getString("nom"),
                        resultSet.getString("prenom"),
                        resultSet.getBoolean("is_favoris")
                    ));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return contacts;
    }
}