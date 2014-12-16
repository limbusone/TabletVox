package com.example.tabletvox03f.management.assocImagemSom;

import java.io.IOException;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tabletvox03f.Erro;
import com.example.tabletvox03f.ImgItem;
import com.example.tabletvox03f.R;
import com.example.tabletvox03f.Utils;
import com.example.tabletvox03f.dal.FilesIO;
import com.example.tabletvox03f.dal.assocImagemSom.AssocImagemSom;
import com.example.tabletvox03f.dal.assocImagemSom.AssocImagemSomDAO;
import com.example.tabletvox03f.management.FormularioBaseActivity;

public class FormularioAssocImagemSomActivity extends FormularioBaseActivity
{
	protected static final int REQUEST_CODE_PICK_FILE_TO_OPEN = 1;
	protected static final int REQUEST_CODE_PICK_DIRECTORY = 2;
	
	private final int REQUEST_CODE_PICK_IMG   = 1;
	private final int REQUEST_CODE_PICK_SOUND = 2;
	
	public static final int RC_IMG_INCLUIDA_SUCESSO = 1;
	public static final int RC_IMG_EDITADA_SUCESSO 	= 2; 
	
	// serve para saber se está requerindo um arquivo de imagem ou som
	private int current_file_code;
	
	private String caminho_origem_img;
	private String caminho_origem_som;
	
	private String caminho_destino_img;
	private String caminho_destino_som;
	
	private AssocImagemSom ais;
	
	private AssocImagemSom old_ais;
	
	private boolean isSomChanged; 
	
	private OnClickListener tocarSomEvento = new OnClickListener()
	{
		
		@Override
		public void onClick(View v)
		{
			FormularioAssocImagemSomActivity thisContext = FormularioAssocImagemSomActivity.this;
			ImgItem imgItem = new ImgItem(thisContext);
			
			if (tipo_form == FORM_ALTERAR) 
			{
				// se o form for tipo editar
				
				// se o usuario não trocou o som
				if (!isSomChanged)
				{
					imgItem.setAssocImagemSom(ais);
					imgItem.tocarSom(thisContext);
				}
				else
					imgItem.tocarSom(caminho_origem_som);
			}
			else
				// se o form for tipo incluir
				imgItem.tocarSom(caminho_origem_som);
		}
	};

	@Override
	protected int[] getDadosForm()
	{
		int[] dados = {R.layout.formulario_ais_interface, R.menu.um_action_salvar, 
				R.string.title_activity_criar_imagem, R.string.title_activity_editar_imagem};
		return dados;
	}	
	
	@Override
	protected void onCreateFilho()
	{

		ImageButton btnUploadImg = (ImageButton) findViewById(R.id.btnUploadImg);
		Button btnUploadSom = (Button) findViewById(R.id.btnUploadSom);
		
		// abre o ES explorer para escolha do arquivo de imagem
		btnUploadImg.setOnClickListener(new OnClickListener()
		{
			
			@Override
			public void onClick(View arg0)
			{
				current_file_code = REQUEST_CODE_PICK_IMG;
				openFile();
			}
		});
		
		// abre o ES explorer para escolha do arquivo de som
		btnUploadSom.setOnClickListener(new OnClickListener()
		{
			
			@Override
			public void onClick(View arg0)
			{
				current_file_code = REQUEST_CODE_PICK_SOUND;
				openFile();
			}
		});
		
		ImageButton btnTocar = (ImageButton) findViewById(R.id.btnTocar);
		btnTocar.setOnClickListener(tocarSomEvento);
		
		isSomChanged = false;
	}
	
	@Override
	protected void initCriarForm()
	{
		ais = new AssocImagemSom();
		caminho_origem_img = caminho_origem_som = caminho_destino_img = caminho_destino_som = "";
	}

	@Override
	protected void initEditarForm(Intent intent)
	{
		EditText txtDesc 		 = (EditText) findViewById(R.id.txtDesc);
		RelativeLayout laySom 	 = (RelativeLayout) findViewById(R.id.laySom);
		TextView lblSomSel 		 = (TextView) findViewById(R.id.lblSomSel);
		ImageButton	btnUploadImg = (ImageButton) findViewById(R.id.btnUploadImg);
		//CheckBox chkIsCategoria	 = (CheckBox) findViewById(R.id.chkIsCategoria);
		
		FilesIO fio = new FilesIO(this);
		
		ais = intent.getParcelableExtra("ais");
		
		old_ais = new AssocImagemSom(ais);
		
		txtDesc.setText(ais.getDesc());
		caminho_destino_img = ais.getTituloImagem() + "." + ais.getExt();
		caminho_destino_som = ais.getTituloSom() + "." + FilesIO.EXTENSAO_ARQUIVO_SOM;
		
		btnUploadImg.setImageDrawable(fio.getImgItemDrawableFromInternalStorageOrAssets(ais));
		lblSomSel.setText(caminho_destino_som);
		laySom.setVisibility(View.VISIBLE);
		
//		if (ais.getTipo() == 'c')
//			chkIsCategoria.setChecked(true);
			
	}

	@Override
	protected void incluir()
	{
		EditText txtDesc 		 = (EditText) findViewById(R.id.txtDesc);
//		CheckBox chkIsCategoria	 = (CheckBox) findViewById(R.id.chkIsCategoria);
//		RadioButton optSubs = (RadioButton) findViewById(R.id.optSubs);
//		RadioButton optVerb = (RadioButton) findViewById(R.id.optVerb);

		FilesIO fio = new FilesIO(this);
		
		// populando objeto AssocImagemSom
		ais.setDesc(txtDesc.getText().toString());
		ais.setTituloImagem(fio.getNomeDoArquivoSemExtensao(caminho_destino_img));
		ais.setTituloSom(fio.getNomeDoArquivoSemExtensao(caminho_destino_som));
		ais.setExt(fio.getExtensaoDoArquivo(caminho_destino_img));
		//ais.setTipo((chkIsCategoria.isChecked()) ? 'c' : 'n');
//		ais.setTipo((optSubs.isChecked()) ? 'n' : optVerb.isChecked()? 'v' : 'c');		
		ais.setCmd(0);
		ais.setAtalho(false);
		
		
		retirarErros();
		
		// *** validacao ***
		if (!(validarUpload() & validarDados()))
		{
			Utils.exibirErros(this);
			return;
		}
		
		try
		{
		// *** upload ***
			
			// upload imagem
			fio.copiarArquivoDeSomOuImagemParaInternalStorage(caminho_origem_img, caminho_destino_img, 0);
			// upload som
			fio.copiarArquivoDeSomOuImagemParaInternalStorage(caminho_origem_som, caminho_destino_som, 1);

		// *** grava dados no banco ***
//			AssocImagemSomDAOSingleton dao_ais = AssocImagemSomDAOSingleton.getInstance();
//			dao_ais.incluirAssocImagemSomWithRandomGeneratedID(ais);
			
			AssocImagemSomDAO dao_ais = new AssocImagemSomDAO(this);
			dao_ais.open();
			dao_ais.create(ais);
			dao_ais.close();
			
			//Toast.makeText(this, "Inclusão bem sucedida!", Toast.LENGTH_SHORT).show();
			
			Intent data = new Intent();
			data.putExtra("ais", ais);			
			this.setResult(RC_IMG_INCLUIDA_SUCESSO, data);
			finish();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}		
	}

	@Override
	protected void editar()
	{
		EditText txtDesc = (EditText) findViewById(R.id.txtDesc);
//		CheckBox chkIsCategoria	 = (CheckBox) findViewById(R.id.chkIsCategoria);
		
		FilesIO fio = new FilesIO(this);
		
		// populando objeto AssocImagemSom
		ais.setDesc(txtDesc.getText().toString());
		ais.setTituloImagem(fio.getNomeDoArquivoSemExtensao(caminho_destino_img));
		ais.setTituloSom(fio.getNomeDoArquivoSemExtensao(caminho_destino_som));
		ais.setExt(fio.getExtensaoDoArquivo(caminho_destino_img));
		//ais.setTipo((chkIsCategoria.isChecked()) ? 'c' : 'n');
//		ais.setTipo((optSubs.isChecked()) ? 'n' : optVerb.isChecked()? 'v' : 'c');		
		ais.setCmd(0);
		ais.setAtalho(false);		

		retirarErros();
		
		// *** validacao ***
		if (!(validarUpload() & validarDados()))
		{
			Utils.exibirErros(this);
			return;
		}
		
		try 
		{
		// *** upload *** obs: uploada caso o usuario troque os arquivos
			
			// se os arquivos forem diferentes da imagem antiga então uploada
			
			// upload imagem
			if ( ! ( ais.getTituloImagem().equals(old_ais.getTituloImagem()) && ais.getExt().equals(old_ais.getExt()) ) )
				fio.copiarArquivoDeSomOuImagemParaInternalStorage(caminho_origem_img, caminho_destino_img, 0);
			// upload som
			if ( ! ( ais.getTituloSom().equals(old_ais.getTituloSom()) ) )
				fio.copiarArquivoDeSomOuImagemParaInternalStorage(caminho_origem_som, caminho_destino_som, 1);

		// *** grava dados no banco ***
//			AssocImagemSomDAOSingleton dao_ais = AssocImagemSomDAOSingleton.getInstance();
//			dao_ais.editarAssocImagemSom(ais, ais.getId());
			AssocImagemSomDAO dao_ais = new AssocImagemSomDAO(this);
			dao_ais.open();
			dao_ais.update(ais, ais.getId());
			dao_ais.close();
			
			//Toast.makeText(this, "Inclusão bem sucedida!", Toast.LENGTH_SHORT).show();
			
			this.setResult(RC_IMG_EDITADA_SUCESSO);
			finish();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}				
	}

	private boolean validarDados()
	{
		boolean retorno = true;
		
		// descrição não pode ser vazia
		if (ais.getDesc().length() == 0)
		{
			Utils.erros.add(new Erro("Descrição vazia, campo obrigatório", findViewById(R.id.txtDesc), "EditText"));
			retorno = false;
		}
		
		return retorno;
	}
	
	private boolean validarUpload()
	{
		boolean retorno = true;
//		TextView lblImgSel 		 = (TextView) findViewById(R.id.lblImgSel);
//		TextView lblSomSel 		 = (TextView) findViewById(R.id.lblSomSel);
		
		FilesIO fio = new FilesIO(this);
		
		// imagem não selecionada
		if 	(
				(caminho_origem_img  == null || caminho_origem_img.length()  == 0) 
				&& 
				(caminho_destino_img == null || caminho_destino_img.length() == 0)
				
			)
		{
			Utils.erros.add(new Erro("Imagem não selecionada, por favor selecione"));
			retorno = false;
		}
		// extensao img errada
		else if (!(fio.verificarExtensaoImagem((caminho_origem_img == null) ? caminho_destino_img : caminho_origem_img)))
		{
			Utils.erros.add(new Erro("Arquivo não é uma imagem, por favor corrija"));
			retorno = false;			
		}
		
		// som não selecionado
		if 	(
				(caminho_origem_som  == null || caminho_origem_som.length()  == 0) 
				&& 
				(caminho_destino_som == null || caminho_destino_som.length() == 0)
			)
		{
			Utils.erros.add(new Erro("Som não selecionado, por favor selecione"));
			retorno = false;
		}
		// extensao som errada
		else if (!(fio.verificarExtensaoSom((caminho_origem_som == null) ? caminho_destino_som : caminho_origem_som)))
		{
			Utils.erros.add(new Erro("Arquivo não é um som, por favor corrija"));
			retorno = false;			
		}
		
		return retorno;
	}
	
	private void retirarErros()
	{
		Utils.limpaErros();
		((EditText) findViewById(R.id.txtDesc)).setError(null);
		((TextView) findViewById(R.id.textView3)).setError(null);
		((TextView) findViewById(R.id.lblSomSel)).setError(null);
	}
	
	
	@SuppressLint("ShowToast")
	private void openFile()
	{
    	try
    	{
    		Intent intent = new Intent("com.estrongs.action.PICK_FILE");
    		intent.putExtra("com.estrongs.intent.extra.TITLE", "Select File");
    		
    		startActivityForResult(intent, REQUEST_CODE_PICK_FILE_TO_OPEN);
	    } 
    	catch (ActivityNotFoundException e) 
    	{
			Toast.makeText(this, "no filemanager installed", 0).show();
		}
    }

	// método executado quando o som ou imagem for selecionado
	@SuppressLint("ShowToast")
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) 
	{
		super.onActivityResult(requestCode, resultCode, data);
		
		if (resultCode != RESULT_OK || data == null) 
			return;
		
		Uri uri = data.getData();
		FilesIO fio = new FilesIO(this);
		
		//String realPath = Utils.getRealPathFromURI(uri, this);
		String realPath = fio.getRealPathFromURI(uri);
		//String lastPathSegment = Utils.getLastPathSegment(realPath);
		String lastPathSegment = fio.getLastPathSegment(realPath);
		
		if ((requestCode == REQUEST_CODE_PICK_FILE_TO_OPEN) && uri != null)
		{
			// setar imagem na imagem button e outras configs
			if (current_file_code == REQUEST_CODE_PICK_IMG)
			{
				ImageButton btnUploadImg = (ImageButton) findViewById(R.id.btnUploadImg);
				
				btnUploadImg.setImageURI(uri);
				
				caminho_origem_img  = realPath;
				caminho_destino_img = lastPathSegment;
				
			}
			// setar caminho do som no lblSom e outras configs			
			else if (current_file_code == REQUEST_CODE_PICK_SOUND)
			{
				RelativeLayout laySom 	 = (RelativeLayout) findViewById(R.id.laySom);
				TextView lblSomSel 		 = (TextView) findViewById(R.id.lblSomSel);
				
				lblSomSel.setText(lastPathSegment);

				caminho_origem_som  = realPath;
				caminho_destino_som = lastPathSegment;
				
				laySom.setVisibility(View.VISIBLE);
				
				isSomChanged = true;
			}
			
			Toast.makeText(this, "Caminho selecionado: " + lastPathSegment, 0).show();
			
		}

	}


}
