package rr.scr.syntax.expr;

import static rr.scr.lexical.Token.Type.*;

import java.util.Stack;

import rr.scr.exception.SyntaxException;
import rr.scr.lexical.Token;
import rr.scr.util.Value;

/**
 * level1value := ++<variable>
 *           | --<variable>
 *           | <variable>++
 *           | <variable>--
 *           | -<factor>
 *           | +<factor>
 *           | !<factor>
 *           | <factor>
 *
 * @author redraiment
 *
 */
public class Level1Value implements Item {
	private Value value = null;

	public Level1Value(Stack<Token> list) {
		if (list.empty()) {
			// Error
			throw new SyntaxException("level 1 value");
		} else if (list.peek().getType().equals(OPERATOR_INCREMENT)) {
			// i++
			list.pop();
			Value var = new Variable(list).value();
			value = var.clone().toInteger();
			var.assign(var.increment());
		} else if (list.peek().getType().equals(OPERATOR_DECREMENT)) {
			// i--
			list.pop();
			Value var = new Variable(list).value();
			value = var.clone().toInteger();
			var.assign(var.decrement());
		} else if (list.size() > 1) {
			int i = list.size() - 1;
			// 先过滤 ( expression )
			// 2010-05-18
			if (list.get(i).getType().equals(OPERATOR_CLOSE_PAREN)) {
				int count = 0;
				do {
					if (list.get(i).getType().equals(OPERATOR_CLOSE_PAREN)) {
						count++;
					} else if (list.get(i).getType().equals(OPERATOR_OPEN_PAREN)) {
						count--;
					}
					i--;
				} while (i >= 0 && count != 0);
				if (count != 0) {
					throw new SyntaxException("paren don't match");
				}
			}

			while (i >= 0 && (!list.get(i).getType()
					.name().startsWith("OPERATOR")
					|| list.get(i).getType().equals(OPERATOR_CLOSE_PAREN)
					|| list.get(i).getType().equals(OPERATOR_OPEN_PAREN))) {
				i--;
			}
			if (i < 0) {
				// 没有操作符
				value = new Factor(list).value();
			} else if (list.get(i).getType().equals(OPERATOR_INCREMENT)) {
				// ++i
				value = new Variable(list).value();
				value.assign(value.increment());
				list.pop();
			} else if (list.get(i).getType().equals(OPERATOR_DECREMENT)) {
				// --i
				value = new Variable(list).value();
				value.assign(value.decrement());
				list.pop();
			} else if (list.get(i).getType().equals(OPERATOR_NOT)) {
				value = new Factor(list).value().not();
				list.pop();
			} else if (list.get(i).getType().equals(OPERATOR_POSITIVE)) {
				// ignore
				value = new Factor(list).value();
				list.pop();
			} else if (list.get(i).getType().equals(OPERATOR_NEGATIVE)) {
				value = new Factor(list).value().negate();
				list.pop();
			} else {
				value = new Factor(list).value();
			}
		} else {
			value = new Factor(list).value();
		}
	}

	@Override
	public Value value() {
		return value;
	}
}
