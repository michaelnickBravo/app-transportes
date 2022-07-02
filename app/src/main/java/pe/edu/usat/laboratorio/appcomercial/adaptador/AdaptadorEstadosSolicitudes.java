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
import pe.edu.usat.laboratorio.appcomercial.logica.Cliente;
import pe.edu.usat.laboratorio.appcomercial.logica.EstadoSolicitud;
import pe.edu.usat.laboratorio.appcomercial.logica.Tarifa;

public class AdaptadorEstadosSolicitudes extends RecyclerView.Adapter<AdaptadorEstadosSolicitudes.ViewHolder> {

    private Context context;
    public static ArrayList<EstadoSolicitud> listaclientes;
    public int posicionItemSeleccionadoRecyclerView;

    public AdaptadorEstadosSolicitudes(Context context) {
        this.context = context;
        listaclientes = new ArrayList<>();
    }

    @NonNull
    @Override
    public AdaptadorEstadosSolicitudes.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //Realizar el enlace entre el adaptador y el archivo XML que contiene al cardview
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_estados_solicitud, parent, false);
        return new AdaptadorEstadosSolicitudes.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdaptadorEstadosSolicitudes.ViewHolder holder, int position) {
        EstadoSolicitud solicitud = listaclientes.get(position);
        holder.txtEstado.setText(solicitud.getNombre());
        holder.txtObservacion.setText(solicitud.getObservacion());
        holder.txtFecha.setText(solicitud.getFecha_hora());


    }

    @Override
    public int getItemCount() {
        return listaclientes.size();
    }

    public void cargarListaSolicitudes(ArrayList<EstadoSolicitud> lista){
        listaclientes = lista;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder  {

        TextView txtEstado, txtObservacion, txtFecha;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtEstado = itemView.findViewById(R.id.txt_estado);
            txtObservacion = itemView.findViewById(R.id.txt_observacion);
            txtFecha = itemView.findViewById(R.id.txt_fecha_hora);



        }

    }

}

