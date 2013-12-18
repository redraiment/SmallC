package rr.scr.lexical;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Stack;

public final class Lexer {
	private Tokenizer tk = null;
	private Stack<Token> list = null;

	public static Lexer load(String file) {
		return new Lexer(file);
	}
	
	public static Lexer load(InputStream in) {
		return new Lexer(in);
	}

	private Lexer(String file) {
		try {
			tk = new Tokenizer(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	private Lexer(InputStream in) {
		tk = new Tokenizer(in);
	}

	public Stack<Token> parse() {
		if (list == null) {
			list = new Stack<Token>();
			while (tk.hasMoreToken()) {
				list.add(tk.nextToken());
			}
		}

		return list;
	}
}
