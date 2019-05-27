package hr.fer.zemris.java.custom.scripting.exec;

/**
 * Simple class that holds the specific operations.
 *
 * @author Filip Nemec
 */
class Operations  {
	
	/**
	 * Addition operation.
	 */
	public static final Operation ADD = new Operation() {
		
		@Override
		public int asInt(int i1, int i2) {
			return i1 + i2;
		}
		
		@Override
		public double asDouble(double d1, double d2) {
			return d1 + d2;
		}
	};
	
	/**
	 * Subtraction operation.
	 */
	public static final Operation SUB = new Operation() {
		
		@Override
		public int asInt(int i1, int i2) {
			return i1 - i2;
		}
		
		@Override
		public double asDouble(double d1, double d2) {
			return d1 - d2;
		}
	};
	
	/**
	 * Multiplication operation.
	 */
	public static final Operation MUL = new Operation() {
		
		@Override
		public int asInt(int i1, int i2) {
			return i1 * i2;
		}
		
		@Override
		public double asDouble(double d1, double d2) {
			return d1 * d2;
		}
	};
	
	/**
	 * Division operation.
	 */
	public static final Operation DIV = new Operation() {
		
		@Override
		public int asInt(int i1, int i2) {
			if(i2 == 0)
				throw new ArithmeticException("Can't divide by zero.");
			return i1 / i2;
		}
		
		@Override
		public double asDouble(double d1, double d2) {
			if(d2 == 0)
				throw new ArithmeticException("Can't divide by zero.");
			return d1 / d2;
		}
	};
}
