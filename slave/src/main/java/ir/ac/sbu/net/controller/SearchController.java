package ir.ac.sbu.net.controller;

import ir.ac.sbu.net.common.entity.Slave;
import ir.ac.sbu.net.conf.Conf;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class SearchController {
    private Conf conf;

    public SearchController(Conf conf) {
        this.conf = conf;
    }

    public List<Slave> search(String query) {
        try {
            Socket socket = new Socket(conf.getMasterIp(), conf.getMasterSearchPort());
            OutputStream outputStream = socket.getOutputStream();
            InputStream inputStream = socket.getInputStream();
            PrintStream printWriter = new PrintStream(outputStream);
            printWriter.println("QUERY: " + query);
            printWriter.println("");
            printWriter.flush();
            Scanner scanner = new Scanner(inputStream);
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if (line.startsWith("HTTP/1.1") && !line.contains("200")) {
                    return new ArrayList<>();
                }
                if (line.isEmpty()) {
                    break;
                }
            }
            List<Slave> slaves = new ArrayList<>();
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] ipPort = line.split(" ");
                String ip = ipPort[0];
                int port = Integer.parseInt(ipPort[1]);
                slaves.add(new Slave(ip, port, null));
            }
            printWriter.close();
            scanner.close();
            inputStream.close();
            socket.close();
            return slaves;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void download(Slave slave, String file) {
        try {
            Socket socket = new Socket(slave.getIp(), slave.getPort());
            OutputStream outputStream = socket.getOutputStream();
            PrintStream printStream = new PrintStream(outputStream);
            printStream.println("DOWNLOAD: " + file);
            printStream.println();
            printStream.flush();
            OutputStream fileOutputStream = new FileOutputStream(conf.getFilesPath() + "/download/" + file);
            InputStream inputStream = socket.getInputStream();
            int count;
            byte[] buffer = new byte[conf.getBufferSize()];
            while ((count = inputStream.read(buffer)) > 0) {
                fileOutputStream.write(buffer, 0, count);
            }
            fileOutputStream.close();
            inputStream.close();
            printStream.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
