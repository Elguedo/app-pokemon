package com.example.myapplication;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class PokemonApiUtil {

    private static final String BASE_URL = "https://pokeapi.co/api/v2/";
    private OkHttpClient client;
    private Gson gson;

    public PokemonApiUtil() {
        client = new OkHttpClient();
        gson = new Gson();
    }

    public Pokemon getPokemon(String name) throws IOException {
        String url = BASE_URL + "pokemon/" + name.toLowerCase();

        Request request = new Request.Builder()
                .url(url)
                .build();

        Response response = client.newCall(request).execute();
        String json = response.body().string();

        JsonObject jsonObject = gson.fromJson(json, JsonObject.class);
        String pokemonName = jsonObject.get("name").getAsString();
        JsonObject spritesObject = jsonObject.getAsJsonObject("sprites");
        String imageUrl = spritesObject.get("front_default").getAsString();

        return new Pokemon(pokemonName, imageUrl);
    }


    public List<Pokemon> getAllPokemon() throws IOException {
        String url = BASE_URL + "pokemon?limit=151"; // Cambia el límite según tus necesidades

        Request request = new Request.Builder()
                .url(url)
                .build();

        Response response = client.newCall(request).execute();
        String json = response.body().string();

        JsonObject jsonObject = gson.fromJson(json, JsonObject.class);
        JsonArray results = jsonObject.getAsJsonArray("results");

        List<Pokemon> pokemonList = new ArrayList<>();

        for (JsonElement jsonElement : results) {
            JsonObject pokemonObject = jsonElement.getAsJsonObject();
            String name = pokemonObject.get("name").getAsString();
            String pokemonUrl = pokemonObject.get("url").getAsString();
            String imageUrl = getPokemonImageUrl(pokemonUrl);

            Pokemon pokemon = new Pokemon(name, imageUrl);
            pokemonList.add(pokemon);
        }

        return pokemonList;
    }

    private String getPokemonImageUrl(String pokemonUrl) throws IOException {
        Request request = new Request.Builder()
                .url(pokemonUrl)
                .build();

        Response response = client.newCall(request).execute();
        String json = response.body().string();

        JsonObject jsonObject = gson.fromJson(json, JsonObject.class);
        JsonObject spritesObject = jsonObject.getAsJsonObject("sprites");
        String imageUrl = spritesObject.get("front_default").getAsString();

        return imageUrl;
    }
}
