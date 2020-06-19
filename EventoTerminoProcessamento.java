package trabalho;

import desmoj.core.simulator.EventOf2Entities;
import desmoj.core.simulator.Model;

public class EventoTerminoProcessamento extends EventOf2Entities<Processador,Processo > {

	public EventoTerminoProcessamento(Model owner, String name, boolean showInTrace) {
		super(owner, name, showInTrace);
	}

	@Override
	public void eventRoutine(Processador processador , Processo processo) {
		
		SiteCompras modeloSiteCompras = (SiteCompras) getModel();

		modeloSiteCompras.sendTraceNote(processador + "terminou o processamento");

		modeloSiteCompras.getNumeroRequisicoesCompletadasProcessador().update();

		modeloSiteCompras.liberarProcessador(processo, processador);

		processo.setTempoSaidaProcessador(presentTime().getTimeAsDouble());
		
		double tempoProcessamentoCPU = processo.getTempoSaidaProcessador()-processo.getTempoEntradaProcessador();
		
		modeloSiteCompras.getSomaTempoOcupacaoCPU().update((long) tempoProcessamentoCPU);
		
		
		
		
	}

}
