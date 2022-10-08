package com.example.memeshare

import android.content.Intent
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.View.*
import android.widget.ProgressBar
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target

class MainActivity : AppCompatActivity() {

    private var currentImageUrl: String?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //calling loadMeme() function
        loadMeme()
    }
    private fun loadMeme(){
        val prog:ProgressBar=findViewById(R.id.progressBar)
        prog.visibility= VISIBLE
        //used before singleton
        //val queue = Volley.newRequestQueue(this)
        val url = "https://meme-api.herokuapp.com/gimme"

        // Request a string response from the provided URL.
        val jsonObjectRequest = JsonObjectRequest(Request.Method.GET, url,null,
            { response -> currentImageUrl = response.getString("url")
            Glide.with(this).load(currentImageUrl).listener(object: RequestListener<Drawable>{
                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: Target<Drawable>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    prog.visibility= GONE
                    return false
                }

                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    //if Target is in yellow then it is different than reqd. delete and replace
                    target: Target<Drawable>?,
                    isFirstResource: Boolean
                ): Boolean {
                    prog.visibility= GONE
                    return false
                }
            }).into(findViewById(R.id.MemeImage))
            },
            {
                Toast.makeText(this,"Something went wrong!",Toast.LENGTH_LONG).show()
            })

        // Add the request to the RequestQueue.
    //used before singleton
    //queue.add(jsonObjectRequest)
        MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest)
    }

    //never remove view:View then the app will close quickly
    fun shareMeme(view: View) {
        //explicit intent
        val intent= Intent(Intent.ACTION_SEND)
        intent.type="text/plain"
        intent.putExtra(Intent.EXTRA_TEXT,"Hey, checkout this cool meme from Reddit! \n $currentImageUrl")
        val chooser= Intent.createChooser(intent,"Share using:")
        startActivity(chooser)
    }
    fun nextMeme(view: View) {
        loadMeme()
    }
}