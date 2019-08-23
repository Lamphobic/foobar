/*
Re-ID
=====

There's some unrest in the minion ranks: minions with ID numbers like "1", "42", and other "good" numbers have been lording it over the poor minions who are stuck with more boring IDs. To quell the unrest, Commander Lambda has tasked you with reassigning everyone new, random IDs based on her Completely Foolproof Scheme. 

She's concatenated the prime numbers in a single long string: "2357111317192329...". Now every minion must draw a number from a hat. That number is the starting index in that string of primes, and the minion's new ID number will be the next five digits in the string. So if a minion draws "3", their ID number will be "71113". 

Help the Commander assign these IDs by writing a function answer(n) which takes in the starting index n of Lambda's string of all primes, and returns the next five digits in the string. Commander Lambda has a lot of minions, so the value of n will always be between 0 and 10000.

Languages
=========

To provide a Python solution, edit solution.py
To provide a Java solution, edit solution.java

Test cases
==========

Inputs:
    (int) n = 0
Output:
    (string) "23571"

Inputs:
    (int) n = 3
Output:
    (string) "71113"


Use verify [file] to test your solution and see how it does. When you are finished editing your code, use submit [file] to submit your answer. If your solution passes the test cases, it will be removed from your home folder.
*/
public class Solution{ 
    public static String solution(int i) {
        System.out.println(i);
        String cat = "2357";
        int s = 11;
        while (cat.length() < i+5){
            if(isPrime(s)){
                cat += s;
            }
            s+=2;
        }
        //System.out.println(cat);
		String soltn = cat.substring(i,i+5);
		System.out.println(soltn);
		System.out.println();
        return soltn;
    }
    
    public static boolean isPrime(int s){
        for(int i = 2; i *i <= s; i++){
            if(s%i == 0){
                return false;
            }
        }
        //System.out.printf("%d is prime\n", s);
        return true;
    }
	public static void main(String[] args){
		solution(0);
		solution(1);
		solution(2);
		solution(3);
		solution(4);
		solution(5);
		solution(6);
		solution(7);
		solution(8);
		solution(9);
		solution(10);
	}
}