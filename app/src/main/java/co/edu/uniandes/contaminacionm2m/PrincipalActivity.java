package co.edu.uniandes.contaminacionm2m;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import co.edu.uniandes.contaminacionm2m.list.ListArrayAdapter;
import co.edu.uniandes.contaminacionm2m.list.event;

public class PrincipalActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public final static String APPID="Contamina1";
    public final static String CONTAINER="contamination";

    RequestQueue mRequestQueue;
    String ip;
    String puerto;
    List<String> results;
    List<event> events;
    String url;
    Context context;
    Handler autoUpdateHandler;

    private ListView lv;
    private ListArrayAdapter arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        final Intent intent=getIntent();
        ip=intent.getStringExtra("ip");
        puerto=intent.getStringExtra("puerto");
        this.context=getApplicationContext();

        autoUpdateHandler= new Handler();
        autoUpdateHandler.postDelayed(new Runnable() {
            @Override
            public void run()
            {
                //start the following method every 1 second.
                updateListJSON();
                autoUpdateHandler.postDelayed(this,1000);

            }
        },1000);

        // Instantiate the cache
        Cache cache = new DiskBasedCache(getCacheDir(), 1024 * 1024); // 1MB cap

        // Set up the network to use HttpURLConnection as the HTTP client.
        Network network = new BasicNetwork(new HurlStack());

        // Instantiate the RequestQueue with the cache and network.
        mRequestQueue = new RequestQueue(cache, network);

        // Start the queue
        mRequestQueue.start();


        url="http://"+ip+":"+puerto+"/m2m/applications/"+APPID+"/containers/"+CONTAINER+"/contentInstances";
        Log.d("IP",url);

        results= new ArrayList<String>();
        events = new ArrayList<event>();
        lv = (ListView) findViewById(R.id.list);

        arrayAdapter = new ListArrayAdapter(context,results,events);

        //arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,results);

        lv.setAdapter(arrayAdapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent1=new Intent(PrincipalActivity.this,EventActivity.class);
                event test=arrayAdapter.getEvent(i);
                intent1.putExtra("ip",ip);
                intent1.putExtra("puerto",puerto);
                intent1.putExtra("event",test);

                mRequestQueue.stop();

                startActivity(intent1);
            }
        });
    }

    private void updateListJSON()
    {
        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, url, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try
                        {
                            //Log.d("ENTRO","Entra al try");
                            JSONObject instances=response.getJSONObject("contentInstances");
                            JSONObject collection=instances.getJSONObject("contentInstanceCollection");
                            JSONArray contentInstances=collection.getJSONArray("contentInstance");
                            arrayAdapter.clear();
                            arrayAdapter.clearEvents();
                            for(int i=0;i<contentInstances.length();i++)
                            {
                                JSONObject temp=contentInstances.getJSONObject(i);
                                JSONObject content=temp.getJSONObject("content");
                                String time=temp.getString("creationTime");
                                String[] tiempo=time.split(":");
                                String tiempoF=tiempo[0].replace("T"," ")+":"+tiempo[1];
                                //Log.d("ENTRO","el tiempo es: "+tiempoF);
                                String resp=content.getString("binaryContent");
                                byte[] decoded= Base64.decode(resp.getBytes(), Base64.DEFAULT);
                                String pop=new String(decoded);
                                JSONObject json=new JSONObject(pop);
                                //Log.d("ENTRO","nuevo json: "+pop);
                                pop=pop.replace("\"","");
                                pop=pop.replace("{","");
                                pop=pop.replace("}","");
                                Log.d("ENTRO",pop);
                                String[] variables=pop.split(",");
                                String tipo=variables[0].split(":")[0].trim();
                                String tipo2=variables[1].split(":")[0].trim();
                                String type=tipo.toUpperCase();
                                String type2=tipo2.toUpperCase();

                                int valor=Integer.parseInt(variables[0].split(":")[1].trim());
                                int valor2=Integer.parseInt(variables[1].split(":")[1].trim());

                                //Log.d("ENTRO","el valor es: "+valor);
                                event temporal=new event(type,tiempoF,valor);
                                event temporal2=new event(type2,tiempoF,valor2);
                                arrayAdapter.add(tiempoF);
                                arrayAdapter.addEvent(temporal);
                                int tam=tiempoF.length();
                                int last=Integer.parseInt(tiempoF.charAt(tam-1)+"");
                                last++;
                                String tiempo2=tiempoF.substring(0,(tam-1))+last;
                                arrayAdapter.add(tiempo2);
                                arrayAdapter.addEvent(temporal2);

                                //Log.d("ENTRO","Voy a notificar los datos");
                                arrayAdapter.notifyDataSetChanged();
                            }
                            arrayAdapter.notifyDataSetChanged();
                        }
                        catch (JSONException e)
                        {
                            Log.e("ERROR",e.getMessage());
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        try {
                            Log.d("ERROR", error.getMessage());
                        }catch (Exception e)
                        {
                            //No se hace nada, no hay conexión.
                        }

                    }
                });
        mRequestQueue.add(jsObjRequest);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            //No se debe devolver
        }
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
            mRequestQueue.stop();
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.nav_slideshow) {
            Intent intent=new Intent(this, GraphsActivity.class);
            mRequestQueue.stop();
            ArrayList<event> eventsF=new ArrayList<event>();
            eventsF.addAll(arrayAdapter.getEvents());
            intent.putExtra("lista",eventsF.size());
            intent.putExtra("ip",ip);
            intent.putExtra("puerto",puerto);
            startActivity(intent);

        }  else if (id == R.id.nav_share) {
            AlertDialog alertDialog = new AlertDialog.Builder(PrincipalActivity.this).create();
            alertDialog.setTitle("Datos de la conexión");
            alertDialog.setMessage("IP: "+ip+"\n"+"Puerto: "+puerto);
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            alertDialog.show();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
