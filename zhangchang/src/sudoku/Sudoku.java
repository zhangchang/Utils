package sudoku;

import java.util.Random;

public class Sudoku {
	
	private static int N = 3;
	private static int[][][] SudokuArray = new int[N*N][N*N][N*N+1];
	

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		//test
		int[] aa = {1,4,8};
		//System.out.println((9-1)/N*N+1);
		//System.out.println(((9-1)/N+1)*N);
		
		//initSudokuArray();
		boolean result = false;
		
		for (int i=0;i<1000;i++) {
			do {
				result = getSudoku();
				//printSudoku();
				//System.out.println("------------------------------------------");
			}  while(!result);
			
			printSudoku();
			System.out.println("------------------------------------------");
		}
		

	}
	
	public static void initSudokuArray() {
		
		for (int i=0;i<N*N;i++) {
			for (int j=0;j<N*N;j++) {
				for (int k=0;k<N*N+1;k++){
					SudokuArray[i][j][k] = k;
				}
			}
		}
	}
	
	public static boolean getSudoku() {
		
		int tmpNum = 0;
		initSudokuArray();
		for (int i=0;i<N*N;i++) {
			for (int j=0;j<N*N;j++) {
				tmpNum = setSudokuNum(i+1,j+1);
				if (tmpNum == 0)
					return false;
				adjustSudokuArray(i+1,j+1,tmpNum);
			}
		}
		
		return true;
	}
	
	public static void printSudoku() {
		
		for (int i=0;i<N*N;i++) {
			for (int j=0;j<N*N;j++) {
				System.out.print(SudokuArray[i][j][0]);
				System.out.print("\t");
			}
			System.out.print("\n");
		}
	}
	
	public static int getRandomNumFromArray(int[] arr) {
		int min = 0;
		int max = arr.length;
		
		Random random = new Random();
		int s = random.nextInt(max)%(max-min+1) + min;
		return arr[s];
	}
	
	public static void setNumToSudokuArray(int row,int col,int value) {
		SudokuArray[row-1][col-1][0] = value;
	}
	
	public static void delNumFromSudokuArray(int row,int col,int value) {
		SudokuArray[row-1][col-1][value] = 0;
	}
	
	public static int setSudokuNum(int row,int col) {
		int availableNumCnt = 0;
		
		for (int i=1;i<N*N+1;i++) {
			if (SudokuArray[row-1][col-1][i] != 0)
				availableNumCnt++;
		}
		
		if (availableNumCnt == 0)
			return 0;
		
		int[] tmpArry = new int[availableNumCnt];
		int j = 0;
		
		for (int i=1;i<N*N+1;i++) {
			if (SudokuArray[row-1][col-1][i] != 0) {
				tmpArry[j] = SudokuArray[row-1][col-1][i];
				j++;
			}
		}
		
		int num = getRandomNumFromArray(tmpArry);
		
		setNumToSudokuArray(row, col ,num);
		
		return num;
	}
	
	public static void adjustSudokuArray(int row,int col,int num) {
		
		for (int i=0;i<N*N;i++) {
			if (SudokuArray[row-1][i][0] != 0)
				continue;
			delNumFromSudokuArray(row,i+1,num);
		}
		
		for (int i=0;i<N*N;i++) {
			if (SudokuArray[i][col-1][0] != 0)
				continue;
			delNumFromSudokuArray(i+1,col,num);
		}
		
		int minRow = (row-1)/N*N+1;
		int maxRow = ((row-1)/N+1)*N;
		int minCol = (col-1)/N*N+1;
		int maxCol = ((col-1)/N+1)*N;
		
		for (int m=minRow-1;m<maxRow;m++) {
			for (int n=minCol-1;n<maxCol;n++) {
				if (SudokuArray[m][n][0] != 0)
					continue;
				delNumFromSudokuArray(m+1,n+1,num);
			}
		}
		
	}

}
