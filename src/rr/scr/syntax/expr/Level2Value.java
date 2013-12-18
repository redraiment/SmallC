package rr.scr.syntax.expr;

import static rr.scr.lexical.Token.Type.OPERATOR_DIVIDE;
import static rr.scr.lexical.Token.Type.OPERATOR_MOD;
import static rr.scr.lexical.Token.Type.OPERATOR_MULTIPLY;

import java.util.Stack;

import rr.scr.lexical.Token;
import rr.scr.util.Value;

/**
 * level2value := <level2value> * <level1value>
 *              | <level2value> / <level1value>
 *              | <level2value> % <level1value>
 *              | <level1value>
 *
 * @author redraiment
 *
 */
public class Level2Value implements Item {
	private Level2Value left = null;
	private Level1Value right = null;
	private Token op = null;

	public Level2Value(Stack<Token> list) {
		right = new Level1Value(list);
		if (!list.empty()) {
			op = list.peek();
			if (op.getType().equals(OPERATOR_MULTIPLY)
					|| op.getType().equals(OPERATOR_DIVIDE)
					|| op.getType().equals(OPERATOR_MOD)) {
				list.pop();
				left = new Level2Value(list);
			} else {
				op = null;
			}
		}
	}

	@Override
	public Value value() {
		if (op == null) {
			return right.value();
		} else if (op.getType().equals(OPERATOR_MULTIPLY)) {
			return left.value().multiply(right.value());
		} else if (op.getType().equals(OPERATOR_DIVIDE)) {
			return left.value().divide(right.value());
		} else if (op.getType().equals(OPERATOR_MOD)) {
			return left.value().mod(right.value());
		}
		return null;
	}
}
