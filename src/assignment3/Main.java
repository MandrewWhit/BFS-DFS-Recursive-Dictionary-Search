/* WORD LADDER Main.java


 * EE422C Project 3 submission by
 * Replace <...> with your actual data.
 * <Anthony Bazzini>
 * <alb5834>
 * <75535>
 * Michael Whitaker
 * maw5299
 * 75535
 * 0
 * Git URL: https://github.com/EE422C/sum-2020-proj3-su20-pr3-pair-7
 * Summer 2020
 */


package assignment3;
import java.util.*;
import java.io.*;

public class Main {
	
	// static variables and constants only here.
	static String startWord;
	static String endWord;
	
	public static void main(String[] args) throws Exception {
		
		Scanner kb;	// input Scanner for commands
		PrintStream ps;	// output file, for student testing and grading only
		// If arguments are specified, read/write from/to files instead of Std IO.
		if (args.length != 0) {
			kb = new Scanner(new File(args[0]));
			ps = new PrintStream(new File(args[1]));
			System.setOut(ps);			// redirect output to ps
		} else {
			kb = new Scanner(System.in);// default input from Stdin
			ps = System.out;			// default output to Stdout
		}
		initialize();
		ArrayList<String> input = new ArrayList();
		input = parse(kb);
		if(input.get(0)!="/quit") {
			
			String start = input.get(0);
			String end = input.get(1);
			startWord = start;
			endWord = end;
	
			ArrayList<String> result = new ArrayList();
			//result = getWordLadderBFS(start, end);
			//printLadder(result);
			result = getWordLadderDFS(start, end);
			
			
			printLadder(result);
		// TODO methods to read in words, output ladder
		}
	}
	
	public static void initialize() {
		// initialize your static variables or constants here.
		// We will call this method before running our JUNIT tests.  So call it 
		// only once at the start of main.
	}
	
	/**
	 * @param keyboard Scanner connected to System.in
	 * @return ArrayList of Strings containing start word and end word. 
	 * If command is /quit, return empty ArrayList. 
	 */
	public static ArrayList<String> parse(Scanner keyboard) {
		// TO DO
		ArrayList<String> inputWords = new ArrayList<String>();
		String input = keyboard.nextLine();
		if(input.equals("/quit")) {
			inputWords.add("/quit");
			return inputWords;
		}
		String start = "";
		String end = "";
		int index = 0;
		for(int i=0;input.charAt(i)!=' ';i++) {
			start += input.charAt(i);
			index++;
		}
		index++;
		for(int i=index;i<input.length();i++) {
			end += input.charAt(i);
		}
		if(start.equals("/quit")) {
			inputWords.add("/quit");
			return inputWords;
		}
		if(end.equals("/quit")) {
			inputWords.add("/quit");
			return inputWords;
		}
		startWord = start;
		endWord = end;
		inputWords.add(start);
		inputWords.add(end);
		return inputWords;
	}
	
	public static ArrayList<String> getWordLadderDFS(String start, String end) {
		
		// Returned list should be ordered start to end.  Include start and end.
		// If ladder is empty, return list with just start and end.
		// TODO some code
		startWord = start;
		endWord = end;
		Set<String> dictionarySet = makeDictionary();
		//convert to arraylist
		ArrayList<String> dictionaryList = new ArrayList<String>();
		dictionaryList.addAll(dictionarySet);
		//create Ladder
		ArrayList<String> ladder = new ArrayList<String>();
		ArrayList<String> visitedNodes = new ArrayList<String>();
		start = start.toUpperCase();
		end = end.toUpperCase();
		
		ladder = getWordLadderHelperDFS(start, end, dictionaryList, ladder, start, visitedNodes);
		if(ladder.size()!=0) {	
			ladder.add(0, start);
		}
		return ladder; 
	}
	
	public static ArrayList<String> getWordLadderHelperDFS(String start, String end, ArrayList<String> dictionaryList, ArrayList<String> ladder, String currentWord, ArrayList<String> visitedNodes){
		//base case
		
		boolean finished = false;
		boolean endOfList = false;
		boolean found = false;
		int i=0;
		//System.out.println(currentWord);
		
		while(i<dictionaryList.size()&&(found==false)) {
			int differentLetters = 0;
			for(int j=0;j<dictionaryList.get(i).length();j++) {
				if(currentWord.charAt(j)!=dictionaryList.get(i).charAt(j)) {
					differentLetters++;
				}
			}

			if((differentLetters<2)&&(start.equals(dictionaryList.get(i))==false)) {
				boolean visited = false;
				for(int index=0;index<visitedNodes.size();index++) {
					if(dictionaryList.get(i)==visitedNodes.get(index)) {
						visited = true;
					}
				}
				if(visited==false) {
					if(end.equals(dictionaryList.get(i))) {
						finished = true;
					}
					
					found = true;
					currentWord = dictionaryList.get(i);
					
				}
			}
			if(i+1==dictionaryList.size()) {
				endOfList = true;
			}
			i++;
			
			if((found)||(endOfList)) {
				if(endOfList&&(finished==false)) {
					//System.out.println(currentWord);
					
					//visitedNodes.add(ladder.get(ladder.size()-1));
					if(ladder.size()==0) {
						return ladder;
					}
					ladder.remove(ladder.size()-1);
					if(ladder.size()!=0) {
						currentWord = ladder.get(ladder.size()-1);
					}
					//System.out.println("back");
					//System.out.println("");
					ladder = getWordLadderHelperDFS(start, end, dictionaryList, ladder, currentWord, visitedNodes);
					return ladder;
				}
				
				if(finished) {
					ladder.add(currentWord);
					return ladder;
				}else if(endOfList==false) {
					visitedNodes.add(currentWord);
					ladder.add(currentWord);
					
					ladder = getWordLadderHelperDFS(start, end, dictionaryList, ladder, currentWord, visitedNodes);
				}else {
					return ladder;
				}
			}
			
		}
		
		return ladder;
	}

	
	
	
    public static ArrayList<String> getWordLadderBFS(String start, String end) {
    	
		
		// TODO some code
    	Set<String> dictionarySet = makeDictionary();
		//convert to arraylist
		ArrayList<String> dictionaryList = new ArrayList<String>();
		dictionaryList.addAll(dictionarySet);
		ArrayList<Node> ladder = new ArrayList<Node>();	
		ArrayList<Node> startLadder = new ArrayList<Node>();
		Main.Node startNode = new Main().new Node();
		start = start.toUpperCase();
		end = end.toUpperCase();
		startNode.word = start;
		startLadder.add(startNode);
		
		
		ladder = getLadderBFSHelper(startLadder, end, dictionaryList, ladder, 0);
		
		ArrayList<String> finalLadder = new ArrayList<String>();
		if(ladder==null) {
			return finalLadder;
		}
		finalLadder.add(start);
		for(int i=0;i<ladder.size();i++) {
			finalLadder.add(ladder.get(ladder.size()-1-i).word);
		}
		return finalLadder;
		
		/*
		startNode.word = start;
		int differentChar = 0;
		for(int i=0;i<dictionaryList.size();i++) {
			for(int j=0;j<dictionaryList.get(i).length();j++) {
				if(dictionaryList.get(i).charAt(j)!=startNode.word.charAt(j)) {
					differentChar++;
				}
			}
			if(differentChar<2) {
				Main.Node newNode = new Main().new Node();
				newNode.word = dictionaryList.get(i);
				startNode.nextWords.add(newNode);
			}
		}
		
		boolean notFound = true;
		boolean notEmpty = true;
		Main.Node currentNode = new Main().new Node();
		currentNode = startNode;
		while(notFound&&notEmpty) {
			
			for(int i=0;i<currentNode.nextWords.size();i++) {
				for(int j=0;j<dictionaryList.size();j++) {
					differentChar = 0;
					for(int k=0;k<dictionaryList.get(j).length();k++) {
						if(dictionaryList.get(j).charAt(k)==currentNode.nextWords.get(i).word.charAt(k)) {
							differentChar++;
						}
					}
					if(differentChar<2) {
						Main.Node newNode = new Main().new Node();
						newNode.word = dictionaryList.get(j);
						currentNode.nextWords.get(i).nextWords.add(newNode);
					}
				}
			}
			
			
			
			
			
		}
		*/
		
		 // replace this line later with real return
	}
    
    public static ArrayList<Node> getLadderBFSHelper(ArrayList<Node> children, String end, ArrayList<String> dictionary, ArrayList<Node> ladder, int numCalls){
    	
    	ArrayList<Node> parents = new ArrayList<Node>();
    	parents = children;
    	ArrayList<Node> newChildren = new ArrayList<Node>();
    	numCalls++;
    	
    	if(numCalls>500) {
    		return null;
    	}
    	
    	for(int i=0;i<parents.size();i++) {
    		for(int j=0;j<dictionary.size();j++) {
    			int differentChar = 0;
    			for(int k=0;k<dictionary.get(j).length();k++) {
    				if(dictionary.get(j).charAt(k)!=parents.get(i).word.charAt(k)) {
    					differentChar++;
    				}
    			}
    			if(differentChar<2) {
    				Main.Node newNode = new Main().new Node();
					newNode.word = dictionary.get(j);
					newNode.parent = parents.get(i).word;
					newChildren.add(newNode);
    			}
    		}
    	}
    	
    	for(int i=0;i<newChildren.size();i++) {
    		if((newChildren.get(i).word).equals(end)){
    			Main.Node addNode = new Main().new Node();
    			addNode.word = newChildren.get(i).word;
    			addNode.parent = newChildren.get(i).parent;
    			//System.out.println(addNode.word);
    			ladder.add(addNode);
    			return ladder;
    		}
    	}
    	
    	ladder = getLadderBFSHelper(newChildren, end, dictionary, ladder, numCalls);
    	if(ladder==null) {
    		return null;
    	}
    	Main.Node topNode = new Main().new Node();
    	Main.Node addNode = new Main().new Node();
    	parents = newChildren;
    	topNode = ladder.get(ladder.size()-1);
    	for(int i=0;i<parents.size();i++) {
    		if(topNode.parent.equals(parents.get(i).word)) {
    			addNode = parents.get(i);
    			//System.out.println(addNode.word);
    			ladder.add(addNode);
    			return ladder;
    		}
    	}
    	
    	
    	return ladder;
    }
    
    class Node{
    	String word;
    	String parent;
    }
    
   
    
	
	public static void printLadder(ArrayList<String> ladder) {
		int count = 0;
		for(int i=0;i<ladder.size();i++) {
			count++;
		}
		if(ladder.size()!=0) {
			System.out.println("a " + count + "-rung word ladder exists between " + startWord.toLowerCase() + " and " + endWord.toLowerCase());
			for(int i=0;i<ladder.size();i++) {
				System.out.println(ladder.get(i).toLowerCase());
			}
		}else {
			System.out.println("no word ladder can be found between " + startWord + " and " + endWord);
		}
		
	}
	// TODO
	// Other private static methods here


	/* Do not modify makeDictionary */
	public static Set<String>  makeDictionary () {
		Set<String> words = new HashSet<String>();
		Scanner infile = null;
		try {
			infile = new Scanner (new File("five_letter_words.txt"));
		} catch (FileNotFoundException e) {
			System.out.println("Dictionary File not Found!");
			e.printStackTrace();
			System.exit(1);
		}
		while (infile.hasNext()) {
			words.add(infile.next().toUpperCase());
		}
		return words;
	}
}
