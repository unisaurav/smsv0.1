package com.example.saurav.smsv01

import android.content.Intent
import android.os.AsyncTask
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main_login.*
import org.json.JSONArray
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.util.regex.Pattern

class main_login : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_login)
        tosignup.setOnClickListener {
            var gosign=Intent(this,MainActivity::class.java)
            startActivity(gosign)
        }
    }
    fun login(view: View){
//        var name=t1.text.toString()
//        var lastname=t2.text.toString()
        var username= etusername.text.toString()
        var password= etpassword.text.toString()
        var flag:Boolean=false
        var add="https://unribbed-headers.000webhostapp.com/login.php?FNAME="+username+"&PASSWORD="+password
        print("1st step")
        // var add="https://unribbed-headers.000webhostapp.com/test.php?user="+ name +"&pass=" +lastname
        MyAsyncTask().execute(add)
    }


// CALL HTTP
inner class MyAsyncTask: AsyncTask<String, String, String>() {

    override fun onPreExecute() {

        //Before task started
    }
    override fun doInBackground(vararg p0: String?): String {
        try {

            val url= URL(p0[0])

            val urlConnect=url.openConnection() as HttpURLConnection
            urlConnect.connectTimeout=7000


            var inString= ConvertStreamToString(urlConnect.inputStream)
            //Cannot access to ui
            publishProgress(inString)
        }catch (ex:Exception){}


        return " "

    }

    override fun onProgressUpdate(vararg values: String?) {
        try{
            var json= JSONObject(values[0])
            Toast.makeText(applicationContext,json.getString("msg"), Toast.LENGTH_LONG).show()

            if (json.getString("msg")== "pass login"){

                var userinfo= JSONArray(json.getString("info"))
                var userCredentials=userinfo.getJSONObject(0)
                var name=userCredentials.getString("TID")


                Toast.makeText(applicationContext,userCredentials.getString("MAIL"), Toast.LENGTH_LONG).show()


            }else{
                    btnlogin.isEnabled=true
                }

        }catch (ex:Exception){}
    }

    override fun onPostExecute(result: String?) {

        //after task done
    }


}
fun ConvertStreamToString(inputStream: InputStream):String{

    val bufferReader= BufferedReader(InputStreamReader(inputStream))
    var line:String
    var AllString:String=""

    try {
        do{
            line=bufferReader.readLine()
            if(line!=null){
                AllString+=line
            }
        }while (line!=null)
        inputStream.close()
    }catch (ex:Exception){}



    return AllString
}
fun isEmailValid(email: String): Boolean {
    val expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$"
    val pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE)
    val matcher = pattern.matcher(email)
    return matcher.matches()
}
}
