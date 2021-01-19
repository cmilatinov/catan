package ui.constraints;

import ui.UIConstraint;
import ui.UIConstraints;
import ui.UIDimensions;

import static ui.UIDimensions.*;

public class AspectCoverConstraint extends UIConstraint  {

    private final float aspect;

    public AspectCoverConstraint(float aspect) {
        super(UIConstraints.PRIORITY_FIRST);
        this.aspect = aspect;
    }

    public int compute(UIDimensions parent, UIDimensions computed, int dimension) {
        float parentAspect = (float) parent.getWidth() / parent.getHeight();
        return switch (dimension) {
            case DIMENSION_X -> aspect < parentAspect ? 0 : -(computed.getWidth() - parent.getWidth()) / 2;
            case DIMENSION_Y -> aspect > parentAspect ? 0 : - (computed.getHeight() - parent.getHeight()) / 2;
            case DIMENSION_WIDTH -> aspect < parentAspect ? parent.getWidth() : (int) (parent.getHeight() * aspect);
            case DIMENSION_HEIGHT -> aspect > parentAspect ? parent.getHeight() : (int) (parent.getWidth() / aspect);
            default -> 0;
        };
    }

}
