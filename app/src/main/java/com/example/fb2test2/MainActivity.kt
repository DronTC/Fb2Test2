package com.example.fb2test2

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import org.xml.sax.InputSource
import java.io.File
import java.io.FileInputStream
import java.io.StringReader
import javax.xml.parsers.DocumentBuilderFactory


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val filesDir = File("/storage/emulated/0/Download")
        val fb2File = File(findFb2File(filesDir))

        if(fb2File != null){
            val inputStream = FileInputStream(fb2File)

            val factory = DocumentBuilderFactory.newInstance()
            val builder = factory.newDocumentBuilder()
            val document = builder.parse(InputSource(inputStream))

            val title = document.getElementsByTagName("book-title").item(0).textContent
            val author = document.getElementsByTagName("author").item(0).textContent
            val base64Cover = document.getElementsByTagName("binary").item(0).textContent

            val imageBytes = Base64.decode(base64Cover, Base64.DEFAULT)
            val cover = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)

            val titleTextView = findViewById<TextView>(R.id.titleTextView)
            titleTextView.text = title
            val authorTextView = findViewById<TextView>(R.id.authorTextView)
            authorTextView.text = author
            val coverImageView = findViewById<ImageView>(R.id.coverImageView)
            coverImageView.setImageBitmap(cover)

            inputStream.close()
        }
    }

    private fun findFb2File(directory: File): String {

        val files = directory.listFiles { dir, name ->
            name.endsWith(".fb2")
        }

        val fb2File = files[0]
        return fb2File.absolutePath
    }
}