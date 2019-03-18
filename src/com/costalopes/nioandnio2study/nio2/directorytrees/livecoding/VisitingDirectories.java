package com.costalopes.nioandnio2study.nio2.directorytrees.livecoding;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class VisitingDirectories {

	public static void main(String[] args) throws IOException {
		
		// contar o numero de diretorios vazios
		// contar o numero de arquivos/tipo
		CustomFileVistor fileVistor = new CustomFileVistor();
		Path workspaceDir = Paths.get("/media/16AC8619AC85F391/workspaces/");
		
		Files.walkFileTree(workspaceDir, fileVistor);
		
		System.out.println("Number of empty dirs = " + fileVistor.getEmptyDirs());
		System.out.println("File Types:");
		
		fileVistor.getFileTypes().forEach((type, qtd) -> System.out.println(type + " -> " + qtd));
		
	}
	
	private static class CustomFileVistor implements FileVisitor<Path> {
		
		private long emptyDirs = 0L;
		private Map<String, Long> fileTypes = new HashMap<>();
		
		@Override
		public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
			
			DirectoryStream<Path> directoryStream = Files.newDirectoryStream(dir);
			Stream<Path> stream = StreamSupport.stream(directoryStream.spliterator(), false);
			
			boolean dirIsNotEmpty = stream.findFirst().isPresent();
			
			if (dirIsNotEmpty) {
				return FileVisitResult.CONTINUE;
			} else {
				emptyDirs++;
				return FileVisitResult.SKIP_SUBTREE;
			}
				
		}
		
		@Override
		public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
			return FileVisitResult.CONTINUE;
		}
		
		@Override
		public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
			
			String fileType = Files.probeContentType(file);
			fileTypes.merge(fileType, 1L, (l1, l2) -> l1 + l2);
			
			return FileVisitResult.CONTINUE;
		}

		@Override
		public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
			return FileVisitResult.CONTINUE;
		}

		public long getEmptyDirs() {
			return emptyDirs;
		}

		public Map<String, Long> getFileTypes() {
			return fileTypes;
		}

	}
	
}
