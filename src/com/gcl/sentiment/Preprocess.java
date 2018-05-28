package com.gcl.sentiment;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.stream.Collectors;

public class Preprocess {
	public static void distinct(String inname, String outname) {
		try {
			BufferedReader br = new BufferedReader(new FileReader("src/" + inname + ".txt"));
			BufferedWriter bw = new BufferedWriter(new FileWriter("src/" + outname + ".txt"));
			br.lines()
			  .filter(line -> line.length() > 0)
			  .collect(Collectors.toSet())
			  .forEach(line -> {
				try {
					bw.write(line + "\n");
				} catch (IOException e) {
					e.printStackTrace();
				}
			});
			
			br.close();
			bw.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} 
	}
}
