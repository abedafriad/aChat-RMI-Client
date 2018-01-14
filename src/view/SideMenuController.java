/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTreeTableView;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;

/**
 * FXML Controller class
 *
 * @author Abed
 */
public class SideMenuController implements Initializable {

    @FXML
    private ImageView img;

    @FXML
    private VBox sidePane;
    
    @FXML
    private JFXTreeTableView table;

    @FXML
    private JFXButton exit;

    @FXML
    private JFXButton disconnect;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        table.setBackground(new Background(new BackgroundFill(Paint.valueOf("#333"), CornerRadii.EMPTY, Insets.EMPTY)));
        // TODO
    }

    public JFXTreeTableView getTable() {
        return table;
    }

    public void setTable(JFXTreeTableView table) {
        this.table = table;
    }

    
    
    public ImageView getImg() {
        return img;
    }

    public void setImg(ImageView img) {
        this.img = img;
    }

    public JFXButton getExit() {
        return exit;
    }

    public void setExit(JFXButton exit) {
        this.exit = exit;
    }

    public VBox getSidePane() {
        return sidePane;
    }

    public void setSidePane(VBox sidePane) {
        this.sidePane = sidePane;
    }

}
