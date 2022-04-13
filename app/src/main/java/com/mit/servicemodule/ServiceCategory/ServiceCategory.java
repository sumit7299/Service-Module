package com.mit.servicemodule.ServiceCategory;

import androidx.constraintlayout.widget.Group;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

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
import com.mit.servicemodule.CategoryList.Categorylist;
import com.mit.servicemodule.R;
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

public class ServiceCategory extends BaseActivity implements ServiceCategoryAdapter.Editcategoryservice {

    Button addcategory;
    RecyclerView categoryrecyclerView;
    ArrayList<ServiceCategoryModel> servicecategorylist = new ArrayList<ServiceCategoryModel>();
    ServiceCategoryAdapter serviceCategoryAdapter;
    ImageView backservicecategory;
    ImageView closesearchcategory,searchcategory,emptyimagecategory;
    Group closecategorygroup,searchcategorygroup;
    EditText searchservicecategory;
    TextView categorylistsize;
    SwipeRefreshLayout swipeRefresh1;
    int i1 = 0;

    @Override
    protected void onStart() {
        super.onStart();
        if (i1==0){
            getservicecategorydata();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_category);

        i1=1;
        addcategory=findViewById(R.id.addcategory);
        categoryrecyclerView=findViewById(R.id.categoryrecyclerView);
        backservicecategory=findViewById(R.id.backservicecategory);
        closesearchcategory=findViewById(R.id.closesearchcategory);
        searchcategory=findViewById(R.id.searchcategory);
        closecategorygroup=findViewById(R.id.closecategorygroup);
        searchcategorygroup=findViewById(R.id.searchcategorygroup);
        searchservicecategory=findViewById(R.id.searchservicecategory);
        categorylistsize=findViewById(R.id.categorylistsize);
        emptyimagecategory=findViewById(R.id.emptyimagecategory);
        swipeRefresh1=findViewById(R.id.swipeRefresh1);


        swipeRefresh1.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getservicecategorydata();
            }
        });


        searchservicecategory.addTextChangedListener(new TextWatcher() {
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

        searchcategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                closecategorygroup.setVisibility(View.VISIBLE);
                searchcategorygroup.setVisibility(View.INVISIBLE);

            }
        });

        closesearchcategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                closecategorygroup.setVisibility(View.INVISIBLE);
                searchcategorygroup.setVisibility(View.VISIBLE);
                searchservicecategory.setText("");
            }
        });



        backservicecategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        addcategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomsheetaddservicecategory();
            }
        });




        getservicecategorydata();


        categoryrecyclerView.setHasFixedSize(true);
        categoryrecyclerView.setLayoutManager(new LinearLayoutManager(ServiceCategory.this));



    }

    private void filter(String text) {
        ArrayList<ServiceCategoryModel> filteredlist = new ArrayList<>();
        for (ServiceCategoryModel item : servicecategorylist) {
            if (item.getName().toLowerCase().contains(text.toLowerCase())) {
                filteredlist.add(item);
            }
        }
        if (filteredlist.isEmpty()) {

            emptyimagecategory.setVisibility(View.VISIBLE);
            categoryrecyclerView.setVisibility(View.INVISIBLE);

        } else {
            serviceCategoryAdapter.filterList(filteredlist);
            emptyimagecategory.setVisibility(View.INVISIBLE);
            categoryrecyclerView.setVisibility(View.VISIBLE);
        }
    }






    BottomSheetDialog bottomSheetDialogaddservicecategory;

    void bottomsheetaddservicecategory() {
        bottomSheetDialogaddservicecategory = new BottomSheetDialog(ServiceCategory.this);
        bottomSheetDialogaddservicecategory.setContentView(R.layout.addcategorybottomsheet);

        Button submitcategory;
        EditText addservicecategory;

        submitcategory=bottomSheetDialogaddservicecategory.findViewById(R.id.submitcategory);
        addservicecategory=bottomSheetDialogaddservicecategory.findViewById(R.id.addservicecategory);

        submitcategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (addservicecategory.getText().toString().isEmpty()) {
                    addservicecategory.setError("Enter Service Name");
                }
                else {
                    if (NetworkUtils.isNetworkConnected(ServiceCategory.this)) {
                        insertservicecategorydata(
                                addservicecategory.getText().toString() + ""
                        );

                    } else {
                        Toast.makeText(ServiceCategory.this, "No Internet..", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });


        bottomSheetDialogaddservicecategory.show();
    }


    private void insertservicecategorydata(String addservicecategory){


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
        jsonObject.addProperty("name", addservicecategory);


        Call<ResponseBody> call = retrofitAPI.addservicecategory(jsonObject);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                if (response.code() == 200) {
                    if (response.body() != null) {

                        try {

                            JSONObject jsonObject1 = new JSONObject(response.body().string());
                            showSnackBarGreen(jsonObject1.getString("status").toString());

                            hideLoading();
                            getservicecategorydata();
                            bottomSheetDialogaddservicecategory.dismiss();


                        } catch (Exception e) {
                            showSnackBarRed("Catch");
                        }
                    } else {
                        showSnackBarRed("Something Error");
                        hideLoading();
                    }
                } else {
                    showSnackBarRed("Something Error");
                    hideLoading();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                showSnackBarRed("Something Error");
                hideLoading();

            }
        });

    }



    private void getservicecategorydata() {


        showLoading();

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("apikey", "164310030161efb88d2d888");
        jsonObject.addProperty("userid", "1");
        jsonObject.addProperty("storeid", "158");


        Call<ResponseBody> call = APIClient.getInstance().create(ApiPlaceHolder.class).listservicecategory(jsonObject);


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

                                servicecategorylist.clear();
                                while (x.hasNext()) {
                                    String key = (String) x.next();

                                    JSONObject json2 = new JSONObject(json1.get(key).toString());
                                    servicecategorylist.add(new ServiceCategoryModel
                                            (
                                                    json2.get("id")+"",
                                                    json2.get("name") + ""
                                            ));
                                }
                                swipeRefresh1.setRefreshing(false);
                                emptyimagecategory.setVisibility(View.INVISIBLE);
                                categoryrecyclerView.setVisibility(View.VISIBLE);

                                hideLoading();

                                serviceCategoryAdapter = new ServiceCategoryAdapter(ServiceCategory.this,servicecategorylist,ServiceCategory.this);
                                categoryrecyclerView.setAdapter(serviceCategoryAdapter);


                                    categorylistsize.setText(servicecategorylist.size()+" Categories Found");



                            } else {


                                emptyimagecategory.setVisibility(View.VISIBLE);
                                categoryrecyclerView.setVisibility(View.INVISIBLE);

                                hideLoading();
                                swipeRefresh1.setRefreshing(false);

                            }
                        } catch (JSONException e) {
                            hideLoading();
                            emptyimagecategory.setVisibility(View.VISIBLE);
                            categoryrecyclerView.setVisibility(View.INVISIBLE);
                            swipeRefresh1.setRefreshing(false);

                            e.printStackTrace();

                        } catch (IOException e) {
                            hideLoading();
                            emptyimagecategory.setVisibility(View.VISIBLE);
                            categoryrecyclerView.setVisibility(View.INVISIBLE);
                            swipeRefresh1.setRefreshing(false);

                            e.printStackTrace();
                        }

                    }
                } else {
                    hideLoading();
                    swipeRefresh1.setRefreshing(false);

                    showSnackBarRed("Something Error!!");


                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                hideLoading();
                swipeRefresh1.setRefreshing(false);

                showSnackBarRed("Something Error!!");


            }
        });

    }





    @Override
    public void editservicecategory1(ServiceCategoryModel itemviewmodel, int pos) {

        i1=0;
        Intent intent = new Intent(ServiceCategory.this, Categorylist.class);
        intent.putExtra("categoryid",itemviewmodel.getId()+"");
        intent.putExtra("categoryname",itemviewmodel.getName());



        startActivity(intent);

//        Toast.makeText(this, "Hello", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void sharecategory(ServiceCategoryModel itemviewmodel, int pos) {

        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);

        String text="Category Name : "+itemviewmodel.getName();
        intent.putExtra(Intent.EXTRA_TEXT,text);
        intent.setType("text/plain");
        intent=Intent.createChooser(intent,"Share By");
        startActivity(intent);
    }


}