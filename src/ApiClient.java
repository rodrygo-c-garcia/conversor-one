import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ApiClient {
    private static final String BASE_URL = "https://api.currencyapi.com/v3/latest";
    private String apikey;

    public ApiClient() {
        Properties properties = new Properties();
        try {
            // Cargar el archivo de propiedades desde una ruta específica
            properties.load(new FileInputStream("config.properties"));
            // Obtener el valor de la apikey por su nombre
            apikey = properties.getProperty("apikey");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getApiKey() {
        return this.apikey;
    }
}
