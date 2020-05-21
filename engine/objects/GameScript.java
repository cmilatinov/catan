package objects;

import display.Window;
import main.Scene;

public abstract class GameScript implements GameResource {

    Scene assignedScene;

    State currentState = State.TO_INITIALIZE;

    public enum State {
        TO_START,
        TO_STOP,
        TO_UPDATE,
        TO_INITIALIZE,
        STOPPED
    }

    public void setState(State newState) throws Exception {
        // Can't move backwards into an uninitialized state
        if(newState == State.TO_INITIALIZE) {
            throw new Exception("Can't move into an uninitialized state");
        }
        this.currentState = newState;
    }

    public State getCurrentState()
    {
        return this.currentState;
    }

    public void setContext(Scene scene) {
        this.assignedScene = scene;
    }

    public Scene getScene()
    {
        return this.assignedScene;
    }

    public Window getWindow()
    {
        return this.assignedScene.getWindow();
    }

    public void stop() {};
    public void start() {};
    public void initialize() {};
    public void update(double delta) {};
}
