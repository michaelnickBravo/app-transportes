package pe.edu.usat.laboratorio.appcomercial;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import pe.edu.usat.laboratorio.appcomercial.adaptador.AdaptadorListadoVehiculos;
import pe.edu.usat.laboratorio.appcomercial.adaptador.AdaptadorTodosVehiculos;
import pe.edu.usat.laboratorio.appcomercial.logica.Vehiculo;
import pe.edu.usat.laboratorio.appcomercial.util.Helper;

public class ListaTodosVehiculos extends Fragment implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {
    RecyclerView listadoTodosVehiculosRecyclerView;
    SwipeRefreshLayout swipeRefreshLayoutSC;

    ArrayList<Vehiculo> listaVehiculosWS;
    AdaptadorTodosVehiculos adaptador;

    ProgressDialog dialog;

    public ListaTodosVehiculos() {
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
        View view= inflater.inflate(R.layout.fragment_lista_todos_vehiculos, container, false);
        //Mostrar un titulo para el fragment
        this.getActivity().setTitle("LISTADO DE TODOS LOS VEHICULOS");

        //configurar los controles
        listadoTodosVehiculosRecyclerView = view.findViewById(R.id.listadoTodosVehiculosRecyclerView);
        listadoTodosVehiculosRecyclerView.setHasFixedSize(true);
        listadoTodosVehiculosRecyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));

        swipeRefreshLayoutSC = view.findViewById(R.id.swipeRefreshLayoutSC);
        swipeRefreshLayoutSC.setColorScheme(
                android.R.color.holo_red_light,
                android.R.color.holo_green_light,
                android.R.color.holo_blue_light
        );
        swipeRefreshLayoutSC.setOnRefreshListener(this);

        //llamar al metodo listarCatalogoProductos() para mostrar el catalogo
        listarTodosVehiculos();

        return view;
    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public void onRefresh() {
        //refescar el catalogo de productos
        listarTodosVehiculos();
        //cerrar la animacion del swipRefreshLayout
        this.swipeRefreshLayoutSC.setRefreshing(false);
    }

    private class listarTodosVehiculosTask extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... voids) {
            boolean resultado = false;
            try {
                String URL_WS_Vehiculos = Helper.BASE_URL_WS + "/vehiculo/listar";
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
                adaptador.cargarTodosVehiculos(listaVehiculosWS);

                //cerrar el cuadro de dialogo
                dialog.dismiss();

            }
        }
    }

    private void listarTodosVehiculos(){
        //mostrar un cuadro de dialogo que se esta descargando el catalogo de productos
        dialog = new ProgressDialog(this.getContext());
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setMessage("Descargando la lista de vehiculos, por favor espere");
        dialog.setCancelable(false);
        dialog.show();

        //inicializar el arrayList, que se descargue desde el servicio web
        listaVehiculosWS = new ArrayList<>();

        //instanciar el adaptador
        adaptador = new AdaptadorTodosVehiculos(this.getContext());
        listadoTodosVehiculosRecyclerView.setAdapter(adaptador);

        //ejecutar la clase
        new ListaTodosVehiculos.listarTodosVehiculosTask().execute();
    }
}
