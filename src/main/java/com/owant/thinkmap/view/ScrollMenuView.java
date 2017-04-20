package com.owant.thinkmap.view;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.owant.thinkmap.R;
import com.owant.thinkmap.control.MenuMEventHandler;
import com.owant.thinkmap.util.DensityUtils;

/**
 * Created by owant on 31/03/2017.
 */
public class ScrollMenuView extends ViewGroup {

    private final String TAG = "ScrollMenuView";

    private Context mContext;
    private MenuItemClickListener mMenuItemClickListener;
    private MenuMEventHandler mMenuMEventHandler;

    public ScrollMenuView(Context context) {
        this(context, null, 0);
    }

    public ScrollMenuView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ScrollMenuView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        //测量子View的大小
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View childAt = getChildAt(i);
            measureChild(childAt, widthMeasureSpec, heightMeasureSpec);
        }
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        int width = getMeasuredWidth();
        int height = getMeasuredHeight();

        int dxMenu = 0;

        int count = getChildCount();
        for (int i = 0; i < count; i++) {
            View childView = getChildAt(i);
            if (i == count - 1) {//顶层的子View
                childView.layout(0, 0, width, height);
                if (mMenuMEventHandler == null) {
                    mMenuMEventHandler = new MenuMEventHandler(childView, width, dxMenu);
                    childView.setOnTouchListener(new OnTouchListener() {
                        @Override
                        public boolean onTouch(View v, MotionEvent event) {
                            return mMenuMEventHandler.move(event);
                        }
                    });
                }
            } else {//菜单的View
                View rightView = null;
                if (i != 0) {
                    rightView = getChildAt(i - 1);
                }
                int r = width;
                if (rightView != null) {
                    r = rightView.getLeft();
                }
                childView.layout(r - childView.getMeasuredWidth(), 0, r, height);
                System.out.printf("\n\ti=%s:t=%s,l=%s,r=%s,b=%s\n",
                        i,
                        childView.getTop(),
                        childView.getLeft(),
                        childView.getRight(),
                        childView.getBottom()
                );
                dxMenu = width - childView.getLeft();
            }
        }

    }

    public void addMenuView(final View itemView) {
        itemView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mMenuItemClickListener != null) {
                    mMenuItemClickListener.onClick(itemView, getChildCount());
                }
            }
        });
        ViewGroup.LayoutParams layoutParams = itemView.getLayoutParams();
        if (layoutParams == null) {
            layoutParams = new ViewGroup.LayoutParams(LayoutParams.WRAP_CONTENT,
                    LayoutParams.MATCH_PARENT);
        }
        itemView.setLayoutParams(layoutParams);
        addView(itemView);
    }

    /**
     * 添加菜单的View
     *
     * @param drawableId
     * @param text
     */
    public void addMenuView(int drawableId, String text) {
        final Button menuBtn = new Button(mContext);
        menuBtn.setBackgroundResource(R.drawable.btn_bg);

        Drawable drawable = mContext.getResources().getDrawable(drawableId);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        menuBtn.setCompoundDrawables(null, drawable, null, null);

        int pdTop = DensityUtils.dp2px(mContext, 20);
        int pdLR = DensityUtils.dp2px(mContext, 30);
        menuBtn.setPadding(pdLR, pdTop, pdLR, 0);
        menuBtn.setCompoundDrawablePadding(2);
        menuBtn.setText(text);
        menuBtn.setTextSize(12);
        menuBtn.setGravity(Gravity.CENTER);
        menuBtn.setTextColor(mContext.getResources().getColor(R.color.cornell_red2));
        LayoutParams params = new ViewGroup.LayoutParams(LayoutParams.WRAP_CONTENT,
                LayoutParams.MATCH_PARENT);
        menuBtn.setLayoutParams(params);
        menuBtn.setVisibility(View.VISIBLE);

        menuBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mMenuItemClickListener != null) {
                    mMenuItemClickListener.onClick(menuBtn, getChildCount());
                }
            }
        });
        addView(menuBtn);
    }

    /**
     * 添加上层的View
     * 该方法要在{@link #addMenuView(View)}
     * 之后使用
     *
     * @param body
     */
    public void addMenuTopView(View body) {
        addView(body);
    }

    public void setMenuItemClickListener(MenuItemClickListener menuItemClickListener) {
        mMenuItemClickListener = menuItemClickListener;
    }

    interface MenuItemClickListener {
        void onClick(View view, int index);
    }

}
