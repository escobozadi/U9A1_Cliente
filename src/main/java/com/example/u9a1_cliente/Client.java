package com.example.u9a1_cliente;

import javafx.scene.layout.VBox;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class Client {
    static ArrayList<MyFile> myFiles = new ArrayList<>();

    private Socket socket;
    // private BufferedReader bufferedReader;
    // private BufferedWriter bufferedWriter;
    private DataInputStream dataInputStream;
    private DataOutputStream dataOutputStream;
    public String username;

    public Client(Socket socket, String username) {
        try {
            this.socket = socket;
            // this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            // this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.dataInputStream = new DataInputStream(socket.getInputStream());
            this.dataOutputStream = new DataOutputStream(socket.getOutputStream());

            System.out.println("Client connected");
            this.username = username;
            sendMessage(username);
            sendMessage(username + "-connected");
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error while creating client socket");
            closeEverything(socket, dataInputStream, dataOutputStream);
        }
    }

    public void sendFile(byte[] filename, byte[] fileContent) throws IOException {
        sendMessage("SENDING FILE");

        dataOutputStream.writeInt(filename.length);
        dataOutputStream.write(filename);

        dataOutputStream.writeInt(fileContent.length);
        dataOutputStream.write(fileContent);
    }

    public void sendMessage(String message) {
        try {
            dataOutputStream.writeUTF(message);
            dataOutputStream.flush();
            // bufferedWriter.write(message);
            // bufferedWriter.newLine();
            // bufferedWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error while sending message");
            closeEverything(socket, dataInputStream, dataOutputStream);
        }
    }

    public void receiveMessage(VBox vBox, JFrame jFrame, JPanel jPanel, VBox clientList) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                int fileId = 0;

                while (socket.isConnected()) {
                    try {
                        String message = dataInputStream.readUTF();
                        // String message = bufferedReader.readLine();

                        // RECEIVING FILE
                        if (message.equals("SENDING FILE")) {
                            int fileNameLength = dataInputStream.readInt();
                            if (fileNameLength > 0) {
                                byte[] fileNameBytes = new byte[fileNameLength];
                                dataInputStream.readFully(fileNameBytes, 0, fileNameBytes.length);
                                String fileName= new String(fileNameBytes);

                                int fileContentLength = dataInputStream.readInt();
                                if (fileContentLength > 0) {
                                    byte[] fileContentBytes = new byte[fileContentLength];

                                    dataInputStream.readFully(fileContentBytes, 0, fileContentLength);
                                    ClientController.addFile(fileId, fileName, fileContentBytes, jFrame, jPanel);
                                }
                            }

                        } else {
                            ClientController.addLabel(message, vBox);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                        System.out.println("Error while receiving message");
                        closeEverything(socket, dataInputStream, dataOutputStream);
                    }
                }
            }
        }).start();
    }

    public void closeEverything(Socket socket, DataInputStream bufferedReader, DataOutputStream bufferedWriter) {
        try {
            if (bufferedReader != null) {
                bufferedReader.close();
            }
            if (bufferedWriter != null){
                bufferedWriter.close();
            }
            if (socket != null) {
                socket.close();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
