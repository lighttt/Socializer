package np.com.manishtuladhar.socializer;

import android.database.Cursor;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Date;

import np.com.manishtuladhar.socializer.provider.SocializerContract;

public class SocializerAdapter extends RecyclerView.Adapter<SocializerAdapter.SocializerViewHolder> {

    //data
    private Cursor mData;

    //dates
    private static final long MINUTE_MILLIS = 1000 * 60;
    private static final long HOUR_MILLIS =60 * MINUTE_MILLIS;
    private static final long DAY_MILLIS = 24 * HOUR_MILLIS;

    @NonNull
    @Override
    public SocializerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate((R.layout.item_socializer_list),parent,false);
        return new SocializerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SocializerViewHolder holder, int position) {
        mData.moveToPosition(position);

        String message = mData.getString(MainActivity.COL_NUM_MESSAGE);
        String author = mData.getString(MainActivity.COL_NUM_AUTHOR);
        String authorKey = mData.getString(MainActivity.COL_NUM_AUTHOR_KEY);

        //date display
        long dateMillis = mData.getLong(MainActivity.COL_NUM_DATE);
        String date = "";
        long now = System.currentTimeMillis();

        if(now - dateMillis < (DAY_MILLIS))
        {
            if(now - dateMillis< (HOUR_MILLIS))
            {
                long minutes = Math.round((now-dateMillis) / MINUTE_MILLIS);
                date = String.valueOf(minutes) + "m";
            }
            else{
                long hours = Math.round((now-dateMillis)/HOUR_MILLIS);
                date = String.valueOf(hours) +"h";
            }
        }
        else{
            Date dateDate = new Date(dateMillis);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MM");
            date = simpleDateFormat.format(dateDate);
        }
        holder.messageTV.setText(message);
        holder.authorTV.setText(author);
        holder.dateTV.setText(date);

        switch (authorKey)
        {
            case SocializerContract
                    .MANISH_KEY:
                holder.authorIV.setImageResource(R.drawable.profile);
            break;

            case SocializerContract
                    .SAMIP_KEY:
                holder.authorIV.setImageResource(R.drawable.profile);
                break;

            case SocializerContract
                    .RAM_KEY:
                holder.authorIV.setImageResource(R.drawable.profile);
                break;

            case SocializerContract
                    .HARI_KEY:
                holder.authorIV.setImageResource(R.drawable.profile);
                break;

            case SocializerContract
                    .SHYAM_KEY:
                holder.authorIV.setImageResource(R.drawable.profile);
                break;
            default:
                holder.authorIV.setImageResource(R.drawable.profile);
        }
    }

    @Override
    public int getItemCount() {
        if(mData == null)
        {
            return 0;
        }
        return mData.getCount();
    }

    public void swapCursor(Cursor newCursor)
    {
        mData= newCursor;
        notifyDataSetChanged();
    }

    public class SocializerViewHolder extends RecyclerView.ViewHolder {
        final TextView authorTV;
        final TextView messageTV;
        final TextView dateTV;
        final ImageView authorIV;

        public SocializerViewHolder(@NonNull View itemView) {
            super(itemView);
            authorTV = itemView.findViewById(R.id.author_tv);
            messageTV = itemView.findViewById(R.id.message_tv);
            dateTV = itemView.findViewById(R.id.date_tv);
            authorIV = itemView.findViewById(R.id.author_iv);
        }
    }
}
