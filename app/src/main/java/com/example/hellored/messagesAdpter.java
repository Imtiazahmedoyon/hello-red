package com.example.hellored;

import static com.example.hellored.chatwindo.reciverIImg;
import static com.example.hellored.chatwindo.senderImg;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class messagesAdpter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context context;
    ArrayList<msgModelclass> messagesAdpterArrayList;
    int ITEM_SEND = 1;
    int ITEM_RECIVE = 2;

    public messagesAdpter(Context context, ArrayList<msgModelclass> messagesAdpterArrayList) {
        this.context = context;
        this.messagesAdpterArrayList = messagesAdpterArrayList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == ITEM_SEND) {
            View view = LayoutInflater.from(context).inflate(R.layout.sender_layout, parent, false);
            return new senderViewHolder(view);
        } else {
            View view = LayoutInflater.from(context).inflate(R.layout.reciver_layout, parent, false);
            return new receiverViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        msgModelclass messages = messagesAdpterArrayList.get(position);

        holder.itemView.setOnLongClickListener(view -> {
            new AlertDialog.Builder(context)
                    .setTitle("Delete")
                    .setMessage("Are you sure you want to delete this message?")
                    .setPositiveButton("Yes", (dialogInterface, i) -> {
                        // Perform delete operation here
                    })
                    .setNegativeButton("No", (dialogInterface, i) -> {
                        dialogInterface.dismiss(); // Dismiss dialog
                    })
                    .show();
            return false;
        });

        if (holder.getClass() == senderViewHolder.class) {
            senderViewHolder viewHolder = (senderViewHolder) holder;
            viewHolder.msgtxt.setText(messages.getMessage());
            Picasso.get().load(senderImg).into(viewHolder.circleImageView);
        } else {
            receiverViewHolder viewHolder = (receiverViewHolder) holder;
            viewHolder.msgtxt.setText(messages.getMessage());
            Picasso.get().load(reciverIImg).into(viewHolder.circleImageView);
        }
    }

    @Override
    public int getItemCount() {
        return messagesAdpterArrayList.size();
    }

    @Override
    public int getItemViewType(int position) {
        msgModelclass messages = messagesAdpterArrayList.get(position);
        if (FirebaseAuth.getInstance().getCurrentUser().getUid().equals(messages.getSenderid())) {
            return ITEM_SEND;
        } else {
            return ITEM_RECIVE;
        }
    }

    // Sender ViewHolder
    static class senderViewHolder extends RecyclerView.ViewHolder {
        CircleImageView circleImageView;
        TextView msgtxt;

        public senderViewHolder(@NonNull View itemView) {
            super(itemView);
            circleImageView = itemView.findViewById(R.id.profilerggg);
            msgtxt = itemView.findViewById(R.id.msgsendertyp);
        }
    }

    // Receiver ViewHolder
    class receiverViewHolder extends RecyclerView.ViewHolder {
         CircleImageView circleImageView;
        TextView msgtxt;

        public receiverViewHolder(@NonNull View itemView) {
            super(itemView);
            circleImageView = itemView.findViewById(R.id.pro);
            msgtxt = itemView.findViewById(R.id.recivertextset);
        }
    }
}
