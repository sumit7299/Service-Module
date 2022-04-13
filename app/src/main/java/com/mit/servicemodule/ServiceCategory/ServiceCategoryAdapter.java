package com.mit.servicemodule.ServiceCategory;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.mit.servicemodule.R;
import com.mit.servicemodule.ServiceList.ServiceModel;

import java.util.ArrayList;

public class ServiceCategoryAdapter extends RecyclerView.Adapter<ServiceCategoryAdapter.ViewHolder> {

    private Context context;
    private ArrayList<ServiceCategoryModel> servicecategorylist;
    private ServiceCategoryAdapter.Editcategoryservice editcategoryservice;


    public ServiceCategoryAdapter(Context context, ArrayList<ServiceCategoryModel> servicecategorylist,Editcategoryservice editcategoryservice){

        this.context=context;
        this.servicecategorylist=servicecategorylist;
        this.editcategoryservice=editcategoryservice;

    }

    public void filterList(ArrayList<ServiceCategoryModel> filteredlist) {
        servicecategorylist = filteredlist;
        notifyDataSetChanged();
    }

    public interface Editcategoryservice {

        void editservicecategory1 (ServiceCategoryModel itemviewmodel, int pos);

        void sharecategory(ServiceCategoryModel itemviewmodel, int pos);


    }


    @NonNull
    @Override

    public ServiceCategoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater=LayoutInflater.from(parent.getContext());
        View servicecategorylist=layoutInflater.inflate(R.layout.category_item,parent,false);
        ServiceCategoryAdapter.ViewHolder viewHolder=new ServiceCategoryAdapter.ViewHolder(servicecategorylist);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ServiceCategoryAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {

        holder.categorynameitem.setText(servicecategorylist.get(position).getName());




        holder.categorylist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                editcategoryservice.editservicecategory1(servicecategorylist.get(position),position);


            }
        });

        holder.sharecategoryitem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editcategoryservice.sharecategory(servicecategorylist.get(position),position);
            }
        });

    }

    @Override
    public int getItemCount() {
        return servicecategorylist.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView categorynameitem,servicelistitem;
        ImageView sharecategoryitem;
        ConstraintLayout categorylist;

        public ViewHolder(@NonNull View itemView)
        {
            super(itemView);

            categorynameitem=itemView.findViewById(R.id.categorynameitem);
//            servicelistitem=itemView.findViewById(R.id.servicelistitem);
            sharecategoryitem=itemView.findViewById(R.id.sharecategoryitem);
            categorylist=itemView.findViewById(R.id.categorylist);

        }
    }
}
