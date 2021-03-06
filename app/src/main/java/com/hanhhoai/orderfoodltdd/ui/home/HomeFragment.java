package com.hanhhoai.orderfoodltdd.ui.home;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.hanhhoai.orderfoodltdd.Adapter.FoodAdapter;
import com.hanhhoai.orderfoodltdd.Adapter.ImagesAdapter;
import com.hanhhoai.orderfoodltdd.Convert.DataConvert;
import com.hanhhoai.orderfoodltdd.DAO.AppDatabase;
import com.hanhhoai.orderfoodltdd.Database.OrderDatabase;
import com.hanhhoai.orderfoodltdd.Entity.Foody;
import com.hanhhoai.orderfoodltdd.Entity.Order;
import com.hanhhoai.orderfoodltdd.Model.Image;
import com.hanhhoai.orderfoodltdd.R;

import java.util.ArrayList;

public class HomeFragment extends Fragment {
    private GridView GridViewIntro;
    //private ListView GridViewIntro;
    private RecyclerView RecyclerViewFood;
    private ArrayList<Image> imageArrayList = new ArrayList<>();
    private ImagesAdapter images_adapter;
    private FoodAdapter foodAdapter;
    private com.hanhhoai.orderfoodltdd.ui.home.HomeViewModel homeViewModel;
    private EditText SearchView;
    Context context;
    private AppDatabase db;
    private ArrayList<Foody> foodies;
    private DataConvert dataConvert;
    public HomeFragment()
    {

    }
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) { homeViewModel
            = ViewModelProviders.of(this).get(com.hanhhoai.orderfoodltdd.ui.home.HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        GridViewIntro = root.findViewById(R.id.gv_intro);
        RecyclerViewFood = root.findViewById(R.id.rcl_food);
        SearchView = root.findViewById(R.id.sv_serch);
        SearchView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                filter(s.toString());
            }
        });
        mapping();

        dataConvert = new DataConvert(getContext());
        images_adapter = new ImagesAdapter(getContext(), R.layout.images_intro, imageArrayList);
        GridViewIntro.setAdapter(images_adapter);
        GridViewIntro.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getActivity(), "" + imageArrayList.get(position).getText(), Toast.LENGTH_SHORT).show();
                if(imageArrayList.get(position).getText() == "COMBO")
                {
                    foodies = selectArr("1");
                    foodAdapter = new FoodAdapter((ArrayList<Foody>) foodies, getContext());
                    RecyclerViewFood.setAdapter(foodAdapter);
                }
                else if (imageArrayList.get(position).getText() == "G??-BURGER-C??M")
                {
                    foodies = selectArr("2");
                    foodAdapter = new FoodAdapter((ArrayList<Foody>) foodies, getContext());
                    RecyclerViewFood.setAdapter(foodAdapter);
                }
                else if (imageArrayList.get(position).getText() == "TH???C U???NG")
                {
                    foodies = selectArr("3");
                    foodAdapter = new FoodAdapter((ArrayList<Foody>) foodies, getContext());
                    RecyclerViewFood.setAdapter(foodAdapter);
                }
                else if (imageArrayList.get(position).getText() == "M??N ??N K??M")
                {
                    foodies = selectArr("4");
                    foodAdapter = new FoodAdapter((ArrayList<Foody>) foodies, getContext());
                    RecyclerViewFood.setAdapter(foodAdapter);
                }
            }
        });


        db = AppDatabase.getInstance(getContext());

        foodies = FOODY_LIST();

        Log.d("SL", foodies.size() +""  );
        foodAdapter = new FoodAdapter((ArrayList<Foody>) foodies, getContext());
        RecyclerView.LayoutManager manager = new LinearLayoutManager(getContext());
        RecyclerViewFood.setLayoutManager(manager);
        RecyclerViewFood.setAdapter(foodAdapter);
        foodAdapter.setOnItemClickListener(new FoodAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(final int Position) {
                Toast.makeText(getContext(), ""+Position, Toast.LENGTH_SHORT).show();
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("B???n mu???n ?");
                builder.setPositiveButton("X??a", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ArrayList<Foody> foodies1 = (ArrayList<Foody>) db.daoFood().FOODY_LIST();
                        Foody foody= foodies1.get(Position);
                        db.daoFood().deleteFooy(foody);
                        try {
                            foodies = (ArrayList<Foody>) db.daoFood().FOODY_LIST();
                            foodAdapter = new FoodAdapter((ArrayList<Foody>) foodies, getContext());
                            RecyclerView.LayoutManager manager = new LinearLayoutManager(getContext());
                            RecyclerViewFood.setLayoutManager(manager);
                            RecyclerViewFood.setAdapter(foodAdapter);
                        }catch (Exception e){
                            Log.e("ERRO!",""+e);
                        }
                        Toast.makeText(getContext(), "???? x??a "+foody.getName(), Toast.LENGTH_SHORT).show();
                    }
                });
                builder.show();

            }

            @Override
            public void deleteItem(final int Position) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Mua h??ng");
                builder.setPositiveButton("Mua", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Foody foody = foodies.get(Position);
                        Order order = new Order();
                        order.setName(foody.getName());
                        order.setPrice(foody.getPrice());
                        order.setImage(foody.getImage());
                        OrderDatabase database = OrderDatabase.getInstance(getContext());
                        database.daoOrder().insertorder(order);
                        Toast.makeText(getContext(), "???? th??m v??o ????n h??ng ", Toast.LENGTH_SHORT).show();
                    }
                });
                builder.setNegativeButton("H???y", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.show();
            }
        });
        return root;
    }

    private void filter(String Text) {
        ArrayList<Foody> foodyList = new ArrayList<>();
        for (Foody foody : foodies) {
            if (foody.getName().toLowerCase().contains(Text.toLowerCase())) {
                foodyList.add(foody);
            }
        }

        foodAdapter.FilterList(foodyList);
    }

    private void mapping() {
        imageArrayList.add(new Image("COMBO", R.drawable.combo));
        imageArrayList.add(new Image("G??-BURGER-C??M", R.drawable.gaburgercom));
        imageArrayList.add(new Image("TH???C U???NG", R.drawable.thucuong));
        imageArrayList.add(new Image("M??N ??N K??M", R.drawable.monankem));
    }
    public ArrayList<Foody> selectArr(String loai)
    {
        ArrayList<Foody> arr = new ArrayList<>();
        for (Foody f : FOODY_LIST())
        {
            if(f.getCategory().contains(loai))
            {
                arr.add(f);
            }
        }
        return arr;
    }

    public ArrayList<Foody> FOODY_LIST(){
        ArrayList<Foody> tmp = new ArrayList<>();
        tmp.add(new Foody("Combo g?? gi??n cay",
                "1", 82.000
                ,"Combo C1" , DataConvert.ConvertImages(BitmapFactory.decodeResource(getContext().getResources(), R.drawable.combogacay))));
        tmp.add(new Foody("Combo g?? gi??n kh??ng cay",
                "1", 82.000
                ,"Combo C1" , DataConvert.ConvertImages(BitmapFactory.decodeResource(getContext().getResources(), R.drawable.combogakhongcay))));
        tmp.add(new Foody("Combo g?? quay ti??u",
                "1", 82.000
                ,"Combo C2" , DataConvert.ConvertImages(BitmapFactory.decodeResource(getContext().getResources(), R.drawable.combogaquaytieu))));
        tmp.add(new Foody("Combo g?? quay gi???y b???c",
                "1", 82.000
                ,"Combo C2" , DataConvert.ConvertImages(BitmapFactory.decodeResource(getContext().getResources(), R.drawable.combogaquaytieu))));
        tmp.add(new Foody("Combo TEENS CHOICE",
                "1", 63.000
                ,"Combo C3" , DataConvert.ConvertImages(BitmapFactory.decodeResource(getContext().getResources(), R.drawable.comboteenschoice))));
        tmp.add(new Foody("Combo KIDDIE",
                "1", 57.000
                ,"Combo C4" , DataConvert.ConvertImages(BitmapFactory.decodeResource(getContext().getResources(), R.drawable.combokiddie))));
        tmp.add(new Foody("Combo XL MEAL",
                "1", 101.000
                ,"Combo C5" , DataConvert.ConvertImages(BitmapFactory.decodeResource(getContext().getResources(), R.drawable.comboxlmeal))));
        tmp.add(new Foody("Combo 2 ng?????i",
                "1", 183.000
                ,"Combo C6" , DataConvert.ConvertImages(BitmapFactory.decodeResource(getContext().getResources(), R.drawable.combo2nguoi))));
        tmp.add(new Foody("Combo 3 ng?????i",
                "1", 254.000
                ,"Combo C7" , DataConvert.ConvertImages(BitmapFactory.decodeResource(getContext().getResources(), R.drawable.combo3nguoi))));

        tmp.add(new Foody("G?? gi??n cay",
                "2", 36.000
                ,"1 mi???ng" , DataConvert.ConvertImages(BitmapFactory.decodeResource(getContext().getResources(), R.drawable.gagioncay))));
        tmp.add(new Foody("G?? gi??n cay",
                "2", 68.000
                ,"2 mi???ng" , DataConvert.ConvertImages(BitmapFactory.decodeResource(getContext().getResources(), R.drawable.gagioncay))));
        tmp.add(new Foody("G?? gi??n cay",
                "2", 99.000
                ,"3 mi???ng" , DataConvert.ConvertImages(BitmapFactory.decodeResource(getContext().getResources(), R.drawable.gagioncay))));
        tmp.add(new Foody("G?? gi??n cay",
                "2", 195.000
                ,"6 mi???ng" , DataConvert.ConvertImages(BitmapFactory.decodeResource(getContext().getResources(), R.drawable.gagioncay))));
        tmp.add(new Foody("G?? gi??n cay",
                "2", 289.000
                ,"9 mi???ng" , DataConvert.ConvertImages(BitmapFactory.decodeResource(getContext().getResources(), R.drawable.gagioncay))));
        tmp.add(new Foody("G?? gi??n kh??ng cay",
                "2", 36.000
                ,"1 mi???ng" , DataConvert.ConvertImages(BitmapFactory.decodeResource(getContext().getResources(), R.drawable.gagionkhongcay))));
        tmp.add(new Foody("G?? gi??n kh??ng cay",
                "2", 68.000
                ,"2 mi???ng" , DataConvert.ConvertImages(BitmapFactory.decodeResource(getContext().getResources(), R.drawable.gagionkhongcay))));
        tmp.add(new Foody("G?? gi??n kh??ng cay",
                "2", 99.000
                ,"3 mi???ng" , DataConvert.ConvertImages(BitmapFactory.decodeResource(getContext().getResources(), R.drawable.gagionkhongcay))));
        tmp.add(new Foody("G?? gi??n kh??ng cay",
                "2", 195.000
                ,"6 mi???ng" , DataConvert.ConvertImages(BitmapFactory.decodeResource(getContext().getResources(), R.drawable.gagionkhongcay))));
        tmp.add(new Foody("G?? gi??n kh??ng cay",
                "2", 289.000
                ,"9 mi???ng" , DataConvert.ConvertImages(BitmapFactory.decodeResource(getContext().getResources(), R.drawable.gagionkhongcay))));
        tmp.add(new Foody("C??nh g?? hot WINGS",
                "2", 71.000
                ,"5 mi???ng" , DataConvert.ConvertImages(BitmapFactory.decodeResource(getContext().getResources(), R.drawable.canhga))));
        tmp.add(new Foody("????i g?? quay ti??u",
                "2", 68.000
                ,"1 mi???ng" , DataConvert.ConvertImages(BitmapFactory.decodeResource(getContext().getResources(), R.drawable.duigaquaytieu))));
        tmp.add(new Foody("????i g?? quay gi???y b???c",
                "2", 68.000
                ,"1 mi???ng" , DataConvert.ConvertImages(BitmapFactory.decodeResource(getContext().getResources(), R.drawable.duigaquaygiaybac))));

        tmp.add(new Foody("Burger ZINGER",
                "2", 51.000
                ,"1 c??i" , DataConvert.ConvertImages(BitmapFactory.decodeResource(getContext().getResources(), R.drawable.burgerzinger))));
        tmp.add(new Foody("Burger T??m",
                "2", 42.000
                ,"1 c??i" , DataConvert.ConvertImages(BitmapFactory.decodeResource(getContext().getResources(), R.drawable.burgerzinger))));
        tmp.add(new Foody("Burger g?? quay FLAVA",
                "2", 47.000
                ,"1 c??i" , DataConvert.ConvertImages(BitmapFactory.decodeResource(getContext().getResources(), R.drawable.burgergaquay))));

        tmp.add(new Foody("C??m g?? quay ti??u",
                "2", 41.000
                ,"1 ph???n" , DataConvert.ConvertImages(BitmapFactory.decodeResource(getContext().getResources(), R.drawable.comgaquaytieu))));
        tmp.add(new Foody("C??m g?? quay FLAVA",
                "2", 41.000
                ,"1 ph???n" , DataConvert.ConvertImages(BitmapFactory.decodeResource(getContext().getResources(), R.drawable.comgaquayflava))));
        tmp.add(new Foody("C??m g?? gi??n cay",
                "2", 41.000
                ,"1 ph???n" , DataConvert.ConvertImages(BitmapFactory.decodeResource(getContext().getResources(), R.drawable.comgagioncay))));
        tmp.add(new Foody("C??m g?? gi??n kh??ng cay",
                "2", 41.000
                ,"1 ph???n" , DataConvert.ConvertImages(BitmapFactory.decodeResource(getContext().getResources(), R.drawable.comgagionkhongcay))));
        tmp.add(new Foody("C??m g?? x??o s???t Nh???t",
                "2", 41.000
                ,"1 ph???n" , DataConvert.ConvertImages(BitmapFactory.decodeResource(getContext().getResources(), R.drawable.comgaxaosotnhat))));

        tmp.add(new Foody("Kem",
                "3", 5.000
                ,"Kem" , DataConvert.ConvertImages(BitmapFactory.decodeResource(getContext().getResources(), R.drawable.kem))));
        tmp.add(new Foody("Kem SUNDAE",
                "3", 12.000
                ,"Kem" , DataConvert.ConvertImages(BitmapFactory.decodeResource(getContext().getResources(), R.drawable.kemsundae))));
        tmp.add(new Foody("Tr?? ????o",
                "3", 24.000
                ,"N?????c" , DataConvert.ConvertImages(BitmapFactory.decodeResource(getContext().getResources(), R.drawable.tradao))));
        tmp.add(new Foody("Pepsi/7Up nh???",
                "3", 10.000
                ,"N?????c" , DataConvert.ConvertImages(BitmapFactory.decodeResource(getContext().getResources(), R.drawable.pepsi))));
        tmp.add(new Foody("Pepsi/7Up l???n",
                "3", 17.000
                ,"N?????c" , DataConvert.ConvertImages(BitmapFactory.decodeResource(getContext().getResources(), R.drawable.pepsi))));

        tmp.add(new Foody("B??nh tr???ng",
                "4", 17.000
                ,"1 c??i" , DataConvert.ConvertImages(BitmapFactory.decodeResource(getContext().getResources(), R.drawable.banhtrung))));
        tmp.add(new Foody("Salad",
                "4", 16.000
                ,"1 ph???n" , DataConvert.ConvertImages(BitmapFactory.decodeResource(getContext().getResources(), R.drawable.salad))));
        tmp.add(new Foody("Salad g??",
                "4", 20.000
                ,"1 ph???n" , DataConvert.ConvertImages(BitmapFactory.decodeResource(getContext().getResources(), R.drawable.saladga))));
        tmp.add(new Foody("Thanh c??",
                "4", 41.000
                ,"3 c??i" , DataConvert.ConvertImages(BitmapFactory.decodeResource(getContext().getResources(), R.drawable.thanhca))));
        tmp.add(new Foody("Khoai t??y chi??n",
                "4", 19.000
                ,"1 ph???n" , DataConvert.ConvertImages(BitmapFactory.decodeResource(getContext().getResources(), R.drawable.khoaitaychien))));
        tmp.add(new Foody("G?? PopCorn",
                "4", 57.000
                ,"1 ph???n" , DataConvert.ConvertImages(BitmapFactory.decodeResource(getContext().getResources(), R.drawable.gapopcorn))));

        return tmp;
    }


}
