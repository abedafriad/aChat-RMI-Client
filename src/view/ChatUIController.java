/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDrawer;
import com.jfoenix.controls.JFXHamburger;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTreeTableColumn;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import com.jfoenix.transitions.hamburger.HamburgerSlideCloseTransition;
import java.io.IOException;

import java.net.URL;
import java.rmi.Naming;

import java.util.ResourceBundle;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TreeTableCell;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.cell.TreeItemPropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;

import service.ChatClient;
import service.ChatClientInt;
import service.ChatServerInt;

/**
 * FXML Controller class
 *
 * @author Abed
 */
public class ChatUIController implements Initializable {

    @FXML
    private AnchorPane homePane;

    @FXML
    private JFXTextField name;

    @FXML
    private JFXTextField ip;

    @FXML
    private JFXButton connect;

    @FXML
    private JFXButton cancel;

    @FXML
    private JFXButton send;

    @FXML
    private VBox vBox;

    @FXML
    private JFXTextField message;

    @FXML
    private JFXDrawer drawer;

    @FXML
    private JFXHamburger burger;

    @FXML
    private Label label;

    @FXML
    private ScrollPane sp;

    private ChatClient client;
    private ChatServerInt server;
    private SpeechBox speechBox;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        homePane.setVisible(true);
        cancel.setOnAction((e) -> {
            System.exit(0);
        });
        send.setBackground(new Background(new BackgroundImage(new Image(getClass().getResourceAsStream("send.png")),
                BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER, BackgroundSize.DEFAULT)));
        send.setMaxSize(45.0, 45.0);
        send.setShape(new Circle(30));
        vBox.setSpacing(10);
        // SIDE MENU
        try {
            initSidemenu();
            moveHamburger();
        } catch (IOException ex) {
            Logger.getLogger(ChatUIController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private void alert(ActionEvent actionEvent, Alert.AlertType alertType, String titre, String contenu) {
        Alert alert = new Alert(alertType);
        alert.setTitle(titre);
        alert.setContentText(contenu);
        alert.setHeaderText(null);
        alert.showAndWait();
    }

    public void doConnect(ActionEvent actionEvent) {
        if (connect.getText().equals("CONNECT")) {
            if (name.getText().length() < 2) {
                alert(actionEvent, Alert.AlertType.ERROR, "Name Error..", "You need to type a name.");
                return;
            }
            if (ip.getText().length() < 2) {
                alert(actionEvent, Alert.AlertType.ERROR, "IP Error..", "You need to type an IP@.");
                return;
            }
            try {
                client = new ChatClient(name.getText());
                label.setText(name.getText());
                client.setCuic(this);
                server = (ChatServerInt) Naming.lookup("rmi://" + ip.getText() + ":1099/acg");
                server.login(client);
                updateUsers(server.getConnected());
                connect.setText("DISCONNECT");
                homePane.setVisible(false);
            } catch (Exception e) {
                e.printStackTrace();
                alert(actionEvent, Alert.AlertType.ERROR, "Connection Failed!!", "ERROR, we wouldn't connect...");
            }
        } else {
            updateUsers(null);
            connect.setText("CONNECT");
            ip.setText("");
            name.setText("");
            client = null;
            server = null;

            //Better to implement Logout ....
        }

    }

    public void sendText(ActionEvent actionEvent) {
        if (connect.getText().equals("CONNECT")) {
            alert(actionEvent, Alert.AlertType.ERROR, "Error!", "You need to connect first.");
            return;
        }
        String st = message.getText();
        if (!st.equals("")) {
            speechBox = new SpeechBox(st, SpeechDirection.RIGHT);
            vBox.getChildren().add(speechBox);
            message.setText("");
            sp.setVvalue(1.0);
            vBox.heightProperty().addListener((observable, oldvalue, newValue) -> {
                sp.setVvalue((Double) newValue);
            });

//Remove if you are going to implement for remote invocation
            try {
                server.publish(st, client.getName());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void updateUsers(Vector v) {

        if (v != null) {
            JFXTreeTableColumn column = (JFXTreeTableColumn) tableView.getColumns().get(0);
            for (int i = 0; i < v.size(); i++) {
                try {
                    String tmp = ((ChatClientInt) v.get(i)).getName();
                    column.setCellFactory(new TreeItemPropertyValueFactory(tmp));
//                    tx.appendText(tmp+" Just Connected");
//                    listModel.addElement(tmp);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        
//        lst.setModel(listModel);
    }

    public void writeMsg(String st) {
        Platform.runLater(() -> {
            speechBox = new SpeechBox(st, SpeechDirection.LEFT);
            vBox.getChildren().add(speechBox);

        });
    }

    /*/*******************************************************
    Side Menu Controls And Methods
    */
    HamburgerSlideCloseTransition transition;

    public void moveHamburger() {
        transition = new HamburgerSlideCloseTransition(burger);
        transition.setRate(-1);
        burger.addEventHandler(MouseEvent.MOUSE_PRESSED, (e) -> {
            transBurger();
        });
    }

    public void transBurger() {
        transition.setRate(transition.getRate() * (-1));
        transition.play();
        moveDrawer();
    }

    public void moveDrawer() {
        if (drawer.isShown()) {
            drawer.close();
        } else {
            drawer.open();
        }
    }

    JFXTreeTableView tableView;

    public void initSidemenu() throws IOException {
//        drawer = new JFXDrawer();
        VBox box = FXMLLoader.load(getClass().getResource("SideMenu.fxml"));
        drawer.setSidePane(box);
        for (Node node : box.getChildren()) {
            if (node.getAccessibleText() != null) {
                if (node.getAccessibleText().equals("TABLE")) {
                    tableView = (JFXTreeTableView) node;
                    tableView.getColumns().add(new JFXTreeTableColumn("test"));
                }
                node.addEventHandler(MouseEvent.MOUSE_CLICKED, (e) -> {
                    switch (node.getAccessibleText()) {
                        case "M1":
                            homePane.setVisible(true);
                            transBurger();
                            break;
                        case "M0":
                            System.exit(0);
                    }
                });
            }
        }
    }

}
