package com.kate;

import java.net.SocketException;

public class Main {
    public static void main(String[] args)  {
        try {
            new Server().start();
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }
}
