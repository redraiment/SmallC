package rr.scr.syntax;

import java.util.Stack;

import rr.scr.lexical.Token;
import rr.scr.syntax.stmt.Block;

/**
 * statement := <define-function-statement>
 *            | <block>
 *            | <signal>
 * 
 * define-function-statement := function <ID> <block-statement>
 * 
 * block := <block-statement> [on locking <IDs>] ;
 *        | <block-statement> [on locking <IDs>] .
 * 
 * block-statement := { <statements> }
 * 
 * statements := <statement> [<statements>]
 *             | <EMPTY>
 * 
 * signal := <signal-statement> [on locking [<IDs>,] <ID>] ;
 *         | <signal-statement> [on locking [<IDs>,] <ID>] .
 * 
 * signal-statement := <if-statement>
 *                   | <for-statement>
 *                   | <while-statement>
 *                   | <do-statement>
 *                   | <return-statement>
 *                   | <expression>
 */
public class Yacc {
	private Stack<Token> list = null;

	public Yacc(Stack<Token> list) {
		this.list = list;
	}

	public void parse() {
		new Block(list).execute();
	}
}
