import javax.swing.*;

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
}
