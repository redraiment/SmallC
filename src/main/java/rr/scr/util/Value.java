package rr.scr.util;

import java.io.Serializable;
import java.math.BigInteger;

/**
 * Weakly-typed variables.
 * 
 * @author redraiment
 * 
 */
public class Value implements Serializable, Comparable<Value>, Cloneable {
	public final static Value ZERO = new Value(0);
	public final static Value ONE = new Value(1);
	
	private static final long serialVersionUID = -762198471809932663L;

	private String data = null;

	public Value() {
		data = "";
	}

	public Value(Object data) {
		this.data = data.toString();
	}

	public void assign(Object data) {
		this.data = data == null ? null : data.toString();
	}

	public boolean isNull() {
		return data == null;
	}

	public boolean isInteger() {
		return data != null ? data.matches("^[-+]?\\d+$") : false;
	}

	public boolean isTrue() {
		return data != null && !data.equals("") && !data.equals("0");
	}

	// BigInteger Method
	public Value increment() {
		return add(ONE);
	}

	public Value decrement() {
		return subtract(ONE);
	}

	/**
	 * Add for integer & concat for String
	 * @param val
	 * @return
	 */
	public Value add(Value val) {
		Value result = null;

		if (this.isInteger() && val.isInteger()) {
			BigInteger l = new BigInteger(data);
			BigInteger r = new BigInteger(val.data);
			result = new Value(l.add(r));
		} else {
			result = concat(val);
		}

		return result;
	}

	public Value subtract(Value val) {
		BigInteger l = new BigInteger(toInteger().toString());
		BigInteger r = new BigInteger(val.toInteger().toString());
		return new Value(l.subtract(r));
	}

	public Value divide(Value val) {
		BigInteger l = new BigInteger(toInteger().toString());
		BigInteger r = new BigInteger(val.toInteger().toString());
		return new Value(l.divide(r));
	}

	public Value mod(Value val) {
		BigInteger l = new BigInteger(toInteger().toString());
		BigInteger r = new BigInteger(val.toInteger().toString());
		return new Value(l.mod(r));
	}

	public Value multiply(Value val) {
		BigInteger l = new BigInteger(toInteger().toString());
		BigInteger r = new BigInteger(val.toInteger().toString());
		return new Value(l.multiply(r));
	}

	public Value negate() {
		return new Value(new BigInteger(toInteger().toString()).negate());
	}

	public Value pow(int exponent) {
		return new Value(new BigInteger(toInteger().toString()).pow(exponent));
	}

	public Value remainder(Value val) {
		BigInteger l = new BigInteger(toInteger().toString());
		BigInteger r = new BigInteger(val.toInteger().toString());
		return new Value(l.remainder(r));
	}
	
	// Associative Arrays
	// public void put() {
	// 	;
	// }

	// String Method
	public char charAt(int index) {
		return data.charAt(index);
	}

	public Value concat(String str) {
		return new Value(data.concat(str));
	}

	public Value concat(Value str) {
		return new Value(data.concat(str.toString()));
	}

	public int length() {
		return data.length();
	}

	public boolean matches(String regex) {
		return data.matches(regex);
	}

	public Value repeat(int n) {
		StringBuilder item = new StringBuilder(data);
		StringBuilder result = new StringBuilder("");
		while (n > 0) {
			if (n % 2 == 1)
				result.append(item);
			if (n == 1)
				break;
			item.append(item);
			n = n >> 1;
		}

		return new Value(result);

	}

	// Logic Method
	public Value and(Value var) {
		return isTrue() && var.isTrue()? ONE: ZERO;
	}

	public Value or(Value var) {
		return isTrue() || var.isTrue()? ONE: ZERO;
	}

	public Value not() {
		return !isTrue()? ONE: ZERO;
	}

	public Value greatEquals(Value var) {
		return compareTo(var) >= 0? ONE: ZERO;
	}
	
	public Value great(Value var) {
		return compareTo(var) > 0? ONE: ZERO;
	}
	
	public Value lessEquals(Value var) {
		return compareTo(var) <= 0? ONE: ZERO;
	}

	public Value less(Value var) {
		return compareTo(var) < 0? ONE: ZERO;
	}

	public Value notEquals(Value var) {
		return compareTo(var) != 0? ONE: ZERO;
	}

	public Value equals(Value var) {
		return compareTo(var) == 0? ONE: ZERO;
	}

	// Override
	@Override
	public Value clone() {
		try {
			return (Value) super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public boolean equals(Object o) {
		return data == null ? o == null : data.equals(o.toString());
	}

	@Override
	public String toString() {
		return data;
	}
	
	public Value toInteger() {
		String v = null;
		if (isInteger()) {
			v = data;
		} else {
			v = isTrue()? "1": "0";
		}
		return new Value(v);
	}

	@Override
	public int compareTo(Value var) {
		if (var == null) {
			return -1;
		} else if (data == null) {
			return var.data == null? 0: -1;
		} else if (var.data == null) {
			return 1;
		} else if (isInteger() && var.isInteger()) {
			BigInteger l = new BigInteger(data);
			BigInteger r = new BigInteger(var.data);
			return l.compareTo(r);
		} else {
			return data.compareTo(var.data);
		}
	}
}
