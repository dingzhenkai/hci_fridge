package io.appetizer.hci_fridge.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import io.appetizer.hci_fridge.Model.Foodinfo;
import io.appetizer.hci_fridge.R;


/**
 * Created by user on 2018/5/31.
 */

public class FoodAdapter extends RecyclerView.Adapter<FoodAdapter.ItemViewHolder> {

    private List<Foodinfo> data;
    private LayoutInflater mInflater;
    private OnItemClickListener mOnItemClickListener;

    public FoodAdapter(Context context, List<Foodinfo> objects) {
        this.data = objects;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @SuppressLint("NewApi")
    @Override
    public void onBindViewHolder(final ItemViewHolder itemViewHolder, final int i) {
        Foodinfo food = data.get(i);
        itemViewHolder.foodName.setText(food.getName());
        itemViewHolder.foodNum.setText(food.getNum()+"");
        itemViewHolder.foodImage.setImageResource(food.getImageId());
        if(mOnItemClickListener != null) {
            /**
             * 这里加了判断，itemViewHolder.itemView.hasOnClickListeners()
             * 目的是减少对象的创建，如果已经为view设置了click监听事件,就不用重复设置了
             * 不然每次调用onBindViewHolder方法，都会创建两个监听事件对象，增加了内存的开销
             */
            if(!itemViewHolder.itemView.hasOnClickListeners()) {
                Log.e("ListAdapter", "setOnClickListener");
                itemViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int pos = itemViewHolder.getPosition();
                        mOnItemClickListener.onItemClick(v, pos);
                    }
                });
                itemViewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        int pos = itemViewHolder.getPosition();
                        mOnItemClickListener.onItemLongClick(v, pos);
                        return true;
                    }
                });
            }
        }
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        /**
         * 使用RecyclerView，ViewHolder是可以复用的。这根使用ListView的VIewHolder复用是一样的
         * ViewHolder创建的个数好像是可见item的个数+3
         */
        Log.e("ListAdapter", "onCreateViewHolder");
        ItemViewHolder holder = new ItemViewHolder(mInflater.inflate(
                R.layout.fridge_item, viewGroup, false));
        return holder;
    }

    /**
     * 向指定位置添加元素
     * @param position
     * @param value
     */
    public void add(int position, Foodinfo value) {
        if(position > data.size()) {
            position = data.size();
        }
        if(position < 0) {
            position = 0;
        }
        data.add(position, value);
        /**
         * 使用notifyItemInserted/notifyItemRemoved会有动画效果
         * 而使用notifyDataSetChanged()则没有
         */
        notifyItemInserted(position);
    }

    /**
     * 移除指定位置元素
     * @param position
     * @return
     */
    public String remove(int position) {
        if(position > data.size()-1) {
            return null;
        }
        Foodinfo value = data.remove(position);
        notifyItemRemoved(position);
        return value.toString();
    }


    public void setOnItemClickListener(OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }

    /**
     * 处理item的点击事件和长按事件
     */
    public interface OnItemClickListener {
        public void onItemClick(View view, int position);
        public void onItemLongClick(View view, int position);
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {

        public ImageView foodImage;
        public TextView foodName;
        public TextView foodNum;

        public ItemViewHolder(View itemView) {
            super(itemView);
            foodImage = (ImageView) itemView.findViewById(R.id.icon);
            foodName = (TextView) itemView.findViewById(R.id.label);
            foodNum = (TextView) itemView.findViewById(R.id.num);
        }
    }

}