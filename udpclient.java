import java.util.Scanner;
import java.io.*;
import java.net.*;
import java.nio.*;
import java.nio.file.*;
import java.nio.channels.*;

class udpclient{
	public static void main(String args[]){
		try{

			DatagramChannel dgc = DatagramChannel.open();
			Console cons = System.console();

			String ip = "";
			int port = 0;
			int size = 0;
			FileOutputStream fos;
			FileChannel fc;

			while(!dgc.isConnected()){
				try{
					ip = cons.readLine("Address: ");
					port = Integer.parseInt(cons.readLine("Port: "));
					dgc.connect(new InetSocketAddress(ip, port));
				} catch(NumberFormatException e){
					System.out.println("Wrong port, yo.");
				}
			}

			InetSocketAddress addr = new InetSocketAddress(ip, port);

			String initialFile = cons.readLine("Enter filename: ");
			File file = new File(initialFile);
			Path path = file.toPath();
			
			ByteBuffer buf = ByteBuffer.wrap(initialFile.getBytes());	

			dgc.send(buf, addr);

			buf = ByteBuffer.allocate(4);
			dgc.receive(buf);
			
			fos = new FileOutputStream("out_"+initialFile);
			fc = fos.getChannel();
			
			buf.flip();
			System.out.println("1");
			System.out.println(buf);
			size = buf.getInt();
			System.out.println("Size: " + size);
			
			buf = ByteBuffer.allocate(size);
			buf.put(Files.readAllBytes(path));
			
			dgc.send(buf, addr);
			
			System.out.println("You got the size which is: " + size);

			//This is where I would put the sliding window stuff
			//IF I HAD ANY 


			/** THIS WAS MY FIRST VERSION - NOT RIGHT 
			while(looper){ 
				path = cons.readLine("Enter a file: ");
				if(path.equals(exitMessage)){
					looper = false;
				}

				ByteBuffer buf = ByteBuffer.wrap(path.getBytes());
				dgc.send(buf,new InetSocketAddress(ip,port));

				File file = new File(path);
				Path filePath = file.toPath();

				buf = ByteBuffer.allocate((int)Files.size(filePath));
				buf.put(Files.readAllBytes(filePath));

				int bytesread = dgc.read(buf);
				FileOutputStream fos = new FileOutputStream("out_"+filePath);
				FileChannel fc = fos.getChannel();
				while(bytesread != -1){
					buf.flip();
					fc.write(buf);
					buf.compact();
					bytesread = sc.read(buf);
				}

				buf = ByteBuffer.allocate(4096); 
				SocketAddress serveraddr = dgc.receive(buf);
				String returnMessage = new String(buf.array());
				System.out.println("Your message was: " + returnMessage);
			}	
			 **/

			dgc.close();

		}catch(IOException e){
			System.out.println("Got an IO Exception");
		}
	}
}
