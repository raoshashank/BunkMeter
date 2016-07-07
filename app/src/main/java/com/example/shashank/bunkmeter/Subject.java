package com.example.shashank.bunkmeter;

/**
 * Created by Shashank Rao M on 29-06-2016.
 */
public class Subject {
  private  String subject_name;
    private int max_bunkhours;
    private int hours_bunked;
public Subject(){

}
    public Subject(String name , int bunk, int max)
    {
      this.subject_name = name;
      this.max_bunkhours = max;
      this.hours_bunked = bunk;

    }

    public int getHours_bunked() {
        return hours_bunked;
    }

    public void setHours_bunked(int hours_bunked) {
        this.hours_bunked = hours_bunked;
    }

    public int getMax_bunkhours() {
        return max_bunkhours;
    }

    public void setMax_bunkhours(int max_bunkhours) {
        this.max_bunkhours = max_bunkhours;
    }

    public String getSubject_name() {
        return subject_name;
    }

    public void setSubject_name(String subject_name) {
        this.subject_name = subject_name;
    }



}
