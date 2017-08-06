package com.shavkunov.razvitie.samo.entity;

import android.os.AsyncTask;
import android.util.Log;

import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PatterLab {

    private static final String TAG = "PatterLab";

    private static PatterLab patterLab;
    private List<Patter> list;

    public static PatterLab getInstance() {
        if (patterLab == null) {
            patterLab = new PatterLab();
        }

        return patterLab;
    }

    private PatterLab() {
        list = new ArrayList<>();
        new PatterTask().execute();
    }

    public List<Patter> getList() {
        return list;
    }

    private class PatterTask extends AsyncTask<Void, Void, Patter[]> {

        @Override
        protected Patter[] doInBackground(Void... params) {
            try {
                final String url = "https://speechapp-service.herokuapp.com/get";
                RestTemplate restTemplate = new RestTemplate();
                restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
                ResponseEntity<Patter[]> responseEntity = restTemplate.getForEntity(url, Patter[].class);
                return responseEntity.getBody();
            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
            }

            return null;
        }

        @Override
        protected void onPostExecute(Patter[] p) {
            if (p != null) {
                Collections.addAll(list, p);
            }
        }
    }
}
