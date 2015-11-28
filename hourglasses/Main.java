package be.samwise.logicpuzzles.hourglasses;

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
import java.util.LinkedList;

public class Main {
	
	static class State {
		private int[]filled;
		private State prev;
		
		public State(int len){
			this.filled = new int[len+1];
		}
		public State(State prev) {
			this.prev = prev;
			this.filled = new int[prev.filled.length];
			for(int i=0;i<this.filled.length;i++){
				this.filled[i] = prev.filled[i];
			}
		}
		public int getTime() {
			return this.filled[0]; // first index is total time
		}
		public boolean swap(int idx, int max){
			if(this.filled[idx]+this.filled[idx]==max){
				return false;
			}
			this.filled[idx] = (max-this.filled[idx]);
			return true;
		}
		public State go(){
			int min = Integer.MAX_VALUE;
			for(int i=1;i<filled.length;i++){
				min = Math.min(filled[i], min);
			}
			if(min == 0){
				return null;
			}
			filled[0]+=min;
			for(int i=1;i<filled.length;i++){
				filled[i]-=min;
			}
			State newState = new State(this);
			filled[0]-=min;
			for(int i=1;i<filled.length;i++){
				filled[i]+=min;
			}
			return newState;
		}
		@Override
		public int hashCode() {
			return Arrays.hashCode(filled);
		}
		@Override
		public boolean equals(Object obj) {
			if (!(obj instanceof State))
	            return false;
	        if (obj == this)
	            return true;
	        return Arrays.equals(this.filled,((State)obj).filled);
		}
	}
	
	// pouring water		
	public static void main(String[] args) throws Exception {
		final InputReader in = new InputReader(System.in);
		final OutputWriter out = new OutputWriter(System.out);
		// READ & INIT
		int N = in.readInt(), // number of glasses
			T = in.readInt(); // result
		long startTime = System.currentTimeMillis();
		int[] size = new int[N+1];
		State initState = new State(N);
		{
			for (int i = 1; i <= N; i++) {
				size[i] = in.readInt();
				if(size[i]<=0){
					throw new IllegalArgumentException("Size of a hourglass should be strictly possitive: "+size[i]);
				}
			}
		}
		// Calculate
		HashSet<State> set = new HashSet<>();
		LinkedList<State> queue = new LinkedList<>();
		set.add(initState);
		queue.add(initState);
		State endState = null;
		while(!queue.isEmpty()){
			State cur = queue.pollFirst();
			if(cur.getTime() > T){
				continue; // impossible to stop at T time
			}
			if(cur.getTime() == T){
				endState = cur; // exactly made it at T time
				break;
			}
			State next = cur.go();
			if(next != null && set.add(next)){
				queue.addLast(next);
			}
			for(int i=1;i<=N;i++){
				if(cur.swap(i, size[i])){
					State newState = new State(cur);
					if(set.add(newState)){
						queue.addLast(newState);
					}
					cur.swap(i, size[i]);
				}
			}
		}
		// Print solution
		if(endState == null){
			out.printLine("No solution was found.");
		}else{
			int steps = 0;
			while(endState!=null){
				steps++;
				out.printLine(Arrays.toString(endState.filled));
				out.printLine("------------------");
				endState = endState.prev;
			}
			out.printLine("> Solution found in "+steps+" steps.");
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