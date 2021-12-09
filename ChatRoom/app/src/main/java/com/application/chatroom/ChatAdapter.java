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

public class ChatAdapter extends BaseAdapter {

    private LayoutInflater mInflater;
    private Context ctx;
    private List<ChatEntity> mDatas ;
    public ChatAdapter(Context ctx, List<ChatEntity> mDatas) {
        mInflater = LayoutInflater.from(ctx);
        this.ctx = ctx;
        this.mDatas = mDatas;
    }

    public void addAll(List<ChatEntity> mDatas){
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
    public ChatEntity getItem(int position) {
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
            convertView = mInflater.inflate(R.layout.item_chat, null);
            Emojiconize.view(convertView).go();
            holder = new ViewHolder();
            holder.text1 = (TextView) convertView.findViewById(R.id.text1);
            holder.text3 = (TextView) convertView.findViewById(R.id.text3);
            holder.textname1 = (TextView) convertView.findViewById(R.id.textname1);
            holder.textname2 = (TextView) convertView.findViewById(R.id.textname2);
            holder.ll1 = (LinearLayout) convertView.findViewById(R.id.ll1);
            holder.ll2 = (LinearLayout) convertView.findViewById(R.id.ll2);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if (mDatas.get(position).getWhosend().equals(FirebaseAuth.getInstance().getCurrentUser().getEmail())){
            holder.ll1.setVisibility(View.GONE);
            holder.ll2.setVisibility(View.VISIBLE);

                    holder.text3.setVisibility(View.VISIBLE);
                    holder.text3.setText(mDatas.get(position).getContent());
                    holder.textname2.setText(mDatas.get(position).getSendname());


        }else {
            holder.ll2.setVisibility(View.GONE);
            holder.ll1.setVisibility(View.VISIBLE);
                holder.text1.setVisibility(View.VISIBLE);
                holder.text1.setText(mDatas.get(position).getContent());
                holder.textname1.setText(mDatas.get(position).getSendname());
        }
        return convertView;
    }

    private final class ViewHolder {
        TextView text1;
        TextView text3;
        TextView textname1;
        TextView textname2;
        LinearLayout ll1;
        LinearLayout ll2;
    }
}