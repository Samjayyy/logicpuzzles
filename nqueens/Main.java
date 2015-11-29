package be.samwise.logicpuzzles.nqueens;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.InputMismatchException;

import javax.naming.OperationNotSupportedException;

public class Main {
	
	// n-queens
	static int N;
	public static void main(String[] args) throws Exception {
		final InputReader in = new InputReader(System.in);
		final OutputWriter out = new OutputWriter(System.out);
		// READ & INIT
		N = in.readInt(); // size of the the board
		if(N > 32){
			throw new OperationNotSupportedException("For now no implementation for a board of size "+N);
		}
		long startTime = System.currentTimeMillis();
		sol = new int[N];
		// Calculate
		cols = diag1 = diag2 = 0;
		if(findQueen(0)){
			// print
			for(int r=0;r<N;r++){
				for(int c=0;c<N;c++){
					if(sol[r]==c){
						out.print("Q ");
					}else{
						out.print("X ");						
					}					
				}
				out.printLine();
			}
		}else{
			out.printLine("No queen combination found.");
		}
		System.err.println("Time: "+(System.currentTimeMillis()-startTime)+"ms");
		out.close();
	}
	
	static long cols, diag1, diag2;
	static int[] sol;
	static boolean findQueen(int r){
		if(r==N){
			return true;
		}
		long cm = 1;
		for(int c=0;c<N;c++,cm<<=1){
			if((cm & cols) != 0) continue; // col used
			if(((1L<<(N+r-c-1)) & diag1) != 0) continue; // diag1 used
			if(((1L<<(N+N-(2+r+c)))&diag2) != 0) continue; // diag2 used
			cols |= cm;
			diag1 |= 1L<<(N+r-c-1);
			diag2 |= 1L<<(N+N-(2+r+c));
			sol[r]=c;
			if(findQueen(r+1)){
				return true;
			}
			cols ^= cm;
			diag1 ^= (1L<<(N+r-c-1));
			diag2 ^= 1L<<(N+N-(2+r+c));
		}
		return false;
	}
	
	static void testDiagonals(){
		System.err.println("diag1:");
		for(int i=0;i<N;i++){
			for(int j=0;j<N;j++){
				System.err.print(String.format("%2d ",(N+i-j-1)));
			}
			System.err.println();
		}
		System.err.println("diag2");
		for(int i=0;i<N;i++){
			for(int j=0;j<N;j++){
				System.err.print(String.format("%2d ",(N+N)-(2+i+j)));
			}
			System.err.println();
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