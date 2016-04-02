package puc.redes.controller;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.*;

import org.slf4j.*;
import org.slf4j.Logger;
import org.springframework.http.MediaType;
import org.springframework.messaging.handler.annotation.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import puc.redes.dto.Jogador;
import puc.redes.dto.Message;
import puc.redes.dto.OutputMessage;

@Controller
@RequestMapping("/")
public class ChatController {

  private String url = "";
  private AtomicInteger counter = new AtomicInteger();

  private static Jogador jogador;

  //ENDERE�O DO SERVIDOR
  String IPServidor = "192.168.1.241";
  int PortaServidor = 7000;

  @RequestMapping(method = RequestMethod.GET)
  public String viewApplication() {
    return "index";
  }

  @RequestMapping(value = "/enviar/{palavra}", method = RequestMethod.GET)
  public String guess(@PathVariable String palavra){
    cliente(palavra);
    return palavra;
  }

  public void cliente (String msg) {
    try
    {

      //ESTABELECE CONEX�O COM SERVIDOR
      System.out.println(" -C- Conectando ao servidor ->" + IPServidor + ":" +PortaServidor);
      Socket socktCli = new Socket (IPServidor,PortaServidor);
      System.out.println(" -C- Detalhes conexao :" + socktCli.toString()); //DETALHAMENTO (EXTRA)

      //CRIA UM PACOTE DE SA�DA PARA ENVIAR MENSAGENS, ASSOCIANDO-O � CONEX�O (c)
      ObjectOutputStream sCliOut = new ObjectOutputStream(socktCli.getOutputStream());
      sCliOut.writeObject(msg);//ESCREVE NO PACOTE
      System.out.println(" -C- Enviando mensagem...");
      sCliOut.flush(); //ENVIA O PACOTE

      //CRIA UM PACOTE DE ENTRADA PARA RECEBER MENSAGENS, ASSOCIADO � CONEX�O (c)
      ObjectInputStream sCliIn = new ObjectInputStream (socktCli.getInputStream());
      System.out.println(" -C- Recebendo mensagem...");
      String strMsg = sCliIn.readObject().toString(); //ESPERA (BLOQUEADO) POR UM PACOTE

      //PROCESSA O PACOTE RECEBIDO
      System.out.println(" -C- Mensagem recebida: " + strMsg);

      //FINALIZA A CONEX�O
      socktCli.close();
      System.out.println(" -C- Conexao finalizada...");
    }
    catch(Exception e) //SE OCORRER ALGUMA EXCESS�O, ENT�O DEVE SER TRATADA (AMIGAVELMENTE)
    {
      System.out.println(" -C- O seguinte problema ocorreu : \n" + e.toString());
    }
  }

  @RequestMapping(value = "/jogador")
  @ResponseBody
  public Jogador getJogador(){
    jogador = new Jogador(counter.incrementAndGet(), "Jogador " + counter.get());
    return jogador;
  }

  @RequestMapping(value = "/palavra", produces = "text/plain")
  @ResponseBody
  public String getPalavra(){
    return "cadeira";
  }

  @RequestMapping(value = "/foto" ,
          method = RequestMethod.POST,
          consumes = MediaType.ALL_VALUE,
          produces = MediaType.ALL_VALUE,
          headers = "Accept=*/*")
  @ResponseBody
  public String receberFoto(@RequestBody String url){
    this.url = url;
    return url;
  }

  @RequestMapping(value = "/foto",
          method = RequestMethod.GET)
  @ResponseBody
  public String enviarFoto(){
    return url;
  }


  @MessageMapping("/hello")
  @SendTo("/topic/greetings")
  @ResponseBody
  public OutputMessage greeting(Message message) throws Exception {
//    guess(message.getMessage());
    return new OutputMessage(message, new Date(), jogador);
  }
}
