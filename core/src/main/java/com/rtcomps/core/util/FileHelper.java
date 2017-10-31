package com.rtcomps.core.util;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;

import org.apache.commons.io.input.ReversedLinesFileReader;

public class FileHelper {
	
	public static String[] readLastLines(File file, int lines) throws IOException {
		if (lines <1) {
			throw new RuntimeException("Number of lines must be > 0");
		}
		String[] lastLines = new String[lines];
		ReversedLinesFileReader reversedReader = new ReversedLinesFileReader(file, Charset.defaultCharset());
		try {
			String line = null;
			for (int i=0;i<lines;i++) {
				line = reversedReader.readLine();
				if (line!=null) {
					lastLines[i]=line;
				} else {
					break;
				}
			}
			return (lastLines[0]==null)? new String[0]:lastLines;
		} finally {
			reversedReader.close();
		}
	}

}
