package ui.constraints;

import ui.UIConstraint;
import ui.UIConstraints;
import ui.UIDimensions;

import static ui.UIDimensions.*;
import static ui.UIDimensions.DIRECTION_NEGATIVE;

public class AbsoluteConstraint extends UIConstraint {
    private final int position;
    private final int direction;

    public AbsoluteConstraint(int position, int direction) {
        super(UIConstraints.PRIORITY_FIRST);
        this.position = position;
        this.direction = direction;
    }

    public AbsoluteConstraint(int position) {
        super(UIConstraints.PRIORITY_FIRST);
        this.position = position;
        this.direction = DIRECTION_NEGATIVE;
    }

    public int compute(UIDimensions parent, UIDimensions computed, int dimension) {
        return switch (dimension) {
            case DIMENSION_X -> direction == UIDimensions.DIRECTION_NEGATIVE ?
                    position - computed.getWidth() / 2 :
                    parent.getRightX() - position - computed.getWidth() / 2;
            case DIMENSION_Y -> direction == UIDimensions.DIRECTION_NEGATIVE ?
                    position - computed.getHeight() / 2 :
                    parent.getBottomY() - position - computed.getHeight() / 2;
            default -> 0;
        };
    }

}
