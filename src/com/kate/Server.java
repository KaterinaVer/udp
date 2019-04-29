package com.kate;

import java.io.*;
import java.net.*;

public class Server extends Thread {
    private DatagramSocket socket;
    private byte[] buf = new byte[1024];

    Server() throws SocketException {
        socket = new DatagramSocket(4445);
    }

    public void run() {
        boolean running = true;
        InetAddress ip = null;
        try {
            ip = InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        System.out.println("Host:" + ip);
        while (running) {
            DatagramPacket packet = new DatagramPacket(buf, buf.length);
            try {
                socket.receive(packet);
                InetAddress address = packet.getAddress();
                int port = packet.getPort();
                packet = new DatagramPacket(buf, buf.length, address, port);
                String received = new String(packet.getData(), 0, packet.getLength());
                System.out.println(received);
                String[] inputString = received.split(" ");
                if (inputString[0].equals("load")) {
                    String fileName = inputString[1].trim();
                    String message = readFromFile(fileName);
                    if (message.isEmpty()) {
                        send("File was not found! Connection was closed", address, port);
                        socket.close();
                    } else {
                        send("Text from file:" + message, address, port);
                    }
                } else {
                    send("Command was not found! Connection was closed", address, port);
                    socket.close();
                }
                running = false;
            } catch (IOException e) {
                e.printStackTrace();
                running = false;
            }
        }
        socket.close();
    }

    private String readFromFile(String fileName) throws IOException {
        String message = "";
        try {
            FileReader fileReader = new FileReader(fileName);
            BufferedReader br = new BufferedReader(fileReader);
            String strLine;
            while ((strLine = br.readLine()) != null) {
                message += strLine + " ";
            }
            System.out.println("Message for client - " + message);
        } catch (FileNotFoundException e) {
            System.out.println("File was not found!");
            message = "";
        }
        return message;
    }

    private void send(String msg, InetAddress address, int port) throws IOException {
        String[] message = msg.split("(?<=\\G.{6})");
        DatagramPacket packet;
        for (String m : message) {
            buf = m.getBytes();
            packet = new DatagramPacket(buf, buf.length, address, port);
            socket.send(packet);
        }
        String size = Integer.toString(message.length);
        buf = size.getBytes();
        packet = new DatagramPacket(buf, buf.length, address, port);
        socket.send(packet);
    }
}


