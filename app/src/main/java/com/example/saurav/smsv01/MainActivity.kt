package com.example.saurav.smsv01

import android.content.Intent
import android.os.AsyncTask
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.util.regex.Pattern

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //btsig.setOnClickListener{
           // var next=Intent(this,Login::class.java)
            //startActivity(next)
      //  }
    }
    fun signup(view: View){
//        var name=t1.text.toString()
//        var lastname=t2.text.toString()
        var name = etfirstname.text.toString()
        var lastname = etlastname.text.toString()
        var mob = etmob.text.toString()
        var moblen:Int=mob.length
        var email = etemail.text.toString()
        var pass1 = etpassword.text.toString()
        var pass2 = etconpassword.text.toString()
        var username=name+lastname.toString()
        var flag:Boolean=false

        if (name == ""||lastname==""||mob==""||email==""||pass1==""||pass2==""){
            Toast.makeText(this,"Enter Your Data !",Toast.LENGTH_SHORT).show()
        }else if(isEmailValid(email)==false){
            Toast.makeText(this,"Invalid Email !",Toast.LENGTH_SHORT).show()
        }else if(moblen!=10){
            Toast.makeText(this,"Enter Valid Moblile Number !",Toast.LENGTH_SHORT).show()
        }else if(pass1!=pass2){
            Toast.makeText(this,"Match the Password !",Toast.LENGTH_SHORT).show()
        }else{
            flag=true
        }
        if(flag==true){
            var add="https://unribbed-headers.000webhostapp.com/send.php?FNAME="+name+"&LNAME="+lastname+"&USERNAME="+username+"klsajdf&MOBILE_NO="+mob+"&MAIL="+email+"&PASSWORD="+pass1
           // var add="https://unribbed-headers.000webhostapp.com/test.php?user="+ name +"&pass=" +lastname
            MyAsyncTask().execute(add)
        }


    }
    // CALL HTTP
    inner class MyAsyncTask: AsyncTask<String, String, String>() {

        override fun onPreExecute() {
            //Before task started
        }
        override fun doInBackground(vararg p0: String?): String {
            try {

                val url=URL(p0[0])

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
                var json=JSONObject(values[0])
                Toast.makeText(applicationContext,json.getString("msg"),Toast.LENGTH_LONG).show()

                if (json.getString("msg")== "Sucessfully Registred"){

                }else{
                    btnlogin.isEnabled=true
                }

            }catch (ex:Exception){

            }
        }

        override fun onPostExecute(result: String?) {

            //after task done
        }


    }
    fun ConvertStreamToString(inputStream:InputStream):String{

        val bufferReader=BufferedReader(InputStreamReader(inputStream))
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
