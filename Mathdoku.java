import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class Mathdoku {

	Map<String, Group> string2group = new HashMap<String, Group>();// contains groupname and Group object for diff
																	// groups
	int[][] puzzle;// puzzle to be returned after solving
	ArrayList<String> a = new ArrayList<String>();
	int k = 0;
	int constraintsize = 0;
	int groupsize = 0;
	// this method loads the puzzle

	boolean loadPuzzle(BufferedReader stream) throws IOException {

		if (stream == null) {
			return false;
		}

		// string2cells map stores the characters(unique)
		// from input stream along with a arraylist of of cell indexes
		Map<String, ArrayList<int[]>> string2cells = new HashMap<String, ArrayList<int[]>>();
		int n = 0;
		String t = null;
		int r = 0;
		while (true) {
			// reading lines from input stream
			t = stream.readLine();
			if (t == null) {
				return false;
			}
			t = t.trim();
			// if line is empty in stream
			if (t.isEmpty()) {
				continue;
			}
			// n tells about size of puzzle
			if (n == 0) {
				n = t.length();
				k = n;
			}
			if (t.length() != n) {
				return false;
			}
			int c = 0;
			for (char i : t.toCharArray()) {
				a.add(i + "");
				if (!string2cells.containsKey("" + i)) {
					string2cells.put("" + i, new ArrayList<int[]>());
				}
				// put the indexes array in the arraylist
				string2cells.get("" + i).add(new int[] { r, c++ });
			}
			// puzzle is a square grid
			if (n - 1 == r)
				break;
			r++;

		}

		groupsize = string2cells.size();
		puzzle = new int[n][n];

		// read the input stream for operation outcome for grouping and operator

		while ((t = stream.readLine()) != null) {
			if (t.length() == 0)// if there is an empty line
			{
				continue;
			}
			String[] parts = t.trim().split("\\s+"); // might need to use \s+
			constraintsize++;
			if (parts.length < 3)
				return false;
			// first character represents group name
			String group_name = parts[0];
			int target = Integer.parseInt(parts[1]);// value to be achieved in group
			char op = parts[2].charAt(0);// operator for group
//			if(count<t.length())
//			{
//				return false;
//			}
			Group g = null;
			try {
				// applying various operators on the groups
				switch (op) {
				case '=':
					g = new EqGroup(target, string2cells.getOrDefault(group_name, new ArrayList<int[]>()));
					break;
				case '+':
					g = new AddGroup(target, string2cells.getOrDefault(group_name, new ArrayList<int[]>()));
					break;
				case '-':
					g = new DiffGroup(target, string2cells.getOrDefault(group_name, new ArrayList<int[]>()));
					break;
				case '*':
					g = new MultGroup(target, string2cells.getOrDefault(group_name, new ArrayList<int[]>()));
					break;
				case '/':
					g = new DivGroup(target, string2cells.getOrDefault(group_name, new ArrayList<int[]>()));
					break;
				}
			} catch (Exception e) {
			}
			string2group.put(group_name, g);

		}
		return true;
	}

//this method prints the puzzle if its solvable and else printle the loaded values
	String print() {
		if (puzzle == null) {
			return null;
		}
		int z = k;
		String s = "";
		if (puzzle[0][0] != 0 && solve()) {
			for (int i = 0; i < puzzle.length; i++) {
				for (int j = 0; j < puzzle[i].length; j++) {
					s += puzzle[i][j] + "";
				}
				s = s + "\n";

			}
		}

		else {
			for (int i = 0; i < a.size(); i++) {
				for (int j = i; j < k; j++) {

					s = s + a.get(j);
				}
				i = i + z - 1;
				k = k + z;
				s = s + "\n";

			}
		}
		// System.out.println("==================");
		return s;
	}

	int c = 0;// counting no of choices

//this methods tries all combinations of groups and solves the full puzzle
	public boolean tryOut(ArrayList<Group> groups, int idx) {
		if (idx == groups.size()) {
			// when all groups are filled calling isSolved method
			return Helper.isSolved(puzzle);
		}

		Group g = groups.get(idx); // picking groups in sorted order(no of solutions)
		if (g == null) {
			return false;
		}
		boolean isSolved = false;
		for (int i = 0; i < g.solutions.size(); i++) {
			int j = 0;// cell_index

			for (int[] row_col : g.cells) {
				// filling the puzzle array with for a group by taking indexes from
				// cells and taking first combination from solutions
				puzzle[row_col[0]][row_col[1]] = g.solutions.get(i).get(j++);
			}
			// if the i-th solution of Group g is at odds with the previously chosen
			// solutions
			// of groups<idx, reset the entries to 0 and try another solution for g.
			// calling isSolving method in Helper class
			if (!Helper.isSolving(puzzle)) {
				for (int[] row_col : g.cells) {
					puzzle[row_col[0]][row_col[1]] = 0;
				}
				continue;
			}
			// calling tryout method for other groups
			// If isSolved = False, we have tried all solutions of the remaining groups
			// If isSolved = True, we have found a solution for the puzzle
			isSolved = tryOut(groups, idx + 1);
			// if we get a soln for puzzle then break
			if (isSolved)
				break;

			// If none of the possible candidate solutions of remaining groups was able
			// to solve the puzzle, we have to try another solution for this group.
			// (Backtracking)

			for (int[] row_col : g.cells) {
				puzzle[row_col[0]][row_col[1]] = 0;
			}
			c += 1;// incrementing choices everytime we backtrack at group level
		}
		// If idx == 0 and isSolved=False, it means that this puzzle cannot be solved.
		return isSolved;
	}

	// this method check if puzzle could be solved or not
	boolean readyToSolve() {
		// if no of constraints are less than no of groups
		if (constraintsize < groupsize) {
			return false;
		}
		return solve();

	}

	// this method solves the puzzle
	boolean solve() {
		if (puzzle == null) {
			return false;
		}
//		if(!readyToSolve())
//		{
//			return false;
//				}
		// couting choices
		c = 0;
		// 2d array puzzle which will contain final soln
		puzzle = new int[puzzle.length][puzzle.length];
		// iterate on all characters(unique) in string2group which are group names
		for (String s : string2group.keySet()) {
			// take out the Group object so we will call compute soln in respective
			// Classes(AddGroup,EqGroup....)
			Group g = string2group.get(s);
			// compute soln compute all possible combinations for a group
			if (g == null) {
				return false;
			}
			g.computeSolutions(puzzle.length);

			// System.out.println(s);
			// System.out.println(g.solutions);
			// System.out.println("=========================");
		}

		// arraylist of group objects
		ArrayList<Group> groups = new ArrayList<Group>(string2group.values());
		// Sorting solutions arraylist of arraylist basis on the no of combinations
		Collections.sort(groups, Comparator.comparingInt(g -> g.solutions.size()));
		// calling tryout method which will check various combinations to solve the
		// puzzle
		tryOut(groups, 0);

//		printPuzzle();
		for (int i = 0; i < puzzle.length; i++) {
			for (int j = 0; j < puzzle.length; j++) {
				if (puzzle[i][j] == 0) {
					return false;
				}
			}
		}
		return Helper.isSolved(puzzle);
	}

	// this method returns the no of choices we did while trying diff combinations
	// for groups at group level
	int choices() {
		// global variable calculate in solve method which returns choices
		// if puzzle is not solvable return 0
		if (!solve()) {
			return 0;
		}
		return c;
//		int prod = 1;
//		for(Group g:string2group.values())
//			prod *= g.solutions.size();
//		return prod;
	}

}
