/**
 * Copyright(C) 2020 Christopher Hensley. 
 * Do as thou wilt shall be the whole of the License. 
 * Love is the License, love under will.
 **/
package chensley.da.ecs.components;

import java.util.Map;

/**
 * Artificial intelligence component
 */
public class AI {
	//Current state of the machine
	private String state;
	
	//Maps states to game messages
	private final Map<String, String> machine;
	
	/**
	 * Constructor
	 * @param state
	 * 		Initial state
	 * @param machine
	 * 		State machine
	 */
	public AI(String state, Map<String, String> machine) {
		this.state = state;
		this.machine = machine;
	}
	
	public AI(AI ai) {
		this.state = ai.state;
		this.machine = ai.machine;
	}
	
	public String state() { return state; }
	public String msg() { return machine.get(state); }
	public void setState(String state) { this.state = state; }
}
