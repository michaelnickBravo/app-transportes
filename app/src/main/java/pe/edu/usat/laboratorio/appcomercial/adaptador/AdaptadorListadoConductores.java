package pe.edu.usat.laboratorio.appcomercial.adaptador;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Build;
import android.text.TextUtils;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import pe.edu.usat.laboratorio.appcomercial.R;
import pe.edu.usat.laboratorio.appcomercial.logica.Conductor;
import pe.edu.usat.laboratorio.appcomercial.logica.Producto;
import pe.edu.usat.laboratorio.appcomercial.util.Helper;

public class AdaptadorListadoConductores extends RecyclerView.Adapter<AdaptadorListadoConductores.ViewHolder>{

    private Context context;
    public static ArrayList<Conductor> listaConductor;
    public static ArrayList<Conductor> listaFiltrada;
    public int posicionItemSelecionadoRecyclerView;

    public AdaptadorListadoConductores(Context context) {
        this.context = context;
        listaConductor = new ArrayList<Conductor>();
        listaFiltrada = new ArrayList<Conductor>();
        listaFiltrada.addAll(listaConductor);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //Realizar el enlace entre el adaptador y el archivo XML que contiene al cardview
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_conductor_listado, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AdaptadorListadoConductores.ViewHolder holder, int position) {
        //Mostrar los datos de cada producto en los controles del cardview
        Conductor conductor = listaConductor.get(position);
        holder.txtNombreConductorList.setText(conductor.getNombre());
        holder.txtBreveteList.setText(conductor.getNum_brevete());
        holder.txtEmailList.setText(conductor.getEmail());

        holder.txtNombreConductorList.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        holder.txtNombreConductorList.setSelected(true);

        holder.txtBreveteList.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        holder.txtBreveteList.setSelected(true);

        holder.txtEmailList.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        holder.txtEmailList.setSelected(true);

    }

    public void filtrado(String txtBusqueda){
        int longitud = txtBusqueda.length();
        if(longitud == 0){
            listaConductor.clear();
            listaConductor.addAll(listaFiltrada);
        }else{
            List<Conductor> con = listaConductor.stream().filter(i -> i.getNum_brevete().toUpperCase().contains(txtBusqueda.toUpperCase())).collect(Collectors.toList());
            List<Conductor> con1 = listaConductor.stream().filter(i -> i.getNombre().toUpperCase().contains(txtBusqueda.toUpperCase())).collect(Collectors.toList());
            listaConductor.clear();
            listaConductor.addAll(con);
            listaConductor.addAll(con1);
            notifyDataSetChanged();
        }
    }

    @Override
    public int getItemCount() {
        return listaConductor.size();
    }

    public void cargarConductores(ArrayList<Conductor> lista){
        listaConductor = lista;
        notifyDataSetChanged();
    }



    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnCreateContextMenuListener, View.OnLongClickListener {
        TextView txtNombreConductorList, txtBreveteList, txtEmailList;

        public ViewHolder(View itemView) {
            super(itemView);

            //Enlazar los controles JAVA con los controles XML del cardview
            txtNombreConductorList = itemView.findViewById(R.id.txtNombreConductorList);
            txtBreveteList = itemView.findViewById(R.id.txtBreveteList);
            txtEmailList = itemView.findViewById(R.id.txtEmailList);
            itemView.setOnClickListener(this);

            itemView.setOnCreateContextMenuListener(this);
            itemView.setOnLongClickListener(this);
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
