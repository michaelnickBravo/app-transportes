package pe.edu.usat.laboratorio.appcomercial.adaptador;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import pe.edu.usat.laboratorio.appcomercial.R;
import pe.edu.usat.laboratorio.appcomercial.logica.Cliente;

public class AdaptadorListaClientes extends RecyclerView.Adapter<AdaptadorListaClientes.ViewHolder> {

    private Context context;
    public static ArrayList<Cliente> listaclientes;

    public AdaptadorListaClientes(Context context) {
        this.context = context;
        listaclientes = new ArrayList<>();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //Realizar el enlace entre el adaptador y el archivo XML que contiene al cardview
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_clientes, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Cliente solicitud = listaclientes.get(position);
        holder.txtNombre.setText(solicitud.getNombre());
        holder.txtDni.setText(solicitud.getDocumento());
        holder.txtTipo.setText(solicitud.getTipo());
        holder.txtDireccion.setText(solicitud.getDireccion());
        holder.txtTelefono.setText(solicitud.getTelefono());
        holder.txtEstado.setText(solicitud.getEstado());

    }

    @Override
    public int getItemCount() {
        return listaclientes.size();
    }

    public void cargarListaSolicitudes(ArrayList<Cliente> lista){
        listaclientes = lista;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtNombre, txtDni, txtTipo, txtDireccion, txtTelefono, txtEstado;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtNombre = itemView.findViewById(R.id.txt_nombre);
            txtDni = itemView.findViewById(R.id.txt_dni);
            txtTipo = itemView.findViewById(R.id.txt_tipo);
            txtDireccion = itemView.findViewById(R.id.txt_direccion);
            txtTelefono = itemView.findViewById(R.id.txt_telefono);
            txtEstado = itemView.findViewById(R.id.txt_estado);

        }
    }

}
