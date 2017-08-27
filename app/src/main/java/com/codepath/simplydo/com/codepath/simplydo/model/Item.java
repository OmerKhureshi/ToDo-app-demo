package com.codepath.simplydo.com.codepath.simplydo.model;


import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

import java.io.Serializable;

@Table(database = MyDatabase.class)
public class Item extends BaseModel implements Serializable{
    @Column
    @PrimaryKey (autoincrement=true)
    int id;

    @Column
    public String desc;


    @Override
    public String toString() {
        return desc;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
