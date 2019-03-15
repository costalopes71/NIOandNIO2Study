package com.costalopes.nioandnio2study.nio2.filesystem;

import java.io.IOException;
import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.PosixFileAttributes;
import java.nio.file.attribute.PosixFilePermission;
import java.nio.file.spi.FileSystemProvider;
import java.util.Set;

public class AcessingFileAttributes {

	/*
	  Apenas no java NIO 2 podemos acessar os atributos do sistemas, para ambos sistemas: Unix e Windows.
	  Para tanto ha 3 interfaces envolvidas.
	  	- BasicFileAttributes (comum a todos os sistemas de arquivo)
	  	- DosFileAttributes (para o Windows)
	  	- PosixFileAttributes (para Unix)
	 */
	
	public static void main(String[] args) {
		
		Path path = Paths.get("/home/joao/Imagens/screen_backgrounds/dandelion_fire_sparks_120687_1920x1080.jpg");

		FileSystem fileSystem = path.getFileSystem();
		FileSystemProvider provider = fileSystem.provider();
		
		try {
			PosixFileAttributes readAttributes = provider.readAttributes(path, PosixFileAttributes.class);
			
			Set<PosixFilePermission> permissions = readAttributes.permissions();
			
			permissions.forEach(System.out::println);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		//
		// outro modo (mais facil e seguro tbm)
		//
		try {
			PosixFileAttributes readAttributes = Files.readAttributes(path, PosixFileAttributes.class);
			System.out.println("Group: " + readAttributes.group().getName());
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
}
