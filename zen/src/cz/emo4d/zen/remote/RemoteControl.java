package cz.emo4d.zen.remote;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;

public class RemoteControl {
	
	private static final int serverPort = 5869;
	
	public RemoteControl() {
		 RemoteControlAsync rca = new RemoteControlAsync();
		 rca.start();
	}
	
	class RemoteControlAsync extends Thread {

		@Override
		public void run() {
			ServerSocket server = null; 

	        try {
	            server = new ServerSocket(); 
	            
	            SocketAddress addr = new InetSocketAddress(InetAddress.getByName("0.0.0.0"), serverPort);
	            server.bind(addr); 


	            while(true) {
	                Socket client = server.accept(); 
	                PrintStream output = new PrintStream(client.getOutputStream());
	                
	                // run input listener
	    			try {
	    				BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
	    			} catch (IOException e) {
	    				e.printStackTrace();
	    			}
	                

	                try {
	                    output.close(); 
	                    client.close(); 
	                }
	                catch(IOException e) {
	                    e.printStackTrace();
	                }
	            }
	        }
	        catch(IOException e) {
	            e.printStackTrace();
	        }
	        finally {
	           if(server != null) {
	                try {
	                    server.close();
	                } catch(IOException e) {}
	            }
	        }
			
		}
		
	}
}
