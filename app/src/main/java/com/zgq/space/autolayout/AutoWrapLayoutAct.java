package com.zgq.space.autolayout;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zgq.space.R;

import java.util.ArrayList;
import java.util.List;

/**
 * create by guangqi at 2019/4/4
 */
public class AutoWrapLayoutAct extends AppCompatActivity {
    AutoWrapLayout mAutoWrapLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAutoWrapLayout = new AutoWrapLayout(this);
        setContentView(mAutoWrapLayout);

        List<String> data = getData();
        for (String s : data) {
            TextView tv = new TextView(this);
            tv.setText(s);
            tv.setTextColor(Color.WHITE);
            tv.setBackgroundResource(R.drawable.shape_bg);
            tv.setPadding(5, 0, 5, 0);
            ViewGroup.MarginLayoutParams params = new ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.setMargins(5, 5, 5, 5);
            mAutoWrapLayout.addView(tv, params);
        }
    }

    private List<String> getData() {
        List<String> list = new ArrayList<>();
        list.add("美食");
        list.add("动物");
        list.add("明星");
        list.add("这就是命");
        list.add("娱乐头条看今天");
        list.add("职位");
        list.add("科技公司的命运");
        list.add("如果有一天你变的很有钱");
        list.add("三农");
        list.add("山东卫视");
        list.add("红色");
        list.add("卫视");
        list.add("请选择");
        list.add("谷歌、领英、等顶尖公司推荐高级有效的秘密");
        list.add("好");
        return list;
    }
}
