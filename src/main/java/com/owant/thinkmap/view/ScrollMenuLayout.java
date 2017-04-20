package com.owant.thinkmap.view;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;

import com.owant.thinkmap.R;
import com.owant.thinkmap.control.MenuMEventHandler;
import com.owant.thinkmap.util.DensityUtils;

/**
 * Created by owant on 31/03/2017.
 */
public class ScrollMenuLayout extends ViewGroup {

    private final String TAG = "ScrollMenuLayout";

    private Context mContext;
    private MenuItemClickListener mMenuItemClickListener;
    private MenuMEventHandler mMenuMEventHandler;
    private boolean target = false;

    public ScrollMenuLayout(Context context) {
        this(context, null, 0);
    }

    public ScrollMenuLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ScrollMenuLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        if (getChildCount() > 2) {
            new IllegalStateException("This ViewGroup only have two child view!");
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View childAt = getChildAt(i);
            measureChild(childAt, widthMeasureSpec, heightMeasureSpec);
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        FrameLayout frameLayout;
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        int width = getMeasuredWidth();
        int height = getMeasuredHeight();

        View menuView = getChildAt(0);
        View contentView = getChildAt(1);

        contentView.layout(100, 0, width, height);

        int dxMenu = 0;
//        if (menuView instanceof ViewGroup) {
//            ViewGroup menuViewGroup = (ViewGroup) menuView;
//            int childCount = menuViewGroup.getChildCount();
//            for (int i = 0; i < childCount; i++) {
//                View menu = menuViewGroup.getChildAt(i);
//                View rightMenu = null;
//
//                int r = width;
//                if (i != 0) {
//                    rightMenu = menuViewGroup.getChildAt(i - 1);
//                    r = rightMenu.getLeft();
//                }
//
//                menu.layout(r - menu.getMeasuredWidth(), 0, r, height);
//                System.out.printf("\n\ti=%s:t=%s,l=%s,r=%s,b=%s\n",
//                        i,
//                        menu.getTop(),
//                        menu.getLeft(),
//                        menu.getRight(),
//                        menu.getBottom()
//                );
//                dxMenu = width - menu.getLeft();
//            }
//        } else {
//            menuView.layout(width - menuView.getMeasuredWidth(), 0, width, height);
//        }

        menuView.layout(0, 0, width, height);

        if (mMenuMEventHandler == null) {
            mMenuMEventHandler = new MenuMEventHandler(contentView, 2000, 300);

            contentView.setOnTouchListener(new OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    return mMenuMEventHandler.move(event);
                }
            });
        }
    }

    public void setMenuItemClickListener(MenuItemClickListener menuItemClickListener) {
        mMenuItemClickListener = menuItemClickListener;
    }

    interface MenuItemClickListener {
        void onClick(View view, int index);
    }

}
