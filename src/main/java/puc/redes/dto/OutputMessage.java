package puc.redes.dto;

import java.util.Date;

public class OutputMessage extends Message {

	private Date time;

	private Jogador jogador;

	public OutputMessage(Message original, Date time, Jogador j) {
		super(original.getId(), original.getMessage());
		this.jogador = j;
		this.time = time;
	}

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}

	public Jogador getJogador() {
		return jogador;
	}

	public void setJogador(Jogador jogador) {
		this.jogador = jogador;
	}
}
