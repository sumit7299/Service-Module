package com.mit.servicemodule;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;


import com.mit.servicemodule.ServiceCategory.ServiceCategoryAdapter;
import com.mit.servicemodule.ServiceList.ServiceAdapter;
import com.mit.servicemodule.ServiceList.ServiceModel;

import java.util.ArrayList;

public class ServicelistAdapter extends RecyclerView.Adapter<ServicelistAdapter.ViewHolder> {

    private Context context;
    private ArrayList<ServicelistModel> listservicecategory;
    private Opencategorylist opencategorylist;
//    private Openlist openlist;



    public ServicelistAdapter(Context context, ArrayList<ServicelistModel> listservicecategory,Opencategorylist opencategorylist){

        this.context=context;
        this.listservicecategory=listservicecategory;
        this.opencategorylist=opencategorylist;
//        this.openlist=openlist;



    }

    public interface Opencategorylist
    {

        void opencategorylist1 (ServicelistModel itemviewmodel, int pos);


    }




    @NonNull
    @Override
    public ServicelistAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater=LayoutInflater.from(parent.getContext());
        View listservicecategory=layoutInflater.inflate(R.layout.categorylist_item,parent,false);
        ServicelistAdapter.ViewHolder viewHolder=new ServicelistAdapter.ViewHolder(listservicecategory);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ServicelistAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {

        holder.categoryname.setText(listservicecategory.get(position).getName());

        holder.cardview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                opencategorylist.opencategorylist1(listservicecategory.get(position),position);

            }
        });




    }

    @Override
    public int getItemCount() {
        return listservicecategory.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView categoryname;
        CardView cardview;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            categoryname=itemView.findViewById(R.id.categoryname);
            cardview=itemView.findViewById(R.id.cardview);
        }
    }
}
