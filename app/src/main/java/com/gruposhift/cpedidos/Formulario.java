package com.gruposhift.cpedidos;

import androidx.annotation.ArrayRes;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;
import com.gruposhift.cpedidos.model.Cliente;
import com.gruposhift.cpedidos.model.Pedido;
import com.gruposhift.cpedidos.model.Producto;
import com.loopj.android.http.*;
import com.loopj.android.http.AsyncHttpResponseHandler;
import org.json.JSONArray;
import java.util.ArrayList;
import java.util.Calendar;

import cz.msebera.android.httpclient.Header;

public class Formulario extends AppCompatActivity {

    public final static int INSERTAR = 1;
    public final static int EDITAR = 2;

    private int anio, mes, dia;
    private EditText fechaEdt, edtCantiad;
    private Spinner spCliente, spProducto, spCantidad;
    private Button agregar;
    private ListView listNuevoPedido;
    private AsyncHttpClient httpClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formulario);

        httpClient = new AsyncHttpClient();
        spCliente = (Spinner) findViewById(R.id.spCliente);
        spProducto = (Spinner) findViewById(R.id.spProducto);
        spCantidad = (Spinner) findViewById(R.id.spUnidad);
        fechaEdt = (EditText) findViewById(R.id.edtFecha);
        edtCantiad = (EditText)findViewById(R.id.edtCantidad);
        agregar = (Button) findViewById(R.id.btnAgregarPrdocuto);
        listNuevoPedido = (ListView) findViewById(R.id.listNPedido);
        llamarPHP("http://192.168.0.28:8080/pedidos/readClientes.php", 1);
        llamarPHP("http://192.168.0.28:8080/pedidos/readPRoducto.php", 2);
        spinnerEstatito();


        fechaEdt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker();
            }
        });

        agregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             //   validarFormulario();
                recuperarFormulario();
            }
        });
    }

    private void showDatePicker() {
        final Calendar fecha = Calendar.getInstance();
        anio = fecha.get(Calendar.YEAR);
        mes = fecha.get(Calendar.MONTH) ;
        dia = fecha.get(Calendar.DAY_OF_MONTH) + 1;

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                fechaEdt.setText(year + "-" + month  +"-" + dayOfMonth );
            }
        }, anio, mes, dia);
        datePickerDialog.show();
    }

    public void llamarPHP(String url, int opcion){
        int o = 0;
        o = opcion;
        final int finalO = o;

        httpClient.post(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                if (statusCode == 200){
                    llenarSpiners(new String(responseBody), finalO);
                }else{
                    Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });
    }

    public void llenarSpiners(String respose, int opcion){
        ArrayList<Cliente> clientes = new ArrayList<Cliente>();
        ArrayList<Pedido> pedidos = new ArrayList<Pedido>();

        try {
            JSONArray arrayRespues = new JSONArray(respose);

            if (opcion == 1){

                for (int i=0; i < arrayRespues.length(); i++){
                    Cliente cliente = new Cliente();
                    cliente.setSysPK(arrayRespues.getJSONObject(i).getInt("Sys_PK"));
                    cliente.setNombre(arrayRespues.getJSONObject(i).getString("Nombre"));
                    clientes.add(cliente);
                }
                ArrayAdapter<Cliente> adapter = new ArrayAdapter<Cliente>(this, android.R.layout.simple_dropdown_item_1line,clientes);
                spCliente.setAdapter(adapter);
            } else if (opcion == 2){

                for (int i=0; i < arrayRespues.length(); i++){

                    Pedido pedido = new Pedido();
                    pedido.setSysPK(arrayRespues.getJSONObject(i).getInt("Sys_PK"));
                    pedido.setDescripcion(arrayRespues.getJSONObject(i).getString("Descripcion"));
                    pedidos.add(pedido);
                }
                ArrayAdapter<Pedido> adapter = new ArrayAdapter<Pedido>(this, android.R.layout.simple_dropdown_item_1line, pedidos);
                spProducto.setAdapter(adapter);
            }



        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void spinnerEstatito(){
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.unidad, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spCantidad.setAdapter(adapter);
    }

    private void recuperarFormulario(){
        Cliente cliente = (Cliente) spCliente.getSelectedItem();
        String client = Integer.toString(cliente.getSysPK());
        String fecha = String.valueOf(fechaEdt.getText());

        Pedido producto = (Pedido) spProducto.getSelectedItem();
        String product = Integer.toString(producto.getSysPK());

        ArrayList<String>  generar = new ArrayList<String>();
        generar.add(client);
        generar.add(fecha);
        generar.add(product);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, generar );
        listNuevoPedido.setAdapter(adapter);


        Toast.makeText(getApplicationContext(), "Valor  " + client + " Fecha :" +  fecha + "Prod" + product, Toast.LENGTH_SHORT).show();
    }

    private boolean validarFormulario(){
        if (fechaEdt.getText().toString().isEmpty()) {
            Toast.makeText(getApplicationContext(), "Valores invalidos", Toast.LENGTH_SHORT).show();
            return false;
        }
            else {
                Toast.makeText(getApplicationContext(), "Valores validos", Toast.LENGTH_SHORT).show();
                return  true;
            }

    }
}
