package com.tomerrosenfeld.lost.demo

import android.content.Context
import android.graphics.Bitmap
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.TextView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import com.parse.*
import kotlinx.android.synthetic.main.lost_form.*
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.losts_list.*


/**
 * Created by tomer on 11/5/17.
 */
class FoundFormActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Parse.initialize(this)
        ParseInstallation.getCurrentInstallation().saveInBackground()
        setContentView(R.layout.losts_list)
        val query = ParseQuery.getQuery<ParseObject>("People")
        query.findInBackground { list, e ->
            if (e == null) {
                val people: ArrayList<Person> = ArrayList()
                list.mapTo(people) {
                    Person(it.get("name").toString(),
                            it.get("last_known_location").toString(),
                            it.get("phone")?.toString() ?: "",
                            it.get("time")?.toString() ?: "",
                            it.getParseFile("image").url)
                }
                val adapter = PeopleAdapter(this, people)
                listview.adapter = adapter
            } else {
            }
        }
    }

    data class Person(val name: String, val lkl: String, val phone: String, val time: String, val image: String)

    inner class PeopleAdapter(context: Context, private var people: ArrayList<Person>) : ArrayAdapter<Person>(context, 0, people) {

        override fun getView(position: Int, v: View?, parent: ViewGroup): View {
            var convertView = v

            val person = getItem(position)

            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(R.layout.person_item, parent, false)
            }
            val tvName: TextView = convertView!!.findViewById(R.id.name_item)
            val tvLKL: TextView = convertView.findViewById(R.id.lkl_item)
            val tvPHONE: TextView = convertView.findViewById(R.id.phone_item)
            val profile: ImageView = convertView.findViewById(R.id.profile)
            tvName.text = "Name: ${person.name}"
            tvLKL.text = "Last seen: ${person.lkl}, at ${person.time}"
            tvPHONE.text = person.phone
            Picasso.with(applicationContext).load(person.image).into(profile)
            return convertView
        }
    }

}