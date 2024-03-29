/*
Prepare the Bunnies' Escape
===========================
You're awfully close to destroying the LAMBCHOP doomsday device and freeing Commander Lambda's bunny prisoners, 
but once they're free of the prison blocks, the bunnies are going to need to escape Lambda's space station via 
the escape pods as quickly as possible. 
Unfortunately, the halls of the space station are a maze of corridors and dead ends that will be a deathtrap for 
the escaping bunnies. Fortunately, Commander Lambda has put you in charge of a remodeling project that will give 
you the opportunity to make things a little easier for the bunnies. Unfortunately (again), you can't just remove 
all obstacles between the bunnies and the escape pods - at most you can remove one wall per escape pod path, 
both to maintain structural integrity of the station and to avoid arousing Commander Lambda's suspicions. 
You have maps of parts of the space station, each starting at a prison exit and ending at the door to an escape pod. 
The map is represented as a matrix of 0s and 1s, where 0s are passable space and 1s are impassable walls. 
The door out of the prison is at the top left (0,0) and the door into an escape pod is at the bottom right (w-1,h-1). 
Write a function answer(map) that generates the length of the shortest path from the prison door to the escape pod, 
where you are allowed to remove one wall as part of your remodeling plans. The path length is the total number of 
nodes you pass through, counting both the entrance and exit nodes. The starting and ending positions are always passable (0). 
The map will always be solvable, though you may or may not need to remove a wall. The height and width of the map can
be from 2 to 20. Moves can only be made in cardinal directions; no diagonal moves are allowed.
Languages
=========
To provide a Python solution, edit solution.py
To provide a Java solution, edit solution.java
Test cases
==========
Inputs:
    (int) maze = [[0, 1, 1, 0], [0, 0, 0, 1], [1, 1, 0, 0], [1, 1, 1, 0]]
Output:
    (int) 7
Inputs:
    (int) maze = [[0, 0, 0, 0, 0, 0], [1, 1, 1, 1, 1, 0], [0, 0, 0, 0, 0, 0], [0, 1, 1, 1, 1, 1], [0, 1, 1, 1, 1, 1], [0, 0, 0, 0, 0, 0]]
Output:
    (int) 11
Use verify [file] to test your solution and see how it does. When you are finished editing your code, use submit [file] to submit your answer. If your solution passes the test cases, it will be removed from your home folder.
*/
import java.util.ArrayList;

public class Solution {
    public static int solution(int[][] map) {
		Node[][] nodeMap = new Node[map.length][map[0].length];
        boolean print = true;
		for(int i = 0; i < map.length; i++){
			for(int j = 0; j < map[i].length; j++){
				Node n = new Node(map[i][j], i, j);
				addAdj(nodeMap, n);
				nodeMap[i][j] = n;
				if(print)System.out.print(map[i][j]);
			}
			if(print)System.out.println();
		}
		
		ArrayList<Node> visit = new ArrayList<>();
		Node source = nodeMap[0][0];
		source.dist = 1;
		visit.add(source);
		ArrayList<Node> visited = new ArrayList<>();
		while(!visit.isEmpty()){
			visit.sort((Node a, Node b) -> a.dist - b.dist);
			Node cur = visit.remove(0);
			visited.add(cur);
			cur.visited = true;
			for(Node curAdj : cur.adj){
				if(!curAdj.wall && (curAdj.dist == -1 || curAdj.dist > (cur.dist+1)) && !curAdj.visited){
					curAdj.dist = cur.dist+1;
					visit.add(curAdj);
				}
			}
		}
		while(!visited.isEmpty()){
			Node cur = visited.remove(0);
			cur.visited = false;
			for(Node curAdj : cur.adj){
				if(curAdj.wall && (curAdj.dist == -1 || curAdj.dist > (cur.dist+1))){
					curAdj.dist = cur.dist+1;
					visit.add(curAdj);
				}
			}
		}
		while(!visit.isEmpty()){
			visit.sort((Node a, Node b) -> a.dist - b.dist);
			Node cur = visit.remove(0);
			cur.visited = true;
			for(Node curAdj : cur.adj){
				if(!curAdj.wall && (curAdj.dist == -1 || curAdj.dist > (cur.dist+1)) && !curAdj.visited){
					curAdj.dist = cur.dist+1;
					visit.add(curAdj);
				}
			}
		}
		
		return nodeMap[map.length-1][map[0].length-1].dist;
    }
	
	public static void addAdj(Node[][] nodeMap, Node n){
		if(n.x != 0){
			//System.out.println("beep");
			n.addAdj(nodeMap[n.y][n.x-1]);
		}
		if(n.y != 0){
			//System.out.println("boop");
			n.addAdj(nodeMap[n.y-1][n.x]);
		}
	}
	
	public static class Node{
		ArrayList<Node> adj;
		int dist;
		int x;
		int y;
		boolean wall;
		boolean visited;
		
		public Node(int isWall, int i , int j){
			this.wall = false;
			if(isWall == 1) this.wall = true;
			this.y = i;
			this.x = j;
			this.adj = new ArrayList<Node>();
			this.dist = -1;
			this.visited = false;
		}
		
		public void addAdj(Node n){
			ArrayList<Node> bla = this.adj;
			bla.add(n);
			ArrayList<Node> blb = n.adj;
			blb.add(this);
		}
		
		public String toString(){
			return String.format("(%d,%d)",this.x,this.y);
		}
	}
	public static void main(String[] args){
		int[][] a = {{0, 1, 1, 0}, {0, 0, 0, 1}, {1, 1, 0, 0}, {1, 1, 1, 0}};
		int[][] b = {{0, 0, 0, 0, 0, 0}, {1, 1, 1, 1, 1, 0}, {0, 0, 0, 0, 0, 0}, {0, 1, 1, 1, 1, 1}, {0, 1, 1, 1, 1, 1}, {0, 0, 0, 0, 0, 0}};
		System.out.println(solution(a));
		System.out.println(solution(b));
	}
}