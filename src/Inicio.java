import javax.swing.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

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
    }

    public void fillCombobox(){
        try {
            URL url = new URL(URLEnum.API_URL.getValue());
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.connect();
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
}
