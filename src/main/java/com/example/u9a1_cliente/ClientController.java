package com.example.u9a1_cliente;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class ClientController implements Initializable {

    @FXML
    private AnchorPane app_messenger;
    @FXML
    private ScrollPane sp_main;
    @FXML
    private ScrollPane sp_client_list;
    @FXML
    private ScrollPane sp_file_list;
    @FXML
    private VBox vbox_messenger;
    @FXML
    private VBox client_list;
    @FXML
    private VBox file_list;
    @FXML
    private TextField tf_messenger;
    @FXML
    private Label label;
    @FXML
    private Label comment;

    private Client client;
    private String username = "Client";
    private File[] fileToSend = new File[1];
    static ArrayList<MyFile> myFiles = new ArrayList<>();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        label.setText(username);

        try {
            client = new Client(new Socket("localhost", 1234), username);
        } catch (IOException e) {
            e.printStackTrace();
        }
        vbox_messenger.heightProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
                sp_main.setVvalue((Double) t1);
            }
        });

        /// MESSENGER FILE LIST
        JFrame jFrame = new JFrame("Messenger File List");
        jFrame.setSize(400, 400);
        jFrame.setLayout(new BoxLayout(jFrame.getContentPane(), BoxLayout.Y_AXIS));
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel jPanel = new JPanel();
        jPanel.setLayout(new BoxLayout(jPanel, BoxLayout.Y_AXIS));

        JScrollPane jScrollPane = new JScrollPane(jPanel);
        jScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        JLabel jTitle = new JLabel(username + ": Messenger Files");
        jTitle.setFont(new Font("Arial", Font.BOLD, 25));
        jTitle.setBorder(new EmptyBorder(20, 0, 10, 0));
        jTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        jFrame.add(jTitle);
        jFrame.add(jScrollPane);
        jFrame.setVisible(true);
        ///

        client.receiveMessage(vbox_messenger, jFrame, jPanel, client_list);

    }

    public void sendMessage(ActionEvent actionEvent) throws IOException {
        String message = tf_messenger.getText();

        if (fileToSend[0] != null) {
            FileInputStream fileInputStream = new FileInputStream(fileToSend[0].getAbsolutePath());

            String fileName = fileToSend[0].getName();
            byte[] fileNameBytes = fileName.getBytes();

            byte[] fileContentBytes = new byte[(int) fileToSend[0].length()];
            fileInputStream.read(fileContentBytes);

            client.sendFile(fileNameBytes, fileContentBytes);
            fileInputStream.close();

            // resetting file to send
            fileToSend[0] = null;

        } else if (!message.isEmpty()) {
            HBox hBox = new HBox();
            hBox.setAlignment(Pos.CENTER_RIGHT);
            hBox.setPadding(new Insets(5, 5, 5, 10));

            Text text = new Text(message);
            TextFlow textFlow = new TextFlow(text);
            textFlow.setStyle("-fx-color: rgb(239,242,255); " +
                    "-fx-background-color: rgb(15,125,242); " +
                    "-fx-background-radius: 20px;");
            textFlow.setPadding(new Insets(5, 10, 5, 10));
            text.setFill(Color.color(0.934, 0.945, 0.996));

            hBox.getChildren().add(textFlow);
            vbox_messenger.getChildren().add(hBox);

            client.sendMessage(username + "-" + message);
            tf_messenger.clear();
        }

    }

    public void setClientName(String username) {
        this.username = username;
    }

    public void attachFile(ActionEvent actionEvent) throws IOException {
        JFileChooser jFileChooser = new JFileChooser();
        jFileChooser.setDialogTitle("Choose file to send");

        if (jFileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            fileToSend[0] = jFileChooser.getSelectedFile();
            comment.setText("The file you want to send is: " + fileToSend[0].getName());
        }

    }

    public void attachEmoji(ActionEvent actionEvent) throws IOException {
    }

    public void createGroupchat(ActionEvent actionEvent) throws IOException {
    }

    public static void addClients() {

    }

    public static void addLabel(String message, VBox vBox) {
        String name = message.split("-")[0];
        String msgFromServer = message.split("-")[1];

        HBox hBoxName = new HBox();
        hBoxName.setAlignment(Pos.CENTER_LEFT);
        Text textName = new Text(name);
        TextFlow textFlowName = new TextFlow(textName);
        hBoxName.getChildren().add(textFlowName);

        HBox hBox = new HBox();
        hBox.setAlignment(Pos.CENTER_LEFT);
        hBox.setPadding(new Insets(5, 5, 5, 10));

        Text text = new Text(msgFromServer);
        TextFlow textFlow = new TextFlow(text);
        textFlow.setStyle("-fx-background-color: rgb(233,233,235); " +
                "-fx-background-radius: 20px;");
        textFlow.setPadding(new Insets(5, 10, 5, 10));
        hBox.getChildren().add(textFlow);

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                vBox.getChildren().add(hBoxName);
                vBox.getChildren().add(hBox);
            }
        });
    }

    public static void addFile(Integer fileId, String fileName, byte[] fileContentBytes, JFrame jFrame, JPanel jPanel) {

        JPanel jpFileRow = new JPanel();
        jpFileRow.setLayout(new BoxLayout(jpFileRow, BoxLayout.Y_AXIS));

        JLabel jFilename = new JLabel(fileName);
        jFilename.setFont(new Font("Arial", Font.BOLD, 20));
        jFilename.setBorder(new EmptyBorder(10, 0, 10, 0));

        if (getFileExtension(fileName).equalsIgnoreCase("txt")) {
            jpFileRow.setName(String.valueOf(fileId));
            jpFileRow.addMouseListener(getMyMouseListener());

            jpFileRow.add(jFilename);
            jPanel.add(jpFileRow);
            jFrame.validate();
        } else {
            jpFileRow.setName(String.valueOf(fileId));
            jpFileRow.addMouseListener(getMyMouseListener());

            jpFileRow.add(jFilename);
            jPanel.add(jpFileRow);
            jFrame.validate();
        }
        myFiles.add(new MyFile(fileId, fileName, fileContentBytes, getFileExtension(fileName)));

        /*
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                vBox.getChildren().add(hBox);
            }
        });
        */
    }

    public static MouseListener getMyMouseListener() {
        return new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                JPanel jPanel = (JPanel) e.getSource();

                int fileId = Integer.parseInt(jPanel.getName());
                for (MyFile myFile: myFiles) {
                    if (myFile.getId() == fileId) {
                        JFrame jPreview = createFrame(myFile.getName(), myFile.getData(), myFile.getFileExtension());
                        jPreview.setVisible(true);
                    }
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        };
    }

    public static JFrame createFrame(String fileName, byte[] fileData, String fileExtension) {
        JFrame jFrame = new JFrame("File Downloader");
        jFrame.setSize(400, 400);

        JPanel jPanel = new JPanel();
        jPanel.setLayout(new BoxLayout(jPanel, BoxLayout.Y_AXIS));

        JLabel jTitle = new JLabel("File Downloader");
        jTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        jTitle.setFont(new Font("Arial", Font.BOLD, 25));
        jTitle.setBorder(new EmptyBorder(20, 0, 10, 0));

        JLabel jPrompt = new JLabel("Want to download " + fileName + "?");
        jPrompt.setFont(new Font("Arial", Font.BOLD, 20));
        jPrompt.setBorder(new EmptyBorder(20, 0, 10, 0));
        jPrompt.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton jYes = new JButton("Yes");
        jYes.setPreferredSize(new Dimension(150, 75));
        jYes.setFont(new Font("Arial", Font.BOLD, 20));

        JButton jNo = new JButton("No");
        jNo.setPreferredSize(new Dimension(150, 75));
        jNo.setFont(new Font("Arial", Font.BOLD, 20));

        JLabel jFileContent = new JLabel();
        jFileContent.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel jButtons = new JPanel();
        jButtons.setBorder(new EmptyBorder(20, 0, 10, 0));

        jButtons.add(jYes);
        jButtons.add(jNo);

        if (fileExtension.equalsIgnoreCase("txt")) {
            jFileContent.setText("<html>" + new String(fileData) + "</html>");
        } else {
            jFileContent.setIcon(new ImageIcon(fileData));
        }

        jYes.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                File fileToDownload = new File(fileName);
                try {
                    FileOutputStream fileOutputStream = new FileOutputStream(fileToDownload);
                    fileOutputStream.write(fileData);
                    fileOutputStream.close();

                    jFrame.dispose();
                } catch (IOException ex1) {
                    System.out.println("Error downloading file");
                    ex1.printStackTrace();
                }
            }
        });

        jNo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                jFrame.dispose();
            }
        });

        jPanel.add(jTitle);
        jPanel.add(jPrompt);
        jPanel.add(jFileContent);
        jPanel.add(jButtons);

        jFrame.add(jPanel);

        return jFrame;
    }

    public static String getFileExtension (String filename) {
        int i = filename.lastIndexOf(".");

        if (i > 0) {
            return filename.substring(i+1);
        } else {
            return "No extension found";
        }
    }

    public void shutdown() {
        client.sendMessage(username + "-left");
    }
}
