package com.example.tabletvox03f.management;

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
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tabletvox03f.Erro;
import com.example.tabletvox03f.R;
import com.example.tabletvox03f.Utils;
import com.example.tabletvox03f.dal.AssocImagemSom;
import com.example.tabletvox03f.dal.AssocImagemSomDAOSingleton;
import com.example.tabletvox03f.dal.FilesIO;

public class FormularioAssocImagemSomActivity extends FormularioBaseActivity
{
	protected static final int REQUEST_CODE_PICK_FILE_TO_OPEN = 1;
	protected static final int REQUEST_CODE_PICK_DIRECTORY = 2;
	
	private final int REQUEST_CODE_PICK_IMG   = 1;
	private final int REQUEST_CODE_PICK_SOUND = 2;
	
	// serve para saber se está requerindo um arquivo de imagem ou som
	private int current_file_code;
	
	private String caminho_origem_img;
	private String caminho_origem_som;
	
	private String caminho_destino_img;
	private String caminho_destino_som;
	
	private AssocImagemSom new_ais;
	
	

	@Override
	protected int[] getDadosForm()
	{
		int[] dados = {R.layout.formulario_ais_interface, R.menu.um_action_salvar, 
				R.string.title_activity_incluir_ais, R.string.title_activity_incluir_ais};
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
				// TODO Auto-generated method stub
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
				// TODO Auto-generated method stub
				current_file_code = REQUEST_CODE_PICK_SOUND;
				openFile();
			}
		});		
	}
	
	@Override
	protected void initCriarForm()
	{
		// TODO Auto-generated method stub
		new_ais = new AssocImagemSom();
		caminho_origem_img = caminho_origem_som = caminho_destino_img = caminho_destino_som = "";
	}

	@Override
	protected void initEditarForm(Intent intent)
	{
		// TODO Auto-generated method stub

	}

	@Override
	protected void incluir()
	{
		EditText txtDesc = (EditText) findViewById(R.id.txtDesc);
		RadioButton optSubs = (RadioButton) findViewById(R.id.optSubs);
		RadioButton optVerb = (RadioButton) findViewById(R.id.optVerb);

		FilesIO fio = new FilesIO(this);
		
		// populando objeto AssocImagemSom
		new_ais.setDesc(txtDesc.getText().toString());
		new_ais.setTituloImagem(fio.getNomeDoArquivoSemExtensao(caminho_destino_img));
		new_ais.setTituloSom(fio.getNomeDoArquivoSemExtensao(caminho_destino_som));
		new_ais.setExt(fio.getExtensaoDoArquivo(caminho_destino_img));
		new_ais.setTipo((optSubs.isChecked()) ? 'n' : optVerb.isChecked()? 'v' : 'c');		
		new_ais.setCmd(0);
		new_ais.setAtalho(false);
		
		
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
			AssocImagemSomDAOSingleton dao_ais = AssocImagemSomDAOSingleton.getInstance();
			dao_ais.incluirAssocImagemSomWithRandomGeneratedID(new_ais);
//			AssocImagemSomDAO dao_ais = new AssocImagemSomDAO(this);
//			dao_ais.open();
//			dao_ais.create(new_ais);
//			dao_ais.close();
			
			Toast.makeText(this, "Inclusão bem sucedida!", Toast.LENGTH_SHORT).show();
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}

	@Override
	protected void editar()
	{
		// TODO Auto-generated method stub

	}

	private boolean validarDados()
	{
		boolean retorno = true;
		
		// descrição não pode ser vazia
		if (new_ais.getDesc().length() == 0)
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
		
		FilesIO fio = new FilesIO();
		
		// imagem não selecionada
		if ((caminho_origem_img.length() == 0) && (caminho_destino_img.length() == 0))
		{
			Utils.erros.add(new Erro("Imagem não selecionada, por favor selecione"));
			retorno = false;
		}
		// extensao img errada
		else if (!(fio.verificarExtensaoImagem(caminho_origem_img)))
		{
			Utils.erros.add(new Erro("Arquivo não é uma imagem, por favor corrija"));
			retorno = false;			
		}
		
		// som não selecionado
		if ((caminho_origem_som.length() == 0) && (caminho_destino_som.length() == 0))
		{
			Utils.erros.add(new Erro("Som não selecionado, por favor selecione"));
			retorno = false;
		}
		// extensao som errada
		else if (!(fio.verificarExtensaoSom(caminho_origem_som)))
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
				TextView lblTituloSomSel = (TextView) findViewById(R.id.textView3);
				TextView lblSomSel 		 = (TextView) findViewById(R.id.lblSomSel);
				lblSomSel.setText(lastPathSegment);

				caminho_origem_som  = realPath;
				caminho_destino_som = lastPathSegment;
				
				lblTituloSomSel.setVisibility(View.VISIBLE);
				lblSomSel.setVisibility(View.VISIBLE);				
			}
			
			Toast.makeText(this, "Caminho selecionado: " + lastPathSegment, 0).show();			
		}

	}


}
