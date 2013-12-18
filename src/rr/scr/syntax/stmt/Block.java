package rr.scr.syntax.stmt;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import rr.scr.lexical.Token;
import rr.scr.util.ThreadSet;

/**
 * block := <block-statement> [on locking <IDs>] ;
 *        | <block-statement> [on locking <IDs>] .
 *
 * block-statement := { <statements> }
 *
 * statements := <statement> [<statements>]
 *             | <EMPTY>
 *
 * @author redraiment
 *
 */
public class Block extends AbstractStatement {
	private ThreadSet ts = new ThreadSet();

	public Block(Stack<Token> list) {
		super(list);

		while (!list.isEmpty()) {
			final List<Statement> stmts = new ArrayList<Statement>();
			Statement s = null;
			do {
				s = null;
				switch (list.firstElement().getType()) {
				case KEYWORD_IF:
					s = new IfStmt(list);
					break;
				case KEYWORD_FOR:
					s = new ForStmt(list);
					break;
				case KEYWORD_WHILE:
					s = new WhileStmt(list);
					break;
				case KEYWORD_DO:
					s = new DoStmt(list);
					break;
				case SYMBOL_OPEN_BRACE:
					// 嵌套块解析
					s = new BlockStmt(list);
					break;
				case KEYWORD_FUNCTION:
					new FuncStmt(list);
					break;
				case COMMENT:
					// 注释？那就别理它...牛仔很忙
					list.remove(0);
					break;
				// 以下方法已过时
				// case OPEN_COMMENT:
				// nextComment();
				//	break;
				default:
					s = new ExprStmt(list);
					break;
				}
				if (s != null) {
					stmts.add(s);
				}
			} while (!list.isEmpty() && (s == null || !s.isEnd()));
			ts.add(new Thread() {
				public void run() {
					for (int i = 0; i < stmts.size(); i++) {
						if (stmts.get(i).execute()) {
							stmts.get(i).waitFor();
						}
					}
				}
			});
		}
	}

	@Override
	public boolean execute() {
		ts.start();
		ts.join();

		return true;
	}
}
