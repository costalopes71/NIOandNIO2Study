package com.costalopes.nioandnio2study.nio.channels;

public class ChannelsTypes {

	/*
	 Channel eh uma interface que eh implementada por varias classes concretas. Um channel permite ler e escrever dados
	 
	 	- FileChannel - 
	 		* acessa arquivos, tem um cursor, permite multiplas operacoes de leitura e escrita
	 		* tem um cursor
	 		* eh thread safe
	 	- DatagraChannel
	 		* acessa um socket
	 		* suporta multicast (UDP)
	 		* multiplas leituras e escritas NAO concorrentes
	 	- SocketChannel e ServerSocketChannel
	 		* acessa um socket TCP
	 		* suporta operacoes assincronas
	 		* multiplas leituras e escritas NAO concorrentes
	 
	 	Sao todas classes abstratas que sao extendidas por classes concretas que sao escondidas e nao devem ser utilizadas diretamente
	 	e sim atraves de fabricas.
	 */
	
}
