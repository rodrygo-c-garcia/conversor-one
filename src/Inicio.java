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
    private JLabel labelCurrencyBase;
    private JLabel labelCurrency;
    private JLabel countryNameBase;
    private JLabel countryNameOutput;

    // data for the API
    private String baseCurrency = "BOB";
    private String currency = "BOB";
    private double valueCurrency = 1.0;
    private double valueQuantity = 1.0;
    private ApiClient apiClient;
    HashMap<String, String> diccionario;

    public Inicio() {
        super("Conversor ONE");
        setSize(450, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
        setContentPane(panelMain);
        txt2.setText("Cargando...");

        //  funciones de la funcionalidad del sistema
        startCountries();
        apiClient = new ApiClient();
        connectAPI();
        selectedCodeBaseCountry();
        selectedCurrency();


        // Crear un objeto DocumentListener que realiza una acción cuando cambia el texto
        DocumentListener dl = listenJTextField();
        // Agregar el DocumentListener al campo txt1
        txt1.getDocument().addDocumentListener(dl);
    }

    public void startCountries(){
        this.diccionario = new HashMap<>(10, 0.8f);
        // Insertar el par ("USD", "Dólar estadounidense de Estados Unidos") en el HashMap
        diccionario.put("USD", "Dólar estadounidense de USA");

        // Insertar el par ("BOB", "Boliviano de Bolivia") en el HashMap
        diccionario.put("BOB", "Boliviano de Bolivia");

        // Insertar el par ("EUR", "Euro de la Unión Europea") en el HashMap
        diccionario.put("EUR", "Euro de la Unión Europea");

        // Insertar el par ("GBP", "Libra esterlina del Reino Unido") en el HashMap
        diccionario.put("GBP", "Libra esterlina del Reino Unido");

        // Insertar el par ("JPY", "Yen de Japón") en el HashMap
        diccionario.put("JPY", "Yen de Japón");

        // Insertar el par ("KRW", "Won surcoreano de Corea del Sur") en el HashMap
        diccionario.put("KRW", "Won surcoreano de Corea del Sur");

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
            obtainCurrency();
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
                    txt2.setText("Cargando...");
                    // imprimir el valor seleccionado en la consola
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
                // crear un objeto JSONObject con la cadena informacion
                JSONObject json = new JSONObject(informacion.toString());
                // obtener el objeto JSONObject asociado a la clave "data"
                JSONObject data = json.getJSONObject("data");
                // obtener el objeto JSONObject asociado a la clave
                JSONObject afn = data.getJSONObject(getCurrency());
                // obtener el valor de la divisa de Afganistán
                setValueCurrency(afn.getDouble("value"));
                txt2.setText(((Math.round(this.valueCurrency * 100000) * getValueQuantity()) / 100000.0) + getCurrency());
                searchCountry();
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void searchCountry(){
        String valorBase = diccionario.get(getBaseCurrency());
        String valorOutput = diccionario.get(getCurrency());
        // Asignar el valor al JLabel countryNameBase
        countryNameBase.setText(valorBase);
        countryNameOutput.setText(valorOutput);

    }
    public void setValueCurrency(double value){ this.valueCurrency = value; }
    public double getValueCurrency(){ return this.valueCurrency; }

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
                // Intentar convertirlo a un número
                try {
                    setValueQuantity(Double.parseDouble(txt1.getText()));
                    // Calcular el resultado y mostrarlo en el campo txt2
                    double value = getValueQuantity() * getValueCurrency();
                    txt2.setText((Math.round(value * 100000) / 100000.0) + getCurrency());
                } catch (NumberFormatException ex) {
                    // Si el texto no es un número válido, mostrar un mensaje de error
                    txt2.setText("Ingrese un número válido");
                }
            }
        };
    }

    public void setValueQuantity(double valueQuantity) {
        this.valueQuantity = valueQuantity;
    }
    public double getValueQuantity() {
        return this.valueQuantity;
    }
}
