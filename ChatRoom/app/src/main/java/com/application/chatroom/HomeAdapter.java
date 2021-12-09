package com.application.chatroom;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

import io.github.rockerhieu.emojiconize.Emojiconize;

public class HomeAdapter extends BaseAdapter {

    private LayoutInflater mInflater;
    private Context ctx;
    private List<String> mDatas ;
    public HomeAdapter(Context ctx, List<String> mDatas) {
        mInflater = LayoutInflater.from(ctx);
        this.ctx = ctx;
        this.mDatas = mDatas;
    }

    public void addAll(List<String> mDatas){
        this.mDatas = mDatas;
        notifyDataSetChanged();
    }
    public void clear(){
        this.mDatas.clear();
        notifyDataSetChanged();
    }
    @Override
    public int getCount() {
        return mDatas.size();
    }

    @Override
    public String getItem(int position) {
        return mDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_chatlist, null);
            Emojiconize.view(convertView).go();
            holder = new ViewHolder();
            holder.itemName = (TextView) convertView.findViewById(R.id.itemName);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
                    holder.itemName.setText(mDatas.get(position));
        return convertView;
    }

    private final class ViewHolder {
        TextView itemName;
    }
}