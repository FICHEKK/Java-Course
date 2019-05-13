package hr.fer.zemris.java.gui.layouts;

import static org.junit.jupiter.api.Assertions.*;

import java.awt.Dimension;

import javax.swing.JLabel;
import javax.swing.JPanel;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CalcLayoutTest {
	
	private JPanel p;
	
	@BeforeEach
	void init() {
		p = new JPanel(new CalcLayout(2));
	}

	@Test
	void expectedDimensionTest() {
		JLabel l1 = new JLabel(""); l1.setPreferredSize(new Dimension(10,30));
		JLabel l2 = new JLabel(""); l2.setPreferredSize(new Dimension(20,15));
		p.add(l1, new RCPosition(2,2));
		p.add(l2, new RCPosition(3,3));
		Dimension dim = p.getPreferredSize();
		
		assertEquals(152, dim.width);
		assertEquals(158, dim.height);
	}
	
	@Test
	void expectedDimensionTest2() {
		JLabel l1 = new JLabel(""); l1.setPreferredSize(new Dimension(108,15));
		JLabel l2 = new JLabel(""); l2.setPreferredSize(new Dimension(16,30));
		p.add(l1, new RCPosition(1,1));
		p.add(l2, new RCPosition(3,3));
		Dimension dim = p.getPreferredSize();
		
		assertEquals(152, dim.width);
		assertEquals(158, dim.height);
	}

	
	@Test
	void invalidRowIndexTest() {
		assertThrows(CalcLayoutException.class, () -> p.add(new JLabel(""), new RCPosition(0, 6)));
		assertThrows(CalcLayoutException.class, () -> p.add(new JLabel(""), new RCPosition(6, 6)));
		
		assertDoesNotThrow(() -> p.add(new JLabel(""), new RCPosition(1, 6)));
		assertDoesNotThrow(() -> p.add(new JLabel(""), new RCPosition(5, 6)));
	}
	
	@Test
	void invalidColumnIndexTest() {
		assertThrows(CalcLayoutException.class, () -> p.add(new JLabel(""), new RCPosition(3, 0)));
		assertThrows(CalcLayoutException.class, () -> p.add(new JLabel(""), new RCPosition(3, 8)));
		
		assertDoesNotThrow(() -> p.add(new JLabel(""), new RCPosition(3, 1)));
		assertDoesNotThrow(() -> p.add(new JLabel(""), new RCPosition(3, 7)));
	}
	
	@Test
	void invalidCalculatorDisplayIndexTest() {
		assertThrows(CalcLayoutException.class, () -> p.add(new JLabel(""), new RCPosition(1, 2)));
		assertThrows(CalcLayoutException.class, () -> p.add(new JLabel(""), new RCPosition(1, 5)));
		
		assertDoesNotThrow(() -> p.add(new JLabel(""), new RCPosition(1, 6)));
	}
	
	@Test
	void multipleComponentsSameSpotTest() {
		JLabel label1 = new JLabel("");
		JLabel label2 = new JLabel("");
		RCPosition position = new RCPosition(3, 3);
		
		p.add(label1, position);
		assertThrows(CalcLayoutException.class, () -> p.add(label2, position));
	}
}
