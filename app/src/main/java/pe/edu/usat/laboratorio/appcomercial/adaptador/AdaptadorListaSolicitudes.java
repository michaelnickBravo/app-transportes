package pe.edu.usat.laboratorio.appcomercial.adaptador;

import android.content.Context;
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
import pe.edu.usat.laboratorio.appcomercial.logica.Solicitud;
import pe.edu.usat.laboratorio.appcomercial.logica.Vehiculo;

public class AdaptadorListaSolicitudes extends RecyclerView.Adapter<AdaptadorListaSolicitudes.ViewHolder> {

    private Context context;
    public static ArrayList<Solicitud> listaSolicitudes;
    public static ArrayList<Solicitud> listaFiltrada;
    public int posicionItemSeleccionadoRecyclerView;

    public AdaptadorListaSolicitudes(Context context) {
        this.context = context;
        listaSolicitudes = new ArrayList<>();
        listaFiltrada = new ArrayList<Solicitud>();
        listaFiltrada.addAll(listaSolicitudes);
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //Realizar el enlace entre el adaptador y el archivo XML que contiene al cardview
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_solicitud_cliente, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Solicitud solicitud = listaSolicitudes.get(position);
        holder.txtDireccionLlegada.setText(solicitud.getDireccionLlegada());
        holder.txtDireccionPartida.setText(solicitud.getDireccionPartida());
        holder.fechaPartida.setText(solicitud.getFecaPartida());
        holder.horaPartida.setText(solicitud.getHoraPartida());
        holder.monto.setText("S/."+solicitud.getMonto());
        holder.peso.setText(solicitud.getPesoCarga()+"Kg");
        holder.tarifa.setText("S/."+solicitud.getTarifa());
    }

    @Override
    public int getItemCount() {
        return listaSolicitudes.size();
    }

    public void cargarListaSolicitudes(ArrayList<Solicitud> lista){
        listaSolicitudes = lista;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener, View.OnLongClickListener {

        TextView txtDireccionLlegada, txtDireccionPartida, fechaPartida, horaPartida, monto, peso, tarifa;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtDireccionLlegada = itemView.findViewById(R.id.txt_direccion_llegada);
            txtDireccionPartida = itemView.findViewById(R.id.txt_direccion_partida);
            fechaPartida = itemView.findViewById(R.id.txt_fecha_partida);
            horaPartida = itemView.findViewById(R.id.txt_hora_partida);
            monto = itemView.findViewById(R.id.txt_monto);
            peso = itemView.findViewById(R.id.txt_peso_carga);
            tarifa = itemView.findViewById(R.id.txt_tarifa);
            itemView.setOnCreateContextMenuListener(this);
            itemView.setOnLongClickListener(this);
        }
        @Override
        public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
            contextMenu.setHeaderTitle("Opciones");
            contextMenu.add(0,1,0, "Gestionar Estado");
            contextMenu.add(0,1,0, "Gestionar Asignaciones VC");
        }

        @Override
        public boolean onLongClick(View view) {
            //Obtener la posición del item seleccionado en el RecyclerView
            posicionItemSeleccionadoRecyclerView = this.getAdapterPosition();
            return false;
        }

    }
    public   void filtrado(String txtBusquedaV){
        int longitud = txtBusquedaV.length();
        if(longitud == 0){
            listaSolicitudes.clear();
            listaSolicitudes.addAll(listaFiltrada);
        }else{
            List<Solicitud> ve = listaSolicitudes.stream().filter(i -> i.getDireccionPartida().toUpperCase().contains(txtBusquedaV.toUpperCase())).collect(Collectors.toList());
            List<Solicitud> ve1 = listaSolicitudes.stream().filter(i -> i.getDireccionLlegada().toUpperCase().contains(txtBusquedaV.toUpperCase())).collect(Collectors.toList());
            List<Solicitud> ve2 = listaSolicitudes.stream().filter(i -> i.getDescripcionCarga().toUpperCase().contains(txtBusquedaV.toUpperCase())).collect(Collectors.toList());
            listaSolicitudes.clear();
            listaSolicitudes.addAll(ve);
            listaSolicitudes.addAll(ve1);
            listaSolicitudes.addAll(ve2);
        }
        notifyDataSetChanged();
    }


}
