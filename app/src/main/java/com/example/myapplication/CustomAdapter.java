package com.example.myapplication;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder>{

    private ArrayList<DataModel> localDataSet;
    DBHelper DB;

    Context context;

    //===== 뷰홀더 클래스 =====================================================
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView textView;
        private ImageView imageView;
        private ImageView imageView2;
        private ImageView heart;
        private ImageView viewbutton;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.textView);
            imageView = itemView.findViewById(R.id.imageView);
            imageView2 = itemView.findViewById(R.id.imageView2);
            heart = itemView.findViewById(R.id.imageView3);
            viewbutton = itemView.findViewById(R.id.btnView);
        }
        public TextView getTextView() {
            return textView;
        }
    }
    //========================================================================

    //----- 생성자 --------------------------------------
    // 생성자를 통해서 데이터를 전달받도록 함
    public CustomAdapter (ArrayList<DataModel> dataSet) {
        localDataSet = dataSet;
    }
    //--------------------------------------------------

    @NonNull
    @Override   // ViewHolder 객체를 생성하여 리턴한다.
    public CustomAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recyclerview_item, parent, false);
        CustomAdapter.ViewHolder viewHolder = new CustomAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override   // ViewHolder안의 내용을 position에 해당되는 데이터로 교체한다.
    public void onBindViewHolder(@NonNull CustomAdapter.ViewHolder holder, int position) {
        DB = new DBHelper(holder.heart.getContext());

        DataModel data = localDataSet.get(position);
        String text = data.title;
        holder.textView.setText(text);
        holder.imageView.setImageResource(data.image_path);
        int arrow;
        if(data.change=="UP") arrow = R.drawable.up;
        else arrow = R.drawable.down;

        holder.imageView2.setImageResource(arrow);
        holder.heart.setImageResource(R.drawable.heart_b);
        holder.viewbutton.setImageResource(R.drawable.apple);

        holder.heart.setOnClickListener(new View.OnClickListener() {
            int flag = 0;
            @Override
            public void onClick(View view) {
                String title = text;
                Boolean checkinsertdata = DB.insertuserdata(title);
                if(checkinsertdata==true)
                    Toast.makeText(holder.heart.getContext(), "New Entry Inserted", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(holder.heart.getContext(), "New Entry Not Inserted", Toast.LENGTH_SHORT).show();
                if(flag==0) {
                    holder.heart.setImageResource(R.drawable.heart);
                    flag=1;
                }
                else{
                    holder.heart.setImageResource(R.drawable.heart_b);
                    flag=0;
                }

            }        });
        holder.viewbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Cursor res = DB.getdata();
                if(res.getCount()==0){
                    Toast.makeText(holder.viewbutton.getContext(), "No Entry Exists", Toast.LENGTH_SHORT).show();
                    return;
                }
                StringBuffer buffer = new StringBuffer();
                while(res.moveToNext()){
                    buffer.append("Title :"+res.getString(0)+"\n");
                }

                AlertDialog.Builder builder = new AlertDialog.Builder(holder.viewbutton.getContext());
                builder.setCancelable(true);
                builder.setTitle("User Entries");
                builder.setMessage(buffer.toString());
                builder.show();
            }        });

    }

    @Override   // 전체 데이터의 갯수를 리턴한다.
    public int getItemCount() {
        return localDataSet.size();
    }
}