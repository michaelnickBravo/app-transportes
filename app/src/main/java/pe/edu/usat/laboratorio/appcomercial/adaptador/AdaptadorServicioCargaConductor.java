package pe.edu.usat.laboratorio.appcomercial.adaptador;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

import pe.edu.usat.laboratorio.appcomercial.R;
import pe.edu.usat.laboratorio.appcomercial.logica.Producto;
import pe.edu.usat.laboratorio.appcomercial.logica.ServicioCargaConductor;
import pe.edu.usat.laboratorio.appcomercial.util.Helper;

public class AdaptadorServicioCargaConductor extends RecyclerView.Adapter<AdaptadorServicioCargaConductor.ViewHolder>{
    private Context context;
    public static ArrayList<ServicioCargaConductor> listaCarga;

    public AdaptadorServicioCargaConductor(Context context) {
        this.context = context;
        listaCarga = new ArrayList<ServicioCargaConductor>();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //Realizar el enlace entre el adaptador y el archivo XML que contiene al cardview
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_conductor, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ServicioCargaConductor cargaConductor = listaCarga.get(position);

        holder.txtNombreCliente.setText("CLIENTE: " + cargaConductor.getCliente());
        holder.txtnumerodocumento.setText("NUM. DOCUMENTO: " + cargaConductor.getNumeroDocumento());
        holder.txtClaseCarga.setText("CLASE: " + cargaConductor.getClaseCarga());
        holder.txtTipoCarga.setText("TIPO: " + cargaConductor.getTipoCarga());
        holder.txtDescripcionCarga.setText("DESC: " + cargaConductor.getDescripcionCarga());
        holder.txtCategoriaCarga.setText("CAT: " + cargaConductor.getCategoriaCarga());
        holder.txtID.setText("ID: " + String.valueOf(cargaConductor.getId()));
        holder.txtPesoCarga.setText("PESO: " + String.valueOf(cargaConductor.getPesoCarga()));
        holder.txtPlaca.setText("PLACA VEHICULAR: " + cargaConductor.getPlaca());
        holder.txtDireccionPartida.setText("DIRECCION DE PARTIDA: " + cargaConductor.getDireccionPartida());
        holder.txtFechaPartida.setText("FECHA DE PARTIDA: " + cargaConductor.getFechaHoraPartida());
        holder.txtDireccionLlegada.setText("DIRECCION LLEGADA: " + cargaConductor.getDireccionLlegada());

        holder.txtNombreCliente.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        holder.txtNombreCliente.setSelected(true);
        holder.txtDireccionPartida.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        holder.txtDireccionPartida.setSelected(true);
        holder.txtDireccionLlegada.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        holder.txtDireccionLlegada.setSelected(true);
    }

    @Override
    public int getItemCount() {
        return listaCarga.size();
    }

    public void cargarServiciosConductor(ArrayList<ServicioCargaConductor> lista){
        listaCarga = lista;
        notifyDataSetChanged();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView txtNombreCliente, txtnumerodocumento, txtClaseCarga, txtTipoCarga, txtDescripcionCarga, txtCategoriaCarga, txtID, txtPesoCarga, txtPlaca, txtDireccionPartida, txtFechaPartida, txtDireccionLlegada;

        public ViewHolder(View itemView) {
            super(itemView);

            //Enlazar los controles JAVA con los controles XML del cardview
            txtNombreCliente = itemView.findViewById(R.id.txtNombreCliente);
            txtnumerodocumento = itemView.findViewById(R.id.txtnumerodocumento);
            txtClaseCarga = itemView.findViewById(R.id.txtClaseCarga);
            txtTipoCarga = itemView.findViewById(R.id.txtTipoCarga);
            txtDescripcionCarga = itemView.findViewById(R.id.txtDescripcionCarga);
            txtCategoriaCarga = itemView.findViewById(R.id.txtCategoriaCarga);
            txtID = itemView.findViewById(R.id.txtID);
            txtPesoCarga = itemView.findViewById(R.id.txtPesoCarga);
            txtPlaca = itemView.findViewById(R.id.txtPlaca);
            txtDireccionPartida = itemView.findViewById(R.id.txtDireccionPartida);
            txtFechaPartida = itemView.findViewById(R.id.txtFechaPartida);
            txtDireccionLlegada = itemView.findViewById(R.id.txtDireccionLlegada);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            //Identificar el item seleccionado
            final int posicionItemSeleccionado = this.getAdapterPosition();

            Log.e("ADAPTADOR", "Click en el cardview, posición item: " + String.valueOf(posicionItemSeleccionado));
        }
    }
}
