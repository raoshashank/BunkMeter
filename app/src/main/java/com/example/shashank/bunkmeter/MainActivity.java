package com.example.shashank.bunkmeter;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class MainActivity extends AppCompatActivity {
    DB_Provider mProvider;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager mlayoutmanager;
    private RecyclerView.Adapter mAdapter;
    List<Subject> subject_list;
    public SQLiteDatabase db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        mProvider = new DB_Provider(this);
        mProvider.open();
        TextView title = (TextView)findViewById(R.id.title);
        subject_list = mProvider.getSubjectsFromDatabase();
        mProvider.close();
        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        mAdapter = new SubjectAdapter(this, subject_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(mAdapter);
        if(subject_list.isEmpty())
        {
            Snackbar.make(recyclerView,"Hey!Go on and press the + Button to proudly add in your first Bunk!Swipe to dismiss",Snackbar.LENGTH_INDEFINITE).show();

        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                {


                    final Dialog entry = new Dialog(MainActivity.this);
                    entry.setCancelable(false);
                    entry.setContentView(R.layout.alertlayout);
                    entry.setTitle("Enter the Subject Details");
                    final EditText subject_entry_et = (EditText) entry.findViewById(R.id.subject_entry);
                    final EditText max_bunk_et = (EditText) entry.findViewById(R.id.bunk_limit);
                    Button done = (Button) entry.findViewById(R.id.done);
                    final Button cancel = (Button) entry.findViewById(R.id.cancel);
                    done.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            if (subject_entry_et.getText().toString().equals("") || max_bunk_et.getText().toString().equals("")) {
                                Toast.makeText(getApplicationContext(), "fill both fields please", Toast.LENGTH_LONG).show();
                            } else {


                                Subject s = new Subject(subject_entry_et.getText().toString(), 0, Integer.parseInt(max_bunk_et.getText().toString()));
                                entry.dismiss();
                                subject_list.add(s);
                                mAdapter.notifyDataSetChanged();

                                mProvider.open();
                                mProvider.saveSubjectInDatabase(s);
                                mProvider.close();


                            }
                        }
                    });

                                        cancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            entry.dismiss();
                        }
                    });

                    entry.show();


                }
            }
        });




    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {

            case R.id.action_delete_all:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("Are you Sure you want to delete all subjects?? This action CANNOT BE UNDONE!")
                        .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                mProvider.deleteall();
                                subject_list.clear();
                                dialogInterface.dismiss();
                                recyclerView.getAdapter().notifyDataSetChanged();
                                mAdapter.notifyDataSetChanged();

                            }
                        }).setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                }).create().show();

                break;

            case R.id.about :
                AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
                builder1.setMessage(" Hey! Nice of You to show Interest in Bunkmeter!!This App Records your bunking details and was created by" +
                        " Shashank Rao from NITK Surathkal during his free time! Hope you liked it ! Now go back to your" +
                        " bunking! ;)").setPositiveButton("COOL!", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                }).setNegativeButton("AWESOME!", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                }).create().show();break;

            case R.id.menu_item_share : {
                String msg=new String();
                msg+="Bunking Details:\n";
                for(Subject s : subject_list) {
                    msg += "\n Subject : " + s.getSubject_name()
                            + "\n Classes Bunked : " + String.valueOf(s.getMax_bunkhours()) +
                            "\n limit : " + String.valueOf(s.getHours_bunked()) + "\n-------";
                }
                    Intent sendIntent = new Intent();
                    sendIntent.setAction(Intent.ACTION_SEND);
                    sendIntent.putExtra(Intent.EXTRA_TEXT, msg);
                    sendIntent.setType("text/plain");
                    startActivity(sendIntent);
                    break;

            }

        }


        return super.onOptionsItemSelected(item);
    }


}