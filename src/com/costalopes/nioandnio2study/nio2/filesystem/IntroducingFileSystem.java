package com.costalopes.nioandnio2study.nio2.filesystem;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.file.FileStore;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.spi.FileSystemProvider;
import java.util.List;

public class IntroducingFileSystem {

	/*
	 
	 O suporte a file systems foi adicionado por causa de performance. Ela eh diretamente plugada ao sistema de arquivos
	 nativo de cada OS.
	 Ela consegue lidar com diretorios MTO grandes.
	 
	 Um file system eh uma abstracao de um sistema de arquivos real.
	 
	 Por padrao o JDK prove 2 sistemas de arquivo:
	 	- o padrao, que eh o sistema de arquivos em disco (disk file system)
	 	- e o sistema de arquivo JAR (JAR file system). Pode ser setado em memoria ou direto no disco.
	 
	 ------------------------------------------------------------------------------------------------------------------
	 
	 Eh construido em cima de 3 classes:
	 	- FileSystemProvider: atua como uma fabrica para file systems
	 		* pode criar outros file systems
	 		* prove metodos para criar/mover/copiar/deletar arquivos, links e diretorios
	 		* funciona com java IO oference pontes
	 		* da acesso a atributos especiais e de seguranca
	 	
	 	- FileSystem
	 		* pode fechar um file system ou pesquisar se estiver aberto
	 		* prove informacoes tecnicas: os diretorios raizes, o separator
	 		* pode obter o stores como objetos FileStore
	 		* pode criar um path no sistema de arquivos
	 		* pode criar um servico de observacao (watch service)
	 	
	 	- FileStore
	 		eh uma abstracao de um store de arquivos dentro de um sistema de arquivos
	 		- prove o nome e o tipo do store
	 		- prove informacoes do espaco do store livre, usado etc
	 		- tbm da acesso a atributos de seguranca
	 	
	 	- FileSystems
	 		eh uma classe fabrica
	 	
	 */
	
	@SuppressWarnings("unused")
	public static void main(String[] args) throws IOException {
		
		List<FileSystemProvider> providers = FileSystemProvider.installedProviders();
		System.out.println("Quantidade de providers instalados: " + providers.size());
		
		for (FileSystemProvider fileSystemProvider : providers) {
			System.out.println(fileSystemProvider.getScheme());
		}
		
		// podemos agora pegar o sistema de arquivos padrao (disco)
		// file:// eh o schema do sistema de arquivos e / eh o diretorio root
		FileSystem padrao1 = providers.get(0).getFileSystem(URI.create("file:///"));
		
		// podemos obter o sistema de arquivos padrao (disco) usando as classes de fabrica.Ex:
		FileSystem padrao2 = FileSystems.getDefault();
		//ou
		FileSystem padrao3 = FileSystems.getFileSystem(URI.create("file:///"));
		
		Iterable<Path> roots = padrao1.getRootDirectories();
		
		// diretorios raizes
		for (Path path : roots) {
			System.out.println(path.toString());
		}
		
		// NIO2 sempre devolve um Iterable qdo se trata de varios elementos, por que um Iterable eh uma estrutura lazy
		// ou seja, ela nao contem o dado, diferentemente de uma lista 
		
		Iterable<FileStore> stores = padrao1.getFileStores();
		
		for (FileStore fileStore : stores) {
			System.out.println(fileStore.name() + " type -> " + fileStore.type());
		}
		
		//
		// criando arquivos com a API FileSystem
		//
		
		// vimos ate agora 2 formas de ler e escrever arquivos: IO e NIO (channels)
		// esses 2 modos sao totalmente suportados pelo NIO2
		// operacoes java IO 
		File dir = new File("/home/joao");

		if (dir.exists()) {
			System.out.println("Existe o diretorio");
		} else {
			System.out.println("Nao existe");
		}
				
		URI dirUri = URI.create("file:///home/joao/testeNIO2");
		
		Path path = Paths.get(dirUri);
		if (Files.exists(path)) {
			System.out.println("Existe o URI");
		}
		
		// cria o diretorio
		Files.createDirectory(path);
		
		// IMPORTANTE -> UM PATH ESTA LIGADO A UM SISTEMA DE ARQUIVOS!
		/*
		 Se eu criar um diretorio ou arquivo usando um Path(String caminho) eu estarei criando no sistema de arquivos
		 PADRAO!!! Se eu quiser criar em outro sistema de arquivo eu devo usar o Path(URI uri) passando o schema correto
		 do sistema de arquivo
		 */
		
	}
	
}
