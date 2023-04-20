package test;

import lib.CollisionAvoidance;

public class CollissionAvoidanceTest {

	public static void main(String[] args) {
		byte p[] = new byte[] {90,89,(byte)5,(byte)0,90,(byte)180,(byte)180,(byte)180,(byte)180,(byte)180, 90};
		// TODO Auto-generated method stub
		System.out.println(CollisionAvoidance.validatePosition(p));
	}

}
