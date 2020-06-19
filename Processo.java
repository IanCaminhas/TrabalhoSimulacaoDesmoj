package trabalho;

import desmoj.core.simulator.Entity;
import desmoj.core.simulator.Model;
import desmoj.core.simulator.TimeInstant;

public class Processo extends Entity {

	private double fimTempoResposta;
	private double inicioTempoResposta;

	private double entradaFilaDiscoRapido;
	private double saidaFilaDiscoRapido;

	private double entradaFilaDiscoLento;
	private double saidaFilaDiscoLento;

	private double tempoEntradaProcessador;
	private double tempoSaidaProcessador;

	private double tempoEntradaDisco;
	private double tempoSaidaDisco;

	private double entradaFila;
	private double saidaFila;
	private boolean ehDiscoRapido;
	private boolean usoDisco;

	public Processo(Model owner, String name, boolean showInTrace) {
		super(owner, name, showInTrace);

		TimeInstant tempoCorrente;
		tempoCorrente = owner.presentTime();
		this.inicioTempoResposta = tempoCorrente.getTimeAsDouble();
		this.usoDisco = false;

	}

	public double getInicioTempoResposta() {
		return inicioTempoResposta;
	}

	public double getEntradaFila() {
		return entradaFila;
	}

	public void setEntradaFila(double entradaFila) {
		this.entradaFila = entradaFila;
	}

	public double getSaidaFila() {
		return saidaFila;
	}

	public void setSaidaFila(double saidaFila) {
		this.saidaFila = saidaFila;
	}

	public double getFimTempoResposta() {
		return fimTempoResposta;
	}

	public void setFimTempoResposta(double fimTempoResposta) {
		this.fimTempoResposta = fimTempoResposta;
	}

	public boolean isEhDiscoRapido() {
		return ehDiscoRapido;
	}

	public void setEhDiscoRapido(boolean ehDiscoRapido) {
		this.ehDiscoRapido = ehDiscoRapido;
	}

	public boolean isUsoDisco() {
		return usoDisco;
	}

	public void setUsoDisco(boolean usoDisco) {
		this.usoDisco = usoDisco;
	}

	public double getEntradaFilaDiscoRapido() {
		return entradaFilaDiscoRapido;
	}

	public void setEntradaFilaDiscoRapido(double entradaFilaDiscoRapido) {
		this.entradaFilaDiscoRapido = entradaFilaDiscoRapido;
	}

	public double getSaidaFilaDiscoRapido() {
		return saidaFilaDiscoRapido;
	}

	public void setSaidaFilaDiscoRapido(double saidaFilaDiscoRapido) {
		this.saidaFilaDiscoRapido = saidaFilaDiscoRapido;
	}

	public double getEntradaFilaDiscoLento() {
		return entradaFilaDiscoLento;
	}

	public void setEntradaFilaDiscoLento(double entradaFilaDiscoLento) {
		this.entradaFilaDiscoLento = entradaFilaDiscoLento;
	}

	public double getSaidaFilaDiscoLento() {
		return saidaFilaDiscoLento;
	}

	public void setSaidaFilaDiscoLento(double saidaFilaDiscoLento) {
		this.saidaFilaDiscoLento = saidaFilaDiscoLento;
	}

	public void setInicioTempoResposta(double inicioTempoResposta) {
		this.inicioTempoResposta = inicioTempoResposta;
	}

	public double getTempoEntradaProcessador() {
		return tempoEntradaProcessador;
	}

	public void setTempoEntradaProcessador(double tempoEntradaProcessador) {
		this.tempoEntradaProcessador = tempoEntradaProcessador;
	}

	public double getTempoSaidaProcessador() {
		return tempoSaidaProcessador;
	}

	public void setTempoSaidaProcessador(double tempoSaidaProcessador) {
		this.tempoSaidaProcessador = tempoSaidaProcessador;
	}

	public double getTempoEntradaDisco() {
		return tempoEntradaDisco;
	}

	public void setTempoEntradaDisco(double tempoEntradaDisco) {
		this.tempoEntradaDisco = tempoEntradaDisco;
	}

	public double getTempoSaidaDisco() {
		return tempoSaidaDisco;
	}

	public void setTempoSaidaDisco(double tempoSaidaDisco) {
		this.tempoSaidaDisco = tempoSaidaDisco;
	}

}
