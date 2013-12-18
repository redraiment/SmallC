package rr.scr.syntax.stmt;

import static rr.scr.lexical.Token.Type.ID;
import static rr.scr.lexical.Token.Type.KEYWORD_FUNCTION;
import static rr.scr.lexical.Token.Type.SYMBOL_CLOSE_BRACKET;
import static rr.scr.lexical.Token.Type.SYMBOL_OPEN_BRACKET;

import java.util.Stack;

import rr.scr.exception.SyntaxException;
import rr.scr.lexical.Token;
import rr.scr.syntax.expr.Variable;
import rr.scr.vm.Machine;

/**
 * define-function-statement := function <ID> <block-statement>
 *
 * @author redraiment
 *
 */
public class FuncStmt extends AbstractStatement {
	public FuncStmt(Stack<Token> list) {
		super(list);

		Token t = null;

		t = list.remove(0);
		if (!t.getType().equals(KEYWORD_FUNCTION)) {
			throw new SyntaxException("not function statement");
		}

		// 函数名
		Stack<Token> name = new Stack<Token>();
		t = list.remove(0);
		if (!t.getType().equals(ID)) {
			throw new SyntaxException("expect function name");
		} else {
			name.add(t);
		}

		// 检查中括号，看看是否为关联数组
		// 这是一个好玩的特性，呵呵
		// 2010-05-19
		if (list.firstElement().getType().equals(SYMBOL_OPEN_BRACKET)) {
			// next* 方法会取出起点和终点
			name.add(list.firstElement());
			name.addAll(nextIndex());
			name.add(new Token(SYMBOL_CLOSE_BRACKET, "\\]"));
		}

		// 函数体
		Machine.getVM().setFunction(new Variable(name).getName(), nextBlock());
	}

	@Override
	public boolean execute() {
		// 函数只定义，不执行
		return false;
	}
}
