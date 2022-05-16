package com.example.school_exit;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

    public class RequestAdapter extends ArrayAdapter<Request>{

        Context context;
        List<Request> requests;
        DatabaseTools databaseTools;

        public RequestAdapter(Context context, int resource, List<Request> requests) {
            super(context, resource, requests);

            this.requests =requests;
            this.context=context;
            databaseTools = new DatabaseTools(context);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            //return super.getView(position, convertView, parent);

            LayoutInflater layoutInflater = ((Activity)context).getLayoutInflater();
            View view = layoutInflater.inflate(R.layout.request_item, parent, false);

            TextView requestNumber =(TextView) view.findViewById(R.id.requestParentItemRequestNumber);
            TextView childName =(TextView) view.findViewById(R.id.requestParentItemChild);
            TextView date = (TextView)view.findViewById(R.id.requestParentItemDate);
            TextView reason = (TextView)view.findViewById(R.id.requestParentItemResaon);

            Request request = requests.get(position);

            databaseTools.open();
            Child child = databaseTools.getChildByPersonalId(request.getChildPersonalId()); // childById
            databaseTools.close();

            requestNumber.setText(String.valueOf(position + 1)); // number of request
            childName.setText(child.getFirstName() + " " + child.getLastName()); // Child full name
            date.setText(request.getDate().getDayOfMonth() + "/" + String.valueOf(request.getDate().getMonth().ordinal() + 1) + "/" + request.getDate().getYear()); // date
            reason.setText(String.valueOf(request.getReason())); // reason

            return view;
        }
    }

    /*

    public View getView(int position, View convertView, ViewGroup parent) {// unfinished, down needs to get fixed

            Log.d("data1", "requestParentAdapter, getView started");
            LayoutInflater layoutInflater = ((Activity)context).getLayoutInflater();
            View view = layoutInflater.inflate(R.layout.request_parent_item, parent, false);


            TextView requestNumber =(TextView) view.findViewById(R.id.requestParentItemRequestNumber);
            TextView childName =(TextView) view.findViewById(R.id.requestParentItemChild);
            TextView date = (TextView)view.findViewById(R.id.requestParentItemDate);
            TextView reason = (TextView)view.findViewById(R.id.requestParentItemResaon);

            Request request = requests.get(position);

            databaseTools.open();

            requestNumber.setText(String.valueOf(position + 1)); // number of request
            Child child = databaseTools.getChildByPersonalId(request.getChildPersonalId()); // childById
            childName.setText(child.getFirstName() + " " + child.getLastName()); // Child full name
            date.setText(request.getDate().getDayOfMonth() + "/" + String.valueOf(request.getDate().getMonth().ordinal() + 1) + "/" + request.getDate().getYear()); // date
            reason.setText(String.valueOf(request.getReason())); // reason

            databaseTools.close();

            return view;
        }
     */
