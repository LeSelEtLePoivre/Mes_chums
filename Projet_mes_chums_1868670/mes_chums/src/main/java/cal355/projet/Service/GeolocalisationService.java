package cal355.projet.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import cal355.projet.API.GeoCodageApi;
import cal355.projet.DTO.AdresseDTO;
import cal355.projet.Modèles.Coordonnees;

public class GeolocalisationService implements GeoCodageApi {
    private static final String API_KEY = "AIzaSyDWyjyOGrdC-UQmdgFsFbMsOLqwnUfiMaY"; 
    private static final String GEOCODE_URL = "https://maps.googleapis.com/maps/api/geocode/json?address=%s&key=%s";
    // Methode pour obtenir les coordonnées d'une adresse
    @Override
    public Coordonnees obtenirCoordonnees(AdresseDTO adresseDTO) {
        String adresseStr = String.format("%s, %s, %s, %s",
            adresseDTO.getRue(),
            adresseDTO.getVille(),
            adresseDTO.getCodePostal(),
            adresseDTO.getPays()
        );
        try {
            // Encode l'adresse
            String query = URLEncoder.encode(adresseStr, StandardCharsets.UTF_8);

            // Construit l'URL finale
            String urlString = String.format(GEOCODE_URL, query, API_KEY);

            // Loguer l'URL pour vérifier
            System.out.println("URL Geocoding: " + urlString);

            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
    
            // Lire la réponse JSON
            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();
    
            System.out.println("Réponse Google Maps Geocoding API: " + response.toString());
    
            ObjectMapper mapper = new ObjectMapper();
            JsonNode rootNode = mapper.readTree(response.toString());
    
            JsonNode statusNode = rootNode.get("status");
            if (statusNode != null) {
                System.out.println("Statut de la réponse: " + statusNode.asText());
            }
    
            if (rootNode.has("results") && rootNode.get("results").size() > 0) {
                JsonNode location = rootNode.get("results").get(0)
                        .get("geometry").get("location");
                double latitude = location.get("lat").asDouble();
                double longitude = location.get("lng").asDouble();
                return new Coordonnees(latitude, longitude);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null; 
    }
}    