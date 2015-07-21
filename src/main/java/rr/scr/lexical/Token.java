package rr.scr.lexical;

import java.util.regex.Pattern;

public class Token implements Cloneable {
	public static enum Type {
		/** 量值 */
		VALUE_INTGER,
		VALUE_STRING,
		ID,

		/** 关键字 */
		KEYWORD_FUNCTION,
		KEYWORD_IF,
		KEYWORD_ELSIF,
		KEYWORD_ELSE,
		KEYWORD_FOR,
		KEYWORD_DO,
		KEYWORD_WHILE,
		KEYWORD_ON,
		KEYWORD_LOCKING,
		KEYWORD_RETURN,

		/** 符号 */
		SYMBOL_OPEN_BRACKET,        // 左中括号
		SYMBOL_CLOSE_BRACKET,       // 右中括号
		SYMBOL_OPEN_BRACE,          // 左大括号
		SYMBOL_CLOSE_BRACE,         // 右大括号
		SYMBOL_OPRIOD,              // 句号
		SYMBOL_COMMA,               // 逗号
		SYMBOL_COLON,               // 分号
		
		/** 运算符 */
		/* 算数运算 */
		OPERATOR_OPEN_PAREN,        // 左小括号
		OPERATOR_CLOSE_PAREN,       // 右小括号
		OPERATOR_INCREMENT,         // 自增
		OPERATOR_DECREMENT,         // 自减
		OPERATOR_PLUS,              // 加
		OPERATOR_MINUS,             // 减
		OPERATOR_MULTIPLY,          // 乘
		OPERATOR_DIVIDE,            // 除
		OPERATOR_MOD,               // 模
		OPERATOR_POSITIVE,          // 正号
		OPERATOR_NEGATIVE,          // 负号
		/* 关系运算 */
		OPERATOR_LESS,              // 小于
		OPERATOR_GREAT,             // 大于
		OPERATOR_EQUAL,             // 等于
		OPERATOR_NOT_EQUAL,         // 不等于
		OPERATOR_LESS_EQUAL,        // 不大于
		OPERATOR_GREAT_EQUAL,       // 不小于
		/* 逻辑运算 */
		OPERATOR_AND,               // 且
		OPERATOR_OR,                // 或
		OPERATOR_NOT,               // 非
		/* 赋值 */
		OPERATOR_ASSIGNMENT,        // 赋值
		OPERATOR_MUL_ASSIGNMENT,    // *=
		OPERATOR_DIV_ASSIGNMENT,    // /=
		OPERATOR_MOD_ASSIGNMENT,    // %=
		OPERATOR_ADD_ASSIGNMENT,    // +=
		OPERATOR_SUB_ASSIGNMENT,    // -=
		/* 间隔 */
		OPERATOR_MIN,               // 栈底 #
		COMMENT,                    // /* ... */
		@Deprecated
		OPEN_COMMENT,               // 注释 /*
		@Deprecated
		CLOSE_COMMENT,              // 注释 */
		OTHER,                      // 我也不确定，但大部分时候它用于匹配注释
	}
	private String value = null;
	private Pattern pattern = null;
	private Type type = null;

	public Token(Type type, String regexp) {
		this.type = type;
		pattern = Pattern.compile(regexp);
	}

	@Override
	public Token clone() {
		try {
			return (Token) super.clone();
		} catch (CloneNotSupportedException e) {
			return null;
		}
	}

	public Pattern getPattern() {
		return pattern;
	}

	public Type getType() {
		return type;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
}
