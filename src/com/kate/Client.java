package com.kate;

import java.io.*;
import java.net.*;
import java.util.Date;


public class Client {
    private static DatagramSocket socket;
    private static InetAddress address;
    private static int port;

    private static byte[] buf = new byte[1024];

    public Client() throws SocketException, UnknownHostException {
        //socket = new DatagramSocket();
        //address = InetAddress.getByName("localhost");
    }

    public static void main(String[] args) throws IOException {
        BufferedReader keyboard = new BufferedReader(new InputStreamReader(System.in));

        System.out.println("Establish connection");
        String str = keyboard.readLine();
        String[] inputString = str.split(" ");

        try (FileWriter writer = new FileWriter("log.txt", false)) {
            if (inputString[0].equals("connect")) {
                String host = inputString[1];
                socket = new DatagramSocket();
                address = InetAddress.getByName(host);
                port = Integer.parseInt(inputString[2]);
                Date startConnection = new Date();
                str = "Start connection:" + startConnection;
                writeToLog(str, writer);

                System.out.println("Enter command:");
                str = keyboard.readLine();
                buf = str.getBytes();
                DatagramPacket packet = new DatagramPacket(buf, buf.length, address, port);
                socket.send(packet);
                str = "Dispatch string:" + str;
                writeToLog(str, writer);
                String message = "";
                boolean numeric = false;
                while (true) {
                    packet = new DatagramPacket(buf, buf.length);
                    socket.receive(packet);
                    String received = new String(
                            packet.getData(), 0, packet.getLength());

                    try {
                        Integer.parseInt(received);
                        break;
                    } catch (NumberFormatException e) {
                    }
                    message += received;
                }
                System.out.println(message);

            }
        }
    }

    private static void writeToLog(String str, FileWriter writer) throws IOException {
        writer.write(str);
        writer.append('\n');
        writer.flush();
    }

    public String sendEcho(String msg) throws IOException {
        buf = msg.getBytes();
        DatagramPacket packet
                = new DatagramPacket(buf, buf.length, address, port);
        socket.send(packet);
        packet = new DatagramPacket(buf, buf.length);
        socket.receive(packet);
        String received = new String(
                packet.getData(), 0, packet.getLength());
        return received;
    }

    public void close() {
        socket.close();
    }
}
/*public class Client {
    private static DatagramSocket socket;
    private static InetAddress address;

    private static byte[] buf;

    public Client() throws SocketException, UnknownHostException {

    }

    public static void main(String[] args) throws IOException {
        socket = new DatagramSocket();
        address = InetAddress.getByName("localhost");
        BufferedReader keyboard = new BufferedReader(new InputStreamReader(System.in));

        System.out.println("Establish connection");
        String str = keyboard.readLine();
        String[] inputString = str.split(" ");

        try(FileWriter writer = new FileWriter("log.txt", false)) {
            if (inputString[0].equals("connect")) {
                String host = inputString[1];
                String port = inputString[2];
                Date startConnection = new Date();
                str = "Start connection:" + startConnection;
                writer.write(str);
                writer.append('\n');
                writer.flush();

                System.out.println("Enter command:");
                str=keyboard.readLine();
                buf = str.getBytes();
                DatagramPacket packet = new DatagramPacket(buf, buf.length, address, 4445);
                socket.send(packet);
                str = "Dispatch string:" + str;
                writer.write(str);
                writer.append('\n');
                writer.flush();

                Date dispatchTime = new Date();
                str = "Dispatch time:" + dispatchTime;
                writer.write(str);
                writer.append('\n');
                writer.flush();
                packet = new DatagramPacket(buf, buf.length);
                socket.receive(packet);
                String received = new String(
                        packet.getData(), 0, packet.getLength());
                System.out.println(received);

                Date receiptTime = new Date();
                str = "Receipt time:" + receiptTime;
                writer.write(str);
                writer.append('\n');
                writer.flush();
                packet = new DatagramPacket(buf, buf.length);
                socket.receive(packet);
                received = new String(
                        packet.getData(), 0, packet.getLength());
                System.out.println(received);
                writer.write(received);
                writer.append('\n');
                writer.flush();
                packet = new DatagramPacket(buf, buf.length);
                socket.receive(packet);
                received = new String(
                        packet.getData(), 0, packet.getLength());
                str = received + "";
                if (str.equals("Connection was closed")) {
                    Date endConnection = new Date();
                    System.out.println(str);
                    str = "End connection:" + endConnection;
                    writer.write(str);
                    writer.append('\n');
                    writer.flush();
                }
                if (str.isEmpty()) {
                    System.out.println(str);
                }
            }
        }
    }

    public void close() {
        socket.close();
    }
}*/
