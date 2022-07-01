package pe.edu.usat.laboratorio.appcomercial.adaptador;

import android.content.Context;
import android.text.TextUtils;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import pe.edu.usat.laboratorio.appcomercial.R;
import pe.edu.usat.laboratorio.appcomercial.logica.Tarifa;

public class AdaptadorTarifa extends RecyclerView.Adapter<AdaptadorTarifa.ViewHolder> {

    private Context context;
    public static ArrayList<Tarifa> listaTarifa;
    public int posicionItemSelecionadoRecyclerView;

    public AdaptadorTarifa(Context context) {
        this.context = context;
        listaTarifa = new ArrayList<Tarifa>();
    }

    @Override
    public AdaptadorTarifa.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_tarifa, parent, false);
        return new AdaptadorTarifa.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Tarifa tarifa = listaTarifa.get(position);
        holder.txtPrecioTarifa.setText("PRECIO TN/KM: " + tarifa.getPrecio());

        holder.txtPrecioTarifa.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        holder.txtPrecioTarifa.setSelected(true);
    }

    @Override
    public int getItemCount() {
        return listaTarifa.size();
    }

    public void cargarTarifa(ArrayList<Tarifa> lista){
        listaTarifa = lista;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener, View.OnCreateContextMenuListener{
        TextView txtPrecioTarifa;

        public ViewHolder(View itemView) {
            super(itemView);

            //Enlazar los controles JAVA con los controles XML del cardview
            txtPrecioTarifa = itemView.findViewById(R.id.txtPrecioTarifa);

            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
            itemView.setOnCreateContextMenuListener(this);
        }

        @Override
        public void onClick(View view) {

        }

        @Override
        public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
            contextMenu.setHeaderTitle("Opciones");
            contextMenu.add(0, 1, 0, "Eliminar");
        }

        @Override
        public boolean onLongClick(View view) {
            //obtener la posicion del item seleccionado en el RecyclerView
            posicionItemSelecionadoRecyclerView = this.getAdapterPosition();
            return false;
        }
    }

}

