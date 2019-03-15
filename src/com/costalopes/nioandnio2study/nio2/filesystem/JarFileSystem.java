package com.costalopes.nioandnio2study.nio2.filesystem;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class JarFileSystem {

	/*
	 Eh criado pela JDK qdo lancamos uma aplicacao Java.
	 pode ser usado para ler e escrever arquivos ZIP facilmente.
	 Suporta 2 modos:
	 	- criacao de arquivos zip
	 	- leitura de arquivos zip
	 */
	
	public static void main(String[] args) throws IOException {
		
		//
		// schema do sistema de arquivos jar
		//
		URI zipFile = URI.create("jar:file:///tmp/archive.zip");
		
		//
		// passar opcoes na criacao do sistema de arquivos. Dois valores possiveis para a chave: create e encoding
		// create: dois valores, true e false / encoding: StandardCharsets suportados pelo JDK
		//
		Map<String, String> options = new HashMap<>();
		options.put("create", "true");
		
		//
		// cria o sistema de arquivos e cria o zip file
		//
		FileSystem zipFS = FileSystems.newFileSystem(zipFile, options);

		// duas maneiras de adicionar conteudo ao sistema de arquivos que criei (zip), uma copiando diretamente
		// para ele e a otra escrevendo diretamente nele
		
		//
		// 1 maneira: adicionando um arquivo existente ao archive zip do sistema de arquivos jar
		//
		Path someText = Paths.get("./files/encoding.txt");
		
		//
		// criando um diretorio dentro do sistema de arquivo que acabei de criar (duas maneiras)
		//
//		Path dir = zipFS.getPath("files"); // primeira maneira
		// ou
		Path dir2 = Paths.get(URI.create("jar:file:///tmp/archive.zip!/files")); // segunda maneira
		
		zipFS.provider().createDirectory(dir2);
		
		// apenas esta linha de codigo eh suficiente para copiar um arquivo do sistema de arquivos padrao para
		// o sistema de arquivos jar que eu criei
		Files.copy(someText, zipFS.getPath("files/some.txt"));
		
		//
		// 2 maneira escrevendo diretamente nele
		//
		Path target = zipFS.getPath("ints.bin");
		OutputStream os = zipFS.provider().newOutputStream(target, StandardOpenOption.CREATE_NEW, StandardOpenOption.WRITE);
		
		DataOutputStream dos = new DataOutputStream(os);
		dos.writeByte(10);
		dos.writeByte(10);
		dos.writeByte(10);
		dos.writeByte(10);
		dos.close();
		os.close();
		
		// ou podemos escrever os dados usando channel
		
		Set<OpenOption> optionsSet = new HashSet<>();
		optionsSet.add(StandardOpenOption.CREATE_NEW);
		optionsSet.add(StandardOpenOption.WRITE);
		
		// abre o channel
//		ByteChannel channel = zipFS.provider().newByteChannel(target, optionsSet);
		
		zipFS.close();
		
	}
	
}
