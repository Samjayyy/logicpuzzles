package be.samwise.logicpuzzles.horsejumps;

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
		private long free;
		private BoardState prev;
		private int fillCount;
		private int horseIdx;
		public BoardState(int fillCount, int horseIdx){
			this.fillCount = fillCount;
			this.free = (1L<<fillCount)-1;
			this.horseIdx = horseIdx;
			this.occupy(horseIdx);
		}
		public BoardState(BoardState prev, int newHorseIndex) {
			this.prev = prev;
			this.free = prev.free;
			this.fillCount = prev.fillCount-1;
			this.horseIdx = newHorseIndex;
			this.occupy(horseIdx);
		}
		public void occupy(int id){
			this.free ^= (1L<<id);
		}
		public void unoccupy(int id){
			this.free |= (1L<<id);
		}
		public boolean isOccupied(int id){
			return (this.free & (1L<<id)) == 0;
		}
		public BoardState getPrev() {
			return prev;
		}
		public boolean isFinished(){
			return this.fillCount==1;
		}
		@Override
		public int hashCode() {
			return (int)(((free % 1000000009)*horseIdx)%1000000007);
		}
		@Override
		public boolean equals(Object obj) {
			if (!(obj instanceof BoardState))
	            return false;
	        if (obj == this)
	            return true;
	        BoardState oth = ((BoardState)obj);
	        return (this.free == oth.free) && (this.horseIdx == oth.horseIdx);
		}
	}
	
	static class Board {
		private static final int UNUSED = -1;
		private int totalCount;
		private int[]xval,yval;
		private int[][] board;
		private int[][] ref;
		public Board(int boardCount, int rows, int cols) {
			this.totalCount = boardCount;
			xval = new int[boardCount];
			yval = new int[boardCount];
			board = new int[rows][cols]; // for printing purpose
			for(int r=0;r<rows;r++){
				Arrays.fill(board[r], -1);
			}
			ref = new int[boardCount][8];
		}
		public int getSize(){
			return totalCount;
		}
		public void setId(int x, int y, int idx){
			board[x][y]=idx;
			xval[idx]=x;
			yval[idx]=y;
		}
		public void setUnused(int x, int y){
			board[x][y] = UNUSED;
		}
		public void initRefs(){
			for(int i=0;i<totalCount;i++){
				ref[i][0] = getId(xval[i]-2,yval[i]-1);
				ref[i][1] = getId(xval[i]-2,yval[i]+1);
				ref[i][2] = getId(xval[i]+2,yval[i]-1);
				ref[i][3] = getId(xval[i]+2,yval[i]+1);
				ref[i][4] = getId(xval[i]-1,yval[i]-2);
				ref[i][5] = getId(xval[i]-1,yval[i]+2);
				ref[i][6] = getId(xval[i]+1,yval[i]-2);
				ref[i][7] = getId(xval[i]+1,yval[i]+2);
				// simplify
				int l = 0, r= 7;
				while(l < r){
					if(ref[i][l]!=UNUSED){
						l++;
					}else if(ref[i][r] == UNUSED){
						r--;
					}else{
						ref[i][l]=ref[i][r];
						ref[i][r] = UNUSED;
						l++;r--;
					}
				}
			}
		}
		private int getId(int x, int y){
			if(x < 0 || x >= board.length)return -1;
			if(y < 0 || y >= board[x].length)return -1;
			return board[x][y];
		}
				
		public String toString(BoardState state){
			StringBuilder sb = new StringBuilder();
			for(int i=0;i<this.board.length;i++){
				for(int j=0;j<this.board[0].length;j++){
					if(this.board[i][j]==-1){
						sb.append(NO_BOARD);
					}else if(state.isOccupied(this.board[i][j])){
						sb.append(TAKEN);
					}else{
						sb.append(TILE);
					}
				}
				sb.append("\n");
			}
			return sb.toString();
		}
	}
	
	// horsejumps
	static final char NO_BOARD = '#', TILE = 'O', TAKEN = 'X';
	public static void main(String[] args) throws Exception {
		final InputReader in = new InputReader(System.in);
		final OutputWriter out = new OutputWriter(System.out);
		int L = in.readInt(); // number of lines
		long startTime = System.currentTimeMillis();
		int tiles = 0;
		Board board;
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
						case TILE: tiles++;break;
						default: throw new InputMismatchException("Unknown character: "+boardIn[i].charAt(c)+". Expected one of the following: ("+TILE+", "+NO_BOARD+")");
					}
				}
			}
			if(tiles==0){
				throw new IllegalStateException("It's impossible to solver this puzzle whitout any tile");
			}
			if(tiles > 60){
				throw new UnsupportedOperationException("This calculation method does currently not support more than 60 open places");
			}
			board = new Board(tiles, L, maxL);
			int idx = 0;
			// Handle input into board and initial boardstate
			for(int i=0;i<L;i++){
				for(int c=0;c<boardIn[i].length();c++){
					if(boardIn[i].charAt(c)==NO_BOARD){
						board.setUnused(i,c);
						continue;
					}
					board.setId(i,c,idx++);
				}
			}
			board.initRefs();
		}
		// Calculate
		HashSet<BoardState> set = new HashSet<>();
		Stack<BoardState> stack = new Stack<>();
		for(int i=0;i<tiles;i++){ // start from every possible starting position
			BoardState state = new BoardState(tiles,i);
			if(set.add(state)){
				stack.add(state);				
			}
		}
		BoardState endState = null;
		while(!stack.isEmpty()){
			BoardState cur = stack.pop();
			if(cur.isFinished()){
				endState = cur;
				break;
			}
			
			for(int i=0;i<board.ref[cur.horseIdx].length;i++){
				if(board.ref[cur.horseIdx][i]==-1) {
					break;
				}
				if(cur.isOccupied(board.ref[cur.horseIdx][i])){
					continue;
				}
				BoardState newState = new BoardState(cur, board.ref[cur.horseIdx][i]); 
				if(set.add(newState)){
					stack.add(newState);
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