package pe.edu.usat.laboratorio.appcomercial;

import android.app.ProgressDialog;
import android.content.DialogInterface;
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
import android.widget.SearchView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import pe.edu.usat.laboratorio.appcomercial.adaptador.AdaptadorListadoConductores;
import pe.edu.usat.laboratorio.appcomercial.adaptador.AdaptadorListadoVehiculos;
import pe.edu.usat.laboratorio.appcomercial.logica.Conductor;
import pe.edu.usat.laboratorio.appcomercial.logica.Vehiculo;
import pe.edu.usat.laboratorio.appcomercial.util.Helper;

public class ListadoVehiculosO extends Fragment implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener, SearchView.OnQueryTextListener{

    RecyclerView listadoVehiculosRecyclerView;
    SwipeRefreshLayout swipeRefreshLayoutSC;

    ArrayList<Vehiculo> listaVehiculosWS;
    AdaptadorListadoVehiculos adaptador;

    SearchView txtbusquedaV;

    ProgressDialog dialog;

    public ListadoVehiculosO() {
        // Required empty public constructor
    }

   /*
    public static ListadoVehiculosO newInstance(String param1, String param2) {
        ListadoVehiculosO fragment = new ListadoVehiculosO();
        Bundle args = new Bundle();
        return fragment;
    }*/

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_listado_vehiculos_o, container, false);
        //Mostrar un titulo para el fragment
        this.getActivity().setTitle("LISTADO DE VEHICULOS");

        //configurar los controles
        listadoVehiculosRecyclerView = view.findViewById(R.id.listadoVehiculosRecyclerView);
        listadoVehiculosRecyclerView.setHasFixedSize(true);
        listadoVehiculosRecyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));

        txtbusquedaV = view.findViewById(R.id.txtBusquedaV);
        txtbusquedaV.setOnQueryTextListener(this);

        swipeRefreshLayoutSC = view.findViewById(R.id.swipeRefreshLayoutSC);
        swipeRefreshLayoutSC.setColorScheme(
                android.R.color.holo_red_light,
                android.R.color.holo_green_light,
                android.R.color.holo_blue_light
        );
        swipeRefreshLayoutSC.setOnRefreshListener(this);

        //llamar al metodo listarCatalogoProductos() para mostrar el catalogo
        listarVehiculos();

        return view;
    }

    @Override
    public void onClick(View view) {

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

    @Override
    public void onRefresh() {
        //refescar el catalogo de productos
        listarVehiculos();
        //cerrar la animacion del swipRefreshLayout
        this.swipeRefreshLayoutSC.setRefreshing(false);
    }

    private class listarVehiculosTask extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... voids) {
            boolean resultado = false;
            try {
                String URL_WS_Vehiculos = Helper.BASE_URL_WS + "/vehiculo_conductor/listar";
                HashMap<String, String> parametros = new HashMap<>();

                //realizar la peticion del servicio web
                String resultadoJSON = new Helper().requestHttpPost(URL_WS_Vehiculos, parametros);

                Log.e("JSON Catalogo", resultadoJSON);

                JSONObject jsonCon = new JSONObject(resultadoJSON);
                if (jsonCon.getBoolean("status")){
                    //acceder a los datos de los productos
                    JSONArray jsonArrayVehiculos = jsonCon.getJSONArray("data");

                    //limpiar el almacen de datos de los productos
                    listaVehiculosWS.clear();

                    for (int i=0; i<jsonArrayVehiculos.length(); i++){
                        //capturar los datos de cada producto
                        JSONObject jsonVehiculos= jsonArrayVehiculos.getJSONObject(i);

                        //instanciar un objeto de la clase producto para guardar los datos
                        Vehiculo ve = new Vehiculo();
                        ve.setEstado(jsonVehiculos.getString("estado"));
                        ve.setConductor(jsonVehiculos.getString("nombre"));
                        ve.setNum_brevete(jsonVehiculos.getString("num_brevete"));
                        ve.setNumeroRuedas(jsonVehiculos.getInt("num_ruedas"));
                        ve.setPlaca(jsonVehiculos.getString("placa"));

                        //almacenar el objeto "producto" en el almacen de datos
                        listaVehiculosWS.add(ve);
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
                adaptador.cargarVehiculos(listaVehiculosWS);

                //cerrar el cuadro de dialogo
                dialog.dismiss();

            }
        }
    }

    private void listarVehiculos(){
        //mostrar un cuadro de dialogo que se esta descargando el catalogo de productos
        dialog = new ProgressDialog(this.getContext());
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setMessage("Descargando la lista de vehiculos, por favor espere");
        dialog.setCancelable(false);
        dialog.show();

        //inicializar el arrayList, que se descargue desde el servicio web
        listaVehiculosWS = new ArrayList<>();

        //instanciar el adaptador
        adaptador = new AdaptadorListadoVehiculos(this.getContext());
        listadoVehiculosRecyclerView.setAdapter(adaptador);

        //ejecutar la clase
        new ListadoVehiculosO.listarVehiculosTask().execute();
    }
}