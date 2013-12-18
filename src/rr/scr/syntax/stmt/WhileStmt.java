package rr.scr.syntax.stmt;

import static rr.scr.lexical.Token.Type.*;

import java.util.Stack;

import rr.scr.exception.SyntaxException;
import rr.scr.lexical.Token;
import rr.scr.syntax.expr.Expression;
import rr.scr.util.ThreadSet;

/**
 * while-statement := while ( <expression> ) <block>
 *
 * @author redraiment
 *
 */
public class WhileStmt extends AbstractStatement {
	private Stack<Token> expr = null;
	private BlockStmt block = null;

	public WhileStmt(Stack<Token> list) {
		super(list);

		Token t = null;

		t = list.remove(0);
		if (!t.getType().equals(KEYWORD_WHILE)) {
			throw new SyntaxException("not while statement");
		}

		// 表达式
		expr = nextExpression();
		if (expr != null && expr.size() == 0) {
			throw new SyntaxException("expect expression");
		}

		block = new BlockStmt(list);

		// Optional
		lockList();
		
		// 循环体外的结束符
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
				ThreadSet ths = new ThreadSet();
				while (true) {
					// Expression 里会清空列表
					// 所以需要重新拷贝一份，下同
					Stack<Token> e = new Stack<Token>();
					e.addAll(expr);
					if (!new Expression(e).value().isTrue()) {
						break;
					}

					Thread local = new Thread() {
						public void run() {
							block.execute();
							// block 内部开辟的新的线程
							// 需要等子线程结束
							block.waitFor();
						}
					};
					ths.add(local);
					local.start();
					if (!block.isEnd()) {
						// 循环内层是串行
						try {
							local.join();
						} catch (InterruptedException ie) {
							ie.printStackTrace();
						}
					}
				}
				ths.join();

				unlock();
			}
		};
		me.start();

		return true;
	}

}
