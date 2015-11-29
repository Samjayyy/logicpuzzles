package be.samwise.logicpuzzles.eightpuzzle;

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
import java.util.PriorityQueue;

public class Main {

	static class Board implements Comparable<Board> {
		private final byte[]tiles;
		private final byte prevId;
		private byte curId;
		private final int moves, hash;
		private byte wrongPlace;
		private int cost;
		Board next = null, prev = null;

		public Board(byte[] blocks) {
			tiles = new byte[size];
			this.prevId = this.curId = -1;
			this.moves = 0;
			cost = wrongPlace = 0;
			for (byte i = 0; i < size; i++) {
				this.tiles[i] = blocks[i];
				if (tiles[i] == EMPTY) {
					this.curId = i;
				}else{
					// WRONG PLACE
					if (isWrongPlace(i)) {
						wrongPlace++;
					}					
					// MANHATTAN
					cost += manhattan(i);
				}
			}
			// LINEAR CONFLICT
			for(int r=0;r<rows;r++){
				cost += linearConflictRow(r);
			}
			for(int c=0;c<cols;c++){
				cost += linearConflictCol(c);
			}
			// HASH
			hash = Arrays.hashCode(this.tiles);
		}

		public Board(Board prev, byte newId) {
			this.prev = prev;
			this.tiles = new byte[size];
			for (byte i = 0; i < size; i++) {
				this.tiles[i] = prev.tiles[i];
			}
			this.moves = prev.moves + 1;
			this.wrongPlace = prev.wrongPlace;
			this.cost = prev.cost;
			this.prevId = prev.curId;
			this.curId = newId;
			swapWithPrev();
			// HASH
			hash = Arrays.hashCode(this.tiles);
		}

		private void swapWithPrev() {
			this.cost++; // add move to total cost
			// Wrong place before
			if (isWrongPlace(this.curId)) {
				wrongPlace--;
			}
			// manhattan before
			this.cost -= manhattan(this.curId);
			// linear conflict before
			int r1 = this.curId/cols, // row new spot
				r2 = this.prevId/cols, // row of old spot
				r3 = this.tiles[this.curId]/cols; // expected row of moved tile
			int	c1 = this.curId%cols, // col new spot
				c2 = this.prevId%cols, // col of old spot
				c3 = this.tiles[this.curId]%cols; // expected col of moved tile
			// linear conflict after
			if(c1 != c2
					&& (c1 == c3 || c2 == c3)){ // column change
				this.cost -= linearConflictCol(c3);
			}else if(r1 != r2
					&& (r1 == r3 || r2 == r3)){ // row change
				this.cost -= linearConflictRow(r3);
			}
			// Swap
			byte t = this.tiles[curId];
			this.tiles[curId] = this.tiles[prevId];
			this.tiles[prevId] = t;
			// Wrong place after
			if (isWrongPlace(this.prevId)) {
				wrongPlace++;
			}
			// manhattan after
			this.cost += manhattan(this.prevId);
			// linear conflict after
			if(c1 != c2
					&& (c1 == c3 || c2 == c3)){ // column change
				this.cost += linearConflictCol(c3);
			}else if(r1 != r2
					&& (r1 == r3 || r2 == r3)){ // row change
				this.cost += linearConflictRow(r3);
			}
		}

		private boolean isWrongPlace(byte id) {
			return tiles[id] != id;
		}

		private int manhattan(byte id) {
			return mhdist[id][this.tiles[id]];
		}
		private int linearConflictRow(int row){
			int max = -1;
			int res = 0;
			for(int c=0,id=row*cols;c<cols;c++,id++){
				if(tiles[id] == EMPTY || tiles[id]/cols != row)
					continue;
				if(max < tiles[id]){
					max = tiles[id];
				}else{
					res += 2;
				}
			}
			return res;
		}
		private int linearConflictCol(int col){
			int max = -1;
			int res = 0;
			for(int r=0,id=col;r<rows;r++,id+=cols){
				if(tiles[id] == EMPTY || tiles[id]%cols != col)
					continue;
				if(max < tiles[id]){
					max = tiles[id];
				}else{
					res += 2;
				}
			}
			return res;
		}
		public boolean isGoal() {
			return wrongPlace == 0;
		}

		@Override
		public boolean equals(Object y) {
			Board that = (Board) y;
			return this.hash == that.hash 
					&& this.cost == that.cost
					&& this.wrongPlace == that.wrongPlace
					&& Arrays.equals(this.tiles, that.tiles);
		}

		public int countInversions(){
			int count = 0;
			for(int i=0;i<size;i++){
				if(tiles[i]==EMPTY)continue;
				for(int j=i+1;j<size;j++){
					if(tiles[j]==EMPTY)continue;
					if(tiles[j]<tiles[i])
						count++;
				}
			}
			return count;
		}
		
		public String toString() {
			StringBuilder s = new StringBuilder();
			int c = 0;
			for (byte i = 0; i < size; i++) {
				if(tiles[i]==EMPTY){
					s.append(" X ");
				}else{
					s.append(String.format("%2d ", tiles[i]+1));
				}
				if(++c == cols){
					c=0;
					s.append("\n");
				}
			}
			return s.toString();
		}

		@Override
		public int compareTo(Board o) {
			if (this.cost == o.cost) {
				if(this.moves == o.moves){
					return this.hash < o.hash ? -1 : 1;					
				}
				return this.moves > o.moves ? -1 : 1;
			}
			return this.cost < o.cost ? -1 : 1;
		}
	}

	// Eightpuzzle
	static final int EMPTY = -1;
	static byte rows, cols, size;
	static byte[][]next;
	static int[][]mhdist;
	public static void main(String[] args) throws Exception {
		final InputReader in = new InputReader(System.in);
		final OutputWriter out = new OutputWriter(System.out);
		rows = in.readByte(); // number of rows
		cols = in.readByte(); // number of columns
		long startTime = System.currentTimeMillis();
		Board initial;
		// READ AND INIT DATASTRUCTURE
		{
			size = (byte)(rows * cols);
			byte[]field = new byte[size];
			for(int i=0;i<size;i++){
				field[i]=(byte)(in.readByte()-1);
			}
			next = new byte[size][];
			for(int i=0;i<size;i++){
				int s = 4;
				int r = i/cols;
				int c = i%cols;
				if(r==0||(r+1==rows))s--;
				if(c==0||(c+1==cols))s--;
				next[i] = new byte[s];
				if(r > 0){
					next[i][--s]=(byte)(((r-1)*cols) + c);
				}
				if(c > 0){
					next[i][--s]=(byte)((r*cols) + c-1);
				}
				if(r+1 < rows){
					next[i][--s]=(byte)(((r+1)*cols) + c);
				}
				if(c+1 < cols){
					next[i][--s]=(byte)((r*cols) + c+1);
				}
			}
			mhdist = new int[size][size];
			for(int i=0;i<size;i++){
				mhdist[i][i]=0;
			}
			for(int i=0;i<size;i++){
				for(int j=i+1;j<size;j++){
					mhdist[i][j]=mhdist[j][i]=
							(Math.abs((i / cols) - (j / cols)) + Math.abs((i % cols) - (j % cols)));
				}
			}
			initial = new Board(field);
		}
		if(!isSolvable(initial)){
			out.printLine("This board has no solution.");
		}else{
			// Calculate with ..
			//calcIDAStar(initial); // IDA*
			calcAStar(initial); // A*

			// Print solution
			while(initial != null){
				out.printLine(">> Step "+initial.moves);
				out.printLine(initial);
				initial = initial.next;
			}
		}
		System.err.println("Time: " + (System.currentTimeMillis() - startTime) + "ms");
		out.close();
	}
	// ( (grid width odd) && (#inversions even) )  ||  ( (grid width even) && ((blank on odd row from bottom) == (#inversions even)) )
	static boolean isSolvable(Board b){
		int inversions = (b.countInversions() & 1);
		if((cols & 1) == 1){ // gird width odd
			return (inversions & 1) == 0; // #inversions even
		}
		int row = (rows - b.curId/cols);
		return (row & 1) != (inversions & 1);
	}
	static void calcAStar(Board initial){
		PriorityQueue<Board> pq = new PriorityQueue<>();
		HashSet<Board> set = new HashSet<>();
		pq.add(initial);
		set.add(initial);
		while (!pq.isEmpty()) {
			Board cur = pq.poll();
			if(cur.isGoal()){
				// construct path
				while(cur != initial){
					cur.prev.next = cur;
					cur = cur.prev;
				}
				break;
			}
			for(byte i=0;i<next[cur.curId].length;i++){
				if(cur.prevId == next[cur.curId][i])continue;
				Board nextBoard = new Board(cur, next[cur.curId][i]);
				if(set.add(nextBoard)){
					pq.add(nextBoard);
				}
			}
		}
	}
	static void calcIDAStar(Board initial){
		int costBound = initial.cost;
		while(initial.next == null){ // NOTE: always expecting a solution
			System.err.println(">> "+costBound);
			if(dfs(initial, costBound)!=null)
				break;
			costBound+=2;
		}
	}
	
	static Board dfs(Board board, int maxBound){
		if(board.cost > maxBound)
			return null;
		if(board.isGoal())
			return board;
		for(byte i=0;i<next[board.curId].length;i++){
			if(board.prevId == next[board.curId][i])
				continue;
			board.next = dfs(new Board(board, next[board.curId][i]), maxBound);
			if(board.next != null){
				return board;
			}
		}
		return null;
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