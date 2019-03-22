package com.costalopes.nioandnio2study.nio2.listeningdirectoryevents;

import java.io.IOException;
import java.nio.file.FileSystem;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchEvent.Kind;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.List;

public class ListeningDirectoryEvents {

	/*
	 
	 Observar oq esta acontecendo em um diretorio! Como:
	 	- criacao de arquivos ou diretorios
	 	- delecao de arquivos ou diretorios
	 	- modificacao de arquivos ou diretorios
	 
	 Eh uma API que tbm esta atrelada as APIs nativas do sistema operacional (assim como todas as APIS do pacote NIO2)! Por isso eh mto performatica.
	 
	 No no Java 7 foi introduzido o padrao WatchService que nos permite fazer exatamente isso. Detalhes do WatchService:
	  - usa uma thread especial para ficar ouvindo os eventos
	  - nao tem agendador
	  - eventos nao sao perdidos (alguns podem ser perdidos em alguns casos)
	  - plugado diretamente nos sinais emitidos pelo sistema operacional nativo
	 
	 4 passos para setar uma WatchService:
	 	* criar o WatchService
	 	* registrar o WatchService a um diretorio
	 	* pegar a chave retornada
	 	* puxar os eventos e analisa-los
	 
	 */
	
	public static void main(String[] args) throws IOException, InterruptedException {
		
		Path dir = Paths.get("/home/joao/Documentos");
		FileSystem fileSystem = dir.getFileSystem();
		
		WatchService watchService = fileSystem.newWatchService();
		
		// registrar o watchservice em um diretorio provendo os eventos que queremos ouvir. 
		//Ira retornar uma chave que eh o objeto que sera notificado dos eventos
		// Ha uma chave para cada diretorio
		// A chave eh valida enquanto o diretorio for acessivel ( se o diretorio for deletado ou nao mais acessivel por outro razao, ela sera invalidada)
		WatchKey key = dir.register(watchService, StandardWatchEventKinds.ENTRY_CREATE,
				StandardWatchEventKinds.ENTRY_DELETE,
				StandardWatchEventKinds.ENTRY_MODIFY);
		
		/*
		 Existem 3 metodos para pegar os eventos
		 	* take() : eh uma chamada blocante! Enquanto nao houver eventos disponiveis esse metodo nao retorna
		 	* poll() : chamada NAO blocante! Pode retornar null e retorna imediatamente
		 	* poll(long, TimeUnit) : se houver eventos retorna senao aguarda eventos ate o Timeout retornando null se nao houver eventos
		 */
		
		while (key.isValid()) {
			
			WatchKey take = watchService.take();
			
			List<WatchEvent<?>> events = take.pollEvents();
			
			for (WatchEvent<?> event : events) {
				// fazer algo com os eventos
				// se mtos eventos sao gerados um evento especial eh adicionado a fila, chamado OVERFLOW, isso quer dizer que alguns eventos podem ter sido perdidos
				
				Kind<?> kind = event.kind();
				// pode ser create, modify ou delete. Pode ser um overflow tbm.
				
				if (kind == StandardWatchEventKinds.OVERFLOW) {
					// nao ha mto oq fazer
					continue;
				}
				
				// fazer operacoes com os eventos
				
			}
			
			// chamar o metodo reset, eh uma chamada MANDATORIA, e serve para esvaziar a fila de eventos. Se nao chamarmos este metodo,
			// nao serao adicionados mais eventos a essa chave e o metodo take nunca mais retornara essa chave
			take.reset();
			
		}
		
		
	}
	
}
