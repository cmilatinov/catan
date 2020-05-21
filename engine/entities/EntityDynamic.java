package entities;

import objects.TexturedMesh;

public class EntityDynamic extends Entity {

    public EntityDynamic(TexturedMesh model) {
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
