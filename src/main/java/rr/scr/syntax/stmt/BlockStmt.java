package rr.scr.syntax.stmt;

import static rr.scr.lexical.Token.Type.SYMBOL_COLON;
import static rr.scr.lexical.Token.Type.SYMBOL_OPEN_BRACE;
import static rr.scr.lexical.Token.Type.SYMBOL_OPRIOD;

import java.util.Stack;

import rr.scr.exception.SyntaxException;
import rr.scr.lexical.Token;

public class BlockStmt extends AbstractStatement {
	private Stack<Token> block = null;
	
	public BlockStmt(Stack<Token> list) {
		super(list);

		Token t = null;

		t = list.get(0);
		if (!t.getType().equals(SYMBOL_OPEN_BRACE)) {
			throw new SyntaxException("not block statement");
		}

		block = nextBlock();

		lockList();

		// 块的结束符
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
		final Stack<Token> b = new Stack<Token>();
		if (block != null) {
			b.addAll(block);
		} else {
			return false;
		}

		me = new Thread() {
			public void run() {
				lock();
				new Block(b).execute();
				unlock();
			}
		};
		me.start();

		return true;
	}

}
