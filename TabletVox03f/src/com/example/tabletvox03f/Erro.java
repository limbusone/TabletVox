package com.example.tabletvox03f;

import android.view.View;

public class Erro
{
	private String msgErro;
	private View controle;
	private String tipoCtrl;
	
	public Erro(String msg, View ctrl, String tpCtrl)
	{
		msgErro  = msg;
		controle = ctrl;
		tipoCtrl = tpCtrl;
	}
	
	public Erro(String msg)
	{
		msgErro  = msg;
		controle = null;
		tipoCtrl = "";
	}
	
	public String getMsgErro()
	{
		return msgErro;
	}
	public void setMsgErro(String msgErro)
	{
		this.msgErro = msgErro;
	}
	
	public View getControle()
	{
		return controle;
	}
	public void setControle(View controle)
	{
		this.controle = controle;
	}

	public String getTipoCtrl()
	{
		return tipoCtrl;
	}

	public void setTipoCtrl(String tipoCtrl)
	{
		this.tipoCtrl = tipoCtrl;
	}
}
