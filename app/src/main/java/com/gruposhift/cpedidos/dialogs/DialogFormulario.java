package com.gruposhift.cpedidos.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;
import com.gruposhift.cpedidos.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import cz.msebera.android.httpclient.Header;

public class DialogFormulario extends AppCompatDialogFragment {

    private DialogoFormularioListener listener;
    private EditText prueba;
    private Spinner spinnUnidad;
    private AsyncHttpClient  httpClient;

    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.activity_prodcuto, null);
        builder.setView(view)
                .setTitle("Producto")
                .setNegativeButton("Calcel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .setPositiveButton("Guardar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String nombre = prueba.getText().toString();
                        listener.enviarDatos(nombre);
                    }
                });

        spinnUnidad = view.findViewById(R.id.spUnidad);
        prueba = view.findViewById(R.id.edtCantidadProducto);

        spinnerEstatico();
        return builder.create();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listener = (DialogoFormularioListener) context;
        } catch (ClassCastException e){
            throw  new ClassCastException(context.toString() + "must implements" );
        }
    }

    public void llamarPHP(String url, int opcion){
        int o = 0;
        o = opcion;
        final int finalO = o;

        httpClient.post(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                if (statusCode == 200){
                   // llenarSpiners(new String(responseBody), finalO);
                }else{
                    Toast.makeText(getActivity(), "Error", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });
    }

    public interface DialogoFormularioListener{
        void enviarDatos(String texto);
    }

    private void spinnerEstatico(){
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource( getActivity().getApplicationContext(), R.array.unidad, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnUnidad.setAdapter(adapter);
    }
}
