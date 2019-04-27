package com.kate;

import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Server extends Thread {

    private DatagramSocket socket;
    private boolean running;
    private byte[] buf = new byte[1024];

    public Server() throws SocketException {
        socket = new DatagramSocket(4445);
    }

    public void run() {
        running = true;

        while (running) {
            DatagramPacket packet
                    = new DatagramPacket(buf, buf.length);
            try {
                socket.receive(packet);

                InetAddress address = packet.getAddress();
                int port = packet.getPort();
                packet = new DatagramPacket(buf, buf.length, address, port);
                String received = new String(packet.getData(), 0, packet.getLength());
                System.out.println(received);

                String[] inputString = received.split(" ");
                String fileName = inputString[1].trim();
                if (inputString[0].equals("load")) {
                    String message = readFromFile(fileName);
                    if (message.isEmpty()) {
                        send("Connection was closed", address, port);
                        socket.close();
                    } else {
                        send("Text from file:" + message, address, port);
                    }
                }
                running = false;
            } catch (IOException e) {
                e.printStackTrace();
                running = false;
            }
        }
        socket.close();
    }

    public String readFromFile(String fileName) {
        String message = "";

        try {
            FileReader fileReader = new FileReader(fileName);
            BufferedReader br = new BufferedReader(fileReader);
            String strLine;
            while ((strLine = br.readLine()) != null) message += strLine + " ";
            System.out.println("Message for client - " + message);

        } catch (FileNotFoundException e) {
            System.out.println("File was not found!");
            //out.println("File was not found!");
            message = "";
        } catch (IOException e) {
            System.out.println("Error!");
        }
        return message;
    }

    public void send(String msg, InetAddress address, int port) throws IOException {
        String[] message = msg.split("(?<=\\G.{10})");
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
/*import java.io.*;
import java.net.*;

public class Server extends Thread {
    private DatagramSocket socket;
    private byte[] buf = new byte[256];
    private InetAddress address;

    public Server()  {

    }

    public void run() {
        boolean running = true;
        try {
            socket = new DatagramSocket(4445);
        } catch (SocketException e) {
            e.printStackTrace();
        }
        try {
            address = InetAddress.getByName("localhost");
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        try {
            while (running) {
                DatagramPacket packet = new DatagramPacket(buf, buf.length,address,4445);
                socket.receive(packet);

                InetAddress address = packet.getAddress();
                int port = packet.getPort();
                packet = new DatagramPacket(buf, buf.length, address, port);

                String inputData=new String(packet.getData());
System.out.println(inputData);
                String[] inputString = inputData.split(" ");
                String fileName=inputString[1];
                System.out.println("input1"+inputString[0]);
                if (inputString[0].equals("load")) {
                    System.out.println("input2"+fileName+"4");
                    String message = readFromFile("file.txt");
                    if (message.isEmpty()){
                        send("Connection was closed");
                        socket.close();
                    }
                    else {
                        send("text from file.txt: " + message );
                    }
                }
                else {
                    send("Command wasn't found!");
                    running = false;
                    continue;
                }
                String received
                        = new String(packet.getData(), 0, packet.getLength());

                if (received.equals("end")) {
                    running = false;
                    continue;
                }
                socket.send(packet);
            }
            //System.out.println(readFromFile("file.txt"));
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public String readFromFile(String fileName) {
        String message = "";

        try {
            FileReader fileReader = new FileReader(fileName);
            BufferedReader br = new BufferedReader(fileReader);
            String strLine;
            while ((strLine = br.readLine()) != null) message += strLine + " ";
            System.out.println("Message for client - " + message);

        }
        catch (FileNotFoundException e) {
            System.out.println("File was not found!");
            //out.println("File was not found!");
            message = "";
        }
        catch (IOException e) {
            System.out.println("Error!");
        }
        return message;
    }
    public void send(String msg) throws IOException {
        buf = msg.getBytes();
        DatagramPacket packet = new DatagramPacket(buf, buf.length, address, 4445);
        socket.send(packet);
    }
}*/

