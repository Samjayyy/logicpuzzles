package be.samwise.logicpuzzles.polyominos;

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

	static class Block {
		int size;
		private int[] rows, cols;

		// ctor for the rootblock
		public Block() {
			this(1);
			this.rows[0] = this.cols[0] = 1;
		}

		public Block(int size) {
			this.size = size;
			this.rows = new int[size];
			this.cols = new int[size];
		}

		private int nbMask(int n) {
			if (n <= 0)
				return 2;
			return 5 << (n - 1);
		}

		public boolean set(int r, int c) {
			if ((rows[r] & (1 << c)) != 0)
				return false; // is already set
			if ((rows[r] & nbMask(c)) == 0 && (cols[c] & nbMask(r)) == 0) {
				return false; // no nbs are set
			}
			rows[r] |= 1 << c;
			cols[c] |= 1 << r;
			return true;
		}

		public void unset(int r, int c) {
			rows[r] ^= 1 << c;
			cols[c] ^= 1 << r;
		}

		public Block rotate() {
			Block next = new Block(this.size);
			for (int i = 0; i < size; i++) {
				next.rows[i] = rev(this.cols[i], this.size);
				next.cols[i] = this.rows[size - (1 + i)];
			}
			while (next.shiftLeft())
				;
			while (next.shiftUp())
				;
			return next;
		}

		private static int rev(int n, int toShift) {
			int r = 0;
			while (n > 0) {
				r <<= 1;
				r |= (n & 1);
				n >>= 1;
				toShift--;
			}
			if (toShift > 0) {
				r <<= (toShift);
			}
			return r;
		}

		private void copyFrom(Block b) {
			for (int i = 0; i < b.size; i++) {
				this.rows[i] = b.rows[i];
				this.cols[i] = b.cols[i];
			}
		}

		private boolean shiftLeft() {
			if (this.cols[0] != 0) {
				return false;
			}
			for (int i = 1; i < size; i++) {
				this.cols[i - 1] = this.cols[i];
			}
			this.cols[size - 1] = 0;
			for (int i = 0; i < size; i++) {
				this.rows[i] >>= 1;
			}
			return true;
		}

		private boolean shiftUp() {
			if (this.rows[0] != 0) {
				return false;
			}
			for (int i = 1; i < size; i++) {
				this.rows[i - 1] = this.rows[i];
			}
			this.rows[size - 1] = 0;
			for (int i = 0; i < size; i++) {
				this.cols[i] >>= 1;
			}
			return true;
		}

		private boolean shiftRight() {
			if (this.cols[size - 1] != 0) {
				return false;
			}
			for (int i = size - 1; i > 0; i--) {
				this.cols[i] = this.cols[i - 1];
			}
			this.cols[0] = 0;
			for (int i = 0; i < size; i++) {
				this.rows[i] <<= 1;
				this.rows[i] &= (1 << size) - 1;
			}
			return true;
		}

		private boolean shiftDown() {
			if (this.rows[size - 1] != 0) {
				return false;
			}
			for (int i = size - 1; i > 0; i--) {
				this.rows[i] = this.rows[i - 1];
			}
			this.rows[0] = 0;
			for (int i = 0; i < size; i++) {
				this.cols[i] <<= 1;
				this.cols[i] &= (1 << size) - 1;
			}
			return true;
		}

		@Override
		public String toString() {
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < size; i++) {
				if(rows[i]==0)break;
				int n = rows[i];
				while (n != 0) {
					if ((n & 1) == 1) {
						sb.append('X');
					} else {
						sb.append(' ');
					}
					n >>= 1;
				}
				sb.append('\n');
			}
			return sb.toString();
		}

		@Override
		public int hashCode() {
			// assumption => fully shifted to the left/top
			return Arrays.hashCode(this.rows); 
		}

		@Override
		public boolean equals(Object obj) {
			Block b = (Block) obj;
			// assumption => fully shifted to the left/top
			return this.size == b.size && Arrays.equals(this.rows, b.rows); 
		}
	}

	static class Solver {
		private static final int NORMAL = 0, DOWN = 1, RIGHT = 2;
		private int currentSize;
		private HashSet<Block> set;
		private LinkedList<Block> list, next;
		private Block toAdd = null, cur = null;
		private int mode;

		public Solver() {
			list = new LinkedList<>();
			set = new HashSet<>();
			list.add(new Block());
			currentSize = 1;
			mode = NORMAL;
		}

		public int getCurrentSize(){
			return currentSize;
		}

		public void solveNextSize() {
			this.currentSize++;
			next = new LinkedList<>();
			while (!list.isEmpty()) {
				cur = list.pollFirst();
				addLeft();
				addTop();
				addBetween();
			}
			set.clear();
			list = next;
		}
		
		private void resetToAdd() {
			toAdd = new Block(currentSize);
			if (cur != null) {
				toAdd.copyFrom(cur);
			}
			if(mode == RIGHT){
				toAdd.shiftRight();
			}else if(mode == DOWN){
				toAdd.shiftDown();
			}
		}

		private boolean add(int r, int c) {
			if (toAdd.set(r, c)) {
				if (set.add(toAdd)) {
					next.add(toAdd);
					// add all rotations to set
					toAdd = toAdd.rotate();
					set.add(toAdd);
					toAdd = toAdd.rotate();
					set.add(toAdd);
					toAdd = toAdd.rotate();
					set.add(toAdd);
					// reset toAdd
					resetToAdd();
					return true;
				} else {
					toAdd.unset(r, c);
				}
			}
			return false;
		}

		private void addLeft() {
			mode = RIGHT;
			resetToAdd();
			for (int r = 0; r < currentSize; r++) {
				add(r, 0);
			}
		}

		private void addTop() {
			mode = DOWN;
			resetToAdd();
			for (int c = 0; c < currentSize; c++) {
				add(0, c);
			}
		}

		private void addBetween() {
			mode = NORMAL;
			resetToAdd();
			for (int r = 0; r < currentSize; r++) {
				for (int c = 0; c < currentSize; c++) {
					add(r,c);
				}
			}
		}
		@Override
		public String toString() {
			return "There are "+this.list.size()+" blocks of size "+currentSize;
		}
		public String allBlocks(){
			StringBuilder sb = new StringBuilder();
			for(Block b:list){
				sb.append(b);
				sb.append("-------------\n");
			}
			return sb.toString();
		}
	}

	// polyominos
	public static void main(String[] args) throws Exception {
		final InputReader in = new InputReader(System.in);
		final OutputWriter out = new OutputWriter(System.out);
		// READ/ init
		int N = in.readInt();
		long startTime = System.currentTimeMillis();
		Solver solver = new Solver();
		// SOLVE
		while (solver.currentSize < N) {
			solver.solveNextSize();
		}
		// print solutions
		out.printLine(solver);
		//out.printLine(solver.allBlocks());
		System.err.println("Time: " + (System.currentTimeMillis() - startTime) + "ms");
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