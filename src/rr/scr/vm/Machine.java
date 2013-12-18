package rr.scr.vm;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import rr.scr.exception.SyntaxException;
import rr.scr.lexical.Token;
import rr.scr.syntax.stmt.Block;
import rr.scr.util.SysCall;
import rr.scr.util.Value;

public final class Machine {
	private static Machine m = new Machine();

	public static Machine getVM() {
		return m;
	}

	private Map<String, Value> vars = null;
	private Map<String, Lock> locks = null;
	private Map<String, Stack<Token>> funcs = null;
	private SysCall sc = SysCall.getInstance();

	/**
	 * 全局的锁
	 * 在锁定变量时使用，避免出现死锁
	 */
	private Lock mlock = new ReentrantLock();

	private Machine() {
		vars = new HashMap<String, Value>();
		locks = new HashMap<String, Lock>();
		funcs = new HashMap<String, Stack<Token>>();
	}

	/**
	 * @param id 函数名
	 * @return 函数体的副本
	 */
	public Stack<Token> getFunction(String id) {
		if (funcs.get(id) == null) {
			setFunction(id, new Stack<Token>());
		}
		Stack<Token> body = new Stack<Token>();
		body.addAll(funcs.get(id));
		return body;
	}

	public Lock getLocks(String id) {
		// 避免为空
		getValue(id);
		return locks.get(id);
	}

	public Value getValue(String id) {
		if (vars.get(id) == null) {
			setValue(id, new Value());
		}
		return vars.get(id);
	}

	public void invoke(String id, Stack<Token> args) {
		if (funcs.containsKey(id)) {
			// 先检查自定义函数
			// 这样就能很方便地覆盖系统调用
			new Block(getFunction(id)).execute();
		} else if (sc.hasSysCall(id)) {
			// 再检查系统调用
			sc.call(id, args);
		} else {
			throw new SyntaxException("no such function");
		}
	}

	public void lock() {
		mlock.lock();
	}

	public void setFunction(String id, Stack<Token> body) {
		funcs.put(id, body);
	}

	public void setValue(String id, Value v) {
		if (vars.get(id) == null) {
			locks.put(id, new ReentrantLock());
		}
		vars.put(id, v);
	}

	public void unlock() {
		mlock.unlock();
	}
}
