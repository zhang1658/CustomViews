package com.zgq.space.autolayout;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * create by guangqi at 2019/4/4
 * 自动换行的ViewGroup
 */
public class AutoWrapLayout extends ViewGroup {
    List<Integer> lineHeights = new ArrayList<>();
    List<List<View>> lineViews = new ArrayList<>();

    // 竖直方向上，子view是否居中
    private boolean verticalCenter = false;

    public AutoWrapLayout(Context context) {
        this(context, null);
    }

    public AutoWrapLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int measureWidth = MeasureSpec.getSize(widthMeasureSpec);
        int measureHeight = MeasureSpec.getSize(heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int HeightMode = MeasureSpec.getMode(heightMeasureSpec);

        int childCount = getChildCount();
        int maxWidth = 0;
        // 每行的宽度
        int lineWidth = 0;
        // 每行的高度
        int lineHeight = 0;
        // 记录总高度;
        int totalHeight = 0;
        for (int i = 0; i < childCount; i++) {
            View childView = getChildAt(i);
            measureChild(childView, widthMeasureSpec, heightMeasureSpec);
            MarginLayoutParams params = generateMarginLayoutParams(childView);
            int childWidth = childView.getMeasuredWidth() + params.leftMargin + params.rightMargin;
            int childHeight = childView.getMeasuredHeight() + params.topMargin + params.bottomMargin;
            // 如果当前的宽度 + 子view的宽度 大于了父容器的测量宽度，则换行
            if (lineWidth + childWidth > measureWidth) {
                // 换行时计算总高度
                totalHeight += lineHeight;
                // 标记最大的行宽
                maxWidth = Math.max(maxWidth, lineWidth);
                // 重置行高和行宽;
                lineHeight = childHeight;
                lineWidth = childWidth;
            } else {
                lineWidth += childWidth;
                lineHeight = Math.max(lineHeight, childHeight);
            }

            // 最后一行结束的时候，
            if (i == childCount - 1) {
                maxWidth = Math.max(maxWidth, lineWidth);
                totalHeight += lineHeight;
            }
        }
        setMeasuredDimension(widthMode == MeasureSpec.EXACTLY ? measureWidth : maxWidth,
                HeightMode == MeasureSpec.EXACTLY ? measureHeight : totalHeight
        );
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int lineWidth = 0;
        int lineHeight = 0;
        int width = getMeasuredWidth();
        lineHeights.clear();
        lineViews.clear();

        List<View> lineView = new ArrayList<>();

        int childCount = getChildCount();

        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);
            if (child.getVisibility() == GONE) {
                continue;
            }
            MarginLayoutParams params = generateMarginLayoutParams(child);
            int childWidth = params.leftMargin + params.rightMargin + child.getMeasuredWidth();
            int childHeight = params.topMargin + params.bottomMargin + child.getMeasuredHeight();

            if (lineWidth + childWidth > width) {
                lineHeights.add(lineHeight);
                lineViews.add(lineView);
                lineView = new ArrayList<>();
                lineWidth = childWidth;
                lineHeight = childHeight;
            } else {
                lineWidth += childWidth;
                lineHeight = Math.max(lineHeight, childHeight);
            }
            lineView.add(child);

            if (i == childCount - 1) {
                lineHeights.add(lineHeight);
                lineViews.add(lineView);
            }
        }

        int left = 0;
        int top = 0;

        for (int i = 0; i < lineViews.size(); i++) {
            List<View> children = lineViews.get(i);
            int lineH = lineHeights.get(i);
            for (int i1 = 0; i1 < children.size(); i1++) {
                View child = children.get(i1);
                MarginLayoutParams params = generateMarginLayoutParams(child);
                int childWidth = params.leftMargin + params.rightMargin + child.getMeasuredWidth();
                int childHeight = params.topMargin + params.bottomMargin + child.getMeasuredHeight();
                int ml = left + params.leftMargin;
                int mt = verticalCenter ? top + params.topMargin + ((lineH - childHeight) / 2) : top + params.topMargin;
                int mr = ml + child.getMeasuredWidth();
                int mb = mt + child.getMeasuredHeight();
                child.layout(ml, mt, mr, mb);
                left += childWidth;
            }
            top += lineH;
            left = 0;
        }
    }

    private MarginLayoutParams generateMarginLayoutParams(View childView) {
        LayoutParams p = childView.getLayoutParams();
        MarginLayoutParams params;
        if (p instanceof MarginLayoutParams) {
            params = ((MarginLayoutParams) p);
        } else {
            params = new MarginLayoutParams(p);
        }
        return params;
    }

    public void setIsCenterHorizontal(boolean center) {
        this.verticalCenter = center;
    }

    @Override
    protected LayoutParams generateLayoutParams(LayoutParams p) {
        return new MarginLayoutParams(p);
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(getContext(), attrs);
    }

    @Override
    protected LayoutParams generateDefaultLayoutParams() {
        return new MarginLayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
    }
}
