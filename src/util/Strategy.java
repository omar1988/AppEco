package util;

public enum Strategy {
	INNOVATOR,MILKER,OPTIMISER,COPYCAT,FLEXIBLE;
	
	/**
	 * 
	 * @return a random strategy
	 */
	public static Strategy getRandom() {
        return values()[(int) (Math.random() * values().length)];
    }
}
