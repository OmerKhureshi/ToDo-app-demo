package com.codepath.simplydo.model;


import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

import java.io.Serializable;
import java.util.Date;


/**
 * This class represents each item in the list.
 * This is also mapped as an entity to be created as card_view table in database.
 * */
@Table(database = MyDatabase.class)
public class Item extends BaseModel implements Serializable{
    @Column
    @PrimaryKey (autoincrement=true)
    public int id;

    @Column
    public String desc;

    @Column
    public Date dueDate;

    @Column
    public String priority;

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

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }
}
