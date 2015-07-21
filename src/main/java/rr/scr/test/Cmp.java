package rr.scr.test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Cmp {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Pattern p = Pattern.compile("\\b\"[^\\\"]*(\\.[^\\\"]*)*\"\\b");
		Matcher m = p.matcher("\"Hello\\n\"");
		System.out.println(p.pattern() + ": " + m.matches());
		System.out.println("\"Hello\\n\"".matches("\"[^\\\"]*(\\.[^\\\"]*)*\""));
	}

}
