package com.example.earthquakereport;

import android.app.Activity;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.List;

public class earthquake_adapter extends RecyclerView.Adapter<earthquake_adapter.myviewholder>
{
    public static  final  String LOCATION_SEPARTOR =" of ";
    String primary_location,Secondary_Location;
    List<Earthquake_items_prototype> earthquakes;
    view_click_handling listener;
  MainActivity context;
    earthquake_adapter(List<Earthquake_items_prototype> earthquakes, MainActivity listener)
    {
        this.earthquakes=earthquakes;
        this.listener=listener;
        context=listener;
    }
    @Override
    public myviewholder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.earthquake_item,parent,false);
        myviewholder my=new myviewholder(view);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onViewClick(my.getAdapterPosition());
            }
        });
        return my;
    }

    @Override
    public int getItemCount() {
        return earthquakes.size();
    }

    @Override
    public void onBindViewHolder(myviewholder holder, int position) {
        Earthquake_items_prototype single_earthquake=earthquakes.get(position);
        holder.date.setText(stringDate(single_earthquake.getTime()));
        holder.Time.setText(stringTime(single_earthquake.getTime()));
        holder.magnitude.setText(singlepalce_double(single_earthquake.getMagnitude()));

        String original_laocation=single_earthquake.getlocation();
        if(original_laocation.contains(LOCATION_SEPARTOR))
        {
            String[] arr=original_laocation.split(LOCATION_SEPARTOR);
            primary_location=arr[0]+LOCATION_SEPARTOR;
            Secondary_Location=arr[1];
        }
        else{
            primary_location= context.getString(R.string.near_the);
            Secondary_Location=original_laocation;

        }

        holder.second_location.setText(Secondary_Location);
        holder.first_location.setText(primary_location);


        GradientDrawable circle= (GradientDrawable) holder.magnitude.getBackground();
        circle.setColor(circle_color((int)single_earthquake.getMagnitude()));
    }

    public class myviewholder extends RecyclerView.ViewHolder
    {
        View v;

        TextView magnitude,first_location,second_location,date,Time;
        public myviewholder(View itemView) {
            super(itemView);
            v=itemView;
            magnitude= (TextView) itemView.findViewById(R.id.magnitude_circle);
            first_location=(TextView) itemView.findViewById(R.id.location);
            second_location=(TextView) itemView.findViewById(R.id.place);
            date=(TextView) itemView.findViewById(R.id.date);
            Time=(TextView) itemView.findViewById(R.id.time);
        }
    }
  static String stringTime(long time)
    {
        SimpleDateFormat formatter=new SimpleDateFormat("hh:mm:ss");
        return formatter.format(time);
    }
    static String stringDate(long time)
    {
        SimpleDateFormat formatter=new SimpleDateFormat("MMM dd,yyyy");
        return formatter.format(time);
    }
    static String singlepalce_double(double magnitude)
    {
        DecimalFormat d=new DecimalFormat("0.0");
        return  d.format(magnitude);

    }

   private  int circle_color(int magnitude)
    {
        int magnitudeColorResourceId;
        switch(magnitude)
        {
            case 0:
            case 1:
                magnitudeColorResourceId = R.color.magnitude1;
                break;
            case 2:
                magnitudeColorResourceId = R.color.magnitude2;
                break;
            case 3:
                magnitudeColorResourceId = R.color.magnitude3;
                break;
            case 4:
                magnitudeColorResourceId = R.color.magnitude4;
                break;
            case 5:
                magnitudeColorResourceId = R.color.magnitude5;
                break;
            case 6:
                magnitudeColorResourceId = R.color.magnitude6;
                break;
            case 7:
                magnitudeColorResourceId = R.color.magnitude7;
                break;
            case 8:
                magnitudeColorResourceId = R.color.magnitude8;
                break;
            case 9:
                magnitudeColorResourceId = R.color.magnitude9;
                break;
            default:
                magnitudeColorResourceId = R.color.magnitude10plus;
                break;

        }
        return ContextCompat.getColor(context.getBaseContext(), magnitudeColorResourceId);
    }

    public interface  view_click_handling{
        void onViewClick(int position);
    }
}

