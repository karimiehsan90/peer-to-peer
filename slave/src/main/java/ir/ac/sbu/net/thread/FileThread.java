package ir.ac.sbu.net.thread;

import ir.ac.sbu.net.conf.Conf;

import java.io.*;
import java.net.Socket;

public class FileThread implements Runnable {
    private Socket socket;
    private Conf conf;

    public FileThread(Socket socket, Conf conf) {
        this.socket = socket;
        this.conf = conf;
    }

    @Override
    public void run() {
        BufferedReader reader = null;
        PrintWriter out = null;
        BufferedOutputStream dataOut = null;
        try {
            OutputStream outputStream = socket.getOutputStream();
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(outputStream);
            dataOut = new BufferedOutputStream(outputStream);
            String line;
            String download = null;
            while (((line = reader.readLine()) != null) && !line.isEmpty()) {
                if (line.startsWith("DOWNLOAD: ")) {
                    download = line.substring("DOWNLOAD: ".length());
                }
            }
            if (download == null || download.isEmpty()) {
                out.println("HTTP/1.1 403 Unauthorized");
                out.println();
                out.println("Unauthorized");
            } else {
                InputStream in = new FileInputStream(conf.getFilesPath() + "/" + download);
                int count;
                byte[] buffer = new byte[conf.getBufferSize()];
                while ((count = in.read(buffer)) > 0) {
                    outputStream.write(buffer, 0, count);
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
