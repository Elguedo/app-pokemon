package com.example.myapplication;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.squareup.picasso.Picasso;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private ImageView ivPokemon;
    private TextView tvName;
    private TextView tvHeight;
    private TextView tvWeight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ivPokemon = findViewById(R.id.ivPokemon);
        tvName = findViewById(R.id.tvName);
        tvHeight = findViewById(R.id.tvHeight);
        tvWeight = findViewById(R.id.tvWeight);

        new GetPokemonTask().execute("pikachu");
    }

    private class GetPokemonTask extends AsyncTask<String, Void, Pokemon> {
        @Override
        protected Pokemon doInBackground(String... names) {
            String name = names[0];
            Pokemon pokemon = null;

            try {
                PokemonApiUtil apiUtil = new PokemonApiUtil();
                pokemon = apiUtil.getPokemon(name);
            } catch (IOException e) {
                Log.e(TAG, "Error al obtener los datos del Pokémon", e);
            }

            return pokemon;
        }

        @Override
        protected void onPostExecute(Pokemon pokemon) {
            if (pokemon != null) {
                tvName.setText(pokemon.getName());
                tvHeight.setText("Height: " + pokemon.getHeight());
                tvWeight.setText("Weight: " + pokemon.getWeight());

                // Cargar la imagen utilizando Picasso
                Picasso.get().load(pokemon.getImageUrl()).into(ivPokemon);
            }
        }
    }
}



