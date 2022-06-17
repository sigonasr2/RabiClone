package sig.objects.actor;

import sig.engine.objects.Object;

public interface Attachable {
    Object getAttachedObject();
    void setAttachedObject(Object o);
}
