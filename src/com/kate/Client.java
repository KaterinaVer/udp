package com.kate;

import java.io.*;
import java.net.*;
import java.util.Date;


public class Client {

    public Client() {
    }

    public static void main(String[] args) throws IOException {
        BufferedReader keyboard = new BufferedReader(new InputStreamReader(System.in));

        System.out.println("Establish connection");
        String str = keyboard.readLine();
        String[] inputString = str.split(" ");

        try (FileWriter writer = new FileWriter("log.txt", false)) {
            if (inputString[0].equals("connect")) {
                String host = inputString[1];
                DatagramSocket socket = new DatagramSocket();
                InetAddress address = InetAddress.getByName(host);
                int port = Integer.parseInt(inputString[2]);
                Date startConnection = new Date();
                str = "Start connection:" + startConnection;
                writeToLog(str, writer);

                System.out.println("Enter command:");
                str = keyboard.readLine();
                byte[] buf = str.getBytes();
                DatagramPacket packet = new DatagramPacket(buf, buf.length, address, port);
                socket.send(packet);
                str = "Dispatch string:" + str;
                writeToLog(str, writer);
                String message = "";
                Date dispatchTime = new Date();
                str = "Dispatch time:" + dispatchTime;
                writeToLog(str, writer);
                while (true) {
                    packet = new DatagramPacket(buf, buf.length);
                    socket.receive(packet);
                    String received = new String(packet.getData(), 0, packet.getLength());
                    try {
                        Integer.parseInt(received);
                        break;
                    } catch (NumberFormatException e) {
                    }
                    message += received;
                }
                System.out.println(message);
                Date receiptTime = new Date();
                str = "Receipt time:" + receiptTime;
                writeToLog(str, writer);
                str = "Receipt string:" + message;
                writeToLog(str, writer);
                Date endConnection = new Date();
                str = "End connection:" + endConnection;
                writeToLog(str, writer);
            }
        }
    }

    private static void writeToLog(String str, FileWriter writer) throws IOException {
        writer.write(str);
        writer.append('\n');
        writer.flush();
    }
}