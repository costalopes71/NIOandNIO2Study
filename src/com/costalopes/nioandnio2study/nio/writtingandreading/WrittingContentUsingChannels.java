package com.costalopes.nioandnio2study.nio.writtingandreading;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class WrittingContentUsingChannels {

	public static void main(String[] args) throws IOException {
		
		// 1) criar um byte buffer
		// ByteBuffer byteBuffer = ByteBuffer.allocateDirect(1024); -> este metodo aloca o buffer fora da memoria central da JVM (off-heap memory)
		
		ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
		
		//2) escrever os dados para o buffer
		byteBuffer.putInt(10);
		byteBuffer.rewind();
		
		//3) criar o channel com um arquivo para escrever o conteudo do buffer
		FileChannel fileChannel = FileChannel.open(Paths.get("files/ints.bin"), StandardOpenOption.CREATE, StandardOpenOption.WRITE);
		
		//4) escrever
		fileChannel.write(byteBuffer);
		
		fileChannel.close();
		
	}
	
}
