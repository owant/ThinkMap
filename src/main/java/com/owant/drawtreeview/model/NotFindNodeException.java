package com.owant.drawtreeview.model;

/**
 * Created by owant on 17/12/2016.
 */

public class NotFindNodeException extends NullPointerException {

    public NotFindNodeException(String detailMessage) {
        super(detailMessage);
    }
}
