import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class server {

	public static void main(String[] args) {
		
		
		Scanner in = new Scanner(System.in);
		System.out.print("Port to connect to: ");
		
		int port = in.nextInt();
		
		
		try (ServerSocket ServerSocket = new ServerSocket(port)){
			
			System.out.println("Server is listening on port " + port);
			
			while(true) {
				
				Socket socket = ServerSocket.accept();
				System.out.println("new client connected");
				
				ServerThread ServerThread = new ServerThread(socket);
				ServerThread.start();
				
			}
			
		}
		catch(IOException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		
	}
	}
	
}
	class ServerThread extends Thread {
		private Socket socket;
		
		public ServerThread(Socket socket) {
			this.socket = socket;
		}
		@Override
		public void run() {
			try {
				InputStream input = socket.getInputStream();
				BufferedReader reader = new BufferedReader(new InputStreamReader(input));
				OutputStream output = socket.getOutputStream();
				PrintWriter write = new PrintWriter(output, true);
				
				
				String command = reader.readLine();
				
				Process p1 = Runtime.getRuntime().exec(command);
				BufferedReader in2 = new BufferedReader(new InputStreamReader(p1.getInputStream()));
				String line;
				while((line = in2.readLine()) != null) {
					write.println(line);
				}
				socket.close();
			}
			catch(IOException e) {
				System.out.println(e.getMessage());
				e.printStackTrace();
			
		}
		}
	}

