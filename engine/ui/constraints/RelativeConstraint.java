package ui.constraints;

import static ui.UIDimensions.DIMENSION_X;
import static ui.UIDimensions.DIMENSION_Y;
import static ui.UIDimensions.DIMENSION_HEIGHT;
import static ui.UIDimensions.DIMENSION_WIDTH;
import static ui.UIDimensions.DIRECTION_NEGATIVE;

import ui.UIConstraint;
import ui.UIConstraints;
import ui.UIDimensions;

public class RelativeConstraint extends UIConstraint {
	
	private final float size;
	private final int dir;
	
	public RelativeConstraint(float size) {
		super(UIConstraints.PRIORITY_FIRST);
		this.size = size;
		this.dir = DIRECTION_NEGATIVE;
	}
	
	public RelativeConstraint(float size, int direction) {
		super(UIConstraints.PRIORITY_FIRST);
		this.size = size;
		this.dir = direction;
	}

	public int compute(UIDimensions parent, UIDimensions computed, int dimension) {
		switch(dimension) {
			case DIMENSION_WIDTH:
				return Math.round(parent.getWidth() * size);
				
			case DIMENSION_HEIGHT:
				return Math.round(parent.getHeight() * size);
				
			case DIMENSION_X:
				return dir == DIRECTION_NEGATIVE ? 
						parent.getLeftX() + Math.round(parent.getWidth() * size) : 
						parent.getRightX() - Math.round(parent.getWidth() * size) - computed.getWidth();
						
			case DIMENSION_Y:
				return dir == DIRECTION_NEGATIVE ? 
						parent.getTopY() + Math.round(parent.getHeight() * size) : 
						parent.getBottomY() - Math.round(parent.getHeight() * size) - computed.getHeight();
						
			default: 
				return 0;
		}
	}
	
}
