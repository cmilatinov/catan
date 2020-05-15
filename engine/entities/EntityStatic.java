package entities;

import objects.TexturedMesh;

public class EntityStatic extends Entity {

    public EntityStatic(TexturedMesh model) {
        super(model);
    }

    public boolean shouldRender() {
        return true;
    }

    @Override
    public void destroy() {
        this.getModel().destroy();
    }
}
