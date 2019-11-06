package ir.ac.sbu.net.thread;

import ir.ac.sbu.net.dao.DAO;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerSearchThread implements Runnable {
    private ServerSocket serverSocket;
    private DAO dao;

    public ServerSearchThread(ServerSocket serverSocket, DAO dao) {
        this.serverSocket = serverSocket;
        this.dao = dao;
    }

    @Override
    public void run() {
        while (true) {
            try {
                Socket accept = serverSocket.accept();
                SearchThread searchThread = new SearchThread(accept, dao);
                Thread thread = new Thread(searchThread);
                thread.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
