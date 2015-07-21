package rr.scr.vm;

import java.util.Stack;

import rr.scr.lexical.Lexer;
import rr.scr.lexical.Token;
import rr.scr.syntax.Yacc;

/**
 * Small C, or Parallel C? I like call it "small", just for fun.
 * 
 * @author redraiment
 *
 */
public class SmallC {

	/**
	 * 
	 * @param args 鑴氭湰鏂囦欢鍒楄〃
	 */
	public static void main(String[] args) {
		Stack<Token> code = new Stack<Token>();

		if (args.length == 0) {
			// 既然没有文件，那就从终端吧？
			// BTW, I suggest you take an alias for "java -jar sc.jar". 
			// 2010－05-19
			// System.err.println("Usage: sc script [script ...]");
			Lexer lex = Lexer.load(System.in);
			code.addAll(lex.parse());
		} else {
			for (String name : args) {
				Lexer lex = Lexer.load(name);
				code.addAll(lex.parse());
			}
		}
		new Yacc(code).parse();
	}
}
