package co.edu.uniandes.contaminacionm2m;

import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;

import co.edu.uniandes.contaminacionm2m.list.event;

public class GraphsActivity extends AppCompatActivity {

    String ip;
    String puerto;
    PieChart pieChart;
    LineChart lineChart;
    List<event> eventos;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_flush:
                    lineChart();
                    return true;
                case R.id.navigation_all:
                    pieChart();
                    return true;
                case R.id.navigation_smoke:
                    lineChart2();
                    return true;
            }
            return false;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_graphs);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



        Intent intent=getIntent();

        ip=intent.getStringExtra("ip");
        puerto=intent.getStringExtra("puerto");
        eventos= (ArrayList) intent.getParcelableArrayListExtra("lista");
        //Log.d("Tamaño", "El tamaño del arraylist es: "+eventos.size());

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        lineChart();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.principal, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent=new Intent(this,MainActivity.class);
            startActivity(intent);
        }
        else if (item.getItemId() == android.R.id.home) {
            // do something here, such as start an Intent to the parent activity.
            Intent intent=new Intent(this,PrincipalActivity.class);
            intent.putExtra("ip",ip);
            intent.putExtra("puerto",puerto);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        Intent intent=new Intent(this,PrincipalActivity.class);
        intent.putExtra("ip",ip);
        intent.putExtra("puerto",puerto);
        startActivity(intent);
    }

    private void pieChart()
    {
        View line=(View)findViewById(R.id.chartFlujo);
        line.setVisibility(View.GONE);

        View view=(View)findViewById(R.id.pie1);

        view.setVisibility(View.VISIBLE);

        int events=0;
        int eventsAlert=0;
        for(int i=0;i<eventos.size();i++)
        {
                if(eventos.get(i).isAlert())
                {
                    eventsAlert++;
                }
                else
                {
                    events++;
                }
        }
        int total=events+eventsAlert;
        double pEvents=(double) events/total;
        double pAlert=(double) eventsAlert/total;

        pEvents=pEvents*100;
        pAlert=pAlert*100;

        pieChart=(PieChart) findViewById(R.id.pie1);
        pieChart.setTouchEnabled(false);

        List<PieEntry> entries = new ArrayList<>();

        PieEntry pieEntry1=new PieEntry((float)pEvents, "Eventos normales");
        PieEntry pieEntry2=new PieEntry((float)pAlert, "Eventos alarmantes");

        entries.add(pieEntry1);
        entries.add(pieEntry2);

        PieDataSet set = new PieDataSet(entries, "Monitoreo de Alertas");
        Description description=new Description();
        description.setText("Nivel de alertas en el tiempo");
        pieChart.setDescription(description);

        ArrayList<Integer> colors = new ArrayList<Integer>();

        for (int c : ColorTemplate.VORDIPLOM_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.JOYFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.COLORFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.LIBERTY_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.PASTEL_COLORS)
            colors.add(c);

        colors.add(ColorTemplate.getHoloBlue());

        set.setColors(colors);

        PieData data = new PieData(set);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(11f);
        data.setValueTextColor(Color.BLACK);

        pieChart.setData(data);
        pieChart.invalidate();
    }

    private void lineChart() {
        View pie = (View) findViewById(R.id.pie1);
        pie.setVisibility(View.GONE);

        View view = (View) findViewById(R.id.chartFlujo);
        view.setVisibility(View.VISIBLE);

        lineChart = (LineChart) findViewById(R.id.chartFlujo);
        Description description=new Description();
        description.setText("Flujo del agua a través del tiempo");
        lineChart.setDescription(description);

        List<Entry> entries = new ArrayList<Entry>();

        for (int i = 0; i < eventos.size(); i++) {
            if (eventos.get(i).getType().equals(event.FLUSH)) {
                Entry entry = new Entry(eventos.get(i).getId(), eventos.get(i).getValue());
                entries.add(entry);
                Log.d("Evento", "x: " + entry.getX() + " y: " + entry.getY());
            }
        }

        LineDataSet dataSet = new LineDataSet(entries, "Flujo medido");
        dataSet.setDrawCircles(true);// add entries to dataset
        dataSet.setDrawFilled(true);
        dataSet.setColor(Color.BLUE);
        dataSet.setValueTextColor(Color.BLACK);

        LineData lineData = new LineData(dataSet);
        lineChart.setData(lineData);

        lineChart.animateX(1000);
    }

    private void lineChart2() {
        View pie = (View) findViewById(R.id.pie1);
        pie.setVisibility(View.GONE);

        View view = (View) findViewById(R.id.chartFlujo);
        view.setVisibility(View.VISIBLE);

        lineChart = (LineChart) findViewById(R.id.chartFlujo);
        Description description=new Description();
        description.setText("Flujo de humo a través del tiempo");
        lineChart.setDescription(description);

        List<Entry> entries = new ArrayList<Entry>();

        for (int i = 0; i < eventos.size(); i++) {
            if (eventos.get(i).getType().equals(event.SMOKE)) {
                Entry entry = new Entry(eventos.get(i).getId(), eventos.get(i).getValue());
                entries.add(entry);
                Log.d("Evento", "x: " + entry.getX() + " y: " + entry.getY());
            }
        }

        LineDataSet dataSet = new LineDataSet(entries, "Flujo de humo medido");
        dataSet.setDrawCircles(true);// add entries to dataset
        dataSet.setDrawFilled(true);
        dataSet.setColor(Color.BLUE);
        dataSet.setValueTextColor(Color.BLACK);

        LineData lineData = new LineData(dataSet);
        lineChart.setData(lineData);

        lineChart.animateX(1000);
    }
}
