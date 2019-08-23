/*
Bringing a Gun to a Guard Fight
===============================

Uh-oh - you've been cornered by one of Commander Lambdas elite guards! Fortunately, you grabbed a beam weapon from an abandoned guard post while you were running through the station, so you have a chance to fight your way out. But the beam weapon is potentially dangerohittables to you as well as to the elite guard: its beams reflect off walls, meaning you'll have to be very careful where you shoot to avoid bouncing a shot toward yourself!

Luckily, the beams can only travel a certain maximum distance before becoming too weak to cahittablese damage. You also know that if a beam hits a corner, it will bounce back in exactly the same direction. And of course, if the beam hits either you or the guard, it will stop immediately (albeit painfully). 

Write a function solution(dimensions, your_position, guard_position, distance) that gives an array of 2 integers of the WIDTH and HEIGHT of the room, an array of 2 integers of your x and y coordinates in the room, an array of 2 integers of the guard's x and y coordinates in the room, and returns an integer of the number of distinct directions that you can fire to hit the elite guard, given the maximum distance that the beam can travel.

The room has integer dimensions [1 < x_dim <= 1250, 1 < y_dim <= 1250]. You and the elite guard are both positioned on the integer lattice at different distinct positions (x, y) inside the room such that [0 < x < x_dim, 0 < y < y_dim]. Finally, the maximum distance that the beam can travel before becoming harmless will be given as an integer 1 < distance <= 10000.

For example, if you and the elite guard were positioned in a room with dimensions [3, 2], your_position [1, 1], guard_position [2, 1], and a maximum shot distance of 4, you could shoot in seven different directions to hit the elite guard (given as vector bearings from your location): [1, 0], [1, 2], [1, -2], [3, 2], [3, -2], [-3, 2], and [-3, -2]. As specific examples, the shot at bearing [1, 0] is the straight line horizontal shot of distance 1, the shot at bearing [-3, -2] bounces off the left wall and then the bottom wall before hitting the elite guard with a total shot distance of sqrt(13), and the shot at bearing [1, 2] bounces off jhittablest the top wall before hitting the elite guard with a total shot distance of sqrt(5).

Languages
=========

To provide a Java solution, edit Solution.java
To provide a Python solution, edit solution.py

Test cases
==========
Your code should pass the following test cases.
Note that it may also be run against hidden test cases not shown here.

-- Java cases -- 
Input:
Solution.solution([3,2], [1,1], [2,1], 4)
Output:
    7

Input:
Solution.solution([300,275], [150,150], [185,100], 500)
Output:
    9

-- Python cases -- 
Input:
solution.solution([3,2], [1,1], [2,1], 4)
Output:
    7

Input:
solution.solution([300,275], [150,150], [185,100], 500)
Output:
    9
*/
/*
//I think this one is way easier than Distract the guards.
//Had to do so much mathy analysis stuff for that one.
//This one on the other hand is pretty straight forwards.
//Only had to remember how to mirror a point over a line and
//figure out a good enough tiling method.
//Then count all non blocked guards in range of me.
//I did still make some stupid mistakes though.
//For a while my code was too slow so I had to do some simple optimizations.
//At one point I had everything but a single test case. After spending some 10+ hours trying to devise
//a test case that failed for a non time-based reason, I caved and found a different solutions code to
//compare answers against.
//And then I learned. I made a monumentally stupid oversight.
//I accidentally assumed that the first guard would always be within LASER_DISTANCE distance.
*/
import java.util.ArrayList;
import java.math.BigInteger;
import java.util.Objects;
import java.util.HashSet;
import java.util.Set;

public class Solution {
	static HashSet<Entity> hittables;
	static int WIDTH;
	static int HEIGHT;
	static int LASER_DISTANCE;
	
    public static int solution(int[] dimensions, int[] your_position, int[] guard_position, int distance) {
		//Seed final variables. (That aren't actaully final becuase java y.)
		WIDTH = dimensions[0];
		HEIGHT = dimensions[1];
		LASER_DISTANCE = distance;
		
		//Start of recentering graph. 
		//
		//get positions of the mirror lines
		int lWall = 0;
		int rWall = WIDTH;
		int dWall = 0;
		int uWall = HEIGHT;
		
		int xShift = your_position[0]; //Not necessary but I do it for readability.
		int yShift = your_position[1];
		
		lWall -= xShift;
		rWall -= xShift;
		dWall -= yShift;
		uWall -= yShift;
		
		guard_position[0] -= xShift;
		guard_position[1] -= yShift;
		
		your_position[0] = 0;
		your_position[1] = 0;
		//
		//End of recentering graph.
		
		hittables = new HashSet<>();
		
		Entity source = new Entity(your_position[0], your_position[1], false);
		Entity guard = new Entity(guard_position[0], guard_position[1], true);
		if(!outOfRange(guard))hittables.add(guard);
		
		tileHorizontal(source, guard, lWall, -1);
		tileHorizontal(source, guard, rWall, 1);
		tileVertical(source, guard, lWall, uWall,1);
		tileVertical(source, guard, lWall, dWall,-1);
		
		int col = 0;
		for(Entity e : hittables){
			if(e.guard){
				col++;
			}
		}
		
		return col;
    }
	
	
	//I've spent a decent amount of time trying to figure out a way to fit both
	//of these functions into one function elegnatly, and I can't. So for now
	//they stay as they are.
	public static void tileHorizontal(Entity tile, Entity guard, int lwall, int direction){
		Entity t = mirrorHorizontal(tile, lwall);
		Entity g = mirrorHorizontal(guard, lwall);
		while(!outOfRange(t) || !outOfRange(g)){
			if(!outOfRange(t)){
				hittables.add(t);
			}
			if(!outOfRange(g)){
				hittables.add(g);
			}
			lwall += WIDTH * direction;
			tile = t;
			guard = g;
			t = mirrorHorizontal(tile, lwall);
			g = mirrorHorizontal(guard, lwall);
		}
	}

	public static void tileVertical(Entity tile, Entity guard, int lwall, int uwall, int direction){
		Entity t = mirrorVertical(tile, uwall);
		Entity g = mirrorVertical(guard, uwall);
		while(!outOfRange(t) || !outOfRange(g)){
			if(!outOfRange(t)){
				hittables.add(t);
			}
			if(!outOfRange(g)){
				hittables.add(g);
			}
			uwall += HEIGHT * direction;
			tileHorizontal(t, g, lwall, -1);
			tileHorizontal(t, g, lwall+WIDTH, 1);
			tile = t;
			guard = g;
			t = mirrorVertical(tile, uwall);
			g = mirrorVertical(guard, uwall);
		}
	}
	
	public static boolean outOfRange(Entity a){
		double distance = (a.x*a.x)+(a.y*a.y);
		return (distance > LASER_DISTANCE*LASER_DISTANCE);
	}
	
	public static Entity mirrorHorizontal(Entity e, int wall){
		return new Entity(2*wall - e.x, e.y, e.guard);
	}
	
	public static Entity mirrorVertical(Entity e, int wall){
		return new Entity(e.x, 2*wall - e.y, e.guard);
	}
	
	/*
	//A lot of my optomization is in this. The core of how my solution works
	//is, I'm guaranteed through my tiling method that the first time I reach
	//an entity on some ray, it will be the closest entity on that ray to
	//the origin. By taking advantage of that, I can get my reduced x and y
	//values, effectively my ray, and have my hashing and equality based off
	//of that. This way I can take advantage of the speed of hashing to
	//determine if I'd seen this slope before. If I have, then I've already
	//recorded the entity I hit along this ray. I still need the original
	//x and y though, because I need to continue tiling along a path. Still
	//with this, I've offloaded checking for collision to an effectively O(1)
	//operation. This, by the way, is the reason I need to tile both the
	//shooter and the guard at the sime time. If I fully tile one before the
	//other, then I can violate the requirement that the first time I see a ray
	//is the closest object along that ray.
	//
	*/
	public static class Entity{
		final int x;
		final int y;
		final int reducedX;
		final int reducedY;
		final int gcd;
		final boolean guard;
		public Entity(int xx, int yy, boolean g){
			this.x = xx;
			this.y = yy;
			this.guard = g;
			if(x == 0 && y == 0){ //This should never happen except for graph center.
				this.reducedX = x;
				this.reducedY = y;
				gcd = 0;
			} else {
				gcd = BigInteger.valueOf(x).gcd(BigInteger.valueOf(y)).intValue();
				this.reducedX = x/gcd;
				this.reducedY = y/gcd;
			}
		}
		
		@Override
		public boolean equals(Object o) {
			if (this == o)
				return true;
			if (o == null)
				return false;
			if (getClass() != o.getClass())
				return false;
			Entity e = (Entity) o;
			return Objects.equals(reducedX, e.reducedX) && Objects.equals(reducedY, e.reducedY);
		}
		
		@Override
		public int hashCode() {
			int result = Integer.hashCode(reducedX);
			result = 31 * result + Integer.hashCode(reducedY);
			return result;
		}
		
		@Override
		public String toString(){ //Now unused but important when I had my print debugs in.
			return String.format("(%d,%d) as (%d,%d) %b", x, y, reducedX, reducedY, guard);
		}
	}
	
	public static void main(String[] args){
		System.out.println(solution(new int[]{3,2},new int[]{1,1},new int[]{2,2},1)); //0 THIS!!!
		//System.out.println(solution(new int[]{3,2},new int[]{1,1},new int[]{2,1},4)); //7
		//System.out.println(solution(new int[]{4,3},new int[]{1,1},new int[]{3,2},5)); //9
		//System.out.println(solution(new int[]{3,2},new int[]{1,1},new int[]{1,2},5)); //5
		//System.out.println(solution(new int[]{2,3},new int[]{1,1},new int[]{2,1},5)); //5
		//System.out.println(solution(new int[]{300,275},new int[]{150,150},new int[]{185,100},500)); //9
		//System.out.println(solution(new int[]{2,5},new int[]{1,2},new int[]{1,4},11)); //27
		//System.out.println(solution(new int[]{42,59},new int[]{34,44},new int[]{6,34},5000)); //30904
		//System.out.println(solution(new int[]{3,2},new int[]{1,1},new int[]{2,1},1000)); //397845
		//System.out.println(solution(new int[]{10,10},new int[]{4,4},new int[]{3,3},5000)); //739323
		//System.out.println(solution(new int[]{23,10},new int[]{6,4},new int[]{3,2},23)); //8
		//System.out.println(solution(new int[]{3,2},new int[]{1,1},new int[]{2,1},5000)); //
	}
}