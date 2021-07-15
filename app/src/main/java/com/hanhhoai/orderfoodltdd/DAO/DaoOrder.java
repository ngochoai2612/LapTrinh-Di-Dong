package com.hanhhoai.orderfoodltdd.DAO;



import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.hanhhoai.orderfoodltdd.Entity.Order;

import java.util.List;

@Dao
public interface DaoOrder {
    @Query("Select * from order_db")
    List<Order> ORDERS_LIST();

    @Query("Delete  from order_db")
    void deleteall();

    @Insert
    void insertorder(Order order);

    @Update
    void updateorder(  Order order);

    @Delete
    void deleteorder( Order order);

}
