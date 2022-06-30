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
import java.util.List;
import java.util.stream.Collectors;

import pe.edu.usat.laboratorio.appcomercial.R;
import pe.edu.usat.laboratorio.appcomercial.logica.Conductor;
import pe.edu.usat.laboratorio.appcomercial.logica.Vehiculo;

public class AdaptadorListadoVehiculos extends RecyclerView.Adapter<AdaptadorListadoVehiculos.ViewHolder>{
    private Context context;
    public static ArrayList<Vehiculo> listaVehiculo;
    public static ArrayList<Vehiculo> listaFiltrada;
    public int posicionItemSelecionadoRecyclerView;

    public AdaptadorListadoVehiculos(Context context) {
        this.context = context;
        listaVehiculo = new ArrayList<Vehiculo>();
        listaFiltrada = new ArrayList<Vehiculo>();
        listaFiltrada.addAll(listaVehiculo);
    }

    @Override
    public AdaptadorListadoVehiculos.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //Realizar el enlace entre el adaptador y el archivo XML que contiene al cardview
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_vehiculo_listado, parent, false);
        return new AdaptadorListadoVehiculos.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Vehiculo vehiculo = listaVehiculo.get(position);
        holder.txtPlacaVehiculoLis.setText("PLACA: " + vehiculo.getPlaca());
        holder.txtConductorVehiculoLis.setText("CONDUCTOR: " + vehiculo.getConductor());
        holder.txtNumBreveteVehiculoList.setText("N° BREVETE: " + vehiculo.getNum_brevete());
        //holder.txtNumRuedasLis.setText(vehiculo.getNumeroRuedas());
        holder.txtSituacionVehiculoLis.setText("SITUACION ACTUAL: " + vehiculo.getEstado());

        holder.txtPlacaVehiculoLis.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        holder.txtPlacaVehiculoLis.setSelected(true);

        holder.txtConductorVehiculoLis.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        holder.txtConductorVehiculoLis.setSelected(true);

        holder.txtNumBreveteVehiculoList.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        holder.txtNumBreveteVehiculoList.setSelected(true);

        //holder.txtNumRuedasLis.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        //holder.txtNumRuedasLis.setSelected(true);

        holder.txtSituacionVehiculoLis.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        holder.txtSituacionVehiculoLis.setSelected(true);
    }

    public void filtrado(String txtBusquedaV){
        int longitud = txtBusquedaV.length();
        if(longitud == 0){
            listaVehiculo.clear();
            listaVehiculo.addAll(listaFiltrada);
        }else{
            List<Vehiculo> ve = listaVehiculo.stream().filter(i -> i.getPlaca().toUpperCase().contains(txtBusquedaV.toUpperCase())).collect(Collectors.toList());
            List<Vehiculo> ve1 = listaVehiculo.stream().filter(i -> i.getConductor().toUpperCase().contains(txtBusquedaV.toUpperCase())).collect(Collectors.toList());
            List<Vehiculo> ve2 = listaVehiculo.stream().filter(i -> i.getNum_brevete().toUpperCase().contains(txtBusquedaV.toUpperCase())).collect(Collectors.toList());
            listaVehiculo.clear();
            listaVehiculo.addAll(ve);
            listaVehiculo.addAll(ve1);
            listaVehiculo.addAll(ve2);
        }
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return listaVehiculo.size();
    }

    public void cargarVehiculos(ArrayList<Vehiculo> lista){
        listaVehiculo = lista;
        notifyDataSetChanged();
    }



    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView txtPlacaVehiculoLis, txtConductorVehiculoLis, txtNumBreveteVehiculoList, txtSituacionVehiculoLis;

        public ViewHolder(View itemView) {
            super(itemView);

            //Enlazar los controles JAVA con los controles XML del cardview
            txtPlacaVehiculoLis = itemView.findViewById(R.id.txtPlacaVehiculoLis);
            txtConductorVehiculoLis = itemView.findViewById(R.id.txtConductorVehiculoLis);
            txtNumBreveteVehiculoList = itemView.findViewById(R.id.txtNumBreveteVehiculoList);
            //txtNumRuedasLis = itemView.findViewById(R.id.txtNumRuedasLis);
            txtSituacionVehiculoLis = itemView.findViewById(R.id.txtSituacionVehiculoLis);
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
        }
/*
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
        }*/
    }
}
