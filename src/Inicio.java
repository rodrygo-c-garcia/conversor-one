import javax.swing.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

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
        fillCombobox();
    }

    public void fillCombobox(){
        try {
            URL url = new URL(URLEnum.API_URL.getValue());
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.connect();

            int responseCode = con.getResponseCode();

            // si el estado es distinto lanzamos un error
            if(responseCode != 200) {
                throw new RuntimeException("HttpResponseCode: " + responseCode);
            } else {
                StringBuilder informacion = new StringBuilder();
                Scanner scanner = new Scanner(url.openStream());
                
                while(scanner.hasNext()) {
                    informacion.append(scanner.nextLine());
                }

                scanner.close();

                System.out.println(informacion);
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
}
