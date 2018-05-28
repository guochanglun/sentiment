package com.gcl.sentiment;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import com.huaban.analysis.jieba.JiebaSegmenter;
import com.huaban.analysis.jieba.JiebaSegmenter.SegMode;
import com.huaban.analysis.jieba.SegToken;

public class Main {
	private static List<String> negList;
	private static List<String> posList;
	private static List<String> notList;
	
	private static JiebaSegmenter segmenter = new JiebaSegmenter();
	
	static{
		String negtiveFileName = "src/negtive.txt";
		String positiveFileName = "src/positive.txt";
		String notFileName = "src/not.txt";
		
		File negtiveFile = new File(negtiveFileName);
		File positiveFile = new File(positiveFileName);
		File notFile = new File(notFileName);
		
		try(BufferedReader negBufferedReader = new BufferedReader(new FileReader(negtiveFile));
			BufferedReader posBufferedReader = new BufferedReader(new FileReader(positiveFile));
			BufferedReader notBufferedReader = new BufferedReader(new FileReader(notFile))){
			negList = negBufferedReader.lines().filter(line -> line.length() > 0).collect(Collectors.toList());
			posList = posBufferedReader.lines().filter(line -> line.length() > 0).collect(Collectors.toList());
			notList = notBufferedReader.lines().filter(line -> line.length() > 0).collect(Collectors.toList());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * main
	 */
	public static void main(String[] args) {
		String sentence = "今天我很不高兴，太他妈倒霉了,我有点想死的感觉，天都要塌下来了";
		if(classify(sentence)) {
			System.out.println("情感积极");
		} else {
			System.out.println("情感消极");
		}
	}	
	
	/**
	 * sentiment classify
	 */
	private static boolean classify(String sentence) {
		// split words
		List<SegToken> segTokenList = segmenter.process(sentence, SegMode.INDEX);
		double positiveScore = 1.0;
		double negtiveScore = 1.0;
		
		for(SegToken token: segTokenList){
			String word = token.word;
			if(word.length() <= 1) continue;
			
			if(negList.contains(word)){
				negtiveScore *= 2.0 / (negList.size() + 1);
			}else{
				negtiveScore *= 1.0 / (negList.size() + 1);
			}
			if(posList.contains(word)){
				positiveScore *= 2.0 / (posList.size() + 1);
			}else{
				positiveScore *= 1.0 / (posList.size() + 1);
			}
		}
		System.out.println("negtiveScore: " + negtiveScore);
		System.out.println("positiveScore: " + positiveScore);
		if(negtiveScore > positiveScore){
			return false;
		}else{
			return true;
		}
	}
}
