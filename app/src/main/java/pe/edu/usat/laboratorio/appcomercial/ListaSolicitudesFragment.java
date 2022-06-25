package pe.edu.usat.laboratorio.appcomercial;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import pe.edu.usat.laboratorio.appcomercial.adaptador.AdaptadorCatalogoProducto;
import pe.edu.usat.laboratorio.appcomercial.adaptador.AdaptadorListaSolicitudes;
import pe.edu.usat.laboratorio.appcomercial.logica.Producto;
import pe.edu.usat.laboratorio.appcomercial.logica.Sesion;
import pe.edu.usat.laboratorio.appcomercial.logica.Solicitud;
import pe.edu.usat.laboratorio.appcomercial.util.Helper;

public class ListaSolicitudesFragment extends Fragment {

    RecyclerView recyclerViewSolicitudes;
    ArrayList<Solicitud> listaSolicitudesWS;
    AdaptadorListaSolicitudes adaptadorListaSolicitudes;
    ProgressDialog dialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_lista_solicitudes, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerViewSolicitudes = view.findViewById(R.id.recycler_view_solicitudes);
        recyclerViewSolicitudes.setHasFixedSize(true);
        recyclerViewSolicitudes.setLayoutManager(new LinearLayoutManager(this.getContext()));
        this.listarSolicitudes();
    }

    private void listarSolicitudes() {

        //Mostrar un cuadro de dialogo indicando que se esta descargando la lista de solicitudes.
        dialog = new ProgressDialog(this.getContext());
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setMessage("Descargando la lista de solicitudes, por favor espere...");
        dialog.setCancelable(false);
        dialog.show();

        //Inicializar el ArrayList.
        listaSolicitudesWS = new ArrayList<>();

        //Inicializar el adaptador.
        adaptadorListaSolicitudes = new AdaptadorListaSolicitudes(this.getContext());
        recyclerViewSolicitudes.setAdapter(adaptadorListaSolicitudes);
        //Ejecutar la clase CatalogoProductoTask
        new ListarSolicitudesTask().execute();

    }

    private class ListarSolicitudesTask extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... voids) {
            boolean resultado = false;
            try {
                String URL_WS_ListaSolicitudes = Helper.BASE_URL_WS + "/solicitud_servicio/listar_detalles_solicitud_cliente";
                HashMap<String, String> parametros = new HashMap<>();
                parametros.put("cliente_id", Sesion.NUM_DOCUMENTO);
                parametros.put("token", Sesion.TOKEN);

                //Realizar la petición (request) del servicio web
                String resultadoJSON = new Helper().requestHttpPost(URL_WS_ListaSolicitudes, parametros);

                JSONObject jsonListaSolicitudes = new JSONObject(resultadoJSON);
                if (jsonListaSolicitudes.getBoolean("status")) {
                    //Acceder a los datos de la lista de solicitudes
                    JSONArray jsonArraySolicitudes = jsonListaSolicitudes.getJSONArray("data");

                    //Limpia el almacen de datos
                    listaSolicitudesWS.clear();

                    for (int i = 0; i < jsonArraySolicitudes.length(); i++) {
                        //Capturar los datos de cada solicitud
                        JSONObject jsonSolicitud = jsonArraySolicitudes.getJSONObject(i);

                        //Instanciar un objeto de la clase Solicitud para guardar los datos
                        Solicitud solicitud = new Solicitud();
                        solicitud.setCategoria(jsonSolicitud.getString("categoria"));
                        solicitud.setClase(jsonSolicitud.getString("clase"));
                        solicitud.setDescripcionCarga(jsonSolicitud.getString("descripcion_carga"));
                        solicitud.setDireccionLlegada(jsonSolicitud.getString("direccion_llegada"));
                        solicitud.setDireccionPartida(jsonSolicitud.getString("direccion_partida"));
                        String[] fechaHoraPartida = jsonSolicitud.getString("fecha_hora_partida").split("T");
                        solicitud.setFecaPartida(fechaHoraPartida[0]);
                        solicitud.setHoraPartida(fechaHoraPartida[1]);
                        solicitud.setId(jsonSolicitud.getInt("id"));
                        solicitud.setMonto(jsonSolicitud.getDouble("monto"));
                        solicitud.setPesoCarga(jsonSolicitud.getDouble("peso_carga"));
                        solicitud.setTarifa(jsonSolicitud.getDouble("precio_tn_kg"));
                        solicitud.setTipo(jsonSolicitud.getString("tipo"));

                        //Almacenar el objeto solicitud en el almacen de datos
                        listaSolicitudesWS.add(solicitud);
                    }

                    //Retornar un valor verdadero
                    resultado = true;
                }
            }catch (Exception e){
                e.printStackTrace();
            }
            return resultado;
        }

        @Override
        protected void onPostExecute(Boolean resultado) {
            super.onPostExecute(resultado);
            if (resultado) {
                //Se ha descargado la lista de solicitudes
                //Enviar los datos almacenados en el ArrayList al adaptador
                adaptadorListaSolicitudes.cargarListaSolicitudes(listaSolicitudesWS);
                //Cerrar el cuadro de dialogo de descarga.
                dialog.dismiss();
            }
        }
    }

}