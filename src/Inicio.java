import javax.swing.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;

import org.json.JSONException;
import org.json.JSONObject;

public class Inicio extends JFrame {
    private JPanel panelMain;
    private JLabel labelTitle;
    private JComboBox<String> comboBox1;
    private JComboBox<String> comboBox2;
    private JTextField txt1;
    private JTextField txt2;

    public Inicio() {
        super("Conversor ONE");
        setSize(600, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
        setContentPane(panelMain);
        connectAPI();
    }

    public void connectAPI(){
        try {
            // crear un objeto URL con el valor de la enumeración URLEnum.API_URL, que contiene la dirección de la API
            URL url = new URL(URLEnum.API_URL.getValue());
            // abrir una conexión HttpURLConnection con el objeto URL
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            // establecer el método de solicitud en GET
            con.setRequestMethod("GET");
            // iniciar la conexión con la API
            con.connect();
            // obtener el código de respuesta de la conexión, que indica el estado de la misma
            int responseCode = con.getResponseCode();

            // si el estado es distinto de 200 lanzamos un error
            if(responseCode != 200) {
                throw new RuntimeException("HttpResponseCode: " + responseCode);
            } else {
                // si el código de respuesta es 200, que significa que la conexión fue exitosa, proceder a leer los datos que devuelve la API

                // crear un objeto StringBuilder llamado informacion para almacenar los datos de la API como una cadena
                StringBuilder informacion = new StringBuilder();
                // crear un objeto Scanner llamado scanner para leer el flujo de entrada (InputStream) que se abre desde el objeto URL
                Scanner scanner = new Scanner(url.openStream());

                // mientras haya más líneas por leer en el flujo de entrada, agregar cada línea al objeto informacion usando el método append()
                while(scanner.hasNext()) {
                    informacion.append(scanner.nextLine());
                }

                // cerrar el objeto scanner usando el método close(
                scanner.close();
                fillCombobox(informacion);
                System.out.println(informacion);
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    void fillCombobox(StringBuilder informacion) throws JSONException {
        // crear un objeto JSONObject con la cadena informacion
        JSONObject json = new JSONObject(informacion.toString());
        try {
            // obtener el objeto JSONObject asociado a la clave "data"
            JSONObject data = json.getJSONObject("data");
            // obtener un iterador de las claves del objeto data
            Iterator<String> keys = data.keys();
            // crear una lista de cadenas para almacenar los códigos de los países
            List<String> codesCountry = new ArrayList<>();

            // mientras haya más claves por recorrer, agregar cada clave al modelo usando el método addItem
            while(keys.hasNext()) {
                String key = keys.next();
                codesCountry.add(key);
            }
            System.out.println("Desordenado: " + codesCountry);
            // ordenar la lista de códigos alfabéticamente usando el método sort
            Collections.sort(codesCountry);
            System.out.println("Ordenado: " + codesCountry);
            // crear un objeto DefaultComboBoxModel para almacenar los códigos de los países
            DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>();
            // recorrer la lista de códigos ordenada y agregar cada código al modelo usando el método addElement
            for (String code : codesCountry) {
                model.addElement(code);
            }
            // asignar el modelo al JComboBox usando el método setModel
            this.comboBox1.setModel(model);
        } catch (JSONException e) {
            // manejar la excepción
        }
    }
}
