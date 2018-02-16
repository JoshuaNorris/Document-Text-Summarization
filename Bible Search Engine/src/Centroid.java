import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;

public class Centroid {
	
	public ArrayList<Duple<ArrayList<String>, Double>> getSentenceScore(String text) throws FileNotFoundException {
		ArrayList<Duple<ArrayList<String>, Double>> result = new ArrayList<Duple<ArrayList<String>, Double>>();
		
		BibleParser parser = new BibleParser();
		ArrayList<String> words = parser.getWords(text);
		HashMap<String, Integer> wordcount = new HashMap<String, Integer>();
		ArrayList<String> allwords = parser.getAllWords(text);
		int numofwords = allwords.size();
		ArrayList<ArrayList<String>> chapters = parser.getChapters(text);
		ArrayList<ArrayList<String>> sentences = parser.getSentences(text);

		for (int x = 0; x < words.size(); x++) {
			wordcount.put(words.get(x), 0);
		}

		for (int x = 0; x < allwords.size(); x++) {
			String str = allwords.get(x);
			
			wordcount.put(str, wordcount.get(str) + 1);
		}
		
		for(int x = 0; x < sentences.size(); x++) {
			ArrayList<String> thissent = sentences.get(x);
			Duple<ArrayList<String>, Double> sent = new Duple<ArrayList<String>, Double>(null, null);
			sent.setFirst(thissent);
			double totalscore = 0.0;
			for (int y = 0; y < thissent.size(); y++) {
				String thisword = thissent.get(y);
				double tf = getTermFrequency(wordcount, numofwords, thisword);
				double idf = getInverseDocFrequency(chapters, thisword);
				double thisscore = tf * idf;
				totalscore = totalscore + thisscore;
				// look into the / |R|
			}
			
			// I added this because I think that the algorithm is favoring the sentences by the longest
			// length so hopefully the average will fix this. But this could be on purpose. Maybe the 
			// best summary sentences are the ones that are longer?
			totalscore = totalscore / (thissent.size() + 0.0);
			//
			
			sent.setSecond(totalscore);
			result.add(sent);
		}
		
		return result;
	}

	public static double getTermFrequency(HashMap<String, Integer> map, int total, String str) {
		int num = map.get(str);
		double result = (num + 0.0) / (total + 0.0);
		return result;
	}

	public static double getInverseDocFrequency(ArrayList<ArrayList<String>> chapters, String str) {
		int numofdocs = chapters.size();
		int count = 0;
		for (int x = 0; x < chapters.size(); x++) {
			ArrayList<String> chap = chapters.get(x);
			if (chap.contains(str)) {
				count++;
			}
		}
		double expression = (numofdocs + 0.0) / (count + 0.0);
		double result = Math.log10(expression);
		return result;

	}

}
