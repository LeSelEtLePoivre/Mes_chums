package cal355.projet.DAO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import cal355.projet.Modèles.Adresse;
import cal355.projet.Modèles.Contact;
import cal355.projet.Modèles.Coordonnees;

public class ContactDAO implements GeneriqueDAO<Contact> {
    private Connection connection;

    public ContactDAO(Connection connection) {
        this.connection = connection;
    }
    //Méthode pour ajouter un contact
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
    //Méthode pour supprimer un contact
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
    //Méthode pour mettre à jour un contact
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
    //Méthode pour trouver les contacts favoris
    public List<Contact> trouverContactsFavoris() {
        List<Contact> favoris = new ArrayList<>();
        String sql = "SELECT * FROM Contact WHERE is_favoris = 1"; // Utilisez is_favoris
    
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    Contact contact = new Contact();
                    contact.setId_contact(resultSet.getInt("id_contact"));
                    contact.setNom(resultSet.getString("nom"));
                    contact.setPrenom(resultSet.getString("prenom"));
                    contact.setFavoris(resultSet.getBoolean("is_favoris")); // Correction ici
    
                    favoris.add(contact);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return favoris;
    }
    
    //Méthode pour trouver un contact par son id
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
    //Méthode pour trouver tous les contacts
    @Override
    public List<Contact> trouverTous() {
    String sqlContact = "SELECT * FROM Contact";
    String sqlAdresse = "SELECT * FROM Adresse WHERE id_contact = ?";

    List<Contact> contacts = new ArrayList<>();
    try (PreparedStatement stmtContact = connection.prepareStatement(sqlContact)) {
        ResultSet rsContact = stmtContact.executeQuery();
        while (rsContact.next()) {
            Contact contact = new Contact(
                rsContact.getInt("id_contact"),
                rsContact.getString("nom"),
                rsContact.getString("prenom"),
                rsContact.getBoolean("is_favoris")
            );

            // Charger les adresses associées
            try (PreparedStatement stmtAdresse = connection.prepareStatement(sqlAdresse)) {
                stmtAdresse.setInt(1, contact.getId_contact());
                ResultSet rsAdresse = stmtAdresse.executeQuery();
                List<Adresse> adresses = new ArrayList<>();
                while (rsAdresse.next()) {
                    Adresse adresse = new Adresse(
                        rsAdresse.getInt("id_adresse"),
                        rsAdresse.getString("rue"),
                        rsAdresse.getString("ville"),
                        rsAdresse.getString("codePostal"),
                        rsAdresse.getString("pays"),
                        new Coordonnees(
                            rsAdresse.getDouble("latitude"),
                            rsAdresse.getDouble("longitude")
                        ),
                        contact.getId_contact()
                    );

                    adresses.add(adresse);
                }
                contact.setAdresses(adresses); // Assigner les adresses
            }

            contacts.add(contact);
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return contacts;
}

}