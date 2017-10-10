import java.util.Scanner;
import java.io.*;
import java.net.*;
import java.nio.*;
import java.nio.file.*;
import java.nio.channels.*;

class udpserver {

	public static void main(String[] args) {
		try {
			DatagramChannel s = DatagramChannel.open();
			Console cons = System.console();

			int port = 0;
			int looper = -1; 
			int size = 0;

			while(looper < 0){
				try{
					port = Integer.parseInt(cons.readLine("Enter a port number: "));

					if(port >= 1024){
						s.bind(new InetSocketAddress(port));
						looper = 1;
					} else 
						throw new NumberFormatException();
				} catch( IOException e ) {
					System.out.println("Wrong port number. Enter a correct number.");
				} catch( NumberFormatException e ) {
					System.out.println("Wrong port number. Enter a correct number.");
				}
			}

			System.out.println("1");

			ByteBuffer buffer = ByteBuffer.allocate (1024);
			SocketAddress clientaddr = s.receive(buffer);
			//s.receive(buffer);

			String path = new String(buffer.array()).trim();

			System.out.println("Path: " + path);

			File file = new File(path);
			Path filePath = file.toPath();
			
			System.out.println("2");	
	
			//Something instead of randomaccessfile

			size = (int)Files.size(filePath);
			System.out.println("Size: " + size);

			ByteBuffer bufSize = ByteBuffer.allocate(4);
			bufSize.putInt(size);
			System.out.println("bufSize: " + bufSize);

			bufSize.flip();

			byte[] number = bufSize.array();
			for(byte b : number){
					System.out.println(b);
					bufSize.put(b);
			}
			
			s.send(bufSize, clientaddr);
			System.out.println(buffer);
			
			bufSize.allocate(size);
			//bufSize.put(b);
			bufSize.flip();
			s.send(bufSize, clientaddr);

			//} catch(IOException e){
			//System.out.println("Invalid path: " + e);

			//buffer.put(ByteBuffer.wrap("invalid path".getBytes()));
			//buffer.flip();
			//s.write(buffer);
			//}
			//buffer = ByteBuffer.wrap(message.getBytes());
			//s.send(buffer,clientaddr);
			//}


		} catch(IOException e) {
			System.out.println("Exception found");
		}
	}

}
