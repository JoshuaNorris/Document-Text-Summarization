import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;

public class Position {

	public ArrayList<Duple<ArrayList<String>, Double>> getSentenceScore(String text) throws FileNotFoundException {

		BibleParser parser = new BibleParser();
		ArrayList<String> words = parser.getWords(text);
		HashMap<String, Integer> wordcount = new HashMap<String, Integer>();
		ArrayList<String> allwords = parser.getAllWords(text);
		int numofwords = allwords.size();
		ArrayList<ArrayList<ArrayList<String>>> chapters = parser.getChapterswithSentences(text);
		ArrayList<ArrayList<ArrayList<String>>> chapters2 = parser.getChapterswithSentences(text);
		ArrayList<ArrayList<String>> sentences = parser.getSentences(text);

		for (int x = 0; x < words.size(); x++) {
			wordcount.put(words.get(x), 0);
		}

		for (int x = 0; x < allwords.size(); x++) {
			String str = allwords.get(x);

			wordcount.put(str, wordcount.get(str) + 1);
		}

		ArrayList<Duple<ArrayList<String>, Double>> scores = new ArrayList<Duple<ArrayList<String>, Double>>();

		for (ArrayList<ArrayList<String>> chapter : chapters) {
			int count = chapter.size() / 2;
			int size = chapter.size() / 2;
			while (!chapter.isEmpty()) {
				Duple<ArrayList<String>, Double> dup1 = new Duple<ArrayList<String>, Double>(null, null);
				dup1.setFirst(chapter.get(0));
				dup1.setSecond((count + 0.0) / (size + 0.0));
				Duple<ArrayList<String>, Double> dup2 = new Duple<ArrayList<String>, Double>(null, null);
				dup2.setFirst(chapter.remove(chapter.size() - 1));
				dup2.setSecond((count + 0.0) / (size + 0.0));
				scores.add(dup1);
				scores.add(dup2);
				if (!chapter.isEmpty()) {
					chapter.remove(0);
				}
				count--;
			}
		}

		ArrayList<Duple<ArrayList<String>, Double>> result = new ArrayList<Duple<ArrayList<String>, Double>>();
		for (ArrayList<ArrayList<String>> chapter : chapters2) {
			for (int x = 0; x < chapter.size(); x++) {
				Duple<ArrayList<String>, Double> dup1 = new Duple<ArrayList<String>, Double>(null, null);
				dup1.setFirst(chapter.get(x));
				for (int y = 0; y < scores.size(); y++) {
					if (scores.get(y).getFirst().equals(chapter.get(x))) {
						dup1.setSecond(scores.get(y).getSecond());
						if (y != 0 && !scores.get(y - 1).getFirst().equals(scores.get(y).getFirst())) {
							result.add(dup1);
						} else if (y == 0){
							result.add(dup1);
						}
					}
				}

			}
		}

		return result;
	}
}
