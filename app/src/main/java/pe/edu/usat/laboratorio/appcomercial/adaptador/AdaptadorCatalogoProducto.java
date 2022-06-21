package pe.edu.usat.laboratorio.appcomercial.adaptador;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.util.Log;
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

import pe.edu.usat.laboratorio.appcomercial.R;
import pe.edu.usat.laboratorio.appcomercial.logica.Producto;
import pe.edu.usat.laboratorio.appcomercial.util.Helper;


public class AdaptadorCatalogoProducto extends RecyclerView.Adapter<AdaptadorCatalogoProducto.ViewHolder> {
    private Context context;
    public static ArrayList<Producto> listaProducto;
    private DisplayImageOptions options;

    public AdaptadorCatalogoProducto(Context context) {
        this.context = context;
        listaProducto = new ArrayList<Producto>();

        /*Configurar la descarga de las fotos de los productos*/
        options = Helper.configurarOpcionesDescargaImagenes();
        /*Configurar la descarga de las fotos de los productos*/
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //Realizar el enlace entre el adaptador y el archivo XML que contiene al cardview
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_producto, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        //Mostrar los datos de cada producto en los controles del cardview
        Producto producto = listaProducto.get(position);
        holder.txtNombreProducto.setText(producto.getNombre());
        holder.txtPrecioVenta.setText("P.Venta: s/ " + String.valueOf(producto.getPrecio()));
        holder.txtCategoria.setText(producto.getCategoria());
        holder.txtStock.setText("Stock: " + String.valueOf(producto.getStock()));
        holder.txtProductoCantidadVenta.setText("Cantidad: " + String.valueOf(producto.getCantidadVendida()));
        holder.txtID.setText("ID: " + String.valueOf(producto.getId()));

        //Mostrar la foto del producto
        ImageLoader.getInstance().displayImage(producto.getImg(), holder.imgProducto, options);
    }

    @Override
    public int getItemCount() {
        return listaProducto.size();
    }

    public void cargarCatalogoProductos(ArrayList<Producto> lista){
        listaProducto = lista;
        notifyDataSetChanged();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        ImageView imgProducto;
        TextView txtNombreProducto, txtPrecioVenta, txtCategoria, txtStock, txtID, txtProductoCantidadVenta;

        public ViewHolder(View itemView) {
            super(itemView);

            //Enlazar los controles JAVA con los controles XML del cardview
            imgProducto = itemView.findViewById(R.id.imgProducto);
            txtNombreProducto = itemView.findViewById(R.id.txtNombreProducto);
            txtPrecioVenta = itemView.findViewById(R.id.txtPrecioVenta);
            txtCategoria = itemView.findViewById(R.id.txtCategoria);
            txtStock = itemView.findViewById(R.id.txtStock);
            txtID = itemView.findViewById(R.id.txtID);
            txtProductoCantidadVenta = itemView.findViewById(R.id.txtProductoCantidadVenta);
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
