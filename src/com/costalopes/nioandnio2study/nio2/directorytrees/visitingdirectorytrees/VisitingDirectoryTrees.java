package com.costalopes.nioandnio2study.nio2.directorytrees.visitingdirectorytrees;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;

public class VisitingDirectoryTrees {

	/*
	 Visitar um arvore de diretorio eh DIFERENTE de andar (walk)!!! Visitar oferece mais controle sobre o processo!
	 	- 1) pode interromper o processo. Ex: estamos procurando um arquivo na arvore, ao achar na precisamos mais continuar
	 	- 2) podemos passar elementos (skip) baseados em filtragem, sejam eles diretorios, arquivos, elementos escondidos e etc
	 
	 O modelo utilizado para tanto eh o metodo Files.walkFileTree, quem tem os seguintes parametros:
	 	- um FileVisitor
	 	- seguir ou nao links simbolicos
	 	- profundidade maxima de exploracao
	 
	 O FileVisitor eh um objeto usado durante a travessia de uma arvore de arquivos. Ele permite agir quando um diretorio eh
	 alcancado, antes e depois desse diretorio ser visitado, e age tbm sobre cada arquivo falando oq fazer com cada, e tbm 
	 lida com as excecoes caso algo de errrado aconteca durante a visita.
	 
	 FileVisitor eh uma interface, e tbm existe um classe adapter com implementacao default para os 4 metodos da implementacao
	 da interface.
	 
	 */
	
	public static void main(String[] args) throws IOException {
		
		Path dir = Paths.get("/home/joao");
		
		FileVisitor<Path> fileVisitor = new FileFinder("joao.txt");
		
		Files.walkFileTree(dir, fileVisitor);
		
		
		
	}
	
	public static class FileFinder implements FileVisitor<Path> {

		/*
		 Todos metodos retornam um objeto FileVisitResult, que eh um enum com os seguinte valores:
		  - CONTINUE -> continue a visitar os arquivos
		  - TERMINATE -> termina o processamento da arvore
		  - SKIP_SUBTREE -> geralmente usado no metodo preVisitDirectory, nao ira explorar o diretorio, ira pular os subdiretorios
		  - SKIP_SIBLINGS -> o resto do diretorio nao sera visitado
		 */

		private String searchedFileName;
		private Path found;
		
		public FileFinder(String file2Find) {
			searchedFileName = file2Find;
		}
		
		@Override
		public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
			// antes de visitar
			return FileVisitResult.CONTINUE;
		}

		@Override
		public FileVisitResult visitFile(Path path, BasicFileAttributes attrs) throws IOException {
			
			if (path.toFile().getName().equals(searchedFileName)) {
				this.found = path;
				return FileVisitResult.TERMINATE;
			} else {
				return FileVisitResult.CONTINUE;
			}
			
		}

		@Override
		public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
			// esse arquivo nao pode ser aberto por alguma razao, entao apenas continuaremos a visitar a arvore
			return FileVisitResult.CONTINUE;
		}

		@Override
		public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
			// depois de visitar
			return FileVisitResult.CONTINUE;
		}
		
		public Path getFound() {
			return found;
		}
		
	}
	
}
