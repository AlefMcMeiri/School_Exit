package com.example.school_exit;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class ChildAdapter extends ArrayAdapter<Child> {


    Context context;
    List<Child> children;
    DatabaseTools databaseTools;

    public ChildAdapter(@NonNull Context context, int resource, List<Child> children) {
        super(context, resource, children);

        this.context = context;
        this.children = children;
        databaseTools = new DatabaseTools(context);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater layoutInflater = ((Activity)context).getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.child_item, parent, false);

        TextView childNumber =(TextView) view.findViewById(R.id.ChildItemChildNumber);
        TextView childName =(TextView) view.findViewById(R.id.ChildItemName);
        TextView childClass = (TextView)view.findViewById(R.id.ChildItemClass);
        TextView childTeacher = (TextView)view.findViewById(R.id.ChildItemTeacher);

        Child child = children.get(position);

        databaseTools.open();

        childNumber.setText(String.valueOf(position + 1)); // number of child
        childName.setText(child.getFirstName() + " " + child.getLastName()); // Child full name
        childClass.setText(child.getNumberClass()); // Class
        Teacher teacher = databaseTools.getTeacherByClass(child.getNumberClass());
        if (teacher != null)
            childTeacher.setText(teacher.getFirstName() + " " + teacher.getLastName()); // Teacher
        else
            childTeacher.setText("Unknown");

        databaseTools.close();

        return view;
    }
}
