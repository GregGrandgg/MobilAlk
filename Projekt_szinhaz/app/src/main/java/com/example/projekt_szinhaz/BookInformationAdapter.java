package com.example.projekt_szinhaz;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class BookInformationAdapter extends RecyclerView.Adapter<BookInformationAdapter.ViewHolder> implements Filterable {
    private ArrayList<BookInformation> mBookInformationData = new ArrayList<>();
    private ArrayList<BookInformation> mBookInformationDataAll = new ArrayList<>();
    private Context mContext;
    private int lastPosition = -1;

    BookInformationAdapter(Context context, ArrayList<BookInformation> itemsData){
        this.mBookInformationData = itemsData;
        this.mBookInformationDataAll =itemsData;
        this.mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(BookInformationAdapter.ViewHolder holder, int position) {
        BookInformation currentItem = mBookInformationData.get(position);

        holder.bindTo(currentItem);

        if(holder.getAdapterPosition() > lastPosition) {
            Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.slide);
            Animation animation2 = AnimationUtils.loadAnimation(mContext, R.anim.rotate);
            holder.itemView.startAnimation(animation);
            holder.itemView.startAnimation(animation2);
            lastPosition = holder.getAdapterPosition();
        }
    }

    @Override
    public int getItemCount() {
        return mBookInformationData.size();
    }

    @Override
    public Filter getFilter() {
        return ShopFilter;
    }
    private Filter ShopFilter = new Filter(){

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            ArrayList<BookInformation> filteredList = new ArrayList<>();
            FilterResults results = new FilterResults();

            if(constraint == null || constraint.length() == 0) {
                results.count = mBookInformationDataAll.size();
                results.values = mBookInformationDataAll;
            }else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for(BookInformation item : mBookInformationDataAll) {
                    if(item.getNev().toLowerCase().contains(filterPattern)){
                        filteredList.add(item);
                    }
                }

                results.count = filteredList.size();
                results.values = filteredList;
            }

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            mBookInformationDataAll = (ArrayList)results.values;
            notifyDataSetChanged();
        }
    };

    class ViewHolder extends RecyclerView.ViewHolder{
        private TextView mTitleText;
        private TextView mInfoText;
        private TextView mPriceText;
        private ImageView mItemImage;

        public ViewHolder(View itemView) {
            super(itemView);

            mTitleText = itemView.findViewById(R.id.itemTitle);
            mInfoText = itemView.findViewById(R.id.subTitle);
            mItemImage = itemView.findViewById(R.id.itemImage);
            mPriceText = itemView.findViewById(R.id.price);

            itemView.findViewById(R.id.add_to_cart).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d("Activity","Kosarhoz adas gomb lenyomva");
                    ((BookListActivity)mContext).updateAlertIcon();
                }
            });

        }

        public void bindTo(BookInformation currentItem) {
            mTitleText.setText(currentItem.getNev());
            mInfoText.setText(currentItem.getInfo());
            mPriceText.setText(currentItem.getAr());

            Glide.with(mContext).load(currentItem.getImageRes()).into(mItemImage);
        }
    }
}


