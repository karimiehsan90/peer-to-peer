package ir.ac.sbu.net.thread;

import ir.ac.sbu.net.dao.DAO;
import ir.ac.sbu.net.entity.Slave;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class SearchThread implements Runnable {
    private Socket socket;
    private DAO dao;

    public SearchThread(Socket socket, DAO dao) {
        this.socket = socket;
        this.dao = dao;
    }

    @Override
    public void run() {
        BufferedReader reader = null;
        PrintWriter out = null;
        BufferedOutputStream dataOut = null;
        try {
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream());
            dataOut = new BufferedOutputStream(socket.getOutputStream());
            String line;
            String query = null;
            while (((line = reader.readLine()) != null) && !line.isEmpty()) {
                if (line.startsWith("QUERY: ")) {
                    query = line.substring("QUERY: ".length());
                }
            }
            if (query == null || query.isEmpty()) {
                out.println("HTTP/1.1 403 Unauthorized");
                out.println();
                out.println("Unauthorized");
            } else {
                Set<Slave> slaves = dao.search(query);
                if (slaves != null && slaves.size() > 0) {
                    out.println("HTTP/1.1 200 OK");
                    out.println();
                    for (Slave slave : slaves) {
                        out.println(slave.getIp() + " " + slave.getPort());
                    }
                }
                else {
                    out.println("HTTP/1.1 404 Not found");
                    out.println();
                    out.println("Not fount");
                }
            }
        } catch (Exception e) {
            out.println("HTTP/1.1 403 Unauthorized");
            out.println();
            out.println("Unauthorized");
        } finally {
            try {
                out.flush();
                reader.close();
                out.close();
                dataOut.close();
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
