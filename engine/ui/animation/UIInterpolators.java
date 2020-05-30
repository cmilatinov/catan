package ui.animation;

import static java.lang.Math.cos;
import static java.lang.Math.PI;

public class UIInterpolators {

    public static final UIInterpolator LINEAR = x -> x;

    public static final UIInterpolator EASE_IN = x -> x * x;

    public static final UIInterpolator EASE_OUT = x -> 1 - (x - 1) * (x - 1);

    public static final UIInterpolator EASE_IN_OUT = x -> (1 - (float)cos(PI * x)) / 2;

}
