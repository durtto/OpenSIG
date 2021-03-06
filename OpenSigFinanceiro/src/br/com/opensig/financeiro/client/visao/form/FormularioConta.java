package br.com.opensig.financeiro.client.visao.form;

import br.com.opensig.core.client.OpenSigCore;
import br.com.opensig.core.client.servico.CoreProxy;
import br.com.opensig.core.client.visao.abstrato.AFormulario;
import br.com.opensig.core.shared.modelo.sistema.SisFuncao;
import br.com.opensig.financeiro.shared.modelo.FinBanco;
import br.com.opensig.financeiro.shared.modelo.FinConta;

import com.gwtext.client.data.ArrayReader;
import com.gwtext.client.data.FieldDef;
import com.gwtext.client.data.IntegerFieldDef;
import com.gwtext.client.data.Record;
import com.gwtext.client.data.RecordDef;
import com.gwtext.client.data.Store;
import com.gwtext.client.data.StringFieldDef;
import com.gwtext.client.data.event.StoreListenerAdapter;
import com.gwtext.client.widgets.MessageBox;
import com.gwtext.client.widgets.MessageBox.ConfirmCallback;
import com.gwtext.client.widgets.form.ComboBox;
import com.gwtext.client.widgets.form.Hidden;
import com.gwtext.client.widgets.form.MultiFieldPanel;
import com.gwtext.client.widgets.form.NumberField;
import com.gwtext.client.widgets.form.TextField;

public class FormularioConta extends AFormulario<FinConta> {

	private Hidden hdnCod;
	private TextField txtNome;
	private TextField txtNumero;
	private TextField txtAgencia;
	private TextField txtCarteira;
	private TextField txtConvenio;
	private NumberField txtSaldo;
	private ComboBox cmbBanco;

	public FormularioConta(SisFuncao funcao) {
		super(new FinConta(), funcao);
		inicializar();
	}

	public void inicializar() {
		super.inicializar();

		hdnCod = new Hidden("finContaId", "0");
		add(hdnCod);

		txtNome = new TextField(OpenSigCore.i18n.txtNome(), "finContaNome", 130);
		txtNome.setAllowBlank(false);
		txtNome.setMaxLength(20);

		txtNumero = new TextField(OpenSigCore.i18n.txtNumero(), "finContaNumero", 60);
		txtNumero.setMaxLength(11);

		txtAgencia = new TextField(OpenSigCore.i18n.txtAgencia(), "finContaAgencia", 60);
		txtAgencia.setMaxLength(11);

		txtCarteira = new TextField(OpenSigCore.i18n.txtCarteira(), "finContaCarteira", 60);
		txtCarteira.setMaxLength(11);

		txtConvenio = new TextField(OpenSigCore.i18n.txtConvenio(), "finContaConvenio", 60);
		txtConvenio.setMaxLength(11);

		txtSaldo = new NumberField(OpenSigCore.i18n.txtSaldo(), "finContaSaldo", 80);
		txtSaldo.setAllowBlank(false);
		txtSaldo.setMaxLength(11);
		txtSaldo.setDecimalPrecision(2);
		
		MultiFieldPanel linha1 = new MultiFieldPanel();
		linha1.setBorder(false);
		linha1.addToRow(txtNome, 150);
		linha1.addToRow(txtNumero, 80);
		linha1.addToRow(txtAgencia, 70);
		linha1.addToRow(txtCarteira, 70);
		linha1.addToRow(txtConvenio, 70);
		linha1.addToRow(txtSaldo, 100);
		linha1.addToRow(getBanco(), 250);
		add(linha1);
	}

	public boolean setDados() {
		classe.setFinContaId(Integer.valueOf(hdnCod.getValueAsString()));
		classe.setFinContaNome(txtNome.getValueAsString());
		classe.setFinContaNumero(txtNumero.getValueAsString() == null ? "" : txtNumero.getValueAsString());
		classe.setFinContaAgencia(txtAgencia.getValueAsString() == null ? "" : txtAgencia.getValueAsString());
		classe.setFinContaCarteira(txtCarteira.getValueAsString() == null ? "" : txtCarteira.getValueAsString());
		classe.setFinContaConvenio(txtConvenio.getValueAsString() == null ? "" : txtConvenio.getValueAsString());

		if (cmbBanco.getValue() != null) {
			FinBanco banco = new FinBanco(Integer.valueOf(cmbBanco.getValue()));
			classe.setFinBanco(banco);
		}
		if (txtSaldo.getValue() != null) {
			classe.setFinContaSaldo(txtSaldo.getValue().doubleValue());
		}
		return true;
	}

	public void limparDados() {
		getForm().reset();
	}

	public void mostrarDados() {
		if (cmbBanco.getStore().getRecords().length == 0) {
			cmbBanco.getStore().load();
		} else {
			mostrar();
		}
	}

	private void mostrar() {
		MessageBox.hide();
		Record rec = lista.getPanel().getSelectionModel().getSelected();
		if (rec != null) {
			getForm().loadRecord(rec);
		}
		txtNome.focus(true);

		if (duplicar) {
			hdnCod.setValue("0");
			duplicar = false;
		}
	}

	public void gerarListas() {
	}

	private ComboBox getBanco() {
		FieldDef[] fdBanco = new FieldDef[] { new IntegerFieldDef("finBancoId"), new StringFieldDef("finBancoNumero"), new StringFieldDef("finBancoDescricao") };
		CoreProxy<FinBanco> proxy = new CoreProxy<FinBanco>(new FinBanco());
		final Store storeBanco = new Store(proxy, new ArrayReader(new RecordDef(fdBanco)), false);
		storeBanco.addStoreListener(new StoreListenerAdapter() {
			public void onLoad(Store store, Record[] records) {
				mostrar();
			}

			public void onLoadException(Throwable error) {
				MessageBox.confirm(OpenSigCore.i18n.txtBanco(), OpenSigCore.i18n.msgRecarregar(), new ConfirmCallback() {
					public void execute(String btnID) {
						if (btnID.equalsIgnoreCase("yes")) {
							storeBanco.load();
						}
					}
				});
			}
		});

		cmbBanco = new ComboBox(OpenSigCore.i18n.txtBanco(), "finBanco.finBancoId", 230);
		cmbBanco.setAllowBlank(false);
		cmbBanco.setEditable(false);
		cmbBanco.setStore(storeBanco);
		cmbBanco.setTriggerAction(ComboBox.ALL);
		cmbBanco.setMode(ComboBox.LOCAL);
		cmbBanco.setDisplayField("finBancoDescricao");
		cmbBanco.setValueField("finBancoId");
		cmbBanco.setForceSelection(true);
		cmbBanco.setListWidth(230);

		return cmbBanco;
	}

	public Hidden getHdnCod() {
		return hdnCod;
	}

	public void setHdnCod(Hidden hdnCod) {
		this.hdnCod = hdnCod;
	}

	public TextField getTxtNome() {
		return txtNome;
	}

	public void setTxtNome(TextField txtNome) {
		this.txtNome = txtNome;
	}

	public TextField getTxtNumero() {
		return txtNumero;
	}

	public void setTxtNumero(TextField txtNumero) {
		this.txtNumero = txtNumero;
	}

	public TextField getTxtAgencia() {
		return txtAgencia;
	}

	public void setTxtAgencia(TextField txtAgencia) {
		this.txtAgencia = txtAgencia;
	}

	public TextField getTxtCarteira() {
		return txtCarteira;
	}

	public void setTxtCarteira(TextField txtCarteira) {
		this.txtCarteira = txtCarteira;
	}

	public NumberField getTxtSaldo() {
		return txtSaldo;
	}

	public void setTxtSaldo(NumberField txtSaldo) {
		this.txtSaldo = txtSaldo;
	}

	public ComboBox getCmbBanco() {
		return cmbBanco;
	}

	public void setCmbBanco(ComboBox cmbBanco) {
		this.cmbBanco = cmbBanco;
	}

	public TextField getTxtConvenio() {
		return txtConvenio;
	}

	public void setTxtConvenio(TextField txtConvenio) {
		this.txtConvenio = txtConvenio;
	}

}
