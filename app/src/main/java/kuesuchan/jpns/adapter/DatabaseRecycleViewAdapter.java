package kuesuchan.jpns.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import kuesuchan.jpns.R;
import kuesuchan.jpns.database.AppDatabase;
import kuesuchan.jpns.database.entity.KanjiWriting;
import kuesuchan.jpns.database.entity.Vocabulary;
import kuesuchan.jpns.dialog.DatabaseInputDialog;

public class DatabaseRecycleViewAdapter extends RecyclerView.Adapter<DatabaseRecycleViewAdapter.ViewHolder> {

    private List<? extends Object> objects = new ArrayList<>();
    private Context context;
    public DatabaseRecycleViewAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_database, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Object selectedRow = objects.get(position);
        holder.rowInfo.setText(objects.get(position).toString());
        holder.databaseListItemParent.setOnLongClickListener(view -> {
            if (selectedRow instanceof Vocabulary){
                DatabaseInputDialog dialog = new DatabaseInputDialog(context, objects.get(position) , AppDatabase.SearchableTable.Vocabulary);
                dialog.show();
            } else if ( selectedRow instanceof KanjiWriting) {
                DatabaseInputDialog dialog = new DatabaseInputDialog(context, selectedRow, AppDatabase.SearchableTable.Kanji_Writing);
                dialog.show();
           }

            return true;
        });
    }

    @Override
    public int getItemCount() {
        return objects.size();
    }

    public void setObjects(List<? extends Object> objects) {
        this.objects = objects;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private TextView rowInfo;
        private LinearLayout databaseListItemParent;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            rowInfo = itemView.findViewById(R.id.rowInfoTextView);
            databaseListItemParent = itemView.findViewById(R.id.databaseListItemParent);
        }
    }


}
