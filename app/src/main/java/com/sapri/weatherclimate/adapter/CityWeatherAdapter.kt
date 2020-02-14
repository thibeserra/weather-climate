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

        val v: View = convertView ?: LayoutInflater.from(context).inflate(R.layout.list_view_item, parent, false)

        val item = getItem(position)

        val txtCity = v.findViewById<TextView>(R.id.txt_city)
        val txtWeatherDescription = v.findViewById<TextView>(R.id.txt_wheather_description)
        val txtCurrentyTemperature = v.findViewById<TextView>(R.id.txt_currenty_temperature)
        val txtMinTemperature = v.findViewById<TextView>(R.id.txt_min_temperature)
        val txtMaxTemperature = v.findViewById<TextView>(R.id.txt_max_temperature)
        val imgWeather = v.findViewById<ImageView>(R.id.img_wheather)

        item?.let {
            txtCity.text = item.city
            txtWeatherDescription.text = item.wheatherDescription
            txtCurrentyTemperature.text = item.currentTemperature
            txtMinTemperature.text = item.minTemperature
            txtMaxTemperature.text = item.maxTemperature
            Picasso.get().load("http://openweathermap.org/img/w/${item.iconId}.png").into(imgWeather)
        }

        return v

    }

}