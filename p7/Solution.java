/*
Distract the Guards
===================
The time for the mass escape has come, and you need to distract the guards so that the bunny prisoners can make it out!
Unfortunately for you, they're watching the bunnies closely. Fortunately, this means they haven't realized yet that the
space station is about to explode due to the destruction of the LAMBCHOP doomsday device. Also fortunately, all that
time you spent working as first a minion and then a henchman means that you know the guards are fond of bananas.
And gambling. And thumb wrestling.
The guards, being bored, readily accept your suggestion to play the Banana Games.
You will set up simultaneous thumb wrestling matches. In each match, two guards will pair off to thumb wrestle. The
guard with fewer bananas will bet all their bananas, and the other guard will match the bet. The winner will receive
all of the bet bananas. You don't pair off guards with the same number of bananas (you will see why, shortly). You know
enough guard psychology to know that the one who has more bananas always gets over-confident and loses. Once a match
begins, the pair of guards will continue to thumb wrestle and exchange bananas, until both of them have the same number
of bananas. Once that happens, both of them will lose interest and go back to guarding the prisoners, and you don't want
THAT to happen!
For example, if the two guards that were paired started with 3 and 5 bananas, after the first round of thumb wrestling
they will have 6 and 2 (the one with 3 bananas wins and gets 3 bananas from the loser). After the second round, they
will have 4 and 4 (the one with 6 bananas loses 2 bananas). At that point they stop and get back to guarding.
How is all this useful to distract the guards? Notice that if the guards had started with 1 and 4 bananas, then they
keep thumb wrestling! 1, 4 -> 2, 3 -> 4, 1 -> 3, 2 -> 1, 4 and so on.
Now your plan is clear. You must pair up the guards in such a way that the maximum number of guards go into an infinite
thumb wrestling loop!
Write a function answer(banana_list) which, given a list of positive integers depicting the amount of bananas the each
guard starts with, returns the fewest possible number of guards that will be left to watch the prisoners. Element i of
the list will be the number of bananas that guard i (counting from 0) starts with.
The number of guards will be at least 1 and not more than 100, and the number of bananas each guard starts with will be
a positive integer no more than 1073741823 (i.e. 2^30 -1). Some of them stockpile a LOT of bananas.
Languages
=========
To provide a Python solution, edit solution.py
To provide a Java solution, edit solution.java
Test cases
==========
Inputs:
    (int list) banana_list = [1, 1]
Output:
    (int) 2
Inputs:
    (int list) banana_list = [1, 7, 3, 21, 13, 19]
Output:
    (int) 0
*/
import java.util.Objects;
import java.util.HashSet;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Map;
import java.math.BigInteger;
import java.util.Arrays;

public class Solution {
	/*
	//TODO: Nothing. This one was hard. I need sleep now. And entertainment. I'm not starting the next puzzle tomorrow. Maybe Tuesday.
	//This problem drove me insane, mostly because of stupid mistakes that I could solve by using an IDE. Note to self. Maybe setup an ide before the next problem.
	//Actually commenting this one becasue otherwise there'd be no chance of me ever understanding what I wrote later.
	//
	//EXPLANATION:
	//Ok fam. Here's the basic overview of what happens here.
	//When solution is called instantiate an ArrayList of ArrayLists.
	//This will hold all of the ArrayLists that act as adjacency lists for each node.
	//Oh yeah, nodes. Indices are nodes. In version 1 savings were nodes. 
	//	Note: That didn't work out because multiple guards could have the same savings.
	//	Back to this one.
	//The whole adjLists object is filled with an ArrayList for each index in banana_list.
	//	Note: In hindsight I know how many I'll need so I have made an array of ArrayLists instead but I'm not changing everything now.
	//Fill each adjList with nodes that are adj to it.
	//	Note: Also could have optomized this with j = i+1 and linking both sides as I found them. Oh well.
	//To determine if a node is adj to it, they are sent through newerTest().
	//	Note: That's v3 of testing. Like 2 notebook pages of analysis and horribly written proofs say that it should work.
	//	I'm honestly surprised because I don't actually know why all reduced pairs that sum to a power of 2 converge to a mersenne ratio.
	//  I have proofs for why all mersenne ratios fail to cycle though, so I can live with not knowing, for now.
	//After creating all the asjacency lists we proceed to the matching step.
	//Here we find the node with the shortest adj list and either remove it or match it with another node.
	//We remove it if it has an empty adj list, because it can't possibly be matched with another node.
	//If it has an adj list we match it with the node on it adj list with the shortest adj list.
	//	Note: I have no proof that this actually produces the right answer in every case.
	//	It was the first, simplest matching solution that I could think of that wasn't attempting to brute force it.
	//	It just sorta feels right. If you prioritize matching the nodes with the least candidates, leaving more candidate heavy nodes for later.
	//	I spent a tiny amount of time trying to find a counter example where this wouldn't work, but had little success.
	//	This just happened to work. I'm sure that if I tried I could proove it or disprove it, but for now it rests.
	//After matching we remove both nodes.
	//Whenever a node is removed relevant variables are updated.
	//After this process is repeated till all nodes have been processed, we return the number of nodes that couldn't be matched.
	*/
	
	//public static HashMap<Pair, Boolean> cachedPairs = new HashMap<>(); //Used in conjunction with test().
	
	public static int solution(int[] banana_list) {
		ArrayList<ArrayList<Integer>> adjLists = new ArrayList<>();
		for(int j : banana_list){ //fill adjLists with empty lists
			adjLists.add(new ArrayList<Integer>());
		}
		for(int i = 0; i < banana_list.length; i++){ //fill lists in adjLists with adj nodes
			for(int j = 0; j < banana_list.length; j++){
				Pair p = new Pair(banana_list[i],banana_list[j]);
				if(newerTest(p)){
					adjLists.get(i).add(j);
				}
			}
		}
		
		int distracted = 0; //unused now. used for debugging
		int undistracted = 0; 
		int unprocessed = banana_list.length;
		while(unprocessed > 0){
			int curShortest = shortestAdjList(adjLists); //guard with current shortest adjacency list
			if(adjLists.get(curShortest).size() == 0){
				removeReferences(adjLists, curShortest);
				undistracted++;
				unprocessed--;
			} else {
				int shortestAdj = shortestFromAdj(adjLists, adjLists.get(curShortest)); //guard adj to current with shortest adjacency list
				removeReferences(adjLists, curShortest);
				removeReferences(adjLists, shortestAdj);
				distracted++;
				distracted++;
				unprocessed--;
				unprocessed--;
			}
		}
        return undistracted;
    }
	
	//Prints information about my lists.
	//Useful for debugging.
	public static void printLists(ArrayList<ArrayList<Integer>> adjLists, int[] banana_list){
		for(int i = 0; i < adjLists.size(); i++){
			System.out.printf("Location %d\tId %d\tValues %s\t",i,banana_list[i],adjLists.get(i));
			if(adjLists.get(i) != null){
				System.out.printf("Size %d",adjLists.get(i).size());
			}
			System.out.println();
		}
	}
	
	//Removes references to and from a node.
	//
	public static void removeReferences(ArrayList<ArrayList<Integer>> adjLists, int adjListLocation){
		for(int reference : adjLists.get(adjListLocation)){ //references exist on both sides lists. need to remove references to this object from the other side
			adjLists.get(reference).remove(Integer.valueOf(adjListLocation));
		}
		adjLists.set(adjListLocation, null);
	}
	
	//Self descriptive name IMO.
	//Used to determine the node with the shortest adjacency list from all nodes
	public static int shortestAdjList(ArrayList<ArrayList<Integer>> adjLists){
		int size = 101; //this should really be Integer.MAX_VALUE
		int ret = -1;
		for(int i = 0; i < adjLists.size(); i++){
			if(adjLists.get(i) != null && adjLists.get(i).size() < size){
				ret = i;
				size = adjLists.get(i).size();
			}
		}
		return ret;
	}
	
	//Self descriptive name IMO.
	//Used to determine the node with the shortest adjacency list from nodes on an adjacency list
	public static int shortestFromAdj(ArrayList<ArrayList<Integer>> adjLists, ArrayList<Integer> adjList){
		int size = 101; //this should also really be Integer.MAX_VALUE
		int ret = -1;
		for(int i = 0; i < adjList.size(); i++){//i is the location in adjList not adjLists
			if(adjLists.get(adjList.get(i)) != null && adjLists.get(adjList.get(i)).size() < size){
				ret = adjList.get(i);
				size = adjLists.get(adjList.get(i)).size();
			}
		}
		return ret;
	}
	
	
	/*
	//Made with guesswork and luck. Not enough luck though.
	//Was significantly faster than test, but was wrong on a good chunk of test cases.
	//Still not entirely sure why this worked as well as it did.
	
	public static boolean newTest(Pair p){ //because the old one was too slow. 100% luck and guesswork. Don't know why it works. Ask OEIS A000225
		BigInteger a = new BigInteger("" + p.getA());
		BigInteger b = new BigInteger("" + p.getB());
		BigInteger two = new BigInteger("2");
		
		return two.modPow(b,a.add(b)).compareTo(BigInteger.ZERO) != 0;
	}
	*/
	
	//So pairs of the form (1,((2^n)+1)) fail.
	//Note that multiples of this should fail as well. ex. (2, 2((2^n)+1))
	//The sum of a mersenne number and 1 will be a power of two.
	//So I just need to check the sum of my reduced numbers to see if it's a power of 2.
	public static boolean newerTest(Pair p){ 
		int s = p.sum()/p.gcd();
		return (s & (s-1)) != 0; //bitwise and. 2^n and (2^n)-1 produces 0. All else not.
	}
	
	
	/*
	//Old testing code. No longer usefull except to test newer tests against. Slow but accurate.
	
	public static boolean test(Pair p){ //too slow. useless. begone.
		if(cachedPairs.containsKey(p)) return cachedPairs.get(p);
		HashSet<Pair> traversedPairs = new HashSet<Pair>();
		//System.out.println(p);
		while(!p.dead() && !traversedPairs.contains(p) && !cachedPairs.containsKey(p)){
			traversedPairs.add(p); //add each pair after it's checked
			p = new Pair(p.getA()*2, p.getB()-p.getA());
			//System.out.println(p);
		}
		boolean cycles = false; //Assume it's dead. I could assume any. I just randomly chose this.
		if(cachedPairs.containsKey(p)){
			cycles = cachedPairs.get(p);
		}
		if(traversedPairs.contains(p)){
			cycles = true;
		}
		for(Pair q : traversedPairs){
			cachedPairs.put(q, cycles);
		}
		return cycles;
	}
	*/
	
	public static class Pair{
		private int a;
		private int b;
		private int gcd;
		public Pair(int aa, int bb){
			updatePair(aa,bb);
			//System.out.println(this);
		}
		
		private void updatePair(int aa, int bb){
			//System.out.printf("%d,%d",aa,bb);
			int min = Math.min(aa,bb);
			int max = Math.max(aa,bb);
			a = min;
			b = max;
			//gcd = BigInteger.valueOf(a).gcd(BigInteger.valueOf(b)).intValue();
			//System.out.println(this);
			//a = a / gcd;
			//b = b / gcd;
		}
		
		private int gcd(){ 
			return BigInteger.valueOf(a).gcd(BigInteger.valueOf(b)).intValue();
		}
		
		public boolean dead(){
			if(a == b) return true;
			return false;
		}
		
		public int getA(){
			return a;
		}
		
		public int getB(){
			return b;
		}
		
		public int sum(){
			return a+b;
		}
		
		@Override
		public boolean equals(Object o) {
			if (this == o)
				return true;
			if (o == null)
				return false;
			if (getClass() != o.getClass())
				return false;
			Pair p = (Pair) o;
			return Objects.equals(a, p.a) && Objects.equals(b, p.b);
		}
		
		@Override
		public int hashCode() {
			int result = Integer.hashCode(a);
			result = 31 * result + Integer.hashCode(b);
			return result;
		}
		
		@Override
		public String toString(){
			return String.format("(%d,%d) %d", a, b, gcd);
		}
	}
	
	public static void main(String[] args){
		//I'm leaving my testing code here.
		//This most of what I used to test solutions.
		
		final int size = 50;
		System.out.println(solution(new int[]{1,1}));
		System.out.println(solution(new int[]{1, 7, 3, 21, 13, 19}));
		System.out.println(solution(new int[]{1, 7, 3, 21, 13, 19, 2, 1}));
		System.out.println(solution(new int[]{1, 3}));
		int[] tes = new int[size];
		for(int n = 0; n < size; n++){
			tes[n] = (int)((Math.random()*1000000000)+1000000);
		}
		System.out.println(solution(tes));
		
		/*
		for(int i = 1; i < 101; i++){
			Pair p = new Pair(1,i);
			if(!test(p)){
				System.out.println(p);
			}
		}
		for(int i = 0; i < 100000; i++){
			//System.out.println(i);
			Pair p = new Pair((int)((Math.random()*10000)+1),(int)((Math.random()*10000)+1));
			if(newerTest(p) != test(p))System.out.println(p);
		}
		for(int i = 1; i < 16; i++){
			for(int j = i+1; j < 16; j++){
				//System.out.printf("loop %d,%d\n",i,j);
				Pair p = new Pair(i,j);
				if(newerTest(p) != test(p))System.out.println(p);
			}
		}
		Pair q = new Pair(5,3);
		System.out.printf("%s %b\n\n", q, newerTest(q));
		
		Pair q = new Pair(1,7);
		Pair w = new Pair(1,3);
		Pair e = new Pair(1,21);
		Pair r = new Pair(1,13);
		Pair t = new Pair(1,19);
		Pair y = new Pair(7,3);
		Pair u = new Pair(7,21);
		Pair i = new Pair(7,13);
		Pair o = new Pair(7,19);
		Pair p = new Pair(3,21);
		Pair a = new Pair(3,13);
		Pair s = new Pair(3,19);
		Pair d = new Pair(21,13);
		Pair f = new Pair(21,19);
		Pair g = new Pair(13,19);
		
		System.out.printf("%s %b\n\n", q, newerTest(q));
		System.out.printf("%s %b\n\n", w, newerTest(w));
		System.out.printf("%s %b\n\n", e, newerTest(e));
		System.out.printf("%s %b\n\n", r, newerTest(r));
		System.out.printf("%s %b\n\n", t, newerTest(t));
		System.out.printf("%s %b\n\n", y, newerTest(y));
		System.out.printf("%s %b\n\n", u, newerTest(u));
		System.out.printf("%s %b\n\n", i, newerTest(i));
		System.out.printf("%s %b\n\n", o, newerTest(o));
		System.out.printf("%s %b\n\n", p, newerTest(p));
		System.out.printf("%s %b\n\n", a, newerTest(a));
		System.out.printf("%s %b\n\n", s, newerTest(s));
		System.out.printf("%s %b\n\n", d, newerTest(d));
		System.out.printf("%s %b\n\n", f, newerTest(f));
		System.out.printf("%s %b\n\n", g, newerTest(g));
		*/
		
	}
}