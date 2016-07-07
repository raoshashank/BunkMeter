package com.example.shashank.bunkmeter;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/*
 * Created by Shashank Rao M on 29-06-2016.
 */
public class SubjectAdapter extends RecyclerView.Adapter<SubjectAdapter.MyViewHolder> {

    private LayoutInflater inflater;
    List<Subject> subject_list = new ArrayList<Subject>();
    private DB_Provider mProvider;
   public SubjectAdapter (Context context , List<Subject> list){
        inflater= LayoutInflater.from(context);
        this.subject_list = list;
        mProvider = new DB_Provider(context);
   }

    public SubjectAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view =inflater.inflate(R.layout.recycler_view_row,parent,false);
        MyViewHolder vh = new MyViewHolder(view);
        return vh;

    }
    public void update(Subject temp){



    }
    @Override
    public void onBindViewHolder(final SubjectAdapter.MyViewHolder holder, int position) {
        final Subject temp = subject_list.get(position);
        final String oldname = temp.getSubject_name();
        holder.subname.setText("  Subject  :  "+temp.getSubject_name().toString());
        holder.max_bunk.setText("  Bunk Limit : "+String.valueOf(temp.getMax_bunkhours()));
        holder.bunked.setText("  Hours Bunked : "+String.valueOf(temp.getHours_bunked()));
        holder.add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(temp.getMax_bunkhours()>temp.getHours_bunked()){

                    if(temp.getMax_bunkhours() -2 ==temp.getHours_bunked())
                        Snackbar.make(holder.rel,"Hold on there buddy! one more bunk and you are gonna have to repeat"+temp.getSubject_name(),Snackbar.LENGTH_INDEFINITE).show();
                    temp.setHours_bunked(temp.getHours_bunked()+1);
                holder.bunked.setText("Hours Bunked : "+String.valueOf(temp.getHours_bunked()));
            //update database
                    update(temp);
                }
            else {
                    Snackbar.make(holder.rel,"That's Not Funny. That's one extra class for you next sem! ",Snackbar.LENGTH_INDEFINITE).show();

                }
            }

        });
    }

    @Override
    public int getItemCount() {
        return subject_list.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView subname;
        TextView max_bunk;
        TextView bunked;
        ImageButton add;
        RelativeLayout rel;
        public MyViewHolder(View itemView) {
            super(itemView);
            subname= (TextView) itemView.findViewById(R.id.subject_entry_recycler);
            bunked  = (TextView)itemView.findViewById(R.id.bunks);
           max_bunk = (TextView)itemView.findViewById(R.id.bunking_limit);
            rel= (RelativeLayout) itemView.findViewById(R.id.relout);
           add = (ImageButton)itemView.findViewById(R.id.image);
        }
    }
}