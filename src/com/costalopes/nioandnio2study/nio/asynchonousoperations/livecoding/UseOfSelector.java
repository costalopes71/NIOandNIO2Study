package com.costalopes.nioandnio2study.nio.asynchonousoperations.livecoding;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Set;

public class UseOfSelector {

	public static void main(String[] args) throws IOException {
		
		ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
		serverSocketChannel.configureBlocking(false);
		
		ServerSocket serverSocket = serverSocketChannel.socket();
		serverSocket.bind(new InetSocketAddress(12345));
		
		Selector selector = Selector.open();
		serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
		
		while (true) {
			
			System.out.println("We are waiting for events...");
			
			int select = selector.select();
			System.out.printf("Got %d events\n", select);
			
			Set<SelectionKey> keys = selector.selectedKeys();
			for (SelectionKey key : keys) {
				
				if ((key.readyOps() & SelectionKey.OP_ACCEPT) == SelectionKey.OP_ACCEPT) {
					
					// aceitando a conexao
					System.out.println("Accepting the connection...");
					
					ServerSocketChannel channel = (ServerSocketChannel) key.channel();
					SocketChannel socketChannel = channel.accept();
					socketChannel.configureBlocking(false);
					socketChannel.register(selector, SelectionKey.OP_READ);
					keys.remove(key);
					
				} else if ((key.readyOps() & SelectionKey.OP_READ) == SelectionKey.OP_READ) {
					
					// lendo a informacao do socket
					SocketChannel socketChannel = (SocketChannel) key.channel();
					ByteBuffer buffer = ByteBuffer.allocate(1024);
					socketChannel.read(buffer);
					buffer.flip();
					CharBuffer charBuffer = StandardCharsets.UTF_8.decode(buffer);
					
					String message = new String(charBuffer.array());
					System.out.println("Read: " + message);
					buffer.clear();
					keys.remove(key);
					key.cancel();
					socketChannel.close();
					
				}
				
			}
			
		}
		
	}
	
}
