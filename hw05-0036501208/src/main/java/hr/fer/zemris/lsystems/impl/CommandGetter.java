package hr.fer.zemris.lsystems.impl;

import java.awt.Color;
import java.util.Objects;

import hr.fer.zemris.lsystems.impl.commands.ColorCommand;
import hr.fer.zemris.lsystems.impl.commands.DrawCommand;
import hr.fer.zemris.lsystems.impl.commands.PopCommand;
import hr.fer.zemris.lsystems.impl.commands.PushCommand;
import hr.fer.zemris.lsystems.impl.commands.RotateCommand;
import hr.fer.zemris.lsystems.impl.commands.ScaleCommand;
import hr.fer.zemris.lsystems.impl.commands.SkipCommand;

/**
 * Simple getter class for retrieving a command for the
 * given {@code String} sequence.
 * 
 * @author Filip Nemec
 */
public class CommandGetter {
	
	/**
	 * Returns the corresponding {@code Command} for the
	 * given {@code String} sequence.
	 * 
	 * @param action the input sequence
	 * @return the corresponding {@code Command} 
	 */
	public static Command getCommand(String action) {
		String[] words = WordGetter.getWords(Objects.requireNonNull(action));
		int wordCount = words.length;
		
		if(wordCount == 1) {
			return get1WordCommand(words[0]);
			
		} else if(wordCount == 2) {
			return get2WordCommand(words[0], words[1]);
			
		} else {
			throw new IllegalArgumentException("No commands exist for the provided action.");
			
		}
	}
	
	private static Command get1WordCommand(String action) {
		if(action.equals("push")) {
			return new PushCommand();
			
		} else if(action.equals("pop")) {
			return new PopCommand();
			
		} else {
			throw new IllegalArgumentException("'" + action  + "' is not a valid action.");
			
		}
	}
	
	private static Command get2WordCommand(String action, String value) {
		if(action.equals("color")) {
			return getColorCommand(value);
		}
		
		try {
			double number = Double.parseDouble(value);
			
			if(action.equals("draw")) {
				return new DrawCommand(number);
				
			} else if(action.equals("skip")) {
				return new SkipCommand(number);
				
			} else if(action.equals("scale")) {
				return new ScaleCommand(number);
				
			} else if(action.equals("rotate")) {
				return new RotateCommand(number);
				
			} else {
				throw new IllegalArgumentException("'" + action  + "' is not a valid action.");
				
			}
		} catch(NumberFormatException nfe) {
			throw new IllegalArgumentException("'" + value  + "' could not be parsed to double.");
			
		}
	}
	
	private static Command getColorCommand(String colorString) {
		if(colorString.length() != 6) {
			throw new IllegalArgumentException("Color should be of format \"rrggbb\"");
		}
		
	    Color color =  new Color(Integer.valueOf(colorString.substring(0, 2), 16),
	            				 Integer.valueOf(colorString.substring(2, 4), 16),
	            				 Integer.valueOf(colorString.substring(4, 6), 16));
	    
	    return new ColorCommand(color);
	}
}
