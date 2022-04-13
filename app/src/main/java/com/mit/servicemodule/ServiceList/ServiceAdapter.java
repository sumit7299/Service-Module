package com.mit.servicemodule.ServiceList;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.mit.servicemodule.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class ServiceAdapter extends RecyclerView.Adapter<ServiceAdapter.ViewHolder> {


    private Context context;
    private ArrayList<ServiceModel> servicelist;
    private Editservice editservice;

    public ServiceAdapter(Context context, ArrayList<ServiceModel> servicelist,Editservice editservice){

        this.context=context;
        this.servicelist=servicelist;
        this.editservice=editservice;
    }

    public void filterList(ArrayList<ServiceModel> filteredlist) {
        servicelist=filteredlist;
        notifyDataSetChanged();
    }

    public interface Editservice
    {

        void editservice1 (ServiceModel itemviewmodel, int pos);

        void sharedata(ServiceModel serviceModel, int pos);

    }


    @NonNull
    @Override
    public ServiceAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater=LayoutInflater.from(parent.getContext());
        View servicelist=layoutInflater.inflate(R.layout.service_item,parent,false);
        ServiceAdapter.ViewHolder viewHolder=new ServiceAdapter.ViewHolder(servicelist);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ServiceAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {

        holder.servicename.setText(servicelist.get(position).getName());
        holder.servicehours.setText(servicelist.get(position).getHours()+" hour");
        holder.serviceminutes.setText(servicelist.get(position).getMinutes()+" mins");
        holder.servicetype.setText(servicelist.get(position).getCategoryname());
        holder.serviceprice.setText(context.getString(R.string.Rs)+" "+servicelist.get(position).getPrice());

        holder.cardview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                editservice.editservice1(servicelist.get(position),position);
            }
        });

        holder.shareservicedetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editservice.sharedata(servicelist.get(position),position);
            }
        });


        if (!servicelist.get(position).getImage().isEmpty() && servicelist.get(position).getImage() != null) {

            //   Log.e("img",list.get(position).getCouponimage());
            String uri = servicelist.get(position).getImage();
            Picasso.get().load(uri)
                    .placeholder(R.drawable.ic_icon_colorcode_purple)
                    // To fit image into imageView
                    // To prevent fade animation
                    .noFade()
                    .into(holder.serviceprofile);
        }


        }

    @Override
    public int getItemCount() {
        return servicelist.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView servicename,servicehours,serviceminutes,servicetype,serviceprice;
        ConstraintLayout cardview;
        ImageView shareservicedetails,serviceprofile;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            servicename=itemView.findViewById(R.id.servicename);
            servicehours=itemView.findViewById(R.id.servicehours);
            serviceminutes=itemView.findViewById(R.id.serviceminutes);
            servicetype=itemView.findViewById(R.id.servicetype);
            serviceprice=itemView.findViewById(R.id.serviceprice);
            cardview=itemView.findViewById(R.id.cardview);
            shareservicedetails=itemView.findViewById(R.id.shareservicedetails);
            serviceprofile=itemView.findViewById(R.id.serviceprofile);

        }
    }
}
