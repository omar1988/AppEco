package util;

import java.nio.ByteBuffer;
import java.util.Random;

public class RandomGenerator {
	
	Random r;
	int max = 1000000;
	
	int[] ints = new int[max];
	byte[] b = new byte[4*max];
	int cptInt;
	int cptFloat;
	
	
	public RandomGenerator(){
		r= new Random();
		cptInt=0;
		r.nextBytes(b);
		ByteBuffer.wrap(b).asIntBuffer().get(ints);		
	}

	
	public int nextInt(int max){
		if (cptInt>=max){
			cptInt=r.nextInt(max);
		}
		if(ints[cptInt]<0){
			return (-ints[cptInt++])%max;
		}
		return ints[cptInt++]%max;
	}
	
}
