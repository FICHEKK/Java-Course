package hr.fer.zemris.java.hw06.crypto;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class UtilTest {

	@Test
	void invalidStringLengthTest() {
		assertThrows(IllegalArgumentException.class, () -> Util.hexToByte("0"));
		assertThrows(IllegalArgumentException.class, () -> Util.hexToByte("012"));
		assertThrows(IllegalArgumentException.class, () -> Util.hexToByte("012aa"));
	}
	
	@Test
	void invalidHexDigitTest() {
		assertThrows(IllegalArgumentException.class, () -> Util.hexToByte("ZZ"));
		assertThrows(IllegalArgumentException.class, () -> Util.hexToByte("JJJJ"));
		assertThrows(IllegalArgumentException.class, () -> Util.hexToByte("G123"));
	}
	
	@Test
	void hexToByteValidInputTest() {
		byte[] bytes = Util.hexToByte("01aE22");
		
		assertEquals(3, bytes.length);
		assertEquals(1,   bytes[0]);
		assertEquals(-82, bytes[1]);
		assertEquals(34,  bytes[2]);
		
		bytes = Util.hexToByte("0000");
		
		assertEquals(2, bytes.length);
		assertEquals(0, bytes[0]);
		assertEquals(0, bytes[1]);
		
		bytes = Util.hexToByte("ffee");
		
		assertEquals(2, bytes.length);
		assertEquals(-1,  bytes[0]);
		assertEquals(-18, bytes[1]);
	}
	
	@Test
	void byteToHexValidInputTest() {
		byte[] bytes = {0, 1, 127};
		assertEquals("00017f", Util.byteToHex(bytes));
		
		bytes = new byte[] {-1, 2};
		assertEquals("ff02", Util.byteToHex(bytes));
		
		bytes = new byte[] {-23, 100};
		assertEquals("e964", Util.byteToHex(bytes));
	}
	
	@Test
	void emptyByteArrayTest() {
		byte[] bytes = {};
		assertEquals("", Util.byteToHex(bytes));
	}
}
