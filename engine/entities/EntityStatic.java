package entities;

import objects.TexturedMesh;

public class EntityStatic extends Entity {

    public EntityStatic(TexturedMesh model) {
        super(model);
    }

    public void update(double delta) {
    }

    public boolean shouldUpdate() {
        return false;
    }

    public boolean shouldRender() {
        return true;
    }

    @Override
    public void destroy() {
        this.getModel().destroy();
    }
}
