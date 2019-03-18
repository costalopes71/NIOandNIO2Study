package com.costalopes.nioandnio2study.nio2.directorytrees.directorystream;

import java.io.IOException;
import java.net.URI;
import java.nio.file.DirectoryStream;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class DirectoryStreamAndMatchers {

	/*
	 - DirectoyStream eh uma forma de analisar o conteudo do diretorio! Nao tem nada a ver com o stream do
	 IO api e tbm nada a ver com a api do java 8 Stream.
	 - Nao explora os subdiretorios, mas esses ainda sao parte da analise
	 - pode ser usado para pegar todo o conteudo do diretorio e tbm pode filtrar conteudo passando um
	 lambda Filter
	 */
	
	@SuppressWarnings("unused")
	public static void main(String[] args) throws IOException {
		
		Path path = Paths.get(URI.create("file:///home/joao/Documentos"));
		// ou 
		Path path2 = Paths.get("/media/16AC8619AC85F391/workspaces/"
				+ "Pluralsight_workspaces/NIOandNIO2_workspace/NIOandNIO2Study/src/com/"
				+ "costalopes/nioandnio2study/nio/channels");
		
		DirectoryStream<Path> directoryStream = Files.newDirectoryStream(path, Files::isDirectory);
		
		// tbm eh possivel passar diretamente uma REGEX para corresponder ao arquivo ou ao nome dos subdiretorios
		
		//ex:
		DirectoryStream<Path> directoryStream2 = Files.newDirectoryStream(path2, "*.java");
		
		//tbm podemos passar um objeto PathMatcher como parametro se precisarmos de checagem de nome de
		// arquivos complexas!
		// o objeto PathMatcher permite dois tipos de expressoes regulares, que depende do schema dado:
		//	- regex: (especificado na classe Pattern.class)
		// 	- glob: (versao simplicada da regex especificado no metodo FileSystem.getPathMatcher
		PathMatcher pathMatcher = FileSystems.getDefault().getPathMatcher("glob:**/*.java");
		DirectoryStream<Path> directoryStream3 = Files.newDirectoryStream(path2, pathMatcher::matches);
		
		// DirectoryStream implementa Iterable (por motivos de performance, uma vez que nao contem dados, 
		// diferentemente de um List por exemplo) -> ou seja eh uma lazy structure
		for (Path auxPath : directoryStream) {
			// operacoes com os elementos
		}
		// ou
		directoryStream.forEach(System.out::println);
		
		// se quisermos criar um Stream (API Stream do java 8)
		List<Path> paths = StreamSupport.stream(directoryStream.spliterator(), false)
				.collect(Collectors.toList());
		
	}
	
}
