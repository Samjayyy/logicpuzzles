package be.samwise.logicpuzzles.nonogram;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.InputMismatchException;
import java.util.LinkedList;

public class Main {
	
	// nonogram
	static int R, C;
	static int[][]rows, cols;
	static long[]grid;
	static long[][]rowPerms; // bitwise possible permutations per row
	static final char EMPTY = '0', FILLED = '#';
	public static void main(String[] args) throws Exception {
		final InputReader in = new InputReader(System.in);
		final OutputWriter out = new OutputWriter(System.out);
		// READ & INIT
		R = in.readInt();
		C = in.readInt();
		long startTime = System.currentTimeMillis();
		// read rows
		{
			rows = new int[R][];
			for(int r=0;r<R;r++){
				String[] l = in.readLine().split(" ");
				rows[r] = new int[l.length];
				for(int i=0;i<l.length;i++){
					rows[r][i] = Integer.parseInt(l[i]);
				}
			}
			// read columns
			cols = new int[C][];
			for(int c=0;c<C;c++){
				String[] l = in.readLine().split(" ");
				cols[c] = new int[l.length];
				for(int i=0;i<l.length;i++){
					cols[c][i] = Integer.parseInt(l[i]);
				}
			}
			// read initial board
			grid = new long[R];
			for(int r=0;r<R;r++){
				String s = in.readLine();
				for(int c=0;c<C;c++){
					if(s.charAt(c)==EMPTY)continue;
					grid[r] |= 1L<<c;
				}
			}
		}
		// Precalc
		rowPerms = new long[R][];
		for(int r=0;r<R;r++){
			LinkedList<Long> res = new LinkedList<Long>();
			int spaces = C - (rows[r].length-1);
			for(int i=0;i<rows[r].length;i++){
				spaces -= rows[r][i];
			}
			calcPerms(r, 0, spaces, 0, 0,res);
			if(res.isEmpty()){
				throw new RuntimeException("Impossible to find solution with row "+r);
			}
			rowPerms[r] = new long[res.size()];
			while(!res.isEmpty()){
				rowPerms[r][res.size()-1]=res.pollLast();
			}
		}
		// Calculate
		colVal = new int[R][C];
		colIx = new int[R][C];
		if(dfs(0)){
			// Print
			for(int r=0;r<R;r++){
				for(int c=0;c<C;c++){
					out.print((grid[r]&(1L<<c))==0 ? EMPTY : FILLED);
				}
				out.printLine();
			}
		}else{
			out.printLine("No solution was found");
		}
		System.err.println("Time: "+(System.currentTimeMillis()-startTime)+"ms");
		out.close();
	}
	
	static int[][]colVal, colIx;
	static boolean dfs(int row){
		if(row==R){
			// last check for the rows
			for(int c=0;c<C;c++){
				if(colIx[R-1][c]==cols[c].length
					|| (colIx[R-1][c] == cols[c].length-1
						&& colVal[R-1][c] == cols[c][colIx[R-1][c]])){
					continue;
				}
				return false;
			}
			return true;
		}
		for(int i=0;i<rowPerms[row].length;i++){
			grid[row] = rowPerms[row][i];
			if(updateCols(row)){
				if(dfs(row+1)){
					return true;
				}
			}
		}
		return false;
	}
	
	static boolean updateCols(int row){
		if(row==0){
			for(int c=0,ixc=1;c<C;c++,ixc<<=1){
				if((grid[0]&ixc)==0){ // bit not set
					colVal[0][c]=0;
				}else{
					colVal[0][c]=1;
				}
			}
			return true;
		}
		for(int c=0,ixc=1;c<C;c++,ixc<<=1){
			// copy from previous
			colVal[row][c]=colVal[row-1][c];
			colIx[row][c]=colIx[row-1][c];
			if((grid[row]&ixc)==0){ // bit not set
				if(colVal[row-1][c] > 0){
					if(cols[c][colIx[row-1][c]]!=colVal[row-1][c]){
						return false; // higher number expected at this position
					}
					colVal[row][c]=0;
					colIx[row][c]++;
				}
			}else{
				if(colVal[row-1][c] == 0 && colIx[row-1][c]==cols[c].length){
					return false; // no numbers left
				}
				if(cols[c][colIx[row-1][c]]==colVal[row-1][c]){
					return false; // low number expected at this position
				}
				colVal[row][c]++; // increase value
			}
		}
		return true;
	}
	
	static void calcPerms(int r, int cur, int spaces, long perm, int shift, LinkedList<Long> res){
		if(cur == rows[r].length){
			if((grid[r]&perm)==grid[r]){
				res.add(perm);				
			}
			return;
		}
		while(spaces>=0){
			calcPerms(r, cur+1, spaces, perm|(bits(rows[r][cur])<<shift), shift+rows[r][cur]+1,res);
			shift++;
			spaces--;
		}
	}
	
	static long bits(int b){
		return (1L<<b)-1; // 1 => 1, 2 => 11, 3 => 111, ...
	}
	static void printBit(long n){
		while(n>0){
			System.err.print((n&1));
			n>>=1;
		}
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