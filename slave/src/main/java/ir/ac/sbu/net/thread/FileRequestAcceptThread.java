package ir.ac.sbu.net.thread;

import ir.ac.sbu.net.conf.Conf;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class FileRequestAcceptThread implements Runnable {
    private ServerSocket serverSocket;
    private Conf conf;

    public FileRequestAcceptThread(ServerSocket serverSocket, Conf conf) {
        this.serverSocket = serverSocket;
        this.conf = conf;
    }

    @Override
    public void run() {
        while (true) {
            try {
                Socket socket = serverSocket.accept();
                FileThread fileRunnable = new FileThread(socket, conf);
                Thread fileThread = new Thread(fileRunnable);
                fileThread.start();
            } catch (IOException e) {
                break;
            }
        }
    }
}
