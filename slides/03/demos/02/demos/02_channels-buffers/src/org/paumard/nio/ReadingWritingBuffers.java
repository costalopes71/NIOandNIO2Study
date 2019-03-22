package org.paumard.nio;

import java.util.List;
import java.io.IOError;
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


public class ReadingWritingBuffers {

	public static void main(String[] args) throws IOException {

		ByteBuffer buffer = ByteBuffer.allocate(1024*1024);
		
		buffer.putInt(10);
		buffer.putInt(20);
		buffer.putInt(30);
		
		System.out.println("Position = " + buffer.position());
		System.out.println("Limit    = " + buffer.limit());
		
		// buffer.flip();
//		IntBuffer intBuffer = buffer.asIntBuffer();
//		System.out.println("Position = " + intBuffer.position());
//		System.out.println("Limit    = " + intBuffer.limit());
//		
//		int i = intBuffer.get();
//		System.out.println("i = " + i);
		
		Path path = Paths.get("files/ints.bin");
		try (FileChannel fileChannel = FileChannel.open(path, StandardOpenOption.CREATE, StandardOpenOption.WRITE);) {
			
			buffer.flip();
			fileChannel.write(buffer);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		System.out.println("File = " + Files.size(path));
		
		try (FileChannel fileChannel = FileChannel.open(path, StandardOpenOption.READ);) {
			
			buffer.clear();
			fileChannel.read(buffer);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		System.out.println("Position = " + buffer.position());
		System.out.println("Limit    = " + buffer.limit());
		
		buffer.flip();
		IntBuffer intBuffer = buffer.asIntBuffer();
		List<Integer> ints = new ArrayList<>();
		try {
			while (true) {
				ints.add(intBuffer.get());
			}
		} catch (BufferUnderflowException e) {
		}
		System.out.println("Size = " + ints.size());
		ints.forEach(System.out::println);
	}
}
