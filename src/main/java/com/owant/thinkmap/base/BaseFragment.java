package com.owant.thinkmap.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

/**
 * Created by owant on 22/02/2017.
 */

public abstract class BaseFragment extends Fragment {

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        onBaseBindView();
    }

    protected abstract void onBaseBindView();
}
