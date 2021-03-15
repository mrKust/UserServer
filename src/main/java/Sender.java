import com.fasterxml.jackson.databind.ObjectMapper;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class Sender {

    static JFrame frame;
    static JButton buttonSend;
    static JTextField textField;

    private static class inputToSocket {
        Socket socket = null;
        BufferedReader keyboard = null;
        DataOutputStream outputStream = null;

        public inputToSocket(Socket inSocket) {
            try {
                socket = inSocket;
                keyboard = new BufferedReader(new InputStreamReader(System.in));
                outputStream = new DataOutputStream(socket.getOutputStream());
                ActionListener actionListener = new ActionListener();
                buttonSend.addActionListener(actionListener);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public class ActionListener implements java.awt.event.ActionListener {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    ObjectMapper objectMapper = new ObjectMapper();//для работы с json
                    String message = textField.getText();
                    textField.setText("");
                    outputStream.writeUTF(message);
                    outputStream.flush();
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            }
        }
    }

    public static void main(String[] args) {
        try { Socket socket = new Socket("localhost", 3345);
            frame = new JFrame("Окно отправителя");
            frame.setSize(600, 400);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setLayout(new GridLayout());
            buttonSend = new JButton("Отправить сообщение");
            frame.add(buttonSend);
            textField = new JTextField(30);
            frame.add(textField);
            frame.setVisible(true);
            inputToSocket input = new inputToSocket(socket);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}