package com.example.shashank.bunkmeter;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
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
    Context ctx ;
    private LayoutInflater inflater;
    List<Subject> subject_list = new ArrayList<Subject>();
    private DB_Provider mProvider;
   public SubjectAdapter (Context context , List<Subject> list){
        inflater= LayoutInflater.from(context);
        this.subject_list = list;
        mProvider = new DB_Provider(context);
       this.ctx=context;
   }

    public SubjectAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view =inflater.inflate(R.layout.recycler_view_row,parent,false);
        MyViewHolder vh = new MyViewHolder(view);
        return vh;

    }

    @Override
    public void onBindViewHolder(final SubjectAdapter.MyViewHolder holder, int position) {
        final Subject temp = subject_list.get(position);
        final String oldname = temp.getSubject_name();
        holder.itemView.setLongClickable(true);
        holder.subname.setText("  Subject  :  "+temp.getSubject_name().toString());
        holder.max_bunk.setText("  Bunk Limit : "+String.valueOf(temp.getMax_bunkhours()));
        holder.bunked.setText("  Hours Bunked : "+String.valueOf(temp.getHours_bunked()));
        holder.add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.setLongClickable(true);
                if(temp.getMax_bunkhours()>temp.getHours_bunked()){

                    if(temp.getMax_bunkhours() -2 ==temp.getHours_bunked())
                        Snackbar.make(holder.rel,"Hold on there buddy! one more bunk and you are gonna have to repeat"+temp.getSubject_name(),Snackbar.LENGTH_INDEFINITE).show();
                    temp.setHours_bunked(temp.getHours_bunked()+1);
                holder.bunked.setText("Hours Bunked : "+String.valueOf(temp.getHours_bunked()));
                    mProvider.updatehours(temp);
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
        public MyViewHolder(final View itemView) {
            super(itemView);
            subname= (TextView) itemView.findViewById(R.id.subject_entry_recycler);
            bunked  = (TextView)itemView.findViewById(R.id.bunks);
           max_bunk = (TextView)itemView.findViewById(R.id.bunking_limit);
            rel= (RelativeLayout) itemView.findViewById(R.id.relout);
           add = (ImageButton)itemView.findViewById(R.id.image);
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {

                    final Dialog dialog = new Dialog(ctx);
                    dialog.setCancelable(true);
                    dialog.setContentView(R.layout.editdel);
                    TextView edit = (TextView)dialog.findViewById(R.id.edit_tv);
                    TextView del = (TextView)dialog.findViewById(R.id.del_tv);
                    dialog.setTitle("Modify");
                    del.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                             AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
                            builder.setMessage("Are you sure you want to delete this?").setPositiveButton("YES", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    mProvider.deleteFromDatabase(subject_list.get(getLayoutPosition()));
                                    subject_list.remove(subject_list.get(getLayoutPosition()));
                                    dialog.dismiss();
                                    notifyItemRemoved(getLayoutPosition());
                                }
                            }).setNegativeButton("NO", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialog.dismiss();
                                }
                            }).create().show();


                        }
                    });



                    edit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            final String oldname = subject_list.get(getLayoutPosition()).getSubject_name();
                            final Subject s = subject_list.get(getLayoutPosition());
                            final Dialog ed = new Dialog(ctx);
                            ed.setContentView(R.layout.edit);
                            ed.setTitle("Enter New Details");
                            final EditText subject_entry_edit = (EditText)ed.findViewById(R.id.subject_entry_edit);
                            final EditText hours_bunked_edit = (EditText)ed.findViewById(R.id.hours_bunked_edit);
                            final EditText max_bunk_edit = (EditText)ed.findViewById(R.id.bunk_limit_edit);

                            Button done = (Button)ed.findViewById(R.id.done_edit);
                            done.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                    if(subject_entry_edit.getText().toString().equals("") || max_bunk_edit.getText().toString().equals("")|| hours_bunked_edit.getText().toString().equals(0))
                                    {
                                        ed.dismiss();dialog.dismiss();
                                    }else {
                                        if (Integer.parseInt(hours_bunked_edit.getText().toString())>Integer.parseInt(max_bunk_edit.getText().toString())) {
                                            Toast.makeText(ctx, "NICE TRY!", Toast.LENGTH_LONG).show();
                                         }

                                        else{
                                            subject_list.remove(s);
                                            notifyItemRemoved(getLayoutPosition());
                                        s.setSubject_name(subject_entry_edit.getText().toString());
                                        s.setHours_bunked(Integer.parseInt(hours_bunked_edit.getText().toString()));
                                        s.setMax_bunkhours(Integer.parseInt(max_bunk_edit.getText().toString()));
                                        mProvider.updatecompletesubject(s, oldname);
                                        subject_list.add(getLayoutPosition(), s);
                                        notifyDataSetChanged();
                                        notifyItemChanged(getLayoutPosition(),s);
                                        ed.dismiss();
                                        dialog.dismiss();
                                    }
                                }}
                            });

                            Button cancel_Edit = (Button)ed.findViewById(R.id.cancel_edit);
                            cancel_Edit.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    ed.dismiss();
                                }
                            });

                        ed.show();
                        }

                    });



                    dialog.show();




                    return true;
                }
            });

        }


    }




}