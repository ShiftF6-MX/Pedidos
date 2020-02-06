package com.gruposhift.cpedidos.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.gruposhift.cpedidos.R;
import com.gruposhift.cpedidos.model.Venta;

import org.w3c.dom.Text;

import java.util.Date;
import java.util.List;

public class AdapterPedidos extends BaseAdapter {

    private Context context;
    private int layout;
    private List<Venta> ventas;


    public AdapterPedidos(Context context, int layout, List<Venta> ventas) {
        this.context = context;
        this.layout = layout;
        this.ventas = ventas;
    }

    @Override
    public int getCount() {
        return ventas.size();
    }

    @Override
    public Object getItem(int position) {
        return ventas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view;
        LayoutInflater layoutInflater =LayoutInflater.from(this.context);
        view =layoutInflater.inflate(R.layout.list_pedidos, null);

        Venta venta = ventas.get(position);

        TextView nombre = (TextView) view.findViewById(R.id.txtNombreCliente);
        TextView referencia = (TextView) view.findViewById(R.id.txtReferencia);
        TextView fecha = (TextView) view.findViewById(R.id.txtFecha);

        nombre.setText("Cliente: "  + venta.getCliente().getNombre());
        referencia.setText("Ref: " + venta.getReferencia());
        fecha.setText("F. Entrega: " + venta.getFecha());

        return view;
    }
}
