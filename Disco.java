package trabalho;

import desmoj.core.simulator.Entity;

import desmoj.core.simulator.Model;
import desmoj.core.simulator.TimeSpan;
import trabalho.EventoTerminoDisco;

public class Disco extends Entity {

	private boolean ocupado;
	private Processo cliente;

	public Disco(Model owner, String name, boolean showInTrace) {
		super(owner, name, showInTrace);

		this.ocupado = false;
	}

	public void ler(Processo cliente) {

		SiteCompras modeloSiteCompras;
		double tempoLeitura;
		EventoTerminoDisco eventoTerminoDisco;

		this.setCliente(cliente);
		modeloSiteCompras = (SiteCompras) getModel();

		cliente.setTempoEntradaDisco(presentTime().getTimeAsDouble());
		
		if (cliente.isEhDiscoRapido()) {
			modeloSiteCompras.getTempoEsperaDiscoRapido()
					.update(cliente.getSaidaFilaDiscoRapido() - cliente.getEntradaFilaDiscoRapido());
		} else {
			modeloSiteCompras.getTempoEsperaDiscoLento()
					.update(cliente.getSaidaFilaDiscoLento() - cliente.getEntradaFilaDiscoLento());
		}

		tempoLeitura = cliente.isEhDiscoRapido() ? modeloSiteCompras.getTempoLeituraDiscoRapido()
				: modeloSiteCompras.getTempoLeituraDiscoLento();

		modeloSiteCompras.sendTraceNote(this + " serve " + cliente + " por " + tempoLeitura + " minutos.");

		eventoTerminoDisco = new EventoTerminoDisco(modeloSiteCompras,
				"Evento relacionado ao término de leitura" + "do " + " disco", true);

		eventoTerminoDisco.schedule(this, cliente, new TimeSpan(tempoLeitura));

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

}
