package br.com.ruana.mediaflix.principal;

import br.com.ruana.mediaflix.model.DadosEpisodios;
import br.com.ruana.mediaflix.model.DadosSerie;
import br.com.ruana.mediaflix.model.DadosTemporada;
import br.com.ruana.mediaflix.model.Episodio;
import br.com.ruana.mediaflix.service.ConsumoAPI;
import br.com.ruana.mediaflix.service.ConverteDados;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class Principal {

    private Scanner leitura = new Scanner(System.in);
    private ConsumoAPI consumo = new ConsumoAPI();
    private ConverteDados conversor = new ConverteDados();

    private final String ENDERECO = "https://www.omdbapi.com/?t=";
    private final String API_KEY  = "&apikey=344cd69c";

    public void exibirMenu() {

        System.out.println("Geben Sie den Namen der Serie für die Suche ein");
        var nomeSerie = leitura.nextLine();

        // Busca dados da série
        var json = consumo.obterDados(ENDERECO + nomeSerie.replace(" ", "+") + API_KEY);
        DadosSerie dados = conversor.obterDados(json, DadosSerie.class);

        // Busca todas as temporadas
        List<DadosTemporada> temporadas = new ArrayList<>();
        for (int i = 1; i <= dados.totalTemporadas(); i++) {
            json = consumo.obterDados(ENDERECO + nomeSerie.replace(" ", "+") + "&season=" + i + API_KEY);
            DadosTemporada dadosTemporada = conversor.obterDados(json, DadosTemporada.class);
            temporadas.add(dadosTemporada);
        }

        // Achata em uma lista de DadosEpisodios
        List<DadosEpisodios> dadosEpisodios = temporadas.stream()
                .flatMap(t -> t.episodios().stream())
                .collect(Collectors.toList());

        // --- TOP 10 EPISÓDIOS (ordenação mantendo sua lógica por String) ---
        System.out.println("\nTop 10 episodios");
        dadosEpisodios.stream()
                .filter(e -> !e.avaliacao().equalsIgnoreCase("N/A"))
                .sorted(Comparator.comparing(DadosEpisodios::avaliacao).reversed())
                .limit(10)
                .forEach(e -> System.out.println(e.titulo() + " (" + e.avaliacao() + ")"));

        // Converte para entidade Episodio (para usar datas no filtro por ano)
        List<Episodio> episodios = temporadas.stream()
                .flatMap(t -> t.episodios().stream().map(d -> new Episodio(t.numero(), d)))
                .collect(Collectors.toList());

        // --- FILTRO POR ANO ---
        System.out.println("\nAb welchem Jahr möchten Sie schauen?");
        var ano = leitura.nextInt();
        leitura.nextLine();

        LocalDate dataBusca = LocalDate.of(ano, 1, 1);
        DateTimeFormatter formatador = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        episodios.stream()
                .filter(e -> e.getDataDeLanacamento() != null && e.getDataDeLanacamento().isAfter(dataBusca))
                .forEach(e -> System.out.println(
                        "Temporada: " + e.getTemporadas()
                                + " | Episodio: " + e.getTitulo()
                                + " | Data de lancamento: " + e.getDataDeLanacamento().format(formatador)
                ));
    }
}
