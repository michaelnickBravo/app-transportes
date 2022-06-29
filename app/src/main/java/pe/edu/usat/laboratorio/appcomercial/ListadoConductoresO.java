package pe.edu.usat.laboratorio.appcomercial;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import pe.edu.usat.laboratorio.appcomercial.adaptador.AdaptadorListadoConductores;
import pe.edu.usat.laboratorio.appcomercial.logica.Conductor;
import pe.edu.usat.laboratorio.appcomercial.util.Helper;


public class ListadoConductoresO extends Fragment implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener, SearchView.OnQueryTextListener{

    RecyclerView listadoConductoresRecyclerView;
    SwipeRefreshLayout swipeRefreshLayoutSC;

    ArrayList<Conductor> listaConductorWS;
    AdaptadorListadoConductores adaptador;

    SearchView txtbusqueda;

    ProgressDialog dialog;

    public ListadoConductoresO() {
        // Required empty public constructor
    }

    /*
    public static ListadoConductoresO newInstance(String param1, String param2) {
        ListadoConductoresO fragment = new ListadoConductoresO();
        return fragment;
    }*/

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Helper.configurarAlmacenamientoCache(this.getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view= inflater.inflate(R.layout.fragment_listado_conductores_o, container, false);
        //Mostrar un titulo para el fragment
        this.getActivity().setTitle("LISTADO DE CONDUCTORES");

        //configurar los controles
        listadoConductoresRecyclerView = view.findViewById(R.id.listadoConductoresRecyclerView);
        listadoConductoresRecyclerView.setHasFixedSize(true);
        listadoConductoresRecyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));

        txtbusqueda = view.findViewById(R.id.txtBusqueda);
        txtbusqueda.setOnQueryTextListener(this);

        swipeRefreshLayoutSC = view.findViewById(R.id.swipeRefreshLayoutSC);
        swipeRefreshLayoutSC.setColorScheme(
                android.R.color.holo_red_light,
                android.R.color.holo_green_light,
                android.R.color.holo_blue_light
        );
        swipeRefreshLayoutSC.setOnRefreshListener(this);

        //llamar al metodo listarCatalogoProductos() para mostrar el catalogo
        listarConductores();

        return view;
    }

    @Override
    public void onClick(View view) {
    }

    @Override
    public void onRefresh() {
        //refescar el catalogo de productos
        listarConductores();
        //cerrar la animacion del swipRefreshLayout
        this.swipeRefreshLayoutSC.setRefreshing(false);
    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        adaptador.filtrado(s);
        return false;
    }


    private class listarConductoresTask extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... voids) {
            boolean resultado = false;
            try {
                String URL_WS_Conductores = Helper.BASE_URL_WS + "/conductor/listar";
                HashMap<String, String> parametros = new HashMap<>();

                //realizar la peticion del servicio web
                String resultadoJSON = new Helper().requestHttpPost(URL_WS_Conductores, parametros);

                Log.e("JSON Catalogo", resultadoJSON);

                JSONObject jsonCon = new JSONObject(resultadoJSON);
                if (jsonCon.getBoolean("status")){
                    //acceder a los datos de los productos
                    JSONArray jsonArrayProductos = jsonCon.getJSONArray("data");

                    //limpiar el almacen de datos de los productos
                    listaConductorWS.clear();

                    for (int i=0; i<jsonArrayProductos.length(); i++){
                        //capturar los datos de cada producto
                        JSONObject jsonConductores = jsonArrayProductos.getJSONObject(i);

                        //instanciar un objeto de la clase producto para guardar los datos
                        Conductor con = new Conductor();
                        con.setEmail(jsonConductores.getString("email"));
                        con.setNombre(jsonConductores.getString("nombre"));
                        con.setNum_brevete(jsonConductores.getString("num_brevete"));

                        //almacenar el objeto "producto" en el almacen de datos
                        listaConductorWS.add(con);
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
                adaptador.cargarConductores(listaConductorWS);

                //cerrar el cuadro de dialogo
                dialog.dismiss();

            }
        }
    }

    private void listarConductores(){
        //mostrar un cuadro de dialogo que se esta descargando el catalogo de productos
        dialog = new ProgressDialog(this.getContext());
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setMessage("Descargando la lista de conductores, por favor espere");
        dialog.setCancelable(false);
        dialog.show();

        //inicializar el arrayList, que se descargue desde el servicio web
        listaConductorWS = new ArrayList<>();

        //instanciar el adaptador
        adaptador = new AdaptadorListadoConductores(this.getContext());
        listadoConductoresRecyclerView.setAdapter(adaptador);

        //ejecutar la clase
        new listarConductoresTask().execute();
    }


    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        //implementar la opcion "eliminar producto"
        switch (item.getItemId()){
            case 1:  //opcion eliminar
                //confirmar si desea eliminar
                Helper.mensajeConfirmacion
                        (
                                getContext(),
                                "Seguro de eliminar el conductor",
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


    private class TaskEliminarConductor extends AsyncTask<Void, Void, Boolean> {
        private String num_brevete;

        public TaskEliminarConductor(String num_brevete) {
            this.num_brevete = num_brevete;
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            boolean resultado = false;
            try {
                String URL_WS_Eliminar = Helper.BASE_URL_WS + "/conductor/eliminar";
                HashMap<String, String> parametros = new HashMap<>();

                parametros.put("num_brevete", this.num_brevete);

                //Realizar la petición al servicio web
                String respuestaJSON = new Helper().requestHttpPost(URL_WS_Eliminar, parametros);

                //Convetir la respuesta del servicio web a objeto json
                JSONObject json = new JSONObject(respuestaJSON);

                Log.d("EliminarConductor",json.toString());

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
                final AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
                dialog.setTitle("Se elimino correctamente");
                dialog.setIcon(R.drawable.ic_question);

                dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                });

                adaptador.notifyItemRemoved(adaptador.posicionItemSelecionadoRecyclerView);

                //Mostrar el cuadro de dialogo
                AlertDialog alertDialog = dialog.create();
                alertDialog.show();
            }else{
                Toast.makeText(getContext(), "No se pudo eliminar el conductor", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void eliminar() {
        final String num_brevete = listaConductorWS.get(adaptador.posicionItemSelecionadoRecyclerView).getNum_brevete();

        ListadoConductoresO.TaskEliminarConductor eliminar = new ListadoConductoresO.TaskEliminarConductor(num_brevete);
        eliminar.execute();
    }

}