package pe.edu.usat.laboratorio.appcomercial;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import pe.edu.usat.laboratorio.appcomercial.adaptador.AdaptadorServicioCargaConductor;
import pe.edu.usat.laboratorio.appcomercial.logica.Conductor;
import pe.edu.usat.laboratorio.appcomercial.logica.ServicioCargaConductor;
import pe.edu.usat.laboratorio.appcomercial.logica.Sesion;
import pe.edu.usat.laboratorio.appcomercial.util.Helper;

public class SolicitudesServicioCargaConductor extends Fragment implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener{

    RecyclerView servicioCargaRecyclerView;

    ArrayList<ServicioCargaConductor> listaServicioWS;
    AdaptadorServicioCargaConductor adaptador;

    ProgressDialog dialog;
    SwipeRefreshLayout swipeRefreshLayoutSC;


    public SolicitudesServicioCargaConductor() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_solicitudes_servicio_carga_conductor, container, false);
        //Mostrar un titulo para el fragment
        this.getActivity().setTitle("Catalogo de servicios de carga");

        //configurar los controles
        servicioCargaRecyclerView = view.findViewById(R.id.SolicitudServicioConductorRecyclerView);
        servicioCargaRecyclerView.setHasFixedSize(true);
        servicioCargaRecyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));

        swipeRefreshLayoutSC = view.findViewById(R.id.swipeRefreshLayoutSC);
        swipeRefreshLayoutSC.setColorScheme(
                android.R.color.holo_red_light,
                android.R.color.holo_green_light,
                android.R.color.holo_blue_light
        );
        swipeRefreshLayoutSC.setOnRefreshListener(this);

        //llamar al metodo para mostrar el catalogo
        listarSolicitudesServicio();
        return view;
    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public void onRefresh() {
        //refescar el catalogo de productos
        listarSolicitudesServicio();
        //cerrar la animacion del swipRefreshLayout
        this.swipeRefreshLayoutSC.setRefreshing(false);
    }

    private class SolicitudServicioTask extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... voids) {
            boolean resultado = false;
            try {
                String URL_WS_SolicitudCarga = Helper.BASE_URL_WS + "/conductor/listar/solicitud";
                HashMap<String, String> parametros = new HashMap<>();
                parametros.put("num_brevete", Sesion.NUM_BREVETE);

                //realizar la peticion del servicio web
                String resultadoJSON = new Helper().requestHttpPost(URL_WS_SolicitudCarga, parametros);

                //Log.e("JSON Solicitud Carga", resultadoJSON);

                JSONObject jsonServicioCarga = new JSONObject(resultadoJSON);
                if (jsonServicioCarga.getBoolean("status")){
                    //acceder a los datos
                    JSONArray jsonArraySolicitudCarga = jsonServicioCarga.getJSONArray("data");

                    //limpiar el almacen de datos
                    listaServicioWS.clear();

                    for (int i=0; i<jsonArraySolicitudCarga.length(); i++){
                        //capturar los datos
                        JSONObject jsonCarga = jsonArraySolicitudCarga.getJSONObject(i);

                        //instanciar un objeto para guardar los datos
                        ServicioCargaConductor cargaConductor = new ServicioCargaConductor();
                        cargaConductor.setClaseCarga(jsonCarga.getString("clase_carga"));
                        cargaConductor.setCliente(jsonCarga.getString("cliente"));
                        cargaConductor.setDescripcionCarga(jsonCarga.getString("descripcion_carga"));
                        cargaConductor.setDireccionLlegada(jsonCarga.getString("direccion_llegada"));
                        cargaConductor.setDireccionPartida(jsonCarga.getString("direccion_partida"));
                        cargaConductor.setFechaHoraPartida(jsonCarga.getString("fecha_hora_partida"));
                        cargaConductor.setId(jsonCarga.getInt("id"));
                        cargaConductor.setCategoriaCarga(jsonCarga.getString("nombre"));
                        cargaConductor.setNumeroDocumento(jsonCarga.getString("numero_documento"));
                        cargaConductor.setPesoCarga(jsonCarga.getInt("peso_carga"));
                        cargaConductor.setPlaca(jsonCarga.getString("placa"));
                        cargaConductor.setTipoCarga(jsonCarga.getString("tipo_carga"));

                        //almacenar el objeto en el almacen de datos
                        listaServicioWS.add(cargaConductor);
                    }
                    //retornar un valor verdadero(true)
                    resultado = true;
                }else{
                    //retornar un valor falso (false), cuando no hay registro
                    //Toast.makeText(SolicitudesServicioCargaConductor.this.getContext(), "ACTUALMENTE NO SE ENCUENTRAN ATENDIENDO UNA CARGA", Toast.LENGTH_SHORT).show();
                    dialog.setMessage("ACTUALMENTE NO SE ENCUENTRAN ATENDIENDO UNA CARGA");
                    dialog.setCancelable(true);
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
                adaptador.cargarServiciosConductor(listaServicioWS);

                //cerrar el cuadro de dialogo
                dialog.dismiss();

            }
        }
    }

    private void listarSolicitudesServicio(){
        //mostrar un cuadro de dialogo que se esta descargando el catalogo de productos

        dialog = new ProgressDialog(this.getContext());
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

        dialog.setMessage("Descargando la lista de solicitud de servicio, por favor espere");

        dialog.setCancelable(true);
        dialog.show();

        //inicializar el arrayList, que se descargue desde el servicio web
        listaServicioWS = new ArrayList<>();

        //instanciar el adaptador
        adaptador = new AdaptadorServicioCargaConductor(this.getContext());
        servicioCargaRecyclerView.setAdapter(adaptador);

        //ejecutar la clase
        new SolicitudServicioTask().execute();
    }
}