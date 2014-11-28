package com.example.tabletvox03f.management;

import com.example.tabletvox03f.Utils;

public class Opcoes
{
	private static int intervalo_tempo_tocar_frase = 2500; // em milisegundos
	private static int intervalo_tempo_varredura   = 2000; // em milisegundos
	private static boolean tocar_som_ao_selecionar_imagem = true;
	private static int imageWidth 	= 96;
	private static int imageHeight 	= 96;
	private static int corBorda = Utils.BORDA_VERMELHA;
	

	public static int getIntervalo_tempo_tocar_frase()
	{
		return intervalo_tempo_tocar_frase;
	}

	public static void setIntervalo_tempo_tocar_frase(int intervalo_tempo_tocar_frase)
	{
		Opcoes.intervalo_tempo_tocar_frase = intervalo_tempo_tocar_frase;
	}

	public static int getIntervalo_tempo_varredura()
	{
		return intervalo_tempo_varredura;
	}

	public static void setIntervalo_tempo_varredura(int intervalo_tempo_varredura)
	{
		Opcoes.intervalo_tempo_varredura = intervalo_tempo_varredura;
	}

	public static boolean isTocar_som_ao_selecionar_imagem()
	{
		return tocar_som_ao_selecionar_imagem;
	}

	public static void setTocar_som_ao_selecionar_imagem(
			boolean tocar_som_ao_selecionar_imagem)
	{
		Opcoes.tocar_som_ao_selecionar_imagem = tocar_som_ao_selecionar_imagem;
	}

	public static int getImageWidth()
	{
		return imageWidth;
	}

	public static void setImageWidth(int imageWidth)
	{
		Opcoes.imageWidth = imageWidth;
	}

	public static int getImageHeight()
	{
		return imageHeight;
	}

	public static void setImageHeight(int imageHeight)
	{
		Opcoes.imageHeight = imageHeight;
	}

	public static int getCorBorda()
	{
		return corBorda;
	}

	public static void setCorBorda(int corBorda)
	{
		Opcoes.corBorda = corBorda;
	}
	
	
}
