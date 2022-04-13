package com.mit.servicemodule.CategoryList;

import androidx.constraintlayout.widget.Group;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.JsonObject;
import com.mit.servicemodule.R;
import com.mit.servicemodule.ServiceCategory.ServiceCategory;
import com.mit.servicemodule.ServiceCategory.ServiceCategoryAdapter;
import com.mit.servicemodule.ServiceCategory.ServiceCategoryModel;
import com.mit.servicemodule.ServiceList.ServiceAdapter;
import com.mit.servicemodule.ServiceList.ServiceList;
import com.mit.servicemodule.ServiceList.ServiceModel;
import com.mit.servicemodule.Utils.APIClient;
import com.mit.servicemodule.Utils.ApiPlaceHolder;
import com.mit.servicemodule.Utils.BaseActivity;
import com.mit.servicemodule.Utils.NetworkUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class Categorylist extends BaseActivity {

    ArrayList<CategorylistModel> servicelistcategory = new ArrayList<CategorylistModel>();
    RecyclerView categorylistrecyclerview;
    CategorylistAdapter categorylistAdapter;
    TextView categorylistname,categorysize;
    Button editcategorybutton,deletecategorybutton;
    ImageView backcategorylist,closecategorylist,searchcategorylist,emptycategoryimage,sharecategoryservice;
    EditText searchboxcategorylist;
    Group closecategorylistgroup,searchcategorylistgroup;
    String categoryid="0";
    SwipeRefreshLayout swipeRefresh2;
    EditText editcategory;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categorylist);


        Intent intent= getIntent();
        categoryid=intent.getStringExtra("categoryid");

        categorylistrecyclerview=findViewById(R.id.categorylistrecyclerview);
        categorylistname = findViewById(R.id.categorylistname);
        emptycategoryimage = findViewById(R.id.emptycategoryimage);
        swipeRefresh2=findViewById(R.id.swipeRefresh2);
        sharecategoryservice=findViewById(R.id.sharecategoryservice);

        categorylistname.setText(intent.getStringExtra("categoryname"));

        deletecategorybutton=findViewById(R.id.deletecategorybutton);
        editcategorybutton=findViewById(R.id.editcategorybutton);
        categorysize=findViewById(R.id.categorysize);
        backcategorylist=findViewById(R.id.backcategorylist);

        closecategorylist=findViewById(R.id.closecategorylist);
        searchcategorylist=findViewById(R.id.searchcategorylist);
        searchboxcategorylist=findViewById(R.id.searchboxcategorylist);
        closecategorylistgroup=findViewById(R.id.closecategorylistgroup);
        searchcategorylistgroup=findViewById(R.id.searchcategorylistgroup);


        sharecategoryservice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= getIntent();
                intent.setAction(Intent.ACTION_SEND);

                String text="Category Name : "+intent.getStringExtra("categoryname");
                intent.putExtra(Intent.EXTRA_TEXT,text);
                intent.setType("text/plain");
                intent=Intent.createChooser(intent,"Share By");
                startActivity(intent);
            }
        });

        searchboxcategorylist.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                filter(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        swipeRefresh2.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getservicedata();
            }
        });


        closecategorylist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                closecategorylistgroup.setVisibility(View.INVISIBLE);
                searchcategorylistgroup.setVisibility(View.VISIBLE);

            }
        });

        searchcategorylist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                closecategorylistgroup.setVisibility(View.VISIBLE);
                searchcategorylistgroup.setVisibility(View.INVISIBLE);
                searchboxcategorylist.setText("");

            }
        });

        backcategorylist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        editcategorybutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                bottomsheeteditservicecategory();

            }
        });

        deletecategorybutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                deletedialog(categoryid);

            }
        });

        getservicedata();

        categorylistrecyclerview.setHasFixedSize(true);
        categorylistrecyclerview.setLayoutManager(new LinearLayoutManager(Categorylist.this));

    }


    private void deletedialog(String id) {
        AlertDialog.Builder alert = new AlertDialog.Builder(Categorylist.this);
        alert.setTitle("Delete Category");
        alert.setMessage("Are you sure you want to delete?");


        alert.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                // continue with delete
                deleteservicecategorydata(categoryid);
            }
        });
        alert.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // close dialog
                dialog.cancel();
            }
        });
        alert.show();
    }



    private void filter(String text) {
        ArrayList<CategorylistModel> filteredlist = new ArrayList<>();
        for (CategorylistModel item : servicelistcategory) {
            if (item.getServicename().toLowerCase().contains(text.toLowerCase())) {
                filteredlist.add(item);
            }
        }
        if (filteredlist.isEmpty()) {

            emptycategoryimage.setVisibility(View.VISIBLE);
            categorylistrecyclerview.setVisibility(View.INVISIBLE);

        } else {
            categorylistAdapter.filterList(filteredlist);
            emptycategoryimage.setVisibility(View.INVISIBLE);
            categorylistrecyclerview.setVisibility(View.VISIBLE);
        }
    }


    BottomSheetDialog bottomsheeteditservicecategory;

    void bottomsheeteditservicecategory() {
        bottomsheeteditservicecategory = new BottomSheetDialog(Categorylist.this);
        bottomsheeteditservicecategory.setContentView(R.layout.updatecategorybottomsheet);
        Button updatecategorybutton;

        Intent intent= getIntent();

        editcategory=bottomsheeteditservicecategory.findViewById(R.id.editcategory);
        updatecategorybutton=bottomsheeteditservicecategory.findViewById(R.id.updatecategorybutton);

        editcategory.setText(intent.getStringExtra("categoryname"));



        updatecategorybutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (editcategory.getText().toString().isEmpty()) {
                    editcategory.setError("Enter Service Name");
                }
                else {
                    if (NetworkUtils.isNetworkConnected(Categorylist.this)) {
                        updateservicecategorydata(
                                editcategory.getText().toString() + ""
                        );

                    } else {
                        Toast.makeText(Categorylist.this, "No Internet..", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });


        bottomsheeteditservicecategory.show();
    }

    private void updateservicecategorydata(String name) {

            showLoading();
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("https://api.pidu.in/mybiz/api/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            ApiPlaceHolder retrofitAPI = retrofit.create(ApiPlaceHolder.class);


            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("apikey", "164310030161efb88d2d888");
            jsonObject.addProperty("userid", "1");
            jsonObject.addProperty("storeid", "158");
            jsonObject.addProperty("id", categoryid);
            jsonObject.addProperty("name",name);



            Call<ResponseBody> call = retrofitAPI.updatesecategoryrvice(jsonObject);

            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                    if (response.code() == 200) {
                        if (response.body() != null) {

                            try {
                                JSONObject json = new JSONObject(response.body().string());

                                if (json.getString("status").equals("success")) {

                                    hideLoading();

                                    bottomsheeteditservicecategory.dismiss();

                                    showSnackBarGreen("Data Updated Successfully");

                                    categorylistname.setText(name);

//                                 showSnackBarGreen(json.getString("message"));


                                    getservicedata();



                                } else {
                                    showSnackBarRed("Error1");

                                    hideLoading();
                                }


                            } catch (IOException | JSONException e) {
                                showSnackBarRed("Error2");
                                hideLoading();

                            }
                        } else {
                            showSnackBarRed("Error3");
                            hideLoading();
                        }
                    } else {
                        showSnackBarRed("Error4");
                        hideLoading();

                    }

                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    showSnackBarRed("Error5");
                    hideLoading();

                }
            });

        }

    private void deleteservicecategorydata(String id) {

        showLoading();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.pidu.in/mybiz/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ApiPlaceHolder retrofitAPI = retrofit.create(ApiPlaceHolder.class);


        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("apikey", "164310030161efb88d2d888");
        jsonObject.addProperty("userid", "1");
        jsonObject.addProperty("storeid", "158");
        jsonObject.addProperty("id", id);

        Call<ResponseBody> call = retrofitAPI.deleteservicecategory(jsonObject);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                if (response.code() == 200) {
                    if (response.body() != null) {

                        try {
                            JSONObject json = new JSONObject(response.body().string());

                            if (json.getString("status").equals("success")) {

                                showSnackBarGreen("Data Deleted Successfully");

                                finish();

//
                                hideLoading();
                                getservicedata();


                            } else {

                                hideLoading();
                                showSnackBarGreen("Data 0");
                            }


                        } catch (IOException | JSONException e) {
                            hideLoading();
                            showSnackBarGreen("Data 1");
                        }
                    } else {
                        hideLoading();
                        showSnackBarGreen("Data 2");
                    }
                } else {
                    hideLoading();
                    showSnackBarGreen("Data 3");
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                hideLoading();

            }
        });
    }


    private void getservicedata() {

        showLoading();

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("apikey", "164310030161efb88d2d888");
        jsonObject.addProperty("userid", "1");
        jsonObject.addProperty("storeid", "158");
        jsonObject.addProperty("categoryid", categoryid);


        Call<ResponseBody> call = APIClient.getInstance().create(ApiPlaceHolder.class).listservice(jsonObject);


        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.code() == 200) {
                    if (response.body() != null) {
                        // json = null;
                        try {
                            JSONObject json = new JSONObject(response.body().string());
                            if (json.getString("status").equals("success")) {


                                JSONObject json1 = new JSONObject(json.get("data").toString());
                                Iterator x = json1.keys();
                                //  GetStoreModel[] storeModels = new GetStoreModel[json1.length()];

                                servicelistcategory.clear();
                                while (x.hasNext()) {
                                    String key = (String) x.next();

                                    JSONObject json2 = new JSONObject(json1.get(key).toString());
                                    servicelistcategory.add(new CategorylistModel
                                            (
                                                    json2.get("id") + "",
                                                    json2.get("name") + "",
                                                    json2.get("hours") + "",
                                                    json2.get("minutes") + "",
                                                    json2.get("categoryname") + "",
                                                    json2.get("price") + ""
                                            ));
                                }
                                swipeRefresh2.setRefreshing(false);
                                emptycategoryimage.setVisibility(View.INVISIBLE);
                                categorylistrecyclerview.setVisibility(View.VISIBLE);
                                hideLoading();

                                categorylistAdapter = new CategorylistAdapter(Categorylist.this,servicelistcategory);
                                categorylistrecyclerview.setAdapter(categorylistAdapter);
                                categorysize.setText(servicelistcategory.size()+" Service Found");

                            } else {

                                emptycategoryimage.setVisibility(View.VISIBLE);
                                categorylistrecyclerview.setVisibility(View.INVISIBLE);

                                hideLoading();
                                swipeRefresh2.setRefreshing(false);

                            }
                        } catch (JSONException e) {

                            hideLoading();

                            emptycategoryimage.setVisibility(View.VISIBLE);
                            categorylistrecyclerview.setVisibility(View.INVISIBLE);

                            swipeRefresh2.setRefreshing(false);

                            e.printStackTrace();

                        } catch (IOException e) {

                            hideLoading();

                            emptycategoryimage.setVisibility(View.VISIBLE);
                            categorylistrecyclerview.setVisibility(View.INVISIBLE);
                            swipeRefresh2.setRefreshing(false);

                            e.printStackTrace();
                        }

                    }
                } else {
                    hideLoading();

                    swipeRefresh2.setRefreshing(false);

                    showSnackBarRed("Something Error!!");

//                    swipeRefreshLayout.setRefreshing(false);
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

                hideLoading();

                swipeRefresh2.setRefreshing(false);

                showSnackBarRed("Something Error!!");


            }
        });

    }

}