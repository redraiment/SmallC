package rr.scr.syntax.expr;

import static rr.scr.lexical.Token.Type.*;

import java.util.Stack;

import rr.scr.lexical.Token;
import rr.scr.util.Value;

/**
 * level5value := <level5value> && <level4value>
 *              | <level5value> || <level4value>
 *              | <level4value>
 *
 * @author redraiment
 *
 */
public class Level5Value implements Item {
	private Level5Value left = null;
	private Level4Value right = null;
	private Token op = null;

	public Level5Value(Stack<Token> list) {
		right = new Level4Value(list);
		if (!list.empty()) {
			op = list.peek();
			if (op.getType().equals(OPERATOR_AND)
					|| op.getType().equals(OPERATOR_OR)) {
				list.pop();
				left = new Level5Value(list);
			} else {
				op = null;
			}
		}
	}

	@Override
	public Value value() {
		if (op == null) {
			return right.value();
		} else if (op.getType().equals(OPERATOR_AND)) {
			return left.value().and(right.value());
		} else if (op.getType().equals(OPERATOR_OR)) {
			return left.value().or(right.value());
		}
		return null;
	}
}
