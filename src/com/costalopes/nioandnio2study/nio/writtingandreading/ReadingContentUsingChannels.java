package com.costalopes.nioandnio2study.nio.writtingandreading;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class ReadingContentUsingChannels {

	public static void main(String[] args) throws IOException {
		
		ByteBuffer buffer = ByteBuffer.allocate(1024);
		
		FileChannel fileChannel = FileChannel.open(Paths.get("files/ints.bin"), StandardOpenOption.READ);
		
		fileChannel.read(buffer);
		
		buffer.flip();
		
		IntBuffer intBuffer = buffer.asIntBuffer();
		
		int a = intBuffer.get();
		
		System.out.println(a);
		
		intBuffer.clear();
		
	}
	
}
