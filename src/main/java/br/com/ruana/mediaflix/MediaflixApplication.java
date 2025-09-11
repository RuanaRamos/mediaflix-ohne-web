package br.com.ruana.mediaflix;

import br.com.ruana.mediaflix.model.DadosEpsodios;
import br.com.ruana.mediaflix.model.DadosSerie;
import br.com.ruana.mediaflix.model.DadosTemporadas;
import br.com.ruana.mediaflix.service.ConsumoAPI;
import br.com.ruana.mediaflix.service.ConverteDados;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class MediaflixApplication implements CommandLineRunner {


	public static void main(String[] args) {
		SpringApplication.run(MediaflixApplication.class, args);

	}

	@Override
	public void run(String... args) throws Exception {
		var consumoAPI = new ConsumoAPI();
		var json = consumoAPI.obterDados("https://www.omdbapi.com/?t=gilmore+girls&apikey=344cd69c");
		System.out.println(json);
		ConverteDados conversor = new ConverteDados();
		DadosSerie dados = conversor.obterDados(json, DadosSerie.class);
		System.out.println(dados);
		json = consumoAPI.obterDados("https://www.omdbapi.com/?t=gilmore+girls&season=1&episode=2&apikey=344cd69c");
		DadosEpsodios dadosEpsodios = conversor.obterDados(json,DadosEpsodios.class);
		System.out.println(dadosEpsodios);

		List<DadosTemporadas> temporadas = new ArrayList<>();

		for(int i = 1; i<=dados.totalTemporadas(); i++){
			json = consumoAPI.obterDados("https://www.omdbapi.com/?t=gilmore+girls&season" + i + "&apikey=344cd69c");
			DadosTemporadas dadosTemporadas = conversor.obterDados(json, DadosTemporadas.class);
			temporadas.add(dadosTemporadas);
		}

	}
}
