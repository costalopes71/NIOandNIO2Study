package com.costalopes.nioandnio2study.nio2.filesystem.livecoding;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.HashMap;
import java.util.Map;

public class JarFileSystemOperations {

	public static void main(String[] args) {
		
		URI zip = URI.create("jar:file:///home/joao/Documentos/archive.zip");
		
		Map<String, String> options = new HashMap<>();
		options.put("create", "true");
		
		try (FileSystem zipFS = FileSystems.newFileSystem(zip, options);) {
			
			Path dir = zipFS.getPath("files/");
			zipFS.provider().createDirectory(dir);
			Path encondingFile = Paths.get("files/encoding.txt");
			Path target = zipFS.getPath("files/encoding-compressed.txt");
			
			Files.copy(encondingFile, target);
			
			Path binDir = zipFS.getPath("bin/");
			Path binFile = zipFS.getPath("bin/ints.bin");
			
			zipFS.provider().createDirectory(binDir);
			
			OutputStream os = zipFS.provider().newOutputStream(binFile, StandardOpenOption.CREATE_NEW, StandardOpenOption.WRITE);
			DataOutputStream dos = new DataOutputStream(os);
			dos.writeInt(10);
			dos.writeInt(20);
			dos.writeInt(30);
			dos.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
}
