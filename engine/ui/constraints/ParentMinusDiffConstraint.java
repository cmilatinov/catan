package ui.constraints;

import ui.UIConstraint;
import ui.UIConstraints;
import ui.UIDimensions;

import static ui.UIDimensions.*;

public class ParentMinusDiffConstraint extends UIConstraint {

    private final int diff;

    public ParentMinusDiffConstraint(int diff) {
        super(UIConstraints.PRIORITY_FIRST);
        this.diff = diff;
    }

    public int compute(UIDimensions parent, UIDimensions computed, int dimension) {
        return switch (dimension) {
            case DIMENSION_WIDTH -> parent.getWidth() - diff;
            case DIMENSION_HEIGHT -> parent.getHeight() - diff;
            default -> 0;
        };
    }

}
