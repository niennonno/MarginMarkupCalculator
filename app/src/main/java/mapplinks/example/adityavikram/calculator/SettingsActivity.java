package com.mapplinks.calculator;

import android.content.ContentValues;
import android.database.Cursor;
import android.support.v7.app.AlertDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.mapplinks.X.R;
import java.util.ArrayList;
import mapplinks.example.adityavikram.calculator.TaxList;

public class SettingsActivity extends AppCompatActivity {

    AlertDialog addTaxBuilder;

    private TaxList mTaxList;
    private TaxAdapter mAdapter;

    com.mapplinks.calculator.Taxes t = new com.mapplinks.calculator.Taxes();

    com.mapplinks.calculator.DbHelper mDbhelper = com.mapplinks.calculator.DbHelper.getInstance(SettingsActivity.this);

    String[] allColumns = {
            com.mapplinks.calculator.DbHelper.COLUMN_ID,
            com.mapplinks.calculator.DbHelper.COLUMN_TAX_NAME,
            com.mapplinks.calculator.DbHelper.COLUMN_VALUE
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        Intent intent = getIntent();

        mTaxList = TaxList.getsInstance();
        getAllTaxes(mTaxList);


        ListView listView = (ListView) findViewById(R.id.tax_list);
        mAdapter = new TaxAdapter(mTaxList);
        listView.setAdapter(mAdapter);

       listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
           @Override
           public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

           }
       });

//---------------------------------------------------DIALOG--------------------------------------------------------------------------
        addTaxBuilder = new AlertDialog.Builder(this).create();
        TextView addTax = (TextView) findViewById(R.id.add_new_tax);
        addTax.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater inflater = getLayoutInflater();
                View addView = inflater.inflate(R.layout.add_tax_dialog, null);
                addTaxBuilder.setView(addView);
                final EditText taxName = (EditText) addView.findViewById(R.id.new_tax_name);
                final EditText taxRate = (EditText) addView.findViewById(R.id.new_tax_rate);
                Button addTaxButton = (Button) addView.findViewById(R.id.but_add_tax);
                Button cancel = (Button) addView.findViewById(R.id.but_cancel);

                addTaxButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        t.setTax(taxName.getText().toString());
                        t.setRate(Float.parseFloat(taxRate.getText().toString()));
                        AddTax(t);// /Add the DbHelper here
                        addTaxBuilder.dismiss();
                        taxName.setText("");
                        taxRate.setText("");
                    }
                });
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        addTaxBuilder.dismiss();
                        taxName.setText("");
                        taxRate.setText("");
                    }
                });
                addTaxBuilder.show();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_settings, menu);
        return true;
    }

    public void AddTax(com.mapplinks.calculator.Taxes tax) {
        ContentValues values = new ContentValues();
        values.put(com.mapplinks.calculator.DbHelper.COLUMN_TAX_NAME, tax.getTax());
        values.put(com.mapplinks.calculator.DbHelper.COLUMN_VALUE, tax.getRate());
        tax.id = mDbhelper.getWritableDatabase().insert(com.mapplinks.calculator.DbHelper.TABLE_NAME, null, values);
        if (tax.id > 0) {
            Toast.makeText(SettingsActivity.this, "Added!", Toast.LENGTH_SHORT).show();
            mAdapter.add(tax);
            mAdapter.notifyDataSetChanged();
        } else {
            Toast.makeText(SettingsActivity.this, "Error!", Toast.LENGTH_SHORT).show();
        }
       /* getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);
                try {
                    for (int index = 0; index < json_array.length(); index++) {
                        JSONObject object = json_array.getJSONObject(index);
                        //String number = object.getString("order_num");
                        //String date = object.getString("date");
                        //Double amount = object.getDouble("amount");
                        //order = new Order(number, date, Double.toString(amount));
                        //adapter.add(order);
                        //adapter.notifyDataSetChanged();
                    }
                } catch (JSONException e) {
                }
            }
        }*/
    }

    private class TaxAdapter extends ArrayAdapter<com.mapplinks.calculator.Taxes> {
        TaxAdapter(ArrayList<com.mapplinks.calculator.Taxes> t) {
            super(SettingsActivity.this, R.layout.tax_list_row, t);
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.tax_list_row, parent, false);
            final com.mapplinks.calculator.Taxes t = getItem(position);
            TextView taxName = (TextView) convertView.findViewById(R.id.tax_name);
            taxName.setText(t.getTax().toUpperCase());
            TextView taxRate = (TextView) convertView.findViewById(R.id.tax_rate);
            taxRate.setText(t.getRate().toString());
            ImageView deleteButton = (ImageView) convertView.findViewById(R.id.delete_tax);
            deleteButton.setImageResource(R.drawable.ic_delete);

            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    deleteTax(t);
                    Toast.makeText(SettingsActivity.this, "Deleted!", Toast.LENGTH_SHORT).show();

                    notifyDataSetChanged();
                }
            });
            return convertView;
        }
    }

    public void deleteTax(com.mapplinks.calculator.Taxes t) {
        String[] whereArgs = {String.valueOf(t.id)};

        mDbhelper.getWritableDatabase().delete(mDbhelper.TABLE_NAME, mDbhelper.COLUMN_ID + "=?", whereArgs);
        mTaxList.remove(t);
    }


    public void getAllTaxes(TaxList taxList) {                                  //gets all the taxes
        Cursor cursor = mDbhelper.getReadableDatabase().query(com.mapplinks.calculator.DbHelper.TABLE_NAME, allColumns, null, null, null, null, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            com.mapplinks.calculator.Taxes tax = cursorToTaxes(cursor);
            taxList.add(tax);
            cursor.moveToNext();
        }
        cursor.close();
    }

    private com.mapplinks.calculator.Taxes cursorToTaxes(Cursor cursor) {                    //To display the taxes
        com.mapplinks.calculator.Taxes tax = new com.mapplinks.calculator.Taxes();
        tax.id = cursor.getLong(0);
        tax.tax = cursor.getString(1);
        tax.rate = cursor.getFloat(2);
        return tax;
    }
}
/*
    public List<com.mapplinks.calculator.Taxes> getAllTaxes(){
       List<com.mapplinks.calculator.Taxes> taxes= new ArrayList<>();
       Cursor cursor= mDbhelper.getReadableDatabase().query(com.mapplinks.calculator.DbHelper.TABLE_NAME,allColumns,null,null,null,null,null);
        cursor.moveToFirst();
        while(!cursor.isAfterLast()){
            com.mapplinks.calculator.Taxes tax=cursorToTaxes(cursor);
            taxes.add(tax);

            cursor.moveToNext();
        }
        cursor.close();
        return taxes;
    }

    public void updateTax(com.mapplinks.calculator.Taxes tax){                      //When editing and updating an already present tax
        ContentValues values = new ContentValues();
        // values.put(com.mapplinks.calculator.DbHelper.COLUMN_TAX_NAME, tax.tax);
        // values.put(com.mapplinks.calculator.DbHelper.COLUMN_VALUE, tax.rate);

        String[] whereArgs={String.valueOf(tax.id)};

        mDbhelper.getWritableDatabase().update(mDbhelper.TABLE_NAME, values, mDbhelper.COLUMN_ID + "=?", whereArgs);
    }


*/