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
                else if (imageArrayList.get(position).getText() == "GÀ-BURGER-CƠM")
                {
                    foodies = selectArr("2");
                    foodAdapter = new FoodAdapter((ArrayList<Foody>) foodies, getContext());
                    RecyclerViewFood.setAdapter(foodAdapter);
                }
                else if (imageArrayList.get(position).getText() == "THỨC UỐNG")
                {
                    foodies = selectArr("3");
                    foodAdapter = new FoodAdapter((ArrayList<Foody>) foodies, getContext());
                    RecyclerViewFood.setAdapter(foodAdapter);
                }
                else if (imageArrayList.get(position).getText() == "MÓN ĂN KÈM")
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
                builder.setTitle("Bạn muốn ?");
                builder.setPositiveButton("Xóa", new DialogInterface.OnClickListener() {
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
                        Toast.makeText(getContext(), "Đã xóa "+foody.getName(), Toast.LENGTH_SHORT).show();
                    }
                });
                builder.show();

            }

            @Override
            public void deleteItem(final int Position) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Mua hàng");
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
                        Toast.makeText(getContext(), "Đã thêm vào đơn hàng ", Toast.LENGTH_SHORT).show();
                    }
                });
                builder.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
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
        imageArrayList.add(new Image("GÀ-BURGER-CƠM", R.drawable.gaburgercom));
        imageArrayList.add(new Image("THỨC UỐNG", R.drawable.thucuong));
        imageArrayList.add(new Image("MÓN ĂN KÈM", R.drawable.monankem));
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
        tmp.add(new Foody("Combo gà giòn cay",
                "1", 82.000
                ,"Combo C1" , DataConvert.ConvertImages(BitmapFactory.decodeResource(getContext().getResources(), R.drawable.combogacay))));
        tmp.add(new Foody("Combo gà giòn không cay",
                "1", 82.000
                ,"Combo C1" , DataConvert.ConvertImages(BitmapFactory.decodeResource(getContext().getResources(), R.drawable.combogakhongcay))));
        tmp.add(new Foody("Combo gà quay tiêu",
                "1", 82.000
                ,"Combo C2" , DataConvert.ConvertImages(BitmapFactory.decodeResource(getContext().getResources(), R.drawable.combogaquaytieu))));
        tmp.add(new Foody("Combo gà quay giấy bạc",
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
        tmp.add(new Foody("Combo 2 người",
                "1", 183.000
                ,"Combo C6" , DataConvert.ConvertImages(BitmapFactory.decodeResource(getContext().getResources(), R.drawable.combo2nguoi))));
        tmp.add(new Foody("Combo 3 người",
                "1", 254.000
                ,"Combo C7" , DataConvert.ConvertImages(BitmapFactory.decodeResource(getContext().getResources(), R.drawable.combo3nguoi))));

        tmp.add(new Foody("Gà giòn cay",
                "2", 36.000
                ,"1 miếng" , DataConvert.ConvertImages(BitmapFactory.decodeResource(getContext().getResources(), R.drawable.gagioncay))));
        tmp.add(new Foody("Gà giòn cay",
                "2", 68.000
                ,"2 miếng" , DataConvert.ConvertImages(BitmapFactory.decodeResource(getContext().getResources(), R.drawable.gagioncay))));
        tmp.add(new Foody("Gà giòn cay",
                "2", 99.000
                ,"3 miếng" , DataConvert.ConvertImages(BitmapFactory.decodeResource(getContext().getResources(), R.drawable.gagioncay))));
        tmp.add(new Foody("Gà giòn cay",
                "2", 195.000
                ,"6 miếng" , DataConvert.ConvertImages(BitmapFactory.decodeResource(getContext().getResources(), R.drawable.gagioncay))));
        tmp.add(new Foody("Gà giòn cay",
                "2", 289.000
                ,"9 miếng" , DataConvert.ConvertImages(BitmapFactory.decodeResource(getContext().getResources(), R.drawable.gagioncay))));
        tmp.add(new Foody("Gà giòn không cay",
                "2", 36.000
                ,"1 miếng" , DataConvert.ConvertImages(BitmapFactory.decodeResource(getContext().getResources(), R.drawable.gagionkhongcay))));
        tmp.add(new Foody("Gà giòn không cay",
                "2", 68.000
                ,"2 miếng" , DataConvert.ConvertImages(BitmapFactory.decodeResource(getContext().getResources(), R.drawable.gagionkhongcay))));
        tmp.add(new Foody("Gà giòn không cay",
                "2", 99.000
                ,"3 miếng" , DataConvert.ConvertImages(BitmapFactory.decodeResource(getContext().getResources(), R.drawable.gagionkhongcay))));
        tmp.add(new Foody("Gà giòn không cay",
                "2", 195.000
                ,"6 miếng" , DataConvert.ConvertImages(BitmapFactory.decodeResource(getContext().getResources(), R.drawable.gagionkhongcay))));
        tmp.add(new Foody("Gà giòn không cay",
                "2", 289.000
                ,"9 miếng" , DataConvert.ConvertImages(BitmapFactory.decodeResource(getContext().getResources(), R.drawable.gagionkhongcay))));
        tmp.add(new Foody("Cánh gà hot WINGS",
                "2", 71.000
                ,"5 miếng" , DataConvert.ConvertImages(BitmapFactory.decodeResource(getContext().getResources(), R.drawable.canhga))));
        tmp.add(new Foody("Đùi gà quay tiêu",
                "2", 68.000
                ,"1 miếng" , DataConvert.ConvertImages(BitmapFactory.decodeResource(getContext().getResources(), R.drawable.duigaquaytieu))));
        tmp.add(new Foody("Đùi gà quay giấy bạc",
                "2", 68.000
                ,"1 miếng" , DataConvert.ConvertImages(BitmapFactory.decodeResource(getContext().getResources(), R.drawable.duigaquaygiaybac))));

        tmp.add(new Foody("Burger ZINGER",
                "2", 51.000
                ,"1 cái" , DataConvert.ConvertImages(BitmapFactory.decodeResource(getContext().getResources(), R.drawable.burgerzinger))));
        tmp.add(new Foody("Burger Tôm",
                "2", 42.000
                ,"1 cái" , DataConvert.ConvertImages(BitmapFactory.decodeResource(getContext().getResources(), R.drawable.burgerzinger))));
        tmp.add(new Foody("Burger gà quay FLAVA",
                "2", 47.000
                ,"1 cái" , DataConvert.ConvertImages(BitmapFactory.decodeResource(getContext().getResources(), R.drawable.burgergaquay))));

        tmp.add(new Foody("Cơm gà quay tiêu",
                "2", 41.000
                ,"1 phần" , DataConvert.ConvertImages(BitmapFactory.decodeResource(getContext().getResources(), R.drawable.comgaquaytieu))));
        tmp.add(new Foody("Cơm gà quay FLAVA",
                "2", 41.000
                ,"1 phần" , DataConvert.ConvertImages(BitmapFactory.decodeResource(getContext().getResources(), R.drawable.comgaquayflava))));
        tmp.add(new Foody("Cơm gà giòn cay",
                "2", 41.000
                ,"1 phần" , DataConvert.ConvertImages(BitmapFactory.decodeResource(getContext().getResources(), R.drawable.comgagioncay))));
        tmp.add(new Foody("Cơm gà giòn không cay",
                "2", 41.000
                ,"1 phần" , DataConvert.ConvertImages(BitmapFactory.decodeResource(getContext().getResources(), R.drawable.comgagionkhongcay))));
        tmp.add(new Foody("Cơm gà xào sốt Nhật",
                "2", 41.000
                ,"1 phần" , DataConvert.ConvertImages(BitmapFactory.decodeResource(getContext().getResources(), R.drawable.comgaxaosotnhat))));

        tmp.add(new Foody("Kem",
                "3", 5.000
                ,"Kem" , DataConvert.ConvertImages(BitmapFactory.decodeResource(getContext().getResources(), R.drawable.kem))));
        tmp.add(new Foody("Kem SUNDAE",
                "3", 12.000
                ,"Kem" , DataConvert.ConvertImages(BitmapFactory.decodeResource(getContext().getResources(), R.drawable.kemsundae))));
        tmp.add(new Foody("Trà đào",
                "3", 24.000
                ,"Nước" , DataConvert.ConvertImages(BitmapFactory.decodeResource(getContext().getResources(), R.drawable.tradao))));
        tmp.add(new Foody("Pepsi/7Up nhỏ",
                "3", 10.000
                ,"Nước" , DataConvert.ConvertImages(BitmapFactory.decodeResource(getContext().getResources(), R.drawable.pepsi))));
        tmp.add(new Foody("Pepsi/7Up lớn",
                "3", 17.000
                ,"Nước" , DataConvert.ConvertImages(BitmapFactory.decodeResource(getContext().getResources(), R.drawable.pepsi))));

        tmp.add(new Foody("Bánh trứng",
                "4", 17.000
                ,"1 cái" , DataConvert.ConvertImages(BitmapFactory.decodeResource(getContext().getResources(), R.drawable.banhtrung))));
        tmp.add(new Foody("Salad",
                "4", 16.000
                ,"1 phần" , DataConvert.ConvertImages(BitmapFactory.decodeResource(getContext().getResources(), R.drawable.salad))));
        tmp.add(new Foody("Salad gà",
                "4", 20.000
                ,"1 phần" , DataConvert.ConvertImages(BitmapFactory.decodeResource(getContext().getResources(), R.drawable.saladga))));
        tmp.add(new Foody("Thanh cá",
                "4", 41.000
                ,"3 cái" , DataConvert.ConvertImages(BitmapFactory.decodeResource(getContext().getResources(), R.drawable.thanhca))));
        tmp.add(new Foody("Khoai tây chiên",
                "4", 19.000
                ,"1 phần" , DataConvert.ConvertImages(BitmapFactory.decodeResource(getContext().getResources(), R.drawable.khoaitaychien))));
        tmp.add(new Foody("Gà PopCorn",
                "4", 57.000
                ,"1 phần" , DataConvert.ConvertImages(BitmapFactory.decodeResource(getContext().getResources(), R.drawable.gapopcorn))));

        return tmp;
    }


}
