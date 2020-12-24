package com.example.tranlator;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    Retrofit retrofit = new Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create()) // ответ от сервера в виде строки
            .baseUrl(AzureTranslationAPI.API_URL) // адрес API сервера
            .build();

    AzureTranslationAPI api = retrofit.create(AzureTranslationAPI.class);

    Spinner langs;
    String[] str;
    ArrayAdapter adapter;
    String object;
    String Lang;
    EditText write_field;
    TextView out_field;

    class Text{
        String text;
    }

    public void createSpinner() {
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, str);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapter.notifyDataSetChanged();
        langs.setAdapter(adapter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Call<LanguagesResponse> call = api.getLanguages(); // создаём объект-вызов
        call.enqueue(new LanguagesCallback());

        write_field = findViewById(R.id.textForTranslate);
        langs = findViewById(R.id.langs);
        out_field = findViewById(R.id.translateField);

        AdapterView.OnItemSelectedListener itemSelectedListener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                object = (String)parent.getItemAtPosition(position);
                Lang = object.split("-")[0];
                Log.d("language", object + " " + Lang);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(MainActivity.this, "you have not chosen anything", Toast.LENGTH_SHORT).show();
            }
        };
        langs.setOnItemSelectedListener(itemSelectedListener);
    }
    public void onClick(View v){
        String text = String.valueOf(write_field.getText());
        Log.d("mytag", text + "\t" +object);
        Text body = new Text();
        body.text = text;
        Text[] array = {body};
        Call<TranslatedText[]> call = api.translate(Lang, array );
        Log.d("mytag", text);
        call.enqueue(new TranslatedCallback());
    }

    class LanguagesCallback implements Callback<LanguagesResponse> {
        @Override
        public void onResponse(Call<LanguagesResponse> call, Response<LanguagesResponse> response) {
            if (response.isSuccessful()) {
                String languages = response.body().toString();
                str = languages.split(":");
                createSpinner();
                Log.d("mytag", "response: ");
            } else { Log.d("mytag", "response: failed"); }
        }
        @Override
        public void onFailure(Call<LanguagesResponse> call, Throwable t) {
            Toast.makeText(MainActivity.this, "Failed getting the list of languages", Toast.LENGTH_LONG).show();
        }
    }

    class TranslatedCallback implements Callback<TranslatedText[]> {
        @Override
        public void onResponse(Call<TranslatedText[]> call, Response<TranslatedText[]> response) {
            if (response.isSuccessful()) {
                String translatedText = response.body()[0].toString();
                out_field.setText(translatedText);
                Log.d("mytag", "response: " + translatedText);
            } else { Log.d("mytag", "response: failed in Response"); }
        }
        @Override
        public void onFailure(Call<TranslatedText[]> call, Throwable t) {
            Log.d("mytag", "response: failed in failed");
        }
    }
}
