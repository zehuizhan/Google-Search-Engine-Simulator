package Web;
import javax.swing.JOptionPane;

import java.io.IOException;
import java.util.*;
public class Simulation {
	private static int heapSize;
	
	//To maintain the max-heap property for the Set of array.
	public static void maxHeapify(int[] array,int i){
		int l = 2*i+1;
		int r = 2*i+2;
		int largest = i;;
		if(l<=heapSize-1&& array[l]>array[i]) {
			largest = l;
		}
		else {
			largest = i;
		}
		if(r<=heapSize-1 && array[r]>array[largest]) {
			largest = r;
		}
		if(largest != i) {
			int temp = array[i];
			array[i]= array[largest];
			array[largest] = temp;
			maxHeapify(array,largest);
		}
		
	}
	
	//Find the parent node for element index
	public static int parent(int index) {
		return index = (index - 1) / 2;
	}

	//Use Max-Heapify to convert an array Ainto a max-heap
	public static void bulidMaxHeap(int[] A) {
		heapSize = A.length;
		for (int i = (heapSize - 1) / 2; i >= 0; i--) {
			maxHeapify(A, i);
		}
	}

	//To sort an array in place by heapSort
	public static void heapSort(int[] A) {
		bulidMaxHeap(A);
		for (int i = A.length - 1;i>0; i--) {
			int temp = A[i];
			A[i] = A[0];
			A[0] = temp;
			heapSize -= 1;
			maxHeapify(A, 0);
		}
	}
	
	//returns the element of A with the largest key.
	public static int heapMaximum(int[] A) {
		bulidMaxHeap(A);
		return A[0];
	}
	
	//increases the value of element x’skey to the new value key, where key >=x’scurrent key value
	public static void heapIncreaseKey(int[] A,int index,int key) throws IOException {
		if(key<A[index]) {
			throw new IOException("new key is smaller than the current key");
		}
		A[index] = key;
		while(index>0&&A[parent(index)]<A[index]) {
			int temp = A[index];
			A[index]=A[parent(index)];
			A[parent(index)]=temp;
			index = parent(index);
		}
		heapSort(A);
	}
	
	//inserts the element key into the set A.
	public static int[] maxHeapInsert(int[] A,int key) throws IOException{
		heapSize = A.length +1;
		A=Arrays.copyOf(A, heapSize);
		A[heapSize-1] = -1;
		heapIncreaseKey(A,heapSize-1,key);
		heapSort(A);
		return A;
	}
	
	//removes and returns the element of A with the largest key.
	public static int heapExtractMax(int[] A) throws IOException{
		if(A.length<1) {
			throw new IOException("heap underflow");
		}
		int max = A[A.length-1];
		A[0] = A[heapSize-1];
		heapSize-=1;
		maxHeapify(A,0);
		return max;
	}
	
	
	
	
	
	
	
	
	
	public static void main(String[] args) throws IOException {
		//1a) Allow users to enter a keyword
		String keyWord = JOptionPane.showInputDialog("KEY WORD");
		//If user did not follow the rule, system would be quit out.
		if(keyWord.equals("")) {
			keyWord = JOptionPane.showInputDialog("PLEASE TYPE IN A KEY WORD"+"\n"+ "OR"+"\n"+ "QUIT");
			if(keyWord.equals("")) {
				throw new IOException("USER REFUSE TO ENTER KEY WORD");
			}
		}
		
		//1b) Web Crawler searches for the keyword on internet to generate a list of web url links
		WebCrawler craw = new WebCrawler(keyWord);
		craw.search();
		//1c) Allow users to display the search result containing at least 30 web url links 
		ArrayList<String> urls = new ArrayList(craw.getUrls());
		JOptionPane.showMessageDialog(null,"RESULT FOR SEARCHING KEY WORD"+urls);
		//1d) Assign 4 scores (integer numbers between 1 and 100) for the 4 PageRank factors to each web url link. 
		Random random = new Random();
		//Make two different type of HashMap to sort the Link and Score
		HashMap<Integer,String> pageRank = new HashMap<Integer,String>();
		HashMap<String,Integer> Pagerank = new HashMap<String,Integer>();
		int[] scoresArray = new int[31];
		ArrayList<String> linksAnd4Scores = new ArrayList();
		for(int i =0; i<31;i++) {
			//Use random to generate four scores
			int factor1= random.nextInt(100);
			int factor2= random.nextInt(100);
			int factor3= random.nextInt(100);
			int factor4= random.nextInt(100);		
			String urlsScores = urls.get(i)+" SCORE1="+factor1+" SCORE2="+factor2+" SCORE3="+factor3+" SCORE4="+factor4;
			linksAnd4Scores.add(urlsScores);
			//1e) Calculate total PageRank score (sum up the 4 scores) for each web url link.
			int totalScores = factor1+factor2+factor3+factor4;
			scoresArray[i]=totalScores;
			//1f) Store the PageRank score to each web url link
			pageRank.put(totalScores,urls.get(i));
			Pagerank.put(urls.get(i),totalScores);

		}
		JOptionPane.showMessageDialog(null,"30 LINKS AND 4 SCORES FOR EACH"+linksAnd4Scores);
		//1g) Use Heapsort algorithm to sort the 30 PageRank scores.
		heapSort(scoresArray);
		//1h) Allow users to print out the sorting result in the sequence order based on the PageRank score
		ArrayList<String> linksAndScoreSorted = new ArrayList();
		for(int i = scoresArray.length-1; i>0;i--) {
			linksAndScoreSorted.add(pageRank.get(scoresArray[i])+  "  TotalScore: "+scoresArray[i]);
		}	
		JOptionPane.showMessageDialog(null,"30 LINKES SORTED BY TOTAL SCORE ORDER"+linksAndScoreSorted);
		//2a) Implement Heap priority queue to store the first sorted 20 out of 30 web url links into Heap
		int[] scoresQueue = new int[20];
		for(int i =0;i<scoresQueue.length;i++) {
			scoresQueue[i]=scoresArray[i+11];
		}
		ArrayList<String> links = new ArrayList();
		//2b) Allow users to insert a new web url link into Heap based on the PageRank score by
		String newLink = JOptionPane.showInputDialog("INSERT A NEW WEB URL LINK");
		int scoreForNewLink = random.nextInt(100)+random.nextInt(100)+random.nextInt(100)+random.nextInt(100);
		//If user did not follow the rule, system would be quit out.
		if(newLink.equals("")) {
			newLink = JOptionPane.showInputDialog("ARE U SURE NOT TO INSERT LINK?"+"\n"+
			"INSERT:PLEASE TYPE IN THE LINK"+"\n"+"NOT INSERT:ITS FINE AND LEAVE IT");
			if(newLink.equals("")) {
				scoreForNewLink=0;
			}
		}
		//collect all the score and link back into two different type of hashmap
		pageRank.put(scoreForNewLink,newLink);
		Pagerank.put(newLink,scoreForNewLink);
		
		int[] newScoresQueue =maxHeapInsert(scoresQueue,scoreForNewLink);
		
		//2c) Allow users to view the first ranked web url link by implementing Heap-Extract- Max().
		JOptionPane.showMessageDialog(null,"FIRST RANKED WEB URL LINK:  "+pageRank.get(heapExtractMax(newScoresQueue)));
	
		//Make a new ArrayList to sort the original one and new thing
		ArrayList<String> listAfterInsert = new ArrayList();
		for(int i = newScoresQueue.length-1; i>=0;i--) {
			if(newScoresQueue[i]==0) {
				break;
			}
			listAfterInsert.add("\n" + pageRank.get(newScoresQueue[i])+  "  TotalScore: "+newScoresQueue[i]);
		}
		JOptionPane.showMessageDialog(null,"AFTER INSERT"+listAfterInsert);
		
	

		//2d) Allow users to choose any one of the web url links stored in the heap Priority 
		//Queue and increase its PageRank score and its priority in the queue to 
		//be listed by implementing Heap-Increase-Key().		
		String choosenLink = JOptionPane.showInputDialog("CHOOSE A URL TO MAKE CHANGE");
		//If user did not follow the rule, system would be quit out.
		if(Pagerank.containsKey(choosenLink)==false) {
			choosenLink = JOptionPane.showInputDialog("THERE IS NO SUCH LINK EXIST RIGHT NOW"+"\n"+
					"PLEASE TYPE INTO THE EXISTED LINK"+"\n"+ "OR"+"\n"+ "QUIT AND WE WOULD NOT REFUND TO U");
			if(Pagerank.containsKey(choosenLink)==false) {
				throw new IOException("USER REFUES TO INCREASE SCORE");
			}
		}
		//If user did not follow the rule, system would be quit out.
		if(choosenLink.equals("")) {
			choosenLink = JOptionPane.showInputDialog("PLEASE TYPE A EXISTED LINK"+"\n"+ "OR"+"\n"+
					"QUIT AND WE WOULD NOT REFUND TO U");
			if(choosenLink.equals("")||Pagerank.containsKey(choosenLink)==false) {
				throw new IOException("USER REFUSED TO INCREASE SCORE");
			}
		}
	
		String goalscore = JOptionPane.showInputDialog("THE SCORE BE INCREASE TO");
		//If user did not follow the rule, system would be quit out.
		if(goalscore.equals("")) {
			goalscore = JOptionPane.showInputDialog("ARE U SURE NOT TO INCREASE THE SCORE?"+"\n"+
					"IF U WOULD LIKE TO INCREASE PLEASE TYPE IN A SCORE"+"\n"+"IF NOT, WE WOULD NOT REFUND TO U");
			if(goalscore.equals("")) {
				throw new IOException("USER REFUSED TO INCREASE SCORE");
			}
		}
		
		//Convert the String type number to Int type number
		int goalScore = Integer.parseInt(goalscore);
		int originalScore=Pagerank.get(choosenLink);
		
		//FInd the index of originalScore back
		int indexOfOriginalScore=0;
		for(int i =0;i<newScoresQueue.length;i++) {
			if(newScoresQueue[i]==originalScore) {
				indexOfOriginalScore =i;
				break;
			}
		}
		heapIncreaseKey(newScoresQueue,indexOfOriginalScore,goalScore);

		//Make a new ArrayList to sort the original one and the changing one
		ArrayList<String> listAfterIncrease = new ArrayList();
		for(int i = newScoresQueue.length-1; i>0;i--) {
			if(newScoresQueue[i]==goalScore) {
				listAfterIncrease.add(choosenLink+  "  TotalScore: "+goalScore);
			}
			else {
				listAfterIncrease.add("\n"+pageRank.get(newScoresQueue[i])+  "  TotalScore: "+newScoresQueue[i]);
			}
		}
		JOptionPane.showMessageDialog(null,"AFTER INCREASE"+listAfterIncrease);
	}
}
	

