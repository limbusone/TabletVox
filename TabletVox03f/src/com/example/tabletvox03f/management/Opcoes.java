package com.example.tabletvox03f.management;

public class Opcoes
{
	private static int intervalo_tempo_tocar_frase = 2500; // em milisegundos
	private static int intervalo_tempo_varredura   = 2000; // em milisegundos
	

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
	
	
}
