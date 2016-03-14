package com.tcs.learning.sis;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by 883633 on 26-10-2015.
 */
public class AdapterShowResults extends RecyclerView.Adapter<AdapterShowResults.ViewHolderShowResults> {


    private ArrayList<TextLinks> listTextLinks=new ArrayList<>();
    private LayoutInflater layoutInflater;
    public static TextLinks currentTextLink;
    public static Context context;
    public AdapterShowResults(Context context){
        layoutInflater=layoutInflater.from(context);
        this.context=context;
        currentTextLink=null;
    }

    public void setListTextLinks(ArrayList<TextLinks> listTextLinks){
        this.listTextLinks=listTextLinks;
        notifyItemRangeChanged(0, listTextLinks.size());

    }

    @Override
    public ViewHolderShowResults onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=layoutInflater.inflate(R.layout.layout_card_view, parent, false);
        ViewHolderShowResults viewHolder=new ViewHolderShowResults(view);
        return viewHolder;
    }



    @Override
    public void onBindViewHolder(ViewHolderShowResults holder, int position) {

        currentTextLink=listTextLinks.get(position);
        holder.textViewTitle.setText(currentTextLink.getTitle());
        holder.textViewUrl.setText(currentTextLink.getUrlreceived());
        holder.textViewTime.setText("Time required is: " + currentTextLink.getTime() + " minutes");


    }

    @Override
    public int getItemCount() {
        return listTextLinks.size();
    }


    static class ViewHolderShowResults extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView textViewTitle;
        public TextView textViewTime;
        public TextView textViewUrl;

        public ViewHolderShowResults(View itemView){
            super(itemView);
            itemView.setOnClickListener(this);
            textViewTitle = (TextView) itemView.findViewById(R.id.textViewTitle);
            textViewUrl = (TextView) itemView.findViewById(R.id.textViewUrl);
            textViewTime = (TextView) itemView.findViewById(R.id.textViewDuration);
        }

        @Override
        public void onClick(View v) {
            Uri uri = Uri.parse(currentTextLink.getUrlreceived());
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }
    }
}
