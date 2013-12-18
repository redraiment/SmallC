package rr.scr.syntax.expr;

import static rr.scr.lexical.Token.Type.*;

import java.util.Stack;

import rr.scr.exception.SyntaxException;
import rr.scr.lexical.Token;
import rr.scr.util.Value;
import rr.scr.vm.Machine;

/**
 * variable := <id>
 *           | <array>
 *
 * @author redraiment
 *
 */
public class Variable implements Item {
	private String id = null;

	public Variable(Stack<Token> list) {
		if (list.empty()) {
			throw new SyntaxException("expect id");
		} else if (list.peek().getType().equals(ID)) {
			// Variable
			id = list.pop().getValue();
		} else if (list.peek().getType().equals(SYMBOL_CLOSE_BRACKET)) {
			// Array
			Stack<Token> sub = new Stack<Token>();

			list.pop();
			while (!list.empty() && !list.peek().getType()
					.equals(SYMBOL_OPEN_BRACKET)) {
				sub.add(0, list.pop());
			}
			list.pop();
			if (!list.empty() && list.peek().getType().equals(ID)) {
				// id [ expression ]
				Value index = new Expression(sub).value();
				id = list.pop().getValue() + "$" + index.toString();
			}
		} else {
			throw new SyntaxException("not a variable");
		}
	}

	public String getName() {
		return id;
	}

	@Override
	public Value value() {
		return Machine.getVM().getValue(id);
	}
}
