package com.costalopes.nioandnio2study.nio.channels.livecoding;

import java.io.IOException;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

public class ReadingWritingBuffers {

	public static void main(String[] args) throws IOException {
		
		ByteBuffer buffer = ByteBuffer.allocate(1024*1024);
		
		buffer.putInt(10);
		buffer.putInt(20);
		buffer.putInt(30);
		
		System.out.println("Byte buffer position: " + buffer.position());
		System.out.println("Byte buffer limit: " + buffer.limit());
		
		buffer.flip();
		
		IntBuffer intBuffer = buffer.asIntBuffer();

		System.out.println("Int buffer position: " + intBuffer.position());
		System.out.println("Int buffer limit: " + intBuffer.limit());
		
		int i = intBuffer.get(0);
		System.out.println("i = " + i);
		
		//
		// escrevendo arquivo
		//
		
		// Obs: se nao tivesse chamado o metodo flip ele iria escrever o buffer inteiro ou seja 1048576 bytes - a posicao corrente
		Path path = Paths.get("files/livecodingchapter2/ints.bin");
		try (FileChannel fileChannel = FileChannel.open(path, StandardOpenOption.CREATE, StandardOpenOption.WRITE);){
			
			fileChannel.write(buffer);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		System.out.println("File size: " + Files.size(path));
		
		//
		// lendo arquivo
		//
		
		try (FileChannel fileChannel = FileChannel.open(path, StandardOpenOption.READ);){
			
			buffer.clear();
			fileChannel.read(buffer);
			
		} catch (IOException e) {
			e.printStackTrace();
		}

		buffer.flip();
		IntBuffer intBuffer2 = buffer.asIntBuffer();
		List<Integer> ints = new ArrayList<>();
		
		try {
			while (true) {
				ints.add(intBuffer2.get());
			}
		} catch (BufferUnderflowException e) { }
		
		System.out.println(ints.size());
		ints.forEach(System.out::println);
		
	}
	
}
