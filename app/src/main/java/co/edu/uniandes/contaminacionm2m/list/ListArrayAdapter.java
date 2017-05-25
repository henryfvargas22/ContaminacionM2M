package co.edu.uniandes.contaminacionm2m.list;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import co.edu.uniandes.contaminacionm2m.R;

/**
 * Created by henryfabianvargas on 22/05/17.
 */

public class ListArrayAdapter extends ArrayAdapter<String> {
    private final Context context;
    private final List<String> values;
    private final List<event> events;

    public ListArrayAdapter(Context context, List<String> values, List<event> events) {
        super(context,R.layout.list_sensor,values);
        this.context = context;
        this.values = values;
        this.events=events;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View rowView = inflater.inflate(R.layout.list_sensor, parent, false);
        TextView textView = (TextView) rowView.findViewById(R.id.label);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.logo);
        textView.setText(values.get(position));

        // Change icon based on name
        String type = events.get(position).getType();

        //System.out.println(type);

        if (type.contains(event.FLUSH)) {
            imageView.setImageResource(R.drawable.drop);
            if(events.get(position).isAlert())
            {
                textView.setText("Alerta: "+values.get(position));
                textView.setTextColor(Color.RED);
            }
        } else {
            imageView.setImageResource(R.drawable.match);
            if(events.get(position).isAlert())
            {
                textView.setText("Alerta: "+values.get(position));
                textView.setTextColor(Color.RED);
            }
        }

        return rowView;
    }

    public void addEvent(event event) {
        events.add(event);
    }

    public void clearEvents()
    {
        events.clear();
    }

    public List<event> getEvents()
    {
        return events;
    }

    public event getEvent(int position)
    {
        return events.get(position);
    }
}
