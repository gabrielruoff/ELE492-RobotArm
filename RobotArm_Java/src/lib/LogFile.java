package lib;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class LogFile {
	File log;
	FileWriter fw;
	public LogFile(String fileName) throws Exception {
		log = new File("log"+File.separator+fileName);
		
		log.mkdirs();
			if(log.exists())
				log.delete();
			log.createNewFile();
	}
	
	public void writeLine(String line[]) throws IOException {
		fw = new FileWriter(log.getAbsolutePath(),true);
		fw.write(csvLine(line));
		fw.close();
	}
	
	private String csvLine(String []s) {
		StringBuilder l = new StringBuilder();
		for(String ss : s) {
			l.append(ss+",");
		}
		l.append("\n");
		return l.toString();
	}

}
