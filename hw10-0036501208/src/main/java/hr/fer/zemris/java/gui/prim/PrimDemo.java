package hr.fer.zemris.java.gui.prim;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListModel;
import javax.swing.SwingUtilities;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

/**
 * A simple program that shows the usage of {@code JList}
 * and {@code ListModel} interface. There are 2 lists, each
 * displaying a list of prime numbers. However, those lists
 * use only a single data source which is modeled by a
 * {@code PrimListModel} internal class. 
 *
 * @author Filip Nemec
 */
public class PrimDemo extends JFrame {
	
	/** Used for serialization. */
	private static final long serialVersionUID = 1L;

	/**
	 * Constructs a new frame and initializes the graphical
	 * user interface. The frame consists of 2 lists that
	 * hold a collection of prime numbers and a single button
	 * which allows the user to generate and add the next prime
	 * to both lists at the same time.
	 */
	public PrimDemo() {
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setSize(500, 300);
		setTitle("JList prime number displayer");
		
		// Middle of the screen placement
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		setLocation((screen.width - getSize().width) / 2,
					(screen.height - getSize().height) / 2);
		
		initGUI();
	}
	
	/**
	 * Initializes the graphical user interface that consists
	 * of 2 {@code JList} objects and a single {@code JButton}
	 * used to generate a new prime number to be shown in those
	 * lists.
	 */
	private void initGUI() {
		Container pane = getContentPane();
		
		// Panel with lists
		JPanel listPanel = new JPanel();
		listPanel.setLayout(new GridLayout(1, 2));
		pane.add(listPanel, BorderLayout.CENTER);
		
		PrimListModel model = new PrimListModel();
		
		listPanel.add(new JScrollPane(new JList<>(model)), BorderLayout.CENTER);
		listPanel.add(new JScrollPane(new JList<>(model)), BorderLayout.SOUTH);
		
		// "Next" button
		JButton nextPrimeButton = new JButton("Next prime");
		pane.add(nextPrimeButton, BorderLayout.SOUTH);
		nextPrimeButton.addActionListener(e -> model.next());
	}

	/**
	 * A simple main method which creates a new {@code PrimDemo}
	 * frame in the event-dispatching thread.
	 *
	 * @param args none are used
	 */
	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> new PrimDemo().setVisible(true));
	}
	
	/**
	 * A simple implementation of the {@code ListModel} that has the
	 * capability to generate new prime numbers.
	 *
	 * @author Filip Nemec
	 */
	public static class PrimListModel implements ListModel<Integer> {
		
		/** List of objects that track the changes that happen in this list model. */
		private List<ListDataListener> listeners = new LinkedList<>();
		
		/** A list of all the generated prime numbers. */
		private List<Integer> primeNumbers = new LinkedList<>();
		
		/** The last generated prime number cached for performance. */
		private int currentPrime = 2;
		
		/**
		 * Constructs a new prime list model, initially only with a single
		 * value of 1.
		 */
		public PrimListModel() {
			primeNumbers.add(1);
		}
		
		/**
		 * Finds the next prime number, adds it to the collection and
		 * informs all the listeners of the change.
		 */
		public void next() {
			int intervalStartIndex = getSize();
			
			while(!isPrime(currentPrime))
				currentPrime++;
			
			primeNumbers.add(currentPrime++);
			
			int intervalEndIndex = getSize();
			
			// Inform all the listeners about the newly generated prime
			ListDataEvent event = new ListDataEvent(this, ListDataEvent.INTERVAL_ADDED, intervalStartIndex, intervalEndIndex);
			listeners.forEach(l -> l.intervalAdded(event));
		}
		
		@Override
		public int getSize() {
			return primeNumbers.size();
		}

		@Override
		public Integer getElementAt(int index) {
			return primeNumbers.get(index);
		}

		@Override
		public void addListDataListener(ListDataListener l) {
			Objects.requireNonNull(l, "Listener cannot be null.");
			listeners.add(l);
		}

		@Override
		public void removeListDataListener(ListDataListener l) {
			listeners.remove(l);
		}
		
		/**
		 * Checks if the given number is a prime number.
		 *
		 * @param number number to be evaluated
		 * @return {@code true} if prime, {@code false} otherwise
		 */
		private static boolean isPrime(int number) {
			if(number < 2) return false;
			
			int sqrt = (int) Math.sqrt(number);
			for(int i = 2; i <= sqrt; i++) {
				if(number % i == 0) return false;
			}
			
			return true;
		}
	}
}
