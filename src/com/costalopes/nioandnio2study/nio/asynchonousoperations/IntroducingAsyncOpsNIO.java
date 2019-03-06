package com.costalopes.nioandnio2study.nio.asynchonousoperations;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Set;

public class IntroducingAsyncOpsNIO {

	/*
	 Operacoes assincronas sao operacoes NAO blocantes. Um operacao eh trigada e podemos continuar na thread e qdo o dado estiver
	 pronto pra ser consumido entao somos chamados denovo (callback). Nesse meio tempo podemos fazer outras coisas.
	 
	 Exemplo: um servidor web com mtos requests chegando de mtos sockets de mtos clientes! Se a operacao fosse sincrona, cada operacao
	 teria que ser conduzida em uma thread e essa thread estaria bloqueada ate terminar a operacao.
	 De forma assincrona uma unica thread pode lidar com mtos requests, trazendo mtos beneficios de processamento por exemplo.
	 
	 Ficar mudando de uma thread para outra tem um custo de CPU que se chama mudanca de contexto. Por isso sistemas assincronos
	 foram adicionados ao JDK para permitir o desenvolvimento de servidores web mais performaticos.
	 
	 Operacoes assincronas sao lidadas com o uso de um objeto chamado Selector.
	 
	 Um Selector eh o ponto de entrada para configurar um sistema assincrono!
	 	1 ) criar um channel
	 	2 ) configurar o channel como nao blocante
	 	3 ) registrar o channel com o selector
	 	4 ) obter a chave de registro
	 	
	 Um unico selector pode lidar com varios channels ao mesmo tempo.
	 Um channel ira gerar eventos para o selector, podendo ser:
	 	- READ, WRITE: o canal esta pronto para ler ou escrever
	 		nos casos de socket channels
	 	- CONNECT: uma conexao foi estabelecida nesse socket
	 	- ACCEPT: uma conexao foi aceita
	 
	 O registro eh configurado para ouvir certos eventos , os quais sao passados por parametro. Os eventos tbm podem ser modificados
	 diretamente atraves da chave.

	 Estando o sistema configurado, chamamos o metodo select() no selector.
	 Esta sera uma chamada blocante, ate que alguns dos canais registrados nesse seletor tenham eventos a serem consumidos. E a partir
	 do selector podemos obter as chaves desses canais que tem eventos para serem processados.
	 
	 */
	
	public static void main(String[] args) throws IOException {
		
		// channel especial para ouvir requests em sockets.
		ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
		// configurar o channel para ser assincrono
		serverSocketChannel.configureBlocking(false);
		
		// criar um socket de servidor a partir do server socket channel e vincula-lo a um endereco na maquina local e porta
		ServerSocket serverSocker = serverSocketChannel.socket();
		serverSocker.bind(new InetSocketAddress(12345));
		
		// criar um objeto Selector que ira lidar de forma assincrona com os eventos nos sockets
		Selector selector = Selector.open();
		
		// registrar o cnaal de soquete no Selector para o evento OP_ACCEPT.
		// retorna uma chave que deve ser guardada para uso futuro (cancelar o registro do canal, checar a validade etc)
		@SuppressWarnings("unused")
		SelectionKey serverSocketKey = serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
		
		while (true) {
			
			// o metodo select() eh blocante ate que os eventos estejam prontos para serem consumidos. N eh o numero de chaves
			// com eventos disponiveis para consumo
			int n = selector.select();
			System.out.printf("Got %d events", n);
			
			// retorna as chaves que tem eventos
			Set<SelectionKey> selectedKeys = selector.selectedKeys();
			for (SelectionKey sKey : selectedKeys) {
				
				// primeiro iremos nos certificar que a chave recebida corresponde a uma requisicao de conexao
				if ((sKey.readyOps() & SelectionKey.OP_ACCEPT) == SelectionKey.OP_ACCEPT) {
					// & operador AND bitwise: compara bit a bit (0010 & 1110) -> 0010
					
					// como eh uma operacao de conexao podemos obter o canal usando a chave
					ServerSocketChannel channel = (ServerSocketChannel) sKey.channel();
					
					// podemos abrir um segundo socket channel dedicado a comunicacao com o cliente que fez a requisicao de conexao
					SocketChannel socketChannel = channel.accept();
					socketChannel.configureBlocking(false);
					// registrar esse novo socket no mesmo selector para obter os dados de entrada desse novo cliente
					socketChannel.register(selector, SelectionKey.OP_READ);
					selectedKeys.remove(sKey);
					
				}
				
				/*
				 Ou seja, nesse ponto, nosso selector esta registrado com 2 canais e escutara eventos de ambos:
				 	1) o primeiro canal ouve requisicoes que estao chegando, e vai criando outros canais se essas requisicoes
				 	forem de conexao
				 	2) o segundo canal foi criado para lidar com a comunicacao com um cliente especifico
				 Portanto devemos em seguida adicionar comportamento para lidar com o evento de leitura do segundo canal
				 */
				else if ((sKey.readyOps() & SelectionKey.OP_READ) == SelectionKey.OP_READ) {
					
					ByteBuffer byteBuffer = ByteBuffer.allocate(1024*1024);
					
					// lendo a informacao do cliente
					SocketChannel channel = (SocketChannel) sKey.channel();
					channel.read(byteBuffer);
					
					// fazer algo com a informacao
					byteBuffer.clear();
					selectedKeys.remove(sKey);
					sKey.cancel();
					channel.close();
					
				}
				
				
			}
			
		}
		
	}
	
}
