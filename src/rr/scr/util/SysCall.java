package rr.scr.util;

import static rr.scr.lexical.Token.Type.SYMBOL_COMMA;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import rr.scr.lexical.Token;

/**
 * 该类用于调用现有 Java 程序的静态方法
 * 这样，可以很轻松地利用现有程序来扩充语言的功能
 *
 * @author redraiment
 *
 */
public final class SysCall {
	private static SysCall sc = new SysCall();

	private Map<String, Method> method = new HashMap<String, Method>(); 

	private SysCall() {
		// Load System call in rr.src.lang package
		try {
			Class<?> c = Class.forName("rr.scr.lang.IOStream");
			for (Method m : c.getMethods()) {
				method.put(m.getName(), m);
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	public boolean hasSysCall(String id) {
		return method.containsKey(id);
	}

	public static SysCall getInstance() {
		return sc;
	}

	public void call(String name, Stack<Token> list) {
		Method m = method.get(name);
		if (m == null) {
			// no such method
			return;
		}

		try {
			m.invoke(null, new Object[] {split(list)});
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
	}

	protected List<Stack<Token>> split(Stack<Token> list) {
		List<Stack<Token>> args = new ArrayList<Stack<Token>>();
		while (!list.isEmpty()) {
			Stack<Token> a = new Stack<Token>();
			while (!list.isEmpty() && !list.firstElement()
					.getType().equals(SYMBOL_COMMA)) {
				a.push(list.remove(0));
			}
			if (!list.isEmpty()) {
				list.remove(0);
			}
			if (a.size() > 0) {
				args.add(a);
			}
		}

		return args;
	}
}
