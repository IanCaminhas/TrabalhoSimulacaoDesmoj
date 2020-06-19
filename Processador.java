package trabalho;

import desmoj.core.simulator.Entity;

import desmoj.core.simulator.Model;
import desmoj.core.simulator.TimeSpan;

public class Processador extends Entity {

	private boolean ocupado;
	private Processo cliente;

	public Processador(Model owner, String name, boolean showInTrace) {
		super(owner, name, showInTrace);
		this.ocupado = false;

	}

	public boolean isOcupado() {
		return ocupado;
	}

	public void setOcupado(boolean ocupado) {
		this.ocupado = ocupado;
	}

	public Processo getCliente() {
		return cliente;
	}

	public void setCliente(Processo cliente) {
		this.cliente = cliente;
	}

	public void processar(Processo cliente) {
		SiteCompras modeloSiteCompras;
		double tempoProcessamento;
		EventoTerminoProcessamento eventoTerminoProcessamento;

		this.setCliente(cliente);
		modeloSiteCompras = (SiteCompras) getModel();

		cliente.setTempoEntradaProcessador(presentTime().getTimeAsDouble());
		
		
		double sub = cliente.getSaidaFila() - cliente.getEntradaFila();
		modeloSiteCompras.getTempoEsperaProcessador().update(sub);

		tempoProcessamento = modeloSiteCompras.getTempoProcessamento();
		modeloSiteCompras.sendTraceNote(this + " serve " + cliente + " por " + tempoProcessamento + " minutos.");
		eventoTerminoProcessamento = new EventoTerminoProcessamento(modeloSiteCompras,
				"Evento relacionado ao término de processamento pela CPU", true);

		eventoTerminoProcessamento.schedule(this, cliente, new TimeSpan(tempoProcessamento));

	}

}
