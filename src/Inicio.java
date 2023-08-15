import javax.swing.*;

public class Inicio extends JFrame {
    private JPanel panelMain;
    private JLabel labelTitle;

    public Inicio() {
        super("Conversor ONE");
        setSize(900, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
        add(panelMain);
    }
}
