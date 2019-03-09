package com.costalopes.nioandnio2study.nio.channels.encodeanddecode;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class EncodingAndDecodingByteBuffers {

	/*
	 	Channels SEMPRE leem ou escrevem BYTEBUFFERS! Ou seja, se quisermos ler ou escrever um arquivo texto temos que usar
	 	os metodos encode ou decode do Charset apropriado. Exemplo abaixo.
	 	
	 	Lembretes:
	 		- Channels podem ler e escrever apenas BYTEBUFFERS
	 		- usando operacao encode e decode podemos converter um byte buffer para char buffer e vice versa
	 		- essas operacoes estao disponiveis apenas usando a classe Charsets do JDK
	 */
	
	public static void main(String[] args) throws IOException {
		
		//
		// operacao de leitura
		//
		
		FileChannel channel = FileChannel.open(Paths.get("./anotacoes.txt"), StandardOpenOption.READ);
		ByteBuffer byteBuffer = ByteBuffer.allocate(5024);
		
		channel.read(byteBuffer);
		
		byteBuffer.flip();
		
		Charset latin1 = StandardCharsets.ISO_8859_1;
		CharBuffer latin1Buffer = latin1.decode(byteBuffer);
		
		String result = new String(latin1Buffer.array());
		System.out.println(result);
		
		//
		// operacao de escrita
		//
		latin1Buffer.rewind();
		
		Charset utf8 = StandardCharsets.UTF_8;
		ByteBuffer byteBufferToWrite = utf8.encode(latin1Buffer);
		
		FileChannel writeChannel = FileChannel.open(Paths.get("./files/encoding.txt"), 
				StandardOpenOption.CREATE, 
				StandardOpenOption.WRITE);
		
		writeChannel.write(byteBufferToWrite);
		
		writeChannel.close();
		
	}
	
}
