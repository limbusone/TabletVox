package com.example.tabletvox03f.management;

import java.io.IOException;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tabletvox03f.Erro;
import com.example.tabletvox03f.R;
import com.example.tabletvox03f.Utils;
import com.example.tabletvox03f.dal.AssocImagemSom;
import com.example.tabletvox03f.dal.AssocImagemSomDAO;
import com.example.tabletvox03f.dal.FilesIO;

public class IncluirAISActivity extends Activity
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
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_incluir_ais);
		
		Button btnProximo 	= (Button) findViewById(R.id.btnProximo);
		Button btnAnterior 	= (Button) findViewById(R.id.btnAnterior);
		Button btnUploadImg = (Button) findViewById(R.id.btnUploadImg);
		Button btnUploadSom = (Button) findViewById(R.id.btnUploadSom);
		Button btnConcluido = (Button) findViewById(R.id.btnConcluido);
		
		new_ais = new AssocImagemSom();
		
		// vai para a proxima secao (upload)
		btnProximo.setOnClickListener(new OnClickListener()
		{
			
			@Override
			public void onClick(View v)
			{
				// TODO Auto-generated method stub
				
				EditText txtDesc = (EditText) IncluirAISActivity.this.findViewById(R.id.txtDesc);
				RadioButton optSubs = (RadioButton) IncluirAISActivity.this.findViewById(R.id.optSubs);
				RadioButton optVerb = (RadioButton) IncluirAISActivity.this.findViewById(R.id.optVerb);
				CheckBox chkComando = (CheckBox) IncluirAISActivity.this.findViewById(R.id.chkComando);
				
				// populando objeto AssocImagemSom
				//Editable e = txtDesc.getText();
				//ais.setDesc( (e.length() == 0) ? "" : e.toString() );
				new_ais.setDesc(txtDesc.getText().toString());
				new_ais.setTipo((optSubs.isChecked()) ? 'n' : optVerb.isChecked()? 'v' : 'c');
				new_ais.setCmd(chkComando.isChecked() ? 1 : 0);
				
				if (!(validarDados()))
				{
					exibirErros();
					return;
				}
				
				retirarErros();
				
				// mudar de secao
				IncluirAISActivity.this.findViewById(R.id.secao_dados).setVisibility(View.GONE);
				IncluirAISActivity.this.findViewById(R.id.secao_upload).setVisibility(View.VISIBLE);

				TextView t = (TextView) IncluirAISActivity.this.findViewById(R.id.lblDados);
				t.setTypeface(Typeface.DEFAULT);
				t.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
				
				t = (TextView) IncluirAISActivity.this.findViewById(R.id.lblUpload);
				t.setTypeface(Typeface.DEFAULT_BOLD);
				t.setTextSize(TypedValue.COMPLEX_UNIT_SP, 24);
				
			}
		});
		
		// volta para a secao inicial (dados)
		btnAnterior.setOnClickListener(new OnClickListener()
		{
			
			@Override
			public void onClick(View v)
			{
				// TODO Auto-generated method stub

				// mudar de secao
				IncluirAISActivity.this.findViewById(R.id.secao_dados).setVisibility(View.VISIBLE);
				IncluirAISActivity.this.findViewById(R.id.secao_upload).setVisibility(View.GONE);
				
				TextView t = (TextView) IncluirAISActivity.this.findViewById(R.id.lblDados);
				t.setTypeface(Typeface.DEFAULT_BOLD);
				t.setTextSize(TypedValue.COMPLEX_UNIT_SP, 24);
				
				t = (TextView) IncluirAISActivity.this.findViewById(R.id.lblUpload);
				t.setTypeface(Typeface.DEFAULT);
				t.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);				
			}
		});
		
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
		
		// finalizar inclusão
		// valida a secao de upload, uploada os arquivos e inclui no banco todos os dados
		btnConcluido.setOnClickListener(new OnClickListener()
		{
			
			@Override
			public void onClick(View arg0)
			{
				// TODO Auto-generated method stub
//				TextView lblImgSel 		 = (TextView) findViewById(R.id.lblImgSel);
//				TextView lblSomSel 		 = (TextView) findViewById(R.id.lblSomSel);
				
				// *** valida ***
				if (!(validarUpload()))
				{
					exibirErros();
					return;
				}
				
				FilesIO fio = new FilesIO(IncluirAISActivity.this);
				
				retirarErros();
				
				//new_ais.setTituloImagem(Utils.getNomeDoArquivoSemExtensao(caminho_destino_img));
				new_ais.setTituloImagem(fio.getNomeDoArquivoSemExtensao(caminho_destino_img));
				//new_ais.setTituloSom(Utils.getNomeDoArquivoSemExtensao(caminho_destino_som));
				new_ais.setTituloSom(fio.getNomeDoArquivoSemExtensao(caminho_destino_som));
				//new_ais.setExt(Utils.getExtensaoDoArquivo(caminho_origem_img));
				new_ais.setExt(fio.getExtensaoDoArquivo(caminho_origem_img));
				
				// *** upload ***
				try
				{
					
					
					// upload imagem
					fio.copiarArquivoDeSomOuImagemParaInternalStorage(caminho_origem_img, caminho_destino_img, 0);
					//Utils.copiarArquivoDeSomOuImagemParaInternalStorage(IncluirAISActivity.this, caminho_origem_img, caminho_destino_img, 0);
					// upload som
					fio.copiarArquivoDeSomOuImagemParaInternalStorage(caminho_origem_som, caminho_destino_som, 1);
					//Utils.copiarArquivoDeSomOuImagemParaInternalStorage(IncluirAISActivity.this, caminho_origem_som, caminho_destino_som, 1);

				// *** grava dados no banco ***
					AssocImagemSomDAO dao_ais = new AssocImagemSomDAO(IncluirAISActivity.this);
					dao_ais.open();
					dao_ais.create(new_ais);
					dao_ais.close();
					
					Toast.makeText(IncluirAISActivity.this, "Inclusão bem sucedida!", Toast.LENGTH_SHORT).show();
				} catch (IOException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
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
			Utils.erros.add(new Erro("Imagem não selecionada, por favor selecione", findViewById(R.id.textView2), "TextView"));
			retorno = false;
		}
		
		// som não selecionado
		if ((caminho_origem_som.length() == 0) && (caminho_destino_som.length() == 0))
		{
			Utils.erros.add(new Erro("Som não selecionado, por favor selecione", findViewById(R.id.textView3), "TextView"));
			retorno = false;
		}
		
		// extensao img errada
		//if (!(Utils.verificarExtensaoImagem(caminho_origem_img)))
		if (!(fio.verificarExtensaoImagem(caminho_origem_img)))
		{
			Utils.erros.add(new Erro("Arquivo não é uma imagem, por favor corrija", findViewById(R.id.lblImgSel), "TextView"));
			retorno = false;			
		}
		
		// extensao som errada
		//if (!(Utils.verificarExtensaoSom(caminho_origem_som)))
		if (!(fio.verificarExtensaoSom(caminho_origem_som)))
		{
			Utils.erros.add(new Erro("Arquivo não é um som, por favor corrija", findViewById(R.id.lblSomSel), "TextView"));
			retorno = false;			
		}		
		
		return retorno;
	}
	
	private void exibirErros()
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
			
		}
		Toast.makeText(this, "Ops. Há algo errado!", Toast.LENGTH_SHORT).show();
	}
	
	private void retirarErros()
	{
		Utils.limpaErros();
		((EditText) findViewById(R.id.txtDesc)).setError(null);
		((TextView) findViewById(R.id.textView2)).setError(null);
		((TextView) findViewById(R.id.textView3)).setError(null);
		((TextView) findViewById(R.id.lblImgSel)).setError(null);
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
	
	@SuppressLint("ShowToast")
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) 
	{
		super.onActivityResult(requestCode, resultCode, data);
		
		if (resultCode != RESULT_OK || data == null) 
		{
			return ;
		}
		
		Uri uri = data.getData();
		FilesIO fio = new FilesIO(this);
		
		//String realPath = Utils.getRealPathFromURI(uri, this);
		String realPath = fio.getRealPathFromURI(uri);
		//String lastPathSegment = Utils.getLastPathSegment(realPath);
		String lastPathSegment = fio.getLastPathSegment(realPath);
		
//		
//		try
//		{
//			Utils.copiarArquivoDeSomOuImagemParaInternalStorage(this, realPath, lastPathSegment, 0);
//		} 
//		catch (IOException e)
//		{
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
		if ((requestCode == REQUEST_CODE_PICK_FILE_TO_OPEN) && uri != null)
		{
			if (current_file_code == REQUEST_CODE_PICK_IMG)
			{
				TextView lblTituloImgSel = (TextView) findViewById(R.id.textView2);
				TextView lblImgSel 		 = (TextView) findViewById(R.id.lblImgSel);
				lblImgSel.setText(lastPathSegment);

				caminho_origem_img  = realPath;
				caminho_destino_img = lastPathSegment;
				
				lblTituloImgSel.setVisibility(View.VISIBLE);
				lblImgSel.setVisibility(View.VISIBLE);
			} 
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
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.incluir_ai, menu);
		return true;
	}
	
}
