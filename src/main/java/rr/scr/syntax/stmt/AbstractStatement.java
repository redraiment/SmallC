package rr.scr.syntax.stmt;

import static rr.scr.lexical.Token.Type.*;

import java.util.HashSet;
import java.util.Set;
import java.util.Stack;

import rr.scr.exception.SyntaxException;
import rr.scr.lexical.Token;
import rr.scr.lexical.Token.Type;
import rr.scr.syntax.expr.Variable;
import rr.scr.vm.Machine;

public abstract class AbstractStatement implements Statement {
	// 为了时列表里的id能动态解析，从 String -> Stack<Token>
	// 2010-05-17 by redraiment
	private Set<Stack<Token>> locking = new HashSet<Stack<Token>>();
	// private Set<String> locking = new HashSet<String>();

	private Machine m = Machine.getVM();
	private boolean e = false;

	protected Thread me = null;
	protected Stack<Token> list = null;

	public AbstractStatement(Stack<Token> list) {
		this.list = list;
	}

	@Override
	public void addLocking(Stack<Token> var) {
		// locking.add(new Variable(var).getName());
		// -- see also the comment on locking field
		locking.add(var);
	}

	protected void lock() {
		if (locking.size() == 0) {
			return;
		}

		// Machine Lock
		// 全局锁：避免给多个变量加锁时出现死锁
		m.lock();

		// 动态解析 ID
		for (Stack<Token> name : locking) {
			Stack<Token> id = new Stack<Token>();
			id.addAll(name);
			m.getLocks(new Variable(id).getName()).lock();
		}

		// -- see also the comment on locking field
		// for (String id : locking) {
		// m.getLocks(id).lock();
		// }
	}

	protected void unlock() {
		if (locking.size() == 0) {
			return;
		}

		// 动态解析 ID
		for (Stack<Token> name : locking) {
			Stack<Token> id = new Stack<Token>();
			id.addAll(name);
			m.getLocks(new Variable(id).getName()).unlock();
		}
		// for (String id : locking) {
		// m.getLocks(id).unlock();
		// }

		// 理论上也可以在 for 之前释放，而且能有更好的性能
		// 但还是稳当点好，呵呵。
		m.unlock();
	}

	@Override
	public boolean isEnd() {
		return e;
	}

	@Override
	public void setEnd(boolean e) {
		this.e = e;
	}

	@Override
	public void waitFor() {
		try {
			if (me != null) {
				me.join();
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	/** common part */
	protected Stack<Token> range(Type begin, Type end) {
		Stack<Token> sub = new Stack<Token>();

		if (!list.firstElement().getType().equals(begin)) {
			// 从 return null 改为 return sub
			// 只为以防万一
			// 2010-05-19
			return sub;
		} else {
			list.remove(0);
		}

		int count = 1;
		while (count > 0) {
			// 先算结束，避免 begin == end 时出错
			// 但也尽量避免这样的代码
			if (list.firstElement().getType().equals(end)) {
				count--;
			} else if (list.firstElement().getType().equals(begin)) {
				count++;
			}
			sub.add(list.remove(0));
		}
		sub.remove(sub.size() - 1);

		return sub;
	}

	/** 表达式 (小括号之间) */
	protected Stack<Token> nextExpression() {
		return range(OPERATOR_OPEN_PAREN, OPERATOR_CLOSE_PAREN);
	}

	/** 数组的下标 (中括号之间) */
	protected Stack<Token> nextIndex() {
		return range(SYMBOL_OPEN_BRACKET, SYMBOL_CLOSE_BRACKET);
	}

	/** 块 (大括号之间) */
	protected Stack<Token> nextBlock() {
		return range(SYMBOL_OPEN_BRACE, SYMBOL_CLOSE_BRACE);
	}

	/**
	 * 注释
	 * 
	 * 该方法已过时，因为它会对注释里的内容进行词法分析
	 * 这即影响效率，也不能正确处理注释中词法有误的代码
	 * 比如，只要在注释内部写一个"，则会将“关注释”解释为字符串
	 */
	@Deprecated
	protected Stack<Token> nextComment() {
		return range(OPEN_COMMENT, CLOSE_COMMENT);
	}

	protected void lockList() {
		// on
		if (list.firstElement().getType().equals(KEYWORD_ON)) {
			list.remove(0);
		} else {
			return;
		}

		// locking
		if (list.firstElement().getType().equals(KEYWORD_LOCKING)) {
			list.remove(0);
		} else {
			throw new SyntaxException("except locking");
		}

		// id list
		if (!list.firstElement().getType().equals(ID)) {
			throw new SyntaxException("except id list");
		}
		while (list.firstElement().getType().equals(ID)) {
			Stack<Token> var = new Stack<Token>();
			var.add(list.remove(0));
			// Array
			if (list.firstElement().getType().equals(SYMBOL_OPEN_BRACKET)) {
				while (!list.firstElement().getType()
						.equals(SYMBOL_CLOSE_BRACKET)) {
					var.add(list.remove(0));
				}
				// ']'
				var.add(list.remove(0));
			}
			// 添加锁
			addLocking(var);

			// 逗号
			if (list.firstElement().getType().equals(SYMBOL_COMMA)) {
				list.remove(0);
			}
		}
	}
}
