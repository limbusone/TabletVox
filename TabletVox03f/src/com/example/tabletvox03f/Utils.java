package com.example.tabletvox03f;

import java.util.ArrayList;

import android.content.Context;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tabletvox03f.dal.FilesIO;
import com.example.tabletvox03f.dal.assocImagemSom.AssocImagemSomDAO;
import com.example.tabletvox03f.dal.categoria.Categoria;
import com.example.tabletvox03f.dal.categoria.CategoriaDAO;
import com.example.tabletvox03f.dal.categoria.CategoriaDAOSingleton;
import com.example.tabletvox03f.dal.perfil.Perfil;
import com.example.tabletvox03f.dal.perfil.PerfilDAO;
import com.example.tabletvox03f.dal.perfil.PerfilDAOSingleton;

public class Utils 
{
	public static String MODO_ATIVO = "modo_mouse";
	
	public static String TELAS_NOME_ARQUIVO_XML_ATIVO = "perfil01"; // default : perfil01
	
	//public static String PERFIL_ATIVO_STR = "perfil01"; // default : perfil01
	
	//public static Perfil PERFIL_ATIVO = new Perfil(1, "perfil01", "default_author"); // perfil default
	public static Perfil PERFIL_ATIVO; 
	
	public static String EXTENSAO_ARQUIVO_SOM = "wav";
	
	public static ArrayList<Erro> erros = new ArrayList<Erro>();
	
	// frase global utilizada principalmente no perfil categorizado
	public static ArrayList<ImgItem> lista_imagens_frase_global;
	
	public static final int FORM_INCLUIR = 0;
	
	public static final int FORM_ALTERAR = 1;
	
	public static final int FORM_ALTERAR_NP = 2; // NP : Não Persistente (em memória)
	
	public static final int BORDA_VERMELHA = 0;
	
	public static final int BORDA_PRETA = 1;
	
	public static final int TAMANHO_IMAGEM_DEFAULT = 96;
	
    public static void limpaErros()
    {
    	erros.clear();
    }
    
    public static boolean hasErros()
    {
    	return (erros.size() > 0);
    }
    
	public static void exibirErros(Context ctx)
	{
		String msgErro, tipoCtrl;
		View ctrl;
		int length = Utils.erros.size();
		for (int i = 0; i < length; i++) // varrendo um list de erros 
        {                                // armazenado em Utils.erros
			msgErro  = Utils.erros.get(i).getMsgErro();
			ctrl 	 = Utils.erros.get(i).getControle();
			tipoCtrl = Utils.erros.get(i).getTipoCtrl();
			
			if (tipoCtrl.equals("EditText"))
			{
				EditText e = (EditText) ctrl;
				e.setError(msgErro);
			}
			else if (tipoCtrl.equals("TextView"))
			{
				TextView t = (TextView) ctrl;
				t.setError(msgErro);
			}
			else
				Toast.makeText(ctx, msgErro, Toast.LENGTH_SHORT).show();
			
		}
		Toast.makeText(ctx, "Ops. Há algo errado!", Toast.LENGTH_SHORT).show();
	}
	
	
	public static void carregarDadosIniciais(Context context)
	{
		// inicializar o banco
		Utils.inicializarBD(context);

		(new FilesIO(context)).copiarArquivosXmlDeAssetsParaInternalStorage();
		
	}
	
	public static boolean isAppCarregado(Context context)
	{
		boolean retorno = true;
		AssocImagemSomDAO dao_ais = new AssocImagemSomDAO(context);
		dao_ais.open();
		
		// verifica se existem registros no banco
		retorno = dao_ais.regs_exist();
			
		
		return retorno;
	}
	
	public static void inicializarPerfilDefault(Context context)
	{
		// inicializar perfil default		
		PerfilDAO pfl_dao = new PerfilDAO(context);
		pfl_dao.open();
		Utils.PERFIL_ATIVO = pfl_dao.getPerfilById(1);
		pfl_dao.close();
	}
	
	
	public static void inicializarMock(Context context)
	{
		AssocImagemSomDAO dao_ais = new AssocImagemSomDAO(context);
		
		dao_ais.open();
		
		ArrayList<Categoria> categorias = new ArrayList<Categoria>();
		
		Categoria categoria = null;
		
		// alimentos
		CategoriaDAOSingleton.getInstance().
		incluirCategoria(categoria = new Categoria(1, "alimentos", dao_ais.getImagemById(29), dao_ais.getImagensByIdInterval(43, 54)));
		categorias.add(categoria);
		
		// animais
		CategoriaDAOSingleton.getInstance().
		incluirCategoria(categoria = new Categoria(2, "animais", dao_ais.getImagemById(30), dao_ais.getImagensByIdInterval(55, 66)));
		categorias.add(categoria);
		
		// aparelhos
		CategoriaDAOSingleton.getInstance().
		incluirCategoria(categoria = new Categoria(3, "aparelhos", dao_ais.getImagemById(31), dao_ais.getImagensByIdInterval(67, 75)));
		categorias.add(categoria);
		
		// banheiro
		CategoriaDAOSingleton.getInstance().
		incluirCategoria(categoria = new Categoria(4, "banheiro", dao_ais.getImagemById(32), dao_ais.getImagensByIdInterval(76, 87)));
		categorias.add(categoria);
		
		// bebidas
		CategoriaDAOSingleton.getInstance().
		incluirCategoria(categoria = new Categoria(5, "bebidas", dao_ais.getImagemById(33), dao_ais.getImagensByIdInterval(88, 94)));
		categorias.add(categoria);
		
		// corpo
		CategoriaDAOSingleton.getInstance().
		incluirCategoria(categoria = new Categoria(6, "corpo", dao_ais.getImagemById(34), dao_ais.getImagensByIdInterval(95, 106)));
		categorias.add(categoria);
		
		// familia 1
		CategoriaDAOSingleton.getInstance().
		incluirCategoria(categoria = new Categoria(7, "familia 1", dao_ais.getImagemById(35), dao_ais.getImagensByIdInterval(107, 122)));
		categorias.add(categoria);
		
		// familia 2
		CategoriaDAOSingleton.getInstance().
		incluirCategoria(categoria = new Categoria(8, "familia 2", dao_ais.getImagemById(36), dao_ais.getImagensByIdInterval(123, 136)));
		categorias.add(categoria);		
		
		// frutas
		CategoriaDAOSingleton.getInstance().
		incluirCategoria(categoria = new Categoria(9, "frutas", dao_ais.getImagemById(37), dao_ais.getImagensByIdInterval(137, 148)));
		categorias.add(categoria);		
		
		// lugares
		CategoriaDAOSingleton.getInstance().
		incluirCategoria(categoria = new Categoria(10, "lugares", dao_ais.getImagemById(38), dao_ais.getImagensByIdInterval(149, 160)));
		categorias.add(categoria);		
		
		// pessoas
		CategoriaDAOSingleton.getInstance().
		incluirCategoria(categoria = new Categoria(11, "pessoas", dao_ais.getImagemById(39), dao_ais.getImagensByIdInterval(161, 172)));
		categorias.add(categoria);
		
		// sensacoes
		CategoriaDAOSingleton.getInstance().
		incluirCategoria(categoria = new Categoria(12, "sensacoes", dao_ais.getImagemById(40), dao_ais.getImagensByIdInterval(173, 184)));
		categorias.add(categoria);

		// transporte
		CategoriaDAOSingleton.getInstance().
		incluirCategoria(categoria = new Categoria(13, "transporte", dao_ais.getImagemById(41), dao_ais.getImagensByIdInterval(185, 196)));
		categorias.add(categoria);		
		
		// verbos
		CategoriaDAOSingleton.getInstance().
		incluirCategoria(categoria = new Categoria(14, "verbos", dao_ais.getImagemById(42), dao_ais.getImagensByIdInterval(197, 208)));
		categorias.add(categoria);
		
//		categoria = CategoriaDAOSingleton.getInstance().getCategoriaById(2);
//		categoria.setImagens(dao_ais.getAISListbyIdInterval(161, 172));
//		categorias.add(categoria);
		
		PerfilDAOSingleton.getInstance().getPerfilById(1).setCategorias(categorias);
		
		dao_ais.close();
		
	}
	
	// carregar primeiros dados no banco de dados
	public static void inicializarBD(Context context)
	{
		AssocImagemSomDAO dao_ais = new AssocImagemSomDAO(context);
		
		dao_ais.open();
		
		dao_ais.create("letraa", "letraa", 	"s01002", 	"png", 'n', 0);
		dao_ais.create("letrab", "letrab", 	"s02012", 	"png", 'n', 0);
		dao_ais.create("letrac", "letrac", 	"s05012", 	"png", 'n', 0);
		dao_ais.create("letrac", "letracc", "cedilha", 	"png", 'n', 0);
		dao_ais.create("letrad", "letrad", 	"s10002", 	"png", 'n', 0);
		dao_ais.create("letrae", "letrae", 	"s12012", 	"png", 'n', 0);
		dao_ais.create("letraf", "letraf", 	"s13002", 	"png", 'n', 0);
		dao_ais.create("letrag", "letrag", 	"s16012", 	"png", 'n', 0);
		dao_ais.create("letrah", "letrah", 	"s19012", 	"png", 'n', 0);
		dao_ais.create("letrai", "letrai", 	"s20012", 	"png", 'n', 0);
		dao_ais.create("letraj", "letraj", 	"s21002", 	"png", 'n', 0);
		dao_ais.create("letrak", "letrak", 	"k", 		"png", 'n', 0);
		dao_ais.create("letral", "letral", 	"s22012", 	"png", 'n', 0);
		dao_ais.create("letram", "letram", 	"s24002", 	"png", 'n', 0);
		dao_ais.create("letran", "letran", 	"s25012", 	"png", 'n', 0);
		dao_ais.create("letrao", "letrao", 	"s27012", 	"png", 'n', 0);
		dao_ais.create("letrap", "letrap", 	"s28012", 	"png", 'n', 0);
		dao_ais.create("letraq", "letraq", 	"s31002", 	"png", 'n', 0);
		dao_ais.create("letrar", "letrar", 	"s32002", 	"png", 'n', 0);
		dao_ais.create("letras", "letras", 	"s37002", 	"png", 'n', 0);
		dao_ais.create("letrat", "letrat", 	"s39002", 	"png", 'n', 0);
		dao_ais.create("letrau", "letrau", 	"s42002", 	"png", 'n', 0);
		dao_ais.create("letrav", "letrav", 	"s43012", 	"png", 'n', 0);
		dao_ais.create("letraw", "letraw", 	"letra_w", 	"png", 'n', 0);
		dao_ais.create("letrax", "letrax", 	"s45002", 	"png", 'n', 0);
		dao_ais.create("letray", "letray", 	"y", 		"png", 'n', 0);
		dao_ais.create("letraz", "letraz", 	"s47002", 	"png", 'n', 0);
		
		dao_ais.create("comando falar", "cmd10", "cmd10", "jpg", 'n', 1);
		
		// registros da versao categorizada
		
		// categorias
		dao_ais.create("alimentos", 	"ALIMENT", 		"ALIMENT", 		"jpg", 'c', 0);
		dao_ais.create("animais", 		"ANIMAIS", 		"ANIMAIS", 		"jpg", 'c', 0);
		dao_ais.create("aparelhos", 	"APARELHOS", 	"", 			"jpg", 'c', 0);
		dao_ais.create("banheiro", 		"BANHEIR", 		"BANHEIR", 		"jpg", 'c', 0);
		dao_ais.create("bebidas", 		"BEBIDAS", 		"BEBIDAS", 		"jpg", 'c', 0);
		dao_ais.create("corpo", 		"CORPO", 		"CORPO", 		"jpg", 'c', 0);
		dao_ais.create("familia1", 		"FSP01", 		"FSP01", 		"jpg", 'c', 0);
		dao_ais.create("familia2", 		"FLD02", 		"FLD02", 		"jpg", 'c', 0);
		dao_ais.create("frutas", 		"FRUTAS", 		"frutas", 		"jpg", 'c', 0);
		dao_ais.create("lugares", 		"LUGAR", 		"LUGAR", 		"jpg", 'c', 0);
		dao_ais.create("pessoas", 		"PESSOAS", 		"PESSOAS", 		"jpg", 'c', 0);
		dao_ais.create("sensacoes", 	"SENSAC", 		"SENSAC", 		"jpg", 'c', 0);
		dao_ais.create("transporte", 	"TRANSPOR", 	"TRANSPOR", 	"jpg", 'c', 0);
		dao_ais.create("verbos", 		"verbos", 		"verbos", 		"jpg", 'c', 0);
		
		// itens das categorias
		
		// alimentos
		dao_ais.create("arroz", 	"af357", 	"af357", 	"jpg", 'n', 0);
		dao_ais.create("batata", 	"bf190", 	"bf190", 	"jpg", 'n', 0);
		dao_ais.create("carne", 	"cf133_1", 	"cf133_1", 	"jpg", 'n', 0);
		dao_ais.create("churrasco", "cf286_1", 	"cf286_1", 	"jpg", 'n', 0);
		dao_ais.create("frango", 	"ff118", 	"ff118", 	"jpg", 'n', 0);
		dao_ais.create("feijão", 	"ff129", 	"ff129", 	"jpg", 'n', 0);
		dao_ais.create("macarrão", 	"mf026", 	"mf026", 	"jpg", 'n', 0);
		dao_ais.create("pão", 		"pf138", 	"pf138", 	"jpg", 'n', 0);
		dao_ais.create("pizza", 	"pf361", 	"pf361", 	"jpg", 'n', 0);
		dao_ais.create("salada", 	"sf036", 	"sf036", 	"jpg", 'n', 0);
		dao_ais.create("sanduíche", "sf058", 	"sf058", 	"jpg", 'n', 0);
		dao_ais.create("sopa", 		"sf111", 	"sf111", 	"jpg", 'n', 0);
		
		// animais
		dao_ais.create("baleia", 	"bf097", 	"bf097", 	"jpg", 'n', 0);
		dao_ais.create("cachorro", 	"cf027", 	"cf027", 	"jpg", 'n', 0);
		dao_ais.create("coelho", 	"cf344_1", 	"cf344_1", 	"jpg", 'n', 0);
		dao_ais.create("elefante", 	"ef060", 	"ef060", 	"jpg", 'n', 0);
		dao_ais.create("galinha", 	"gf007", 	"gf007", 	"jpg", 'n', 0);
		dao_ais.create("gato", 		"gf022", 	"gf022", 	"jpg", 'n', 0);
		dao_ais.create("jacaré", 	"jf007", 	"jf007", 	"jpg", 'n', 0);
		dao_ais.create("macaco", 	"mf013", 	"mf013", 	"jpg", 'n', 0);
		dao_ais.create("pato", 		"pf412", 	"pf412", 	"jpg", 'n', 0);
		dao_ais.create("porco", 	"pf523", 	"pf523", 	"jpg", 'n', 0);
		dao_ais.create("sapo", 		"sf084", 	"sf084", 	"jpg", 'n', 0);
		dao_ais.create("vaca", 		"vf005", 	"vf005", 	"jpg", 'n', 0);
		
		// aparelhos
		dao_ais.create("computador", 			"cf403_1", 	"cf403_1", 	"jpg", 'n', 0);
		dao_ais.create("controle remoto", 		"cf465", 	"cf465", 	"jpg", 'n', 0);
		dao_ais.create("filmadora", 			"ff195a", 	"ff195a", 	"jpg", 'n', 0);
		dao_ais.create("máquina fotografica", 	"mf216", 	"mf216", 	"jpg", 'n', 0);
		dao_ais.create("rádio", 				"rf008", 	"rf008", 	"jpg", 'n', 0);
		dao_ais.create("secadora de cabelos", 	"sf112", 	"sf112", 	"jpg", 'n', 0);
		dao_ais.create("telefone", 				"tf054a", 	"tf054a", 	"jpg", 'n', 0);
		dao_ais.create("celular", 				"tf056", 	"tf056", 	"jpg", 'n', 0);
		dao_ais.create("televisão", 			"tf069", 	"tf069", 	"jpg", 'n', 0);
		
		// banheiro
		dao_ais.create("banheira", 			"bf141", 	"bf141", 	"jpg", 'n', 0);
		dao_ais.create("chuveiro", 			"cf291_1", 	"cf291_1", 	"jpg", 'n', 0);
		dao_ais.create("cotonete", 			"cf523", 	"cf523", 	"jpg", 'n', 0);
		dao_ais.create("desodorante", 		"df163", 	"df163", 	"jpg", 'n', 0);
		dao_ais.create("pasta de dente", 	"df231", 	"df231", 	"jpg", 'n', 0);
		dao_ais.create("pente", 			"pf014a", 	"pf014a", 	"jpg", 'n', 0);
		dao_ais.create("privada", 			"pf168", 	"pf168", 	"jpg", 'n', 0);
		dao_ais.create("papel higiênico", 	"pf186", 	"pf186", 	"jpg", 'n', 0);
		dao_ais.create("escova de dente", 	"pf396_2", 	"pf396_2", 	"jpg", 'n', 0);
		dao_ais.create("sabonete", 			"sf004a", 	"sf004a", 	"jpg", 'n', 0);
		dao_ais.create("toalha", 			"tf173", 	"tf173", 	"jpg", 'n', 0);
		dao_ais.create("shampoo", 			"", 		"", 		"jpg", 'n', 0);
		
		// bebidas
		dao_ais.create("água", 			"af139", 	"af139", 	"jpg", 'n', 0);
		dao_ais.create("café", 			"cf035", 	"cf035", 	"jpg", 'n', 0);
		dao_ais.create("chá", 			"cf231_1", 	"cf231_1", 	"jpg", 'n', 0);
		dao_ais.create("gelo", 			"gf029", 	"gf029", 	"jpg", 'n', 0);
		dao_ais.create("leite", 		"lf258", 	"lm258", 	"jpg", 'n', 0);
		dao_ais.create("refrigerante", 	"rf069", 	"rf069", 	"jpg", 'n', 0);
		dao_ais.create("suco", 			"sf150", 	"sf150", 	"jpg", 'n', 0);
		
		// corpo
		dao_ais.create("boca", 			"bf031", "bf031", "jpg", 'n', 0);
		dao_ais.create("braço", 		"bf129", "bf129", "jpg", 'n', 0);
		dao_ais.create("dedo da mão", 	"df102", "df102", "jpg", 'n', 0);
		dao_ais.create("dedo do pé", 	"df105", "df105", "jpg", 'n', 0);
		dao_ais.create("dentes", 		"df228", "df228", "jpg", 'n', 0);
		dao_ais.create("língua", 		"lf020", "lf020", "jpg", 'n', 0);
		dao_ais.create("mão", 			"mf196", "mf196", "jpg", 'n', 0);
		dao_ais.create("nariz", 		"nf026", "nf026", "jpg", 'n', 0);
		dao_ais.create("olho", 			"of037", "of037", "jpg", 'n', 0);
		dao_ais.create("orelha", 		"of061", "of061", "jpg", 'n', 0);
		dao_ais.create("perna", 		"pf112", "pf112", "jpg", 'n', 0);
		dao_ais.create("pé", 			"pf444", "pf444", "jpg", 'n', 0);
		
		
		// familia1
		dao_ais.create("lucas", 		"FSP01", "FSP01", "jpg", 'n', 0);
		dao_ais.create("mamãe", 		"FSP02", "FSP02", "jpg", 'n', 0);
		dao_ais.create("papai", 		"FSP03", "FSP03", "jpg", 'n', 0);
		dao_ais.create("helena", 		"FSP04", "FSP04", "jpg", 'n', 0);
		dao_ais.create("tata", 			"FSP05", "FSP05", "jpg", 'n', 0);
		dao_ais.create("vó geny", 		"FSP06", "FSP07", "jpg", 'n', 0);
		dao_ais.create("vô brandão", 	"FSP07", "FSP06", "jpg", 'n', 0);
		dao_ais.create("tio murilo", 	"FSP08", "FSP08", "jpg", 'n', 0);
		dao_ais.create("baba", 			"FSP09", "FSP09", "jpg", 'n', 0);
		dao_ais.create("tia lidia", 	"FSP10", "FSP10", "jpg", 'n', 0);
		dao_ais.create("tio fernando", 	"FSP11", "FSP11", "jpg", 'n', 0);
		dao_ais.create("tia ivania", 	"FSP12", "FSP12", "jpg", 'n', 0);
		dao_ais.create("tio ridley", 	"FSP13", "FSP13", "jpg", 'n', 0);
		dao_ais.create("tia olga", 		"FSP14", "FSP14", "jpg", 'n', 0);
		dao_ais.create("erica", 		"FSP15", "FSP15", "jpg", 'n', 0);
		dao_ais.create("bruna", 		"FSP16", "FSP16", "jpg", 'n', 0);
		
		
		// familia2
		dao_ais.create("vô claudio", 	"FLD02", "FLD02", "jpg", 'n', 0);
		dao_ais.create("vó natalia", 	"FLD01", "FLD01", "jpg", 'n', 0);
		dao_ais.create("tia vania", 	"FLD03", "FLD03", "jpg", 'n', 0);
		dao_ais.create("tia ana", 		"FLD04", "FLD04", "jpg", 'n', 0);
		dao_ais.create("tia lidia", 	"FLD05", "FLD05", "jpg", 'n', 0);
		dao_ais.create("tio manuel", 	"FLD06", "FLD06", "jpg", 'n', 0);
		dao_ais.create("tia gata", 		"FLD07", "FLD07", "jpg", 'n', 0);
		dao_ais.create("tia cecilia", 	"FLD08", "FLD08", "jpg", 'n', 0);
		dao_ais.create("tia alexandra", "FLD09", "FLD09", "jpg", 'n', 0);
		dao_ais.create("cynara", 		"FLD10", "FLD10", "jpg", 'n', 0);
		dao_ais.create("juliana", 		"FLD11", "FLD11", "jpg", 'n', 0);
		dao_ais.create("suze", 			"FLD12", "FLD12", "jpg", 'n', 0);
		dao_ais.create("rebecca", 		"FLD13", "FLD13", "jpg", 'n', 0);
		dao_ais.create("tais", 			"FLD14", "FLD14", "jpg", 'n', 0);

		// frutas
		dao_ais.create("abacate", 	"af013", 	"af013", "jpg", 'n', 0);
		dao_ais.create("abacaxi", 	"af014", 	"af014", "jpg", 'n', 0);
		dao_ais.create("banana", 	"bf101a", 	"bf101", "jpg", 'n', 0);
		dao_ais.create("laranja", 	"lf111", 	"lf110", "jpg", 'n', 0);
		dao_ais.create("maçã", 		"mf007", 	"mf007", "jpg", 'n', 0);
		dao_ais.create("morango", 	"mf117", 	"mf117", "jpg", 'n', 0);
		dao_ais.create("mamão", 	"mf142", 	"mf142", "jpg", 'n', 0);
		dao_ais.create("melancia", 	"mf155", 	"mf155", "jpg", 'n', 0);
		dao_ais.create("melão", 	"mf158_1", 	"mf158", "jpg", 'n', 0);
		dao_ais.create("maracujá", 	"mf226", 	"mf226", "jpg", 'n', 0);
		dao_ais.create("pêra", 		"pf033", 	"pf033", "jpg", 'n', 0);
		dao_ais.create("uva", 		"uf057", 	"uf057", "jpg", 'n', 0);
		
		// lugares
		dao_ais.create("apartamento", 			"af290", 	"af290", 	"jpg", 'n', 0);
		dao_ais.create("casa", 					"CASA", 	"CASA", 	"jpg", 'n', 0);
		dao_ais.create("circo", 				"cf111_2", 	"cf111_2", 	"jpg", 'n', 0);
		dao_ais.create("escola", 				"ef049", 	"ef049", 	"jpg", 'n', 0);
		dao_ais.create("estrada", 				"ef124", 	"ef124", 	"jpg", 'n', 0);
		dao_ais.create("fazenda", 				"ff084", 	"ff084", 	"jpg", 'n', 0);
		dao_ais.create("hospital", 				"hf040", 	"hf040", 	"jpg", 'n', 0);
		dao_ais.create("igreja", 				"if010", 	"if010", 	"jpg", 'n', 0);
		dao_ais.create("jardim zôológico", 		"jf017", 	"jf017", 	"jpg", 'n', 0);
		dao_ais.create("praia", 				"pf078", 	"pf078", 	"jpg", 'n', 0);
		dao_ais.create("parque de diversões", 	"pf342", 	"pf342", 	"jpg", 'n', 0);
		dao_ais.create("piscina", 				"pf344", 	"pf344",	"jpg", 'n', 0);
		
		
		// pessoas
		dao_ais.create("fisioterapeuta", 	"ff007", 	"ff007_1", 	"jpg", 'n', 0);
		dao_ais.create("fonoaudiólogo", 	"ff068", 	"ff068", 	"jpg", 'n', 0);
		dao_ais.create("irmão", 			"if106", 	"if106", 	"jpg", 'n', 0);
		dao_ais.create("irmã", 				"if107", 	"if107", 	"jpg", 'n', 0);
		dao_ais.create("mãe", 				"mf071", 	"mf071", 	"jpg", 'n', 0);
		dao_ais.create("pai", 				"", 		"", 		"jpg", 'n', 0);
		dao_ais.create("menina", 			"mf176", 	"mf176", 	"jpg", 'n', 0);
		dao_ais.create("menino", 			"mf185", 	"mf185", 	"jpg", 'n', 0);
		dao_ais.create("professora", 		"profa", 	"profa", 	"jpg", 'n', 0);
		dao_ais.create("vovô", 				"vf027a", 	"vf122a", 	"jpg", 'n', 0);
		dao_ais.create("vovó", 				"vf027", 	"vf122", 	"jpg", 'n', 0);
		dao_ais.create("t.o.", 				"", 		"", 		"jpg", 'n', 0);
		
		// sensacoes
		dao_ais.create("calor", 		"cf093", 	"cf093", 	"jpg", 'n', 0);
		dao_ais.create("dor", 			"d521", 	"d454_1", 	"jpg", 'n', 0);
		dao_ais.create("dor de ouvido", "d527", 	"d527", 	"jpg", 'n', 0);
		dao_ais.create("fome", 			"ff053", 	"ff064", 	"jpg", 'n', 0);
		dao_ais.create("frio", 			"ff135", 	"ff135", 	"jpg", 'n', 0);
		dao_ais.create("gripe", 		"gf090", 	"gf090", 	"jpg", 'n', 0);
		dao_ais.create("sono", 			"sf109", 	"sf109", 	"jpg", 'n', 0);
		dao_ais.create("sede", 			"sf126", 	"sf126", 	"jpg", 'n', 0);
		dao_ais.create("suor", 			"sf129a", 	"sf129b", 	"jpg", 'n', 0);
		dao_ais.create("tosse", 		"tf132", 	"tf132", 	"jpg", 'n', 0);
		dao_ais.create("vontade", 		"vf119", 	"vf119", 	"jpg", 'n', 0);
		dao_ais.create("dor de cabeça", "", 		"", 		"jpg", 'n', 0);
		
		// transporte
		dao_ais.create("ambulância", 	"af211", 	"af211", 	"jpg", 'n', 0);
		dao_ais.create("avião", 		"af442", 	"af442", 	"jpg", 'n', 0);
		dao_ais.create("balão de ar", 	"bf089", 	"bf089", 	"jpg", 'n', 0);
		dao_ais.create("barco", 		"bf168", 	"bf168", 	"jpg", 'n', 0);
		dao_ais.create("bicicleta", 	"bf249", 	"bf249", 	"jpg", 'n', 0);
		dao_ais.create("caminhão", 		"cf069", 	"cf069", 	"jpg", 'n', 0);
		dao_ais.create("carro", 		"cf142_1", 	"cf142_1", 	"jpg", 'n', 0);
		dao_ais.create("helicóptero", 	"hf013", 	"hf013", 	"jpg", 'n', 0);
		dao_ais.create("moto", 			"mf156", 	"mf156", 	"jpg", 'n', 0);
		dao_ais.create("metrô", 		"mf262", 	"mf262", 	"jpg", 'n', 0);
		dao_ais.create("ônibus", 		"of050", 	"of050", 	"jpg", 'n', 0);
		dao_ais.create("trem", 			"tf094", 	"tf094", 	"jpg", 'n', 0);
		
		// verbos
		dao_ais.create("tomar banho", 	"bf137", 	"bf137", 	"jpg", 'v', 0);
		dao_ais.create("beber", 		"bf218", 	"bf218", 	"jpg", 'n', 0);
		dao_ais.create("comer", 		"cf384_1", 	"cf384_1", 	"jpg", 'v', 0);
		dao_ais.create("dormir", 		"d533", 	"d533", 	"jpg", 'v', 0);
		dao_ais.create("deitar", 		"df171", 	"df171", 	"jpg", 'v', 0);
		dao_ais.create("estar", 		"e2s", 		"e2s", 		"jpg", 'n', 0);
		dao_ais.create("escovar", 		"ef077", 	"ef083", 	"jpg", 'v', 0);
		dao_ais.create("gostar", 		"gostar", 	"gostar", 	"jpg", 'v', 0);
		dao_ais.create("ir", 			"if103_1", 	"if103_1", 	"jpg", 'v', 0);
		dao_ais.create("querer", 		"querer", 	"querer", 	"jpg", 'v', 0);
		dao_ais.create("sentar", 		"sf187", 	"sf187", 	"jpg", 'v', 0);
		dao_ais.create("ser", 			"", 		"", 		"jpg", 'v', 0);
		
		dao_ais.create("comando voltar", "cmd11", "cmd11", "jpg", 'n', 2);
		
		
		// incluindo as categorias de fato
		CategoriaDAO dao_cat = new CategoriaDAO(context);
		
		dao_cat.open();
		
		dao_cat.create(29, "alimentos", 	dao_ais.getImagensByIdInterval(43, 54));
		dao_cat.create(30, "animais", 		dao_ais.getImagensByIdInterval(55, 66));
		dao_cat.create(31, "aparelhos", 	dao_ais.getImagensByIdInterval(67, 75));
		dao_cat.create(32, "banheiro",  	dao_ais.getImagensByIdInterval(76, 87));
		dao_cat.create(33, "bebidas", 		dao_ais.getImagensByIdInterval(88, 94));
		dao_cat.create(34, "corpo", 		dao_ais.getImagensByIdInterval(95, 106));
		dao_cat.create(35, "familia 1", 	dao_ais.getImagensByIdInterval(107, 122));
		dao_cat.create(36, "familia 2", 	dao_ais.getImagensByIdInterval(123, 136));
		dao_cat.create(37, "frutas", 		dao_ais.getImagensByIdInterval(137, 148));
		dao_cat.create(38, "lugares", 		dao_ais.getImagensByIdInterval(149, 160));
		dao_cat.create(39, "pessoas", 		dao_ais.getImagensByIdInterval(161, 172));
		dao_cat.create(40, "sensacoes", 	dao_ais.getImagensByIdInterval(173, 184));
		dao_cat.create(41, "transporte", 	dao_ais.getImagensByIdInterval(185, 196));
		dao_cat.create(42, "verbos", 		dao_ais.getImagensByIdInterval(197, 208));
		
		dao_ais.close();
		
		PerfilDAO dao_pfl = new PerfilDAO(context);
		
		dao_pfl.open();
		
		dao_pfl.create("Perfil 01", "Autor 01", dao_cat.getCategoriasByIdInterval(1, 14));

		dao_cat.close();
		
		dao_pfl.close();
		
		
	}	

}
