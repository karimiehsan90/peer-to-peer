package ir.ac.sbu.net;

import ir.ac.sbu.net.common.entity.Slave;
import ir.ac.sbu.net.conf.Conf;
import ir.ac.sbu.net.controller.SearchController;
import ir.ac.sbu.net.thread.FileRequestAcceptThread;
import ir.ac.sbu.net.thread.HelloThread;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

/**
 * Hello world!
 */
public class App {
    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);
        Conf conf = Conf.load();
        new File(conf.getFilesPath(), "download").mkdirs();
        InetAddress inetAddress = InetAddress.getByName(conf.getBind());
        ServerSocket serverSocket = new ServerSocket(conf.getPort(), conf.getBacklog(), inetAddress);
        SearchController controller = new SearchController(conf);
        HelloThread helloRunnable = new HelloThread(conf);
        Thread helloThread = new Thread(helloRunnable);
        helloThread.start();
        FileRequestAcceptThread fileRequestAcceptRunnable = new FileRequestAcceptThread(serverSocket, conf);
        Thread fileRequestAcceptThread = new Thread(fileRequestAcceptRunnable);
        fileRequestAcceptThread.start();
        while (scanner.hasNextLine()) {
            String query = scanner.nextLine();
            List<Slave> slaves = controller.search(query);
            if (slaves.size() > 0) {
                Collections.shuffle(slaves);
                Slave slave = slaves.get(0);
                controller.download(slave, query);
            }
            else {
                System.out.println("Not found");
            }
        }
    }
}
