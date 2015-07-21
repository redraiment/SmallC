package rr.scr.lexical;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static rr.scr.lexical.Token.Type.*;
import rr.scr.exception.SyntaxException;

public class Tokenizer {
	private StringBuilder source = null;
	private Token[] model = {
		new Token(VALUE_STRING, "\"[^\\\"]*(\\.[^\\\"]*)*\""),
		new Token(VALUE_INTGER, "\\b\\d+\\b"),
		new Token(KEYWORD_FUNCTION, "\\bfunction\\b"),
		new Token(KEYWORD_IF, "\\bif\\b"),
		new Token(KEYWORD_ELSIF, "\\belsif\\b"),
		new Token(KEYWORD_ELSE, "\\belse\\b"),
		new Token(KEYWORD_FOR, "\\bfor\\b"),
		new Token(KEYWORD_DO, "\\bdo\\b"),
		new Token(KEYWORD_WHILE, "\\bwhile\\b"),
		new Token(KEYWORD_ON, "\\bon\\b"),
		new Token(KEYWORD_LOCKING, "\\blocking\\b"),
		new Token(KEYWORD_RETURN, "\\breturn\\b"),
		new Token(ID, "\\b[A-Za-z]\\w*\\b"),
		new Token(COMMENT, "/\\*[^*]*(\\*[^*]*)*\\*/"),
		// 以下方法已经过时，详见 nextComment()的注释
		// new Token(OPEN_COMMENT, "/\\*"),
		// new Token(CLOSE_COMMENT, "\\*/"),
		new Token(OPERATOR_INCREMENT, "\\+\\+"),
		new Token(OPERATOR_DECREMENT, "--"),
		new Token(OPERATOR_EQUAL, "=="),
		new Token(OPERATOR_NOT_EQUAL, "!="),
		new Token(OPERATOR_LESS_EQUAL, "<="),
		new Token(OPERATOR_GREAT_EQUAL, ">="),
		new Token(OPERATOR_AND, "&&"),
		new Token(OPERATOR_OR, "\\|\\|"),
		new Token(OPERATOR_ADD_ASSIGNMENT, "\\+="),
		new Token(OPERATOR_SUB_ASSIGNMENT, "-="),
		new Token(OPERATOR_MUL_ASSIGNMENT, "\\*="),
		new Token(OPERATOR_DIV_ASSIGNMENT, "/="),
		new Token(OPERATOR_MOD_ASSIGNMENT, "%="),
		new Token(OPERATOR_NOT, "!"),
		new Token(OPERATOR_ASSIGNMENT, "="),
		new Token(OPERATOR_PLUS, "\\+"),
		new Token(OPERATOR_MINUS, "-"),
		new Token(OPERATOR_MULTIPLY, "\\*"),
		new Token(OPERATOR_DIVIDE, "/"),
		new Token(OPERATOR_MOD, "%"),
		new Token(OPERATOR_LESS, "<"),
		new Token(OPERATOR_GREAT, ">"),
		new Token(OPERATOR_OPEN_PAREN, "\\("),
		new Token(OPERATOR_CLOSE_PAREN, "\\)"),
		new Token(SYMBOL_OPEN_BRACKET, "\\["),
		new Token(SYMBOL_CLOSE_BRACKET, "\\]"),
		new Token(SYMBOL_OPEN_BRACE, "\\{"),
		new Token(SYMBOL_CLOSE_BRACE, "\\}"),
		new Token(SYMBOL_COMMA, ","),
		new Token(SYMBOL_COLON, ";"),
		new Token(SYMBOL_OPRIOD, "\\."),
		new Token(OTHER, "."),
	};

	/** 用于判断正负号 */
	private Token last = null;
	
	public Tokenizer(InputStream in) {
		source = new StringBuilder();

		try {
			byte[] buffer = new byte[512];
			while (true) {
				int len = in.read(buffer);
				if (len <= 0) {
					break;
				}
				source.append(new String(buffer, 0, len));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public Tokenizer(String srcFile) throws FileNotFoundException {
		this(new BufferedInputStream(new FileInputStream(srcFile)));
	}

	public boolean hasMoreToken() {
		source = new StringBuilder(source.toString().trim());
		return source.length() > 0;
	}

	public Token nextToken() {
		if (!hasMoreToken()) {
			return null;
		}

		Token token = null;
		Matcher m = Pattern.compile(".").matcher(source);
		for (Token t : model) {
			m.usePattern(t.getPattern());
			if (m.lookingAt()) {
				if ((t.getType().equals(OPERATOR_PLUS)
					|| t.getType().equals(OPERATOR_MINUS))
					&& (last == null
					|| (!last.getType().equals(OPERATOR_OPEN_PAREN)
					&& !last.getType().equals(OPERATOR_CLOSE_PAREN)
					&& !last.getType().equals(SYMBOL_OPEN_BRACKET)
					&& !last.getType().equals(SYMBOL_CLOSE_BRACKET)
					&& !last.getType().equals(ID)
					&& !last.getType().name().startsWith("VALUE")))) {
					if (t.getType().equals(OPERATOR_PLUS)) {
						token = new Token(OPERATOR_POSITIVE, "\\+");
					} else {
						token = new Token(OPERATOR_NEGATIVE, "-");
					}
				} else {
					token = t.clone();
				}
				token.setValue(m.group());
				source.delete(m.start(), m.end());
				return last = token;
			}
		}

		throw new SyntaxException(source.toString());
	}
}
