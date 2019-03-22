package org.paumard.nio2;

import java.io.IOException;
import java.net.URI;
import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.List;

public class DirectroryEvents {

	public static void main(String[] args) throws IOException, InterruptedException {

		Path dir = Paths.get(URI.create("file:///E:tmp/events"));
		FileSystem fileSystem = dir.getFileSystem();
		
		WatchService watchService = fileSystem.newWatchService();
		
		WatchKey key = dir.register(watchService, 
				StandardWatchEventKinds.ENTRY_CREATE,
				StandardWatchEventKinds.ENTRY_MODIFY, 
				StandardWatchEventKinds.ENTRY_DELETE);
		
		while (key.isValid()) {
			
			WatchKey take = watchService.take();
			List<WatchEvent<?>> events = take.pollEvents();
			for (WatchEvent<?> event : events) {
				if (event.kind() == StandardWatchEventKinds.OVERFLOW) {
					continue;
				} else if (event.kind() == StandardWatchEventKinds.ENTRY_CREATE) {
					
					Path path = (Path)event.context();
					System.out.println("File creation: " + path + " - " + Files.probeContentType(path));
				} else if (event.kind() == StandardWatchEventKinds.ENTRY_MODIFY) {
					
					Path path = (Path)event.context();
					System.out.println("File modified: " + path + " - " + Files.probeContentType(path));
				} else if (event.kind() == StandardWatchEventKinds.ENTRY_DELETE) {
					
					Path path = (Path)event.context();
					System.out.println("File deleted: " + path + " - " + Files.probeContentType(path));
				}
			}
			take.reset();
		}
		System.out.println("Key is invalid");
	}
}
