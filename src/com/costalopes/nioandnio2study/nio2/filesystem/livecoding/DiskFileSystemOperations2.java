package com.costalopes.nioandnio2study.nio2.filesystem.livecoding;

import java.nio.file.FileStore;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Path;

public class DiskFileSystemOperations2 {

	public static void main(String[] args) {
		
		FileSystem fileSystem = FileSystems.getDefault();
		
		Iterable<Path> rootDirectories = fileSystem.getRootDirectories();
		rootDirectories.forEach(System.out::println);
		
		Iterable<FileStore> fileStores = fileSystem.getFileStores();
		fileStores.forEach(fStore -> System.out.println("name = " + fStore.name() + " - type =  " + fStore.type()));
	}
	
}
