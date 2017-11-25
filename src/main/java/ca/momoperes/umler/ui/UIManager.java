package ca.momoperes.umler.ui;

import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import javax.swing.*;
import java.io.IOException;

public class UIManager {

    private Scene mainScene;
    private Stage mainStage;

    public void start() {
        SwingUtilities.invokeLater(() -> {
            new JFXPanel();
            Platform.runLater(() -> {
                try {
                    createMainWindow();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        });
    }

    private void createMainWindow() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/UMLMain.fxml"));
        Parent root = loader.load();
        mainScene = new Scene(root);
        UMLMainController controller = loader.getController();
        controller.initController(mainScene);

        mainStage = new Stage();
        mainStage.setTitle("UMLer");
        mainStage.setScene(mainScene);
        mainStage.sizeToScene();
        mainStage.setAlwaysOnTop(true);
        mainStage.show();
        mainStage.setAlwaysOnTop(false);
        mainStage.requestFocus();
        mainStage.setOnCloseRequest(event -> System.exit(0));
    }
}
