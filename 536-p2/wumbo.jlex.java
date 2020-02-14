import java_cup.runtime.*; // defines the Symbol class
// The generated scanner will return a Symbol for each token that it finds.
// A Symbol contains an Object field named value; that field will be of type
// TokenVal, defined below.
//
// A TokenVal object contains the line number on which the token occurs as
// well as the number of the character on that line that starts the token.
// Some tokens (literals and IDs) also include the value of the token.
class TokenVal {
  // fields
    int linenum;
    int charnum;
  // constructor
    TokenVal(int line, int ch) {
        linenum = line;
        charnum = ch;
    }
}
class IntLitTokenVal extends TokenVal {
  // new field: the value of the integer literal
    int intVal;
  // constructor
    IntLitTokenVal(int line, int ch, int val) {
        super(line, ch);
        intVal = val;
    }
}
class IdTokenVal extends TokenVal {
  // new field: the value of the identifier
    String idVal;
  // constructor
    IdTokenVal(int line, int ch, String val) {
        super(line, ch);
    idVal = val;
    }
}
class StrLitTokenVal extends TokenVal {
  // new field: the value of the string literal
    String strVal;
  // constructor
    StrLitTokenVal(int line, int ch, String val) {
        super(line, ch);
        strVal = val;
    }
}
// The following class is used to keep track of the character number at which
// the current token starts on its line.
class CharNum {
    static int num=1;
}


class Yylex implements java_cup.runtime.Scanner {
	private final int YY_BUFFER_SIZE = 512;
	private final int YY_F = -1;
	private final int YY_NO_STATE = -1;
	private final int YY_NOT_ACCEPT = 0;
	private final int YY_START = 1;
	private final int YY_END = 2;
	private final int YY_NO_ANCHOR = 4;
	private final int YY_BOL = 128;
	private final int YY_EOF = 129;
	private java.io.BufferedReader yy_reader;
	private int yy_buffer_index;
	private int yy_buffer_read;
	private int yy_buffer_start;
	private int yy_buffer_end;
	private char yy_buffer[];
	private int yyline;
	private boolean yy_at_bol;
	private int yy_lexical_state;

	Yylex (java.io.Reader reader) {
		this ();
		if (null == reader) {
			throw (new Error("Error: Bad input stream initializer."));
		}
		yy_reader = new java.io.BufferedReader(reader);
	}

	Yylex (java.io.InputStream instream) {
		this ();
		if (null == instream) {
			throw (new Error("Error: Bad input stream initializer."));
		}
		yy_reader = new java.io.BufferedReader(new java.io.InputStreamReader(instream));
	}

	private Yylex () {
		yy_buffer = new char[YY_BUFFER_SIZE];
		yy_buffer_read = 0;
		yy_buffer_index = 0;
		yy_buffer_start = 0;
		yy_buffer_end = 0;
		yyline = 0;
		yy_at_bol = true;
		yy_lexical_state = YYINITIAL;
	}

	private boolean yy_eof_done = false;
	private final int YYINITIAL = 0;
	private final int yy_state_dtrans[] = {
		0
	};
	private void yybegin (int state) {
		yy_lexical_state = state;
	}
	private int yy_advance ()
		throws java.io.IOException {
		int next_read;
		int i;
		int j;

		if (yy_buffer_index < yy_buffer_read) {
			return yy_buffer[yy_buffer_index++];
		}

		if (0 != yy_buffer_start) {
			i = yy_buffer_start;
			j = 0;
			while (i < yy_buffer_read) {
				yy_buffer[j] = yy_buffer[i];
				++i;
				++j;
			}
			yy_buffer_end = yy_buffer_end - yy_buffer_start;
			yy_buffer_start = 0;
			yy_buffer_read = j;
			yy_buffer_index = j;
			next_read = yy_reader.read(yy_buffer,
					yy_buffer_read,
					yy_buffer.length - yy_buffer_read);
			if (-1 == next_read) {
				return YY_EOF;
			}
			yy_buffer_read = yy_buffer_read + next_read;
		}

		while (yy_buffer_index >= yy_buffer_read) {
			if (yy_buffer_index >= yy_buffer.length) {
				yy_buffer = yy_double(yy_buffer);
			}
			next_read = yy_reader.read(yy_buffer,
					yy_buffer_read,
					yy_buffer.length - yy_buffer_read);
			if (-1 == next_read) {
				return YY_EOF;
			}
			yy_buffer_read = yy_buffer_read + next_read;
		}
		return yy_buffer[yy_buffer_index++];
	}
	private void yy_move_end () {
		if (yy_buffer_end > yy_buffer_start &&
		    '\n' == yy_buffer[yy_buffer_end-1])
			yy_buffer_end--;
		if (yy_buffer_end > yy_buffer_start &&
		    '\r' == yy_buffer[yy_buffer_end-1])
			yy_buffer_end--;
	}
	private boolean yy_last_was_cr=false;
	private void yy_mark_start () {
		int i;
		for (i = yy_buffer_start; i < yy_buffer_index; ++i) {
			if ('\n' == yy_buffer[i] && !yy_last_was_cr) {
				++yyline;
			}
			if ('\r' == yy_buffer[i]) {
				++yyline;
				yy_last_was_cr=true;
			} else yy_last_was_cr=false;
		}
		yy_buffer_start = yy_buffer_index;
	}
	private void yy_mark_end () {
		yy_buffer_end = yy_buffer_index;
	}
	private void yy_to_mark () {
		yy_buffer_index = yy_buffer_end;
		yy_at_bol = (yy_buffer_end > yy_buffer_start) &&
		            ('\r' == yy_buffer[yy_buffer_end-1] ||
		             '\n' == yy_buffer[yy_buffer_end-1] ||
		             2028/*LS*/ == yy_buffer[yy_buffer_end-1] ||
		             2029/*PS*/ == yy_buffer[yy_buffer_end-1]);
	}
	private java.lang.String yytext () {
		return (new java.lang.String(yy_buffer,
			yy_buffer_start,
			yy_buffer_end - yy_buffer_start));
	}
	private int yylength () {
		return yy_buffer_end - yy_buffer_start;
	}
	private char[] yy_double (char buf[]) {
		int i;
		char newbuf[];
		newbuf = new char[2*buf.length];
		for (i = 0; i < buf.length; ++i) {
			newbuf[i] = buf[i];
		}
		return newbuf;
	}
	private final int YY_E_INTERNAL = 0;
	private final int YY_E_MATCH = 1;
	private java.lang.String yy_error_string[] = {
		"Error: Internal error.\n",
		"Error: Unmatched input.\n"
	};
	private void yy_error (int code,boolean fatal) {
		java.lang.System.out.print(yy_error_string[code]);
		java.lang.System.out.flush();
		if (fatal) {
			throw new Error("Fatal Error.\n");
		}
	}
	private int[][] unpackFromString(int size1, int size2, String st) {
		int colonIndex = -1;
		String lengthString;
		int sequenceLength = 0;
		int sequenceInteger = 0;

		int commaIndex;
		String workString;

		int res[][] = new int[size1][size2];
		for (int i= 0; i < size1; i++) {
			for (int j= 0; j < size2; j++) {
				if (sequenceLength != 0) {
					res[i][j] = sequenceInteger;
					sequenceLength--;
					continue;
				}
				commaIndex = st.indexOf(',');
				workString = (commaIndex==-1) ? st :
					st.substring(0, commaIndex);
				st = st.substring(commaIndex+1);
				colonIndex = workString.indexOf(':');
				if (colonIndex == -1) {
					res[i][j]=Integer.parseInt(workString);
					continue;
				}
				lengthString =
					workString.substring(colonIndex+1);
				sequenceLength=Integer.parseInt(lengthString);
				workString=workString.substring(0,colonIndex);
				sequenceInteger=Integer.parseInt(workString);
				res[i][j] = sequenceInteger;
				sequenceLength--;
			}
		}
		return res;
	}
	private int yy_acpt[] = {
		/* 0 */ YY_NOT_ACCEPT,
		/* 1 */ YY_NO_ANCHOR,
		/* 2 */ YY_NO_ANCHOR,
		/* 3 */ YY_NO_ANCHOR,
		/* 4 */ YY_NO_ANCHOR,
		/* 5 */ YY_NO_ANCHOR,
		/* 6 */ YY_NO_ANCHOR,
		/* 7 */ YY_NO_ANCHOR,
		/* 8 */ YY_NO_ANCHOR,
		/* 9 */ YY_NO_ANCHOR,
		/* 10 */ YY_NO_ANCHOR,
		/* 11 */ YY_NO_ANCHOR,
		/* 12 */ YY_NO_ANCHOR,
		/* 13 */ YY_NO_ANCHOR,
		/* 14 */ YY_NO_ANCHOR,
		/* 15 */ YY_NO_ANCHOR,
		/* 16 */ YY_NO_ANCHOR,
		/* 17 */ YY_NO_ANCHOR,
		/* 18 */ YY_NO_ANCHOR,
		/* 19 */ YY_NO_ANCHOR,
		/* 20 */ YY_NO_ANCHOR,
		/* 21 */ YY_NO_ANCHOR,
		/* 22 */ YY_NO_ANCHOR,
		/* 23 */ YY_NO_ANCHOR,
		/* 24 */ YY_NO_ANCHOR,
		/* 25 */ YY_NO_ANCHOR,
		/* 26 */ YY_NO_ANCHOR,
		/* 27 */ YY_NO_ANCHOR,
		/* 28 */ YY_NO_ANCHOR,
		/* 29 */ YY_NO_ANCHOR,
		/* 30 */ YY_NO_ANCHOR,
		/* 31 */ YY_NO_ANCHOR,
		/* 32 */ YY_NO_ANCHOR,
		/* 33 */ YY_NO_ANCHOR,
		/* 34 */ YY_NO_ANCHOR,
		/* 35 */ YY_NO_ANCHOR,
		/* 36 */ YY_NO_ANCHOR,
		/* 37 */ YY_NO_ANCHOR,
		/* 38 */ YY_NO_ANCHOR,
		/* 39 */ YY_NO_ANCHOR,
		/* 40 */ YY_NO_ANCHOR,
		/* 41 */ YY_NO_ANCHOR,
		/* 42 */ YY_NO_ANCHOR,
		/* 43 */ YY_NO_ANCHOR,
		/* 44 */ YY_NO_ANCHOR,
		/* 45 */ YY_NO_ANCHOR,
		/* 46 */ YY_NO_ANCHOR,
		/* 47 */ YY_NO_ANCHOR,
		/* 48 */ YY_NO_ANCHOR,
		/* 49 */ YY_NO_ANCHOR,
		/* 50 */ YY_NO_ANCHOR,
		/* 51 */ YY_NO_ANCHOR,
		/* 52 */ YY_NO_ANCHOR,
		/* 53 */ YY_NOT_ACCEPT,
		/* 54 */ YY_NO_ANCHOR,
		/* 55 */ YY_NO_ANCHOR,
		/* 56 */ YY_NO_ANCHOR,
		/* 57 */ YY_NO_ANCHOR,
		/* 58 */ YY_NO_ANCHOR,
		/* 59 */ YY_NO_ANCHOR,
		/* 60 */ YY_NO_ANCHOR,
		/* 61 */ YY_NOT_ACCEPT,
		/* 62 */ YY_NO_ANCHOR,
		/* 63 */ YY_NO_ANCHOR,
		/* 64 */ YY_NOT_ACCEPT,
		/* 65 */ YY_NO_ANCHOR,
		/* 66 */ YY_NO_ANCHOR,
		/* 67 */ YY_NOT_ACCEPT,
		/* 68 */ YY_NO_ANCHOR,
		/* 69 */ YY_NOT_ACCEPT,
		/* 70 */ YY_NO_ANCHOR,
		/* 71 */ YY_NOT_ACCEPT,
		/* 72 */ YY_NO_ANCHOR,
		/* 73 */ YY_NOT_ACCEPT,
		/* 74 */ YY_NO_ANCHOR,
		/* 75 */ YY_NOT_ACCEPT,
		/* 76 */ YY_NO_ANCHOR,
		/* 77 */ YY_NO_ANCHOR,
		/* 78 */ YY_NO_ANCHOR,
		/* 79 */ YY_NO_ANCHOR,
		/* 80 */ YY_NO_ANCHOR,
		/* 81 */ YY_NO_ANCHOR,
		/* 82 */ YY_NO_ANCHOR,
		/* 83 */ YY_NO_ANCHOR,
		/* 84 */ YY_NO_ANCHOR,
		/* 85 */ YY_NO_ANCHOR,
		/* 86 */ YY_NO_ANCHOR,
		/* 87 */ YY_NO_ANCHOR,
		/* 88 */ YY_NO_ANCHOR,
		/* 89 */ YY_NO_ANCHOR,
		/* 90 */ YY_NO_ANCHOR,
		/* 91 */ YY_NO_ANCHOR,
		/* 92 */ YY_NO_ANCHOR,
		/* 93 */ YY_NO_ANCHOR,
		/* 94 */ YY_NO_ANCHOR,
		/* 95 */ YY_NO_ANCHOR,
		/* 96 */ YY_NO_ANCHOR,
		/* 97 */ YY_NO_ANCHOR,
		/* 98 */ YY_NO_ANCHOR,
		/* 99 */ YY_NO_ANCHOR,
		/* 100 */ YY_NO_ANCHOR,
		/* 101 */ YY_NO_ANCHOR,
		/* 102 */ YY_NO_ANCHOR,
		/* 103 */ YY_NO_ANCHOR,
		/* 104 */ YY_NO_ANCHOR,
		/* 105 */ YY_NO_ANCHOR,
		/* 106 */ YY_NO_ANCHOR
	};
	private int yy_cmap[] = unpackFromString(1,130,
"43:9,46,42,43:2,41,43:18,45,31,38,44,43:2,32,40,20,21,29,27,23,28,24,30,37:" +
"10,43,22,25,34,26,40,43,35:26,43,39,43:2,36,43,13,1,15,8,11,12,35,17,4,35:2" +
",3,35,5,2,35:2,9,14,6,10,7,16,35:3,18,33,19,43:2,0:2")[0];

	private int yy_rmap[] = unpackFromString(1,107,
"0,1,2,1:7,3,4,5,6,1,7,8,9,10,11,12,1,13:2,14,1:10,15:2,13,14:2,16,17,1,14:5" +
",18,14:4,19,20,21,1:2,18,1,17,22,23,1,24,25,26,15,27,28,29,30,31,32,33,17,3" +
"4,35,36,37,38,39,40,41,14,42,43,44,45,46,47,48,49,50,51,52,53,54,14,55,56,5" +
"7,58,59,60,61,62")[0];

	private int yy_nxt[][] = unpackFromString(63,47,
"1,2,98:2,54,98,100,101,98,102,98,103,104,98,105,81,106,98,3,4,5,6,7,8,9,10," +
"11,12,13,14,15,16,17,55,18,98:2,19,20,63:2,-1,21,63,66,22,23,-1:48,98,83,98" +
":15,-1:17,98,84:2,-1:34,25,-1:8,26,-1:38,27,-1:7,28,-1:39,29,-1:47,30,-1:48" +
",53,-1:50,31,-1:44,32,-1:48,34,-1:49,19,-1:10,20:37,35,61,20:2,36,20:4,-1:4" +
"5,37:2,-1,98:17,-1:17,98,84:2,-1:10,67:38,69,67:7,-1,40:37,48,40:2,71,59,40" +
":4,-1,75:37,60,75:2,-1:2,75:4,-1,58:37,48,58:2,-1:2,58:4,-1:30,64,-1:17,98:" +
"4,62,98:6,24,98:5,-1:17,98,84:2,-1:42,33,-1:14,40:4,82:2,40:31,82:3,40,41,4" +
"0:4,-1,98:5,38,98:11,-1:17,98,84:2,-1:10,64:40,-1,42,64:4,-1,98:4,39,98:12," +
"-1:17,98,84:2,-1:53,64,-1:3,98:2,43,98:14,-1:17,98,84:2,-1:10,40:4,-1:2,40:" +
"31,-1:3,40,41,40:4,-1,98:10,44,98:6,-1:17,98,84:2,-1:10,71:37,-1,71:3,59,71" +
":4,-1,98:7,45,98:9,-1:17,98,84:2,-1:14,82:2,-1:31,82:3,-1:7,98:10,46,98:6,-" +
"1:17,98,84:2,-1:10,98:5,47,98:11,-1:17,98,84:2,-1:10,98:10,49,98:6,-1:17,98" +
",84:2,-1:10,98:10,50,98:6,-1:17,98,84:2,-1:10,98:4,51,98:12,-1:17,98,84:2,-" +
"1:10,98:5,52,98:11,-1:17,98,84:2,-1:10,98,91,98,65,98:13,-1:17,98,84:2,-1:1" +
"0,82:37,56,73,82:2,57,82:4,-1,98,68,98:15,-1:17,98,84:2,-1:10,98:9,70,98:7," +
"-1:17,98,84:2,-1:10,98:3,72,98:13,-1:17,98,84:2,-1:10,98:5,93,98:11,-1:17,9" +
"8,84:2,-1:10,98:13,74,98:3,-1:17,98,84:2,-1:10,98:2,94,98:14,-1:17,98,84:2," +
"-1:10,98:8,99,98:8,-1:17,98,84:2,-1:10,98:9,76,98:7,-1:17,98,84:2,-1:10,98:" +
"3,95,98:13,-1:17,98,84:2,-1:10,98:9,96,98:7,-1:17,98,84:2,-1:10,98:13,77,98" +
":3,-1:17,98,84:2,-1:10,98:2,78,98:14,-1:17,98,84:2,-1:10,98:8,79,98:8,-1:17" +
",98,84:2,-1:10,98:14,80,98:2,-1:17,98,84:2,-1:10,98:9,97,98:7,-1:17,98,84:2" +
",-1:10,98:8,85,98:8,-1:17,98,84:2,-1:10,98,86,98:15,-1:17,98,84:2,-1:10,98:" +
"10,87,98:6,-1:17,98,84:2,-1:10,98:2,88,98:14,-1:17,98,84:2,-1:10,98:12,89,9" +
"8:4,-1:17,98,84:2,-1:10,98:5,90,98:11,-1:17,98,84:2,-1:10,98:16,92,-1:17,98" +
",84:2,-1:9");

	public java_cup.runtime.Symbol next_token ()
		throws java.io.IOException {
		int yy_lookahead;
		int yy_anchor = YY_NO_ANCHOR;
		int yy_state = yy_state_dtrans[yy_lexical_state];
		int yy_next_state = YY_NO_STATE;
		int yy_last_accept_state = YY_NO_STATE;
		boolean yy_initial = true;
		int yy_this_accept;

		yy_mark_start();
		yy_this_accept = yy_acpt[yy_state];
		if (YY_NOT_ACCEPT != yy_this_accept) {
			yy_last_accept_state = yy_state;
			yy_mark_end();
		}
		while (true) {
			if (yy_initial && yy_at_bol) yy_lookahead = YY_BOL;
			else yy_lookahead = yy_advance();
			yy_next_state = YY_F;
			yy_next_state = yy_nxt[yy_rmap[yy_state]][yy_cmap[yy_lookahead]];
			if (YY_EOF == yy_lookahead && true == yy_initial) {

return new Symbol(sym.EOF);
			}
			if (YY_F != yy_next_state) {
				yy_state = yy_next_state;
				yy_initial = false;
				yy_this_accept = yy_acpt[yy_state];
				if (YY_NOT_ACCEPT != yy_this_accept) {
					yy_last_accept_state = yy_state;
					yy_mark_end();
				}
			}
			else {
				if (YY_NO_STATE == yy_last_accept_state) {
					throw (new Error("Lexical Error: Unmatched Input."));
				}
				else {
					yy_anchor = yy_acpt[yy_last_accept_state];
					if (0 != (YY_END & yy_anchor)) {
						yy_move_end();
					}
					yy_to_mark();
					switch (yy_last_accept_state) {
					case 1:
						
					case -2:
						break;
					case 2:
						{
										Symbol s = new Symbol(sym.ID, new IdTokenVal(yyline+1, CharNum.num,yytext()));
										CharNum.num += yytext().length();
										return s;
									}
					case -3:
						break;
					case 3:
						{
			Symbol s = new Symbol(sym.LCURLY, new IdTokenVal(yyline+1, CharNum.num, "{"));
			CharNum.num += yytext().length();
			return s;
		  }
					case -4:
						break;
					case 4:
						{
			Symbol s = new Symbol(sym.RCURLY, new IdTokenVal(yyline+1, CharNum.num, "}"));
			CharNum.num += yytext().length();
			return s;
		  }
					case -5:
						break;
					case 5:
						{
			Symbol s = new Symbol(sym.LPAREN, new IdTokenVal(yyline+1, CharNum.num, "("));
			CharNum.num += yytext().length();
			return s;
		  }
					case -6:
						break;
					case 6:
						{
			Symbol s = new Symbol(sym.RPAREN, new IdTokenVal(yyline+1, CharNum.num, ")"));
			CharNum.num += yytext().length();
			return s;
		  }
					case -7:
						break;
					case 7:
						{
			Symbol s = new Symbol(sym.SEMICOLON, new IdTokenVal(yyline+1, CharNum.num, ";"));
			CharNum.num += yytext().length();
			return s;
		  }
					case -8:
						break;
					case 8:
						{
			Symbol s = new Symbol(sym.COMMA, new IdTokenVal(yyline+1, CharNum.num, ","));
			CharNum.num += yytext().length();
			return s;
		  }
					case -9:
						break;
					case 9:
						{
			Symbol s = new Symbol(sym.DOT, new IdTokenVal(yyline+1, CharNum.num, "."));
			CharNum.num += yytext().length();
			return s;
		  }
					case -10:
						break;
					case 10:
						{
			Symbol s = new Symbol(sym.LESS, new IdTokenVal(yyline+1, CharNum.num, "<"));
			CharNum.num += yytext().length();
			return s;
		  }
					case -11:
						break;
					case 11:
						{
			Symbol s = new Symbol(sym.GREATER, new IdTokenVal(yyline+1, CharNum.num, ">"));
			CharNum.num += yytext().length();
			return s;
		  }
					case -12:
						break;
					case 12:
						{
			Symbol s = new Symbol(sym.PLUS, new IdTokenVal(yyline+1, CharNum.num, "+"));
			CharNum.num += yytext().length();
			return s;
		  }
					case -13:
						break;
					case 13:
						{
			Symbol s = new Symbol(sym.MINUS, new IdTokenVal(yyline+1, CharNum.num, "-"));
			CharNum.num += yytext().length();
			return s;
		  }
					case -14:
						break;
					case 14:
						{
			Symbol s = new Symbol(sym.TIMES, new IdTokenVal(yyline+1, CharNum.num, "*"));
			CharNum.num += yytext().length();
			return s;
		  }
					case -15:
						break;
					case 15:
						{
			Symbol s = new Symbol(sym.DIVIDE, new IdTokenVal(yyline+1, CharNum.num, "/"));
			CharNum.num += yytext().length();
			return s;
		  }
					case -16:
						break;
					case 16:
						{
			Symbol s = new Symbol(sym.NOT, new IdTokenVal(yyline+1, CharNum.num, "!"));
			CharNum.num += yytext().length();
			return s;
		  }
					case -17:
						break;
					case 17:
						{ ErrMsg.fatal(yyline+1, CharNum.num,
                         "ignoring illegal character: " + yytext());
            CharNum.num++;
          }
					case -18:
						break;
					case 18:
						{
			Symbol s = new Symbol(sym.ASSIGN, new IdTokenVal(yyline+1, CharNum.num, "="));
			CharNum.num += yytext().length();
			return s;
		  }
					case -19:
						break;
					case 19:
						{ // NOTE: the following computation of the integer value does NOT
            //       check for overflow.  This must be modified.
            int val;
            try {
            	val = Integer.parseInt(yytext());
            } catch (NumberFormatException e) {
                ErrMsg.warn(yyline+1, CharNum.num, "integer literal too large; using max value");
            	val = Integer.MAX_VALUE;
            }
            Symbol s = new Symbol(sym.INTLITERAL,
                             new IntLitTokenVal(yyline+1, CharNum.num, val));
            CharNum.num += yytext().length();
            return s;
          }
					case -20:
						break;
					case 20:
						{
												//This is for catching the (eof)
												ErrMsg.warn(yyline+1, CharNum.num, "unterminated string literal ignored");
												CharNum.num = 1;
											}
					case -21:
						break;
					case 21:
						{ CharNum.num = 1; }
					case -22:
						break;
					case 22:
						{
			CharNum.num += 1;	
		  }
					case -23:
						break;
					case 23:
						{
			 CharNum.num += 1;
			}
					case -24:
						break;
					case 24:
						{
			Symbol s = new Symbol(sym.IF, new IdTokenVal(yyline+1, CharNum.num, "if"));
			CharNum.num += yytext().length();
			return s;
		  }
					case -25:
						break;
					case 25:
						{
			Symbol s = new Symbol(sym.WRITE, new IdTokenVal(yyline+1, CharNum.num, "<<"));
			CharNum.num += yytext().length();
			return s;
		  }
					case -26:
						break;
					case 26:
						{
			Symbol s = new Symbol(sym.LESSEQ, new IdTokenVal(yyline+1, CharNum.num, "<="));
			CharNum.num += yytext().length();
			return s;
		  }
					case -27:
						break;
					case 27:
						{
			Symbol s = new Symbol(sym.READ, new IdTokenVal(yyline+1, CharNum.num, ">>"));
			CharNum.num += yytext().length();
			return s;
		  }
					case -28:
						break;
					case 28:
						{
			Symbol s = new Symbol(sym.GREATEREQ, new IdTokenVal(yyline+1, CharNum.num, ">="));
			CharNum.num += yytext().length();
			return s;
		  }
					case -29:
						break;
					case 29:
						{
			Symbol s = new Symbol(sym.PLUSPLUS, new IdTokenVal(yyline+1, CharNum.num, "++"));
			CharNum.num += yytext().length();
			return s;
		  }
					case -30:
						break;
					case 30:
						{
			Symbol s = new Symbol(sym.MINUSMINUS, new IdTokenVal(yyline+1, CharNum.num, "--"));
			CharNum.num += yytext().length();
			return s;
		  }
					case -31:
						break;
					case 31:
						{
			Symbol s = new Symbol(sym.NOTEQUALS, new IdTokenVal(yyline+1, CharNum.num, "!="));
			CharNum.num += yytext().length();
			return s;
		  }
					case -32:
						break;
					case 32:
						{
			Symbol s = new Symbol(sym.AND, new IdTokenVal(yyline+1, CharNum.num, "&&"));
			CharNum.num += yytext().length();
			return s;
		  }
					case -33:
						break;
					case 33:
						{
			Symbol s = new Symbol(sym.OR, new IdTokenVal(yyline+1, CharNum.num, "||"));
			CharNum.num += yytext().length();
			return s;
		  }
					case -34:
						break;
					case 34:
						{
			Symbol s = new Symbol(sym.EQUALS, new IdTokenVal(yyline+1, CharNum.num, "=="));
			CharNum.num += yytext().length();
			return s;
		  }
					case -35:
						break;
					case 35:
						{
													Symbol s = new Symbol(sym.STRINGLITERAL, new StrLitTokenVal(yyline+1, CharNum.num, yytext()));
													CharNum.num += yytext().length();
													return s;
												}
					case -36:
						break;
					case 36:
						{
												ErrMsg.warn(yyline+1, CharNum.num, "unterminated string literal ignored");
												CharNum.num = 1;
											}
					case -37:
						break;
					case 37:
						{ CharNum.num += yytext().length(); }
					case -38:
						break;
					case 38:
						{
			Symbol s = new Symbol(sym.INT, new IdTokenVal(yyline+1, CharNum.num, "int"));
			CharNum.num += yytext().length();
			return s;
		  }
					case -39:
						break;
					case 39:
						{
			Symbol s = new Symbol(sym.CIN, new IdTokenVal(yyline+1, CharNum.num, "cin"));
			CharNum.num += yytext().length();
			return s;
		  }
					case -40:
						break;
					case 40:
						{
												//This is for catching the (eof)
												ErrMsg.fatal(yyline+1, CharNum.num,"unterminated string literal with bad escaped character ignored");
												CharNum.num = 1;
										}
					case -41:
						break;
					case 41:
						{
												   ErrMsg.fatal(yyline+1, CharNum.num,"unterminated string literal with bad escaped character ignored");
												   CharNum.num = 1;
											      }
					case -42:
						break;
					case 42:
						{
												CharNum.num =1;
											 }
					case -43:
						break;
					case 43:
						{
			Symbol s = new Symbol(sym.BOOL, new IdTokenVal(yyline+1, CharNum.num, "bool"));
			CharNum.num += yytext().length();
			return s;
		  }
					case -44:
						break;
					case 44:
						{
			Symbol s = new Symbol(sym.TRUE, new IdTokenVal(yyline+1, CharNum.num, "true"));
			CharNum.num += yytext().length();
			return s;
		  }
					case -45:
						break;
					case 45:
						{
			Symbol s = new Symbol(sym.VOID, new IdTokenVal(yyline+1, CharNum.num, "void"));
			CharNum.num += yytext().length();
			return s;
		  }
					case -46:
						break;
					case 46:
						{
			Symbol s = new Symbol(sym.ELSE, new IdTokenVal(yyline+1, CharNum.num, "else"));
			CharNum.num += yytext().length();
			return s;
		  }
					case -47:
						break;
					case 47:
						{
			Symbol s = new Symbol(sym.COUT, new IdTokenVal(yyline+1, CharNum.num, "cout"));
			CharNum.num += yytext().length();
			return s;
		  }
					case -48:
						break;
					case 48:
						{
												ErrMsg.warn(yyline+1, CharNum.num, "string literal with bad escaped character ignored");
												CharNum.num += yytext().length();
											 }
					case -49:
						break;
					case 49:
						{
			Symbol s = new Symbol(sym.FALSE, new IdTokenVal(yyline+1, CharNum.num, "false"));
			CharNum.num += yytext().length();
			return s;
		  }
					case -50:
						break;
					case 50:
						{
			Symbol s = new Symbol(sym.WHILE, new IdTokenVal(yyline+1, CharNum.num, "while"));
			CharNum.num += yytext().length();
			return s;
		  }
					case -51:
						break;
					case 51:
						{
			Symbol s = new Symbol(sym.RETURN, new IdTokenVal(yyline+1, CharNum.num, "return"));
			CharNum.num += yytext().length();
			return s;
		  }
					case -52:
						break;
					case 52:
						{
			Symbol s = new Symbol(sym.STRUCT, new IdTokenVal(yyline+1, CharNum.num, "struct"));
			CharNum.num += yytext().length();
			return s;
		  }
					case -53:
						break;
					case 54:
						{
										Symbol s = new Symbol(sym.ID, new IdTokenVal(yyline+1, CharNum.num,yytext()));
										CharNum.num += yytext().length();
										return s;
									}
					case -54:
						break;
					case 55:
						{ ErrMsg.fatal(yyline+1, CharNum.num,
                         "ignoring illegal character: " + yytext());
            CharNum.num++;
          }
					case -55:
						break;
					case 56:
						{
													Symbol s = new Symbol(sym.STRINGLITERAL, new StrLitTokenVal(yyline+1, CharNum.num, yytext()));
													CharNum.num += yytext().length();
													return s;
												}
					case -56:
						break;
					case 57:
						{
												ErrMsg.warn(yyline+1, CharNum.num, "unterminated string literal ignored");
												CharNum.num = 1;
											}
					case -57:
						break;
					case 58:
						{
												//This is for catching the (eof)
												ErrMsg.fatal(yyline+1, CharNum.num,"unterminated string literal with bad escaped character ignored");
												CharNum.num = 1;
										}
					case -58:
						break;
					case 59:
						{
												   ErrMsg.fatal(yyline+1, CharNum.num,"unterminated string literal with bad escaped character ignored");
												   CharNum.num = 1;
											      }
					case -59:
						break;
					case 60:
						{
												ErrMsg.warn(yyline+1, CharNum.num, "string literal with bad escaped character ignored");
												CharNum.num += yytext().length();
											 }
					case -60:
						break;
					case 62:
						{
										Symbol s = new Symbol(sym.ID, new IdTokenVal(yyline+1, CharNum.num,yytext()));
										CharNum.num += yytext().length();
										return s;
									}
					case -61:
						break;
					case 63:
						{ ErrMsg.fatal(yyline+1, CharNum.num,
                         "ignoring illegal character: " + yytext());
            CharNum.num++;
          }
					case -62:
						break;
					case 65:
						{
										Symbol s = new Symbol(sym.ID, new IdTokenVal(yyline+1, CharNum.num,yytext()));
										CharNum.num += yytext().length();
										return s;
									}
					case -63:
						break;
					case 66:
						{ ErrMsg.fatal(yyline+1, CharNum.num,
                         "ignoring illegal character: " + yytext());
            CharNum.num++;
          }
					case -64:
						break;
					case 68:
						{
										Symbol s = new Symbol(sym.ID, new IdTokenVal(yyline+1, CharNum.num,yytext()));
										CharNum.num += yytext().length();
										return s;
									}
					case -65:
						break;
					case 70:
						{
										Symbol s = new Symbol(sym.ID, new IdTokenVal(yyline+1, CharNum.num,yytext()));
										CharNum.num += yytext().length();
										return s;
									}
					case -66:
						break;
					case 72:
						{
										Symbol s = new Symbol(sym.ID, new IdTokenVal(yyline+1, CharNum.num,yytext()));
										CharNum.num += yytext().length();
										return s;
									}
					case -67:
						break;
					case 74:
						{
										Symbol s = new Symbol(sym.ID, new IdTokenVal(yyline+1, CharNum.num,yytext()));
										CharNum.num += yytext().length();
										return s;
									}
					case -68:
						break;
					case 76:
						{
										Symbol s = new Symbol(sym.ID, new IdTokenVal(yyline+1, CharNum.num,yytext()));
										CharNum.num += yytext().length();
										return s;
									}
					case -69:
						break;
					case 77:
						{
										Symbol s = new Symbol(sym.ID, new IdTokenVal(yyline+1, CharNum.num,yytext()));
										CharNum.num += yytext().length();
										return s;
									}
					case -70:
						break;
					case 78:
						{
										Symbol s = new Symbol(sym.ID, new IdTokenVal(yyline+1, CharNum.num,yytext()));
										CharNum.num += yytext().length();
										return s;
									}
					case -71:
						break;
					case 79:
						{
										Symbol s = new Symbol(sym.ID, new IdTokenVal(yyline+1, CharNum.num,yytext()));
										CharNum.num += yytext().length();
										return s;
									}
					case -72:
						break;
					case 80:
						{
										Symbol s = new Symbol(sym.ID, new IdTokenVal(yyline+1, CharNum.num,yytext()));
										CharNum.num += yytext().length();
										return s;
									}
					case -73:
						break;
					case 81:
						{
										Symbol s = new Symbol(sym.ID, new IdTokenVal(yyline+1, CharNum.num,yytext()));
										CharNum.num += yytext().length();
										return s;
									}
					case -74:
						break;
					case 82:
						{
												//This is for catching the (eof)
												ErrMsg.warn(yyline+1, CharNum.num, "unterminated string literal ignored");
												CharNum.num = 1;
											}
					case -75:
						break;
					case 83:
						{
										Symbol s = new Symbol(sym.ID, new IdTokenVal(yyline+1, CharNum.num,yytext()));
										CharNum.num += yytext().length();
										return s;
									}
					case -76:
						break;
					case 84:
						{
										Symbol s = new Symbol(sym.ID, new IdTokenVal(yyline+1, CharNum.num,yytext()));
										CharNum.num += yytext().length();
										return s;
									}
					case -77:
						break;
					case 85:
						{
										Symbol s = new Symbol(sym.ID, new IdTokenVal(yyline+1, CharNum.num,yytext()));
										CharNum.num += yytext().length();
										return s;
									}
					case -78:
						break;
					case 86:
						{
										Symbol s = new Symbol(sym.ID, new IdTokenVal(yyline+1, CharNum.num,yytext()));
										CharNum.num += yytext().length();
										return s;
									}
					case -79:
						break;
					case 87:
						{
										Symbol s = new Symbol(sym.ID, new IdTokenVal(yyline+1, CharNum.num,yytext()));
										CharNum.num += yytext().length();
										return s;
									}
					case -80:
						break;
					case 88:
						{
										Symbol s = new Symbol(sym.ID, new IdTokenVal(yyline+1, CharNum.num,yytext()));
										CharNum.num += yytext().length();
										return s;
									}
					case -81:
						break;
					case 89:
						{
										Symbol s = new Symbol(sym.ID, new IdTokenVal(yyline+1, CharNum.num,yytext()));
										CharNum.num += yytext().length();
										return s;
									}
					case -82:
						break;
					case 90:
						{
										Symbol s = new Symbol(sym.ID, new IdTokenVal(yyline+1, CharNum.num,yytext()));
										CharNum.num += yytext().length();
										return s;
									}
					case -83:
						break;
					case 91:
						{
										Symbol s = new Symbol(sym.ID, new IdTokenVal(yyline+1, CharNum.num,yytext()));
										CharNum.num += yytext().length();
										return s;
									}
					case -84:
						break;
					case 92:
						{
										Symbol s = new Symbol(sym.ID, new IdTokenVal(yyline+1, CharNum.num,yytext()));
										CharNum.num += yytext().length();
										return s;
									}
					case -85:
						break;
					case 93:
						{
										Symbol s = new Symbol(sym.ID, new IdTokenVal(yyline+1, CharNum.num,yytext()));
										CharNum.num += yytext().length();
										return s;
									}
					case -86:
						break;
					case 94:
						{
										Symbol s = new Symbol(sym.ID, new IdTokenVal(yyline+1, CharNum.num,yytext()));
										CharNum.num += yytext().length();
										return s;
									}
					case -87:
						break;
					case 95:
						{
										Symbol s = new Symbol(sym.ID, new IdTokenVal(yyline+1, CharNum.num,yytext()));
										CharNum.num += yytext().length();
										return s;
									}
					case -88:
						break;
					case 96:
						{
										Symbol s = new Symbol(sym.ID, new IdTokenVal(yyline+1, CharNum.num,yytext()));
										CharNum.num += yytext().length();
										return s;
									}
					case -89:
						break;
					case 97:
						{
										Symbol s = new Symbol(sym.ID, new IdTokenVal(yyline+1, CharNum.num,yytext()));
										CharNum.num += yytext().length();
										return s;
									}
					case -90:
						break;
					case 98:
						{
										Symbol s = new Symbol(sym.ID, new IdTokenVal(yyline+1, CharNum.num,yytext()));
										CharNum.num += yytext().length();
										return s;
									}
					case -91:
						break;
					case 99:
						{
										Symbol s = new Symbol(sym.ID, new IdTokenVal(yyline+1, CharNum.num,yytext()));
										CharNum.num += yytext().length();
										return s;
									}
					case -92:
						break;
					case 100:
						{
										Symbol s = new Symbol(sym.ID, new IdTokenVal(yyline+1, CharNum.num,yytext()));
										CharNum.num += yytext().length();
										return s;
									}
					case -93:
						break;
					case 101:
						{
										Symbol s = new Symbol(sym.ID, new IdTokenVal(yyline+1, CharNum.num,yytext()));
										CharNum.num += yytext().length();
										return s;
									}
					case -94:
						break;
					case 102:
						{
										Symbol s = new Symbol(sym.ID, new IdTokenVal(yyline+1, CharNum.num,yytext()));
										CharNum.num += yytext().length();
										return s;
									}
					case -95:
						break;
					case 103:
						{
										Symbol s = new Symbol(sym.ID, new IdTokenVal(yyline+1, CharNum.num,yytext()));
										CharNum.num += yytext().length();
										return s;
									}
					case -96:
						break;
					case 104:
						{
										Symbol s = new Symbol(sym.ID, new IdTokenVal(yyline+1, CharNum.num,yytext()));
										CharNum.num += yytext().length();
										return s;
									}
					case -97:
						break;
					case 105:
						{
										Symbol s = new Symbol(sym.ID, new IdTokenVal(yyline+1, CharNum.num,yytext()));
										CharNum.num += yytext().length();
										return s;
									}
					case -98:
						break;
					case 106:
						{
										Symbol s = new Symbol(sym.ID, new IdTokenVal(yyline+1, CharNum.num,yytext()));
										CharNum.num += yytext().length();
										return s;
									}
					case -99:
						break;
					default:
						yy_error(YY_E_INTERNAL,false);
					case -1:
					}
					yy_initial = true;
					yy_state = yy_state_dtrans[yy_lexical_state];
					yy_next_state = YY_NO_STATE;
					yy_last_accept_state = YY_NO_STATE;
					yy_mark_start();
					yy_this_accept = yy_acpt[yy_state];
					if (YY_NOT_ACCEPT != yy_this_accept) {
						yy_last_accept_state = yy_state;
						yy_mark_end();
					}
				}
			}
		}
	}
}
