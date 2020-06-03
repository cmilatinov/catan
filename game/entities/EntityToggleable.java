package entities;

import objects.TexturedMesh;

public abstract class EntityToggleable extends Entity{
    private boolean render;

    public EntityToggleable(TexturedMesh model) {
        super(model);
        render = false;
    }

    public void setRender(boolean render) {
        this.render = render;
    }

    @Override
    public boolean shouldRender() {
        return render;
    }

    @Override
    public abstract void destroy();
}
