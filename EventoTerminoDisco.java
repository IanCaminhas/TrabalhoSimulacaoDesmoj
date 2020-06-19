package trabalho;

import desmoj.core.simulator.EventOf2Entities;
import desmoj.core.simulator.Model;

public class EventoTerminoDisco extends EventOf2Entities<Disco, Processo> {

	public EventoTerminoDisco(Model owner, String name, boolean showInTrace) {
		super(owner, name, showInTrace);
	}

	@Override
	public void eventRoutine(Disco disco, Processo processo) {

		SiteCompras modeloSiteCompras = (SiteCompras) getModel();

		modeloSiteCompras.sendTraceNote("terminou a leitura no disco");
		
		processo.setUsoDisco(false);
		
		processo.setTempoSaidaDisco(presentTime().getTimeAsDouble());
		double tempoOcupadoLendoDisco = processo.getTempoSaidaDisco()-processo.getTempoEntradaDisco();

		
		modeloSiteCompras.servirProcesso(processo);

		if (processo.isEhDiscoRapido() == true) {
			modeloSiteCompras.getNumeroRequisicoesCompletadasDiscoRapido().update();
			modeloSiteCompras.examinarFilaDiscoRapido(disco);	
			modeloSiteCompras.getSomaTempoOcupacaoDiscoRapido().update((long) tempoOcupadoLendoDisco);
		}

		else {
			modeloSiteCompras.getNumeroRequisicoesCompletadasDiscoLento().update();
			modeloSiteCompras.examinarFilaDiscoLento(disco);
			modeloSiteCompras.getSomaTempoOcupacaoDiscoLento().update((long) tempoOcupadoLendoDisco);


		}

	}

}
