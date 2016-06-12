package deadlocksoln;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

public class BankerAlgorithm {

	public static void main(String[] args) {
		int n, m;
		Scanner read = new Scanner(System.in);
		
		n = read.nextInt();
		m = read.nextInt();
		
		int available[] = new int[m];
		int maximum[][] = new int[n][m];
		int allocation[][] = new int[n][m];
		int need[][] = new int[n][m];
		
		String path;
		path = read.next();
		for (int i = 0; i < m; ++i)
			available[i] = read.nextInt();
		
		try (BufferedReader br = new BufferedReader(new FileReader(path))) {
			String line = br.readLine();
			int process = 0;
			while (line != null) {
				String temp[] = line.split(",");
				for (int i = 0; i < m; ++i)
					maximum[process][i] = Integer.parseInt(temp[i]);
				++process;
				line = br.readLine();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		for (int i = 0; i < n; ++i) {
			for (int j = 0; j < m; ++j) {
				allocation[i][j] = 0;
				need[i][j] = maximum[i][j];
			}
		}
		
		String instruction = "";
		while (!instruction.equals("quit")) {
			instruction = read.next();
			if (instruction.equals("*")) {
				for (int i = 0; i < m; ++i)
					System.out.print(available[i] + " ");
				System.out.println();
				for (int i = 0; i < n; ++i) {
					for (int j = 0; j < m; ++j) {
						System.out.print(allocation[i][j] + " ");
					}
					System.out.print("  ");
					for (int j = 0; j < m; ++j) {
						System.out.print(maximum[i][j] + " ");
					}
					System.out.print("  ");
					for (int j = 0; j < m; ++j) {
						System.out.print(need[i][j] + " ");
					}
					System.out.println();
				}
			} else if (instruction.equals("RL")) {
				int process;
				process = read.nextInt();
				for (int i = 0; i < m; ++i) {
					int release = read.nextInt();
					available[i] += release;
					allocation[process][i] -= release;
					System.out.println("Process: " + process + " released: " + release + " of resource: " + i);
				}
			} else if (instruction.equals("RQ")) {
				int work[] = new int[m];
				int tempavailable[] = new int[m];
				int tempmaximum[][] = new int[n][m];
				int tempneed[][] = new int[n][m];
				int tempallocation[][] = new int[n][m];
				
				for (int i = 0; i < n; ++i) {
					for (int j = 0; j < m; ++j) {
						tempavailable[j] = available[j];
						tempmaximum[i][j] = maximum[i][j];
						tempneed[i][j] = need[i][j];
						tempallocation[i][j] = allocation[i][j];
					}
				}
				
				int process = read.nextInt();
				boolean safe = true;
				
				for (int i = 0; i < m; ++i) {
					int require = read.nextInt();
					available[i] -= require;
					allocation[process][i] += require;
					if (available[i] < 0 || allocation[process][i] > maximum[process][i]) safe = false;
				}
				
				for (int i = 0; i < m; ++i)
					work[i] = available[i];
				
				boolean finish[] = new boolean[n];
				for (int i = 0; i < n; ++i) {
					finish[i] = false;
					for (int j = 0; j < m; ++j) {
						need[i][j] = maximum[i][j] - allocation[i][j];
						if (need[i][j] < 0) safe = false;
					}
				}
				
				if (!safe) {
					available = tempavailable;
					maximum = tempmaximum;
					need = tempneed;
					allocation = tempallocation;
					System.out.println("1The System is in not in a safe state, the request is NOT approved.");
					continue;
				}
				
				boolean change = true;
				while (change) {
					change = false;
					for (int i = 0; i < n; ++i) {
						if (finish[i] == true) continue;
						boolean canfinish = true;
						for (int j = 0; j < m; ++j) {
							if (work[j] < need[i][j]) canfinish = false;
						}
						if (canfinish) {
							change = true;
							finish[i] = true;
							System.out.println("Do process: " + i);
							for (int j = 0; j < m; ++j) {
								work[j] += allocation[i][j];
							}
						}
					}
				}
				for (int i = 0; i < n; ++i)
					if (finish[i] == false) safe = false;
				
				if (!safe) {
					available = tempavailable;
					maximum = tempmaximum;
					need = tempneed;
					allocation = tempallocation;
					System.out.println("The System is in not in a safe state, the request is NOT approved.");
					continue;
				}
				
				System.out.println("The System have Reached a safe state, the request is approved.");
			}
		}
	}

}
