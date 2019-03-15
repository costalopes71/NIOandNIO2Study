package com.costalopes.nioandnio2study.nio2.filesystem.livecoding;

import java.io.IOException;
import java.net.URI;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.spi.FileSystemProvider;
import java.util.List;

public class DiskFileSystemOperations {

	@SuppressWarnings("unused")
	public static void main(String[] args) throws IOException {
		
		List<FileSystemProvider> installedProviders = FileSystemProvider.installedProviders();
		installedProviders.forEach(System.out::println);
		
		FileSystemProvider linuxFSP = installedProviders.get(0);
		
		FileSystem fileSystem1 = FileSystems.getDefault();
		
		URI rootURI = URI.create("file:///");
		FileSystem fileSystem2 = FileSystems.getFileSystem(rootURI);
		System.out.println(fileSystem2 == fileSystem1);
		
		Path dir = Paths.get("/home/joao/Documentos/teste_nio");
		// ou
		Path dir2 = Paths.get(URI.create("file:///home/joao/Documentos/teste_nio2"));
		
		linuxFSP.createDirectory(dir2);
		
		// tambem posso criar o Path pelo sistema de arquivos
		fileSystem1.getPath("/home/joao/Documentos/teste_nio3");

	}
	
}
