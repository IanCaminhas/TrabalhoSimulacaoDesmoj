package trabalho;

import desmoj.core.dist.ContDistExponential;
import desmoj.core.dist.ContDistUniform;
import desmoj.core.simulator.Experiment;
import desmoj.core.simulator.Model;
import desmoj.core.simulator.Queue;
import desmoj.core.simulator.TimeInstant;
import desmoj.core.simulator.TimeSpan;
import desmoj.core.statistic.Count;
import desmoj.core.statistic.Tally;

public class SiteCompras extends Model {

	private static double tempoSimulacao = 900000;
	private Queue<Processo> filaClientes;
	private Queue<Processo> filaDiscoRapido;
	private Queue<Processo> filaDiscoLento;

	private Disco discoRapido;
	private Disco discoLento;
	private Processador processador;

	private ContDistExponential distribuicaoTempoOciosidade;

	private ContDistUniform distribuicaoTempoServicoProcessador;
	private ContDistUniform distribuicaoTempoDiscoRapido;
	private ContDistExponential distribuicaoTempoDiscoLento;

	private ContDistUniform probabilidadeDeMigracao;

	private Tally tempoEsperaProcessador;
	private Tally tempoEsperaDiscoRapido;
	private Tally tempoEsperaDiscoLento;
	private Tally tempoRespostaServico;

	private Count somaTempoOcupacaoCPU;
	private Count somaTempoOcupacaoDiscoRapido;
	private Count somaTempoOcupacaoDiscoLento;
	private Count numeroRequisicoesCompletadasDiscoLento;
	private Count numeroRequisicoesCompletadasDiscoRapido;
	private Count numeroRequisicoesCompletadasProcessador;
	private Count numeroRequisicoesCompletadas;

	public SiteCompras(Model owner, String name, boolean showInReport, boolean showInTrace) {
		super(owner, name, showInReport, showInTrace);
	}

	@Override
	public String description() {
		return "Site de compras";
	}

	@Override
	public void init() {

		this.discoLento = new Disco(this, "Disco Lento", true);
		this.discoRapido = new Disco(this, "Disco Rapido", true);
		this.processador = new Processador(this, "Processador", true);

		this.probabilidadeDeMigracao = new ContDistUniform(this, "Probabilidade de migracao da requsicao", 0, 100, true,
				true);

		filaClientes = new Queue<Processo>(this, "Fila de clientes aguardando servico", true, true);
		filaDiscoRapido = new Queue<Processo>(this, "Fila de requisicoes aguardando disco rapido", true, true);
		filaDiscoLento = new Queue<Processo>(this, "Fila de requisicoes aguardando disco Lento", true, true);

		this.distribuicaoTempoDiscoLento = new ContDistExponential(this,
				"Distribuicao do tempo de servico do disco lento", 100, true, true);
		this.distribuicaoTempoDiscoRapido = new ContDistUniform(this,
				"Distribuicao do tempo de servico do disco rapido", 10, 140, true, true);
		this.distribuicaoTempoServicoProcessador = new ContDistUniform(this,
				"Distribuicao do tempo de servico do processador", 5, 55, true, true);

		this.distribuicaoTempoDiscoRapido.setNonNegative(true);
		this.distribuicaoTempoDiscoLento.setNonNegative(true);

		this.distribuicaoTempoOciosidade = new ContDistExponential(this, "Distribuicao de ociosidade", 1000, true,
				true);

		this.distribuicaoTempoOciosidade.setNonNegative(true);

		this.tempoEsperaDiscoLento = new Tally(this, "tempo medio de espera na fila do disco lento", true, true);
		this.tempoEsperaDiscoRapido = new Tally(this, "tempo medio de espera na fila do disco rapido", true, true);
		this.tempoEsperaProcessador = new Tally(this, "tempo medio de espera na fila do processador", true, true);

		this.tempoRespostaServico = new Tally(this, "Tempo medio de resposta", true, true);

		this.somaTempoOcupacaoCPU = new Count(this, "Soma tempo de processamento de todas as requisicoes CPU", true,
				true);
		this.somaTempoOcupacaoDiscoRapido = new Count(this,
				"Soma tempo de leitura de todas as visitas pelo Disco Rapido", true, true);
		this.somaTempoOcupacaoDiscoLento = new Count(this, "Soma tempo de leitura de todas as visitas pelo Disco Lento",
				true, true);

		this.numeroRequisicoesCompletadas = new Count(this, "Numero requisicoes completadas", true, true);
		this.numeroRequisicoesCompletadasDiscoLento = new Count(this, "Numero de requisicoes completadas pelo disco lento", true, true);
		this.numeroRequisicoesCompletadasProcessador = new Count(this, "Numero de requisicoes completadas pelo processador", true, true);
		this.numeroRequisicoesCompletadasDiscoRapido = new Count(this, "Numero de requisicoes completadas pelo disco rapido", true, true);

	}
	
	/* Método que vai alocar o processador a uma requisião.
	 * Se o processador estiver ocupado, coloca a requisição na fila.
	 * Se não, processador vai trabalhar 
	 * 
	 */
	
	public void servirProcesso(Processo cliente) {

		if ((processador.isOcupado()) == false) {

			processador.setOcupado(true);
			processador.processar(cliente);
		} else {
			double time = presentTime().getTimeAsDouble();

			cliente.setEntradaFila(time);
			this.filaClientes.insert(cliente);

		}

	}

	@Override
	public void doInitialSchedules() {

		for (int i = 1; i <= 55; i++) {
			EventoGeradorProcesso eventoGeradorProcesso;
			eventoGeradorProcesso = new EventoGeradorProcesso(this,
					"Evento externo responsável por gerar um processo ao pool", true);
			eventoGeradorProcesso.schedule(new TimeSpan(0.0));
		}
	}

	public Count getSomaTempoOcupacaoCPU() {
		return somaTempoOcupacaoCPU;
	}

	public void setSomaTempoOcupacaoCPU(Count somaTempoOcupacaoCPU) {
		this.somaTempoOcupacaoCPU = somaTempoOcupacaoCPU;
	}

	public Count getSomaTempoOcupacaoDiscoRapido() {
		return somaTempoOcupacaoDiscoRapido;
	}

	public void setSomaTempoOcupacaoDiscoRapido(Count somaTempoOcupacaoDiscoRapido) {
		this.somaTempoOcupacaoDiscoRapido = somaTempoOcupacaoDiscoRapido;
	}

	public Count getSomaTempoOcupacaoDiscoLento() {
		return somaTempoOcupacaoDiscoLento;
	}

	public void setSomaTempoOcupacaoDiscoLento(Count somaTempoOcupacaoDiscoLento) {
		this.somaTempoOcupacaoDiscoLento = somaTempoOcupacaoDiscoLento;
	}

	public double getdistribuicaoTempoOciosidade() {
		return this.distribuicaoTempoOciosidade.sample();
	}

	public double getTempoProcessamento() {
		return this.distribuicaoTempoServicoProcessador.sample();
	}

	public double getTempoLeituraDiscoRapido() {
		return this.distribuicaoTempoDiscoRapido.sample();
	}

	public double getTempoLeituraDiscoLento() {
		return this.distribuicaoTempoDiscoLento.sample();
	}

	public double getProbabilidadeDeMigracao() {
		return probabilidadeDeMigracao.sample();
	}

	public void setProbabilidadeDeMigracao(ContDistUniform probabilidadeDeMigracao) {
		this.probabilidadeDeMigracao = probabilidadeDeMigracao;
	}
	
	/*
	 * Método será utilizado para decidir o que fazer com a requisição
	 * quando passar pelo processador de acordo com uma distribuição de
	 * probabilidade. 
	 * 
	 * O metodo faz o seguinte trecho:
	 *  "Um processo, ao ser alocado para tratar uma requisição, vai para o processador do
		site de compras coletivas, iniciar sua execução. Depois de passar pelo
		processador, o processo migra 60% das vezes para o disco rápido; 20% das
		vezes para o disco lento; e 20% das vezes encerra sua execução (termina o
		tratamento da requisição) e retorna para o pool, onde volta a ficar ocioso. Como
		mostrado no modelo acima, o último dispositivo visitado pelo processo, antes de
		ele terminar sua execução, é o processador."
	 */

	public void liberarProcessador(Processo processo, Processador processador) {

		double probabilidadeDeMigracao = this.getProbabilidadeDeMigracao();

		this.encerrarExecucao(processador);
		if (probabilidadeDeMigracao > 60 && probabilidadeDeMigracao <= 80) {
			this.servirDiscoLentoParaLeitura(processo);
		}

		else if (probabilidadeDeMigracao >= 0 && probabilidadeDeMigracao <= 60) {
			this.servirDiscoRapidoParaLeitura(processo);

		}

		else if (probabilidadeDeMigracao > 80 && probabilidadeDeMigracao <= 100) {
			processo.setFimTempoResposta(presentTime().getTimeAsDouble());
			this.numeroRequisicoesCompletadas.update();
			this.tempoRespostaServico.update(processo.getFimTempoResposta() - processo.getInicioTempoResposta());

		}

	}
	
	/*
	 * Método que colocará o disco rápido para atender uma requisição ou colocar 
	 * a requisição na fila quando o dispositivo estiver ocupado
	 * 
	 */
	
	public void servirDiscoRapidoParaLeitura(Processo cliente) {
		cliente.setUsoDisco(true);
		cliente.setEhDiscoRapido(true);
		if (this.discoRapido.isOcupado() == false) {
			this.discoRapido.setOcupado(true);
			this.discoRapido.ler(cliente);

		} else {

			cliente.setEntradaFilaDiscoRapido(presentTime().getTimeAsDouble());
			this.filaDiscoRapido.insert(cliente);

		}
	}
	
	/*
	 * Método que colocará o disco lento para atender uma requisição ou colocar 
	 * a requisição na fila quando o dispositivo estiver ocupado
	 * 
	 */
	

	public void servirDiscoLentoParaLeitura(Processo cliente) {
		cliente.setUsoDisco(true);
		cliente.setEhDiscoRapido(false);

		if (this.discoLento.isOcupado() == false) {
			this.discoLento.setOcupado(true);
			this.discoLento.ler(cliente);

		} else {
			cliente.setEntradaFilaDiscoLento(presentTime().getTimeAsDouble());
			this.filaDiscoLento.insert(cliente);
		}
	}

	
	/*
	 * Metodo que sera usado quando o disco rapido terminar de realizar uma leitura.
	 * O disco rápido ficará desocupado ou pegará outra requisção para ser lida da
	 * sua fila
	 * 
	 */
	public void examinarFilaDiscoRapido(Disco disco) {
		sendTraceNote("Saindo do disco rapido...");
		disco.setOcupado(false);

		if (this.filaDiscoRapido.isEmpty()) {
			sendTraceNote("Disco rápido esperando ser lido...");

		} else {

			sendTraceNote("Disco rápido  será realocado.");

			Processo processo = this.filaDiscoRapido.first();

			this.filaDiscoRapido.remove(processo);
			processo.setSaidaFilaDiscoRapido(presentTime().getTimeAsDouble());
			this.servirDiscoRapidoParaLeitura(processo);
		}

	}

	/*
	 * Método que será usado quando o disco lento 
	 * terminar de realizar uma leitura. O disco lento
	 * ficará desocupado ou pegará outra requisção para ser lida da
	 * sua fila
	 * 
	 * 
	 */
	public void examinarFilaDiscoLento(Disco disco) {
		sendTraceNote("Saindo do disco Lento...");
		disco.setOcupado(false);

		if (this.filaDiscoLento.isEmpty()) {
			sendTraceNote("Disco rapido esperando ser lido...");

		} else {

			sendTraceNote("Disco rápido sera realocado.");

			Processo processo = this.filaDiscoLento.first();

			this.filaDiscoLento.remove(processo);
			processo.setSaidaFilaDiscoLento(presentTime().getTimeAsDouble());

			this.servirDiscoLentoParaLeitura(processo);
		}

	}

	/*
	 * Método que será usado quando o processador terminar de realizar o processamento
	 * de uma requisição. 
	 * O processador vai pegar outra requisção caso ou ficar desocupado caso 
	 * a fila estiver vazia
	 */

	private void encerrarExecucao(Processador processador) {

		processador.setOcupado(false);

		sendTraceNote("Saindo do pool...");
		if (filaClientes.isEmpty()) {
			sendTraceNote("Processador esperando requisições...");

		} else {

			sendTraceNote("Processador será realocado.");
			Processo processo = filaClientes.first();

			filaClientes.remove(processo);

			processo.setSaidaFila(presentTime().getTimeAsDouble());

			this.servirProcesso(processo);
		}

	}

	public Disco getDiscoLento() {
		return discoLento;
	}

	public Disco getDiscoRapido() {
		return discoRapido;
	}

	public Tally getTempoEsperaProcessador() {
		return tempoEsperaProcessador;
	}

	public void setTempoEsperaProcessador(Tally tempoEsperaProcessador) {
		this.tempoEsperaProcessador = tempoEsperaProcessador;
	}

	public Tally getTempoEsperaDiscoRapido() {
		return tempoEsperaDiscoRapido;
	}

	public void setTempoEsperaDiscoRapido(Tally tempoEsperaDiscoRapido) {
		this.tempoEsperaDiscoRapido = tempoEsperaDiscoRapido;
	}

	public Tally getTempoEsperaDiscoLento() {
		return tempoEsperaDiscoLento;
	}

	public void setTempoEsperaDiscoLento(Tally tempoEsperaDiscoLento) {
		this.tempoEsperaDiscoLento = tempoEsperaDiscoLento;
	}

	public Count getNumeroRequisicoesCompletadasDiscoLento() {
		return numeroRequisicoesCompletadasDiscoLento;
	}

	public void setNumeroRequisicoesCompletadasVisitasDiscoLento(Count numeroRequisicoesCompletadasDiscoLento) {
		this.numeroRequisicoesCompletadasDiscoLento = numeroRequisicoesCompletadasDiscoLento;
	}

	public Count getNumeroRequisicoesCompletadasDiscoRapido() {
		return numeroRequisicoesCompletadasDiscoRapido;
	}

	public void setNumeroRequisicoesCompletadasDiscoRapido(Count numeroRequisicoesCompletadasDiscoRapido) {
		this.numeroRequisicoesCompletadasDiscoRapido = numeroRequisicoesCompletadasDiscoRapido;
	}

	public Count getNumeroRequisicoesCompletadasProcessador() {
		return numeroRequisicoesCompletadasProcessador;
	}

	public void setNumeroRequisicoesCompletadasProcessador(Count numeroRequisicoesCompletadasProcessador) {
		this.numeroRequisicoesCompletadasProcessador = numeroRequisicoesCompletadasProcessador;
	}

	public Count getNumeroRequisicoesCompletadas() {
		return numeroRequisicoesCompletadas;
	}

	public void setNumeroRequisicoesCompletadas(Count numeroRequisicoesCompletadas) {
		this.numeroRequisicoesCompletadas = numeroRequisicoesCompletadas;
	}

	public ContDistUniform getDistribuicaoTempoServicoProcessador() {
		return distribuicaoTempoServicoProcessador;
	}

	public void setDistribuicaoTempoServicoProcessador(ContDistUniform distribuicaoTempoServicoProcessador) {
		this.distribuicaoTempoServicoProcessador = distribuicaoTempoServicoProcessador;
	}

	public static void main(String[] args) {
		SiteCompras modeloSiteDeCompras;
		Experiment experimento;

		modeloSiteDeCompras = new SiteCompras(null, "Modelo Site de compras", true, true);

		experimento = new Experiment("Experimento do site de compras coletivas");

		modeloSiteDeCompras.connectToExperiment(experimento);

		experimento.setShowProgressBar(true);

		experimento.stop(new TimeInstant(tempoSimulacao));

		experimento.tracePeriod(new TimeInstant(0.0), new TimeInstant(tempoSimulacao));

		experimento.start();

		experimento.report();

		experimento.finish();

	}

}
