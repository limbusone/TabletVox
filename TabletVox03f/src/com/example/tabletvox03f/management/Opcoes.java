package com.example.tabletvox03f.management;


public class Opcoes
{
	public static final String INTERVALO_TEMPO_TOCAR_FRASE_KEY = "intervalo_tempo_tocar_frase";
	public static final int INTERVALO_TEMPO_TOCAR_FRASE_DEFAULT = 1000;
	public static final int INTERVALO_TEMPO_TOCAR_FRASE_MIN		= 100;
	public static final int INTERVALO_TEMPO_TOCAR_FRASE_MAX		= 5000;
	
	public static final String INTERVALO_TEMPO_VARREDURA_KEY = "intervalo_tempo_varredura";
	public static final int INTERVALO_TEMPO_VARREDURA_DEFAULT = 2000;
	public static final int INTERVALO_TEMPO_VARREDURA_MIN	  = 500;
	public static final int INTERVALO_TEMPO_VARREDURA_MAX	  = 5000;
	
	public static final String TOCAR_SOM_AO_SELECIONAR_IMAGEM_KEY = "tocar_som_ao_selecionar_imagem";
	public static final boolean TOCAR_SOM_AO_SELECIONAR_IMAGEM_DEFAULT = true;
	
	public static final String VOLTAR_AUTOMATICAMENTE_PARA_TELA_CATEGORIAS_KEY 		= "voltar_automaticamente_para_tela_categorias";
	public static final boolean VOLTAR_AUTOMATICAMENTE_PARA_TELA_CATEGORIAS_DEFAULT = false;	
	
	public static final String TAMANHO_IMAGEM_KEY = "tamanho_imagem";
	public static final int TAMANHO_IMAGEM_DEFAULT = 96;
	
	public static final String COR_BORDA_KEY = "cor_borda";
	public static final int COR_BORDA_DEFAULT = Opcoes.BORDA_VERMELHA;

	public static final int BORDA_VERMELHA = 0;
	
	public static final int BORDA_PRETA = 1;
	
	public static final String SETTINGS_NAME = "settings";
	
	public static final String PERFIL_DEFAULT_KEY = "perfil_default";
	
	public static final int PERFIL_DEFAULT_DEFAULT = 1;
	
	public static int validarIntervaloTempoTocarFrase(String param)
	{
		try
		{
			int value = Integer.parseInt(param);
			
			if (value < INTERVALO_TEMPO_TOCAR_FRASE_MIN)
				return INTERVALO_TEMPO_TOCAR_FRASE_MIN;
			else if (value > INTERVALO_TEMPO_TOCAR_FRASE_MAX)
				return INTERVALO_TEMPO_TOCAR_FRASE_MAX;
			else 
				return value;
		}
		catch (NumberFormatException nEx)
		{
			return INTERVALO_TEMPO_TOCAR_FRASE_DEFAULT; 
		}
	}
	
	public static int validarIntervaloTempoVarredura(String param)
	{
		try
		{
			int value = Integer.parseInt(param);
			
			if (value < INTERVALO_TEMPO_VARREDURA_MIN)
				return INTERVALO_TEMPO_VARREDURA_MIN;
			else if (value > INTERVALO_TEMPO_VARREDURA_MAX)
				return INTERVALO_TEMPO_VARREDURA_MAX;
			else 
				return value;
		}
		catch (NumberFormatException nEx)
		{
			return INTERVALO_TEMPO_VARREDURA_DEFAULT; 
		}
	}
	
}
