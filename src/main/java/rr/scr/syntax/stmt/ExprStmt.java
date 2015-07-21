package rr.scr.syntax.stmt;

import static rr.scr.lexical.Token.Type.KEYWORD_ON;
import static rr.scr.lexical.Token.Type.SYMBOL_COLON;
import static rr.scr.lexical.Token.Type.SYMBOL_OPRIOD;

import java.util.Stack;

import rr.scr.exception.SyntaxException;
import rr.scr.lexical.Token;
import rr.scr.syntax.expr.Expression;

/**
 * := <expression> [on locking <IDs>] ;
 *  | <expression> [on locking <IDs>] .
 *
 * @author redraiment
 *
 */
public class ExprStmt extends AbstractStatement {
	private Stack<Token> expr = null;

	public ExprStmt(Stack<Token> list) {
		super(list);

		Token t = null;

		// 表达式
		expr = new Stack<Token>();
		while (!list.firstElement().getType().equals(SYMBOL_COLON)
				&& !list.firstElement().getType().equals(SYMBOL_OPRIOD)
				&& !list.firstElement().getType().equals(KEYWORD_ON)) {
			expr.add(list.remove(0));
		}
		// Optional
		lockList();

		// 结束符
		t = list.remove(0);
		if (t.getType().equals(SYMBOL_COLON)) {
			setEnd(false);
		} else if (t.getType().equals(SYMBOL_OPRIOD)) {
			setEnd(true);
		} else {
			throw new SyntaxException("expect end symbol with global");
		}
	}

	@Override
	public boolean execute() {
		me = new Thread() {
			public void run() {
				lock();
				Stack<Token> e = new Stack<Token>();
				e.addAll(expr);
				new Expression(e).value();
				unlock();
			}
		};
		me.start();

		return true;
	}

}
