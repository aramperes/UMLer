package ca.momoperes.umler;

import ca.momoperes.umler.ui.UIManager;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;

import javax.swing.*;
import java.util.Arrays;
import java.util.stream.Collectors;

public class Main {

    public static final WordprocessingMLPackage pkg;

    public static void main(String[] args) {
        UIManager ui = new UIManager();
        ui.start();
    }

    static {
        JDialog dialog = new JDialog();
        dialog.setContentPane(new JLabel("    Loading, please wait."));
        dialog.setResizable(false);
        dialog.setSize(300, 80);
        dialog.setTitle("UMLer - Loading");
        dialog.setLocationRelativeTo(null);
        dialog.setFocusableWindowState(false);
        dialog.setModal(false);
        dialog.setVisible(true);
        WordprocessingMLPackage pkg1;
        try {
            pkg1 = WordprocessingMLPackage.createPackage();
            dialog.setVisible(false);
        } catch (Exception e) {
            pkg1 = null;
            dialog.setVisible(false);
            JOptionPane.showMessageDialog(null, "An error occurred :(\n " + e.getMessage() + "\nStack:\n" + Arrays.stream(e.getStackTrace()).map(StackTraceElement::toString).collect(Collectors.joining("\n")));
            System.exit(0);
        }
        pkg = pkg1;
    }
}
