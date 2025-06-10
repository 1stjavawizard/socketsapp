import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Clients {
    public static void main(String[] args) {
        try (Socket client = new Socket("localhost", 8000);
                PrintWriter out = new PrintWriter(client.getOutputStream(), true); // auto-flush
                BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
                Scanner pen = new Scanner(System.in)) {

            // Thread for receiving messages from the server
            Thread receiveThread = new Thread(() -> {
                try {
                    String message;
                    while ((message = in.readLine()) != null) {
                        System.out.println("Server: " + message);
                    }
                } catch (IOException e) {
                    System.err.println("Error receiving message: " + e.getMessage());
                }
            });
            receiveThread.start();

            // Main thread for sending messages
            String userInput;
            while (true) {
                System.out.println("Clients Message: ");
                userInput = pen.nextLine();
                out.println(userInput); // Auto-flush sends immediately
                if ("exit".equalsIgnoreCase(userInput)) {
                    break;
                }
            }

            receiveThread.interrupt(); // Stop the receive thread
        } catch (IOException err) {
            System.err.println("Client error: " + err.getMessage());
        }
    }
}