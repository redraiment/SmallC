package rr.scr.syntax.stmt;

import static rr.scr.lexical.Token.Type.KEYWORD_FOR;
import static rr.scr.lexical.Token.Type.OPERATOR_CLOSE_PAREN;
import static rr.scr.lexical.Token.Type.OPERATOR_MIN;
import static rr.scr.lexical.Token.Type.OPERATOR_OPEN_PAREN;
import static rr.scr.lexical.Token.Type.SYMBOL_COLON;
import static rr.scr.lexical.Token.Type.SYMBOL_OPRIOD;

import java.util.Stack;

import rr.scr.exception.SyntaxException;
import rr.scr.lexical.Token;
import rr.scr.syntax.expr.Expression;
import rr.scr.util.ThreadSet;

/**
 * for-statement := for ( <expression> ; <expression> ; <expression> ) <block>
 *
 * @author redraiment
 *
 */
public class ForStmt extends AbstractStatement {
	private Stack<Token> expr1 = null;
	private Stack<Token> expr2 = null;
	private Stack<Token> expr3 = null;
	private BlockStmt block = null;

	public ForStmt(Stack<Token> list) {
		super(list);

		Token t = null;

		t = list.remove(0);
		if (!t.getType().equals(KEYWORD_FOR)) {
			throw new SyntaxException("not for statement");
		}

		// 三个表达式
		expr1 = range(OPERATOR_OPEN_PAREN, SYMBOL_COLON);
		if (expr1 != null && expr1.size() == 0) {
			expr1 = null;
		}
		list.add(0, new Token(OPERATOR_MIN, "#"));
		expr2 = range(OPERATOR_MIN, SYMBOL_COLON);
		if (expr2 != null && expr2.size() == 0) {
			expr2 = null;
		}
		list.add(0, new Token(OPERATOR_MIN, "#"));
		expr3 = range(OPERATOR_MIN, OPERATOR_CLOSE_PAREN);
		if (expr3 != null && expr3.size() == 0) {
			expr3 = null;
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
				if (expr1 != null) {
					new Expression(expr1).value();
				}

				ThreadSet ths = new ThreadSet();
				while (true) {
					if (expr2 != null) {
						// Expression 里会清空列表
						// 所以需要重新拷贝一份，下同
						Stack<Token> e = new Stack<Token>();
						e.addAll(expr2);
						if (!new Expression(e).value().isTrue()) {
							break;
						}
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
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}

					if (expr3 != null) {
						Stack<Token> e = new Stack<Token>();
						e.addAll(expr3);
						new Expression(e).value();
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
