/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mx.itson.macotascliente.services;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.List;
import mx.itson.macotascliente.entities.Mascota;

public class MascotaApiClient {

    private static final String BASE_URL = "http://127.0.0.1:8001/api/mascotas/";
    private final Gson gson = new Gson();

    private String enviar(String urlStr, String metodo, String cuerpo) throws IOException {
        HttpURLConnection con = (HttpURLConnection) new URL(urlStr).openConnection();
        con.setRequestMethod(metodo);
        con.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
        con.setRequestProperty("Accept", "application/json");

        if (cuerpo != null) {
            con.setDoOutput(true);
            try (OutputStream os = con.getOutputStream()) {
                os.write(cuerpo.getBytes(StandardCharsets.UTF_8));
            }
        }

        int codigo = con.getResponseCode();
        InputStream is = (codigo >= 200 && codigo < 300)
                ? con.getInputStream() : con.getErrorStream();

        StringBuilder sb = new StringBuilder();
        if (is != null) {
            try (BufferedReader br = new BufferedReader(
                    new InputStreamReader(is, StandardCharsets.UTF_8))) {
                String linea;
                while ((linea = br.readLine()) != null) {
                    sb.append(linea);
                }
            }
        }

        if (codigo < 200 || codigo >= 300) {
            throw new IOException("Error " + codigo + ": " + sb);
        }
        return sb.toString();
    }

    private String aJsonSinId(Mascota m) {
        JsonObject json = gson.toJsonTree(m).getAsJsonObject();
        json.remove("id");
        return json.toString();
    }

    public List<Mascota> listar() throws IOException {
        String respuesta = enviar(BASE_URL, "GET", null);
        Type tipoLista = new TypeToken<List<Mascota>>() {}.getType();
        return gson.fromJson(respuesta, tipoLista);
    }

    public Mascota crear(Mascota m) throws IOException {
        String respuesta = enviar(BASE_URL, "POST", aJsonSinId(m));
        return gson.fromJson(respuesta, Mascota.class);
    }

    public Mascota actualizar(Mascota m) throws IOException {
        String respuesta = enviar(BASE_URL + m.getId() + "/", "PUT", aJsonSinId(m));
        return gson.fromJson(respuesta, Mascota.class);
    }

    public void eliminar(int id) throws IOException {
        enviar(BASE_URL + id + "/", "DELETE", null);
    }
}