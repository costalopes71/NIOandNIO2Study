package com.costalopes.nioandnio2study.nio.channels.writtingandreadingmultiplebuffers;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class WritingAndReadingMultipleBuffers {

	/*
	 Escrever e ler em multiplos buffers ao mesmo tempo eh mto util para mensagens com tamanho fixo (ex: http)
	 
	 	* leitura : se chama Scattering reading operation (ou Operacao de leitura de espalhamento), le de um Channel unico para varios
	 	* buffers. Primeiro enche o primeiro buffer e dai por diante.
	 	
	 	* escrita: se chama Gathering (ou seha operacao de gravacao de coleta), le de varios buffers para um Channel unico. Faz exatamente
	 	* o oposto da Scattering operation
	 */
	
	@SuppressWarnings("unused")
	public static void main(String[] args) throws IOException {
		//
		// Scattering operation
		//
		
		FileChannel channel = FileChannel.open(Paths.get("exemplo"), StandardOpenOption.READ, StandardOpenOption.WRITE);
		
		ByteBuffer header = ByteBuffer.allocate(1024);
		ByteBuffer body = ByteBuffer.allocate(4096);
		ByteBuffer footer = ByteBuffer.allocate(128);
		
		ByteBuffer[] message = {header, body, footer};
		
		// retorna a quantidade de bytes lida
		long bytesRead = channel.read(message);
		
		//
		// Gathering operation
		//
		
		ByteBuffer header2Write = ByteBuffer.allocate(1024);
		ByteBuffer body2Write = ByteBuffer.allocate(4096);
		ByteBuffer footer2Write = ByteBuffer.allocate(128);

		ByteBuffer[] message2 = {header2Write, body2Write, footer2Write};
		
		FileChannel channel2Write = FileChannel.open(Paths.get("exemplo"), StandardOpenOption.READ, StandardOpenOption.WRITE);
	
		// retorna a quantidade de bytes escrita no channel
		long bytesWritten = channel2Write.write(message2);
		
	}
	
}
