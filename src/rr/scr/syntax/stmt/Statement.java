package rr.scr.syntax.stmt;

import java.util.Stack;

import rr.scr.lexical.Token;

/**
 * statement := <define-function-statement>
 *            | <block>
 *            | <signal>
 *
 * @author redraiment
 *
 */
public interface Statement {
	/** 添加共享变量锁 */
	public void addLocking(Stack<Token> var);

	/** 是否以句号结束 */
	public boolean isEnd();

	public void setEnd(boolean e);

	/** 执行 */
	public boolean execute();

	/** 等待该语句执行完毕 */
	public void waitFor();
}
