import java.io.FileNotFoundException;
import java.util.ArrayList;

public class BibleParser {

	public ArrayList<ArrayList<String>> getChapters(String text) throws FileNotFoundException {

		String[] precut = text.split(" ");

		ArrayList<String> allchapters = new ArrayList<String>();
		String chapters = "";
		for (int x = 0; x < precut.length; x++) {
			if (precut[x].contains("</c>")) {
				chapters = chapters + " " + precut[x] + " " + '>';
				allchapters.add(chapters);
				chapters = "<";
			} else {
				chapters = chapters + " " + precut[x];
			}
		}

		ArrayList<ArrayList<String>> chapterswithwords = new ArrayList<ArrayList<String>>();

		for (int x = 0; x < allchapters.size(); x++) {
			String str = allchapters.get(x);
			ArrayList<String> allwords = getAllWords(str);
			chapterswithwords.add(allwords);
		}

		ArrayList<String> lastchapter = chapterswithwords.get(chapterswithwords.size() - 1);
		lastchapter.remove(lastchapter.size() - 1);
		return chapterswithwords;
	}
	
	public ArrayList<ArrayList<ArrayList<String>>> getChapterswithSentences(String text) throws FileNotFoundException {

		String[] precut = text.split(" ");

		ArrayList<String> allchapters = new ArrayList<String>();
		String chapters = "";
		for (int x = 0; x < precut.length; x++) {
			if (precut[x].contains("</c>")) {
				chapters = chapters + " " + precut[x] + " " + '>';
				allchapters.add(chapters);
				chapters = "<";
			} else {
				chapters = chapters + " " + precut[x];
			}
		}

		ArrayList<ArrayList<ArrayList<String>>> chapterswithwords = new ArrayList<ArrayList<ArrayList<String>>>();

		for (int x = 0; x < allchapters.size(); x++) {
			String str = allchapters.get(x);
			ArrayList<ArrayList<String>> allsentences = getAllSentences(str);
			chapterswithwords.add(allsentences);
		}

		ArrayList<ArrayList<String>> lastchapter = chapterswithwords.get(chapterswithwords.size() - 1);
		ArrayList<String> lastlastchapter = lastchapter.get(lastchapter.size() - 1);
		lastlastchapter.remove(lastlastchapter.size() - 1);
		return chapterswithwords;
	}
	
	public ArrayList<String> getAllSentenceWords(String text) throws FileNotFoundException {
		int y = 0;
		while (y < text.length()) {
			if (text.charAt(y) == '<') {
				text = removeVerseNumbers(text, y);
			}

			y++;
		}
		String[] precut = text.split(" ");
		ArrayList<String> wordbyword = new ArrayList<String>();
		for (int x = 0; x < precut.length; x++) {
			if (precut[x].equals(" ") || precut[x].equals("")) {
				// This gets rid of the spaces but if this is
				// taken out the index of " " is a good separator
				// between verses.
			} else if (precut[x].indexOf('\\') != -1) {

			} else {
				wordbyword.add(precut[x]);
			}
		}

		ArrayList<String> result = new ArrayList<String>();
		for (int x = 0; x < wordbyword.size(); x++) {
			String str = wordbyword.get(x);
			if (str.equals("")) {
			} else {
				result.add(str);
			}
		}

		return result;
	}

	public ArrayList<ArrayList<String>> getAllSentences(String text) throws FileNotFoundException {
		ArrayList<String> words = getAllSentenceWords(text);
		
		
		ArrayList<ArrayList<String>> result = new ArrayList<ArrayList<String>>();
		ArrayList<String> thissentence = new ArrayList<String>();
		for (int x = 0; x < words.size(); x++) {
			String str = words.get(x);
			if (str.contains(".") || str.contains("!") || str.contains("?")) {
				if (!str.equals("")) {
					thissentence.add(str);
				}
				result.add(thissentence);
				thissentence = new ArrayList<String>();
			} else {
				thissentence.add(str);
			}
		}


		return result;

	}
	
	public ArrayList<ArrayList<String>> getSentences(String text) throws FileNotFoundException {
		ArrayList<String> words = getSentenceWords(text);
		ArrayList<ArrayList<String>> result = new ArrayList<ArrayList<String>>();
		ArrayList<String> thissentence = new ArrayList<String>();
		for (int x = 0; x < words.size(); x++) {
			String str = words.get(x);
			if (str.contains(".") || str.contains("!") || str.contains("?")) {
				String newstring = str.substring(0, str.length() - 1);
				if (!isStopword(newstring) && !newstring.equals("")) {
					thissentence.add(newstring);
				}
				result.add(thissentence);
				thissentence = new ArrayList<String>();
			} else {
				thissentence.add(str);
			}
		}

		for (int x = 0; x < result.size(); x++) {
			// x.out.println(result.get(x));
		}

		return result;

	}

	public ArrayList<String> getSentenceWords(String text) throws FileNotFoundException {
		int y = 0;
		while (y < text.length()) {
			if (text.charAt(y) == '<') {
				text = removeVerseNumbers(text, y);
			}

			y++;
		}
		String[] precut = text.split(" ");
		ArrayList<String> wordbyword = new ArrayList<String>();
		for (int x = 0; x < precut.length; x++) {
			if (precut[x].equals(" ") || precut[x].equals("")) {
				// This gets rid of the spaces but if this is
				// taken out the index of " " is a good separator
				// between verses.
			} else if (precut[x].contains(":")) {
				String str = precut[x];
				int index = str.indexOf(':');
				wordbyword.add(str.substring(0, index));
				wordbyword.add(str.substring(index + 1));
			} else if (precut[x].indexOf('\\') != -1) {

			} else {
				wordbyword.add(precut[x]);
			}
		}

		for (int x = 0; x < wordbyword.size(); x++) {
			String str = wordbyword.get(x);
			if (str.contains("--")) {
				if (str.substring(0, 2).equals("--")) {
					wordbyword.set(x, str.substring(2));
				} else if (str.substring(str.length() - 2, str.length()).equals("--")) {
					wordbyword.set(x, str.substring(0, str.length() - 2));
				} else {
					int index = str.indexOf("--");
					wordbyword.set(x, str.substring(0, index));
					wordbyword.add(x + 1, str.substring(index + 2));
				}
			}
		}

		for (int x = 0; x < wordbyword.size(); x++) {
			String str = wordbyword.get(x);
			if (str.contains("\"")) {
				if (str.length() == 1) {
					wordbyword.set(x, "");
				} else if (str.charAt(0) == '"' && str.charAt(str.length() - 1) == '"') {
					wordbyword.set(x, str.substring(1, str.length() - 1));
				} else if (str.charAt(0) == '"') {
					wordbyword.set(x, str.substring(1));
				} else if (str.charAt(str.length() - 1) == '"') {
					wordbyword.set(x, str.substring(0, str.length() - 1));
				} else {
					int index = str.indexOf('"');
					wordbyword.set(x, str.substring(0, index));
					wordbyword.add(x + 1, str.substring(index + 1));
				}
			}
		}
		
		for (int x = 0; x < wordbyword.size(); x++) {
			String str = wordbyword.get(x);
			if (str.contains("'")) {
				if (str.length() == 1) {
					wordbyword.set(x, "");
				} else if (str.charAt(0) == '\'' && str.charAt(str.length() - 1) == '\'') {
					wordbyword.set(x, str.substring(1, str.length() - 1));
				} else if (str.charAt(0) == '\'') {
					wordbyword.set(x, str.substring(1));
				} else if (str.charAt(str.length() - 1) == '\'') {
					wordbyword.set(x, str.substring(0, str.length() - 1));
				} else {
					int index = str.indexOf('\'');
					wordbyword.set(x, str.substring(0, index));
					wordbyword.add(x + 1, str.substring(index + 1));
				}
			}
		}

		for (int x = 0; x < wordbyword.size(); x++) {
			String str = wordbyword.get(x);
			if (str.contains(",")) {
				if (str.length() == 1) {
					wordbyword.set(x, "");
				} else if (str.charAt(0) == ',' && str.charAt(str.length() - 1) == ',') {
					wordbyword.set(x, str.substring(1, str.length() - 1));
				} else if (str.charAt(0) == ',') {
					wordbyword.set(x, str.substring(1));
				} else if (str.charAt(str.length() - 1) == ',') {
					wordbyword.set(x, str.substring(0, str.length() - 1));
				} else {
					int index = str.indexOf(',');
					wordbyword.set(x, str.substring(0, index));
					wordbyword.add(x + 1, str.substring(index + 1));
				}
			}
			if (str.contains("]")) {
				if (str.length() == 1) {
					wordbyword.set(x, "");
				} else if (str.charAt(0) == ']' && str.charAt(str.length() - 1) == ']') {
					wordbyword.set(x, str.substring(1, str.length() - 1));
				} else if (str.charAt(0) == ']') {
					wordbyword.set(x, str.substring(1));
				} else if (str.charAt(str.length() - 1) == ']') {
					wordbyword.set(x, str.substring(0, str.length() - 1));
				} else {
					int index = str.indexOf(']');
					wordbyword.set(x, str.substring(0, index));
					wordbyword.add(x + 1, str.substring(index + 1));
				}
			}
			if (str.contains("[")) {
				if (str.length() == 1) {
					wordbyword.set(x, "");
				} else if (str.charAt(0) == '[' && str.charAt(str.length() - 1) == '[') {
					wordbyword.set(x, str.substring(1, str.length() - 1));
				} else if (str.charAt(0) == '[') {
					wordbyword.set(x, str.substring(1));
				} else if (str.charAt(str.length() - 1) == '[') {
					wordbyword.set(x, str.substring(0, str.length() - 1));
				} else {
					int index = str.indexOf('[');
					wordbyword.set(x, str.substring(0, index));
					wordbyword.add(x + 1, str.substring(index + 1));
				}
			}
			if (str.contains(")")) {
				if (str.length() == 1) {
					wordbyword.set(x, "");
				} else if (str.charAt(0) == ')' && str.charAt(str.length() - 1) == ')') {
					wordbyword.set(x, str.substring(1, str.length() - 1));
				} else if (str.charAt(0) == ')') {
					wordbyword.set(x, str.substring(1));
				} else if (str.charAt(str.length() - 1) == ')') {
					wordbyword.set(x, str.substring(0, str.length() - 1));
				} else {
					int index = str.indexOf(')');
					wordbyword.set(x, str.substring(0, index));
					wordbyword.add(x + 1, str.substring(index + 1));
				}
				str = wordbyword.get(x);
			}
			if (str.contains("(")) {
				if (str.length() == 1) {
					wordbyword.set(x, "");
				} else if (str.charAt(0) == '(' && str.charAt(str.length() - 1) == '(') {
					wordbyword.set(x, str.substring(1, str.length() - 1));
				} else if (str.charAt(0) == '(') {
					wordbyword.set(x, str.substring(1));
				} else if (str.charAt(str.length() - 1) == '(') {
					wordbyword.set(x, str.substring(0, str.length() - 1));
				} else {
					int index = str.indexOf('(');
					wordbyword.set(x, str.substring(0, index));
					wordbyword.add(x + 1, str.substring(index + 1));
				}
			}
			
			if (str.contains(";")) {
				if (str.length() == 1) {
					wordbyword.set(x, "");
				} else if (str.charAt(0) == ';' && str.charAt(str.length() - 1) == ';') {
					wordbyword.set(x, str.substring(1, str.length() - 1));
				} else if (str.charAt(0) == ';') {
					wordbyword.set(x, str.substring(1));
				} else if (str.charAt(str.length() - 1) == ';') {
					wordbyword.set(x, str.substring(0, str.length() - 1));
				} else {
					int index = str.indexOf(';');
					wordbyword.set(x, str.substring(0, index));
					wordbyword.add(x + 1, str.substring(index + 1));
				}
			}
		}

		ArrayList<String> result = new ArrayList<String>();
		for (int x = 0; x < wordbyword.size(); x++) {
			String str = wordbyword.get(x);
			str = str.toLowerCase();
			if (str.equals("")) {
			} else if (isStopword(str)) {
			} else {
				result.add(str);
			}
		}

		return result;
	}

	public ArrayList<String> getAllWords(String text) throws FileNotFoundException {
		int y = 0;
		while (y < text.length()) {
			if (text.charAt(y) == '<') {
				text = removeVerseNumbers(text, y);
			}

			y++;
		}
		String[] precut = text.split(" ");
		ArrayList<String> wordbyword = new ArrayList<String>();
		for (int x = 0; x < precut.length; x++) {
			if (precut[x].equals(" ") || precut[x].equals("")) {
				// This gets rid of the spaces but if this is
				// taken out the index of " " is a good separator
				// between verses.
			} else if (precut[x].contains(":")) {
				String str = precut[x];
				int index = str.indexOf(':');
				wordbyword.add(str.substring(0, index));
				wordbyword.add(str.substring(index + 1));
			} else if (precut[x].indexOf('\\') != -1) {

			} else {
				wordbyword.add(precut[x]);
			}
		}

		for (int x = 0; x < wordbyword.size(); x++) {
			String str = wordbyword.get(x);
			if (str.contains("--")) {
				if (str.substring(0, 2).equals("--")) {
					wordbyword.set(x, str.substring(2));
				} else if (str.substring(str.length() - 2, str.length()).equals("--")) {
					wordbyword.set(x, str.substring(0, str.length() - 2));
				} else {
					int index = str.indexOf("--");
					wordbyword.set(x, str.substring(0, index));
					wordbyword.add(x + 1, str.substring(index + 2));
				}
			}
		}

		for (int x = 0; x < wordbyword.size(); x++) {
			String str = wordbyword.get(x);
			if (str.contains("\"")) {
				if (str.length() == 1) {
					wordbyword.set(x, "");
				} else if (str.length() == 1) {
					wordbyword.set(x, "");
				} else if (str.charAt(0) == '"' && str.charAt(str.length() - 1) == '"') {
					wordbyword.set(x, str.substring(1, str.length() - 1));
				} else if (str.charAt(0) == '"') {
					wordbyword.set(x, str.substring(1));
				} else if (str.charAt(str.length() - 1) == '"') {
					wordbyword.set(x, str.substring(0, str.length() - 1));
				} else {
					int index = str.indexOf('"');
					wordbyword.set(x, str.substring(0, index));
					wordbyword.add(x + 1, str.substring(index + 1));
				}
			}
		}
		
		for (int x = 0; x < wordbyword.size(); x++) {
			String str = wordbyword.get(x);
			if (str.contains("'")) {
				if (str.length() == 1) {
					wordbyword.set(x, "");
				} else if (str.length() == 1) {
					wordbyword.set(x, "");
				} else if (str.charAt(0) == '\'' && str.charAt(str.length() - 1) == '\'') {
					wordbyword.set(x, str.substring(1, str.length() - 1));
				} else if (str.charAt(0) == '\'') {
					wordbyword.set(x, str.substring(1));
				} else if (str.charAt(str.length() - 1) == '\'') {
					wordbyword.set(x, str.substring(0, str.length() - 1));
				} else {
					int index = str.indexOf('\'');
					wordbyword.set(x, str.substring(0, index));
					wordbyword.add(x + 1, str.substring(index + 1));
				}
			}
		}

		for (int x = 0; x < wordbyword.size(); x++) {
			String str = wordbyword.get(x);
			if (str.contains(".")) {
				if (str.length() == 1) {
					wordbyword.set(x, "");
				} else if (str.charAt(0) == '.' && str.charAt(str.length() - 1) == '.') {
					wordbyword.set(x, str.substring(1, str.length() - 1));
				} else if (str.charAt(0) == '.') {
					wordbyword.set(x, str.substring(1));
				} else if (str.charAt(str.length() - 1) == '.') {
					wordbyword.set(x, str.substring(0, str.length() - 1));
				} else {
					int index = str.indexOf('.');
					wordbyword.set(x, str.substring(0, index));
					wordbyword.add(x + 1, str.substring(index + 1));
				}
			}
			if (str.contains(",")) {
				if (str.length() == 1) {
					wordbyword.set(x, "");
				} else if (str.charAt(0) == ',' && str.charAt(str.length() - 1) == ',') {
					wordbyword.set(x, str.substring(1, str.length() - 1));
				} else if (str.charAt(0) == ',') {
					wordbyword.set(x, str.substring(1));
				} else if (str.charAt(str.length() - 1) == ',') {
					wordbyword.set(x, str.substring(0, str.length() - 1));
				} else {
					int index = str.indexOf(',');
					wordbyword.set(x, str.substring(0, index));
					wordbyword.add(x + 1, str.substring(index + 1));
				}
			}
			if (str.contains(";")) {
				if (str.length() == 1) {
					wordbyword.set(x, "");
				} else if (str.charAt(0) == ';' && str.charAt(str.length() - 1) == ';') {
					wordbyword.set(x, str.substring(1, str.length() - 1));
				} else if (str.charAt(0) == ';') {
					wordbyword.set(x, str.substring(1));
				} else if (str.charAt(str.length() - 1) == ';') {
					wordbyword.set(x, str.substring(0, str.length() - 1));
				} else {
					int index = str.indexOf(';');
					wordbyword.set(x, str.substring(0, index));
					wordbyword.add(x + 1, str.substring(index + 1));
				}
			}
			if (str.contains("]")) {
				if (str.length() == 1) {
					wordbyword.set(x, "");
				} else if (str.charAt(0) == ']' && str.charAt(str.length() - 1) == ']') {
					wordbyword.set(x, str.substring(1, str.length() - 1));
				} else if (str.charAt(0) == ']') {
					wordbyword.set(x, str.substring(1));
				} else if (str.charAt(str.length() - 1) == ']') {
					wordbyword.set(x, str.substring(0, str.length() - 1));
				} else {
					int index = str.indexOf(']');
					wordbyword.set(x, str.substring(0, index));
					wordbyword.add(x + 1, str.substring(index + 1));
				}
			}
			if (str.contains("[")) {
				if (str.length() == 1) {
					wordbyword.set(x, "");
				} else if (str.charAt(0) == '[' && str.charAt(str.length() - 1) == '[') {
					wordbyword.set(x, str.substring(1, str.length() - 1));
				} else if (str.charAt(0) == '[') {
					wordbyword.set(x, str.substring(1));
				} else if (str.charAt(str.length() - 1) == '[') {
					wordbyword.set(x, str.substring(0, str.length() - 1));
				} else {
					int index = str.indexOf('[');
					wordbyword.set(x, str.substring(0, index));
					wordbyword.add(x + 1, str.substring(index + 1));
				}
			}
			if (str.contains(")")) {
				if (str.length() == 1) {
					wordbyword.set(x, "");
				} else if (str.charAt(0) == ')' && str.charAt(str.length() - 1) == ')') {
					wordbyword.set(x, str.substring(1, str.length() - 1));
				} else if (str.charAt(0) == ')') {
					wordbyword.set(x, str.substring(1));
				} else if (str.charAt(str.length() - 1) == ')') {
					wordbyword.set(x, str.substring(0, str.length() - 1));
				} else {
					int index = str.indexOf(')');
					wordbyword.set(x, str.substring(0, index));
					wordbyword.add(x + 1, str.substring(index + 1));
				}
			}
			if (str.contains("(")) {
				if (str.length() == 1) {
					wordbyword.set(x, "");
				} else if (str.charAt(0) == '(' && str.charAt(str.length() - 1) == '(') {
					wordbyword.set(x, str.substring(1, str.length() - 1));
				} else if (str.charAt(0) == '(') {
					wordbyword.set(x, str.substring(1));
				} else if (str.charAt(str.length() - 1) == '(') {
					wordbyword.set(x, str.substring(0, str.length() - 1));
				} else {
					int index = str.indexOf('(');
					wordbyword.set(x, str.substring(0, index));
					wordbyword.add(x + 1, str.substring(index + 1));
				}
			}
			if (str.contains("!")) {
				if (str.length() == 1) {
					wordbyword.set(x, "");
				} else if (str.charAt(0) == '!' && str.charAt(str.length() - 1) == '!') {
					wordbyword.set(x, str.substring(1, str.length() - 1));
				} else if (str.charAt(0) == '!') {
					wordbyword.set(x, str.substring(1));
				} else if (str.charAt(str.length() - 1) == '!') {
					wordbyword.set(x, str.substring(0, str.length() - 1));
				} else {
					int index = str.indexOf('!');
					wordbyword.set(x, str.substring(0, index));
					wordbyword.add(x + 1, str.substring(index + 1));
				}
			}

			if (str.contains("?")) {
				if (str.length() == 1) {
					wordbyword.set(x, "");
				} else if (str.equals("?")) {
					wordbyword.set(x, "");
				} else if (str.charAt(0) == '?' && str.charAt(str.length() - 1) == '?') {
					wordbyword.set(x, str.substring(1, str.length() - 1));
				} else if (str.charAt(0) == '?') {
					wordbyword.set(x, str.substring(1));
				} else if (str.charAt(str.length() - 1) == '?') {
					wordbyword.set(x, str.substring(0, str.length() - 1));
				} else {
					int index = str.indexOf('?');
					wordbyword.set(x, str.substring(0, index));
					wordbyword.add(x + 1, str.substring(index + 1));
				}
			}
		}

		ArrayList<String> result = new ArrayList<String>();
		for (int x = 0; x < wordbyword.size(); x++) {
			String str = wordbyword.get(x);
			str = str.toLowerCase();
			if (str.equals("")) {
			} else if (isStopword(str)) {
			} else {
				result.add(str);
			}
		}

		return result;
	}

	public ArrayList<String> getWords(String text) throws FileNotFoundException {
		int y = 0;
		while (y < text.length()) {
			if (text.charAt(y) == '<') {
				text = removeVerseNumbers(text, y);
			}

			y++;
		}
		String[] precut = text.split(" ");
		ArrayList<String> wordbyword = new ArrayList<String>();
		for (int x = 0; x < precut.length; x++) {
			if (precut[x].equals(" ") || precut[x].equals("")) {
				// This gets rid of the spaces but if this is
				// taken out the index of " " is a good separator
				// between verses.
			} else if (precut[x].contains(":")) {
				String str = precut[x];
				int index = str.indexOf(':');
				wordbyword.add(str.substring(0, index));
				wordbyword.add(str.substring(index + 1));
			} else if (precut[x].indexOf('\\') != -1) {

			} else {
				wordbyword.add(precut[x]);
			}
		}

		for (int x = 0; x < wordbyword.size(); x++) {
			String str = wordbyword.get(x);
			if (str.contains("--")) {
				if (str.substring(0, 2).equals("--")) {
					wordbyword.set(x, str.substring(2));
				} else if (str.substring(str.length() - 2, str.length()).equals("--")) {
					wordbyword.set(x, str.substring(0, str.length() - 2));
				} else {
					int index = str.indexOf("--");
					wordbyword.set(x, str.substring(0, index));
					wordbyword.add(x + 1, str.substring(index + 2));
				}
			}
		}

		for (int x = 0; x < wordbyword.size(); x++) {
			String str = wordbyword.get(x);
			if (str.contains("\"")) {
				if (str.length() == 1) {
					wordbyword.set(x, "");
				} else if (str.charAt(0) == '"' && str.charAt(str.length() - 1) == '"') {
					wordbyword.set(x, str.substring(1, str.length() - 1));
				} else if (str.charAt(0) == '"') {
					wordbyword.set(x, str.substring(1));
				} else if (str.charAt(str.length() - 1) == '"') {
					wordbyword.set(x, str.substring(0, str.length() - 1));
				} else {
					int index = str.indexOf('"');
					wordbyword.set(x, str.substring(0, index));
					wordbyword.add(x + 1, str.substring(index + 1));
				}
			}
		}
		
		for (int x = 0; x < wordbyword.size(); x++) {
			String str = wordbyword.get(x);
			if (str.contains("\'")) {
				if (str.length() == 1) {
					wordbyword.set(x, "");
				} else if (str.charAt(0) == '\'' && str.charAt(str.length() - 1) == '\'') {
					wordbyword.set(x, str.substring(1, str.length() - 1));
				} else if (str.charAt(0) == '\'') {
					wordbyword.set(x, str.substring(1));
				} else if (str.charAt(str.length() - 1) == '\'') {
					wordbyword.set(x, str.substring(0, str.length() - 1));
				} else {
					int index = str.indexOf('\'');
					wordbyword.set(x, str.substring(0, index));
					wordbyword.add(x + 1, str.substring(index + 1));
				}
			}
		}

		for (int x = 0; x < wordbyword.size(); x++) {
			String str = wordbyword.get(x);
			if (str.contains(".")) {
				if (str.length() == 1) {
					wordbyword.set(x, "");
				} else if (str.charAt(0) == '.' && str.charAt(str.length() - 1) == '.') {
					wordbyword.set(x, str.substring(1, str.length() - 1));
				} else if (str.charAt(0) == '.') {
					wordbyword.set(x, str.substring(1));
				} else if (str.charAt(str.length() - 1) == '.') {
					wordbyword.set(x, str.substring(0, str.length() - 1));
				} else {
					int index = str.indexOf('.');
					wordbyword.set(x, str.substring(0, index));
					wordbyword.add(x + 1, str.substring(index + 1));
				}
			}
			if (str.contains(",")) {
				if (str.length() == 1) {
					wordbyword.set(x, "");
				} else if (str.charAt(0) == ',' && str.charAt(str.length() - 1) == ',') {
					wordbyword.set(x, str.substring(1, str.length() - 1));
				} else if (str.charAt(0) == ',') {
					wordbyword.set(x, str.substring(1));
				} else if (str.charAt(str.length() - 1) == ',') {
					wordbyword.set(x, str.substring(0, str.length() - 1));
				} else {
					int index = str.indexOf(',');
					wordbyword.set(x, str.substring(0, index));
					wordbyword.add(x + 1, str.substring(index + 1));
				}
			}
			if (str.contains(";")) {
				if (str.length() == 1) {
					wordbyword.set(x, "");
				} else if (str.charAt(0) == ';' && str.charAt(str.length() - 1) == ';') {
					wordbyword.set(x, str.substring(1, str.length() - 1));
				} else if (str.charAt(0) == ';') {
					wordbyword.set(x, str.substring(1));
				} else if (str.charAt(str.length() - 1) == ';') {
					wordbyword.set(x, str.substring(0, str.length() - 1));
				} else {
					int index = str.indexOf(';');
					wordbyword.set(x, str.substring(0, index));
					wordbyword.add(x + 1, str.substring(index + 1));
				}
			}
			if (str.contains("]")) {
				if (str.length() == 1) {
					wordbyword.set(x, "");
				} else if (str.charAt(0) == ']' && str.charAt(str.length() - 1) == ']') {
					wordbyword.set(x, str.substring(1, str.length() - 1));
				} else if (str.charAt(0) == ']') {
					wordbyword.set(x, str.substring(1));
				} else if (str.charAt(str.length() - 1) == ']') {
					wordbyword.set(x, str.substring(0, str.length() - 1));
				} else {
					int index = str.indexOf(']');
					wordbyword.set(x, str.substring(0, index));
					wordbyword.add(x + 1, str.substring(index + 1));
				}
			}
			if (str.contains("[")) {
				if (str.length() == 1) {
					wordbyword.set(x, "");
				} else if (str.charAt(0) == '[' && str.charAt(str.length() - 1) == '[') {
					wordbyword.set(x, str.substring(1, str.length() - 1));
				} else if (str.charAt(0) == '[') {
					wordbyword.set(x, str.substring(1));
				} else if (str.charAt(str.length() - 1) == '[') {
					wordbyword.set(x, str.substring(0, str.length() - 1));
				} else {
					int index = str.indexOf('[');
					wordbyword.set(x, str.substring(0, index));
					wordbyword.add(x + 1, str.substring(index + 1));
				}
			}
			if (str.contains(")")) {
				if (str.length() == 1) {
					wordbyword.set(x, "");
				} else if (str.charAt(0) == ')' && str.charAt(str.length() - 1) == ')') {
					wordbyword.set(x, str.substring(1, str.length() - 1));
				} else if (str.charAt(0) == ')') {
					wordbyword.set(x, str.substring(1));
				} else if (str.charAt(str.length() - 1) == ')') {
					wordbyword.set(x, str.substring(0, str.length() - 1));
				} else {
					int index = str.indexOf(')');
					wordbyword.set(x, str.substring(0, index));
					wordbyword.add(x + 1, str.substring(index + 1));
				}
			}
			if (str.contains("(")) {
				if (str.length() == 1) {
					wordbyword.set(x, "");
				} else if (str.charAt(0) == '(' && str.charAt(str.length() - 1) == '(') {
					wordbyword.set(x, str.substring(1, str.length() - 1));
				} else if (str.charAt(0) == '(') {
					wordbyword.set(x, str.substring(1));
				} else if (str.charAt(str.length() - 1) == '(') {
					wordbyword.set(x, str.substring(0, str.length() - 1));
				} else {
					int index = str.indexOf('(');
					wordbyword.set(x, str.substring(0, index));
					wordbyword.add(x + 1, str.substring(index + 1));
				}
			}
			if (str.contains("!")) {
				if (str.length() == 1) {
					wordbyword.set(x, "");
				} else if (str.charAt(0) == '!' && str.charAt(str.length() - 1) == '!') {
					wordbyword.set(x, str.substring(1, str.length() - 1));
				} else if (str.charAt(0) == '!') {
					wordbyword.set(x, str.substring(1));
				} else if (str.charAt(str.length() - 1) == '!') {
					wordbyword.set(x, str.substring(0, str.length() - 1));
				} else {
					int index = str.indexOf('!');
					wordbyword.set(x, str.substring(0, index));
					wordbyword.add(x + 1, str.substring(index + 1));
				}
			}

			if (str.contains("?")) {
				if (str.length() == 1) {
					wordbyword.set(x, "");
				} else if (str.equals("?")) {
					wordbyword.set(x, "");
				} else if (str.charAt(0) == '?' && str.charAt(str.length() - 1) == '?') {
					wordbyword.set(x, str.substring(1, str.length() - 1));
				} else if (str.charAt(0) == '?') {
					wordbyword.set(x, str.substring(1));
				} else if (str.charAt(str.length() - 1) == '?') {
					wordbyword.set(x, str.substring(0, str.length() - 1));
				} else {
					int index = str.indexOf('?');
					wordbyword.set(x, str.substring(0, index));
					wordbyword.add(x + 1, str.substring(index + 1));
				}
			}
		}

		ArrayList<String> result = new ArrayList<String>();
		for (int x = 0; x < wordbyword.size(); x++) {
			String str = wordbyword.get(x);
			str = str.toLowerCase();
			if (!result.contains(str) && !str.equals("") && !isStopword(str)) {
				result.add(str);
			}
		}

		return result;
	}

	private static String removeVerseNumbers(String text, int index) {

		int num = index;
		num++;
		while (!(text.charAt(num) == '>')) {
			if (text.charAt(num) == '<') {
				removeVerseNumbers(text, num);
			}
			num++;
		}
		num++;
		text = text.substring(0, index) + " " + text.substring(num);
		return text;
	}

	private static boolean isAlpha(char c) {
		if (c == 'a') {
			return true;
		} else if  (c == 'b') {
			return true;
		} else if  (c == 'c') {
			return true;
		} else if  (c == 'd') {
			return true;
		} else if  (c == 'e') {
			return true;
		} else if  (c == 'f') {
			return true;
		} else if  (c == 'g') {
			return true;
		} else if  (c == 'h') {
			return true;
		} else if  (c == 'i') {
			return true;
		} else if  (c == 'j') {
			return true;
		} else if  (c == 'k') {
			return true;
		} else if  (c == 'l') {
			return true;
		} else if  (c == 'm') {
			return true;
		} else if  (c == 'n') {
			return true;
		} else if  (c == 'o') {
			return true;
		} else if  (c == 'p') {
			return true;
		} else if  (c == 'q') {
			return true;
		} else if  (c == 'r') {
			return true;
		} else if  (c == 's') {
			return true;
		} else if  (c == 't') {
			return true;
		} else if  (c == 'u') {
			return true;
		} else if  (c == 'v') {
			return true;
		} else if  (c == 'w') {
			return true;
		} else if  (c == 'x') {
			return true;
		} else if  (c == 'y') {
			return true;
		} else if  (c == 'z') {
			return true;
		} else if (c == 'A') {
			return true;
		} else if  (c == 'B') {
			return true;
		} else if  (c == 'C') {
			return true;
		} else if  (c == 'D') {
			return true;
		} else if  (c == 'E') {
			return true;
		} else if  (c == 'F') {
			return true;
		} else if  (c == 'G') {
			return true;
		} else if  (c == 'H') {
			return true;
		} else if  (c == 'I') {
			return true;
		} else if  (c == 'J') {
			return true;
		} else if  (c == 'K') {
			return true;
		} else if  (c == 'L') {
			return true;
		} else if  (c == 'M') {
			return true;
		} else if  (c == 'N') {
			return true;
		} else if  (c == 'O') {
			return true;
		} else if  (c == 'P') {
			return true;
		} else if  (c == 'Q') {
			return true;
		} else if  (c == 'R') {
			return true;
		} else if  (c == 'S') {
			return true;
		} else if  (c == 'T') {
			return true;
		} else if  (c == 'U') {
			return true;
		} else if  (c == 'V') {
			return true;
		} else if  (c == 'W') {
			return true;
		} else if  (c == 'X') {
			return true;
		} else if  (c == 'Y') {
			return true;
		} else if  (c == 'Z') {
			return true;
		} else {
			return false;
		}
	}
	
	private boolean isStopword(String str) {
		ArrayList<String> stopwords = new ArrayList<String>();
		stopwords.add("a");
		stopwords.add("able");
		stopwords.add("about");
		stopwords.add("above");
		stopwords.add("abst");
		stopwords.add("accordance");
		stopwords.add("according");
		stopwords.add("accordingly");
		stopwords.add("across");
		stopwords.add("act");
		stopwords.add("actually");
		stopwords.add("added");
		stopwords.add("adj");
		stopwords.add("affected");
		stopwords.add("affecting");
		stopwords.add("affects");
		stopwords.add("after");
		stopwords.add("afterwards");
		stopwords.add("again");
		stopwords.add("against");
		stopwords.add("ah");
		stopwords.add("all");
		stopwords.add("almost");
		stopwords.add("alone");
		stopwords.add("along");
		stopwords.add("already");
		stopwords.add("also");
		stopwords.add("although");
		stopwords.add("always");
		stopwords.add("am");
		stopwords.add("among");
		stopwords.add("amonst");
		stopwords.add("an");
		stopwords.add("and");
		stopwords.add("announce");
		stopwords.add("another");
		stopwords.add("any");
		stopwords.add("anybody");
		stopwords.add("anyhow");
		stopwords.add("anymore");
		stopwords.add("anyone");
		stopwords.add("anything");
		stopwords.add("anyway");
		stopwords.add("anyways");
		stopwords.add("anywhere");
		stopwords.add("apparently");
		stopwords.add("approximately");
		stopwords.add("are");
		stopwords.add("aren");
		stopwords.add("aren't");
		stopwords.add("arise");
		stopwords.add("around");
		stopwords.add("as");
		stopwords.add("aside");
		stopwords.add("ask");
		stopwords.add("asking");
		stopwords.add("at");
		stopwords.add("auth");
		stopwords.add("available");
		stopwords.add("away");
		stopwords.add("awfully");
		stopwords.add("back");
		stopwords.add("be");
		stopwords.add("became");
		stopwords.add("because");
		stopwords.add("become");
		stopwords.add("becomes");
		stopwords.add("becoming");
		stopwords.add("been");
		stopwords.add("before");
		stopwords.add("beforehand");
		stopwords.add("begin");
		stopwords.add("beginning");
		stopwords.add("beginnings");
		stopwords.add("begins");
		stopwords.add("behind");
		stopwords.add("being");
		stopwords.add("believe");
		stopwords.add("below");
		stopwords.add("beside");
		stopwords.add("besides");
		stopwords.add("between");
		stopwords.add("beyond");
		stopwords.add("biol");
		stopwords.add("both");
		stopwords.add("brief");
		stopwords.add("briefly");
		stopwords.add("but");
		stopwords.add("by");
		stopwords.add("ca");
		stopwords.add("came");
		stopwords.add("can");
		stopwords.add("cannot");
		stopwords.add("can't");
		stopwords.add("cause");
		stopwords.add("causes");
		stopwords.add("certain");
		stopwords.add("certainly");
		stopwords.add("co");
		stopwords.add("com");
		stopwords.add("come");
		stopwords.add("comes");
		stopwords.add("contain");
		stopwords.add("containing");
		stopwords.add("contains");
		stopwords.add("could");
		stopwords.add("couldn't");
		stopwords.add("date");
		stopwords.add("did");
		stopwords.add("didn't");
		stopwords.add("different");
		stopwords.add("do");
		stopwords.add("does");
		stopwords.add("doesn't");
		stopwords.add("doing");
		stopwords.add("done");
		stopwords.add("don't");
		stopwords.add("down");
		stopwords.add("downwards");
		stopwords.add("due");
		stopwords.add("during");
		stopwords.add("each");
		stopwords.add("ed");
		stopwords.add("edu");
		stopwords.add("effect");
		stopwords.add("eg");
		stopwords.add("eight");
		stopwords.add("eighty");
		stopwords.add("either");
		stopwords.add("else");
		stopwords.add("elsewhere");
		stopwords.add("end");
		stopwords.add("ending");
		stopwords.add("enough");
		stopwords.add("especially");
		stopwords.add("et");
		stopwords.add("etc");
		stopwords.add("even");
		stopwords.add("ever");
		stopwords.add("every");
		stopwords.add("everybody");
		stopwords.add("everyone");
		stopwords.add("everything");
		stopwords.add("everywhere");
		stopwords.add("ex");
		stopwords.add("except");
		stopwords.add("far");
		stopwords.add("few");
		stopwords.add("fifth");
		stopwords.add("first");
		stopwords.add("five");
		stopwords.add("fix");
		stopwords.add("followed");
		stopwords.add("following");
		stopwords.add("follows");
		stopwords.add("for");
		stopwords.add("former");
		stopwords.add("formerly");
		stopwords.add("forth");
		stopwords.add("found");
		stopwords.add("four");
		stopwords.add("from");
		stopwords.add("further");
		stopwords.add("furthermore");
		stopwords.add("gave");
		stopwords.add("get");
		stopwords.add("gets");
		stopwords.add("getting");
		stopwords.add("give");
		stopwords.add("given");
		stopwords.add("gives");
		stopwords.add("giving");
		stopwords.add("go");
		stopwords.add("goes");
		stopwords.add("gone");
		stopwords.add("got");
		stopwords.add("gotten");
		stopwords.add("had");
		stopwords.add("happens");
		stopwords.add("hardly");
		stopwords.add("has");
		stopwords.add("hasn't");
		stopwords.add("have");
		stopwords.add("haven't");
		stopwords.add("having");
		stopwords.add("he");
		stopwords.add("he'd");
		stopwords.add("hence");
		stopwords.add("her");
		stopwords.add("here");
		stopwords.add("hereafter");
		stopwords.add("hereby");
		stopwords.add("herein");
		stopwords.add("here's");
		stopwords.add("hereupon");
		stopwords.add("her's");
		stopwords.add("herself");
		stopwords.add("he's");
		stopwords.add("hi");
		stopwords.add("hid");
		stopwords.add("him");
		stopwords.add("himself");
		stopwords.add("his");
		stopwords.add("hither");
		stopwords.add("home");
		stopwords.add("how");
		stopwords.add("howbeit");
		stopwords.add("however");
		stopwords.add("hundred");
		stopwords.add("i");
		stopwords.add("i'd");
		stopwords.add("ie");
		stopwords.add("if");
		stopwords.add("i'll");
		stopwords.add("i'm");
		stopwords.add("immediate");
		stopwords.add("immediately");
		stopwords.add("importance");
		stopwords.add("important");
		stopwords.add("in");
		stopwords.add("inc");
		stopwords.add("indeed");
		stopwords.add("index");
		stopwords.add("information");
		stopwords.add("instead");
		stopwords.add("into");
		stopwords.add("invention");
		stopwords.add("inward");
		stopwords.add("is");
		stopwords.add("isn't");
		stopwords.add("it");
		stopwords.add("it'd");
		stopwords.add("it'll");
		stopwords.add("its");
		stopwords.add("it's");
		stopwords.add("itself");
		stopwords.add("i've");
		stopwords.add("just");
		stopwords.add("keep");
		stopwords.add("keeps");
		stopwords.add("kept");
		stopwords.add("kg");
		stopwords.add("km");
		stopwords.add("know");
		stopwords.add("known");
		stopwords.add("knows");
		stopwords.add("largely");
		stopwords.add("last");
		stopwords.add("lately");
		stopwords.add("later");
		stopwords.add("latter");
		stopwords.add("latterly");
		stopwords.add("least");
		stopwords.add("less");
		stopwords.add("lest");
		stopwords.add("let");
		stopwords.add("lets");
		stopwords.add("let's");
		stopwords.add("like");
		stopwords.add("liked");
		stopwords.add("likely");
		stopwords.add("line");
		stopwords.add("little");
		stopwords.add("'ll");
		stopwords.add("look");
		stopwords.add("looking");
		stopwords.add("looks");
		stopwords.add("it'd");
		stopwords.add("made");
		stopwords.add("mainly");
		stopwords.add("make");
		stopwords.add("makes");
		stopwords.add("many");
		stopwords.add("may");
		stopwords.add("maybe");
		stopwords.add("me");
		stopwords.add("mean");
		stopwords.add("means");
		stopwords.add("meantime");
		stopwords.add("meanwhile");
		stopwords.add("merely");
		stopwords.add("mg");
		stopwords.add("might");
		stopwords.add("million");
		stopwords.add("miss");
		stopwords.add("ml");
		stopwords.add("more");
		stopwords.add("moreover");
		stopwords.add("most");
		stopwords.add("mostly");
		stopwords.add("mr");
		stopwords.add("mrs");
		stopwords.add("much");
		stopwords.add("mug");
		stopwords.add("must");
		stopwords.add("my");
		stopwords.add("myself");
		stopwords.add("name");
		stopwords.add("namely");
		stopwords.add("nay");
		stopwords.add("near");
		stopwords.add("nearly");
		stopwords.add("necessarily");
		stopwords.add("necessary");
		stopwords.add("need");
		stopwords.add("needs");
		stopwords.add("neither");
		stopwords.add("never");
		stopwords.add("nevertheless");
		stopwords.add("new");
		stopwords.add("next");
		stopwords.add("nine");
		stopwords.add("ninety");
		stopwords.add("no");
		stopwords.add("nobody");
		stopwords.add("non");
		stopwords.add("none");
		stopwords.add("nonetheless");
		stopwords.add("noone");
		stopwords.add("nor");
		stopwords.add("normally");
		stopwords.add("nos");
		stopwords.add("not");
		stopwords.add("noted");
		stopwords.add("nothing");
		stopwords.add("now");
		stopwords.add("nowhere");
		stopwords.add("obtain");
		stopwords.add("obtained");
		stopwords.add("obviously");
		stopwords.add("of");
		stopwords.add("off");
		stopwords.add("often");
		stopwords.add("oh");
		stopwords.add("ok");
		stopwords.add("okay");
		stopwords.add("old");
		stopwords.add("omitted");
		stopwords.add("on");
		stopwords.add("once");
		stopwords.add("one");
		stopwords.add("ones");
		stopwords.add("one's");
		stopwords.add("only");
		stopwords.add("onto");
		stopwords.add("or");
		stopwords.add("ord");
		stopwords.add("other");
		stopwords.add("others");
		stopwords.add("otherwise");
		stopwords.add("ought");
		stopwords.add("our");
		stopwords.add("ours");
		stopwords.add("ourselves");
		stopwords.add("out");
		stopwords.add("outside");
		stopwords.add("over");
		stopwords.add("overall");
		stopwords.add("owing");
		stopwords.add("own");
		stopwords.add("page");
		stopwords.add("pages");
		stopwords.add("part");
		stopwords.add("particular");
		stopwords.add("particularly");
		stopwords.add("past");
		stopwords.add("per");
		stopwords.add("perhaps");
		stopwords.add("placed");
		stopwords.add("please");
		stopwords.add("plus");
		stopwords.add("poorly");
		stopwords.add("possible");
		stopwords.add("possibly");
		stopwords.add("potentially");
		stopwords.add("pp");
		stopwords.add("predominantly");
		stopwords.add("present");
		stopwords.add("previously");
		stopwords.add("primarily");
		stopwords.add("probably");
		stopwords.add("promptly");
		stopwords.add("proud");
		stopwords.add("provides");
		stopwords.add("put");
		stopwords.add("que");
		stopwords.add("quickly");
		stopwords.add("quite");
		stopwords.add("qv");
		stopwords.add("ran");
		stopwords.add("rather");
		stopwords.add("rd");
		stopwords.add("re");
		stopwords.add("readily");
		stopwords.add("really");
		stopwords.add("recent");
		stopwords.add("recently");
		stopwords.add("ref");
		stopwords.add("refs");
		stopwords.add("regarding");
		stopwords.add("regardless");
		stopwords.add("regards");
		stopwords.add("related");
		stopwords.add("relatively");
		stopwords.add("research");
		stopwords.add("respectively");
		stopwords.add("resulted");
		stopwords.add("resulting");
		stopwords.add("results");
		stopwords.add("right");
		stopwords.add("run");
		stopwords.add("said");
		stopwords.add("same");
		stopwords.add("saw");
		stopwords.add("say");
		stopwords.add("saying");
		stopwords.add("says");
		stopwords.add("sec");
		stopwords.add("section");
		stopwords.add("see");
		stopwords.add("seeing");
		stopwords.add("seem");
		stopwords.add("seemed");
		stopwords.add("seeming");
		stopwords.add("seems");
		stopwords.add("seen");
		stopwords.add("self");
		stopwords.add("selves");
		stopwords.add("sent");
		stopwords.add("seven");
		stopwords.add("several");
		stopwords.add("shall");
		stopwords.add("she");
		stopwords.add("she'd");
		stopwords.add("she'll");
		stopwords.add("she's");
		stopwords.add("should");
		stopwords.add("shouldn't");
		stopwords.add("show");
		stopwords.add("showed");
		stopwords.add("shown");
		stopwords.add("showns");
		stopwords.add("shows");
		stopwords.add("significant");
		stopwords.add("significantly");
		stopwords.add("similar");
		stopwords.add("similarly");
		stopwords.add("since");
		stopwords.add("six");
		stopwords.add("slightly");
		stopwords.add("so");
		stopwords.add("some");
		stopwords.add("somebody");
		stopwords.add("somehow");
		stopwords.add("someone");
		stopwords.add("somethan");
		stopwords.add("something");
		stopwords.add("sometime");
		stopwords.add("sometimes");
		stopwords.add("somewhat");
		stopwords.add("somewhere");
		stopwords.add("soon");
		stopwords.add("sorry");
		stopwords.add("specifically");
		stopwords.add("specified");
		stopwords.add("specify");
		stopwords.add("specifying");
		stopwords.add("still");
		stopwords.add("stop");
		stopwords.add("strongly");
		stopwords.add("sub");
		stopwords.add("substantially");
		stopwords.add("successfully");
		stopwords.add("such");
		stopwords.add("sufficiently");
		stopwords.add("suggest");
		stopwords.add("sup");
		stopwords.add("sure");
		stopwords.add("take");
		stopwords.add("taken");
		stopwords.add("taking");
		stopwords.add("tell");
		stopwords.add("tends");
		stopwords.add("th");
		stopwords.add("than");
		stopwords.add("thank");
		stopwords.add("thanks");
		stopwords.add("thanx");
		stopwords.add("that");
		stopwords.add("that'll");
		stopwords.add("thats");
		stopwords.add("that's");
		stopwords.add("that've");
		stopwords.add("the");
		stopwords.add("their");
		stopwords.add("theirs");
		stopwords.add("them");
		stopwords.add("themselves");
		stopwords.add("then");
		stopwords.add("thence");
		stopwords.add("thereafter");
		stopwords.add("thereby");
		stopwords.add("thered");
		stopwords.add("therein");
		stopwords.add("there'll");
		stopwords.add("thereof");
		stopwords.add("there're");
		stopwords.add("theres");
		stopwords.add("there's");
		stopwords.add("therento");
		stopwords.add("thereupon");
		stopwords.add("there've");
		stopwords.add("these");
		stopwords.add("they");
		stopwords.add("they'd");
		stopwords.add("they'll");
		stopwords.add("they're");
		stopwords.add("they've");
		stopwords.add("think");
		stopwords.add("this");
		stopwords.add("those");
		stopwords.add("thou");
		stopwords.add("though");
		stopwords.add("thousand");
		stopwords.add("through");
		stopwords.add("throughout");
		stopwords.add("thus");
		stopwords.add("'til");
		stopwords.add("tip");
		stopwords.add("to");
		stopwords.add("together");
		stopwords.add("too");
		stopwords.add("took");
		stopwords.add("toward");
		stopwords.add("towards");
		stopwords.add("tried");
		stopwords.add("tries");
		stopwords.add("truly");
		stopwords.add("try");
		stopwords.add("trying");
		stopwords.add("ts");
		stopwords.add("twice");
		stopwords.add("two");
		stopwords.add("un");
		stopwords.add("under");
		stopwords.add("unfortunately");
		stopwords.add("unless");
		stopwords.add("unlike");
		stopwords.add("unlikely");
		stopwords.add("until");
		stopwords.add("unto");
		stopwords.add("up");
		stopwords.add("upon");
		stopwords.add("ups");
		stopwords.add("us");
		stopwords.add("use");
		stopwords.add("used");
		stopwords.add("useful");
		stopwords.add("usefully");
		stopwords.add("usefulness");
		stopwords.add("uses");
		stopwords.add("using");
		stopwords.add("usually");
		stopwords.add("value");
		stopwords.add("various");
		stopwords.add("'ve");
		stopwords.add("very");
		stopwords.add("via");
		stopwords.add("viz");
		stopwords.add("vol");
		stopwords.add("vols");
		stopwords.add("vs");
		stopwords.add("want");
		stopwords.add("wants");
		stopwords.add("was");
		stopwords.add("wasn't");
		stopwords.add("way");
		stopwords.add("we");
		stopwords.add("we'd");
		stopwords.add("welcome");
		stopwords.add("we'll");
		stopwords.add("went");
		stopwords.add("were");
		stopwords.add("weren't");
		stopwords.add("we've");
		stopwords.add("what");
		stopwords.add("whatever");
		stopwords.add("what'll");
		stopwords.add("whats");
		stopwords.add("what's");
		stopwords.add("when");
		stopwords.add("whence");
		stopwords.add("whenever");
		stopwords.add("where");
		stopwords.add("whereafter");
		stopwords.add("whereas");
		stopwords.add("whereby");
		stopwords.add("wherein");
		stopwords.add("wheres");
		stopwords.add("where's");
		stopwords.add("whereupon");
		stopwords.add("wherever");
		stopwords.add("whether");
		stopwords.add("which");
		stopwords.add("while");
		stopwords.add("whim");
		stopwords.add("whither");
		stopwords.add("who");
		stopwords.add("whod");
		stopwords.add("who'd");
		stopwords.add("whoever");
		stopwords.add("whole");
		stopwords.add("who'll");
		stopwords.add("whom");
		stopwords.add("whomever");
		stopwords.add("whos");
		stopwords.add("who's");
		stopwords.add("whose");
		stopwords.add("why");
		stopwords.add("widely");
		stopwords.add("willing");
		stopwords.add("wish");
		stopwords.add("with");
		stopwords.add("within");
		stopwords.add("without");
		stopwords.add("wont");
		stopwords.add("won't");
		stopwords.add("words");
		stopwords.add("would");
		stopwords.add("wouldnt");
		stopwords.add("wouldn't");
		stopwords.add("yes");
		stopwords.add("yet");
		stopwords.add("you");
		stopwords.add("you'd");
		stopwords.add("you'll");
		stopwords.add("your");
		stopwords.add("you're");
		stopwords.add("yours");
		stopwords.add("yourself");
		stopwords.add("yourselves");
		stopwords.add("you've");
		stopwords.add("zero");

		if (stopwords.contains(str)) {
			return true;
		} else {
			return false;
		}
	}
}