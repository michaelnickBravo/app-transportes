package pe.edu.usat.laboratorio.appcomercial;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import pe.edu.usat.laboratorio.appcomercial.adaptador.AdaptadorCatalogoProducto;
import pe.edu.usat.laboratorio.appcomercial.adaptador.AdaptadorListadoConductores;
import pe.edu.usat.laboratorio.appcomercial.adaptador.AdaptadorListadoVehiculos;
import pe.edu.usat.laboratorio.appcomercial.adaptador.AdaptadorTarifa;
import pe.edu.usat.laboratorio.appcomercial.logica.Conductor;
import pe.edu.usat.laboratorio.appcomercial.logica.Producto;
import pe.edu.usat.laboratorio.appcomercial.logica.Tarifa;
import pe.edu.usat.laboratorio.appcomercial.logica.Vehiculo;
import pe.edu.usat.laboratorio.appcomercial.util.Helper;

public class Tarifas extends AppCompatActivity implements View.OnClickListener {

    RecyclerView recyclerViewTarifas;
    TextView txtTarifa;
    Button btnTarifa;

    ArrayList<Tarifa> listaTarifassWS;
    AdaptadorTarifa adaptador;

    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tarifas);
        //Mostrar un titulo para el fragment
        this.setTitle("REGISTRO Y LISTADO DE TARIFAS");

        //configurar los controles
        recyclerViewTarifas = findViewById(R.id.recyclerViewTarifas);
        recyclerViewTarifas.setHasFixedSize(true);
        recyclerViewTarifas.setLayoutManager(new LinearLayoutManager(this));

        btnTarifa = findViewById(R.id.btnTarifa);
        txtTarifa = findViewById(R.id.txtTarifa);

        btnTarifa.setOnClickListener(this);

        //llamar al metodo listarCatalogoProductos() para mostrar el catalogo
        listarTarifas();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnTarifa:
                registrarTarifa();
                break;
        }
    }

    private class listarTarifasTask extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... voids) {
            boolean resultado = false;
            try {
                String URL_WS_Tarifas = Helper.BASE_URL_WS + "/tarifa/listar";
                HashMap<String, String> parametros = new HashMap<>();

                //realizar la peticion del servicio web
                String resultadoJSON = new Helper().requestHttpPost(URL_WS_Tarifas, parametros);

                Log.e("JSON Catalogo", resultadoJSON);

                JSONObject jsonCon = new JSONObject(resultadoJSON);
                if (jsonCon.getBoolean("status")){
                    //acceder a los datos de los productos
                    JSONArray jsonArrayProductos = jsonCon.getJSONArray("data");

                    //limpiar el almacen de datos de los productos
                    listaTarifassWS.clear();

                    for (int i=0; i<jsonArrayProductos.length(); i++){
                        //capturar los datos de cada producto
                        JSONObject jsonConductores = jsonArrayProductos.getJSONObject(i);

                        //instanciar un objeto de la clase producto para guardar los datos
                        Tarifa tarifa = new Tarifa();
                        tarifa.setId(jsonConductores.getInt("id"));
                        tarifa.setPrecio(jsonConductores.getInt("precio_tn_kg"));

                        //almacenar el objeto "producto" en el almacen de datos
                        listaTarifassWS.add(tarifa);
                    }
                    //retornar un valor verdadero(true)
                    resultado = true;
                }else{
                    //retornar un valor falso (false), cuando no hay registro en el catalogo de productos
                    resultado = false;
                }
            }catch (Exception e){
                //Toast.makeText(CatalogoProductoFragment.this.getContext(), "error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                //Log.d("ERROR APP COMERCIAL", e.getMessage());
                e.printStackTrace();
            }
            return resultado;
        }


        @Override
        protected void onPostExecute(Boolean resultado) {
            super.onPostExecute(resultado);

            if (resultado) { //true
                //se ha descargado el catalogo correctamente
                //enviar los datos almacenados en el ArrayList al adaptador
                adaptador.cargarTarifa(listaTarifassWS);

                //cerrar el cuadro de dialogo
                dialog.dismiss();

            }
        }
    }

    private void listarTarifas(){
        //mostrar un cuadro de dialogo que se esta descargando el catalogo de productos
        dialog = new ProgressDialog(this);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setMessage("Descargando la lista de tarifas, por favor espere");
        dialog.setCancelable(false);
        dialog.show();

        //inicializar el arrayList, que se descargue desde el servicio web
        listaTarifassWS = new ArrayList<>();

        //instanciar el adaptador
        adaptador = new AdaptadorTarifa(this);
        recyclerViewTarifas.setAdapter(adaptador);

        //ejecutar la clase
        new Tarifas.listarTarifasTask().execute();
    }

    private class RegistrarTarifaTask extends AsyncTask<Void, Void, Boolean> {
        private String tarifa;

        public RegistrarTarifaTask(String tarifa) {
            this.tarifa = tarifa;
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            boolean resultado = false;
            try {
                String URL_WS_Registrar = Helper.BASE_URL_WS + "/tarifa/registrar";
                HashMap<String, String> parametros = new HashMap<>();

                parametros.put("precio_tn_kg", this.tarifa);

                //Realizar la petición al servicio web
                String respuestaJSONRegistro = new Helper().requestHttpPost(URL_WS_Registrar, parametros);

                //Convetir la respuesta del servicio web a objeto json
                JSONObject json = new JSONObject(respuestaJSONRegistro);

                Log.d("RegistrarTarifa",json.toString());

                resultado = json.getBoolean("status");

            }catch (Exception e) {
                e.printStackTrace();
            }
            return resultado;
        }

        @Override
        protected void onPostExecute(Boolean resultado) {
            super.onPostExecute(resultado);

            if (resultado) {
                //Crear un dialog para confirmar si se desea grabar la venta
                final AlertDialog.Builder dialog = new AlertDialog.Builder(Tarifas.this);
                dialog.setTitle("Se registro correctamente");
                dialog.setIcon(R.drawable.ic_question);

                dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                });
                listarTarifas();
                //Mostrar el cuadro de dialogo
                AlertDialog alertDialog = dialog.create();
                alertDialog.show();
            }else{
                Toast.makeText(Tarifas.this, "No se pudo registrar la tarifa", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void registrarTarifa() {
        final String tarifa = txtTarifa.getText().toString();

        if(txtTarifa.equals("")){
            Toast.makeText(Tarifas.this, "DEBE INGRESAR DATOS", Toast.LENGTH_SHORT).show();
        }else {
            Tarifas.RegistrarTarifaTask registrarTarifaTask = new Tarifas.RegistrarTarifaTask(tarifa);
            registrarTarifaTask.execute();
        }
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        //implementar la opcion "eliminar producto"
        switch (item.getItemId()){
            case 1:  //opcion eliminar
                //confirmar si desea eliminar
                Helper.mensajeConfirmacion
                        (
                                Tarifas.this,
                                "Seguro de eliminar la tarifa",
                                "Seguro de eliminar",
                                "No",
                                new Runnable() {
                                    @Override
                                    public void run() {
                                        eliminar();
                                    }
                                }
                        );
        }
        return true;
    }


    private class TaskEliminarTarifa extends AsyncTask<Void, Void, Boolean> {
        private String id;

        public TaskEliminarTarifa(String id) {
            this.id = id;
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            boolean resultado = false;
            try {
                String URL_WS_Eliminar = Helper.BASE_URL_WS + "/tarifa/eliminar";
                HashMap<String, String> parametros = new HashMap<>();

                parametros.put("id", this.id);

                //Realizar la petición al servicio web
                String respuestaJSON = new Helper().requestHttpPost(URL_WS_Eliminar, parametros);

                //Convetir la respuesta del servicio web a objeto json
                JSONObject json = new JSONObject(respuestaJSON);

                Log.d("EliminarTarifa",json.toString());

                resultado = json.getBoolean("status");

            }catch (Exception e) {
                e.printStackTrace();
            }
            return resultado;
        }

        @Override
        protected void onPostExecute(Boolean resultado) {
            super.onPostExecute(resultado);

            if (resultado) {
                //Crear un dialog para confirmar si se desea grabar la venta
                final AlertDialog.Builder dialog = new AlertDialog.Builder(Tarifas.this);
                dialog.setTitle("Se elimino correctamente");
                dialog.setIcon(R.drawable.ic_question);

                dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                });

                listaTarifassWS.remove(adaptador.posicionItemSelecionadoRecyclerView);
                adaptador.notifyItemRemoved(adaptador.posicionItemSelecionadoRecyclerView);

                //Mostrar el cuadro de dialogo
                AlertDialog alertDialog = dialog.create();
                alertDialog.show();
            }else{
                Toast.makeText(Tarifas.this, "No se pudo eliminar la tarifa", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void eliminar() {
        final String id = String.valueOf(listaTarifassWS.get(adaptador.posicionItemSelecionadoRecyclerView).getId());

        Tarifas.TaskEliminarTarifa eliminar = new Tarifas.TaskEliminarTarifa(id);
        eliminar.execute();
    }
}