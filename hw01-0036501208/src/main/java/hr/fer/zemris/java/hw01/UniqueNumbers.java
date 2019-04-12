package hr.fer.zemris.java.hw01;

import java.util.Scanner;

/**
 * Program that implements binary tree data structure and let's user
 * insert numbers into it.
 * After using the keyword 'kraj', the whole binary tree gets printed sorted
 * by smallest and by largest.
 */
public class UniqueNumbers {
	
	/**
	 * Method that let's user insert numbers into a binary tree.
	 * After insertion, it prints the sorted tree, from smallest to largest and
	 * from largest to smallest.
	 * 
	 * @param args none of the arguments will be used in the calculation.
	 */
	public static void main(String[] args) {
		TreeNode root = null;
		
		Scanner scanner = new Scanner(System.in);
		
		while(true) {
			System.out.println("Unesite broj > ");
			String input = scanner.next();
			
			try {
				int number = Integer.parseInt(input);
				
				if(containsValue(root, number)) {
					System.out.println("Broj već postoji. Preskačem.");
				} else {
					root = addNode(root, number);
					System.out.println("Dodano.");
				}
				
			} catch(NumberFormatException ex) {
				if(input.equals("kraj")) {
					break;
				}
				
				System.out.println("'" + input + "' nije cijeli broj.");
			}
		}
		
		scanner.close();
		
		System.out.print("Ispis od najmanjeg: ");
		printTreeSorted(root, true);
		System.out.println();
		
		System.out.print("Ispis od najveceg: ");
		printTreeSorted(root, false);
	}
	
	/**
	 * Counts the total number of nodes for the given binary tree.
	 * 
	 * @param root root of the given binary tree.
	 * @return total number of nodes in the binary tree.
	 */
	public static int treeSize(TreeNode root) {
		if(root == null) {
			return 0;
		}
		
		return 1 + treeSize(root.left) + treeSize(root.right);
	}
	
	/**
	 * Checks whether the given value is already in the given binary tree.
	 * 
	 * @param root root of the given binary tree.
	 * @param value value that is being checked.
	 * @return {@code true} if found, {@code false} if not.
	 */
	public static boolean containsValue(TreeNode root, int value) {
		if(root == null) {
			return false;
		}
		
		if(root.value == value) {
			return true;
		}
		
		if(value < root.value) {
			return containsValue(root.left, value);
		} else {
			return containsValue(root.right, value);
		}
	}
	
	/**
	 * Adds the given value to the given binary tree, unless the given
	 * value is already present.
	 * 
	 * @param root root of the given binary tree.
	 * @param value value that is being added to the binary tree.
	 * @return returns the reference to the root of the "new" binary tree.
	 */
	public static TreeNode addNode(TreeNode root, int value) {
		if(root == null) {
			TreeNode newNode = new TreeNode();
			newNode.value = value;
			return newNode;
		}
		
		if(value == root.value) {
			return root;
		}
		
		if(value < root.value) {
			root.left = addNode(root.left, value);
		} else {
			root.right = addNode(root.right, value);
		}
		
		return root;
	}
	
	/**
	 * Prints the whole binary tree, from smallest to largest,
	 * in the console.
	 * 
	 * @param root root of the binary tree that is being printed.
	 */
	private static void printSmallestFirst(TreeNode root) {
		if(root.left != null) {
			printSmallestFirst(root.left);
		}
		
		System.out.print(root.value + " ");
		
		if(root.right != null) {
			printSmallestFirst(root.right);
		}
	}
	
	/**
	 * Prints the whole binary tree, from largest to smallest,
	 * in the console.
	 * 
	 * @param root root of the binary tree that is being printed.
	 */
	private static void printLargestFirst(TreeNode root) {
		if(root.right != null) {
			printLargestFirst(root.right);
		}
		
		System.out.print(root.value + " ");
		
		if(root.left != null) {
			printLargestFirst(root.left);
		}
	}
	
	/**
	 * Helper method that checks whether given tree is not empty.
	 * If the given tree is empty, displays error message to the user.
	 * Otherwise, prints the given tree.
	 * 
	 * @param root root of the binary tree that is being checked.
	 * @param smallestFirst if {@code true}, tree will be printed from 
	 * 		  smallest to largest, otherwise reversed.
	 */
	private static void printTreeSorted(TreeNode root, boolean smallestFirst) {
		if(root == null) {
			System.err.println("Can't print an empty tree!");
			return;
		} else {
			if(smallestFirst) {
				printSmallestFirst(root);
			} else {
				printLargestFirst(root);
			}
		}
	}
	
	/**
	 * Tree node, or the smallest element of a binary tree.
	 * Contains a single value and points to another 2 nodes.
	 */
	public static class TreeNode {
		int value;
		TreeNode left;
		TreeNode right;
	}
}
