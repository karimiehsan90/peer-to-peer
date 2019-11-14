package ir.ac.sbu.net.thread;

import ir.ac.sbu.net.dao.DAO;
import ir.ac.sbu.net.common.entity.Slave;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class RegisterThread implements Runnable {
    private Socket socket;
    private DAO dao;

    public RegisterThread(Socket socket, DAO dao) {
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
            String ip = null;
            int port = 0;
            List<String> files = new ArrayList<>();
            while (((line = reader.readLine()) != null) && !line.isEmpty()) {
                if (line.startsWith("IP: ")) {
                    ip = line.substring("IP: ".length());
                }
                if (line.startsWith("PORT: ")) {
                    port = Integer.parseInt(line.substring("PORT: ".length()));
                }
                if (line.startsWith("FILE: ")) {
                    files.add(line.substring("FILE: ".length()));
                }
            }
            if (ip == null || ip.isEmpty() || port == 0) {
                out.println("HTTP/1.1 403 Unauthorized");
                out.println();
                out.println("Unauthorized");
            } else {
                Slave slave = new Slave(ip, port, files);
                dao.registerSlave(slave);
                out.println("HTTP/1.1 200 OK");
                out.println();
                out.println("OK");
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
