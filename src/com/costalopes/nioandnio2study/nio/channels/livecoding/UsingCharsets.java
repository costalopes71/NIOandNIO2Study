package com.costalopes.nioandnio2study.nio.channels.livecoding;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class UsingCharsets {

	public static void main(String[] args) throws IOException {
		
		Charset utf8 = StandardCharsets.UTF_8;
		Charset latin1 = StandardCharsets.ISO_8859_1;
		
		String hello = "Hello from José";
		System.out.println("Length: " + hello.length());
		
		CharBuffer charBuffer = CharBuffer.allocate(1024*1024);
		charBuffer.put(hello);
		
		charBuffer.flip();
		
		ByteBuffer utf8Buffer = utf8.encode(charBuffer);
		
		charBuffer.rewind();
		
		ByteBuffer latin1Buffer = latin1.encode(charBuffer);
		
		Path path1 = Paths.get("files/livecodingchapter2/hello-latin1.txt");
		Path path2 = Paths.get("files/livecodingchapter2/hello-utf8.txt");
		try (FileChannel channel1 = FileChannel.open(path1, StandardOpenOption.CREATE, StandardOpenOption.WRITE);
			 FileChannel channel2 = FileChannel.open(path2, StandardOpenOption.CREATE, StandardOpenOption.WRITE);) {
			
			channel1.write(latin1Buffer);
			channel2.write(utf8Buffer);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		System.out.println("File size: " + Files.size(path1));
		System.out.println("File size: " + Files.size(path2));
		
		//
		// reading
		//
		System.out.println("\nReading");
		
		try (FileChannel channel1 = FileChannel.open(path1, StandardOpenOption.READ);
			 FileChannel channel2 = FileChannel.open(path2, StandardOpenOption.READ);) {
			
			latin1Buffer.clear();
			channel1.read(latin1Buffer);
			latin1Buffer.flip();
			
			utf8Buffer.clear();
			channel2.read(utf8Buffer);
			utf8Buffer.flip();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		charBuffer.clear();
		
		charBuffer = latin1.decode(latin1Buffer);
		String resultLatin1 = new String(charBuffer.array());
		System.out.println("Latin 1 result: " + resultLatin1);
		
		charBuffer.clear();
		charBuffer = utf8.decode(utf8Buffer);
		String resultUtf8 = new String(charBuffer.array());
		System.out.println("UTF8 result: " + resultUtf8);
		
	}
	
}
