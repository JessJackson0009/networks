import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class client {

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        Scanner in2 = new Scanner(System.in);
        System.out.print("Server IP address: ");
        String IP = in.next();

        System.out.print("Port number: ");
        int port = in.nextInt();

        System.out.print("Operation: ");
        String command = in2.nextLine();

        System.out.print("Number of client requests: ");
        int numRequests = in.nextInt();

        long totalTurnaroundTime = 0;

        for (int i = 0; i < numRequests; i++) {
            ClientThread clientThread = new ClientThread(IP, port, command);
            clientThread.start();
            try {
                clientThread.join();
                totalTurnaroundTime += clientThread.getTurnaroundTime();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        double averageTurnaroundTime = (double) totalTurnaroundTime / numRequests;
        System.out.println("Total Turnaround Time: " + totalTurnaroundTime + " ms");
        System.out.println("Average Turnaround Time: " + averageTurnaroundTime + " ms");
    }
}

class ClientThread extends Thread {
    public String IP;
    public int port;
    public String command;

    public long startTime;
    public long endTime;

    public ClientThread(String IP, int port, String command) {
        this.IP = IP;
        this.port = port;
        this.command = command;
    }

    @Override
    public void run() {
        try (Socket s = new Socket(IP, port)) {
            OutputStream output = s.getOutputStream();
            PrintWriter writer = new PrintWriter(output, true);
            writer.println(command);
            startTime = System.currentTimeMillis();

            InputStream input = s.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(input));
            String time = reader.readLine();
            endTime = System.currentTimeMillis();
            System.out.println("Response from server: " + time);
        } catch (UnknownHostException e) {
            System.out.println("Server not found: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
    public long getTurnaroundTime() {
        return endTime - startTime;
    }
}

