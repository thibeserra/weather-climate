package com.sapri.weatherclimate.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.sapri.weatherclimate.data.weatherapi.CityWeather
import com.sapri.weatherclimate.R
import com.squareup.picasso.Picasso

class CityWeatherAdapter (context: Context) : ArrayAdapter<CityWeather>(context, 0) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

        val v: View

        if(convertView != null) {
            v = convertView
        } else {
            v = LayoutInflater.from(context).inflate(R.layout.list_view_item, parent, false)
        }

        val item = getItem(position)

        val txt_city = v.findViewById<TextView>(R.id.txt_city)
        val txt_wheather_description = v.findViewById<TextView>(R.id.txt_wheather_description)
        val txt_currenty_temperature = v.findViewById<TextView>(R.id.txt_currenty_temperature)
        val txt_min_temperature = v.findViewById<TextView>(R.id.txt_min_temperature)
        val txt_max_temperature = v.findViewById<TextView>(R.id.txt_max_temperature)
        val img_wheather = v.findViewById<ImageView>(R.id.img_wheather)

        item?.let {
            txt_city.text = item.city
            txt_wheather_description.text = item.wheatherDescription
            txt_currenty_temperature.text = item.currentTemperature
            txt_min_temperature.text = item.minTemperature
            txt_max_temperature.text = item.maxTemperature
            Picasso.get().load("http://openweathermap.org/img/w/${item.iconId}.png").into(img_wheather)
        }

        return v

    }

}