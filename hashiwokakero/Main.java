package be.samwise.logicpuzzles.hashiwokakero;

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
	
	static class Island{
		private final int r, c, val, id;
		private int rest, vr, vd;
		private Island right, down, next;
		private Island left,up;
		public Island(int id, int r, int c, int v, Island left, Island up, Island prev) {
			this.id=id;
			this.r=r;this.c=c;
			this.rest=this.val=v;
			vr = vd = 0;
			this.left = left;
			if(left!=null){
				left.right = this;
			}
			this.up=up;
			if(up!=null){
				up.down = this;
			}
			if(prev!=null){
				prev.next = this;
			}
		}
		private boolean allConnected(){
			HashSet<Island> set = new HashSet<Island>();
			Stack<Island> stack = new Stack<Island>();
			set.add(this);
			stack.add(this);
			while(!stack.isEmpty()){
				Island cur = stack.pop();
				if(cur.left != null
						&& cur.left.vr > 0
						&& set.add(cur.left)){
					stack.add(cur.left);
				}
				if(cur.up != null
						&& cur.up.vd > 0
						&& set.add(cur.up)){
					stack.add(cur.up);
				}
				if(cur.vr > 0 && set.add(cur.right)){
					stack.add(cur.right);
				}
				if(cur.vd > 0
						&& set.add(cur.down)){
					stack.add(cur.down);
				}
			}
			return set.size() == totalIslands;
		}
		@Override
		public int hashCode() {
			return this.id;
		}
		@Override
		public boolean equals(Object obj) {
			return this.id == ((Island)obj).id;
		}
		public boolean dfs(){
			if(this.next == null){
				return this.rest == 0
						&& allConnected(); // done if last island is done and all connected
			}
			if(this.rest == 0){
				return this.next.dfs(); // value already 0 => continue with next island
			}
			int maxDown = this.down == null ? 0 : Math.min(MAX_BRIDGE, this.down.rest);
			if(this.rest > MAX_BRIDGE + maxDown) {
				return false;				
			}
			int maxRight = 0;
			if(this.right != null){
				maxRight = Math.min(MAX_BRIDGE, this.right.rest);
				for(int cc=this.c+1;cc<this.right.c;cc++){
					if(depth[cc] >= this.r){
						maxRight = 0;
						break;
					}
				}
			}
			if(this.rest > maxRight + maxDown) 
				return false;
			int tmpDepth = depth[this.c];
			vr=Math.min(maxRight,this.rest);
			vd =this.rest-vr;
			while(vr >= 0 && vd <= maxDown){
				if(this.vr > 0) {
					this.right.rest -= vr;
				}
				if(this.vd > 0){
					this.down.rest -= vd;
					depth[this.c] = this.down.r;
				}else{
					depth[this.c] = tmpDepth;
				}
				if(this.next.dfs()){
					return true;
				}
				if(this.vr > 0) {
					this.right.rest += vr;
				}
				if(this.vd > 0){
					this.down.rest += vd;
				}
				vr--;vd++;
			}
			depth[this.c]=tmpDepth;
			this.vr=this.vd=0;
			return false;
		}
		public char[][] generateSol(){
			char[][]sol = new char[R][C];
			for(int r=0;r<R;r++){
				Arrays.fill(sol[r], map[0]);
			}
			Island cur = this;
			while(cur!=null){				
				sol[cur.r][cur.c]=(char)('0'+cur.val);
				if(cur.vr > 0){
					for(int cc=cur.c+1;cc<cur.right.c;cc++){
						sol[cur.r][cc]=map[cur.vr];
					}
				}
				if(cur.vd > 0){
					for(int rr=cur.r+1;rr<cur.down.r;rr++){
						sol[rr][cur.c]=map[cur.vd+2];
					}
				}
				cur = cur.next;
			}
			return sol;
		}
	}
	
	// hashiwokakero
	static int R, C;
	static Island root;
	static int totalIslands;
	static int[] depth;
	static final int MAX_BRIDGE=2;
	static final char[] map = {'.','-','=','|','#'};
	static final int EMPTY = 0, h1 = 1, h2 = 2, v1 = 3, v2 = 4;
	public static void main(String[] args) throws Exception {
		final InputReader in = new InputReader(System.in);
		final OutputWriter out = new OutputWriter(System.out);
		// READ & INIT
		R = in.readInt();
		C = in.readInt();
		long startTime = System.currentTimeMillis();
		// read rows
		{
			depth = new int[C];
			Arrays.fill(depth, -1);
			Island[]cols = new Island[C];
			Island prev = null;
			root = null;
			totalIslands = 0;
			for(int r=0;r<R;r++){
				String l = in.readLine();
				if(C != l.length()){
					throw new InputMismatchException("Unexpected number of columns in row "+(r+1)+": "+l.length()+" <> "+C);
				}
				Island left = null;
				for(int c=0;c<C;c++){
					if(l.charAt(c)==map[EMPTY])continue;
					Island il = new Island(totalIslands++,r,c,l.charAt(c)-'0', left, cols[c], prev);
					if(root == null){
						root = il;
					}
					// set up for future
					prev = left = cols[c] = il;
				}
			}
		}
		// Calculate
		if(root.dfs()){
			char[][] sol = root.generateSol();
			for(int r=0;r<R;r++){
				for(int c=0;c<C;c++){
					out.print(sol[r][c]);
				}
				out.printLine();
			}
		}else{
			out.printLine("No solution was found");
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