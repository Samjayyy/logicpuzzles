package be.samwise.logicpuzzles.pegsolitaire;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.Arrays;
import java.util.HashSet;
import java.util.InputMismatchException;
import java.util.Stack;

public class Main {
	
	static class BoardState{
		private long occupied;
		private BoardState prev;
		private int fillCount;
		public BoardState(int fillCount){
			this.fillCount = fillCount;
		}
		public BoardState(BoardState prev) {
			this(prev.fillCount-1);
			this.prev = prev;
			this.occupied = prev.occupied;
		}
		public void occupy(int id){
			this.occupied |= (1L<<id);
		}
		public void unoccupy(int id){
			this.occupied ^= (1L<<id);
		}
		public boolean isOccupied(int id){
			return (this.occupied & (1L<<id)) != 0;
		}
		public BoardState getPrev() {
			return prev;
		}
		public boolean isFinished(){
			return this.fillCount==1;
		}
		@Override
		public int hashCode() {
			return (int)(occupied % 1000000007);
		}
		@Override
		public boolean equals(Object obj) {
			if (!(obj instanceof BoardState))
	            return false;
	        if (obj == this)
	            return true;
	        return this.occupied == ((BoardState)obj).occupied;
		}
	}
	
	static class Board {
		private int[]up,right,down,left;
		private int totalCount;
		private BoardState[] dirs;
		private int[][] board;
		public Board(int boardCount, int rows, int cols) {
			this.totalCount = boardCount;
			up = new int[boardCount];
			right = new int[boardCount];
			down = new int[boardCount];
			left = new int[boardCount];
			Arrays.fill(up, -1);
			Arrays.fill(right, -1);
			Arrays.fill(down, -1);
			Arrays.fill(left, -1);
			dirs = new BoardState[4];// init array of states for each dimension
			board = new int[rows][cols]; // for printing purpose
		}
		public int getSize(){
			return totalCount;
		}
		public void linkRow(int leftId, int rightId){
			left[rightId]=leftId;
			right[leftId]=rightId;
		}
		public void linkCol(int upId, int downId){
			up[downId]=upId;
			down[upId]=downId;
		}
		public BoardState[] getNextDirs() {
			return dirs;
		}
		public void initNextFor(BoardState state, int idx){
			dirs[0] = nextState(state, idx, up);
			dirs[1] = nextState(state, idx, right);
			dirs[2] = nextState(state, idx, down);
			dirs[3] = nextState(state, idx, left);
		}		
		/*
		 * ix ix2 ix3
		 *  O   O   .
		 *  ---------
		 *  .   .   O 
		 */
		private BoardState nextState(BoardState state, int idx, int[]direction){
			if(direction[idx]==-1 
					|| direction[direction[idx]]==-1){
				return null;	// no path to left
			}
			if(!state.isOccupied(idx)
					|| !state.isOccupied(direction[idx])
					|| state.isOccupied(direction[direction[idx]])){
				return null;
			}
			state.unoccupy(idx);
			state.unoccupy(direction[idx]);
			state.occupy(direction[direction[idx]]);
			BoardState next = new BoardState(state);
			state.occupy(idx);
			state.occupy(direction[idx]);
			state.unoccupy(direction[direction[idx]]);
			return next;
		}
		
		public String toString(BoardState state){
			StringBuilder sb = new StringBuilder();
			for(int i=0;i<this.board.length;i++){
				for(int j=0;j<this.board[0].length;j++){
					if(this.board[i][j]==-1){
						sb.append(NO_BOARD);
					}else if(state.isOccupied(this.board[i][j])){
						sb.append(FILLED);
					}else{
						sb.append(FREE);
					}
				}
				sb.append("\n");
			}
			return sb.toString();
		}
	}
	
	// PEGSOLVER
	static final char NO_BOARD = '#',
			FREE = '.',
			FILLED = 'O';			
	public static void main(String[] args) throws Exception {
		final InputReader in = new InputReader(System.in);
		final OutputWriter out = new OutputWriter(System.out);
		int L = in.readInt(); // number of lines
		long startTime = System.currentTimeMillis();
		int freeCount = 0,
			filledCount = 0;
		Board board;
		BoardState initState;
		// READ AND INIT DATASTRUCTURE
		{
			String[] boardIn = new String[L];
			int maxL = 0;
			for(int i=0;i<L;i++){
				boardIn[i]=in.readLine();
				maxL = Math.max(maxL, boardIn[i].length());
				for(int c = 0;c < boardIn[i].length();c++){
					switch(boardIn[i].charAt(c)){
						case NO_BOARD: continue;
						case FREE: freeCount++;break;
						case FILLED: filledCount++;break;
						default: throw new InputMismatchException("Unknown character: "+boardIn[i].charAt(c)+". Expected one of the following: ("+FREE+", "+FILLED+", "+NO_BOARD+")");
					}
				}
			}
			if(freeCount==0){
				throw new IllegalStateException("It's impossible to solver this puzzle whitout any free places");				
			}
			if(filledCount==0){
				throw new IllegalStateException("The board is empty");
			}
			if(freeCount+filledCount > 60){
				throw new UnsupportedOperationException("This calculation method does currently not support bigger more than 60 open places");
			}
			board = new Board(freeCount+filledCount, L, maxL);
			initState = new BoardState(filledCount);
			int[]prev=new int[maxL];
			Arrays.fill(prev, -1);
			int idx = 0;			
			// Handle input into board and initial boardstate
			for(int i=0;i<L;i++){
				for(int c=0;c<boardIn[i].length();c++){
					if(boardIn[i].charAt(c)==NO_BOARD){
						prev[c]=-1;
						board.board[i][c]=-1;
						continue;
					}
					board.board[i][c]=idx;
					if(boardIn[i].charAt(c)==FILLED){
						initState.occupy(idx);
					}
					if(c > 0 && prev[c-1]>=0){
						board.linkRow(prev[c-1], idx);
					}
					if(prev[c]>=0){
						board.linkCol(prev[c], idx);
					}
					prev[c]=idx++;
				}				
			}
		}
		// Calculate
		HashSet<BoardState> set = new HashSet<>();
		Stack<BoardState> stack = new Stack<>();
		set.add(initState);
		stack.add(initState);
		BoardState endState = null;
		while(!stack.isEmpty()){
			BoardState cur = stack.pop();
			if(cur.isFinished()){
				endState = cur;
				break;
			}
			for(int i=0;i<board.getSize();i++){
				if(!cur.isOccupied(i)){
					continue;
				}
				board.initNextFor(cur, i);
				for(BoardState nstate : board.getNextDirs()){
					if(nstate != null && set.add(nstate)){
						stack.add(nstate);
					}
				}
			}
		}
		// Print solution
		if(endState == null){
			out.printLine("No solution was found.");
		}else{
			while(endState!=null){				
				out.print(board.toString(endState));
				out.printLine("------------------");
				endState = endState.prev;
			}
		}
		System.err.println("Time: "+(System.currentTimeMillis()-startTime)+"ms");
		out.close();
	}
	
	static class InputReader {
		private InputStream stream;
		private byte[] buf = new byte[1024];
		private int curChar;
		private int numChars;

		public InputReader(InputStream stream) {
			this.stream = stream;
		}

		public int read() {
			if (numChars == -1)
				throw new InputMismatchException();
			if (curChar >= numChars) {
				curChar = 0;
				try {
					numChars = stream.read(buf);
				} catch (IOException e) {
					throw new InputMismatchException();
				}
				if (numChars <= 0)
					return -1;
			}
			return buf[curChar++];
		}

		public String readLine() {
			int c = read();
			while (isSpaceChar(c))
				c = read();
			StringBuilder res = new StringBuilder();
			do {
				res.appendCodePoint(c);
				c = read();
			} while (!isEndOfLine(c));
			return res.toString();
		}

		public String readString() {
			int c = read();
			while (isSpaceChar(c))
				c = read();
			StringBuilder res = new StringBuilder();
			do {
				res.appendCodePoint(c);
				c = read();
			} while (!isSpaceChar(c));
			return res.toString();
		}

		public long readLong() {
			int c = read();
			while (isSpaceChar(c))
				c = read();
			int sgn = 1;
			if (c == '-') {
				sgn = -1;
				c = read();
			}
			long res = 0;
			do {
				if (c < '0' || c > '9')
					throw new InputMismatchException();
				res *= 10;
				res += c - '0';
				c = read();
			} while (!isSpaceChar(c));
			return res * sgn;
		}

		public int readInt() {
			int c = read();
			while (isSpaceChar(c))
				c = read();
			int sgn = 1;
			if (c == '-') {
				sgn = -1;
				c = read();
			}
			int res = 0;
			do {
				if (c < '0' || c > '9')
					throw new InputMismatchException();
				res *= 10;
				res += c - '0';
				c = read();
			} while (!isSpaceChar(c));
			return res * sgn;
		}

		public boolean isSpaceChar(int c) {
			return c == ' ' || c == '\n' || c == '\r' || c == '\t' || c == -1;
		}

		public boolean isEndOfLine(int c) {
			return c == '\n' || c == '\r' || c == -1;
		}
	}

	static class OutputWriter {
		private final PrintWriter writer;

		public OutputWriter(OutputStream outputStream) {
			writer = new PrintWriter(new BufferedWriter(new OutputStreamWriter(
					outputStream)));
		}

		public OutputWriter(Writer writer) {
			this.writer = new PrintWriter(writer);
		}

		public void print(Object... objects) {
			for (int i = 0; i < objects.length; i++) {
				if (i != 0)
					writer.print(' ');
				writer.print(objects[i]);
			}
		}

		public void printLine(Object... objects) {
			print(objects);
			writer.println();
		}

		public void close() {
			writer.close();
		}
	}
}