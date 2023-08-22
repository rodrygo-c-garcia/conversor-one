import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Inicio extends JFrame {
    private JPanel panelMain;
    private JLabel labelTitle;
    private JComboBox<String> comboBox1;
    private JComboBox<String> comboBox2;
    private JTextField txt1;
    private JTextField txt2;

    // data for the API
    private String baseCurrency = "BOB";
    private String currency = "BOB";
    private double valueCurrency = 1.0;
    private double valueQuantity = 1.0;
    private ApiClient apiClient;

    public Inicio() {
        super("Conversor ONE");
        setSize(400, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
        setContentPane(panelMain);
        //  funciones de la funcionalidad del sistema
        apiClient = new ApiClient();
        connectAPI();
        selectedCodeBaseCountry();
        selectedCurrency();


        // Crear un objeto DocumentListener que realiza una acción cuando cambia el texto
        DocumentListener dl = listenJTextField();
        // Agregar el DocumentListener al campo txt1
        txt1.getDocument().addDocumentListener(dl);
    }

    public void connectAPI(){
        try {
            // crear un objeto URL con el valor de la enumeración URLEnum.API_URL, que contiene la dirección de la API
            URL url = new URL(apiClient.getBaseUrl() + "?apikey=" + apiClient.getApiKey());
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
                System.out.println("API: " + informacion);
                fillCombobox(informacion);
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    void fillCombobox(StringBuilder informacion) throws JSONException {
        try {
            // crear un objeto JSONObject con la cadena informacion
            JSONObject json = new JSONObject(informacion.toString());
            // obtener el objeto JSONObject asociado a la clave "data"
            JSONObject data = json.getJSONObject("data");
            // obtener un iterador de las claves del objeto data
            Iterator<String> keys = data.keys();
            // crear una lista de cadenas para almacenar los códigos de los países
            List<String> codesCountry = new ArrayList<>();

            // crear una lista de cadenas con los códigos de las monedas que quieres agregar
            List<String> desiredCodes = Arrays.asList("USD", "BOB", "EUR", "GBP", "JPY", "KRW");
            // mientras haya más claves por recorrer, agregar cada clave al modelo usando el método addItem
            while(keys.hasNext()) {
                String key = keys.next();

                if(desiredCodes.contains(key)){
                    codesCountry.add(key);
                }
            }
            System.out.println("Desordenado: " + codesCountry);
            // ordenar la lista de códigos alfabéticamente usando el método sort
            codesCountry = orderCodeCountry(codesCountry);
            // crear un objeto DefaultComboBoxModel para almacenar los códigos de los países
            DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>();
            DefaultComboBoxModel<String> model2 = new DefaultComboBoxModel<>();
            // recorrer la lista de códigos ordenada y agregar cada código al modelo usando el método addElement
            for (String code : codesCountry) {
                model.addElement(code);
                model2.addElement(code);
            }
            // asignar el modelo al JComboBox usando el método setModel
            this.comboBox1.setModel(model);
            this.comboBox2.setModel(model2);
        } catch (JSONException e) {
            // manejar la excepción
        }
    }

    public List<String> orderCodeCountry(List<String> codesCountry){
        Collections.sort(codesCountry);
        System.out.println("Ordenado: " + codesCountry);
        return codesCountry;
    }

    public void selectedCodeBaseCountry(){
        comboBox1.addItemListener(new ItemListener() {

            @Override
            public void itemStateChanged(ItemEvent e) {
                // comprobar si el evento es de tipo selección
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    // obtener el valor seleccionado en el JComboBox
                    setBaseCurrency((String) e.getItem());
                    // imprimir el valor seleccionado en la consola
                    txt2.setText(getBaseCurrency());
                    obtainCurrency();
                }
            }
        });
    }

    public void setBaseCurrency(String baseCurrency){ this.baseCurrency = baseCurrency; }
    public String getBaseCurrency(){ return this.baseCurrency;}

    public void selectedCurrency(){
        comboBox2.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                // comprobar si el evento es de tipo selección
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    // obtener el valor seleccionado en el JComboBox
                    setCurrency((String) e.getItem());
                    // imprimir el valor seleccionado en la consola
                    System.out.println("El código seleccionado es: " + getCurrency());
                    obtainCurrency();
                }
            }
        });
    }

    public void setCurrency(String currency){ this.currency = currency; }
    public String getCurrency(){ return this.currency; }

    public void obtainCurrency(){
        try {
            // crear un objeto URL con el valor de la enumeración URLEnum.API_URL, que contiene la dirección de la API
            URL url = new URL(apiClient.getBaseUrl() + "?apikey=" + apiClient.getApiKey() + "&currencies=" + getCurrency() + "&base_currency=" + getBaseCurrency());
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
                System.out.println("Value Informacion: " + informacion);
                System.out.println(apiClient.getBaseUrl() + "?apikey=" + apiClient.getApiKey() + "&curriencies=" + getCurrency() + "&base_currency=" + getBaseCurrency());
                // crear un objeto JSONObject con la cadena informacion
                JSONObject json = new JSONObject(informacion.toString());
                // obtener el objeto JSONObject asociado a la clave "data"
                JSONObject data = json.getJSONObject("data");
                // obtener el objeto JSONObject asociado a la clave
                JSONObject afn = data.getJSONObject(getCurrency());
                // obtener el valor de la divisa de Afganistán
                this.value = afn.getDouble("value");
                txt2.setText(value + getCurrency());
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setValue(double value){ this.value = value; }
    public double getValue(){ return this.value; }

    public DocumentListener listenJTextField(){
        return new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                // Se llama cuando se inserta texto
                updateText();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                // Se llama cuando se elimina texto
                updateText();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                // Se llama cuando se modifica el texto
                updateText();
            }

            // Método que realiza la acción deseada cuando cambia el texto
            public void updateText() {
                // Obtener el texto del campo txt1
                String text = txt1.getText();
                // Intentar convertirlo a un número
                try {
                    double value = Double.parseDouble(text);
                    // Calcular el resultado y mostrarlo en el campo txt2
                    double result = value * getValue();
                    txt2.setText(result + getCurrency());
                } catch (NumberFormatException ex) {
                    // Si el texto no es un número válido, mostrar un mensaje de error
                    txt2.setText("Por favor, ingrese un número válido");
                }
            }
        };
    }
}
