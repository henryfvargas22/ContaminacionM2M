package co.edu.uniandes.contaminacionm2m;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import co.edu.uniandes.contaminacionm2m.list.event;

public class EventActivity extends AppCompatActivity {

    String ip;
    String puerto;
    TextView textType;
    TextView textTime;
    TextView textAlert;
    TextView textValor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent=getIntent();
        ip=intent.getStringExtra("ip");
        puerto=intent.getStringExtra("puerto");
        event evento= (event) intent.getSerializableExtra("event");
        setTitle(evento.getTime());

        textTime=(TextView)findViewById(R.id.text_timeR);
        textType=(TextView)findViewById(R.id.text_typeR);
        textAlert=(TextView)findViewById(R.id.text_alertR);
        textValor=(TextView)findViewById(R.id.text_valorR);

        textTime.setText(evento.getTime());
        if(evento.getType().equals(event.FLUSH))
        {
            textType.setText("Flujo");
        }
        else
        {
            textType.setText("Humo");
        }
        if(evento.isAlert())
        {
            textAlert.setText("Alta");
            textAlert.setTextColor(Color.RED);
        }
        else
        {
            textAlert.setText("Normal");
        }

        textValor.setText(evento.toString());
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
}
