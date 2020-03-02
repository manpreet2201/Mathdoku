import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

public class Helper {
	
	//this method checks that repeated values in soln combinations 
	//for group should not be in same row or column as it can be diagonally(in add and mult)
	
	static ArrayList<ArrayList<Integer>> filter_solutions(ArrayList<ArrayList<Integer>> all_solutions, ArrayList<int[]> cells)
	{
		ArrayList<ArrayList<Integer>> selected_solutions = new ArrayList<ArrayList<Integer>>();
		
		//iterating through all the solutions of group as if all cells(in add or mult) 
		//are in same row or column then we can repeat cell values
		for(ArrayList<Integer> solution: all_solutions)
		{
			boolean selected = true;
			for(int i=0;i<cells.size();i++)
			{
				for(int j=i+1;j<cells.size();j++)
				{
					//if all cells of group are in same row or column as we cannot repeat value then
					boolean same = (cells.get(i)[0] == cells.get(j)[0]) || (cells.get(i)[1] == cells.get(j)[1]);
					if(same && solution.get(i)==solution.get(j)) {
						selected = false;
						break;
					}
				}
				if(!selected)
					break;
			}
			//if values not repeated add soln or values repeated are not in same row or column
			if(selected)
				selected_solutions.add(solution);
		}
		//final soln combinations
		return selected_solutions; 
	}
	
	//this method returns a arraylist of arraylist of  different combinations to form a group in addition 
	static ArrayList<ArrayList<Integer>> sum_combinations(int target, int num_steps, ArrayList<Integer> array)
	{
		//arraylist to return
		ArrayList<ArrayList<Integer>> res = new ArrayList<ArrayList<Integer>>();
		
		if (num_steps==1) {
			//when no of steps is 1 and target left is in array add that no to res arraylist
			if(array.contains(target))
			{
				ArrayList<Integer> temp = new ArrayList<Integer>();
				temp.add(target);
				res.add(temp);
			}
			return res;
		}
		//Traversing array which contains no from 1 to length of puzzle
		for(Integer i:array)
		{
			//calling sum_funcions recursively by subtracting i from target
			ArrayList<ArrayList<Integer>> j = sum_combinations(target-i,num_steps-1,array);
			//when we get sol from recursive calls for target-i then
			// append "i" in the start of list
			for(ArrayList<Integer> k:j)
			{
				k.add(0,i);
				if(k.size()==num_steps)
					res.add(k);
			}
		}
		return res;
	}
	
	//this method returns a arraylist of arraylist of  different combinations to form a group in multiplication
	static ArrayList<ArrayList<Integer>> mult_combinations(int target, int num_steps, ArrayList<Integer> array)
	{
		ArrayList<ArrayList<Integer>> res = new ArrayList<ArrayList<Integer>>();
		
		if (num_steps==1) {
			//when no of steps is 1 and target left is in array mult that no to res arraylist
			if(array.contains(target))
			{
				ArrayList<Integer> temp = new ArrayList<Integer>();
				temp.add(target);
				res.add(temp);
			}
			return res;
		}
		for(Integer i:array)
		{
			//Traversing array which contains no from 1 to length of puzzle
			//continue if target cannot be divided by no
			if(target%i!=0)
				continue;
			//calling mult_combinations recursively dividing target with i and subtracting 1 from numsteps
			ArrayList<ArrayList<Integer>> j = mult_combinations(target/i,num_steps-1,array);
			//when we get sol from recursive calls by dividing target with i then
			// append "i" in the start of list
			for(ArrayList<Integer> k:j)
			{
				k.add(0,i);
				if(k.size()==num_steps)
					res.add(k);
			}
		}
		return res;
	}
	
	//checks if the rows and columns are solved and they dont have 0 as value
	public static boolean isSolving(int[][] puzzle)
	{
		boolean row_solved = true;
		for(int i = 0;i<puzzle.length;i++)
		{
			int count = 0;
			HashSet<Integer> temp = new HashSet<Integer>();
			for(int j = 0;j<puzzle.length;j++)
				if(puzzle[i][j]!=0) {
					temp.add(puzzle[i][j]);
					count++;
				}
			
			if(temp.size()!=count)
			{
				row_solved = false;
				break;
			}
		}
		
		boolean col_solved = true;
		for(int i = 0;i<puzzle.length;i++)
		{
			int count = 0;
			HashSet<Integer> temp = new HashSet<Integer>();
			for(int j = 0;j<puzzle.length;j++)
				if(puzzle[j][i]!=0) {
					temp.add(puzzle[j][i]);
					count++;
				}
			
			if(temp.size()!=count)
			{
				col_solved = false;
				break;
			}
		}
		return row_solved && col_solved;
	}
	
	public static boolean isSolved(int[][] puzzle)
	{
		boolean row_solved = true;
		for(int i = 0;i<puzzle.length;i++)
		{
			HashSet<Integer> temp = new HashSet<Integer>();
			for(int j = 0;j<puzzle.length;j++)
					temp.add(puzzle[i][j]);
			
			if(temp.size()!=puzzle.length)
			{
				row_solved = false;
				break;
			}
		}
		
		boolean col_solved = true;
		for(int i = 0;i<puzzle.length;i++)
		{
			int count = 0;
			HashSet<Integer> temp = new HashSet<Integer>();
			for(int j = 0;j<puzzle.length;j++)
					temp.add(puzzle[j][i]);
			
			if(temp.size()!=puzzle.length)
			{
				col_solved = false;
				break;
			}
		}
		return row_solved && col_solved;
	}
	
}
