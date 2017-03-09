package com.owant.thinkmap.model;

/**
 * Created by owant on 09/03/2017.
 */

public interface ForTreeItem<T extends NodeModel<?>> {
    void next(int msg, T next);
}
