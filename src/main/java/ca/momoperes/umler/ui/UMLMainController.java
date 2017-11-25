package ca.momoperes.umler.ui;

import ca.momoperes.umler.uml.UML;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

public class UMLMainController {
//    @FXML
//    private MenuBar menuBar;

    @FXML
    private StackPane dropPane;

    public void initController(Scene scene) {
        //menuBar.prefWidthProperty().bind(scene.widthProperty());
        dropPane.setCursor(Cursor.HAND);
        dropPane.setOnDragOver(event -> {
            Dragboard db = event.getDragboard();
            if (db.hasFiles() && db.getFiles().size() == 1 && supportsFile(db.getFiles().get(0))) {
                event.acceptTransferModes(TransferMode.COPY);
            } else {
                event.consume();
            }
        });

        dropPane.setOnDragDropped(event -> {
            Dragboard db = event.getDragboard();
            boolean success = false;
            if (db.hasFiles() && db.getFiles().size() == 1 && supportsFile(db.getFiles().get(0))) {
                File file = db.getFiles().get(0);
                createUML(file);
                success = true;
            }
            event.setDropCompleted(success);
            event.consume();
        });

        dropPane.setOnMouseClicked(event -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("UMLer - Open Java source file");
            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("Java Source File", "*.java")
            );
            File file = fileChooser.showOpenDialog(scene.getWindow());
            if (file != null) {
                createUML(file);
            }
        });
    }

    private boolean supportsFile(File file) {
        return file.getName().toLowerCase().endsWith(".java");
    }

    private void createUML(File file) {
        System.out.println("Loading: " + file.getAbsolutePath());
        UML uml = UML.forFile(file);
        if (uml == null) {
            System.out.println("not supported yet :(");
            return;
        }
        try {
            uml.load(file);
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Something wrong happened when trying to generate that UML.");
            alert.setContentText(e.getMessage());
            alert.show();
            return;
        }
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/UMLViewer.fxml"));
            Parent root = loader.load();
            Scene umlScene = new Scene(root);
            UMLViewerController controller = loader.getController();
            controller.init(umlScene, uml);

            Stage umlStage = new Stage();
            umlStage.setTitle("UML - " + uml.getClassName());
            umlStage.setScene(umlScene);
            umlStage.setResizable(false);
            umlStage.sizeToScene();
            umlStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
