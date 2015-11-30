package be.samwise.logicpuzzles.sudoku;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.InputMismatchException;

public class Main {

	// Sudoku
	static final int BOARD_SIZE = 9;
	static final byte UNSET = 0;
	static int[][]grid;
	static boolean[][]row, col, block;
	static int[][]b;
	public static void main(String[] args) throws Exception {
		final InputReader in = new InputReader(System.in);
		final OutputWriter out = new OutputWriter(System.out);
		// INIT
		grid = new int[BOARD_SIZE][BOARD_SIZE];
		row = new boolean[BOARD_SIZE][BOARD_SIZE+1];
		col = new boolean[BOARD_SIZE][BOARD_SIZE+1];
		block = new boolean[BOARD_SIZE][BOARD_SIZE+1];
		b = new int[BOARD_SIZE][BOARD_SIZE];
		// READ
		for (int r = 0; r < BOARD_SIZE; r++) {
			for (int c = 0; c < BOARD_SIZE; c++) {
				b[r][c] = ((r/3)*3)+(c/3);
				grid[r][c] = in.readByte();
				if(grid[r][c]!=UNSET){
					row[r][grid[r][c]]=true;
					col[c][grid[r][c]]=true;
					block[b[r][c]][grid[r][c]]=true;
				}
			}
		}
		// SOLVE
		long startTime = System.currentTimeMillis();
		if(solve(0,0)){
			// PRINT
			for(int i=0;i<BOARD_SIZE;i++){
				for(int j=0;j<BOARD_SIZE;j++){
					out.print(grid[i][j]+" ");
				}
				out.printLine();
			}
		}else{
			out.printLine("This board has no solution.");
		}
		System.err.println("Time: " + (System.currentTimeMillis() - startTime) + "ms");
		out.close();
	}
	static boolean solve(int r, int c){
		if(r == BOARD_SIZE){
			return true;
		}
		if(c == BOARD_SIZE){
			return solve(r+1,0);
		}
		if(grid[r][c]!=UNSET){
			return solve(r,c+1);
		}
		for(int i=1;i<=BOARD_SIZE;i++){
			if(row[r][i] || col[c][i] || block[b[r][c]][i])continue;
			row[r][i] = true;
			col[c][i] = true;
			block[b[r][c]][i] = true;
			if(solve(r,c+1)){
				grid[r][c]=i;
				return true;
			}
			row[r][i] = false;
			col[c][i] = false;
			block[b[r][c]][i] = false;
		}
		return false;
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

		public byte readByte() {
			int c = read();
			while (isSpaceChar(c))
				c = read();
			byte res = 0;
			do {
				if (c < '0' || c > '9')
					throw new InputMismatchException();
				res *= 10;
				res += c - '0';
				c = read();
			} while (!isSpaceChar(c));
			return res;
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
			writer = new PrintWriter(new BufferedWriter(new OutputStreamWriter(outputStream)));
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