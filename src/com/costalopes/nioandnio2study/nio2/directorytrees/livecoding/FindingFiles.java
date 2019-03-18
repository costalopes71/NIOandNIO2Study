package com.costalopes.nioandnio2study.nio2.directorytrees.livecoding;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.stream.Stream;

public class FindingFiles {

	public static void main(String[] args) throws IOException {
		
		Path workspaceDir = Paths.get("/media/16AC8619AC85F391/workspaces/");
		
		boolean exists = Files.exists(workspaceDir);
		System.out.println("exists: " + exists);
		
		Stream<Path> findStream1 = Files.find(workspaceDir, Integer.MAX_VALUE, (p, attr) -> true);
		System.out.println("count = " + findStream1.count());
		
		Stream<Path> findStream2 = Files
				.find(workspaceDir, Integer.MAX_VALUE, (p, attr) -> p.toString().endsWith(".java"));
		System.out.println("count (.java files) = " + findStream2.count());
		
		LocalDateTime data = LocalDateTime.of(2019, 1, 1, 0, 0);
		long janFirst2019 = data.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
		
		Stream<Path> findStream3 = Files
				.find(workspaceDir, Integer.MAX_VALUE, (p, attr) -> attr.creationTime().toMillis() > janFirst2019);
		System.out.println("count (files created after 1/1/2019) = " + findStream3.count());
		
		findStream1.close();
		findStream2.close();
		findStream3.close();
		
	}
	
}
