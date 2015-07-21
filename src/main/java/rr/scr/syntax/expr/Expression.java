package rr.scr.syntax.expr;

import static rr.scr.lexical.Token.Type.ID;
import static rr.scr.lexical.Token.Type.SYMBOL_CLOSE_BRACKET;
import static rr.scr.lexical.Token.Type.SYMBOL_OPEN_BRACKET;

import java.util.Stack;

import rr.scr.exception.SyntaxException;
import rr.scr.lexical.Token;
import rr.scr.util.Value;

/**
 * From left to right
 * 
 * expression := <id> = <level5value>
 *             | <id> *= <level5value>
 *             | <id> /= <level5value>
 *             | <id> %= <level5value>
 *             | <id> += <level5value>
 *             | <id> -= <level5value>
 *             | <level5value>
 *
 * @author redraiment
 *
 */
public class Expression implements Item {
	private Value left = null;
	private Value right = null;
	private Token op = null;

	public Expression(Stack<Token> list) {
		Stack<Token> sub = new Stack<Token>();
		int p = 0;
		if (list.size() > 2 && list.get(0).getType().equals(ID)) {
			sub.add(list.get(0));
			p = 1;
			if (list.get(1).getType().equals(SYMBOL_OPEN_BRACKET)) {
				while (p < list.size()
					&& !list.get(p).getType().equals(SYMBOL_CLOSE_BRACKET)) {
					sub.add(list.get(p));
					p++;
				}
				sub.add(list.get(p));
				p++;
			}
			if (p < list.size()) {
				op = list.get(p);
				p++;
			}
		}
		if (op != null && op.getType().name().endsWith("ASSIGNMENT")) {
			for (int i = 0; i < p; i++) {
				list.remove(0);
			}
			right = new Expression(list).value();

			// 先计算赋值符号右边的值，再算左边
			// 比如：a[i] += (a[i] = 3) * (i = 4);
			// 如果先计算左边，就等价于
			//    a[i] += (a[i] = 3) * (i = 4);
			// => a[0] += (a[i] = 3) * (i = 4);
			// => a[0] += (a[i] = 3) * 4;
			// => a[0] += (a[4] = 3) * 4;
			// => a[0] += 3 * 4;
			// => a[0] = 0 + 12;
			// 结果是 12，而预期是 15
			left = new Variable(sub).value();

			switch (op.getType()) {
			case OPERATOR_ASSIGNMENT:
				left.assign(right);
				break;
			case OPERATOR_MUL_ASSIGNMENT:
				left.assign(left.multiply(right));
				break;
			case OPERATOR_DIV_ASSIGNMENT:
				left.assign(left.divide(right));
				break;
			case OPERATOR_MOD_ASSIGNMENT:
				left.assign(left.mod(right));
				break;
			case OPERATOR_ADD_ASSIGNMENT:
				left.assign(left.add(right));
				break;
			case OPERATOR_SUB_ASSIGNMENT:
				left.assign(left.subtract(right));
				break;
			default:
				throw new SyntaxException("unknow assignment");
			}
		} else {
			op = null;
			right = new Level5Value(list).value();
		}
	}

	@Override
	public Value value() {
		return op == null? right: left;
	}
}
