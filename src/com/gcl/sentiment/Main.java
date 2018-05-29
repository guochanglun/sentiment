package com.gcl.sentiment;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;
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
		Scanner scanner = new Scanner(System.in);
		while(true) {
			System.out.print("> ");
			String sentence = scanner.nextLine();
			if("end".equals(sentence)) break;
			if("cls".equals(sentence)) { clear(); continue;}
			
			if(classify(sentence)) {
				System.out.println("��л���");
			} else {
				System.out.println("�������");
			}
		}
		scanner.close();
	}	
	
	/**
	 * sentiment classify
	 */
	private static boolean classify(String sentence) {
		System.out.println("--------------------------------------------------");
		// split words
		List<SegToken> segTokenList = segmenter.process(sentence, SegMode.INDEX);
		double positiveScore = 10E100d;
		double negtiveScore = 10E100d;
		
		String preWord = "";
		
		for(SegToken token: segTokenList){
			String word = token.word;
			if((negList.contains(word) && !"".equals(preWord) && !notList.contains(preWord)) || 
					(posList.contains(word) && !"".equals(preWord) && notList.contains(preWord))) {
				negtiveScore *= 2.0 / (negList.size() + 1);
			}else {
				negtiveScore *= 1.0 / (negList.size() + 1);
			}
			
			if((posList.contains(word) && !"".equals(preWord) && !notList.contains(preWord)) || 
					(negList.contains(word) && !"".equals(preWord) && notList.contains(preWord))) {
				positiveScore *= 2.0 / (posList.size() + 1);
			}else {
				positiveScore *= 1.0 / (posList.size() + 1);
			}
			preWord = word;
		}
		System.out.println("negtiveScore: " + negtiveScore);
		System.out.println("positiveScore: " + positiveScore);
		System.out.println("--------------------------------------------------");
		
		if(negtiveScore > positiveScore){
			return false;
		}else{
			return true;
		}
	}
	
	public static void clear() {
        Robot robot;
		try {
			robot = new Robot();
			robot.mousePress(InputEvent.BUTTON3_MASK);       // ��������Ҽ�
	        robot.mouseRelease(InputEvent.BUTTON3_MASK);    // �ͷ�����Ҽ�
	        robot.keyPress(KeyEvent.VK_CONTROL);             // ����Ctrl��
	        robot.keyPress(KeyEvent.VK_R);                    // ����R��
	        robot.keyRelease(KeyEvent.VK_R);                  // �ͷ�R��
	        robot.keyRelease(KeyEvent.VK_CONTROL);            // �ͷ�Ctrl��
	        robot.delay(100);       
		} catch (AWTException e) {
			e.printStackTrace();
		}
    }
}
