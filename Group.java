import java.util.ArrayList;
import java.util.Arrays;

public abstract class Group {
	int num_elements;

	// cells arraylist contains cell indexes of a group
	ArrayList<int[]> cells;
	ArrayList<ArrayList<Integer>> solutions;

	public Group(ArrayList<int[]> cells) {
		this.cells = cells; // optionally, clone
		num_elements = cells.size();// no of elements in the group
	}

	public abstract void computeSolutions(int N);
}

class AddGroup extends Group {
	int target;

	// calls superclass Group and stores indexes of cells in global arraylist cells,
	// target is the result to be achieved in group for addition
	public AddGroup(int target, ArrayList<int[]> n) {
		super(n);
		this.target = target;
	}

	//
	public void computeSolutions(int N) {
		ArrayList<Integer> numbers = new ArrayList<Integer>(); // creating an arraylist of integers which
	                                                        	//could be used to solve puzzle(from 1 to length of puzzle)
		for (int i = 1; i <= N; i++)
			numbers.add(i);
//candidate_solutions is the arraylist of arraylist 
		//of integers returned from sum_combinations method of Helper class
		ArrayList<ArrayList<Integer>> candidate_solutions = Helper.sum_combinations(target, num_elements, numbers);
		solutions = Helper.filter_solutions(candidate_solutions, cells);
	}
}

class DiffGroup extends Group {
	int target;

	// calls superclass Group and stores indexes of cells in global arraylist
	// cells,n of element in group
	// target is the result to be achieved in group for subtraction

	public DiffGroup(int target, ArrayList<int[]> n) throws Exception {
		super(n);
		
		this.target = target;
		if(n.size() != 2) throw new Exception("Number of Cells should be 2");
	}

	public void computeSolutions(int N) {
		solutions = new ArrayList<ArrayList<Integer>>();
		for (int i = 1; i <= N; i++) {
			for (int j = 1; j <= N; j++)
				if (Math.abs(i - j) == target)
					solutions.add(new ArrayList<Integer>(Arrays.asList(i, j)));
		}
	}
}

class DivGroup extends Group {
	int target;

	// calls superclass Group and stores indexes of cells in global arraylist
	// cells,no of element in group
	// target is the result to be achieved in group for divison

	public DivGroup(int target, ArrayList<int[]> n) throws Exception {
		super(n);
		this.target = target;
		if(n.size() != 2) throw new Exception("Number of Cells should be 2");
	}

	public void computeSolutions(int N) {
		solutions = new ArrayList<ArrayList<Integer>>();
		for (int i = 1; i <= N; i++) {
			for (int j = 1; j <= N; j++)
				if ((j % i == 0 && j / i == target) || (i % j == 0 && i / j == target))
					solutions.add(new ArrayList<Integer>(Arrays.asList(i, j)));
		}
	}
}

class MultGroup extends Group {
	int target;

	// calls superclass Group and stores indexes of cells in global arraylist
	// cells,n of element in group
	// target is the result to be achieved in group for multiplication

	public MultGroup(int target, ArrayList<int[]> n) {
		super(n);
		this.target = target;
	}

	public void computeSolutions(int N) {
		ArrayList<Integer> numbers = new ArrayList<Integer>();
		for (int i = 1; i <= N; i++)
			numbers.add(i);

		ArrayList<ArrayList<Integer>> candidate_solutions = Helper.mult_combinations(target, num_elements, numbers);
		solutions = Helper.filter_solutions(candidate_solutions, cells);
	}
}

class EqGroup extends Group {
	
	int target;

	public EqGroup(int target, ArrayList<int[]> n) {
		//
		super(n);
		this.target = target;
		assert n.size() == 1;
	}

	public void computeSolutions(int N) {
		solutions = new ArrayList<ArrayList<Integer>>();
		ArrayList<Integer> sol = new ArrayList<Integer>();
		if(target>0 && target<=N)
		sol.add(target);
		solutions.add(sol);
	}

	public String toString() {
		return this.target + " " + solutions;
	}
}