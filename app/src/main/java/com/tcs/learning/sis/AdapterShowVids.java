package com.tcs.learning.sis;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.internal.view.menu.MenuView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.tcs.learning.sis.Network.VolleySingleton;

import java.util.ArrayList;

/**
 * Created by 883633 on 28-10-2015.
 */
public class AdapterShowVids extends RecyclerView.Adapter<AdapterShowVids.ViewHolderShowVids> {

    private ArrayList<VideoLinks> listVideoLinks=new ArrayList<>();
    private LayoutInflater layoutInflater;

    public static VideoLinks currentVideoLink;

    private VolleySingleton volleySingleton;
    private ImageLoader imageLoader;
    public static Context context;
    public AdapterShowVids(Context context){
        layoutInflater=LayoutInflater.from(context);
        volleySingleton=VolleySingleton.getsInstance();
        imageLoader=VolleySingleton.getImageLoader();

        this.context=context;
        currentVideoLink = null;
    }

    public void setListVideoLinks(ArrayList<VideoLinks> listVideoLinks){
        this.listVideoLinks=listVideoLinks;
        notifyItemRangeChanged(0,listVideoLinks.size());
    }

    @Override
    public ViewHolderShowVids onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=layoutInflater.inflate(R.layout.layout_card_view_video,parent,false);
        ViewHolderShowVids viewHolderShowVids=new ViewHolderShowVids(view);
        return viewHolderShowVids;
    }

    @Override
    public void onBindViewHolder(final ViewHolderShowVids holder, int position) {
        currentVideoLink=listVideoLinks.get(position);
        holder.textViewTitle.setText(currentVideoLink.getTitle());
        String urlThumbnail= currentVideoLink.getThumbnail();
        if(urlThumbnail!=null){
            imageLoader.get(urlThumbnail,new ImageLoader.ImageListener(){

                @Override
                public void onErrorResponse(VolleyError error) {

                }

                @Override
                public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
                    holder.imageViewThumb.setImageBitmap(response.getBitmap());
                }
            });
        }
        holder.textViewTime.setText("Duration: " + currentVideoLink.getTime());
    }

    @Override
    public int getItemCount() {
        return listVideoLinks.size();
    }

    static class ViewHolderShowVids extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView textViewTitle;
        public TextView textViewTime;
        public ImageView imageViewThumb;

        public ViewHolderShowVids(View itemView){
            super(itemView);
            itemView.setOnClickListener(this);
            textViewTitle = (TextView) itemView.findViewById(R.id.textViewTitle);
            imageViewThumb = (ImageView) itemView.findViewById(R.id.imageViewThumb);
            textViewTime = (TextView) itemView.findViewById(R.id.textViewDuration);
        }

        @Override
        public void onClick(View v) {
            Uri uri = Uri.parse(currentVideoLink.getUrlreceived());
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }
    }
}
