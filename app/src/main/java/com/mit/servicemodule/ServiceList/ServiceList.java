package com.mit.servicemodule.ServiceList;

import static android.app.Activity.RESULT_OK;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.Group;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.JsonObject;
import com.mit.servicemodule.CategoryList.Categorylist;
import com.mit.servicemodule.R;
import com.mit.servicemodule.ServiceCategory.ServiceCategory;
import com.mit.servicemodule.ServicelistAdapter;
import com.mit.servicemodule.ServicelistModel;
import com.mit.servicemodule.Utils.APIClient;
import com.mit.servicemodule.Utils.ApiPlaceHolder;
import com.mit.servicemodule.Utils.BaseFragment;
import com.mit.servicemodule.Utils.NetworkUtils;
import com.sangcomz.fishbun.FishBun;
import com.sangcomz.fishbun.adapter.image.impl.CoilAdapter;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;


import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class ServiceList extends BaseFragment implements ServiceAdapter.Editservice ,ServicelistAdapter.Opencategorylist {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    View view;
    Button addservice;
    RecyclerView servicerecyclerview;
    ArrayList<ServiceModel> servicelist = new ArrayList<ServiceModel>();
    ServiceAdapter serviceAdapter;
    ImageView servicecategory, showcategorydata1, searchbutton, cancelbutton, emptyserviceimage;
    TextView showcategorydata;
    Group searchgroup, cancelsearchgroup;
    EditText searchbox, addcategoryservice, editservicecategory,adduploadimage;
    String categoryid = "0";
    SwipeRefreshLayout swipeRefresh;
    ArrayList<String> path;
    MultipartBody.Part image1;
    String imageid = "";
    ImageView addserviceimage;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_service_list, container, false);

        addservice = view.findViewById(R.id.addservice);
        servicerecyclerview = view.findViewById(R.id.servicerecyclerview);
        servicecategory = view.findViewById(R.id.servicecategory);
        emptyserviceimage = view.findViewById(R.id.emptyserviceimage);
        swipeRefresh = view.findViewById(R.id.swipeRefresh2);
        path = new ArrayList<>();

        showcategorydata1 = view.findViewById(R.id.showcategorydata1);
        showcategorydata = view.findViewById(R.id.showcategorydata);

        searchbutton = view.findViewById(R.id.searchbutton);
        cancelbutton = view.findViewById(R.id.cancelbutton);

        searchgroup = view.findViewById(R.id.searchgroup);
        cancelsearchgroup = view.findViewById(R.id.cancelsearchgroup);

        searchbox = view.findViewById(R.id.searchbox);


        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getservicedata("0");
                showcategorydata.setText("Show All");
            }
        });

        searchbox.addTextChangedListener(new TextWatcher() {
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


        searchbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                cancelsearchgroup.setVisibility(View.VISIBLE);
                searchgroup.setVisibility(View.INVISIBLE);

            }
        });

        cancelbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cancelsearchgroup.setVisibility(View.INVISIBLE);
                searchgroup.setVisibility(View.VISIBLE);
                searchbox.setText("");
            }
        });


        showcategorydata.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                categorydetailsbottomsheet();
            }
        });

        showcategorydata1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                categorydetailsbottomsheet();
            }
        });


        servicecategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(requireContext(), ServiceCategory.class));

            }
        });


        addservice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                bottomsheetaddservice();

            }
        });

//        servicelist.add(new ServiceModel("Hair Cutting","1 hour 30 mins","Hair Service","230.00"));
//        servicelist.add(new ServiceModel("Hair Cutting","1 hour 30 mins","Hair Service","230.00"));
//        servicelist.add(new ServiceModel("Hair Cutting","1 hour 30 mins","Hair Service","230.00"));
//        servicelist.add(new ServiceModel("Hair Cutting","1 hour 30 mins","Hair Service","230.00"));
//        servicelist.add(new ServiceModel("Hair Cutting","1 hour 30 mins","Hair Service","230.00"));
//        servicelist.add(new ServiceModel("Hair Cutting","1 hour 30 mins","Hair Service","230.00"));
//        servicelist.add(new ServiceModel("Hair Cutting","1 hour 30 mins","Hair Service","230.00"));
//        servicelist.add(new ServiceModel("Hair Cutting","1 hour 30 mins","Hair Service","230.00"));
//        servicelist.add(new ServiceModel("Hair Cutting","1 hour 30 mins","Hair Service","230.00"));
//        servicelist.add(new ServiceModel("Hair Cutting","1 hour 30 mins","Hair Service","230.00"));
//        servicelist.add(new ServiceModel("Hair Cutting","1 hour 30 mins","Hair Service","230.00"));
//        servicelist.add(new ServiceModel("Hair Cutting","1 hour 30 mins","Hair Service","230.00"));


        getservicedata("0");

        servicerecyclerview.setHasFixedSize(true);
        servicerecyclerview.setLayoutManager(new LinearLayoutManager(requireContext()));


        return view;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {

            case FishBun.FISHBUN_REQUEST_CODE:
                if (resultCode == RESULT_OK) {
                    path = data.getStringArrayListExtra(FishBun.INTENT_PATH);
//                     imageviewcoupon.setImageURI(Uri.parse(String.valueOf(path.get(0))));

                    uploadimage();
//                    Toast.makeText(requireContext(), "", Toast.LENGTH_SHORT).show();
                    break;
                }
        }

    }

    public String getRealPathFromURI(Uri uri) {
        Cursor cursor = getContext().getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);
    }

    private void uploadimage() {

        showLoading(requireContext());
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.pidu.in/mybiz/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ApiPlaceHolder retrofitAPI = retrofit.create(ApiPlaceHolder.class);



        RequestBody key = RequestBody.create(MultipartBody.FORM,  "164310030161efb88d2d888");
        RequestBody userid = RequestBody.create(MultipartBody.FORM,  "1");
        RequestBody storeid = RequestBody.create(MultipartBody.FORM,  "158");
        File file = new File(getRealPathFromURI(Uri.parse(String.valueOf(path.get(0)))));


        RequestBody filePart = RequestBody.create(file, MediaType.parse("image/*"));
        image1 = MultipartBody.Part.createFormData("images[]", file.getName(), filePart);

        Call<ResponseBody> call = retrofitAPI.uploadimage(image1, key, userid,storeid);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                if (response.code() == 200) {
                    if (response.body() != null) {

                        try {
                            JSONObject json = new JSONObject(response.body().string());
                            //  JSONObject json1=new JSONObject(json.getString("data"));
                            JSONObject json1 = new JSONObject(json.get("images").toString());
                            JSONObject json2 = new JSONObject(json1.get("0").toString());

                            if (json.getString("status").equals("success")) {



                                hideLoading();

                                if (bottomSheetDialogaddservice != null && bottomSheetDialogaddservice.isShowing()){

                                    adduploadimage.setText("Image Uploaded");

                                    imageid = json2.getString("path");
                                    String uri = imageid;
                                    Picasso.get().load(uri)
                                            .placeholder(R.drawable.ic_baseline_image_24)
                                            .noFade()
                                            .into(addserviceimage);

                                    }
                                else
                                {
                                    Toast.makeText(requireContext(), "Image Uploaded", Toast.LENGTH_SHORT).show();
                                }

                                imageid = json2.getString("id");


                            }
                            else
                            {
                                showSnackBarRed("Something Error!!");
                                hideLoading();
                                bottomSheetDialogaddservice.dismiss();
                            }


                        } catch (IOException | JSONException e) {
                            showSnackBarRed("Something Error!!");
                            hideLoading();
                            bottomSheetDialogaddservice.dismiss();
                        }
                    }
                }
                else {
                    showSnackBarRed("Something Error!!");
                    hideLoading();
                    bottomSheetDialogaddservice.dismiss();
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                t.printStackTrace();  showSnackBarRed("Something Error!!");
                hideLoading();
                bottomSheetDialogaddservice.dismiss();
            }
        });

    }

    private void filter(String text) {
        ArrayList<ServiceModel> filteredlist = new ArrayList<>();
        for (ServiceModel item : servicelist) {
            if (item.getName().toLowerCase().contains(text.toLowerCase())) {
                filteredlist.add(item);
            }
        }
        if (filteredlist.isEmpty()) {

            emptyserviceimage.setVisibility(View.VISIBLE);
            servicerecyclerview.setVisibility(View.INVISIBLE);

        } else {
            serviceAdapter.filterList(filteredlist);
            emptyserviceimage.setVisibility(View.INVISIBLE);
            servicerecyclerview.setVisibility(View.VISIBLE);
        }
    }


    String[] minutes = {"0", "5", "10", "15", "20", "25", "30", "35", "40", "45", "50", "55"};

    String[] hours = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12"};

    BottomSheetDialog bottomSheetDialogaddservice;

    void bottomsheetaddservice() {
        bottomSheetDialogaddservice = new BottomSheetDialog(requireContext());
        bottomSheetDialogaddservice.setContentView(R.layout.addservicebottomsheet);

        EditText addservicename, addserviceprice, addservicedescription;
        Button addsubmitservice;
        Spinner addservicehours, addserviceminutes;


        addservicename = bottomSheetDialogaddservice.findViewById(R.id.addservicename);
        addservicehours = bottomSheetDialogaddservice.findViewById(R.id.addservicehours);
        addserviceminutes = bottomSheetDialogaddservice.findViewById(R.id.addserviceminutes);
        addserviceprice = bottomSheetDialogaddservice.findViewById(R.id.addserviceprice);
        addservicedescription = bottomSheetDialogaddservice.findViewById(R.id.addservicedescription);
        addsubmitservice = bottomSheetDialogaddservice.findViewById(R.id.addsubmitservice);
        addcategoryservice = bottomSheetDialogaddservice.findViewById(R.id.addcategoryservice);
        adduploadimage=bottomSheetDialogaddservice.findViewById(R.id.adduploadimage);
        addserviceimage=bottomSheetDialogaddservice.findViewById(R.id.addserviceimage);

        adduploadimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FishBun.with(ServiceList.this).setImageAdapter(new CoilAdapter()).setMaxCount(1).startAlbum();
            }
        });



        addcategoryservice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                categorydetailsbottomsheet();
            }
        });


        ArrayAdapter ad1 = new ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, hours);

        ArrayAdapter ad = new ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, minutes);


        addservicehours.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        ad1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        addservicehours.setAdapter(ad1);


        addserviceminutes.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        ad.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        addserviceminutes.setAdapter(ad);


        addsubmitservice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (addservicename.getText().toString().isEmpty()) {
                    addservicename.setError("Enter Service Name");
                } if (addserviceprice.getText().toString().isEmpty()) {
                    addserviceprice.setError("Enter Price");
                } if (addcategoryservice.getText().toString().isEmpty()) {
                    addcategoryservice.setError("Select Category");
                } else {
                    if (NetworkUtils.isNetworkConnected(requireContext())) {
                        insertservicedata(
                                addservicename.getText().toString() + "",
                                addservicehours.getSelectedItem().toString() + "",
                                addserviceminutes.getSelectedItem().toString() + "",
                                addserviceprice.getText().toString() + "",
                                addservicedescription.getText().toString() + "");

                    } else {
                        Toast.makeText(requireContext(), "No Internet..", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });

        bottomSheetDialogaddservice.show();

    }


    BottomSheetDialog bottomSheetDialogeditservice;

    void bottomsheeteditservice(ServiceModel itemviewmodel) {
        bottomSheetDialogeditservice = new BottomSheetDialog(requireContext());
        bottomSheetDialogeditservice.setContentView(R.layout.editservicebottomsheet);

        EditText editservicename, editserviceprice, editservicedescription;
        Spinner editservicehour, editserviceminutes;
        Button editservicebutton, deleteservicebutton;
        TextView updateprofileimage;

        editservicename = bottomSheetDialogeditservice.findViewById(R.id.editservicename);
        editservicehour = bottomSheetDialogeditservice.findViewById(R.id.editservicehour);
        editserviceminutes = bottomSheetDialogeditservice.findViewById(R.id.editserviceminutes);
        editserviceprice = bottomSheetDialogeditservice.findViewById(R.id.editserviceprice);
        editservicedescription = bottomSheetDialogeditservice.findViewById(R.id.editservicedescription);
        editservicebutton = bottomSheetDialogeditservice.findViewById(R.id.editservicebutton);
        deleteservicebutton = bottomSheetDialogeditservice.findViewById(R.id.deleteservicebutton);
        editservicecategory = bottomSheetDialogeditservice.findViewById(R.id.editservicecategory);
        updateprofileimage=bottomSheetDialogeditservice.findViewById(R.id.updateprofileimage);


        editservicecategory.setText(itemviewmodel.getCategoryname());
        editservicename.setText(itemviewmodel.getName());
        editserviceprice.setText(itemviewmodel.getPrice());
        editservicedescription.setText(itemviewmodel.getDescription());

        updateprofileimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FishBun.with(ServiceList.this).setImageAdapter(new CoilAdapter()).setMaxCount(1).startAlbum();
            }
        });



        editservicecategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                categorydetailsbottomsheet();
            }
        });


        ArrayAdapter ad1 = new ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, hours);

        ArrayAdapter ad = new ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, minutes);


        editservicehour.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        ad1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        editservicehour.setAdapter(ad1);

        if (itemviewmodel.getHours().equals("1")) {
            editservicehour.setSelection(1);
        } else if (itemviewmodel.getHours().equals("2")) {
            editservicehour.setSelection(2);
        } else if (itemviewmodel.getHours().equals("3")) {
            editservicehour.setSelection(3);
        } else if (itemviewmodel.getHours().equals("4")) {
            editservicehour.setSelection(4);
        } else if (itemviewmodel.getHours().equals("5")) {
            editservicehour.setSelection(5);
        } else if (itemviewmodel.getHours().equals("6")) {
            editservicehour.setSelection(6);
        } else if (itemviewmodel.getHours().equals("7")) {
            editservicehour.setSelection(7);
        } else if (itemviewmodel.getHours().equals("8")) {
            editservicehour.setSelection(8);
        } else if (itemviewmodel.getHours().equals("9")) {
            editservicehour.setSelection(9);
        } else if (itemviewmodel.getHours().equals("10")) {
            editservicehour.setSelection(10);
        } else if (itemviewmodel.getHours().equals("11")) {
            editservicehour.setSelection(11);
        } else if (itemviewmodel.getHours().equals("12")) {
            editservicehour.setSelection(12);
        }


        editserviceminutes.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        ad.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        editserviceminutes.setAdapter(ad);


        if (itemviewmodel.getMinutes().equals("5")) {
            editserviceminutes.setSelection(1);
        } else if (itemviewmodel.getMinutes().equals("10")) {
            editserviceminutes.setSelection(2);
        } else if (itemviewmodel.getMinutes().equals("15")) {
            editserviceminutes.setSelection(3);
        } else if (itemviewmodel.getMinutes().equals("20")) {
            editserviceminutes.setSelection(4);
        } else if (itemviewmodel.getMinutes().equals("25")) {
            editserviceminutes.setSelection(5);
        } else if (itemviewmodel.getMinutes().equals("30")) {
            editserviceminutes.setSelection(6);
        } else if (itemviewmodel.getMinutes().equals("35")) {
            editserviceminutes.setSelection(7);
        } else if (itemviewmodel.getMinutes().equals("40")) {
            editserviceminutes.setSelection(8);
        } else if (itemviewmodel.getMinutes().equals("45")) {
            editserviceminutes.setSelection(9);
        } else if (itemviewmodel.getMinutes().equals("50")) {
            editserviceminutes.setSelection(10);
        } else if (itemviewmodel.getMinutes().equals("55")) {
            editserviceminutes.setSelection(11);
        }


        editservicebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (editservicename.getText().toString().isEmpty()) {
                    editservicename.setError("Enter Service Name");
                } else if (editservicecategory.getText().toString().isEmpty()) {
                    editservicecategory.setError("Select Category");
                } else if (editserviceprice.getText().toString().isEmpty()) {
                    editserviceprice.setError("Enter Price");
                } else {
                    if (NetworkUtils.isNetworkConnected(requireContext())) {

                        updateservicedata(
                                itemviewmodel.getId() + "",
                                editservicename.getText().toString() + "",
                                editservicehour.getSelectedItem().toString() + "",
                                editserviceminutes.getSelectedItem().toString() + "",
                                editserviceprice.getText().toString() + "",
                                editservicedescription.getText().toString() + ""
                        );

                    } else {
                        Toast.makeText(requireContext(), "No Internet..", Toast.LENGTH_SHORT).show();

                    }
                }

            }
        });


        deleteservicebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                deleteservicedata(itemviewmodel.getId());
                deletedialog(itemviewmodel.getId());
            }
        });


        bottomSheetDialogeditservice.show();

    }

    private void deletedialog(String id) {
        AlertDialog.Builder alert = new AlertDialog.Builder(requireContext());
        alert.setTitle("Delete Service");
        alert.setMessage("Are you sure you want to delete?");


        alert.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                // continue with delete

                deleteservicedata(id);
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


    @Override
    public void editservice1(ServiceModel itemviewmodel, int pos) {

        bottomsheeteditservice(itemviewmodel);

    }

    @Override
    public void sharedata(ServiceModel serviceModel, int pos) {


        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);

        String text = "Service Name : " + serviceModel.getName();
        text += "\nCategory Name: " + serviceModel.getCategoryname();
        text += "\nPrice : " + serviceModel.getPrice();
        intent.putExtra(Intent.EXTRA_TEXT, text);
        intent.setType("text/plain");
        intent = Intent.createChooser(intent, "Share By");
        startActivity(intent);

    }

    //    insert service data
    private void insertservicedata(String addservicename, String addservicehours, String addserviceminutes, String addserviceprice, String addservicedescription) {

        showLoading(requireContext());
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.pidu.in/mybiz/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ApiPlaceHolder retrofitAPI = retrofit.create(ApiPlaceHolder.class);

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("apikey", "164310030161efb88d2d888");
        jsonObject.addProperty("userid", "1");
        jsonObject.addProperty("storeid", "158");
        jsonObject.addProperty("categoryid", categoryid);
        jsonObject.addProperty("name", addservicename);
        jsonObject.addProperty("hours", addservicehours);
        jsonObject.addProperty("minutes", addserviceminutes);
        jsonObject.addProperty("price", addserviceprice);
        jsonObject.addProperty("description", addservicedescription);
        jsonObject.addProperty("imageid",imageid);

        Call<ResponseBody> call = retrofitAPI.addservice(jsonObject);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                if (response.code() == 200) {
                    if (response.body() != null) {

                        try {

                            JSONObject jsonObject1 = new JSONObject(response.body().string());
                            showSnackBarGreen(jsonObject1.getString("status").toString());

                            hideLoading();
                            getservicedata("0");
//                            addserviceimage.se
                            imageid="empty";
                            bottomSheetDialogaddservice.dismiss();



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


//    list service data

    private void getservicedata(String id) {
        showLoading(requireContext());

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("apikey", "164310030161efb88d2d888");
        jsonObject.addProperty("userid", "1");
        jsonObject.addProperty("storeid", "158");
        jsonObject.addProperty("categoryid", id);


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

                                servicelist.clear();
                                while (x.hasNext()) {
                                    String key = (String) x.next();

                                    JSONObject json2 = new JSONObject(json1.get(key).toString());
                                    servicelist.add(new ServiceModel
                                            (
                                                    json2.get("id") + "",
                                                    json2.get("name") + "",
                                                    json2.get("description") + "",
                                                    json2.get("categoryid") + "",
                                                    json2.get("categoryname") + "",
                                                    json2.get("imageid") + "",
                                                    json2.get("hours") + "",
                                                    json2.get("minutes") + "",
                                                    json2.get("price") + "",
                                                    json2.get("image") + ""

                                            ));
                                }
                                swipeRefresh.setRefreshing(false);
                                emptyserviceimage.setVisibility(View.INVISIBLE);
                                servicerecyclerview.setVisibility(View.VISIBLE);
                                hideLoading();

                                serviceAdapter = new ServiceAdapter(requireActivity(), servicelist, ServiceList.this);
                                servicerecyclerview.setAdapter(serviceAdapter);

                            } else {


                                emptyserviceimage.setVisibility(View.VISIBLE);
                                servicerecyclerview.setVisibility(View.INVISIBLE);

                                hideLoading();
                                swipeRefresh.setRefreshing(false);

                            }
                        } catch (JSONException e) {
                            hideLoading();

                            emptyserviceimage.setVisibility(View.VISIBLE);
                            servicerecyclerview.setVisibility(View.INVISIBLE);
                            swipeRefresh.setRefreshing(false);

                            e.printStackTrace();

                        } catch (IOException e) {
                            hideLoading();
                            emptyserviceimage.setVisibility(View.VISIBLE);
                            servicerecyclerview.setVisibility(View.INVISIBLE);
                            swipeRefresh.setRefreshing(false);

                            e.printStackTrace();
                        }

                    }
                } else {
                    hideLoading();
                    swipeRefresh.setRefreshing(false);

                    showSnackBarRed("Something Error!!");


                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                hideLoading();
                swipeRefresh.setRefreshing(false);

                showSnackBarRed("Something Error!!");

                swipeRefresh.setRefreshing(false);
            }
        });

    }


//    update service data

    private void updateservicedata(String id, String editservicename, String editservicehour, String editserviceminutes, String editserviceprice, String editservicedescription) {

        showLoading(requireContext());
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
        jsonObject.addProperty("categoryid", categoryid);
        jsonObject.addProperty("name", editservicename);
        jsonObject.addProperty("hours", editservicehour);
        jsonObject.addProperty("minutes", editserviceminutes);
        jsonObject.addProperty("price", editserviceprice);
        jsonObject.addProperty("description", editservicedescription);
        jsonObject.addProperty("imageid",imageid);



        Call<ResponseBody> call = retrofitAPI.updateservice(jsonObject);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                if (response.code() == 200) {
                    if (response.body() != null) {

                        try {
                            JSONObject json = new JSONObject(response.body().string());

                            if (json.getString("status").equals("success")) {

                                hideLoading();

                                getservicedata("0");

                                bottomSheetDialogeditservice.dismiss();

                                showSnackBarGreen("Data Updated Successfully");
//                                 showSnackBarGreen(json.getString("message"));

                                imageid="empty";

//                                getservicedata(id);


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

//    delete service data

    private void deleteservicedata(String id) {

        showLoading(requireContext());
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

        Call<ResponseBody> call = retrofitAPI.deleteservice(jsonObject);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                if (response.code() == 200) {
                    if (response.body() != null) {

                        try {
                            JSONObject json = new JSONObject(response.body().string());

                            if (json.getString("status").equals("success")) {

                                bottomSheetDialogeditservice.dismiss();
//                                    PlansModel model1 = new PlansModel(id,editpackagename,editdays,editduration,editamount,editnotes);
//                                    list.set(pos, model1);
//                                    plansadapter.notifyDataSetChanged();


                                hideLoading();
                                getservicedata("0");
                                showSnackBarGreen("Data Deleted Successfully");
                            } else {
                                hideLoading();
                            }


                        } catch (IOException | JSONException e) {
                            hideLoading();

                        }
                    } else {
                        hideLoading();
                    }
                } else {
                    hideLoading();

                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                hideLoading();

            }
        });
    }

    BottomSheetDialog categorydetailsbottomsheet;
    ArrayList<ServicelistModel> listservicecategory = new ArrayList<ServicelistModel>();
    ServicelistAdapter servicelistAdapter;
    RecyclerView categorydetailsrecyclerview;

    void categorydetailsbottomsheet() {

        categorydetailsbottomsheet = new BottomSheetDialog(requireContext());
        categorydetailsbottomsheet.setContentView(R.layout.categorydetailsbottomsheet);

        categorydetailsrecyclerview = categorydetailsbottomsheet.findViewById(R.id.categorydetailsrecyclerview);

        getservicelistcategorydata();

        categorydetailsrecyclerview.setHasFixedSize(true);
        categorydetailsrecyclerview.setLayoutManager(new LinearLayoutManager(requireContext()));


        categorydetailsbottomsheet.show();


    }


    private void getservicelistcategorydata() {


        showLoading(requireContext());

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

                                listservicecategory.clear();
                                while (x.hasNext()) {
                                    String key = (String) x.next();

                                    JSONObject json2 = new JSONObject(json1.get(key).toString());
                                    listservicecategory.add(new ServicelistModel
                                            (
                                                    json2.get("id") + "",
                                                    json2.get("name") + ""
                                            ));
                                }
//                                swiperefresh.setRefreshing(false);
//                                emptyplanimage.setVisibility(View.INVISIBLE);
//                                recyclerView.setVisibility(View.VISIBLE);

                                hideLoading();

                                servicelistAdapter = new ServicelistAdapter(requireContext(), listservicecategory, ServiceList.this);
                                categorydetailsrecyclerview.setAdapter(servicelistAdapter);


                            } else {


//                                emptyplanimage.setVisibility(View.VISIBLE);
//                                recyclerView.setVisibility(View.INVISIBLE);

                                hideLoading();
//                                swiperefresh.setRefreshing(false);

                            }
                        } catch (JSONException e) {
                            hideLoading();
//                            emptyplanimage.setVisibility(View.VISIBLE);
//                            recyclerView.setVisibility(View.INVISIBLE);
//                            swiperefresh.setRefreshing(false);

                            e.printStackTrace();

                        } catch (IOException e) {
                            hideLoading();
//                            emptyplanimage.setVisibility(View.VISIBLE);
//                            recyclerView.setVisibility(View.INVISIBLE);
//                            swiperefresh.setRefreshing(false);

                            e.printStackTrace();
                        }

                    }
                } else {
                    hideLoading();
//                    swiperefresh.setRefreshing(false);

                    showSnackBarRed("Something Error!!");

//                    swipeRefreshLayout.setRefreshing(false);
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                hideLoading();
//                swiperefresh.setRefreshing(false);

                showSnackBarRed("Something Error!!");


            }
        });

    }


    @Override
    public void opencategorylist1(ServicelistModel itemviewmodel, int pos) {

        getservicedata(itemviewmodel.getId());
        categoryid = itemviewmodel.getId();
        showcategorydata.setText(itemviewmodel.getName());

        if (bottomSheetDialogaddservice != null && bottomSheetDialogaddservice.isShowing()) {
            addcategoryservice.setText(itemviewmodel.getName());
            categorydetailsbottomsheet.dismiss();

        } else if (bottomSheetDialogeditservice != null && bottomSheetDialogeditservice.isShowing()) {
            editservicecategory.setText(itemviewmodel.getName());
            categorydetailsbottomsheet.dismiss();
        } else {
            categorydetailsbottomsheet.dismiss();

        }


    }
}

