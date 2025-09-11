package br.com.ruana.mediaflix.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record DadosEpsodios(@JsonAlias("Title") String titulo,
                            @JsonAlias("Runtime") String numero,
                            @JsonAlias("imdbRating")String avaliacao,
                            @JsonAlias("Released")String dataDeLanacamento) {

}
