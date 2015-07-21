package rr.scr.syntax.expr;

import static rr.scr.lexical.Token.Type.ID;
import static rr.scr.lexical.Token.Type.OPERATOR_CLOSE_PAREN;
import static rr.scr.lexical.Token.Type.OPERATOR_OPEN_PAREN;
import static rr.scr.lexical.Token.Type.SYMBOL_CLOSE_BRACKET;
import static rr.scr.lexical.Token.Type.SYMBOL_OPEN_BRACKET;

import java.util.Stack;

import rr.scr.exception.SyntaxException;
import rr.scr.lexical.Token;
import rr.scr.util.Value;
import rr.scr.vm.Machine;

/**
 * factor := <string>
 *         | <digit>
 *         | <call-function-statement>
 *         | <variable>
 *         | ( <expression> )
 *
 * string := /"[^\"]*(\.[^\"]*)*"/
 *
 * digit := /\d+/
 *
 * call_function_statement := <id> ( <ids> )
 *
 * variable := <id>
 *        | <array>
 *
 * array := <id> [ <expression> ]
 * 
 * ids := [<ids>,] <id>
 *      | <EMPTY>
 *
 * id := /[a-zA-Z]\w+/
 * 
 * EMPTY :=
 * 
 * @author redraiment
 *
 */
public class Factor implements Item {
	private Value value = null;

	public Factor(Stack<Token> list) {
		if (list.empty()) {
			value = new Value("");
		} else if (list.peek().getType().equals(OPERATOR_CLOSE_PAREN)) {
			Stack<Token> sub = new Stack<Token>();

			list.pop();
			int p_count = 1;
			while (!list.empty() && p_count != 0) {
				if (list.peek().getType().equals(OPERATOR_CLOSE_PAREN)) {
					p_count++;
				} else if (list.peek().getType().equals(OPERATOR_OPEN_PAREN)) {
					p_count--;
				}
				sub.add(0, list.pop());
			}
			if (p_count != 0) {
				throw new SyntaxException("paren don't match");
			} else {
				// 去除最后一个多余的开括号
				sub.remove(0);
			}

			// 这个算法处理嵌套括号时有误
			// while (!list.empty() && !list.peek().getType()
			// 	.equals(OPERATOR_OPEN_PAREN)) {
			// 	sub.add(0, list.pop());
			// }
			// list.pop();

			if (!list.empty() && (list.peek().getType().equals(ID)
					|| list.peek().getType().equals(SYMBOL_CLOSE_BRACKET))) {
				// 函数名
				Stack<Token> name = new Stack<Token>();

				// 检查中括号，看看是否为关联数组
				// 用关联数组来做函数明，这是一个好玩的特性，呵呵
				// 2010-05-19
				if (list.peek().getType().equals(SYMBOL_CLOSE_BRACKET)) {
					name.add(0, list.pop());
					int b_count = 1;
					while (!list.isEmpty() && b_count > 0) {
						if (list.peek().getType().equals(SYMBOL_CLOSE_BRACKET)) {
							b_count++;
						} else if (list.peek().getType().equals(SYMBOL_OPEN_BRACKET)) {
							b_count--;
						}
						name.add(0, list.pop());
					}
				}

				if (list.isEmpty() || !list.peek().getType().equals(ID)) {
					throw new SyntaxException("expect an ID for function name");
				} else {
					name.add(0, list.pop());
				}

				// function call
				String id = new Variable(name).getName();
				// 目前函数还不支持传值和返回值
				// 暂时用全局变量代替
				/* 
				 * 重新实现了调度方法（替换下段被注释代码）
				 * 默认先调用自定义函数（方便覆盖系统调用）
				 */
				Machine.getVM().invoke(id, sub);

				// if (sub.size() > 0) {
				// // 如果有参数，就判断为系统调用
				// Machine.getVM().invoke(id, sub);
				// } else {
				// // 不然就看做自定义函数调用
				// new Block(Machine.getVM().getFunction(id)).execute();
				// }
				value = new Value();
			} else {
				// ( expression )
				value = new Expression(sub).value();
			}
		} else if (list.peek().getType().equals(ID)
			|| list.peek().getType().equals(SYMBOL_CLOSE_BRACKET)) {
			// Variable
			value = new Variable(list).value();
		} else {
			String v = list.pop().getValue();
			if (!v.matches("\\d+")) {
				v = v.substring(1, v.length() - 1);
			}
			value = new Value(v);
		}
	}
	
	@Override
	public Value value() {
		return value;
	}
}
