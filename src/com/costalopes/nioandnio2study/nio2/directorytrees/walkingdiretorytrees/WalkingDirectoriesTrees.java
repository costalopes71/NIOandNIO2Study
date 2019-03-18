package com.costalopes.nioandnio2study.nio2.directorytrees.walkingdiretorytrees;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class WalkingDirectoriesTrees {

	/*
	 
	 Permite explorar o conteudo de um diretorio e seus subdiretorios e seus conteudos!
	 Pode ser feito de duas maneiras (a mudanca eh na ordem de exploracao):
	 	- Depth-first approach (usado no JDK)
	 		Explora o primeiro subdiretorio, e seu conteudo, depois o segundo subdiretorio e seu conteudo, depois o terceiro
	 		subdiretorio, e o primeiro subdiretorio desse subdiretorio, e os arquivos desse subdiretorio e por ai em diante
	 	
	 	- Breadth-first: explora todo conteudo do primeiro diretorio, depois o conteudo do primeiro diretorio, depois do
	 	segundo, depois do terceiro
	 	(para entender melhor ver o slides da aula)
	 */

	@SuppressWarnings({ "resource", "unused" })
	public static void main(String[] args) throws IOException {
		
		/*
		 o metodo Files.walk explora uma arvore de diretorio usando a abordagem de depth-first
		 Os parametros passados para esse metodo sao:
		 	1) o ponto de partida como um objeto Path (que deve ser um diretorio)
		 	2) opcionalmente a profundidade maxima da exploracao
		 	3) opcionalmente se deve seguir links simbolicos ou nao
		 */
		
		Path dir = Paths.get("/home/joao/Documentos");

		Stream<Path> paths = Files.walk(dir);
		Stream<Path> walkComProfundidadeMaxima = Files.walk(dir, 3);
		// se exister um cycle uma excecao sera lancada
		Stream<Path> walkComProfundidadeMaximaSegueLinksSimbolicos = Files.walk(dir, 4, FileVisitOption.FOLLOW_LINKS);
		
		// o metodo find funciona da mesma maneira, mas recebe um BiPredicate como parametro para filtrar os arquivos
		// enquanto explora e retorna um stream dos paths que correspondem
		PathMatcher pathMatcher = FileSystems.getDefault().getPathMatcher("glob:**/*.java");
		
		// uma coisa bem legal eh que os atributos sao parte do parametro do bi predicado, por exemplo, poderiamos
		// filtrar por todos os arquivos modificados desde tal data ou todos arquivos que pertencem a determinado usuario
		Files.find(dir, 999, (path, attributes) -> pathMatcher.matches(path));
		
		// IMPORTANTE! esses streams sao construidos de forma lazy, enquanto explorando a arvore de diretorio.
		// isso quer dizer que esse metodo NAO FAZ LOCK nos arquivos (oq seria, claro, uma pessima ideia), e isso
		// acarreta que o sistema de arquivo pode mudar durante o processo!!!
		// por exemplo, estamos explorando um subdiretorio e ele eh deletado (uma excecao eh lancada)
		
	}
	
}
