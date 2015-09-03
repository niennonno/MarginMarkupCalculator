package com.mapplinks.calculator;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;

import io.fabric.sdk.android.Fabric;
import mapplinks.example.adityavikram.calculator.TaxList;


import com.mapplinks.X.R;
import com.mapplinks.calculator.SettingsActivity;
import com.mixpanel.android.mpmetrics.MixpanelAPI;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.tweetcomposer.TweetComposer;


import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

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
    float diff;
    com.mapplinks.calculator.DbHelper mDbHelper= com.mapplinks.calculator.DbHelper.getInstance(this);
    TaxList mTaxList;
    int clk = 0;
    ArrayList<String> taxName=new ArrayList();
    com.mapplinks.calculator.Taxes t;
    String[] allColumns = {
            com.mapplinks.calculator.DbHelper.COLUMN_ID,
            com.mapplinks.calculator.DbHelper.COLUMN_TAX_NAME,
            com.mapplinks.calculator.DbHelper.COLUMN_VALUE
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TwitterAuthConfig authConfig = new TwitterAuthConfig("consumerKey", "consumerSecret");
        Fabric.with(this, new Twitter(authConfig), new TweetComposer());
        getAllTaxes(mTaxList);
        for (int i=0;!mTaxList.isEmpty();i++){
            t=mTaxList.get(i);
            taxName.add(t.getTax());
        }
       String projectToken = "d0791a98ffdef2197bc005a77abd92ba";
       final MixpanelAPI mixpanel = MixpanelAPI.getInstance(this, projectToken);


        setContentView(R.layout.activity_calculator);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);

        historyAdapter = new ArrayAdapter<String>(this, R.layout.textstyle, result);

        history = (ListView) findViewById(R.id.history_list_view);
        history.setAdapter(historyAdapter);


        Button calculate_but, reset_but;


        cost = (EditText) findViewById(R.id.edit_cost_price);
        margin = (EditText) findViewById(R.id.edit_margin);
        markup = (EditText) findViewById(R.id.edit_markup);
        sale = (EditText) findViewById(R.id.edit_sale_price);
        TextView addTax=(TextView)findViewById(R.id.add_tax_dialog);
        reset_but = (Button) findViewById(R.id.butreset);
        reset_but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cost.setEnabled(true);
                sale.setEnabled(true);
                margin.setEnabled(true);
                markup.setEnabled(true);
                cost.setText(null);
                sale.setText(null);
                margin.setText(null);
                markup.setText(null);

            }
        });

        addTax.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        calculate_but = (Button) findViewById(R.id.butcalculate);
        i = 0;
        calculate_but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mixpanel.track("Calculate");
                clk++;
                if (clk == 10) {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse("market://details?id=com.mapplinks.calculator"));
                    if (!MyStartActivity(intent)) {
                        intent.setData(Uri.parse("https://play.google.com/store/apps/details?[Id]"));
                        if (!MyStartActivity(intent)) {
                            Toast.makeText(CalculatorActivity.this, "Could not open Android market, please install the market app.", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                String SalePrice = sale.getText().toString();
                String CostPrice = cost.getText().toString();
                String Margin = margin.getText().toString();
                String Markup = markup.getText().toString();
                String res = new String();

                calc(SalePrice, CostPrice, Margin, Markup, res);

                historyAdapter.notifyDataSetChanged();

            }

        });
    }

    public void calc(String SalePrice, String CostPrice, String Margin, String Markup, String res) {
        if (!SalePrice.isEmpty() && !CostPrice.isEmpty() && !Margin.isEmpty() && !Markup.isEmpty()) {      //being an insufferable know-it-all
            Toast.makeText(CalculatorActivity.this, "You seem to know it all! Please delete one or more entries.", Toast.LENGTH_SHORT).show();

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
                diff=s-c;
                if(diff<0){
                    diff=diff*-1;
                    res = "CP:" + c + "\tSP:" + s + "\nMargin:" + mgn + "%\tMarkup:" + mkp + "%\tLoss:"+diff;
                }else{
                    res = "CP:" + c + "\tSP:" + s + "\nMargin:" + mgn + "%\tMarkup:" + mkp + "%\tProfit:"+diff;
                }
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
                diff=s-c;
                if(diff<0){
                    diff=diff*-1;
                    res = "CP:" + c + "\tSP:" + s + "\nMargin:" + mgn + "%\tMarkup:" + mkp + "%\tLoss:"+diff;
                }else{
                    res = "CP:" + c + "\tSP:" + s + "\nMargin:" + mgn + "%\tMarkup:" + mkp + "%\tProfit:"+diff;
                }
                Toast.makeText(CalculatorActivity.this, "Calculation Done!", Toast.LENGTH_SHORT).show();
                result.add(i, res);
                i++;
                i = i % 5;
            } else {
                Toast.makeText(CalculatorActivity.this, "This combo no good. Please enter one more field. ", Toast.LENGTH_SHORT).show();
            }
        } else if (!CostPrice.isEmpty() && !SalePrice.isEmpty()) {
            if (!Margin.isEmpty()) {
                float c = Float.parseFloat(CostPrice);
                float s = Float.parseFloat(SalePrice);
                float mgn = ((s - c) / s) * 100;
                float mkp = ((s - c) / c) * 100;
                sale.setText(Float.toString(s));
                cost.setText(Float.toString(c));
                margin.setText(Float.toString(mgn) + "%");
                markup.setText(Float.toString(mkp) + "%");
                diff=s-c;
                if(diff<0){
                    diff=diff*-1;
                    res = "CP:" + c + "\tSP:" + s + "\nMargin:" + mgn + "%\tMarkup:" + mkp + "%\tLoss:"+diff;
                }else{
                    res = "CP:" + c + "\tSP:" + s + "\nMargin:" + mgn + "%\tMarkup:" + mkp + "%\tProfit:"+diff;
                }
                Toast.makeText(CalculatorActivity.this, "Calculation Done!", Toast.LENGTH_SHORT).show();
                result.add(i, res);
                i++;
                i = i % 5;
            } else if (!Markup.isEmpty()) {
                float c = Float.parseFloat(CostPrice);
                float s = Float.parseFloat(SalePrice);
                float mkp = ((s - c) / c) * 100;
                float mgn = ((s - c) / s) * 100;
                sale.setText(Float.toString(s));
                cost.setText(Float.toString(c));
                margin.setText(Float.toString(mgn) + "%");
                markup.setText(Float.toString(mkp) + "%");
                diff=s-c;
                if(diff<0){
                    diff=diff*-1;
                    res = "CP:" + c + "\tSP:" + s + "\nMargin:" + mgn + "%\tMarkup:" + mkp + "%\tLoss:"+diff;
                }else{
                    res = "CP:" + c + "\tSP:" + s + "\nMargin:" + mgn + "%\tMarkup:" + mkp + "%\tProfit:"+diff;
                }
                Toast.makeText(CalculatorActivity.this, "Calculation Done!", Toast.LENGTH_SHORT).show();
                result.add(i, res);
                i++;
                i = i % 5;

            } else {
                float c = Float.parseFloat(CostPrice);
                float s = Float.parseFloat(SalePrice);
                float mgn = ((s - c) / s) * 100;
                float mkp = ((s - c) / c) * 100;
                sale.setText(Float.toString(s));
                cost.setText(Float.toString(c));
                margin.setText(Float.toString(mgn) + "%");
                markup.setText(Float.toString(mkp) + "%");
                diff=s-c;
                if(diff<0){
                    diff=diff*-1;
                    res = "CP:" + c + "\tSP:" + s + "\nMargin:" + mgn + "%\tMarkup:" + mkp + "%\tLoss:"+diff;
                }else{
                    res = "CP:" + c + "\tSP:" + s + "\nMargin:" + mgn + "%\tMarkup:" + mkp + "%\tProfit:"+diff;
                }
                Toast.makeText(CalculatorActivity.this, "Calculation Done!", Toast.LENGTH_SHORT).show();
                result.add(i, res);
                i++;
                i = i % 5;
            }

        } else if (!CostPrice.isEmpty() && !Margin.isEmpty()) {
            float c = Float.parseFloat(CostPrice);
            float mgn = Float.parseFloat(Margin);
            float s = c / (1 - mgn / 100);
            float mkp = ((s - c) / c) * 100;
            sale.setText(Float.toString(s));
            cost.setText(Float.toString(c));
            markup.setText(Float.toString(mkp) + "%");
            margin.setText(Float.toString(mgn) + "%");
            diff=s-c;
            if(diff<0){
                diff=diff*-1;
                res = "CP:" + c + "\tSP:" + s + "\nMargin:" + mgn + "%\tMarkup:" + mkp + "%\tLoss:"+diff;
            }else{
                res = "CP:" + c + "\tSP:" + s + "\nMargin:" + mgn + "%\tMarkup:" + mkp + "%\tProfit:"+diff;
            }
            Toast.makeText(CalculatorActivity.this, "Calculation Done!", Toast.LENGTH_SHORT).show();
            result.add(i, res);
            i++;
            i = i % 5;

        } else if (!CostPrice.isEmpty() && !Markup.isEmpty()) {
            float c = Float.parseFloat(CostPrice);
            float mkp = Float.parseFloat(Markup);
            float s = c + (c * mkp / 100);
            float mgn = ((s - c) / s) * 100;
            sale.setText(Float.toString(s));
            cost.setText(Float.toString(c));
            markup.setText(Float.toString(mkp) + "%");
            margin.setText(Float.toString(mgn) + "%");
            diff=s-c;
            if(diff<0){
                diff=diff*-1;
                res = "CP:" + c + "\tSP:" + s + "\nMargin:" + mgn + "%\tMarkup:" + mkp + "%\tLoss:"+diff;
            }else{
                res = "CP:" + c + "\tSP:" + s + "\nMargin:" + mgn + "%\tMarkup:" + mkp + "%\tProfit:"+diff;
            }
            Toast.makeText(CalculatorActivity.this, "Calculation Done!", Toast.LENGTH_SHORT).show();
            result.add(i, res);
            i++;
            i = i % 5;

        } else if (!SalePrice.isEmpty() && !Margin.isEmpty()) {
            float s = Float.parseFloat(SalePrice);
            float mgn = Float.parseFloat(Margin);
            float c = (1 - mgn / 100) * s;
            float mkp = ((s - c) / c) * 100;
            markup.setText(Float.toString(mkp) + "%");
            margin.setText(Float.toString(mgn) + "%");
            sale.setText(Float.toString(s));
            cost.setText(Float.toString(c));
            diff=s-c;
            if(diff<0){
                diff=diff*-1;
                res = "CP:" + c + "\tSP:" + s + "\nMargin:" + mgn + "%\tMarkup:" + mkp + "%\tLoss:"+diff;
            }else{
                res = "CP:" + c + "\tSP:" + s + "\nMargin:" + mgn + "%\tMarkup:" + mkp + "%\tProfit:"+diff;
            }
            Toast.makeText(CalculatorActivity.this, "Calculation Done!", Toast.LENGTH_SHORT).show();
            result.add(i, res);
            i++;
            i = i % 5;

        } else if (!SalePrice.isEmpty() && !Markup.isEmpty()) {
            float s = Float.parseFloat(SalePrice);
            float mkp = Float.parseFloat(Markup);
            float c = s / (1 + mkp / 100);
            float mgn = ((s - c) / s) * 100;
            sale.setText(Float.toString(s));
            cost.setText(Float.toString(c));
            markup.setText(Float.toString(mkp) + "%");
            margin.setText(Float.toString(mgn) + "%");
            diff=s-c;
            if(diff<0){
                diff=diff*-1;
                res = "CP:" + c + "\tSP:" + s + "\nMargin:" + mgn + "%\tMarkup:" + mkp + "%\tLoss:"+diff;
            }else{
                res = "CP:" + c + "\tSP:" + s + "\nMargin:" + mgn + "%\tMarkup:" + mkp + "%\tProfit:"+diff;
            }
            Toast.makeText(CalculatorActivity.this, "Calculation Done!", Toast.LENGTH_SHORT).show();
            result.add(i, res);
            i++;
            i = i % 5;

        } else {
            Toast.makeText(CalculatorActivity.this, "We ain't that clever! Please enter one or more fields(s)", Toast.LENGTH_SHORT).show();
        }

        cost.setEnabled(false);
        sale.setEnabled(false);
        margin.setEnabled(false);
        markup.setEnabled(false);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_calculator, menu);
        return true;

    }

    public void getAllTaxes(TaxList taxList) {                                  //gets all the taxes
        Cursor cursor = mDbHelper.getReadableDatabase().query(com.mapplinks.calculator.DbHelper.TABLE_NAME, allColumns, null, null, null, null, null);
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

    private boolean MyStartActivity(Intent aIntent) {
        try {
            startActivity(aIntent);
            return true;
        } catch (ActivityNotFoundException e) {
            return false;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.share) {
            shareTextUrl();
        } else if(id==R.id.add_tax_menu){
            Intent i1 =new Intent(CalculatorActivity.this, com.mapplinks.calculator.SettingsActivity.class);
            CalculatorActivity.this.startActivity(i1);
        }else if (id == R.id.feedback) {
            Intent i = new Intent(CalculatorActivity.this, com.mapplinks.calculator.FeedbackActivity.class);
            CalculatorActivity.this.startActivity(i);
        } else if (id == R.id.rate) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("market://details?id=com.mapplinks.calculator"));
            if (!MyStartActivity(intent)) {
                intent.setData(Uri.parse("https://play.google.com/store/apps/details?[Id]"));
                if (!MyStartActivity(intent)) {
                    Toast.makeText(this, "Could not open Android market, please install the market app.", Toast.LENGTH_SHORT).show();
                }
            }
        }

        return super.onOptionsItemSelected(item);
    }


    private void shareTextUrl() {
        Intent share = new Intent(android.content.Intent.ACTION_SEND);
        share.setType("text/plain");
        share.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);

        // Add data to the intent, the receiving app will decide
        // what to do with it.
        share.putExtra(Intent.EXTRA_SUBJECT, "Percentor");
        share.putExtra(Intent.EXTRA_TEXT, "I am using @Percentor to improve my #sales efficiency via @Mapplinks. Find it here: https://goo.gl/DIuFZI");

        startActivity(Intent.createChooser(share, "Spread the Word!"));
    }
}
