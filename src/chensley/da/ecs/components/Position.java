/**
 * Copyright(C) 2020 Christopher Hensley. 
 * Do as thou wilt shall be the whole of the License. 
 * Love is the License, love under will.
 **/
package chensley.da.ecs.components;

public class Position {
	private final int x;
	private final int y;
	
	public Position(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public Position(Position position) {
		this.x = position.x();
		this.y = position.y();
	}

	public int x() {
		return x;
	}
	
	public int y() {
		return y;
	}
	
	@Override
	public boolean equals(Object object) {
		if(object == null || !object.getClass().equals(this.getClass())) return false;
		
		Position position = (Position)object;
		return position.x() == x && position.y() == y;
	}
}
