package ir.ac.sbu.net.thread;

import ir.ac.sbu.net.common.entity.Slave;
import ir.ac.sbu.net.conf.Conf;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.Socket;

public class HelloThread implements Runnable {
    private Conf conf;

    public HelloThread(Conf conf) {
        this.conf = conf;
    }

    @Override
    public void run() {
        while (true) {
            try {
                sayHello();
            } catch (IOException e) {
                e.printStackTrace();
                break;
            }
            try {
                Thread.sleep(conf.getAliveTime() * 1000);
            } catch (InterruptedException e) {
                break;
            }
        }
    }

    private void sayHello() throws IOException {
        Socket socket = new Socket(conf.getMasterIp(), conf.getMasterRegisterPort());
        OutputStream outputStream = socket.getOutputStream();
        PrintStream ps = new PrintStream(outputStream);
        ps.println("IP: " + conf.getIp());
        ps.println("PORT: " + conf.getPort());
        for (String file : conf.getFiles()) {
            ps.println("FILE: " + file);
        }
        ps.close();
        outputStream.close();
        socket.close();
    }
}
