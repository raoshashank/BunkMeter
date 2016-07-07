package com.example.shashank.bunkmeter;

import android.app.Dialog;
import android.content.Context;
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
    public void onCreateContextMenu(ContextMenu menu, final View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater= getMenuInflater();
        inflater.inflate(R.menu.contextmenu,menu);
        Button edit = (Button)findViewById(R.id.action_edit);
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {




            }
        });


        Button del = (Button)findViewById(R.id.action_delete);
        del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        mProvider = new DB_Provider(this);
        mProvider.open();


        subject_list = mProvider.getSubjectsFromDatabase();
        mProvider.close();
        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        mAdapter = new SubjectAdapter(this, subject_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(mAdapter);
      registerForContextMenu(recyclerView);
      recyclerView.setOnLongClickListener(new View.OnLongClickListener() {
          @Override
          public boolean onLongClick(View view) {
              ContextMenu contextMenu ;
                Toast.makeText(MainActivity.this,"long click",Toast.LENGTH_SHORT).show();


              return true;
          }
      });







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

        //noinspection SimplifiableIfStatement
        switch (id) {
           case R.id.action_tell_friend:
                Toast.makeText(this, "Tell a Friend button clicked!", Toast.LENGTH_LONG).show();
                break;
            case R.id.action_delete_all:
                Toast.makeText(this, "Delete All button clicked!", Toast.LENGTH_LONG).show();
                break;
            case R.id.notification_settings:
                Toast.makeText(this, "Notify button clicked!", Toast.LENGTH_LONG).show();
                break;
        }

        return super.onOptionsItemSelected(item);
    }


}