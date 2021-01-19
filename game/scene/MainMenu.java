package scene;

import main.Scene;

public class MainMenu extends Scene {

    @Override
    public void initialize() {
        this.register(new scripts.MainMenu());
    }
}
