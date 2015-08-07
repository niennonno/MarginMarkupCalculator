package org.example.adityavikram.marginmarkupcalculator;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;


public class CalculatorActivity extends ActionBarActivity {
    int i;
    ArrayAdapter<String> historyAdapter;
    ListView history;
    ArrayList<String> result = new ArrayList<>(5);
    EditText cost;
    EditText sale;
    EditText margin;
    EditText markup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_calculator);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.icon);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);

        historyAdapter = new ArrayAdapter<String>(this, R.layout.textstyle, result);

        history = (ListView) findViewById(R.id.history_list_view);
        history.setAdapter(historyAdapter);


        Button calculate_but, reset_but;


        cost = (EditText) findViewById(R.id.edit_cost_price);
        margin = (EditText) findViewById(R.id.edit_margin);
        markup = (EditText) findViewById(R.id.edit_markup);
        sale = (EditText) findViewById(R.id.edit_sale_price);

        reset_but = (Button) findViewById(R.id.butreset);
        reset_but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cost.setText(null);
                sale.setText(null);
                margin.setText(null);
                markup.setText(null);
            }
        });

        calculate_but = (Button) findViewById(R.id.butcalculate);

        i = 0;
        calculate_but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String SalePrice = sale.getText().toString();
                String CostPrice = cost.getText().toString();
                String Margin = margin.getText().toString();
                String Markup = markup.getText().toString();
                String res;
                if (!SalePrice.isEmpty() && !CostPrice.isEmpty() && !Margin.isEmpty() && !Markup.isEmpty()) {      //being an insufferable know-it-all
                    Toast.makeText(CalculatorActivity.this, "You seem to know it all! Please delete one or more entries.", Toast.LENGTH_SHORT).show();

                } else if (!CostPrice.isEmpty() && !SalePrice.isEmpty()) {
                    if (!Margin.isEmpty()) {
                        float c = Float.parseFloat(CostPrice);
                        float s = Float.parseFloat(SalePrice);
                        float mgn = ((s - c) / s) * 100;
                        float temp = Float.parseFloat(margin.getText().toString());
                        if (temp == mgn) {
                            float mkp = ((s - c) / c) * 100;
                            sale.setText(Float.toString(s));
                            cost.setText(Float.toString(c));
                            margin.setText(Float.toString(mgn) + "%");
                            markup.setText(Float.toString(mkp) + "%");
                            res = "CP:" + c + "\tSP:" + s + "\nMargin:" + mgn + "%\tMarkup:" + mkp + "%";
                            Toast.makeText(CalculatorActivity.this, "Calculation Done!", Toast.LENGTH_SHORT).show();
                            result.add(i, res);
                            i++;
                            i = i % 5;

                        } else {
                            Toast.makeText(CalculatorActivity.this, "Well, that's an abnormal number. Please check and re-enter.", Toast.LENGTH_SHORT).show();
                        }
                    } else if (!Markup.isEmpty()) {
                        float c = Float.parseFloat(CostPrice);
                        float s = Float.parseFloat(SalePrice);
                        float mkp = ((s - c) / c) * 100;
                        float temp = Float.parseFloat(markup.getText().toString());
                        if (temp == mkp) {
                            float mgn = ((s - c) / s) * 100;
                            sale.setText(Float.toString(s));
                            cost.setText(Float.toString(c));
                            margin.setText(Float.toString(mgn) + "%");
                            markup.setText(Float.toString(mkp) + "%");
                            res = "CP:" + c + "\tSP:" + s + "\nMargin:" + mgn + "%\tMarkup:" + mkp + "%";
                            Toast.makeText(CalculatorActivity.this, "Calculation Done!", Toast.LENGTH_SHORT).show();
                            result.add(i, res);
                            i++;
                            i = i % 5;

                        } else {
                            Toast.makeText(CalculatorActivity.this, "Well, that's an abnormal number. Please check and re-enter.", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        float c = Float.parseFloat(CostPrice);
                        float s = Float.parseFloat(SalePrice);
                        float mgn = ((s - c) / s) * 100;
                        float mkp = ((s - c) / c) * 100;
                        sale.setText(Float.toString(s));
                        cost.setText(Float.toString(c));
                        margin.setText(Float.toString(mgn) + "%");
                        markup.setText(Float.toString(mkp) + "%");
                        res = "CP:" + c + "\tSP:" + s + "\nMargin:" + mgn + "%\tMarkup:" + mkp + "%";
                        Toast.makeText(CalculatorActivity.this, "Calculation Done!", Toast.LENGTH_SHORT).show();
                        result.add(i, res);
                        i++;
                        i = i % 5;
                    }

                } else if (SalePrice.equals("") && Markup.equals("")) {
                    float c = Float.parseFloat(CostPrice);
                    float mgn = Float.parseFloat(Margin);
                    float s = c / (1 - mgn / 100);
                    float mkp = ((s - c) / c) * 100;
                    sale.setText(Float.toString(s));
                    cost.setText(Float.toString(c));
                    markup.setText(Float.toString(mkp) + "%");
                    margin.setText(Float.toString(mgn) + "%");
                    res = "CP:" + c + "\tSP:" + s + "\nMargin:" + mgn + "%\tMarkup:" + mkp + "%";
                    Toast.makeText(CalculatorActivity.this, "Calculation Done!", Toast.LENGTH_SHORT).show();
                    result.add(i, res);
                    i++;
                    i = i % 5;

                } else if (SalePrice.equals("") && Margin.equals("")) {
                    float c = Float.parseFloat(CostPrice);
                    float mkp = Float.parseFloat(Markup);
                    float s = c + (c * mkp / 100);
                    float mgn = ((s - c) / s) * 100;
                    sale.setText(Float.toString(s));
                    cost.setText(Float.toString(c));
                    markup.setText(Float.toString(mkp) + "%");
                    margin.setText(Float.toString(mgn) + "%");
                    res = "CP:" + c + "\tSP:" + s + "\nMargin:" + mgn + "%\tMarkup:" + mkp + "%";
                    Toast.makeText(CalculatorActivity.this, "Calculation Done!", Toast.LENGTH_SHORT).show();
                    result.add(i, res);
                    i++;
                    i = i % 5;

                } else if (CostPrice.equals("") && Markup.equals("")) {
                    float s = Float.parseFloat(SalePrice);
                    float mgn = Float.parseFloat(Margin);
                    float c = (1 - mgn / 100) * s;
                    float mkp = ((s - c) / c) * 100;
                    markup.setText(Float.toString(mkp) + "%");
                    margin.setText(Float.toString(mgn) + "%");
                    sale.setText(Float.toString(s));
                    cost.setText(Float.toString(c));
                    res = "CP:" + c + "\tSP:" + s + "\nMargin:" + mgn + "%\tMarkup:" + mkp + "%";
                    Toast.makeText(CalculatorActivity.this, "Calculation Done!", Toast.LENGTH_SHORT).show();
                    result.add(i, res);
                    i++;
                    i = i % 5;

                } else if (CostPrice.equals("") && Margin.equals("")) {
                    float s = Float.parseFloat(SalePrice);
                    float mkp = Float.parseFloat(Markup);
                    float c = s / (1 + mkp / 100);
                    float mgn = ((s - c) / s) * 100;
                    sale.setText(Float.toString(s));
                    cost.setText(Float.toString(c));
                    markup.setText(Float.toString(mkp) + "%");
                    margin.setText(Float.toString(mgn) + "%");
                    res = "CP:" + c + "\tSP:" + s + "\nMargin:" + mgn + "%\tMarkup:" + mkp + "%";
                    Toast.makeText(CalculatorActivity.this, "Calculation Done!", Toast.LENGTH_SHORT).show();
                    result.add(i, res);
                    i++;
                    i = i % 5;

                } else if (!Margin.isEmpty() && !Markup.isEmpty()) {
                    if (!SalePrice.isEmpty()) {
                        float s = Float.parseFloat(SalePrice);
                        float mgn = Float.parseFloat(Margin);
                        float mkp = Float.parseFloat(Markup);
                        float c = (mgn / mkp) * s;
                        sale.setText(Float.toString(s));
                        markup.setText(Float.toString(mkp) + "%");
                        margin.setText(Float.toString(mgn) + "%");
                        cost.setText(Float.toString(c));
                        res = "CP:" + c + "\tSP:" + s + "\nMargin:" + mgn + "%\tMarkup:" + mkp + "%";
                        Toast.makeText(CalculatorActivity.this, "Calculation Done!", Toast.LENGTH_SHORT).show();
                        result.add(i, res);
                        i++;
                        i = i % 5;

                    } else if (!CostPrice.isEmpty()) {
                        float c = Float.parseFloat(CostPrice);
                        float mgn = Float.parseFloat(Margin);
                        float mkp = Float.parseFloat(Markup);
                        float s = (mkp / mgn) * c;
                        markup.setText(Float.toString(mkp) + "%");
                        margin.setText(Float.toString(mgn) + "%");
                        cost.setText(Float.toString(c));
                        sale.setText(Float.toString(s));
                        res = "CP:" + c + "\tSP:" + s + "\nMargin:" + mgn + "%\tMarkup:" + mkp + "%";
                        Toast.makeText(CalculatorActivity.this, "Calculation Done!", Toast.LENGTH_SHORT).show();
                        result.add(i, res);
                        i++;
                        i = i % 5;
                    } else {
                        Toast.makeText(CalculatorActivity.this, "This combo no good. Please enter one more field. ", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(CalculatorActivity.this, "We ain't that clever! Please enter one or more fields(s)", Toast.LENGTH_SHORT).show();
                }


                historyAdapter.notifyDataSetChanged();

            }

        });


    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
