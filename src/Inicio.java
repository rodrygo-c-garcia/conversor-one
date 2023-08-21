import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;
import java.util.List;

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

        //  funciones de la funcionalidad del sistema
        connectAPI();
        selectedCodeCountry();

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

    public List<String> orderCodeCountry(List<String> codesCountry){
        Collections.sort(codesCountry);
        System.out.println("Ordenado: " + codesCountry);
        return codesCountry;
    }

    public void selectedCodeCountry(){
        comboBox1.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                // comprobar si el evento es de tipo selección
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    // obtener el valor seleccionado en el JComboBox
                    String baseCurrency = (String) e.getItem();
                    // imprimir el valor seleccionado en la consola
                    txt2.setText(baseCurrency);
//                    System.out.println("El código seleccionado es: " + selectedCode);
                }
            }
        });
    }
}
