package com.alexlee1987.smartlrecyclerview.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.alexlee1987.smartlrecyclerview.R;
import com.alexlee1987.smartlrecyclerview.bean.TestBean;
import com.alexlee1987.smartlrecyclerview.util.MakeDataUtil;
import com.alexlee1987.smartrecyclerview.adapter.SmartRecyclerViewAdapter;
import com.alexlee1987.smartrecyclerview.adapter.SmartRecyclerViewHolder;

import java.util.List;

/**
 * 基于SmartRecyclerViewAdapter自定义的线性适配器：可以参考使用方法和注释
 * @author alexlee1987
 * @version 1.0.0
 * @time 2019/09/28
 */
public class LinearAdapter extends SmartRecyclerViewAdapter<TestBean> {
    //以下提供适配器的几种构造使用方式，请选择自己喜欢的【一种】运用方式就可以了
    //方式一：data layoutId都从外部传入，例如：new LinearAdapter(mList, this, R.layout.linear_item)
    //注意layoutIds是表示可变参数，支持传入多个布局，用于支持多item布局，例如：new LinearAdapter(mList,this,R.layout.linear_item，R.layout.linear_item2)
    public LinearAdapter(List<TestBean> data, Context context, int... layoutIds) {
        super(data, context, layoutIds);
    }

    //方式二：layoutIds从外部传入，例如：LinearAdapter adapter = new LinearAdapter(this,R.layout.linear_item)，数据传递用adapter.setListAll(mList);
    public LinearAdapter(Context context, int... layoutIds) {
        super(context, layoutIds);
    }

    //方式三：数据从外部传入，布局直接写在里面，好处把适配器相关的item布局都放在adapter中统一管理
    public LinearAdapter(List<TestBean> mList, Context context) {
        super(mList, context,R.layout.linear_item);//单item布局
        //super(mList, context,R.layout.linear_item,R.layout.linear_item2);//多item布局
    }

    //方式四：布局直接通过在构造方法中设置（推荐使用方式），数据集合通过setListAll设置
    public LinearAdapter(Context context) {
        super(context, R.layout.linear_item);
    }

    //不需要自己再自定义viewHolder类了 库里定义有viewHolder基类 SmartRecyclerViewHolder
    @Override
    protected void smartBindData(SmartRecyclerViewHolder viewHolder, int position, TestBean item) {
        /****1.数据获取方式*****/
        final TestBean testBean = getData(position);

        /****2.view赋值*****/
        //方式一：采用链式的设计的书写方式，一点到尾。（方式一）
        viewHolder.setText(R.id.name, testBean.getName())
                .setImageResource(R.id.avatar_img, MakeDataUtil.getPicId(position))
                // 设置Item中子控件的点击事件
               .setOnClickListener(R.id.avatar_img, new View.OnClickListener() {//点击事件
                   @Override
                   public void onClick(View view) {
                       Toast.makeText(mContext, "我是子控件" + testBean.getName() + "的头像", Toast.LENGTH_LONG).show();
                   }
               });

        //方式二：不采用链式的方式，通过getView直接获取控件对象，不需要强转了，采用的是泛型
        TextView textView =viewHolder.getView(R.id.city);
        textView.setText(testBean.getCity());
    }

    /// 设置item事件监听：在Adapter内部设置设置item事件监听，还有一种方式是在Adapter外部设置，如mAdapter.setOnItemClickListener(new BaseRecyclerViewAdapter.OnItemClickListener<TestBean>(){});，具体的参见LinearActivity设置的LinearAdapter的Item事件监听
    /*@Override
    protected void setListener(SmartRecyclerViewHolder viewHolder, final int position, TestBean item) {
        viewHolder.getItemView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(mContext,"我是Item：" + position,Toast.LENGTH_SHORT).show();
            }
        });
    }*/
}
