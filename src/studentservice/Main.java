package studentservice;

import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            StudentServiceGUI gui = new StudentServiceGUI();
            gui.setVisible(true);
        });
    }
}
