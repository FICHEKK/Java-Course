package hr.fer.zemris.lsystems.impl;

import java.awt.Color;
import java.util.Objects;

import hr.fer.zemris.java.custom.collections.Dictionary;
import hr.fer.zemris.lsystems.LSystem;
import hr.fer.zemris.lsystems.LSystemBuilder;
import hr.fer.zemris.lsystems.Painter;
import hr.fer.zemris.lsystems.impl.commands.processors.AngleCommandProcessor;
import hr.fer.zemris.lsystems.impl.commands.processors.AxiomCommandProcessor;
import hr.fer.zemris.lsystems.impl.commands.processors.CommandCommandProcessor;
import hr.fer.zemris.lsystems.impl.commands.processors.CommandProcessor;
import hr.fer.zemris.lsystems.impl.commands.processors.OriginCommandProcessor;
import hr.fer.zemris.lsystems.impl.commands.processors.ProductionCommandProcessor;
import hr.fer.zemris.lsystems.impl.commands.processors.UnitLengthCommandProcessor;
import hr.fer.zemris.lsystems.impl.commands.processors.UnitLengthDegreeScalerCommandProcessor;
import hr.fer.zemris.math.Vector2D;

/**
 * Builder class that is used to create L-Systems. It
 * provides numerous methods for setting the L-System
 * up before calling a building method which will return
 * the before configured L-System.
 * 
 * @author Filip Nemec
 */
public class LSystemBuilderImpl implements LSystemBuilder {
	
	/** The length of a single forward movement. */
	private double unitLength = 0.1;
	
	/** Scales the {@code #unitLength} by this amount. */
	private double unitLengthDegreeScaler = 1.0;
	
	/** The starting point. */
	private Vector2D origin = new Vector2D(0, 0);
	
	/** The starting angle */
	private double angle = 0;
	
	/** The starting sequence. */
	private String axiom = "";
	
	/** The default turtle's color. */
	private static final Color DEFAULT_COLOR = Color.BLACK;
	
	/**
	 * Maps the given character to its production sequence.
	 */
	private Dictionary<Character, String> productions = new Dictionary<>();
	
	/**
	 * Maps the given character to its command.
	 */
	private Dictionary<Character, Command> commands = new Dictionary<>();
	
	/**
	 * Maps the given keyword to the appropriate command processor.
	 */
	private static Dictionary<String, CommandProcessor> processors;
	
	/**
	 * Pair all of the possible keywords to its corresponding processors.
	 */
	static {
		processors = new Dictionary<String, CommandProcessor>();
		
		processors.put("origin", 				 new OriginCommandProcessor());
		processors.put("angle", 				 new AngleCommandProcessor());
		processors.put("unitLength", 			 new UnitLengthCommandProcessor());
		processors.put("unitLengthDegreeScaler", new UnitLengthDegreeScalerCommandProcessor());
		processors.put("axiom", 				 new AxiomCommandProcessor());
		processors.put("production",			 new ProductionCommandProcessor());
		processors.put("command",			 	 new CommandCommandProcessor());	
	}
	
	@Override
	public LSystem build() {
		return new LSystemImpl();
	}

	@Override
	public LSystemBuilder configureFromText(String[] lines) {
		Objects.requireNonNull(lines);
		
		for(String sequence : lines) {
			if(sequence.isBlank()) continue;
			
			String[] words = WordGetter.getWords(sequence);
			String keyword = words[0];
			
			CommandProcessor processor = processors.get(keyword);
			
			if(processor != null) {
				processor.process(this, words);
			} else {
				throw new IllegalArgumentException("'" + keyword + "' is not a valid configuration command.");
			}
		}
		
		return this;
	}

	@Override
	public LSystemBuilder registerCommand(char symbol, String action) {
		Objects.requireNonNull(action);
		commands.put(symbol, CommandGetter.getCommand(action));
		return this;
	}

	@Override
	public LSystemBuilder registerProduction(char symbol, String production) {
		Objects.requireNonNull(production);
		productions.put(symbol, production);
		return this;
	}
	
	//======================================================
	//						SETTERS
	//======================================================
	@Override
	public LSystemBuilder setAngle(double angle) {
		this.angle = angle;
		return this;
	}

	@Override
	public LSystemBuilder setAxiom(String axiom) {
		Objects.requireNonNull(axiom);
		this.axiom = axiom;
		return this;
	}

	@Override
	public LSystemBuilder setOrigin(double x, double y) {
		this.origin = new Vector2D(x, y);
		return this;
	}

	@Override
	public LSystemBuilder setUnitLength(double unitLength) {
		this.unitLength = unitLength;
		return this;
	}

	@Override
	public LSystemBuilder setUnitLengthDegreeScaler(double unitLengthDegreeScaler) {
		this.unitLengthDegreeScaler = unitLengthDegreeScaler;
		return this;
	}
	
	private class LSystemImpl implements LSystem {
		
		@Override
		public void draw(int level, Painter painter) {
			Context context = new Context();
			
			Vector2D start = origin.copy();
			Vector2D direction = new Vector2D(1, 0).rotated(Math.toRadians(angle));
			double stepLength = unitLength * Math.pow(unitLengthDegreeScaler, level);
			
			context.pushState(new TurtleState(start, direction, DEFAULT_COLOR, stepLength));
			
			char[] sequence = generate(level).toCharArray();
			
			for(int i = 0, length = sequence.length; i < length; i++) {
				Command command = commands.get(sequence[i]);
				
				if(command != null) {
					command.execute(context, painter);
				}
			}
		}

		@Override
		public String generate(int level) {
			String sequence = axiom;
			
			for(int i = 0; i < level; i++) {
				var sb = new StringBuilder();
				
				for(int j = 0; j < sequence.length(); j++) {
					char current = sequence.charAt(j);
					String transformation = productions.get(current);
					
					if(transformation != null) {
						sb.append(transformation);
					} else {
						sb.append(current);
					}
				}
				
				sequence = sb.toString();
			}

			return sequence;
		}
	}
}
