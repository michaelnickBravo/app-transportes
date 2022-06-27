package pe.edu.usat.laboratorio.appcomercial;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import pe.edu.usat.laboratorio.appcomercial.util.Pickers;

public class PrincipalConductor extends Fragment implements View.OnClickListener {

    private Button btnReportarEstado, btnReportarUbicacion;

    public PrincipalConductor() {
        // Required empty public constructor
    }

   /* public static PrincipalConductor newInstance(String param1, String param2) {
        PrincipalConductor fragment = new PrincipalConductor();
    }*/

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_principal_conductor, container, false);

        btnReportarEstado = view.findViewById(R.id.btnReportarEstado);
        btnReportarUbicacion = view.findViewById(R.id.btnReportarUbicacion);

        btnReportarEstado.setOnClickListener(this);

        return view;
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnReportarEstado:
                Intent intent = new Intent(this.getActivity(), ReportarEstadoVehiculo.class);
                this.getContext().startActivity(intent);
                break;
            case R.id.btnReportarUbicacion:
                
                break;
        }
    }
}