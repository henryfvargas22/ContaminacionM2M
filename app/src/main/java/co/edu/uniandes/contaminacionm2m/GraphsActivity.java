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

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
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
    List<event> eventos;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_flush:
                    pieChart();
                    return true;
                case R.id.navigation_all:
                    //mTextMessage.setText(R.string.title_dashboard);
                    return true;
                case R.id.navigation_smoke:
                    //mTextMessage.setText(R.string.title_notifications);
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
        pieChart();
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

    }

    private void pieChart()
    {
        final ViewGroup viewF=(ViewGroup)findViewById(R.id.viewGroup);

        final ViewGroup viewGroup = (ViewGroup)findViewById(R.id.grupo);
        viewGroup.removeAllViews();
        View view=View.inflate(this,R.layout.pie_chart,null);

        viewGroup.addView(View.inflate(this,R.layout.pie_chart,null));

        int flush=0;
        int smoke=0;
        for(int i=0;i<eventos.size();i++)
        {
            if(eventos.get(i).getType().equals(event.FLUSH))
            {
                flush++;
                Log.d("Entro","El evento es tipo FLUSH");
            }
            else
            {
                smoke++;
            }
        }
        int total=flush+smoke;
        double pFlush=(double) flush/total;
        double pSmoke=(double) smoke/total;

        pFlush=pFlush*100;
        pSmoke=pSmoke*100;

        Log.d("dou1",pFlush+"");
        Log.d("dou2",pSmoke+"");

        pieChart=(PieChart) findViewById(R.id.pie1);
        pieChart.setTouchEnabled(false);

        List<PieEntry> entries = new ArrayList<>();

        PieEntry pieEntry1=new PieEntry((float)pFlush, "Flujo");
        PieEntry pieEntry2=new PieEntry((float)pSmoke, "Humo");

        float en1=pieEntry1.getValue();
        float en2=pieEntry2.getValue();

        Log.d("Float1",en1+"");
        Log.d("Float2",en2+"");

        entries.add(pieEntry1);
        entries.add(pieEntry2);

        PieDataSet set = new PieDataSet(entries, "Resultados del monitoreo");

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
}
