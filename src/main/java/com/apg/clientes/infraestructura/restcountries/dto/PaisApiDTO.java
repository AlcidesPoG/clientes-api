package com.apg.clientes.infraestructura.restcountries.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PaisApiDTO {

    private final Demonyms demonyms;

    @JsonCreator
    public PaisApiDTO(@JsonProperty("demonyms") Demonyms demonyms) {
        this.demonyms = demonyms;
    }

    public Demonyms getDemonyms() {
        return demonyms;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Demonyms {

        private final Language eng;

        @JsonCreator
        public Demonyms(@JsonProperty("eng") Language eng) {
            this.eng = eng;
        }

        public Language getEng() {
            return eng;
        }

    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Language {

        private final String f;
        private final String m;

        @JsonCreator
        public Language(@JsonProperty("f") String f, @JsonProperty("m") String m) {
            this.f = f;
            this.m = m;
        }

        public String getF() {
            return f;
        }

        public String getM() {
            return m;
        }

    }
}