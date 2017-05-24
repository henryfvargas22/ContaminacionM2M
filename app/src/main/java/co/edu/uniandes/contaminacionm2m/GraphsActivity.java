package co.edu.uniandes.contaminacionm2m;

import android.content.Intent;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

import co.edu.uniandes.contaminacionm2m.list.event;

public class GraphsActivity extends AppCompatActivity {

    String ip;
    String puerto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graphs);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent=getIntent();
        int test= intent.getIntExtra("lista",-1);
        TextView textView=(TextView) findViewById(R.id.texto);
        textView.setText(test+"");

        ip=intent.getStringExtra("ip");
        puerto=intent.getStringExtra("puerto");
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
}
