package com.example.whatmovietoday;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.squareup.picasso.Picasso;
import java.util.List;


public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.MyViewHolder> {
    List<ListItem> listItems;
    private Context context;
    private MovieDAO dao;

    public SearchAdapter(List<ListItem> listItems, Context context) {
        this.listItems = listItems;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_item,parent,false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        ListItem listItem =listItems.get(position);
        holder.textViewHead.setText(listItem.getHead());
        holder.textViewDesc.setText(listItem.getDesc());
        holder.hiddentxtID.setText(listItem.getID());
        Picasso.with(context).load(listItem.getImageUrl()).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView textViewHead;
        public TextView textViewDesc;
        public TextView hiddentxtID;
        public ImageView imageView;
        public Button btnSave;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewHead=(TextView)itemView.findViewById(R.id.textViewHead);
            textViewDesc=(TextView)itemView.findViewById(R.id.textViewDesc);
            imageView =(ImageView)itemView.findViewById((R.id.imageView));
            hiddentxtID = itemView.findViewById(R.id.hiddentxtID);
            btnSave = itemView.findViewById(R.id.btnRemove);
            btnSave.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    saveMovie(hiddentxtID.getText().toString());
                }
            });
        }
    }

    public void saveMovie(String id){
        User u = MainActivity.getUser();
        Movie selectedMovie = new Movie();
        selectedMovie.id = Integer.parseInt(id);
        selectedMovie.userId = u.id;

        dao = MainActivity.mDao;
        List<Movie> tmpTable = dao.getUserSaves(u.id);

        Boolean exists = false;
        for (Movie m : tmpTable){
            if (m.id == Integer.parseInt(id)){
                exists = true;
            }
        }
        if (!exists){dao.insert(selectedMovie);}
    }
}