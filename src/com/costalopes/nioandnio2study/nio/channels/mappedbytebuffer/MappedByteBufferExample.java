package com.costalopes.nioandnio2study.nio.channels.mappedbytebuffer;

import java.nio.CharBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import static java.nio.file.StandardOpenOption.*;

import java.io.IOException;

public class MappedByteBufferExample {
	
	/*
	 
	 	MappedByteBuffer eh um buffer que mapeia um arquivo para a memoria. Ou seja um buffer capaz de carregar o arquivo em memoria.
		Eh mto pratico para aplicacoes que leem o mesmo arquivo varias vezes pois sera mto mais eficiente uma vez que a leitura
		sera feita em memoria e nao em disco. Existem 3 modos:
			* READ : para ler
			* READ_WRITE: ler e escrever (modificar)
			* PRIVATE: as modificacoes podem ser privadas se essa flag for setada
	 	Permite tbm fazer o buffer de apenas uma porcao do arquivo.
	 */
	
	@SuppressWarnings("unused")
	public static void main(String[] args) throws IOException {
		
		FileChannel fileChannel = FileChannel.open(Paths.get("exemplo.txt"), READ);		
		
		//
		// criando um buffer em memoria (neste exemplo o arquivo inteiro sera posto no buffer)
		//
		MappedByteBuffer mappedBuffer = fileChannel.map(MapMode.READ_ONLY, 0, fileChannel.size());
		
		//
		// se for um arquivo texto posso transformar em um buffer de char e ja decodificar com o charset apropriado
		//
		CharBuffer charBuffer = StandardCharsets.UTF_8.decode(mappedBuffer);
	
		// Obs: para ler arquivos texto usando NIO sempre teremos que converter um ByteBuffer para um CharBuffer e para tanto
		// sempre devemos usar os metodos decode (bytebuffer -> charbuffer) ou encode para o oposto (charbuffer -> bytebuffer)
		
	}
	
}
