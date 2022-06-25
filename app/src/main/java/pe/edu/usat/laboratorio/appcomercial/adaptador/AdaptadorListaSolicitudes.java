package pe.edu.usat.laboratorio.appcomercial.adaptador;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;

import java.util.ArrayList;

import pe.edu.usat.laboratorio.appcomercial.R;
import pe.edu.usat.laboratorio.appcomercial.logica.Producto;
import pe.edu.usat.laboratorio.appcomercial.logica.Solicitud;

public class AdaptadorListaSolicitudes extends RecyclerView.Adapter<AdaptadorListaSolicitudes.ViewHolder> {

    private Context context;
    public static ArrayList<Solicitud> listaSolicitudes;

    public AdaptadorListaSolicitudes(Context context) {
        this.context = context;
        listaSolicitudes = new ArrayList<>();
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

    public class ViewHolder extends RecyclerView.ViewHolder {

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
        }
    }

}
