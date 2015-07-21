package rr.scr.syntax.stmt;

import static rr.scr.lexical.Token.Type.KEYWORD_ELSE;
import static rr.scr.lexical.Token.Type.KEYWORD_ELSIF;
import static rr.scr.lexical.Token.Type.KEYWORD_IF;
import static rr.scr.lexical.Token.Type.SYMBOL_COLON;
import static rr.scr.lexical.Token.Type.SYMBOL_OPRIOD;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import rr.scr.exception.SyntaxException;
import rr.scr.lexical.Token;
import rr.scr.syntax.expr.Expression;

/**
 * if-statement := if ( <expression> ) <block-statement>
 *                 [[elsif <block-statement>]
 *                 else <block-statement>]
 *
 * @author redraiment
 *
 */
public class IfStmt extends AbstractStatement {
	private List<Stack<Token>> expr = null;
	private List<Stack<Token>> block = null;

	public IfStmt(Stack<Token> list) {
		super(list);

		expr = new ArrayList<Stack<Token>>();
		block = new ArrayList<Stack<Token>>();

		Token t = null;

		t = list.remove(0);
		if (!t.getType().equals(KEYWORD_IF)) {
			throw new SyntaxException("not if statement");
		}
		
		expr.add(nextExpression());
		block.add(nextBlock());

		// Optional
		while (list.firstElement().getType().equals(KEYWORD_ELSIF)) {
			list.remove(0);
			expr.add(nextExpression());
			block.add(nextBlock());
		}

		if (list.firstElement().getType().equals(KEYWORD_ELSE)) {
			list.remove(0);
			// else 没有判断语句
			expr.add(null);
			block.add(nextBlock());
		}
		
		lockList();
		// end Optional

		t = list.remove(0);
		if (t.getType().equals(SYMBOL_COLON)) {
			setEnd(false);
		} else if (t.getType().equals(SYMBOL_OPRIOD)) {
			setEnd(true);
		} else {
			throw new SyntaxException("expect end symbol");
		}
	}

	@Override
	public boolean execute() {
		me = new Thread() {
			public void run() {
				lock();
				for (int i = 0; i < expr.size(); i++) {
					Stack<Token> t = expr.get(i);
					if (t == null || new Expression(t).value().isTrue()) {
						Stack<Token> b = new Stack<Token>();
						b.addAll(block.get(i));
						new Block(b).execute();
						break;
					}
				}
				unlock();
			}
		};
		me.start();

		return true;
	}
}
