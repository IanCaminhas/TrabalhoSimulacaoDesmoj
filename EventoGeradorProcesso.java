package trabalho;

import desmoj.core.simulator.ExternalEvent;

import desmoj.core.simulator.Model;
import desmoj.core.simulator.TimeSpan;
import trabalho.EventoGeradorProcesso;

public class EventoGeradorProcesso extends ExternalEvent{

	public EventoGeradorProcesso(Model owner, String name, boolean showInTrace) {
		super(owner, name, showInTrace);
	}

	@Override
	public void eventRoutine() {
		SiteCompras modeloSiteCompras;
		Processo cliente;
		
		EventoGeradorProcesso eventoGeradorCliente;
		double instanteChegadaProcesso;
		
		
		modeloSiteCompras = (SiteCompras) getModel();
		cliente = new Processo(modeloSiteCompras, "Processo", true);
		modeloSiteCompras.sendTraceNote("Cliente chegou ao pool de processos em " + modeloSiteCompras.presentTime());
		modeloSiteCompras.servirProcesso(cliente);
		
		instanteChegadaProcesso = modeloSiteCompras.getdistribuicaoTempoOciosidade();
		
		eventoGeradorCliente = new EventoGeradorProcesso(modeloSiteCompras, "Evento externo responsável por gerar um processo ao pool", true);
		eventoGeradorCliente.schedule (new TimeSpan(instanteChegadaProcesso));	
		
	}
	
	

}
