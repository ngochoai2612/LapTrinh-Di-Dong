package com.hanhhoai.orderfoodltdd.ui.Me;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;


import com.hanhhoai.orderfoodltdd.Activity.MainActivity;
import com.hanhhoai.orderfoodltdd.DAO.AppDatabase;
import com.hanhhoai.orderfoodltdd.Database.AccountDatabase;
import com.hanhhoai.orderfoodltdd.Database.BuyDatabase;
import com.hanhhoai.orderfoodltdd.Entity.Account;
import com.hanhhoai.orderfoodltdd.Entity.Buy;
import com.hanhhoai.orderfoodltdd.Entity.Foody;
import com.hanhhoai.orderfoodltdd.R;

import java.util.ArrayList;
import java.util.List;

public class MeFragment extends Fragment {
private Button Button_logout;
private TextView TextView_name_me;
private ListView ListView_me;
TextView TextViewThunhap;
TextView TextViewSoluong;
TextView TextViewSoluongdangban;
TextView TextViewSoluongtaikhoan;
Button buttondsacc;
    private com.hanhhoai.orderfoodltdd.ui.Me.MeViewModel meViewModel;
    private Context context;
    ArrayList <String> arr = new ArrayList<>();
    public View onCreateView(@NonNull LayoutInflater inflater,
    ViewGroup container, Bundle savedInstanceState) {
        meViewModel =
        ViewModelProviders.of(this).get(com.hanhhoai.orderfoodltdd.ui.Me.MeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_me, container, false);
        Button_logout=root.findViewById(R.id.btn_logout);
        Button_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent= new Intent(getActivity(), MainActivity.class);
                startActivity(intent);
            }
        });
        TextViewThunhap= root.findViewById(R.id.tv_thunhap);
        TextViewSoluong= root.findViewById(R.id.tv_soluong);
        TextViewSoluongdangban=root.findViewById(R.id.tv_soluongdangban);
        TextViewSoluongtaikhoan=root.findViewById(R.id.tv_soluongtaikhoan);
        AccountDatabase database3 = AccountDatabase.getInstance(getContext());
        List<Account> accounts = database3.daoAccount().ACCOUNT_LIST();
        TextViewSoluongtaikhoan.setText("Tài khoản đang hoạt động "+accounts.size());


        BuyDatabase database = BuyDatabase.getInstance(getContext());
        AppDatabase database1 = AppDatabase.getInstance(getContext());
        List<Foody> foodyList = database1.daoFood().FOODY_LIST();
        TextViewSoluongdangban.setText("Số lượng mặt hàng đang bán : "+foodyList.size());

      List<Buy> buyList = database.daoBuy().BUY_LIST();
      double tn= 0.000;
      for (Buy b : buyList){
          tn += b.getPrice();
      }
      TextViewThunhap.setText("Tổng thu nhập : "+tn+" Đ");
      TextViewSoluong.setText("Số lượng đơn hàng đã bán : "+buyList.size());
        return root;
    }
}