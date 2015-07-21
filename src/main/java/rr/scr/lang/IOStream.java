package rr.scr.lang;

import java.util.List;
import java.util.Scanner;
import java.util.Stack;

import rr.scr.lexical.Token;
import rr.scr.syntax.expr.Expression;
import rr.scr.syntax.expr.Variable;
import rr.scr.util.Value;
import rr.scr.vm.Machine;

public class IOStream {
	public static void read(List<Stack<Token>> args) {
		Scanner cin = new Scanner(System.in);
		Machine m = Machine.getVM();
		for (int i = 0; i < args.size(); i++) {
			String id = new Variable(args.get(i)).getName();
			Value value = new Value(cin.next());
			m.setValue(id, value);
		}
	}

	public static void getLine(List<Stack<Token>> args) {
		Scanner cin = new Scanner(System.in);
		Machine m = Machine.getVM();
		for (int i = 0; i < args.size(); i++) {
			String id = new Variable(args.get(i)).getName();
			Value value = new Value(cin.nextLine());
			m.setValue(id, value);
		}
	}

	public static void print(List<Stack<Token>> args) {
		for (int i = 0; i < args.size(); i++) {
			if (i > 0) {
				System.out.print('\t');
			}
			System.out.print(new Expression(args.get(i)).value());
		}
	}

	public static void println(List<Stack<Token>> args) {
		print(args);
		System.out.println();
	}
}
