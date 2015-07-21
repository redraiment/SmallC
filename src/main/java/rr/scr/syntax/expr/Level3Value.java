package rr.scr.syntax.expr;

import static rr.scr.lexical.Token.Type.*;

import java.util.Stack;

import rr.scr.lexical.Token;
import rr.scr.util.Value;

/**
 * level3value := <level3value> + <level2value>
 *              | <level3value> - <level2value>
 *              | <level2value>
 *
 * @author redraiment
 *
 */
public class Level3Value implements Item {
	private Level3Value left = null;
	private Level2Value right = null;
	private Token op = null;

	public Level3Value(Stack<Token> list) {
		right = new Level2Value(list);
		if (!list.empty()) {
			op = list.peek();
			if (op.getType().equals(OPERATOR_PLUS)
					|| op.getType().equals(OPERATOR_MINUS)) {
				list.pop();
				left = new Level3Value(list);
			} else {
				op = null;
			}
		}
	}

	@Override
	public Value value() {
		if (op == null) {
			return right.value();
		} else if (op.getType().equals(OPERATOR_PLUS)) {
			return left.value().add(right.value());
		} else if (op.getType().equals(OPERATOR_MINUS)) {
			return left.value().subtract(right.value());
		}
		return null;
	}
}
