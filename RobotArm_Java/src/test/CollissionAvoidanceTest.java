package test;

import lib.CollisionAvoidance;

public class CollissionAvoidanceTest {

	public static void main(String[] args) {
		byte p[] = new byte[] {72, 37, 62, (byte)179, 10, (byte)180, (byte)180,(byte) 180, (byte)180, (byte)180, (byte)147, (byte)161};
		// TODO Auto-generated method stub
		System.out.println(CollisionAvoidance.validatePosition(p));
	}
}
