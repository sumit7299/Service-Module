package com.mit.servicemodule.CategoryList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mit.servicemodule.R;
import com.mit.servicemodule.ServiceCategory.ServiceCategoryModel;


import java.util.ArrayList;

public class CategorylistAdapter extends RecyclerView.Adapter<CategorylistAdapter.ViewHolder> {

    private Context context;
    private ArrayList<CategorylistModel> servicelistcategory;


    public CategorylistAdapter(Context context, ArrayList<CategorylistModel> servicelistcategory) {

        this.context = context;
        this.servicelistcategory = servicelistcategory;

    }

    public void filterList(ArrayList<CategorylistModel> filteredlist) {
        servicelistcategory = filteredlist;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CategorylistAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater=LayoutInflater.from(parent.getContext());
        View servicelistcategory=layoutInflater.inflate(R.layout.service_item,parent,false);
        CategorylistAdapter.ViewHolder viewHolder=new CategorylistAdapter.ViewHolder(servicelistcategory);
        return viewHolder;

    }

    @Override
    public void onBindViewHolder(@NonNull CategorylistAdapter.ViewHolder holder, int position) {

        holder.servicename1.setText(servicelistcategory.get(position).getServicename());
        holder.servicehours1.setText(servicelistcategory.get(position).getServicehours()+" hour");
        holder.serviceminutes1.setText(servicelistcategory.get(position).getServiceminutes()+" mins");
        holder.servicetype1.setText(servicelistcategory.get(position).getCategoryservice());
        holder.serviceprice1.setText(context.getString(R.string.Rs)+" "+servicelistcategory.get(position).getServiceprice());
    }

    @Override
    public int getItemCount() {

        return servicelistcategory.size();

    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView servicename1,servicehours1,serviceminutes1,servicetype1,serviceprice1;

        public ViewHolder(@NonNull View itemView) {

            super(itemView);

            servicename1=itemView.findViewById(R.id.servicename);
            servicehours1=itemView.findViewById(R.id.servicehours);
            serviceminutes1=itemView.findViewById(R.id.serviceminutes);
            servicetype1=itemView.findViewById(R.id.servicetype);
            serviceprice1=itemView.findViewById(R.id.serviceprice);

        }
    }
}