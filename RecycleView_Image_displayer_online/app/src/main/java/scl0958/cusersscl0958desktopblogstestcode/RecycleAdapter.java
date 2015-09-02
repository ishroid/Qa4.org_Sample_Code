package scl0958.cusersscl0958desktopblogstestcode;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;

/**
 * Created by SCL0958 on 02-09-2015.
 */

public class RecycleAdapter extends RecyclerView.Adapter<RecycleAdapter.ViewHolderRec> {

    List<HashMap<String, String>> onlineData;
    Context context;
    RecycleAdapter(Context context,List<HashMap<String, String>> onlineData){
        this.onlineData = onlineData;
        this.context=context;
    }

    @Override
    public ViewHolderRec onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolderRec( LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recycle, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolderRec holder, int position) {
        HashMap<String,String> map =onlineData.get(position);

        //Download image using picasso library
        Picasso.with(context).load(map.get("thump"))
                .error(R.drawable.placeholder)
                .placeholder(R.drawable.placeholder)
                .into(holder.iv);

        holder.tv.setText(map.get("title"));

    }

    @Override
    public int getItemCount() {
        return onlineData.size();
    }

    public class ViewHolderRec extends RecyclerView.ViewHolder {
        ImageView iv;
        TextView tv;
        CardView cardItemLayout;

        public ViewHolderRec(View itemView) {
            super(itemView);
            iv = (ImageView) itemView.findViewById(R.id.thumbnail);
            tv = (TextView) itemView.findViewById(R.id.title);
            cardItemLayout = (CardView) itemView.findViewById(R.id.cardlist_item);
        }
    }
}
