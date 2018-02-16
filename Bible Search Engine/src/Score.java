import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class Score {

	public static void main(String[] args) throws FileNotFoundException {
		if (args.length == 0) {
			System.err.println("Usage: Parsing Jack Programs");
			System.exit(1);
		}

		String inputfile = args[0];

		File fin = new File(inputfile);
		@SuppressWarnings("resource")
		Scanner s = new Scanner(fin);
		String text = "";
		while (s.hasNextLine()) {
			String line = s.nextLine();
			text = text + line;
		}

		BibleParser parse = new BibleParser();
		ArrayList<ArrayList<String>> sentences = parse.getAllSentences(text);
		ArrayList<ArrayList<String>> sentences2 = parse.getAllSentences(text);

		Centroid centroid = new Centroid();
		ArrayList<Duple<ArrayList<String>, Double>> sentenceCentroids = centroid.getSentenceScore(text);
		ArrayList<ArrayList<String>> unorderedsummary = new ArrayList<ArrayList<String>>();
		ArrayList<String> summary = new ArrayList<String>();
		
		Position position = new Position();
		ArrayList<Duple<ArrayList<String>, Double>> positionalscore = position.getSentenceScore(text);
		
		double max1 = 0.0;
		for (int y = 0; y < sentenceCentroids.size(); y++) {
			Duple<ArrayList<String>, Double> thisdup = sentenceCentroids.get(y);
			double thisscore = thisdup.getSecond();
			if (thisscore > max1) {
				max1 = thisscore;
			}
		}
		
		for (Duple<ArrayList<String>, Double> dup : positionalscore) {
			Double score = dup.getSecond();
			dup.setSecond(((max1 + 0.0) / (score + 0.0)) / 2.0);
			
		}
		
		ArrayList<Duple<ArrayList<String>, Double>> scores = new ArrayList<Duple<ArrayList<String>, Double>>();
		

		for (int x = 0; x < sentenceCentroids.size(); x++) {
			Duple<ArrayList<String>, Double> dup = new Duple<ArrayList<String>, Double>(null, null);
			dup.setFirst(sentenceCentroids.get(x).getFirst());
			double sc = sentenceCentroids.get(x).getSecond() + positionalscore.get(x).getSecond();
			dup.setSecond(sc);
			scores.add(dup);
		}


		for (int x = 0; x < 10; x++) {
			double max = 0.0;
			int bestdup = 0;
			for (int y = 0; y < scores.size(); y++) {
				Duple<ArrayList<String>, Double> thisdup = scores.get(y);
				double thisscore = thisdup.getSecond();
				if (thisscore > max) {
					max = thisscore;
					bestdup = y;
				}
			}
			ArrayList<String> winner = sentences.get(bestdup);
			unorderedsummary.add(winner);
			scores.remove(bestdup);
			sentences.remove(bestdup);
		}
		
		for (int x = 0; x < sentences2.size(); x++) {
			ArrayList<String> sentence = sentences2.get(x);
			if (unorderedsummary.contains(sentence)) {
				String winnerstr = "";
				for (int y = 0; y < sentence.size(); y++) {
					if (y != 0) {
						winnerstr = winnerstr + " " + sentence.get(y);
					} else {
						winnerstr = sentence.get(y);
					}
				}
				summary.add(winnerstr);
			}
		}

		for (int x = 0; x < summary.size(); x++) {
			System.out.println("[" + summary.get(x) + "]");
		}
	}

}
