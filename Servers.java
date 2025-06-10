import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Servers {
    public static void main(String[] args) {
        try (ServerSocket server = new ServerSocket(8000)) {
            System.out.println("Waiting for connection...");
            Socket communication = server.accept();
            System.out.println("Client accepted");

            BufferedReader in = new BufferedReader(new InputStreamReader(communication.getInputStream()));
            PrintWriter out = new PrintWriter(communication.getOutputStream(), true); // auto-flush
            BufferedReader any = new BufferedReader(new InputStreamReader(System.in));

            // Start receiving thread first
            Thread receiveThread = new Thread(() -> {
                try {
                    String message;
                    while ((message = in.readLine()) != null) {
                        System.out.println("Client: " + message);
                    }
                } catch (IOException e) {
                    System.err.println("Error receiving message: " + e.getMessage());
                }
            });
            receiveThread.start();

            // Sending messages
            String msg;
            while ((msg = any.readLine()) != null) {
                out.println(msg);
                if ("exit".equalsIgnoreCase(msg)) {
                    break;
                }
            }

            // Cleanup
            receiveThread.interrupt();
            communication.close();
        } catch (IOException e) {
            System.err.println("Server error: " + e.getMessage());
        }
    }
}