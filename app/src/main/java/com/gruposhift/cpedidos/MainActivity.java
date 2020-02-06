package com.gruposhift.cpedidos;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.gruposhift.cpedidos.adapters.AdapterPedidos;
import com.gruposhift.cpedidos.model.Cliente;
import com.gruposhift.cpedidos.model.Venta;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity {

    private Button insertar;
    private ListView listaPedidos;
    private FloatingActionButton floatInsert;
    private AsyncHttpClient httpClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        httpClient = new AsyncHttpClient();

        insertar = (Button) findViewById(R.id.btnNuevo);
        floatInsert = (FloatingActionButton) findViewById(R.id.flotInsert);
        listaPedidos = (ListView) findViewById(R.id.lPedidos);
        leerPhp();


        floatInsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Formulario.class);
                startActivity(intent);
            }
        });
    }

    public void leerPhp(){
        String url = "http://192.168.0.28:8080/pedidos/ReadMaxi.php";
        httpClient.post(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                if(statusCode == 200){
                    cargarDatos( new String(responseBody));
                }
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });
    }

    public void cargarDatos(String response){

        final ArrayList<Venta> ventas = new ArrayList<Venta>();
        try {
            JSONArray array = new JSONArray(response);
            for (int i = 0; i < array.length(); i++){
                final Venta venta = new Venta();
                venta.setSysPK(array.getJSONObject(i).getInt("Sys_PK"));
                Cliente cliente = new Cliente();
                cliente.setNombre(array.getJSONObject(i).getString("Nombre"));
                venta.setCliente(cliente);
                venta.setReferencia(array.getJSONObject(i).getString("Referencia"));
                venta.setFecha(array.getJSONObject(i).getString("Fecha"));
                ventas.add(venta);

                AdapterPedidos adapterPedidos = new AdapterPedidos(this,R.layout.list_pedidos, ventas);
                listaPedidos.setAdapter(adapterPedidos);

                listaPedidos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        Intent intent = new Intent(MainActivity.this, Formulario.class);
                        intent.putExtra("sysPK", ventas.get(position).getSysPK());
                        intent.putExtra("nombre", ventas.get(position).getCliente().getNombre());
                        intent.putExtra("fecha", ventas.get(position).getFecha());
                        intent.putExtra("opcion", Formulario.EDITAR);
                        startActivity(intent);
                    }
                });

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

}
