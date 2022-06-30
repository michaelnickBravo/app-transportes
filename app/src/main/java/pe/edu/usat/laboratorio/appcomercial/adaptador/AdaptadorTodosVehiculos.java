package pe.edu.usat.laboratorio.appcomercial.adaptador;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import pe.edu.usat.laboratorio.appcomercial.R;
import pe.edu.usat.laboratorio.appcomercial.logica.Vehiculo;

public class AdaptadorTodosVehiculos extends RecyclerView.Adapter<AdaptadorTodosVehiculos.ViewHolder>{
    private Context context;
    public static ArrayList<Vehiculo> listaVehiculo;
    public int posicionItemSelecionadoRecyclerView;

    public AdaptadorTodosVehiculos(Context context) {
        this.context = context;
        listaVehiculo = new ArrayList<Vehiculo>();
    }

    @Override
    public AdaptadorTodosVehiculos.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //Realizar el enlace entre el adaptador y el archivo XML que contiene al cardview
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_vehiculos_listado_o, parent, false);
        return new AdaptadorTodosVehiculos.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Vehiculo vehiculo = listaVehiculo.get(position);
        holder.txtPlacaVehiculos.setText("PLACA: " + vehiculo.getPlaca());
        holder.txtNumRuedas.setText("N° DE RUEDAS: " + vehiculo.getNumeroRuedas());
        holder.txtSituacionVehiculos.setText("ESTADO: " + vehiculo.getEstado());

        holder.txtPlacaVehiculos.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        holder.txtPlacaVehiculos.setSelected(true);

        holder.txtNumRuedas.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        holder.txtNumRuedas.setSelected(true);

        holder.txtSituacionVehiculos.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        holder.txtSituacionVehiculos.setSelected(true);

    }

    @Override
    public int getItemCount() {
        return listaVehiculo.size();
    }

    public void cargarTodosVehiculos(ArrayList<Vehiculo> lista){
        listaVehiculo = lista;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView txtPlacaVehiculos, txtNumRuedas, txtSituacionVehiculos;

        public ViewHolder(View itemView) {
            super(itemView);

            //Enlazar los controles JAVA con los controles XML del cardview
            txtPlacaVehiculos = itemView.findViewById(R.id.txtPlacaVehiculos);
            txtNumRuedas = itemView.findViewById(R.id.txtNumRuedas);
            txtSituacionVehiculos = itemView.findViewById(R.id.txtSituacionVehiculos);

            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
        }
    }
}
