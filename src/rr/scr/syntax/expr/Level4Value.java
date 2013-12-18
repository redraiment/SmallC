package rr.scr.syntax.expr;

import static rr.scr.lexical.Token.Type.*;

import java.util.Stack;

import rr.scr.lexical.Token;
import rr.scr.util.Value;

/**
 * level4value := <level4value> == <level3value>
 *              | <level4value> != <level3value>
 *              | <level4value> >= <level3value>
 *              | <level4value> <= <level3value>
 *              | <level4value> > <level3value>
 *              | <level4value> < <level3value>
 *              | <level3value>
 *
 * @author redraiment
 *
 */
public class Level4Value implements Item {
	private Level4Value left = null;
	private Level3Value right = null;
	private Token op = null;

	public Level4Value(Stack<Token> list) {
		right = new Level3Value(list);
		if (!list.empty()) {
			op = list.peek();
			if (op.getType().equals(OPERATOR_LESS)
					|| op.getType().equals(OPERATOR_GREAT)
					|| op.getType().equals(OPERATOR_EQUAL)
					|| op.getType().equals(OPERATOR_NOT_EQUAL)
					|| op.getType().equals(OPERATOR_LESS_EQUAL)
					|| op.getType().equals(OPERATOR_GREAT_EQUAL)) {
				list.pop();
				left = new Level4Value(list);
			} else {
				op = null;
			}
		}
	}

	@Override
	public Value value() {
		if (op == null) {
			return right.value();
		} else if (op.getType().equals(OPERATOR_LESS)) {
			return left.value().less(right.value());
		} else if (op.getType().equals(OPERATOR_GREAT)) {
			return left.value().great(right.value());
		} else if (op.getType().equals(OPERATOR_EQUAL)) {
			return left.value().equals(right.value());
		} else if (op.getType().equals(OPERATOR_NOT_EQUAL)) {
			return left.value().notEquals(right.value());
		} else if (op.getType().equals(OPERATOR_LESS_EQUAL)) {
			return left.value().lessEquals(right.value());
		} else if (op.getType().equals(OPERATOR_GREAT_EQUAL)) {
			return left.value().greatEquals(right.value());
		}
		return null;
	}
}
