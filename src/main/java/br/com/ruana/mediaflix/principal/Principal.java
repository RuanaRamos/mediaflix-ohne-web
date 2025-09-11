package br.com.ruana.mediaflix.principal;

import br.com.ruana.mediaflix.model.DadosSerie;
import br.com.ruana.mediaflix.model.DadosTemporada;
import br.com.ruana.mediaflix.service.ConsumoAPI;
import br.com.ruana.mediaflix.service.ConverteDados;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Principal {

    private Scanner leitura = new Scanner(System.in);
    private ConsumoAPI consumo = new ConsumoAPI();

    private ConverteDados conversor = new ConverteDados();

    private final String ENDERECO ="https://www.omdbapi.com/?t=";
    private final String API_KEY = "&apikey=344cd69c";

    public void exibirMenu(){

        System.out.println("Digite o nome da Serie para a busca");
        var nomeSerie = leitura.nextLine();
        var json = consumo.obterDados( ENDERECO + nomeSerie.replace(" ", "+") + API_KEY );
        DadosSerie dados = conversor.obterDados(json, DadosSerie.class);
        System.out.println(dados);

        List<DadosTemporada> temporadas = new ArrayList<>();

        for(int i = 1; i<=dados.totalTemporadas(); i++) {
            json = consumo.obterDados (ENDERECO + nomeSerie.replace(" ", "+") + "&season=" + i + API_KEY );
            DadosTemporada dadosTemporada = conversor.obterDados(json, DadosTemporada.class);
            temporadas.add(dadosTemporada);

        }
        temporadas.forEach(System.out::println);
//
        temporadas.forEach(t -> t.episodios().forEach(e -> System.out.println(e.titulo())));


    }
}
