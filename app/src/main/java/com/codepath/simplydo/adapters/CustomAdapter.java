package com.codepath.simplydo.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.codepath.simplydo.BuildConfig;
import com.codepath.simplydo.Constants;
import com.codepath.simplydo.R;
import com.codepath.simplydo.activities.EditItemActivity;
import com.codepath.simplydo.model.Item;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static com.codepath.simplydo.activities.MainActivity.EXTRA_MESSAGE_POS;
import static com.codepath.simplydo.activities.MainActivity.REQUEST_CODE_EDIT;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder> {
    private List<Item> itemList;
    Context context;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tvTaskNameCard;
        public TextView tvPriorityLabelCard;
        public TextView tvDueDateCard;
        public View vPriorityBar;

        public ViewHolder(View v) {
            super(v);
            tvTaskNameCard = ((TextView) v.findViewById(R.id.tvTaskNameCard));
            tvPriorityLabelCard = ((TextView) v.findViewById(R.id.tvPriorityCard));
            tvDueDateCard = ((TextView) v.findViewById(R.id.tvDueDateCard));
            vPriorityBar = v.findViewById(R.id.vPriorityBar);
        }
    }

    public CustomAdapter(List<Item> itemList, Context context) {
        this.itemList = itemList;
        this.context = context;
    }

    @Override
    public CustomAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_view, parent, false);

        return new ViewHolder(v);
    }


    // Replace the contents of card_view view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        // Set the task name
        holder.tvTaskNameCard.setText(itemList.get(position).getDesc());

        // Set the priority and associated color
        String priority = itemList.get(position).getPriority();

        int hpc = ContextCompat.getColor(context, R.color.colorHighPriority);
        int mpc = ContextCompat.getColor(context, R.color.colorMediumPriority);
        int lpc = ContextCompat.getColor(context, R.color.colorLowPriority);

        int color = priority.equalsIgnoreCase("High") ?
                hpc: priority.equalsIgnoreCase("Medium") ? mpc : lpc;

        holder.tvPriorityLabelCard.setText(priority);
        holder.tvPriorityLabelCard.setTextColor(color);

        // Set due date.
        Date dueDate = itemList.get(position).getDueDate();

        Calendar cal = Calendar.getInstance();
        cal.setTime(dueDate);

        holder.tvDueDateCard.setText((cal.get(Calendar.MONTH) + 1) + "/"
                        + cal.get(Calendar.DAY_OF_MONTH)+ "/"
                        + cal.get(Calendar.YEAR));



        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int position = holder.getAdapterPosition();
                Item item = itemList.get(position);

                Bundle bundle = new Bundle();
                bundle.putSerializable("item", ((Serializable) item));

                Intent intent = new Intent(context, EditItemActivity.class);
                intent.putExtras(bundle);
                intent.putExtra(EXTRA_MESSAGE_POS, position);

                if (BuildConfig.DEBUG) Log.e(Constants.LOG, "Item clicked. Item: " + item + " at position: " + position);

                ((Activity) context).startActivityForResult(intent, REQUEST_CODE_EDIT);
            }
        });


        // set color of priority bar.
        holder.vPriorityBar.setBackgroundColor(color);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return itemList.size();
    }
}

