package sample;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.FileChooser;

import javax.swing.*;
import java.awt.event.InputEvent;
import java.io.*;

import static javafx.scene.input.KeyCode.SHIFT;
import static javafx.scene.input.KeyCombination.CONTROL_DOWN;


public class Controller {

    protected FileChooser fc;
    protected File file;
    protected File oldFile;
    protected FileWriter fileWriter;

    @FXML
    VBox vbox;

    @FXML
    MenuBar menuBar;

    @FXML
    Menu fileMenu;
    @FXML
    MenuItem open;
    @FXML
    MenuItem save;
    @FXML
    MenuItem saveAs;

    @FXML
    Menu edit;
    @FXML
    MenuItem delete;
    @FXML
    MenuItem toggleLines;

    @FXML
    Menu help;
    @FXML
    MenuItem about;

    @FXML
    HBox hbox;

    @FXML
    TextArea lineNumbers;
    @FXML
    TextArea editor;

    @FXML
    TextFlow textFlow;
    @FXML
    Text status;


    @FXML
    public void initialize() {
        System.out.println("Program started");
        editor.setWrapText(true);
        delete.setDisable(true);

        //bind both text areas
        editor.scrollTopProperty().bindBidirectional(lineNumbers.scrollTopProperty());

        //create hotkeys
        open.setAccelerator(new KeyCodeCombination(KeyCode.O, CONTROL_DOWN));
        save.setAccelerator(new KeyCodeCombination(KeyCode.S, CONTROL_DOWN));
        saveAs.setAccelerator(new KeyCodeCombination(KeyCode.S, CONTROL_DOWN,KeyCombination.SHIFT_DOWN));

        //meant to update line count
        // TODO update lineCount when backspacing, deleting, or cutting text
        editor.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (event.getCode() == KeyCode.ENTER) {
                    updateLines();
                }
            }
        });
    }

    //Methods named according to their function

    public void openFile(ActionEvent event) {
        try {
            oldFile = new File("");
            fc = new FileChooser();
            FileChooser.ExtensionFilter extentionFilter = new FileChooser.ExtensionFilter("HTML files (*.html)", "*.html");
            fc.getExtensionFilters().add(extentionFilter);
            fc.setTitle("Open File");
            String userDirectoryString = System.getProperty("user.home");
            File userDirectory = new File(userDirectoryString);
            if (!userDirectory.canRead()) {
                userDirectory = new File("c:/");
            }
            fc.setInitialDirectory(userDirectory);
            file = fc.showOpenDialog(null);
            if (file != oldFile) {
                editor.clear();
            }

            BufferedReader bf = new BufferedReader(new FileReader(file));
            String str;
            while ((str = bf.readLine()) != null) {
                editor.appendText(str + "\n");
            }
            oldFile = file;
        } catch (FileNotFoundException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("File not found!");
            alert.showAndWait();

        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (NullPointerException npe) {
            editor.setText("No File Selected");
        }

    }

    public void saveFile(ActionEvent event) {
        try {
            fileWriter = new FileWriter(file);
            fileWriter.write(editor.getText());
            fileWriter.close();
            status.setText("File Saved!");
        } catch (FileNotFoundException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("File not found!");
            alert.showAndWait();

        } catch (IOException ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("An error occured!");
            alert.showAndWait();
        } catch (NullPointerException npe) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("No File Selected");
            alert.showAndWait();
        }
    }

    public void saveFileAs(ActionEvent event) {
        try {
            fc = new FileChooser();
            fc.setTitle("Save File As");
            FileChooser.ExtensionFilter extentionFilter = new FileChooser.ExtensionFilter("HTML files (*.html)", "*.html");
            fc.getExtensionFilters().add(extentionFilter);
            file = fc.showSaveDialog(null);
            fileWriter = new FileWriter(file);
            fileWriter.write(editor.getText());
            fileWriter.close();
        } catch (FileNotFoundException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("File not found!");
            alert.showAndWait();

        } catch (IOException ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("An error occured!");
            alert.showAndWait();
        } catch (NullPointerException npe) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("No File Selected");
            alert.showAndWait();
        }
    }

    public void updateLines() {
        String count = "";
        String lines[] = editor.getText().split("\n", -1);
        for (int i = 0; i <= lines.length; i++) {
            count += i + 1 + " \n";
        }
        lineNumbers.setText(count);
        lineNumbers.setOnScrollFinished(editor.getOnScrollFinished());

    }

    public void toggleLines(ActionEvent event){
        if(lineNumbers.isVisible()) {
            lineNumbers.setVisible(false);
            lineNumbers.setManaged(false);
        }
        else {
            lineNumbers.setVisible(true);
            lineNumbers.setManaged(true);
        }
    }

    public void aboutMenuItemSelected(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("About");
        alert.setHeaderText(null);
        alert.setContentText("Created by Jose Hinojo.");

        alert.showAndWait();
    }


}
