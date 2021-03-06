package com.idbd.admin.myapplication.adapter;

import android.app.Activity;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.idbd.admin.myapplication.Gr_info_Activity_;
import com.idbd.admin.myapplication.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2017-01-30.
 */

public class ChatAdapter extends RecyclerView.Adapter <ChatAdapter.ViewHolder>  {

    public static ChatAdapter adapter;

    private Activity a;
    private List<JSONObject> mList= new ArrayList();

    public ChatAdapter(Cursor cursor , Activity a){
        this.a = a;
        cursor.moveToFirst();
        try {
            for(int i = 0 ; i<cursor.getCount() ; i++){
                JSONObject jsonObject = new JSONObject();
                    jsonObject.put("content" , cursor.getString(cursor.getColumnIndex("content")));
                    String name = "알수없음";
                    for(int j = 0 ; j<Gr_info_Activity_.user_list.length() ; j++){
                        JSONObject userObject = Gr_info_Activity_.user_list.getJSONObject(j);
                        if(userObject.getInt("uid") == Integer.parseInt(cursor.getString(cursor.getColumnIndex("writer")))){
                            name = userObject.getString("name");
                            break;
                        }
                    }
                    jsonObject.put("name" , name);
                    mList.add(i , jsonObject);
                    cursor.moveToNext();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView writer;
        public TextView chat_content;

        public ViewHolder(View itemView) {
            super(itemView);
            writer = (TextView)itemView.findViewById(R.id.chat_writer);
            chat_content = (TextView)itemView.findViewById(R.id.chat_content);
        }
        public void setData(JSONObject jsonObject){
            try {
                this.writer.setText(jsonObject.getString("name"));
                chat_content.setText(jsonObject.getString("content"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }



    @Override
    public ChatAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_layout_item, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ChatAdapter.ViewHolder holder, int position) {
        JSONObject group= mList.get(position);
        holder.setData(group);
    }


    @Override
    public int getItemCount() {
        return mList.size();
    }

    public void insertChat(JSONObject jsonObject){
        mList.add(mList.size() , jsonObject);
        a.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ChatAdapter.adapter.notifyItemInserted(ChatAdapter.adapter.getItemCount());
                RecyclerView recyclerView = (RecyclerView)ChatAdapter.adapter.a.findViewById(R.id.gr_layout4_list);
                if(recyclerView!=null){
                    recyclerView.scrollToPosition(mList.size()-1);
                }
            }
        });
    }
}
