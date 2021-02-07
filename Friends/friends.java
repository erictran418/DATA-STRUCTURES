package friends;

import java.util.ArrayList;

import structures.Queue;
import structures.Stack;

public class Friends {

	/**
	 * Finds the shortest chain of people from p1 to p2.
	 * Chain is returned as a sequence of names starting with p1,
	 * and ending with p2. Each pair (n1,n2) of consecutive names in
	 * the returned chain is an edge in the graph.
	 * 
	 * @param g Graph for which shortest chain is to be found.
	 * @param p1 Person with whom the chain originates
	 * @param p2 Person at whom the chain terminates
	 * @return The shortest chain from p1 to p2. Null or empty array list if there is no
	 *         path from p1 to p2
	 */
	public static ArrayList<String> shortestChain(Graph g, String p1, String p2) {
		
		/** COMPLETE THIS METHOD **/

		if(g.map.isEmpty()) {
			return null;
		}
		if(p1 == null || p2 == null) {
			return null;
		}
		if(p1.equals(p2)) {
			ArrayList<String> shortest = new ArrayList<String>();
			shortest.add(p1);
			return shortest;
		}
		
		p1 = p1.toLowerCase();
		p2 = p2.toLowerCase();
		
		ArrayList<String> shortest = new ArrayList<String>();
		Queue<Person> q = new Queue<Person>();
		int glength = g.members.length;
		Person[] visitedPerson = new Person[glength];
		boolean[] visited = new boolean[glength];
		int index = g.map.get(p1);
		
		
		
		for (int i = 0; i < visited.length; i++) {
			if (i == index) {
				visited[index] = true;
				continue;
			}
			visited[i] = false;
		}
		
		q.enqueue(g.members[index]);		
		
		while(!q.isEmpty()) {
			Person current = q.dequeue();
			Friend neighbor = current.first;
			
			if(neighbor == null) {
				return null;
			}
			
			int pIndex = g.map.get(current.name);
			visited[pIndex] = true;
			
			while(neighbor != null) {
				int temp = neighbor.fnum;

				if(visited[temp] == false) {
					q.enqueue(g.members[temp]);
					visitedPerson[temp] = current;
					visited[temp] = true;
					
					
					if(g.members[temp].name.equals(p2)) {
						current = g.members[temp];
						
						while(current.name.equals(p1) == false) {
							shortest.add(0, current.name);
							current = visitedPerson[g.map.get(current.name)];
						}
						shortest.add(0,p1);
						return shortest;
					}
				}
				neighbor = neighbor.next;
				
			}
			
			
		}
		
		return null;
		
	}
	
	/**
	 * Finds all cliques of students in a given school.
	 * 
	 * Returns an array list of array lists - each constituent array list contains
	 * the names of all students in a clique.
	 * 
	 * @param g Graph for which cliques are to be found.
	 * @param school Name of school
	 * @return Array list of clique array lists. Null or empty array list if there is no student in the
	 *         given school
	 */
	public static ArrayList<ArrayList<String>> cliques(Graph g, String school) {
		/** COMPLETE THIS METHOD **/
		if(g.map.isEmpty() || school == null) {
			return null;
		}
		
		ArrayList<ArrayList<String>> result = new ArrayList<ArrayList<String>>();
		int glength = g.members.length;
		boolean[] visited = new boolean[glength];
		
		for(int i = 0; i < visited.length; i++) {
			if (g.members[i].school == null || g.members[i].school.equals(school) == false) {
				visited[i] = true;
			}
		}
		
		for(int i = 0; i < visited.length; i++) {
			Queue<Person> q = new Queue<Person>();
			ArrayList<String> resultClique = new ArrayList<String>();


			if(visited[i] == false) {
			//need to check the neighbor of every main
			//all of this is a daisy chain off of main
				
			Person main = g.members[i];
			visited[i] = true;
			q.enqueue(main);
			boolean tbd= true;
			int capacity = 0;
	
			
			while(tbd) {
				Friend neighbor = main.first;
				while(neighbor!= null) {
					if(visited[neighbor.fnum] == false) {
					Person iterator = g.members[neighbor.fnum];
					q.enqueue(iterator);
					visited[neighbor.fnum] = true;
					}
					//if main has only 1 neighbor
					if(neighbor.next == null) {
						main = g.members[neighbor.fnum];
						neighbor = main.first;
						if(capacity == visited.length) {
							tbd = false;
							break;
						}
						capacity++;
						continue;
					}
					if(capacity == visited.length) {
						tbd = false;
						break;
					}
					neighbor = neighbor.next;
					capacity++;
					
				}
				
				
			}
			
			}
			else {
				continue;
			}
			
			//resultoperations here
			
			while(!q.isEmpty()) {
				resultClique.add(q.dequeue().name);
			}
			result.add(resultClique);
		}
		
		
		
		
		return result;
	}
	
	

	
	
	/**
	 * Finds and returns all connectors in the graph.
	 * 
	 * @param g Graph for which connectors needs to be found.
	 * @return Names of all connectors. Null or empty array list if there are no connectors.
	 */
	public static ArrayList<String> connectors(Graph g) {
		/** COMPLETE THIS METHOD **/
		if(g.members.length == 1 || g.members.length < 1) {
			return null;
		}
		
		ArrayList<String> result = new ArrayList<String>();
		int temp = g.members.length;
		int[] prev = new int[temp];
		int[] curr = new int[temp];
		
		Person main = g.members[0];
		Person neighbor = g.members[1];
		
		dfs(g, main, curr, prev, 1, result);
		curr = new int[g.members.length];
		prev = new int[g.members.length];
		dfs(g, neighbor, curr, prev, 1, result);
		
		
		return result;
		
	}
	
	private static int dfs(Graph g, Person current, int[] dfsnum, int[] back, int check, ArrayList<String> result) {
		
		if(dfsnum[g.map.get(current.name)] > 0) {
			return check;
		}
		Friend pntr = current.first;
		
		int currname = g.map.get(current.name);
		dfsnum[currname] = check;
		back[currname] = check;
		check+=1;
		
		while(pntr != null) {
			int a = currname;
			int b = pntr.fnum;
			if(dfsnum[pntr.fnum] > 0) {
				int temp = Math.min(back[a], dfsnum[b]);
				back[a] = temp;
			}
			check = dfs(g, g.members[pntr.fnum], dfsnum, back, check, result);
			
			if(back[b] < dfsnum[a]) {
				int temp = Math.min(back[a], back[b]);
				back[a] = temp;
			}
			else  {
				if(dfsnum[a] > 1) {
					if(!result.contains(current.name)) {
						result.add(current.name);
					}
				}
			}
			pntr = pntr.next;
		}
		
		return check;
	}

}

