package entities;

import objects.TexturedMesh;

public class DynamicEntity extends Entity {

    public DynamicEntity(TexturedMesh model) {
        super(model);
    }

    public void update(double delta) {
    }

    public boolean shouldUpdate() {
        return true;
    }

    public boolean shouldRender() {
        return true;
    }

    @Override
    public void destroy() {
        this.getModel().destroy();
    }
}
